/*
 * org.openmicroscopy.shoola.env.ui.SplashScreenManager
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.env.ui;

//Java imports
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.env.Container;
import org.openmicroscopy.shoola.env.data.login.UserCredentials;
import org.openmicroscopy.shoola.util.ui.UIUtilities;

/** 
 * Manages the splash screen input, data and update.
 * Plays both the role of Controller and Model within the splash screen
 * component.
 * <p>Provides clients with the splash screen component's functionality &#151;
 * as specified by the the {@link SplashScreen} interface.  However, clients 
 * never get an instance of this class.  The reason is that this component is
 * meant to be used during the initialization procedure, which runs within its
 * own thread &#151; this component's event handling happens within the
 * <i>Swing</i> dispatching thread instead.  In order to separate threading
 * issues from the actual component's functionality, we use Active Object: this
 * class palys the role of the Servant and a proxy ({@link SplashScreenProxy})
 * is actually returned to clients &#151; by the {@link UIFactory}.</p>
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

class SplashScreenManager
	implements ActionListener, PropertyChangeListener
{
    
	/** The component's UI. */
	private SplashScreenView	view;
	
	/** The component's UI. */
	private SplashScreenViewTop	viewTop;
	
	/** Tells whether or not the splash screen window is open. */
	private boolean				isOpen;
	
	/** Filled in with the user's login when available. */
	private SplashScreenFuture	userCredentials;

	/** The current number of tasks to be executed. */
	private int					totalTasks;
	
	/** The current number of tasks that have been executed. */
	private int					doneTasks;
	
	/** Reference to the singleton {@link Container}. */
	private Container			container;
	
    /** 
     * Bings up the server dialog to select an existing server or enter
     * a new server address.
     */
    private void handleServerSelection()
    {
    	ServerDialog d = new ServerDialog(view.servers);
    	d.addPropertyChangeListener(this);
    	UIUtilities.centerAndShow(d);
    }
    
	/**
	 * Creates the splash screen component.
	 * Creates a new instance of this manager, of its corresponding UI
	 * ({@link SplashScreenView}), and links them as needed.
     * 
     * @param listener 	A listener for {@link SplashScreenView#cancel} button.
     * @param c			Reference to the singleton {@link Container}.
	 */
	SplashScreenManager(ActionListener listener, Container c)
	{
		container = c;
		view = new SplashScreenView();
		viewTop = new SplashScreenViewTop();
		Rectangle r = viewTop.getBounds();
		view.setBounds(r.x, 
					r.y+SplashScreenViewTop.WIN_H+SplashScreenViewTop.GAP, 
					SplashScreenView.LOGIN_WIDTH, 
					SplashScreenView.LOGIN_HEIGHT);
		view.user.addActionListener(this);
		view.pass.addActionListener(this);
		view.login.addActionListener(this);
        view.cancel.addActionListener(listener);
        view.configButton.addActionListener(new ActionListener() {
        
            public void actionPerformed(ActionEvent e)
            {
                handleServerSelection();
            }
        
        });
        view.addWindowListener(new WindowAdapter()
        {
        	public void windowOpened(WindowEvent e) {
        		view.user.requestFocus();
        	} 
        });
        view.setAlwaysOnTop(true);
        viewTop.setAlwaysOnTop(true);
		isOpen = false;
		doneTasks = 0;
	}

	/**
	 * Implemented as specified by {@link SplashScreen}.
	 * @see SplashScreen#open()
	 */
	void open()
	{
		//close() has already been called.
		if (view == null || viewTop == null) return;  
		//view.setVisible(true);
		viewTop.setVisible(true);
		isOpen = true;	
	}

	/**
	 * Implemented as specified by {@link SplashScreen}.
	 * @see SplashScreen#close()
	 */
	void close()
	{
		//close() has already been called.
		if (view == null || viewTop == null) return;
		view.dispose();
		viewTop.dispose();
		view = null;
		viewTop = null;
		isOpen = false;
	}

	/**
	 * Sets the total number of initialization tasks that have to be
     * performed.
     * 
     * @param value The total number of tasks.
	 * @see SplashScreen#setTotalTasks(int)
	 */
	void setTotalTasks(int value)
	{
		if (!isOpen) return;
		totalTasks = value;
		//NB: Increment to show that the execution process is finished 
		// i.e. all tasks executed.
		totalTasks++;	
		viewTop.progressBar.setMinimum(0);
		viewTop.progressBar.setMaximum(value);
		viewTop.progressBar.setValue(0);
	}
	
	/**
	 * Updates the display to the current state of the initialization
     * procedure.
     * 
     * @param task  The name of the initialization task that is about to
     *              be executed.
	 * @see SplashScreen#updateProgress(String)
	 */
	void updateProgress(String task)
	{
		if (!isOpen) return;
		viewTop.currentTask.setText(task);
		viewTop.progressBar.setValue(doneTasks++);
		if (doneTasks == totalTasks) {
			viewTop.progressBar.setVisible(false);
			view.setVisible(true);
		}
	}
    
    /**
     * Registers a request to fill in the given <code>future</code> with
     * the user's credentials when available.
     * 
     * @param future The Future to collect the credentials.
     * @param init   Flag to control if it's the first attempt. 
     */
    void collectUserCredentials(SplashScreenFuture future, boolean init)
    {
        userCredentials = future;
        view.user.setEnabled(true);
        view.pass.setEnabled(true);
        view.login.setEnabled(true);
        //view.cancel.setEnabled(true);
        view.configButton.setEnabled(true);
        if (!init) {
            view.setCursor(Cursor.getDefaultCursor());
            view.user.setText("");
            view.pass.setText("");
        }
    }
	
    /**
     * Registers a request to fill in the given <code>future</code> with
     * the user's credentials when available.
     * 
     * @param future The Future to collect the credentials.
     */
    void collectUserCredentialsInit(SplashScreenFuture future)
    {
        userCredentials = future;
        view.user.setEnabled(true);
        view.pass.setEnabled(true);
        view.login.setEnabled(true);
        view.configButton.setEnabled(true);
    }
    
	/** 
	 * Handles action events fired by the login fields and button.
	 * Once user name and password have been entered, the login fields and
	 * button will be disabled. 
     * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
        StringBuffer buf = new StringBuffer();
        buf.append(view.pass.getPassword());
        String usr = view.user.getText().trim(), psw = buf.toString();
        String s = view.serverText.getText().trim();
        try {
            UserCredentials uc = new UserCredentials(usr, psw, s);
            if (userCredentials != null) { 
                userCredentials.set(uc);
                view.user.setEnabled(false);
                view.pass.setEnabled(false);
                view.login.setEnabled(false);
                //view.cancel.setEnabled(false);
                view.configButton.setEnabled(false);
                view.setCursor(
                        Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            }
        } catch (IllegalArgumentException iae) {
            UserNotifier un = UIFactory.makeUserNotifier(container);
            un.notifyError("Login Incomplete", iae.getMessage());
        }
	}

	/**
	 * Reacts to property changes fired by the {@link ServerDialog}.
	 * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt)
	{
		String name = evt.getPropertyName();
		if (ServerDialog.SERVER_PROPERTY.equals(name)) {
			String v = view.getServerName();
			String s = (String) evt.getNewValue();
			if (s == null) {
				view.setNewServer(null);
				return;
			}
			String trim = s.trim();
			if (v.equals(trim)) return;
			view.setNewServer(trim);
		} else if (ServerDialog.CLOSE_PROPERTY.equals(name)) {
			view.user.requestFocus();
		} else if (ServerDialog.REMOVE_PROPERTY.equals(name)) {
			view.user.requestFocus();
			String v = view.getServerName();
			String oldValue = (String) evt.getOldValue();
			UIFactory.removeServer(oldValue);
			if (v.equals(oldValue)) 
				view.setNewServer((String) evt.getNewValue());
		} 
			
	}

}
