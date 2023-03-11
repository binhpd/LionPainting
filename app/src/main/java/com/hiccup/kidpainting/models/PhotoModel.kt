package com.hiccup.kidpainting.models

import java.io.Serializable

/**
 * Created by binh on 10/5/2015.
 */
class PhotoModel(var name: String = "", var description: String = "", var rootPath: String = "") : Serializable {
    var isEnable = false
}
