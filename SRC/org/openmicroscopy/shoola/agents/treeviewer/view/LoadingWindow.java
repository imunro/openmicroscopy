/*
 * org.openmicroscopy.shoola.agents.treeviewer.view.LoadingWindow
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

package org.openmicroscopy.shoola.agents.treeviewer.view;



//Java imports
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.treeviewer.browser.Browser;
import org.openmicroscopy.shoola.util.ui.MultilineLabel;

/** 
 * 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
class LoadingWindow
    extends JDialog
{
    
    /** The message diplaying during the loading process */
    private static final String         LOADING_MSG = "Loading Data and " +
                                                      "Building the display.";
    
    /** 
     * The preferred size of the widget that displays the notification message.
     * Only the part of text that fits into this display area will be displayed.
     */
    private static final Dimension    MSG_AREA_SIZE = new Dimension(300, 50);
    
    /** 
     * The size of the invisible components used to separate widgets
     * horizontally.
     */
    private static final Dimension    H_SPACER_SIZE = new Dimension(20, 1);
    
    /** 
     * The size of the invisible components used to separate widgets
     * vertically.
     */
    private static final Dimension    V_SPACER_SIZE = new Dimension(1, 20);
    
    /** The panel hosting the message. */
    private JPanel          messagePanel;
    
    /** Hides and disposes of the dialog. */
    private JButton         cancelButton;
    
    /** The bar notifying the user for the data retrieval progress. */
    private JProgressBar    progressBar;
    
    /** Creates the various UI components that make up the dialog. */
    private void createComponents()
    {
        cancelButton = new JButton("Cancel");
        progressBar = new JProgressBar();
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
    }
    
    /**
     * Builds a {@link MultilineLabel label} hosting the message to display.
     * 
     * @param message The message to display.
     * @return See above.
     */
    private MultilineLabel buildLabel(String message)
    {
        MultilineLabel messageLabel = new MultilineLabel(message);
        messageLabel.setPreferredSize(MSG_AREA_SIZE);
        messageLabel.setAlignmentY(TOP_ALIGNMENT);
        return messageLabel;
    }
    
    /**
     * Binds the {@link #close() close} action to the exit event generated
     * either by the close icon or by the {@link #cancelButton}.
     */
    private void attachListeners()
    {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) { close(); }
        });
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { close(); }
        });
    }
    
    /**
     * Hides the dialog and fires an event to cancel any ongoing 
     * data retrieval.
     */
    private void close()
    {
        setVisible(false);
        firePropertyChange(Browser.CANCEL_PROPERTY, Boolean.FALSE, 
                            Boolean.TRUE);
    }
    
    /**
     * Builds and lays out the panel hosting the buttons.
     * 
     * @return See above.
     */
    private JPanel buildButtonsPanel()
    {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(Box.createRigidArea(H_SPACER_SIZE));
        return buttonsPanel;
    }
    
    /** 
     * Builds and lays out the panel hosting the message.
     * 
     * @return See above.
     */
    private JPanel buildMessagePanel()
    {
        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
        Icon icon = null;
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setAlignmentY(TOP_ALIGNMENT);
        messagePanel.add(iconLabel);
        messagePanel.add(Box.createRigidArea(H_SPACER_SIZE));
        MultilineLabel messageLabel = buildLabel(LOADING_MSG);
        messagePanel.add(messageLabel);
        return messagePanel;
    }
    
    /** Builds and lays out the GUI. */
    private void buildGUI()
    {
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                BorderFactory.createEmptyBorder(5, 5, 15, 10)));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(buildMessagePanel());
        contentPanel.add(Box.createRigidArea(V_SPACER_SIZE));
        contentPanel.add(progressBar);
        contentPanel.add(Box.createRigidArea(V_SPACER_SIZE));
        contentPanel.add(buildButtonsPanel());
        getContentPane().add(contentPanel);
    }
    
    /**
     * Creates a new instance. 
     * 
     * @param owner The owner of the frame.
     */
    LoadingWindow(JFrame owner)
    {
        super(owner, "Loading Data", true);
        setResizable(false);
        createComponents();
        attachListeners();
        buildGUI();
        pack();
    }
    
}
