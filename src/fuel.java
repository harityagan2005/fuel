import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class fuel {
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Fuel Cost Estimator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400); // bigger window
        frame.setLayout(new GridBagLayout());

        // Dark background
        frame.getContentPane().setBackground(new Color(25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1; // allow horizontal expansion

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Labels and text fields
        JLabel distanceLabel = new JLabel("Distance (km):");
        distanceLabel.setForeground(Color.WHITE);
        distanceLabel.setFont(labelFont);
        JTextField distanceField = new JTextField();
        distanceField.setPreferredSize(new Dimension(250, 30)); // bigger box
        distanceField.setBackground(new Color(45, 45, 45));
        distanceField.setForeground(Color.WHITE);
        distanceField.setCaretColor(Color.CYAN);
        distanceField.setFont(inputFont);

        JLabel mileageLabel = new JLabel("Mileage (km/litre):");
        mileageLabel.setForeground(Color.WHITE);
        mileageLabel.setFont(labelFont);
        JTextField mileageField = new JTextField();
        mileageField.setPreferredSize(new Dimension(250, 30));
        mileageField.setBackground(new Color(45, 45, 45));
        mileageField.setForeground(Color.WHITE);
        mileageField.setCaretColor(Color.CYAN);
        mileageField.setFont(inputFont);

        JLabel priceLabel = new JLabel("Fuel Price per litre:");
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setFont(labelFont);
        JTextField priceField = new JTextField();
        priceField.setPreferredSize(new Dimension(250, 30));
        priceField.setBackground(new Color(45, 45, 45));
        priceField.setForeground(Color.WHITE);
        priceField.setCaretColor(Color.CYAN);
        priceField.setFont(inputFont);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBackground(new Color(70, 70, 70));
        calculateButton.setForeground(Color.CYAN);
        calculateButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setBackground(new Color(35, 35, 35));
        resultArea.setForeground(new Color(0, 255, 128));
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setBorder(BorderFactory.createLineBorder(Color.CYAN));

        // Add components
        gbc.gridx = 0; gbc.gridy = 0; frame.add(distanceLabel, gbc);
        gbc.gridx = 1; frame.add(distanceField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; frame.add(mileageLabel, gbc);
        gbc.gridx = 1; frame.add(mileageField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; frame.add(priceLabel, gbc);
        gbc.gridx = 1; frame.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; frame.add(calculateButton, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1; frame.add(new JScrollPane(resultArea), gbc);

        // Action listener
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double distance = Double.parseDouble(distanceField.getText());
                    double mileage = Double.parseDouble(mileageField.getText());
                    double pricePerLitre = Double.parseDouble(priceField.getText());

                    double litresNeeded = distance / mileage;
                    double totalCost = litresNeeded * pricePerLitre;

                    resultArea.setText("Distance: " + distance + " km\n"
                            + "Mileage: " + mileage + " km/litre\n"
                            + "Fuel Price: " + pricePerLitre + " per litre\n"
                            + "Litres Needed: " + litresNeeded + "\n"
                            + "Total Fuel Cost: " + totalCost);
                } catch (NumberFormatException ex) {
                    resultArea.setText("⚠️ Please enter valid numbers!");
                }
            }
        });

        // Show frame
        frame.setVisible(true);
    }
}
