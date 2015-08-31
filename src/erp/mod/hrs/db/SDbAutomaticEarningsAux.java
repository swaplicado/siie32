
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
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
 */
public class SDbAutomaticEarningsAux extends SDbRegistry {
    
    protected int mnPkAutomaticId;
    protected int mnFkEmployeeId_n;
    
    protected ArrayList<SDbAutomaticEarning> maAutomaticEarnings;
    
    public SDbAutomaticEarningsAux() {
        super(SModConsts.HRSX_AUT_EAR);
    }

    public void setPkAutomaticId(int n) { mnPkAutomaticId = n; }
    public void setFkEmployeeId_n(int n) { mnFkEmployeeId_n = n; }
    
    public void setAutomaticEarnings(ArrayList<SDbAutomaticEarning> a) { maAutomaticEarnings = a; }

    public int getPkAutomaticId() { return mnPkAutomaticId; }
    public int getFkEmployeeId_n() { return mnFkEmployeeId_n; }
    
    public ArrayList<SDbAutomaticEarning> getAutomaticEarnings() { return maAutomaticEarnings; }
    
    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;
        Statement statement = null;
        String sql = "";
        SDbAutomaticEarning earningRow = null;
        
        setPrimaryKey(pk);
        statement = session.getDatabase().getConnection().createStatement();
        
        if (mnFkEmployeeId_n == 0) {
            sql = "SELECT id_ear, id_aut FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_EAR) +  " " +
                    " WHERE fk_emp_n IS NULL AND b_del = 0 ";
        }
        else {
            sql = "SELECT id_ear, id_aut FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_EAR) +  " " +
                    " WHERE fk_emp_n = " + mnFkEmployeeId_n + " AND b_del = 0 ";
        }
        
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            earningRow = new SDbAutomaticEarning();

            earningRow.read(session, new int[] { resultSet.getInt("id_ear"), resultSet.getInt("id_aut") } );
            maAutomaticEarnings.add(earningRow);
        }
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        if (!maAutomaticEarnings.isEmpty()) {
            for (SDbAutomaticEarning row : maAutomaticEarnings) {
                row.save(session);
            }
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAutomaticId = pk[0];
        mnFkEmployeeId_n = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAutomaticId, mnFkEmployeeId_n };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkAutomaticId = 0;
        mnFkEmployeeId_n = 0;
        
        maAutomaticEarnings = new ArrayList<SDbAutomaticEarning>();
    }

    @Override
    public String getSqlTable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbAutomaticEarningsAux clone() throws CloneNotSupportedException {
        SDbAutomaticEarningsAux registry = new SDbAutomaticEarningsAux();
        
        registry.setPkAutomaticId(this.getPkAutomaticId());
        registry.setFkEmployeeId_n(this.getFkEmployeeId_n());
        
        registry.getAutomaticEarnings().clone();
        
        return registry;
    }
}
