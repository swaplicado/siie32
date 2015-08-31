/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.session;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Sergio Flores
 */
public class SSessionTaxGroup implements Serializable {

    private int mnPkTaxGroupId;
    private ArrayList<SSessionTaxGroupEntry> maEntries;

    public SSessionTaxGroup(int idTaxGroup) {
        mnPkTaxGroupId = idTaxGroup;
        maEntries = new ArrayList<SSessionTaxGroupEntry>();
    }

    public void setPkTaxGroupId(int n) { mnPkTaxGroupId = n; }

    public int getPkTaxGroupId() { return mnPkTaxGroupId; }
    public ArrayList<SSessionTaxGroupEntry> getEntries() { return maEntries; }
}
