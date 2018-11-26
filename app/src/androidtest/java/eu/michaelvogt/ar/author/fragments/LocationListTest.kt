/*
    ARTester - AR for tourists by tourists
    Copyright (C) 2018  Michael Vogt

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package eu.michaelvogt.ar.author.fragments

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import eu.michaelvogt.ar.author.AuthorActivity
import eu.michaelvogt.ar.author.testcase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationListTest {
    @get:Rule
    val activityRule = ActivityTestRule(AuthorActivity::class.java)

    @Test
    fun toolbarTest() {
        testcase {
            inLocationList {
                inToolbar {
                    isDisplayed()
                    hasNoNavigationIcon()
                    hasText(LocationListScreen.title)
                    hasAction()
                    hasLocationInfo()
                }
            }
        }
    }

    @Test
    fun infoButtonTest() {
        testcase {
            inLocationList {
                inLocationInfo { click() }
                inInfoPrompt { isDisplayed() }
            }
        }
    }

    @Test
    fun bottomBarTest() {
        testcase {
            inAppFrame {
                inBottomBar {
                    isDisplayed()
                    hasNoAction()
                    hasNavigation()
                }

                inFabItem { isDisplayed() }
            }
        }
    }

    @Test
    fun deleteTest() {
        testcase {
            inLocationList {
                inList {
                    hasItems(LocationListScreen.itemCount)
                    inItem(2) {
                        inItemButton {
                            click()
                        }
                    }
                }

                inEditMenu { click() }
            }

            inLocationEdit {
                inToolbar {
                    isDisplayed()
                    hasText(LocationEditScreen.title)
                }

                inDelete { click() }
            }

            inLocationList {
                inList {
                    hasItems(LocationListScreen.itemCount - 1)
                }
            }
        }
    }

    @Test
    fun addTest() {
        testcase {
            inLocationList {
                inList { hasItems(LocationListScreen.itemCount) }
            }

            // Add Location
            inAppFrame {
                inFabItem { click() }
            }

            inLocationEdit {
                inNameLayout {
                    inEdit {
                        hasText("New Location")
                        clearText()
                        typeText(LocationEditScreen.input)
                    }
                }

                inDescLayout {
                    inEdit { typeText(LocationEditScreen.input) }
                }

                inThumbLayout {
                    // because scrolling of a nested scroll view doesn't work
                    closeSoftKeyboard()
                    inEdit { typeText(LocationEditScreen.input) }
                }

                inIntroLayout {
                    closeSoftKeyboard()
                    inEdit { typeText(LocationEditScreen.input) }
                }
            }

            // Save new location
            inAppFrame {
                inFabItem { click() }
            }

            inLocationList {
                inList {
                    hasItems(LocationListScreen.itemCount + 1)

                    inItem(0) {
                        hasTextInside(LocationEditScreen.input)
                    }
                }
            }
        }
    }
}