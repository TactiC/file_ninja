package nl.ronalddehaan.controllers;

import java.awt.event.KeyEvent;
import java.util.logging.Logger;

import nl.ronalddehaan.exceptions.UnknownTypeException;
import nl.ronalddehaan.filesystem.FsModel;
import nl.ronalddehaan.filesystem.UpdateModelMessage;
import nl.ronalddehaan.gui.MainWindow;

public class KeyEventHandler
{
    private static final Logger logger = Logger.getLogger(KeyEventHandler.class.getName());
   
    /* When console mode is true commands can be entered.
     * Console mode gets active (by default) via ':'
     */
    private boolean consoleMode = false;
    
    
    public UpdateModelMessage handleEvent(KeyEvent e, FsModel model) throws UnknownTypeException
    {
        logger.info("keyTyped event received");
        
        if (e.getKeyChar() == 'j') // move down
        {
            model.moveDown();
        }
        else if (e.getKeyChar() == 'k') // move up
        {
            model.moveUp();
        }
        else if (e.getKeyChar() == 'h') // move left (towards the root)
        {
            model.moveLeft();
        }
        else if (e.getKeyChar() == 'l') // move right (away from the root)
        {
            model.moveRight();
        }
        
        else if (e.getKeyChar() == ':')
        {
            this.consoleMode = true;
//            mainWindow.openConsole();
        }
        
        else if (e.getKeyChar() == KeyEvent.VK_ENTER)
        {
            // Get content from console
            // Process
            // Execute command
            // Leave console mode
            this.consoleMode = false;
        }

        else if (e.getKeyChar() == KeyEvent.VK_ESCAPE)
        {
//            mainWindow.closeConsole();
            // Leave console mode
            this.consoleMode = false;
        }
        
//        mainWindow.updateLabel();
        return model.getCurrentModelState();
    }

}
