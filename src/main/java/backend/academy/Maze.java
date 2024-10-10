package backend.academy;

import backend.academy.entities.Cell;

public record Maze(int height, int width, Cell[][] grid) {
}
