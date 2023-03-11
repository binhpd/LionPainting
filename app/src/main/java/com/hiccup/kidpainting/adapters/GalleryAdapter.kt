package com.hiccup.kidpainting.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.models.PhotoModel
import java.io.File
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions


class GalleryAdapter(var context: Context, var photos: ArrayList<PhotoModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<PhotoHolder>() {
    lateinit var listener: OnSelectListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        return PhotoHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_photo_collection, parent, false))
    }

    override fun getItemCount(): Int {
        return this.photos.size
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.ivContent?.let { loadImageToView(photos[position].rootPath + "/" + photos[position].name, it) }
        holder.ivLock?.visibility = View.GONE
        holder.itemView.setOnClickListener {
            listener.onSelectItem(it, position)
        }
    }

    private fun loadImageToView(path: String, ivView: ImageView) {
        val file = File(path)
        if (file.isFile) {
            Glide.with(context)
                    .load(file)
                    .apply(RequestOptions().override(500, 500)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(ivView)
        }
    }

}