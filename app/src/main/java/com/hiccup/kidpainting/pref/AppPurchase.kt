package com.hiccup.kidpainting.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.hiccup.kidpainting.pref.AppPurchase
import com.hiccup.kidpainting.utilities.RewardHelper
import com.hiccup.kidpainting.utilities.SoundEffect

class AppPurchase {

    private val MY_PREFERENCE = "Pref"
    private val KEY_STATE_PURCHASE = "state_purchase" //none: 0, remove_ads: 1, full_purchase: 2
    val purchaseChange = MutableLiveData<Boolean>()

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(MY_PREFERENCE, Context.MODE_PRIVATE)
    }

    @Synchronized
    fun saveStatePurchase(context: Context, token: String?) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_STATE_PURCHASE, token)
        editor.apply()

        purchaseChange.postValue(isHasPurchase(context))
    }

    @Synchronized
    fun getStatePurchase(context: Context): String? {
        return getPreferences(context).getString(KEY_STATE_PURCHASE, "")
    }

    @Synchronized
    fun isHasPurchase(context: Context): Boolean {
//        if (BuildConfig.DEBUG) {
//            return true;
//        }
        return getStatePurchase(context)!!.isNotEmpty()
    }

    fun clear(context: Context) {
        val editor = getPreferences(context).edit()
        editor.remove(KEY_STATE_PURCHASE)
        editor.apply()
    }

    companion object {
        private var reward: AppPurchase? = null
        val instance: AppPurchase
            get() {
                if (reward == null) {
                    reward = AppPurchase()
                }
                return reward!!
            }
    }
}