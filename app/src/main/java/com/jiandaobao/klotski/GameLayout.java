package com.jiandaobao.klotski;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class GameLayout extends ViewGroup {
    private int level;
    private int blockWidth;
    private int blockHeight;
    private boolean[] isFree;
    private ArrayList<Step> stepList;
    private int stepCount;

    class Step{
        int index;
        boolean isX;
        int count;

        Step(int index, boolean isX, int count) {
            this.index = index;
            this.isX = isX;
            this.count = count;
        }
    }

    public GameLayout(Context context) {
        super(context);
        level = ((GameActivity) context).get_level();
        isFree = new boolean[20];
        for (int i = 0; i < 20 ; i++) {
            isFree[i] = true;
        }
        stepList = new ArrayList<>();
        stepCount = 0;
        setMotionEventSplittingEnabled(false);
    }

    public GameLayout(Context context, AttributeSet attr) {
        super(context, attr);
        level = ((GameActivity) context).get_level();
        isFree = new boolean[20];
        for (int i = 0; i < 20 ; i++) {
            isFree[i] = true;
        }
        stepList = new ArrayList<>();
        stepCount = 0;
        setMotionEventSplittingEnabled(false);
    }

    public boolean checkFree(int i)
    {
        return isFree[i];
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        String[] levels = getResources().getStringArray(R.array.layouts);
        int[] positions = getResources().getIntArray(getResources().getIdentifier(
                levels[level],"array","com.jiandaobao.klotski" ));

        int height = getHeight();
        int width = getWidth();
        blockWidth = width / 4;
        blockHeight = height / 5;
        int blockNumX, blockNumY, childWidth, childHeight, X , Y;
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
            ((MoveImageView) child).init(this, i, index, blockNumX, blockNumY);
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

    public void addStep(int index, boolean isX, int count) {
        this.stepList.add(new Step(index, isX, count));
        stepCount += count;
    }

    public void retractStep(){
        if (this.stepList.size() != 0) {
            Step temp = stepList.remove(stepList.size() - 1);
            MoveImageView child = (MoveImageView) getChildAt(temp.index);
            child.moveBack(temp.isX, temp.count);
            stepCount -= temp.count;
    }
    }

    public void clearLevel(){
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(this.getContext());
        myAlertBuilder.setTitle("恭喜通关");
        myAlertBuilder.setMessage(String.format("您帮助曹操走出了华容道！共花了%d步！", stepCount));
        myAlertBuilder.setPositiveButton("进入下一关", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent =new Intent(getContext(), GameActivity.class);
                intent.putExtra(CheckPoints.EXTRA_INT, level + 1);
                getContext().startActivity(intent);
            }
        });
        myAlertBuilder.setNegativeButton("返回主菜单", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent =new Intent(getContext(), StartActivity.class);
                getContext().startActivity(intent);
            }
        });
        myAlertBuilder.show();
    }
}
