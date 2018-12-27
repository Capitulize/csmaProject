package csmaProject;

import java.awt.Color;

abstract class Terminal
{
    private String id;
    protected int state;
    private int stateChangeTime;
    protected SimTimer timer;
    protected Packet emmitedPacket;
    protected Channel myChannel;
    protected TimeLine timeLine;
    
    Terminal(String string, SimTimer simtimer) {
	id = string;
	timer = simtimer;
	myChannel = new Channel(this);
	stateChangeTime = timer.getTime();
    }
    
    protected void changeState(int i) {
	state = i;
	stateChangeTime = timer.getTime();
    }
    
    public void channelUpdate() {
	myChannel.update();
    }
    
    protected boolean elapsedTime(int i) {
    	boolean bool = timer.getTime() >= stateChangeTime + i;
    	return bool;
    }
    
    public abstract boolean emmiting();
    
    protected abstract Color getChannelColor();
    
    public String getId() {
	return id;
    }
    
    protected abstract Color getStateColor();
    
    protected void graphicUpdate() {
	timeLine.update(getStateColor(), getChannelColor());
    }
    
    public void markEmmitedPacketAsCorrupted(Terminal terminal_0_) {
	emmitedPacket.setCorrupted(terminal_0_);
    }
    
    public abstract void receptionAction(Packet packet);
    
    public void setDisplay(TimeLine timeline) {
	timeLine = timeline;
    }
    
    public void setVisibleTerminals(Terminal[] terminals) {
	myChannel.setVisibleTerminals(terminals);
    }
    
    public abstract void update();
}
