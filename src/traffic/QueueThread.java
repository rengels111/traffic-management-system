package traffic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class QueueThread extends Thread {

    private EnState SysState;

    public enum EnState {
        NOT_STARTED, MENU, SYSTEM;
    }

    public QueueThread() {
        super("QueueThread");
        this.SysState = EnState.NOT_STARTED;
    }

    public void setEnumState(EnState SysState) {
        this.SysState = SysState;
    }

    private int time = 0;

    private int roads = 0;

    private int interval = 0;

    private String input = "";

    private CircularQueue queue;

    private final Scanner sc = new Scanner(System.in);

    public int getTime() {
        return time;
    }

    private void addSecond() {
        time++;
    }

    public int getRoads() {
        return roads;
    }

    public int getInterval() {
        return interval;
    }

    public String getInput() {
        return input;
    }

    public void promptForRoads() {
        System.out.print("Input the number of roads: ");
        while(true) {
            try {
                this.roads = Integer.parseInt(sc.nextLine());
                if (this.roads > 0) {
                    this.queue = new CircularQueue(roads);
                    break;
                } else {
                    System.out.print("Error! Incorrect input. Try again: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Error! Incorrect input. Try again: ");
            }
        }
    }

    public void promptForInterval() {
        System.out.print("Input the interval: ");
        while (true) {
            try {
                this.interval = Integer.parseInt(sc.nextLine());
                if (this.interval > 0) {
                    break;
                } else {
                    System.out.print("Error! Incorrect input. Try again: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Error! Incorrect input. Try again: ");
            }
        }
    }

    public void promptForMenuSelection() {
        this.input = sc.nextLine();
    }

    public void addRoad() {
        System.out.print("Input road name: ");
        String roadName = sc.nextLine();
        queue.enqueue(roadName, interval);
    }

    public void deleteRoad() {
            queue.dequeue(interval);
    }

    synchronized public void printSystem() {
        System.out.printf("! %ds. have passed since system startup !%n", time);
        System.out.printf("! Number of roads: %d !%n", roads);
        System.out.printf("! Interval: %d !%n%n", interval);
        queue.iterateQueue();
        System.out.println();
        System.out.println("! Press \"Enter\" to open menu!");
    }



    @Override
    public void run() {
        while (!isInterrupted()) {
            if (this.SysState == EnState.SYSTEM) {
                printSystem();
            } else {
                this.SysState = EnState.MENU;
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
            addSecond();
            if (time > 1) {
                queue.upgradeRoadTimers(interval);
            }
        }
    }
}
