package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Created by _red_ on 11.06.17.
 */
public class OrderForm extends JFrame {
    private final JTextArea textArea;

    public OrderForm(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(200 , 300);
        frame.setResizable(true);

        frame.setLocationRelativeTo(null);

        JPanel firstPanel = new JPanel();

        textArea = new JTextArea(15, 13);
        textArea.setEnabled(false);
        textArea.setLineWrap(true);

        firstPanel.add(textArea);

        frame.add(firstPanel, BorderLayout.NORTH);

        JScrollPane scrollBar = new JScrollPane(textArea);
        frame.getContentPane().add(scrollBar, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public synchronized void setTextArea(Map<String, Integer> map) {
        this.textArea.setText(null);
        StringBuilder str = new StringBuilder();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            str.append("Word - ").append(entry.getKey()).append(" | Entries - ").
                    append(entry.getValue().toString()).append("\n");
        }

        this.textArea.append(str.toString());
    }
}
