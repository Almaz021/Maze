package backend.academy.entities;

import backend.academy.enums.Type;

/**
 * Represents a cell in the maze.
 * <p>
 * A Cell consists of its coordinates in the maze and its type, which determines its properties
 * and behavior in the maze context.
 *
 * @param coordinate The coordinates of the cell, represented by a {@link Coordinate} instance.
 * @param type The type of the cell, represented by a {@link Type} enum, which defines the nature of the cell
 *             (e.g., normal, wall, ice, sand, etc.).
 */
public record Cell(Coordinate coordinate, Type type) {
}
