package nl.ronalddehaan;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class FsLogger
{
    private static String FILE_NAME = "";
    private static int LOG_SIZE = 10;
    private static int ROTATION_COUNT = 10;
    
    private static Handler handler;
    private static Logger logger;
    
    static
    {
        try
        {
            handler = new FileHandler(FILE_NAME, LOG_SIZE, ROTATION_COUNT);
            Logger.getLogger("").addHandler(handler);

        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
