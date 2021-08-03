package erp.gui.mod.cfg;

import erp.gui.mod.SBaseCfg;
import javax.swing.JMenuItem;

/**
 *
 * @author Sergio Flores
 */
public class SCfgMenuSectionItem extends SBaseCfg {

    protected JMenuItem moMenuItem;

    /**
     *
     * @param menuItem
     * @param identifier
     */
    public SCfgMenuSectionItem(JMenuItem menuItem, String identifier) {
        this(menuItem, identifier, true);
    }
    
    /**
     *
     * @param menuItem
     * @param identifier
     * @param visible
     */
    public SCfgMenuSectionItem(JMenuItem menuItem, String identifier, boolean visible) {
        super(identifier, visible);
        moMenuItem = menuItem;
    }

    /*
     * Public setter/getter methods:
     */

    public void setMenuItem(JMenuItem o) {
        moMenuItem = o;
    }

    public JMenuItem getMenuItem() {
        return moMenuItem;
    }
    
    /*
     * Overriden methods:
     */

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        
        moMenuItem.setVisible(visible);
    }
}
