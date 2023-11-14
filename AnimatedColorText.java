import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimatedColorText extends JFrame {
    private JLabel textLabel;
    private Timer timer;
    private int colorIndex = 0;
    private Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};

    public AnimatedColorText() {
        setTitle("Animated Color Text");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textLabel = new JLabel("Yahahaha");
        textLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(textLabel, BorderLayout.CENTER);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textLabel.setForeground(colors[colorIndex]);
                colorIndex = (colorIndex + 1) % colors.length;
            }
        });

        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AnimatedColorText app = new AnimatedColorText();
            app.setVisible(true);
        });
    }
}
