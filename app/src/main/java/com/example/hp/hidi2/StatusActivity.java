package com.example.hp.hidi2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class StatusActivity extends AppCompatActivity
{
    RelativeLayout backgroundLayout;
    ImageView colorBack,fontText;
    EditText status;
    int t=1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        final int[] backColors = getResources().getIntArray(R.array.back);
        backgroundLayout=(RelativeLayout)findViewById(R.id.background);
        status=(EditText)findViewById(R.id.text);
        backgroundLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        colorBack=(ImageView)findViewById(R.id.color);


        colorBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                backgroundLayout.setBackgroundColor(backColors[t]);
                t++;
                if(t==backColors.length)t=0;
            }
        });
    }
}
