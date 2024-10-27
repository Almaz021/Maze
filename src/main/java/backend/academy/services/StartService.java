package backend.academy.services;

import backend.academy.MainInterface;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Type;
import backend.academy.factories.GeneratorFactory;
import backend.academy.factories.ModifierFactory;
import backend.academy.factories.SolverFactory;
import backend.academy.interfaces.Generator;
import backend.academy.interfaces.Modifier;
import backend.academy.interfaces.Solver;
import backend.academy.renderer.BaseRenderer;
import backend.academy.settings.Settings;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StartService {
    private final BufferedReader reader;
    private final BaseRenderer renderer;
    private final SecureRandom random;
    private final MainInterface mainInterface;
    private Maze maze;

    public void start() throws IOException {
        mainInterface.helloMessage();

        int width = getMazeWidth();

        int height = getMazeHeight();

        mainInterface.chosenSize(height, width);

        Generator generator = getMazeGenerator();

        Solver solver = getMazeSolver();

        generateMaze(generator, height, width);

        if (isModificationRequested()) {
            modifyMaze(height, width);
        }

        Coordinate firstPoint = getPointCoordinates(height, width);
        Coordinate secondPoint = getPointCoordinates(height, width);

        calculateAndShowPath(solver, firstPoint, secondPoint);

        mainInterface.finish();

    }

    public int getMazeWidth() throws IOException {
        mainInterface.chooseWidth();
        String w = getInput();
        return checkSize(w, Settings.MIN_WIDTH, Settings.MAX_WIDTH);
    }

    public int getMazeHeight() throws IOException {
        mainInterface.chooseHeight();
        String h = getInput();
        return checkSize(h, Settings.MIN_HEIGHT, Settings.MAX_HEIGHT);
    }

    public Generator getMazeGenerator() throws IOException {
        mainInterface.chooseGenerator();
        String g = getInput();
        Generator generator = selectGenerator(g);
        mainInterface.chosenGenerator(generator);
        return generator;
    }

    public Solver getMazeSolver() throws IOException {
        mainInterface.chooseSolver();
        String s = getInput();
        Solver solver = selectSolver(s);
        mainInterface.chosenSolver(solver);
        return solver;
    }

    public void generateMaze(Generator generator, int height, int width) {
        mainInterface.generateMaze();
        maze = generator.generate(height, width);
        StringBuilder mazeString = renderer.render(maze);
        mainInterface.showMaze(mazeString);
    }

    public boolean isModificationRequested() throws IOException {
        mainInterface.requestForModification();
        String answer = getInput();
        return "YES".equalsIgnoreCase(answer);
    }

    public void modifyMaze(int height, int width) throws IOException {
        mainInterface.chooseModifier();
        String m = getInput();
        Modifier modifier = selectModifier(m);
        mainInterface.chosenModifier(modifier);
        mainInterface.modifyMaze();
        maze = modifier.modify(height, width);
        StringBuilder mazeString = renderer.render(maze);
        mainInterface.showMaze(mazeString);
    }

    public Coordinate getPointCoordinates(int height, int width) throws IOException {
        mainInterface.choosePoint();
        mainInterface.choosePointCoordinates();
        String coords;
        do {
            coords = getInput();
        } while (checkCoordinates(coords, height, width));

        int firstPointX = Integer.parseInt(coords.split(" ")[0]);
        int firstPointY = Integer.parseInt(coords.split(" ")[1]);
        mainInterface.chosenPoint(firstPointX, firstPointY);
        return new Coordinate(firstPointY, firstPointX);
    }

    public void calculateAndShowPath(Solver solver, Coordinate firstPoint, Coordinate secondPoint) {
        mainInterface.calculatePath();
        List<Coordinate> path;
        path = solver.solve(maze, firstPoint, secondPoint);
        StringBuilder mazeString = renderer.render(maze, path);
        mainInterface.showMaze(mazeString);
    }

    public String getInput() throws IOException {
        String input = reader.readLine();
        return input == null ? "" : input;
    }

    public int checkSize(String size, int min, int max) {
        try {
            int s = Integer.parseInt(size);
            if (s >= min && s <= max && s % 2 != 0) {
                return s;
            } else {
                mainInterface.chooseRandom();
                return selectRandomSize(min, max);
            }
        } catch (NumberFormatException e) {
            mainInterface.chooseRandom();
            return selectRandomSize(min, max);
        }
    }

    public boolean checkCoordinates(String coordinates, int height, int width) {
        try {
            String[] parts = coordinates.split(" ");
            if (parts.length != 2) {
                mainInterface.onlyNumbers();
                return true;
            }
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            boolean inBounds = (0 <= x && x < width && 0 <= y && y < height);

            if (inBounds) {
                if (checkPassage(y, x)) {
                    return false;
                } else {
                    mainInterface.wrongPoint();
                }
            } else {
                mainInterface.incorrectCoordinates();
            }

            return true;
        } catch (NumberFormatException e) {
            mainInterface.onlyNumbers();
            return true;
        }
    }

    public boolean checkPassage(int y, int x) {
        return List.of(Type.NORMAL, Type.ICE, Type.SAND).contains(maze.grid()[y][x].type());
    }

    public int selectRandomSize(int min, int max) {
        int[] range = IntStream.rangeClosed(min, max).filter(n -> n % 2 != 0).toArray();
        return range[random.nextInt(range.length)];
    }

    public Generator selectGenerator(String generator) {
        return new GeneratorFactory(random).selectGenerator(generator);
    }

    public Solver selectSolver(String solver) {
        return new SolverFactory(random).selectSolver(solver);
    }

    public Modifier selectModifier(String modifier) {
        return new ModifierFactory(maze, random).selectModifier(modifier);
    }
}
