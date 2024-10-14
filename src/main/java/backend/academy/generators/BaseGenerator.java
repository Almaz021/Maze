package backend.academy.generators;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.enums.Direction;
import backend.academy.enums.Type;
import java.security.SecureRandom;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BaseGenerator {
    protected int xWall;
    protected int yWall;
    protected int xPassage;
    protected int yPassage;

    protected Cell[][] grid;
    protected final SecureRandom random;

    public void fill(int height, int width) {
        grid = new Cell[height][width];
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (x % 2 != 0 && y % 2 != 0) {
                    grid[y][x] = new Cell(new Coordinate(y, x), Type.DEFAULT);
                } else if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    grid[y][x] = new Cell(new Coordinate(y, x), Type.BEDROCK);
                } else {
                    grid[y][x] = new Cell(new Coordinate(y, x), Type.WALL);
                }
            }
        }
    }

    public Cell selectStartPoint(int height, int width) {
        int[] rangeHeight = IntStream.iterate(1, n -> n + 2).limit(height / 2).toArray();
        int[] rangeWidth = IntStream.iterate(1, n -> n + 2).limit(width / 2).toArray();
        int h = rangeHeight[random.nextInt(rangeHeight.length)];
        int w = rangeWidth[random.nextInt(rangeWidth.length)];
        return grid[h][w];
    }

    public boolean checkPath(Cell point, Direction direction) {
        calculateWallAndPassage(point, direction);
        return grid[yWall][xWall].type() == Type.WALL && grid[yPassage][xPassage].type() == Type.DEFAULT;
    }

    public void calculateWallAndPassage(Cell point, Direction direction) {
        xWall = calculateWallX(point.coordinate().col(), direction);
        yWall = calculateWallY(point.coordinate().row(), direction);
        xPassage = calculatePassageX(point.coordinate().col(), direction);
        yPassage = calculatePassageY(point.coordinate().row(), direction);
    }

    public int calculateWallX(int col, Direction direction) {
        return switch (direction) {
            case RIGHT -> col + 1;
            case LEFT -> col - 1;
            default -> col;
        };
    }

    public int calculateWallY(int row, Direction direction) {
        return switch (direction) {
            case UP -> row - 1;
            case DOWN -> row + 1;
            default -> row;
        };
    }

    public int calculatePassageX(int col, Direction direction) {
        return switch (direction) {
            case RIGHT -> col + 2;
            case LEFT -> col - 2;
            default -> col;
        };
    }

    public int calculatePassageY(int row, Direction direction) {
        return switch (direction) {
            case UP -> row - 2;
            case DOWN -> row + 2;
            default -> row;
        };
    }

    public Type getRandomCellType() {
        Type[] types = new Type[] {
            Type.NORMAL,
            Type.NORMAL,
            Type.NORMAL,
            Type.ICE,
            Type.SAND
        };
        return types[random.nextInt(types.length)];
    }
}
