package gg.sulfur.client.soundfx;

import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class SoundFXUtil {


    public static void setVolume(float volume, String filename, Clip clip) {

        try {
            File sound = new File(filename);
            //System.out.println(volume);
            if (!clip.isOpen()) {
                clip.open(AudioSystem.getAudioInputStream(sound));
                FloatControl gainControl = (FloatControl) clip
                        .getControl(FloatControl.Type.MASTER_GAIN);
                if (volume > 1f) {
                    return;
                }
                double gain = volume; //Double.parseDouble("." + volume); // number between 0 and 1 (loudest)
                float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            } else {
                FloatControl gainControl = (FloatControl) clip
                        .getControl(FloatControl.Type.MASTER_GAIN);
                if (volume > 1f) {
                    return;
                }
                double gain = volume; //Double.parseDouble("." + volume); // number between 0 and 1 (loudest)
                float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
                gainControl.setValue(dB);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            LogManager.getLogger().error("?????????? ERROR ??????????");
        }
    }

}
