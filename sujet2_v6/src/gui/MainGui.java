package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.midi.Soundbank;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import dao.DaoOperation;
import model.Bucket;
import treeProcessing.DiseaseAnonymiser;
import treeProcessing.RecordSwapper;
import treeProcessing.hierarchy;
import utilities.MyChart;
import utilities.Parameter;


import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class MainGui extends JFrame {

	// JTable vars
	private JTable table;
	private JTable tableAnony;
	private DefaultTableModel model;
	private DefaultTableModel modelAnonymized;
	
	// panels
	private Panel panCenter = new Panel(new GridLayout(2, 1));
	private Panel panBtn = new Panel(new GridLayout(5, 1));
	private Panel paneEst = new Panel();

	// buttons
	private JButton btnok = new JButton("ok");
	private JButton ncp = new JButton("apply NCP & NRC");
	private JButton exportExcelBtn = new JButton("export as XLSX");
	private JButton exportSqlBtn = new JButton("export as sql");

	// variables to hold the value of NRC & 
	public static double Total_afterTC;
	public static double Total_beforeTC;
	public static double NumberOfRecordChanged;
	public  ArrayList<Bucket> l;

	private JComboBox<String> comboOPT = new JComboBox<String>(
			
			// options for the swap operation
			new String[] { "max to max", "min to min", "max to min", "min to max" });

	private Object[] columns;

	public MainGui(DaoOperation dboparation) throws HeadlessException, ClassNotFoundException, SQLException {

		// adding the buttons to the pan 
		panBtn.add(comboOPT);
		panBtn.add(btnok);
		panBtn.add(ncp);
		panBtn.add(exportExcelBtn);
		panBtn.add(exportSqlBtn);
		
		// disabling the buttons
		exportExcelBtn.setEnabled(false);
		exportSqlBtn.setEnabled(false);

		// filling the original table
		columns = Parameter.columns;
		model = new DefaultTableModel(DaoOperation.getResultSet(Parameter.tableName), columns);
		table = new JTable(model);
		JScrollPane jScrollPane = new JScrollPane(table);
		table.setBackground(new Color(245, 222, 179));
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFillsViewportHeight(true);
		
		
		// adding listeners to buttons
		
		// button for ncp
		ncp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				new MyChart(dboparation);
				Parameter.Nbchaged = 0;
				dispose();

			}
		});

		// ok btn  
		
		btnok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println(comboOPT.getSelectedItem());
				//enabling the buttons
				ncp.setEnabled(true);
				exportSqlBtn.setEnabled(true);
				exportExcelBtn.setEnabled(true);
				
				// Retreating all the buckets
				 l = DaoOperation.getAllBuckets();
				// attribute the disease ID for each record of the list passed
				hierarchy.setDiseaseId(l);
				//make the records of the list anonymous  by attributing a code (number) for each diseaseID
				DiseaseAnonymiser.anonimizeDisease(l);
				//swap the records that are belonging to the same hierarchy 
				// sort the list passed by the "anonymizedCode"  value 
				RecordSwapper.sortDiseaseCode(l);

				for (Bucket b : l) {
					System.out.println("_______________________");
					for (Map<String, String> ele : b.getRecords()) {
						System.out.println("::::::::::::::::");
						for (Entry<String, String> ee : ele.entrySet()) {
							System.out.println(ee.getKey() + "   :" + ee.getValue());
						}
					}
				}
				// classify buckets  ( buckets that should participate to the swap  , andd those that should not)
				RecordSwapper.bucketsClassifier(l);

				HashMap<Bucket, Double> bukects_avant = DiseaseAnonymiser.getMoyenneEachBucket(l);

				for (Entry<Bucket, Double> b : bukects_avant.entrySet()) {
					System.out.println("#######################");
					System.out.println(b + " : " + b.getValue());

				}

				System.out.println(
						"Total Moyenne NCP AVANT :" + DiseaseAnonymiser.getTotalMoyenneNCP(bukects_avant.values()));

				Total_beforeTC = DiseaseAnonymiser.getTotalMoyenneNCP(bukects_avant.values());

				if (comboOPT.getSelectedItem() == "max to max") {
					RecordSwapper.bucketsShuffler(l, "max");
				} else if (comboOPT.getSelectedItem() == "min to min") {
					RecordSwapper.bucketsShuffler(l, "min");
				} else if (comboOPT.getSelectedItem() == "max to min") {
					RecordSwapper.bucketsShuffler(l, "maxtomin");
				} else if (comboOPT.getSelectedItem() == "min to max") {
					RecordSwapper.bucketsShuffler(l, "mintomax");
				}

				System.out.println("##############  APRES   #########");

				for (Bucket b : l) {

					System.out.println("_______________________");
					for (Map<String, String> ele : b.getRecords()) {
						System.out.println("::::::::::::::::");
						for (Entry<String, String> ee : ele.entrySet()) {
							System.out.println(ee.getKey() + "   :" + ee.getValue());
						}
					}
				}

				System.out.println(".....................");

				HashMap<Bucket, Double> bukects_apres = DiseaseAnonymiser.getMoyenneEachBucket(l);

				for (Entry<Bucket, Double> b : bukects_apres.entrySet()) {
					System.out.println("#######################");
					System.out.println(b + " : " + b.getValue());

				}

				System.out.println("Total Moyenne NCP APRES :" + DiseaseAnonymiser.getTotalMoyenneNCP(bukects_apres.values()));

				Total_afterTC = DiseaseAnonymiser.getTotalMoyenneNCP(bukects_apres.values());

				NumberOfRecordChanged = DiseaseAnonymiser.getNRC();

				DaoOperation.insertResult(l);
				// show the tab
				tableAnony.setModel(new DefaultTableModel(DaoOperation.getResultSet(Parameter.resultTable), columns));

			}
		});
		
		//generate the result table as excel
		exportExcelBtn.addActionListener(new ActionListener()   {
			
			@Override
			public void actionPerformed(ActionEvent e)   {
				// TODO Auto-generated method stub
				
				try {
				File selectedFile=null;       
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				fileChooser.showOpenDialog(null);
				selectedFile = fileChooser.getSelectedFile();
				if(selectedFile!=null){
				    

				 String  file=selectedFile.getAbsolutePath();
				if(!selectedFile.getAbsolutePath().endsWith(".xls")){
				 file=selectedFile.getAbsolutePath()+".xls";  
				}
				  
				
				
				
				

			   WritableWorkbook workbook = Workbook.createWorkbook(new File(file));
			   WritableSheet writablesheet1 = workbook.createSheet("Sheet1", 0);
			   WritableFont times10pt = new WritableFont(WritableFont.TIMES, 12,WritableFont.BOLD);
			   WritableFont times12pt = new WritableFont(WritableFont.TIMES, 11);
			   // Define the cell format
			   WritableCellFormat  times = new WritableCellFormat(times10pt);
			   WritableCellFormat  times2 = new WritableCellFormat(times12pt);
			   times.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			   times2.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			   times.setBackground(Colour.WHITE);
			   times.setAlignment(Alignment.CENTRE);
			   times.setVerticalAlignment(VerticalAlignment.CENTRE);
			   WritableCellFormat cellFormat = new WritableCellFormat(times);
			   cellFormat.setBackground(Colour.YELLOW);
			   cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			   //  automatically wrap the cells
			  //  create a bold font with unterlines
			   WritableFont times10ptBoldUnderline = new WritableFont(
			   WritableFont.TIMES, 18, WritableFont.BOLD, false,
			   UnderlineStyle.SINGLE);
			   WritableCellFormat	timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
			   //  automatically wrap the cells
			   Label label;
			   label = new Label(4, 1, " the result after applying t-closeness", timesBoldUnderline);
			   writablesheet1.addCell(label);
			    
			   writablesheet1.setRowView(3, 26*20);
			    
			    
			    int cols=3;
			    int  rows =4;
			    
			    for(Object  o : Parameter.columns) {
				       
			   
			        writablesheet1.setColumnView(cols,16); 
			        writablesheet1.addCell(new Label(cols,3,o.toString(),cellFormat));
			       
			    	cols++;
			    }
			    cols=3;
			    
			    for(Object[]  result : DaoOperation.getResultSet(Parameter.resultTable)) {
			    	
			    	for(Object o : result) {
			    		
			    		 writablesheet1.setColumnView(cols,20); 
			    	     writablesheet1.addCell(new Label(cols,rows,o.toString(),times2));
			    		
			    	     
			    	     cols++;
			    	}
			    	
			    	
			    	cols=3;
			    	rows++;
		
			    }
			    
			   
			    JOptionPane.showMessageDialog(null, "operation  successful   ","info",JOptionPane.INFORMATION_MESSAGE);			     
			     
			     workbook.write();
			     workbook.close();
				
			
				}	
				
				
			}catch (Exception e2) {
				// TODO: handle exception
			}
				
			}
		});
		// generate the result table as sql
		exportSqlBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				
				File selectedFile=null;       
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				fileChooser.showOpenDialog(null);
				selectedFile = fileChooser.getSelectedFile();
				if(selectedFile!=null){
				    

			     String  file=selectedFile.getAbsolutePath();
			     
				if(!selectedFile.getAbsolutePath().endsWith(".sql")){
				 file=selectedFile.getAbsolutePath()+".sql";  
				}
				  
				
							
				BufferedWriter writer = null;

				try {
					
				    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(file))));

					String createTab="create table if NOT EXISTS "+Parameter.resultTable+" as select * from "+Parameter.tableName+";";
					String trunCatTab="TRUNCATE "+Parameter.resultTable+";";
									
				    writer.write(createTab+"\n");
				    writer.write(trunCatTab+"\n");
			
					String insertQry="insert into "+Parameter.resultTable+" (";
					String compeletion=" values(";											
				    int i=0;
				    
				
				   	for(Object[]  result : DaoOperation.getResultSet(Parameter.resultTable)) {
				    	
				    	for(Object o : result) {
				    										
								if(i==Parameter.columns.length-1) {
									insertQry=insertQry+" "+Parameter.columns[i]+")";
									compeletion+=o.toString()+");";
								}else {
									insertQry=insertQry+" "+Parameter.columns[i]+",";	
									compeletion+=o.toString()+",";
								}
								i++;
				    	}
				    					    		
				    		 writer.write(insertQry+compeletion);
							 writer.write("\n");
													
							 insertQry="insert into "+Parameter.resultTable+" (";
							 compeletion=" values(";
							 i=0;
						
					}
				
			
				
				} catch (IOException ex) {
				    // Report
				} finally {
				   try {writer.close();} catch (Exception ex) {/*ignore*/}
				}
				
			      JOptionPane.showMessageDialog(null, "operation  successful","Info",JOptionPane.INFORMATION_MESSAGE);

				
				}
				
				
				
				
				
			}
		});
		
		
		comboOPT.setLightWeightPopupEnabled(false);
		comboOPT.setPreferredSize(new Dimension(90, 30));

		table.setBackground(new Color(245, 222, 179));
		// tableAnony.setVisible(true);
		modelAnonymized = new DefaultTableModel();
		tableAnony = new JTable(modelAnonymized);
		JScrollPane jScrollPane2 = new JScrollPane(tableAnony);
		tableAnony.setPreferredScrollableViewportSize(table.getPreferredSize());
		tableAnony.setFillsViewportHeight(true);

		paneEst.add(panBtn);
		this.add(paneEst, BorderLayout.EAST);

		panCenter.add(jScrollPane);
		panCenter.add(jScrollPane2);

		this.add(panCenter);
		ncp.setEnabled(false);
		setTitle("t-closeness technique");
		setLocationRelativeTo(null);
		setVisible(true);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.setSize(600, 500);

	}

}
