package com.andrejmilanovic.favoritebook.data.remote.response


import com.google.gson.annotations.SerializedName

data class SearchInfo(
    @SerializedName("textSnippet")
    val textSnippet: String
)