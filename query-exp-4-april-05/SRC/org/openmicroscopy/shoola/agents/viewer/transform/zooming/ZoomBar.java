/*
 * org.openmicroscopy.shoola.agents.viewer.transform.zooming.ZoomBar
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

package org.openmicroscopy.shoola.agents.viewer.transform.zooming;

//Java imports
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.viewer.IconManager;
import org.openmicroscopy.shoola.agents.viewer.ViewerUIF;
import org.openmicroscopy.shoola.agents.viewer.transform.ImageInspectorManager;
import org.openmicroscopy.shoola.env.config.Registry;
import org.openmicroscopy.shoola.util.ui.UIUtilities;

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
public class ZoomBar
    extends JPanel
{
    
    private static final String     MAX_LETTER = "300%";
    
    /** width of a letter according to the Font. */
    private int                     txtWidth;
    
    /** zoom buttons. */                                                                                                                                                
    private JButton                 zoomIn, zoomOut, zoomFit;
    
    /** Field displaying the zooming factor. */
    private JTextField              zoomField;
    
    private ZoomBarManager          manager;
    
    private Registry                registry;

    public ZoomBar(Registry registry, 
                    ImageInspectorManager mng, double magFactor)
    {
        this.registry = registry;
        initTxtWidth();
        initZoomComponents(magFactor);
        manager = new ZoomBarManager(this, mng, magFactor);
        manager.attachListeners();
        buildGUI();
    }
    
    Registry getRegistry() { return registry; }
    
    JButton getZoomIn() { return zoomIn; }
    
    JButton getZoomOut() { return zoomOut; }
    
    JButton getZoomFit() { return zoomFit; }
    
    JTextField getZoomField() { return zoomField; }
    
    public ZoomBarManager getManager() { return manager; }
    
    /** Initialize the zoom components. */
    private void initZoomComponents(double magFactor)
    {
        //buttons
        String s = ""+(int)(magFactor*100)+"%";
        IconManager im = IconManager.getInstance(registry);
        Icon zoomInIcon = im.getIcon(IconManager.ZOOMIN);
        zoomIn = new JButton(zoomInIcon);
        zoomIn.setToolTipText(
            UIUtilities.formatToolTipText("Zoom in.")); 
        zoomOut = new JButton(im.getIcon(IconManager.ZOOMOUT));
        zoomOut.setToolTipText(
            UIUtilities.formatToolTipText("Zoom out."));
        zoomFit = new JButton(im.getIcon(IconManager.ZOOMFIT));
        zoomFit.setToolTipText(
            UIUtilities.formatToolTipText("Reset."));
        zoomField = new JTextField(s, MAX_LETTER.length());
        zoomField.setForeground(ViewerUIF.STEELBLUE);
        zoomField.setToolTipText(
            UIUtilities.formatToolTipText("Zooming percentage."));  
    }   

    /** Build and lay out the GUI. */
    private void buildGUI()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(UIUtilities.buildComponentPanel(UIUtilities.setTextFont("Zoom")));
        add(buildToolBar());
    }
    
    /** Build the toolBar. */
    private JToolBar buildToolBar() 
    {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        bar.add(zoomOut);
        bar.add(zoomIn);
        bar.add(zoomFit);
        bar.add(buildTextPanel());
        return bar;
    }

    /** Panel containing textField. */
    private JPanel buildTextPanel()
    {
        JPanel p = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        p.setLayout(gridbag);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        c.ipadx = txtWidth/2;
        gridbag.setConstraints(zoomField, c);
        p.add(zoomField);
        p.setAlignmentX(LEFT_ALIGNMENT);
        return p;
    }
    
    /** Initializes the width of the text. */
    private void initTxtWidth()
    {
        FontMetrics metrics = getFontMetrics(getFont());
        txtWidth = MAX_LETTER.length()*metrics.charWidth('m');
    }

}
