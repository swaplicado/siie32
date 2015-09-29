package erp.gui.mod.xml;

import erp.gui.mod.SBase;
import java.util.ArrayList;

/**
 * Represents element <code>section</code> of XML GUI configuration file.
 *
 * @author Sergio Flores
 */
public class SXmlMenuSection extends SBase {

    protected ArrayList<SXmlMenuSectionItem> maChildItems;

    public SXmlMenuSection() {
        this("");
    }

    public SXmlMenuSection(String identifier) {
        super(identifier);
        maChildItems = new ArrayList<>();
    }

    /*
     * Public setter/getter methods:
     */

    public ArrayList<SXmlMenuSectionItem> getChildItems() {
        return maChildItems;
    }
    
    /*
     * Public methods:
     */
    
    public SXmlMenuSectionItem getChildItem(String identifier) {
        SXmlMenuSectionItem item = null;
        
        for (SXmlMenuSectionItem child : maChildItems) {
            if (child.getIdentifier().compareTo(identifier) == 0) {
                item = child;
                break;
            }
        }
        
        return item;
    }
}
