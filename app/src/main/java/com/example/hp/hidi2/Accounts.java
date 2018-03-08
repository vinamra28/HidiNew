package com.example.hp.hidi2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Accounts extends AppCompatActivity {
    float x1, x2, y1, y2;
    ProgressBar progressBar;
    ImageView imageView_plus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        ImageView myImage = (ImageView) findViewById(R.id.image);
        //imageView_plus = (ImageView) findViewById(R.id.plus_btn);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(50);
        progressBar.setMax(100);
//        progressBar.
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.index);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), myBitmap);
        roundedBitmapDrawable.setCornerRadius(55f);
        myImage.setImageDrawable(roundedBitmapDrawable);

        // pop up of text and camera
//        imageView_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initiatepopupWindow();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.loc) {

        }
        if (id == R.id.pers) {

        }
        if (id == R.id.my) {

        }
        return super.onOptionsItemSelected(item);
    }

    private PopupWindow popupWindow;

//    private void initiatepopupWindow() {
//        try {
//            //We need to get the instance of the LayoutInflater, use the context of this activity
//            LayoutInflater inflater = (LayoutInflater) Accounts.this
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            //Inflate the view from a predefined XML layout
//            View layout = inflater.inflate(R.layout.pop_up_on_plus, (ViewGroup) findViewById(R.id.pop_up_on_plus));
//            // create a 300px width and 470px height PopupWindow
//            popupWindow = new PopupWindow(layout, 400, 300, true);
//            // display the popup in the center
//            popupWindow.showAtLocation(layout, Gravity.CENTER_HORIZONTAL, 50, 580);
//
//            TextView textView_text = (TextView) layout.findViewById(R.id.text_text);
//            ImageButton imageButton_text = (ImageButton) layout.findViewById(R.id.text_img);
//            TextView textView_camera = (TextView) layout.findViewById(R.id.text_camera);
//            ImageButton imageButton_camera = (ImageButton) layout.findViewById(R.id.text_camera);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                if (x1 > x2) {
                    Intent intent = new Intent(Accounts.this, PostActivity.class);
                    startActivity(intent);
                }
                break;
        }
        return false;
    }
}
