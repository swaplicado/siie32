/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SW
 */
public class SDataBankNbDay extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected ArrayList maBankNbDay;
    protected boolean mbIsDeleted;
    protected int mnFkUserId;
    protected int mnIdBankNbDay = 0;
    
    public SDataBankNbDay() {
        super(SDataConstants.FINU_BANK_NB_DAY);
    }
    
    public void setBankNbDay(ArrayList list){ maBankNbDay = list; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkUserId(int n) { mnFkUserId = n; }
    
    public ArrayList getBankNbDay(){ return maBankNbDay; }
    public boolean getIsDeleted(){ return mbIsDeleted; }
    
    @Override
    public void setPrimaryKey(Object pk) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getPrimaryKey() {
        return null;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int read(Object pk, Statement statement) {
        java.lang.Object[] key = (java.lang.Object[]) pk;
        java.lang.String sql = "";
        java.sql.ResultSet resultSet = null;
        
        try{
            sql = "SELECT * FROM erp.finu_bank_nb_day WHERE id_bank_nb_day = " + key[0];
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                Date day = resultSet.getDate("nb_day");
                String name = resultSet.getString("name");
                String[] bankNbDay = {"" + (day.getYear() + 1900) + "-" + (day.getMonth() + 1) + "-" + day.getDate(),name};
                maBankNbDay = new ArrayList();
                maBankNbDay.add(bankNbDay);
                mbIsDeleted = resultSet.getBoolean("b_del");
                mnFkUserId = resultSet.getInt("fid_usr_edit");
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
                mnIdBankNbDay = Integer.parseInt(key[0].toString());
            }
        }catch (Exception e){
            System.err.println(e);
        }
        
        return mnLastDbActionResult;
    }

    @Override
    public int save(Connection connection) {
        mnLastDbActionResult = SLibConstants.UNDEFINED;
        Statement oStatement = null;
        try {
            oStatement = connection.createStatement();
            java.sql.CallableStatement callableStatement = null;
            
            for (Object bankNbDay : maBankNbDay) {
                int nParam = 1;
                if (bankNbDay instanceof String[]) {
                    String[] date = (((String[]) bankNbDay)[0]).split("\\-");
                    
                    callableStatement = connection.prepareCall("{ CALL erp.finu_bank_nb_day_save(?, ?, ?, ?, ?, ?, ?) }");
                    callableStatement.setInt(nParam++, mnIdBankNbDay);
                    callableStatement.setString(nParam++, (((String[]) bankNbDay)[1]));
                    callableStatement.setDate(nParam++, new java.sql.Date((Integer.parseInt(date[0]))-1900, (Integer.parseInt(date[1]))-1, Integer.parseInt(date[2])));
                    callableStatement.setBoolean(nParam++, true);
                    callableStatement.setBoolean(nParam++, true);
                    callableStatement.setBoolean(nParam++, mbIsDeleted);
                    callableStatement.setInt(nParam++, mnFkUserId);
                    callableStatement.execute();
                    
                    mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
                }
            }
                
            oStatement.close();
        } catch (SQLException ex) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            System.err.println(ex);
            Logger.getLogger(SDataBankNbDay.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mnLastDbActionResult; //cambiar esto
    }

    @Override
    public Date getLastDbUpdate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
