/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.session;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Sergio Flores
 */
public class SSessionTaxItemLink implements Serializable {

    private int mnPkLinkId;
    private int mnPkReferenceId;
    private Date mtDateStart;
    private int mnFkTaxRegionId;
    private int mnFkTaxGroupId;

    public SSessionTaxItemLink(int idLink, int idReference) {
        mnPkLinkId = idLink;
        mnPkReferenceId = idReference;
    }

    public void setPkLinkId(int n) { mnPkLinkId = n; }
    public void setPkReferenceId(int n) { mnPkReferenceId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setFkTaxRegionId(int n) { mnFkTaxRegionId = n; }
    public void setFkTaxGroupId(int n) { mnFkTaxGroupId = n; }

    public int getPkLinkId() { return mnPkLinkId; }
    public int getPkReferenceId() { return mnPkReferenceId; }
    public Date getDateStart() { return mtDateStart; }
    public int getFkTaxRegionId() { return mnFkTaxRegionId; }
    public int getFkTaxGroupId() { return mnFkTaxGroupId; }
}
