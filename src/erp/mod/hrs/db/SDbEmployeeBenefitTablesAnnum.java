/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
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
public class SDbEmployeeBenefitTablesAnnum extends SDbRegistryUser {

    protected int mnPkEmployeeId;
    protected int mnPkBenefitTypeId;
    protected int mnPkAnnumId;
    protected int mnBenefitDays;
    protected double mdBenefitBonusPercentage;
    protected int mnFkBenefitId;
    protected int mnFkUserId;
    protected Date mtTsUser;
    
    public SDbEmployeeBenefitTablesAnnum() {
        super(SModConsts.HRS_EMP_BEN_ANN);
    }

    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setPkBenefitTypeId(int n) { mnPkBenefitTypeId = n; }
    public void setPkAnnumId(int n) { mnPkAnnumId = n; }
    public void setBenefitDays(int n) { mnBenefitDays = n; }
    public void setBenefitBonusPercentage(double d) { mdBenefitBonusPercentage = d; }
    public void setFkBenefitId(int n) { mnFkBenefitId = n; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    public void setTsUser(Date t) { mtTsUser = t; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkBenefitTypeId() { return mnPkBenefitTypeId; }
    public int getPkAnnumId() { return mnPkAnnumId; }
    public int getBenefitDays() { return mnBenefitDays; }
    public double getBenefitBonusPercentage() { return mdBenefitBonusPercentage; }
    public int getFkBenefitId() { return mnFkBenefitId; }
    public int getFkUserId() { return mnFkUserId; }
    public Date getTsUser() { return mtTsUser; }
    
    public double getBenefit() {
        double benefit = 0;
        
        switch (mnPkBenefitTypeId) {
            case SModSysConsts.HRSS_TP_BEN_ANN_BON:
            case SModSysConsts.HRSS_TP_BEN_VAC:
                benefit = mnBenefitDays;
                break;
            case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                benefit = mdBenefitBonusPercentage;
                break;
            default:
                // nothing
        }
        
        return benefit;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEmployeeId = pk[0];
        mnPkBenefitTypeId = pk[1];
        mnPkAnnumId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEmployeeId, mnPkBenefitTypeId, mnPkAnnumId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkEmployeeId = 0;
        mnPkBenefitTypeId = 0;
        mnPkAnnumId = 0;
        mnBenefitDays = 0;
        mdBenefitBonusPercentage = 0;
        mnFkBenefitId = 0;
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
                + "id_tp_ben = " + mnPkBenefitTypeId + " AND "
                + "id_ann = " + mnPkAnnumId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_emp = " + pk[0] + " AND "
                + "id_tp_ben = " + pk[1] + " AND "
                + "id_ann = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAnnumId = 0;

        msSql = "SELECT COALESCE(MAX(id_ann), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE id_emp = " + mnPkEmployeeId + " AND "
                + "id_tp_ben = " + mnPkBenefitTypeId + " ";
        
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
            mnPkBenefitTypeId = resultSet.getInt("id_tp_ben");
            mnPkAnnumId = resultSet.getInt("id_ann");
            mnBenefitDays = resultSet.getInt("ben_day");
            mdBenefitBonusPercentage = resultSet.getDouble("ben_bon_per");
            mnFkBenefitId = resultSet.getInt("fk_ben");
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
                    mnPkBenefitTypeId + ", " + 
                    mnPkAnnumId + ", " + 
                    mnBenefitDays + ", " + 
                    mdBenefitBonusPercentage + ", " + 
                    mnFkBenefitId + ", " + 
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
    public SDbEmployeeBenefitTablesAnnum clone() throws CloneNotSupportedException {
        SDbEmployeeBenefitTablesAnnum registry = new SDbEmployeeBenefitTablesAnnum();

        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setPkBenefitTypeId(this.getPkBenefitTypeId());
        registry.setPkAnnumId(this.getPkAnnumId());
        registry.setBenefitDays(this.getBenefitDays());
        registry.setBenefitBonusPercentage(this.getBenefitBonusPercentage());
        registry.setFkBenefitId(this.getFkBenefitId());
        registry.setFkUserId(this.getFkUserId());
        registry.setTsUser(this.getTsUser());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
