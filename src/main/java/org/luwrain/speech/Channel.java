
package org.luwrain.speech;

import java.util.Set;
import java.io.OutputStream;
import javax.sound.sampled.AudioFormat;

import org.luwrain.core.Registry;

public interface Channel
{
    public enum Features {
	CAN_SYNTH_TO_STREAM,
	CAN_SYNTH_TO_SPEAKERS,
	CAN_NOTIFY_WHEN_FINISHED,
    };

    public enum PuncMode {
	ALL,
	NONE,
    };

    public interface Listener 
    {
	void onFinished();
    }

    //Registry and path may be null, meaning the initialization should be used from command line options
    boolean init(String[] cmdLine, Registry registry, String path);
    void close();
    Voice[] getVoices();
    String getChannelName();
    Set<Features>  getFeatures();
    boolean isDefault();
    String getCurrentVoiceName();
    void setCurrentVoice(String name);
    int getDefaultPitch();
    void setDefaultPitch(int value);
    int getDefaultRate();
    void setDefaultRate(int value);
    PuncMode getCurrentPuncMode();
    void setCurrentPuncMode(PuncMode mode);
    void speak(String text, Listener listener, int relPitch, int relRate);
    void speakLetter(char letter, Listener listener, int relPitch, int relRate);
    void silence();
    AudioFormat[] getSynthSupportedFormats();
    boolean synth(String text, int pitch, int rate, AudioFormat format, OutputStream stream);
}
