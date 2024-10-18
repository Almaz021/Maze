package backend.academy.modifiers;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Type;
import backend.academy.interfaces.Modifier;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NonIdealMazeModifier implements Modifier {
    private static final int NORMAL_WEIGHT = 60;
    private static final int OTHER_WEIGHT = 20;
    private static final int TOTAL_WEIGHT = 100;
    private static final double WALL_DELETION_FACTOR = 0.05;
    private final Cell[][] grid;
    private final SecureRandom random;

    @Override
    public Maze modify(int height, int width) {
        deleteSomeWalls(height, width);
        return new Maze(height, width, grid);
    }

    public void deleteSomeWalls(int height, int width) {
        List<Coordinate> wallCoordinates = new ArrayList<>();

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                if (grid[h][w].type() == Type.WALL) {
                    wallCoordinates.add(new Coordinate(h, w));
                }
            }
        }
        Collections.shuffle(wallCoordinates, random);

        int wallsToDelete = (int) Math.ceil(wallCoordinates.size() * WALL_DELETION_FACTOR);
        for (int i = 0; i < wallsToDelete && i < wallCoordinates.size(); i++) {
            Coordinate coordinate = wallCoordinates.get(i);
            grid[coordinate.row()][coordinate.col()] = new Cell(coordinate, getRandomCellType());
        }
    }

    public Type getRandomCellType() {
        int randomNum = random.nextInt(TOTAL_WEIGHT);
        if (randomNum < NORMAL_WEIGHT) {
            return Type.NORMAL;
        } else if (randomNum < NORMAL_WEIGHT + OTHER_WEIGHT) {
            return Type.ICE;
        } else {
            return Type.SAND;
        }
    }

    @Override
    public String toString() {
        return "NonIdealMazeModifier";
    }
}
