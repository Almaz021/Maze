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
        Type[] types = new Type[] {
            Type.NORMAL,
            Type.NORMAL,
            Type.NORMAL,
            Type.ICE,
            Type.SAND
        };
        return types[random.nextInt(types.length)];
    }
}
