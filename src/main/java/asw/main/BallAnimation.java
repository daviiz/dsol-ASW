package asw.main;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.animation.D2.Renderable2D;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * The Animation of a Ball.
 * <p>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 * @since 1.4
 */
public class BallAnimation extends Renderable2D<Locatable>
{
    /**
     * the color of the ballAnimation.
     */
    private Color color = Color.ORANGE;
    
    private int detectRange ;
    
    private LineData target = null;
    

    /**
     * constructs a new BallAnimation.
     * @param source Locatable; the source
     * @param simulator SimulatorInterface.TimeDouble; the simulator
     * @throws NamingException on registration error
     * @throws RemoteException on remote animation error
     */
    public BallAnimation(final Locatable source, final SimulatorInterface.TimeDouble simulator,Color _color,int _detectRange,LineData _target)
            throws RemoteException, NamingException
    {
        super(source, simulator);
        this.color = _color;
        this.detectRange = _detectRange;
        this.target = _target;
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Graphics2D graphics, final ImageObserver observer)
    {
        graphics.setColor(this.color);
        graphics.fillOval(-(int) Ball.RADIUS, -(int) Ball.RADIUS, (int) (Ball.RADIUS * 2.0), (int) (Ball.RADIUS * 2.0));
        Font f = new Font("Consolas",Font.BOLD ,6);
        graphics.setFont(f);
        graphics.setColor(Color.GRAY);
        graphics.drawString(getSource().toString(), (float) (Ball.RADIUS * -1.0), (float) (Ball.RADIUS * 1.0));
        if(detectRange>0) {
        	graphics.setColor(this.color);
            graphics.drawOval(-detectRange, -detectRange, detectRange*2, detectRange*2);
        }
        
        if(this.target != null) {
    		graphics.setColor(this.color);
    		int x=0,y=0;
    			x = target.x1>target.x2 ? (-1)*(Math.abs(target.x1-target.x2)):(Math.abs(target.x1-target.x2));
    			y = target.y1>target.y2 ? (Math.abs(target.y1-target.y2)):(-1)*(Math.abs(target.y1-target.y2));;
            graphics.drawLine(0,0,x,y);
        }
        
    }
    
    /**
     * @return Returns the color.
     */
    public Color getColor()
    {
        return this.color;
    }

    /**
     * @param color Color; The color to set.
     */
    public void setColor(final Color color)
    {
        this.color = color;
    }
}
