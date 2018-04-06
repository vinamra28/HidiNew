package com.example.hp.hidi2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class ShowUnlockedVisitors extends AppCompatActivity
{
    RecyclerView recyclerView;
    UnlockedAdapter adapter;
    ArrayList<String> visitlist=new ArrayList<>();
    ArrayList<String> visitlist1=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_unlocked_visitors);
        Bundle bundle=getIntent().getExtras();
        visitlist.addAll(bundle.getStringArrayList("viewed"));
        recyclerView=findViewById(R.id.unlockedRecyclerView);
        adapter=new UnlockedAdapter(getApplicationContext(),visitlist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        for(int i=0;i<visitlist.size();i++)
        {
            visitlist1.add(visitlist.get(i));
        }
    }
}
