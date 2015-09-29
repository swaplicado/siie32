package erp.gui.mod.cfg;

import erp.gui.mod.SBaseCfg;
import java.util.ArrayList;

/**
 *
 * @author Sergio Flores
 */
public class SCfgMenuSection extends SBaseCfg {

    protected SCfgMenuSectionSeparator moChildSeparator;
    protected ArrayList<SCfgMenuSectionItem> maChildItems;

    /**
     *
     * @param identifier
     */
    public SCfgMenuSection(String identifier) {
        this(identifier, true);
    }
    
    /**
     *
     * @param identifier
     * @param visible
     */
    public SCfgMenuSection(String identifier, boolean visible) {
        super(identifier, visible);
        moChildSeparator = null;
        maChildItems = new ArrayList<>();
    }

    /*
     * Public setter/getter methods:
     */

    public void setChildSeparator(SCfgMenuSectionSeparator o) {
        moChildSeparator = o;
    }

    public SCfgMenuSectionSeparator getChildSeparator() {
        return moChildSeparator;
    }

    public ArrayList<SCfgMenuSectionItem> getChildItems() {
        return maChildItems;
    }
    
    /*
     * Public methods:
     */

    public void checkSeparatorVisibility() {
        boolean visibleItems = false;
        
        if (moChildSeparator != null) {
            for (SCfgMenuSectionItem item : maChildItems) {
                if (item.isVisible()) {
                    visibleItems = true;
                    break;
                }
            }
            
            moChildSeparator.setVisible(mbVisible && visibleItems);
        }
    }
    
    /*
     * Overriden methods:
     */

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        
        for (SCfgMenuSectionItem item : maChildItems) {
            item.setVisible(visible);
        }
        checkSeparatorVisibility();
    }
}
