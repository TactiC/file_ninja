package nl.ronalddehaan;

import java.util.logging.Logger;

import nl.ronalddehaan.controllers.MainController;
import nl.ronalddehaan.exceptions.ConfigurationException;
import nl.ronalddehaan.exceptions.UnknownTypeException;
import nl.ronalddehaan.filesystem.FsModel;
import nl.ronalddehaan.gui.MainWindow;

/**
 *
 */
public class App 
{
    private static final Logger logger = Logger.getLogger(App.class.getName());
    
    public static void main( String[] args )
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                MainWindow window = new MainWindow();
                MainController controller = new MainController(window);
                controller.addModel(new FsModel());

                window.createAndShowGui();
//                window.init();

                window.addController(controller);
                try
                {
                    controller.initController();
                }
                /* Unrecoverable, fix this... */
                catch (ConfigurationException | UnknownTypeException e)
                {
                    e.printStackTrace();
                    System.exit(-1);
                }
                window.showWindow();
            }
        });
    }
}
