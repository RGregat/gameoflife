package de.r.gregat.gameoflife.engine;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.r.gregat.gameoflife.engine.model.Cell;

public class World {
    // Welt initialisieren
    // Nachbarbeziehung ermitteln
    // Regeln anwenden
    // Die nächste Generation berechnen
    // Zufällige Zellen aktivieren

    private static int NEIGHBOURS = 8;
    private int width, height;
    private int mColumnWidth, mRowHeight;
    private int randomActivation;
    private int generation;
    private Cell[][] cells;
    private int[] xNeighbours;
    private int[] yNeighbours;

    public World(int width, int height, int randomActivation, int columnWidth, int rowHeight) {
        this.width = width;
        this.height = height;
        this.randomActivation = randomActivation;
        this.mColumnWidth = columnWidth;
        this.mRowHeight = rowHeight;

        xNeighbours = new int[]{1, 1, 0, -1, -1, -1, 0, 1};
        yNeighbours = new int[]{0, 1, 1, 1, 0, -1, -1, -1};

        cells = new Cell[width][height];

        generation = 0;

        setupWorld();

        Log.d("World", "Setup finished");
    }

    private void setupWorld() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(x, y, Cell.DEAD, mColumnWidth, mRowHeight);
            }
        }

        for (int i = 0; i < randomActivation; i++) {
            int xRandom = generateRandomInteger(0, width);
            int yRandom = generateRandomInteger(0, height);

            cells[xRandom][yRandom].reborn();
        }
    }


    private int checkForNeighbours(Cell cell) {
        int neighbourCount = 0;

        for (int i = 0; i < NEIGHBOURS; i++) {
            int xNeighbour = (cell.getX() + xNeighbours[i] + width) % width;
            int yNeighbour = (cell.getY() + yNeighbours[i] + height) % height;

            if ((xNeighbour >= 0 && xNeighbour <= width) && (yNeighbour >= 0 && yNeighbour <= height)) {
                if (cells[xNeighbour][yNeighbour].getState() == Cell.ALIVE) {
                    neighbourCount++;
                }
            }
        }

        return neighbourCount;
    }


    public void nextGenerationV2(List<Integer> rulesToSurvive, List<Integer> rulesToBeReborn) {
        generation++;
        List<Cell> liveCells = new ArrayList<>();
        List<Cell> deadCells = new ArrayList<>();
        Log.d("World", "Generation: " + generation);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int neighbourCount = checkForNeighbours(cells[x][y]);

                int state = cells[x][y].getState();
                boolean result = false;

                if (state == Cell.ALIVE) {
                    for (int ruleToSurvive : rulesToSurvive) {
                        if (neighbourCount == ruleToSurvive) {
                            result = true;
                            break;
                        }
                    }
                } else {
                    for (int ruleToBeReborn : rulesToBeReborn) {
                        if (neighbourCount == ruleToBeReborn) {
                            result = true;
                            break;
                        }
                    }
                }

                if (result) {
                    liveCells.add(cells[x][y]);
                } else {
                    deadCells.add(cells[x][y]);
                }

            }
        }
        for (Cell cell : liveCells) {
            cell.savePrevious();
            cell.reborn();
        }

        for (Cell cell : deadCells) {
            cell.savePrevious();
            cell.die();
        }
    }


    public Cell getCell(int x, int y) {
        return cells[x][y];
    }


    private int generateRandomInteger(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public int getGeneration() {
        return generation;
    }
}
