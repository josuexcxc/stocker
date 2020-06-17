package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class screen_splash extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textView;
    private Typeface sf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_splash);
        progressBar = findViewById(R.id.p_bar);
        textView = findViewById(R.id.tv_pbar);


        String fuente1= "fonts/Hunters.otf";
        this.sf = Typeface.createFromAsset(getAssets(),fuente1);
        textView.setTypeface(sf);

        progressBar.setMax(100);
        progressBar.setScaleY(3f);
        pb_animations();
    }
    public void pb_animations(){
        pb_animation pbAnimation = new pb_animation(this,progressBar,textView,0f,100f);
        pbAnimation.setDuration(3000);
        progressBar.getProgressDrawable().setColorFilter(Color.rgb(141,119,10), PorterDuff.Mode.MULTIPLY);
        progressBar.setAnimation(pbAnimation);


    }
}
