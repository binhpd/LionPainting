package com.hiccup.kidpainting.activities.store

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.android.billingclient.api.ProductDetails
import com.hiccup.kidpainting.utilities.billing.BillingClientLifecycle
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.activities.BaseActivity
import com.hiccup.kidpainting.databinding.ActivityStoreBinding
import com.hiccup.kidpainting.pref.AppPurchase
import com.hiccup.kidpainting.utilities.PaintingApplication
import com.hiccup.kidpainting.utilities.billing.BillingConstants
import com.hiccup.kidpainting.utilities.extension.viewBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StoreActivity : BaseActivity(), View.OnClickListener {

    private lateinit var billingClientLifecycle: BillingClientLifecycle
    private lateinit var authenticationViewModel: FirebaseUserViewModel
    private lateinit var billingViewModel: BillingViewModel
    private lateinit var subscriptionViewModel: SubscriptionStatusViewModel

    override val binding by viewBinding {
        ActivityStoreBinding.inflate(layoutInflater)
    }

    override fun onInitValue() {
        populateView()
        setUpBilling()
    }

    private fun setUpBilling() {
        authenticationViewModel = ViewModelProvider(this)[FirebaseUserViewModel::class.java]
        billingViewModel = ViewModelProvider(this)[BillingViewModel::class.java]
        subscriptionViewModel =
            ViewModelProvider(this)[SubscriptionStatusViewModel::class.java]

        // Billing APIs are all handled in the this lifecycle observer.
        billingClientLifecycle = (application as PaintingApplication).billingClientLifecycle
        lifecycle.addObserver(billingClientLifecycle)

        // Launch the billing flow when the user clicks a button to buy something.
        billingViewModel.buyEvent.observe(this) {
            if (it != null) {
                billingClientLifecycle.launchBillingFlow(this, it)
            }
        }

        billingClientLifecycle.productsWithProductDetails.observe(this) { products ->
            val basicMonthly = ((products[BillingConstants.BASIC_PRODUCT] as ProductDetails).subscriptionOfferDetails)?.
            filter { it.offerTags.contains(BillingConstants.BASIC_MONTHLY_PLAN) }
            basicMonthly?.let {
                if (!it.isNullOrEmpty()) {
                    Log.d(TAG, it[0].pricingPhases.pricingPhaseList[0].formattedPrice)
                    binding.tvPriceRemoveAds.text = it[0].pricingPhases.pricingPhaseList[0].formattedPrice
                }

            }

            val basicYearly = ((products[BillingConstants.BASIC_PRODUCT] as ProductDetails).subscriptionOfferDetails)?.
            filter { it.offerTags.contains(BillingConstants.BASIC_YEARLY_PLAN) }
            basicYearly?.let {
                if (!it.isNullOrEmpty()) {
                    Log.d(TAG, it[0].pricingPhases.pricingPhaseList[0].formattedPrice)
                    binding.tvPriceFullPurchase.text = it[0].pricingPhases.pricingPhaseList[0].formattedPrice
                }

            }

        }

        GlobalScope.launch {
            ((application as PaintingApplication).billingClientLifecycle.purchases).collect { purchases ->
                Log.i(TAG, "Local purchases...")
                if (purchases.isNotEmpty()) {
                    AppPurchase.instance.saveStatePurchase(baseContext, purchases[0].purchaseToken)
                } else {
                    AppPurchase.instance.clear(baseContext)
                }
            }
        }

        // Open the Play Store when this event is triggered.
        billingViewModel.openPlayStoreSubscriptionsEvent.observe(this) { product ->
            Log.i(TAG, "Viewing subscriptions on the Google Play Store")
            val url = if (product == null) {
                // If the Product is not specified, just open the Google Play subscriptions URL.
                BillingConstants.PLAY_STORE_SUBSCRIPTION_URL
            } else {
                // If the Product is specified, open the deeplink for this Product on Google Play.
                String.format(BillingConstants.PLAY_STORE_SUBSCRIPTION_DEEPLINK_URL, product, packageName)
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    private fun populateView() {
        binding.ivClose.setOnClickListener(this)
        binding.groupRemoveAds.setOnClickListener(this)
        binding.groupFullPurchase.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_close -> finish()

            R.id.groupRemoveAds -> {
//                onClickPurchaseAds()
//                FireBaseTracker.sendEvent(FireBaseEvent.STORE_SELECT_REMOVE_ADS)
                billingViewModel.buyBasePlans(BillingConstants.BASIC_MONTHLY_PLAN, BillingConstants.BASIC_PRODUCT, false)
            }

            R.id.groupFullPurchase -> {
                billingViewModel.buyBasePlans(BillingConstants.BASIC_YEARLY_PLAN, BillingConstants.BASIC_PRODUCT, false)
                //FireBaseTracker.sendEvent(FireBaseEvent.STORE_SELECT_REMOVE_ADS)
            }
        }
    }


    companion object {
        private const val TAG = "StoreActivity"
    }

}
