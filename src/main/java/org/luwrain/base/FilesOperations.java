
package org.luwrain.base;

import java.io.*;

public interface FilesOperations
{
    FilesOperation copy(FilesOperation.Listener listener, String name, File[] whatToCopy, File copyTo);
    FilesOperation move(FilesOperation.Listener listener, String name, File[] whatToMove, File moveTo);
    FilesOperation delete(FilesOperation.Listener listener, String name, File[] whatToDelete);
}
