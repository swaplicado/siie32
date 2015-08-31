
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
public class SDbAutomaticDeductionsAux extends SDbRegistry {
    
    protected int mnPkAutomaticId;
    protected int mnFkEmployeeId_n;
    
    protected ArrayList<SDbAutomaticDeduction> maAutomaticDeductions;
    
    public SDbAutomaticDeductionsAux() {
        super(SModConsts.HRSX_AUT_DED);
    }

    public void setPkAutomaticId(int n) { mnPkAutomaticId = n; }
    public void setFkEmployeeId_n(int n) { mnFkEmployeeId_n = n; }
    
    public void setAutomaticDeductions(ArrayList<SDbAutomaticDeduction> a) { maAutomaticDeductions = a; }

    public int getPkAutomaticId() { return mnPkAutomaticId; }
    public int getFkEmployeeId_n() { return mnFkEmployeeId_n; }
    
    public ArrayList<SDbAutomaticDeduction> getAutomaticDeductions() { return maAutomaticDeductions; }
    
    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;
        Statement statement = null;
        String sql = "";
        SDbAutomaticDeduction earningRow = null;
        
        setPrimaryKey(pk);
        statement = session.getDatabase().getConnection().createStatement();
        
        if (mnFkEmployeeId_n == 0) {
            sql = "SELECT id_ded, id_aut FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_DED) +  " " +
                    " WHERE fk_emp_n IS NULL AND b_del = 0 ";
        }
        else {
            sql = "SELECT id_ded, id_aut FROM " + SModConsts.TablesMap.get(SModConsts.HRS_AUT_DED) +  " " +
                    " WHERE fk_emp_n = " + mnFkEmployeeId_n + " AND b_del = 0 ";
        }
        
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            earningRow = new SDbAutomaticDeduction();

            earningRow.read(session, new int[] { resultSet.getInt("id_ded"), resultSet.getInt("id_aut") } );
            maAutomaticDeductions.add(earningRow);
        }
    }

    @Override
    public void save(SGuiSession session) throws SQLException, Exception {
        if (!maAutomaticDeductions.isEmpty()) {
            for (SDbAutomaticDeduction row : maAutomaticDeductions) {
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
        
        maAutomaticDeductions = new ArrayList<SDbAutomaticDeduction>();
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
    public SDbAutomaticDeductionsAux clone() throws CloneNotSupportedException {
        SDbAutomaticDeductionsAux registry = new SDbAutomaticDeductionsAux();
        
        registry.setPkAutomaticId(this.getPkAutomaticId());
        registry.setFkEmployeeId_n(this.getFkEmployeeId_n());
        
        registry.getAutomaticDeductions().clone();
        
        return registry;
    }
}
