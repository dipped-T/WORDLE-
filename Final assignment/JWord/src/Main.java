import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Main {

    //a list of all possible 5-letter words in English
    public static HashSet<String> dictionary = new HashSet<>();

    //a smaller list of words for selecting the target word from (i.e. the word the player needs to guess)
    public static ArrayList<String> targetWords = new ArrayList<>();

    //declare new ArrayList
    static ArrayList<String> targetChars = new ArrayList<>();

    //declare variables to store target words
    public static String target;

    Main(){
        //game instructions
        JOptionPane.showMessageDialog(null, """
                WORDLE -  Guess the word.
                You must guess the 5 letter word.
                You have 6 attempts to guess correctly.
                You must input one (lowercase) letter into each character slot.
                If a letter you enter appears Grey, it means it is not in the word.
                If a letter you enter appears Yellow, it means that it is in the word but in the wrong place.
                If a letter you enter appears Green, it means it is in the word and the correct place.
                
                HINT* press tab to navigate to the next text box.
                       ** click the enter button when you have types your guess.
                       *** press the ? to get some help.
                       
                                                                             HAVE FUN - Tej
                """);

        //Creating the JFrame
        JFrame frame = new JFrame("WORDLE GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,450);
        frame.getContentPane().setBackground(Color.black);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        frame.setResizable(false);

        //Game title
        JLabel title = new JLabel("W O R D L E ", SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        title.setForeground(Color.orange);
        title.setFont(new Font("Times New Roman Bold", Font.PLAIN, 20));

        //panels for text
        JPanel textBox = new JPanel();
        textBox.setLayout(new GridLayout(6, 1));
        textBox.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 0));
        textBox.setBackground(Color.black);

        JPanel[] subPanel = new JPanel[6];
        JTextField[][] textField = new JTextField[6][5];

        //store the textFields
        for (int i = 0; i < 6; i++) {
            subPanel[i] = new JPanel();
            subPanel[i].setLayout(new GridLayout(0, 5));
            textBox.add(subPanel[i]);

            //textField from jTextField
            for (int j = 0; j < 5; j++) {
                textField[i][j] = new JTextField();
                textField[i][j].setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 15));
                textField[i][j].setHorizontalAlignment(JTextField.CENTER);
                textField[i][j].setForeground(Color.white);
                textField[i][j].setBackground(Color.gray);
                subPanel[i].add(textField[i][j]);

                if(i > 0){
                    textField[i][j].setEditable(false); //seEditable false makes it so user can't edit (vice versa with true)
                }
            }
        }
        //making JPanel container for the buttons
        JPanel buttonPan = new JPanel();
        buttonPan.setLayout(new GridLayout(0, 1));
        buttonPan.setBorder(BorderFactory.createEmptyBorder(20,0,20,20));
        buttonPan.setBackground(Color.black);

        //making 6 buttons for entering words
        JButton[] buttons = new JButton[6];
        for (int i = 0; i < buttons.length; i++) {
            int finalI = i;
            buttons[i] = new JButton("Enter");
            buttons[i].setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 7));
            buttons[i].setPreferredSize(new Dimension(50, 30));
            buttons[i].setBackground(Color.white);

            //making sure button is disabled to prevent further input after the previous row
            if(i > 0){
                buttons[i].setEnabled(false);
                buttons[i].setVisible(false);
            }

            //making actionListeners for each of the buttons
            buttons[i].addActionListener(e -> {
                int wordLength = 5;
                if (e.getSource() == buttons[finalI]){

                    StringBuilder checkWord = new StringBuilder();
                    int count = 0;

                    for (int i1 = 0; i1 < wordLength; i1++){
                        if (textField[finalI][i1].getText().length() > 1){
                            JOptionPane.showMessageDialog(frame, "Enter 1 letter into each section");
                            break;
                        }
                        checkWord.append(textField[finalI][i1].getText());
                    }

                    System.out.println("Row "+ finalI + " input word = "+ checkWord.toString().toLowerCase());

                    if (checkWord.length() == 0) {
                        JOptionPane.showMessageDialog(null, "Make sure all boxes have a single letter");

                    }
                    else if (!checkWord.toString().matches("[a-zA-z]+")){
                        JOptionPane.showMessageDialog(null, "unrecognised character, English alphabet only.");

                    }
                    else if (checkWord.length()!=wordLength){
                        JOptionPane.showMessageDialog(null, "only 5 letters per line");

                    }
                    else {
                        if (!dictionary.contains(checkWord.toString().toLowerCase())){
                            JOptionPane.showMessageDialog(null, "word isn't in the dictionary");

                        }else{
                            //setting colours for whether letter is correct
                            for(int j = 0; j < wordLength; j++){
                                textField[finalI][j].setEditable(false);

                                if (targetChars.contains(textField[finalI][j].getText())){
                                    textField[finalI][j].setBackground(Color.yellow);
                                }

                                if (j == targetChars.indexOf(textField[finalI][j].getText()) ||
                                        textField[finalI][j].getText().equals(targetChars.get(j))) {
                                    textField[finalI][j].setBackground(Color.GREEN);
                                    count++;

                                    if (count == targetChars.size()) {
                                        System.out.println("You have guessed the word, congrats: " + checkWord + "==" + target);
                                        JOptionPane.showMessageDialog(null, "Game Won. you can press QUIT to leave, or press NEW GAME to get another word.");

                                        for (int i1 = 0; i1 < buttons.length; i1++){
                                            buttons[i1].setEnabled(false);
                                            for (int k = 0; k < wordLength; k++){
                                                textField[i1][k].setEditable(false);
                                            }
                                        }
                                    }
                                }

                                else if (!targetChars.contains(textField[finalI][j].getText())){
                                    textField[finalI][j].setBackground(Color.gray);
                                }

                                try{
                                    if(count !=targetChars.size()){
                                        buttons[finalI].setEnabled(false);
                                        buttons[finalI].setVisible(false);
                                        buttons[finalI + 1].setEnabled(true);
                                        buttons[finalI + 1].setVisible(true);
                                        textField[finalI + 1][j].setEditable(true);
                                    }
                                }
                                catch (ArrayIndexOutOfBoundsException x){
                                    System.out.println("ArrayIndexOutOfBoundsException");
                                }
                            }
                        }
                    }
                }
            });
            //adds a button to the panel
            buttonPan.add(buttons[i]);
        }

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(1, 2));

        //new game button
        JButton newGame = new JButton("New Game");
        menuPanel.add(newGame);
        newGame.setPreferredSize(new Dimension(30, 20));
        newGame.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 10));
        newGame.setBackground(Color.white);
        newGame.addActionListener(e -> {
            targetChars.clear();
            target = getTarget();
            pushTarget(target);

            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setEnabled(true);
                buttons[i].setVisible(true);
                if (i > 0){
                    buttons[i].setEnabled(false);
                    buttons[i].setVisible(false);
                }
                for (int j = 0; j < textField[i].length; j++) {
                    textField[i][j].setText("");
                    textField[i][j].setEditable(true);
                    textField[i][j].setBackground(Color.gray); //self note, this changes colour when new game is pressed
                    if(i > 0){
                        textField[i][j].setEditable(false);
                    }
                }
            }
        });

        //help button
        JButton help = new JButton("Help");
        menuPanel.add(help);
        help.setText("?");
        newGame.setPreferredSize(new Dimension(30, 35));
        help.setFont(new Font("Franklin Gothic Medium", Font.BOLD, 15));
        help.setBackground(Color.white);

        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Visit WORDLE on the NYTimes page for the official game and details of game instructions.");
            }
        });

        //quit button
        JButton quit = new JButton("Quit");
        menuPanel.add(quit);
        quit.setPreferredSize(new Dimension(30, 20));
        quit.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 15));
        quit.setBackground(Color.white);

        quit.addActionListener(e -> {
            System.exit(0);
        });

        //adds the buttons to the frame location
        frame.add(title, BorderLayout.PAGE_START);
        frame.add(textBox, BorderLayout.CENTER);
        frame.add(buttonPan, BorderLayout.LINE_END);
        frame.add(menuPanel, BorderLayout.PAGE_END);
    }


    //bringing in the words form dictionaries
    public static void importWords(){
        //load in the two word lists
        try{
            Scanner in_dict = new Scanner(new File("JWord/gameDictionary.txt"));
            while (in_dict.hasNext()){
                dictionary.add(in_dict.next());
            }
            Scanner in_targets = new Scanner(new File("JWord/targetWords.txt"));
             while (in_targets.hasNext()){
                targetWords.add(in_targets.next());
            }
            in_dict.close();
            in_targets.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private static String getTarget(){
        Random r = new Random();
        String target = targetWords.get(r.nextInt(targetWords.size()));
        //don't delete this line.
        System.out.println("Wordle word:" + target); //prints the word to the console
        return target;
    }

    //push target to arraylist
    public static void pushTarget(String target) {
        for (int i = 0; i < target.length(); i++) {
            targetChars.add(String.valueOf(target.charAt(i)));
        }
        System.out.println("Arraylist of word: " + targetChars); // creates an arraylist of each character in the word, prints it to console.
    }

    public static void main(String[] args){
        //brings the words in from file
        importWords();
        //gets a random word from the list pushes it to the arraylist
        target = getTarget();
        pushTarget(target);
        SwingUtilities.invokeLater(Main::new);
    }
}
