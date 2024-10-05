package backend.academy.interfaces;

import backend.academy.Coordinate;
import backend.academy.Maze;
import java.util.List;

public interface Renderer {
    String render(Maze maze);

    String render(Maze maze, List<Coordinate> path);
}
