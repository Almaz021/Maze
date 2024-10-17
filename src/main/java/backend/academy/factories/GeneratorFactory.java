package backend.academy.factories;

import backend.academy.generators.PrimGenerator;
import backend.academy.generators.RecursiveBacktrackingGenerator;
import backend.academy.interfaces.Generator;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GeneratorFactory {

    private final SecureRandom random;

    public Generator selectGenerator(String generator) {
        return switch (generator) {
            case "1" -> createRecursiveBacktrackingGenerator();
            case "2" -> createPrimGenerator();
            default -> randomGenerator();
        };
    }

    private Generator createRecursiveBacktrackingGenerator() {
        return new RecursiveBacktrackingGenerator(random);
    }

    private Generator createPrimGenerator() {
        return new PrimGenerator(random);
    }

    public Generator randomGenerator() {
        Generator[] generators = {
            createRecursiveBacktrackingGenerator(),
            createPrimGenerator()
        };
        return generators[random.nextInt(generators.length)];
    }
}
