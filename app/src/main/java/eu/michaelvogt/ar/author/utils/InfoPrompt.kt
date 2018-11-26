package eu.michaelvogt.ar.author.utils

import androidx.fragment.app.Fragment
import eu.michaelvogt.ar.author.R
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence

object InfoPrompt {
    fun showLocationInfo(fragment: Fragment, target: Int, title: Int, info: Int): Boolean {
        MaterialTapTargetPrompt.Builder(fragment, R.style.MaterialTabTargetPromptTheme)
                .setTarget(target)
                .setPrimaryText(title)
                .setSecondaryText(info)
                .show()
        return true
    }

    fun showUiInfo(fragment: Fragment) {
        MaterialTapTargetSequence()
                .addPrompt(MaterialTapTargetPrompt.Builder(fragment)
                        .setTarget(com.google.android.material.R.id.design_navigation_view)
                        .setPrimaryText("Navigation menu")
                        .setSecondaryText("Info")
                        .show())
                .addPrompt(MaterialTapTargetPrompt.Builder(fragment)
                        .setTarget(R.id.bottom_nav_fab)
                        .setPrimaryText("FAB")
                        .setSecondaryText("Info")
                        .show())
                .show()
    }
}