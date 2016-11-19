package com.google.code.chatterbotapi;

import javax.swing.*;
import java.io.*;
import java.lang.*;

class CustomOutputStream extends OutputStream {
    private JTextArea textArea;

    CustomOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        textArea.append(String.valueOf((char)b));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}

public class ChatbotMeetsChatbot {
    private JButton beginConversationButton;
    private JComboBox botType1;
    private JComboBox botType2;
    private JPanel ChatbotMeetsChatbot;
    private JTextField firstPhrase;
    private JTextField startingPhraseTooltip;
    private JTextField numLoops;
    private JTextField numberOfLoopsTooltip;
    private JTextArea conversationOutput;
    private PrintStream standardOut = System.out;
    private PrintStream printStream;
    private JScrollPane scrollPane;
    private JButton clearButton;
    private JButton exportButton;
    private JButton endConversationButton;
    private int loops;
    private boolean terminator;
    private String s;
    private String pandorabotBotID = "b0dafd24ee35a477";
    private String botOneType;
    private String botTwoType;
    private ChatterBotFactory factory = new ChatterBotFactory();
    private ChatterBot bot1;
    private ChatterBot bot2;
    private ChatterBotSession bot1session;
    private ChatterBotSession bot2session;

    private void talk() {
        printStream = new PrintStream(new CustomOutputStream(conversationOutput));
        Thread thread = new Thread(() -> {
            for (int i = loops; i != 0; i--)
            {
                if (i < -1) i = -1;

                printStream.print("bot1> " + s + "\n");

                if (terminator) {
                    printStream.close();
                    return;
                }

                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                if (terminator) {
                    printStream.close();
                    return;
                }

                try {
                    s = bot2session.think(s);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                if (terminator) {
                    printStream.close();
                    return;
                }

                printStream.print("bot2> " + s + "\n");

                if (terminator) {
                    printStream.close();
                    return;
                }

                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                if (terminator) {
                    printStream.close();
                    return;
                }

                try {
                    s = bot1session.think(s);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                if (terminator) {
                    printStream.close();
                    return;
                }
            }

            printStream.print("\n----- CONVERSATION END -----\n\n");
        });
        thread.start();
    }

    private ChatbotMeetsChatbot() {
        beginConversationButton.addActionListener(e -> {
            terminator = false;
            s = firstPhrase.getText();
            loops = Integer.parseInt(numLoops.getText());
            botOneType = botType1.getSelectedItem().toString().toUpperCase();
            botTwoType = botType2.getSelectedItem().toString().toUpperCase();O
            try {
                bot1 = factory.create(ChatterBotType.valueOf(botOneType), pandorabotBotID);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            assert bot1 != null;
            bot1session = bot1.createSession();
            try {
                bot2 = factory.create(ChatterBotType.valueOf(botTwoType), pandorabotBotID);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            assert bot2 != null;
            bot2session = bot2.createSession();

            talk();
        });
        clearButton.addActionListener(e -> conversationOutput.setText(""));
        exportButton.addActionListener(e -> {
            try (PrintWriter out = new PrintWriter("ChatbotOutput.txt")) {
                out.print(conversationOutput.getText());
                out.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        endConversationButton.addActionListener(e -> {
            terminator = true;
            printStream.print("\n----- CONVERSATION END EARLY -----\n\n");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("ChatbotMeetsChatbot");
            frame.setContentPane(new ChatbotMeetsChatbot().ChatbotMeetsChatbot);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
