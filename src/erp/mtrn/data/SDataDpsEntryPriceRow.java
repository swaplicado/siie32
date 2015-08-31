/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.data;

/**
 *
 * @author Irving Sánchez
 */
public class SDataDpsEntryPriceRow extends erp.lib.table.STableRow {

    public SDataDpsEntryPriceRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }
    
    @Override
    public void prepareTableRow() {
        SDataDpsEntryPrice price = (SDataDpsEntryPrice) moData;
        
         mvValues.clear();
         mvValues.add(price.getReferenceNumber());
         mvValues.add(price.getContractPriceYear());
         mvValues.add(price.getContractPriceMonth());
         mvValues.add(price.getIsPriceVariable());
         mvValues.add(price.getOriginalQuantity());
         mvValues.add(price.getOriginalPriceUnitaryCy());
         mvValues.add(price.getContractBase());
         mvValues.add(price.getContractFuture());
         mvValues.add(price.getContractFactor());
         mvValues.add(price.getIsDeleted());
    }    
}
