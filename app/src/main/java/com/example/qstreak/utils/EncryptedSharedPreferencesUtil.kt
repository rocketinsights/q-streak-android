package com.example.qstreak.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object EncryptedSharedPreferencesUtil {

    private const val USER_PREFS = "user_prefs"
    private const val UID_KEY = "uid"

    fun getUid(context: Context): String? {
        return getEncryptedSharedPreferences(context).getString(UID_KEY, null)
    }

    fun setUid(context: Context, uid: String) {
        getEncryptedSharedPreferences(context).edit().putString(UID_KEY, uid).apply()
    }

    private fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        return EncryptedSharedPreferences.create(
            USER_PREFS,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}