package asw.platform;

import java.rmi.RemoteException;

import asw.main.Ball;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;

public class SubmarineSensor extends Ball implements EventListenerInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4135176818498338837L;
	
    /** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;

	public SubmarineSensor(final DEVSSimulatorInterface.TimeDouble simulator) {
		this.simulator = simulator;
		// subscribe to the events 
	}

	@Override
	public void notify(final EventInterface event) throws RemoteException {
		if (event.getType().equals(Fleet.FLEET_LOCATION_UPDATE_EVENT))
        {
            System.out.println("sadffffffffffff");
			//fireTimedEvent(Fleet.FLEET_LOCATION_UPDATE_EVENT, (LOC)event.getContent(), this.simulator.getSimulatorTime());
        }
	}

	@Override
	public DirectedPoint getLocation() throws RemoteException {
		return null;
	}

}
