package backend.academy.factories;

import backend.academy.entities.Maze;
import backend.academy.interfaces.Modifier;
import backend.academy.modifiers.NonIdealMazeModifier;
import java.security.SecureRandom;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

/**
 * Factory class for creating maze modifiers.
 * <p>
 * This class provides methods to select and instantiate different maze modifiers
 * based on user input or randomly choose one from the available options.
 */
@RequiredArgsConstructor
public class ModifierFactory {
    private final Modifier nonIdealMazeModifier = createNonIdealMazeModifier();
    private final Modifier[] modifiers = {nonIdealMazeModifier};

    private final Maze maze;
    private final SecureRandom random;

    public Modifier selectModifier(String modifier) {
        return switch (modifier) {
            case "1" -> createNonIdealMazeModifier();
            default -> getRandomModifier();
        };
    }

    private Modifier createNonIdealMazeModifier() {
        return new NonIdealMazeModifier(Objects.requireNonNull(maze).grid(), random);
    }

    private Modifier getRandomModifier() {
        return modifiers[random.nextInt(modifiers.length)];
    }
}
