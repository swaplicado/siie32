/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.account;

/**
 *
 * @author Sergio Flores
 */
public class SAccountLedger extends SAccount {

    protected int mnDeep;
    protected int[] manTypeKey;

    /**
     * Creates new ledger account.
     * @param idAccount Account's database ID (primary key).
     * @param codeStd Account's code on standard-format (<code>String</code> of 48 digits, i.e. 8 levels * 6 digits).
     * @param name Account's name.
     * @param deleted Deletion status of account's database registry.
     * @param deep Account's deep.
     * @param digits Digits of current level for account code on user-format (first level).
     */
    public SAccountLedger(int idAccount, String codeStd, String name, boolean deleted, int deep, int digits) {
        this(idAccount, codeStd, name, deleted, deep, digits, null);
    }

    /**
     * Creates new ledger account.
     * @param idAccount Account's database ID (primary key).
     * @param code Account's code on standard-format (<code>String</code> of 48 digits, i.e. 8 levels * 6 digits).
     * @param name Account's name.
     * @param deleted Deletion status of account's database registry.
     * @param deep Account's deep.
     * @param digits Digits of current level for account code on user-format (first level).
     * @param keyType Account's type.
     */
    public SAccountLedger(int idAccount, String code, String name, boolean deleted, int deep, int digits, int[] keyType) {
        super(idAccount, code, name, deleted, 1, digits);
        manTypeKey = keyType;
        mnDeep = deep;
    }

    public int getDeep() {
        return mnDeep;
    }

    public void setDeep(int deep) {
        mnDeep = deep;
    }

    public int[] getTypeKey() {
        return manTypeKey;
    }

    public void setTypeKey(int[] key) {
        manTypeKey = key;
    }
}
