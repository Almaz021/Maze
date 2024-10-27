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

/**
 * BaseRenderer is responsible for rendering a maze and its components
 * using different colors based on the cell types and paths.
 */
public class BaseRenderer implements Renderer {
    private static final char RECTANGLE = '█'; // Character used to represent cells in the maze

    /**
     * Renders the maze without any specified path.
     *
     * @param maze the maze to be rendered
     * @return a StringBuilder containing the rendered maze
     */
    public StringBuilder render(Maze maze) {
        return render(maze, null);
    }

    /**
     * Renders the maze with a specified path.
     *
     * @param maze the maze to be rendered
     * @param path a list of coordinates representing the path through the maze
     * @return a StringBuilder containing the rendered maze with the path
     */
    public StringBuilder render(Maze maze, List<Coordinate> path) {
        // Check if a path is provided and is empty
        if (path != null && path.isEmpty()) {
            return new StringBuilder("NO PATH FOUND");
        }
        StringBuilder result = new StringBuilder();
        Coordinate start = path != null ? path.getFirst() : null; // Get start coordinate
        Coordinate end = path != null ? path.getLast() : null; // Get end coordinate

        for (Cell[] cells : maze.grid()) {
            for (Cell cell : cells) {
                boolean isOnPath = path != null && path.contains(cell.coordinate());
                if (isOnPath && cell.coordinate().equals(start)) {
                    // Render start cell
                    renderCell(result, Type.START);
                } else if (isOnPath && cell.coordinate().equals(end)) {
                    // Render end cell
                    renderCell(result, Type.END);
                } else {
                    // Render normal cell
                    renderCell(result, cell, isOnPath);
                }
            }
            result.append('\n'); // New line after each row of the maze
        }
        return result;
    }

    /**
     * Renders a specific cell based on its type and whether it is part of the path.
     *
     * @param result   the StringBuilder to append the rendered cell to
     * @param cell     the Cell to be rendered
     * @param isOnPath true if the cell is part of the path, false otherwise
     */
    private void renderCell(StringBuilder result, Cell cell, boolean isOnPath) {
        String color = getColor(cell.type(), isOnPath).colorCode();
        result.append(color).append(RECTANGLE).append(RECTANGLE).append(RESET);
    }

    /**
     * Renders a special type of cell (START or END) with a specific color.
     *
     * @param result     the StringBuilder to append the rendered cell to
     * @param specialType the special Type (START or END) to be rendered
     */
    private void renderCell(StringBuilder result, Type specialType) {
        String color = getSpecialColor(specialType).colorCode();
        result.append(color).append(RECTANGLE).append(RECTANGLE).append(RESET);
    }

    private Color getColor(Type type, boolean isOnPath) {
        return switch (type) {
            case BEDROCK -> PURPLE;
            case NORMAL -> isOnPath ? RED : BLACK;
            case ICE -> isOnPath ? RED : CYAN;
            case SAND -> isOnPath ? RED : YELLOW;
            case WALL -> WHITE;
            default -> RED; // Default color for unknown types
        };
    }

    private Color getSpecialColor(Type type) {
        return switch (type) {
            case START -> GREEN;
            case END -> ORANGE;
            default -> RED; // Default color for unknown types
        };
    }
}
