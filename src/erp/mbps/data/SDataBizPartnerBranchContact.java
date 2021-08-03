/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.data;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 * WARNING: Every change that affects the structure of this registry must be reflected in SIIE/ETL Avista classes and methods!
 * @author Alfonso Flores, Sergio Flores, Claudio Peña
 */
public class SDataBizPartnerBranchContact extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkBizPartnerBranchId;
    protected int mnPkContactId;
    protected java.lang.String msContact;
    protected java.lang.String msContactPrefix;
    protected java.lang.String msContactSuffix;
    protected java.lang.String msLastname;
    protected java.lang.String msFirstname;
    protected java.lang.String msCharge;
    protected java.lang.String msTelAreaCode01;
    protected java.lang.String msTelNumber01;
    protected java.lang.String msTelExt01;
    protected java.lang.String msTelAreaCode02;
    protected java.lang.String msTelNumber02;
    protected java.lang.String msTelExt02;
    protected java.lang.String msTelAreaCode03;
    protected java.lang.String msTelNumber03;
    protected java.lang.String msTelExt03;
    protected java.lang.String msNextelId01;
    protected java.lang.String msNextelId02;
    protected java.lang.String msEmail01;
    protected java.lang.String msEmail02;
    protected java.lang.String msSkype01;
    protected java.lang.String msSkype02;
    protected boolean mbIsDeleted;
    protected int mnFkContactTypeId;
    protected int mnFkTelephoneType01Id;
    protected int mnFkTelephoneType02Id;
    protected int mnFkTelephoneType03Id;
    protected int mnFkUserNewId;
    protected int mnFkUserEditId;
    protected int mnFkUserDeleteId;
    protected java.util.Date mtUserNewTs;
    protected java.util.Date mtUserEditTs;
    protected java.util.Date mtUserDeleteTs;

    protected int mnDbmsFkBizPartnerId;
    protected String msDbmsContactType;
    protected String msDbmsTelephoneType01;
    protected String msDbmsTelephoneType02;
    protected String msDbmsTelephoneType03;
    protected String msDbmsUserNew;
    protected String msDbmsUserEdit;
    protected String msDbmsUserDelete;

    public SDataBizPartnerBranchContact() {
        super(SDataConstants.BPSU_BPB_CON);
        reset();
    }

    public void setPkBizPartnerBranchId(int n) { mnPkBizPartnerBranchId = n; }
    public void setPkContactId(int n) { mnPkContactId = n; }
    public void setContact(java.lang.String s) { msContact = s; }
    public void setContactPrefix(java.lang.String s) { msContactPrefix = s; }
    public void setContactSuffix(java.lang.String s) { msContactSuffix = s; }
    public void setLastname(java.lang.String s) { msLastname = s; }
    public void setFirstname(java.lang.String s) { msFirstname = s; }
    public void setCharge(java.lang.String s) { msCharge = s; }
    public void setTelAreaCode01(java.lang.String s) { msTelAreaCode01 = s; }
    public void setTelNumber01(java.lang.String s) { msTelNumber01 = s; }
    public void setTelExt01(java.lang.String s) { msTelExt01 = s; }
    public void setTelAreaCode02(java.lang.String s) { msTelAreaCode02 = s; }
    public void setTelNumber02(java.lang.String s) { msTelNumber02 = s; }
    public void setTelExt02(java.lang.String s) { msTelExt02 = s; }
    public void setTelAreaCode03(java.lang.String s) { msTelAreaCode03 = s; }
    public void setTelNumber03(java.lang.String s) { msTelNumber03 = s; }
    public void setTelExt03(java.lang.String s) { msTelExt03 = s; }
    public void setNextelId01(java.lang.String s) { msNextelId01 = s; }
    public void setNextelId02(java.lang.String s) { msNextelId02 = s; }
    public void setEmail01(java.lang.String s) { msEmail01 = s; }
    public void setEmail02(java.lang.String s) { msEmail02 = s; }
    public void setSkype01(java.lang.String s) { msSkype01 = s; }
    public void setSkype02(java.lang.String s) { msSkype02 = s; }
    public void setIsDeleted(boolean b) { mbIsDeleted = b; }
    public void setFkContactTypeId(int n) { mnFkContactTypeId = n; }
    public void setFkTelephoneType01Id(int n) { mnFkTelephoneType01Id = n; }
    public void setFkTelephoneType02Id(int n) { mnFkTelephoneType02Id = n; }
    public void setFkTelephoneType03Id(int n) { mnFkTelephoneType03Id = n; }
    public void setFkUserNewId(int n) { mnFkUserNewId = n; }
    public void setFkUserEditId(int n) { mnFkUserEditId = n; }
    public void setFkUserDeleteId(int n) { mnFkUserDeleteId = n; }
    public void setUserNewTs(java.util.Date t) { mtUserNewTs = t; }
    public void setUserEditTs(java.util.Date t) { mtUserEditTs = t; }
    public void setUserDeleteTs(java.util.Date t) { mtUserDeleteTs = t; }

    public int getPkBizPartnerBranchId() { return mnPkBizPartnerBranchId; }
    public int getPkContactId() { return mnPkContactId; }
    public java.lang.String getContact() { return msContact; }
    public java.lang.String getContactPrefix() { return msContactPrefix; }
    public java.lang.String getContactSuffix() { return msContactSuffix; }
    public java.lang.String getLastname() { return msLastname; }
    public java.lang.String getFirstname() { return msFirstname; }
    public java.lang.String getCharge() { return msCharge; }
    public java.lang.String getTelAreaCode01() { return msTelAreaCode01; }
    public java.lang.String getTelNumber01() { return msTelNumber01; }
    public java.lang.String getTelExt01() { return msTelExt01; }
    public java.lang.String getTelAreaCode02() { return msTelAreaCode02; }
    public java.lang.String getTelNumber02() { return msTelNumber02; }
    public java.lang.String getTelExt02() { return msTelExt02; }
    public java.lang.String getTelAreaCode03() { return msTelAreaCode03; }
    public java.lang.String getTelNumber03() { return msTelNumber03; }
    public java.lang.String getTelExt03() { return msTelExt03; }
    public java.lang.String getNextelId01() { return msNextelId01; }
    public java.lang.String getNextelId02() { return msNextelId02; }
    public java.lang.String getEmail01() { return msEmail01; }
    public java.lang.String getEmail02() { return msEmail02; }
    public java.lang.String getSkype01() { return msSkype01; }
    public java.lang.String getSkype02() { return msSkype02; }
    public boolean getIsDeleted() { return mbIsDeleted; }
    public int getFkContactTypeId() { return mnFkContactTypeId; }
    public int getFkTelephoneType01Id() { return mnFkTelephoneType01Id; }
    public int getFkTelephoneType02Id() { return mnFkTelephoneType02Id; }
    public int getFkTelephoneType03Id() { return mnFkTelephoneType03Id; }
    public int getFkUserNewId() { return mnFkUserNewId; }
    public int getFkUserEditId() { return mnFkUserEditId; }
    public int getFkUserDeleteId() { return mnFkUserDeleteId; }
    public java.util.Date getUserNewTs() { return mtUserNewTs; }
    public java.util.Date getUserEditTs() { return mtUserEditTs; }
    public java.util.Date getUserDeleteTs() { return mtUserDeleteTs; }

    public void setDbmsContactType(String s) { msDbmsContactType = s; }
    public void setDbmsTelephoneType01(String s) { msDbmsTelephoneType01 = s; }
    public void setDbmsTelephoneType02(String s) { msDbmsTelephoneType02 = s; }
    public void setDbmsTelephoneType03(String s) { msDbmsTelephoneType03 = s; }
    public void setDbmsUserNew(String s) { msDbmsUserNew = s; }
    public void setDbmsUserEdit(String s) { msDbmsUserEdit = s; }
    public void setDbmsUserDelete(String s) { msDbmsUserDelete = s; }

    public java.lang.String getDbmsContactType() { return msDbmsContactType; }
    public java.lang.String getDbmsTelephoneType01() { return msDbmsTelephoneType01; }
    public java.lang.String getDbmsTelephoneType02() { return msDbmsTelephoneType02; }
    public java.lang.String getDbmsTelephoneType03() { return msDbmsTelephoneType03; }
    public java.lang.String getDbmsUserNew() { return msDbmsUserNew; }
    public java.lang.String getDbmsUserEdit() { return msDbmsUserEdit; }
    public java.lang.String getDbmsUserDelete() { return msDbmsUserDelete; }

    public int getDmbsFkBizPartnerId() { return mnDbmsFkBizPartnerId; }

    public java.lang.String getAuxTelephone01() { return msTelNumber01.length() == 0 ? "" : (msDbmsTelephoneType01 + " " + (msTelAreaCode01.length() == 0 ? "" : "(" + msTelAreaCode01 + ") ") + msTelNumber01 + (msTelExt01.length() == 0 ? "" : " ext. " + msTelExt01)); }
    public java.lang.String getAuxTelephone02() { return msTelNumber02.length() == 0 ? "" : (msDbmsTelephoneType02 + " " + (msTelAreaCode02.length() == 0 ? "" : "(" + msTelAreaCode02 + ") ") + msTelNumber02 + (msTelExt02.length() == 0 ? "" : " ext. " + msTelExt02)); }
    public java.lang.String getAuxTelephone03() { return msTelNumber03.length() == 0 ? "" : (msDbmsTelephoneType03 + " " + (msTelAreaCode03.length() == 0 ? "" : "(" + msTelAreaCode03 + ") ") + msTelNumber03 + (msTelExt03.length() == 0 ? "" : " ext. " + msTelExt03)); }

    public java.lang.String getAuxTelephoneNumber01() { return msTelNumber01.length() == 0 ? "" : ((msTelAreaCode01.length() == 0 ? "" : "(" + msTelAreaCode01 + ") ") + msTelNumber01 + (msTelExt01.length() == 0 ? "" : " ext. " + msTelExt01)); }
    public java.lang.String getAuxTelephoneNumber02() { return msTelNumber02.length() == 0 ? "" : ((msTelAreaCode02.length() == 0 ? "" : "(" + msTelAreaCode02 + ") ") + msTelNumber02 + (msTelExt02.length() == 0 ? "" : " ext. " + msTelExt02)); }
    public java.lang.String getAuxTelephoneNumber03() { return msTelNumber03.length() == 0 ? "" : ((msTelAreaCode03.length() == 0 ? "" : "(" + msTelAreaCode03 + ") ") + msTelNumber03 + (msTelExt03.length() == 0 ? "" : " ext. " + msTelExt03)); }
    
    public java.lang.String getAuxTelephoneNumbers() {
        String numbers = getAuxTelephoneNumber01();
        numbers += (numbers.isEmpty() ? "" : (getAuxTelephoneNumber02().isEmpty() ? "" : ", ")) + getAuxTelephoneNumber02();
        numbers += (numbers.isEmpty() ? "" : (getAuxTelephoneNumber03().isEmpty() ? "" : ", ")) + getAuxTelephoneNumber03();
        return numbers;
    }

    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkBizPartnerBranchId = ((int[]) pk)[0];
        mnPkContactId= ((int[]) pk)[1];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkBizPartnerBranchId, mnPkContactId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkBizPartnerBranchId = 0;
        mnPkContactId = 0;
        msContact = "";
        msContactPrefix = "";
        msContactSuffix = "";
        msLastname = "";
        msFirstname = "";
        msCharge = "";
        msTelAreaCode01 = "";
        msTelNumber01 = "";
        msTelExt01 = "";
        msTelAreaCode02 = "";
        msTelNumber02 = "";
        msTelExt02 = "";
        msTelAreaCode03 = "";
        msTelNumber03 = "";
        msTelExt03 = "";
        msNextelId01 = "";
        msNextelId02 = "";
        msEmail01 = "";
        msEmail02 = "";
        msSkype01 = "";
        msSkype02 = "";
        mbIsDeleted = false;
        mnFkContactTypeId = 0;
        mnFkTelephoneType01Id = 0;
        mnFkTelephoneType02Id = 0;
        mnFkTelephoneType03Id = 0;
        mnFkUserNewId = 0;
        mnFkUserEditId = 0;
        mnFkUserDeleteId = 0;
        mtUserNewTs = null;
        mtUserEditTs = null;
        mtUserDeleteTs = null;

        mnDbmsFkBizPartnerId = 0;
        msDbmsContactType = "";
        msDbmsTelephoneType01 = "";
        msDbmsTelephoneType02 = "";
        msDbmsTelephoneType03 = "";
        msDbmsUserNew = "";
        msDbmsUserEdit = "";
        msDbmsUserDelete = "";
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT bpb_con.*, bpb.fid_bp, tp.tp_con, t1.tp_tel, t2.tp_tel, t3.tp_tel, un.usr, ue.usr, ud.usr " +
                    "FROM erp.bpsu_bpb_con AS bpb_con " +
                    "INNER JOIN erp.bpsu_bpb AS bpb ON " +
                    "bpb_con.id_bpb = bpb.id_bpb " +
                    "INNER JOIN erp.bpss_tp_con AS tp ON " +
                    "bpb_con.fid_tp_con = tp.id_tp_con " +
                    "INNER JOIN erp.bpss_tp_tel AS t1 ON " +
                    "bpb_con.fid_tp_tel_01 = t1.id_tp_tel " +
                    "INNER JOIN erp.bpss_tp_tel AS t2 ON " +
                    "bpb_con.fid_tp_tel_02 = t2.id_tp_tel " +
                    "INNER JOIN erp.bpss_tp_tel AS t3 ON " +
                    "bpb_con.fid_tp_tel_03 = t3.id_tp_tel " +
                    "INNER JOIN erp.usru_usr AS un ON " +
                    "bpb_con.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON " +
                    "bpb_con.fid_usr_edit = ue.id_usr " +
                    "INNER JOIN erp.usru_usr AS ud ON " +
                    "bpb_con.fid_usr_del = ud.id_usr " +
                    "WHERE bpb_con.id_bpb = " + key[0] + " AND bpb_con.id_con = " + key[1] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkBizPartnerBranchId = resultSet.getInt("bpb_con.id_bpb");
                mnPkContactId = resultSet.getInt("bpb_con.id_con");
                msContact = resultSet.getString("bpb_con.bpb_con");
                msContactPrefix = resultSet.getString("bpb_con.bpb_con_prefix");
                msContactSuffix = resultSet.getString("bpb_con.bpb_con_suffix");
                msLastname = resultSet.getString("bpb_con.lastname");
                msFirstname = resultSet.getString("bpb_con.firstname");
                msCharge = resultSet.getString("bpb_con.charge");
                msTelAreaCode01 = resultSet.getString("bpb_con.tel_area_code_01");
                msTelNumber01 = resultSet.getString("bpb_con.tel_num_01");
                msTelExt01 = resultSet.getString("bpb_con.tel_ext_01");
                msTelAreaCode02 = resultSet.getString("bpb_con.tel_area_code_02");
                msTelNumber02 = resultSet.getString("bpb_con.tel_num_02");
                msTelExt02 = resultSet.getString("bpb_con.tel_ext_02");
                msTelAreaCode03 = resultSet.getString("bpb_con.tel_area_code_03");
                msTelNumber03 = resultSet.getString("bpb_con.tel_num_03");
                msTelExt03 = resultSet.getString("bpb_con.tel_ext_03");
                msNextelId01 = resultSet.getString("bpb_con.nextel_id_01");
                msNextelId02 = resultSet.getString("bpb_con.nextel_id_02");
                msEmail01 = resultSet.getString("bpb_con.email_01");
                msEmail02 = resultSet.getString("bpb_con.email_02");
                msSkype01 = resultSet.getString("bpb_con.skype_01");
                msSkype02 = resultSet.getString("bpb_con.skype_02");
                mbIsDeleted = resultSet.getBoolean("bpb_con.b_del");
                mnFkContactTypeId = resultSet.getInt("bpb_con.fid_tp_con");
                mnFkTelephoneType01Id = resultSet.getInt("bpb_con.fid_tp_tel_01");
                mnFkTelephoneType02Id = resultSet.getInt("bpb_con.fid_tp_tel_02");
                mnFkTelephoneType03Id = resultSet.getInt("bpb_con.fid_tp_tel_03");
                mnFkUserNewId = resultSet.getInt("bpb_con.fid_usr_new");
                mnFkUserEditId = resultSet.getInt("bpb_con.fid_usr_edit");
                mnFkUserDeleteId = resultSet.getInt("bpb_con.fid_usr_del");
                mtUserNewTs = resultSet.getTimestamp("bpb_con.ts_new");
                mtUserEditTs = resultSet.getTimestamp("bpb_con.ts_edit");
                mtUserDeleteTs = resultSet.getTimestamp("bpb_con.ts_del");

                mnDbmsFkBizPartnerId = resultSet.getInt("bpb.fid_bp");
                msDbmsContactType = resultSet.getString("tp.tp_con");
                msDbmsTelephoneType01 = resultSet.getString("t1.tp_tel");
                msDbmsTelephoneType02 = resultSet.getString("t2.tp_tel");
                msDbmsTelephoneType03 = resultSet.getString("t3.tp_tel");
                msDbmsUserNew = resultSet.getString("un.usr");
                msDbmsUserEdit = resultSet.getString("ue.usr");
                msDbmsUserDelete = resultSet.getString("ud.usr");

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
    public int save(java.sql.Connection connection) {
        int nParam = 1;
        CallableStatement callableStatement = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            callableStatement = connection.prepareCall(
                    "{ CALL erp.bpsu_bpb_con_save(" +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?," +
                    "?, ?) }");
            callableStatement.setInt(nParam++, mnPkBizPartnerBranchId);
            callableStatement.setInt(nParam++, mnPkContactId);
            callableStatement.setString(nParam++, msContact);
            callableStatement.setString(nParam++, msContactPrefix);
            callableStatement.setString(nParam++, msContactSuffix);
            callableStatement.setString(nParam++, msLastname);
            callableStatement.setString(nParam++, msFirstname);
            callableStatement.setString(nParam++, msCharge);
            callableStatement.setString(nParam++, msTelAreaCode01);
            callableStatement.setString(nParam++, msTelNumber01);
            callableStatement.setString(nParam++, msTelExt01);
            callableStatement.setString(nParam++, msTelAreaCode02);
            callableStatement.setString(nParam++, msTelNumber02);
            callableStatement.setString(nParam++, msTelExt02);
            callableStatement.setString(nParam++, msTelAreaCode03);
            callableStatement.setString(nParam++, msTelNumber03);
            callableStatement.setString(nParam++, msTelExt03);
            callableStatement.setString(nParam++, msNextelId01);
            callableStatement.setString(nParam++, msNextelId02);
            callableStatement.setString(nParam++, msEmail01);
            callableStatement.setString(nParam++, msEmail02);
            callableStatement.setString(nParam++, msSkype01);
            callableStatement.setString(nParam++, msSkype02);
            callableStatement.setBoolean(nParam++, mbIsDeleted);
            callableStatement.setInt(nParam++, mnFkContactTypeId);
            callableStatement.setInt(nParam++, mnFkTelephoneType01Id);
            callableStatement.setInt(nParam++, mnFkTelephoneType02Id);
            callableStatement.setInt(nParam++, mnFkTelephoneType03Id);
            callableStatement.setInt(nParam++, mbIsRegistryNew ? mnFkUserNewId : mnFkUserEditId);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.SMALLINT);
            callableStatement.registerOutParameter(nParam++, java.sql.Types.VARCHAR);
            callableStatement.execute();

            mnPkContactId = callableStatement.getInt(nParam - 3);
            mnDbmsErrorId = callableStatement.getInt(nParam - 2);
            msDbmsError = callableStatement.getString(nParam - 1);

            if (mnDbmsErrorId != 0) {
                throw new Exception(msDbmsError);
            }
            else {
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
    public java.util.Date getLastDbUpdate() {
        return mtUserEditTs;
    }
}
