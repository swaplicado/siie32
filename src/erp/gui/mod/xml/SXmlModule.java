package erp.gui.mod.xml;

import erp.gui.mod.SBase;
import java.util.ArrayList;

/**
 * Represents element <code>module</code> of XML GUI configuration file.
 *
 * @author Sergio Flores
 */
public class SXmlModule extends SBase {

    protected ArrayList<SXmlMenu> maChildMenus;

    public SXmlModule() {
        this("");
    }

    public SXmlModule(String identifier) {
        super(identifier);
        maChildMenus = new ArrayList<>();
    }

    /*
     * Public setter/getter methods:
     */

    public ArrayList<SXmlMenu> getChildMenus() {
        return maChildMenus;
    }
    
    /*
     * Public methods:
     */
    
    public SXmlMenu getChildMenu(String identifier) {
        SXmlMenu item = null;
        
        for (SXmlMenu child : maChildMenus) {
            if (child.getIdentifier().compareTo(identifier) == 0) {
                item = child;
                break;
            }
        }
        
        return item;
    }
}
