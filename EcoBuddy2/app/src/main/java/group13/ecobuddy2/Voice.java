package group13.ecobuddy2;

import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by ariffia@mcmaster.ca on 2015-04-21.
 */
public class Voice
{
    public static TextToSpeech tts;

    /**
     * Text to speech engine setup
     */
    public static void installTTS() {
        tts = new TextToSpeech(
                Main.me.getApplication(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status != TextToSpeech.ERROR){
                            tts.setLanguage(Locale.UK);
                        }
                    }
                }
        );
    }

    /**
     * Say a string message
     * @param message
     */
    public static void sayThis(String message) {
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }
}
