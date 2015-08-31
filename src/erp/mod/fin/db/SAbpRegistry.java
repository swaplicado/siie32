/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.gui.account.SAccountUtils;
import erp.mod.SModSysConsts;
import java.sql.SQLException;
import sa.lib.SLibConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SAbpRegistry extends SDbRegistryUser implements SGridRow {

    protected int mnAccountTypeId;
    protected String msAccountType;
    protected int[] manAccountTypeFilterKey;
    protected int mnAccountId;
    protected String msAccountCode;
    protected String msAccount;
    protected boolean mbIsCostCenterRequired;
    protected int mnCostCenterId;
    protected String msCostCenterCode;
    protected String msCostCenter;
    protected boolean mbIsItemRequired;
    protected int mnItemId;
    protected String msItemCode;
    protected String msItem;
    protected int mnMaskAccount;
    protected int mnMaskCostCenter;

    public SAbpRegistry(int accountTypeId, String accountType, int[] accountTypeFilterKey,
            int accountId, String accountCode, String accountName) {
        this(accountTypeId, accountType, accountTypeFilterKey, accountId, accountCode, accountName, false, SModSysConsts.FIN_CC_NA, "", "", false, SLibConsts.UNDEFINED, "", "");
    }

    public SAbpRegistry(int accountTypeId, String accountType, int[] accountTypeFilterKey,
            int accountId, String accountCode, String accountName,
            int costCenterId, String costCenterCode, String costCenterName) {
        this(accountTypeId, accountType, accountTypeFilterKey, accountId, accountCode, accountName, true, costCenterId, costCenterCode, costCenterName, false, SLibConsts.UNDEFINED, "", "");
    }

    public SAbpRegistry(int accountTypeId, String accountType, int[] accountTypeFilterKey,
            int accountId, String accountCode, String accountName,
            int costCenterId, String costCenterCode, String costCenterName,
            int itemId, String itemCode, String itemName) {
        this(accountTypeId, accountType, accountTypeFilterKey, accountId, accountCode, accountName, true, costCenterId, costCenterCode, costCenterName, true, itemId, itemCode, itemName);
    }

    private SAbpRegistry(int accountTypeId, String accountType, int[] accountTypeFilterKey,
            int accountId, String accountCode, String accountName,
            boolean isCostCenterRequired, int costCenterId, String costCenterCode, String costCenterName,
            boolean isItemRequired, int itemId, String itemCode, String itemName) {
        super(SLibConsts.UNDEFINED);

        mnAccountTypeId = accountTypeId;
        msAccountType = accountType;
        manAccountTypeFilterKey = accountTypeFilterKey;
        mnAccountId = accountId;
        msAccountCode = accountCode;
        msAccount = accountName;
        mbIsCostCenterRequired = isCostCenterRequired;
        mnCostCenterId = costCenterId;
        msCostCenterCode = costCenterCode;
        msCostCenter = costCenterName;
        mbIsItemRequired = isItemRequired;
        mnItemId = itemId;
        msItemCode = itemCode;
        msItem = itemName;
    }

    @Override
    public SAbpRegistry clone() throws CloneNotSupportedException {
        SAbpRegistry registry = new SAbpRegistry(mnAccountTypeId, msAccountType, manAccountTypeFilterKey,
                mnAccountId, msAccountCode, msAccount,
                mnCostCenterId, msCostCenterCode, msCostCenter,
                mnItemId, msItemCode, msItem);

        registry.setMaskAccount(this.getMaskAccount());
        registry.setMaskCostCenter(this.getMaskCostCenter());

        return registry;
    }

    public void setAccountTypeId(int n) { mnAccountTypeId = n; }
    public void setAccountType(String s) { msAccountType = s; }
    public void setAccountTypeFilterKey(int[] a) { manAccountTypeFilterKey = a; }
    public void setAccountId(int n) { mnAccountId = n; }
    public void setAccountCode(String s) { msAccountCode = s; }
    public void setAccount(String s) { msAccount = s; }
    public void setCostCenterRequired(boolean b) { mbIsCostCenterRequired = b; }
    public void setCostCenterId(int n) { mnCostCenterId = n; }
    public void setCostCenterCode(String s) { msCostCenterCode = s; }
    public void setCostCenter(String s) { msCostCenter = s; }
    public void setItemRequired(boolean b) { mbIsItemRequired = b; }
    public void setItemId(int n) { mnItemId = n; }
    public void setItemCode(String s) { msItemCode = s; }
    public void setItem(String s) { msItem = s; }
    public void setMaskAccount(int n) { mnMaskAccount= n; }
    public void setMaskCostCenter(int n) { mnMaskCostCenter= n; }

    public int getAccountTypeId() { return mnAccountTypeId; }
    public String getAccountType() { return msAccountType; }
    public int[] getAccountTypeFilterKey() { return manAccountTypeFilterKey; }
    public int getAccountId() { return mnAccountId; }
    public String getAccountCode() { return msAccountCode; }
    public String getAccount() { return msAccount; }
    public boolean isCostCenterRequired() { return mbIsCostCenterRequired; }
    public int getCostCenterId() { return mnCostCenterId; }
    public String getCostCenterCode() { return msCostCenterCode; }
    public String getCostCenter() { return msCostCenter; }
    public boolean isItemRequired() { return mbIsItemRequired; }
    public int getItemId() { return mnItemId; }
    public String getItemCode() { return msItemCode; }
    public String getItem() { return msItem; }
    public int getMaskAccount() { return mnMaskAccount; }
    public int getMaskCostCenter() { return mnMaskCostCenter; }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = msAccountType;
                break;
            case 1:
                value = SAccountUtils.convertCodeUsr(mnMaskAccount, msAccountCode);
                break;
            case 2:
                value = msAccount;
                break;
            case 3:
                value = msCostCenterCode.isEmpty() ? "" : SAccountUtils.convertCodeUsr(mnMaskCostCenter, msCostCenterCode);
                break;
            case 4:
                value = msCostCenter;
                break;
            case 5:
                value = msItemCode;
                break;
            case 6:
                value = msItem;
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
    public void setPrimaryKey(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initRegistry() {
        mnAccountTypeId = 0;
        msAccountType = "";
        manAccountTypeFilterKey = null;
        mnAccountId = SModSysConsts.FIN_ACC_NA;
        msAccountCode = "";
        msAccount = "";
        mbIsCostCenterRequired = false;
        mnCostCenterId = SModSysConsts.FIN_CC_NA;;
        msCostCenterCode = "";
        msCostCenter = "";
        mbIsItemRequired = false;
        mnItemId = 0;
        msItemCode = "";
        msItem = "";
        mnMaskAccount = 0;
        mnMaskCostCenter = 0;
    }

    @Override
    public String getSqlTable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return getAccountCode();
    }

    @Override
    public String getRowName() {
        return getAccount();
    }

    @Override
    public boolean isRowSystem() {
        return isSystem();
    }

    @Override
    public boolean isRowDeletable() {
        return isDeletable();
    }

    @Override
    public boolean isRowEdited() {
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setRegistryEdited(edited);
    }
}
