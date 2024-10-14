package backend.academy.generators;

import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Type;
import backend.academy.interfaces.Generator;
import java.security.SecureRandom;
import java.util.ArrayList;

public class PrimGenerator extends BaseGenerator implements Generator {
    public PrimGenerator(SecureRandom random) {
        super(random);
    }

    public Maze generate(int height, int width) {
        fill(height, width);
        Cell startPoint = selectStartPoint(height, width);
        start(startPoint);
        return new Maze(height, width, grid);
    }

    public void start(Cell startPoint) {
        ArrayList<Cell> cells = new ArrayList<>();
        ArrayList<Cell> passages = new ArrayList<>();
        Cell selectedCell =
            new Cell(new Coordinate(startPoint.coordinate().row(), startPoint.coordinate().col()), getRandomCellType());
        grid[startPoint.coordinate().row()][startPoint.coordinate().col()] = selectedCell;
        do {
            for (int i = 1; i < FIVE; i++) {
                if (checkPath(grid[selectedCell.coordinate().row()][selectedCell.coordinate().col()], i)) {
                    int xPassage = calculatePassageX(selectedCell.coordinate().col(), i);
                    int yPassage = calculatePassageY(selectedCell.coordinate().row(), i);
                    if (!cells.contains(grid[yPassage][xPassage])) {
                        cells.add(grid[yPassage][xPassage]);
                    }
                }
            }

            selectedCell = cells.get(random.nextInt(cells.size()));

            grid[selectedCell.coordinate().row()][selectedCell.coordinate().col()] =
                new Cell(new Coordinate(selectedCell.coordinate().row(), selectedCell.coordinate().col()),
                    getRandomCellType());

            for (int i = 1; i < FIVE; i++) {
                if (checkIsPassage(grid[selectedCell.coordinate().row()][selectedCell.coordinate().col()], i)) {

                    int xPassage = calculatePassageX(selectedCell.coordinate().col(), i);
                    int yPassage = calculatePassageY(selectedCell.coordinate().row(), i);
                    passages.add(grid[yPassage][xPassage]);
                }
            }
            Cell passage = passages.get(random.nextInt(passages.size()));
            passages.clear();

            int xWall = (int) ((long) selectedCell.coordinate().col() + passage.coordinate().col()) / 2;
            int yWall = (int) ((long) selectedCell.coordinate().row() + passage.coordinate().row()) / 2;
            grid[yWall][xWall] = new Cell(new Coordinate(yWall, xWall), getRandomCellType());

            cells.remove(selectedCell);
            cells.remove(passage);

        } while (!cells.isEmpty());

    }

    public boolean checkIsPassage(Cell point, int path) {
        calculateWallAndPassage(point, path);
        return grid[yWall][xWall].type() == Type.WALL && grid[yPassage][xPassage].type() != Type.DEFAULT;
    }

}
