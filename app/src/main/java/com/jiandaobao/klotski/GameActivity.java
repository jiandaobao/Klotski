package com.jiandaobao.klotski;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    private GameLayout gameLayout;
    private TextView title;
    private int level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        level = getIntent().getIntExtra(CheckPoints.EXTRA_INT, 0);
        if (level >= getResources().getStringArray(R.array.layouts).length)
            level = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameLayout = findViewById(R.id.game_layout);
        title = findViewById(R.id.text_checkpoint);
        title.setText(getResources().getStringArray(R.array.text_points)[level]);
    }
    public int get_level(){
        return this.level;
    }

    public void retract(View view) {
        gameLayout.retractStep();
//        Toast.makeText(this, "11111111", Toast.LENGTH_SHORT).show();
    }
    public void resetGame(View view) {
        this.recreate();
    }
    public void backStart(View view) {
        Intent intent =new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}
