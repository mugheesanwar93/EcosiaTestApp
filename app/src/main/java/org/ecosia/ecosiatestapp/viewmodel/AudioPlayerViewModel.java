package org.ecosia.ecosiatestapp.viewmodel;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import org.ecosia.ecosiatestapp.R;
import org.ecosia.ecosiatestapp.pojo.AudioMetaData;
import org.ecosia.ecosiatestapp.ui.AudioPlayerActivity;
import org.ecosia.ecosiatestapp.utils.AppUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class AudioPlayerViewModel extends BaseViewModel {
    private Activity activity;
    private MediaPlayer mp;

    //custom data class and object to store the audio files on run-time
    private ArrayList<AudioMetaData> audioList = new ArrayList<>();
    private AudioMetaData randomAudioItem;

    //handling two way databinding as both values are being updated continuously
    public final ObservableField<Integer> progressValue = new ObservableField<>();
    public final ObservableField<String> currentTime = new ObservableField<>();

    private Handler mHandler = new Handler();

    public AudioPlayerViewModel(AudioPlayerActivity audioPlayerActivity) {
        this.activity = audioPlayerActivity;
        getMediaFileList();
    }

    //Getting all the media files from the device
    private void getMediaFileList() {
        ContentResolver contentResolver = activity.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        //in case no audio is found
        if (cursor == null) {
            AppUtil.showToast(activity, activity.getString(R.string.something_went_wrong));
        } else if (!cursor.moveToFirst()) {
            AppUtil.showToast(activity, activity.getString(R.string.no_media_found));
        } else {
            //in case audio is found. Add all to a custom class
            do {
                audioList.add(new AudioMetaData(
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID))));
            } while (cursor.moveToNext());
        }
        randomAudioChooser();
    }

    //choose a random audio from the collection
    private void randomAudioChooser() {
        randomAudioItem = audioList.get(new Random().nextInt(audioList.size()));
        startAudio();
    }

    //play the random audio
    private void startAudio() {
        mp = new MediaPlayer();
        try {
            mp.setDataSource(randomAudioItem.getData());
            mp.prepare();
            mp.start();
            setProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //set progress of the audio to the seekbar / progress bar
    private void setProgress() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mp != null) {
                    float mSeekValue = ((float) mp.getCurrentPosition() / (float) mp.getDuration()) * 100;
                    progressValue.set((int) mSeekValue);
                    currentTime.set(changeTimeFormat(mp.getCurrentPosition()));
                }
                mHandler.postDelayed(this, 1000);
            }
        });
    }

    //disable the seekbar so the user can't interact with it
    public Boolean isEnabled() {
        return false;
    }

    //setup the artist value
    @Bindable
    public String getArtistName() {
        return randomAudioItem.getArtist();
    }

    //setup the title value
    @Bindable
    public String getTitle() {
        return randomAudioItem.getTitle();
    }

    //setup the duration value
    @Bindable
    public String getDuration() {
        return changeTimeFormat(mp.getDuration());
    }

    //get bitmap from Album ID
    public Bitmap getImageBitmap() {
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, Long.parseLong(randomAudioItem.getalbumId()));
        ContentResolver res = activity.getContentResolver();
        InputStream in = null;
        try {
            in = res.openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(in);
    }

    //setup the album art
    @BindingAdapter({"imageBitmap"})
    public static void setImageBitmap(ImageView view, Bitmap imageBitmap) {
        if (imageBitmap != null)
            view.setImageBitmap(imageBitmap);
    }

    //handling play and stop feature (btn)
    public View.OnClickListener btnPressed() {
        return v -> {
            if (mp.isPlaying()) {
                v.setActivated(true);
                progressValue.set(0);
                currentTime.set(changeTimeFormat(0));
                mHandler.removeCallbacksAndMessages(null);
                mp.stop();
            } else {
                v.setActivated(false);
                startAudio();
            }
        };
    }
}
