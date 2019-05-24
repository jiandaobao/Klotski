package com.example.klotski;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.jar.Attributes;

public class MoveImageView extends android.support.v7.widget.AppCompatImageView {
    float moveX;
    float moveY;

    public MoveImageView(Context context) {
        super(context);
    }
    public MoveImageView(Context context, AttributeSet attr) {
        super(context, attr);
    }
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveX = event.getX();
                moveY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                setTranslationX(getX() - getLeft() + (event.getX() - moveX));
                setTranslationY(getY() - getTop() + (event.getY() - moveY));
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }
}
