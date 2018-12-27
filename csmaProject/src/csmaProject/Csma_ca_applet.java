package csmaProject;


import java.applet.Applet;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Csma_ca_applet extends Applet
{
    private Scenario scenario;
    private Button reset;
    private Button simAction;
    private Label simLabel;
    private GridBagLayout gridbag;
    
    private Scenario getScenarioParam() {
	String string = "Hidden";
	if (string.compareTo("Hidden") == 0)
	    return Scenario.HIDDEN3();
	return Scenario.STANDARD3();
    }
    
    public void init() {
	gridbag = new GridBagLayout();
	this.setBackground(Color.white);
	this.setFont(new Font("Helvetica", 0, 14));
	this.setLayout(gridbag);
	loadScenario();
    }
    
    private void loadScenario() {
	this.removeAll();
	scenario = getScenarioParam();
	reset = new Button("Reset");
	reset.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent actionevent) {
		Csma_ca_applet.this.loadScenario();
	    }
	});
	simAction = new Button(scenario.getSimActionName());
	simLabel = new Label(scenario.getSimState());
	simAction.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent actionevent) {
		scenario.simAction();
		simAction.setLabel(scenario.getSimActionName());
		simLabel.setText(scenario.getSimState());
	    }
	});
	makeComponent(reset, 1, 1, 0.0, 0.0);
	makeComponent(simAction, 1, 1, 0.0, 0.0);
	makeComponent(simLabel, 0, 1, 0.0, 0.0);
	makeComponentBG(new Canvas(), Color.white, 0, 1, 0.0, 0.1);
	makeComponentBG(new Canvas(), Color.black, 0, 1, 0.0, 0.1);
	makeLabel(new Label("Emit Frame", 1), 1, 1, 0.0, 0.0);
	makeLabel(new Label(" ", 1), 3, 1, 3.0, 0.0);
	makeLabel(new Label("Queue", 1), 1, 1, 0.0, 0.0);
	makeLabel(new Label("BackOff", 1), 0, 1, 0.0, 0.0);
	makeComponentBG(new Canvas(), Color.darkGray, 0, 1, 0.0, 0.1);
	for (int i = 0; i < scenario.getMobileNumber(); i++) {
	    Button button = new Button(scenario.getMobileName(i));
	    button.addActionListener(scenario.getActionListnerForMobile(i));
	    Label label = new Label("0", 1);
	    Label label_2_ = new Label("0", 1);
	    TimeLine timeline = new TimeLine();
	    scenario.setMobileDisplay(i, label, label_2_, timeline);
	    makeComponent(button, 1, 1, 0.0, 0.0);
	    makeComponent(timeline, 3, 1, 3.0, 1.0);
	    makeLabel(label, 1, 1, 0.0, 0.0);
	    makeLabel(label_2_, 0, 1, 0.0, 0.0);
	    makeComponentBG(new Canvas(), Color.darkGray, 0, 1, 0.0, 0.1);
	    scenario.addEmitButton(button);
	}
	TimeLine timeline = new TimeLine();
	scenario.setBSDisplay(timeline);
	makeLabel(new Label("Access Point", 1), 1, 1, 0.0, 0.0);
	makeComponent(timeline, 3, 1, 3.0, 1.0);
	makeLabel(new Label(""), 0, 1, 0.0, 0.0);
	makeComponentBG(new Canvas(), Color.darkGray, 0, 1, 0.0, 0.1);
	this.validate();
	scenario.enableEmitButtons(false);
    }
    
    private void makeComponent(Component component, int i, int i_3_, double d,
			       double d_4_) {
	GridBagConstraints gridbagconstraints = new GridBagConstraints();
	gridbagconstraints.fill = 1;
	gridbagconstraints.gridwidth = i;
	gridbagconstraints.gridheight = i_3_;
	gridbagconstraints.weightx = d;
	gridbagconstraints.weighty = d_4_;
	gridbag.setConstraints(component, gridbagconstraints);
	this.add(component);
    }
    
    private void makeComponentBG(Component component, Color color, int i,
				 int i_5_, double d, double d_6_) {
	component.setBackground(color);
	makeComponent(component, i, i_5_, d, d_6_);
    }
    
    private void makeLabel(Label label, int i, int i_7_, double d,
			   double d_8_) {
	makeComponentBG(label, Color.lightGray, i, i_7_, d, d_8_);
    }
}
