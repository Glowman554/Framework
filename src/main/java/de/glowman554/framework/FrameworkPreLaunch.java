package de.glowman554.framework;

import de.glowman554.framework.client.event.EventManager;
import de.glowman554.framework.client.event.EventTarget;
import de.glowman554.framework.client.event.impl.WindowOpeningEvent;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class FrameworkPreLaunch implements PreLaunchEntrypoint {
    public static Optional<JFrame> frame = Optional.empty();

    private static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("mac");
    }

    @Override
    public void onPreLaunch() {
        if (isMac()) {
            return;
        }

        EventManager.register(this);

        try {
            createAndShowUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventTarget
    public void onWindowOpen(WindowOpeningEvent event) {
        frame.ifPresent(jFrame -> {
            jFrame.setVisible(false);
            jFrame.dispose();
            frame = Optional.empty();
        });
    }

    private void createAndShowUI() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        JFrame loadingFrame = new JFrame("Minecraft");
        loadingFrame.setResizable(false);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(256, (int) progressBar.getPreferredSize().getHeight()));

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(new JLabel("Initializing Minecraft..."), BorderLayout.NORTH);
        mainPanel.add(progressBar, BorderLayout.CENTER);
        mainPanel.add(new JLabel("Thank you for using Framework by glowman554 <3"), BorderLayout.SOUTH);

        loadingFrame.setContentPane(mainPanel);
        loadingFrame.pack();
        loadingFrame.setLocationRelativeTo(null);
        loadingFrame.setVisible(true);
        frame = Optional.of(loadingFrame);
    }
}
