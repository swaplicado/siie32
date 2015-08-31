/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.mtrn.data.SDataDsmEntry;

import erp.data.SDataConstantsSys;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataDsmEntryRow extends erp.lib.table.STableRow {

    public SDataDsmEntryRow(java.lang.Object data){
        moData = data;
        prepareTableRow();
    }

    private java.lang.String renderReferenceSourceDescription(erp.mtrn.data.SDataDsmEntry data) {
        String sRef = "-";

        int[] nDocType = new int[] { data.getFkAccountingMoveTypeId(), data.getFkAccountingMoveClassId(), data.getFkAccountingMoveSubclassId() };

        if (SLibUtilities.compareKeys(nDocType, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_APP) ||
                SLibUtilities.compareKeys(nDocType, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_TRA) ||
                SLibUtilities.compareKeys(nDocType, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_CLO)) {
            if (data.getSourceReference().length() <= 0) {
                sRef = SDataConstantsSys.REF_BLANK;
            }
            else {
                sRef = data.getSourceReference();
            }
        }
        
        return sRef;
    }

    private java.lang.String renderReferenceDestinyDescription(erp.mtrn.data.SDataDsmEntry data) {
        String sRef = "-";

        int[] nDocType = new int[] { data.getFkAccountingMoveTypeId(), data.getFkAccountingMoveClassId(), data.getFkAccountingMoveSubclassId() };

        if (SLibUtilities.compareKeys(nDocType, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_TRA) ||
                SLibUtilities.compareKeys(nDocType, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_PAY_OPE) ||
                SLibUtilities.compareKeys(nDocType, SDataConstantsSys.FINS_CLS_ACC_MOV_SUBSYS_BAL_APP)) {
            if (data.getDestinyReference().length() <= 0) {
                sRef = SDataConstantsSys.REF_BLANK;
            }
            else {
                sRef = data.getDestinyReference();
            }
        }

        return sRef;
    }

    @Override
    public void prepareTableRow() {
        SDataDsmEntry data = (SDataDsmEntry) moData;

        mvValues.clear();
        mvValues.add(data.getDbmsBiz().toString().length() > 0 ? data.getDbmsBiz() : "-");
        mvValues.add(data.getDbmsSubclassMove().toString().length() > 0 ? data.getDbmsSubclassMove() : "-");
        mvValues.add(renderReferenceSourceDescription(data));
        mvValues.add(data.getFkSourceAccountId_n().toString().length() > 0 ? data.getFkSourceAccountId_n() : "-");
        mvValues.add(data.getDbmsSourceDps().toString().length() > 0 ? data.getDbmsSourceDps() : "-");
        mvValues.add(data.getSourceValue());
        mvValues.add(data.getSourceExchangeRate());
        mvValues.add(data.getSourceValueCy());
        mvValues.add(data.getDbmsSourceCurrencyKey().toString().length() > 0 ? data.getDbmsSourceCurrencyKey() : "-");
        mvValues.add(renderReferenceDestinyDescription(data));
        mvValues.add(data.getFkDestinyAccountId_n().toString().length() > 0 ? data.getFkDestinyAccountId_n() : "-");
        mvValues.add(data.getDbmsDestinyDps().toString().length() > 0 ? data.getDbmsDestinyDps() : "-");
        mvValues.add(data.getDestinyValue());
        mvValues.add(data.getDestinyExchangeRate());
        mvValues.add(data.getDestinyValueCy());
        mvValues.add(data.getDbmsDestinyCurrencyKey().toString().length() > 0 ? data.getDbmsDestinyCurrencyKey() : "-");
        mvValues.add(data.getIsDeleted());
        mvValues.add(data.getDbmsUserNew());
        mvValues.add(data.getUserNewTs());
        mvValues.add(data.getDbmsUserEdit());
        mvValues.add(data.getUserEditTs());
        mvValues.add(data.getDbmsUserDelete());
        mvValues.add(data.getUserDeleteTs());
    }
}
