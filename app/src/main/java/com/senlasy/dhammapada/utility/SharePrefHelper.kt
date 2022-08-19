package com.senlasy.dhammapada.utility

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class SharePrefHelper {

    var CURRENT_DB_VER = 5
    var DB_PREF_NAME = "DHAMMAPADA_SENLASY_DB_VERSION"

    fun DBVersion(context: Context) : Int {
         var PRIVATE_MODE = 0
         val PREF_NAME = "DHAMMAPADA-PREF"

         val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        if(sharedPref.contains(DB_PREF_NAME)) {
            return sharedPref.getInt(DB_PREF_NAME, 0)
        } else {
            return 0
        }
    }

    public fun saveDBVersion(context: Context) {
        var PRIVATE_MODE = 0
        val PREF_NAME = "DHAMMAPADA-PREF"
        val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor : SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(DB_PREF_NAME, CURRENT_DB_VER)
        editor.apply()
    }

    public fun checkCurrentDB(context: Context) : Boolean {
       return CURRENT_DB_VER == DBVersion(context)
    }

}