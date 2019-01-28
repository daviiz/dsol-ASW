package asw.weapon;

import java.awt.Color;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import asw.main.Ball;
import asw.main.BallAnimation;
import asw.main.EntityMSG;
import asw.main.LineData;
import asw.main.SimUtil;
import asw.platform.Fleet;
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

public class Torpedo extends Ball implements EventListenerInterface{

	private static final long serialVersionUID = -8295279255703776031L;
	
	public static final EventType TORPEDO_LOCATION_MSG = new EventType("TORPEDO_LOCATION_MSG");
	
	public int belong = -1;
    
    public boolean status = true;
    
    public boolean isFired = false;
    
    public String name = "";
    
    /** the origin. */
    private CartesianPoint origin = new CartesianPoint(-200, -100, 0);

    /** the destination. */
    private CartesianPoint destination = new CartesianPoint(-200, -100, 0);
    
    /** the start time. */
    private double startTime = Double.NaN;

    /** the stop time. */
    private double stopTime = Double.NaN;
    
    /** the stream -- ugly but works. */
    private static StreamInterface stream = new MersenneTwister();
    
	/** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;
    
    private  EntityMSG lastTarget = null;
    
    private double lastDistance = 400;
    
    private volatile LineData ld = new LineData(0,0,0,0);

	public Torpedo(String name, double x,double y,final DEVSSimulatorInterface.TimeDouble simulator) {
		super(name);
		this.name = name;
        origin = new CartesianPoint(x, y, 0);
        destination = new CartesianPoint(x, y, 0);
		this.simulator = simulator;
		
	}
	
	@Override
	public synchronized void notify(EventInterface event) throws RemoteException {
		
		if(isFired) {
			EntityMSG tmp = (EntityMSG) event.getContent();
			
			if(event.getType().equals(Fleet.FLEET_LOCATION_UPDATE_EVENT) || event.getType().equals(Decoy.DECOY_LOCATION_MSG)){
				double tmpL = SimUtil.calcLength(this.origin.x, this.origin.y, tmp.x, tmp.y);
				if(tmpL<lastDistance && tmpL < 150) {
					ld.x1 = (int)this.origin.x;
					ld.y1 = (int)this.origin.y;
					ld.x2 = (int)tmp.x; 
					ld.y2 = (int)tmp.y;
					lastTarget = tmp;
				}else {
					ld.x1 = 0;
					ld.y1 = 0;
					ld.x2 = 0; 
					ld.y2 = 0;
				}
			}
			
		}
	}
	//鱼雷被发射：鱼雷的速度为4；
	public synchronized void fire(final EntityMSG object) throws RemoteException, NamingException, SimRuntimeException {
		isFired = true;
		lastTarget = object;
		new BallAnimation(this, this.simulator, Color.BLUE,150,ld);
		next();
	}
	
	/**
     * next movement.
     * @throws RemoteException on network failure
     * @throws SimRuntimeException on simulation failure
     */
    private synchronized void next() throws RemoteException, SimRuntimeException
    {
        this.origin = this.destination;
        //this.destination = new CartesianPoint(-100 + stream.nextInt(0, 200), -100 + stream.nextInt(0, 200), 0);
        //this.destination = new CartesianPoint(this.destination.x+4, this.destination.y+4, 0);
    	if(lastTarget == null) {
        	this.destination = new CartesianPoint(this.destination.x+4, this.destination.y+4, 0);
        }else {
        	this.destination = SimUtil.nextPoint(this.origin.x, this.origin.y, lastTarget.x, lastTarget.y, 4.0,true);
        }
        this.startTime = this.simulator.getSimulatorTime();
        this.stopTime = this.startTime + Math.abs(new DistNormal(stream, 9, 1.8).draw());
        this.simulator.scheduleEventAbs(this.stopTime, this, this, "next", null);
        
        super.fireTimedEvent(TORPEDO_LOCATION_MSG, new EntityMSG(name,belong,status,this.origin.x,this.origin.y),this.simulator.getSimTime().plus(2.0));
        
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
