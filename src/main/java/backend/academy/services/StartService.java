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
import lombok.extern.log4j.Log4j2;

@Log4j2 @RequiredArgsConstructor
public class StartService {
    private final BufferedReader reader;
    private final BaseRenderer renderer;
    private final SecureRandom random;
    private final MainInterface mainInterface;
    private Maze maze;

    public void start() throws IOException {
        Generator generator;
        Solver solver;
        Modifier modifier;
        mainInterface.helloMessage();

        mainInterface.chooseWidth();
        String w = getInput();
        int width = checkSize(w, Settings.MIN_WIDTH, Settings.MAX_WIDTH);

        mainInterface.chooseHeight();
        String h = getInput();
        int height = checkSize(h, Settings.MIN_HEIGHT, Settings.MAX_HEIGHT);

        mainInterface.chosenSize(height, width);

        mainInterface.chooseGenerator();
        String g = getInput();
        generator = selectGenerator(g);
        mainInterface.chosenGenerator(generator);

        mainInterface.chooseSolver();
        String s = getInput();
        solver = selectSolver(s);
        mainInterface.chosenSolver(solver);

        mainInterface.generateMaze();

        // Maze generation
        maze = generator.generate(height, width);
        StringBuilder mazeString = renderer.render(maze);
        mainInterface.showMaze(mazeString);

        mainInterface.requestForModification();
        String answer = getInput();
        if ("YES".equalsIgnoreCase(answer)) {
            mainInterface.chooseModifier();
            String m = getInput();
            modifier = selectModifier(m);
            mainInterface.chosenModifier(modifier);
            mainInterface.modifyMaze();
            modifier.modify(height, width);
            mazeString = renderer.render(maze);
            mainInterface.showMaze(mazeString);
        }

        String coords;
        int firstPointX;
        int firstPointY;
        int secondPointX;
        int secondPointY;

        mainInterface.choosePoint();
        mainInterface.choosePointCoordinates();
        do {
            coords = getInput();
        } while (checkCoordinates(coords, height, width));

        firstPointX = Integer.parseInt(coords.split(" ")[0]);
        firstPointY = Integer.parseInt(coords.split(" ")[1]);
        Coordinate firstPoint = new Coordinate(firstPointY, firstPointX);
        mainInterface.chosenPoint(firstPointX, firstPointY);

        mainInterface.choosePointCoordinates();
        do {
            coords = getInput();
        } while (checkCoordinates(coords, height, width));

        secondPointX = Integer.parseInt(coords.split(" ")[0]);
        secondPointY = Integer.parseInt(coords.split(" ")[1]);
        Coordinate secondPoint = new Coordinate(secondPointY, secondPointX);
        mainInterface.chosenPoint(secondPointX, secondPointY);

        mainInterface.calculatePath();

        List<Coordinate> path;
        path = solver.solve(maze, firstPoint, secondPoint);
        mazeString = renderer.render(maze, path);
        mainInterface.showMaze(mazeString);

        mainInterface.finish();

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
        int[] range = IntStream.iterate(min, n -> n + 2).limit(max / 2).toArray();
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
