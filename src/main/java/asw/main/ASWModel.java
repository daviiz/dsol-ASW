package asw.main;


import java.rmi.RemoteException;

import asw.platform.Fleet;
import asw.platform.Submarine;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 */
public class ASWModel extends AbstractDSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;
    
    private  Fleet f1;
    private  Fleet f2;
    private  Submarine s1;
   
    /**
     * constructs a new BallModel.
     * @param simulator the simulator
     */
    public ASWModel(final DEVSSimulatorInterface.TimeDouble simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel() throws SimRuntimeException
    {
        try
        {
            f1 = new Fleet("Fleet1",-200,-50,this.simulator);
            
            f2 = new Fleet("Fleet2",-250,0,this.simulator);
            
            s1 = new Submarine("Sub1",200,100,this.simulator);
            
            //���ͷ� .addListener ���շ�
            //ս��1ģ��ʵ����Ϣ���£�
            f1.addListener(f1._decoy1, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            f1.addListener(f1._decoy2, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            f1.addListener(f2, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            f1.addListener(f2._decoy1, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            f1.addListener(f2._decoy2, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            f1.addListener(s1, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            f1.addListener(s1._t1, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            f1.addListener(s1._t2, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            //ս��2ģ��ʵ����Ϣ���£�
            f2.addListener(f2._decoy1, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            f2.addListener(f2._decoy2, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            f2.addListener(f1, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            f2.addListener(f1._decoy1, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            f2.addListener(f1._decoy2, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            f2.addListener(s1, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            f2.addListener(s1._t1, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            f2.addListener(s1._t2, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            //Ǳͧ1ģ��ʵ����Ϣ���£�
            s1.addListener(f2, Submarine.SUBMARINE_LOCATION_UPDATE_EVENT);
            s1.addListener(f2._decoy1, Submarine.SUBMARINE_LOCATION_UPDATE_EVENT);
            s1.addListener(f2._decoy2, Submarine.SUBMARINE_LOCATION_UPDATE_EVENT);
            s1.addListener(f1, Submarine.SUBMARINE_LOCATION_UPDATE_EVENT);
            s1.addListener(f1._decoy1, Submarine.SUBMARINE_LOCATION_UPDATE_EVENT);
            s1.addListener(f1._decoy2, Submarine.SUBMARINE_LOCATION_UPDATE_EVENT);
            s1.addListener(s1._t1, Submarine.SUBMARINE_LOCATION_UPDATE_EVENT);
            s1.addListener(s1._t2, Submarine.SUBMARINE_LOCATION_UPDATE_EVENT);

            
            
            ///////////////////ƽ̨ʵ��ģ��֮����Ϣ����-����/////////////////////////////////////////////////
            //���ͷ� .addListener ���շ�
            //f1.addListener(f2._sensor, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            //f1.addListener(s1._sensor, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            //f1.addListener(s1._t1, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            //f1.addListener(s1._t2, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            
            //f2.addListener(f1._sensor, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            //f2.addListener(s1._sensor, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            //f2.addListener(s1._t1, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            //f2.addListener(s1._t2, Fleet.FLEET_LOCATION_UPDATE_EVENT);
            
            //s1.addListener(f1._sensor, Submarine.SUBMARINE_LOCATION_UPDATE_EVENT);
            //s1.addListener(f2._sensor, Submarine.SUBMARINE_LOCATION_UPDATE_EVENT);
            //s1.addListener(f1._decoy1, Submarine.SUBMARINE_LOCATION_UPDATE_EVENT);
            //s1.addListener(f1._decoy2, Submarine.SUBMARINE_LOCATION_UPDATE_EVENT);
            
        	///////////////////ƽ̨ģ���ڲ���Ϣ����-����/////////////////////////////////////////////////
            //f1._sensor.addListener(f1._controller, FleetSensor.DETECT_ENTITY_MSG);
            //f1._controller.addListener(f1, FleetController.DECISION_MSG);
            
            ///////////////////����ϵͳģ����Ϣ����-����/////////////////////////////////////////////////
            
            
        }
        catch (RemoteException exception)
        {
            SimLogger.always().error(exception);
        }
    }

}
