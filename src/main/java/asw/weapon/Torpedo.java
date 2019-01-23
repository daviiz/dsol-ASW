package asw.weapon;

import java.rmi.RemoteException;

import asw.main.Ball;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;

public class Torpedo extends Ball implements EventListenerInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8295279255703776031L;
	
	/** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;

	public Torpedo(final DEVSSimulatorInterface.TimeDouble simulator) {

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
