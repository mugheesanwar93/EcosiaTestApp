package org.ecosia.ecosiatestapp.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.databinding.BaseObservable;

import java.text.SimpleDateFormat;
import java.util.Date;


class BaseViewModel extends BaseObservable {

    @SuppressLint("SimpleDateFormat")
    String changeTimeFormat(int time) {
        return new SimpleDateFormat("m:ss").format(new Date(time));
    }

}
