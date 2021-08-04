package erp.gui.mod.xml;

import erp.gui.mod.SBase;
import java.util.ArrayList;

/**
 * Represents element <code>menu</code> of XML GUI configuration file.
 * 
 * @author Sergio Flores
 */
public class SXmlMenu extends SBase {

    protected ArrayList<SXmlMenuSection> maChildSections;

    public SXmlMenu() {
        this("");
    }

    public SXmlMenu(String identifier) {
        super(identifier);
        maChildSections = new ArrayList<>();
    }

    /*
     * Public setter/getter methods:
     */

    public ArrayList<SXmlMenuSection> getChildSections() {
        return maChildSections;
    }
    
    /*
     * Public methods:
     */
    
    public SXmlMenuSection getChildSection(String identifier) {
        SXmlMenuSection item = null;
        
        for (SXmlMenuSection child : maChildSections) {
            if (child.getIdentifier().compareTo(identifier) == 0) {
                item = child;
                break;
            }
        }
        
        return item;
    }
}
