package ui.gui;


import model.exception.NoShotsTakenException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Represents an action listener to control the buttons
 */
public class ButtonManger implements ActionListener {
    private static final String SWISH_SOUND = "data/swishSound.wav";
    private static final String MISS_SOUND = "data/missSound.wav";
    private static final String REMOVE_SOUND = "data/removeShotSound.wav";

    private int countClick = 0;
    private int indexOfLocation;
    private final ImageIcon ballImage = new ImageIcon("data/basketball.jpg");
    private final ImageIcon shotImage = new ImageIcon("data/shot.jpg");
    private final SessionPanel sessionPanel;
    private Clip swishSound;
    private Clip missSound;
    private Clip removeSound;

    // EFFECTS: assign fields and handle buttons
    public ButtonManger(SessionPanel sessionPanel) {
        this.sessionPanel = sessionPanel;
        handleButtons();
    }

    // MODIFIES: this
    // EFFECTS: adds action listeners to the buttons
    private void handleButtons() {
        for (JButton button : sessionPanel.getButtons()) {
            button.addActionListener(this);
        }
        sessionPanel.getScoreButton().addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: perform appropriate actions based on action listeners on buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sessionPanel.getScoreButton()) {
            JOptionPane.showMessageDialog(null, sessionPanel.getCurrentSession().toString());
            return;
        }
        if (countClick % 2 == 0) {
            countClick++;
            sessionPanel.getMadeButton().addActionListener(this);
            sessionPanel.getMissedButton().addActionListener(this);
            sessionPanel.getRemoveButton().addActionListener(this);
            selectShot(e);
        } else {
            sessionPanel.getMadeButton().removeActionListener(this);
            sessionPanel.getMissedButton().removeActionListener(this);
            sessionPanel.getRemoveButton().removeActionListener(this);
            countClick++;
            scoreShot(e);
        }


    }

    // MODIFIES: this
    // EFFECTS: change the image Icon of the selected shot location
    private void selectShot(ActionEvent e) {
        for (JButton button : sessionPanel.getButtons()) {
            if (e.getSource() == button) {
                button.setIcon(shotImage);
                indexOfLocation = sessionPanel.getButtons().indexOf(button);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds or removes a shot to/from the appropriate location of current session
    private void scoreShot(ActionEvent e) {
        if (e.getSource() == sessionPanel.getMadeButton()) {
            sessionPanel.getCurrentSession().getLocations().get(indexOfLocation).addShot(true);
            playSwishSound();
        } else if (e.getSource() == sessionPanel.getMissedButton()) {
            sessionPanel.getCurrentSession().getLocations().get(indexOfLocation).addShot(false);
            playMissSound();
        } else if (e.getSource() == sessionPanel.getRemoveButton()) {
            try {
                sessionPanel.getCurrentSession().getLocations().get(indexOfLocation).removeShot();
                playRemoveSound();
            } catch (NoShotsTakenException noShotsTakenException) {
                // no shots taken to remove, so remain at 0
            }
        }
        for (JButton button : sessionPanel.getButtons()) {
            button.setIcon(ballImage);
        }
    }

    // EFFECTS: plays the sound of missing a shot
    private void playMissSound() {
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File(MISS_SOUND));
            missSound = AudioSystem.getClip();
            missSound.open(input);
            missSound.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: plays the sound of making a shot
    private void playSwishSound() {
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File(SWISH_SOUND));
            swishSound = AudioSystem.getClip();
            swishSound.open(input);
            swishSound.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playRemoveSound() {
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File(REMOVE_SOUND));
            removeSound = AudioSystem.getClip();
            removeSound.open(input);
            removeSound.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
