package com.example.hp.hidi2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SelectTags extends AppCompatActivity
{
    Button button_arts,button_asia,button_australia,button_beauty,button_books,button_bussiness,button_carrees,button_cars,button_edu,button_europe;
    Button button_fitness,button_food,button_fun,button_games,button_health,button_it,button_math,button_add,button_law;
    ImageView tagging;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tags);
        button_arts = findViewById(R.id.arts);
        button_asia = findViewById(R.id.asia);
        button_australia = findViewById(R.id.australia);
        button_beauty = findViewById(R.id.beauty);
        button_books = findViewById(R.id.books);
        button_bussiness = findViewById(R.id.bussinesss);
        button_carrees = findViewById(R.id.carreers);
        button_cars = findViewById(R.id.cartransport);
        button_edu = findViewById(R.id.education);
        button_europe = findViewById(R.id.europe);
        button_fitness = findViewById(R.id.fitness);
        button_food = findViewById(R.id.fooddrink);
        button_fun = findViewById(R.id.fun);
        button_games = findViewById(R.id.games);
        button_health = findViewById(R.id.health);
        button_it = findViewById(R.id.it);
        button_math = findViewById(R.id.mathematics);
        button_add = findViewById(R.id.adventure);
        button_law =findViewById(R.id.law);
        tagging=findViewById(R.id.addTags);
        tagging.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(SelectTags.this,PostActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        button_arts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_arts.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_asia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_asia.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_australia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_australia.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_beauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_beauty.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_books.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_bussiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_bussiness.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_carrees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_carrees.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_cars.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_edu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_edu.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_europe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_europe.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_fitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_fitness.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_food.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_fun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_fun.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_games.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_health.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_it.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_math.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_add.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
        button_law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_law.setBackgroundResource(R.drawable.circular_round_click);
            }
        });
    }
}
