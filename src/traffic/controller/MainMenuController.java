package traffic.controller;

import traffic.QueueThread;

import java.io.IOException;

public class MainMenuController implements Runnable {

    private static final String MENU_TEXT = """
            Menu:
            1. Add
            2. Delete
            3. System
            0. Quit""";


    private final QueueThread queueThread = new QueueThread();

    private void printWelcome() {
        System.out.println("Welcome to the traffic management system!");
    }



    private void printMenu() {
        System.out.println(MENU_TEXT);
    }

    private void setRoads() {
        queueThread.promptForRoads();
    }

    private void setInterval() {
        queueThread.promptForInterval();
    }

    private void controlMenu() throws IOException {
        while(true) {
            printMenu();
            queueThread.promptForMenuSelection();

            String choice = queueThread.getInput();
            switch (choice) {
                case "1":
                    queueThread.addRoad();
                    break;
                case "2":
                    queueThread.deleteRoad();
                    break;
                case "3":
                    queueThread.setEnumState(QueueThread.EnState.SYSTEM);
                    clearCommand();
                    break;
                case "0":
                    System.out.println("Bye!");
                    queueThread.interrupt();
                    return;
                default:
                    System.out.println("Incorrect option");
            }
            waitForUserInput();
        }
    }

    private void waitForUserInput() {
        try {
            System.in.read();
            queueThread.setEnumState(QueueThread.EnState.MENU);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearCommand() {
        try {
            var clearCommand = System.getProperty("os.name").contains("Windows")
                    ? new ProcessBuilder("cmd", "/c", "cls")
                    : new ProcessBuilder("clear");
            clearCommand.inheritIO().start().waitFor();
        }
        catch (IOException | InterruptedException e) {}
    }


    @Override
    public void run() {
        printWelcome();
        setRoads();
        setInterval();
        queueThread.start();
        try {
            controlMenu();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}