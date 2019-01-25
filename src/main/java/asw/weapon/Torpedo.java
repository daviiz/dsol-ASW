package asw.weapon;

import java.awt.Color;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import asw.main.Ball;
import asw.main.BallAnimation;
import asw.main.EntityMSG;
import asw.main.SimUtil;
import asw.platform.Fleet;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.jstats.distributions.DistNormal;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.language.d3.CartesianPoint;
import nl.tudelft.simulation.language.d3.DirectedPoint;

public class Torpedo extends Ball implements EventListenerInterface{

	private static final long serialVersionUID = -8295279255703776031L;
	
	public int belong = -1;
    
    public boolean status = true;
    
    public boolean isFired = false;
    
    public String target = "";
    
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

	public Torpedo(String name, double x,double y,final DEVSSimulatorInterface.TimeDouble simulator) {
		super(name);
		this.name = name;
        origin = new CartesianPoint(x, y, 0);
        destination = new CartesianPoint(x, y, 0);
		this.simulator = simulator;
		
	}
	
	@Override
	public void notify(EventInterface event) throws RemoteException {
		if(isFired) {
			if(event.getType().equals(Fleet.FLEET_LOCATION_UPDATE_EVENT)){
				EntityMSG tmp = (EntityMSG) event.getContent();
				if(target.equals(tmp.name)) {
					this.origin = this.destination;
			        //this.destination = new CartesianPoint(-100 + stream.nextInt(0, 200), -100 + stream.nextInt(0, 200), 0);
			        this.destination = SimUtil.nextPoint(this.origin.x, this.origin.y, tmp.x, tmp.y, 4.0,true);
			        this.startTime = this.simulator.getSimulatorTime();
			        this.stopTime = this.startTime + Math.abs(new DistNormal(stream, 9, 1.8).draw());
				}
			}
			
		}
	}
	//鱼雷被发射：鱼雷的速度为4；
	public synchronized void fire(final EntityMSG object) throws RemoteException, NamingException {
		isFired = true;
		target = object.name;
		new BallAnimation(this, this.simulator, Color.YELLOW);
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
