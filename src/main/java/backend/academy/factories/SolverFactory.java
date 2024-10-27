package backend.academy.factories;

import backend.academy.interfaces.Solver;
import backend.academy.solvers.BFSSolver;
import backend.academy.solvers.DFSSolver;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;

/**
 * Factory class for creating maze solvers.
 * <p>
 * This class provides methods to select and instantiate different maze solvers
 * based on user input or randomly choose one from the available options.
 */
@RequiredArgsConstructor
public class SolverFactory {

    private final SecureRandom random;

    public Solver selectSolver(String solver) {
        return switch (solver) {
            case "1" -> createBFSSolver();
            case "2" -> createDFSSolver();
            default -> getRandomSolver();
        };
    }

    private Solver createBFSSolver() {
        return new BFSSolver();
    }

    private Solver createDFSSolver() {
        return new DFSSolver();
    }

    private Solver getRandomSolver() {
        Solver[] solvers = {
            createBFSSolver(),
            createDFSSolver()
        };
        return solvers[random.nextInt(solvers.length)];
    }
}
