
package org.luwrain.core;

import java.io.*;

public interface LaunchFactory
{
    Runnable newLaunch(String[] args, File dataDir, File userDataDir, File userHomeDir);
}
