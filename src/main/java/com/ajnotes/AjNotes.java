package com.ajnotes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.swing.border.*;

public class AjNotes {
    private static JTextArea textArea;
    private static JFrame frame;
    private static File currentFile = null;
    private static JPanel homePanel;
    private static JPanel editorPanel;
    private static CardLayout cardLayout;
    private static JPanel mainPanel;
    private static final String HOME_CARD = "HOME";
    private static final String EDITOR_CARD = "EDITOR";
    private static final String NOTES_DIR = "notes";
    private static JPanel notesListPanel;
    private static JTextField titleField;

    public static void main(String[] args) {
        // Create notes directory if it doesn't exist
        File notesDir = new File(NOTES_DIR);
        if (!notesDir.exists()) {
            notesDir.mkdir();
        }

        // Create the main window
        frame = new JFrame("AJ-Notes 1.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);

        // Create card layout for switching between home and editor
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        // Create home panel
        createHomePanel();

        // Create editor panel
        createEditorPanel();

        // Add panels to card layout
        mainPanel.add(homePanel, HOME_CARD);
        mainPanel.add(editorPanel, EDITOR_CARD);
        // Set the main background color of the home panel
        homePanel.setBackground(new Color(224, 205, 153));

        // Add the main panel to the frame
        frame.add(mainPanel);

        // Load existing notes
        loadNotes();

        // Show home panel first
        cardLayout.show(mainPanel, HOME_CARD);

        // Center the window
        frame.setLocationRelativeTo(null);

        // Make the window visible
        frame.setVisible(true);
    }

    @SuppressWarnings("unused")
    private static void createHomePanel() {
        homePanel = new JPanel(new BorderLayout(10, 10));
        homePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        homePanel.setBackground(new Color(224, 205, 153));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(22, 59, 32));
        headerPanel.setBorder(new LineBorder(new Color(22, 59, 32), 8, true));

        // Main title of the home page color settings
        JLabel titleLabel = new JLabel("Hi Alex, Take a Note!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Create a panel for buttons that will sit side by side
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(22, 59, 32)); // Match header background

        JButton viewNoteButton = new JButton("View All Notes");
        viewNoteButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewNoteButton.setBackground(new Color(224, 205, 153));
        viewNoteButton.setForeground(new Color(22, 59, 32));
        viewNoteButton.setFocusPainted(false);
        viewNoteButton.addActionListener(e -> {
            try {
                // Get the notes directory
                File notesDir = new File("Notes");
                if (!notesDir.exists()) {
                    notesDir.mkdir();
                }
                
                // Open the file explorer
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(notesDir);
                } else {
                    JOptionPane.showMessageDialog(frame,
                        "Unable to open file explorer on this system.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame,
                    "Error opening file explorer: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton newNoteButton = new JButton("New Note");
        newNoteButton.setFont(new Font("Arial", Font.BOLD, 14));
        newNoteButton.setBackground(new Color(224, 205, 153));
        newNoteButton.setForeground(new Color(22, 59, 32));
        newNoteButton.setFocusPainted(false);
        newNoteButton.addActionListener(e -> createNewNote());

        // Add buttons to the buttons panel (order matters - first added is leftmost)
        buttonsPanel.add(viewNoteButton);
        buttonsPanel.add(newNoteButton);
        
        // Add the buttons panel to the EAST position of the header
        headerPanel.add(buttonsPanel, BorderLayout.EAST);

        // Create notes list panel with a scroll pane
        notesListPanel = new JPanel();
        notesListPanel.setLayout(new BoxLayout(notesListPanel, BoxLayout.Y_AXIS));
        notesListPanel.setBackground(new Color(22, 59, 32));
        notesListPanel.setBorder(new LineBorder(new Color(22, 59, 32), 8, true));

        JScrollPane scrollPane = new JScrollPane(notesListPanel);
        scrollPane.setBorder(new LineBorder(new Color(22, 59, 32), 8, true));

        // Add components to home panel
        homePanel.add(headerPanel, BorderLayout.NORTH);
        homePanel.add(scrollPane, BorderLayout.CENTER);

        // Add welcome message at the bottom
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(new Color(224, 205, 153));
        JLabel welcomeLabel = new JLabel("Custom NotePad Created by Alex Sanna - ajsanna@yahoo.com - All Rights Reserved");
        welcomeLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        homePanel.add(welcomePanel, BorderLayout.SOUTH);
    }

    private static void createEditorPanel() {
        // Create menu bar
        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);
        
        // Create toolbar
        JToolBar toolBar = createToolBar();
        toolBar.setFloatable(false);
        toolBar.setBackground(new Color(224, 205, 153));
        toolBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(22, 59, 32)));
        
        // Create main panel with BoxLayout
        editorPanel = new JPanel();
        editorPanel.setLayout(new BoxLayout(editorPanel, BoxLayout.Y_AXIS));
        editorPanel.setBackground(new Color(224, 205, 153));
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(224, 205, 153));
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleField = new JTextField(20);
        titleField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createHorizontalStrut(10));
        titlePanel.add(titleField);
        
        // Formatting toolbar
        JPanel formatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        formatPanel.setBackground(new Color(224, 205, 153));
        formatPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        // Font family combo
        JComboBox<String> fontFamilyCombo = new JComboBox<>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontFamilyCombo.setMaximumSize(new Dimension(150, 30));
        fontFamilyCombo.addActionListener(_ -> {
            String family = (String) fontFamilyCombo.getSelectedItem();
            Font currentFont = textArea.getFont();
            textArea.setFont(new Font(family, currentFont.getStyle(), currentFont.getSize()));
        });
        
        // Font size combo
        JComboBox<Integer> fontSizeCombo = new JComboBox<>(new Integer[]{8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72});
        fontSizeCombo.setMaximumSize(new Dimension(80, 30));
        fontSizeCombo.addActionListener(_ -> {
            int size = (Integer) fontSizeCombo.getSelectedItem();
            Font currentFont = textArea.getFont();
            textArea.setFont(new Font(currentFont.getFamily(), currentFont.getStyle(), size));
        });
        
        // Bold button
        JButton boldButton = new JButton("B");
        boldButton.setFont(new Font("Arial", Font.BOLD, 14));
        boldButton.setMaximumSize(new Dimension(30, 30));
        boldButton.addActionListener(_ -> {
            Font currentFont = textArea.getFont();
            textArea.setFont(new Font(currentFont.getFamily(), 
                currentFont.getStyle() ^ Font.BOLD, currentFont.getSize()));
        });
        
        // Italic button
        JButton italicButton = new JButton("I");
        italicButton.setFont(new Font("Arial", Font.ITALIC, 14));
        italicButton.setMaximumSize(new Dimension(30, 30));
        italicButton.addActionListener(_ -> {
            Font currentFont = textArea.getFont();
            textArea.setFont(new Font(currentFont.getFamily(), 
                currentFont.getStyle() ^ Font.ITALIC, currentFont.getSize()));
        });
        
        // Text color button
        JButton textColorButton = new JButton("Text Color");
        textColorButton.setMaximumSize(new Dimension(100, 30));
        textColorButton.addActionListener(_ -> chooseTextColor());
        
        // Background color button
        JButton bgColorButton = new JButton("Background");
        bgColorButton.setMaximumSize(new Dimension(100, 30));
        bgColorButton.addActionListener(_ -> chooseBackgroundColor());
        
        // Add components to format panel
        formatPanel.add(fontFamilyCombo);
        formatPanel.add(Box.createHorizontalStrut(10));
        formatPanel.add(fontSizeCombo);
        formatPanel.add(Box.createHorizontalStrut(10));
        formatPanel.add(boldButton);
        formatPanel.add(Box.createHorizontalStrut(5));
        formatPanel.add(italicButton);
        formatPanel.add(Box.createHorizontalStrut(10));
        formatPanel.add(textColorButton);
        formatPanel.add(Box.createHorizontalStrut(5));
        formatPanel.add(bgColorButton);
        
        // Text area
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(22, 59, 32), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Add components to editor panel
        editorPanel.add(Box.createVerticalStrut(10));
        editorPanel.add(titlePanel);
        editorPanel.add(Box.createVerticalStrut(5));
        editorPanel.add(formatPanel);
        editorPanel.add(Box.createVerticalStrut(5));
        editorPanel.add(scrollPane);
        editorPanel.add(Box.createVerticalStrut(10));
        
        // Add toolbar to the frame
        frame.add(toolBar, BorderLayout.NORTH);
    }

    private static void loadNotes() {
        notesListPanel.removeAll();
        
        File notesDir = new File(NOTES_DIR);
        if (!notesDir.exists()) {
            notesDir.mkdir();
        }
        
        File[] files = notesDir.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    Properties props = new Properties();
                    try (FileInputStream fis = new FileInputStream(file)) {
                        props.load(fis);
                    }
                    
                    String content = props.getProperty("content", "");
                    String title = props.getProperty("title", "");
                    if (title.isEmpty()) {
                        title = content.length() > 50 ? content.substring(0, 47) + "..." : content;
                        if (title.isEmpty()) title = file.getName();
                    }
                    
                    Date lastModified = new Date(file.lastModified());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
                    
                    NoteInfo note = new NoteInfo(file, title, content, dateFormat.format(lastModified));
                    notesListPanel.add(createNotePanel(note));
                } catch (IOException e) {
                    System.err.println("Error reading file: " + e.getMessage());
                }
            }
        }
        
        notesListPanel.add(Box.createVerticalGlue());
        notesListPanel.revalidate();
        notesListPanel.repaint();
    }

    private static JPanel createNotePanel(NoteInfo note) {
        JPanel notePanel = new JPanel(new BorderLayout(10, 5));
        notePanel.setBackground(new Color(255, 255, 240));
        notePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Maximum width for the note panel
        notePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        // Title and date panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 255, 240));

        JLabel titleLabel = new JLabel(note.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JLabel dateLabel = new JLabel(note.getDate());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(Color.GRAY);
        headerPanel.add(dateLabel, BorderLayout.EAST);

        // Preview panel
        JLabel previewLabel = new JLabel(note.getPreview());
        if (previewLabel.getText().length() > 80) {
            previewLabel.setText(previewLabel.getText().substring(0, 77) + "...");
        }
        previewLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        previewLabel.setForeground(new Color(80, 80, 80));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(255, 255, 240));

        JButton openButton = new JButton("Open");
        openButton.setFont(new Font("Arial", Font.PLAIN, 12));
        openButton.setBackground(new Color(100, 150, 200));
        openButton.setForeground(Color.BLUE);
        openButton.setFocusPainted(false);
        openButton.addActionListener(_ -> openNote(note.getFile()));

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 12));
        deleteButton.setBackground(new Color(220, 100, 100));
        deleteButton.setForeground(Color.RED);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(_ -> deleteNote(note.getFile()));

        buttonPanel.add(openButton);
        buttonPanel.add(deleteButton);

        // Add components to note panel
        notePanel.add(headerPanel, BorderLayout.NORTH);
        notePanel.add(previewLabel, BorderLayout.CENTER);
        notePanel.add(buttonPanel, BorderLayout.SOUTH);

        // Make the entire panel clickable
        notePanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openNote(note.getFile());
            }

            public void mouseEntered(MouseEvent e) {
                notePanel.setBackground(new Color(245, 245, 230));
                headerPanel.setBackground(new Color(245, 245, 230));
                buttonPanel.setBackground(new Color(245, 245, 230));
                notePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                notePanel.setBackground(new Color(255, 255, 240));
                headerPanel.setBackground(new Color(255, 255, 240));
                buttonPanel.setBackground(new Color(255, 255, 240));
                notePanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return notePanel;
    }

    private static void openNote(File file) {
        try {
            // Load the note properties
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream(file)) {
                props.load(fis);
            }
            
            // Set the content
            textArea.setText(props.getProperty("content", ""));
            
            // Set the background color
            String bgColorStr = props.getProperty("background");
            if (bgColorStr != null) {
                Color bgColor = new Color(Integer.parseInt(bgColorStr));
                textArea.setBackground(bgColor);
            } else {
                textArea.setBackground(Color.WHITE);
            }
            
            // Set the text color
            String textColorStr = props.getProperty("textColor");
            if (textColorStr != null) {
                Color textColor = new Color(Integer.parseInt(textColorStr));
                textArea.setForeground(textColor);
            } else {
                textArea.setForeground(Color.BLACK);
            }
            
            // Set the font
            String fontFamily = props.getProperty("fontFamily", "Arial");
            int fontSize = Integer.parseInt(props.getProperty("fontSize", "14"));
            int fontStyle = Integer.parseInt(props.getProperty("fontStyle", "0"));
            textArea.setFont(new Font(fontFamily, fontStyle, fontSize));
            
            // Set the title
            String title = props.getProperty("title", "");
            titleField.setText(title);
            
            currentFile = file;
            cardLayout.show(mainPanel, EDITOR_CARD);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,
                "Error reading file: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void deleteNote(File file) {
        int option = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to delete this note?",
                "Delete Note",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            boolean deleted = file.delete();
            if (deleted) {
                loadNotes(); // Refresh the notes list
            } else {
                JOptionPane.showMessageDialog(
                        frame,
                        "Failed to delete the note.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void createNewNote() {
        // Clear the text area
        textArea.setText("");
        currentFile = null;
        frame.setTitle("AJ-Notes 1.0 - New Note");

        // Switch to editor view
        cardLayout.show(mainPanel, EDITOR_CARD);
    }

    private static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Home Button
        JMenu homeMenu = new JMenu("Home");
        homeMenu.setMnemonic(KeyEvent.VK_H);
        JMenuItem homeMenuItem = new JMenuItem("Go to Home", KeyEvent.VK_H);
        homeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
        homeMenuItem.addActionListener(_ -> goToHome());
        homeMenu.add(homeMenuItem);

        // File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem newMenuItem = new JMenuItem("New", KeyEvent.VK_N);
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newMenuItem.addActionListener(_ -> newFile());

        JMenuItem openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openMenuItem.addActionListener(_ -> openFile());

        JMenuItem saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.addActionListener(_ -> saveFile(false));

        JMenuItem saveAsMenuItem = new JMenuItem("Save As...", KeyEvent.VK_A);
        saveAsMenuItem.addActionListener(_ -> saveFile(true));

        JMenuItem exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitMenuItem.addActionListener(_ -> System.exit(0));

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);

        JMenuItem cutMenuItem = new JMenuItem("Cut", KeyEvent.VK_T);
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        cutMenuItem.addActionListener(_ -> textArea.cut());

        JMenuItem copyMenuItem = new JMenuItem("Copy", KeyEvent.VK_C);
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        copyMenuItem.addActionListener(_ -> textArea.copy());

        JMenuItem pasteMenuItem = new JMenuItem("Paste", KeyEvent.VK_P);
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        pasteMenuItem.addActionListener(_ -> textArea.paste());

        JMenuItem selectAllMenuItem = new JMenuItem("Select All", KeyEvent.VK_A);
        selectAllMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        selectAllMenuItem.addActionListener(_ -> textArea.selectAll());

        editMenu.add(cutMenuItem);
        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);
        editMenu.addSeparator();
        editMenu.add(selectAllMenuItem);

        // Format menu
        JMenu formatMenu = new JMenu("Format");
        formatMenu.setMnemonic(KeyEvent.VK_O);

        JMenuItem fontMenuItem = new JMenuItem("Font...", KeyEvent.VK_F);
        fontMenuItem.addActionListener(_ -> chooseFont());

        JCheckBoxMenuItem wordWrapMenuItem = new JCheckBoxMenuItem("Word Wrap");
        wordWrapMenuItem.setMnemonic(KeyEvent.VK_W);
        wordWrapMenuItem.addActionListener(_ -> textArea.setLineWrap(wordWrapMenuItem.isSelected()));

        // Text and background color options
        JMenuItem textColorMenuItem = new JMenuItem("Text Color...", KeyEvent.VK_T);
        textColorMenuItem.addActionListener(_ -> chooseTextColor());

        JMenuItem bgColorMenuItem = new JMenuItem("Background Color...", KeyEvent.VK_B);
        bgColorMenuItem.addActionListener(_ -> chooseBackgroundColor());

        formatMenu.add(fontMenuItem);
        formatMenu.add(wordWrapMenuItem);
        formatMenu.addSeparator();
        formatMenu.add(textColorMenuItem);
        formatMenu.add(bgColorMenuItem);

        // Add menus to menu bar
        menuBar.add(homeMenu);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);

        return menuBar;
    }

    private static void goToHome() {
        // If there are unsaved changes, prompt to save
        if (textArea.getText().length() > 0 && (currentFile == null || isDirty())) {
            int option = JOptionPane.showConfirmDialog(
                    frame,
                    "Do you want to save changes before going to home?",
                    "Save Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                if (!saveFile(false)) {
                    return; // User canceled save operation
                }
            } else if (option == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }

        // Reload notes to show any new ones
        loadNotes();

        // Switch to home view
        cardLayout.show(mainPanel, HOME_CARD);
        frame.setTitle("AJ-Notes 1.0");
    }

    private static boolean isDirty() {
        // Check if current text differs from saved file
        if (currentFile != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                StringBuilder fileContent = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    fileContent.append(line).append("\n");
                }

                return !textArea.getText().equals(fileContent.toString());
            } catch (IOException e) {
                return true;
            }
        }
        return true;
    }

    private static JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(_ -> goToHome());

        JButton newButton = new JButton("New");
        newButton.addActionListener(_ -> newFile());

        JButton openButton = new JButton("Open");
        openButton.addActionListener(_ -> openFile());

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(_ -> saveFile(false));

        toolBar.add(homeButton);
        toolBar.add(newButton);
        toolBar.add(openButton);
        toolBar.add(saveButton);
        toolBar.addSeparator();

        JButton cutButton = new JButton("Cut");
        cutButton.addActionListener(_ -> textArea.cut());

        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(_ -> textArea.copy());

        JButton pasteButton = new JButton("Paste");
        pasteButton.addActionListener(_ -> textArea.paste());

        toolBar.add(cutButton);
        toolBar.add(copyButton);
        toolBar.add(pasteButton);

        return toolBar;
    }

    private static void newFile() {
        if (textArea.getText().length() > 0) {
            int option = JOptionPane.showConfirmDialog(
                    frame,
                    "Do you want to save changes?",
                    "Save Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                if (!saveFile(false)) {
                    return; // User canceled save operation
                }
            } else if (option == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }

        textArea.setText("");
        currentFile = null;
        frame.setTitle("AJ-Notes 1.0 - New Note");

        // Switch to editor view if not already there
        cardLayout.show(mainPanel, EDITOR_CARD);
    }

    private static void openFile() {
        if (textArea.getText().length() > 0) {
            int option = JOptionPane.showConfirmDialog(
                    frame,
                    "Do you want to save changes?",
                    "Save Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                if (!saveFile(false)) {
                    return; // User canceled save operation
                }
            } else if (option == JOptionPane.CANCEL_OPTION) {
                return;
            }
        }

        JFileChooser fileChooser = new JFileChooser(new File(NOTES_DIR));
        int option = fileChooser.showOpenDialog(frame);

        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            openNote(selectedFile);
        }
    }

    private static boolean saveFile(boolean saveAs) {
        if (currentFile == null || saveAs) {
            JFileChooser fileChooser = new JFileChooser(NOTES_DIR);
            int result = fileChooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
            } else {
                return false;
            }
        }
        
        try {
            // Save the note content and its properties
            String content = textArea.getText();
            Color bgColor = textArea.getBackground();
            Color textColor = textArea.getForeground();
            String title = titleField.getText();
            Font font = textArea.getFont();
            
            // Create a properties object to store additional data
            Properties props = new Properties();
            props.setProperty("content", content);
            props.setProperty("background", String.valueOf(bgColor.getRGB()));
            props.setProperty("textColor", String.valueOf(textColor.getRGB()));
            props.setProperty("title", title);
            props.setProperty("fontFamily", font.getFamily());
            props.setProperty("fontSize", String.valueOf(font.getSize()));
            props.setProperty("fontStyle", String.valueOf(font.getStyle()));
            
            // Save to file
            try (FileOutputStream fos = new FileOutputStream(currentFile)) {
                props.store(fos, "Note Properties");
            }
            
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,
                "Error saving file: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private static void chooseTextColor() {
        Color currentColor = textArea.getForeground();
        Color newColor = JColorChooser.showDialog(
                frame,
                "Choose Text Color",
                currentColor);

        if (newColor != null) {
            textArea.setForeground(newColor);
        }
    }

    private static void chooseBackgroundColor() {
        Color currentColor = textArea.getBackground();
        Color newColor = JColorChooser.showDialog(
                frame,
                "Choose Background Color",
                currentColor);

        if (newColor != null) {
            textArea.setBackground(newColor);
        }
    }

    private static void chooseFont() {
        Font currentFont = textArea.getFont();
        Font selectedFont = JFontChooser.showDialog(frame, "Choose Font", currentFont);
        if (selectedFont != null) {
            textArea.setFont(selectedFont);
        }
    }

    // Simple Note Info class to store note metadata
    static class NoteInfo {
        private File file;
        private String title;
        private String preview;
        private String date;

        public NoteInfo(File file, String title, String preview, String date) {
            this.file = file;
            this.title = title;
            this.preview = preview;
            this.date = date;
        }

        public File getFile() {
            return file;
        }

        public String getTitle() {
            return title;
        }

        public String getPreview() {
            return preview;
        }

        public String getDate() {
            return date;
        }
    }

    // Simple Font Chooser implementation
    static class JFontChooser extends JDialog {
        private Font selectedFont;
        private JComboBox<String> fontFamilyCombo;
        private JComboBox<Integer> fontSizeCombo;
        private JCheckBox boldCheckBox;
        private JCheckBox italicCheckBox;

        public static Font showDialog(Component parent, String title, Font initialFont) {
            JFontChooser fontChooser = new JFontChooser(parent, title, initialFont);
            fontChooser.setVisible(true);
            return fontChooser.selectedFont;
        }

        private JFontChooser(Component parent, String title, Font initialFont) {
            super((Frame) null, title, true);
            setSize(400, 300);
            setLocationRelativeTo(parent);

            // Initialize components
            fontFamilyCombo = new JComboBox<>(
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
            fontSizeCombo = new JComboBox<>(
                    new Integer[] { 8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72 });
            boldCheckBox = new JCheckBox("Bold");
            italicCheckBox = new JCheckBox("Italic");

            // Set initial values
            if (initialFont != null) {
                fontFamilyCombo.setSelectedItem(initialFont.getFamily());
                fontSizeCombo.setSelectedItem(initialFont.getSize());
                boldCheckBox.setSelected(initialFont.isBold());
                italicCheckBox.setSelected(initialFont.isItalic());
            }

            // Create buttons
            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("Cancel");

            // Add action listeners
            okButton.addActionListener(_ -> {
                selectedFont = createFont();
                dispose();
            });

            cancelButton.addActionListener(_ -> {
                selectedFont = null;
                dispose();
            });

            // Layout
            JPanel mainPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0;
            gbc.gridy = 0;
            mainPanel.add(new JLabel("Font Family:"), gbc);

            gbc.gridx = 1;
            mainPanel.add(fontFamilyCombo, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            mainPanel.add(new JLabel("Font Size:"), gbc);

            gbc.gridx = 1;
            mainPanel.add(fontSizeCombo, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            mainPanel.add(boldCheckBox, gbc);

            gbc.gridx = 1;
            mainPanel.add(italicCheckBox, gbc);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);

            setLayout(new BorderLayout());
            add(mainPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private Font createFont() {
            String family = (String) fontFamilyCombo.getSelectedItem();
            int size = (Integer) fontSizeCombo.getSelectedItem();
            int style = Font.PLAIN;
            if (boldCheckBox.isSelected())
                style |= Font.BOLD;
            if (italicCheckBox.isSelected())
                style |= Font.ITALIC;
            return new Font(family, style, size);
        }
    }
}