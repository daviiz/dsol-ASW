package asw.platform;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

public class SubmarineController implements EventListenerInterface{

	public SubmarineController(final DEVSSimulatorInterface.TimeDouble simulator) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void notify(EventInterface event) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
