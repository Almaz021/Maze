package backend.academy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
    NORMAL(1), SAND(5), ICE(0), WALL(-1);
    private final int type;

    @Override public String toString() {
        return String.valueOf(type);
    }
}
