package backend.academy.generators;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;
import backend.academy.enums.Type;
import backend.academy.interfaces.Generator;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;

/**
 * Base class for generating mazes.
 * This class provides fundamental methods for maze generation,
 * including grid initialization, cell setting, and random cell type generation.
 */
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

    /**
     * Generates a maze with the specified height and width.
     *
     * @param height The height of the maze.
     * @param width  The width of the maze.
     * @return The generated Maze object.
     */
    @Override
    public Maze generate(int height, int width) {
        fill(height, width);
        startPoint = selectStartPoint(height, width);
        setCell(createRandomCell(startPoint.coordinate().row(), startPoint.coordinate().col()));
        return new Maze(height, width, grid);
    }

    /**
     * Fills the grid with cells based on the specified height and width.
     *
     * @param height The height of the maze.
     * @param width  The width of the maze.
     */
    @Override
    public void fill(int height, int width) {
        grid = new Cell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                initializeCell(x, y, height, width);
            }
        }
    }

    /**
     * Initializes the cell at the specified coordinates.
     * <p>
     * This method sets the cell in the maze grid based on its position:
     * <ul>
     *     <li>If the cell is on the boundary, it is set to {@link Type#BEDROCK}.</li>
     *     <li>If the cell is a passage (odd coordinates), it is set to {@link Type#DEFAULT}.</li>
     *     <li>All other cells are set to {@link Type#WALL}.</li>
     * </ul>
     * The method ensures that the borders of the maze are always filled with BEDROCK,
     * while the interior cells alternate between DEFAULT and WALL types based on their position.
     * </p>
     *
     * @param x      The x-coordinate of the cell.
     * @param y      The y-coordinate of the cell.
     * @param height The total height of the maze.
     * @param width  The total width of the maze.
     */
    private void initializeCell(int x, int y, int height, int width) {
        Coordinate coordinate = new Coordinate(y, x);

        if (isPassage(x, y)) {
            grid[y][x] = new Cell(coordinate, Type.DEFAULT);
        } else if (isBoundary(x, y, height, width)) {
            grid[y][x] = new Cell(coordinate, Type.BEDROCK);
        } else {
            grid[y][x] = new Cell(coordinate, Type.WALL);
        }
    }

    /**
     * Checks if the specified coordinates represent a passage.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return True if the coordinates represent a passage, false otherwise.
     */

    private boolean isPassage(int x, int y) {
        return x % 2 != 0 && y % 2 != 0;
    }

    /**
     * Checks if the specified coordinates are on the boundary of the maze.
     *
     * @param x      The x-coordinate.
     * @param y      The y-coordinate.
     * @param height The height of the maze.
     * @param width  The width of the maze.
     * @return True if the coordinates are on the boundary, false otherwise.
     */

    private boolean isBoundary(int x, int y, int height, int width) {
        return x == 0 || y == 0 || x == width - 1 || y == height - 1;
    }

    @Override
    public Cell selectStartPoint(int height, int width) {
        int h = getRandomOdd(height - 2);
        int w = getRandomOdd(width - 2);
        return grid[h][w];
    }

    /**
     * Sets the specified cell in the maze grid.
     * <p>
     * This method updates the grid at the cell's coordinates with the provided cell instance.
     * It is used to change the type of the cell at a specific location in the maze,
     * allowing for modifications during maze generation or solving processes.
     * </p>
     *
     * @param cell The cell to set, containing the new type and coordinates to be updated in the grid.
     */
    @Override
    public void setCell(Cell cell) {
        grid[cell.coordinate().row()][cell.coordinate().col()] = cell;
    }

    public Cell createRandomCell(int row, int col) {
        return new Cell(new Coordinate(row, col), getRandomCellType());
    }

    /**
     * Checks if there is a path from the specified cell in the given direction.
     *
     * @param point     The cell to check.
     * @param direction The direction to check.
     * @return True if there is a path, false otherwise.
     */
    @Override
    public boolean checkPath(Cell point, Direction direction) {
        calculateWallAndPassage(point, direction);
        return grid[yWall][xWall].type() == Type.WALL && grid[yPassage][xPassage].type() == Type.DEFAULT;
    }

    /**
     * Calculates the wall and passage coordinates based on the given point and direction.
     *
     * @param point     The cell from which to calculate.
     * @param direction The direction to calculate.
     */
    public void calculateWallAndPassage(Cell point, Direction direction) {
        xWall = calculateCoordinate(point.coordinate(), direction, 1, false);
        yWall = calculateCoordinate(point.coordinate(), direction, 1, true);
        xPassage = calculateCoordinate(point.coordinate(), direction, 2, false);
        yPassage = calculateCoordinate(point.coordinate(), direction, 2, true);
    }

    protected int calculateCoordinate(Coordinate point, Direction direction, int offset, boolean isRow) {
        return switch (direction) {
            case UP -> isRow ? point.row() - offset : point.col();
            case DOWN -> isRow ? point.row() + offset : point.col();
            case LEFT -> isRow ? point.row() : point.col() - offset;
            case RIGHT -> isRow ? point.row() : point.col() + offset;
        };
    }

    private Type getRandomCellType() {
        int randomNum = random.nextInt(TOTAL_WEIGHT);
        if (randomNum < NORMAL_WEIGHT) {
            return Type.NORMAL;
        } else if (randomNum < NORMAL_WEIGHT + OTHER_WEIGHT) {
            return Type.ICE;
        } else {
            return Type.SAND;
        }
    }

    private int getRandomOdd(int max) {
        return random.nextInt((max + 1) / 2) * 2 + 1;
    }
}
