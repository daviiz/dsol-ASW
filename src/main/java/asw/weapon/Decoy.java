package asw.weapon;

import java.awt.Color;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import asw.main.Ball;
import asw.main.BallAnimation;
import asw.main.EntityMSG;
import asw.main.SimUtil;
import nl.tudelft.simulation.dsol.SimRuntimeException;
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
    
    private boolean isFired = false;
    
    private EntityMSG lastThreat = null;
    
    /** the stream -- ugly but works. */
    private static StreamInterface stream = new MersenneTwister();
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1123053440800621212L;
	
	/** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;
    
    private volatile  boolean isDead = false;
    
    private BallAnimation visualComponent = null;
	
	public Decoy(String name, double x,double y,final DEVSSimulatorInterface.TimeDouble simulator) {
		super(name);
		this.name = name;
        origin = new CartesianPoint(x, y, 0);
        destination = new CartesianPoint(x, y, 0);
		this.simulator = simulator;
		
	}
	@Override
	public void notify(EventInterface event) throws RemoteException {
		if(isFired && (!isDead)) {
			if(event.getType().equals(Torpedo.TORPEDO_LOCATION_MSG)) {
	        	EntityMSG tmp = (EntityMSG) event.getContent();
	        	System.out.println(name+" received msg: "+tmp.name+" current location:x="+tmp.x+", y="+tmp.y);
	        	double dis = SimUtil.calcLength(this.origin.x, this.origin.y, tmp.x, tmp.y);
	        	//战舰雷达探测范围：100
	        	if(dis<100) {
	        		lastThreat = tmp;
	        		if (dis < 20) {
						visualComponent.setColor(Color.BLACK);
						isDead = true;
					}
	        	}
	        	
	        }
		}
		
	}
	//鱼雷被发射：鱼雷的速度为4；
	public synchronized void fire(final EntityMSG object) throws RemoteException, NamingException, SimRuntimeException {
		isFired = true;
		//lastThreat = null;
		lastThreat = object;
		
		next();
	}
	/**
     * next movement.
     * @throws RemoteException on network failure
     * @throws SimRuntimeException on simulation failure
	 * @throws NamingException 
     */
    private synchronized void next() throws RemoteException, SimRuntimeException, NamingException
    {
    	if(visualComponent== null) {
    		visualComponent = new BallAnimation(this, this.simulator, Color.GREEN);
    	}
        
    	this.origin = this.destination;
        //this.destination = new CartesianPoint(-100 + stream.nextInt(0, 200), -100 + stream.nextInt(0, 200), 0);
        //this.destination = new CartesianPoint(this.destination.x+4, this.destination.y+4, 0);
    	if(isDead) {
    		this.destination = new CartesianPoint(this.destination.x, this.destination.y, 0);
    	}
    	else if(lastThreat == null) {
        	//this.destination = new CartesianPoint(this.destination.x, this.destination.y, 0);
        }else {
        	this.destination = SimUtil.nextPoint(this.origin.x, this.origin.y, lastThreat.x, lastThreat.y, 2.0,true);
        }
        this.startTime = this.simulator.getSimulatorTime();
        this.stopTime = this.startTime + Math.abs(new DistNormal(stream, 9, 1.8).draw());
        this.simulator.scheduleEventAbs(this.stopTime, this, this, "next", null);
        super.fireTimedEvent(DECOY_LOCATION_MSG, new EntityMSG(name,belong,status,this.origin.x,this.origin.y),this.simulator.getSimTime().plus(2.0));
        
    }
    private synchronized void destroy() {
    	System.out.println("");
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
