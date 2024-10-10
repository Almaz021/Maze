package backend.academy.interfaces;

import backend.academy.entities.Maze;

public interface Generator {
    Maze generate(int height, int width);
}
