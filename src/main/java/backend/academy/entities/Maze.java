package backend.academy.entities;

/**
 * Represents a maze consisting of a grid of cells.
 * <p>
 * A Maze is defined by its height and width, along with a 2D array of
 * cells that make up the maze structure. Each cell can represent different
 * types of terrain or obstacles within the maze.
 *
 * @param height The height of the maze, representing the number of rows.
 * @param width The width of the maze, representing the number of columns.
 * @param grid A 2D array of {@link Cell} objects that defines the maze structure.
 */
public record Maze(int height, int width, Cell[][] grid) {
}
