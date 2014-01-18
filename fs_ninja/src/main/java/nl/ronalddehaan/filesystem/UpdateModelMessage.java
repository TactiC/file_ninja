package nl.ronalddehaan.filesystem;

import java.util.List;

import nl.ronalddehaan.exceptions.UnknownTypeException;

public class UpdateModelMessage<T>
{
    private int indexInList;
    private List<T> parentList;
    private List<T> currentList;
    private List<T> childList;
    
    //TODO: This thing screams for a builder pattern...
    public UpdateModelMessage()
    {
        this.indexInList = 0;
        this.parentList = null;
        this.currentList = null;
        this.childList = null;
    }
    
    
    
    public void setIndexInList(int index)
    {
        this.indexInList = index;
    }
    
    public int getIndexInList()
    {
        return this.indexInList;
    }
    
    public void addList(List<T> list, ContentType type) throws UnknownTypeException
    {
        switch (type)
        {
            case PARENT:
                this.parentList = list;
                break;
            case CURRENT:
                this.currentList = list;
                break;
            case CHILD:
                this.childList = list;
                break;
            default: 
                throw new UnknownTypeException("Unknown ContentType enum value...");
        }
    }
    
    public List<T> getList(ContentType type)
    {
        List<T> list = null;
        switch (type)
        {
            case PARENT:
                list = this.parentList;
                break;
            case CURRENT:
                list = this.currentList;
                break;
            case CHILD:
                list = this.childList;
                break;
        }
        
        return list;
    }
}
