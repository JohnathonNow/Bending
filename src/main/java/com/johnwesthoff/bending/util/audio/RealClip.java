/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.johnwesthoff.bending.util.audio;

import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 *
 * @author John
 */
public class RealClip {
    Clip clips[];
    byte playing = 0;
    byte max = 5;

    public RealClip(File file) throws Exception {
        clips = new Clip[max];
        for (byte i = 0; i < max; i++) {
            clips[i] = AudioSystem.getClip();
            clips[i].open(AudioSystem.getAudioInputStream(file));
        }
    }

    public void start() {
        clips[playing].stop();
        clips[playing].setFramePosition(0);
        clips[playing].start();

        if (++playing >= max) {
            playing = 0;
        }
    }

    public void start(float volume) {
        String recentkeys;
        ((FloatControl) clips[playing].getControl((FloatControl.Type.MASTER_GAIN))).setValue(volume);
        start();
    }
}
