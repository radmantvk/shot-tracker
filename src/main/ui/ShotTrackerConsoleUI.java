package ui;

import model.Location;
import model.Session;
import model.Sessions;
import model.exception.NoShotsTakenException;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Shot Tracker user interface
 */

public class ShotTrackerConsoleUI {

    private static final String JSON_STORE = "./data/shotTracker.json";
    private static final boolean ADD = true;
    private static final boolean REMOVE = false;
    private static final boolean MADE = true;
    private static final boolean MISSED = false;

    private List<Session> savedSessions;
    private final List<Session> sessions;
    private Scanner input;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;


    // EFFECTS: instantiates scanner and sessions and runs the Shot Tracker
    public ShotTrackerConsoleUI() {
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        savedSessions = new ArrayList<>();
        sessions = new ArrayList<>();
        runShotTracker();
    }

    // MODIFIES: this
    // EFFECTS: displays menu and receives input
    //          if "q" is selected, exit the program; otherwise processes user input
    public void runShotTracker() {
        System.out.println("Welcome to the Shot Tracker Program!");
        displayMainMenu();
        boolean keepGoing = true;

        while (keepGoing) {
            String command = getInput();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processShotTracker(command);
            }
        }
        System.out.println("\nGoodbye!");
    }

    // EFFECTS: processes user input
    public void processShotTracker(String command) {
        switch (command) {
            case "s":
                startNewSession();
                break;
            case "v":
                viewSessions();
                break;
            case "l":
                loadSession();
                break;
            case "q":
                System.out.println("Goodbye!");
                break;
            default:
                tryAgainMessage();
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: displays session menu and receives input
    //          stops when "e" is received as input
    private void runSession(Session session) {
        displaySessionMenu(session.getName());
        boolean keepGoing = true;

        while (keepGoing) {
            String command = getInput();

            if (command.equals("e")) {
                keepGoing = false;
            } else {
                processSession(session, command);
            }
        }
        displayMainMenu();
    }

    // EFFECTS: processes user input
    public void processSession(Session session, String command) {
        switch (command) {
            case "a":
                runShot(ADD, session);
                displaySessionMenu(session.getName());
                break;
            case "r":
                runShot(REMOVE, session);
                displaySessionMenu(session.getName());
                break;
            case "v":
                System.out.println(session.toString());
                displaySessionMenu(session.getName());
                break;
            case "s":
                saveSession(session);
                break;
            default:
                tryAgainMessage();
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: Starts a new session with the given name and adds it to the list of sessions
    //          runSession
    private void startNewSession() {
        System.out.print("What would you like to name your new session?\n> ");
        input = new Scanner(System.in);
        String command = input.nextLine().trim();
        Session newSession = new Session(command);
        sessions.add(newSession);
        runSession(newSession);
    }


    // MODIFIES: this
    // EFFECTS: displays shooting locations; Until the input is found, displays retry messaege. Once found, calls
    //          runAddShot or RemoveShot accordingly
    public void runShot(boolean isAdding, Session session) {
        displayShootingLocation();
        boolean keepGoing = true;
        boolean inputLocationFound = false;
        String command;
        while (keepGoing) {
            command = getInputLine();
            for (Location location : session.getLocations()) {
                String name = location.getName().trim().toLowerCase().replace(" ", "");
                if (command.equals(name)) {
                    if (isAdding) {
                        runAddShot(location);
                    } else {
                        removeShot(location);
                    }
                    keepGoing = false;
                    inputLocationFound = true;
                }
            }
            if (!inputLocationFound) {
                tryAgainMessage();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: displays make or miss options, and adds the shot accordingly. If wrong input, try again message
    private void runAddShot(Location location) {
        displayMakeOrMiss();
        boolean keepGoing = true;
        while (keepGoing) {
            String command = getInputLine();
            if (command.equals("1")) {
                location.addShot(MADE);
                System.out.println("A shot is made at " + location.getName() + "!");
                keepGoing = false;
            } else if (command.equals("2")) {
                location.addShot(MISSED);
                System.out.println("A shot is missed at " + location.getName() + "!");
                keepGoing = false;
            } else {
                tryAgainMessage();
            }
        }
    }

    // MODIFIES: this and Location
    // EFFECTS: if no shots taken exception is caught, prints an error message;
    //          otherwise removes the last shot from the location along a print message
    private void removeShot(Location location) {
        try {
            location.removeShot();
            System.out.println("Last shot is removed from " + location.getName() + "!");
        } catch (NoShotsTakenException e) {
            System.out.println("There are no recorded shots at this location!");
        }
    }

    // EFFECTS: saves the session to file
    private void saveSession(Session session) {
        if (savedSessions.contains(session)) {
            System.out.print(session.getName() + " is already saved to " + JSON_STORE + "!\n> ");
        } else {
            savedSessions.add(session);

            try {
                jsonWriter.open();
                jsonWriter.write(new Sessions(savedSessions));
                jsonWriter.close();
                System.out.print("Saved " + session.getName() + " to " + JSON_STORE + "\n> ");
            } catch (FileNotFoundException e) {
                System.out.println("Unable to write to file: " + JSON_STORE);
            }
        }
    }

    // EFFECTS: loads sessions from file
    private void loadSession() {
        try {
            Sessions loadedSessions = jsonReader.read();
            savedSessions = loadedSessions.getSessions();

            for (Session session : savedSessions) {
                sessions.add(session);
            }

            System.out.print("Loaded recordings from " + JSON_STORE + "\n> ");
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    //  EFFECTS: prints the recorded sessions
    private void viewSessions() {
        if (sessions.isEmpty()) {
            System.out.println("You have not created a session yet!\n> ");
        } else {
            for (Session session : sessions) {
                System.out.print(session.toString() + "\n");
            }
            System.out.print("> ");
        }
    }

    // EFFECTS: prints the main menu select options
    private void displayMainMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\ts -> start a new shooting session");
        System.out.println("\tv -> view all sessions");
        System.out.println("\tl -> load recorded sessions");
        System.out.print("\tq -> quit" + "\n> ");
    }

    // EFFECTS: prints the session menu select options
    private void displaySessionMenu(String name) {
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add a shot");
        System.out.println("\tr -> remove the previous shot");
        System.out.println("\tv -> view the session");
        System.out.println("\ts -> save the session");
        System.out.print("\te -> end the session: " + name + "\n> ");
    }

    // EFFECTS: prints the shooting locations select options
    private void displayShootingLocation() {
        System.out.println("\nSelect from:");
        for (Location location : sessions.get(0).getLocations()) {
            System.out.println("\t" + location.getName());
        }
        System.out.print("\n> ");
    }

    // EFFECTS: prints the shot type selection
    private void displayMakeOrMiss() {
        System.out.println("\nSelect from:");
        System.out.println("\t1 -> shot is made");
        System.out.print("\t2 -> shot is missed\n> ");
    }

    // EFFECTS: prints a wrong user input error message
    private void tryAgainMessage() {
        System.out.print("Please try again!" + "\n> ");
    }

    // MODIFIES: this
    // EFFECTS: scans and returns the next user input in lowercase and trimmed and spaces removed
    private String getInput() {
        input = new Scanner(System.in);
        return input.next().toLowerCase().trim().replace(" ", "");
    }

    //Effects: scans and returns the user input line in lower case and trimmed and spaces removed
    private String getInputLine() {
        input = new Scanner(System.in);
        return input.nextLine().toLowerCase().trim().replaceAll(" ", "");
    }
}
