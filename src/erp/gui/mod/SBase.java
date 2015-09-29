package erp.gui.mod;

/**
 *
 * @author Sergio Flores
 */
public abstract class SBase {

    protected String msIdentifier;

    public SBase() {
        this("");
    }

    public SBase(String identifier) {
        msIdentifier = identifier;
    }

    public void setIdentifier(String s) {
        msIdentifier = s;
    }

    public String getIdentifier() {
        return msIdentifier;
    }
}
