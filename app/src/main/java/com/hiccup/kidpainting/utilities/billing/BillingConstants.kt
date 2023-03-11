package com.hiccup.kidpainting.utilities.billing

object BillingConstants {

    @Volatile
    var USE_FAKE_SERVER = false

    //Product IDs
    const val BASIC_PRODUCT = "up_basic_sub"
    const val PREMIUM_PRODUCT = "up_premium_sub"

    //Tags
    const val BASIC_MONTHLY_PLAN = "monthlybasic"
    const val BASIC_YEARLY_PLAN = "yearlybasic"
    const val PREMIUM_MONTHLY_PLAN = "premiummonthly"
    const val PREMIUM_YEARLY_PLAN = "premiumyearly"
    const val BASIC_PREPAID_PLAN_TAG = "prepaidbasic"
    const val PREMIUM_PREPAID_PLAN_TAG = "prepaidpremium"

    const val PLAY_STORE_SUBSCRIPTION_URL = "https://play.google.com/store/account/subscriptions"
    const val PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL =
        "https://play.google.com/store/account/subscriptions?product=%s&package=%s"
}