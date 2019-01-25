package asw.weapon;

import java.rmi.RemoteException;

import asw.main.Ball;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.language.d3.CartesianPoint;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 *  鱼雷诱饵，简化处理 施放后不动
 * @author daiwenzhi
 *
 */
public class Decoy extends Ball implements EventListenerInterface{

	public int belong = 1;
    
    public boolean status = true;
    
    private String name;
    
    public static final EventType DECOY_LOCATION_MSG = new EventType("DECOY_LOCATION_MSG");
    
    /** the origin. */
    private CartesianPoint origin = new CartesianPoint(-200, -100, 0);

    /** the destination. */
    private CartesianPoint destination = new CartesianPoint(-200, -100, 0);
    
    /** the start time. */
    private double startTime = Double.NaN;

    /** the stop time. */
    private double stopTime = Double.NaN;
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1123053440800621212L;
	
	/** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;
	
	public static final EventType DECOY_LOCATION_UPDATE_EVENT = new EventType("DECOY_LOCATION_UPDATE_EVENT");

	public Decoy(String name, double x,double y,final DEVSSimulatorInterface.TimeDouble simulator) {
		super(name);
		this.name = name;
        origin = new CartesianPoint(x, y, 0);
        destination = new CartesianPoint(x, y, 0);
		this.simulator = simulator;
		
	}
	@Override
	public void notify(EventInterface event) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DirectedPoint getLocation() throws RemoteException {
		double fraction = (this.simulator.getSimulatorTime() - this.startTime) / (this.stopTime - this.startTime);
        double x = this.origin.x + (this.destination.x - this.origin.x) * fraction;
        double y = this.origin.y + (this.destination.y - this.origin.y) * fraction;
        return new DirectedPoint(x, y, 0, 0.0, 0.0, this.theta);
	}
	public void setLocation(CartesianPoint _origin) {
		this.origin =_origin;
		this.destination = _origin;
	}

}
