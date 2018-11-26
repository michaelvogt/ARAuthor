package eu.michaelvogt.ar.author

import android.view.View
import android.widget.ImageButton
import androidx.annotation.IdRes
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.android.material.textfield.TextInputEditText
import eu.michaelvogt.ar.author.EspressoMatchers.nthChildOf
import eu.michaelvogt.ar.author.fragments.*
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher

/**
 * Simple DSL for UI tests, inspired by
 * https://proandroiddev.com/kotlin-using-test-robots-to-make-espresso-8cec2d746973
 */
class TestCase {
    fun inAppFrame(action: AppFrameScreen.() -> Actions) = AppFrameScreen().action()

    fun inLocationList(action: LocationListScreen.() -> Actions) = LocationListScreen().action()
    fun inLocationEdit(action: LocationEditScreen.() -> Actions) = LocationEditScreen().action()

    fun inMarkerList(action: MarkerListScreen.() -> Actions) = MarkerListScreen().action()
    fun inMarkerEdit(action: MarkerEditScreen.() -> Actions) = MarkerEditScreen().action()
}

fun testcase(action: TestCase.() -> Actions) {
    TestCase().action()
}

internal fun idMatcher(@IdRes resId: Int) = { ViewMatchers.withId(resId) }
internal fun indexMatcher(@IdRes resId: Int, index: Int) = { nthChildOf(withId(resId), index) }
internal fun textMatcher(text: String) = { ViewMatchers.withText(text) }
internal fun editMatcher(@IdRes inputLayoutId: Int) =
        CoreMatchers.allOf(
                CoreMatchers.isA(TextInputEditText::class.java),
                ViewMatchers.isDescendantOfA(withId(inputLayoutId))) as Matcher<View>

internal fun siblingMatcher(@IdRes resId: Int, title: String) = {
    CoreMatchers.allOf(
            CoreMatchers.isA(ImageButton::class.java),
            ViewMatchers.hasSibling(ViewMatchers.withSubstring(title)))
}