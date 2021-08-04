package erp.gui.mod.cfg;

import erp.gui.mod.SBaseCfg;
import javax.swing.JSeparator;

/**
 *
 * @author Sergio Flores
 */
public class SCfgMenuSectionSeparator extends SBaseCfg {

    protected JSeparator moSeparator;

    /**
     *
     * @param separator
     * @param identifier
     */
    public SCfgMenuSectionSeparator(JSeparator separator) {
        this(separator, true);
    }
    
    /**
     *
     * @param separator
     * @param identifier
     * @param visible
     */
    public SCfgMenuSectionSeparator(JSeparator separator, boolean visible) {
        super("", visible);
        moSeparator = separator;
    }

    /*
     * Public setter/getter methods:
     */

    public void setSeparator(JSeparator o) {
        moSeparator = o;
    }

    public JSeparator getSeparator() {
        return moSeparator;
    }
    
    /*
     * Overriden methods:
     */

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        
        moSeparator.setVisible(visible);
    }
}
