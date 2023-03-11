package com.hiccup.kidpainting.activities

import android.content.Intent
import com.hiccup.kidpainting.services.MediaService


/**
 * Created by ${binhpd} on 10/17/2015.
 */
abstract class BasePaintingActivity : BaseActivity() {

    override fun onInitValue() {
    }

    protected fun playAudio() {
        val pauseIntent = Intent(Broadcast_PLAY_AUDIO)
        pauseIntent.putExtra(MediaService.ACTION, MediaService.ACTION_PLAY)
        pauseIntent.putExtra(MediaService.EXTRA_AUDIO, getAudioSrc())
        sendBroadcast(pauseIntent)
    }

    protected fun pauseAudio() {
        val pauseIntent = Intent(Broadcast_PLAY_AUDIO)
        pauseIntent.putExtra(MediaService.ACTION, MediaService.ACTION_PAUSE)
        pauseIntent.putExtra(MediaService.EXTRA_AUDIO, getAudioSrc())
        sendBroadcast(pauseIntent)
    }

    protected fun resumeAudio() {
        val pauseIntent = Intent(Broadcast_PLAY_AUDIO)
        pauseIntent.putExtra(MediaService.ACTION, MediaService.ACTION_RESUME)
        pauseIntent.putExtra(MediaService.EXTRA_AUDIO, getAudioSrc())
        sendBroadcast(pauseIntent)
    }

    protected abstract fun getAudioSrc(): Int
}
