package erp.gui.mod;

/**
 *
 * @author Sergio Flores
 */
public abstract class SBaseCfg extends SBase {

    protected boolean mbVisible;

    public SBaseCfg(String identifier, boolean visible) {
        super(identifier);
        mbVisible = visible;
    }

    public void setVisible(boolean b) {
        mbVisible = b;
    }

    public boolean isVisible() {
        return mbVisible;
    }
}
