package Util;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;

public class GenerateSound {
    private static final String TAG = "GenerateSound";
    //tone for key

    //tone for alert

    //tone for alarm
    private int VOLUME = 50;


    public void generateToneAlert() {
        int streamType = AudioManager.STREAM_ALARM;
        int volume = VOLUME;
        int toneType = ToneGenerator.TONE_CDMA_ABBR_ALERT;
        int durationMs = 500;
        try {
            ToneGenerator toneGenerator = new ToneGenerator(streamType, volume);
            if (toneGenerator != null) {
                toneGenerator.startTone(toneType, durationMs);
            }
            //toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP,1000);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "createToneGenerator: exception");
            e.printStackTrace();

        } finally {

        }
    }

    //beep sound
    public void generateToneRing() {
        int streamType = AudioManager.STREAM_RING;
        int volume = VOLUME;
        int toneType = ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK;
        int durationMs = 500;
        try {
            ToneGenerator toneGenerator = new ToneGenerator(streamType, volume);
            if (toneGenerator != null) {
                toneGenerator.startTone(toneType, durationMs);
            }
            //toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP,1000);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "createToneGenerator: exception");
            e.printStackTrace();

        } finally {

        }
    }


    //beep sound
    public void generateTone1() {
        int streamType = AudioManager.STREAM_MUSIC;
        int volume = VOLUME;
        int toneType = ToneGenerator.TONE_DTMF_1;
        int durationMs = 500;
        try {
            ToneGenerator toneGenerator = new ToneGenerator(streamType, volume);
            if (toneGenerator != null) {
                toneGenerator.startTone(toneType, durationMs);
            }
            //toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP,1000);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "createToneGenerator: exception");
            e.printStackTrace();

        } finally {

        }
    }

    //beep sound
    public void generateTone2() {
        int streamType = AudioManager.STREAM_MUSIC;
        int volume = VOLUME;
        int toneType = ToneGenerator.TONE_DTMF_2;
        int durationMs = 500;
        try {
            ToneGenerator toneGenerator = new ToneGenerator(streamType, volume);
            if (toneGenerator != null) {
                toneGenerator.startTone(toneType, durationMs);
            }
            //toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP,1000);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "createToneGenerator: exception");
            e.printStackTrace();

        } finally {

        }
    }

    //beep sound
    public void generateTone3() {
        int streamType = AudioManager.STREAM_MUSIC;
        int volume = VOLUME;
        int toneType = ToneGenerator.TONE_DTMF_3;
        int durationMs = 500;
        try {
            ToneGenerator toneGenerator = new ToneGenerator(streamType, volume);
            if (toneGenerator != null) {
                toneGenerator.startTone(toneType, durationMs);
            }
            //toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP,1000);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "createToneGenerator: exception");
            e.printStackTrace();

        } finally {

        }
    }

    //beep sound
    public void generateTone4() {
        int streamType = AudioManager.STREAM_MUSIC;
        int volume = VOLUME;
        int toneType = ToneGenerator.TONE_DTMF_4;
        int durationMs = 500;
        try {
            ToneGenerator toneGenerator = new ToneGenerator(streamType, volume);
            if (toneGenerator != null) {
                toneGenerator.startTone(toneType, durationMs);
            }
            //toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP,1000);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "createToneGenerator: exception");
            e.printStackTrace();

        } finally {

        }
    }

    //beep sound
    public void generateTone5() {
        int streamType = AudioManager.STREAM_MUSIC;
        int volume = VOLUME;
        int toneType = ToneGenerator.TONE_DTMF_5;
        int durationMs = 500;
        try {
            ToneGenerator toneGenerator = new ToneGenerator(streamType, volume);
            if (toneGenerator != null) {
                toneGenerator.startTone(toneType, durationMs);
            }
            //toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP,1000);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "createToneGenerator: exception");
            e.printStackTrace();

        } finally {

        }
    }
}
