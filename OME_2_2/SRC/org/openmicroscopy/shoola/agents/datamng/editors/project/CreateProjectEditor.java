/*
 * org.openmicroscopy.shoola.agents.datamng.editors.CreateProjectEditor
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

package org.openmicroscopy.shoola.agents.datamng.editors.project;


//Java imports
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.datamng.DataManager;
import org.openmicroscopy.shoola.agents.datamng.DataManagerCtrl;
import org.openmicroscopy.shoola.agents.datamng.IconManager;
import org.openmicroscopy.shoola.env.config.Registry;
import org.openmicroscopy.shoola.env.data.model.ProjectData;
import org.openmicroscopy.shoola.util.ui.TitlePanel;

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
public class CreateProjectEditor
	extends JDialog
{
	
	private Registry 					registry;
	private CreateProjectPane 			creationPane;
	private CreateProjectDatasetsPane	datasetsPane;
	private CreateProjectEditorBar		bar;
	private CreateProjectEditorManager	manager;
	
	public CreateProjectEditor(Registry registry, DataManagerCtrl control,
								ProjectData model, List datasets)
	{
		super(control.getReferenceFrame(), true);
		this.registry = registry;
		manager = new CreateProjectEditorManager(this, control, model,
												datasets);
		creationPane = new CreateProjectPane(manager);
		datasetsPane = new CreateProjectDatasetsPane(manager);
		bar = new CreateProjectEditorBar();
		buildGUI();
		manager.initListeners();
		setSize(DataManager.EDITOR_WIDTH, DataManager.EDITOR_HEIGHT);
	}
	
	Registry getRegistry() { return registry; }
	
	/** Returns the widget {@link CreateProjectEditorManager manager}. */
	CreateProjectEditorManager getManager() { return manager; }
	
	/** Returns the TextArea displayed in {@link CreateProjectPane}. */
	JTextArea getDescriptionArea() { return creationPane.getDescriptionArea(); }

	/** Returns the textfield displayed in {@link CreateProjectPane}. */
	JTextArea getNameField() { return creationPane.getNameField(); }
	
	/** Returns the save button displayed in {@link CreateProjectEditorBar}. */
	JButton getSaveButton() { return bar.getSaveButton(); }
	
	/** Returns the save button displayed in {@link CreateProjectEditorBar}. */
	JButton getCancelButton() { return bar.getCancelButton(); }
	
	/** 
	 * Returns the select button displayed in {@link CreateProjectDatasetsPane}.
	 */
	JButton getSelectButton() { return datasetsPane.getSelectButton(); }
	
	/** 
	 * Returns the reset button displayed in {@link CreateProjectDatasetsPane}.
	 */
	JButton getResetButton() { return datasetsPane.getResetButton(); }
	
	/** Forward event to the pane {@link CreateProjectDatasetsPane}. */
	void selectAll()
	{
		datasetsPane.setSelection(new Boolean(true));
	}
	
	/** Forward event to the pane {@link CreateProjectDatasetsPane}. */
	void cancelSelection()
	{
		datasetsPane.setSelection(new Boolean(false));
	}
	
	/** Build and lay out the GUI. */
	private void buildGUI()
	{
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, 
										  JTabbedPane.WRAP_TAB_LAYOUT);
		tabs.setAlignmentX(LEFT_ALIGNMENT);
		IconManager im = IconManager.getInstance(registry);
		//TODO: specify lookup name.
		Font font = (Font) registry.lookup("/resources/fonts/Titles");
		tabs.addTab("New Project", im.getIcon(IconManager.PROJECT), 
					creationPane);
		tabs.addTab("Add Datasets", im.getIcon(IconManager.DATASET), 
					datasetsPane);
		tabs.setSelectedComponent(creationPane);
		tabs.setFont(font);
		tabs.setForeground(DataManager.STEELBLUE);
		TitlePanel tp = new TitlePanel("Project", "Create a new project.", 
						im.getIcon(IconManager.CREATE_PROJECT_BIG));
		//set layout and add components
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(tp, BorderLayout.NORTH);
		getContentPane().add(tabs, BorderLayout.CENTER);
		getContentPane().add(bar, BorderLayout.SOUTH);
	}

	
}
