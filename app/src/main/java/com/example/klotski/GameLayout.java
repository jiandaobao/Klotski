package com.example.klotski;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class GameLayout extends ViewGroup {
    private String level;
    private int blockWidth;
    private int blockHeight;
    private boolean[] isFree;

    public GameLayout(Context context) {
        super(context);
        level = ((GameActivity) context).get_level();
        isFree = new boolean[20];
        for (int i = 0; i < 20 ; i++) {
            isFree[i] = true;
        }
    }

    public GameLayout(Context context, AttributeSet attr) {
        super(context, attr);
        level = ((GameActivity) context).get_level();
        isFree = new boolean[20];
        for (int i = 0; i < 20 ; i++) {
            isFree[i] = true;
        }
    }

    public boolean checkFree(int i)
    {
        return isFree[i];
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int[] positions = getResources().getIntArray(getResources().getIdentifier(
                level,"array","com.example.klotski" ));
        int height = getHeight();
        int width = getWidth();
        blockWidth = width / 4;
        blockHeight = height / 5;
        int blockNumX, blockNumY, childWidth, childHeight, X , Y;

        View caocao = getChildAt(0);

        for (int i = 0; i < 10; i ++) {
            View child = getChildAt(i);
            if (i == 0) {
                blockNumX = 2;
                blockNumY = 2;
            } else if (i > 5) {
                blockNumX = 1;
                blockNumY = 1;
            } else {
                if (positions[i] / 100 % 10 == 1) {
                    blockNumX = 2;
                    blockNumY = 1;
                } else {
                    blockNumX = 1;
                    blockNumY = 2;
                }
            }
            X = positions[i] % 100 / 10;
            Y = positions[i] % 10;
            child.layout(X * blockWidth, Y * blockHeight,
                    blockWidth * (X + blockNumX), (Y + blockNumY) * blockHeight);
            int index = X + Y *4;
            ((MoveImageView) child).init(this, index, blockNumX, blockNumY);
            setFree(index, blockNumX, blockNumY,false);

        }
    }

    public void setFree (int index, int blockNumX, int blockNumY, boolean free) {
        for (int k = 0; k < blockNumY; k++) {
            index += k * 4;
            for (int j = 0; j < blockNumX; j++) {
                isFree[index + j] = free;
            }
        }
    }
    public int getBlockWidth() {
        return blockWidth;
    }
    public int getBlockHeight(){
        return blockHeight;
    }

//
//    @Override
//    public View getChildAt(int index) {
//        return super.getChildAt(index);
//    }
}
