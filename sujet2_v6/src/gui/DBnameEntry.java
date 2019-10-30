package gui;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dao.connection;

public class DBnameEntry extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton submitButton= new JButton("go!") ;
	JLabel lbl = new JLabel("database Name:");
	JTextField txtField= new JTextField(15);
	
	JComboBox<String> comboBDS = new JComboBox<String>();

	JPanel pan1= new JPanel();
	JPanel pan2= new JPanel();
	
	public DBnameEntry() throws HeadlessException {
		
		
		
		
      
        comboBDS=connection.getDbsName();
        

        //adding components  
        pan1.add(lbl);
        pan1.add(comboBDS);
        pan2.add(submitButton);
        this.add(pan1);
        this.add(pan2);
        
        // necessary conf
        setSize(400,150);
        setTitle("welcome");
        setLocationRelativeTo(null);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(2, 1));
        this.pack();
        
        // listener to the submit button
        submitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					
					
					String bd=comboBDS.getSelectedItem().toString();
					System.out.println(bd);
					// pass the db name to the next interface
					new choicesGUI(bd);
					
					setVisible(false);
				} catch (ClassNotFoundException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Oops ! something went wrong", JOptionPane.ERROR_MESSAGE, null);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Oops ! something went wrong", JOptionPane.ERROR_MESSAGE, null);
				}
				
			}
		});
	}


}
