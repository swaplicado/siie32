/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 * Employee benefit tables.
 * Check <code>erp.mod.hrs.db.SHrsBenefitUtils</code> for convenience methods for managing benefit tables.
 * @author Sergio Flores
 */
public class SDbEmployeeBenefitTables extends SDbRegistryUser {
    
    /** Número máximo de años para tablas de prestaciones. */
    public static final int MAX_ANNUMS = 50;

    protected int mnPkEmployeeId;
    protected int mnPkBenefitsId;
    protected int mnStartingAnniversaryAnnualBonus;
    protected int mnStartingAnniversaryVacation;
    protected int mnStartingAnniversaryVacationBonus;
    protected boolean mbObsolete;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkBenefitAnnualBonusId;
    protected int mnFkBenefitVacationId;
    protected int mnFkBenefitVacationBonusId;
    protected int mnFkUserUpdateAnnualBonusId;
    protected int mnFkUserUpdateVacationId;
    protected int mnFkUserUpdateVacationBonusId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserUpdateAnnualBonus;
    protected Date mtTsUserUpdateVacation;
    protected Date mtTsUserUpdateVacationBonus;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbEmployeeBenefitTablesAnnum> maChildAnnums;
    protected ArrayList<SDbEmployeeWageFactorAnnum> maChildWageFactors;
    
    protected String msXtaBenefitNameAnnualBonus;
    protected String msXtaBenefitNameVacation;
    protected String msXtaBenefitNameVacationBonus;
    
    protected String msXtaUserUpdateNameAnnualBonus;
    protected String msXtaUserUpdateNameVacation;
    protected String msXtaUserUpdateNameVacationBonus;
    
    protected boolean mbAuxUpdateAnnualBonus;
    protected boolean mbAuxUpdateVacation;
    protected boolean mbAuxUpdateVacationBonus;
    
    /** Activate preservation of benefit annums for required benefit tables during massive assignation. */
    protected static boolean PreserveGlobalBenefitAnnumsMap = false;
    
    /** Retrieve and prepare benefit annums for required benefit tables only once exclusively during massive assignation. */
    protected static HashMap<Integer, ArrayList<SDbEmployeeBenefitTablesAnnum>> GlobalBenefitAnnumsMap; // key = ID of benefit table; value = array of benefit annums
    
    public SDbEmployeeBenefitTables() {
        super(SModConsts.HRS_EMP_BEN);
    }

    /*
     * Set-get public methods
     */
    
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setPkBenefitsId(int n) { mnPkBenefitsId = n; }
    public void setStartingAnniversaryAnnualBonus(int n) { mnStartingAnniversaryAnnualBonus = n; }
    public void setStartingAnniversaryVacation(int n) { mnStartingAnniversaryVacation = n; }
    public void setStartingAnniversaryVacationBonus(int n) { mnStartingAnniversaryVacationBonus = n; }
    public void setObsolete(boolean b) { mbObsolete = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkBenefitAnnualBonusId(int n) { mnFkBenefitAnnualBonusId = n; }
    public void setFkBenefitVacationId(int n) { mnFkBenefitVacationId = n; }
    public void setFkBenefitVacationBonusId(int n) { mnFkBenefitVacationBonusId = n; }
    public void setFkUserUpdateAnnualBonusId(int n) { mnFkUserUpdateAnnualBonusId = n; }
    public void setFkUserUpdateVacationId(int n) { mnFkUserUpdateVacationId = n; }
    public void setFkUserUpdateVacationBonusId(int n) { mnFkUserUpdateVacationBonusId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserUpdateAnnualBonus(Date t) { mtTsUserUpdateAnnualBonus = t; }
    public void setTsUserUpdateVacation(Date t) { mtTsUserUpdateVacation = t; }
    public void setTsUserUpdateVacationBonus(Date t) { mtTsUserUpdateVacationBonus = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public int getPkBenefitsId() { return mnPkBenefitsId; }
    public int getStartingAnniversaryAnnualBonus() { return mnStartingAnniversaryAnnualBonus; }
    public int getStartingAnniversaryVacation() { return mnStartingAnniversaryVacation; }
    public int getStartingAnniversaryVacationBonus() { return mnStartingAnniversaryVacationBonus; }
    public boolean isObsolete() { return mbObsolete; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkBenefitAnnualBonusId() { return mnFkBenefitAnnualBonusId; }
    public int getFkBenefitVacationId() { return mnFkBenefitVacationId; }
    public int getFkBenefitVacationBonusId() { return mnFkBenefitVacationBonusId; }
    public int getFkUserUpdateAnnualBonusId() { return mnFkUserUpdateAnnualBonusId; }
    public int getFkUserUpdateVacationId() { return mnFkUserUpdateVacationId; }
    public int getFkUserUpdateVacationBonusId() { return mnFkUserUpdateVacationBonusId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserUpdateAnnualBonus() { return mtTsUserUpdateAnnualBonus; }
    public Date getTsUserUpdateVacation() { return mtTsUserUpdateVacation; }
    public Date getTsUserUpdateVacationBonus() { return mtTsUserUpdateVacationBonus; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<SDbEmployeeBenefitTablesAnnum> getChildAnnums() { return maChildAnnums; }
    public ArrayList<SDbEmployeeWageFactorAnnum> getChildWageFactors() { return maChildWageFactors; }
    
    public void setXtaBenefitNameAnnualBonus(String s) { msXtaBenefitNameAnnualBonus = s; }
    public void setXtaBenefitNameVacation(String s) { msXtaBenefitNameVacation = s; }
    public void setXtaBenefitNameVacationBonus(String s) { msXtaBenefitNameVacationBonus = s; }
    public void setXtaUserUpdateNameAnnualBonus(String s) { msXtaUserUpdateNameAnnualBonus = s; }
    public void setXtaUserUpdateNameVacation(String s) { msXtaUserUpdateNameVacation = s; }
    public void setXtaUserUpdateNameVacationBonus(String s) { msXtaUserUpdateNameVacationBonus = s; }
    
    public String getXtaBenefitNameAnnualBonus() { return msXtaBenefitNameAnnualBonus; }
    public String getXtaBenefitNameVacation() { return msXtaBenefitNameVacation; }
    public String getXtaBenefitNameVacationBonus() { return msXtaBenefitNameVacationBonus; }
    public String getXtaUserUpdateNameAnnualBonus() { return msXtaUserUpdateNameAnnualBonus; }
    public String getXtaUserUpdateNameVacation() { return msXtaUserUpdateNameVacation; }
    public String getXtaUserUpdateNameVacationBonus() { return msXtaUserUpdateNameVacationBonus; }
    
    public void setAuxUpdateAnnualBonus(boolean b) { mbAuxUpdateAnnualBonus = b; }
    public void setAuxUpdateVacation(boolean b) { mbAuxUpdateVacation = b; }
    public void setAuxUpdateVacationBonus(boolean b) { mbAuxUpdateVacationBonus = b; }
    
    public boolean isAuxUpdateAnnualBonus() { return mbAuxUpdateAnnualBonus; }
    public boolean isAuxUpdateVacation() { return mbAuxUpdateVacation; }
    public boolean isAuxUpdateVacationBonus() { return mbAuxUpdateVacationBonus; }
    
    /*
     * Private methods
     */
    
    private int countBenefitAnnums(final int benefitType) {
        int count = 0;
        
        for (SDbEmployeeBenefitTablesAnnum child : maChildAnnums) {
            if (child.getPkBenefitTypeId() == benefitType) {
                count++;
            }
        }
        
        return count;
    }
    
    private void checkBenefitTablesAnnums(final SGuiSession session) throws Exception {
        int[] benefitTypes = SHrsBenefitUtils.createBenefitTypes();
        int[] benefitTables = new int[] { mnFkBenefitAnnualBonusId, mnFkBenefitVacationId, mnFkBenefitVacationBonusId };
        
        for (int type = 0; type < benefitTypes.length; type++) {
            int count = countBenefitAnnums(benefitTypes[type]);
            
            if (benefitTables[type] == SModSysConsts.HRS_BEN_ND) {
                // benefit table not set:
                
                if (count != 0) {
                    throw SHrsBenefitUtils.createBenefitAnnumsCountException(session, mnPkEmployeeId, benefitTypes[type], count, 0);
                }
            }
            else {
                // benefit table already set:
                
                if (count == 0) {
                    SDbBenefitTable benefitTable = (SDbBenefitTable) session.readRegistry(SModConsts.HRS_BEN, new int[] { benefitTables[type] });
                    maChildAnnums.addAll(benefitTable.createBenefitAnnums(MAX_ANNUMS));
                }
                else if (count != MAX_ANNUMS) {
                    throw SHrsBenefitUtils.createBenefitAnnumsCountException(session, mnPkEmployeeId, benefitTypes[type], count, MAX_ANNUMS);
                }
            }
        }
    }
    
    /**
     * Get all benefit annums of requested benefit type.
     * @param session GUI user session (for retrieving name of benefit type for exceptions).
     * @param benefitType Benefit type. Options defined in SModSysConsts.HRSS_TP_BEN_...
     * @return Array of benefit annums of requested benefit type.
     * @throws Exception Exception thrown if number of benefit annums differs from MAX_ANNUMS.
     */
    public ArrayList<SDbEmployeeBenefitTablesAnnum> getBenefitAnnums(final SGuiSession session, final int benefitType) throws Exception {
        ArrayList<SDbEmployeeBenefitTablesAnnum> benefitAnnums = new ArrayList<>();
        
        for (SDbEmployeeBenefitTablesAnnum child : maChildAnnums) {
            if (child.getPkBenefitTypeId() == benefitType) {
                benefitAnnums.add(child);
            }
        }
        
        if (benefitAnnums.size() != MAX_ANNUMS) {
            throw SHrsBenefitUtils.createBenefitAnnumsCountException(session, mnPkEmployeeId, benefitType, benefitAnnums.size(), MAX_ANNUMS);
        }
        
        return benefitAnnums;
    }
    
    /**
     * Check if benefit type is already set.
     * @param benefitType Benefit type. Options defined in SModSysConsts.HRSS_TP_BEN_...
     * @return 
     */
    public boolean isBenefitSet(final int benefitType) {
        boolean set = false;
        
        switch (benefitType) {
            case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                if (mnFkBenefitAnnualBonusId != SModSysConsts.HRS_BEN_ND) {
                    set = true;
                }
                break;
            case SModSysConsts.HRSS_TP_BEN_VAC:
                if (mnFkBenefitVacationId != SModSysConsts.HRS_BEN_ND) {
                    set = true;
                }
                break;
            case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                if (mnFkBenefitVacationBonusId != SModSysConsts.HRS_BEN_ND) {
                    set = true;
                }
                break;
            default:
                // nothing
        }
        
        return set;
    }
    
    /**
     * Compute wage factors.
     * @param session GUI user session.
     * @param computeWithoutCheckingCompleteness Compute without checking completeness of benefit tables annums.
     * @return <code>true</code> if wage factors were computed.
     * @throws java.lang.Exception
     */
    public boolean computeWageFactors(final SGuiSession session, final boolean computeWithoutCheckingCompleteness) throws Exception {
        boolean compute = true;
        
        if (!computeWithoutCheckingCompleteness) {
            int[] benefitTypes = SHrsBenefitUtils.createBenefitTypes();
            for (int benefitType : benefitTypes) {
                if (countBenefitAnnums(benefitType) != MAX_ANNUMS) {
                    compute = false;
                    break;
                }
            }
        }
        
        if (compute) {
            ArrayList<SDbEmployeeBenefitTablesAnnum> annualBonusAnnums = getBenefitAnnums(session, SModSysConsts.HRSS_TP_BEN_ANN_BON); // exception thrown if benefit annums incomplete
            ArrayList<SDbEmployeeBenefitTablesAnnum> vacationAnnums = getBenefitAnnums(session, SModSysConsts.HRSS_TP_BEN_VAC); // exception thrown if benefit annums incomplete
            ArrayList<SDbEmployeeBenefitTablesAnnum> vacationBonusAnnums = getBenefitAnnums(session, SModSysConsts.HRSS_TP_BEN_VAC_BON); // exception thrown if benefit annums incomplete

            maChildWageFactors.clear();

            for (int annum = 1; annum <= MAX_ANNUMS; annum++) {
                SDbEmployeeWageFactorAnnum wageFactor = new SDbEmployeeWageFactorAnnum();
                wageFactor.setPkEmployeeId(mnPkEmployeeId);
                wageFactor.setPkAnnumId(annum);
                wageFactor.setAnnualBonusDays(annualBonusAnnums.get(annum - 1).getBenefitDays());
                wageFactor.setVacationDays(vacationAnnums.get(annum - 1).getBenefitDays());
                wageFactor.setVacationBonusPct(vacationBonusAnnums.get(annum - 1).getBenefitBonusPct());
                wageFactor.computeWageFactor();
                maChildWageFactors.add(wageFactor);
            }
        }
        
        return compute;
    }
    
    /**
     * Assign benefit table.
     * @param session GUI user session.
     * @param benefitType Benefit type. Options defined in SModSysConsts.HRSS_TP_BEN_...
     * @param benefitTable Benefit table.
     * @param startingAnniversary Starting anniversary, from to 1 to MAX_ANNUMS.
     * @throws java.lang.Exception
     */
    public void assignBenefitTable(final SGuiSession session, final int benefitType, final int benefitTable, final int startingAnniversary) throws Exception {
        if (startingAnniversary < 1 || startingAnniversary > MAX_ANNUMS) {
            throw new Exception("Para '" + (String) session.readField(SModConsts.HRSU_EMP, new int[] { mnPkEmployeeId }, SDbRegistry.FIELD_NAME) + "' "
                    + "se está solicitando realizar la asignación a partir del aniversario " + startingAnniversary+ ".\n"
                    + "El rango valido de aniversarios es de 1 a " + MAX_ANNUMS + ".");
        }
        else {
            // get benefit annums for required benefit table only once:
            
            ArrayList<SDbEmployeeBenefitTablesAnnum> newBenefitAnnums = null;
            
            if (PreserveGlobalBenefitAnnumsMap) {
                if (GlobalBenefitAnnumsMap == null) {
                    GlobalBenefitAnnumsMap = new HashMap<>(); // instantiate only once during massive assignation
                }

                newBenefitAnnums = GlobalBenefitAnnumsMap.get(benefitTable);
            }
            
            if (newBenefitAnnums == null) {
                SDbBenefitTable reqBenefitTable = (SDbBenefitTable) session.readRegistry(SModConsts.HRS_BEN, new int[] { benefitTable });
                newBenefitAnnums = reqBenefitTable.createBenefitAnnums(MAX_ANNUMS);
                
                if (PreserveGlobalBenefitAnnumsMap) {
                    GlobalBenefitAnnumsMap.put(benefitTable, newBenefitAnnums);
                }
            }
            
            // assign benefit table:
            
            int count = countBenefitAnnums(benefitType);
            
            if (count == 0) {
                if (startingAnniversary != 1) {
                    throw new Exception("La tabla de prestaciones '" + (String) session.readField(SModConsts.HRSS_TP_BEN, new int[] { benefitType }, SDbRegistry.FIELD_NAME) + "' "
                            + "de '" + (String) session.readField(SModConsts.HRSU_EMP, new int[] { mnPkEmployeeId }, SDbRegistry.FIELD_NAME) + "' "
                            + "tiene " + count + " aniversarios, el aniversario inicial de asignación debe ser 1.");
                }
                
                maChildAnnums.addAll(newBenefitAnnums);
            }
            else {
                ArrayList<SDbEmployeeBenefitTablesAnnum> curBenefitAnnums = getBenefitAnnums(session, benefitType);
                
                for (int index = startingAnniversary - 1; index < MAX_ANNUMS; index++) {
                    maChildAnnums.set(maChildAnnums.indexOf(curBenefitAnnums.get(index)), newBenefitAnnums.get(index));
                }
            }
            
            switch (benefitType) {
                case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                    mnStartingAnniversaryAnnualBonus = startingAnniversary;
                    mnFkBenefitAnnualBonusId = benefitTable;
                    mnFkUserUpdateAnnualBonusId = session.getUser().getPkUserId();
                    mbAuxUpdateAnnualBonus = true;
                    break;
                    
                case SModSysConsts.HRSS_TP_BEN_VAC:
                    mnStartingAnniversaryVacation = startingAnniversary;
                    mnFkBenefitVacationId = benefitTable;
                    mnFkUserUpdateVacationId = session.getUser().getPkUserId();
                    mbAuxUpdateVacation = true;
                    break;
                    
                case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                    mnStartingAnniversaryVacationBonus = startingAnniversary;
                    mnFkBenefitVacationBonusId = benefitTable;
                    mnFkUserUpdateVacationBonusId = session.getUser().getPkUserId();
                    mbAuxUpdateVacationBonus = true;
                    break;
                    
                default:
                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n"
                            + "El tipo de prestaciones a asignar a '" + (String) session.readField(SModConsts.HRSU_EMP, new int[] { mnPkEmployeeId }, SDbRegistry.FIELD_NAME) + "' "
                            + "no es correcto (" + benefitType + ").");
            }
        }
    }
    
    /*
     * Overwritten methods
     */
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkEmployeeId = pk[0];
        mnPkBenefitsId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEmployeeId, mnPkBenefitsId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkEmployeeId = 0;
        mnPkBenefitsId = 0;
        mnStartingAnniversaryAnnualBonus = 0;
        mnStartingAnniversaryVacation = 0;
        mnStartingAnniversaryVacationBonus = 0;
        mbObsolete = false;
        mbDeleted = false;
        mnFkBenefitAnnualBonusId = SModSysConsts.HRS_BEN_ND;
        mnFkBenefitVacationId = SModSysConsts.HRS_BEN_ND;
        mnFkBenefitVacationBonusId = SModSysConsts.HRS_BEN_ND;
        mnFkUserUpdateAnnualBonusId = SUtilConsts.USR_NA_ID;
        mnFkUserUpdateVacationId = SUtilConsts.USR_NA_ID;
        mnFkUserUpdateVacationBonusId = SUtilConsts.USR_NA_ID;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserUpdateAnnualBonus = null;
        mtTsUserUpdateVacation = null;
        mtTsUserUpdateVacationBonus = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildAnnums = new ArrayList<>();
        maChildWageFactors = new ArrayList<>();
        
        msXtaBenefitNameAnnualBonus = "";
        msXtaBenefitNameVacation = "";
        msXtaBenefitNameVacationBonus = "";
        msXtaUserUpdateNameAnnualBonus = "";
        msXtaUserUpdateNameVacation = "";
        msXtaUserUpdateNameVacationBonus = "";
        
        mbAuxUpdateAnnualBonus = false;
        mbAuxUpdateVacation = false;
        mbAuxUpdateVacationBonus = false;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_emp = " + mnPkEmployeeId + " AND "
                + "id_ben = " + mnPkBenefitsId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_emp = " + pk[0] + " AND "
                + "id_ben = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkBenefitsId = 0;

        msSql = "SELECT COALESCE(MAX(id_ben), 0) + 1 "
                + ""
                + "FROM " + getSqlTable() + " "
                + "WHERE id_emp = " + mnPkEmployeeId + ";";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkBenefitsId = resultSet.getInt(1);
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
            mnPkBenefitsId = resultSet.getInt("id_ben");
            mnStartingAnniversaryAnnualBonus = resultSet.getInt("sta_ann_bon");
            mnStartingAnniversaryVacation = resultSet.getInt("sta_vac");
            mnStartingAnniversaryVacationBonus = resultSet.getInt("sta_vac_bon");
            mbObsolete = resultSet.getBoolean("b_obs");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkBenefitAnnualBonusId = resultSet.getInt("fk_ben_ann_bon");
            mnFkBenefitVacationId = resultSet.getInt("fk_ben_vac");
            mnFkBenefitVacationBonusId = resultSet.getInt("fk_ben_vac_bon");
            mnFkUserUpdateAnnualBonusId = resultSet.getInt("fk_usr_upd_ann_bon");
            mnFkUserUpdateVacationId = resultSet.getInt("fk_usr_upd_vac");
            mnFkUserUpdateVacationBonusId = resultSet.getInt("fk_usr_upd_vac_bon");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserUpdateAnnualBonus = resultSet.getTimestamp("ts_usr_upd_ann_bon");
            mtTsUserUpdateVacation = resultSet.getTimestamp("ts_usr_upd_vac");
            mtTsUserUpdateVacationBonus = resultSet.getTimestamp("ts_usr_upd_vac_bon");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // read as well child registries:
            
            if (!mbObsolete) {
                Statement statement = session.getStatement().getConnection().createStatement();

                msSql = "SELECT id_tp_ben, id_ann "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_BEN_ANN) + " "
                        + "WHERE id_emp = " + mnPkEmployeeId + " "
                        + "ORDER BY id_tp_ben, id_ann;";
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    SDbEmployeeBenefitTablesAnnum child = new SDbEmployeeBenefitTablesAnnum();
                    child.read(session, new int[] { mnPkEmployeeId, resultSet.getInt("id_tp_ben"), resultSet.getInt("id_ann") });
                    maChildAnnums.add(child);
                }

                msSql = "SELECT id_ann "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_WAGE_FAC_ANN) + " "
                        + "WHERE id_emp = " + mnPkEmployeeId + " "
                        + "ORDER BY id_ann;";
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    SDbEmployeeWageFactorAnnum child = new SDbEmployeeWageFactorAnnum();
                    child.read(session, new int[] { mnPkEmployeeId, resultSet.getInt("id_ann") });
                    maChildWageFactors.add(child);
                }
            }
            
            msXtaBenefitNameAnnualBonus = (String) session.readField(SModConsts.HRS_BEN, new int[] { mnFkBenefitAnnualBonusId }, SDbRegistry.FIELD_NAME);
            msXtaBenefitNameVacation = (String) session.readField(SModConsts.HRS_BEN, new int[] { mnFkBenefitVacationId}, SDbRegistry.FIELD_NAME);
            msXtaBenefitNameVacationBonus = (String) session.readField(SModConsts.HRS_BEN, new int[] { mnFkBenefitVacationBonusId}, SDbRegistry.FIELD_NAME);
            msXtaUserUpdateNameAnnualBonus = (String) session.readField(SModConsts.USRU_USR, new int[] { mnFkUserUpdateAnnualBonusId }, SDbRegistry.FIELD_NAME);
            msXtaUserUpdateNameVacation = (String) session.readField(SModConsts.USRU_USR, new int[] { mnFkUserUpdateVacationId}, SDbRegistry.FIELD_NAME);
            msXtaUserUpdateNameVacationBonus = (String) session.readField(SModConsts.USRU_USR, new int[] { mnFkUserUpdateVacationBonusId}, SDbRegistry.FIELD_NAME);
            
            // finish reading registry:
            
            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew && !mbObsolete) {
            mbAuxUpdateAnnualBonus = true;
            mbAuxUpdateVacation = true;
            mbAuxUpdateVacationBonus = true;
        }
        
        checkBenefitTablesAnnums(session);
        
        boolean wageFactorsComputed = false;
        
        if (!mbObsolete) {
            wageFactorsComputed = computeWageFactors(session, false);
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkEmployeeId + ", " + 
                    mnPkBenefitsId + ", " + 
                    mnStartingAnniversaryAnnualBonus + ", " + 
                    mnStartingAnniversaryVacation + ", " + 
                    mnStartingAnniversaryVacationBonus + ", " + 
                    (mbObsolete ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkBenefitAnnualBonusId + ", " + 
                    mnFkBenefitVacationId + ", " + 
                    mnFkBenefitVacationBonusId + ", " + 
                    mnFkUserUpdateAnnualBonusId + ", " + 
                    mnFkUserUpdateVacationId + ", " + 
                    mnFkUserUpdateVacationBonusId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            if (mbAuxUpdateAnnualBonus) {
                mnFkUserUpdateAnnualBonusId = mnFkUserUpdateId;
            }
            
            if (mbAuxUpdateVacation) {
                mnFkUserUpdateVacationId = mnFkUserUpdateId;
            }
            
            if (mbAuxUpdateVacationBonus) {
                mnFkUserUpdateVacationBonusId = mnFkUserUpdateId;
            }

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_emp = " + mnPkEmployeeId + ", " +
                    //"id_ben = " + mnPkBenefitsId + ", " +
                    "b_obs = " + (mbObsolete ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    
                    (!mbAuxUpdateAnnualBonus ? "" :
                    "sta_ann_bon = " + mnStartingAnniversaryAnnualBonus + ", " +
                    "fk_ben_ann_bon = " + mnFkBenefitAnnualBonusId + ", " +
                    "fk_usr_upd_ann_bon = " + mnFkUserUpdateAnnualBonusId + ", " +
                    "ts_usr_upd_ann_bon = " + "NOW()" + ", ") +
                    
                    (!mbAuxUpdateVacation ? "" :
                    "sta_vac = " + mnStartingAnniversaryVacation + ", " +
                    "fk_ben_vac = " + mnFkBenefitVacationId + ", " +
                    "fk_usr_upd_vac = " + mnFkUserUpdateVacationId + ", " +
                    "ts_usr_upd_vac = " + "NOW()" + ", ") +
                    
                    (!mbAuxUpdateVacationBonus ? "" :
                    "sta_vac_bon = " + mnStartingAnniversaryVacationBonus + ", " +
                    "fk_ben_vac_bon = " + mnFkBenefitVacationBonusId + ", " +
                    "fk_usr_upd_vac_bon = " + mnFkUserUpdateVacationBonusId + ", " +
                    "ts_usr_upd_vac_bon = " + "NOW()" + ", ") +
                    
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // save as well child registries:
        
        if (!mbObsolete) {
            if (mbAuxUpdateAnnualBonus) {
                msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_BEN_ANN) + " "
                        + "WHERE id_emp = " + mnPkEmployeeId + " AND "
                        + "id_tp_ben = " + SModSysConsts.HRSS_TP_BEN_ANN_BON + " AND "
                        + "id_ann >= " + mnStartingAnniversaryAnnualBonus + ";";
                session.getStatement().execute(msSql);
            }

            if (mbAuxUpdateVacation) {
                msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_BEN_ANN) + " "
                        + "WHERE id_emp = " + mnPkEmployeeId + " AND "
                        + "id_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC + " AND "
                        + "id_ann >= " + mnStartingAnniversaryVacation + ";";
                session.getStatement().execute(msSql);
            }

            if (mbAuxUpdateVacationBonus) {
                msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_BEN_ANN) + " "
                        + "WHERE id_emp = " + mnPkEmployeeId + " AND "
                        + "id_tp_ben = " + SModSysConsts.HRSS_TP_BEN_VAC_BON + " AND "
                        + "id_ann >= " + mnStartingAnniversaryVacationBonus + ";";
                session.getStatement().execute(msSql);
            }

            if (mbAuxUpdateAnnualBonus || mbAuxUpdateVacation || mbAuxUpdateVacationBonus) {
                for (SDbEmployeeBenefitTablesAnnum child : maChildAnnums) {
                    boolean save = false;

                    switch (child.getPkBenefitTypeId()) {
                        case SModSysConsts.HRSS_TP_BEN_ANN_BON:
                            if (mbAuxUpdateAnnualBonus && child.getPkAnnumId() >= mnStartingAnniversaryAnnualBonus) {
                                save = true;
                            }
                            break;
                        case SModSysConsts.HRSS_TP_BEN_VAC:
                            if (mbAuxUpdateVacation && child.getPkAnnumId() >= mnStartingAnniversaryVacation) {
                                save = true;
                            }
                            break;
                        case SModSysConsts.HRSS_TP_BEN_VAC_BON:
                            if (mbAuxUpdateVacationBonus && child.getPkAnnumId() >= mnStartingAnniversaryVacationBonus) {
                                save = true;
                            }
                            break;
                        default:
                            // nothing
                    }

                    if (save) {
                        child.setPkEmployeeId(mnPkEmployeeId);
                        child.setRegistryNew(true);
                        child.save(session);
                    }
                }
            }
            
            if (wageFactorsComputed) {
                msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_EMP_WAGE_FAC_ANN) + " "
                        + "WHERE id_emp = " + mnPkEmployeeId + ";";
                session.getStatement().execute(msSql);

                for (SDbEmployeeWageFactorAnnum child : maChildWageFactors) {
                    child.setPkEmployeeId(mnPkEmployeeId);
                    child.setRegistryNew(true);
                    child.save(session);
                }
            }
        }
        
        // finish saving registry:
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbEmployeeBenefitTables clone() throws CloneNotSupportedException {
        SDbEmployeeBenefitTables registry = new SDbEmployeeBenefitTables();

        registry.setPkEmployeeId(this.getPkEmployeeId());
        registry.setPkBenefitsId(this.getPkBenefitsId());
        registry.setStartingAnniversaryAnnualBonus(this.getStartingAnniversaryAnnualBonus());
        registry.setStartingAnniversaryVacation(this.getStartingAnniversaryVacation());
        registry.setStartingAnniversaryVacationBonus(this.getStartingAnniversaryVacationBonus());
        registry.setObsolete(this.isObsolete());
        registry.setDeleted(this.isDeleted());
        registry.setFkBenefitAnnualBonusId(this.getFkBenefitAnnualBonusId());
        registry.setFkBenefitVacationId(this.getFkBenefitVacationId());
        registry.setFkBenefitVacationBonusId(this.getFkBenefitVacationBonusId());
        registry.setFkUserUpdateAnnualBonusId(this.getFkUserUpdateAnnualBonusId());
        registry.setFkUserUpdateVacationId(this.getFkUserUpdateVacationId());
        registry.setFkUserUpdateVacationBonusId(this.getFkUserUpdateVacationBonusId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserUpdateAnnualBonus(this.getTsUserUpdateAnnualBonus());
        registry.setTsUserUpdateVacation(this.getTsUserUpdateVacation());
        registry.setTsUserUpdateVacationBonus(this.getTsUserUpdateVacationBonus());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (SDbEmployeeBenefitTablesAnnum child : maChildAnnums) {
            registry.getChildAnnums().add(child.clone());
        }
        
        for (SDbEmployeeWageFactorAnnum child : maChildWageFactors) {
            registry.getChildWageFactors().add(child.clone());
        }
        
        registry.setXtaBenefitNameAnnualBonus(this.getXtaBenefitNameAnnualBonus());
        registry.setXtaBenefitNameVacation(this.getXtaBenefitNameVacation());
        registry.setXtaBenefitNameVacationBonus(this.getXtaBenefitNameVacationBonus());
        
        registry.setAuxUpdateAnnualBonus(this.isAuxUpdateAnnualBonus());
        registry.setAuxUpdateVacation(this.isAuxUpdateVacation());
        registry.setAuxUpdateVacationBonus(this.isAuxUpdateVacationBonus());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
