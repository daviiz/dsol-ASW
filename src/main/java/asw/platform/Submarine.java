package asw.platform;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.naming.NamingException;

import asw.main.Ball;
import asw.main.BallAnimation;
import asw.main.EntityMSG;
import asw.main.LineData;
import asw.main.SimUtil;
import asw.weapon.Torpedo;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.language.d3.CartesianPoint;
import nl.tudelft.simulation.language.d3.DirectedPoint;
/**
 * 
 * @author daiwenzhi
 *
 */
public class Submarine extends Ball implements EventListenerInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5001962561864197742L;

	/** TOTAL_ORDERING_COST_EVENT is fired whenever ordering occurs. */
    public static final EventType SUBMARINE_LOCATION_UPDATE_EVENT = new EventType("SUBMARINE_LOCATION_UPDATE_EVENT");

    private String name;

	/** the origin. */
    private CartesianPoint origin = new CartesianPoint(0,0,0);

    /** the destination. */
    private CartesianPoint destination = new CartesianPoint(0,0,0);

    /** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;

    /** the start time. */
    private double startTime = Double.NaN;

    /** the stop time. */
    private double stopTime = Double.NaN;

    /** the stream -- ugly but works. */
    private static StreamInterface stream = new MersenneTwister();
    
    public int belong = -1;
    
    public boolean status = true;
	
	public Torpedo _t1 = null;
	public Torpedo _t2 = null;
	
	private int weaponCounts = 0;
	
	private HashMap<String, String> LockedTarget = new HashMap<String, String>();
	
	private volatile LineData ld = new LineData(0,0,0,0);
	
	/**
	 * 雷达探测范围
	 */
	private double detectRange = 400;
	
	public Submarine(String name, double x,double y,final DEVSSimulatorInterface.TimeDouble simulator) throws RemoteException, SimRuntimeException
    {
		super(name);
		this.name = name;
        origin = new CartesianPoint(x, y, 0);
        destination = new CartesianPoint(x, y, 0);
        this.simulator = simulator;
        // URL image = URLResource.getResource("/nl/tudelft/simulation/examples/dsol/animation/images/customer.jpg");
        // new SingleImageRenderable(this, simulator, image);
        _t1 = new Torpedo(name+"_torpedo1",x,y,simulator);
        _t2 = new Torpedo(name+"_torpedo2",x,y,simulator);
        weaponCounts = 2;
        
        try
        {
            new BallAnimation(this, simulator,Color.BLUE,(int)detectRange,ld);
            //_maneuver = new SubmarineManeuver(simulator);
            
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
        //System.out.println("--------------"+Math.abs(new DistNormal(stream, 9, 1.8).draw()));
        //this.stopTime = this.startTime + Math.abs(new DistNormal(stream, 9, 1.8).draw());
        this.stopTime = this.startTime + 10;
        this.simulator.scheduleEventAbs(this.stopTime, this, this, "next", null);
    }

	@Override
	public DirectedPoint getLocation() throws RemoteException {
		double fraction = (this.simulator.getSimulatorTime() - this.startTime) / (this.stopTime - this.startTime);
        double x = this.origin.x + (this.destination.x - this.origin.x) * fraction;
        double y = this.origin.y + (this.destination.y - this.origin.y) * fraction;
        return new DirectedPoint(x, y, 0, 0.0, 0.0, 0);
	}
	@Override
	public synchronized void notify(EventInterface event) throws RemoteException {
		if (event.getType().equals(Fleet.FLEET_LOCATION_UPDATE_EVENT))
        {
			EntityMSG tmp = (EntityMSG) event.getContent();
			System.out.println(name+" received msg: "+tmp.name+" current location:x="+tmp.x+", y="+tmp.y);
			
			double dis = SimUtil.calcLength(this.origin.x, this.origin.y, tmp.x, tmp.y);
			if(dis < detectRange) {
				//设置通信线数据
				ld.x1 = (int)this.origin.x;
				ld.y1 = (int)this.origin.y;
				ld.x2 = (int)tmp.x; 
				ld.y2 = (int)tmp.y;
				//施放鱼雷，对同一目标仅施放一个鱼雷
				if(!LockedTarget.containsKey(tmp.name)) {
					if(weaponCounts == 2) {
						try {
							_t1.setLocation(this.origin);
							this.simulator.scheduleEventRel(2.0, this, _t1, "fire", new Object[] {tmp});
							weaponCounts--;
							LockedTarget.put(tmp.name, tmp.name);
						} catch (SimRuntimeException e) {
							e.printStackTrace();
						}
					}else if (weaponCounts == 1){
						try {
							_t2.setLocation(this.origin);
							this.simulator.scheduleEventRel(2.0, this, _t2, "fire", new Object[] {tmp});
							LockedTarget.put(tmp.name, tmp.name);
							weaponCounts--;
						} catch (SimRuntimeException e) {
							e.printStackTrace();
						}
					}else {
						//逃逸
					}
				}
			}
			else {
				ld.x1 = 0;
				ld.y1 = 0;
				ld.x2 = 0; 
				ld.y2 = 0;
			}
			//fireTimedEvent(Fleet.FLEET_LOCATION_UPDATE_EVENT, (LOC)event.getContent(), this.simulator.getSimulatorTime());
        }
		
	}

}
