package com.doordash.interview.detail

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DetailItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("cover_img_url")
    val logo: String,
    @SerializedName("phone_number")
    val phone_number: String,
    @SerializedName("tags")
    val tags: List<String>
)  : Serializable
