package com.hiccup.kidpainting.adapters

import android.content.Context
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.hiccup.kidpainting.R
import com.hiccup.kidpainting.models.PhotoModel
import com.hiccup.kidpainting.utilities.AppConstants


open class PhotoCollectionAdapter(var context: Context, var collectionName: String, var photos: ArrayList<PhotoModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<PhotoHolder>() {
    lateinit var listener: OnSelectListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        return PhotoHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_photo_collection, parent, false))
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        holder.ivContent?.let { loadImageToView(context,collectionName + "/" + photos[position].name, it) }
        holder.itemView.setOnClickListener {
            listener.onSelectItem(it, position)
        }
        if (photos[position].isEnable) {
            holder.ivLock?.visibility = View.GONE
        } else {
            holder.ivLock?.visibility = View.VISIBLE
        }

    }

    companion object {
        fun loadImageToView(context: Context, name: String, ivView: ImageView) {
            val file = Uri.parse(AppConstants.ASSET_PATH + name)
//            Picasso.get().load(file).into(ivView)
            Glide.with(context).load(file).into(ivView)
        }
    }


}