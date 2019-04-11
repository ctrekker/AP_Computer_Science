import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
public class ResourceLoaderGUI extends JFrame {
    private JProgressBar progressBar;
    private JLabel label;
    public ResourceLoaderGUI() {
        try {
            setIconImage(new ImageIcon(getClass().getResource("icon.png")).getImage());
        }
        catch(Exception e) {
            System.out.println("Error loading window icon");
            System.out.println("Continuing without asset...");
        }

        setTitle("Installing TexasHoldem by Connor Burns");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container content = getContentPane();
        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder("Installing Resources...");
        progressBar.setBorder(border);
        content.add(progressBar, BorderLayout.NORTH);
        JLabel note=new JLabel("This is a first-time only operation");
        note.setFont(new Font("", Font.PLAIN, 10));
        content.add(note, BorderLayout.CENTER);
        label=new JLabel("");
        content.add(label, BorderLayout.SOUTH);
        setSize(300, 120);
        setVisible(true);
    }
    public void updateProgress(int progress) {
        progressBar.setValue(progress);
        progressBar.repaint();
        revalidate();
    }
    public void updateLabel(String str) {
        label.setText(str);
    }
}
