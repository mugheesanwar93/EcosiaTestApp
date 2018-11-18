package org.ecosia.ecosiatestapp.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import org.ecosia.ecosiatestapp.R;
import org.ecosia.ecosiatestapp.databinding.ActivityAudioPlayerBinding;
import org.ecosia.ecosiatestapp.utils.AppUtil;
import org.ecosia.ecosiatestapp.viewmodel.AudioPlayerViewModel;

public class AudioPlayerActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            setupBinding();
        }
    }

    private void setupBinding() {
        ActivityAudioPlayerBinding activityAudioPlayerBinding = DataBindingUtil.setContentView(this, R.layout.activity_audio_player);

        AudioPlayerViewModel audioPlayerViewModel = new AudioPlayerViewModel(this);
        activityAudioPlayerBinding.setPlayer(audioPlayerViewModel);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupBinding();
                } else {
                    AppUtil.showToast(this, getString(R.string.need_all_permissions));
                }
            }
        }
    }
}
