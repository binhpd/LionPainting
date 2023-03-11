package com.hiccup.kidpainting.activities

import android.app.Dialog
import android.content.Intent
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.adapters.CollectionAdapter
import com.hiccup.kidpainting.adapters.OnSelectListener
import com.hiccup.kidpainting.common.KidConfig
import com.hiccup.kidpainting.databinding.ActivityCollectionBinding
import com.hiccup.kidpainting.models.CollectionItem
import com.hiccup.kidpainting.pref.AppPurchase
import com.hiccup.kidpainting.utilities.FileUtils
import com.hiccup.kidpainting.utilities.OnboardingHelper
import com.hiccup.kidpainting.utilities.RewardHelper
import com.hiccup.kidpainting.utilities.SoundEffect
import com.hiccup.kidpainting.utilities.extension.viewBinding
import com.hiccup.kidpainting.utilities.tracking.FireBaseEvent
import com.hiccup.kidpainting.utilities.tracking.FireBaseTracker
import com.hiccup.kidpainting.view.dialog.FreeUserDialog

class CollectionActivity : BaseActivity() {

    var dialog: Dialog? = null

    override val binding by viewBinding {
        ActivityCollectionBinding.inflate(layoutInflater)
    }

    override fun onInitValue() {
        FireBaseTracker.sendEvent(FireBaseEvent.COLLECTION_OPEN)
    }

    override fun onResume() {
        super.onResume()
        loadCollection()
    }

    private fun loadCollection() {
        val collections = loadCollectionList()
        bindCollection(collections)
    }

//    override fun onRewarded(reward: RewardItem) {
//        super.onRewarded(reward)
//        RewardHelper.getInstance().setRewardCollection(reward.amount)
//        loadCollection()
//    }

    private fun bindCollection(collections: ArrayList<CollectionItem>) {
        val adapter = CollectionAdapter(this@CollectionActivity, collections)
        adapter.listener = OnSelectListener { _: View, index: Int ->
            FireBaseTracker.sendEvent(FireBaseEvent.COLLECTION_SELECT_ITEM)
            SoundEffect.instance.playSound(R.raw.tap)

            if (adapter.list[index].isNew) {
                FireBaseTracker.sendEvent(FireBaseEvent.COLLECTION_CLICK_NEW)
            }

            if (adapter.list[index].isEnable) {
                openGraphicScreen(adapter.list[index], SelectGraphicActivity.MODE_NORMAL)
            } else {
                FireBaseTracker.sendEvent(FireBaseEvent.COLLECTION_SELECT_UNLOCK_ITEM)
                showFreeUserDialog()
            }
        }
        binding.rvCollection.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvCollection.adapter = adapter

        binding.ivBack.setOnClickListener {
            SoundEffect.instance.playSound(R.raw.tap)
            finish()
        }

        showToolTip()
    }

    private fun openGraphicScreen(collectionItem: CollectionItem, mode: Int) {
        intent = Intent(this@CollectionActivity, SelectGraphicActivity::class.java)
        intent.putExtra(SelectGraphicActivity.COLLECTION_TYPE, collectionItem)
        intent.putExtra(SelectGraphicActivity.MODE_OPEN, mode)
        startActivity(intent)
        if (!AppPurchase.instance.isHasPurchase(this@CollectionActivity)) {
            FireBaseTracker.sendEvent(FireBaseEvent.COLLECTION_SELECT_FREE_ITEM)
        }
    }

    private fun openStore() {
        val intent = Intent(this@CollectionActivity, LockKidActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun showFreeUserDialog() {
        val dialog = FreeUserDialog(this, object : FreeUserDialog.DialogListener {
            override fun onClickShare() {
                FireBaseTracker.sendEvent(FireBaseEvent.SHOW_REWARD_VIDEO)
                showLoadingDialog()
                loadRewardedVideoAd()
            }

            override fun onClickGotoStore() {
                if (!AppPurchase.instance.isHasPurchase(this@CollectionActivity)) {
                    FireBaseTracker.sendEvent(FireBaseEvent.CLICK_OPEN_STORE_COLLECTION_DIALOG)
                }
                openStore()
            }

            override fun onDismiss() {
                fullScreenRequest()
            }
        })
        dialog.showDialog()

    }

//    override fun onRewardedVideoAdLoaded() {
//        mRewardedVideoAd.show()
//        dialog?.hide()
//    }
//
//    override fun onRewardedVideoAdFailedToLoad(errorCode: Int) {
//        super.onRewardedVideoAdFailedToLoad(errorCode)
//        dialog?.hide()
//    }


    private fun loadCollectionList(): ArrayList<CollectionItem> {
        FileUtils.getStringFromAsset(this, KidConfig.COLORING_CONFIG)
        val file = FileUtils.getStringFromAsset(this, KidConfig.COLORING_CONFIG)
        val collectionType = object : TypeToken<ArrayList<CollectionItem>>() {}.type
        val collections = Gson().fromJson<ArrayList<CollectionItem>>(file, collectionType)

        val isPurchase = AppPurchase.instance.isHasPurchase(this@CollectionActivity)
        if (isPurchase || RewardHelper.instance.rewardAccessCollection >= 0) {
            for (collectionItem in collections) {
                collectionItem.isEnable = true
            }
        } else {
            collections[0].isEnable = true
            collections[1].isEnable = true
            collections[2].isEnable = true
        }

        collections[1].isNew = true
        collections[2].isNew = true

        collections[0].title = getString(R.string.supper_hero)
        collections[1].title = getString(R.string.pokemon)
        collections[2].title = getString(R.string.unicorns)
        collections[3].title = getString(R.string.collection_animal)
        collections[4].title = getString(R.string.number)
        collections[5].title = getString(R.string.collection_animal_water)
        collections[6].title = getString(R.string.collection_monster)
        collections[7].title = getString(R.string.collection_cartoon)
        collections[8].title = getString(R.string.collection_transport)
        return collections
    }

    private fun showToolTip() {
        val handler = Handler()
        handler.postDelayed({
            if (binding.rvCollection.childCount > 0) {
                try {
                    OnboardingHelper.showBoarding(this, getString(R.string.onboarding_step2),
                            Gravity.CENTER_VERTICAL, Gravity.CENTER,
                        binding.rvCollection.getChildAt(0),
                            CollectionActivity::class.java.simpleName)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        }, 750)
    }

    private fun showLoadingDialog() {
        if (dialog == null) {
            dialog = Dialog(this)
            val view = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
            dialog!!.setContentView(view)
        }
        dialog!!.setCancelable(false)
        dialog!!.show()

        val handler = Handler()
        handler.postDelayed({
            dialog?.setCancelable(true)
        }, 5000)
    }
}