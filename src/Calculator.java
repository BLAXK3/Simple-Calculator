import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Objects;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Calculator {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Lazy Calculator");
        frame.setSize(400, 580);
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(Calculator.class.getResource("Image/icon.jpg"))).getImage());
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.DARK_GRAY);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));
        buttonPanel.setBackground(Color.DARK_GRAY);

        JLabel label = new JLabel(" ");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.WHITE);

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(400, 130));
        textField.setMaximumSize(new Dimension(400, 130));
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setFont(new Font("Arial", Font.BOLD, 32));
        textField.setBorder(new EmptyBorder(0, 0, 0, 0));
        textField.setBackground(Color.DARK_GRAY);
        textField.setForeground(Color.WHITE);
        textField.setEditable(false);

        String[] text = {
                "%", "÷", "C", "CE",
                "7", "8", "9", "DEL",
                "4", "5", "6", "×",
                "1", "2", "3", "+",
                ".", "0", "-", "="
        };

        for (String texts : text) {
            JButton button = getJButton(texts, textField, label);
            buttonPanel.add(button);
        }

        panel.add(label);
        panel.add(textField);
        panel.add(buttonPanel);
        frame.add(panel);
        frame.setVisible(true);
    }

    /**
     * This method creates a JButton for the calculator with specific functionality based on the button text.
     *
     * @param texts The text displayed on the button (e.g., numbers, operators, or commands like "C", "CE").
     * @param textField The JTextField where the result or input is displayed.
     * @param label The JLabel that shows the current operation or result in the calculator.
     * @return A JButton object configured with an action listener that handles button clicks.
     */
    private static JButton getJButton(String texts, JTextField textField, JLabel label) {
        JButton button = new JButton(texts);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setBackground(Color.LIGHT_GRAY);
        button.addActionListener(e -> {
            String previousText = textField.getText();
            if (texts.equals("C")) {
                textField.setText("");
                label.setText(" ");
            } else if (texts.matches(".*[+\\-*/÷×%=].*") && !previousText.isEmpty()) {
                double result;

                if (label.getText().length() > 1 && !label.getText().contains("=") && label.getText().substring(label.getText().length() - 1).equals(texts)) {
                    if (textField.getText().matches("\\d+(\\.\\d+)?") && texts.matches(".*[+\\-*/÷×%].*")) {
                        result = evaluate(Double.parseDouble(label.getText().substring(0, label.getText().length() - 2)), Double.parseDouble(textField.getText()), texts);
                        if (result - (int) result <= 0) {
                            textField.setText(Integer.toString((int) result));
                        } else {
                            textField.setText(Double.toString(result));
                        }
                    }
                }
                if (!texts.equals("=") && label.getText().equals(" ")) {
                    label.setText(previousText + " " + texts);
                } else {
                    if (texts.equals("=") && !label.getText().contains("=")) {
                        label.setText(label.getText() + " " + textField.getText() + " " + texts);
                        result = evaluate(Double.parseDouble(label.getText().substring(0, label.getText().length() - (textField.getText().length() + 5))), Double.parseDouble(textField.getText()), label.getText().replaceAll("[^+\\-÷×%]", "").trim());
                        if (result - (int) result <= 0) {
                            textField.setText(Integer.toString((int) result));
                        } else {
                            textField.setText(Double.toString(result));
                        }
                    }
                }
            } else if (texts.equals("DEL") && !previousText.isEmpty()) {
                if (label.getText().contains("=")) {
                    label.setText(" ");
                } else {
                    textField.setText(previousText.substring(0, previousText.length() - 1));
                }
            } else if (texts.equals("CE")) {
                if (label.getText().contains("=")) {
                    label.setText(" ");
                }
                textField.setText("");
            } else {
                if (!texts.matches(".*[+*\\-/÷×%=DELGithub].*")) {
                    if (texts.equals(".") && !textField.getText().isEmpty()) {
                        textField.setText(previousText + texts);
                    } else {
                        if (!texts.equals(".")) {
                            textField.setText(previousText + texts);
                        }
                    }
                }
            }
        });
        return button;
    }

    /**
     * Evaluates a mathematical operation based on the provided operator
     *
     * @param number1 The first operand
     * @param number2 The second operand
     * @param operator The mathematical operator
     * @return The result of the calculation
     * @throws ArithmeticException If division by zero is attempted
     */
    private static double evaluate(double number1, double number2, String operator) {
        return switch (operator) {
            case "+" -> number1 + number2;
            case "-" -> number1 - number2;
            case "×" -> number1 * number2;
            case "÷" -> {
                if (number2 == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }
                yield number1 / number2;
            }
            case "%" -> number1 % number2;
            default -> Double.NaN;
        };
    }
}
