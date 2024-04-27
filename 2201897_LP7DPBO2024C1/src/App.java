import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App extends JFrame {
    private JLabel scoreLabel;
    private JButton StartButton;
    private JButton exitButton;
    private JPanel mainPanel;

    public static void main(String[] args)
    {
        App window = new App();
        // atur ukuran window
        window.setSize(400, 300);
        // letakkan window di tengah layar
        window.setLocationRelativeTo(null);
        // isi window
        window.setContentPane(window.mainPanel);
        // ubah warna background
        window.getContentPane().setBackground(Color.white);
        // tampilkan window
        window.setVisible(true);
        // agar program ikut berhenti saat window diclose
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public App() {
        // klik start
        StartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // masuk ke flappy bird
                startApp();
            }
        });
        // klik exit
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // memberhentikan semua ketika exit di klik
            }
        });
    }

    public void startApp() {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        //JLabel
        scoreLabel = new JLabel("0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(scoreLabel, BorderLayout.NORTH);

        //buat objek JPanel
        FlappyBird flappyBird = new FlappyBird(this);
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);
    }

    public void setSkor(int skor)
    {
        scoreLabel.setText(Integer.toString(skor));
    }
}





