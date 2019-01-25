package asw.deprecated;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;

public class FleetSensor extends EventProducer implements EventListenerInterface{

	/** 探测到外部实体信息. */
    public static final EventType DETECT_ENTITY_MSG = new EventType("DETECT_ENTITY_MSG");
	/**
	 * 
	 */
	private static final long serialVersionUID = -8600709375499107756L;
	/** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;
	
	public FleetSensor(final DEVSSimulatorInterface.TimeDouble simulator) {

		this.simulator = simulator;
	}

	@Override
	public void notify(EventInterface event) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
