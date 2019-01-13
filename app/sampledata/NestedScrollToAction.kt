package eu.michaelvogt.ar.author

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ScrollToAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf

/**
 * Improved [ScrollToAction] which works with [NestedScrollView]
 */
class NestedScrollToAction : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return allOf(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE), isDescendantOfA(anyOf(
                isAssignableFrom(ScrollView::class.java), isAssignableFrom(HorizontalScrollView::class.java),
                isAssignableFrom(NestedScrollView::class.java))))
    }

    override fun perform(uiController: UiController, view: View) {
        if (isDisplayingAtLeast(90).matches(view)) {
            return
        }
        val rect = Rect()
        view.getDrawingRect(rect)
        if (!/* immediate */view.requestRectangleOnScreen(rect, true)) {
            Log.w("nestedScrollAction", "Scrolling to view was requested, but none of the parents scrolled.")
        }

        uiController.loopMainThreadUntilIdle()
        if (!isDisplayingAtLeast(90).matches(view)) {
            throw PerformException.Builder()
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(RuntimeException(
                            "Scrolling to view was attempted, but the view is not displayed"))
                    .build()
        }
    }

    override fun getDescription(): String {
        return "scroll to"
    }

    companion object {
        fun betterScrollTo(): ViewAction {
            return ViewActions.actionWithAssertions(NestedScrollToAction())
        }
    }
}