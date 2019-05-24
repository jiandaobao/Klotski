package com.example.klotski;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {
    private MoveImageView imageView;
    private String level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        level = "bingfensanlu";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
//        imageView = findViewById(R.id.test);
//        imageView.setImageResource(R.drawable.test);
    }
    public String get_level(){
        return this.level;
    }
}
