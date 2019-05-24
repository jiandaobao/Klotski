package com.example.klotski;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class GameLayout extends ViewGroup {
    private String level;

    public GameLayout(Context context) {
        super(context);
        level = ((GameActivity) context).get_level();
    }

    public GameLayout(Context context, AttributeSet attr) {
        super(context, attr);
        level = ((GameActivity) context).get_level();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int[] positions = getResources().getIntArray(getResources().getIdentifier(
                level,"array","com.example.klotski" ));
        Log.d("ssssssssssssssssss", Integer.toString(positions[1]));
        int height = getHeight();
        int width = getWidth();
        int blockWidth = width / 4;
        int blockHeight = height / 5;
        int childWidth, childHeight, X , Y;

        View caocao = getChildAt(0);

        for (int i = 0; i < 10; i ++) {
            View child = getChildAt(i);
            if (i == 0) {
                childWidth = 2 * blockWidth;
                childHeight = 2 * blockHeight;
            } else if (i > 5) {
                childWidth = blockWidth;
                childHeight = blockHeight;
            } else {
                if (positions[i] / 100 % 10 == 1) {
                    childWidth = blockWidth * 2;
                    childHeight = blockHeight;
                } else {
                    childWidth = blockWidth;
                    childHeight = blockHeight * 2;
                }
            }
            X = positions[i] % 100 / 10 * blockWidth;
            Y = positions[i] % 10 * blockHeight;
            Log.d("ssssssssssssssssss", Integer.toString(X));
            Log.d("ssssssssssssssssss", Integer.toString(Y));
            Log.d("ssssssssssssssssss", Integer.toString(childWidth));
            Log.d("ssssssssssssssssss", Integer.toString(childHeight));
            Log.d("ssssssssssssssssss", "==========");
            child.layout(X, Y, X + childWidth, Y + childHeight );

        }
//        View child = getChildAt(0);
//        child.layout(positions[0] / 10 * childWidth, positions[0] % 10,
//                childWidth*2 + positions[0] / 10 * childWidth,
//                childHeight*2 + positions[0] % 10);
    }
//
//    @Override
//    public View getChildAt(int index) {
//        return super.getChildAt(index);
//    }
}
