package eu.michaelvogt.ar.author

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withParent
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


object EspressoMatchers {
    fun withTitle(expectedTitle: String): Matcher<View> {
        return object : BoundedMatcher<View, Toolbar>(Toolbar::class.java) {
            override
            fun describeTo(description: Description) {
                description.appendText("Checking the matcher on provided View: ")
                description.appendText("with expected title $expectedTitle")
            }

            override
            fun matchesSafely(item: Toolbar): Boolean {
                return item.title == expectedTitle
            }
        }
    }

    fun hasAction(): Matcher<View> {
        return object : BoundedMatcher<View, Toolbar>(Toolbar::class.java) {
            override
            fun describeTo(description: Description) {
                description.appendText("Checking the matcher on provided View: ")
                description.appendText("with actions")
            }

            override
            fun matchesSafely(item: Toolbar): Boolean {
                return item.menu.hasVisibleItems()
            }
        }
    }

    fun withAction(expectedAction: Int): Matcher<View> {
        return object : BoundedMatcher<View, Toolbar>(Toolbar::class.java) {
            override
            fun describeTo(description: Description) {
                description.appendText("Checking the matcher on provided View: ")
                description.appendText("with expected action $expectedAction")
            }

            override
            fun matchesSafely(item: Toolbar): Boolean {
                return item.menu.findItem(expectedAction) != null
            }
        }
    }

    fun hasNavigationIcon(): Matcher<View> {
        return object : BoundedMatcher<View, Toolbar>(Toolbar::class.java) {
            override
            fun describeTo(description: Description) {
                description.appendText("Checking the matcher on provided View: ")
                description.appendText("with navigation icon")
            }

            override
            fun matchesSafely(item: Toolbar): Boolean {
                return item.navigationIcon != null
            }

        }
    }

    fun hamburgerMenu(): Matcher<View> {
        return allOf(
                isAssignableFrom(ImageButton::class.java),
                withParent(isAssignableFrom(Toolbar::class.java))
        )
    }

    fun hasItems(): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override
            fun describeTo(description: Description) {
                description.appendText("Checking if items in provided View: ")
            }

            override
            fun matchesSafely(item: RecyclerView): Boolean {
                return item.adapter?.itemCount != 0
            }
        }
    }

    fun hasItems(expected: Int): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override
            fun describeTo(description: Description) {
                description.appendText("Checking the number of items on provided View: ")
                description.appendText("expected: $expected ")
            }

            override
            fun matchesSafely(item: RecyclerView): Boolean {
                val actual = item.adapter?.itemCount
                return actual == expected
            }
        }
    }

    fun nthChildOf(parentMatcher: Matcher<View>, index: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override
            fun describeTo(description: Description) {
                description.appendText("position $index of parent ")
                parentMatcher.describeTo(description)
            }

            override
            fun matchesSafely(item: View): Boolean {
                if (item.parent !is ViewGroup) return false
                val parent = item.parent as ViewGroup

                return parentMatcher.matches(parent)
                        && parent.childCount > index
                        && parent.getChildAt(index) == item
            }
        }
    }
}
