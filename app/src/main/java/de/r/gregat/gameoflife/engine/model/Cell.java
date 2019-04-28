package de.r.gregat.gameoflife.engine.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import de.r.gregat.gameoflife.R;
import de.r.gregat.gameoflife.screens.GameOfLifeApplication;

public class Cell {

    public static final int ALIVE = 0;
    public static final int DEAD = 1;

    private int mX;
    private int mY;
    private int mColumnWidth;
    private int mRowHeight;
    private int mState;
    private int mLastState;

    private Rect mCellRectangle = new Rect();
    private Paint mCellPainOutline = new Paint();
    private Paint mCellPaint = new Paint();

    public Cell(int x, int y, int state, int columnWidth, int rowHeight) {
        this.mX = x;
        this.mY = y;
        this.mState = state;
        this.mLastState = state;
        this.mColumnWidth = columnWidth;
        this.mRowHeight = rowHeight;

        mCellPainOutline.setStyle(Paint.Style.STROKE);
        mCellPainOutline.setStrokeWidth(1);
        mCellPainOutline.setColor(GameOfLifeApplication.getmContext().getColor(R.color.outline));
    }

    public void drawCell(Canvas canvas) {
        mCellRectangle.set(
                (getX() * mColumnWidth) - 1,
                (getY() * mRowHeight) - 1,
                (getX() * mColumnWidth + mColumnWidth) - 1,
                (getY() * mRowHeight + mRowHeight) - 1
        );

        if(mLastState == ALIVE && mState == DEAD) {
            mCellPaint.setColor(GameOfLifeApplication.getmContext().getColor(R.color.die));
        } else if(mState == DEAD) {
            mCellPaint.setColor(GameOfLifeApplication.getmContext().getColor(R.color.dead));
        } else if(mLastState == DEAD && mState == ALIVE) {
            mCellPaint.setColor(GameOfLifeApplication.getmContext().getColor(R.color.reborn));
        } else {
            mCellPaint.setColor(GameOfLifeApplication.getmContext().getColor(R.color.alive));
        }

        canvas.drawRect(mCellRectangle, mCellPaint);
        canvas.drawRect(mCellRectangle, mCellPainOutline);
    }

    public void die() {
        mState = DEAD;
    }

    public void reborn() {
        mState = ALIVE;
    }

    public void invert() {
        if(mState == ALIVE) {
            mState = DEAD;
        } else {
            mState = ALIVE;
        }
    }

    public void savePrevious() {
        mLastState = mState;
    }

    public int getState() {
        return mState;
    }

    public int getX() {
        return mX;
    }

    public void setX(int x) {
        this.mX = x;
    }

    public int getY() {
        return mY;
    }

    public void setY(int y) {
        this.mY = y;
    }
}
