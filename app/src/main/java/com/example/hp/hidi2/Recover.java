package com.example.hp.hidi2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class Recover extends AppCompatActivity {
    Button recovery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);
        recovery = findViewById(R.id.recovery);
        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final Animation myAnim = AnimationUtils.loadAnimation(Recover.this, R.anim.bounce);
//                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.0, 1);
//                myAnim.setInterpolator(interpolator);
//                recovery.startAnimation(myAnim);
            }
        });
    }
}
