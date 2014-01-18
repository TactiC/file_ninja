package nl.ronalddehaan.filesystem;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FsManager
{
    private static boolean atRoot;
    private static Path currentPath;
    private static final Logger logger = Logger.getLogger(FsManager.class.getName());
    
    /*
     * Possible filters,
     * TODO: Make filters configurable
     */
    private static DirectoryStream.Filter<Path> noHiddenFilesFilter;
    private static DirectoryStream.Filter<Path> noDirectoryFilter;
    private static DirectoryStream.Filter<Path> onlyDirectoryFilter;
    private static DirectoryStream.Filter<Path> allFilesFilter;

    /* Active filter, defaults to noHiddenFiles */
    private static DirectoryStream.Filter<Path> activeFilter;
    
    
    static
    {
        /*
         * Do some initialization stuff,
         * Create filters, set default filter and current path.
         */

        atRoot = false;
        
        currentPath = Paths.get("/");
        
        noHiddenFilesFilter = new DirectoryStream.Filter<Path>()
        {
            @Override
            public boolean accept(Path entry) throws IOException
            {
                boolean result = true;
                if ((entry.getFileName().toString().equals("lost+found")) ||
                    (Files.isHidden(entry) == true))
                    result = false;
                
                return result;
            }
        };
        
        noDirectoryFilter = new DirectoryStream.Filter<Path>()
        {
            @Override
            public boolean accept(Path entry) throws IOException
            {
                return !Files.isDirectory(entry);
            }
        };

        onlyDirectoryFilter = new DirectoryStream.Filter<Path>()
        {
            @Override
            public boolean accept(Path entry) throws IOException
            {
                return Files.isDirectory(entry);
            }
        };

        allFilesFilter = new DirectoryStream.Filter<Path>()
        {
            @Override
            public boolean accept(Path entry) throws IOException
            {
                return true;
            }
        };
        
        /* Set active filter */
        activeFilter = noHiddenFilesFilter;
    }
    
   
    /**
     * This class is not supposed to be instantiated
     */
    private FsManager()
    {
        
    }
    
    /**
     * Reads all files and directories in a given location
     * and returns a list of filenames and/or directories based
     * on the currently set filter.
     * 
     * @param location The location to be read
     * @return a List of type string containing file/directory names.
     */
    public static List<String> readDirectory(String location, boolean resolveSibling)
    {
        logger.info("readDirectory");
        List<String> list = new ArrayList<>();
        
        if (resolveSibling)
        {
            logger.info("Resolve sibling directory");
            currentPath = currentPath.resolveSibling(location);
        }
        else
        {
            logger.info("Resolve child directory");
            currentPath = currentPath.resolve(location);
        }
        
        if (Files.isDirectory(currentPath))
        {
            try(DirectoryStream<Path> stream = Files.newDirectoryStream(
                    FileSystems.getDefault().getPath(currentPath.toString()), activeFilter))
            {
                for (Path p : stream)
                {
                    list.add(p.getFileName().toString());
                }
            } 
            catch (AccessDeniedException ade)
            {
                logger.info("This (" + currentPath.toString() + ") is not for your eyes...");
            }
            catch (IOException e)
            {
                //TODO: What should be done when this happens?
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 
     * @return
     */
    public static List<String> readParentDirectory()
    {

        List<String> list = new ArrayList<>();
        
        currentPath = currentPath.resolve("../").normalize();
       
        if (isRoot(currentPath) == false)
        {
            Path parent =  currentPath.resolve("../../").normalize();
            if (parent != null)
            {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(
                       FileSystems.getDefault().getPath(parent.toString()), activeFilter))
                {
                    for (Path p : stream)
                    {
                        list.add(p.getFileName().toString());
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            atRoot = true;
        }
        return list;
    }

    /**
     * 
     * @param location
     * @return
     */
//    public static List<String> readChildDirectory(String location)
//    {
//        List<String> list = new ArrayList<>();
//        
//        Path child = currentPath.resolve(location);
//        
//        if (Files.isDirectory(child))
//        {
//            try (DirectoryStream<Path> stream = Files.newDirectoryStream(
//                    FileSystems.getDefault().getPath(child.toString()), activeFilter))
//            {
//                for (Path p : stream)
//                {
//                    list.add(p.getFileName().toString());
//                }
//            } 
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//        return list;
//    }

    /**
     * 
     * @return
     */
    public static String getCurrentPath()
    {
        String result = null;
        if (currentPath.getParent() != null)
            result = currentPath.getParent().toString();
        else
            result = currentPath.toString();
        
        return result;
    }
    
    public static String getParentPath()
    {
        String result = null;
        int count = currentPath.getNameCount();
        result = currentPath.getName(count - 2).toString();
        return result;
    }
   
    /**
     * 
     * @param location
     */
    public static void setCurrentPath(String location)
    {
        currentPath = currentPath.resolve(location);
        logger.info("Current path set to: " + currentPath.toString());
    }
    
    /**
     * 
     * @param location
     * @return
     */
    public static boolean isdirectory(String location)
    {
        Path path = currentPath.resolve(location);
        return Files.isDirectory(path);
    }

    public static boolean atRoot()
    {
        atRoot = (currentPath.getRoot() == currentPath.getParent());
        return atRoot;
    }
    
    private static boolean isRoot(Path path)
    {
        atRoot = (path.getParent() == path.getRoot());
        logger.info("Root reached: " + atRoot);
        return atRoot;
    }
}
