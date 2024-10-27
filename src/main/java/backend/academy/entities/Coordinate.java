package backend.academy.entities;

/**
 * Represents a coordinate in a 2D maze.
 * <p>
 * A Coordinate consists of a row and a column index, which together define
 * a specific position in a maze.
 *
 * @param row The row index of the coordinate.
 * @param col The column index of the coordinate.
 */
public record Coordinate(int row, int col) {
}
