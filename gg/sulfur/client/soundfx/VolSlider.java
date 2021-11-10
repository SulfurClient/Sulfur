package gg.sulfur.client.soundfx;

import gg.sulfur.client.soundfx.downloader.References;
import net.minecraft.client.gui.GuiMainMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

public class VolSlider extends JFrame {

    JSlider volSlider = new JSlider(SwingConstants.HORIZONTAL, 1, 100, 50);
    float vol = (float) volSlider.getValue();
    float vol2 = ((float) volSlider.getValue()) / 100;

    public VolSlider() {
        JPanel panel = new JPanel();
        this.setSize(300, 180);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        JLabel label = new JLabel(volSlider.getValue() + "%");
        JButton muteButton = new JButton("Mute");
        muteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SoundFXUtil.setVolume(0, new File(References.Files.MAINMENUFILE.getFile().getCanonicalPath()).getAbsolutePath(), GuiMainMenu.clip);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                volSlider.setValue(0);
                setVol(0);
                setVol2(0);
            }
        });
        volSlider.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                label.setText(volSlider.getValue() + "%");
                setVol((float) volSlider.getValue());
                setVol2(vol / 100);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                label.setText(volSlider.getValue() + "%");
                setVol(volSlider.getValue());
                setVol2(volSlider.getValue() / 100);
            }
        });
        label.setLocation(150, 150);
        JLabel descLabel = new JLabel("This is a volume slider for main menu music");
        descLabel.setLocation(0, 165);
        panel.add(muteButton);
        panel.add(volSlider);
        panel.add(label);
        panel.add(descLabel);
        this.add(panel);
    }

    public float getVol() {
        return vol;
    }

    public float getVol2() {
        return vol2;
    }

    public void setVol(float vol) {
        this.vol = vol;
    }

    public void setVol2(float vol2) {
        this.vol2 = vol2;
    }
}
