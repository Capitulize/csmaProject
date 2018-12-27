package csmaProject;

import java.awt.Color;

/***************************************************************************************
*    Title: BaseStation.java
*    Author: William Burcham
*    Date: 11-20-2018
*    Code version: V1
*
***************************************************************************************/
public class BaseStation extends Terminal
{
    private MobileStation curSender;
    
    class State
    {
	static final int idle = 0;
	static final int SIFS_before_emitCTS = 1;
	static final int emitCTS = 2;
	static final int SIFS_before_rcvPKT = 3;
	static final int rcvPKT = 4;
	static final int SIFS_before_emitACK = 5;
	static final int emitACK = 6;
    }
    
    public BaseStation(String string, SimTimer simtimer) {
	super(string, simtimer);
    }
    

    //Switch statement method to handle cases between packages, specifically sending packages to
    //channels, and handling states of base station. State changes are caused by changeState(x),
    //where x is the State object represented by integer values.
    private void activeAction() {
    	switch (state) 
    	{
    	case State.idle:
    		break;
    		
    	case State.SIFS_before_emitCTS:
    		if(elapsedTime(3))
    		{
			changeState(State.emitCTS);
    		}
    		break;
    		
    		/**
    		 * Emits a clear to send to the channel it is dealing with.
    		 */
    	case State.emitCTS:
    		if(elapsedTime(15))
    		{
    		myChannel.receptionAction(emmitedPacket);	
    		changeState(State.SIFS_before_rcvPKT);
    		}
    		break;
    		
    	case State.SIFS_before_rcvPKT:
    		if(elapsedTime(3))
    		{	
    		changeState(State.rcvPKT);
    		}
    		break;
    		
    	case State.rcvPKT:
    		if(elapsedTime(61))
    		{	
    		changeState(State.idle);
    		}
    		break;
    	
    	case State.SIFS_before_emitACK:
    		if(elapsedTime(3))
    		{	
    		changeState(State.emitACK);
    		}
    		break;
    		
    		/**
    		 * Emits a package from basestation to mobilestation, acknowledgment package.
    		 */
    	case State.emitACK:
    		if(elapsedTime(10))
    		{
    			myChannel.receptionAction(emmitedPacket);
    			changeState(State.idle);
    		}
    		break;
    	
    	}
    }
    
    protected void changeState(int i) {
	super.changeState(i);
	if (i == 2)
	    emmitedPacket = new Packet(curSender, 2);
	if (i == 6)
	    emmitedPacket = new Packet(curSender, 4);
    }
    
    public boolean emmiting() {
	return state == 6 || state == 2;
    }
    
    protected Color getChannelColor() {
	return Color.white;
    }
    
    protected Color getStateColor() {
	Color color = Color.white;
	switch (state) {
	case 2:
	    color = new Color(0, 0, 128);
	    break;
	case 6:
	    color = new Color(0, 0, 0);
	    break;
	}
	return color;
    }

    // Implement Protocol State Machine (Receive portion)
    // The receival portion of the base station, such as getting packets from mobile stations
    // like rts and payload.
    public void receptionAction(Packet packet) {
    	
    	switch (state) 
    	{
    	/**
    	 * Check if a packet was sent and that it is a request to send.
    	 */
    	case State.idle:
    		if((packet.getCorrupted(this) == false) && (packet.getType() == Packet.RTS))
    		{
    			curSender = packet.getOwner();
    			changeState(State.SIFS_before_emitCTS);
    		}
    		break;
    	/**
    	 * Check if the packet is a package from the channel and that it is from the correct 
    	 * channel.	
    	 */
    	case State.rcvPKT:
    		if((packet.getCorrupted(this) == false) && 
    				(packet.getType() == Packet.PKT) && (curSender == packet.getOwner()))
    		{
    			
    			changeState(State.SIFS_before_emitACK);
    		}
    		break;
    	}
    }
    
    public void update() {
	activeAction();
	this.graphicUpdate();
    }
}
