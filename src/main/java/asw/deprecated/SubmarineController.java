package asw.deprecated;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

public class SubmarineController implements EventListenerInterface{

	/** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;
	
	public SubmarineController(final DEVSSimulatorInterface.TimeDouble simulator) {
		
		this.simulator = simulator;
		
	}

	@Override
	public void notify(EventInterface event) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
