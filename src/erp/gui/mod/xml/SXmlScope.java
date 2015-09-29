package erp.gui.mod.xml;

import java.util.ArrayList;

/**
 * Represents element <code>scope</code> of XML GUI configuration file.
 *
 * @author Sergio Flores
 */
public class SXmlScope {

    protected int mnAmbit;
    protected int mnReference;
    protected ArrayList<SXmlModule> maChildModules;

    public SXmlScope(int ambit, int reference) {
        mnAmbit = ambit;
        mnReference = reference;
        maChildModules = new ArrayList<>();
    }

    /*
     * Public setter/getter methods:
     */

    public void setAmbit(int n) {
        mnAmbit = n;
    }

    public void setReference(int n) {
        mnReference = n;
    }

    public int getAmbit() {
        return mnAmbit;
    }

    public int getReference() {
        return mnReference;
    }

    public ArrayList<SXmlModule> getChildModules() {
        return maChildModules;
    }
    
    /*
     * Public methods:
     */
    
    public SXmlModule getChildModule(String identifier) {
        SXmlModule item = null;
        
        for (SXmlModule child : maChildModules) {
            if (child.getIdentifier().compareTo(identifier) == 0) {
                item = child;
                break;
            }
        }
        
        return item;
    }
}
