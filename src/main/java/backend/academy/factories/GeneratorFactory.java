package backend.academy.factories;

import backend.academy.generators.PrimGenerator;
import backend.academy.generators.RecursiveBacktrackingGenerator;
import backend.academy.interfaces.Generator;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;

/**
 * Factory class for creating maze generator instances.
 * <p>
 * This class provides methods to select and instantiate different maze generator algorithms
 * based on user input or randomly choose one from the available options.
 */
@RequiredArgsConstructor
public class GeneratorFactory {

    private final SecureRandom random;

    public Generator selectGenerator(String generator) {
        return switch (generator) {
            case "1" -> createRecursiveBacktrackingGenerator();
            case "2" -> createPrimGenerator();
            default -> getRandomGenerator();
        };
    }

    private Generator createRecursiveBacktrackingGenerator() {
        return new RecursiveBacktrackingGenerator(random);
    }

    private Generator createPrimGenerator() {
        return new PrimGenerator(random);
    }

    private Generator getRandomGenerator() {
        Generator[] generators = {
            createRecursiveBacktrackingGenerator(),
            createPrimGenerator()
        };
        return generators[random.nextInt(generators.length)];
    }
}
