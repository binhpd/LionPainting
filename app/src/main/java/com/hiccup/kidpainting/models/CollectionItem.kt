package com.hiccup.kidpainting.models

import java.io.Serializable

class CollectionItem(var title: String, var name: String, var logoName: String, var logoIcon: String, var photos: ArrayList<PhotoModel>, var isEnable: Boolean) : Serializable {
    var isNew = false
}
