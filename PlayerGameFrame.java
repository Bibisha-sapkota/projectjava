package coursework;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class PlayerGameFrame extends JFrame {
    private int playerID;
    private String playerName;
    private String playerLevel;
    private String country;
    private int currentGame = 1;
    private int questionIndex = 0;
    private int totalScore = 0;
    private int gameScore = 0;
    private int[] gameScores = new int[5];
    private List<Question> questions = new ArrayList<>();
    private JButton[] answerButtons = new JButton[4];
    private JLabel scoreLabel;
    private JPanel questionPanel;

    public PlayerGameFrame(int playerID, String playerName, String playerLevel, String country) {
        this.playerID = playerID;
        this.playerName = playerName;
        this.playerLevel = playerLevel;
        this.country = country;

        setTitle("Player Game");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Quiz Game", SwingConstants.CENTER);
        titleLabel.setBackground(new Color(255, 255, 128));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        mainPanel.setBackground(new Color(34, 45, 60));  
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Score Panel
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scorePanel.setBackground(new Color(34, 45, 60));
        scoreLabel = new JLabel("Total Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        scoreLabel.setForeground(Color.WHITE);
        scorePanel.add(scoreLabel);
        mainPanel.add(scorePanel, BorderLayout.SOUTH);

        // Question Panel
        questionPanel = new JPanel(new GridLayout(6, 1));
        questionPanel.setBackground(new Color(128, 255, 255));  
        mainPanel.add(questionPanel, BorderLayout.CENTER);

        startGame();

        getContentPane().add(mainPanel);
        setVisible(true);
    }

    private void startGame() {
        loadQuestions();
        Collections.shuffle(questions);
        questionIndex = 0;
        gameScore = 0;
        displayQuestion();
    }

    private void loadQuestions() {
        questions.clear();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CompetitionDB", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM QuizQuestions WHERE difficulty = ?")) {
            stmt.setString(1, playerLevel);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                questions.add(new Question(
                        rs.getInt("id"),
                        rs.getString("question"),
                        rs.getString("option1"),
                        rs.getString("option2"),
                        rs.getString("option3"),
                        rs.getString("option4"),
                        rs.getInt("correct_option")
                ));
            }

            if (questions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No questions found for difficulty level: " + playerLevel);
                dispose();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayQuestion() {
        if (questionIndex >= 5 || questionIndex >= questions.size()) {
            endGame();
            return;
        }

        Question question = questions.get(questionIndex);
        questionPanel.removeAll();
        JLabel questionLabel = new JLabel(question.getQuestion());
        questionPanel.add(questionLabel);

        answerButtons[0] = new JButton(question.getOption1());
        answerButtons[1] = new JButton(question.getOption2());
        answerButtons[2] = new JButton(question.getOption3());
        answerButtons[3] = new JButton(question.getOption4());

        for (JButton button : answerButtons) {
            button.addActionListener(e -> checkAnswer(e.getActionCommand(), question));
            questionPanel.add(button);
        }

        questionPanel.revalidate();
        questionPanel.repaint();
    }

    private void checkAnswer(String selectedAnswer, Question question) {
        int selectedIndex = -1;
        for (int i = 0; i < answerButtons.length; i++) {
            if (selectedAnswer.equalsIgnoreCase(answerButtons[i].getText())) {
                selectedIndex = i + 1; 
                break;
            }
        }

        if (selectedIndex == question.getCorrectOption()) {
            gameScore += 1;
        }

        questionIndex++;
        displayQuestion();
    }

    private void endGame() {
        gameScores[currentGame - 1] = gameScore;
        totalScore = Arrays.stream(gameScores).sum();
        JOptionPane.showMessageDialog(this, "Game " + currentGame + " Over! Your score: " + gameScore);
        scoreLabel.setText("Total Score: " + totalScore);
        saveGameScore();

        if (currentGame < 5) {
            currentGame++;
            startGame();
        } else {
            JOptionPane.showMessageDialog(this, "All games completed! Final Score: " + totalScore);
            dispose();
        }
    }

    private void saveGameScore() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CompetitionDB", "root", "");
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE Competitors SET score1 = ?, score2 = ?, score3 = ?, score4 = ?, score5 = ? WHERE competitorID = ?")) {

            for (int i = 0; i < 5; i++) {
                stmt.setInt(i + 1, gameScores[i]);
            }
            stmt.setInt(6, playerID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

