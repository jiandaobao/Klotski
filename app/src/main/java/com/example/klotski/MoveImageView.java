package com.example.klotski;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.jar.Attributes;

public class MoveImageView extends android.support.v7.widget.AppCompatImageView {
    // 棋子未移动是位置
    private float localX;
    private float localY;
    // 棋子触摸事件开始的位置
    private float moveX;
    private float moveY;
    // 棋子长宽格子数
    private int blockNumX;
    private int blockNumY;
    // 父布局
    private GameLayout parent;
    // 在布局中的编号
    private int index;
    // 棋子移动状态
    public enum Direction {
        X,
        Y,
        UP,
        DOWN,
        LEFT,
        RIGHT,
        STATIC
    };
    // 当前棋子移动状态
    private Direction direction;
    // 棋子在当前移动方向(XY方向)的移动范围
    private int maxMove;
    private int minMove;
    // 棋子距离原位置的距离
    private int distance;

    public MoveImageView(Context context) {
        super(context);
    }

    public MoveImageView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public void init(GameLayout parent, int index, int blockNumX, int blockNumY) {
        this.parent = parent;
        this.index = index;
        this.blockNumX = blockNumX;
        this.blockNumY = blockNumY;
        this.direction = Direction.STATIC;
        this.distance = 0;
        this.localX = getLeft();
        this.localY = getTop();
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveX = event.getX();
                moveY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float distanceX = event.getX() - moveX;
                float distanceY = event.getY() - moveY;
                switch (this.direction) {
                    case X:
                        movInX(distanceX);
                        break;
                    case Y:
                        movInY(distanceY);
                        break;
                    default:
                        if (Math.abs(distanceX) > Math.abs(distanceY)) {
                            maxMove = canMove(Direction.RIGHT) * parent.getBlockWidth();
                            minMove = - canMove(Direction.LEFT) * parent.getBlockWidth();
                            direction = Direction.X;
                            movInX(distanceX);
                        } else {
                            maxMove = canMove(Direction.DOWN) * parent.getBlockHeight();
                            minMove = - canMove(Direction.UP) * parent.getBlockHeight();
                            direction = Direction.Y;
                            movInY(distanceY);
                        }
                }
                break;
            case MotionEvent.ACTION_UP:
                putChessman(direction);
                distance = 0;
                direction = Direction.STATIC;
                break;

            case MotionEvent.ACTION_CANCEL:
                setX(localX);
                setY(localY);
                distance = 0;
                direction = Direction.STATIC;
                break;
        }
        return true;
    }

    // 确定落子最后的位置
    private void putChessman(Direction direction) {
        switch (direction) {
            case X:
                float localX_new = Math.round(getX() + 0.5 * parent.getBlockWidth()) /
                        parent.getBlockWidth();
                // TODO
                parent.setFree(index, blockNumX, blockNumY, true);
                index = (int) localX_new + index / 4 * 4;
                parent.setFree(index, blockNumX, blockNumY, false);
                localX = localX_new * parent.getBlockWidth();
                setX(localX);
                break;
            case Y:
                float localY_new = Math.round(getY() + 0.5 * parent.getBlockHeight()) /
                        parent.getBlockHeight();
                parent.setFree(index, blockNumX, blockNumY, true);
                index = (int) localY_new * 4 + index % 4;
                parent.setFree(index, blockNumX, blockNumY, false);
                localY = localY_new * parent.getBlockHeight();
                setY(localY);
                break;
            default:
                break;
        }
    }

    // 在X方向上的移动状态处理
    private void movInX(float distanceX) {
        if (this.distance + distanceX >= maxMove) {
            setX(localX + maxMove);
            this.distance = maxMove;
        } else if (this.distance + distanceX <= minMove) {
            setX(localX + minMove);
            this.distance = minMove;
        } else {
            setTranslationX(getX() - getLeft() + distanceX);
            this.distance += distanceX;
        }
    }

    // 在Y方向的移动状态处理
    private void  movInY(float distanceY) {
        if (this.distance + distanceY >= maxMove) {
            setY(localY + maxMove);
            this.distance = maxMove;
        } else if (this.distance + distanceY <= minMove) {
            setY(localY + minMove);
            this.distance = minMove;
        } else {
            setTranslationY(getY() - getTop() + distanceY);
            this.distance += distanceY;
        }
    }

    // 向父布局请求在当前移动方向下能否移动 能移动几格
    private int canMove(Direction direction) {
        int canMovNum = 0;
        boolean canMove = true;
        int temp2 = index;
        int temp;
        switch (direction){
            case RIGHT:
                while (canMove) {
                    temp = temp2;
                    for (int i = 0; i < this.blockNumY; temp += 4, i++) {
                        int checkNum = temp + this.blockNumX;
                        if ((checkNum - 1) % 4 == 3) {
                            canMove = false;
                            break;
                        }
                        else
                            canMove = canMove & parent.checkFree(checkNum);
                    }
                    if (canMove)
                        canMovNum += 1;
                    temp2 += 1;
                }
                break;
            case LEFT:
                while (canMove) {
                    temp = temp2;
                    for (int i = 0; i < this.blockNumY; temp += 4, i++) {
                        if (temp % 4 == 0) {
                            canMove = false;
                            break;
                        }
                        else
                            canMove = canMove & parent.checkFree(temp - 1);
                    }
                    if (canMove)
                        canMovNum += 1;
                    temp2 -= 1;
                }
                break;
            case DOWN:
                while (canMove) {
                    temp = temp2;
                    for (int i = 0; i < this.blockNumX; temp++, i++) {
                        int checkNum = temp + 4 * blockNumY;
                        if (checkNum > 19) {
                            canMove = false;
                            break;
                        }
                        else
                            canMove = canMove & parent.checkFree(checkNum);
                    }
                    if (canMove)
                        canMovNum += 1;
                    temp2 += 4;
                }
                break;
            case UP:
                while (canMove) {
                    temp = temp2;
                    for (int i = 0; i < this.blockNumX; temp++, i++) {
                        if (temp < 4) {
                            canMove = false;
                            break;
                        }
                        else
                            canMove = canMove & parent.checkFree(temp - 4);
                    }
                    if (canMove)
                        canMovNum += 1;
                    temp2 -= 4;
                }
                break;
            default:
                break;
        }
        return canMovNum;
    }
}
