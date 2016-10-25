/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mfin.data;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.table.STableUtilities;
import erp.mod.fin.db.SFinRecordLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas
 */
public class SDataLayoutBank extends erp.lib.data.SDataRegistry implements java.io.Serializable {

    protected int mnPkLayBankId;
    protected Date mtDateLayout;
    protected Date mtDateDue;
    protected String msConcept;
    protected int mnConsecutive;
    protected double mdAmount;
    protected double mdAmountPayed;
    protected int mnDocs;
    protected int mnDocsPayed;
    protected String msLayoutText;
    protected String msLayoutXml;
    protected boolean mbDeleted;
    protected int mnFkBankLayoutId;
    protected int mnFkBankLayoutTypeId;
    protected int mnFkBankCompanyBranchId;
    protected int mnFkBankAccountCashId;
    protected int mnFkDpsCurId;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    
    protected String msAuxTitle;
    
    protected ArrayList<SFinRecordLayout> maFinRecordLayout;

    private boolean validatePeriodRecordLayout(java.sql.Connection connection, java.lang.String psMsg) throws Exception {
        int i = 0;
        String sMsg = psMsg;
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        CallableStatement oCallableStatement = null;
        
        return true;
    }
    
    public SDataLayoutBank() {
        super(SDataConstants.FIN_LAY_BANK);
    }

    public void setPkLayBankId(int n) { mnPkLayBankId = n; }
    public void setDateLayout(Date t) { mtDateLayout = t; }
    public void setDateDue(Date t) { mtDateDue = t; }
    public void setConcept(String s) { msConcept = s; }
    public void setConsecutive(int n) { mnConsecutive = n; }
    public void setAmount(double d) { mdAmount = d; }
    public void setAmountPayed(double d) { mdAmountPayed = d; }
    public void setDocs(int n) { mnDocs = n; }
    public void setDocsPayed(int n) { mnDocsPayed = n; }
    public void setLayoutText(String s) { msLayoutText = s; }
    public void setLayoutXml(String s) { msLayoutXml = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkBankLayoutId(int n) { mnFkBankLayoutId = n; }
    public void setFkBankLayoutTypeId(int n) { mnFkBankLayoutTypeId = n; }
    public void setFkBankCompanyBranchId(int n) { mnFkBankCompanyBranchId = n; }
    public void setFkBankAccountCashId(int n) { mnFkBankAccountCashId = n; }
    public void setFkDpsCurId(int n) { mnFkDpsCurId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }
    
    public void setAuxTitle(String s) { msAuxTitle = s; }
    
    public int getPkLayBankId() { return mnPkLayBankId; }
    public Date getDateLayout() { return mtDateLayout; }
    public Date getDateDue() { return mtDateDue; }
    public String getConcept() { return msConcept; }
    public int getConsecutive() { return mnConsecutive; }
    public double getAmount() { return mdAmount; }
    public double getAmountPayed() { return mdAmountPayed; }
    public int getDocs() { return mnDocs; }
    public int getDocsPayed() { return mnDocsPayed; }
    public String getLayoutText() { return msLayoutText; }
    public String getLayoutXml() { return msLayoutXml; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkBankLayoutId() { return mnFkBankLayoutId; }
    public int getFkBankLayoutTypeId() { return mnFkBankLayoutTypeId; }
    public int getFkBankCompanyBranchId() { return mnFkBankCompanyBranchId; }
    public int getFkBankAccountCashId() { return mnFkBankAccountCashId; }
    public int getFkDpsCurId() { return mnFkDpsCurId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public String getAuxTitle() { return msAuxTitle; }
    
    public ArrayList<SFinRecordLayout> getFinRecordLayout() { return maFinRecordLayout; }
    
    @Override
    public void setPrimaryKey(java.lang.Object pk) {
        mnPkLayBankId = ((int[]) pk)[0];
    }

    @Override
    public java.lang.Object getPrimaryKey() {
        return new int[] { mnPkLayBankId };
    }

    @Override
    public void reset() {
        super.resetRegistry();

        mnPkLayBankId = 0;
        mtDateLayout = null;
        mtDateDue = null;
        msConcept = "";
        mnConsecutive = 0;
        mdAmount = 0;
        mdAmountPayed = 0;
        mnDocs = 0;
        mnDocsPayed = 0;
        msLayoutText = null;
        msLayoutXml = null;
        mbDeleted = false;
        mnFkBankLayoutId = 0;
        mnFkBankLayoutTypeId = 0;
        mnFkBankCompanyBranchId = 0;
        mnFkBankAccountCashId = 0;
        mnFkDpsCurId = 1;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        msAuxTitle = "";
        maFinRecordLayout = new ArrayList<SFinRecordLayout>();
    }
    
    public void computePrimaryKey(java.sql.Connection connection) throws SQLException, Exception {
        String sql = "";
        ResultSet resultSet = null;

        mnPkLayBankId = 0;

        sql = "SELECT COALESCE(MAX(id_lay_bank), 0) + 1 FROM fin_lay_bank " + " ";
        resultSet = connection.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            mnPkLayBankId = resultSet.getInt(1);
        }
    }

    @Override
    public int read(java.lang.Object pk, java.sql.Statement statement) {
        int[] key = (int[]) pk;
        String sql = "";
        ResultSet resultSet = null;
        Statement statementAux = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;
        reset();

        try {
            sql = "SELECT * FROM fin_lay_bank WHERE id_lay_bank = " + key[0] + " ";
            resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new Exception(SLibConstants.MSG_ERR_REG_FOUND_NOT);
            }
            else {
                mnPkLayBankId = resultSet.getInt("id_lay_bank");
                mtDateLayout = resultSet.getDate("dt_lay");
                mtDateDue = resultSet.getDate("dt_due");
                msConcept = resultSet.getString("cpt");
                mnConsecutive = resultSet.getInt("con");
                mdAmount = resultSet.getDouble("amt");
                mdAmountPayed = resultSet.getDouble("amt_pay");
                mnDocs = resultSet.getInt("dps");
                mnDocsPayed = resultSet.getInt("dps_pay");
                msLayoutText = resultSet.getString("lay_txt");
                msLayoutXml = resultSet.getString("lay_xml");
                mbDeleted = resultSet.getBoolean("b_del");
                mnFkBankLayoutId = resultSet.getInt("fk_lay_bank");
                mnFkBankLayoutTypeId = resultSet.getInt("fk_tp_lay_bank");
                mnFkBankCompanyBranchId = resultSet.getInt("fk_bank_cob");
                mnFkBankAccountCashId = resultSet.getInt("fk_bank_acc_cash");
                mnFkDpsCurId = resultSet.getInt("fk_dps_cur");
                mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
                mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
                mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
                mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

                statementAux = statement.getConnection().createStatement();
                
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
        String sql = "";
        ResultSet resultSet = null;

        mnLastDbActionResult = SLibConstants.UNDEFINED;

        try {
            if (mbIsRegistryNew) {
                sql = "SELECT COUNT(*) FROM fin_lay_bank WHERE id_lay_bank = " + mnPkLayBankId + " ";
                resultSet = connection.createStatement().executeQuery(sql);

                if (resultSet.next()) {
                    mbIsRegistryNew = resultSet.getInt(1) == 0;
                }
            }

            if (mbIsRegistryNew) {
                computePrimaryKey(connection);
                mbDeleted = false;
                mnFkUserUpdateId = SUtilConsts.USR_NA_ID;

                sql = "INSERT INTO fin_lay_bank VALUES (" +
                        mnPkLayBankId + ", " + 
                        "'" + SLibUtils.DbmsDateFormatDate.format(mtDateLayout) + "', " + 
                        "'" + SLibUtils.DbmsDateFormatDate.format(mtDateDue) + "', " + 
                        "'" + msConcept + "', " + 
                        mnConsecutive + ", " + 
                        mdAmount + ", " + 
                        mdAmountPayed + ", " + 
                        mnDocs + ", " + 
                        mnDocsPayed + ", " + 
                        "'" + msLayoutText + "', " + 
                        "'" + msLayoutXml + "', " + 
                        (mbDeleted ? 1 : 0) + ", " + 
                        mnFkBankLayoutId + ", " + 
                        mnFkBankLayoutTypeId + ", " + 
                        mnFkBankCompanyBranchId + ", " + 
                        mnFkBankAccountCashId + ", " + 
                        mnFkDpsCurId + ", " +
                        mnFkUserInsertId + ", " + 
                        mnFkUserUpdateId + ", " + 
                        "NOW()" + " , " +
                        "NOW()" + " " +
                        ")";
            }
            else {
                sql = "UPDATE fin_lay_bank SET " +
                        //"id_lay_bank = " + mnPkLayBankId + ", " +
                        "dt_lay = '" + SLibUtils.DbmsDateFormatDate.format(mtDateLayout) + "', " +
                        "dt_due = '" + SLibUtils.DbmsDateFormatDate.format(mtDateDue) + "', " +
                        "cpt = '" + msConcept + "', " +
                        "con = " + mnConsecutive + ", " +
                        "amt = " + mdAmount + ", " +
                        "amt_pay = " + mdAmountPayed + ", " +
                        "dps = " + mnDocs + ", " +
                        "dps_pay = " + mnDocsPayed + ", " +
                        "lay_txt = '" + msLayoutText + "', " +
                        "lay_xml = '" + msLayoutXml + "', " +
                        "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                        "fk_lay_bank = " + mnFkBankLayoutId + ", " +
                        "fk_tp_lay_bank = " + mnFkBankLayoutTypeId + ", " +
                        "fk_bank_cob = " + mnFkBankCompanyBranchId + ", " +
                        "fk_bank_acc_cash = " + mnFkBankAccountCashId + ", " +
                        "fk_dps_cur = " + mnFkDpsCurId + ", " +
                        "fk_usr_ins = " + mnFkUserInsertId + ", " +
                        "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                        "ts_usr_ins = " + "NOW()" + ", " +
                        "ts_usr_upd = " + "NOW()" + " " +
                        "WHERE id_lay_bank = " + mnPkLayBankId + " ";
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
    public java.util.Date getLastDbUpdate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public int canSave(java.sql.Connection connection) {
        int can = super.canSave(connection);
        
        try {
            if (validatePeriodRecordLayout(connection, "No se puede guardar el layout: ")) {
                mnLastDbActionResult = SLibConstants.DB_CAN_DELETE_YES;
            }
        }
        catch (Exception exception) {
            can = SLibConstants.DB_CAN_SAVE_NO;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_SAVE;
            }
            SLibUtilities.printOutException(this, exception);
        }
        
        return can;
    }
    
    @Override
    public int canDelete(java.sql.Connection connection) {
        int can = super.canSave(connection);
        
        try {
            if (validatePeriodRecordLayout(connection, "No se puede eliminar el layout: ")) {
                mnLastDbActionResult = SLibConstants.DB_CAN_DELETE_YES;
            }
        }
        catch (Exception exception) {
            can = SLibConstants.DB_CAN_DELETE_NO;
            if (msDbmsError.isEmpty()) {
                msDbmsError = SLibConstants.MSG_ERR_DB_REG_CAN_DELETE;
            }
            SLibUtilities.printOutException(this, exception);
        }
        
        return can;
    }
    
    public void writeLayout(SClientInterface client) {
        BufferedWriter bw = null;
        
        client.getFileChooser().setSelectedFile(new File(client.getSessionXXX().getFormatters().getFileNameDatetimeFormat().format(new java.util.Date()) + " " + msAuxTitle + ".txt"));
        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());

            try {
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ASCII"));
                
                bw.write(msLayoutText);
                bw.close();
                
                if (client.showMsgBoxConfirm(SLibConstants.MSG_INF_FILE_CREATE + file.getPath() + "\n" + SLibConstants.MSG_CNF_FILE_OPEN) == JOptionPane.YES_OPTION) {
                    SLibUtilities.launchFile(file.getPath());
                }
            }
            catch (java.lang.Exception e) {
                SLibUtilities.renderException(STableUtilities.class.getName(), e);
            }
        }
    }
}
