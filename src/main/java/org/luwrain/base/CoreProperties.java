
package org.luwrain.base;

import java.nio.file.*;

public interface CoreProperties
{
    String getProperty(String propName);
    Path getPathProperty(String propName);
}
