package nl.ronalddehaan.listeners;

import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ContentSelectionListener
            implements ListSelectionListener
{
    private static final Logger logger = 
            Logger.getLogger(ContentSelectionListener.class.getName());

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
}
