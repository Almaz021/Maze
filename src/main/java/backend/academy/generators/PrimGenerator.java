package backend.academy.generators;

import backend.academy.entities.Cell;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;
import backend.academy.enums.Type;
import backend.academy.interfaces.Generator;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class PrimGenerator extends BaseGenerator implements Generator {
    public PrimGenerator(SecureRandom random) {
        super(random);
    }

    @Override
    public Maze generate(int height, int width) {
        super.generate(height, width);
        start();
        return new Maze(height, width, grid);
    }

    public void start() {
        List<Cell> cells = new ArrayList<>();
        List<Cell> passages = new ArrayList<>();

        Cell selectedCell = startPoint;

        do {
            collectCells(cells, selectedCell);

            selectedCell = cells.get(random.nextInt(cells.size()));
            setCell(createRandomCell(selectedCell.coordinate().row(), selectedCell.coordinate().col()));

            collectPassages(passages, selectedCell);

            Cell passage = passages.get(random.nextInt(passages.size()));
            passages.clear();

            xWall = (int) ((long) selectedCell.coordinate().col() + passage.coordinate().col()) / 2;
            yWall = (int) ((long) selectedCell.coordinate().row() + passage.coordinate().row()) / 2;

            setCell(createRandomCell(yWall, xWall));

            cells.remove(selectedCell);
            cells.remove(passage);

        } while (!cells.isEmpty());

    }

    public void collectCells(List<Cell> cells, Cell selectedCell) {
        for (Direction direction : Direction.values()) {
            if (checkPath(grid[selectedCell.coordinate().row()][selectedCell.coordinate().col()], direction)) {
                xPassage = calculateCoordinateX(selectedCell.coordinate().col(), direction, 2);
                yPassage = calculateCoordinateY(selectedCell.coordinate().row(), direction, 2);
                if (!cells.contains(grid[yPassage][xPassage])) {
                    cells.add(grid[yPassage][xPassage]);
                }
            }
        }
    }

    public void collectPassages(List<Cell> passages, Cell selectedCell) {
        for (Direction direction : Direction.values()) {
            if (checkIsPassage(grid[selectedCell.coordinate().row()][selectedCell.coordinate().col()], direction)) {

                xPassage = calculateCoordinateX(selectedCell.coordinate().col(), direction, 2);
                yPassage = calculateCoordinateY(selectedCell.coordinate().row(), direction, 2);
                passages.add(grid[yPassage][xPassage]);
            }
        }
    }

    public boolean checkIsPassage(Cell point, Direction direction) {
        calculateWallAndPassage(point, direction);
        return grid[yWall][xWall].type() == Type.WALL && grid[yPassage][xPassage].type() != Type.DEFAULT;
    }

    @Override
    public String toString() {
        return "PrimGenerator";
    }
}
