package backend.academy.renderer;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Type;
import backend.academy.interfaces.Renderer;
import java.util.List;

public class BaseRenderer implements Renderer {
    private static final String BLACK = "\u001B[38;2;0;0;0m";
    private static final String WHITE = "\u001B[38;2;240;240;240m";
    private static final String GREEN = "\u001B[38;2;21;212;0m";
    private static final String RED = "\u001B[38;2;222;150;150m";
    private static final String CYAN = "\u001B[38;2;123;255;255m";
    private static final String PURPLE = "\u001B[38;2;195;161;255m";
    private static final String YELLOW = "\u001B[38;2;255;248;163m";
    private static final String ORANGE = "\u001B[38;2;255;111;5m";

    private static final String RESET = "\u001B[0m";
    private static final char RECTANGLE = '█';

    public StringBuilder render(Maze maze) {
        return render(maze, null);
    }

    public StringBuilder render(Maze maze, List<Coordinate> path) {
        if (path != null && path.isEmpty()) {
            return new StringBuilder("NO PATH FOUND");
        }
        StringBuilder result = new StringBuilder();
        Coordinate start = path != null ? path.getFirst() : null;
        Coordinate end = path != null ? path.getLast() : null;

        for (Cell[] cells : maze.grid()) {
            for (Cell cell : cells) {
                boolean isOnPath = path != null && path.contains(cell.coordinate());
                if (isOnPath && cell.coordinate().equals(start)) {
                    renderCell(result, Type.START);
                } else if (isOnPath && cell.coordinate().equals(end)) {
                    renderCell(result, Type.END);
                } else {
                    renderCell(result, cell, isOnPath);
                }
            }
            result.append('\n');
        }
        return result;
    }

    public void renderCell(StringBuilder result, Cell cell, boolean isOnPath) {
        String color = getColor(cell, isOnPath);
        result.append(color).append(RECTANGLE).append(RECTANGLE).append(RESET);
    }

    private void renderCell(StringBuilder result, Type specialType) {
        String color = getSpecialColor(specialType);
        result.append(color).append(RECTANGLE).append(RECTANGLE).append(RESET);
    }

    public String getColor(Cell cell, boolean isOnPath) {
        return switch (cell.type()) {
            case BEDROCK -> PURPLE;
            case NORMAL -> isOnPath ? RED : BLACK;
            case ICE -> isOnPath ? RED : CYAN;
            case SAND -> isOnPath ? RED : YELLOW;
            case WALL -> WHITE;
            default -> RED;
        };
    }

    private String getSpecialColor(Type type) {
        return switch (type) {
            case START -> GREEN;
            case END -> ORANGE;
            default -> RED;
        };
    }
}
