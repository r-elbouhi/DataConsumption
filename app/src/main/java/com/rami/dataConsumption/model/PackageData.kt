package com.rami.dataConsumption.model

/**
 * Created by Rami El-bouhi on 09,December,2021
 */
data class PackageData(
    val name: String? = null,
    val version: String? = null,
    val packageName: String? = null,
    val uid: Int? = null,
    var bytes: Long? = null
)