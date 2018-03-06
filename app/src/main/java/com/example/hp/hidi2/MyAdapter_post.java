package com.example.hp.hidi2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by asus on 06-03-2018.
 */

public class MyAdapter_post extends RecyclerView.Adapter<MyAdapter_post.MyViewHolder> {
    private Context context;
    private List<PostGet> postList;

    public MyAdapter_post(Context context, List<PostGet> postList) {
        this.context = context;
        this.postList = postList;
    }

    public MyAdapter_post(Context context) {
        this.context = context;
    }

    public MyAdapter_post(List<PostGet> postList) {
        this.postList = postList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_design, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PostGet post = postList.get(position);
        holder.user_dp.setImageDrawable(post.getUser_dp());
        holder.user_name.setText(post.getUser_name());
        holder.post_location.setText(post.getPost_location());
        holder.total_favours.setText(post.getTotal_favours());
        holder.do_arguments.setText(post.getDo_arguments());
        holder.image_posted.setImageDrawable(post.getUser_post_image());
        holder.do_like.setImageDrawable(post.getDo_like());
        holder.do_dislike.setImageDrawable(post.getDo_dislike());
        holder.do_arguments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Arguments.class);
                v.getContext().startActivity(intent);
//                Intent intent = new Intent(context,Argument.class);
//                context.startActivity(intent);
            }
        });
    }

//    PopupWindow pwindo;
//
//    private void initiatePopUpWindow(View v,int pos) {
//        try {
//
//// We need to get the instance of the LayoutInflater
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View layout = inflater.inflate(R.layout.popup_layout, null);
//            pwindo = new PopupWindow(layout,ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            pwindo.setBackgroundDrawable(new BitmapDrawable());
//            pwindo.setFocusable(true);
//            pwindo.setOutsideTouchable(true);
//            pwindo.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    //TODO do sth here on dismiss
//                }
//            });
//            pwindo.showAsDropDown(v, v.getScrollX(), v.getScrollY());
////            pwindo = new PopupWindow(layout, 300, 370, true);
////            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
//
////                    btnClosePopup = (Button) layout.findViewById(R.id.btn_close_popup);
////                    btnClosePopup.setOnClickListener(cancel_button_click_listener);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView user_name, post_location, post_content_text, total_favours, total_arguments, do_arguments;
        public ImageView user_dp, image_posted, do_like, do_dislike;

        public MyViewHolder(View itemView) {
            super(itemView);
            user_dp = itemView.findViewById(R.id.user_dp);
            user_name = itemView.findViewById(R.id.user_name);
            post_location = itemView.findViewById(R.id.post_location);
            total_favours = itemView.findViewById(R.id.favours_number);
            image_posted = itemView.findViewById(R.id.image_posted);
            do_arguments = itemView.findViewById(R.id.do_arguments);
            do_like = itemView.findViewById(R.id.like);
            do_dislike = itemView.findViewById(R.id.dislike);


        }


        private void initiatePopUpWindow() {

        }
    }
}
