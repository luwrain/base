
package org.luwrain.speech;

public interface BackEnd
{
    String init(String[] cmdLine);
    void say(String text);
    void sayLetter(char letter);
    void silence();
    void setPitch(int value);
}
