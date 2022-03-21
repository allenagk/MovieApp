package jp.ml.movies.common

import java.text.NumberFormat
import java.util.*

/**
 * Helper class contains methods which can be reused across the application
 */
object Utils {

    /**
     * Returns display language for language code
     */
    fun getDisplayLanguage(code: String?): String {
        return if (code.isNullOrBlank()) {
            "-"
        } else {
            val locale = Locale(code)
            locale.displayLanguage
        }
    }

    /**
     * Returns display duration
     */
    fun getFormattedDuration(duration: Int?): String {
        return if (duration == null || duration == 0) {
            "-"
        } else {
            val hours = duration / 60
            val minutes = duration % 60
            if (hours > 0) {
                "${hours}h ${minutes}m"
            } else {
                "${minutes}m"
            }
        }
    }

    /**
     * Returns formatted currency
     */
    fun getFormattedCurrency(value: Int?): String {
        return if (value == null || value == 0) {
            "-"
        } else {
            val numberFormat = NumberFormat.getCurrencyInstance()
            numberFormat.maximumFractionDigits = 2;
            numberFormat.format(value)
        }
    }

}