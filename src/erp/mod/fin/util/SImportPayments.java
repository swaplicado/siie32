/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.util;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerCategory;
import erp.mfin.data.SDataRecord;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SBpsUtils;
import erp.mod.fin.db.SAnalystDepositRow;
import erp.mod.fin.db.SDbBankLayoutDeposits;
import erp.mod.fin.db.SDbBankLayoutDepositsAnalyst;
import erp.mod.fin.db.SXmlAnalystImportation;
import erp.mod.fin.db.SXmlAnalystImportationPayment;
import erp.mod.fin.db.SXmlImportFile;
import erp.mod.fin.db.SXmlImportFilePayment;
import erp.musr.data.SDataUser;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridRow;
import sa.lib.gui.SGuiClient;
import sa.lib.xml.SXmlElement;

/**
 * Processing and utilities of the importation of payments bancomer CIE
 * 
 * @author Edwin Carmona
 */
public class SImportPayments {
    
    private final static int LAY_RIC_ACCOUNT = 0;
    private final static int LAY_RIC_SERVICE = 1;
    private final static int LAY_RIC_CONTRACT = 2;
    private final static int LAY_RIC_DATE = 3;
    private final static int LAY_RIC_TIME = 4;
    private final static int LAY_RIC_CURRENCY = 5;
    private final static int LAY_RIC_REF1_REFERENCE = 6;
    private final static int LAY_RIC_REF2_CONCEPT = 7;
    private final static int LAY_RIC_NUMBERTX = 8;
    private final static int LAY_RIC_AMOUNT = 9;
    private final static int LAY_RIC_AMOUNT_MOV_NET = 10;
    private final static int LAY_RIC_BRANCH_OFFI = 11;
    private final static int LAY_RIC_BRANCH_BANK = 12;
    private final static int LAY_RIC_PAYMENT_TYPE = 13;
    private final static int LAY_RIC_CHANNEL = 14;
    private final static int LAY_RIC_OPERATION_CODE = 15;
    private final static int LAY_RIC_COMMERCIAL_NAME = 16;
    private final static int LAY_RIC_DISCOUNT_RATE = 17;
    private final static int LAY_RIC_PROMOTION_RATE = 18;
    private final static int LAY_RIC_CHECK_NUMBER = 19;
    private final static int LAY_RIC_TPV_COMISSION = 20;
    private final static int LAY_RIC_IVA_TPV = 21;
    private final static int LAY_RIC_ORIGIN_SOURCE = 22;
    
    private final static String LAY_RIC_CUR_MX_CODE = "MXP";
    private final static String SYS_CUR_MX_CODE = "MXN";
    
    SGuiClient miClient;
    
    private String msAccount;
    private String msCurrency;
    private double mdAmountFile;
    private int mnPaymentsFile;
    private byte[] maBytes;

    public void setAccount(String s) { msAccount = s; }
    public void setCurrency(String s) { msCurrency = s; }
    public void setAmountFile(double d) { mdAmountFile = d; }
    public void setPaymentsFile(int n) { mnPaymentsFile = n; }
    public void setBytes(byte[] o) { maBytes = o; }
   
    public String getAccount() { return msAccount; }
    public String getCurrency() { return msCurrency; }
    public double getAmountFile() { return mdAmountFile; }
    public int getPaymentsFile() { return mnPaymentsFile; }
    public byte[] getBytes() { return maBytes; }
    
    public SImportPayments(SGuiClient client) {
        miClient = client;
    }
    
    /**
     * This method reads the file, empty the lines of this into a list of strings 
     * that in turn is processed for the data to be extracted
     * 
     * @param filePath Path of the file to be processed that was selected in the import form
     * @return An ArrayList of the rows of data that will be displayed in the form of import
     */
    public ArrayList<SAnalystDepositRow> processFile(String filePath) {
        ArrayList<String> lines = null;
        ArrayList<String[]> dataLines = null;
        ArrayList<SAnalystDepositRow> deposits = null;
        
        try {
            maBytes = SZip.compressFileToZip(new File[] { new File(filePath) });
            lines = SFinUtils.readImportFile(filePath);
            dataLines = explodeLines(lines);
            deposits = loadDeposits(dataLines);
        }
        catch (Exception ex) {
            SLibUtils.showException(this, ex);
        }
        
        return deposits;
    }
    
    /**
     * Returns a list of string arrays with the data identified in each line, 
     * the files in the file are separated by the character ¦
     * 
     * @param lines Lines contained in the file that were identified.
     * @return list of string arrays with the data identified in each line
     */
    private ArrayList<String[]> explodeLines(ArrayList<String> lines) {
        ArrayList<String[]> dataLines = new ArrayList<>();
        
        for (String fline : lines) {
            /**
             * The separator character changes when read by the ANSI encoding of the file
             */
            dataLines.add(fline.split("�")); //XXX caracter of separation
        }
        
        return dataLines;
    }
    
    /**
     * Processes the data contained in the file and transforms them into SAnalystDepositRow lines 
     * 
     * @param dataLines List of arrays with deposit data
     * @return Lines that will be shown in the form
     */
    private ArrayList<SAnalystDepositRow> loadDeposits(ArrayList<String[]> dataLines) {
        SDataBizPartner bizPartner = null;
        SDataBizPartnerCategory bizPartnerCategory = null;
        SDataUser user = null;
        SAnalystDepositRow row = null;
        ArrayList<SAnalystDepositRow> deposits = new ArrayList<>();
        int bizPartnerId = SLibConstants.UNDEFINED;
        int index = SLibConstants.UNDEFINED;
        
        if (dataLines.size() > 0) {
            if (dataLines.get(0).length == 1) { // If there is only one line indicates that the separator character could not be found
                miClient.showMsgBoxWarning("El archivo seleccionado no es válido");
                return null;
            }
            switch(dataLines.get(0)[LAY_RIC_CURRENCY]) {
                case LAY_RIC_CUR_MX_CODE : 
                    msCurrency = SYS_CUR_MX_CODE;
                    break;
                default:
                    msCurrency = dataLines.get(0)[LAY_RIC_CURRENCY];
                    break;
            }

            msAccount = dataLines.get(0)[LAY_RIC_ACCOUNT];
        }
        
        mdAmountFile = 0;
        index = 1;
        for (String[] str : dataLines) {
            row = new SAnalystDepositRow(miClient);
            
            row.setPkDepositId(index);
            row.setDateDeposit(SFinUtils.stringToDateTime(str[LAY_RIC_DATE].trim(), str[LAY_RIC_TIME].trim()));
            
            bizPartnerId = SBpsUtils.getBizParterIdByFiscalId(miClient.getSession(), str[LAY_RIC_REF1_REFERENCE].trim().substring(0, str[LAY_RIC_REF1_REFERENCE].trim().length()-1), SModSysConsts.BPSS_CT_BP_CUS);
            
            if (bizPartnerId != SLibConstants.UNDEFINED) {
                bizPartner = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BP, new int[] { bizPartnerId }, SLibConstants.EXEC_MODE_VERBOSE);
                
                if (bizPartner != null) {
                    row.setBizPartnerId(bizPartnerId);
                    row.setBizPartner(bizPartner);

                    bizPartnerCategory = bizPartner.getDbmsCategorySettingsCus();
                    if (bizPartnerCategory != null) {
                        row.setPkAnalystId(bizPartnerCategory.getFkUserAnalystId_n());
                        if (bizPartnerCategory.getFkUserAnalystId_n() != SLibConstants.UNDEFINED) {
                            user = (SDataUser) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.USRU_USR, new int[] { bizPartnerCategory.getFkUserAnalystId_n() }, SLibConstants.EXEC_MODE_SILENT);
                            row.setUserAnalyst(user.getName());
                        }
                        else {
                            row.setUserAnalyst("N/I");
                        }
                    }
                }
            }
            else {
                row.setBizPartnerId(SLibConstants.UNDEFINED);
                row.setBizPartner(null);
                row.setPkAnalystId(SLibConstants.UNDEFINED);
                row.setUserAnalyst("N/I");
            }
            
            row.setReference(str[LAY_RIC_REF1_REFERENCE].trim());
            row.setConcept(str[LAY_RIC_REF2_CONCEPT].trim());
            row.setNumberTx(str[LAY_RIC_NUMBERTX].trim());
            row.setPaymentType(str[LAY_RIC_PAYMENT_TYPE].trim());
            row.setAmountOrigCurrency(SLibUtils.parseDouble(str[LAY_RIC_AMOUNT].trim()));
            row.setCurrency(str[LAY_RIC_CURRENCY].trim());
            row.setImported(false);
            row.setReferenceAdv("");
            if (msCurrency.trim().equals(miClient.getSession().getSessionCustom().getLocalCurrencyCode().trim())) {
                row.setExchangeRate(1.0);
                row.setCurrencyId(miClient.getSession().getSessionCustom().getLocalCurrencyKey()[0]);
            }
            else {
                row.setExchangeRate(0.0);
                row.setCurrencyId(SLibConstants.UNDEFINED);
            }
            row.setAmountLocal(row.getAmountOrigCurrency() * row.getExchangeRate());
            row.setRecord(null);
            
            mdAmountFile += row.getAmountOrigCurrency();
            deposits.add(row);
            index++;
        }
        
        mnPaymentsFile = deposits.size();
        
        return deposits;
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
    public SXmlImportFile populateXmlImportFile(Vector <SGridRow> rows, int accountId, String account, int currencyId, String currencyCode) {
        SXmlImportFile xmlFile = new SXmlImportFile();
        SXmlImportFilePayment xmlFilePayment = null;
        SAnalystDepositRow row = null;

        xmlFile.getAttribute(SXmlImportFile.ATT_ACC).setValue(account);
        xmlFile.getAttribute(SXmlImportFile.ATT_ACC_ID).setValue(accountId);
        xmlFile.getAttribute(SXmlImportFile.ATT_CUR_CODE).setValue(currencyCode);
        xmlFile.getAttribute(SXmlImportFile.ATT_CUR_ID).setValue(currencyId);
        
        for (SGridRow gridRow : rows) {
            xmlFilePayment = new SXmlImportFilePayment();
            row = (SAnalystDepositRow) gridRow;
            
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
    
    /**
     * Puts the values of the import form in the xml file
     * 
     * @param rows Lines of the import form
     * @param analystId
     * @return The resulting xml object
     */
    public SXmlAnalystImportation populateAnalystImportation(Vector <SGridRow> rows, int analystId) {
        SXmlAnalystImportation xmlImport = new SXmlAnalystImportation();
        SXmlAnalystImportationPayment xmlImportPayment = null;
        SAnalystDepositRow row = null;
        
        xmlImport.getAttribute(SXmlAnalystImportation.ATT_ANALYST_ID).setValue(analystId);
        
        for (SGridRow gridRow : rows) {
            row = (SAnalystDepositRow) gridRow;
            
            if(row.getPkAnalystId() == miClient.getSession().getUser().getPkUserId()) {
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

                xmlImport.getXmlElements().add(xmlImportPayment);
            }
        }
        
        return xmlImport;
    }
    
    /**
     * Obtains the xml from the DB register, reads it and forms the corresponding SAnalystDepositRow rows
     * 
     * @param bankLayoutDeposits DataBase import file registry
     * @param currencyId Id of import currency
     * @param date Import date
     * @return ArrayList of rows that will be shown in the form
     * @throws Exception 
     */
    public ArrayList<SAnalystDepositRow> populateGridRows(SDbBankLayoutDeposits bankLayoutDeposits, final int currencyId, final Date date) throws Exception {
        ArrayList<SAnalystDepositRow> rowsToReturn = new ArrayList<>();
        SAnalystDepositRow row = null;
        SDbBankLayoutDepositsAnalyst analystImportation = null;
        SXmlImportFile xmlFile = new SXmlImportFile();
        SXmlImportFilePayment xmlRow = null;
        SXmlAnalystImportation xmlImportation = null;
        SXmlAnalystImportationPayment xmlImportationPayment = null;
        SDataBizPartner bizPartner = null;
        SDataUser user = null;
        Object[] recordKey = null;
        int idPayment = SLibConstants.UNDEFINED;
        
        xmlFile.processXml(bankLayoutDeposits.getDepositsXml());
        
        for (SXmlElement element : xmlFile.getXmlElements()) {
            if (element instanceof SXmlImportFilePayment) {
                xmlRow = (SXmlImportFilePayment) element;
                
                row = new SAnalystDepositRow(miClient);
                
                row.setPkDepositId((int) xmlRow.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_ID).getValue());
                row.setDateDeposit((Date) xmlRow.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_TIME_STAMP).getValue());
                row.setBizPartnerId((int) xmlRow.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_CUSTOMER_ID).getValue());
                row.setPkAnalystId((int) xmlRow.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_ANALYST_ID).getValue());
                
                if (row.getBizPartnerId() != SLibConstants.UNDEFINED) {
                    bizPartner = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BP, new int[] { row.getBizPartnerId() }, SLibConstants.EXEC_MODE_VERBOSE);

                    if (bizPartner != null) {
                        row.setBizPartner(bizPartner);
                    }
                    else {
                        row.setBizPartner(null);
                    }
                }
               
                if (row.getPkAnalystId() != SLibConstants.UNDEFINED) {
                    user = (SDataUser) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.USRU_USR, new int[] { row.getPkAnalystId() }, SLibConstants.EXEC_MODE_SILENT);
                    if (user != null) {
                        row.setUserAnalyst(user.getName());
                    }
                    else {
                        row.setUserAnalyst("N/I");
                    }
                }
                else {
                    row.setUserAnalyst("N/I");
                }
              
                row.setReference((String) xmlRow.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_REFERENCE).getValue());
                row.setConcept((String) xmlRow.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_CONCEPT).getValue());
                row.setNumberTx((String) xmlRow.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_NUMBER_TX).getValue());
                row.setPaymentType((String) xmlRow.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_TYPE).getValue());
                row.setAmountOrigCurrency((double) xmlRow.getAttribute(SXmlImportFilePayment.ATT_PAYMENT_AMOUNT).getValue());
                row.setCurrency((String) xmlFile.getAttribute(SXmlImportFile.ATT_CUR_CODE).getValue());
                row.setCurrencyId(SLibUtils.parseInt((String)xmlFile.getAttribute(SXmlImportFile.ATT_CUR_ID).getValue()));
 
                if (currencyId == row.getCurrencyId()) {
                    row.setExchangeRate(1.0);
                }
                
                analystImportation = bankLayoutDeposits.getAnalystImportations().get(row.getPkAnalystId());
                
                if (analystImportation != null) {
                    xmlImportation = new SXmlAnalystImportation();
                    xmlImportation.processXml(analystImportation.getUserAnalystXml());

                    for (SXmlElement elemImport : xmlImportation.getXmlElements()) {
                        if (elemImport instanceof SXmlAnalystImportationPayment) {
                            xmlImportationPayment = (SXmlAnalystImportationPayment) elemImport;

                            idPayment = (int) xmlImportationPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAYMENT_ID).getValue();
                            if (row.getPkDepositId() == idPayment) {
                                row.setImported((boolean) xmlImportationPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAYMENT_IMPORTED).getValue());
                                row.setReferenceAdv((String) xmlImportationPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_ACC_REF).getValue());                                
                                row.setExchangeRate((double) xmlImportationPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_EXCH_RATE).getValue());
                                row.setAmountLocal((double) xmlImportationPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_AMOUNT).getValue());

                                recordKey = new Object[5];
                                recordKey[0] = (int) xmlImportationPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_YEAR_ID).getValue();
                                recordKey[1] = (int) xmlImportationPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_PER_ID).getValue();
                                recordKey[2] = (int) xmlImportationPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_BKC_ID).getValue();
                                recordKey[3] = (String) xmlImportationPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_TP_ID).getValue();
                                recordKey[4] = (int) xmlImportationPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_REC_NUM_ID).getValue();

                                if ((Integer)recordKey[0] != SLibConstants.UNDEFINED) {
                                    row.setRecord((SDataRecord) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.FIN_REC, recordKey, SLibConstants.EXEC_MODE_VERBOSE));
                                }
                                else {
                                    row.setRecord(null);
                                }
                                break;
                            }
                        }
                    }
                }
            }
            rowsToReturn.add(row);
        }
        
        return rowsToReturn;
    }
    
    /**
     * Read the account of customers
     * 
     * @param rows lines in the form of import
     */
    public void populateAccounts(ArrayList<SAnalystDepositRow> rows) {
        Vector<Object> mvParams = null;
        
        for (SAnalystDepositRow row : rows) {
            if (row.getBizPartner() != null && row.getRecord() != null) {
                mvParams = new Vector<Object>();

                mvParams.add(row.getBizPartner().getPkBizPartnerId());
                mvParams.add(row.getBizPartner().getDbmsCategorySettingsCus().getPkBizPartnerCategoryId());
                mvParams.add(row.getRecord().getPkBookkeepingCenterId());
                mvParams.add(SDataConstantsSys.FINS_TP_ACC_BP_PAY);
                mvParams.add(row.getRecord().getDate());
                mvParams = SDataUtilities.callProcedure((SClientInterface) miClient, SProcConstants.FIN_ACC_BP_GET, mvParams, SLibConstants.EXEC_MODE_SILENT);

                if (mvParams.size() > 0) {
                    if (SLibUtilities.parseInt(mvParams.get(1).toString()) > 0) {
                        System.out.println(mvParams.get(2).toString());
                    }
                    row.setBizPartnerAccountId(mvParams.get(0).toString());
                }
            }
        }
    }
}
