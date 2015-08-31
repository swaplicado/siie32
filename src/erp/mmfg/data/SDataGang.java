/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmfg.data;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Néstor Ávalos
 */
public class SDataGang extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkGangId;
    protected java.lang.String msNumber;
    protected java.lang.String msDescription;
    protected boolean mbIsDeleted;
    protected int mnFkTurnId;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected java.lang.String msDbmsTurn;
    
    protected java.util.Vector<erp.mmfg.data.SDataGangEntry> mvDbmsGangEntries;

    public SDataGang() {
        super(SDataConstants.MFGU_GANG);
        mvDbmsGangEntries = new Vector<SDataGangEntry>();

        reset();      
    }
    
    public void setPkGangId(int n) { mnPkGangId = n; }
    public void setNumber(java.lang.String s) { msNumber = s; }
    public void setDescription(java.lang.String s) { msDescription = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkTurnId(int n) { mnFkTurnId = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }
    
    public int getPkGangId() { return mnPkGangId; }
    public java.lang.String getNumber() { return msNumber; }
    public java.lang.String getDescription() { return msDescription; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkTurnId() { return mnFkTurnId; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsTurn(java.lang.String s) { msDbmsTurn = s; }

    public java.lang.String getDbmsTurn() { return msDbmsTurn; }
    
    public java.util.Vector<SDataGangEntry> getDbmsGangEntries() { return mvDbmsGangEntries; }
    
    @Override
    public void reset() {
        super.resetRegistry();

        mnPkGangId = 0;
        msNumber = "";
        msDescription = "";
        mbIsDeleted = false;
        mnFkTurnId = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        msDbmsTurn = "";

        mvDbmsGangEntries.clear();        
    }
    
    @Override
    public void setPrimaryKey(java.lang.Object key) {
        mnPkGangId = ((int[]) key)[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkGangId };
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";

        SDataGangEntry oGangEntry = null;
        ResultSet resultSet = null;
        Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT g.*, t.turn " +
                "FROM mfgu_gang AS g " +
                "INNER JOIN erp.mfgu_turn AS t ON g.fid_turn = t.id_turn " +
                "INNER JOIN erp.usru_usr AS un ON g.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON g.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON g.fid_usr_del = ud.id_usr " +
                "WHERE g.id_gang = " + key[0];

            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkGangId = resultSet.getInt("g.id_gang");
                msNumber = resultSet.getString("g.num");
                msDescription = resultSet.getString("g.des");
                mbIsDeleted = resultSet.getBoolean("g.b_del");
                mnFkTurnId = resultSet.getInt("g.fid_turn");
                mnFkUserNewId = resultSet.getInt("g.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("g.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("g.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("g.ts_new");
                mtUserEditTs = resultSet.getTimestamp("g.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("g.ts_del");

                msDbmsTurn = resultSet.getString("t.turn");
                
                // Read entries:

                mvDbmsGangEntries.removeAllElements();
                sql = "SELECT * " +
                    "FROM mfgu_gang_ety " +
                    "WHERE id_gang = " + key[0] + " " +
                    "ORDER BY id_ety ";
                resultSet = statement.executeQuery(sql);
                statementAux = statement.getConnection().createStatement();
                while (resultSet.next()) {
                    oGangEntry = new SDataGangEntry();
                    if (oGangEntry.read(new int[] { resultSet.getInt("id_gang"), resultSet.getInt("id_ety") }, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
                        throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                    }
                    else {
                        mvDbmsGangEntries.add(oGangEntry);
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(java.sql.Connection connection) {
        int i = 0;
        int nParam = 1;
        
        SDataGangEntry oGangEntry = null;
        
        CallableStatement callableStatement = null;
        Statement statementAux = null;
        
        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {            
            callableStatement = connection.prepareCall(
                    "{ CALL mfgu_gang_save(" +
                        "?, ?, ?, ?, ?, " +
                        "?, ?, ?, ?) }");
            callableStatement.setInt(nParam++, mnPkGangId);
            callableStatement.setString(nParam++, msNumber);
            callableStatement.setString(nParam++, msDescription);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkTurnId);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.INTEGER);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkGangId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);
            
            if (mnDbmsErrorId != 0) {
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            }
            else {
  
                // Delete entries:
                
                statementAux = connection.createStatement();
                statementAux.execute("DELETE FROM mfgu_gang_ety WHERE id_gang = " + mnPkGangId + "; ");

                // Save entries:

                for (i = 0; i < mvDbmsGangEntries.size(); i++) {
                    oGangEntry = (SDataGangEntry) mvDbmsGangEntries.get(i);
                    if (oGangEntry != null) {
                        oGangEntry.setPkGangId(mnPkGangId);
                        oGangEntry.setPkEntryId(0);
                        if (oGangEntry.save(connection) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                        else {
                            mvDbmsGangEntries.set(i, oGangEntry);
                        }
                    }
                }

                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
            }
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int delete(java.sql.Connection connection) {        
        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        return null;
    }
}
