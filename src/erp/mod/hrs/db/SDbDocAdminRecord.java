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
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbDocAdminRecord extends SDbRegistryUser {
    
    protected int mnPkDocAdminRecordId;
    protected int mnNumber;
    protected String msLocality;
    protected Date mtRecordTsStart;
    protected Date mtRecordTsEnd;
    protected String msBreachAbstract;
    protected String msBreachDescrip;
    protected String msOffenderComments1;
    protected String msOffenderComments2;
    protected String msFilevaultId;
    protected Date mtFilevaultTs_n;
    protected String msFileType;
    protected boolean mbOffenderUnionized;
    protected boolean mbHumanResourcesWitness1;
    protected boolean mbHumanResourcesWitness2;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkDocumentId;
    protected int mnFkCompanyBranchId;
    protected int mnFkCompanyBranchAddressId;
    protected int mnFkStateId;
    protected int mnFkEmployeeOffenderId;
    protected int mnFkEmployeeBossId;
    protected int mnFkEmployeeUnionId_n;
    protected int mnFkEmployeeHumanResourcesId;
    protected int mnFkEmployeeWitness1Id;
    protected int mnFkEmployeeWitness2Id;
    protected int mnFkHumanResourcesDepartmentId;
    protected int mnFkOffenderDepartmentId;
    protected int mnFkOffenderPositionId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbDocAdminRecordPreceptSubsection> maChildPreceptSubsections;
    
    protected String msOldFilevaultId;
    
    public SDbDocAdminRecord() {
        super(SModConsts.HRS_DOC_ADM_REC);
    }
    
    public void setPkDocAdminRecordId(int n) { mnPkDocAdminRecordId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setLocality(String s) { msLocality = s; }
    public void setRecordTsStart(Date t) { mtRecordTsStart = t; }
    public void setRecordTsEnd(Date t) { mtRecordTsEnd = t; }
    public void setBreachAbstract(String s) { msBreachAbstract = s; }
    public void setBreachDescrip(String s) { msBreachDescrip = s; }
    public void setOffenderComments1(String s) { msOffenderComments1 = s; }
    public void setOffenderComments2(String s) { msOffenderComments2 = s; }
    public void setFilevaultId(String s) { msFilevaultId = s; }
    public void setFilevaultTs_n(Date t) { mtFilevaultTs_n = t; }
    public void setFileType(String s) { msFileType = s; }
    public void setOffenderUnionized(boolean b) { mbOffenderUnionized = b; }
    public void setHumanResourcesWitness1(boolean b) { mbHumanResourcesWitness1 = b; }
    public void setHumanResourcesWitness2(boolean b) { mbHumanResourcesWitness2 = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkDocumentId(int n) { mnFkDocumentId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkCompanyBranchAddressId(int n) { mnFkCompanyBranchAddressId = n; }
    public void setFkStateId(int n) { mnFkStateId = n; }
    public void setFkEmployeeOffenderId(int n) { mnFkEmployeeOffenderId = n; }
    public void setFkEmployeeBossId(int n) { mnFkEmployeeBossId = n; }
    public void setFkEmployeeUnionId_n(int n) { mnFkEmployeeUnionId_n = n; }
    public void setFkEmployeeHumanResourcesId(int n) { mnFkEmployeeHumanResourcesId = n; }
    public void setFkEmployeeWitness1Id(int n) { mnFkEmployeeWitness1Id = n; }
    public void setFkEmployeeWitness2Id(int n) { mnFkEmployeeWitness2Id = n; }
    public void setFkHumanResourcesDepartmentId(int n) { mnFkHumanResourcesDepartmentId = n; }
    public void setFkOffenderDepartmentId(int n) { mnFkOffenderDepartmentId = n; }
    public void setFkOffenderPositionId(int n) { mnFkOffenderPositionId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public int getPkDocAdminRecordId() { return mnPkDocAdminRecordId; }
    public int getNumber() { return mnNumber; }
    public String getLocality() { return msLocality; }
    public Date getRecordTsStart() { return mtRecordTsStart; }
    public Date getRecordTsEnd() { return mtRecordTsEnd; }
    public String getBreachAbstract() { return msBreachAbstract; }
    public String getBreachDescrip() { return msBreachDescrip; }
    public String getOffenderComments1() { return msOffenderComments1; }
    public String getOffenderComments2() { return msOffenderComments2; }
    public String getFilevaultId() { return msFilevaultId; }
    public Date getFilevaultTs_n() { return mtFilevaultTs_n; }
    public String getFileType() { return msFileType; }
    public boolean isOffenderUnionized() { return mbOffenderUnionized; }
    public boolean isHumanResourcesWitness1() { return mbHumanResourcesWitness1; }
    public boolean isHumanResourcesWitness2() { return mbHumanResourcesWitness2; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkDocumentId() { return mnFkDocumentId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkCompanyBranchAddressId() { return mnFkCompanyBranchAddressId; }
    public int getFkStateId() { return mnFkStateId; }
    public int getFkEmployeeOffenderId() { return mnFkEmployeeOffenderId; }
    public int getFkEmployeeBossId() { return mnFkEmployeeBossId; }
    public int getFkEmployeeUnionId_n() { return mnFkEmployeeUnionId_n; }
    public int getFkEmployeeHumanResourcesId() { return mnFkEmployeeHumanResourcesId; }
    public int getFkEmployeeWitness1Id() { return mnFkEmployeeWitness1Id; }
    public int getFkEmployeeWitness2Id() { return mnFkEmployeeWitness2Id; }
    public int getFkHumanResourcesDepartmentId() { return mnFkHumanResourcesDepartmentId; }
    public int getFkOffenderDepartmentId() { return mnFkOffenderDepartmentId; }
    public int getFkOffenderPositionId() { return mnFkOffenderPositionId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<SDbDocAdminRecordPreceptSubsection> getChildPreceptSubsections() { return maChildPreceptSubsections; }
    
    public void setOldFilevaultId(String s) { msOldFilevaultId = s; }
    
    public String getOldFilevaultId() { return msOldFilevaultId; }
    
    public void setPreceptSubsectionKeys(final ArrayList<int[]> keys) {
        maChildPreceptSubsections.clear();
        
        for (int[] key : keys) {
            SDbDocAdminRecordPreceptSubsection child = new SDbDocAdminRecordPreceptSubsection();
            
            child.setPkDocAdminRecordId(mnPkDocAdminRecordId);
            child.setPkPreceptId(key[0]);
            child.setPkSectionId(key[1]);
            child.setPkSubsectionId(key[2]);
            
            maChildPreceptSubsections.add(child);
        }
    }

    public ArrayList<int[]> getPreceptSubsectionKeys() {
        ArrayList<int[]> keys = new ArrayList<>();
        
        for (SDbDocAdminRecordPreceptSubsection child : maChildPreceptSubsections) {
            keys.add(child.getPreceptSubsectionKey());
        }
        
        return keys;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDocAdminRecordId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDocAdminRecordId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDocAdminRecordId = 0;
        mnNumber = 0;
        msLocality = "";
        mtRecordTsStart = null;
        mtRecordTsEnd = null;
        msBreachAbstract = "";
        msBreachDescrip = null;
        msOffenderComments1 = null;
        msOffenderComments2 = null;
        msFilevaultId = "";
        mtFilevaultTs_n = null;
        msFileType = "";
        mbOffenderUnionized = false;
        mbHumanResourcesWitness1 = false;
        mbHumanResourcesWitness2 = false;
        mbDeleted = false;
        mnFkDocumentId = 0;
        mnFkCompanyBranchId = 0;
        mnFkCompanyBranchAddressId = 0;
        mnFkStateId = 0;
        mnFkEmployeeOffenderId = 0;
        mnFkEmployeeBossId = 0;
        mnFkEmployeeUnionId_n = 0;
        mnFkEmployeeHumanResourcesId = 0;
        mnFkEmployeeWitness1Id = 0;
        mnFkEmployeeWitness2Id = 0;
        mnFkHumanResourcesDepartmentId = 0;
        mnFkOffenderDepartmentId = 0;
        mnFkOffenderPositionId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildPreceptSubsections = new ArrayList<>();
        
        msOldFilevaultId = "";
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_doc_adm_rec = " + mnPkDocAdminRecordId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_doc_adm_rec = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkDocAdminRecordId = 0;

        msSql = "SELECT COALESCE(MAX(id_doc_adm_rec), 0) + 1 "
                + "FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkDocAdminRecordId = resultSet.getInt(1);
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
            mnPkDocAdminRecordId = resultSet.getInt("id_doc_adm_rec");
            mnNumber = resultSet.getInt("num");
            msLocality = resultSet.getString("locality");
            mtRecordTsStart = resultSet.getTimestamp("rec_dt_sta");
            mtRecordTsEnd = resultSet.getTimestamp("rec_dt_end");
            msBreachAbstract = resultSet.getString("breach_abstract");
            msBreachDescrip = resultSet.getString("breach_descrip");
            msOffenderComments1 = resultSet.getString("offender_cmts_1");
            msOffenderComments2 = resultSet.getString("offender_cmts_2");
            msFilevaultId = resultSet.getString("filevault_id");
            mtFilevaultTs_n = resultSet.getTimestamp("filevault_ts_n");
            msFileType = resultSet.getString("file_type");
            mbOffenderUnionized = resultSet.getBoolean("b_offender_uni");
            mbHumanResourcesWitness1 = resultSet.getBoolean("b_hrs_witness_1");
            mbHumanResourcesWitness2 = resultSet.getBoolean("b_hrs_witness_2");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkDocumentId = resultSet.getInt("fk_doc");
            mnFkCompanyBranchId = resultSet.getInt("fk_cob");
            mnFkCompanyBranchAddressId = resultSet.getInt("fk_cob_add");
            mnFkStateId = resultSet.getInt("fk_sta");
            mnFkEmployeeOffenderId = resultSet.getInt("fk_emp_offender");
            mnFkEmployeeBossId = resultSet.getInt("fk_emp_boss");
            mnFkEmployeeUnionId_n = resultSet.getInt("fk_emp_union_n");
            mnFkEmployeeHumanResourcesId = resultSet.getInt("fk_emp_hrs");
            mnFkEmployeeWitness1Id = resultSet.getInt("fk_emp_witness_1");
            mnFkEmployeeWitness2Id = resultSet.getInt("fk_emp_witness_2");
            mnFkHumanResourcesDepartmentId = resultSet.getInt("fk_hrs_dep");
            mnFkOffenderDepartmentId = resultSet.getInt("fk_offender_dep");
            mnFkOffenderPositionId = resultSet.getInt("fk_offender_pos");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // read children:
            
            Statement statement = session.getStatement().getConnection().createStatement();
                    
            msSql = "SELECT id_prec, id_sec, id_subsec "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DOC_ADM_REC_PREC_SUBSEC) + " "
                    + "WHERE id_doc_adm_rec = " + mnPkDocAdminRecordId + " "
                    + "ORDER BY sort, id_prec, id_sec, id_subsec;";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbDocAdminRecordPreceptSubsection child = new SDbDocAdminRecordPreceptSubsection();
                child.read(session, new int[] { mnPkDocAdminRecordId, resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
                maChildPreceptSubsections.add(child);
            }
            
            // preserve old values:
            
            msOldFilevaultId = msFilevaultId;
            
            // finish reading:

            mbRegistryNew = false;
        }

        mnQueryResultId = SDbConsts.READ_OK;
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        // get next number:
        
        if (mnNumber == 0) {
            mnNumber = getNextNumber(session);
        }
        
        // normalize timestamp of filevalut:
        
        String filevaultTs_n = "";
        
        if (msFilevaultId.isEmpty()) {
            filevaultTs_n = "NULL";
            msFileType = "";
        }
        else if (!msFilevaultId.equals(msOldFilevaultId)) {
            filevaultTs_n = "NOW()";
        }
        
        // save registry:

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDocAdminRecordId + ", " + 
                    mnNumber + ", " + 
                    "'" + msLocality + "', " + 
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtRecordTsStart) + "'" + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtRecordTsEnd) + "'" + ", " + 
                    "'" + msBreachAbstract + "', " + 
                    "'" + msBreachDescrip + "', " + 
                    "'" + msOffenderComments1 + "', " + 
                    "'" + msOffenderComments2 + "', " + 
                    "'" + msFilevaultId + "', " + 
                    filevaultTs_n + ", " + 
                    "'" + msFileType + "', " + 
                    (mbOffenderUnionized ? 1 : 0) + ", " + 
                    (mbHumanResourcesWitness1 ? 1 : 0) + ", " + 
                    (mbHumanResourcesWitness2 ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkDocumentId + ", " + 
                    mnFkCompanyBranchId + ", " + 
                    mnFkCompanyBranchAddressId + ", " + 
                    mnFkStateId + ", " + 
                    mnFkEmployeeOffenderId + ", " + 
                    mnFkEmployeeBossId + ", " + 
                    (mnFkEmployeeUnionId_n == 0 ? "NULL" : mnFkEmployeeUnionId_n) + ", " + 
                    mnFkEmployeeHumanResourcesId + ", " + 
                    mnFkEmployeeWitness1Id + ", " + 
                    mnFkEmployeeWitness2Id + ", " + 
                    mnFkHumanResourcesDepartmentId + ", " + 
                    mnFkOffenderDepartmentId + ", " + 
                    mnFkOffenderPositionId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_doc_adm_rec = " + mnPkDocAdminRecordId + ", " +
                    "num = " + mnNumber + ", " +
                    "locality = '" + msLocality + "', " +
                    "rec_dt_sta = " + "'" + SLibUtils.DbmsDateFormatDatetime.format(mtRecordTsStart) + "'" + ", " +
                    "rec_dt_end = " + "'" + SLibUtils.DbmsDateFormatDatetime.format(mtRecordTsEnd) + "'" + ", " +
                    "breach_abstract = '" + msBreachAbstract + "', " +
                    "breach_descrip = '" + msBreachDescrip + "', " +
                    "offender_cmts_1 = '" + msOffenderComments1 + "', " +
                    "offender_cmts_2 = '" + msOffenderComments2 + "', " +
                    "filevault_id = '" + msFilevaultId + "', " +
                    "filevault_ts_n = " + filevaultTs_n + ", " +
                    "file_type = '" + msFileType + "', " +
                    "b_offender_uni = " + (mbOffenderUnionized ? 1 : 0) + ", " +
                    "b_hrs_witness_1 = " + (mbHumanResourcesWitness1 ? 1 : 0) + ", " +
                    "b_hrs_witness_2 = " + (mbHumanResourcesWitness2 ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_doc = " + mnFkDocumentId + ", " +
                    "fk_cob = " + mnFkCompanyBranchId + ", " +
                    "fk_cob_add = " + mnFkCompanyBranchAddressId + ", " +
                    "fk_sta = " + mnFkStateId + ", " +
                    "fk_emp_offender = " + mnFkEmployeeOffenderId + ", " +
                    "fk_emp_boss = " + mnFkEmployeeBossId + ", " +
                    "fk_emp_union_n = " + (mnFkEmployeeUnionId_n == 0 ? "NULL" : mnFkEmployeeUnionId_n) + ", " +
                    "fk_emp_hrs = " + mnFkEmployeeHumanResourcesId + ", " +
                    "fk_emp_witness_1 = " + mnFkEmployeeWitness1Id + ", " +
                    "fk_emp_witness_2 = " + mnFkEmployeeWitness2Id + ", " +
                    "fk_hrs_dep = " + mnFkHumanResourcesDepartmentId + ", " +
                    "fk_offender_dep = " + mnFkOffenderDepartmentId + ", " +
                    "fk_offender_pos = " + mnFkOffenderPositionId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // delete children:
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DOC_ADM_REC_PREC_SUBSEC) + " "
                + "WHERE id_doc_adm_rec = " + mnPkDocAdminRecordId + " ";
        
        session.getStatement().execute(msSql);
        
        // save children:
        
        int sortingPos = 0;
        for (SDbDocAdminRecordPreceptSubsection child : maChildPreceptSubsections) {
            child.setRegistryNew(true);
            child.setPkDocAdminRecordId(mnPkDocAdminRecordId);
            child.setSortingPos(++sortingPos);
            child.save(session);
        }
        
        // finish reading:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbDocAdminRecord clone() throws CloneNotSupportedException {
        SDbDocAdminRecord registry = new SDbDocAdminRecord();
        
        registry.setPkDocAdminRecordId(this.getPkDocAdminRecordId());
        registry.setNumber(this.getNumber());
        registry.setLocality(this.getLocality());
        registry.setRecordTsStart(this.getRecordTsStart());
        registry.setRecordTsEnd(this.getRecordTsEnd());
        registry.setBreachAbstract(this.getBreachAbstract());
        registry.setBreachDescrip(this.getBreachDescrip());
        registry.setOffenderComments1(this.getOffenderComments1());
        registry.setOffenderComments2(this.getOffenderComments2());
        registry.setFilevaultId(this.getFilevaultId());
        registry.setFilevaultTs_n(this.getFilevaultTs_n());
        registry.setFileType(this.getFileType());
        registry.setOffenderUnionized(this.isOffenderUnionized());
        registry.setHumanResourcesWitness1(this.isHumanResourcesWitness1());
        registry.setHumanResourcesWitness2(this.isHumanResourcesWitness2());
        registry.setDeleted(this.isDeleted());
        registry.setFkDocumentId(this.getFkDocumentId());
        registry.setFkCompanyBranchId(this.getFkCompanyBranchId());
        registry.setFkCompanyBranchAddressId(this.getFkCompanyBranchAddressId());
        registry.setFkStateId(this.getFkStateId());
        registry.setFkEmployeeOffenderId(this.getFkEmployeeOffenderId());
        registry.setFkEmployeeBossId(this.getFkEmployeeBossId());
        registry.setFkEmployeeUnionId_n(this.getFkEmployeeUnionId_n());
        registry.setFkEmployeeHumanResourcesId(this.getFkEmployeeHumanResourcesId());
        registry.setFkEmployeeWitness1Id(this.getFkEmployeeWitness1Id());
        registry.setFkEmployeeWitness2Id(this.getFkEmployeeWitness2Id());
        registry.setFkHumanResourcesDepartmentId(this.getFkHumanResourcesDepartmentId());
        registry.setFkOffenderDepartmentId(this.getFkOffenderDepartmentId());
        registry.setFkOffenderPositionId(this.getFkOffenderPositionId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        for (SDbDocAdminRecordPreceptSubsection child : maChildPreceptSubsections) {
            registry.getChildPreceptSubsections().add(child.clone());
        }

        registry.setOldFilevaultId(this.getOldFilevaultId());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    public static int getNextNumber(SGuiSession session) throws SQLException, Exception {
        int nextNumber = 0;

        String sql = "SELECT COALESCE(MAX(num), 0) + 1 "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DOC_ADM_REC) + " "
                + "WHERE NOT b_del;";
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                nextNumber = resultSet.getInt(1);
            }
        }
        
        return nextNumber;
    }
}
