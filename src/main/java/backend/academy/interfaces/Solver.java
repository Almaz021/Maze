package backend.academy.interfaces;

import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import java.util.List;

public interface Solver {
    List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end);
}
