
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

/* IMPORTANT:
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
    protected String msEmergenciesContact;
    protected String msEmergenciesTelNumber;
    protected String msBeneficiaries;
    protected int mnFkCatSexMateClassId;
    protected int mnFkCatSexMateTypeId;
    protected int mnFkCatSexSon1ClassId;
    protected int mnFkCatSexSon1TypeId;
    protected int mnFkCatSexSon2ClassId;
    protected int mnFkCatSexSon2TypeId;
    protected int mnFkCatSexSon3ClassId;
    protected int mnFkCatSexSon3TypeId;
    protected int mnFkCatSexSon4ClassId;
    protected int mnFkCatSexSon4TypeId;
    protected int mnFkCatSexSon5ClassId;
    protected int mnFkCatSexSon5TypeId;
    protected int mnFkCatKinshipEmergenciesClassId;
    protected int mnFkCatKinshipEmergenciesTypeId;
    
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
    public void setEmergenciesContact(String s) { msEmergenciesContact = s; }
    public void setEmergenciesTelNumber(String s) { msEmergenciesTelNumber = s; }
    public void setBeneficiaries(String s) { msBeneficiaries = s; }
    public void setFkCatSexMateClassId(int n) { mnFkCatSexMateClassId = n; }
    public void setFkCatSexMateTypeId(int n) { mnFkCatSexMateTypeId = n; }
    public void setFkCatSexSon1ClassId(int n) { mnFkCatSexSon1ClassId = n; }
    public void setFkCatSexSon1TypeId(int n) { mnFkCatSexSon1TypeId = n; }
    public void setFkCatSexSon2ClassId(int n) { mnFkCatSexSon2ClassId = n; }
    public void setFkCatSexSon2TypeId(int n) { mnFkCatSexSon2TypeId = n; }
    public void setFkCatSexSon3ClassId(int n) { mnFkCatSexSon3ClassId = n; }
    public void setFkCatSexSon3TypeId(int n) { mnFkCatSexSon3TypeId = n; }
    public void setFkCatSexSon4ClassId(int n) { mnFkCatSexSon4ClassId = n; }
    public void setFkCatSexSon4TypeId(int n) { mnFkCatSexSon4TypeId = n; }
    public void setFkCatSexSon5ClassId(int n) { mnFkCatSexSon5ClassId = n; }
    public void setFkCatSexSon5TypeId(int n) { mnFkCatSexSon5TypeId = n; }
    public void setFkCatKinshipEmergenciesClassId(int n) { mnFkCatKinshipEmergenciesClassId = n; }
    public void setFkCatKinshipEmergenciesTypeId(int n) { mnFkCatKinshipEmergenciesTypeId = n; }

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
    public String getEmergenciesContact() { return msEmergenciesContact; }
    public String getEmergenciesTelNumber() { return msEmergenciesTelNumber; }
    public String getBeneficiaries() { return msBeneficiaries; }
    public int getFkCatSexMateClassId() { return mnFkCatSexMateClassId; }
    public int getFkCatSexMateTypeId() { return mnFkCatSexMateTypeId; }
    public int getFkCatSexSon1ClassId() { return mnFkCatSexSon1ClassId; }
    public int getFkCatSexSon1TypeId() { return mnFkCatSexSon1TypeId; }
    public int getFkCatSexSon2ClassId() { return mnFkCatSexSon2ClassId; }
    public int getFkCatSexSon2TypeId() { return mnFkCatSexSon2TypeId; }
    public int getFkCatSexSon3ClassId() { return mnFkCatSexSon3ClassId; }
    public int getFkCatSexSon3TypeId() { return mnFkCatSexSon3TypeId; }
    public int getFkCatSexSon4ClassId() { return mnFkCatSexSon4ClassId; }
    public int getFkCatSexSon4TypeId() { return mnFkCatSexSon4TypeId; }
    public int getFkCatSexSon5ClassId() { return mnFkCatSexSon5ClassId; }
    public int getFkCatSexSon5TypeId() { return mnFkCatSexSon5TypeId; }
    public int getFkCatKinshipEmergenciesClassId() { return mnFkCatKinshipEmergenciesClassId; }
    public int getFkCatKinshipEmergenciesTypeId() { return mnFkCatKinshipEmergenciesTypeId; }

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
        msEmergenciesContact = "";
        msEmergenciesTelNumber = "";
        msBeneficiaries = "";
        mnFkCatSexMateClassId = 0;
        mnFkCatSexMateTypeId = 0;
        mnFkCatSexSon1ClassId = 0;
        mnFkCatSexSon1TypeId = 0;
        mnFkCatSexSon2ClassId = 0;
        mnFkCatSexSon2TypeId = 0;
        mnFkCatSexSon3ClassId = 0;
        mnFkCatSexSon3TypeId = 0;
        mnFkCatSexSon4ClassId = 0;
        mnFkCatSexSon4TypeId = 0;
        mnFkCatSexSon5ClassId = 0;
        mnFkCatSexSon5TypeId = 0;
        mnFkCatKinshipEmergenciesClassId = 0;
        mnFkCatKinshipEmergenciesTypeId = 0;
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
                msEmergenciesContact = resultSet.getString("emergs_con");
                msEmergenciesTelNumber = resultSet.getString("emergs_tel_num");
                msBeneficiaries = resultSet.getString("benefs");
                mnFkCatSexMateClassId = resultSet.getInt("fk_cl_cat_sex_mate");
                mnFkCatSexMateTypeId = resultSet.getInt("fk_tp_cat_sex_mate");
                mnFkCatSexSon1ClassId = resultSet.getInt("fk_cl_cat_sex_son_1");
                mnFkCatSexSon1TypeId = resultSet.getInt("fk_tp_cat_sex_son_1");
                mnFkCatSexSon2ClassId = resultSet.getInt("fk_cl_cat_sex_son_2");
                mnFkCatSexSon2TypeId = resultSet.getInt("fk_tp_cat_sex_son_2");
                mnFkCatSexSon3ClassId = resultSet.getInt("fk_cl_cat_sex_son_3");
                mnFkCatSexSon3TypeId = resultSet.getInt("fk_tp_cat_sex_son_3");
                mnFkCatSexSon4ClassId = resultSet.getInt("fk_cl_cat_sex_son_4");
                mnFkCatSexSon4TypeId = resultSet.getInt("fk_tp_cat_sex_son_4");
                mnFkCatSexSon5ClassId = resultSet.getInt("fk_cl_cat_sex_son_5");
                mnFkCatSexSon5TypeId = resultSet.getInt("fk_tp_cat_sex_son_5");
                mnFkCatKinshipEmergenciesClassId = resultSet.getInt("fk_cl_cat_kin_emergs");
                mnFkCatKinshipEmergenciesTypeId = resultSet.getInt("fk_tp_cat_kin_emergs");
            
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
                        "'" + msEmergenciesContact + "', " + 
                        "'" + msEmergenciesTelNumber + "', " + 
                        "'" + msBeneficiaries + "', " + 
                        mnFkCatSexMateClassId + ", " + 
                        mnFkCatSexMateTypeId + ", " + 
                        mnFkCatSexSon1ClassId + ", " + 
                        mnFkCatSexSon1TypeId + ", " + 
                        mnFkCatSexSon2ClassId + ", " + 
                        mnFkCatSexSon2TypeId + ", " + 
                        mnFkCatSexSon3ClassId + ", " + 
                        mnFkCatSexSon3TypeId + ", " + 
                        mnFkCatSexSon4ClassId + ", " + 
                        mnFkCatSexSon4TypeId + ", " + 
                        mnFkCatSexSon5ClassId + ", " + 
                        mnFkCatSexSon5TypeId + ", " + 
                        mnFkCatKinshipEmergenciesClassId + ", " + 
                        mnFkCatKinshipEmergenciesTypeId + " " + 
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
                        "emergs_con = '" + msEmergenciesContact + "', " +
                        "emergs_tel_num = '" + msEmergenciesTelNumber + "', " +
                        "benefs = '" + msBeneficiaries + "', " +
                        "fk_cl_cat_sex_mate = " + mnFkCatSexMateClassId + ", " +
                        "fk_tp_cat_sex_mate = " + mnFkCatSexMateTypeId + ", " +
                        "fk_cl_cat_sex_son_1 = " + mnFkCatSexSon1ClassId + ", " +
                        "fk_tp_cat_sex_son_1 = " + mnFkCatSexSon1TypeId + ", " +
                        "fk_cl_cat_sex_son_2 = " + mnFkCatSexSon2ClassId + ", " +
                        "fk_tp_cat_sex_son_2 = " + mnFkCatSexSon2TypeId + ", " +
                        "fk_cl_cat_sex_son_3 = " + mnFkCatSexSon3ClassId + ", " +
                        "fk_tp_cat_sex_son_3 = " + mnFkCatSexSon3TypeId + ", " +
                        "fk_cl_cat_sex_son_4 = " + mnFkCatSexSon4ClassId + ", " +
                        "fk_tp_cat_sex_son_4 = " + mnFkCatSexSon4TypeId + ", " +
                        "fk_cl_cat_sex_son_5 = " + mnFkCatSexSon5ClassId + ", " +
                        "fk_tp_cat_sex_son_5 = " + mnFkCatSexSon5TypeId + ", " +
                        "fk_cl_cat_kin_emergs = " + mnFkCatKinshipEmergenciesClassId + ", " +
                        "fk_tp_cat_kin_emergs = " + mnFkCatKinshipEmergenciesTypeId + " " +
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
