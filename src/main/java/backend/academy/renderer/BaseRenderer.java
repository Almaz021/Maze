package backend.academy.renderer;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.interfaces.Renderer;
import java.util.List;

public class BaseRenderer implements Renderer {
    private static final String BLACK = "\u001B[38;2;0;0;0m";
    private static final String WHITE = "\u001B[38;2;255;255;255m";
    private static final String GREEN = "\u001B[38;2;0;255;0m";
    private static final String RED = "\u001B[38;2;255;0;0m";
    private static final String BLUE = "\u001B[38;2;0;0;255m";
    private static final String YELLOW = "\u001B[38;2;255;255;0m";
    private static final String RESET = "\u001B[0m";
    private static final char RECTANGLE = '█';

    public StringBuilder render(Maze maze) {
        return render(maze, null);
    }

    public StringBuilder render(Maze maze, List<Coordinate> path) {
        StringBuilder result = new StringBuilder();
        for (Cell[] cells : maze.grid()) {
            for (Cell cell : cells) {
                boolean isOnPath = path != null && path.contains(cell.coordinate());
                renderCell(result, cell, isOnPath);
            }
            result.append('\n');
        }
        return result;
    }

    public void renderCell(StringBuilder result, Cell cell, boolean isOnPath) {
        String color = getColor(cell, isOnPath);
        result.append(color).append(RECTANGLE).append(RECTANGLE).append(RESET);
    }

    public String getColor(Cell cell, boolean isOnPath) {
        return switch (cell.type()) {
            case BEDROCK -> GREEN;
            case NORMAL -> isOnPath ? RED : BLACK;
            case ICE -> isOnPath ? RED : BLUE;
            case SAND -> isOnPath ? RED : YELLOW;
            case WALL -> WHITE;
            default -> RED;
        };
    }
}
