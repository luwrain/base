
package org.luwrain.core;

import java.io.*;

public interface LaunchFactory
{
    Runnable newLaunch(boolean standalone, String[] args, File dataDir, File userDataDir, File userHomeDir);
}
