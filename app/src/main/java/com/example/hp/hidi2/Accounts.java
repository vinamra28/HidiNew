package com.example.hp.hidi2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;

public class Accounts extends AppCompatActivity
{
    float x1, x2, y1, y2;
    CircleProgress progress;
    ImageView imageView_plus,userdp;
    TextView admire,love,visitors,hidies,blocks;
    Button see_notifications,my_journey;
    static int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        userdp = findViewById(R.id.profilepic);
        progress=findViewById(R.id.popularProgress);
        admire=findViewById(R.id.admireCount);
        love=findViewById(R.id.loveCount);
        visitors=findViewById(R.id.visitorsCount);
        hidies=findViewById(R.id.hidiCount);
        blocks=findViewById(R.id.blockCount);
        see_notifications=findViewById(R.id.notificationButton);
        imageView_plus = findViewById(R.id.statusButton);
        my_journey=findViewById(R.id.journeyButton);
        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.index);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), myBitmap);
        roundedBitmapDrawable.setCornerRadius(55f);
        userdp.setImageDrawable(roundedBitmapDrawable);
        imageView_plus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Accounts.this,StatusActivity.class);
                startActivity(intent);
            }
        });
        userdp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showFileChooser();
            }
        });
    }
    private void showFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.notification)
        {
        }
        if (id == R.id.night)
        {
        }
        if (id == R.id.logout)
        {
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                if (x1 > x2)
                {
                    Intent intent = new Intent(Accounts.this, PostActivity.class);
                    startActivity(intent);
                }
                break;
            default:System.out.println();
        }
        return false;
    }
}



//for later use

//    private PopupWindow popupWindow;
//
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
