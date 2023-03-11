package com.hiccup.kidpainting.utilities

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.widget.Toast

import com.hiccup.kidpainting.R

import java.util.HashMap

class SoundEffect private constructor() {

    private var context: Context? = null

    fun init(context: Context) {
        this.context = context
    }

    fun playSound(soundId: Int) {
        val mp = MediaPlayer.create(context, soundId)
        mp.setVolume(0.5f, 0.5f)
        mp.start()
    }

    companion object {
        private val TAG = SoundEffect::class.java.toString()

        @JvmStatic
        val instance = SoundEffect()
    }
}
