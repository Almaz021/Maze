package backend.academy;

import backend.academy.services.ExceptionHandlerService;
import backend.academy.services.StartService;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        try {
            StartService startService = new StartService();
            startService.start();

        } catch (Exception e) {
            ExceptionHandlerService.handleException(e);
            System.exit(1);
        }
    }
}
