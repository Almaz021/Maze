package backend.academy.interfaces;

import backend.academy.entities.Maze;
import backend.academy.entities.Coordinate;
import java.util.List;

public interface Renderer {
    String render(Maze maze);

    String render(Maze maze, List<Coordinate> path);
}
