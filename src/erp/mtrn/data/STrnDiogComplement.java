/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import java.io.Serializable;
import sa.lib.SLibConsts;

/**
 *
 * @author Sergio Flores
 */
public class STrnDiogComplement implements Serializable {

    protected int[] manDiogTypeKey;
    protected int[] manProdOrderKey;
    protected SDataDps moDpsSource;
    
    protected int mnMaintMovementType;  // Constants in SModSysConsts.TRNS_TP_MAINT_MOV_...
    protected int mnMaintUserType;      // Constants in SModSysConsts.TXT_TRNX_TP_MAINT_USER_...

    public STrnDiogComplement() {
        this(null, null, null);
    }

    public STrnDiogComplement(int[] diogTypeKey) {
        this(diogTypeKey, null, null);
    }

    public STrnDiogComplement(int[] diogTypeKey, int[] prodOrderKey) {
        this(diogTypeKey, prodOrderKey, null);
    }

    public STrnDiogComplement(int[] diogTypeKey, SDataDps dpsSource) {
        this(diogTypeKey, null, dpsSource);
    }

    private STrnDiogComplement(int[] diogTypeKey, int[] prodOrderKey, SDataDps dpsSource) {
        manDiogTypeKey = diogTypeKey;
        manProdOrderKey = prodOrderKey;
        moDpsSource = dpsSource;
        
        mnMaintMovementType = SLibConsts.UNDEFINED;
        mnMaintUserType = SLibConsts.UNDEFINED;
    }

    /**
     * Sets type of maintenance movement.
     * @param type Constants in <code>SModSysConsts.TRNS_TP_MAINT_MOV_...</code>
     */
    public void setMaintMovementType(int type) { mnMaintMovementType = type; }
    
    /**
     * Sets type of maintenance user.
     * @param type Constants in <code>SModSysConsts.TXT_TRNX_TP_MAINT_USER_...</code>
     */
    public void setMaintUserType(int type) { mnMaintUserType = type; }
    
    public int[] getDiogTypeKey() { return manDiogTypeKey; }
    public int[] getProdOrderKey() { return manProdOrderKey; }
    public SDataDps getDpsSource() { return moDpsSource; }
    
    public int getMaintMovementType() { return mnMaintMovementType; }
    public int getMaintUserType() { return mnMaintUserType; }
}
