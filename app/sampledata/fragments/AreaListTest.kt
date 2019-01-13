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
class AreaListTest {
    @get:Rule
    val activityRule = ActivityTestRule(AuthorActivity::class.java)

    @Before
    fun openAreaEdit() {
        AreaListScreen.open()
    }

    @Test
    fun toolbarTest() {
        testcase {
            inAreaList {
                inToolbar {
                    isDisplayed()
                    hasNavigationIcon()
                    hasText(AreaListScreen.title)
                    hasAction()
                    hasNavigationIcon()
                }
            }
        }
    }

    @Test
    fun infoButtonTest() {
        testcase {
            inAreaList {
                inAreaInfo { click() }
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
                    hasAction()
                    hasNavigation()
                }

                inFabItem { isNotDisplayed() }
            }
        }
    }

    @Test
    fun deleteTest() {
        testcase {
            inAreaList {
                inList {
                    hasItems(AreaListScreen.itemCount)
                    hasItems()
                    inItem(2) {
                        click()
                    }
                }
            }

            inAreaEdit {
                inToolbar {
                    isDisplayed()
                    hasText(AreaEditScreen.title)
                }

                inDelete { click() }
            }

            inAreaList {
                inList {
                    hasItems(AreaListScreen.itemCount - 1)
                }
            }
        }
    }

    @Test
    fun addTest() {
        testcase {
            inAreaList {
                inList { hasItems(AreaListScreen.itemCount) }
            }

            inAppFrame {
                inFabItem { click() }
            }

            inAreaEdit {
                isDisplayed()
            }

            inAppFrame {
                inFabItem { click() }
            }

            inAreaList {
                inList { hasItems(AreaListScreen.itemCount + 1) }
            }
        }
    }
}
