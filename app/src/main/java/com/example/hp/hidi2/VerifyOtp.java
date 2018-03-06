package com.example.hp.hidi2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class VerifyOtp extends AppCompatActivity
{
    EditText n1,n2,n3,n4,n5,n6;
    Button log,verifi;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        n1=findViewById(R.id.no1);
        n2=findViewById(R.id.no2);
        n3=findViewById(R.id.no3);
        n4=findViewById(R.id.no4);
        n5=findViewById(R.id.no5);
        n6=findViewById(R.id.no6);
        n1.addTextChangedListener(new GenericTextWatcher(n1));
        n2.addTextChangedListener(new GenericTextWatcher(n2));
        n3.addTextChangedListener(new GenericTextWatcher(n3));
        n4.addTextChangedListener(new GenericTextWatcher(n4));
        n5.addTextChangedListener(new GenericTextWatcher(n5));
        n6.addTextChangedListener(new GenericTextWatcher(n6));
        log=findViewById(R.id.login);
        log.setVisibility(View.INVISIBLE);
        verifi=findViewById(R.id.verify);
        verifi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Animation myAnim = AnimationUtils.loadAnimation(VerifyOtp.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.0, 1);
                myAnim.setInterpolator(interpolator);
                verifi.startAnimation(myAnim);
                log.setVisibility(View.VISIBLE);
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Animation myAnim = AnimationUtils.loadAnimation(VerifyOtp.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.0, 1);
                myAnim.setInterpolator(interpolator);
                log.startAnimation(myAnim);
                Intent intent=new Intent(VerifyOtp.this,SelectName.class);
                startActivity(intent);
            }
        });
    }
    public class GenericTextWatcher implements TextWatcher
    {
        private View view;
        public GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.no1:
                    if(text.length()==1)
                        n2.requestFocus();
                    break;
                case R.id.no2:
                    if(text.length()==1)
                        n3.requestFocus();
                    break;
                case R.id.no3:
                    if(text.length()==1)
                        n4.requestFocus();
                    break;
                case R.id.no4:
                    if(text.length()==1)
                        n5.requestFocus();
                    break;
                case R.id.no5:
                    if(text.length()==1)
                        n6.requestFocus();
                    break;
                case R.id.no6:
                    if(text.length()==1)
                        verifi.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
}
