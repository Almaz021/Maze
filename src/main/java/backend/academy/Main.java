package backend.academy;

import backend.academy.renderer.BaseRenderer;
import backend.academy.services.ExceptionHandlerService;
import backend.academy.services.StartService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import lombok.experimental.UtilityClass;

/**
 * The entry point of the application that initializes and starts the maze-solving process.
 */
@UtilityClass
public class Main {
    public static void main(String[] args) {
        try {
            StartService startService = new StartService(
                new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)),
                new BaseRenderer(),
                new SecureRandom(),
                new MainInterface(new PrintWriter(System.out, true, StandardCharsets.UTF_8)));
            startService.start();

        } catch (Exception e) {
            ExceptionHandlerService.handleException(e);
            System.exit(1);
        }
    }
}
