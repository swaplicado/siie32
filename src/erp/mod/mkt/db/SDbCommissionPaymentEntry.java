/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.mkt.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos
 */
public class SDbCommissionPaymentEntry extends SDbRegistryUser implements SGridRow {

    protected int mnPkPaymentId;
    protected int mnPkEntryId;
    protected double mdPayment;
    protected double mdRefund;
    protected int mnFkCommsYearId;
    protected int mnFkCommsDocId;
    protected int mnFkCommsEntryId;
    protected int mnFkCommsSalesAgentId;

    protected int mnAuxPkCommsYearId;
    protected int mnAuxPkCommsDocId;
    protected int mnAuxPkCommsEntryId;
    protected int mnAuxPkCommsSalesAgentId;
    protected int mnAuxPkItemId;
    protected String msAuxOriginCommision;
    protected Date mtAuxDateDoc;
    protected String msAuxNumber;
    protected String msAuxBizPartner;
    protected double mdAuxSubtotal;
    protected double mdAuxAdjustment;
    protected String msAuxItem;
    protected String msAuxItemKey;
    protected double mdAuxDpsEntryQuantity;
    protected String msAuxDpsEntryUnit;
    protected double mdAuxEntrySubtotal;
    protected double mdAuxEntryAdjustment;
    protected double mdAuxCommision;
    protected double mdAuxCommisionAdjustment;
    protected double mdAuxCommisionTotal;
    protected double mdAuxCommisionPaid;
    protected String msAuxCurrencyKey;
    protected String msAuxCommissionsModality;

    public SDbCommissionPaymentEntry() {
        super(SModConsts.MKT_COMMS_PAY_ETY);
    }

    /*
     * Public methods
     */

    public void setPkPaymentId(int n) { mnPkPaymentId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setPayment(double d) { mdPayment = d; }
    public void setRefund(double d) { mdRefund = d; }
    public void setFkCommsYearId(int n) { mnFkCommsYearId = n; }
    public void setFkCommsDocId(int n) { mnFkCommsDocId = n; }
    public void setFkCommsEntryId(int n) { mnFkCommsEntryId = n; }
    public void setFkCommsSalesAgentId(int n) { mnFkCommsSalesAgentId = n; }

    public int getPkPaymentId() { return mnPkPaymentId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public double getPayment() { return mdPayment; }
    public double getRefund() { return mdRefund; }
    public int getFkCommsYearId() { return mnFkCommsYearId; }
    public int getFkCommsDocId() { return mnFkCommsDocId; }
    public int getFkCommsEntryId() { return mnFkCommsEntryId; }
    public int getFkCommsSalesAgentId() { return mnFkCommsSalesAgentId; }

    public void setAuxPkCommsYearId(int n) { mnAuxPkCommsYearId = n; }
    public void setAuxPkCommsDocId(int n) { mnAuxPkCommsDocId = n; }
    public void setAuxPkCommsEntryId(int n) { mnAuxPkCommsEntryId = n; }
    public void setAuxPkCommsSalesAgentId(int n) { mnAuxPkCommsSalesAgentId = n; }
    public void setAuxPkItemId(int n) { mnAuxPkItemId = n; }
    public void setAuxOriginCommision(String s) { msAuxOriginCommision = s; }
    public void setAuxDateDoc(Date t) { mtAuxDateDoc = t; }
    public void setAuxNumber(String s) { msAuxNumber = s; }
    public void setAuxBizPartner(String s) { msAuxBizPartner = s; }
    public void setAuxSubtotal(double d) { mdAuxSubtotal = d; }
    public void setAuxAdjustment(double d) { mdAuxAdjustment = d; }
    public void setAuxItem(String s) { msAuxItem = s; }
    public void setAuxItemKey(String s) { msAuxItemKey = s; }
    public void setAuxDpsEntryQuantity(double d) { mdAuxDpsEntryQuantity = d; }
    public void setAuxDpsEntryUnit(String s) { msAuxDpsEntryUnit = s; }
    public void setAuxEntrySubtotal(double d) { mdAuxEntrySubtotal = d; }
    public void setAuxEntryAdjustment(double d) { mdAuxEntryAdjustment = d; }
    public void setAuxCommission(double d) { mdAuxCommision = d; }
    public void setAuxCommissionAdjustment(double d) { mdAuxCommisionAdjustment  = d; }
    public void setAuxCommissionTotal(double d) { mdAuxCommisionTotal = d; }
    public void setAuxCommissionPaid(double d) { mdAuxCommisionPaid = d; }
    public void setAuxCurrencyKey(String s) { msAuxCurrencyKey = s; }
    public void setAuxCommissionsModality(String s) { msAuxCommissionsModality = s; }

    public int getAuxPkCommsYearId() { return mnAuxPkCommsYearId; }
    public int getAuxPkCommsDocId() { return mnAuxPkCommsDocId; }
    public int getAuxPkCommsEntryId() { return mnAuxPkCommsEntryId; }
    public int getAuxPkCommsSalesAgentId() { return mnAuxPkCommsSalesAgentId; }
    public int getAuxPkItemId() { return mnAuxPkItemId; }
    public String getAuxOriginCommision() { return msAuxOriginCommision; }
    public Date getAuxDateDoc() { return mtAuxDateDoc; }
    public String getAuxNumber() { return msAuxNumber; }
    public String getAuxBizPartner() { return msAuxBizPartner; }
    public double getAuxSubtotal() { return mdAuxSubtotal; }
    public double getAuxAdjustment() { return mdAuxAdjustment; }
    public String getAuxItem() { return msAuxItem; }
    public String getAuxItemKey() { return msAuxItemKey; }
    public double getAuxDpsEntryQuantity() { return mdAuxDpsEntryQuantity; }
    public String getAuxDpsEntryUnit() { return msAuxDpsEntryUnit; }
    public double getAuxEntrySubtotal() { return mdAuxEntrySubtotal; }
    public double getAuxEntryAdjustment() { return mdAuxEntryAdjustment; }
    public double getAuxCommision() { return mdAuxCommision; }
    public double getAuxCommisionAdjustment() { return mdAuxCommisionAdjustment; }
    public double getAuxCommisionTotal() { return mdAuxCommisionTotal; }
    public double getAuxCommisionPaid() { return mdAuxCommisionPaid; }
    public String getAuxCurrencyKey() { return msAuxCurrencyKey; }
    public String getAuxCommissionsModality() { return msAuxCommissionsModality; }


    public int[] getForeingKeyCommision() {
        return new int[] { mnFkCommsYearId, mnFkCommsDocId, mnFkCommsEntryId, mnFkCommsSalesAgentId };
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPaymentId = pk[0];
        mnPkEntryId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPaymentId, mnPkEntryId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPaymentId = 0;
        mnPkEntryId = 0;
        mdPayment = 0;
        mdRefund = 0;
        mnFkCommsYearId = 0;
        mnFkCommsDocId = 0;
        mnFkCommsEntryId = 0;
        mnFkCommsSalesAgentId = 0;

        mnAuxPkCommsYearId = 0;
        mnAuxPkCommsDocId = 0;
        mnAuxPkCommsEntryId = 0;
        mnAuxPkCommsSalesAgentId = 0;
        mnAuxPkItemId = 0;
        msAuxOriginCommision = "";
        mtAuxDateDoc = null;
        msAuxNumber = "";
        msAuxBizPartner = "";
        mdAuxSubtotal = 0;
        mdAuxAdjustment = 0;
        msAuxItem = "";
        msAuxItemKey = "";
        mdAuxDpsEntryQuantity = 0;
        msAuxDpsEntryUnit = "";
        mdAuxEntrySubtotal = 0;
        mdAuxEntryAdjustment = 0;
        mdAuxCommision = 0;
        mdAuxCommisionAdjustment = 0;
        mdAuxCommisionTotal = 0;
        mdAuxCommisionPaid = 0;
        msAuxCurrencyKey = "";
        msAuxCommissionsModality = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_pay = " + mnPkPaymentId + " AND id_ety = " + mnPkEntryId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_pay = " + pk[0] + " AND id_ety = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkEntryId = 0;

        msSql = "SELECT COALESCE(MAX(id_ety), 0) + 1 FROM " + getSqlTable() + " WHERE id_pay = " + mnPkPaymentId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkEntryId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkPaymentId = resultSet.getInt("id_pay");
            mnPkEntryId = resultSet.getInt("id_ety");
            mdPayment = resultSet.getDouble("pay");
            mdRefund = resultSet.getDouble("rfd");
            mnFkCommsYearId = resultSet.getInt("fk_year");
            mnFkCommsDocId = resultSet.getInt("fk_doc");
            mnFkCommsEntryId = resultSet.getInt("fk_ety");
            mnFkCommsSalesAgentId = resultSet.getInt("fk_sal_agt");

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPaymentId + ", " +
                    mnPkEntryId + ", " +
                    mdPayment + ", " +
                    mdRefund + ", " +
                    mnFkCommsYearId + ", " +
                    mnFkCommsDocId + ", " +
                    mnFkCommsEntryId + ", " +
                    mnFkCommsSalesAgentId + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    // "id_pay = " + mnPkPaymentId + ", " +
                    // "id_ety = " + mnPkEntryId + ", " +
                    "pay = " + mdPayment + ", " +
                    "rfd = " + mdRefund + ", " +
                    "fk_year = " + mnFkCommsYearId + ", " +
                    "fk_doc = " + mnFkCommsDocId + ", " +
                    "fk_ety = " + mnFkCommsEntryId + ", " +
                    "fk_sal_agt = " + mnFkCommsSalesAgentId + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbCommissionPaymentEntry clone() throws CloneNotSupportedException {
        SDbCommissionPaymentEntry registry = new SDbCommissionPaymentEntry();

        registry.setPkPaymentId(this.getPkPaymentId());
        registry.setPkEntryId(this.getPkEntryId());
        registry.setPayment(this.getPayment());
        registry.setRefund(this.getRefund());
        registry.setFkCommsYearId(this.getFkCommsYearId());
        registry.setFkCommsDocId(this.getFkCommsDocId());
        registry.setFkCommsEntryId(this.getFkCommsEntryId());
        registry.setFkCommsSalesAgentId(this.getFkCommsSalesAgentId());

        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkPaymentId, mnPkEntryId };
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {

    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch(col) {
            case 0:
                value = msAuxItemKey;
                break;
            case 1:
                value = msAuxItem;
                break;
            case 2:
                value = mdAuxCommisionTotal;
                break;
            case 3:
                value = mdAuxCommisionPaid;
                break;
            case 4:
                value = msAuxCommissionsModality;
                break;
            case 5:
                value = mdAuxEntrySubtotal;
                break;
            case 6:
                value = mdAuxEntryAdjustment;
                break;
            case 7:
                value = mdAuxEntrySubtotal - mdAuxEntryAdjustment;
                break;
            case 8:
                value = msAuxCurrencyKey;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
         switch(col) {
            case 0:
            case 1:
            case 2:
            case 3:
                mdAuxCommisionPaid = (Double) value;

                mdPayment = mdAuxCommisionPaid > 0 ? mdAuxCommisionPaid : 0;
                mdRefund = mdAuxCommisionPaid < 0  ? mdAuxCommisionPaid : 0;

                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                break;
            default:
        }
    }
}
