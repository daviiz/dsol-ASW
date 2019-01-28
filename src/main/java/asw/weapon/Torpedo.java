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

/**
 * 
 * @author daiwenzhi
 *
 */
public class Torpedo extends Ball implements EventListenerInterface {

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

	private EntityMSG lastTarget = null;

	private double lastDistance = 250;

	private volatile LineData ld = new LineData(0, 0, 0, 0);
	
	private int next_x = -2 + stream.nextInt(0, 5);
	private int next_y = -3 + stream.nextInt(0, 5);

	/**
	 * 雷达探测范围
	 */
	private double detectRange = 250;

	public Torpedo(String name, double x, double y, final DEVSSimulatorInterface.TimeDouble simulator) {
		super(name);
		this.name = name;
		this.detectRange = 100;
		origin = new CartesianPoint(x, y, 0);
		destination = new CartesianPoint(x, y, 0);
		this.simulator = simulator;

	}

	@Override
	public synchronized void notify(EventInterface event) throws RemoteException {

		if (isFired) {

			if (event.getType().equals(Fleet.FLEET_LOCATION_UPDATE_EVENT)
					|| event.getType().equals(Decoy.DECOY_LOCATION_MSG)) {
				EntityMSG tmp = (EntityMSG) event.getContent();
				double tmpL = SimUtil.calcLength(this.origin.x, this.origin.y, tmp.x, tmp.y);

				if (tmpL < detectRange) {
					//在探测范围内，并且是生存状态的实体才显示通信线
					if(tmp.status == true) {
						ld.x1 = (int) this.origin.x;
						ld.y1 = (int) this.origin.y;
						ld.x2 = (int) tmp.x;
						ld.y2 = (int) tmp.y;
					}
					//在探测范围内，找到更近的，设置其为目标
					if (tmpL < lastDistance) {
						lastTarget = new EntityMSG(tmp);
						lastDistance = tmpL;
					}
					//如果自己的目标已经死亡，在探测范围内寻找目标，找到就重新设置目标
					if(this.lastTarget.status == false) {
						lastDistance = tmpL;
						lastTarget = new EntityMSG(tmp);
					}
				} else {
					ld.x1 = 0;
					ld.y1 = 0;
					ld.x2 = 0;
					ld.y2 = 0;
				}
			}
		}
	}

	/**
	 * 鱼雷施放
	 * 
	 * @param object
	 * @throws RemoteException
	 * @throws NamingException
	 * @throws SimRuntimeException
	 */
	public synchronized void fire(final EntityMSG object) throws RemoteException, NamingException, SimRuntimeException {
		isFired = true;
		lastTarget = object;
		new BallAnimation(this, this.simulator, Color.BLUE, (int) detectRange, ld);
		next();
	}

	/**
	 * next movement.
	 * 
	 * @throws RemoteException     on network failure
	 * @throws SimRuntimeException on simulation failure
	 */
	private synchronized void next() throws RemoteException, SimRuntimeException {
		this.origin = this.destination;
		// this.destination = new CartesianPoint(-100 + stream.nextInt(0, 200), -100 +
		// stream.nextInt(0, 200), 0);
		// this.destination = new CartesianPoint(this.destination.x+4,
		// this.destination.y+4, 0);
		
		if (lastTarget == null || lastTarget.status == false) {
			this.destination = new CartesianPoint(this.destination.x + next_x, this.destination.y + next_y, 0);
		} else {
			this.destination = SimUtil.nextPoint(this.origin.x, this.origin.y, lastTarget.x, lastTarget.y, 4.0, true);
		}
		this.startTime = this.simulator.getSimulatorTime();
		this.stopTime = this.startTime + Math.abs(new DistNormal(stream, 9, 1.8).draw());
		this.simulator.scheduleEventAbs(this.stopTime, this, this, "next", null);

		super.fireTimedEvent(TORPEDO_LOCATION_MSG, new EntityMSG(name, belong, status, this.origin.x, this.origin.y),
				this.simulator.getSimTime().plus(2.0));

	}

	@Override
	public DirectedPoint getLocation() throws RemoteException {
		double fraction = (this.simulator.getSimulatorTime() - this.startTime) / (this.stopTime - this.startTime);
		double x = this.origin.x + (this.destination.x - this.origin.x) * fraction;
		double y = this.origin.y + (this.destination.y - this.origin.y) * fraction;
		return new DirectedPoint(x, y, 0, 0.0, 0.0, this.theta);
	}

	public void setLocation(CartesianPoint _origin) {
		this.origin = _origin;
		this.destination = _origin;
	}

}
