/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.account;

import sa.lib.grid.SGridRowCustom;

/**
 *
 * @author Sergio Flores
 */
public class SRowAccountByNumber extends SGridRowCustom implements SRowAccount {

    private SAccount moAccount;

    public SRowAccountByNumber(int mask, SAccount account) {
        super(new int[] { account.getAccountId() }, SAccountUtils.convertCodeUsr(mask, account.getCodeStd()), account.getName());
        moAccount = account;
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = msRowCode;
                break;
            case 1:
                value = msRowName;
                break;
            case 2:
                value = moAccount.isDeleted();
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SAccount getAccount() {
        return moAccount;
    }
}
