package backend.academy.renderer;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Color;
import backend.academy.enums.Type;
import backend.academy.interfaces.Renderer;
import java.util.List;
import static backend.academy.enums.Color.BLACK;
import static backend.academy.enums.Color.CYAN;
import static backend.academy.enums.Color.GREEN;
import static backend.academy.enums.Color.ORANGE;
import static backend.academy.enums.Color.PURPLE;
import static backend.academy.enums.Color.RED;
import static backend.academy.enums.Color.RESET;
import static backend.academy.enums.Color.WHITE;
import static backend.academy.enums.Color.YELLOW;

public class BaseRenderer implements Renderer {
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
        String color = getColor(cell.type(), isOnPath).colorCode();
        result.append(color).append(RECTANGLE).append(RECTANGLE).append(RESET);
    }

    private void renderCell(StringBuilder result, Type specialType) {
        String color = getSpecialColor(specialType).colorCode();
        result.append(color).append(RECTANGLE).append(RECTANGLE).append(RESET);
    }

    public Color getColor(Type type, boolean isOnPath) {
        return switch (type) {
            case BEDROCK -> PURPLE;
            case NORMAL -> isOnPath ? RED : BLACK;
            case ICE -> isOnPath ? RED : CYAN;
            case SAND -> isOnPath ? RED : YELLOW;
            case WALL -> WHITE;
            default -> RED;
        };
    }

    private Color getSpecialColor(Type type) {
        return switch (type) {
            case START -> GREEN;
            case END -> ORANGE;
            default -> RED;
        };
    }
}
