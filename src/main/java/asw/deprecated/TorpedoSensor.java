package asw.deprecated;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

public class TorpedoSensor implements EventListenerInterface{
	
	private DEVSSimulatorInterface.TimeDouble simulator = null;
	
	public TorpedoSensor(final DEVSSimulatorInterface.TimeDouble simulator) {
		this.simulator = simulator;
	}
	
	@Override
	public void notify(EventInterface event) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
