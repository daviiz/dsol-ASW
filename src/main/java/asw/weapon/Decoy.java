package asw.weapon;

import java.rmi.RemoteException;

import asw.main.Ball;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
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

	public Decoy(final DEVSSimulatorInterface.TimeDouble simulator) {
		// TODO Auto-generated constructor stub
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
