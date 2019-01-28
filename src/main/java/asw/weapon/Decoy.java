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
 *  鱼雷诱饵模型
 * @author daiwenzhi
 *
 */
public class Decoy extends Ball implements EventListenerInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1020956635649196808L;

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
	
	/** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;
    
    private volatile  boolean isDead = false;
    
    private BallAnimation visualComponent = null;
    
    /**
	 * 雷达探测范围
	 */
	private double detectRange = 100;
	
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
	        	//System.out.println(name+" received msg: "+tmp.name+" current location:x="+tmp.x+", y="+tmp.y);
	        	double dis = SimUtil.calcLength(this.origin.x, this.origin.y, tmp.x, tmp.y);
	        	if(dis<this.detectRange) {
	        		lastThreat = tmp;
	        		if (dis < SimUtil.hit_distance) {
						visualComponent.setColor(Color.BLACK);
						isDead = true;
						status = false;
					}
	        	}
	        	
	        }
		}
		
	}
	/**
	 * 鱼雷诱饵施放
	 * @param object
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws SimRuntimeException
	 */
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
    		visualComponent = new BallAnimation(this, this.simulator, Color.GREEN,(int)detectRange,null);
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
        	this.destination = SimUtil.nextPoint(this.origin.x, this.origin.y, lastThreat.x, lastThreat.y, 2.0,false);
        }
        this.startTime = this.simulator.getSimulatorTime();
        this.stopTime = this.startTime + Math.abs(new DistNormal(stream, 9, 1.8).draw());
        this.simulator.scheduleEventAbs(this.stopTime, this, this, "next", null);
       
        super.fireTimedEvent(DECOY_LOCATION_MSG, new EntityMSG(name,belong,status,this.origin.x,this.origin.y),this.simulator.getSimTime().plus(2.0));
        
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
