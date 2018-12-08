package eu.michaelvogt.ar.author.utils

import android.view.View
import androidx.annotation.StringRes
import eu.michaelvogt.ar.author.R
import eu.michaelvogt.ar.author.fragments.AreaEditCard

/**
 *  Provide display strings for non-string data types
 */
class DisplayStringFunctions(
        private val hostView: View,
        private val methodName: String?) : Function<String> {

    fun accept(): String {
        return when (methodName) {
            stringResource(R.string.display_converter_from_vector3) -> displayFromVector3()
            stringResource(R.string.display_converter_from_quaternion) -> displayFromQuaternion()
            stringResource(R.string.display_converter_from_selection) -> displayFromSelect()
            else -> (hostView as AreaEditCard).getXValue()
        }
    }

    fun displayFromVector3(): String {
        if (hostView !is AreaEditCard) return ""
        return "${stringResource(R.string.area_card_labelx)}: ${hostView.getXValue()},  " +
                "${stringResource(R.string.area_card_labely)}: ${hostView.getYValue()},  " +
                "${stringResource(R.string.area_card_labelz)}: ${hostView.getZValue()}"
    }

    fun displayFromQuaternion(): String {
        if (hostView !is AreaEditCard) return ""
        return "${stringResource(R.string.area_card_labelx)}: ${hostView.getXValue()},  " +
                "${stringResource(R.string.area_card_labely)}: ${hostView.getYValue()},  " +
                "${stringResource(R.string.area_card_labelz)}: ${hostView.getZValue()},  " +
                "${stringResource(R.string.area_card_labelw)}: ${hostView.getWValue()}"
    }

    fun displayFromSelect(): String {
        if (hostView !is AreaEditCard) return ""
        return hostView.getSValue()
    }

    private fun stringResource(@StringRes resource: Int) = hostView.context.resources.getString(resource)
}
