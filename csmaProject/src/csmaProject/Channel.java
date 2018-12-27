package csmaProject;

public class Channel
{
    private Terminal owner;
    private Terminal[] visibleTerminals;
    private boolean busy;
    
    public Channel(Terminal terminal) {
	owner = terminal;
    }
    
    private int countSenders() {
	int i = 0;
	for (int i_0_ = 0; i_0_ < visibleTerminals.length; i_0_++) {
	    if (visibleTerminals[i_0_].emmiting())
		i++;
	}
	return i;
    }
    
    public boolean getBusy() {
	return busy;
    }
    
    public void receptionAction(Packet packet) {
	for (int i = 0; i < visibleTerminals.length; i++)
	    visibleTerminals[i].receptionAction(packet);
    }
    
    public void setVisibleTerminals(Terminal[] terminals) {
	visibleTerminals = terminals;
    }
    
    public void update() {
	int i = countSenders();
	busy = i > 0;
	if (i > 1) {
	    for (int i_1_ = 0; i_1_ < visibleTerminals.length; i_1_++) {
		if (visibleTerminals[i_1_].emmiting())
		    visibleTerminals[i_1_].markEmmitedPacketAsCorrupted(owner);
	    }
	}
    }
}
