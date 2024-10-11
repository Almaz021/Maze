package backend.academy.interfaces;

import backend.academy.entities.Maze;

public interface Modifier {
    Maze modify(int height, int width);
}
