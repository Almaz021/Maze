package backend.academy.enums;

import lombok.Getter;

/**
 * Enum representing the four cardinal directions.
 * <p>
 * This enum can be used to specify movement or orientation in a grid-based
 * environment, such as in maze generation or pathfinding algorithms.
 */
@Getter
public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT
}
