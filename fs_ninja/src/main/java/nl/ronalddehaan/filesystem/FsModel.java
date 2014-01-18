package nl.ronalddehaan.filesystem;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import nl.ronalddehaan.exceptions.UnknownTypeException;

/**
 * 
 * @author ronald
 *
 */
public class FsModel
{
    private static final Logger logger =
            Logger.getLogger(FsModel.class.getName());

    private int selectedIndex;
    
    private List<String> parentList;
    private List<String> currentList;
    private List<String> childList;
   
    
    public FsModel()
    {
        this.selectedIndex = 0;
        this.parentList = new ArrayList<String>();
        this.currentList = new ArrayList<String>();
        this.childList = new ArrayList<String>();
    }
    
   
    /**
     * 
     * @return
     * @throws UnknownTypeException
     */
    public UpdateModelMessage<String> initModel() throws UnknownTypeException
    {
        UpdateModelMessage<String> message = new UpdateModelMessage<>();

        fetchModel("/", ContentType.PARENT, false);
        fetchModel("home", ContentType.CURRENT, false);
        fetchModel(currentList.get(0), ContentType.CHILD, false);
        
        message.addList(this.parentList, ContentType.PARENT);
        message.addList(this.currentList, ContentType.CURRENT);
        message.addList(this.childList, ContentType.CHILD);

        return message;
    }

    /**
     * 
     * @param type
     * @return
     */
//    public UpdateModelMessage updateModel(ContentType type)
//    {
//        UpdateModelMessage<String> message = new UpdateModelMessage<>();
//        
//        return message;
//    }
   
    /**
     * 
     * @return
     * @throws UnknownTypeException
     */
    public UpdateModelMessage<String> getCurrentModelState() throws UnknownTypeException
    {
        UpdateModelMessage<String> message = new UpdateModelMessage<>();
        message.addList(this.childList, ContentType.CHILD);
        message.addList(this.currentList, ContentType.CURRENT);
        message.addList(this.parentList, ContentType.PARENT);
        message.setIndexInList(this.selectedIndex);

        return message;
    }

    /**
     * 
     */
    public void moveUp()
    {
        logger.info("Moving up");
        decrementIndex();
        updateChildList();
    }

    /**
     * 
     */
    public void moveDown()
    {
        logger.info("Moving down");
        incrementIndex();
        updateChildList();
    }

    /**
     * Moving towards the root of the tree
     */
    public void moveLeft()
    {
        if (FsManager.atRoot() == false)
        {
            String parentName = FsManager.getParentPath();
            String fetchThis = null;
            int index = 0;
            
            if (currentList.size() > 0)
                fetchThis = currentList.get(selectedIndex);
            
            /* Move content from current to child model */
            if (childList.size() > 0)
                childList.clear();
            
            for (String s : currentList)
                childList.add(s);
            
            /* Move content from parent to current model */
            if (currentList.size() > 0)
                currentList.clear();
            
            for (String s : parentList)
                currentList.add(s);
            
            // Find index to set as selected
            index = findInList(parentList, parentName);
            
            /* Fetch new content for parent list */
            if (parentList.size() > 0)
                parentList.clear();
            
            if (selectedIndex >= 0)
            {
                parentList = FsManager.readParentDirectory();
                selectedIndex = index;
            }
        }
    }
    
   
    /**
     * Moving towards the leafs of the tree
     */
    public void moveRight()
    {
        String childToFetch = null;
        String currentToFetch = null;
        
        if ((currentList.size() > 0) && (selectedIndex != -1))
            currentToFetch = currentList.get(selectedIndex);
        
        if (childList.size() > 0)
        {
            childToFetch = childList.get(0);
            
            /* Move content from current to parent list */
            if (parentList.size() > 0)
                parentList.clear();
            
            for (String s : currentList)
                parentList.add(s);
//            parentList = currentList;
            
            /* Move content from child to current list */
            if (currentList.size() > 0)
                currentList.clear();
            
            for (String s : childList)
                currentList.add(s);
//            currentList = childList;
            
            if (childList.size() > 0)
                childList.clear();
            
            if ((selectedIndex >= 0) && (childToFetch != null))
            {
                fetchModel(childToFetch, ContentType.CHILD, false);
                selectedIndex = 0;
            }
        }
    }
    
    private void incrementIndex()
    {
        
        if (this.selectedIndex < this.currentList.size() - 1)
        {
            this.selectedIndex++;
            logger.info("decremented to " + this.selectedIndex);
        }
    }
    
    private void decrementIndex()
    {
        if (this.selectedIndex > 0)
        {
            this.selectedIndex--;
            logger.info("decremented to " + this.selectedIndex);
        }
    }
   
    private void updateChildList()
    {
         if (childList.size() > 0)
            childList.clear();
        
        if (this.selectedIndex >= 0)
            fetchModel(currentList.get(selectedIndex),
                       ContentType.CHILD,
                       true);
    }

    private void fetchModel(String directory, ContentType type, boolean resolveSibling)
    {
        logger.info("Fetch model for " + directory);
        switch (type)
        {
        case PARENT: 
            this.parentList = FsManager.readDirectory(directory, resolveSibling);
            break;
        case CURRENT:
            this.currentList = FsManager.readDirectory(directory, resolveSibling);
            break;
        case CHILD:
            this.childList = FsManager.readDirectory(directory, resolveSibling);
            break;
        }
        logger.info("Full path of fetched dir: " + FsManager.getCurrentPath());
    }
    
    
    private int findInList(List<?> list, String name)
    {
        int result = -1;
        int i = 0;
        for (Object t : list)
        {
            if (((String)t).equals(name))
                result = i;
            i++;
        }
        return result;
    }
}
