package Util;

import android.media.AudioManager;
import android.media.ToneGenerator;

public class Beep {
    ToneGenerator toneGenerator;

    public Beep(ToneGenerator toneGenerator) {
        this.toneGenerator = toneGenerator;
    }

    public Beep() {
    }

    public void beep1(){
        toneGenerator=new ToneGenerator(AudioManager.STREAM_ALARM, 800);
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 3000);
    }

    public void beep_disable(){
        toneGenerator=new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 200);
    }

    public void beep_key(){
        toneGenerator=new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE, 200);
    }

    public void beep4(){
        toneGenerator=new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_NETWORK_BUSY, 200);
    }

}
