/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import erp.mod.bps.db.SBpsUtils;

/**
 *
 * @author Alfonso Flores
 */
public class SDataBizPartnerBranchBankAccountRow extends erp.lib.table.STableRow {

    public SDataBizPartnerBranchBankAccountRow(java.lang.Object data) {
        moData = data;
        prepareTableRow();
    }

    @Override
    public void prepareTableRow() {
        SDataBizPartnerBranchBankAccount bankAccountRow = (SDataBizPartnerBranchBankAccount) moData;

        mvValues.clear();
        mvValues.add(bankAccountRow.getDbmsAccountCashType());
        mvValues.add(bankAccountRow.getBankAccount());
        mvValues.add(bankAccountRow.getDbmsBank());
        mvValues.add(bankAccountRow.getDbmsCurrencyKey());
        mvValues.add(bankAccountRow.getBankAccountBranchNumber());
        mvValues.add(bankAccountRow.getBankAccountNumber());
        mvValues.add(SBpsUtils.formatBankAccountNumberStd(bankAccountRow.getBankAccountNumberStd()));
        mvValues.add(bankAccountRow.getCode());
        mvValues.add(bankAccountRow.getReference());
        mvValues.add(bankAccountRow.getCodeAba());
        mvValues.add(bankAccountRow.getCodeSwift());
        mvValues.add(bankAccountRow.getIsCardApplying());
        mvValues.add(bankAccountRow.getDbmsCardIssuer());
        mvValues.add(bankAccountRow.getIsDeleted());
        mvValues.add(bankAccountRow.getDbmsUserNew());
        mvValues.add(bankAccountRow.getUserNewTs());
        mvValues.add(bankAccountRow.getDbmsUserEdit());
        mvValues.add(bankAccountRow.getUserEditTs());
        mvValues.add(bankAccountRow.getDbmsUserDelete());
        mvValues.add(bankAccountRow.getUserDeleteTs());
    }
}
