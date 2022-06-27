package ui.gui;

import model.Session;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * represents the ShotTracker game panel
 */
public class SessionPanel extends JPanel {
    private final ImageIcon img = new ImageIcon("data/background.jpg");
    private final ImageIcon ballImage = new ImageIcon("data/basketball.jpg");
    private final JLabel jlCourt = new JLabel();
    private final JButton lc3 = new JButton(ballImage);
    private final JButton rc3 = new JButton(ballImage);
    private final JButton rw3 = new JButton(ballImage);
    private final JButton lw3 = new JButton(ballImage);
    private final JButton c3 = new JButton(ballImage);
    private final JButton ft = new JButton(ballImage);
    private final JButton pnt = new JButton(ballImage);
    private final JButton rlp = new JButton(ballImage);
    private final JButton llp = new JButton(ballImage);
    private final JButton re = new JButton(ballImage);
    private final JButton le = new JButton(ballImage);
    private final JButton removeButton = new JButton("REMOVE");
    private final JButton madeButton = new JButton("SWISH!");
    private final JButton missedButton = new JButton("MISS!");
    private final JButton scoreButton;
    private final List<JButton> buttons = new LinkedList<>();
    private final Session currentSession;

    // EFFECTS: starts a panel with all the buttons
    public SessionPanel(Session currentSession) {
        this.scoreButton = new JButton("<  <  <    " + currentSession.getName() + "    >  >  >");
        setupPanel();
        setupCourt();
        this.currentSession = currentSession;
        new ButtonManger(this);
    }

    // MODIFIES: this
    // EFFECTS: sets up the panel to appropriate size and layout
    private void setupPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setBackground(new Color(200, 170, 145));
    }

    // MODIFIES: this
    // EFFECTS: sets up court with appropriate buttons
    private void setupCourt() {
        jlCourt.setIcon(img);
        setupButtons();
        addButtonsToList();
        addButtons();
        add(jlCourt, BorderLayout.NORTH);

    }

    // MODIFIES: this
    // EFFECTS: sets buttons at appropriate coordinates
    private void setupButtons() {
        int width = 75;
        int height = 75;
        lc3.setBounds(14, 21, width, height);
        rc3.setBounds(892, 21, width, height);
        lw3.setBounds(130, 425, width, height);
        rw3.setBounds(782, 425, width, height);
        c3.setBounds(450, 545, width, height);
        ft.setBounds(450, 380, width, height);
        pnt.setBounds(455, 155, width, height);
        llp.setBounds(190, 100, width, height);
        rlp.setBounds(725, 100, width, height);
        re.setBounds(651, 318, width, height);
        le.setBounds(258, 318, width, height);
        madeButton.setBounds(6, 585, 155, 95);
        missedButton.setBounds(820, 585, 155, 95);
        scoreButton.setBounds(160, 651, 660, 30);
        removeButton.setBounds(415, 6, 150, 61);
    }

    //EFFECTS: adds the buttons to the list in the same order as session's list of locations
    private void addButtonsToList() {
        buttons.add(ft);
        buttons.add(pnt);
        buttons.add(rlp);
        buttons.add(llp);
        buttons.add(re);
        buttons.add(le);
        buttons.add(rc3);
        buttons.add(rw3);
        buttons.add(c3);
        buttons.add(lw3);
        buttons.add(lc3);
    }


    // MODIFIES: this
    // EFFECTS: adds all the buttons to this
    private void addButtons() {
        for (JButton button : buttons) {
            jlCourt.add(button);
        }
        jlCourt.add(madeButton);
        jlCourt.add(missedButton);
        jlCourt.add(scoreButton);
        jlCourt.add(removeButton);
        removeButton.setFont(new Font("Arial", Font.PLAIN, 25));
        madeButton.setFont(new Font("Arial", Font.PLAIN, 31));
        missedButton.setFont(new Font("Arial", Font.PLAIN, 31));
    }

    // getter
    public JButton getMadeButton() {
        return madeButton;
    }

    // getter
    public JButton getMissedButton() {
        return missedButton;
    }

    // getter
    public JButton getRemoveButton() {
        return removeButton;
    }

    // getter
    public JButton getScoreButton() {
        return scoreButton;
    }

    // getter
    public List<JButton> getButtons() {
        return buttons;
    }

    // getter
    public Session getCurrentSession() {
        return currentSession;
    }
}
