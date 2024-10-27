package backend.academy.solvers;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;
import backend.academy.enums.Type;
import backend.academy.settings.Settings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@code BaseSolver} class provides functionality for pathfinding in a maze.
 * It employs a search algorithm to find a route between a start and end coordinate
 * while considering passable cell types.
 */
public class BaseSolver {
    private static final List<Type> PASSABLE_TYPES = List.of(Type.NORMAL, Type.ICE, Type.SAND);
    protected List<Coordinate> list;
    protected List<Coordinate> visited;
    protected List<Coordinate> restricted;
    protected int[][] tempGrid;
    protected Cell[][] grid;

    /**
     * Initializes the solver with the given maze.
     *
     * @param maze The maze to be solved.
     */
    public void init(Maze maze) {
        list = new ArrayList<>();
        visited = new ArrayList<>();
        restricted = new ArrayList<>();
        tempGrid = new int[maze.height()][maze.width()];
        grid = maze.grid();
    }

    /**
     * Fills the temporary grid for pathfinding.
     *
     * @param start The starting coordinate.
     */
    protected void fill(Coordinate start) {
        for (int i = 1; i < tempGrid.length - 1; i++) {
            for (int j = 1; j < tempGrid[i].length - 1; j++) {
                tempGrid[i][j] = Integer.MAX_VALUE; // Initialize to max value
            }
        }
        tempGrid[start.row()][start.col()] = 1; // Starting point
    }

    /**
     * Finds a path from the start coordinate to the end coordinate.
     * Traverses the coordinates and finds the shortest and most optimal path
     * @param start The start coordinate.
     * @param end The end coordinate.
     */
    protected void searchPath(Coordinate start, Coordinate end) {
        Coordinate newPoint = end;
        list.add(newPoint);
        while (checkIfPossibleToFindPath(newPoint, start)) {
            Coordinate minPoint = selectMinCellCoordinate(newPoint);

            if (newPoint.equals(minPoint)) {
                list.remove(newPoint);
                restricted.add(newPoint);
                newPoint = list.getLast(); // Backtrack
            } else {
                newPoint = selectMinCellCoordinate(newPoint);
                list.add(newPoint);
            }
        }
        if (newPoint.equals(start)) {
            Collections.reverse(list); // Found a valid path
        } else {
            list.clear(); // No valid path found
        }
    }

    /**
     * Selects the coordinate of the cell with the minimum value from the
     * current cell, considering all valid neighboring cells in the
     * specified directions.
     *
     * @param point the current coordinate being evaluated
     * @return the coordinate of the cell with the minimum value among the
     *         suitable neighboring cells
     */
    private Coordinate selectMinCellCoordinate(Coordinate point) {
        Cell cell = grid[point.row()][point.col()];
        for (Direction direction : Direction.values()) {
            int row = calculateCoordinate(point, direction, true);
            int col = calculateCoordinate(point, direction, false);
            if (ifCellSuitable(row, col, cell)) {
                cell = grid[row][col];
            }
        }
        return cell.coordinate();
    }

    /**
     * Checks if the specified cell in a given direction from the current
     * coordinate is passable and whether it has been visited or offers
     * a better path.
     *
     * @param point the current coordinate being evaluated
     * @param direction the direction to check for a neighboring cell
     * @return true if the cell is passable and either not visited or has a
     *         better path; false otherwise
     */
    protected boolean checkCellDirection(Coordinate point, Direction direction) {
        Cell cell = grid[calculateCoordinate(point, direction, true)][calculateCoordinate(point, direction, false)];

        return isCellPassable(cell) && (!hasBeenVisited(cell) || hasBetterPath(point, cell));
    }

    private boolean isCellPassable(Cell cell) {
        return PASSABLE_TYPES.contains(cell.type());
    }

    private boolean hasBeenVisited(Cell cell) {
        return visited.contains(cell.coordinate());
    }

    private boolean hasBetterPath(Coordinate point, Cell cell) {
        return tempGrid[cell.coordinate().row()][cell.coordinate().col()]
            > tempGrid[point.row()][point.col()] + cell.type().type();
    }

    /**
     * Determines if a cell at the specified row and column is suitable for traversal.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @param cell the cell being evaluated
     * @return true if the cell is suitable for traversal; false otherwise
     */
    private boolean ifCellSuitable(int row, int col, Cell cell) {
        return checkCoordinatesInBounds(row, col)
            && !isCellInPath(row, col)
            && hasGoodPath(row, col, cell)
            && isCellTypeSuitable(row, col)
            && !isCellRestricted(row, col);
    }


    private boolean isCellInPath(int row, int col) {
        return list.contains(grid[row][col].coordinate());
    }

    private boolean hasGoodPath(int row, int col, Cell cell) {
        return tempGrid[row][col] < tempGrid[cell.coordinate().row()][cell.coordinate().col()] + 1;
    }

    private boolean isCellTypeSuitable(int row, int col) {
        return grid[row][col].type() != Type.BEDROCK;
    }

    private boolean isCellRestricted(int row, int col) {
        return restricted.contains(grid[row][col].coordinate());
    }

    private boolean checkCoordinatesInBounds(int row, int col) {
        return 0 <= row && row < Settings.MAX_HEIGHT && 0 <= col && col < Settings.MAX_WIDTH;
    }

    protected int calculateCoordinate(Coordinate point, Direction direction, boolean isRow) {
        return switch (direction) {
            case UP -> isRow ? point.row() - 1 : point.col();
            case DOWN -> isRow ? point.row() + 1 : point.col();
            case LEFT -> isRow ? point.row() : point.col() - 1;
            case RIGHT -> isRow ? point.row() : point.col() + 1;
        };
    }

    /**
     * Determines if a path from the new point to the start point may exist.
     *
     * @param newPoint the current point being evaluated
     * @param start the starting point for the path
     * @return true if a path may be found; false otherwise
     */
    private boolean checkIfPossibleToFindPath(Coordinate newPoint, Coordinate start) {
        if (!newPoint.equals(start)) {
            if (newPoint.equals(selectMinCellCoordinate(newPoint)) && list.size() > 1) {
                return true;
            } else {
                return !newPoint.equals(selectMinCellCoordinate(newPoint)) || list.size() > 1;
            }
        }
        return false;
    }
}
