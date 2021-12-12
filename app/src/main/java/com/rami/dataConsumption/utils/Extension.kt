package com.rami.dataConsumption.utils

import android.content.Context
import com.rami.dataConsumption.R
import java.math.RoundingMode

/**
 * Created by Rami El-bouhi on 12,December,2021
 */

fun Long.bytesIntoHumanReadable(context: Context?): String {
    val kilobyte: Long = 1024
    val megabyte = kilobyte * 1024
    val gigabyte = megabyte * 1024
    val terabyte = gigabyte * 1024
    if (this < kilobyte) {
        return "$this ${context?.getString(R.string.byte_unit)}"
    } else if ((this >= kilobyte) && (this < megabyte)) {
        val number = (this * 1.0 / kilobyte)
        val rounded = number.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
        return "$rounded ${context?.getString(R.string.kilobyte_unit)}"
    } else if ((this >= megabyte) && (this < gigabyte)) {
        val number = (this * 1.0 / megabyte)
        val rounded = number.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
        return "$rounded ${context?.getString(R.string.megabyte_unit)}"
    } else if ((this >= gigabyte) && (this < terabyte)) {
        val number = (this * 1.0 / gigabyte)
        val rounded = number.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
        return "$rounded ${context?.getString(R.string.gigabyte_unit)}"
    } else {
        val number = (this * 1.0 / terabyte)
        val rounded = number.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
        return "$rounded ${context?.getString(R.string.terabyte_unit)}"
    }
}