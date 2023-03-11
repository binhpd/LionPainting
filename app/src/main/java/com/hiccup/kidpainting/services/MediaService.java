package com.hiccup.kidpainting.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.hiccup.kidpainting.activities.BaseActivity;
import com.hiccup.kidpainting.utilities.SettingApp;

/**
 * Created by ${binhpd} on 10/16/2015.
 */
public class MediaService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,

        AudioManager.OnAudioFocusChangeListener {
    public static final String TAG = "MusicService";
    public static final String EXTRA_AUDIO = "EXTRA_AUDIO";

    public static final String ACTION = "com.valdioveliu.valdio.audioplayer.ACTION";
    public static final String ACTION_PLAY = "com.valdioveliu.valdio.audioplayer.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.valdioveliu.valdio.audioplayer.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.valdioveliu.valdio.audioplayer.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.valdioveliu.valdio.audioplayer.ACTION_NEXT";
    public static final String ACTION_STOP = "com.valdioveliu.valdio.audioplayer.ACTION_STOP";
    public static final String ACTION_RESUME = "com.valdioveliu.valdio.audioplayer.ACTION_RESUME";

    private MediaPlayer mediaPlayer;
    //path to the audio file
    private int mediaFile;

    //Used to pause/resume MediaPlayer
    private int resumePosition;

    // Binder given to clients
    private final IBinder iBinder = new AudioBinder();

    private AudioManager audioManager;

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //Invoked indicating buffering status of
        //a media resource being streamed over the network.
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, " onCompletion");
        //Invoked when playback of a media source has completed.
        stopMedia();
        //stop the service
        stopSelf();
    }

    //Handle errors
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //Invoked when there has been an error during an asynchronous operation
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //Invoked to communicate some info.
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "onPrepared");
        //Invoked when the media source is ready for playback.
        playMedia();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        //Invoked indicating the completion of a seek operation.
    }

    @Override
    public void onAudioFocusChange(int focusState) {
        Log.d(TAG, "onAudioFocusChange " + focusState);
        //Invoked when the audio focus of the system is updated.
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (mediaPlayer == null) initMediaPlayer();
                else if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }

    //The system calls this method when an activity, requests the service be started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        try {
            //An audio file is passed to the service through putExtra();
            mediaFile = intent.getIntExtra(EXTRA_AUDIO, -1);
        } catch (NullPointerException e) {
            stopSelf();
        }

        //Request audio focus
        if (requestAudioFocus() == false) {
            //Could not gain focus
            stopSelf();
        }

        if (mediaFile != -1)
            initMediaPlayer();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Perform one-time setup procedures

        // Manage incoming phone calls during playback.
        // Pause MediaPlayer on incoming call,
        // Resume on hangup.
//        callStateListener();
        //ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs -- BroadcastReceiver
//        registerBecomingNoisyReceiver();
        //Listen for new Audio to play -- BroadcastReceiver
        Log.e(TAG, "onCreate");
        register_playNewAudio();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        if (mediaPlayer != null) {
            stopMedia();
            mediaPlayer.release();
        }
        removeAudioFocus();
        //Disable the PhoneStateListener
//        if (phoneStateListener != null) {
//            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
//        }
//
//        removeNotification();

        //unregister BroadcastReceivers
//        unregisterReceiver(becomingNoisyReceiver);
        unregisterReceiver(playNewAudio);

        //clear cached playlist
//        new StorageUtil(getApplicationContext()).clearCachedAudioPlaylist();
    }

    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true;
        }
        //Could not gain focus
        return false;
    }

    private boolean removeAudioFocus() {
        if (audioManager == null) {
            return false;
        }
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }


    public class AudioBinder extends Binder {
        public MediaService getService() {
            return MediaService.this;
        }
    }

    private void initMediaPlayer() {
        Log.e(TAG, "initMediaPlayer");
        mediaPlayer = MediaPlayer.create(this, mediaFile);
        mediaPlayer.setVolume(0.4f, 0.4f);
        //Set up MediaPlayer event listeners
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setLooping(true);

        mediaPlayer.start();
    }

    private void playMedia() {
        Log.e(TAG, "play Media");
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void stopMedia() {
        Log.e(TAG, "stop Media");
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    private void pauseMedia() {
        Log.e(TAG, "pause Media");
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
        }
    }

    private void resumeMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
        }
    }

    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //Get the new media index form SharedPreferences
//            audioIndex = new StorageUtil(getApplicationContext()).loadAudioIndex();
//            if (audioIndex != -1 && audioIndex < audioList.size()) {
//                //index is in a valid range
//                activeAudio = audioList.get(audioIndex);
//            } else {
//                stopSelf();
//            }

            //A PLAY_NEW_AUDIO action received
            handleIncomingActions(intent);

            //reset mediaPlayer to play the new Audio

//            updateMetaData();
//            buildNotification(PlaybackStatus.PLAYING);
        }
    };

    private void register_playNewAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(BaseActivity.Companion.getBroadcast_PLAY_AUDIO());
        registerReceiver(playNewAudio, filter);
    }

    private void handleIncomingActions(Intent playbackAction) {

        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getStringExtra(ACTION);
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            if (SettingApp.MUTE) {
                return;
            }
            mediaFile = playbackAction.getIntExtra(EXTRA_AUDIO, -1);
            stopMedia();
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            }
            initMediaPlayer();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            if (playbackAction.getIntExtra(EXTRA_AUDIO, -1) == mediaFile) {
                pauseMedia();
            }
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
        } else if (actionString.equalsIgnoreCase(ACTION_RESUME)) {
            resumeMedia();
        }
    }
}

