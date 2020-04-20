package com.example.qstreak.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object EncryptedSharedPreferencesUtil {

    private const val USER_PREFS = "user_prefs"
    private const val UID_KEY = "uid"
    private const val BEARER_PREFIX = "Bearer "

    fun getUidAsBearerToken(context: Context): String? {
        val uid = getEncryptedSharedPreferences(context).getString(UID_KEY, null)
        return if (uid != null) BEARER_PREFIX + uid else null
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
