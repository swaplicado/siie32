
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mhrs.data;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModConsts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;

/*
 * IMPORTANT:
 * Every change made to the structure of this registry apply it as well in erp.mbps.data.SDataEmployee too.
 */

/**
 *
 * @author Sergio Flores
 */
public class SDataEmployeeRelatives extends erp.lib.data.SDataRegistry implements java.io.Serializable {
    
    protected int mnPkEmployeeId;
    protected String msMate;
    protected Date mtMateDateBirth;
    protected boolean mbMateDeceased;
    protected String msSon1;
    protected Date mtSonDateBirth1;
    protected boolean mbSonDeceased1;
    protected String msSon2;
    protected Date mtSonDateBirth2;
    protected boolean mbSonDeceased2;
    protected String msSon3;
    protected Date mtSonDateBirth3;
    protected boolean mbSonDeceased3;
    protected String msSon4;
    protected Date mtSonDateBirth4;
    protected boolean mbSonDeceased4;
    protected String msSon5;
    protected Date mtSonDateBirth5;
    protected boolean mbSonDeceased5;
    protected int mnFkCatSexClassIdMate;
    protected int mnFkCatSexTypeIdMate;
    protected int mnFkCatSexClassIdSon1;
    protected int mnFkCatSexTypeIdSon1;
    protected int mnFkCatSexClassIdSon2;
    protected int mnFkCatSexTypeIdSon2;
    protected int mnFkCatSexClassIdSon3;
    protected int mnFkCatSexTypeIdSon3;
    protected int mnFkCatSexClassIdSon4;
    protected int mnFkCatSexTypeIdSon4;
    protected int mnFkCatSexClassIdSon5;
    protected int mnFkCatSexTypeIdSon5;
    
    public SDataEmployeeRelatives() {
        super(SModConsts.HRSU_EMP_REL);
    }
    
    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setMate(String s) { msMate = s; }
    public void setMateDateBirth(Date t) { mtMateDateBirth = t; }
    public void setMateDeceased(boolean b) { mbMateDeceased = b; }
    public void setSon1(String s) { msSon1 = s; }
    public void setSonDateBirth1(Date t) { mtSonDateBirth1 = t; }
    public void setSonDeceased1(boolean b) { mbSonDeceased1 = b; }
    public void setSon2(String s) { msSon2 = s; }
    public void setSonDateBirth2(Date t) { mtSonDateBirth2 = t; }
    public void setSonDeceased2(boolean b) { mbSonDeceased2 = b; }
    public void setSon3(String s) { msSon3 = s; }
    public void setSonDateBirth3(Date t) { mtSonDateBirth3 = t; }
    public void setSonDeceased3(boolean b) { mbSonDeceased3 = b; }
    public void setSon4(String s) { msSon4 = s; }
    public void setSonDateBirth4(Date t) { mtSonDateBirth4 = t; }
    public void setSonDeceased4(boolean b) { mbSonDeceased4 = b; }
    public void setSon5(String s) { msSon5 = s; }
    public void setSonDateBirth5(Date t) { mtSonDateBirth5 = t; }
    public void setSonDeceased5(boolean b) { mbSonDeceased5 = b; }
    public void setFkCatSexClassIdMate(int n) { mnFkCatSexClassIdMate = n; }
    public void setFkCatSexTypeIdMate(int n) { mnFkCatSexTypeIdMate = n; }
    public void setFkCatSexClassIdSon1(int n) { mnFkCatSexClassIdSon1 = n; }
    public void setFkCatSexTypeIdSon1(int n) { mnFkCatSexTypeIdSon1 = n; }
    public void setFkCatSexClassIdSon2(int n) { mnFkCatSexClassIdSon2 = n; }
    public void setFkCatSexTypeIdSon2(int n) { mnFkCatSexTypeIdSon2 = n; }
    public void setFkCatSexClassIdSon3(int n) { mnFkCatSexClassIdSon3 = n; }
    public void setFkCatSexTypeIdSon3(int n) { mnFkCatSexTypeIdSon3 = n; }
    public void setFkCatSexClassIdSon4(int n) { mnFkCatSexClassIdSon4 = n; }
    public void setFkCatSexTypeIdSon4(int n) { mnFkCatSexTypeIdSon4 = n; }
    public void setFkCatSexClassIdSon5(int n) { mnFkCatSexClassIdSon5 = n; }
    public void setFkCatSexTypeIdSon5(int n) { mnFkCatSexTypeIdSon5 = n; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public String getMate() { return msMate; }
    public Date getMateDateBirth() { return mtMateDateBirth; }
    public boolean isMateDeceased() { return mbMateDeceased; }
    public String getSon1() { return msSon1; }
    public Date getSonDateBirth1() { return mtSonDateBirth1; }
    public boolean isSonDeceased1() { return mbSonDeceased1; }
    public String getSon2() { return msSon2; }
    public Date getSonDateBirth2() { return mtSonDateBirth2; }
    public boolean isSonDeceased2() { return mbSonDeceased2; }
    public String getSon3() { return msSon3; }
    public Date getSonDateBirth3() { return mtSonDateBirth3; }
    public boolean isSonDeceased3() { return mbSonDeceased3; }
    public String getSon4() { return msSon4; }
    public Date getSonDateBirth4() { return mtSonDateBirth4; }
    public boolean isSonDeceased4() { return mbSonDeceased4; }
    public String getSon5() { return msSon5; }
    public Date getSonDateBirth5() { return mtSonDateBirth5; }
    public boolean isSonDeceased5() { return mbSonDeceased5; }
    public int getFkCatSexClassIdMate() { return mnFkCatSexClassIdMate; }
    public int getFkCatSexTypeIdMate() { return mnFkCatSexTypeIdMate; }
    public int getFkCatSexClassIdSon1() { return mnFkCatSexClassIdSon1; }
    public int getFkCatSexTypeIdSon1() { return mnFkCatSexTypeIdSon1; }
    public int getFkCatSexClassIdSon2() { return mnFkCatSexClassIdSon2; }
    public int getFkCatSexTypeIdSon2() { return mnFkCatSexTypeIdSon2; }
    public int getFkCatSexClassIdSon3() { return mnFkCatSexClassIdSon3; }
    public int getFkCatSexTypeIdSon3() { return mnFkCatSexTypeIdSon3; }
    public int getFkCatSexClassIdSon4() { return mnFkCatSexClassIdSon4; }
    public int getFkCatSexTypeIdSon4() { return mnFkCatSexTypeIdSon4; }
    public int getFkCatSexClassIdSon5() { return mnFkCatSexClassIdSon5; }
    public int getFkCatSexTypeIdSon5() { return mnFkCatSexTypeIdSon5; }

    @Override
    public void setPrimaryKey(Object pk) {
        mnPkEmployeeId = ((int[]) pk)[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkEmployeeId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkEmployeeId = 0;
        msMate = "";
        mtMateDateBirth = null;
        mbMateDeceased = false;
        msSon1 = "";
        mtSonDateBirth1 = null;
        mbSonDeceased1 = false;
        msSon2 = "";
        mtSonDateBirth2 = null;
        mbSonDeceased2 = false;
        msSon3 = "";
        mtSonDateBirth3 = null;
        mbSonDeceased3 = false;
        msSon4 = "";
        mtSonDateBirth4 = null;
        mbSonDeceased4 = false;
        msSon5 = "";
        mtSonDateBirth5 = null;
        mbSonDeceased5 = false;
        mnFkCatSexClassIdMate = 0;
        mnFkCatSexTypeIdMate = 0;
        mnFkCatSexClassIdSon1 = 0;
        mnFkCatSexTypeIdSon1 = 0;
        mnFkCatSexClassIdSon2 = 0;
        mnFkCatSexTypeIdSon2 = 0;
        mnFkCatSexClassIdSon3 = 0;
        mnFkCatSexTypeIdSon3 = 0;
        mnFkCatSexClassIdSon4 = 0;
        mnFkCatSexTypeIdSon4 = 0;
        mnFkCatSexClassIdSon5 = 0;
        mnFkCatSexTypeIdSon5 = 0;
    }

    @Override
    public int read(Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM erp.hrsu_emp WHERE id_emp = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                mnPkEmployeeId = resultSet.getInt("id_emp");
                msMate = resultSet.getString("mate");
                mtMateDateBirth = resultSet.getDate("mate_dt_bir_n");
                mbMateDeceased = resultSet.getBoolean("b_mate_dec");
                msSon1 = resultSet.getString("son_1");
                mtSonDateBirth1 = resultSet.getDate("son_dt_bir_1_n");
                mbSonDeceased1 = resultSet.getBoolean("b_son_dec_1");
                msSon2 = resultSet.getString("son_2");
                mtSonDateBirth2 = resultSet.getDate("son_dt_bir_2_n");
                mbSonDeceased2 = resultSet.getBoolean("b_son_dec_2");
                msSon3 = resultSet.getString("son_3");
                mtSonDateBirth3 = resultSet.getDate("son_dt_bir_3_n");
                mbSonDeceased3 = resultSet.getBoolean("b_son_dec_3");
                msSon4 = resultSet.getString("son_4");
                mtSonDateBirth4 = resultSet.getDate("son_dt_bir_4_n");
                mbSonDeceased4 = resultSet.getBoolean("b_son_dec_4");
                msSon5 = resultSet.getString("son_5");
                mtSonDateBirth5 = resultSet.getDate("son_dt_bir_5_n");
                mbSonDeceased5 = resultSet.getBoolean("b_son_dec_5");
                mnFkCatSexClassIdMate = resultSet.getInt("fk_cl_cat_sex_mate");
                mnFkCatSexTypeIdMate = resultSet.getInt("fk_tp_cat_sex_mate");
                mnFkCatSexClassIdSon1 = resultSet.getInt("fk_cl_cat_sex_son_1");
                mnFkCatSexTypeIdSon1 = resultSet.getInt("fk_tp_cat_sex_son_1");
                mnFkCatSexClassIdSon2 = resultSet.getInt("fk_cl_cat_sex_son_2");
                mnFkCatSexTypeIdSon2 = resultSet.getInt("fk_tp_cat_sex_son_2");
                mnFkCatSexClassIdSon3 = resultSet.getInt("fk_cl_cat_sex_son_3");
                mnFkCatSexTypeIdSon3 = resultSet.getInt("fk_tp_cat_sex_son_3");
                mnFkCatSexClassIdSon4 = resultSet.getInt("fk_cl_cat_sex_son_4");
                mnFkCatSexTypeIdSon4 = resultSet.getInt("fk_tp_cat_sex_son_4");
                mnFkCatSexClassIdSon5 = resultSet.getInt("fk_cl_cat_sex_son_5");
                mnFkCatSexTypeIdSon5 = resultSet.getInt("fk_tp_cat_sex_son_5");
            
                mbIsRegistryNew = false;
                mnLastDbActionResult = SLibConstants.DB_ACTION_READ_OK;
            }
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_READ_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public int save(Connection connection) {
        String sql = "";

        try {
            if (mbIsRegistryNew) {
                sql = "INSERT INTO erp.hrsu_emp_rel VALUES (" +
                        mnPkEmployeeId + ", " + 
                        "'" + msMate + "', " + 
                        "'" + SLibUtils.DbmsDateFormatDate.format(mtMateDateBirth) + "', " + 
                        (mbMateDeceased ? 1 : 0) + ", " + 
                        "'" + msSon1 + "', " + 
                        "'" + SLibUtils.DbmsDateFormatDate.format(mtSonDateBirth1) + "', " + 
                        (mbSonDeceased1 ? 1 : 0) + ", " + 
                        "'" + msSon2 + "', " + 
                        "'" + SLibUtils.DbmsDateFormatDate.format(mtSonDateBirth2) + "', " + 
                        (mbSonDeceased2 ? 1 : 0) + ", " + 
                        "'" + msSon3 + "', " + 
                        "'" + SLibUtils.DbmsDateFormatDate.format(mtSonDateBirth3) + "', " + 
                        (mbSonDeceased3 ? 1 : 0) + ", " + 
                        "'" + msSon4 + "', " + 
                        "'" + SLibUtils.DbmsDateFormatDate.format(mtSonDateBirth4) + "', " + 
                        (mbSonDeceased4 ? 1 : 0) + ", " + 
                        "'" + msSon5 + "', " + 
                        "'" + SLibUtils.DbmsDateFormatDate.format(mtSonDateBirth5) + "', " + 
                        (mbSonDeceased5 ? 1 : 0) + ", " + 
                        mnFkCatSexClassIdMate + ", " + 
                        mnFkCatSexTypeIdMate + ", " + 
                        mnFkCatSexClassIdSon1 + ", " + 
                        mnFkCatSexTypeIdSon1 + ", " + 
                        mnFkCatSexClassIdSon2 + ", " + 
                        mnFkCatSexTypeIdSon2 + ", " + 
                        mnFkCatSexClassIdSon3 + ", " + 
                        mnFkCatSexTypeIdSon3 + ", " + 
                        mnFkCatSexClassIdSon4 + ", " + 
                        mnFkCatSexTypeIdSon4 + ", " + 
                        mnFkCatSexClassIdSon5 + ", " + 
                        mnFkCatSexTypeIdSon5 + " " + 
                        ")";
            }
            else {
                sql = "UPDATE erp.hrsu_emp_rel SET " +
                        //"id_emp = " + mnPkEmployeeId + ", " +
                        "mate = '" + msMate + "', " +
                        "mate_dt_bir_n = '" + SLibUtils.DbmsDateFormatDate.format(mtMateDateBirth) + "', " +
                        "b_mate_dec = " + (mbMateDeceased ? 1 : 0) + ", " +
                        "son_1 = '" + msSon1 + "', " +
                        "son_dt_bir_1_n = '" + SLibUtils.DbmsDateFormatDate.format(mtSonDateBirth1) + "', " +
                        "b_son_dec_1 = " + (mbSonDeceased1 ? 1 : 0) + ", " +
                        "son_2 = '" + msSon2 + "', " +
                        "son_dt_bir_2_n = '" + SLibUtils.DbmsDateFormatDate.format(mtSonDateBirth2) + "', " +
                        "b_son_dec_2 = " + (mbSonDeceased2 ? 1 : 0) + ", " +
                        "son_3 = '" + msSon3 + "', " +
                        "son_dt_bir_3_n = '" + SLibUtils.DbmsDateFormatDate.format(mtSonDateBirth3) + "', " +
                        "b_son_dec_3 = " + (mbSonDeceased3 ? 1 : 0) + ", " +
                        "son_4 = '" + msSon4 + "', " +
                        "son_dt_bir_4_n = '" + SLibUtils.DbmsDateFormatDate.format(mtSonDateBirth4) + "', " +
                        "b_son_dec_4 = " + (mbSonDeceased4 ? 1 : 0) + ", " +
                        "son_5 = '" + msSon5 + "', " +
                        "son_dt_bir_5_n = '" + SLibUtils.DbmsDateFormatDate.format(mtSonDateBirth5) + "', " +
                        "b_son_dec_5 = " + (mbSonDeceased5 ? 1 : 0) + ", " +
                        "fk_cl_cat_sex_mate = " + mnFkCatSexClassIdMate + ", " +
                        "fk_tp_cat_sex_mate = " + mnFkCatSexTypeIdMate + ", " +
                        "fk_cl_cat_sex_son_1 = " + mnFkCatSexClassIdSon1 + ", " +
                        "fk_tp_cat_sex_son_1 = " + mnFkCatSexTypeIdSon1 + ", " +
                        "fk_cl_cat_sex_son_2 = " + mnFkCatSexClassIdSon2 + ", " +
                        "fk_tp_cat_sex_son_2 = " + mnFkCatSexTypeIdSon2 + ", " +
                        "fk_cl_cat_sex_son_3 = " + mnFkCatSexClassIdSon3 + ", " +
                        "fk_tp_cat_sex_son_3 = " + mnFkCatSexTypeIdSon3 + ", " +
                        "fk_cl_cat_sex_son_4 = " + mnFkCatSexClassIdSon4 + ", " +
                        "fk_tp_cat_sex_son_4 = " + mnFkCatSexTypeIdSon4 + ", " +
                        "fk_cl_cat_sex_son_5 = " + mnFkCatSexClassIdSon5 + ", " +
                        "fk_tp_cat_sex_son_5 = " + mnFkCatSexTypeIdSon5 + " " +
                        "WHERE id_emp = " + mnPkEmployeeId + ";";
            }

            connection.createStatement().execute(sql);

            mbIsRegistryNew = false;
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_OK;
        }
        catch (java.sql.SQLException e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }
        catch (java.lang.Exception e) {
            mnLastDbActionResult = SLibConstants.DB_ACTION_SAVE_ERROR;
            SLibUtilities.printOutException(this, e);
        }

        return mnLastDbActionResult;
    }

    @Override
    public Date getLastDbUpdate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
