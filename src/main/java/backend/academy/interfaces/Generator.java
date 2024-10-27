package backend.academy.interfaces;

import backend.academy.entities.Cell;
import backend.academy.entities.Maze;
import backend.academy.enums.Direction;

public interface Generator {
    Maze generate(int height, int width);

    void fill(int height, int width);

    Cell selectStartPoint(int height, int width);

    void setCell(Cell cell);

    boolean checkPath(Cell point, Direction direction);

}
