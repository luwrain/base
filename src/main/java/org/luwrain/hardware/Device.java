
package org.luwrain.hardware;

public class Device
{
    public final static int UNKNOWN = 0;
    public final static int PCI = 1;
    public final static int YUSB = 2;

    public int type = UNKNOWN;
    public String id = "";
    public String cls = "";
    public String vendor = "";
    public String model = "";
    public String driver = "";
    public String module = "";
}
