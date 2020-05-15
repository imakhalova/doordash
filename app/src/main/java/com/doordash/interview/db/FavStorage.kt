package com.doordash.interview.db

import android.content.Context
import com.doordash.interview.list.ListItem

interface IFavStorage {
    fun contains(item: ListItem) : Boolean
    fun switchState(item: ListItem)
}

class FavStorage(context: Context) : IFavStorage {

    companion object {
        const val PREF = "favorites"
    }
    val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
    var set = prefs.getStringSet(PREF, emptySet())!!
    override fun contains(item:ListItem) : Boolean {
        return set.contains(item.id.toString())
    }

    override fun switchState(item:ListItem) {
        val mutableSet = set.toMutableSet()
        if (contains(item)) {
            mutableSet.remove(item.id.toString())
        } else {
            mutableSet.add(item.id.toString())
        }
        prefs.edit().putStringSet(PREF, mutableSet).apply()
        set = mutableSet
    }


}