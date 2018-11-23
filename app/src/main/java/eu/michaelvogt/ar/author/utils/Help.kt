package eu.michaelvogt.ar.author.utils

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import eu.michaelvogt.ar.author.R
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetSequence

fun bgColor(fragment: Fragment) = ContextCompat.getColor(fragment.context!!, R.color.secondaryTranslucentColor)

fun showLocationInfo(fragment: Fragment) {
    MaterialTapTargetPrompt.Builder(fragment, R.style.MaterialTabTargetPromptTheme)
            .setTarget(R.id.info_button)
            .setPrimaryText(R.string.location_info_primary)
            .setSecondaryText(R.string.location_info_secondary)
            .setBackgroundColour(bgColor(fragment))
            .show()
}

fun showUiInfo(fragment: Fragment) {
    MaterialTapTargetSequence()
            .addPrompt(MaterialTapTargetPrompt.Builder(fragment)
                    .setTarget(com.google.android.material.R.id.design_navigation_view)
                    .setPrimaryText("Navigation menu")
                    .setSecondaryText("Info")
                    .setBackgroundColour(bgColor(fragment))
                    .show())
            .addPrompt(MaterialTapTargetPrompt.Builder(fragment)
                    .setTarget(R.id.bottom_nav_fab)
                    .setPrimaryText("FAB")
                    .setSecondaryText("Info")
                    .setBackgroundColour(bgColor(fragment))
                    .show())
            .show()
}
