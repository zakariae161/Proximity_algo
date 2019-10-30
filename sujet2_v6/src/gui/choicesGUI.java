package gui;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;


import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dao.DaoOperation;

import utilities.Parameter;

public class choicesGUI extends JFrame {

	
	
	
	private static final long serialVersionUID = 1L;
	// submit button
	JButton submitBtn = new JButton("Go!");
	
	// comboBox models 
	ComboBoxModel<String> modelFlields;
	ComboBoxModel<String> modelNUMERIC;
	
	// combo that holds the tables of the chosen db 
	JComboBox<String> comboTables;
	
	JComboBox<String> comboIDBuckets = new JComboBox<String>();
	JComboBox<String> comboFields = new JComboBox<String>();
	JPanel pan1 = new JPanel();
	JLabel lblTable = new JLabel(" table:  ");
	JPanel pan2 = new JPanel();
	JLabel lblField = new JLabel(" field:  ");
	JPanel pan3 = new JPanel();
	JLabel lblBuckt = new JLabel("goupe id: ");
	JPanel panHelp = new JPanel();
	JPanel panGo = new JPanel();
	
	
	JLabel lblHelp = new JLabel("<html><bold>hints!:</bold> </br> <ul>" + "<li> choose the concerning table</li>"
			+ "<li> choose the field that contains the hierarchy values</li>" + "<li> choose the field that represents the group id</li>" + "</ul></html>");
	private DaoOperation dboparation;

	public choicesGUI(String DBName) throws ClassNotFoundException, SQLException {
		
		
		super();
		// establish the connection to the db 
		dboparation = new DaoOperation(DBName);
		// get all tables in the chosen db 
		comboTables = new JComboBox<String>(DaoOperation.getAllTables());
		
		//customize the size 
		comboFields.setPreferredSize(new Dimension(90, 30) );
		comboIDBuckets.setPreferredSize(new Dimension(90, 30) );
		comboTables.setPreferredSize(new Dimension(90, 30) );
		
		
		// set the corresponding fields and bucketID based on the chosen Table	
		setdependentCombx();
	
	
		
		
		// adding components to the jpanels
		panHelp.add(lblHelp);
		pan1.add(lblTable);
		pan1.add(comboTables);
		pan2.add(lblField);
		pan2.add(comboFields);
		pan3.add(lblBuckt);
		pan3.add(comboIDBuckets);
		panGo.add(submitBtn);
		// adding components to the interface


		
		
		// go button listener
		submitBtn.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// adding the needed parameters
						Parameter.tableName = comboTables.getSelectedItem().toString();
						Parameter.diseaseColumn = comboFields.getSelectedItem().toString();
						Parameter.bktId = comboIDBuckets.getSelectedItem().toString();
						// non
						Parameter.resultTable = Parameter.tableName + "Result";

						try {
							new MainGui(dboparation);
							dispose();
							// handling the  exceptions 
						} catch (HeadlessException e) {
							
							
							JOptionPane.showMessageDialog(null, e.getMessage(), "Oops ! something went wrong",
									JOptionPane.ERROR_MESSAGE, null);
						} catch (ClassNotFoundException e) {
							JOptionPane.showMessageDialog(null, e.getMessage(), "Oops ! something went wrong",
									JOptionPane.ERROR_MESSAGE, null);
						} catch (SQLException e) {
							JOptionPane.showMessageDialog(null, e.getMessage(), "Oops ! something went wrong",
									JOptionPane.ERROR_MESSAGE, null);
						}

					}
				});
		
        comboTables.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							
							setdependentCombx();
						}

					}
				});

		

        
        

		this.add(panHelp);
		this.add(pan1);
		this.add(pan2);
		this.add(pan3);
		this.add(panGo);

				
							
		setSize(400, 500);
	    setTitle("t-closeness technique");
		setLocationRelativeTo(null);
	    setVisible(true);
		this.setLayout(new GridLayout(5, 2));
	    setDefaultCloseOperation(EXIT_ON_CLOSE);

		
		
		
	}
	
	// set the corresponding fields and bucketID based on the chosen Table
	private void setdependentCombx() {
		try {
			
			modelFlields = new DefaultComboBoxModel<String>(DaoOperation.getStringFields(comboTables.getSelectedItem().toString()));
			comboFields.setModel(modelFlields);
			modelNUMERIC = new DefaultComboBoxModel<String>(DaoOperation.getNumericFields(comboTables.getSelectedItem().toString()));
			comboIDBuckets.setModel(modelNUMERIC);
		
			
		} catch (NullPointerException e) {
			// TODO: handle exception
			 submitBtn.setEnabled(false);
		}
		

}}
