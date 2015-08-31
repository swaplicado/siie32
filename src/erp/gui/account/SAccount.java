/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.account;

import java.util.ArrayList;

/**
 *
 * @author Sergio Flores
 */
public class SAccount {

    protected int mnAccountId;
    protected String msCodeStd;
    protected String msCodeLevelStd;
    protected String msName;
    protected boolean mbDeleted;
    protected int mnLevel;
    protected int mnDigits;
    protected ArrayList<SAccount> maChildren;

    /**
     * Creates new account.
     * @param idAccount Account's database ID (primary key).
     * @param codeStd Account's code in standard-format (<code>String</code> of 48 digits, i.e. 8 levels * 6 digits).
     * @param name Account's name.
     * @param deleted Deletion status of account's database registry.
     * @param level Account's level.
     * @param digits Digits of current level for account code in user-format.
     */
    public SAccount(int idAccount, String codeStd, String name, boolean deleted, int level, int digits) {
        mnAccountId = idAccount;
        msCodeStd = codeStd;
        msCodeLevelStd = SAccountUtils.subtractCodeLevelStd(codeStd, level);
        msName = name;
        mbDeleted = deleted;
        mnLevel = level;
        mnDigits = digits;
        maChildren = new ArrayList<SAccount>();
    }

    public int getAccountId() {
        return mnAccountId;
    }

    public void setAccountId(int accountId) {
        mnAccountId = accountId;
    }

    public String getCodeStd() {
        return msCodeStd;
    }

    public void setCodeStd(String codeStd) {
        msCodeStd = codeStd;
    }

    public String getCodeLevelStd() {
        return msCodeLevelStd;
    }

    public void setCodeLevelStd(String codeLevelStd) {
        msCodeLevelStd = codeLevelStd;
    }

    public String getName() {
        return msName;
    }

    public void setName(String name) {
        msName = name;
    }

    public boolean isDeleted() {
        return mbDeleted;
    }

    public void setDeleted(boolean deleted) {
        mbDeleted = deleted;
    }

    public int getLevel() {
        return mnLevel;
    }

    public void setLevel(int level) {
        mnLevel = level;
    }

    public int getDigits() {
        return mnDigits;
    }

    public void setDigits(int digits) {
        mnDigits = digits;
    }

    public ArrayList<SAccount> getChildren() {
        return maChildren;
    }

    @Override
    public String toString() {
        return msCodeLevelStd.substring(msCodeLevelStd.length() - mnDigits) + " - " + msName + (!mbDeleted ? "" : " (INACTIVA)");
    }
}
