package csmaProject;

import java.util.Vector;

public class Packet
{
    static final int RTS = 1;
    static final int CTS = 2;
    static final int PKT = 3;
    static final int ACK = 4;
    private MobileStation owner;
    private int type;
    private Vector corrupted;
    
    public Packet(MobileStation mobilestation, int i) {
	owner = mobilestation;
	type = i;
	corrupted = new Vector();
    }
    
    public boolean getCorrupted(Terminal terminal) {
	return corrupted.contains(terminal);
    }
    
    public int getNav() {
	int i;
	switch (type) {
	case 1:
	    i = 94;
	    break;
	case 2:
	    i = 76;
	    break;
	default:
	    i = 0;
	}
	return i;
    }
    
    public MobileStation getOwner() {
	return owner;
    }
    
    public int getType() {
	return type;
    }
    
    public void setCorrupted(Terminal terminal) {
	if (!corrupted.contains(terminal))
	    corrupted.addElement(terminal);
    }
}
