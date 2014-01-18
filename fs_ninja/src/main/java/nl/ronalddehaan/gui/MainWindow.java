package nl.ronalddehaan.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import nl.ronalddehaan.App;
import nl.ronalddehaan.controllers.MainController;
import nl.ronalddehaan.exceptions.UnknownTypeException;
import nl.ronalddehaan.filesystem.ContentType;
import nl.ronalddehaan.filesystem.FsManager;
import nl.ronalddehaan.filesystem.UpdateModelMessage;

public class MainWindow extends JFrame 
{
   
    private static final Logger logger = Logger.getLogger(App.class.getName());

    /**
     * 
     */
    private static final long serialVersionUID  = 1L;
    private static final int WIDTH              = 600;
    private static final int HEIGHT             = 500;
    
//    private int selectedIndex;
    private JPanel commandPanel;
    private JTextField commandLine;

    private JPanel listPanel;
    private JScrollPane sp_parent, sp_current, sp_child;
    private JList<String> parent_list, current_list, child_list;
    private DefaultListModel<String> parent_model, current_model, child_model;

    private JPanel statusPanel;
    private JLabel lblPath;
    
    
    public MainWindow()
    {
        createAndShowGui();
    }
    
    public void createAndShowGui()
    {
        logger.info("Creating gui");
        setTitle("Filesystem Ninja");
        setSize(WIDTH,  HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        listPanel = new JPanel();
        getContentPane().add(listPanel, BorderLayout.CENTER);
        listPanel.setLayout(new GridLayout(0, 3, 0, 0));
        
        /* setup parent list */
        sp_parent = new JScrollPane();
        sp_parent.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        sp_parent.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp_parent.setEnabled(false);
        listPanel.add(sp_parent);
        
        parent_model = new DefaultListModel<String>();
        
        parent_list = new JList<String>(parent_model);
        parent_list.setEnabled(false);
        parent_list.setBorder(null);
        sp_parent.setViewportView(parent_list);
        

        /* setup current list */
        sp_current = new JScrollPane();
        sp_current.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        sp_current.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        listPanel.add(sp_current);
        
        current_model = new DefaultListModel<String>();
        
        current_list = new JList<String>(current_model);
        current_list.setBorder(null);
        sp_current.setViewportView(current_list);
       

        /* setup child list */
        sp_child = new JScrollPane();
        sp_child.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        sp_child.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp_child.setEnabled(false);
        listPanel.add(sp_child);
        
        child_model = new DefaultListModel<String>();
        
        child_list = new JList<String>(child_model);
        child_list.setBorder(null);
        child_list.setEnabled(false);
        sp_child.setViewportView(child_list);
        
        statusPanel = new JPanel();
        getContentPane().add(statusPanel, BorderLayout.NORTH);
        
        lblPath = new JLabel("...");
        statusPanel.add(lblPath);
        
        /* Create command line panel */
        commandPanel = new JPanel();
        getContentPane().add(commandPanel, BorderLayout.SOUTH);
        
        commandLine = new JTextField();
        commandLine.setBackground(new Color(0, 0, 0));
        commandLine.setForeground(new Color(0, 128, 0));
        commandPanel.add(commandLine);
        commandLine.setColumns(50);
        commandPanel.setVisible(false);
        
    }
   
    public void updateView(UpdateModelMessage message) throws UnknownTypeException
    {
        for (ContentType type : ContentType.values())
        {
            updateModel(message.getList(type), type);
        }
        
        this.current_list.setSelectedIndex(
                                message.getIndexInList());
        
        this.lblPath.setText(FsManager.getCurrentPath());
    }
    
    
    
    /**
     * 
     * @param controller
     */
    public void addController(MainController controller)
    {
        logger.info("Attach listeners");
        current_list.addListSelectionListener(controller);
        current_list.addKeyListener(controller);
        current_list.addMouseListener(controller);
        commandLine.addKeyListener(controller);
    }

    /**
     * 
     */
    public void showWindow()
    {
        this.setVisible(true);
    }

   
    /**
     * 
     * @param list
     * @param type
     * @throws UnknownTypeException
     */
    private <T> void updateModel(List<T> list, ContentType type) throws UnknownTypeException
    {
        DefaultListModel<String> model;
        switch(type)
        {
        case PARENT:
            if (this.parent_model.isEmpty() == false)
                this.parent_model.clear();
            for (T t : list)
                this.parent_model.addElement( (String)t );
            break;
        case CURRENT:
            if (this.current_model.isEmpty() == false)
                this.current_model.clear();
            for (T t : list)
                this.current_model.addElement( (String)t );
            break;
        case CHILD:
            if (this.child_model.isEmpty() == false)
                this.child_model.clear();
            for (T t : list)
                this.child_model.addElement( (String)t );
            break;
        default: throw new UnknownTypeException("Unknown ContentType!");
        }
        
        logger.info("Updating model: " + type.name());
    }

    //TODO: Remove (move) below this...
    public void openConsole()
    {
        commandPanel.setVisible(true);
        commandLine.requestFocusInWindow();
    }
    
    public void closeConsole()
    {
        logger.info("Closing console");
        commandLine.setText("");
        commandPanel.setVisible(false);
        current_list.requestFocusInWindow();
    }
    
    public void updateLabel()
    {
        logger.info("Updating current directory label");
        this.lblPath.setText(FsManager.getCurrentPath());
    }
}
