package ui.gui;


import model.Session;
import model.Sessions;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * represents the main GUI frame
 */

public class ShotTrackerGUI extends JFrame {
    private static final String JSON_STORE = "./data/shotTracker.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JPanel sessionPanel;
    private List<Session> sessions;
    private Session currentSession;
    private JMenuBar mb;
    private JMenu fileMenu;
    private JMenu viewMenu;
    private JMenuItem fmStartSession;
    private JMenuItem fmSaveSession;
    private JMenuItem fmLoadSession;
    private List<JMenuItem> vmSessionItems;
    private int sessionNumber = 1;

    // EFFECTS: starts the ShotTracker gui
    public ShotTrackerGUI() {
        super("Shot Tracker");
        initializeFields();
        frameSetup();
        addComponents();
    }

    // MODIFIES: this
    // EFFECTS: initializes fields in this
    private void initializeFields() {
        this.sessions = new ArrayList<>();
        String name = "Session " + sessionNumber;
        this.currentSession = new Session(name);
        sessionPanel = new SessionPanel(currentSession);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        this.vmSessionItems = new LinkedList<>();
    }

    // MODIFIES: this
    // EFFECTS: sets up frame's size and basic operations
    private void frameSetup() {
        setSize(989, 750);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);    //puts the screen frame in the middle
        setResizable(false);
    }

    // MODIFIES: this
    // EFFECTS: adds the menu bar and sessionPanel to this
    private void addComponents() {
        addMenu();
        addMenuActionListeners();
        add(sessionPanel);
        pack();
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: adds the JMenuBar and its corresponding JMenus and JMenu items
    private void addMenu() {
        mb = new JMenuBar();
        fileMenu = new JMenu("File"); //JMenu
        viewMenu = new JMenu("View"); //JMenu
        mb.add(fileMenu);               // adding JMenu to JMenuBar
        mb.add(viewMenu);

        fmStartSession = new JMenuItem("New Session"); //initializing JMenuItems
        fmSaveSession = new JMenuItem("Save");
        fmLoadSession = new JMenuItem("Load");

        fileMenu.add(fmStartSession);          //adding JMenuItems to JMenu
        fileMenu.add(fmSaveSession);
        fileMenu.add(fmLoadSession);

        setJMenuBar(mb);     // sets it on frame
    }

    // MODIFIES: this
    // EFFECTS: calls all the necessary action listeners
    private void addMenuActionListeners() {
        startSessionActionListener();
        saveSessionActionListener();
        loadSessionActionListener();
        viewSessionActionListener();
    }

    // MODIFIES: this
    // EFFECTS: starts a new session
    private void startSessionActionListener() {
        fmStartSession.addActionListener(e -> {
            remove(sessionPanel);
            currentSession = new Session("Session " + sessionNumber);
            sessionPanel = new SessionPanel(currentSession);
            add(sessionPanel);
            setVisible(true);
        });
    }

    // MODIFIES: this
    // EFFECTS: saves the currentSession to sessions and updates JMenuBar
    private void saveSessionActionListener() {
        fmSaveSession.addActionListener(e -> {
            if (sessions.contains(currentSession)) {
                System.out.print(currentSession.getName() + " is already saved to " + JSON_STORE + "! ");
            } else {
                sessionNumber++;
                sessions.add(currentSession);
                JMenuItem menuItem = new JMenuItem(currentSession.getName());
                menuItem.setName(currentSession.getName());
                viewMenu.add(menuItem);
                vmSessionItems.add(menuItem);
                try {
                    jsonWriter.open();
                    jsonWriter.write(new Sessions(sessions));
                    jsonWriter.close();
                    System.out.print("Saved " + currentSession.getName() + " to " + JSON_STORE);
                } catch (FileNotFoundException d) {
                    System.out.println("Unable to write to file: " + JSON_STORE);
                }
            }
            viewSessionActionListener();
        });
    }

    // MODIFES: this
    // EFFECTS: loads the saved sessions and adds them to JMenu as JMenuItems
    private void loadSessionActionListener() {
        fmLoadSession.addActionListener(e -> {
            try {
                Sessions loadedSessions = jsonReader.read();
                sessions = loadedSessions.getSessions();
                System.out.println("Loaded recordings from " + JSON_STORE + "\n> ");
            } catch (IOException d) {
                System.out.println("Unable to read from file: " + JSON_STORE);
            }
            //remove previous JMenuItems
            viewMenu.removeAll();
            // load JMenuItems
            for (Session session : sessions) {
                JMenuItem menuItem = new JMenuItem(session.getName());
                menuItem.setName(session.getName());
                viewMenu.add(menuItem);
                vmSessionItems.add(menuItem);
            }
            remove(sessionPanel);
            sessionPanel = new SessionPanel(currentSession);
            add(sessionPanel);
            setVisible(true);
            revalidate();
            repaint();
            viewSessionActionListener();
        });
    }

    // MODIFIES: this
    // EFFECTS: views the currentSession report when the report session button is pressed
    private void viewSessionActionListener() {
        for (JMenuItem menuItem : vmSessionItems) {
            menuItem.addActionListener(e -> {
                System.out.println(menuItem.getName());
                for (Session session : sessions) {
                    if (session.getName().equals(menuItem.getName())) {
                        currentSession = session;
                    }
                }

                remove(sessionPanel);
                sessionPanel = new SessionPanel(currentSession);
                add(sessionPanel);
                setVisible(true);
                revalidate();
                repaint();
            });
        }

    }
}
