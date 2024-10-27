package backend.academy.generators;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;
import backend.academy.enums.Type;
import backend.academy.interfaces.Generator;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BaseGenerator implements Generator {
    private static final int NORMAL_WEIGHT = 60;
    private static final int OTHER_WEIGHT = 20;
    private static final int TOTAL_WEIGHT = 100;

    protected int xWall;
    protected int yWall;
    protected int xPassage;
    protected int yPassage;

    protected Cell startPoint;
    protected Cell[][] grid;
    protected final SecureRandom random;

    @Override
    public Maze generate(int height, int width) {
        fill(height, width);
        startPoint = selectStartPoint(height, width);
        setCell(createRandomCell(startPoint.coordinate().row(), startPoint.coordinate().col()));
        return new Maze(height, width, grid);
    }

    @Override
    public void fill(int height, int width) {
        grid = new Cell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                initializeCell(x, y, height, width);
            }
        }
    }

    public void initializeCell(int x, int y, int height, int width) {
        Coordinate coordinate = new Coordinate(y, x);

        if (isPassage(x, y)) {
            grid[y][x] = new Cell(coordinate, Type.DEFAULT);
        } else if (isBoundary(x, y, height, width)) {
            grid[y][x] = new Cell(coordinate, Type.BEDROCK);
        } else {
            grid[y][x] = new Cell(coordinate, Type.WALL);
        }
    }

    public boolean isPassage(int x, int y) {
        return x % 2 != 0 && y % 2 != 0;
    }

    public boolean isBoundary(int x, int y, int height, int width) {
        return x == 0 || y == 0 || x == width - 1 || y == height - 1;
    }

    @Override
    public Cell selectStartPoint(int height, int width) {
        int h = getRandomOdd(height - 2);
        int w = getRandomOdd(width - 2);
        return grid[h][w];
    }

    @Override
    public void setCell(Cell cell) {
        grid[cell.coordinate().row()][cell.coordinate().col()] = cell;
    }

    public Cell createRandomCell(int row, int col) {
        return new Cell(new Coordinate(row, col), getRandomCellType());
    }

    @Override
    public boolean checkPath(Cell point, Direction direction) {
        calculateWallAndPassage(point, direction);
        return grid[yWall][xWall].type() == Type.WALL && grid[yPassage][xPassage].type() == Type.DEFAULT;
    }

    public void calculateWallAndPassage(Cell point, Direction direction) {
        xWall = calculateCoordinateX(point.coordinate().col(), direction, 1);
        yWall = calculateCoordinateY(point.coordinate().row(), direction, 1);
        xPassage = calculateCoordinateX(point.coordinate().col(), direction, 2);
        yPassage = calculateCoordinateY(point.coordinate().row(), direction, 2);
    }

    public int calculateCoordinateX(int value, Direction direction, int offset) {
        return switch (direction) {
            case LEFT -> value - offset;
            case RIGHT -> value + offset;
            default -> value;
        };
    }

    public int calculateCoordinateY(int value, Direction direction, int offset) {
        return switch (direction) {
            case UP -> value - offset;
            case DOWN -> value + offset;
            default -> value;
        };
    }

    public Type getRandomCellType() {
        int randomNum = random.nextInt(TOTAL_WEIGHT);
        if (randomNum < NORMAL_WEIGHT) {
            return Type.NORMAL;
        } else if (randomNum < NORMAL_WEIGHT + OTHER_WEIGHT) {
            return Type.ICE;
        } else {
            return Type.SAND;
        }
    }

    public int getRandomOdd(int max) {
        return random.nextInt((max + 1) / 2) * 2 + 1;
    }
}
