package backend.academy.factories;

import backend.academy.entities.Maze;
import backend.academy.interfaces.Modifier;
import backend.academy.modifiers.NonIdealMazeModifier;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ModifierFactory {
    private final Maze maze;
    private final SecureRandom random;

    public Modifier selectModifier(String modifier) {
        return switch (modifier) {
            case "1" -> createNonIdealMazeModifier();
            default -> randomModifier();
        };
    }

    private Modifier createNonIdealMazeModifier() {
        return new NonIdealMazeModifier(maze.grid(), random);
    }

    private Modifier randomModifier() {
        Modifier[] modifiers = {
            createNonIdealMazeModifier()
        };
        return modifiers[random.nextInt(modifiers.length)];
    }
}
