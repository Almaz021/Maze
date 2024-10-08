package backend.academy;

import backend.academy.entities.Cell;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Maze {
    private final int height;
    private final int width;
    private final Cell[][] grid;
}
