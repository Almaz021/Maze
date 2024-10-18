package backend.academy.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Color {
    BLACK("\u001B[38;2;0;0;0m"),
    WHITE("\u001B[38;2;240;240;240m"),
    GREEN("\u001B[38;2;21;212;0m"),
    RED("\u001B[38;2;222;150;150m"),
    CYAN("\u001B[38;2;123;255;255m"),
    PURPLE("\u001B[38;2;195;161;255m"),
    YELLOW("\u001B[38;2;255;248;163m"),
    ORANGE("\u001B[38;2;255;111;5m"),
    RESET("\u001B[0m");

    private final String colorCode;

    @Override
    public String toString() {
        return colorCode;
    }
}
