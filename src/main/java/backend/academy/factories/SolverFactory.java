package backend.academy.factories;

import backend.academy.interfaces.Solver;
import backend.academy.solvers.BFSSolver;
import backend.academy.solvers.DFSSolver;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SolverFactory {

    private final SecureRandom random;

    public Solver selectSolver(String solver) {
        return switch (solver) {
            case "1" -> createBFSSolver();
            case "2" -> createDFSSolver();
            default -> randomSolver();
        };
    }

    private Solver createBFSSolver() {
        return new BFSSolver();
    }

    private Solver createDFSSolver() {
        return new DFSSolver();
    }

    public Solver randomSolver() {
        Solver[] solvers = {
            createBFSSolver(),
            createDFSSolver()
        };
        return solvers[random.nextInt(solvers.length)];
    }
}
