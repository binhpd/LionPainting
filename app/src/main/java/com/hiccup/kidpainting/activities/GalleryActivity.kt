package com.hiccup.kidpainting.activities

import android.content.Intent
import android.os.Environment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.adapters.GalleryAdapter
import com.hiccup.kidpainting.adapters.OnSelectListener
import com.hiccup.kidpainting.databinding.ActivityDrawingBinding
import com.hiccup.kidpainting.databinding.ActivitySelectGraphicBinding
import com.hiccup.kidpainting.models.PhotoModel
import com.hiccup.kidpainting.utilities.UIUtils
import com.hiccup.kidpainting.utilities.extension.viewBinding
import com.hiccup.kidpainting.utilities.tracking.FireBaseEvent
import com.hiccup.kidpainting.utilities.tracking.FireBaseTracker
import java.io.File

class GalleryActivity : BasePaintingActivity(), View.OnClickListener {
    override val binding by viewBinding {
        ActivitySelectGraphicBinding.inflate(layoutInflater)
    }

    override fun getAudioSrc(): Int {
        return R.raw.drawing_audio
    }

    override fun onInitValue() {
        super.onInitValue()
        findViewById<View>(R.id.ivBack).setOnClickListener(this@GalleryActivity)
        loadPhotoList()
        FireBaseTracker.sendEvent(FireBaseEvent.GALLERY_OPEN)
    }

    override fun onClick(v: View?) {
        finish()
    }

    private fun loadPhotoList() {
        val galleryPath = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES + "/KidPainting"), "")
        val photoNames = galleryPath.list()
        val photosModel = convertToPhotoModels(photoNames, galleryPath.absolutePath)
        val adapter = GalleryAdapter(this, photosModel)
        adapter.listener = OnSelectListener { view, index ->
            val intent = Intent(this@GalleryActivity, PreviewPhotoActivity::class.java)
            intent.putExtra(DrawingActivity.EXTRA_PHOTO, photosModel[index].rootPath + "/" + photosModel[index].name)
            startActivity(intent)
            FireBaseTracker.sendEvent(FireBaseEvent.GALLERY_SELECT)
        }
        binding.rvPhotos.adapter = adapter
        val spancount = if (UIUtils.isTablet(this)) 3 else 2
        binding.rvPhotos.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, spancount, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
    }

    private fun convertToPhotoModels(photos: Array<String>?, rootPath: String): ArrayList<PhotoModel> {
        val photoModels = ArrayList<PhotoModel>()
        if (photos != null) {
            for (photoName in photos) {
                photoModels.add(PhotoModel(name = photoName, rootPath = rootPath))
            }
        }
        return photoModels
    }

}
