package utilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import dao.DaoOperation;
import gui.MainGui;

public class MyChart extends JFrame {

    private String title = "Normalized Certainty Penalty (NCP)";
    
    DefaultPieDataset dataset;
    JFreeChart chart;
    ChartPanel chartPanel;
    
    private DaoOperation dboparation;

    DefaultPieDataset dataset_2;
    JFreeChart chart_2;
    ChartPanel chartPanel_2;
    
    
    
    private JLabel L1 =  new JLabel("<html><body><h4 style=\"margin-left:10px\"># NCP  After applying t-closeness : "+ (MainGui.Total_afterTC==0.0 ? 0.0 : String.valueOf(MainGui.Total_afterTC).substring(0,String.valueOf(MainGui.Total_afterTC).indexOf(".")+3)  )+"</h4></body></html>");
    private JLabel L2 =  new JLabel("<html><body><h4  style=\"margin-left:10px\"># NCP Before applying t-closeness : "+ (MainGui.Total_beforeTC==0.0 ? 0.0 :String.valueOf(MainGui.Total_beforeTC).substring(0,String.valueOf(MainGui.Total_beforeTC).indexOf(".")+3))+"</h4></body></html>");
    
   
    
    
    JLabel  LNRC =new JLabel("<html><body><h3  style=\"margin-left:20px\"># Number of Records Changed (NRC):  "+(MainGui.NumberOfRecordChanged==0.0 ? 0.0 :String.valueOf(MainGui.NumberOfRecordChanged).substring(0,String.valueOf(MainGui.NumberOfRecordChanged).indexOf(".")+3))+"</h3></body></html>");
	
    JButton  retour = new JButton("Go back ");
    
    
    private  JPanel  center = new JPanel(new GridLayout(1,2));
    private  JPanel  grid = new JPanel(new GridLayout(2, 1));

    
    private  JPanel  flow = new JPanel(new BorderLayout());
    private  JPanel  grid2 = new JPanel(new GridLayout(1,1));



    public MyChart(DaoOperation dboparation) {
    	
    	
        createCharts();
        this.dboparation=dboparation;
        
        
        
        this.setLayout(new BorderLayout());
        
        
        
        
        grid.add(L2);
        
        grid.add(L1);

        center.add(chartPanel_2);
        
        center.add(chartPanel);
        
        
        this.add(grid,BorderLayout.NORTH);
        
        
        grid.setBackground(Color.white);
        chartPanel.setBackground(Color.white);
        chartPanel_2.setBackground(Color.white);
             
        center.setBackground(Color.white);
        flow.setBackground(Color.white);

        
        this.add(center);
        
        
        grid2.add(LNRC);
        
        flow.add(grid2,BorderLayout.NORTH);
        
        JPanel p = new JPanel(new FlowLayout());
        p.setBackground(Color.white);

        p.add(retour);
        
        flow.add(p,BorderLayout.CENTER);
        
        this.add(flow,BorderLayout.SOUTH);
       
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setBounds(20,100,700,500);
        this.setLocationRelativeTo(null);
        
        
        
        retour.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				try {
					
					setVisible(false);

					MainGui m = new MainGui(dboparation);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
			}
		});
        
        
        
        
        
        
    }

    private void createCharts() {
    	
        dataset = createDataset("after applying t-closeness",((int)(MainGui.Total_afterTC*100)));
        chart = createChart(dataset, title);
        chartPanel = new ChartPanel(chart);
        
        dataset_2 = createDataset("before applying t-closeness",((int)(MainGui.Total_beforeTC*100)));
        chart_2 = createChart(dataset_2, title);
        chartPanel_2 = new ChartPanel(chart_2);


    }

    private DefaultPieDataset createDataset(String title,double n) {
    	
        DefaultPieDataset result = new DefaultPieDataset();
       
        result.setValue(title, n);
        result.setValue("None", 100-n);


        return result;
    }

    
    
    
    
    private JFreeChart createChart(PieDataset dataset, String title) {
        JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }


    


}


