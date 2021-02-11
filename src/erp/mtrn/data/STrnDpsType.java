/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

import erp.data.SDataConstantsSys;
import erp.lib.SLibUtilities;

/**
 *
 * @author Sergio Flores
 */
public final class STrnDpsType implements java.io.Serializable {
    
    private int mnDpsCategoryId;
    private int mnDpsClassId;
    private int mnDpsTypeId;
    
    public STrnDpsType(final int dpsCategoryId, final int dpsClassId, final int dpsTypeId) {
        mnDpsCategoryId = dpsCategoryId;
        mnDpsClassId = dpsClassId;
        mnDpsTypeId = dpsTypeId;
    }
    
    public boolean isForSales() {
        return mnDpsCategoryId == SDataConstantsSys.TRNS_CT_DPS_SAL;
    }
    
    public boolean isForPurchases() {
        return mnDpsCategoryId == SDataConstantsSys.TRNS_CT_DPS_PUR;
    }
    
    public boolean isDocument() {
        return isDocumentPur() || isDocumentSal();
    }

    public boolean isDocumentPur() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_PUR_DOC);
    }

    public boolean isDocumentSal() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_SAL_DOC);
    }

    public boolean isDocumentOrAdjustment() {
        return isDocument() || isAdjustment();
    }

    public boolean isDocumentOrAdjustmentPur() {
        return isDocumentPur() || isAdjustmentPur();
    }

    public boolean isDocumentOrAdjustmentSal() {
        return isDocumentSal() || isAdjustmentSal();
    }

    public boolean isAdjustment() {
        return isAdjustmentPur() || isAdjustmentSal();
    }

    public boolean isAdjustmentPur() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ);
    }

    public boolean isAdjustmentSal() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ);
    }

    public boolean isOrder() {
        return isOrderPur() || isOrderSal();
    }

    public boolean isOrderPur() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_PUR_ORD);
    }

    public boolean isOrderSal() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_SAL_ORD);
    }

    public boolean isEstimate() {
        return isEstimatePur() || isEstimateSal();
    }

    public boolean isEstimatePur() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_PUR_EST);
    }

    public boolean isEstimateSal() {
        return SLibUtilities.compareKeys(getDpsClassKey(), SDataConstantsSys.TRNS_CL_DPS_SAL_EST);
    }

    public boolean isDpsTypeEstimate() {
        return isDpsTypeEstimatePur()|| isDpsTypeEstimateSal();
    }

    public boolean isDpsTypeEstimatePur() {
        return SLibUtilities.compareKeys(getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_PUR_EST);
    }

    public boolean isDpsTypeEstimateSal() {
        return SLibUtilities.compareKeys(getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_EST);
    }

    public boolean isDpsTypeContract() {
        return isDpsTypeContractPur()|| isDpsTypeContractSal();
    }

    public boolean isDpsTypeContractPur() {
        return SLibUtilities.compareKeys(getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_PUR_CON);
    }

    public boolean isDpsTypeContractSal() {
        return SLibUtilities.compareKeys(getDpsTypeKey(), SDataConstantsSys.TRNU_TP_DPS_SAL_CON);
    }

    public int[] getDpsCategoryKey() { return new int[] { mnDpsCategoryId }; }
    public int[] getDpsClassKey() { return new int[] { mnDpsCategoryId, mnDpsClassId }; }
    public int[] getDpsTypeKey() { return new int[] { mnDpsCategoryId, mnDpsClassId, mnDpsTypeId }; }
}
