package com.hiccup.kidpainting.training.manager;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;

import com.hiccup.kidpainting.common.PaintingConstants;
import com.hiccup.kidpainting.utilities.AnimatorUtils;

/**
 * Created by binh.pd on 10/23/2015.
 */
public class SpeakingManager extends TrainingManager {

    private int LEARN_COLOR = 11;
    private static final int ANMATION_IN = 0;
    private static final int ANMATION_OUT = 1;
    /**
     * lesson what color will be playing in the current section
     */
    private int mCurrentLesson = -1;

    private Context context;
    /**
     * coordinates that view will translate animation to
     */
    private float xTranslate, yTranslate;

    /**
     * executed animation in/ out simultaneously
     */
    AnimationSet animationSet;

    /**
     * list ob UI will be execute animation
     */
    private ArrayList<View> listsColorUI;
    /**
     * all lessons
     */
    private int totalLesson;
    private boolean isOpen = true;
    /**
     * save start coordinate of image color
     */
    private float startY;
    private float startX;

    /**
     * save start coordinate of image color
     */
    private float backY;
    private float backX;

    // screen size
    private int mWidthSreen;
    private int mHeightScreen;

    //check animation completed
    private boolean aniShowRunning = false;
    private boolean aniHideRunning = false;

    public SpeakingManager(Context context) {
        this.context = context;
    }

    @Override
    public void startLesson() {
        mCurrentLesson = 0;
        showLevelAnimation(listsColorUI.get(mCurrentLesson));
    }

    @Override
    public void nextLesson() {
        if (mCurrentLesson < totalLesson-1) {
            mCurrentLesson++;
            if (mCurrentLesson == 0) {
                showLevelAnimation(listsColorUI.get(mCurrentLesson));
            } else if (mCurrentLesson > 0 && mCurrentLesson <= (totalLesson - 1)) {
                hideLevelAnimation(listsColorUI.get(mCurrentLesson - 1));
                showLevelAnimation(listsColorUI.get(mCurrentLesson));
            }
        }
    }

    @Override
    public void forwardLesson() {
        if (mCurrentLesson > 0) {
            mCurrentLesson--;
            hideLevelAnimation(listsColorUI.get(mCurrentLesson+1));
            showLevelAnimation(listsColorUI.get(mCurrentLesson));
        }
    }

    /**
     * color is showing is animation moved in
     * next color will run animation moved out
     */
    public void playLesson() {
        if (isOpen == true) {
            if (mCurrentLesson > 0) {
                hideLevelAnimation(listsColorUI.get(mCurrentLesson - 1));
                showLevelAnimation(listsColorUI.get(mCurrentLesson));
            } else {
                showLevelAnimation(listsColorUI.get(mCurrentLesson));
            }
        } else {
            hideLevelAnimation(listsColorUI.get(mCurrentLesson));
        }
        isOpen = !isOpen;
    }

    /**
     * set list ob UI interact with user
     */
    public void setListUIAnimation(ArrayList<View> listsColorUI) {
        this.listsColorUI = listsColorUI;
        this.totalLesson = listsColorUI.size();
    }

    /**
     *
     */
    private void showLevelAnimation(final View view) {
        int[] location = new int[2];
        //float moveX = 0;
        view.getLocationOnScreen(location);
        final int width = view.getMeasuredWidth();
        final int height = view.getMeasuredHeight();

        float dx = location[0];
        float dy = location[1];

        startX = dx;
        startY = dy;

        //keep color move bone in the screen
        /*if (startX > (mWidthSreen/2 - 200))
            moveX = mWidthSreen - startX - 270;
        else
            moveX = mWidthSreen/2;*/

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 5.0f, 1.0f, 5.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(PaintingConstants.DURATION_ANIMATION_SHOW_COLOR);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, (mWidthSreen/2 - startX), 0, (mHeightScreen/2 - startY));
        translateAnimation.setDuration(PaintingConstants.DURATION_ANIMATION_SHOW_COLOR);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation.setDuration(PaintingConstants.DURATION_ANIMATION_SHOW_COLOR - 300);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                aniShowRunning = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setScaleX(5.0f);
                view.setScaleY(5.0f);
                view.setX(mWidthSreen/2);
                view.setY(mHeightScreen/2);
                view.setAlpha(1.0f);
                aniShowRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animationSet);
    }

    /**
     *
     */
    private void hideLevelAnimation(final View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        float moveX;
        final int width = view.getMeasuredWidth();
        final int height = view.getMeasuredHeight();

        float dx = location[0];
        float dy = location[1];

        backX = startX;
        backY = startY;

        //keep color move bone in the screen
        if (backX > (mWidthSreen/2 - 250))
            moveX = mWidthSreen - backX - 200;
        else
            moveX = mWidthSreen/2;

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.2f, 1.0f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(PaintingConstants.DURATION_ANIMATION_SHOW_COLOR);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, -(xTranslate - moveX), 0, -(yTranslate - backY));
        translateAnimation.setDuration(PaintingConstants.DURATION_ANIMATION_SHOW_COLOR);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(PaintingConstants.DURATION_ANIMATION_SHOW_COLOR);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                aniHideRunning = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
                view.setX(backX);
                view.setY(backY);
                view.setAlpha(1.0f);
                aniHideRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animationSet);
    }

    private Animator createOutAnimation(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        float dx = location[0];
        float dy = location[1];
        view.setScaleX(2);
        view.setScaleY(2);
        view.setTranslationX(dx);
        view.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                view,
                AnimatorUtils.scaleX(2),
                AnimatorUtils.scaleY(2)
//                AnimatorUtils.translationX(dx, 600),
//                AnimatorUtils.translationY(dy, 600)
        );

        return anim;
    }

    private Animator createInAnimation(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        float dx = location[0];
        float dy = location[1];
        view.setScaleX(1f);
        view.setScaleY(1f);
        view.setTranslationX(dx);
        view.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                view,
//                AnimatorUtils.scaleX(0.5f),
//                AnimatorUtils.scaleY(0.5f),
                AnimatorUtils.translationX(dx, 0),
                AnimatorUtils.translationY(dy, 0)
        );

        return anim;
    }

    /**
     * level A will be out, level B will be in
     */
    public void startAnimationInOut(int levelA, int levelB) {
//        showLevelAnimation(levelA);
//        hideLevelAnimation(levelB);
    }

    public float getxTranslate() {
        return xTranslate;
    }

    public void setxTranslate(float xTranslate) {
        this.xTranslate = xTranslate;
    }

    public float getyTranslate() {
        return yTranslate;
    }

    public void setyTranslate(float yTranslate) {
        this.yTranslate = yTranslate;
    }

    public void setScreenSize(int width, int height) {
        mWidthSreen = width;
        mHeightScreen = height;
    }

    public boolean isAniShowRunning(){
        return aniShowRunning;
    }

    public boolean isAniHideRunning() {
        return aniHideRunning;
    }
}
