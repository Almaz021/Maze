package backend.academy.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
    DEFAULT(-1), NORMAL(1), SAND(5), ICE(0), WALL(100), BEDROCK(1000);
    private final int type;

    @Override public String toString() {
        return String.valueOf(type);
    }
}
