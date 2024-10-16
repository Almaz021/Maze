package backend.academy.interfaces;

import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import java.util.List;

public interface Renderer {
    StringBuilder render(Maze maze);

    StringBuilder render(Maze maze, List<Coordinate> path);
}
