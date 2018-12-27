package csmaProject;

import java.awt.Color;
import java.awt.Label;
import java.util.Random;

/***************************************************************************************
*    Title: MobileStation.java
*    Author: William Burcham
*    Date: 11-20-2018
*    Code version: V1
*
***************************************************************************************/

public class MobileStation extends Terminal
{
    private BaseStation BS;
    private int packetToSend;
    private int CWorder;
    private int backoffValue = 0;
    private int nav;
    private static Random rand = new Random();
    private Label queueLBL;
    private Label backoffLBL;
    
    public class State
    {
	static final int idle = 0;
	static final int DIFS_beforeCountdown = 1;
	static final int countdown = 2;
	static final int emitRTS = 3;
	static final int SIFS_before_rcvCTS = 4;
	static final int rcvCTS = 5;
	static final int SIFS_before_emitPKT = 6;
	static final int emitPKT = 7;
	static final int SIFS_before_rcvACK = 8;
	static final int rcvACK = 9;
    }
    
    public MobileStation(String string, SimTimer simtimer,
			 BaseStation basestation) {
	super(string, simtimer);
	BS = basestation;
	changeState(0);
	packetToSend = 0;
    }
    
    // Implement Backoff Function (Random Exponential)
    // Used to create backoff when it realizes nav is on or the channel is busy, simple
    // exponential pow for each time its called, gets larger backoff.
    private void BEB() {
    	backoffValue = rand.nextInt((int)Math.pow(2, CWorder));
    	CWorder++;

    }
    
    // activeAction is called over and over, so any change of states should happen within
    // this method. State changes are called by changeState(x), where x is the State represented
    // by the State class, each state being an integer called upon.
    private void activeAction() {
    	/**
    	 * Large switch statement, for each of the different states.
    	 * If a state wants to send a packet but realizes the channel is busy, gets stuck on 
    	 * difs_before count down until it can move forward. Switch statement was used because 
    	 * a giant if tree would have been real ugly.
    	 */
    	switch (state) 
    	{
    	
    	case State.idle:
    		if(packetToSend > 0)
    		{
        		changeState(State.DIFS_beforeCountdown);
        		
        		
        		if(myChannel.getBusy() || nav > 0)
        		{
        			BEB();
        		}
    		}
    		break;
    		
    	case State.DIFS_beforeCountdown:
    		
    		if(elapsedTime(5))
    		{
			changeState(State.countdown);
    		}
    		if(myChannel.getBusy() || nav > 0)
    		{
    			changeState(State.DIFS_beforeCountdown);
    		}
    		break;
    		
    	case State.countdown:
    		
    		if(elapsedTime(5))
    		{
    			backoffValue--;
    			changeState(State.countdown);
    		}
    		if(myChannel.getBusy() || nav > 0)
    		{
    			changeState(State.idle);
    		}
    		break;
    		/**
    		 * Packet emit here, myChannel is letting base station know a packets coming and that
    		 * it is a request to send.
    		 */
    	case State.emitRTS:
    		
    		if(elapsedTime(15))
    		{
    		myChannel.receptionAction(emmitedPacket);
    		changeState(State.SIFS_before_rcvCTS);
    		}
    		break;
    		
    	case State.SIFS_before_rcvCTS:
    		
    		if(elapsedTime(3))
    		{
    		changeState(State.rcvCTS);
    		}
    		break;
    		
    		/**
    		 * Only time this state should happen is during timeouts, which should be rare.
    		 */
    	case State.rcvCTS:
    		
    		if(elapsedTime(16))
    		{
    		BEB();
    		changeState(State.DIFS_beforeCountdown);
    		}
    		break;
    		
    	case State.SIFS_before_emitPKT:
    		
    		if(elapsedTime(3))
    		{
    		changeState(State.emitPKT);
    		}
    		break;
    		
    		/**
    		 * Actual package emitting here, letting basestation know the payload is coming.
    		 */
    	case State.emitPKT:
    		
    		if(elapsedTime(60))
    		{
    		myChannel.receptionAction(emmitedPacket);
    		changeState(State.SIFS_before_rcvACK);
    		}
    		break;
    		
    	case State.SIFS_before_rcvACK:
    		
    		if(elapsedTime(3))
    		{
    		changeState(State.rcvACK);
    		}
    		break;
    		
    	case State.rcvACK:
    		
    		if(elapsedTime(11))
    		{
    		BEB();
    		changeState(State.DIFS_beforeCountdown);
    		}
    		break;	
    		
    	}
    }
    
    public void addPktToSendingQueue() {
	packetToSend++;
	txtUpdate();
    }
    
    protected void changeState(int i) {
	super.changeState(i);
	if (i == State.countdown && backoffValue == 0)
	    changeState(3);
	if (i == State.idle)
	    CWorder = 3;
	if (i == State.emitRTS)
	    emmitedPacket = new Packet(this, 1);
	if (i == State.emitPKT)
	    emmitedPacket = new Packet(this, 3);
    }
    
    public boolean emmiting() {
	return state == State.emitRTS || state == State.emitPKT;
    }
    
    protected Color getChannelColor() {
	if (nav > 0)
	    return Color.orange;
	if (myChannel.getBusy())
	    return Color.red;
	return Color.green;
    }
    
    protected Color getStateColor() {
	Color color = Color.white;
	switch (state) {
	case 3:
	    color = new Color(0, 0, 255);
	    break;
	case 7:
	    color = new Color(175, 0, 0);
	    break;
	case 2:
	    color = Color.lightGray;
	    break;
	}
	return color;
    }
    
    protected void graphicUpdate() {
	super.graphicUpdate();
	txtUpdate();
    }

    // Here to handle package receival from basestation.
    public void receptionAction(Packet packet) {
    	
    	switch (state)
    	{
    	case State.idle:
    		if((packet.getCorrupted(this) == false) && (packet.getNav() > nav))
    		{
    			nav = packet.getNav();
    		}
    		break;
    	
    	case State.DIFS_beforeCountdown:
    		if((packet.getCorrupted(this) == false) && (packet.getNav() > nav))
    		{
    			nav = packet.getNav();
    		}
    		break;
    	
    	case State.countdown:
    		if((packet.getCorrupted(this) == false) && (packet.getNav() > nav))
    		{
    			nav = packet.getNav();
    		}
    		break;
    		
    		/**
    		 * Just check if everything is correct before changing state to before emitting payload.
    		 */
    	case State.rcvCTS:
    		if((packet.getCorrupted(this) == false) && 
    				(packet.getType() == Packet.CTS) && (packet.getOwner() == this)) 
    		{
    			changeState(State.SIFS_before_emitPKT);
    		}
    		else
    		{
    			BEB();
    			changeState(State.DIFS_beforeCountdown);
    		}
    		
    		/**
    		 * As long as the packet isn't corrupted, the packet type is ACK, and the owner
    		 * belongs correctly, remove the packet from the channel queue and go back to idle.
    		 */
    	case State.rcvACK:
    		if((packet.getCorrupted(this) == false) && 
    				(packet.getType() == Packet.ACK) && (packet.getOwner() == this)) 
    		{
    			packetToSend--;
    			if(packetToSend > 0)
    			{
    				CWorder = 3;
    				BEB();
    				changeState(State.DIFS_beforeCountdown);
    			}
    			else
    			{
    	    		changeState(State.idle);
    			}
    		}
    		//Fail case
    		else if((packet.getCorrupted(this) == true) || 
    				(packet.getType() != Packet.CTS) || (packet.getOwner() != this))
    		{
    			System.out.println(packet.getType());
    			BEB();
    			changeState(State.DIFS_beforeCountdown);
    		}
    		
    		break;
    	
    	}
    }
    
    public void setDisplay(Label label, Label label_0_, TimeLine timeline) {
	queueLBL = label;
	backoffLBL = label_0_;
	super.setDisplay(timeline);
    }
    
    private void txtUpdate() {
	queueLBL.setText(Integer.toString(packetToSend));
	backoffLBL.setText(Integer.toString(backoffValue));
    }
    
    public void update() {
	nav--;
	if (nav < 0)
	    nav = 0;
	activeAction();
	graphicUpdate();
    }
}
