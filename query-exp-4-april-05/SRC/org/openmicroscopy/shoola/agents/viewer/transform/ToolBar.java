/*
 * org.openmicroscopy.shoola.agents.viewer.transform.ToolBar
 *
 *------------------------------------------------------------------------------
 *
 *  Copyright (C) 2004 Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.agents.viewer.transform;


//Java imports
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.viewer.transform.lens.LensBar;
import org.openmicroscopy.shoola.agents.viewer.transform.zooming.ZoomBar;
import org.openmicroscopy.shoola.env.config.Registry;

/** 
 * 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author  <br>Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:a.falconi@dundee.ac.uk">
 * 					a.falconi@dundee.ac.uk</a>
 * @version 2.2 
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
public class ToolBar
    extends JPanel
{
    private static final Dimension  HBOX = new Dimension(10, 0);

    private ZoomBar                 zoomBar;
    
    private LensBar                 lensBar;
    
    public ToolBar(Registry registry, ImageInspectorManager manager, 
                    double magFactor, int w, int h)
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        putClientProperty("JToolBar.isRollover", new Boolean(true));
        zoomBar = new ZoomBar(registry, manager, magFactor);
        lensBar = new LensBar(registry, manager, w, h);
        add(zoomBar);
        add(Box.createRigidArea(HBOX));
        add(new JSeparator(JSeparator.HORIZONTAL));
        add(lensBar);
        add(new JSeparator(JSeparator.HORIZONTAL));
        add(Box.createRigidArea(HBOX));
        
    }
    
    ZoomBar getZoomBar() { return zoomBar; }
    
    LensBar getLensBar() { return lensBar; }

}

