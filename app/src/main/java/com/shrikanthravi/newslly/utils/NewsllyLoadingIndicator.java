package com.shrikanthravi.newslly.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shrikanthravi.newslly.R;

import java.util.logging.Handler;

public class NewsllyLoadingIndicator extends LinearLayout {

    //Context
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    //Views
    protected TextView nTV,eTV,wTV,sTV,l1TV,l2TV,yTV;

    public NewsllyLoadingIndicator(Context context) {
        super(context);
    }


    int height;

    private class UpdateViewRunnable implements Runnable{
        public void run(){
            height = nTV.getHeight()/2;

            ObjectAnimator
                    .ofFloat(nTV, "rotation", 360,0)
                    .setDuration(500)
                    .start();
            ObjectAnimator
                    .ofFloat(nTV, "translationY", 0, -height, +height,0)
                    .setDuration(500)
                    .start();

            android.os.Handler handler = new android.os.Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator
                            .ofFloat(eTV, "rotation", 360,0)
                            .setDuration(500)
                            .start();
                    ObjectAnimator
                            .ofFloat(eTV, "translationY", 0, -height, +height,0)
                            .setDuration(500)
                            .start();

                }
            },150);


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator
                            .ofFloat(wTV, "rotation", 360,0)
                            .setDuration(500)
                            .start();
                    ObjectAnimator
                            .ofFloat(wTV, "translationY", 0, -height, +height,0)
                            .setDuration(500)
                            .start();
                }
            },300);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator
                            .ofFloat(sTV, "rotation", 360,0)
                            .setDuration(500)
                            .start();
                    ObjectAnimator
                            .ofFloat(sTV, "translationY", 0, -height, +height,0)
                            .setDuration(500)
                            .start();
                }
            },450);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator
                            .ofFloat(l1TV, "rotation", 360,0)
                            .setDuration(500)
                            .start();
                    ObjectAnimator
                            .ofFloat(l1TV, "translationY", 0, -height, +height,0)
                            .setDuration(500)
                            .start();
                }
            },600);


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator
                            .ofFloat(l2TV, "rotation", 360,0)
                            .setDuration(500)
                            .start();
                    ObjectAnimator
                            .ofFloat(l2TV, "translationY", 0, -height, +height,0)
                            .setDuration(500)
                            .start();
                }
            },750);


            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator
                            .ofFloat(yTV, "rotation", 360,0)
                            .setDuration(500)
                            .start();
                    ObjectAnimator
                            .ofFloat(yTV, "translationY", 0, -height, +height,0)
                            .setDuration(500)
                            .start();
                }
            },900);









            if(updateView){
                postDelayed(this,1400);
            }
        }
    }
    private boolean updateView=false;
    private UpdateViewRunnable updateViewRunnable = new UpdateViewRunnable();

    public NewsllyLoadingIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        //Load RootView from xml
        View rootView = mLayoutInflater.inflate(R.layout.newslly_loading_view, this, true);
        nTV = rootView.findViewById(R.id.nTV);
        eTV = rootView.findViewById(R.id.eTV);
        wTV = rootView.findViewById(R.id.wTV);
        sTV = rootView.findViewById(R.id.sTV);
        l1TV = rootView.findViewById(R.id.l1TV);
        l2TV = rootView.findViewById(R.id.l2TV);
        yTV = rootView.findViewById(R.id.yTV);

        ObjectAnimator
                .ofFloat(nTV, "rotation", 360,0)
                .setDuration(500)
                .start();
        ObjectAnimator
                .ofFloat(nTV, "translationY", 0, -height, +height,0)
                .setDuration(500)
                .start();

        android.os.Handler handler = new android.os.Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator
                        .ofFloat(eTV, "rotation", 360,0)
                        .setDuration(500)
                        .start();
                ObjectAnimator
                        .ofFloat(eTV, "translationY", 0, -height, +height,0)
                        .setDuration(500)
                        .start();

            }
        },150);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator
                        .ofFloat(wTV, "rotation", 360,0)
                        .setDuration(500)
                        .start();
                ObjectAnimator
                        .ofFloat(wTV, "translationY", 0, -height, +height,0)
                        .setDuration(500)
                        .start();
            }
        },300);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator
                        .ofFloat(sTV, "rotation", 360,0)
                        .setDuration(500)
                        .start();
                ObjectAnimator
                        .ofFloat(sTV, "translationY", 0, -height, +height,0)
                        .setDuration(500)
                        .start();
            }
        },450);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator
                        .ofFloat(l1TV, "rotation", 360,0)
                        .setDuration(500)
                        .start();
                ObjectAnimator
                        .ofFloat(l1TV, "translationY", 0, -height, +height,0)
                        .setDuration(500)
                        .start();
            }
        },600);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator
                        .ofFloat(l2TV, "rotation", 360,0)
                        .setDuration(500)
                        .start();
                ObjectAnimator
                        .ofFloat(l2TV, "translationY", 0, -height, +height,0)
                        .setDuration(500)
                        .start();
            }
        },750);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator
                        .ofFloat(yTV, "rotation", 360,0)
                        .setDuration(500)
                        .start();
                ObjectAnimator
                        .ofFloat(yTV, "translationY", 0, -height, +height,0)
                        .setDuration(500)
                        .start();
            }
        },900);


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateView=true;
        postDelayed(updateViewRunnable,1400);
    }
}
