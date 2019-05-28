package com.ASUPEACLab.safetyapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;

public class CustomPopup extends Activity {

    Bitmap backgroundScreenShot;
    View popupView, popupWindow;
    String titleText, contentText, buttonText;
    int showAniDuration, hideAniDuration;

    public CustomPopup(String titleText, String contentText, String buttonText){
        popupView = findViewById(R.id.viewAlert);
        popupWindow = findViewById(R.id.viewAlertWindow);
        popupView.setVisibility(View.INVISIBLE);
        showAniDuration = 250;
        hideAniDuration = 100;
        this.titleText = titleText;
        this.contentText = contentText;
        this.buttonText = buttonText;
    }

    public void showView(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            takeScreenshot();
            codeViewShowAnimation(true, showAniDuration);
            }
        });
    }

    public void hideView(){
        codeViewShowAnimation(false, hideAniDuration);
    }

    public void setShowAniDuration(int duration){
        showAniDuration = duration;
    }

    public void buttonClicked(View view) {
        buttonDismissViewClicked(popupWindow);
    }

    public void buttonDismissViewClicked(View view) {
        hideKeyboard(view);
        popupWindow.setClickable(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() { codeViewShowAnimation(false, hideAniDuration); }
        });
    }

    private void hideKeyboard(View v) {
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0); }
    }


    private void takeScreenshot() {
        try {
            View screen = getWindow().getDecorView().getRootView();
            screen.setDrawingCacheEnabled(true);
            backgroundScreenShot = Bitmap.createBitmap(screen.getDrawingCache()); }
        catch (Throwable e) {
            e.printStackTrace(); }
    }


    private void codeViewShowAnimation(Boolean show, int duration) {
        AnimationSet animations = new AnimationSet(false);
        animations.setRepeatMode(0);

        if (show) {
            popupView.setVisibility(View.VISIBLE);
            AlphaAnimation fadeInAni = new AlphaAnimation(0.0f, 1.0f);
            fadeInAni.setInterpolator(new AccelerateInterpolator());
            fadeInAni.setDuration(duration);
            animations.addAnimation(fadeInAni);

            Animation scaleInAni = new ScaleAnimation(0.97f, 1f, 0.97f, 1f, popupWindow.getWidth() / 2f, popupWindow.getHeight() / 2f);
            scaleInAni.setDuration(1000);
            scaleInAni.setInterpolator(new DecelerateInterpolator());
            popupWindow.startAnimation(scaleInAni); }
        else {
            AlphaAnimation fadeOutAni = new AlphaAnimation(1.0f, 0.0f);
            fadeOutAni.setInterpolator(new AccelerateInterpolator());
            fadeOutAni.setDuration(duration);
            fadeOutAni.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }

                @Override
                public void onAnimationEnd(Animation animation) {
                    popupView.setVisibility(View.INVISIBLE); }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
            animations.addAnimation(fadeOutAni); }
        popupView.startAnimation(animations);
    }
}
