/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod.fin.db;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.lib.SLibConstants;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataBookkeepingNumber;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistryUser;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Edwin Carmona
 */
public class SDbBankLayoutDeposits extends SDbRegistryUser {
    
    protected int mnPkBankLayoutDepositsId;
    protected String msSourceName;
    protected java.sql.Blob moSourceFile_n;
    protected int mnSourceMovements;
    protected double mdSourceAmount;
    protected Date mtDepositsDate;
    protected String msDepositsXml;
    //protected boolean mbDeleted;
    protected int mnFkBankLayoutDepositsTypeId;
    protected int mnFkBankCompanyBranchId;
    protected int mnFkBankAccountCashId;
    protected int mnFkCurrencyId;
    
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected SXmlImportFile moXmlObject;
    protected ArrayList<SXmlImportFilePayment> maXmlRows;
    
    protected HashMap<Integer, SDbBankLayoutDepositsAnalyst> maAnalystImportations;
    protected ArrayList<SAnalystDepositRow> maDepositsRows;
    protected byte[] maBytes;

    public void setPkBankLayoutDepositsId(int n) { mnPkBankLayoutDepositsId = n; }
    public void setSourceName(String s) { msSourceName = s; }
    public void setSourceFile_n(java.sql.Blob o) { moSourceFile_n = o; }
    public void setSourceMovements(int n) { mnSourceMovements = n; }
    public void setSourceAmount(double d) { mdSourceAmount = d; }
    public void setDepositsDate(Date t) { mtDepositsDate = t; }
    public void setDepositsXml(String s) { msDepositsXml = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkBankLayoutDepositsTypeId(int n) { mnFkBankLayoutDepositsTypeId = n; }
    public void setFkBankCompanyBranchId(int n) { mnFkBankCompanyBranchId = n; }
    public void setFkBankAccountCashId(int n) { mnFkBankAccountCashId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public void setXmlObject(SXmlImportFile o) { moXmlObject = o; }
    public void setBytes(byte[] o) { maBytes = o; }
    
    public int getPkBankLayoutDepositsId() { return mnPkBankLayoutDepositsId; }
    public String getSourceName() { return msSourceName; }
    public java.sql.Blob getSourceFile_n() { return moSourceFile_n; }
    public int getSourceMovements() { return mnSourceMovements; }
    public double getSourceAmount() { return mdSourceAmount; }
    public Date getDepositsDate() { return mtDepositsDate; }
    public String getDepositsXml() { return msDepositsXml; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkBankLayoutDepositsTypeId() { return mnFkBankLayoutDepositsTypeId; }
    public int getFkBankCompanyBranchId() { return mnFkBankCompanyBranchId; }
    public int getFkBankAccountCashId() { return mnFkBankAccountCashId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }
    
    public SXmlImportFile getXmlObject() { return moXmlObject; }
    public ArrayList<SXmlImportFilePayment> getXmlRows() { return maXmlRows; }
    public ArrayList<SAnalystDepositRow> getDepositsRows() { return maDepositsRows; }
    public byte[] getByte() { return maBytes; }
    
    public HashMap<Integer,SDbBankLayoutDepositsAnalyst> getAnalystImportations() { return maAnalystImportations; }
    
    /**
     * Read and save the corresponding record and create the entries to be generated for import
     * 
     * @param session
     * @throws Exception 
     */
    private void computeRecord(final SGuiSession session) throws Exception {
        SDataRecord record = null;
        if (maDepositsRows != null) {
            for (SAnalystDepositRow deposit : maDepositsRows) {
                if (deposit.getPkAnalystId() != SLibConstants.UNDEFINED && deposit.getBizPartnerAccountId() != null && !deposit.getBizPartnerAccountId().isEmpty()) {
                    record = null;
                    if (deposit.getRecord() != null) {
                        if (deposit.getBkcYear() == SLibConstants.UNDEFINED && deposit.getBkcNum() == SLibConstants.UNDEFINED) {
                            record = processRecord(session, deposit.getRecord().getPrimaryKey(), session.getUser().getPkUserId(), deposit, false);
                        }
                        else {
                            if (!deposit.getImported()) {
                                record = processRecord(session, deposit.getRecord().getPrimaryKey(), session.getUser().getPkUserId(), deposit, true);
                            }
                        }
                    }
                    
                    if (record != null) {
                        if (record.save(session.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
                        }
                        if (record.read(deposit.getRecord().getPrimaryKey(), session.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Adds to the record the entry generated by the import
     * 
     * @param session
     * @param recordKey Key of the policy selected in the deposit
     * @param pkAnalystId Id of the analyst in session
     * @param deposit Deposit line
     * @return
     * @throws Exception 
     */
    private SDataRecord processRecord(final SGuiSession session, final Object recordKey, final int pkAnalystId, SAnalystDepositRow deposit, final boolean toDelete) throws Exception {
        int nBookkeepingYear = 0;
        int nBookkeepingNum = 0;
        int nSortPosition = 0;
        Statement statementAux = null;
        SDataRecord record = null;
        ArrayList<SDataRecordEntry> recordEntriesToProcess = null;
        ArrayList<SDataRecordEntry> recordEntrys = null;
        SDataBookkeepingNumber bookkeepingNumber = null;
        
        statementAux = session.getStatement().getConnection().createStatement();
        
        recordEntriesToProcess = new ArrayList<>();
        recordEntrys = new ArrayList<>();
        record = new SDataRecord();
        
        if (record.read(recordKey, statementAux) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
        }

        if (deposit.getPkAnalystId() == pkAnalystId) {
            bookkeepingNumber = new SDataBookkeepingNumber();
            bookkeepingNumber.setPkYearId(record.getPkYearId());
            bookkeepingNumber.setFkUserNewId(session.getUser().getPkUserId());
            if (bookkeepingNumber.save(session.getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE_DEP);
            }
            else {
                nBookkeepingYear = bookkeepingNumber.getPkYearId();
                nBookkeepingNum = bookkeepingNumber.getPkNumberId();
            }

            if (toDelete) {
                for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
                    if (SLibUtils.compareKeys(new int[] { entry.getFkBookkeepingYearId_n(), entry.getFkBookkeepingNumberId_n() }, new int[] { deposit.getBkcYear(),deposit.getBkcNum() }) /*&&
                            SLibUtils.belongsTo(new int[] { entry.getFkSystemMoveCategoryIdXXX(), entry.getFkSystemMoveTypeIdXXX() },  new int[][] { SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK, SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH })*/) {
                        entry.setIsDeleted(true);
                        entry.setIsRegistryEdited(true);
                        recordEntriesToProcess.add(entry);
                    }
                }
            }
            else {
                recordEntrys.add(0, createRecordEntryAdvances(session, nBookkeepingYear, nBookkeepingNum, deposit));
                recordEntrys.add(0, createRecordEntryAccountCash(session, nBookkeepingYear, nBookkeepingNum, deposit));
                deposit.setBkcYear(nBookkeepingYear);
                deposit.setBkcNum(nBookkeepingNum);
            }
        }
        
        for (SDataRecordEntry entry : recordEntrys) {
            entry.setPkYearId(record.getPkYearId());
            entry.setPkPeriodId(record.getPkPeriodId());
            entry.setPkBookkeepingCenterId(record.getPkBookkeepingCenterId());
            entry.setPkRecordTypeId(record.getPkRecordTypeId());
            entry.setPkNumberId(record.getPkNumberId());
            entry.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { entry.getFkAccountId() }));
            entry.setDbmsCurrencyKey(session.getSessionCustom().getCurrencyCode(new int[] { entry.getFkCurrencyId() }));

            if (entry.getFkSystemMoveCategoryIdXXX() == SDataConstantsSys.FINS_CT_SYS_MOV_BPS) {
                entry.setDbmsAccountComplement(deposit.getBizPartnerName());
            }
            recordEntriesToProcess.add(entry);
        }
        
        record.getDbmsRecordEntries().addAll(recordEntriesToProcess);
        
        for (SDataRecordEntry entry : record.getDbmsRecordEntries()) {
            entry.setSortingPosition(++nSortPosition);
        }
        
        return record;
    }
    
    /**
     * Creates the record entry
     * 
     * @param session
     * @param bookkeepingYear
     * @param bookkeepingNum
     * @param deposit Deposit line
     * @return
     * @throws Exception 
     */
    private erp.mfin.data.SDataRecordEntry createRecordEntryAdvances(final SGuiSession session, final int bookkeepingYear, final int bookkeepingNum, SAnalystDepositRow deposit) throws Exception {
        int[] keySystemMoveType = null;
        int[] keySystemMoveTypeXXX = null;
        SDataAccountCash accountCash = null;
        SDataRecordEntry entry = new SDataRecordEntry();
        
        accountCash = new SDataAccountCash();
        if (accountCash.read(new int[] { mnFkBankCompanyBranchId, mnFkBankAccountCashId }, session.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
        }

        if (accountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
            keySystemMoveTypeXXX = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH;
        }
        else {
            keySystemMoveTypeXXX = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK;
        }

        entry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_IN_CUS_ADV[0]);
        entry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_IN_CUS_ADV[1]);
        entry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_IN_CUS_ADV[2]);
        entry.setFkUserNewId(session.getUser().getPkUserId());
        entry.setDbmsAccountingMoveSubclass(session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_CASH_IN_CUS_ADV, SDbRegistry.FIELD_NAME) + "");
        entry.setConcept(deposit.getConcept());
        entry.setDebit(0);
        entry.setCredit(SLibUtils.round(deposit.getAmountLocal(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits()));
        entry.setExchangeRate(deposit.getExchangeRate());
        entry.setExchangeRateSystem(1);
        entry.setDebitCy(0);
        entry.setCreditCy(deposit.getAmountOrigCurrency());
        keySystemMoveType = SModSysConsts.FINS_TP_SYS_MOV_MO_CUS_ADV;

        entry.setFkCurrencyId(deposit.getCurrencyId());
        entry.setFkAccountIdXXX(deposit.getBizPartnerAccountId());
        entry.setFkCostCenterIdXXX_n("");
        entry.setIsExchangeDifference(false);
        entry.setIsSystem(true);
        entry.setIsDeleted(!deposit.getImported());

        entry.setFkSystemMoveClassId(keySystemMoveType[0]);
        entry.setFkSystemMoveTypeId(keySystemMoveType[1]);

        if (accountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
            entry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[0]);
            entry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[1]);
        }
        else {
            entry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[0]);
            entry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[1]);
        }

        entry.setFkSystemMoveCategoryIdXXX(keySystemMoveTypeXXX[0]);
        entry.setFkSystemMoveTypeIdXXX(keySystemMoveTypeXXX[1]);

        entry.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { accountCash.getFkAccountId() }));
        entry.setDbmsAccountComplement(accountCash.getDbmsCompanyBranchEntity().getEntity());
        entry.setDbmsCostCenter_n("");
        entry.setDbmsCurrencyKey(session.getSessionCustom().getCurrencyCode(new int[] { deposit.getCurrencyId() }));

        entry.setReference("");
        entry.setIsReferenceTax(false);
        entry.setFkTaxBasicId_n(0);
        entry.setFkTaxId_n(0);

        entry.setFkBizPartnerId_nr(deposit.getBizPartnerId());
        entry.setFkBizPartnerBranchId_n(0);

        entry.setFkCompanyBranchId_n(accountCash.getPkCompanyBranchId());
        entry.setFkEntityId_n(0);
        entry.setUnits(0d);
        entry.setFkItemId_n(0);
        entry.setFkItemAuxId_n(0);
        entry.setFkYearId_n(0);
        entry.setFkDpsYearId_n(0);
        entry.setFkDpsDocId_n(0);
        entry.setFkDpsAdjustmentYearId_n(0);
        entry.setFkDpsAdjustmentDocId_n(0);
        entry.setFkBookkeepingYearId_n(bookkeepingYear);
        entry.setFkBookkeepingNumberId_n(bookkeepingNum);

        return entry;
    }
    
     /**
     * Creates the record entry
     * 
     * @param session
     * @param bookkeepingYear
     * @param bookkeepingNum
     * @param deposit Deposit line
     * @return
     * @throws Exception 
     */
    private erp.mfin.data.SDataRecordEntry createRecordEntryAccountCash(final SGuiSession session, final int bookkeepingYear, final int bookkeepingNum, SAnalystDepositRow deposit) throws Exception {
        int[] keySystemMoveType = null;
        int[] keySystemMoveTypeXXX = null;
        SDataAccountCash accountCash = null;
        SDataRecordEntry entry = new SDataRecordEntry();
        
        accountCash = new SDataAccountCash();
        if (accountCash.read(new int[] { mnFkBankCompanyBranchId, mnFkBankAccountCashId }, session.getStatement()) != SLibConstants.DB_ACTION_READ_OK) {
            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
        }

        if (accountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
            keySystemMoveTypeXXX = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH;
        }
        else {
            keySystemMoveTypeXXX = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK;
        }

        entry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
        entry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
        entry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
        entry.setFkUserNewId(session.getUser().getPkUserId());
        entry.setDbmsAccountingMoveSubclass(session.readField(SModConsts.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL, SDbRegistry.FIELD_NAME) + "");
        entry.setConcept(deposit.getConcept());

        entry.setDebit(SLibUtils.round(deposit.getAmountLocal(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits()));
        entry.setCredit(0);
        entry.setExchangeRate(deposit.getExchangeRate());
        entry.setExchangeRateSystem(deposit.getExchangeRate());
        entry.setDebitCy(SLibUtils.round(deposit.getAmountOrigCurrency(), SLibUtils.getDecimalFormatAmount().getMaximumFractionDigits()));
        entry.setCreditCy(0);
        keySystemMoveType = SModSysConsts.FINS_TP_SYS_MOV_MO_CUS_ADV;

        entry.setFkCurrencyId(accountCash.getFkCurrencyId());
        entry.setFkAccountIdXXX(accountCash.getFkAccountId());
        entry.setFkCostCenterIdXXX_n("");
        entry.setIsExchangeDifference(false);
        entry.setIsSystem(true);
        entry.setIsDeleted(false);

        entry.setFkSystemMoveClassId(keySystemMoveType[0]);
        entry.setFkSystemMoveTypeId(keySystemMoveType[1]);

        if (accountCash.getFkAccountCashCategoryId() == SDataConstantsSys.FINS_CT_ACC_CASH_CASH) {
            entry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[0]);
            entry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH[1]);
        }
        else {
            entry.setFkSystemAccountClassId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[0]);
            entry.setFkSystemAccountTypeId(SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK[1]);
        }

        entry.setFkSystemMoveCategoryIdXXX(keySystemMoveTypeXXX[0]);
        entry.setFkSystemMoveTypeIdXXX(keySystemMoveTypeXXX[1]);

        entry.setDbmsAccount(SDataReadDescriptions.getCatalogueDescription((SClientInterface) session.getClient(), SDataConstants.FIN_ACC, new Object[] { accountCash.getFkAccountId() }));
        entry.setDbmsAccountComplement(accountCash.getDbmsCompanyBranchEntity().getEntity());
        entry.setDbmsCostCenter_n("");
        entry.setDbmsCurrencyKey(session.getSessionCustom().getCurrencyCode(new int[] { accountCash.getFkCurrencyId() }));

        entry.setReference("");
        entry.setIsReferenceTax(false);
        entry.setFkTaxBasicId_n(0);
        entry.setFkTaxId_n(0);

        entry.setFkBizPartnerId_nr(deposit.getBizPartner().getPkBizPartnerId());
        entry.setFkBizPartnerBranchId_n(deposit.getBizPartner().getDbmsBizPartnerBranchHq().getPkBizPartnerBranchId());

        entry.setFkCompanyBranchId_n(accountCash.getPkCompanyBranchId());
        entry.setFkEntityId_n(accountCash.getPkAccountCashId());
        entry.setUnits(0d);
        entry.setFkItemId_n(0);
        entry.setFkItemAuxId_n(0);
        entry.setFkYearId_n(0);
        entry.setFkDpsYearId_n(0);
        entry.setFkDpsDocId_n(0);
        entry.setFkDpsAdjustmentYearId_n(0);
        entry.setFkDpsAdjustmentDocId_n(0);
        entry.setFkBookkeepingYearId_n(bookkeepingYear);
        entry.setFkBookkeepingNumberId_n(bookkeepingNum);

        return entry;
    }
    
    private void readChildren(SGuiSession session) throws SQLException, Exception {
        String sql;
        ResultSet resultSet = null;
        maAnalystImportations = new HashMap<>();

        sql = "SELECT id_usr_ana "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_LAY_BANK_DEP_ANA) + " " 
                + getSqlWhere()
                + "ORDER BY id_usr_ana; ";
        
        resultSet = session.getStatement().getConnection().createStatement().executeQuery(sql);
        
        while (resultSet.next()) {
            SDbBankLayoutDepositsAnalyst analystImportation = new SDbBankLayoutDepositsAnalyst();

            analystImportation.read(session, new int[] { mnPkBankLayoutDepositsId, resultSet.getInt("id_usr_ana") } );
            maAnalystImportations.put(analystImportation.getPkUserAnalystId(), analystImportation);
        }
    }
    
    private void saveChildren(SGuiSession session) throws Exception {
        for (int index : maAnalystImportations.keySet()) {
            if (mbRegistryNew) {
                maAnalystImportations.get(index).setPkBankLayoutDepositsId(mnPkBankLayoutDepositsId);
            }
            maAnalystImportations.get(index).save(session);
        }
    }
    
/**
     * Puts the values of the import form in the xml file
     * 
     * @param rows Lines of the import form
     * @param accountId Id of bank account
     * @param account Receiving bank account
     * @param currencyId Currency id of the file and the bank account
     * @param currencyCode Currency code of the file and the bank account
     * @return The resulting xml object
     */
    public SXmlImportFile populateXmlImportFile(ArrayList <SAnalystDepositRow> rows, int accountId, String account, int currencyId, String currencyCode) {
        SXmlImportFile xmlFile = new SXmlImportFile();
        SXmlImportFilePayment xmlFilePayment = null;

        xmlFile.getAttribute(SXmlImportFile.ATT_ACC).setValue(account);
        xmlFile.getAttribute(SXmlImportFile.ATT_ACC_ID).setValue(accountId);
        xmlFile.getAttribute(SXmlImportFile.ATT_CUR_CODE).setValue(currencyCode);
        xmlFile.getAttribute(SXmlImportFile.ATT_CUR_ID).setValue(currencyId);
        
        for (SAnalystDepositRow row : rows) {
            xmlFilePayment = new SXmlImportFilePayment();
            
            xmlFilePayment.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_ID).setValue(row.getPkDepositId());
            xmlFilePayment.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_TIME_STAMP).setValue(SLibUtils.DbmsDateFormatDatetime.format(row.getDateDeposit()));
            xmlFilePayment.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_CUSTOMER_ID).setValue(row.getBizPartnerId());
            xmlFilePayment.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_ANALYST_ID).setValue(row.getPkAnalystId());
            xmlFilePayment.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_REFERENCE).setValue(row.getReference());
            xmlFilePayment.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_CONCEPT).setValue(row.getConcept());
            xmlFilePayment.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_NUMBER_TX).setValue(row.getNumberTx());
            xmlFilePayment.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_TYPE).setValue(row.getPaymentType());
            xmlFilePayment.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_AMOUNT).setValue(row.getAmountOrigCurrency());
            
            xmlFile.getXmlElements().add(xmlFilePayment);
        }
        
        return xmlFile;
    }
    
    public void updateData() {
        moXmlObject = new SXmlImportFile();
        msDepositsXml = moXmlObject.getXmlString();
        mnSourceMovements = maXmlRows.size();
        mdSourceAmount = 0;
        
        for (SXmlImportFilePayment ifp : maXmlRows) {
            mdSourceAmount += (double) ifp.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_AMOUNT).getValue();
        }
    }
    
    public SDbBankLayoutDeposits() {
        super(SModConsts.FIN_LAY_BANK_DEP);
        
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBankLayoutDepositsId = pk[0];
    }
    
    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBankLayoutDepositsId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();
        
        mnPkBankLayoutDepositsId = 0;
        msSourceName = "";
        moSourceFile_n = null;
        mnSourceMovements = 0;
        mdSourceAmount = 0;
        mtDepositsDate = null;
        msDepositsXml = null;
        mbDeleted = false;
        mnFkBankLayoutDepositsTypeId = 0;
        mnFkBankCompanyBranchId = 0;
        mnFkBankAccountCashId = 0;
        mnFkCurrencyId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maAnalystImportations = new HashMap<>();
        
        moXmlObject = null;
        maXmlRows = new ArrayList<>();
        maDepositsRows = new ArrayList<>();
    }

    @Override
    public String getSqlTable() {
        return SModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_lay_bank_dep = " + mnPkBankLayoutDepositsId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_lay_bank_dep = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(SGuiSession session) throws SQLException, Exception {
        String sql = "";
        ResultSet resultSet = null;

        mnPkBankLayoutDepositsId = 0;

        sql = "SELECT COALESCE(MAX(id_lay_bank_dep), 0) + 1 FROM " + SModConsts.TablesMap.get(SModConsts.FIN_LAY_BANK_DEP) + " " + " ";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            mnPkBankLayoutDepositsId = resultSet.getInt(1);
        }
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
            mnPkBankLayoutDepositsId = resultSet.getInt("id_lay_bank_dep");
            msSourceName = resultSet.getString("src_name");
            moSourceFile_n = resultSet.getBlob("src_file_n");
            mnSourceMovements = resultSet.getInt("src_movs");
            mdSourceAmount = resultSet.getDouble("src_amt");
            mtDepositsDate = resultSet.getDate("dep_dt");
            msDepositsXml = resultSet.getString("dep_xml");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkBankLayoutDepositsTypeId = resultSet.getInt("fk_tp_lay_bank_dep");
            mnFkBankCompanyBranchId = resultSet.getInt("fk_bank_cob");
            mnFkBankAccountCashId = resultSet.getInt("fk_bank_acc_cash");
            mnFkCurrencyId = resultSet.getInt("fk_cur");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            readChildren(session);
            
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
        
        computeRecord(session);
        moXmlObject = populateXmlImportFile(maDepositsRows, mnFkBankAccountCashId, "", mnFkCurrencyId, "");
        msDepositsXml = moXmlObject.getXmlString();

        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "INSERT INTO " + SModConsts.TablesMap.get(SModConsts.FIN_LAY_BANK_DEP) + " VALUES (" +
                    mnPkBankLayoutDepositsId + ", " + 
                    "'" + msSourceName + "', " + 
                    moSourceFile_n + ", " + 
                    mnSourceMovements + ", " + 
                    mdSourceAmount + ", " + 
                    "'" + SLibUtils.DbmsDateFormatDate.format(mtDepositsDate) + "', " +
                    "'" + msDepositsXml + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkBankLayoutDepositsTypeId + ", " + 
                    mnFkBankCompanyBranchId + ", " + 
                    mnFkBankAccountCashId + ", " + 
                    mnFkCurrencyId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + 
                    ")";
        }
        else {            
            
            msSql = "UPDATE " + SModConsts.TablesMap.get(SModConsts.FIN_LAY_BANK_DEP) + " SET " +
                    "id_lay_bank_dep = " + mnPkBankLayoutDepositsId + ", " +
                    "src_name = '" + msSourceName + "', " +
                    //"src_file_n = " + moSourceFile_n + ", " + //This field is not updatable
                    "src_movs = " + mnSourceMovements + ", " +
                    "src_amt = " + mdSourceAmount + ", " +
                    "dep_dt = '" + SLibUtils.DbmsDateFormatDate.format(mtDepositsDate) + "', " +
                    "dep_xml = '" + msDepositsXml + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tp_lay_bank_dep = " + mnFkBankLayoutDepositsTypeId + ", " +
                    "fk_bank_cob = " + mnFkBankCompanyBranchId + ", " +
                    "fk_bank_acc_cash = " + mnFkBankAccountCashId + ", " +
                    "fk_cur = " + mnFkCurrencyId + ", " +
                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        if (mbRegistryNew) {
            saveFileField(session);
        }
        
        if (maAnalystImportations != null && maAnalystImportations.size() > 0) {
            saveChildren(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = SDbConsts.SAVE_OK;
    }
    
    private void saveFileField(SGuiSession session) throws SQLException {
        int index = 1;
        String sql = "UPDATE fin_lay_bank_dep SET src_file_n = ? " + getSqlWhere();
        
        PreparedStatement preparedStatement = session.getStatement().getConnection().prepareStatement(sql);
        
        if (maBytes != null) {
            preparedStatement.setBytes(index, maBytes);
        }
        else {
            preparedStatement.setNull(index++, java.sql.Types.BLOB);
        }
        
        preparedStatement.execute();
        
    }

    @Override
    public SDbBankLayoutDeposits clone() throws CloneNotSupportedException {
        SDbBankLayoutDeposits registry = new SDbBankLayoutDeposits();

        registry.setPkBankLayoutDepositsId(this.getPkBankLayoutDepositsId());
        registry.setSourceName(this.getSourceName());
        registry.setSourceFile_n(this.getSourceFile_n());
        registry.setSourceMovements(this.getSourceMovements());
        registry.setSourceAmount(this.getSourceAmount());
        registry.setDepositsDate(this.getDepositsDate());
        registry.setDepositsXml(this.getDepositsXml());
        registry.setDeleted(this.isDeleted());
        registry.setFkBankLayoutDepositsTypeId(this.getFkBankLayoutDepositsTypeId());
        registry.setFkBankCompanyBranchId(this.getFkBankCompanyBranchId());
        registry.setFkBankAccountCashId(this.getFkBankAccountCashId());
        registry.setFkCurrencyId(this.getFkCurrencyId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        registry.getAnalystImportations().putAll(this.getAnalystImportations());
        
        return registry;
    }

    @Override
    public void saveField(Statement statement, int[] pk, int field, Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = SDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";
        switch (field) {
            default:
                //throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        
        statement.execute(msSql);
        
        mnQueryResultId = SDbConsts.SAVE_OK;
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
