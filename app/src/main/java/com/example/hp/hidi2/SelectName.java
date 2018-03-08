package com.example.hp.hidi2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SelectName extends AppCompatActivity {
    Spinner spinner;
    Button nextt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_name);
        nextt = findViewById(R.id.next);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.usernames, android.R.layout.select_dialog_item);
        spinner.setAdapter(adapter);
        nextt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(SelectName.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.0, 1);
                myAnim.setInterpolator(interpolator);
                nextt.startAnimation(myAnim);
                Intent intent = new Intent(SelectName.this, PostActivity.class);
                startActivity(intent);
            }
        });
    }
}
