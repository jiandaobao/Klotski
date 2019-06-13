package com.example.klotski;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViewById(R.id.start_button).layout(500,500,500,500);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(CheckPoints.EXTRA_INT, 1);
        startActivity(intent);
    }


    public void toCheckPoints(View view) {
        Intent intent = new Intent(this, CheckPoints.class);
        startActivity(intent);
    }

    public void toHelpInfo(View view) {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }
}
