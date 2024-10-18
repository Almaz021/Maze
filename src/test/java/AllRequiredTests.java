import backend.academy.MainInterface;
import backend.academy.entities.Cell;
import backend.academy.entities.Coordinate;
import backend.academy.entities.Maze;
import backend.academy.enums.Type;
import backend.academy.interfaces.Renderer;
import backend.academy.interfaces.Solver;
import backend.academy.renderer.BaseRenderer;
import backend.academy.services.StartService;
import backend.academy.settings.Settings;
import backend.academy.solvers.BFSSolver;
import backend.academy.solvers.DFSSolver;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AllRequiredTests {

    @Nested
    public class MazeTestingBlock {

        private Maze maze;
        private Coordinate start;
        private Coordinate end;

        @BeforeEach
        public void setUP() {
            Cell[][] grid = {
                {new Cell(new Coordinate(0, 0), Type.BEDROCK), new Cell(new Coordinate(0, 1), Type.BEDROCK),
                    new Cell(new Coordinate(0, 2), Type.BEDROCK), new Cell(new Coordinate(0, 3), Type.BEDROCK),
                    new Cell(new Coordinate(0, 4), Type.BEDROCK)},
                {new Cell(new Coordinate(1, 0), Type.BEDROCK), new Cell(new Coordinate(1, 1), Type.NORMAL),
                    new Cell(new Coordinate(1, 2), Type.NORMAL), new Cell(new Coordinate(1, 3), Type.NORMAL),
                    new Cell(new Coordinate(1, 4), Type.BEDROCK)},
                {new Cell(new Coordinate(2, 0), Type.BEDROCK), new Cell(new Coordinate(2, 1), Type.NORMAL),
                    new Cell(new Coordinate(2, 2), Type.WALL), new Cell(new Coordinate(2, 3), Type.NORMAL),
                    new Cell(new Coordinate(2, 4), Type.BEDROCK)},
                {new Cell(new Coordinate(3, 0), Type.BEDROCK), new Cell(new Coordinate(3, 1), Type.NORMAL),
                    new Cell(new Coordinate(3, 2), Type.WALL), new Cell(new Coordinate(3, 3), Type.NORMAL),
                    new Cell(new Coordinate(3, 4), Type.BEDROCK)},
                {new Cell(new Coordinate(4, 0), Type.BEDROCK), new Cell(new Coordinate(4, 1), Type.BEDROCK),
                    new Cell(new Coordinate(4, 2), Type.BEDROCK), new Cell(new Coordinate(4, 3), Type.BEDROCK),
                    new Cell(new Coordinate(4, 4), Type.BEDROCK)}
            };
            maze = new Maze(5, 5, grid);
            start = new Coordinate(1, 1);
            end = new Coordinate(3, 3);
        }

        @Test
        public void ableToFindPathDFSSolver() {
            Solver solver = new DFSSolver();

            List<Coordinate> list = solver.solve(maze, start, end);

            assertFalse(list.isEmpty());
        }

        @Test
        public void ableToFindPathBFSSolver() {
            Solver solver = new BFSSolver();

            List<Coordinate> list = solver.solve(maze, start, end);

            assertFalse(list.isEmpty());
        }

        @Test
        public void ableToFindNoPathDFSSolver() {
            Solver solver = new DFSSolver();
            maze.grid()[1][2] = new Cell(new Coordinate(1, 2), Type.WALL);

            List<Coordinate> list = solver.solve(maze, start, end);

            assertTrue(list.isEmpty());
        }

        @Test
        public void ableToFindNoPathBFSSolver() {
            Solver solver = new BFSSolver();

            maze.grid()[1][2] = new Cell(new Coordinate(1, 2), Type.WALL);

            List<Coordinate> list = solver.solve(maze, start, end);

            assertTrue(list.isEmpty());
        }

        @Test
        public void correctMazeDisplay() {
            Renderer renderer = new BaseRenderer();
            StringBuilder correctMazeString = new StringBuilder("""
                [38;2;195;161;255m██[0m[38;2;195;161;255m██[0m[38;2;195;161;255m██[0m[38;2;195;161;255m██[0m[38;2;195;161;255m██[0m
                [38;2;195;161;255m██[0m[38;2;0;0;0m██[0m[38;2;0;0;0m██[0m[38;2;0;0;0m██[0m[38;2;195;161;255m██[0m
                [38;2;195;161;255m██[0m[38;2;0;0;0m██[0m[38;2;240;240;240m██[0m[38;2;0;0;0m██[0m[38;2;195;161;255m██[0m
                [38;2;195;161;255m██[0m[38;2;0;0;0m██[0m[38;2;240;240;240m██[0m[38;2;0;0;0m██[0m[38;2;195;161;255m██[0m
                [38;2;195;161;255m██[0m[38;2;195;161;255m██[0m[38;2;195;161;255m██[0m[38;2;195;161;255m██[0m[38;2;195;161;255m██[0m
                """);

            StringBuilder actualMazeString = renderer.render(maze);

            assertEquals(0, correctMazeString.compareTo(actualMazeString));
        }
    }

    @Nested
    public class InputTestingBlock {
        private StartService startService;

        @BeforeEach
        public void setUp() {
            startService = new StartService(
                new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)),
                new BaseRenderer(),
                new SecureRandom(),
                new MainInterface(new PrintWriter(System.out, true, StandardCharsets.UTF_8)));
        }

        @Test
        public void checkSizeNullInput() {
            String s = null;

            int size = startService.checkSize(s, Settings.MIN_HEIGHT, Settings.MAX_HEIGHT);
            boolean result = (size >= Settings.MIN_HEIGHT && size <= Settings.MAX_HEIGHT && size % 2 != 0);

            assertTrue(result);
        }

        @Test
        public void checkSizeNonNumericInput() {
            String s = "null";

            int size = startService.checkSize(s, Settings.MIN_HEIGHT, Settings.MAX_HEIGHT);
            boolean result = (size >= Settings.MIN_HEIGHT && size <= Settings.MAX_HEIGHT && size % 2 != 0);

            System.out.println(size);

            assertTrue(result);
        }

        @Test
        public void checkSizeOutOfRangeInput() {
            String s = String.valueOf(Integer.MAX_VALUE);

            int size = startService.checkSize(s, Settings.MIN_HEIGHT, Settings.MAX_HEIGHT);
            boolean result = (size >= Settings.MIN_HEIGHT && size <= Settings.MAX_HEIGHT && size % 2 != 0);

            assertTrue(result);
        }

        @Test
        public void checkSizeNotOddNumberInput() {
            String s = "10";

            int size = startService.checkSize(s, Settings.MIN_HEIGHT, Settings.MAX_HEIGHT);
            boolean result = (size >= Settings.MIN_HEIGHT && size <= Settings.MAX_HEIGHT && size % 2 != 0);

            assertTrue(result);
        }

        @Test
        public void checkSizeCorrectInput() {
            String s = "19";

            int size = startService.checkSize(s, Settings.MIN_HEIGHT, Settings.MAX_HEIGHT);
            boolean result = (size >= Settings.MIN_HEIGHT && size <= Settings.MAX_HEIGHT && size % 2 != 0);

            assertTrue(result);
        }

        @Test
        public void checkCoordinatesEmptyInput() {
            String s = "";
            int height = 15;
            int width = 15;

            boolean result = startService.checkCoordinates(s, height, width);

            assertTrue(result);
        }

        @Test
        public void checkCoordinatesOnlyOneNum() {
            String s = "5";
            int height = 15;
            int width = 15;

            boolean result = startService.checkCoordinates(s, height, width);

            assertTrue(result);
        }

        @Test
        public void checkCoordinatesNonNumeric() {
            String s = "5 a";
            int height = 15;
            int width = 15;

            boolean result = startService.checkCoordinates(s, height, width);

            assertTrue(result);
        }

    }
}
