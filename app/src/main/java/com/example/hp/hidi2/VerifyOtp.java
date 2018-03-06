package com.example.hp.hidi2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VerifyOtp extends AppCompatActivity
{
    Button log,verifi;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        log=findViewById(R.id.login);
        log.setVisibility(View.INVISIBLE);
        verifi=findViewById(R.id.verify);
        verifi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                log.setVisibility(View.VISIBLE);
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VerifyOtp.this,SelectName.class);
                startActivity(intent);
            }
        });
    }
}
