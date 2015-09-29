package erp.gui.mod.cfg;

import erp.gui.mod.SBase;
import java.util.ArrayList;

/**
 *
 * @author Sergio Flores
 */
public class SCfgModule extends SBase {

    protected ArrayList<SCfgMenu> maChildMenus;

    public SCfgModule(String identifier) {
        super(identifier);
        maChildMenus = new ArrayList<>();
    }

    /*
     * Public setter/getter methods:
     */

    public ArrayList<SCfgMenu> getChildMenus() {
        return maChildMenus;
    }
    
    /*
     * Public methods:
     */

    public void setVisible(boolean visible) {
        for (SCfgMenu menu : maChildMenus) {
            menu.setVisible(visible);
        }
    }
}
