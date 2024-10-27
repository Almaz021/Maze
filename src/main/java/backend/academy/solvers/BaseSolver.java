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

public class BaseSolver {
    private static final List<Type> PASSABLE_TYPES = List.of(Type.NORMAL, Type.ICE, Type.SAND);
    protected List<Coordinate> list;
    protected List<Coordinate> visited;
    protected List<Coordinate> restricted;
    protected int[][] tempGrid;
    protected Cell[][] grid;

    public void init(Maze maze) {
        list = new ArrayList<>();
        visited = new ArrayList<>();
        restricted = new ArrayList<>();
        tempGrid = new int[maze.height()][maze.width()];
        grid = maze.grid();
    }

    public void fill(Coordinate start) {
        for (int i = 1; i < tempGrid.length - 1; i++) {
            for (int j = 1; j < tempGrid[i].length - 1; j++) {
                tempGrid[i][j] = Integer.MAX_VALUE;
            }
        }
        tempGrid[start.row()][start.col()] = 1;
    }

    public void searchPath(Coordinate start, Coordinate end) {
        Coordinate newPoint = end;
        list.add(newPoint);
        while (check(newPoint, start)) {
            if (newPoint.equals(selectMin(newPoint))) {
                list.remove(newPoint);
                restricted.add(newPoint);
                newPoint = list.getLast();
            } else {
                newPoint = selectMin(newPoint);
                list.add(newPoint);
            }
        }
        if (newPoint.equals(start)) {
            Collections.reverse(list);
        } else {
            list.clear();
        }
    }

    public Coordinate selectMin(Coordinate point) {
        Cell cell = grid[point.row()][point.col()];
        for (Direction direction : Direction.values()) {
            int row = calculateCoordinate(point, direction, true);
            int col = calculateCoordinate(point, direction, false);
            if (bigCheck(row, col, cell)) {
                cell = grid[row][col];
            }
        }
        return cell.coordinate();
    }

    public boolean checkDirection(Coordinate point, Direction direction) {
        Cell cell = grid[calculateCoordinate(point, direction, true)][calculateCoordinate(point, direction, false)];
        return
            PASSABLE_TYPES.contains(cell.type()) && (!visited.contains(cell.coordinate())
                || tempGrid[cell.coordinate().row()][cell.coordinate().col()] > tempGrid[point.row()][point.col()]
                + grid[cell.coordinate().row()][cell.coordinate().col()].type().type());
    }

    public boolean bigCheck(int row, int col, Cell cell) {
        return (checkIndexes(row, col) && !list.contains(grid[row][col].coordinate())
            && tempGrid[row][col] < tempGrid[cell.coordinate().row()][cell.coordinate().col()] + 1
            && grid[row][col].type() != Type.BEDROCK && !restricted.contains(grid[row][col].coordinate()));
    }

    public boolean checkIndexes(int row, int col) {
        return 0 <= row && row < Settings.MAX_HEIGHT && 0 <= col && col < Settings.MAX_WIDTH;
    }

    public int calculateCoordinate(Coordinate point, Direction direction, boolean isRow) {
        return switch (direction) {
            case UP -> isRow ? point.row() - 1 : point.col();
            case DOWN -> isRow ? point.row() + 1 : point.col();
            case LEFT -> isRow ? point.row() : point.col() - 1;
            case RIGHT -> isRow ? point.row() : point.col() + 1;
        };
    }

    public boolean check(Coordinate newPoint, Coordinate start) {
        if (!newPoint.equals(start)) {
            if (newPoint.equals(selectMin(newPoint)) && list.size() > 1) {
                return true;
            } else {
                return !newPoint.equals(selectMin(newPoint)) || list.size() > 1;
            }
        }
        return false;
    }
}
