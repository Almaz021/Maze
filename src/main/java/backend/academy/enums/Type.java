package backend.academy.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing different cell types in the maze.
 * <p>
 * Each type corresponds to a specific property or functionality of a cell
 * within the maze grid. The values may represent different terrain types,
 * obstacles, or special points (like start and end points).
 */
@Getter
@RequiredArgsConstructor
public enum Type {
    DEFAULT(-1),
    NORMAL(1),
    SAND(5),
    ICE(0),
    WALL(100),
    BEDROCK(1000),
    START(Integer.MIN_VALUE),
    END(Integer.MAX_VALUE);

    private final int type;

    @Override public String toString() {
        return String.valueOf(type);
    }
}
