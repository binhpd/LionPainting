package com.hiccup.kidpainting.activities

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.view.View
import androidx.core.content.FileProvider
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.databinding.ActivityScreenshotBinding
import com.hiccup.kidpainting.firebase.ImageLoader
import com.hiccup.kidpainting.utilities.LogUtils
import com.hiccup.kidpainting.utilities.extension.viewBinding
import com.hiccup.kidpainting.utilities.tracking.FireBaseEvent
import com.hiccup.kidpainting.utilities.tracking.FireBaseTracker
import java.io.File

class PreviewPhotoActivity : BaseActivity(), View.OnClickListener {

    override val binding by viewBinding {
        ActivityScreenshotBinding.inflate(layoutInflater)
    }

    lateinit var mPhotoPath: String

    override fun onInitValue() {
        binding.ivBack.setOnClickListener(this@PreviewPhotoActivity)
        binding.ivShare.setOnClickListener(this@PreviewPhotoActivity)
        mPhotoPath = intent.extras!!.getString(DrawingActivity.EXTRA_PHOTO)!!

        loadPhoto(mPhotoPath)
        FireBaseTracker.sendEvent(FireBaseEvent.PREVIEW_OPEN)

        val handler = Handler()
        handler.postDelayed({
            showStartEffect()
        }, 1000)
    }

    private fun loadPhoto(path: String) {
        val imageLoader = ImageLoader(path, object : ImageLoader.LoadImageListener {
            override fun onLoadSuccess(bitmap: Bitmap) {
                binding.ivPreview.setImageBitmap(bitmap)
            }

            override fun loadFail() {
            }

        })

        imageLoader.execute()
    }


    private fun showStartEffect() {
//        ParticleSystem(this, 10, R.drawable.star, 3000)
//                .setSpeedByComponentsRange(-0.1f, 0.1f, -0.1f, 0.02f)
//                .setAcceleration(0.000003f, 90)
//                .setInitialRotationRange(0, 360)
//                .setRotationSpeed(120f)
//                .setFadeOut(2000)
//                .addModifier(ScaleModifier(0f, 1.5f, 0, 1500))
//                .oneShot(anchorView, 10)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> finish()
            R.id.ivShare -> {
                FireBaseTracker.sendEvent(FireBaseEvent.PREVIEW_SHARE)
                sharePhoto()
            }
        }
    }

    private fun sharePhoto() {
        val fileUri: Uri? = try {
            FileProvider.getUriForFile(
                    this@PreviewPhotoActivity, getString(R.string.package_auth),
                    File(mPhotoPath))
        } catch (e: IllegalArgumentException) {
            LogUtils.e("File Selector",
                    "The selected file can't be shared: $mPhotoPath")
            null
        }
        if (fileUri != null) {
            val shareIntent = Intent()
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
            shareIntent.type = "image/*"
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"))
            // Grant temporary read permission to the content URI

        }
    }

}