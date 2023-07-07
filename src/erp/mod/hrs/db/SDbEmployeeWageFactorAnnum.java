/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbEmployeeWageFactorAnnum extends SDbRegistryUser {

    protected int mnPkEmployeeId;
    protected int mnPkAnnumId;
    protected int mnAnnualBonusDays;
    protected int mnVacationDays;
    protected double mdVacationBonusPct;
    protected double mdTotalAnnualDays;
    protected double mdWageFactor;
    protected int mnFkUserId;
    protected Date mtTsUser;
    
    public SDbEmployeeWageFactorAnnum() {
        super(SModConsts.HRS_EMP_WAGE_FAC_ANN);
    }

    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setPkAnnumId(int n) { mnPkAnnumId = n; }
    public void setAnnualBonusDays(int n) { mnAnnualBonusDays = n; }
    public void setVacationDays(int n) { mnVacationDays = n; }
    public void setVacationBonusPct(double d) { mdVacationBonusPct = d; }
    public void setTotalAnnualDays(double d) { mdTotalAnnualDays = d; }
    public void setWageFactor(double d) { mdWageFactor = d; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkAnnumId() { return mnPkAnnumId; }
    public int getAnnualBonusDays() { return mnAnnualBonusDays; }
    public int getVacationDays() { return mnVacationDays; }
    public double getVacationBonusPct() { return mdVacationBonusPct; }
    public double getTotalAnnualDays() { return mdTotalAnnualDays; }
    public double getWageFactor() { return mdWageFactor; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }
    
    public void computeWageFactor() {
        mdTotalAnnualDays = SHrsConsts.YEAR_DAYS + (mnVacationDays * mdVacationBonusPct) + mnAnnualBonusDays;
        mdWageFactor = mdTotalAnnualDays / SHrsConsts.YEAR_DAYS;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEmployeeId = pk[0];
        mnPkAnnumId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEmployeeId, mnPkAnnumId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkEmployeeId = 0;
        mnPkAnnumId = 0;
        mnAnnualBonusDays = 0;
        mnVacationDays = 0;
        mdVacationBonusPct = 0;
        mdTotalAnnualDays = 0;
        mdWageFactor = 0;
        mnFkUserId = 0;
        mtTsUser = null;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_emp = " + mnPkEmployeeId + " AND "
                + "id_ann = " + mnPkAnnumId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_emp = " + pk[0] + " AND "
                + "id_ann = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAnnumId = 0;

        msSql = "SELECT COALESCE(MAX(id_ann), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE id_emp = " + mnPkEmployeeId + " ";
        
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAnnumId = resultSet.getInt(1);
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
            mnPkEmployeeId = resultSet.getInt("id_emp");
            mnPkAnnumId = resultSet.getInt("id_ann");
            mnAnnualBonusDays = resultSet.getInt("ann_bon_day");
            mnVacationDays = resultSet.getInt("vac_day");
            mdVacationBonusPct = resultSet.getDouble("vac_bon_per");
            mdTotalAnnualDays = resultSet.getDouble("tot_ann_day");
            mdWageFactor = resultSet.getDouble("wage_fac");
            mnFkUserId = resultSet.getInt("fk_usr");
            mtTsUser = resultSet.getTimestamp("ts_usr");

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
            mnFkUserId = session.getUser().getPkUserId();

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkEmployeeId + ", " + 
                    mnPkAnnumId + ", " + 
                    mnAnnualBonusDays + ", " + 
                    mnVacationDays + ", " + 
                    mdVacationBonusPct + ", " + 
                    mdTotalAnnualDays + ", " + 
                    mdWageFactor + ", " + 
                    mnFkUserId + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            throw new Exception(SDbConsts.ERR_MSG_REG_NON_UPDATABLE);
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbEmployeeWageFactorAnnum clone() throws CloneNotSupportedException {
        SDbEmployeeWageFactorAnnum registry = new SDbEmployeeWageFactorAnnum();

        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setPkAnnumId(this.getPkAnnumId());
        registry.setAnnualBonusDays(this.getAnnualBonusDays());
        registry.setVacationDays(this.getVacationDays());
        registry.setVacationBonusPct(this.getVacationBonusPct());
        registry.setTotalAnnualDays(this.getTotalAnnualDays());
        registry.setWageFactor(this.getWageFactor());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUser(this.getTsUser());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
