package com.doordash.interview.list

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "table")
data class ListItem (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "key")
    @SerializedName("key")
    val key: Int,
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: Int,
    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String,
    @ColumnInfo(name = "description")
    @SerializedName("description")
    val description: String,
    @ColumnInfo(name = "cover_img_url")
    @SerializedName("cover_img_url")
    val logo: String,
    @ColumnInfo(name = "status")
    @SerializedName("status")
    val status: String
    )  : Serializable