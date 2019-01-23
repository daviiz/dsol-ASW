package asw.weapon;

import java.rmi.RemoteException;

import asw.main.Ball;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 *  鱼雷诱饵，简化处理 施放后不动
 * @author daiwenzhi
 *
 */
public class Decoy extends Ball implements EventListenerInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1123053440800621212L;
	
	/** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;
	
	public static final EventType DECOY_LOCATION_UPDATE_EVENT = new EventType("DECOY_LOCATION_UPDATE_EVENT");

	public Decoy(final DEVSSimulatorInterface.TimeDouble simulator) {
		
		this.simulator = simulator;
		
	}
	@Override
	public void notify(EventInterface event) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DirectedPoint getLocation() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
