package traffic;

import java.util.ArrayList;

public class CircularQueue {

    private String[] queue;  // Array zur Speicherung der Queue-Elemente
    private int front;  // Index des vordersten Elements
    private int rear;  // Index des hintersten Elements
    private int size;  // Maximale Kapazität der Queue
    private int count;  // Aktuelle Anzahl der Elemente in der Queue
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";


    public enum RoadState {
        OPEN, CLOSED;
    }

    ArrayList<Integer> roadTimers = new ArrayList<>();
    ArrayList<RoadState> roadStates = new ArrayList<>();

    public String getFrontElement() {
        return this.queue[front];
    }

    // Konstruktor zur Initialisierung der Circular Queue
    public CircularQueue(int size) {
        this.size = size;
        this.queue = new String[size];
        this.front = 0;
        this.rear = -1;
        this.count = 0;
    }

    // Methode zum Hinzufügen eines Elements zur Queue (Enqueue)
    public void enqueue(String item, int interval) {
        if (isFull()) {
            System.out.println("Queue is full");
            return;
        }

        // Rear um eine Position verschieben (zyklisch)
        rear = (rear + 1) % size;
        queue[rear] = item;  // Element hinzufügen


        // Hinzufügen des Zustands für die neue Straße
        if (count == 0) {
            // Erste Straße wird geöffnet
            roadStates.add(RoadState.OPEN);
            roadTimers.add(interval);
        } else {
            // Zusätzliche Straßen werden geschlossen hinzugefügt
            // Gibt es mehr als eine Straße
            if (count > 1) {
                roadStates.add(RoadState.CLOSED);
                // der Timer wird auf die vergangene Straße + Intervall gesetzt
                roadTimers.add(roadTimers.get(rear - 1) + interval);
                // bei nur einer weiteren Straße
            } else {
                roadStates.add(RoadState.CLOSED);
                roadTimers.add(roadTimers.get(rear - 1));
            }
        }

        System.out.println(item + " Added!");
        count++;
    }

    // Methode zum Entfernen eines Elements aus der Queue (Dequeue)
    public String dequeue(int interval) {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return null;  // Fehlerwert
        }

        String item = queue[front];  // Element am Anfang der Queue

        // Front um eine Position verschieben (zyklisch)
        front = (front + 1) % size;
        count--;  // Anzahl der Elemente verringern



        System.out.println(item + " deleted!");
        return item;
    }

    // Methode zur Überprüfung, ob die Queue leer ist
    public boolean isEmpty() {
        return count == 0;
    }

    // Methode zur Überprüfung, ob die Queue voll ist
    public boolean isFull() {
        return count == size;
    }

    // Durch die Elemente der Queue iterieren
    public void iterateQueue() {
        if (isEmpty()) {
            return;
        }

        int currentIndex = front;
        int elementsCounted = 0;

        while (elementsCounted < count) {
            // Sicherstellen, dass currentIndex im gültigen Bereich ist
//            if (currentIndex >= roadTimers.size()) {
//                break; // Vermeidet IndexOutOfBoundsException
//            }

            String roadStatus = roadStates.get(currentIndex) == RoadState.OPEN ? "open" : "closed";
            String color = roadStates.get(currentIndex) == RoadState.OPEN ? ANSI_GREEN : ANSI_RED;
            int timeToChange = roadTimers.get(currentIndex);

            System.out.printf("%s%s will be %s for %ds.%s\n", color, queue[currentIndex], roadStatus, timeToChange,
                    ANSI_RESET);
            currentIndex = (currentIndex + 1) % size; // zyklische Erhöhung des Index
            elementsCounted++;
        }
    }


    public void upgradeRoadTimers(int interval) {
        int numOfRoads = count; // Anzahl der eingetragenen Straßen

        // Wenn keine Straßen vorhanden sind, abbrechen
        if (numOfRoads == 0) return;

        // Sonderfall: Wenn nur eine Straße existiert, finde den korrekten Index der Straße
        if (numOfRoads == 1) {
            int currentIndex = front; // Die verbleibende Straße befindet sich bei 'front'

            // einzige Straße immer offen lassen
            roadStates.set(currentIndex, RoadState.OPEN);

            // Aktualisieren Sie den Timer dieser einen Straße
            int currentTimer = roadTimers.get(currentIndex);
            if (currentTimer > 1) {
                roadTimers.set(currentIndex, currentTimer - 1); // Reduzieren Sie den Timer
            } else {
                // Wenn der Timer 0 erreicht, ändern wir den Zustand und setzen den Timer zurück
                roadTimers.set(currentIndex, interval); // Reset Timer nach dem Umschalten
            }
            return; // Methode verlassen, da keine weiteren Straßen bearbeitet werden müssen
        }

        // Allgemeiner Fall für mehr als eine Straße
        for (int i = 0; i < roadTimers.size(); i++) {
            // Reduziere den Timer der aktuellen Straße um 1 Sekunde
            if (roadTimers.get(i) > 0) {
                roadTimers.set(i, roadTimers.get(i) - 1);
            }

            // Wenn der Timer auf 0 gesunken ist, ändern wir den Zustand
            if (roadTimers.get(i) == 0) {

                // Wenn die Straße aktuell offen ist, schließen wir sie und öffnen die nächste Straße
                if (roadStates.get(i) == RoadState.OPEN) {
                    roadStates.set(i, RoadState.CLOSED);

                    roadTimers.set(i, (numOfRoads - 1) * interval); // Timer für geschlossene Straße setzen

                } else {
                    roadStates.set(i, RoadState.OPEN);
                    roadTimers.set(i, interval);
                }
            }
        }
    }



}