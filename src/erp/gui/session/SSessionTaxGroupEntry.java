/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.session;

import java.io.Serializable;
import sa.lib.SLibKey;

/**
 *
 * @author Sergio Flores
 */
public class SSessionTaxGroupEntry implements Serializable {

    protected int mnPkTaxIdentityEmisorTypeId;
    protected int mnPkTaxIdentityReceptorTypeId;
    protected int mnApplicationOrder;
    protected java.util.Date mtDateStart;
    protected java.util.Date mtDateEnd_n;
    protected int mnFkTaxBasicId;
    protected int mnFkTaxId;

    public SSessionTaxGroupEntry(int idTaxIdentityEmisorType, int idTaxIdentityReceptorType) {
        mnPkTaxIdentityEmisorTypeId = idTaxIdentityEmisorType;
        mnPkTaxIdentityReceptorTypeId = idTaxIdentityReceptorType;
    }

    public void setPkTaxIdentityEmisorTypeId(int n) { mnPkTaxIdentityEmisorTypeId = n; }
    public void setPkTaxIdentityReceptorTypeId(int n) { mnPkTaxIdentityReceptorTypeId = n; }
    public void setApplicationOrder(int n) { mnApplicationOrder = n; }
    public void setDateStart(java.util.Date t) { mtDateStart = t; }
    public void setDateEnd_n(java.util.Date t) { mtDateEnd_n = t; }
    public void setFkTaxBasicId(int n) { mnFkTaxBasicId = n; }
    public void setFkTaxId(int n) { mnFkTaxId = n; }

    public int getPkTaxIdentityEmisorTypeId() { return mnPkTaxIdentityEmisorTypeId; }
    public int getPkTaxIdentityReceptorTypeId() { return mnPkTaxIdentityReceptorTypeId; }
    public int getApplicationOrder() { return mnApplicationOrder; }
    public java.util.Date getDateStart() { return mtDateStart; }
    public java.util.Date getDateEnd_n() { return mtDateEnd_n; }
    public int getFkTaxBasicId() { return mnFkTaxBasicId; }
    public int getFkTaxId() { return mnFkTaxId; }

    public SLibKey getTaxKey() { return new SLibKey(new int[] { mnFkTaxBasicId, mnFkTaxId }); }
}
