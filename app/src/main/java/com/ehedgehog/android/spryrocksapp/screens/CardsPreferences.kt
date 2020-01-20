package com.ehedgehog.android.spryrocksapp.screens

import android.content.Context
import android.preference.PreferenceManager

class CardsPreferences {

    companion object {
        private const val PREF_CARD = "storedCard"

        fun getStoredCardId(context: Context): String? {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_CARD, null)
        }

        fun setStoredCardId(context: Context, cardId: String) {
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_CARD, cardId)
                .apply()
        }
    }

}