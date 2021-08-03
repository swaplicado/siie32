package erp.gui.mod.cfg;

import erp.gui.mod.SBaseCfg;
import java.util.ArrayList;
import javax.swing.JMenu;

/**
 *
 * @author Sergio Flores
 */
public class SCfgMenu extends SBaseCfg {

    protected JMenu moMenu;
    protected ArrayList<SCfgMenuSection> maChildSections;

    /**
     *
     * @param menu
     * @param identifier
     */
    public SCfgMenu(JMenu menu, String identifier) {
        this(menu, identifier, true);
    }
    
    /**
     *
     * @param menu
     * @param identifier
     * @param visible
     */
    public SCfgMenu(JMenu menu, String identifier, boolean visible) {
        super(identifier, visible);
        moMenu = menu;
        maChildSections = new ArrayList<>();
    }

    /*
     * Public setter/getter methods:
     */

    public void setMenu(JMenu o) {
        moMenu = o;
    }

    public JMenu getMenu() {
        return moMenu;
    }

    public ArrayList<SCfgMenuSection> getChildSections() {
        return maChildSections;
    }
    
    /*
     * Overriden methods:
     */

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        
        moMenu.setVisible(visible);
        for (SCfgMenuSection section : maChildSections) {
            section.setVisible(visible);
        }
    }
}
