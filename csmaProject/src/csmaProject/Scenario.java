package csmaProject;

import java.awt.Button;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class Scenario
{
    private String name;
    private SimTimer timer;
    private BaseStation BS;
    private MobileStation[] MS;
    private Vector emitButtons;
    
    public Scenario(String string, String[] strings, boolean[][] bools) {
	name = string;
	emitButtons = new Vector();
	timer = new SimTimer(this);
	BS = new BaseStation("Access Point", timer);
	MS = new MobileStation[strings.length];
	for (int i = 0; i < MS.length; i++)
	    MS[i] = new MobileStation(strings[i], timer, BS);
	for (int i = 0; i < MS.length; i++) {
	    Vector vector = new Vector();
	    vector.addElement(BS);
	    for (int i_0_ = 0; i_0_ < strings.length; i_0_++) {
		if (bools[i][i_0_])
		    vector.addElement(MS[i_0_]);
	    }
	    Terminal[] terminals = new Terminal[vector.size()];
	    for (int i_1_ = 0; i_1_ < vector.size(); i_1_++)
		terminals[i_1_] = (Terminal) vector.elementAt(i_1_);
	    MS[i].setVisibleTerminals(terminals);
	}
	BS.setVisibleTerminals(MS);
    }
    
    public static Scenario HIDDEN3() {
	return new Scenario("Hidden terminal (3 mobiles)",
			    new String[] { "Station 1", "Station 2",
					   "Station 3" },
			    new boolean[][] { { true, false, false },
					      { false, true, false },
					      { false, false, true } });
    }
    
    public static Scenario STANDARD3() {
	return new Scenario("Standard (3 mobiles)",
			    new String[] { "Station 1", "Station 2",
					   "Station 3" },
			    new boolean[][] { { true, true, true },
					      { true, true, true },
					      { true, true, true } });
    }
    
    public void addEmitButton(Button button) {
	emitButtons.addElement(button);
    }
    
    public void enableEmitButtons(boolean bool) {
	for (int i = 0; i < emitButtons.size(); i++)
	    ((Button) emitButtons.elementAt(i)).setEnabled(bool);
    }
    
    public ActionListener getActionListnerForMobile(final int i) {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent actionevent) {
		MS[i].addPktToSendingQueue();
	    }
	};
    }
    
    public String getMobileName(int i) {
	return MS[i].getId();
    }
    
    public int getMobileNumber() {
	return MS.length;
    }
    
    public String getName() {
	return name;
    }
    
    public String getSimActionName() {
	return timer.getActionName();
    }
    
    public String getSimState() {
	return timer.getState();
    }
    
    public void setBSDisplay(TimeLine timeline) {
	BS.setDisplay(timeline);
    }
    
    public void setMobileDisplay(int i, Label label, Label label_4_,
				 TimeLine timeline) {
	MS[i].setDisplay(label, label_4_, timeline);
    }
    
    public void simAction() {
	timer.simAction();
    }
    
    public void update() {
	BS.channelUpdate();
	for (int i = 0; i < MS.length; i++)
	    MS[i].channelUpdate();
	BS.update();
	for (int i = 0; i < MS.length; i++)
	    MS[i].update();
    }
}
