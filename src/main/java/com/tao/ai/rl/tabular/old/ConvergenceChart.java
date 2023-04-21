package com.tao.ai.rl.tabular.old;



import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import com.tao.ai.rl.tabular.old.ReinforcementLearningTest.Algorithm;

public class ConvergenceChart extends ApplicationFrame {
	private XYSeriesCollection dataset1 = new XYSeriesCollection( );  
	private XYSeriesCollection dataset2 = new XYSeriesCollection( ); 
   public ConvergenceChart(Algorithm... names) {
      super("");
      
      JPanel contentPanel=new JPanel();
      contentPanel.setPreferredSize( new java.awt.Dimension( 1600 , 800 ) );
      contentPanel.setLayout(new GridLayout(1,2));
      setContentPane( contentPanel ); 
      
      JFreeChart xylineChart1 = ChartFactory.createXYLineChart(
         "" ,
         "Iteration" ,
         "Error" ,
         dataset1,
         PlotOrientation.VERTICAL ,
         true , true , false);
         
      ChartPanel chartPanel = new ChartPanel( xylineChart1 );
      contentPanel.add( chartPanel);
      
      JFreeChart xylineChart2 = ChartFactory.createXYLineChart(
    	         "" ,
    	         "Iteration" ,
    	         "Objective" ,
    	         dataset2,
    	         PlotOrientation.VERTICAL ,
    	         true , true , false);
    	         
    chartPanel = new ChartPanel( xylineChart2 );
    contentPanel.add( chartPanel);
      
     
      
     
      
      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
      renderer.setSeriesPaint( 0 , Color.BLUE );
      renderer.setSeriesPaint( 1 , Color.RED );
      renderer.setSeriesPaint( 2 , Color.YELLOW );
      renderer.setSeriesStroke( 0 , new BasicStroke( 0.1f ) );
      renderer.setSeriesStroke( 1 , new BasicStroke( 0.1f ) );
      renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
      
       XYPlot plot = xylineChart1.getXYPlot( );
      plot.setRenderer( renderer ); 
      plot = xylineChart2.getXYPlot( );
      plot.setRenderer( renderer ); 
    
      
      for(int i=0;i<names.length;i++) {
	      XYSeries firefox = new XYSeries(names[i]);  
	      dataset1.addSeries(firefox);
	      firefox = new XYSeries(names[i]);  
	      dataset2.addSeries(firefox);
      }
      this.addKeyListener(new KeyListener() {

		@Override
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode()==KeyEvent.VK_ESCAPE) {
				ReinforcementLearningTest.terminate=true;
			}
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}});
   }
   
   public void addData(Algorithm idx,double... data) {
	   dataset1.getSeries(idx).add(data[0], data[1]);
	   dataset2.getSeries(idx).add(data[0], data[2]);
   }
   
  /* public XYSeries addSerie(String name) {
	   XYSeries firefox = new XYSeries(name);  
	   dataset.addSeries(firefox);
	 
	   return firefox;
   }*/
   
   
  
}