/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SDbBenefitTable extends SDbRegistryUser {
    
    protected int mnPkBenefitId;
    protected String msCode;
    protected String msName;
    protected Date mtDateStart;
    protected Date mtDateEnd_n;
    protected String msUnionized;
    //protected boolean mbDeleted;
    protected int mnFkBenefitTypeId;
    protected int mnFkEarningId;
    protected int mnFkDeductionId_n;
    protected int mnFkPaymentTypeId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected ArrayList<SDbBenefitTableRow> maChildRows;

    public SDbBenefitTable() {
        super(SModConsts.HRS_BEN);
    }

    public void setPkBenefitId(int n) { mnPkBenefitId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd_n(Date t) { mtDateEnd_n = t; }
    public void setUnionized(String s) { msUnionized = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkBenefitTypeId(int n) { mnFkBenefitTypeId = n; }
    public void setFkEarningId(int n) { mnFkEarningId = n; }
    public void setFkDeductionId_n(int n) { mnFkDeductionId_n = n; }
    public void setFkPaymentTypeId_n(int n) { mnFkPaymentTypeId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkBenefitId() { return mnPkBenefitId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd_n() { return mtDateEnd_n; }
    public String getUnionized() { return msUnionized; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkBenefitTypeId() { return mnFkBenefitTypeId; }
    public int getFkEarningId() { return mnFkEarningId; }
    public int getFkDeductionId_n() { return mnFkDeductionId_n; }
    public int getFkPaymentTypeId_n() { return mnFkPaymentTypeId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<SDbBenefitTableRow> getChildRows() { return maChildRows; }
    
    public String composeUnionizedDescription() {
        return SDbBenefitTable.composeUnionizedDescription(msUnionized);
    }

    public static String composeUnionizedDescription(String unionized) {
        String description = "";
        
        if (unionized.equals(SHrsConsts.CODE_UNION_NO)) {
            description = SHrsConsts.TXT_UNION_NO;
        }
        else if (unionized.equals(SHrsConsts.CODE_UNION_YES)) {
            description = SHrsConsts.TXT_UNION_YES;
        }
        else if (unionized.isEmpty()) {
            description = SHrsConsts.TXT_INDISTINCT;
        }
        else {
            description = "?";
        }
        
        return description;
    }
    
    /**
     * Create benefit annums up to required maximum number from this benefit table.
     * @param reqAnnums Required number of annums to be created.
     * @return 
     * @throws java.lang.Exception 
     */
    public ArrayList<SDbEmployeeBenefitTablesAnnum> createBenefitAnnums(final int reqAnnums) throws Exception {
        int curAnnum = 1;
        ArrayList<SDbEmployeeBenefitTablesAnnum> benefitAnnums = new ArrayList<>();
        
        for (SDbBenefitTableRow row : maChildRows) {
            int maxAnnum = row.getMonths() / SLibTimeConsts.MONTHS;
            
            for (; curAnnum <= maxAnnum && curAnnum <= reqAnnums; curAnnum++) {
                SDbEmployeeBenefitTablesAnnum benefitAnnum = new SDbEmployeeBenefitTablesAnnum();
                //benefitAnnum.setPkEmployeeId(...);
                benefitAnnum.setPkBenefitTypeId(mnFkBenefitTypeId);
                benefitAnnum.setPkAnnumId(curAnnum);
                benefitAnnum.setBenefitDays(row.getBenefitDays());
                benefitAnnum.setBenefitBonusPct(row.getBenefitBonusPercentage());
                benefitAnnum.setFkBenefitId(mnPkBenefitId);
                //benefitAnnum.setFkUserId(...);
                //benefitAnnum.setTsUser(...);
                benefitAnnums.add(benefitAnnum);
            }
            
            if (curAnnum > reqAnnums) {
                break;
            }
        }
        
        if (benefitAnnums.size() != reqAnnums) {
            throw new Exception("El número de aniversarios creado (" + benefitAnnums.size() + ") es diferente al número de aniversarios solicitado (" + reqAnnums + ").");
        }
        
        return benefitAnnums;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBenefitId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBenefitId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBenefitId = 0;
        msCode = "";
        msName = "";
        mtDateStart = null;
        mtDateEnd_n = null;
        msUnionized = "";
        mbDeleted = false;
        mnFkBenefitTypeId = 0;
        mnFkEarningId = 0;
        mnFkDeductionId_n = 0;
        mnFkPaymentTypeId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        maChildRows = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ben = " + mnPkBenefitId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ben = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkBenefitId = 0;

        msSql = "SELECT COALESCE(MAX(id_ben), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkBenefitId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
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
            mnPkBenefitId = resultSet.getInt("id_ben");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd_n = resultSet.getDate("dt_end_n");
            msUnionized = resultSet.getString("uni");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkBenefitTypeId = resultSet.getInt("fk_tp_ben");
            mnFkEarningId = resultSet.getInt("fk_ear");
            mnFkDeductionId_n = resultSet.getInt("fk_ded_n");
            mnFkPaymentTypeId_n = resultSet.getInt("fk_tp_pay_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            maChildRows.clear();

            msSql = "SELECT id_row FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN_ROW) + " "
                    + "WHERE id_ben = " + mnPkBenefitId + " "
                    + "ORDER BY id_row ";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                maChildRows.add((SDbBenefitTableRow) session.readRegistry(SModConsts.HRS_BEN_ROW, new int[] { mnPkBenefitId, resultSet.getInt(1) }));
            }

            // Finish registry reading:

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkBenefitId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " + 
                    (mtDateEnd_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd_n) + "'") + ", " + 
                    "'" + msUnionized + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkBenefitTypeId + ", " + 
                    mnFkEarningId + ", " +
                    (mnFkDeductionId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkDeductionId_n) + ", " +
                    (mnFkPaymentTypeId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkPaymentTypeId_n) + ", " +
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_ben = " + mnPkBenefitId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "dt_sta = '" + SLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end_n = " + (mtDateEnd_n == null ? "NULL" : "'" + SLibUtils.DbmsDateFormatDate.format(mtDateEnd_n) + "'") + ", " +
                    "uni = '" + msUnionized + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_ben = " + mnFkBenefitTypeId + ", " +
                    "fk_ear = " + mnFkEarningId + ", " +
                    "fk_ded_n = " + (mnFkDeductionId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkDeductionId_n) + ", " +
                    "fk_tp_pay_n = " + (mnFkPaymentTypeId_n == SLibConsts.UNDEFINED ? "NULL" : "" + mnFkPaymentTypeId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN_ROW_AUX) + " "
                + "WHERE id_ben = " + mnPkBenefitId + " ";
        session.getStatement().execute(msSql);

        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_BEN_ROW) + " "
                + "WHERE id_ben = " + mnPkBenefitId + " ";
        session.getStatement().execute(msSql);

        for (SDbBenefitTableRow child : maChildRows) {
            child.setPkBenefitId(mnPkBenefitId);
            child.setRegistryNew(true);
            child.save(session);
        }

        // Finish registry saving:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbBenefitTable clone() throws CloneNotSupportedException {
        SDbBenefitTable registry = new SDbBenefitTable();

        registry.setPkBenefitId(this.getPkBenefitId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd_n(this.getDateEnd_n());
        registry.setUnionized(this.getUnionized());
        registry.setDeleted(this.isDeleted());
        registry.setFkBenefitTypeId(this.getFkBenefitTypeId());
        registry.setFkEarningId(this.getFkEarningId());
        registry.setFkDeductionId_n(this.getFkDeductionId_n());
        registry.setFkPaymentTypeId_n(this.getFkPaymentTypeId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
