
package org.luwrain.core;

public interface OsCommand
{
public interface Output
{
    void onNewOsCommandLine(String line);
}

public interface Listener
{
    void onOsCommandFinish(int exitCode, String[] output);
}

    boolean isRunning();
    void cancel();
    String[] getOutput();

}
