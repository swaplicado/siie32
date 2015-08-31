/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.mkt.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Néstor Ávalos
 */
public class SDbCommission extends SDbRegistryUser implements SGridRow {

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnPkEntryId;
    protected int mnPkSalesAgentId;
    protected Date mtDate;
    protected Date mtDateCommissions;
    protected double mdPercentage;
    protected double mdValueUnitary;
    protected double mdValue;
    protected double mdCommissions;
    protected int mnCommissionsModality;
    protected int mnCommissionsSource;
    protected boolean mbManual;
    protected boolean mbClosed;
    //protected boolean mbDeleted;
    protected int mnFkLogId;
    protected int mnFkOriginCommissionsId;
    protected int mnFkCommissionsTypeId;
    protected int mnFkSalesAgentTypeId_n;
    protected int mnFkLinkTypeId;
    protected int mnFkReferenceId;
    protected int mnFkItemId;
    protected int mnFkUserClosedId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserClosed;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected String msAuxDpsEntryItemKey;
    protected String msAuxDpsEntryItem;
    protected double mdAuxDpsEntryQuantity;
    protected String msAuxDpsEntryUnit;
    protected double mdAuxDpsEntrySubtotal;
    protected double mdAuxDpsEntryAdjustment;
    protected double mdAuxDpsEntryCommision;
    protected double mdAuxDpsEntryCommisionAdjustment;
    protected double mdAuxDpsEntryCommisionTotal;
    protected double mdAuxDpsEntryCommisionPaid;
    protected String msAuxDpsEntryCurrencyKey;
    protected String msAuxCommissionsModality;

    public SDbCommission() {
        super(SModConsts.MKT_COMMS);
    }

    /*
     * Public methods
     */

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkDocId(int n) { mnPkDocId = n; }
    public void setPkEntryId(int n) { mnPkEntryId = n; }
    public void setPkSalesAgentId(int n) { mnPkSalesAgentId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setDateCommissions(Date t) { mtDateCommissions = t; }
    public void setPercentage(double d) { mdPercentage = d; }
    public void setValueUnitary(double d) { mdValueUnitary = d; }
    public void setValue(double d) { mdValue = d; }
    public void setCommissions(double d) { mdCommissions = d; }
    public void setCommissionsModality(int n) { mnCommissionsModality = n; }
    public void setCommissionsSource(int n) { mnCommissionsSource = n; }
    public void setManual(boolean b) { mbManual = b; }
    public void setClosed(boolean b) { mbClosed = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkLogId(int n) { mnFkLogId = n; }
    public void setFkOriginCommissionsId(int n) { mnFkOriginCommissionsId = n; }
    public void setFkCommissionsTypeId(int n) { mnFkCommissionsTypeId = n; }
    public void setFkSalesAgentTypeId_n(int n) { mnFkSalesAgentTypeId_n = n; }
    public void setFkLinkTypeId(int n) { mnFkLinkTypeId = n; }
    public void setFkReferenceId(int n) { mnFkReferenceId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUserClosedId(int n) { mnFkUserClosedId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserClosed(Date t) { mtTsUserClosed = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkDocId() { return mnPkDocId; }
    public int getPkEntryId() { return mnPkEntryId; }
    public int getPkSalesAgentId() { return mnPkSalesAgentId; }
    public Date getDate() { return mtDate; }
    public Date getDateCommissions() { return mtDateCommissions; }
    public double getPercentage() { return mdPercentage; }
    public double getValueUnitary() { return mdValueUnitary; }
    public double getValue() { return mdValue; }
    public double getCommissions() { return mdCommissions; }
    public int getCommissionsModality() { return mnCommissionsModality; }
    public int getCommissionsSource() { return mnCommissionsSource; }
    public boolean isManual() { return mbManual; }
    public boolean isClosed() { return mbClosed; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkLogId() { return mnFkLogId; }
    public int getFkOriginCommissionsId() { return mnFkOriginCommissionsId; }
    public int getFkCommissionsTypeId() { return mnFkCommissionsTypeId; }
    public int getFkSalesAgentTypeId_n() { return mnFkSalesAgentTypeId_n; }
    public int getFkLinkTypeId() { return mnFkLinkTypeId; }
    public int getFkReferenceId() { return mnFkReferenceId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUserClosedId() { return mnFkUserClosedId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserClosed() { return mtTsUserClosed; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setAuxDpsEntryItemKey(String s) { msAuxDpsEntryItemKey = s; }
    public void setAuxDpsEntryItem(String s) { msAuxDpsEntryItem = s; }
    public void setAuxDpsEntryQuantity(double d) { mdAuxDpsEntryQuantity = d; }
    public void setAuxDpsEntryUnit(String s) { msAuxDpsEntryUnit = s; }
    public void setAuxDpsEntrySubtotal(double d) { mdAuxDpsEntrySubtotal = d; }
    public void setAuxDpsEntryAdjustment(double d) { mdAuxDpsEntryAdjustment = d; }
    public void setAuxDpsEntryCommission(double d) { mdAuxDpsEntryCommision  = d; }
    public void setAuxDpsEntryCommissionAdjustment(double d) { mdAuxDpsEntryCommisionAdjustment  = d; }
    public void setAuxDpsEntryCommissionTotal(double d) { mdAuxDpsEntryCommisionTotal = d; }
    public void setAuxDpsEntryCommissionPaid(double d) { mdAuxDpsEntryCommisionPaid = d; }
    public void setAuxDpsEntryCurrencyKey(String s) { msAuxDpsEntryCurrencyKey = s; }
    public void setAuxCommissionsModality(String s) { msAuxCommissionsModality = s; }

    public String getAuxDpsEntryItem() { return msAuxDpsEntryItem; }
    public String getAuxDpsEntryItemKey() { return msAuxDpsEntryItemKey; }
    public double getAuxDpsEntryQuantity() { return mdAuxDpsEntryQuantity; }
    public String getAuxDpsEntryUnit() { return msAuxDpsEntryUnit; }
    public double getAuxDpsEntrySubtotal() { return mdAuxDpsEntrySubtotal; }
    public double getAuxDpsEntryAdjustment() { return mdAuxDpsEntryAdjustment; }
    public double getAuxDpsEntryCommission() { return mdAuxDpsEntryCommision; }
    public double getAuxDpsEntryCommissionAdjustment() { return mdAuxDpsEntryCommisionAdjustment; }
    public double getAuxDpsEntryCommissionTotal() { return mdAuxDpsEntryCommisionTotal; }
    public double getAuxDpsEntryCommissionPaid() { return mdAuxDpsEntryCommisionPaid; }
    public String getAuxDpsEntryCurrencyKey() { return msAuxDpsEntryCurrencyKey; }
    public String getAuxCommissionsModality() { return msAuxCommissionsModality; }

    public void calculateCommission(final int type, final double percentage, final double valueUnitary, final double value, final double subtotal) {

        switch (type) {
            case SModSysConsts.MKTS_TP_COMMS_PER:

                mdCommissions = subtotal * percentage;
                break;
            case SModSysConsts.MKTS_TP_COMMS_NA:
            case SModSysConsts.MKTS_TP_COMMS_AMT:
            case SModSysConsts.MKTS_TP_COMMS_AMT_FIX:
            case SModSysConsts.MKTS_TP_COMMS_AMT_FIX_U:
                break;
            default:
        }
    }

    public void closeCommision(final SGuiSession session, final int[] filter, final int fkUser) throws Exception {
        Statement statement = session.getStatement();

        statement.execute("UPDATE mkt_comms "
                + "SET b_clo = 1, fk_usr_clo = " + fkUser + ", ts_usr_clo = NOW() "
                + "WHERE id_year = " + filter[0] + " AND id_doc = " + filter[1] + " AND id_ety = " + filter[2] + " AND id_sal_agt = " + filter[3] + " ");
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkDocId = pk[1];
        mnPkEntryId = pk[2];
        mnPkSalesAgentId = pk[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkDocId, mnPkEntryId, mnPkSalesAgentId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnPkEntryId = 0;
        mnPkSalesAgentId = 0;
        mtDate = null;
        mtDateCommissions = null;
        mdPercentage = 0;
        mdValueUnitary = 0;
        mdValue = 0;
        mdCommissions = 0;
        mnCommissionsModality = 0;
        mnCommissionsSource = 0;
        mbManual = false;
        mbClosed = false;
        mbDeleted = false;
        mnFkLogId = 0;
        mnFkOriginCommissionsId = 0;
        mnFkCommissionsTypeId = 0;
        mnFkSalesAgentTypeId_n = 0;
        mnFkLinkTypeId = 0;
        mnFkReferenceId = 0;
        mnFkItemId = 0;
        mnFkUserClosedId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserClosed = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        msAuxDpsEntryItemKey = "";
        msAuxDpsEntryItem = "";
        mdAuxDpsEntryQuantity = 0;
        msAuxDpsEntryUnit = "";
        mdAuxDpsEntrySubtotal = 0;
        mdAuxDpsEntryAdjustment = 0;
        mdAuxDpsEntryCommision = 0;
        mdAuxDpsEntryCommisionAdjustment = 0;
        mdAuxDpsEntryCommisionTotal = 0;
        mdAuxDpsEntryCommisionPaid = 0;
        msAuxDpsEntryCurrencyKey = "";
        msAuxCommissionsModality = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_year = " + mnPkYearId + " AND id_doc = " + mnPkDocId + " AND id_ety = " + mnPkEntryId + " AND id_sal_agt = " + mnPkSalesAgentId;
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_year = " + pk[0] + " AND id_doc = " + pk[1] + " AND id_ety = " + pk[2] + " AND id_sal_agt = " + pk[3];
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {

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
            mnPkYearId = resultSet.getInt("id_year");
            mnPkDocId = resultSet.getInt("id_doc");
            mnPkEntryId = resultSet.getInt("id_ety");
            mnPkSalesAgentId = resultSet.getInt("id_sal_agt");
            mtDate = resultSet.getDate("dt");
            mtDateCommissions = resultSet.getDate("dt_comms");
            mdPercentage = resultSet.getDouble("per");
            mdValueUnitary = resultSet.getDouble("val_u");
            mdValue = resultSet.getDouble("val");
            mdCommissions = resultSet.getDouble("comms");
            mnCommissionsModality = resultSet.getInt("comms_mod");
            mnCommissionsSource = resultSet.getInt("comms_src");
            mbManual = resultSet.getBoolean("b_man");
            mbClosed = resultSet.getBoolean("b_clo");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkLogId = resultSet.getInt("fk_log");
            mnFkOriginCommissionsId = resultSet.getInt("fk_orig_comms");
            mnFkCommissionsTypeId = resultSet.getInt("fk_tp_comms");
            mnFkSalesAgentTypeId_n = resultSet.getInt("fk_tp_sal_agt_n");
            mnFkLinkTypeId = resultSet.getInt("fk_tp_link");
            mnFkReferenceId = resultSet.getInt("fk_ref");
            mnFkItemId = resultSet.getInt("fk_item");
            mnFkUserClosedId = resultSet.getInt("fk_usr_clo");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserClosed = resultSet.getTimestamp("ts_usr_clo");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

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
            mnFkUserClosedId = SUtilConsts.USR_NA_ID;
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkYearId + ", " +
                    mnPkDocId + ", " +
                    mnPkEntryId + ", " +
                    mnPkSalesAgentId + ", " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateCommissions) + "', " +
                    mdPercentage + ", " +
                    mdValueUnitary + ", " +
                    mdValue + ", " +
                    mdCommissions + ", " +
                    mnCommissionsModality + ", " +
                    mnCommissionsSource + ", " +
                    (mbManual ? 1 : 0) + ", " +
                    (mbClosed ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkLogId + ", " +
                    mnFkOriginCommissionsId + ", " +
                    mnFkCommissionsTypeId + ", " +
                    (mnFkSalesAgentTypeId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkSalesAgentTypeId_n) + ", " +
                    mnFkLinkTypeId + ", " +
                    mnFkReferenceId + ", " +
                    mnFkItemId + ", " +
                    mnFkUserClosedId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_year = " + mnPkYearId + ", " +
                    "id_doc = " + mnPkDocId + ", " +
                    "id_ety = " + mnPkEntryId + ", " +
                    "id_sal_agt = " + mnPkSalesAgentId + ", " +
                    */
                    "dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "dt_comms = '" + SLibUtils.DbmsDateFormatDate.format(mtDateCommissions) + "', " +
                    "per = " + mdPercentage + ", " +
                    "val_u = " + mdValueUnitary + ", " +
                    "val = " + mdValue + ", " +
                    "comms = " + mdCommissions + ", " +
                    "comms_mod = " + mnCommissionsModality + ", " +
                    "comms_src = " + mnCommissionsSource + ", " +
                    "b_man = " + (mbManual ? 1 : 0) + ", " +
                    "b_clo = " + (mbClosed ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_log = " + mnFkLogId + ", " +
                    "fk_orig_comms = " + mnFkOriginCommissionsId + ", " +
                    "fk_tp_comms = " + mnFkCommissionsTypeId + ", " +
                    "fk_tp_sal_agt_n = " + (mnFkSalesAgentTypeId_n == SLibConsts.UNDEFINED ? "NULL" : mnFkSalesAgentTypeId_n) + ", " +
                    "fk_tp_link = " + mnFkLinkTypeId + ", " +
                    "fk_ref = " + mnFkReferenceId + ", " +
                    "fk_item = " + mnFkItemId + ", " +
                    //"fk_usr_clo = " + mnFkUserClosedId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_clo = " + "NOW()" + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public boolean canDelete(SGuiSession session) throws SQLException, Exception {
        boolean can = super.canDelete(session);
        int nPkYearId = mnPkYearId;
        int nPkDocId = mnPkDocId;
        int nPkEntryId = mnPkEntryId;

        Statement statement = null;
        ResultSet resultSet = null;

        if (can) {

            // Check dps type by sales agent:

            msSql = "SELECT d.fid_ct_dps, d.fid_cl_dps, d.fid_tp_dps "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d "
                    + "WHERE d.id_year = " + nPkYearId + " AND d.id_doc = " + nPkDocId + "; ";

            statement = session.getDatabase().getConnection().createStatement();
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                if (SLibUtils.compareKeys(SModSysConsts.TRNU_TP_DPS_SAL_CN, new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) })) {

                    // Obtain dps ids from adjusment:

                    msSql = "SELECT dda.id_dps_year, dda.id_dps_doc, dda.id_dps_ety "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_DPS_ADJ) + " AS dda "
                        + "WHERE dda.id_adj_year = " + nPkYearId + " AND dda.id_adj_doc = " + nPkDocId + " AND dda.id_adj_ety = " + nPkEntryId + "; ";

                    resultSet = statement.executeQuery(msSql);
                    if (resultSet.next()) {

                        nPkYearId = resultSet.getInt(1);
                        nPkDocId = resultSet.getInt(2);
                        nPkEntryId = resultSet.getInt(3);
                    }
                }
            }

            msSql = "SELECT COUNT(*) "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS_PAY) + " AS p "
                    + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS_PAY_ETY) + " AS pe ON "
                    + "p.id_pay = pe.id_pay "
                    + "WHERE p.b_del = 0 AND pe.fk_year = " + nPkYearId + " AND pe.fk_doc = " + nPkDocId + " AND pe.fk_ety = " + nPkEntryId + " AND pe.fk_sal_agt = " + mnPkSalesAgentId + " "
                    + "GROUP BY pe.id_pay, pe.id_ety ";
            resultSet = session.getStatement().executeQuery(msSql);

            /*
            if (resultSet.next()) {
                if (resultSet.getInt(1) > 0) {

                    msQueryResult = "No se puede eliminar el registro: \nEl documento de compra-venta tiene registrados pagos de comisiones.";
                    can = false;
                }
            }
            */
        }

        return can;
    }

    @Override
    public void delete(final SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        mbDeleted = !mbDeleted;
        mnFkUserUpdateId = session.getUser().getPkUserId();

        msSql = "UPDATE " + getSqlTable() + " SET " +
                "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                "ts_usr_upd = NOW() " +
                getSqlWhere();
        session.getStatement().execute(msSql);

        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbCommission clone() throws CloneNotSupportedException {
        SDbCommission registry = new SDbCommission();

        registry.setPkYearId(this.getPkYearId());
        registry.setPkDocId(this.getPkDocId());
        registry.setPkEntryId(this.getPkEntryId());
        registry.setPkSalesAgentId(this.getPkSalesAgentId());
        registry.setDate(this.getDate());
        registry.setDateCommissions(this.getDateCommissions());
        registry.setPercentage(this.getPercentage());
        registry.setValueUnitary(this.getValueUnitary());
        registry.setValue(this.getValue());
        registry.setCommissions(this.getCommissions());
        registry.setCommissionsModality(this.getCommissionsModality());
        registry.setCommissionsSource(this.getCommissionsSource());
        registry.setManual(this.isManual());
        registry.setClosed(this.isClosed());
        registry.setDeleted(this.isDeleted());
        registry.setFkLogId(this.getFkLogId());
        registry.setFkOriginCommissionsId(this.getFkOriginCommissionsId());
        registry.setFkCommissionsTypeId(this.getFkCommissionsTypeId());
        registry.setFkSalesAgentTypeId_n(this.getFkSalesAgentTypeId_n());
        registry.setFkLinkTypeId(this.getFkLinkTypeId());
        registry.setFkReferenceId(this.getFkReferenceId());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUserClosedId(this.getFkUserClosedId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserClosed(this.getTsUserClosed());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setAuxDpsEntryItemKey(this.getAuxDpsEntryItemKey());
        registry.setAuxDpsEntryItem(this.getAuxDpsEntryItem());
        registry.setAuxDpsEntryQuantity(this.getAuxDpsEntryQuantity());
        registry.setAuxDpsEntryUnit(this.getAuxDpsEntryUnit());
        registry.setAuxDpsEntrySubtotal(this.getAuxDpsEntrySubtotal());
        registry.setAuxDpsEntryCommission(this.getAuxDpsEntryCommission());
        registry.setAuxDpsEntryCommissionAdjustment(this.getAuxDpsEntryCommissionAdjustment());
        registry.setAuxDpsEntryCommissionTotal(this.getAuxDpsEntryCommissionTotal());
        registry.setAuxDpsEntryCommissionPaid(this.getAuxDpsEntryCommissionPaid());
        registry.setAuxDpsEntryCurrencyKey(this.getAuxDpsEntryCurrencyKey());

        return registry;
    }

        @Override
    public int[] getRowPrimaryKey() {
        return new int[] {  mnPkYearId, mnPkDocId, mnPkEntryId, mnPkSalesAgentId };
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
    public void setRowEdited(final boolean edited) {

    }

    @Override
    public Object getRowValueAt(int row) {
        Object value = null;

        switch(row) {
            case 0:
                value = msAuxDpsEntryItemKey;
                break;
            case 1:
                value = msAuxDpsEntryItem;
                break;
            case 2:
                value = mdPercentage * 100;
                break;
            case 3:
                value = mdAuxDpsEntryCommisionTotal;
                break;
            case 4:
                value = msAuxCommissionsModality;
                break;
            case 5:
                value = mdAuxDpsEntrySubtotal;
                break;
            case 6:
                value = mdAuxDpsEntryAdjustment;
                break;
            case 7:
                value = mdAuxDpsEntrySubtotal - mdAuxDpsEntryAdjustment;
                break;
            case 8:
                value = msAuxDpsEntryCurrencyKey;
                break;
            case 9:
                value = mdAuxDpsEntryQuantity;
                break;
            case 10:
                value = msAuxDpsEntryUnit;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int row) {
        switch(row) {
            case 0:
            case 1:
            case 2:
                mdPercentage = (double) value / 100;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                break;
            default:
        }
    }
}
