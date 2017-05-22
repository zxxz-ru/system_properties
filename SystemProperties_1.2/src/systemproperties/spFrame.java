/*
 *Надо переделать класс таким образом что бы в JTextPane Каждая строка отображала
 тот Font имя которого эта строка выводит на экран. См.  метод initFontsTextArea()*/
package systemproperties;

import java.awt.AWTError;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import view.MultilineTableCell;

/**
 *
 * @author zxxz
 */
public class spFrame {
    
    JFrame mainFrame=null;
    JPanel propPanel = null;
    JPanel fontPanel = null;
    JScrollPane propertiesScroller=null;
    JScrollPane fontsScroller=null;
    JTable table = null;
    JTextArea propertiesTextArea=null;
    JTextPane fontsTextArea=null;
    JTabbedPane tabbedPane = new JTabbedPane();
    Dimension frameSize = null;
    MultilineTableCell renderer = null;
    public spFrame(){
        renderer = new MultilineTableCell();
        frameSize = new Dimension(800,600);
        initMainFrame();
    }//constructor
    private void initMainFrame(){
        Dimension screenSize = null;
        try{
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        }catch(AWTError e){
            this.displayErrorMessage("Serious System Error!\n"
                    + "Will terminate now!");
            System.exit(1);
            
        }
    this.mainFrame = new JFrame();
    this.mainFrame.setTitle("System Properties");
    this.mainFrame.setSize(frameSize);
    this.mainFrame.setLocation((screenSize.width-frameSize.width)/2,
            (screenSize.height-frameSize.height)/2);
    this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.mainFrame.setLayout(new BorderLayout());
    this.initPropPanel();
    this.initFontPanel();
    tabbedPane.add("System Properties", propPanel);
    tabbedPane.add("System Fonts", fontPanel);
    this.mainFrame.add(tabbedPane);
    this.mainFrame.setVisible(true);
    }//initMain
    
    
    //System Properties
    
    
    private void initPropPanel(){
    propPanel = new JPanel();
    propPanel.setLayout(new BorderLayout());
    this.initPropertiesTable();
    propPanel.add(this.propertiesScroller, BorderLayout.CENTER);
    
    }
   
    private void initPropertiesTable(){
    DefaultTableModel tm = new DefaultTableModel();
    tm.addColumn("Property name");
 
    tm.addColumn("Property value");
//table = new JTable(tm);
table = new JTable(tm){
            @Override
  public boolean isCellEditable(int rowIndex, int colIndex) {
  return false; //Disallow the editing of any cell
  }
            @Override
  public TableCellRenderer getCellRenderer(int row, int column) {
     
            return renderer;
        }
  
  };
TableColumn col_1 = table.getColumnModel().getColumn(0);
col_1.setPreferredWidth(300);
TableColumn col_2 = table.getColumnModel().getColumn(1);
col_2.setPreferredWidth(500);
table.setRowSelectionAllowed(true);
//table.setEnabled(false);
//table.setRowHeight(26);
//table.setFont(new Font(Font.SERIF,Font.PLAIN,14));

         
         try{
         Properties prop = System.getProperties();
         Enumeration names =prop.propertyNames();
       String[] properties = new String[2];
         while (names.hasMoreElements()){
             String name = (String)names.nextElement();
             properties[0] = name;
             if(name.equals("line.separator")){
         //        System.out.println("!");
                 String s ="";
                 if(prop.getProperty(name).equals("\n")){s+="\\n";}
                else if(prop.getProperty(name).equals("\n\r")){s+="\\n\\r";}
                else if(prop.getProperty(name).equals("\r")){s+="\\r";}
                 else{s+="CAN NOT ESCAPE";}

               properties[1] = s;
               tm.addRow(properties);
               continue;
             }
         properties[1]= prop.getProperty(name);
         tm.addRow(properties);
         }//while
         tm.fireTableDataChanged();
         }catch(SecurityException e){
         this.displayErrorMessage("Security Exception!");
         System.exit(1);
         }
         catch(ClassCastException e){
         this.displayErrorMessage("Class Cast Exception");
         System.exit(1);
         }
         catch(NoSuchElementException e){
         e.printStackTrace();
         }
    
this.propertiesScroller = new JScrollPane(this.table);
//this.propertiesScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
this.propertiesScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    
    }//initPropertiesTable
     
     //System Fonts
    
     private void initFontsTextArea(){
         DefaultStyledDocument doc = new DefaultStyledDocument();
         Font areaFont = new Font(Font.SERIF,Font.PLAIN,14);
       this.fontsTextArea = new JTextPane(doc);
       this.fontsTextArea.setSize(frameSize);
       this.fontsTextArea.setEditable(false);
       //this.fontsTextArea.setWrapStyleWord(false);
       this.fontsTextArea.setFont(areaFont);
       this.fontsTextArea.setText("System Fonts:\n");
       //get fonts and append
       
       
       SimpleAttributeSet atr = new SimpleAttributeSet();
       StyleConstants.setLineSpacing(atr, 1);
       StyleConstants.setFontSize(atr, 15);
              
     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
     Font[] fonts = ge.getAllFonts();
     for(Font font:fonts){
     String logicalName = font.getName();
     String familyName = font.getFamily();
     String fontFace = font.getFontName();
     String postscripName = font.getPSName();
     StyleConstants.setFontFamily(atr, logicalName);
     String str = "\nFont logical name is: "+logicalName+", font family is: "+
        familyName+", face name: " +fontFace+", postscript name: "+postscripName+".\n\n";
            try {
                doc.insertString(doc.getLength(), str, atr);
            } catch (BadLocationException ex) {
                Logger.getLogger(spFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
     }//for
       this.fontsScroller = new JScrollPane(this.fontsTextArea);
       this.fontsScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
       this.fontsScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
     }
     
     private void initFontPanel(){
     fontPanel = new JPanel();
     fontPanel.setLayout(new BorderLayout());
     this.initFontsTextArea();
    fontPanel.add(this.fontsScroller, BorderLayout.CENTER);
     
     }
     
    private void displayErrorMessage(String message){
    JOptionPane.showMessageDialog(null, message, "Error!", JOptionPane.ERROR_MESSAGE);
    
    }
}//class
