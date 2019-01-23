package asw.platform;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import asw.main.Ball;
import asw.main.BallAnimation;
import asw.weapon.Decoy;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.jstats.distributions.DistNormal;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.language.d3.CartesianPoint;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 *   
 * @author daiwenzhi
 */
public class Fleet extends Ball implements EventListenerInterface{
	
	private static final long serialVersionUID = 5337683693470946049L;
	
	/** TOTAL_ORDERING_COST_EVENT is fired whenever ordering occurs. */
    public static final EventType FLEET_LOCATION_UPDATE_EVENT = new EventType("FLEET_LOCATION_UPDATE_EVENT");

	/** the origin. */
    private CartesianPoint origin = new CartesianPoint(-200, -100, 0);

    /** the destination. */
    private CartesianPoint destination = new CartesianPoint(-200, -100, 0);

    /** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;

    /** the start time. */
    private double startTime = Double.NaN;

    /** the stop time. */
    private double stopTime = Double.NaN;

    /** the stream -- ugly but works. */
    private static StreamInterface stream = new MersenneTwister();
	
	public  FleetController _controller;
	
	//private FleetManeuver _maneuver;
	
	public  FleetSensor _sensor;
	
	public Decoy _decoy1;
	
	public Decoy _decoy2;
	
	public Fleet(final DEVSSimulatorInterface.TimeDouble simulator) throws RemoteException, SimRuntimeException
    {
        super("F");
        this.simulator = simulator;
        // URL image = URLResource.getResource("/nl/tudelft/simulation/examples/dsol/animation/images/customer.jpg");
        // new SingleImageRenderable(this, simulator, image);
        
        try
        {
            new BallAnimation(this, simulator);
            
            //_maneuver = new FleetManeuver(simulator);
            _controller = new FleetController(simulator);
            _sensor = new FleetSensor(simulator);
            _decoy1 = new Decoy(simulator);
            _decoy2 = new Decoy(simulator);
        }
        catch (NamingException exception)
        {
            SimLogger.always().error(exception);
        }
        this.next();
    }
	/**
     * next movement.
     * @throws RemoteException on network failure
     * @throws SimRuntimeException on simulation failure
     */
    private void next() throws RemoteException, SimRuntimeException
    {
        this.origin = this.destination;
        //this.destination = new CartesianPoint(-100 + stream.nextInt(0, 200), -100 + stream.nextInt(0, 200), 0);
        this.destination = new CartesianPoint(this.destination.x+1, this.destination.y+1, 0);
        this.startTime = this.simulator.getSimulatorTime();
        this.stopTime = this.startTime + Math.abs(new DistNormal(stream, 9, 1.8).draw());
        this.simulator.scheduleEventAbs(this.stopTime, this, this, "next", null);
        
        super.fireEvent(FLEET_LOCATION_UPDATE_EVENT, new LOC(this.origin.x,this.origin.y));
        
        
    }

	@Override
	public DirectedPoint getLocation() throws RemoteException {
		double fraction = (this.simulator.getSimulatorTime() - this.startTime) / (this.stopTime - this.startTime);
        double x = this.origin.x + (this.destination.x - this.origin.x) * fraction;
        double y = this.origin.y + (this.destination.y - this.origin.y) * fraction;
        return new DirectedPoint(x, y, 0, 0.0, 0.0, this.theta);
	}
	@Override
	public void notify(final EventInterface event) throws RemoteException {
		
		if (event.getType().equals(FLEET_LOCATION_UPDATE_EVENT))
        {
			LOC tmp = (LOC) event.getContent();
			System.out.println("Fleet current location:x="+tmp.x+", y="+tmp.y);
			//fireTimedEvent(Fleet.FLEET_LOCATION_UPDATE_EVENT, (LOC)event.getContent(), this.simulator.getSimulatorTime());
        }
	}
	
	
	

}
