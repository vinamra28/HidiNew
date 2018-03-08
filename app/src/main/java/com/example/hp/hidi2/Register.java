package com.example.hp.hidi2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class Register extends AppCompatActivity {
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = findViewById(R.id.registry);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(Register.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.0, 1);
                myAnim.setInterpolator(interpolator);
                register.startAnimation(myAnim);
            }
        });
    }
}
