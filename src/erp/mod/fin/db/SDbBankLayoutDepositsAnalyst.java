/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.lib.SLibConstants;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbBankLayoutDepositsAnalyst extends SDbRegistryUser {
    
    protected int mnPkBankLayoutDepositsId;
    protected int mnPkUserAnalystId;
    protected int mnSourceMovements;
    protected double mdSourceAmount;
    protected int mnImportedMovements;
    protected double mdImportedAmount;
    protected String msUserAnalystXml;

    /*
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected SXmlAnalystImportation moXmlObject;
    protected ArrayList<SXmlAnalystImportationPayment> maXmlRows;
    protected ArrayList<SAnalystDepositRow> maDepositsRows;

    public void setPkBankLayoutDepositsId(int n) { mnPkBankLayoutDepositsId = n; }
    public void setPkUserAnalystId(int n) { mnPkUserAnalystId = n; }
    public void setSourceMovements(int n) { mnSourceMovements = n; }
    public void setSourceAmount(double d) { mdSourceAmount = d; }
    public void setImportedMovements(int n) { mnImportedMovements = n; }
    public void setImportedAmount(double d) { mdImportedAmount = d; }
    public void setUserAnalystXml(String s) { msUserAnalystXml = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public void setXmlObject(SXmlAnalystImportation x) { moXmlObject = x; }
    
    public int getPkBankLayoutDepositsId() { return mnPkBankLayoutDepositsId; }
    public int getPkUserAnalystId() { return mnPkUserAnalystId; }
    public int getSourceMovements() { return mnSourceMovements; }
    public double getSourceAmount() { return mdSourceAmount; }
    public int getImportedMovements() { return mnImportedMovements; }
    public double getImportedAmount() { return mdImportedAmount; }
    public String getUserAnalystXml() { return msUserAnalystXml; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public SXmlAnalystImportation getXmlObject() { return moXmlObject; }
    public ArrayList<SXmlAnalystImportationPayment> getXmlRows() { return maXmlRows; }
    public ArrayList<SAnalystDepositRow> getDepositsRows() { return maDepositsRows; }
    
    /**
     * Puts the values of the import form in the xml file
     * 
     * @param rows Lines of the import form
     * @param analystId
     * @param userId
     * @return The resulting xml object
     */
    public SXmlAnalystImportation populateAnalystImportation(ArrayList <SAnalystDepositRow> rows, int analystId, int userId) {
        SXmlAnalystImportation xmlImport = new SXmlAnalystImportation();
        SXmlAnalystImportationPayment xmlImportPayment = null;        
        
        xmlImport.getAttribute(SXmlAnalystImportation.ATT_ANALYST_ID).setValue(analystId);
        
        for (SAnalystDepositRow row : rows) {
            if(row.getPkAnalystId() == userId) {
                xmlImportPayment = new SXmlAnalystImportationPayment();

                xmlImportPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAYMENT_ID).setValue(row.getPkDepositId());
                xmlImportPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAYMENT_IMPORTED).setValue(row.getImported());
                xmlImportPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_ACC_REF).setValue(row.getReferenceAdv());
                xmlImportPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_AMOUNT_CY).setValue(row.getAmountOrigCurrency());
                xmlImportPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_EXCH_RATE).setValue(row.getExchangeRate());
                xmlImportPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_AMOUNT).setValue(row.getAmountLocal());
                xmlImportPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_YEAR_ID).setValue(row.getRecord() != null ? row.getRecord().getPkYearId() : SLibConstants.UNDEFINED);
                xmlImportPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_PER_ID).setValue(row.getRecord() != null ? row.getRecord().getPkPeriodId() : SLibConstants.UNDEFINED);
                xmlImportPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_BKC_ID).setValue(row.getRecord() != null ? row.getRecord().getPkBookkeepingCenterId() : SLibConstants.UNDEFINED);
                xmlImportPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_TP_ID).setValue(row.getRecord() != null ? row.getRecord().getPkRecordTypeId() : "");
                xmlImportPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_NUM_ID).setValue(row.getRecord() != null ? row.getRecord().getPkNumberId() : SLibConstants.UNDEFINED);
                xmlImportPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_BKC_YEAR).setValue(row.getBkcYear());
                xmlImportPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_BKC_NUM).setValue(row.getBkcNum());

                xmlImport.getXmlElements().add(xmlImportPayment);
            }
        }
        
        return xmlImport;
    }
    
    
    public SDbBankLayoutDepositsAnalyst() {
        super(SModConsts.FIN_LAY_BANK_DEP_ANA);
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBankLayoutDepositsId = pk[0];
        mnPkUserAnalystId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBankLayoutDepositsId, mnPkUserAnalystId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkBankLayoutDepositsId = 0;
        mnPkUserAnalystId = 0;
        mnSourceMovements = 0;
        mdSourceAmount = 0;
        mnImportedMovements = 0;
        mdImportedAmount = 0;
        msUserAnalystXml = null;
        mbDeleted = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maDepositsRows = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_lay_bank_dep = " + mnPkBankLayoutDepositsId + " AND id_usr_ana = " + mnPkUserAnalystId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_lay_bank_dep = " + pk[0] + " AND id_usr_ana = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {

    }

    @Override
    public void read(SGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;
        initRegistry();
        initQueryMembers();
        mnQueryResultId = SDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().getConnection().createStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkBankLayoutDepositsId = resultSet.getInt("id_lay_bank_dep");
            mnPkUserAnalystId = resultSet.getInt("id_usr_ana");
            mnSourceMovements = resultSet.getInt("src_movs");
            mdSourceAmount = resultSet.getDouble("src_amt");
            mnImportedMovements = resultSet.getInt("imp_movs");
            mdImportedAmount = resultSet.getDouble("imp_amt");
            msUserAnalystXml = resultSet.getString("usr_ana_xml");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            //readXml();

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
            computePrimaryKey(session);
        }
        
        moXmlObject = populateAnalystImportation(maDepositsRows, mnPkUserAnalystId, session.getUser().getPkUserId());
        msUserAnalystXml = moXmlObject.getXmlString();

        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "INSERT INTO " + SModConsts.TablesMap.get(SModConsts.FIN_LAY_BANK_DEP_ANA) + " VALUES (" +
                    mnPkBankLayoutDepositsId + ", " + 
                    mnPkUserAnalystId + ", " + 
                    mnSourceMovements + ", " + 
                    mdSourceAmount + ", " + 
                    mnImportedMovements + ", " + 
                    mdImportedAmount + ", " + 
                    "'" + msUserAnalystXml + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" +
                    ")";
        }
        else {
            msSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.FIN_LAY_BANK_DEP_ANA) + " SET " +
                    "id_lay_bank_dep = " + mnPkBankLayoutDepositsId + ", " +
                    "id_usr_ana = " + mnPkUserAnalystId + ", " +
                    "src_movs = " + mnSourceMovements + ", " +
                    "src_amt = " + mdSourceAmount + ", " +
                    "imp_movs = " + mnImportedMovements + ", " +
                    "imp_amt = " + mdImportedAmount + ", " +
                    "usr_ana_xml = '" + msUserAnalystXml + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }

    @Override
    public SDbBankLayoutDepositsAnalyst clone() throws CloneNotSupportedException {
        SDbBankLayoutDepositsAnalyst registry = new SDbBankLayoutDepositsAnalyst();

        registry.setPkBankLayoutDepositsId(this.getPkBankLayoutDepositsId());
        registry.setPkUserAnalystId(this.getPkUserAnalystId());
        registry.setSourceMovements(this.getSourceMovements());
        registry.setSourceAmount(this.getSourceAmount());
        registry.setImportedMovements(this.getImportedMovements());
        registry.setImportedAmount(this.getImportedAmount());
        registry.setUserAnalystXml(this.getUserAnalystXml());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        
        return registry;
    }

    @Override
    public void saveField(Statement statement, int[] pk, int field, Object value) throws SQLException, Exception {

    }
    
    @Override
    public boolean canSave(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);
        
        return can;
    }
    
    @Override
    public boolean canDelete(final SGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);
        
        return can;
    }
}
