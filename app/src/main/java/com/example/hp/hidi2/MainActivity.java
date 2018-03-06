package com.example.hp.hidi2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    TextView registering,recover;
    Button get;
    ImageButton google,fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registering=findViewById(R.id.creating);
        recover=findViewById(R.id.recovering);
        get=findViewById(R.id.getOtp);
        google=findViewById(R.id.gplusButton);
        fb=findViewById(R.id.fbButton);
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.google);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), myBitmap);
        roundedBitmapDrawable.setCornerRadius(125f);
        fb.setImageDrawable(roundedBitmapDrawable);
        Bitmap myBitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.googlesignin);
        RoundedBitmapDrawable roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(getResources(), myBitmap1);
        roundedBitmapDrawable1.setCornerRadius(125f);
        google.setImageDrawable(roundedBitmapDrawable1);
        registering.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });
        recover.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity.this,Recover.class);
                startActivity(intent);
            }
        });
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.0, 1);
                myAnim.setInterpolator(interpolator);
                get.startAnimation(myAnim);
                Intent intent=new Intent(MainActivity.this,VerifyOtp.class);
                startActivity(intent);
            }
        });
        fb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.0, 1);
                myAnim.setInterpolator(interpolator);
                fb.startAnimation(myAnim);
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.0, 1);
                myAnim.setInterpolator(interpolator);
                google.startAnimation(myAnim);
            }
        });
    }
}
