package backend.academy.modifiers;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Type;
import backend.academy.interfaces.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * Class that implements a modifier to alter the maze by deleting some walls,
 * creating a non-ideal maze configuration.
 */
@RequiredArgsConstructor
public class NonIdealMazeModifier implements Modifier {
    private static final int NORMAL_WEIGHT = 60; // Weight for NORMAL cell type
    private static final int OTHER_WEIGHT = 20;  // Weight for other cell types
    private static final int TOTAL_WEIGHT = 100;  // Total weight for random selection
    private static final BigDecimal WALL_DELETION_FACTOR = new BigDecimal("0.05"); // Percentage of walls to delete
    private final Cell[][] grid;
    private final SecureRandom random;

    @Override
    public Maze modify(int height, int width) {
        deleteSomeWalls(height, width);
        return new Maze(height, width, grid);
    }

    private void deleteSomeWalls(int height, int width) {
        List<Coordinate> wallCoordinates = new ArrayList<>();

        // Collect coordinates of all wall cells
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                if (grid[h][w].type() == Type.WALL) {
                    wallCoordinates.add(new Coordinate(h, w));
                }
            }
        }

        // Shuffle wall coordinates to randomize wall deletion
        Collections.shuffle(wallCoordinates, random);

        // Calculate the number of walls to delete
        BigDecimal size = new BigDecimal(wallCoordinates.size());
        int wallsToDelete = size.multiply(WALL_DELETION_FACTOR).setScale(0, RoundingMode.CEILING).intValue();

        // Delete walls and replace with random cell types
        for (int i = 0; i < wallsToDelete && i < wallCoordinates.size(); i++) {
            Coordinate coordinate = wallCoordinates.get(i);
            grid[coordinate.row()][coordinate.col()] = new Cell(coordinate, getRandomCellType());
        }
    }

    /**
     * Returns a random cell type based on predefined weights.
     *
     * @return a randomly selected Type enum value.
     */
    private Type getRandomCellType() {
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
