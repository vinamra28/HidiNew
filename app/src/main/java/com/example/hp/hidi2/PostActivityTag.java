package com.example.hp.hidi2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class PostActivityTag extends AppCompatActivity
{

    MaterialSpinner materialSpinner;
    List<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tag);
        materialSpinner = (MaterialSpinner)findViewById(R.id.spinner);
        initItems();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialSpinner.setAdapter(adapter);
        materialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initItems() {
        list.add("Arts");
        list.add("Asia");
        list.add("Australia");
        list.add("Beauty&Style");
        list.add("Books");
        list.add("Bussiness");
        list.add("Careers");
        list.add("Cars&Transporatation");
        list.add("Education");
        list.add("Europe");
        list.add("Fitness");
        list.add("Food&Drink");
        list.add("Fun&Humor");
        list.add("Games");
        list.add("HealthCare");
        list.add("IT&Programming");
        list.add("Mathematics");
        list.add("Adventure");
        list.add("Fun");
        list.add("Law&Legal");
    }
}
