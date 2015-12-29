
package org.luwrain.hardware;

import java.io.File;

public interface Hardware
{
    SysDevice[] getSysDevices();
    StorageDevice[] getStorageDevices();
    int mountAllPartitions(StorageDevice device);
    int umountAllPartitions(StorageDevice device);
    Partition[] getMountedPartitions();
    File getRoot(File relativeTo);
    AudioMixer getAudioMixer();
}
