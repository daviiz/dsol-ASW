package asw.deprecated;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;
/**
 * 
 * @author daiwenzhi
 *
 */
public class FleetController extends EventProducer implements EventListenerInterface{

	private static final long serialVersionUID = -7088149054466803591L;
	
	public static final EventType DECISION_MSG = new EventType("DECISION_MSG");
	
	/** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;
	
	public FleetController(final DEVSSimulatorInterface.TimeDouble simulator) {

		this.simulator = simulator;
	}

	@Override
	public void notify(EventInterface event) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
