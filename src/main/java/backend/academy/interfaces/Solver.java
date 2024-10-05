package backend.academy.interfaces;

import backend.academy.Coordinate;
import backend.academy.Maze;
import java.util.List;

public interface Solver {
    List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end);
}
