
package org.luwrain.hardware;

import java.io.File;

public interface Hardware
{
    SysDevice[] getSysDevices();
    StorageDevice[] getStorageDevices();
    Partition[] getMountedPartitions();
    File getRoot(File relativeTo);
}
