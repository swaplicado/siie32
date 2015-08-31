/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

/**
 *
 * @author Alfonso Flores
 */
public class SDataBizPartnerBranchBankAccountCardRow extends erp.lib.table.STableRow {

    public SDataBizPartnerBranchBankAccountCardRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataBizPartnerBranchBankAccountCard bankAccountCardRow = (SDataBizPartnerBranchBankAccountCard) moData;

        mvValues.clear();
        mvValues.add(bankAccountCardRow.getDbmsCardType());
        mvValues.add(bankAccountCardRow.getHolder());
        mvValues.add(bankAccountCardRow.getCardNumber());
        mvValues.add(bankAccountCardRow.getExpirationDate());
        mvValues.add(bankAccountCardRow.getIsDeleted());
    }
}
