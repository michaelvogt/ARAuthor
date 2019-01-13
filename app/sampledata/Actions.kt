package eu.michaelvogt.ar.author

import android.view.View
import android.widget.ImageButton
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.LayoutMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matcher
import org.hamcrest.Matchers

@DslMarker
annotation class ActionsMarker

/**
 * Marker Interface to enforce correct method selection in IDE tab completion and context compile
 * time scope enforcement
 */
@ActionsMarker
interface Actions

/**
 * Marker class to indicate that this leaf does not contain any functionality
 */
object NoActions : Actions

abstract class ScopedActions(protected val matcher: () -> Matcher<View>) : Actions {
    fun isDisplayed(): Actions {
        onView().check(matches(ViewMatchers.isDisplayed()))
        return NoActions
    }

    fun isNotDisplayed(): Actions {
        onView().check(matches(not(ViewMatchers.isDisplayed())))
        return NoActions
    }

    fun hasTextInside(text: CharSequence, textMatcher: (CharSequence) -> Matcher<String> = { `is`<String>(it.toString()) }): Actions {
        Espresso.onView(Matchers.allOf(
                withText(textMatcher(text)),
                anyOf(isDescendantOfA(matcher()), matcher())
        )).check(matches(ViewMatchers.isDisplayed()))
        return NoActions
    }

    fun containsTextInside(text: CharSequence): Actions {
        return hasTextInside(text) { CoreMatchers.containsString(it.toString()) }
    }

    fun hasNotTextInside(text: CharSequence, textMatcher: (CharSequence) -> Matcher<String> = { `is`<String>(it.toString()) }): Actions {
        Espresso.onView(allOf(
                withText(textMatcher(text)),
                anyOf(isDescendantOfA(matcher()), matcher())
        )).check(doesNotExist())
        return NoActions
    }

    protected fun onView(): ViewInteraction = Espresso.onView(matcher())
}

/**
 * Base Actions and Assertions for any view
 */
open class UiActions(matcher: () -> Matcher<View>) : ScopedActions(matcher) {
    fun click(): Actions {
        onView().perform(ViewActions.click())
        return NoActions
    }

    fun isDisabled(): Actions {
        onView().check(matches(not(ViewMatchers.isEnabled())))
        return NoActions
    }

    fun isEnabled(): Actions {
        onView().check(matches(ViewMatchers.isEnabled()))
        return NoActions
    }

    // Fuzzy matches for any view with that text and with an arbitrary parent that matches the scope
    fun hasText(text: CharSequence, textMatcher: (CharSequence) -> Matcher<String> = { `is`<String>(it.toString()) }): Actions {
        Espresso.onView(Matchers.allOf(
                withText(textMatcher(text)),
                anyOf(withParent(matcher()), matcher())
        )).check(matches(ViewMatchers.isDisplayed()))
        return NoActions
    }

    fun hasEllipsizedText(): Actions {
        Espresso.onView(Matchers.allOf(
                LayoutMatchers.hasEllipsizedText(),
                anyOf(withParent(matcher()), matcher())
        )).check(matches(ViewMatchers.isDisplayed()))
        return NoActions
    }

    fun hasNoEllipsizedText(): Actions {
        Espresso.onView(Matchers.allOf(
                LayoutMatchers.hasEllipsizedText(),
                anyOf(withParent(matcher()), matcher())
        )).check(doesNotExist())
        return NoActions
    }

    fun hasImageTag(@DrawableRes resId: Int): Actions {
        onView().check(matches(withTagValue(equalTo(resId.toString()))))
        return NoActions
    }

    fun closeSoftKeyboard(): Actions {
        Espresso.closeSoftKeyboard()
        return NoActions
    }

    fun pressBack(): Actions {
        Espresso.pressBack()
        return NoActions
    }
}

open class EditTextActions(matcher: () -> Matcher<View>) : UiActions(matcher) {
    fun typeText(text: CharSequence): Actions {
        onView().perform(ViewActions.click()).perform(ViewActions.typeText(text.toString()))
        return NoActions
    }

    fun clearText(): Actions {
        onView().perform(ViewActions.replaceText(""))
        return NoActions
    }

    fun pressSearch(): Actions {
        onView().perform(ViewActions.pressImeActionButton())
        return NoActions
    }
}

open class TextInputLayoutActions(private val layoutId: Int) : UiActions(idMatcher(layoutId)) {
    fun inEdit(action: EditTextActions.() -> Actions): Actions =
            EditTextActions { editMatcher(layoutId) }.action()
}

open class ListTextInputLayoutActions(itemMatcher: () -> Matcher<View>, private val inputLayoutId: Int) : UiActions(idMatcher(inputLayoutId, itemMatcher)) {
    fun inEdit(action: EditTextActions.() -> Actions): Actions =
            EditTextActions { editMatcher(inputLayoutId) }.action()
}

open class BarActions(matcher: () -> Matcher<View>) : UiActions(matcher) {
    fun hasAction(): Actions {
        onView().check(matches(EspressoMatchers.hasAction()))
        return NoActions
    }

    fun hasNoAction(): Actions {
        onView().check(matches(not(EspressoMatchers.hasAction())))
        return NoActions
    }
}

open class TabBarActions(matcher: () -> Matcher<View>) : UiActions(matcher) {
    fun swipeLeft(): Actions {
        onView().perform(ViewActions.swipeLeft())
        return NoActions
    }

    fun swipeRight(): Actions {
        onView().perform(ViewActions.swipeRight())
        return NoActions
    }
}

open class BottomBarActions(matcher: () -> Matcher<View>) : BarActions(matcher) {
    fun hasNavigation(): Actions {
        onView().check(matches((EspressoMatchers.hasNavigationIcon())))
        return NoActions
    }
}

open class ToolbarActions(matcher: () -> Matcher<View>) : BarActions(matcher) {
    fun hasLocationInfo(): Actions {
        onView().check(matches(EspressoMatchers.withAction(R.id.toolbar_location_info)))
        return NoActions
    }

    fun hasMarkerInfo(): Actions {
        onView().check(matches(EspressoMatchers.withAction(R.id.toolbar_marker_info)))
        return NoActions
    }

    fun hasNavigationIcon(): Actions {
        onView().check(matches(EspressoMatchers.hasNavigationIcon()))
        return NoActions
    }

    fun hasNoNavigationIcon(): Actions {
        onView().check(matches(not(EspressoMatchers.hasNavigationIcon())))
        return NoActions
    }
}

open class AreaEditCardActions(val cardMatcher: () -> Matcher<View>) : UiActions(cardMatcher) {
    fun inTitle(action: UiActions.() -> Actions) =
            UiActions(idMatcher(R.id.area_card_title)).action()

    fun inValue(action: UiActions.() -> Actions) =
            UiActions(idMatcher(R.id.area_card_values)).action()

    fun inEditX(action: ListTextInputLayoutActions.() -> Actions) =
            ListTextInputLayoutActions(cardMatcher, R.id.area_card_x).action()

    fun inEditY(action: TextInputLayoutActions.() -> Actions) =
            TextInputLayoutActions(R.id.area_card_y).action()

    fun inEditZ(action: TextInputLayoutActions.() -> Actions) =
            TextInputLayoutActions(R.id.area_card_z).action()

    fun inEditW(action: TextInputLayoutActions.() -> Actions) =
            TextInputLayoutActions(R.id.area_card_w).action()
}

open class ScrollViewActions(matcher: () -> Matcher<View>) : ScopedActions(matcher) {
    fun scrollTo(): Actions {
        onView().perform(NestedScrollToAction())
        return this
    }
}

open class ListItemActions(val itemMatcher: () -> Matcher<View>) : UiActions(itemMatcher) {
    fun inItemButton(action: UiActions.() -> Actions) =
            UiActions { allOf(isDescendantOfA(itemMatcher()), isA(ImageButton::class.java)) as Matcher<View> }.action()
}

open class List(private val recyclerId: Int) : ScrollViewActions(idMatcher(recyclerId)) {
    fun inItem(childPosition: Int, action: ListItemActions.() -> Actions): Actions {
        Espresso.closeSoftKeyboard()
        onView().perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(childPosition))
        return ListItemActions { RecyclerViewMatcher(recyclerId).atPosition(childPosition) }.run(action)
    }

    fun hasItems(): Actions {
        onView().check(matches(EspressoMatchers.hasItems()))
        return NoActions
    }

    fun hasItems(itemCount: Int): Actions {
        onView().check(matches(EspressoMatchers.hasItems(itemCount)))
        return NoActions
    }
}