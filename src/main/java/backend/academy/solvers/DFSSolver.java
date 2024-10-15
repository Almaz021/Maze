package backend.academy.solvers;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;
import backend.academy.enums.Type;
import backend.academy.interfaces.Solver;
import backend.academy.settings.Settings;
import java.util.ArrayList;
import java.util.List;

public class DFSSolver implements Solver {
    private List<Coordinate> list;
    private List<Coordinate> visited;
    private List<Coordinate> restricted;
    private int[][] tempGrid;
    private Cell[][] grid;

    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        init(maze);
        fill(start);
        dfs(start);
        searchPath(start, end);
        return list;
    }

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

    public void dfs(Coordinate point) {
        visited.add(point);
        for (Direction direction : Direction.values()) {
            if (checkDirection(point, direction)) {
                int row = calculateRow(point, direction);
                int col = calculateCol(point, direction);
                Coordinate newPoint = new Coordinate(row, col);
                tempGrid[newPoint.row()][newPoint.col()] = Math.min(tempGrid[newPoint.row()][newPoint.col()],
                    tempGrid[point.row()][point.col()] + grid[newPoint.row()][newPoint.col()].type().type());
                dfs(newPoint);
            }
        }
    }

    public void searchPath(Coordinate start, Coordinate end) {
        Coordinate newPoint = end;
        list.add(newPoint);
        while (!newPoint.equals(start)) {
            if (newPoint.equals(selectMin(newPoint))) {
                list.remove(newPoint);
                restricted.add(newPoint);
                newPoint = list.getLast();
            } else {
                newPoint = selectMin(newPoint);
                list.add(newPoint);
            }
        }

    }

    public Coordinate selectMin(Coordinate point) {
        Cell cell = grid[point.row()][point.col()];
        for (Direction direction : Direction.values()) {
            int row = calculateRow(point, direction);
            int col = calculateCol(point, direction);
            if (checkIndexes(row, col) && !list.contains(grid[row][col].coordinate())
                && tempGrid[row][col] < tempGrid[cell.coordinate().row()][cell.coordinate().col()] + 1
                && grid[row][col].type() != Type.BEDROCK && !restricted.contains(grid[row][col].coordinate())) {
                cell = grid[row][col];
            }
        }
        return cell.coordinate();
    }

    public boolean checkDirection(Coordinate point, Direction direction) {
        Cell cell = grid[calculateRow(point, direction)][calculateCol(point, direction)];
        return
            List.of(Type.NORMAL, Type.ICE, Type.SAND).contains(cell.type()) && (!visited.contains(cell.coordinate())
                || tempGrid[cell.coordinate().row()][cell.coordinate().col()] > tempGrid[point.row()][point.col()]
                + grid[cell.coordinate().row()][cell.coordinate().col()].type().type());
    }

    public boolean checkIndexes(int row, int col) {
        return 0 <= row && row < Settings.MAX_HEIGHT && 0 <= col && col < Settings.MAX_WIDTH;
    }

    public int calculateRow(Coordinate point, Direction direction) {
        return switch (direction) {
            case UP -> point.row() - 1;
            case DOWN -> point.row() + 1;
            default -> point.row();
        };
    }

    public int calculateCol(Coordinate point, Direction direction) {
        return switch (direction) {
            case LEFT -> point.col() - 1;
            case RIGHT -> point.col() + 1;
            default -> point.col();
        };
    }
}
