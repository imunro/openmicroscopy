/*
 * org.openmicroscopy.shoola.agents.datamng.editors.controls.CreateBar
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

package org.openmicroscopy.shoola.agents.datamng.editors.controls;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.openmicroscopy.shoola.agents.datamng.DataManagerUIF;
import org.openmicroscopy.shoola.util.ui.UIUtilities;


//Java imports

//Third-party libraries

//Application-internal dependencies

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
public class CreateBar
extends JToolBar
{

    private JButton     save, cancel;
    
    public CreateBar()
    {
        initButtons();
        buildGUI();
        setFloatable(false);
    }
    
    public JButton getSave() { return save; }
    
    public JButton getCancel() { return cancel; }
    
    /** Initializes the buttons. */
    private void initButtons()
    {
        save = new JButton("OK");
        save.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
        save.setToolTipText(
            UIUtilities.formatToolTipText("Save data."));
        save.setEnabled(false);
        cancel = new JButton("Cancel");
        cancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancel.setToolTipText(
            UIUtilities.formatToolTipText("Close without saving."));
    }
    
    /** Build and lay out the GUI. */
    private void buildGUI()
    {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gridbag);
        JPanel controls = buildButtonPanel();
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        Component cp = Box.createRigidArea(DataManagerUIF.VBOX);
        gridbag.setConstraints(cp, c);
        add(cp);
        c.gridy = 1;
        gridbag.setConstraints(controls, c); 
        add(controls);
        c.gridy = 2;
        cp = Box.createRigidArea(DataManagerUIF.VBOX);
        gridbag.setConstraints(cp, c);
        add(cp);
        setOpaque(false); //make panel transparent
    }
    
    /** Build panel with buttons. */
    private JPanel buildButtonPanel()
    {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(save);
        p.add(Box.createRigidArea(DataManagerUIF.HBOX));
        p.add(cancel);
        p.add(Box.createRigidArea(DataManagerUIF.HBOX));
        p.setOpaque(false); //make panel transparent
        return p;
    }

}
