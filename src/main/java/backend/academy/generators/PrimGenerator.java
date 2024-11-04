package backend.academy.generators;

import backend.academy.entities.Cell;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;
import backend.academy.enums.Type;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Prim's algorithm for maze generation.
 * This class extends the BaseGenerator.
 * It generates a maze by starting from a random point and creating passages
 * between cells while ensuring that the maze is connected.
 */
public class PrimGenerator extends BaseGenerator {
    public PrimGenerator(SecureRandom random) {
        super(random);
    }

    @Override
    public Maze generate(int height, int width) {
        super.generate(height, width);
        start();
        return new Maze(height, width, grid);
    }

    private void start() {
        List<Cell> cells = new ArrayList<>();
        List<Cell> passages = new ArrayList<>();

        Cell selectedCell = startPoint;

        do {
            collectCells(cells, selectedCell);

            // Randomly select a cell from the collected cells
            selectedCell = cells.get(random.nextInt(cells.size()));
            setCell(createRandomCell(selectedCell.coordinate().row(), selectedCell.coordinate().col()));

            collectPassages(passages, selectedCell);

            // Randomly select a passage from the collected passages
            Cell passage = passages.get(random.nextInt(passages.size()));
            passages.clear();

            // Calculate the wall coordinates to create a passage
            xWall = selectedCell.coordinate().col() / 2 + passage.coordinate().col() / 2 + 1;
            yWall = selectedCell.coordinate().row() / 2 + passage.coordinate().row() / 2 + 1;
            setCell(createRandomCell(yWall, xWall));

            cells.remove(selectedCell);
            cells.remove(passage);

        } while (!cells.isEmpty());

    }

    /**
     * Collects neighboring cells of the selected cell that can be
     * added to the maze. Cells are added based on the paths available.
     *
     * @param cells        the list to collect neighboring cells into.
     * @param selectedCell the currently selected cell to check for neighbors.
     */
    private void collectCells(List<Cell> cells, Cell selectedCell) {
        for (Direction direction : Direction.values()) {
            if (checkPath(grid[selectedCell.coordinate().row()][selectedCell.coordinate().col()], direction)) {
                xPassage = calculateCoordinate(selectedCell.coordinate(), direction, 2, false);
                yPassage = calculateCoordinate(selectedCell.coordinate(), direction, 2, true);
                if (!cells.contains(grid[yPassage][xPassage])) {
                    cells.add(grid[yPassage][xPassage]);
                }
            }
        }
    }

    /**
     * Collects neighboring passages of the selected cell that can
     * be used to create new passage in the maze.
     *
     * @param passages     the list to collect neighboring passages into.
     * @param selectedCell the currently selected cell to check for passages.
     */
    private void collectPassages(List<Cell> passages, Cell selectedCell) {
        for (Direction direction : Direction.values()) {
            if (checkIsPassage(grid[selectedCell.coordinate().row()][selectedCell.coordinate().col()], direction)) {

                xPassage = calculateCoordinate(selectedCell.coordinate(), direction, 2, false);
                yPassage = calculateCoordinate(selectedCell.coordinate(), direction, 2, true);
                passages.add(grid[yPassage][xPassage]);
            }
        }
    }

    /**
     * Checks if the specified cell has a passage in the given direction.
     *
     * @param point     the cell to check.
     * @param direction the direction to check for a passage.
     * @return true if there is a passage in the specified direction; false otherwise.
     */
    private boolean checkIsPassage(Cell point, Direction direction) {
        int localXWall = calculateCoordinate(point.coordinate(), direction, 1, false);
        int localYWall = calculateCoordinate(point.coordinate(), direction, 1, true);
        int localXPassage = calculateCoordinate(point.coordinate(), direction, 2, false);
        int localYPassage = calculateCoordinate(point.coordinate(), direction, 2, true);
        return grid[localYWall][localXWall].type() == Type.WALL
            && grid[localYPassage][localXPassage].type() != Type.DEFAULT;
    }

    @Override
    public String toString() {
        return "PrimGenerator";
    }
}
