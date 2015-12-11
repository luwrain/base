
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

    public interface Listener 
    {
	void onFinished();
    }

    boolean init(String[] cmdLine, Registry registry, String path);
    Voice[] getVoices();
    String getChannelName();
    Set<Features>  getFeatures();
    boolean isDefault();
    void setDefaultVoice(String name);
    void setDefaultPitch(int value);
    void setDefaultRate(int value);
    void speak(String text);
    void speak(String text, int relPitch, int relRate);
    void speak(String text, Listener listener, int relPitch, int relRate);
    boolean synth(String text, int pitch, int rate, AudioFormat format, OutputStream stream);
    void speak(String text, OutputStream stream, int relPitch, int relRate);
    void silence();
}
