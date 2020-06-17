package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.view.animation.Animation;
import android.widget.TextView;

public class pb_animation extends Animation{
    private Context context;
    private ProgressBar progressBar;
    private float from;
    private float to;
    private TextView textView;

    public pb_animation(Context context,ProgressBar progressBar,TextView textView,float from, float to){
        this.context=context;
        this.progressBar=progressBar;
        this.from=from;
        this.to=to;
        this.textView = textView;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to-from)* interpolatedTime;
        progressBar.setProgress((int)value);
        textView.setText((int)value+" %");
        if (value == to) {
            context.startActivity(new Intent(context, login_activity.class));
        }
        }
    }

