package backend.academy.generators;

import backend.academy.entities.Cell;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;
import backend.academy.enums.Type;
import backend.academy.interfaces.Generator;
import java.security.SecureRandom;
import java.util.ArrayList;

public class PrimGenerator extends BaseGenerator implements Generator {
    public PrimGenerator(SecureRandom random) {
        super(random);
    }

    @Override
    public Maze generate(int height, int width) {
        fill(height, width);
        Cell startPoint = selectStartPoint(height, width);
        start(startPoint);
        return new Maze(height, width, grid);
    }

    public void start(Cell startPoint) {
        ArrayList<Cell> cells = new ArrayList<>();
        ArrayList<Cell> passages = new ArrayList<>();

        Cell selectedCell = createRandomCell(startPoint.coordinate().row(), startPoint.coordinate().col());
        setCell(selectedCell);

        do {
            collectCells(cells, selectedCell);

            selectedCell = cells.get(random.nextInt(cells.size()));
            setCell(createRandomCell(selectedCell.coordinate().row(), selectedCell.coordinate().col()));

            collectPassages(passages, selectedCell);

            Cell passage = passages.get(random.nextInt(passages.size()));
            passages.clear();

            int xWall = (int) ((long) selectedCell.coordinate().col() + passage.coordinate().col()) / 2;
            int yWall = (int) ((long) selectedCell.coordinate().row() + passage.coordinate().row()) / 2;

            setCell(createRandomCell(yWall, xWall));

            cells.remove(selectedCell);
            cells.remove(passage);

        } while (!cells.isEmpty());

    }

    public void collectCells(ArrayList<Cell> cells, Cell selectedCell) {
        for (Direction direction : Direction.values()) {
            if (checkPath(grid[selectedCell.coordinate().row()][selectedCell.coordinate().col()], direction)) {
                int xPassage = calculateCoordinateX(selectedCell.coordinate().col(), direction, 2);
                int yPassage = calculateCoordinateY(selectedCell.coordinate().row(), direction, 2);
                if (!cells.contains(grid[yPassage][xPassage])) {
                    cells.add(grid[yPassage][xPassage]);
                }
            }
        }
    }

    public void collectPassages(ArrayList<Cell> passages, Cell selectedCell) {
        for (Direction direction : Direction.values()) {
            if (checkIsPassage(grid[selectedCell.coordinate().row()][selectedCell.coordinate().col()], direction)) {

                int xPassage = calculateCoordinateX(selectedCell.coordinate().col(), direction, 2);
                int yPassage = calculateCoordinateY(selectedCell.coordinate().row(), direction, 2);
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
