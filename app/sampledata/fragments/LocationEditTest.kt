/*
    ARTester - AR for tourists by tourists
    Copyright (C) 2018, 2019  Michael Vogt

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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationEditTest {
    @get:Rule
    val activityRule = ActivityTestRule(AuthorActivity::class.java)

    @Before
    fun openLocationEdit() {
        LocationEditScreen.open()
    }

    @Test
    fun toolbarTest() {
        testcase {
            inLocationEdit {
                inToolbar {
                    isDisplayed()
                    hasNavigationIcon()
                    hasText(LocationEditScreen.title)
                    hasNoAction()
                    hasNavigationIcon()
                }
            }
        }
    }

    @Test
    fun bottomBarTest() {
        testcase {
            inAppFrame {
                inBottomBar {
                    isDisplayed()
                    hasAction()
                    hasNavigation()
                }

                inFabItem {
                    isDisplayed()
                }
            }
        }
    }

    @Test
    fun cancelEditTest() {
        testcase {
            inLocationEdit {
                inNameLayout {
                    inEdit {
                        typeText(LocationEditScreen.input)
                    }
                    closeSoftKeyboard()
                    pressBack()
                }
            }


            inLocationList {
                hasNotTextInside(LocationEditScreen.input)
            }
        }
    }

    @Test
    fun saveEditTest() {
        testcase {
            inLocationEdit {
                inNameLayout {
                    inEdit { typeText(LocationEditScreen.input) }
                }

                inDescLayout {
                    inEdit { typeText(LocationEditScreen.input) }
                }

                inThumbLayout {
                    // Necessary because Espresso doesn't scroll a NestedScrollView
                    closeSoftKeyboard()
                    inEdit { typeText(LocationEditScreen.input) }
                }

                inIntroLayout {
                    closeSoftKeyboard()
                    inEdit { typeText(LocationEditScreen.input) }

                    closeSoftKeyboard()
                }
            }

            inAppFrame {
                inFabItem { click() }
            }

            inLocationList {
                inList {
                    inItem(1) {
                        inItemButton { click() }
                    }
                }

                inEditMenu { click() }
            }

            inLocationEdit {
                inNameLayout {
                    containsTextInside(LocationEditScreen.input)
                }
                inDescLayout {
                    containsTextInside(LocationEditScreen.input)
                }
                inThumbLayout {
                    containsTextInside(LocationEditScreen.input)
                }

                inIntroLayout {
                    containsTextInside(LocationEditScreen.input)
                }
            }
        }
    }
}