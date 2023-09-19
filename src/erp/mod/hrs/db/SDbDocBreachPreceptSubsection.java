/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores
 */
public class SDbDocBreachPreceptSubsection extends SDbRegistryUser {
    
    protected int mnPkDocBreachId;
    protected int mnPkPreceptId;
    protected int mnPkSectionId;
    protected int mnPkSubsectionId;
    protected int mnSortingPos;
    
    public SDbDocBreachPreceptSubsection() {
        super(SModConsts.HRS_DOC_BREACH_PREC_SUBSEC);
    }
    
    public void setPkDocBreachId(int n) { mnPkDocBreachId = n; }
    public void setPkPreceptId(int n) { mnPkPreceptId = n; }
    public void setPkSectionId(int n) { mnPkSectionId = n; }
    public void setPkSubsectionId(int n) { mnPkSubsectionId = n; }
    public void setSortingPos(int n) { mnSortingPos = n; }

    public int getPkDocBreachId() { return mnPkDocBreachId; }
    public int getPkPreceptId() { return mnPkPreceptId; }
    public int getPkSectionId() { return mnPkSectionId; }
    public int getPkSubsectionId() { return mnPkSubsectionId; }
    public int getSortingPos() { return mnSortingPos; }

    public int[] getPreceptSubsectionKey() {
        return new int[] { mnPkPreceptId, mnPkSectionId, mnPkSubsectionId };
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDocBreachId = pk[0];
        mnPkPreceptId = pk[1];
        mnPkSectionId = pk[2];
        mnPkSubsectionId = pk[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDocBreachId, mnPkPreceptId, mnPkSectionId, mnPkSubsectionId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDocBreachId = 0;
        mnPkPreceptId = 0;
        mnPkSectionId = 0;
        mnPkSubsectionId = 0;
        mnSortingPos = 0;
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_doc_breach = " + mnPkDocBreachId + " "
                + "AND id_prec = " + mnPkPreceptId + " "
                + "AND id_sec = " + mnPkSectionId + " "
                + "AND id_subsec = " + mnPkSubsectionId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_doc_breach = " + pk[0] + " "
                + "AND id_prec = " + pk[1] + " "
                + "AND id_sec = " + pk[2] + " "
                + "AND id_subsec = " + pk[3]+ " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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
            mnPkPreceptId = resultSet.getInt("id_prec");
            mnPkSectionId = resultSet.getInt("id_sec");
            mnPkSubsectionId = resultSet.getInt("id_subsec");
            mnSortingPos = resultSet.getInt("sort");

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
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDocBreachId + ", " + 
                    mnPkPreceptId + ", " + 
                    mnPkSectionId + ", " + 
                    mnPkSubsectionId + ", " + 
                    mnSortingPos + " " + 
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
    public SDbDocBreachPreceptSubsection clone() throws CloneNotSupportedException {
        SDbDocBreachPreceptSubsection registry = new SDbDocBreachPreceptSubsection();

        registry.setPkDocBreachId(this.getPkDocBreachId());
        registry.setPkPreceptId(this.getPkPreceptId());
        registry.setPkSectionId(this.getPkSectionId());
        registry.setPkSubsectionId(this.getPkSubsectionId());
        registry.setSortingPos(this.getSortingPos());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
