import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimpleStart {

    public static void main(String[] args) {     	
       
        JFrame frame = new JFrame("Signature Searcher");
               
        JPanel masterPanel = new JPanel(); // create a new master panel        
     
        masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.PAGE_AXIS)); // use a box layout to stack the panels
        
        mainPanel panel = new mainPanel();
        frame.setJMenuBar(panel.setupMenu());
        
        masterPanel.add(panel); // add sub-panels to master panel        
     
        frame.getContentPane().add(masterPanel); // add the master panel to the frame

        frame.pack();
        frame.setVisible(true);
        
    }
}