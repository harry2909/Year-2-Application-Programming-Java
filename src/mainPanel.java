import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class mainPanel extends JPanel {

 public File file = new File("");
 JTextArea display = new JTextArea(30, 50);
 String chooserTitle;
 ArrayList<String> pattern = new ArrayList<String>();
 public byte [] patternABC = new byte [] { 0x41, 0x42, 0x43 };
 public byte [] patternXYZ = new byte [] { 0x58, 0x59, 0x5A };
 public int byteCount = 0; // counter for position of each byte
 public static String found; //

 /*
  * Declare JMenuBar to hold file.
  * Declare JMenuItem to extend the file menu.
  * Set Mnemonics for each menu name.
  * Set Keystroke events for shortcuts.
  */

 JMenuBar setupMenu() {

  JPanel panel = new JPanel();
  panel.setPreferredSize(new Dimension(600, 300));

  JMenuBar menuBar = new JMenuBar();

  JMenu fileMenu = new JMenu("File");
  fileMenu.setToolTipText("Contents");
  fileMenu.setMnemonic(KeyEvent.VK_F);

  JMenuItem exitMenu = new JMenuItem("Exit");
  exitMenu.addActionListener((event) -> System.exit(0));
  exitMenu.setToolTipText("Exit application");
  exitMenu.setMnemonic(KeyEvent.VK_E);

  exitMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));

  JMenuItem aboutMenu = new JMenuItem("About");
  aboutMenu.setToolTipText("Information on the programme");

  // listener added to use JOptionPane to ouput message on click
  aboutMenu.addActionListener(event -> JOptionPane.showMessageDialog(null, "This is a tool used to identify " +
          "byte-sequences." + "\nAuthor: Harry Preston." + "\nWarning: Copyrighted."));
  aboutMenu.setMnemonic(KeyEvent.VK_A);

  // declare action listener for load
  ActionListener listener1 = new fileLoader();
  JMenuItem load = new JMenuItem("Load");
  load.setToolTipText("About, load file or exit.");
  load.addActionListener(listener1);
  load.setToolTipText("Load file contents");
  load.setMnemonic(KeyEvent.VK_L);
  load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
          InputEvent.CTRL_DOWN_MASK));

  // add items to menuBar
  menuBar.add(fileMenu);
  fileMenu.add(load);
  fileMenu.add(aboutMenu);
  fileMenu.add(exitMenu);

  return menuBar;

 }

 public void loadFile() {

  // Important to use a buffered input stream, since reading a single byte at a time from file input stream would be very slow
  InputStream inputStream = null;
  String filePath = file.getPath();
  try {
   inputStream = new BufferedInputStream(new FileInputStream(filePath));
  } catch (FileNotFoundException e) {
   e.printStackTrace();
  }
  int next = 0; // the most recently read byte from the file.

  // read each byte of the file (until the end of the file has been reached), looking for any of the patterns
  try {
   this.display.append("Scanning: \n");
   int counter = 0; // counter to iterate through
   int counter2 = 0;
   while ((next = inputStream.read()) != -1) {
    byte nextByte = (byte) next;   // convert the value read into an 8 bit byte
    byteCount++; // records position
    if(patternABC[counter] == nextByte){
     counter++;
     if(counter==patternABC.length){    	 
     this.display.append("\n Pattern found: " + Arrays.toString(patternABC) + "\n");  // compare value of 'nextByte' with the next byte within the pattern to be detected      
     counter = 0;
     }
    }
    else {
     counter = 0;
    }
    if(patternXYZ[counter2] == nextByte){
     counter2++;
     if(counter2==patternXYZ.length){
     // compare value of 'nextByte' with the next byte within the pattern to be detected
     this.display.append("\n Pattern found: " + Arrays.toString(patternXYZ) + "\n");    
     counter2 = 0;
     }
    }
    else {
     counter2 = 0;
    }
   }

  } catch (IOException d) {

  }
  try {
   inputStream.close();
  } catch (IOException e) {
   e.printStackTrace();
  }  
  this.display.append("\n Pattern not found.\n");  
   
 }

 public mainPanel() {	 
	  add(display); // styling
	  setPreferredSize(new Dimension(800, 80));
	  setBackground(new java.awt.Color(229, 89, 52));

 }
  
 // new method to call append text


 // actionListener used to choose file, make currencyPanel.file equal to selected file
 public class fileLoader implements ActionListener {
  public void actionPerformed(ActionEvent event) {
   try {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new java.io.File("."));
    fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    fileChooser.setDialogTitle(chooserTitle);
    int result = fileChooser.showOpenDialog(null);
    if (result == JFileChooser.APPROVE_OPTION) {    	
    file = fileChooser.getSelectedFile();

    }
   } catch (Exception e) {
    System.out.print("Error");
   }
   // call loadFile method
   loadFile();

  }
 }
 
 /**
  * Models a single set of bytes that represent a pattern.
  */
 public class BytePattern {

     /**
      * The bytes within the pattern.
      */
     private byte[] bytes;

     /**
      * The next position to be checked within the pattern's byte array.
      */
     private int checkPos = 0;

     /**
      * Checks if the given value matches the next byte to be checked within the pattern.
      *
      * Each time this method is called it progresses to the next byte within the pattern, until the value does not match
      * or the end of the pattern is reached (in which case a match has been found).
      *
      * @param value the value to be checked against the next byte in the pattern.
      * @return true if the pattern has been matched, false if the pattern has not (yet) matched.
      * 
      */
     public boolean checkNext(byte value) {    	 
    	 
    	 for(checkPos = 0; checkPos < bytes.length; checkPos++) {
    	 if(bytes[checkPos] != value) {    		
    		checkPos++;
    		return false;
    	 }
    	 if(bytes[checkPos] == value) {
    		 checkPos++;
    		 return false;    		
    	 }
    	 if(bytes[checkPos] == bytes.length) {
    		 checkPos = 0;
			 return true;
		 }
    	 }
    	 return false;  	 
    	 

         // check the given value against the next byte in the pattern (as determined by checkPos)

         // if the given byte does not match the next byte in the pattern then the match has failed, so reset checkPos and return false

         // if the given byte does match the next byte in the pattern, then

               // if not all bytes in the pattern have yet been checked return false (since a match has not YET been found)

               // if all the bytes in the pattern have been checked then then a full match is found, so return true (also reset checkPos for future checks)
     }
     // other methods, e.g. constructor to set pattern bytes, method to parse line of pattern file data (req 3) etc.

 } 
 
 
 
 
}

