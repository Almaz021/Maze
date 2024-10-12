package backend.academy.modifiers;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Type;
import backend.academy.interfaces.Modifier;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NonIdealMazeModifier implements Modifier {
    private static final int THREE = 3;
    private static final double NUM = 0.1;
    private final Cell[][] grid;
    private final SecureRandom random;

    @Override
    public Maze modify(int height, int width) {
        deleteSomeWalls(height, width);
        return new Maze(height, width, grid);
    }

    public void deleteSomeWalls(int height, int width) {
        int range = (int) Math.ceil((double) ((height - THREE) * (width - THREE)) / 2 * NUM);
        for (int i = 0; i < range; i++) {
            int h = random.nextInt(0, height);
            int w = random.nextInt(0, width);
            if (grid[h][w].type() == Type.WALL) {
                grid[h][w] = new Cell(new Coordinate(h, w), Type.DEFAULT);
            }
        }
    }
}
