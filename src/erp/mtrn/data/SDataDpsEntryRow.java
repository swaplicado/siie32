/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.data;

import erp.gui.account.SAccountUtils;

/**
 *
 * @author Sergio Flores
 */
public class SDataDpsEntryRow extends erp.lib.table.STableRow {

    int maskCc;
    
    public SDataDpsEntryRow(java.lang.Object data, int maskCostCenter) {
        moData = data;
        maskCc = maskCostCenter;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataDpsEntry entry = (SDataDpsEntry) moData;

        mvValues.clear();
        mvValues.add(entry.getSortingPosition());
        mvValues.add(entry.getConceptKey());
        mvValues.add(entry.getConcept());
        mvValues.add(entry.getOriginalQuantity());
        mvValues.add(entry.getDbmsOriginalUnitSymbol());
        mvValues.add(entry.getOriginalPriceUnitaryCy());
        mvValues.add(entry.getOriginalDiscountUnitaryCy());
        mvValues.add(entry.getDiscountEntryCy());
        mvValues.add(entry.getSubtotalProvisionalCy_r());
        mvValues.add(entry.getDiscountDocCy());
        mvValues.add(entry.getSubtotalCy_r());
        mvValues.add(entry.getTaxChargedCy_r());
        mvValues.add(entry.getTaxRetainedCy_r());
        mvValues.add(entry.getTotalCy_r());
        mvValues.add(entry.getIsDeleted());
        mvValues.add(entry.hasDpsLinksAsSource());
        mvValues.add(entry.hasDpsLinksAsDestiny());
        mvValues.add(entry.hasDpsAdjustmentsAsDoc());
        mvValues.add(entry.hasDpsAdjustmentsAsAdjustment());
        mvValues.add(entry.getDbmsDpsEntryType());
        mvValues.add(entry.getDbmsDpsAdjustmentType());
        mvValues.add(entry.getDbmsTaxRegion());
        mvValues.add(entry.getDbmsItemRef_n());
        mvValues.add(entry.getDbmsCostCenterCode().isEmpty() ? "" : SAccountUtils.convertCodeUsr(maskCc, entry.getDbmsCostCenterCode()));
        mvValues.add(entry.getDbmsCostCenter_n());
        mvValues.add(entry.getUserId());
    }
}
