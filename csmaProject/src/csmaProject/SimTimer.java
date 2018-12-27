package csmaProject;

public class SimTimer implements Runnable
{
    private int time;
    private Scenario scenario;
    private Thread timerThread;
    private int state;
    static final int ready = 0;
    static final int running = 1;
    static final int paused = 2;
    
    public SimTimer(Scenario scenario) {
	this.scenario = scenario;
	timerThread = new Thread(this);
	state = 0;
    }
    
    public String getActionName() {
	String string = "";
	switch (state) {
	case 0:
	    string = "  Start  ";
	    break;
	case 1:
	    string = "Pause";
	    break;
	case 2:
	    string = "Resume";
	    break;
	}
	return string;
    }
    
    public String getState() {
	String string = "";
	switch (state) {
	case 0:
	    string = "Simulation is ready";
	    break;
	case 1:
	    string = "Simulation is running";
	    break;
	case 2:
	    string = "Simulation is paused";
	    break;
	}
	return string;
    }
    
    public int getTime() {
	return time;
    }
    
    public void run() {
	for (;;) {
	    time++;
	    scenario.update();
	    try {
		Thread.sleep(100L);
	    } catch (Exception exception) {
		/* empty */
	    }
	}
    }
    
    public void simAction() {
	switch (state) {
	case 0:
	    timerThread.start();
	    state = 1;
	    scenario.enableEmitButtons(true);
	    break;
	case 1:
	    timerThread.suspend();
	    state = 2;
	    scenario.enableEmitButtons(false);
	    break;
	case 2:
	    timerThread.resume();
	    state = 1;
	    scenario.enableEmitButtons(true);
	    break;
	}
    }
}
