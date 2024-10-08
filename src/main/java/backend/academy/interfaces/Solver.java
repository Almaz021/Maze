package backend.academy.interfaces;

import backend.academy.Maze;
import backend.academy.entities.Coordinate;
import java.util.List;

public interface Solver {
    List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end);
}
