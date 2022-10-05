package ui;

import model.Event;
import model.EventLog;
import model.Log;
import model.Member;
import model.exceptions.NegativeValueException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;

// GUI class for UI
// Navigate regions using shortcut Command + Option + Period (MAC), Ctrl + Alt + Period (PC)
public class GUI implements ActionListener, ListSelectionListener {

    // Fields for Main Menu
    private JFrame mainMenu;
    private Panel firstPanel;
    private Panel secondPanel;
    private JLabel title;


    // Fields for Buttons in Main Menu
    private JButton addPersonButton;
    private JButton removePersonButton;
    private JButton addWeightButton;
    private JButton checkLogButton;
    private JButton saveButton;
    private JButton loadButton;

    // Field for array list of members
    protected ArrayList<Member> family;

    // Fields for adding member method
    private Member member;
    private JFrame addMemberFrame;
    private JButton submitButton;
    private JTextField name;
    private JTextField height;
    private JLabel nameTitle;
    private JLabel heightTitle;
    private JLabel mainTitle;

    // Fields for removing member method
    private JLabel subTitle;
    private JButton deleteButton;
    private JList<String> namesOfMembers;
    private JFrame deleteMemberFrame;
    private JScrollPane listScroller;
    private DefaultListModel<String> listModel;

    // Fields for adding weight log to a member
    private JFrame addWeightLogFrame;
    private JTextField weightField;

    // Fields for JSON writer and reader
    private static final String JSON_STORE = "./data/family.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    // GUI constructor that initializes main menu and JSON
    public GUI() {


        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        family = new ArrayList<>();

        // example members
        Member ethan = new Member("Ethan", 183);
        Member bryan = new Member("Bryan", 165);
        family.add(ethan);
        family.add(bryan);

        initMain();
        createMenuButtons();
        addToFrameAndPanel();

        Runtime r = Runtime.getRuntime();
        r.addShutdownHook(new MyThread());
    }

    class MyThread extends Thread {
        public void run() {
            for (Event e : EventLog.getInstance()) {
                System.out.println(e.getDescription());
            }
        }
    }



    // MODIFIES: this
    // EFFECTS: creates main menu frame and first panel and second panel
    private void initMain() {

        // create main menu frame
        mainMenu = new JFrame();
        mainMenu.setTitle("Family Weight Tracker");
        mainMenu.setSize(500, 500);
        mainMenu.setResizable(false);
        mainMenu.setVisible(true);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setSize(500, 700);
        mainMenu.setLayout(null);

        // create first panel
        title = new JLabel("Family Weight Tracker");
        firstPanel = new Panel();
        firstPanel.setLayout(null);
        firstPanel.setBackground(Color.decode("#F8EDEB"));
        firstPanel.setBounds(0, 0, 500, 75);
        firstPanel.add(title);
        title.setFont(new Font("Roboto", Font.PLAIN, 25));
        title.setBounds(120, 10, 300, 50);

        // create second panel
        secondPanel = new Panel();
        secondPanel.setBounds(0, 50, 500, 700);
        secondPanel.setBackground(Color.decode("#FEC89A"));
        secondPanel.setLayout(null);
    }

    // MODIFIES: this
    // EFFECTS: adds panels and buttons to corresponding component
    private void addToFrameAndPanel() {
        mainMenu.add(firstPanel);
        mainMenu.add(secondPanel);
        secondPanel.add(addPersonButton);
        secondPanel.add(removePersonButton);
        secondPanel.add(addWeightButton);
        secondPanel.add(checkLogButton);
        secondPanel.add(saveButton);
        secondPanel.add(loadButton);
    }

    // EFFECTS: creates buttons for the main menu
    private void createMenuButtons() {
        addPersonButton();
        removePersonButton();
        addWeightButton();
        checkLogButton();
        saveButton();
        loadButton();
    }

    // MODIFIES: this
    // EFFECTS: creates the load button
    private void loadButton() {
        loadButton = new JButton();
        loadButton.addActionListener(this);
        loadButton.setText("Load Family");
        loadButton.setBounds(150, 425, 200, 50);
    }

    // MODIFIES: this
    // EFFECTS: creates the save button
    private void saveButton() {
        saveButton = new JButton();
        saveButton.addActionListener(this);
        saveButton.setText("Save Family");
        saveButton.setBounds(150, 350, 200, 50);
    }

    // MODIFIES: this
    // EFFECTS: creates the Check Log button
    private void checkLogButton() {
        checkLogButton = new JButton();
        checkLogButton.addActionListener(this);
        checkLogButton.setText("Check Member's Log");
        checkLogButton.setBounds(150, 275, 200, 50);
    }

    // MODIFIES: this
    // EFFECTS: creates the add weight button
    private void addWeightButton() {
        addWeightButton = new JButton();
        addWeightButton.addActionListener(this);
        addWeightButton.setText("Add Weight Log");
        addWeightButton.setBounds(150, 200, 200, 50);
    }

    // MODIFIES: this
    // EFFECTS: creates the remove person button
    private void removePersonButton() {
        removePersonButton = new JButton();
        removePersonButton.addActionListener(this);
        removePersonButton.setText("Remove Family Member");
        removePersonButton.setBounds(150, 125, 200, 50);
    }

    // MODIFIES: this
    // EFFECTS: creates the add person button
    private void addPersonButton() {
        addPersonButton = new JButton();
        addPersonButton.addActionListener(this);
        addPersonButton.setText("Add Family Member");
        addPersonButton.setBounds(150, 50, 200, 50);
    }


    // main class that instantiates GUI
    public static void main(String[] args) {
        new GUI();
    }

    // EFFECTS: execute corresponding method of given pressed button
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonButton) {
            addMember();
        }
        if (e.getSource() == removePersonButton) {
            removeMember();
        }
        if (e.getSource() == addWeightButton) {
            addWeightToMember();
        }
        if (e.getSource() == checkLogButton) {
            checkLog();
        }
        if (e.getSource() == saveButton) {
            saveFamily();
        }
        if (e.getSource() == loadButton) {
            loadFamily();
        }
    }

    //region Add Member
    // MODIFIES: this
    // EFFECTS: creates UI for adding member
    private void addMember() {

        addMemberFrame = new JFrame();
        mainTitle = new JLabel("Create Member");
        mainTitle.setBounds(200, 50, 100, 50);
        addMemberFrame.setTitle("Create and add member");
        addMemberFrame.setSize(500, 500);
        addMemberFrame.setResizable(false);
        addMemberFrame.setLayout(null);
        addMemberFrame.setVisible(true);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new AddMemberListener());
        submitButton.setBounds(200, 350, 100, 50);

        addNameTextField();
        addHeightTextField();

        addMemberFrame.add(mainTitle);
        addMemberFrame.add(nameTitle);
        addMemberFrame.add(heightTitle);
        addMemberFrame.add(submitButton);
        addMemberFrame.add(name);
        addMemberFrame.add(height);

    }

    // MODIFIES: this
    // EFFECTS: creates add height text field along with its label
    private void addHeightTextField() {
        heightTitle = new JLabel("Height(CM): ");
        heightTitle.setBounds(70, 200, 100, 50);
        height = new JTextField();
        height.setBounds(150, 200, 200, 50);

    }


    // MODIFIES: this
    // EFFECTS: creates add name text field along with its label
    private void addNameTextField() {
        nameTitle = new JLabel("Name: ");
        nameTitle.setBounds(100, 100, 100, 50);
        name = new JTextField();
        name.setBounds(150, 100, 200, 50);
    }

    // action Listener class for adding member
    class AddMemberListener implements ActionListener {

        // MODIFIES: this
        // EFFECTS: add member to family
        public void actionPerformed(ActionEvent e) {
            family.add(createMember(name.getText(), height.getText()));
            addMemberFrame.setVisible(false);
        }

    }

    // EFFECTS: creates and returns  member given name and height
    private Member createMember(String text, String heightText) {
        int temp = Integer.parseInt(heightText);
        showMessageDialog(null, text + " has been added to the family!");
        return member = new Member(text, temp);
    }

    //endregion


    //region Remove Member
    // MODIFIES: this
    // EFFECTS: creates UI for removing member
    public void removeMember() {

        //create frame
        deleteMemberFrame = new JFrame();
        deleteMemberFrame.setTitle("Create and add member");
        deleteMemberFrame.setSize(500, 500);
        deleteMemberFrame.setResizable(false);
        deleteMemberFrame.setLayout(null);
        deleteMemberFrame.setVisible(true);

        // create subtitle
        subTitle = new JLabel("Select Member to delete :(");
        subTitle.setBounds(150, 50, 200, 50);

        // make the list of current members
        createListModel();
        JScrollPane listScroller = initList();

        // create button for deleting member
        deleteButton = new JButton("Delete Member");
        deleteButton.setBounds(175, 350, 125, 50);
        deleteButton.addActionListener(new DeleteMemberListener());

        // add components to frame
        deleteMemberFrame.add(deleteButton);
        deleteMemberFrame.add(listScroller);
        deleteMemberFrame.add(subTitle);

    }


    // part of code modelled from:
    // https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html#ListDemo
    // action listener class that deletes selected member from family
    class DeleteMemberListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // get index of selected member
            int index = namesOfMembers.getSelectedIndex();
            // remove selected member from listmodel
            listModel.remove(index);
            // remove selected member from family
            Member tempMember = family.get(index);
            family.remove(index);
            // show pop up message for user
            showMessageDialog(null, tempMember.getName() + " has been removed from the family");
            deleteMemberFrame.setVisible(false);

            int size = listModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                deleteButton.setEnabled(false);

            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }
                namesOfMembers.setSelectedIndex(index);
                namesOfMembers.ensureIndexIsVisible(index);
            }
        }
    }

    //endregion


    //region Add Weight Log
    // MODIFIES: this
    // EFFECTS: creates UI for adding weight to member
    private void addWeightToMember() {

        // create frame and subtitle
        addWeightLogFrame = new JFrame();

        subTitle = new JLabel("Select Member and add today's weight:");
        subTitle.setBounds(100, 50, 250, 50);

        addWeightLogFrame.setTitle("Create and add member");
        addWeightLogFrame.setSize(500, 500);
        addWeightLogFrame.setResizable(false);
        addWeightLogFrame.setLayout(null);
        addWeightLogFrame.setVisible(true);

        // create list of current members
        createListModel();
        JScrollPane listScroller = initList();

        // create add weight log button
        JButton addWeightLogButton = new JButton("Add Weight");
        addWeightLogButton.addActionListener(this);
        addWeightLogButton.setBounds(175, 350, 125, 50);
        addWeightLogButton.addActionListener(new AddWeightLogListener());

        // create weight log text field
        weightField = new JTextField();
        weightField.setBounds(200, 300, 100, 25);
        JLabel weightFieldLabel = new JLabel("Enter Weight (KG): ");
        weightFieldLabel.setBounds(75, 300, 159, 25);

        // add to frame
        addToWeightLogFrame(listScroller, addWeightLogButton, weightFieldLabel);

    }

    // MODIFIES: this
    // EFFECTS: add components to add weight log frame
    private void addToWeightLogFrame(JScrollPane listScroller, JButton addWeightLogButton, JLabel weightFieldLabel) {
        addWeightLogFrame.add(weightFieldLabel);
        addWeightLogFrame.add(addWeightLogButton);
        addWeightLogFrame.add(listScroller);
        addWeightLogFrame.add(subTitle);
        addWeightLogFrame.add(weightField);
    }

    // part of code modelled from:
    // https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html#ListDemo
    //  action listener that adds weight log to selected member from the list
    class AddWeightLogListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int index = namesOfMembers.getSelectedIndex();

            Double weightFieldNum = Double.valueOf(weightField.getText());

            Member tempMem = family.get(index);
            try {
                tempMem.addWeightLog(weightFieldNum);
            } catch (NegativeValueException ex) {
                ex.printStackTrace();
            }

            showMessageDialog(null, "Added Weight Log of " + weightFieldNum + " KG to "
                    + tempMem.getName() + "!");

            int size = listModel.getSize();

            if (size == 0) {
                addWeightButton.setEnabled(false);

            } else {
                if (index == listModel.getSize()) {
                    index--;
                }

                namesOfMembers.setSelectedIndex(index);
                namesOfMembers.ensureIndexIsVisible(index);
            }
        }

    }
    //endregion


    //region Check Log
    // MODIFIES: this
    // EFFECTS: creates UI for checking a members weight log
    private void checkLog() {

        // create frame
        JFrame checkLogFrame = new JFrame();
        subTitle = new JLabel("Select Member View Log");
        subTitle.setBounds(150, 50, 200, 50);
        checkLogFrame.setTitle("Create and add member");
        checkLogFrame.setSize(500, 500);
        checkLogFrame.setResizable(false);
        checkLogFrame.setLayout(null);
        checkLogFrame.setVisible(true);

        // create list of member's names from family
        createListModel();
        JScrollPane listScroller = initList();

        // create delete member button
        deleteButton = new JButton("Check Log");
        deleteButton.setBounds(175, 350, 125, 50);
        deleteButton.addActionListener(new CheckLogListener());

        // add to frame
        checkLogFrame.add(deleteButton);
        checkLogFrame.add(listScroller);
        checkLogFrame.add(subTitle);
    }

    // part of code modelled from:
    // https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html#ListDemo
    // action listener that brings up weight log of selected member
    class CheckLogListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            // passes selected member to viewMemberLog method
            int index = namesOfMembers.getSelectedIndex();
            Member tempMem = family.get(index);
            viewMemberLog(tempMem);
            int size = listModel.getSize();

            if (size == 0) {
                checkLogButton.setEnabled(false);
            } else {
                if (index == listModel.getSize()) {
                    index--;
                }
                namesOfMembers.setSelectedIndex(index);
                namesOfMembers.ensureIndexIsVisible(index);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: creates table UI of weight logs for given member
    private void viewMemberLog(Member m) {

        //create frame
        JFrame memberLogFrame = new JFrame();

        subTitle = new JLabel("Select Member View Log");
        subTitle.setBounds(150, 50, 200, 50);

        memberLogFrame.setTitle("Weight Log");
        memberLogFrame.setSize(400, 450);
        memberLogFrame.setResizable(false);
        memberLogFrame.setLayout(null);
        memberLogFrame.setVisible(true);

        // initialize column titles
        String[] columnNames = {
                "Date",
                "Weight (KG)"};

        // create 2D array of dates and weights
        Object[][] weightData = createTableData(m);

        // create table
        final JTable table = new JTable(weightData, columnNames);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(75, 40, 250, 300);

        memberLogFrame.add(scrollPane);
    }


    // EFFECTS: creates 2D array of data from given member
    private Object[][] createTableData(Member m) {
        List<List<String>> arr = new ArrayList<>();

        // creates arraylist of member date and weight
        for (Log l : m.getWeightLogs()) {
            List<String> row = new ArrayList<>();
            row.add(l.getDate());
            row.add(l.getWeight().toString());
            arr.add(row);
        }

        // converts arr to 2D array
        Object[][] data = new Object[arr.size()][2];
        int index = 0;
        for (List<String> stuff : arr) {
            data[index][0] = stuff.get(0);
            data[index][1] = stuff.get(1);
            index++;
        }
        return data;
    }
    //endregion

    //region Load Family
    private void loadFamily() {
        try {
            ArrayList<Member> fam = jsonReader.read();
            this.family = fam;
            showMessageDialog(null, "Loaded family from " + JSON_STORE);
        } catch (IOException e) {
            showMessageDialog(null, "Unable to read from file: " + JSON_STORE);
        }
    }
    //endregion

    //region Save Family
    private void saveFamily() {
        try {
            jsonWriter.open();
            jsonWriter.write(family);
            jsonWriter.close();
            showMessageDialog(null, "Saved all members of family to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            showMessageDialog(null, "Unable to write to file: " + JSON_STORE);
        }
    }
    //endregion


    // EFFECTS: returns an arraylist of all members' name in the family
    public ArrayList<String> getFamilyNames() {
        ArrayList<String> temp = new ArrayList<>();
        for (Member m : family) {
            temp.add(m.getName());
        }
        return temp;
    }


    // EFFECTS: creates a list model from current names of members
    private void createListModel() {
        List<String> listOfNames = getFamilyNames();
        listModel = new DefaultListModel<>();
        for (String str : listOfNames) {
            listModel.addElement(str);
        }
    }

    // MODIFIES: this
    // EFFECTS: initialize the list and add it to JScrollPane
    private JScrollPane initList() {
        namesOfMembers = new JList<>(listModel); //data has type Object[]
        namesOfMembers.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        namesOfMembers.setLayoutOrientation(JList.VERTICAL);
        namesOfMembers.setVisibleRowCount(4);
        namesOfMembers.addListSelectionListener(this);
        namesOfMembers.setBounds(150, 100, 200, 150);
        listScroller = new JScrollPane(namesOfMembers);
        listScroller.setBounds(175, 100, 125, 150);
        return listScroller;
    }



    // not used
    @Override
    public void valueChanged(ListSelectionEvent e) {

    }


}



