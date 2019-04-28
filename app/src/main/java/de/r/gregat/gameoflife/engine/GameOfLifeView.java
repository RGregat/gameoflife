package de.r.gregat.gameoflife.engine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import de.r.gregat.gameoflife.R;
import de.r.gregat.gameoflife.engine.model.Cell;

public class GameOfLifeView extends SurfaceView implements Runnable {

    // Default size of a cell
    public static final int DEFAULT_SIZE = 50;

    public static final int DEFAULT_EVOLVE_TIME = 300;
    // Thread which will be responsible to manage the evolution of the World
    private Thread thread;
    // Boolean indicating if the World is evolving or not
    private boolean isRunning = false;
    private int columnWidth = 1;
    private int rowHeight = 1;
    private int nbColumns = 1;
    private int nbRows = 1;
    private int randomActivation = 10;
    private World world;
    private Point point;
    private int customCellSize = DEFAULT_SIZE;
    private int customEvolveTime = DEFAULT_EVOLVE_TIME;
    private boolean allowGeneration = true;

    private List<Integer> rulesToSurvive = new ArrayList<>();
    private List<Integer> rulesToBeReborn = new ArrayList<>();

    private Listener mListener;

    public GameOfLifeView(Context context) {
        super(context);
        setupWorld(null);
    }

    public GameOfLifeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupWorld(attrs);
    }

    public void setmListener(Listener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void run() {
        // while the world is evolving
        while (isRunning) {
            if (!getHolder().getSurface().isValid())
                continue;

            try {
                Thread.sleep(customEvolveTime);
            } catch (InterruptedException e) {
            }

            Canvas canvas = getHolder().lockCanvas();
            if (allowGeneration) {
                world.nextGenerationV2(rulesToSurvive, rulesToBeReborn);
                if (mListener != null) {
                    mListener.generationNumber(world.getGeneration());
                }
            }

            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            drawCells(canvas);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void drawCells(Canvas canvas) {
        for (int x = 0; x < nbColumns; x++) {
            for (int y = 0; y < nbRows; y++) {
                Cell cell = world.getCell(x, y);
                cell.drawCell(canvas);
            }
        }
    }

    public void start() {
        // World is evolving
        isRunning = true;
        thread = new Thread(this);
        // we start the Thread for the World's evolution
        thread.start();
    }

    public void stop() {
        isRunning = false;

        while (true) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
            break;
        }
    }

    private void setupWorld(@Nullable AttributeSet set) {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        point = new Point();
        display.getSize(point);

        if (set != null) {
            TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.GameOfLifeView);
            customCellSize = ta.getInt(R.styleable.GameOfLifeView_cell_size, DEFAULT_SIZE);
            randomActivation = ta.getInt(R.styleable.GameOfLifeView_random_activation_count, 10);
            customEvolveTime = ta.getInt(R.styleable.GameOfLifeView_evolve_time, DEFAULT_EVOLVE_TIME);

            calculateColumns(customCellSize);
            calculateRows(customCellSize);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // we get the coordinates of the touch and we convert it in coordinates for the board
            int i = (int) (event.getX() / columnWidth);
            int j = (int) (event.getY() / rowHeight);
            // we get the cell associated to these positions
            Cell cell = world.getCell(i, j);
            // we call the invert method of the cell got to change its state
            cell.invert();
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    public void setCellSize(int cellSize) {
        customCellSize = cellSize;
        calculateColumns(customCellSize);
        calculateRows(customCellSize);
    }

    public void setActivatedCells(int activatedCells) {
        randomActivation = activatedCells;
    }

    public void setEvolvingTime(int evolvingTime) {
        customEvolveTime = evolvingTime;
    }

    public void setRulesToSurvive(List<Integer> rulesToSurvive) {
        this.rulesToSurvive = rulesToSurvive;
    }

    public void setRulesToBeReborn(List<Integer> rulesToBeReborn) {
        this.rulesToBeReborn = rulesToBeReborn;
    }

    public void calculateColumns(int cellSize) {
        nbColumns = point.x / cellSize;
        columnWidth = point.x / nbColumns;
    }

    public void calculateRows(int cellSize) {
        nbRows = point.y / cellSize;
        rowHeight = point.y / nbRows;
    }

    public void restartWorld() {
        initWorld();
    }

    public void increaseEvolveTime(int value) {
        customEvolveTime += value;
    }

    public void decreaseEvolveTime(int value) {
        customEvolveTime -= value;
    }

    public void toggleAllowGeneration() {
        allowGeneration = !allowGeneration;
    }

    public void initWorld() {
        world = new World(nbColumns, nbRows, randomActivation, columnWidth, rowHeight);
    }

    public interface Listener {
        void generationNumber(int generation);
    }
}
