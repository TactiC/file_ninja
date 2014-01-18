package nl.ronalddehaan.controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nl.ronalddehaan.exceptions.ConfigurationException;
import nl.ronalddehaan.exceptions.UnknownTypeException;
import nl.ronalddehaan.filesystem.FsModel;
import nl.ronalddehaan.filesystem.UpdateModelMessage;
import nl.ronalddehaan.gui.MainWindow;

public class MainController implements ListSelectionListener,
                                       MouseListener,
                                       KeyListener
{
    private static final Logger logger = Logger.getLogger(MainController.class.getName());
    
    private MainWindow mainWindow;
    
    /*
     * Takes care of all key press events.
     */
    private KeyEventHandler keyHandler;
    
    /*
     * 
     */
    private FsModel model;
    
    /*
     * 
     */
//    private ListSelectionListener listSelectionListener;
    
    public MainController(MainWindow mainWindow)
    {
        this.mainWindow = mainWindow;
        this.keyHandler = new KeyEventHandler();
    }
    
    
    public void initController() throws ConfigurationException,    
                                        UnknownTypeException
    {
        if (this.model == null)
            throw new ConfigurationException("No model configured!");
        
        // fetch model(s)
        UpdateModelMessage message = model.initModel();
        
        // update view based on model
        mainWindow.updateView(message);
        
    }
    
    /****************************************************
    /* Getter and setter methods                        *
     ****************************************************/
    
    public void addModel(FsModel model)
    {
        this.model = model;
    }

    /****************************************************
    /* ListSelection Listener                           *
     ****************************************************/

    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        if (e != null && 
            e.getValueIsAdjusting() == false)
        {
            logger.info("valueChanged event triggered");
            Object obj = e.getSource();
            if (obj instanceof JList)
            {
                @SuppressWarnings("unchecked")
                JList<String> list = (JList<String>)obj;
                if (list != null)
                {
                    int[] indices = list.getSelectedIndices();
                    
                    if (indices.length > 0)
                    {
                        DefaultListModel<String> model = 
                                (DefaultListModel<String>)list.getModel();
                    }
                }
            }
        }
    }
    
    
    
    /****************************************************
    /* Key Listener                                     *
     ****************************************************/

    @Override
    public void keyTyped(KeyEvent e)
    {
        logger.info("Hey, a key typed...");
        try
        {
            UpdateModelMessage message = keyHandler.handleEvent(e, model);
            mainWindow.updateView(message);
        } 
        catch (UnknownTypeException e1)
        {
            e1.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    
    
    /****************************************************
    /* Mouse Listener                                   *
     ****************************************************/

    @Override
    public void mouseClicked(MouseEvent e)
    {
//        mainWindow.updateChildModel();
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        // TODO Auto-generated method stub
        
    }
}
