package backend.academy.interfaces;

import backend.academy.Maze;

public interface Generator {
    Maze generate(int height, int width);
}
