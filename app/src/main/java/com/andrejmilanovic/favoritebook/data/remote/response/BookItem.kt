package com.andrejmilanovic.favoritebook.data.remote.response


import com.google.gson.annotations.SerializedName

data class BookItem(
    @SerializedName("accessInfo")
    val accessInfo: AccessInfo,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("saleInfo")
    val saleInfo: SaleInfo,
    @SerializedName("searchInfo")
    val searchInfo: SearchInfo,
    @SerializedName("selfLink")
    val selfLink: String,
    @SerializedName("volumeInfo")
    val volumeInfo: VolumeInfo
)