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
public class SDbDocBreach extends SDbRegistryUser {
    
    protected int mnPkDocBreachId;
    protected int mnNumber;
    protected Date mtBreachTs;
    protected String msBreachAbstract;
    protected String msBreachDescrip;
    protected String msEndingNotes;
    protected String msFilevaultId;
    protected Date mtFilevaultTs_n;
    protected String msFileType;
    protected boolean mbOffenderUnionized;
    protected boolean mbOffenderSigned;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkDocumentId;
    protected int mnFkCompanyBranchId;
    protected int mnFkEmployeeAuthorId;
    protected int mnFkEmployeeOffenderId;
    protected int mnFkEmployeeBossId;
    protected int mnFkEmployeeUnionId_n;
    protected int mnFkOffenderDepartmentId;
    protected int mnFkOffenderPositionId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<SDbDocBreachPreceptSubsection> maChildPreceptSubsections;
    
    protected String msOldFilevaultId;
    
    public SDbDocBreach() {
        super(SModConsts.HRS_DOC_BREACH);
    }
    
    public void setPkDocBreachId(int n) { mnPkDocBreachId = n; }
    public void setNumber(int n) { mnNumber = n; }
    public void setBreachTs(Date t) { mtBreachTs = t; }
    public void setBreachAbstract(String s) { msBreachAbstract = s; }
    public void setBreachDescrip(String s) { msBreachDescrip = s; }
    public void setEndingNotes(String s) { msEndingNotes = s; }
    public void setFilevaultId(String s) { msFilevaultId = s; }
    public void setFilevaultTs_n(Date t) { mtFilevaultTs_n = t; }
    public void setFileType(String s) { msFileType = s; }
    public void setOffenderUnionized(boolean b) { mbOffenderUnionized = b; }
    public void setOffenderSigned(boolean b) { mbOffenderSigned = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkDocumentId(int n) { mnFkDocumentId = n; }
    public void setFkCompanyBranchId(int n) { mnFkCompanyBranchId = n; }
    public void setFkEmployeeAuthorId(int n) { mnFkEmployeeAuthorId = n; }
    public void setFkEmployeeOffenderId(int n) { mnFkEmployeeOffenderId = n; }
    public void setFkEmployeeBossId(int n) { mnFkEmployeeBossId = n; }
    public void setFkEmployeeUnionId_n(int n) { mnFkEmployeeUnionId_n = n; }
    public void setFkOffenderDepartmentId(int n) { mnFkOffenderDepartmentId = n; }
    public void setFkOffenderPositionId(int n) { mnFkOffenderPositionId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkDocBreachId() { return mnPkDocBreachId; }
    public int getNumber() { return mnNumber; }
    public Date getBreachTs() { return mtBreachTs; }
    public String getBreachAbstract() { return msBreachAbstract; }
    public String getBreachDescrip() { return msBreachDescrip; }
    public String getEndingNotes() { return msEndingNotes; }
    public String getFilevaultId() { return msFilevaultId; }
    public Date getFilevaultTs_n() { return mtFilevaultTs_n; }
    public String getFileType() { return msFileType; }
    public boolean isOffenderUnionized() { return mbOffenderUnionized; }
    public boolean isOffenderSigned() { return mbOffenderSigned; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkDocumentId() { return mnFkDocumentId; }
    public int getFkCompanyBranchId() { return mnFkCompanyBranchId; }
    public int getFkEmployeeAuthorId() { return mnFkEmployeeAuthorId; }
    public int getFkEmployeeOffenderId() { return mnFkEmployeeOffenderId; }
    public int getFkEmployeeBossId() { return mnFkEmployeeBossId; }
    public int getFkEmployeeUnionId_n() { return mnFkEmployeeUnionId_n; }
    public int getFkOffenderDepartmentId() { return mnFkOffenderDepartmentId; }
    public int getFkOffenderPositionId() { return mnFkOffenderPositionId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<SDbDocBreachPreceptSubsection> getChildPreceptSubsections() { return maChildPreceptSubsections; }

    public void setOldFilevaultId(String s) { msOldFilevaultId = s; }
    
    public String getOldFilevaultId() { return msOldFilevaultId; }
    
    public void setPreceptSubsectionKeys(final ArrayList<int[]> keys) {
        maChildPreceptSubsections.clear();
        
        for (int[] key : keys) {
            SDbDocBreachPreceptSubsection child = new SDbDocBreachPreceptSubsection();
            
            child.setPkDocBreachId(mnPkDocBreachId);
            child.setPkPreceptId(key[0]);
            child.setPkSectionId(key[1]);
            child.setPkSubsectionId(key[2]);
            
            maChildPreceptSubsections.add(child);
        }
    }

    public ArrayList<int[]> getPreceptSubsectionKeys() {
        ArrayList<int[]> keys = new ArrayList<>();
        
        for (SDbDocBreachPreceptSubsection child : maChildPreceptSubsections) {
            keys.add(child.getPreceptSubsectionKey());
        }
        
        return keys;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDocBreachId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDocBreachId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDocBreachId = 0;
        mnNumber = 0;
        mtBreachTs = null;
        msBreachAbstract = "";
        msBreachDescrip = "";
        msEndingNotes = "";
        msFilevaultId = "";
        mtFilevaultTs_n = null;
        msFileType = "";
        mbOffenderUnionized = false;
        mbOffenderSigned = false;
        mbDeleted = false;
        mnFkDocumentId = 0;
        mnFkCompanyBranchId = 0;
        mnFkEmployeeAuthorId = 0;
        mnFkEmployeeOffenderId = 0;
        mnFkEmployeeBossId = 0;
        mnFkEmployeeUnionId_n = 0;
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
        return "WHERE id_doc_breach = " + mnPkDocBreachId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_doc_breach = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkDocBreachId = 0;

        msSql = "SELECT COALESCE(MAX(id_doc_breach), 0) + 1 "
                + "FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkDocBreachId = resultSet.getInt(1);
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
            mnPkDocBreachId = resultSet.getInt("id_doc_breach");
            mnNumber = resultSet.getInt("num");
            mtBreachTs = resultSet.getTimestamp("breach_ts");
            msBreachAbstract = resultSet.getString("breach_abstract");
            msBreachDescrip = resultSet.getString("breach_descrip");
            msEndingNotes = resultSet.getString("ending_notes");
            msFilevaultId = resultSet.getString("filevault_id");
            mtFilevaultTs_n = resultSet.getTimestamp("filevault_ts_n");
            msFileType = resultSet.getString("file_type");
            mbOffenderUnionized = resultSet.getBoolean("b_offender_uni");
            mbOffenderSigned = resultSet.getBoolean("b_offender_sign");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkDocumentId = resultSet.getInt("fk_doc");
            mnFkCompanyBranchId = resultSet.getInt("fk_cob");
            mnFkEmployeeAuthorId = resultSet.getInt("fk_emp_author");
            mnFkEmployeeOffenderId = resultSet.getInt("fk_emp_offender");
            mnFkEmployeeBossId = resultSet.getInt("fk_emp_boss");
            mnFkEmployeeUnionId_n = resultSet.getInt("fk_emp_union_n");
            mnFkOffenderDepartmentId = resultSet.getInt("fk_offender_dep");
            mnFkOffenderPositionId = resultSet.getInt("fk_offender_pos");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // read children:
            
            Statement statement = session.getStatement().getConnection().createStatement();
                    
            msSql = "SELECT id_prec, id_sec, id_subsec "
                    + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DOC_BREACH_PREC_SUBSEC) + " "
                    + "WHERE id_doc_breach = " + mnPkDocBreachId + " "
                    + "ORDER BY sort, id_prec, id_sec, id_subsec;";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                SDbDocBreachPreceptSubsection child = new SDbDocBreachPreceptSubsection();
                child.read(session, new int[] { mnPkDocBreachId, resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
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
                    mnPkDocBreachId + ", " + 
                    mnNumber + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDatetime.format(mtBreachTs) + "'" + ", " + 
                    "'" + msBreachAbstract + "', " + 
                    "'" + msBreachDescrip + "', " + 
                    "'" + msEndingNotes + "', " + 
                    "'" + msFilevaultId + "', " + 
                    filevaultTs_n + ", " + 
                    "'" + msFileType + "', " + 
                    (mbOffenderUnionized ? 1 : 0) + ", " + 
                    (mbOffenderSigned ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkDocumentId + ", " + 
                    mnFkCompanyBranchId + ", " + 
                    mnFkEmployeeAuthorId + ", " + 
                    mnFkEmployeeOffenderId + ", " + 
                    mnFkEmployeeBossId + ", " + 
                    (mnFkEmployeeUnionId_n == 0 ? "NULL" : mnFkEmployeeUnionId_n) + ", " + 
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
                    //"id_doc_breach = " + mnPkDocBreachId + ", " +
                    "num = " + mnNumber + ", " +
                    "breach_ts = " + "'" + SLibUtils.DbmsDateFormatDatetime.format(mtBreachTs) + "'" + ", " +
                    "breach_abstract = '" + msBreachAbstract + "', " +
                    "breach_descrip = '" + msBreachDescrip + "', " +
                    "ending_notes = '" + msEndingNotes + "', " +
                    "filevault_id = '" + msFilevaultId + "', " +
                    "filevault_ts_n = " + filevaultTs_n + ", " +
                    "file_type = '" + msFileType + "', " +
                    "b_offender_uni = " + (mbOffenderUnionized ? 1 : 0) + ", " +
                    "b_offender_sign = " + (mbOffenderSigned ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_doc = " + mnFkDocumentId + ", " +
                    "fk_cob = " + mnFkCompanyBranchId + ", " +
                    "fk_emp_author = " + mnFkEmployeeAuthorId + ", " +
                    "fk_emp_offender = " + mnFkEmployeeOffenderId + ", " +
                    "fk_emp_boss = " + mnFkEmployeeBossId + ", " +
                    "fk_emp_union_n = " + (mnFkEmployeeUnionId_n == 0 ? "NULL" : mnFkEmployeeUnionId_n) + ", " +
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
        
        msSql = "DELETE FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DOC_BREACH_PREC_SUBSEC) + " "
                + "WHERE id_doc_breach = " + mnPkDocBreachId + " ";
        
        session.getStatement().execute(msSql);
        
        // save children:
        
        int sortingPos = 0;
        for (SDbDocBreachPreceptSubsection child : maChildPreceptSubsections) {
            child.setRegistryNew(true);
            child.setPkDocBreachId(mnPkDocBreachId);
            child.setSortingPos(++sortingPos);
            child.save(session);
        }
        
        // finish reading:

        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbDocBreach clone() throws CloneNotSupportedException {
        SDbDocBreach registry = new SDbDocBreach();

        registry.setPkDocBreachId(this.getPkDocBreachId());
        registry.setNumber(this.getNumber());
        registry.setBreachTs(this.getBreachTs());
        registry.setBreachAbstract(this.getBreachAbstract());
        registry.setBreachDescrip(this.getBreachDescrip());
        registry.setEndingNotes(this.getEndingNotes());
        registry.setFilevaultId(this.getFilevaultId());
        registry.setFilevaultTs_n(this.getFilevaultTs_n());
        registry.setFileType(this.getFileType());
        registry.setOffenderUnionized(this.isOffenderUnionized());
        registry.setOffenderSigned(this.isOffenderSigned());
        registry.setDeleted(this.isDeleted());
        registry.setFkDocumentId(this.getFkDocumentId());
        registry.setFkCompanyBranchId(this.getFkCompanyBranchId());
        registry.setFkEmployeeAuthorId(this.getFkEmployeeAuthorId());
        registry.setFkEmployeeOffenderId(this.getFkEmployeeOffenderId());
        registry.setFkEmployeeBossId(this.getFkEmployeeBossId());
        registry.setFkEmployeeUnionId_n(this.getFkEmployeeUnionId_n());
        registry.setFkOffenderDepartmentId(this.getFkOffenderDepartmentId());
        registry.setFkOffenderPositionId(this.getFkOffenderPositionId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        for (SDbDocBreachPreceptSubsection child : maChildPreceptSubsections) {
            registry.getChildPreceptSubsections().add(child.clone());
        }
        
        registry.setOldFilevaultId(this.getOldFilevaultId());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    public static int getNextNumber(SGuiSession session) throws SQLException, Exception {
        int nextNumber = 0;

        String sql = "SELECT COALESCE(MAX(num), 0) + 1 "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_DOC_BREACH) + " "
                + "WHERE NOT b_del;";
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                nextNumber = resultSet.getInt(1);
            }
        }
        
        return nextNumber;
    }
}
