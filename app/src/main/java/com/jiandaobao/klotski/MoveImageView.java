package com.jiandaobao.klotski;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MoveImageView extends android.support.v7.widget.AppCompatImageView {
    // 棋子长宽格子数
    int blockNumX;
    int blockNumY;
    int blockWidth;
    int blockHeight;
    // 棋子未移动时位置
    float localX;
    float localY;
    // 棋子触摸事件开始的位置
    private float moveX;
    private float moveY;
    // 父布局
    private GameLayout parent;
    // 在夫布局中的节点编号
    private int id;
    // 在布局中的位置编号
    public int index;
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

    public void init(GameLayout parent, int id, int index, int blockNumX, int blockNumY) {
        this.id = id;
        this.parent = parent;
        this.index = index;
        this.blockNumX = blockNumX;
        this.blockNumY = blockNumY;
        this.direction = Direction.STATIC;
        this.distance = 0;
        this.localX = getLeft();
        this.localY = getTop();
        this.blockWidth = parent.getBlockWidth();
        this.blockHeight = parent.getBlockHeight();
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
                            maxMove = canMove(Direction.RIGHT) * blockWidth;
                            minMove = - canMove(Direction.LEFT) * blockWidth;
                            direction = Direction.X;
                            movInX(distanceX);
                        } else {
                            maxMove = canMove(Direction.DOWN) * blockHeight;
                            minMove = - canMove(Direction.UP) * blockHeight;
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
        int new_index;
        switch (direction) {

            case X:
                float localX_new = Math.round(getX() + 0.5 * blockWidth) /
                        blockWidth;
                // TODO
                parent.setFree(index, blockNumX, blockNumY, true);
                new_index = (int) localX_new + index / 4 * 4;
                parent.addStep(id, true, new_index - index);
                index = new_index;
                parent.setFree(index, blockNumX, blockNumY, false);
                localX = localX_new * blockWidth;
                setX(localX);
                break;
            case Y:
                float localY_new = Math.round(getY() + 0.5 * blockHeight) /
                        blockHeight;
                parent.setFree(index, blockNumX, blockNumY, true);
                new_index = (int) localY_new * 4 + index % 4;
                parent.addStep(id, false, (new_index - index) / 4);
                index = new_index;
                parent.setFree(index, blockNumX, blockNumY, false);
                localY = localY_new * blockHeight;
                setY(localY);
                break;
            default:
                break;
        }
        if (id == 0 && index == 13) {
            parent.clearLevel();
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
    // 回退 isX代表方向  count代表步数
    public void moveBack(boolean isX, int count){
        if(isX) {
            parent.setFree(index, blockNumX, blockNumY, true);
            index = - count + index;
            parent.setFree(index, blockNumX, blockNumY, false);
            localX = (index % 4) * blockWidth;
            setX(localX);
        }else{
            parent.setFree(index, blockNumX, blockNumY, true);
            index = - count * 4 + index;
            parent.setFree(index, blockNumX, blockNumY, false);
            localY = (index / 4) * blockHeight;
            setY(localY);
        }
    }
}
