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
import erp.mod.SModConsts;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import sa.lib.SLibConsts;

import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;
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
     * Gets two strings, date and time, returning a formed date object
     * 
     * @param date String of date format: yyyy-mm-dd
     * @param hour String of format: HH.mm.ss
     * @return Date with de values
     * @throws Exception 
     */
    public static Date stringToDateTime(String date, String hour) {
        String[] times = null;
        
        Calendar cal = Calendar.getInstance();
        int day = Integer.parseInt(date.substring(8));
        int month = Integer.parseInt(date.substring(5,7));
        int year = Integer.parseInt(date.substring(0, 4));
        
        if (hour != null && !hour.isEmpty() && hour.contains(".")) {
            cal.set(year, month, day, SLibUtils.parseInt(hour.substring(0,2)), SLibUtils.parseInt(hour.substring(3,5)), SLibUtils.parseInt(hour.substring(6)));
        }
        else {
            cal.set(year, month, day, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED, SLibConsts.UNDEFINED);
        }

        return cal.getTime();
    }
    
    public static Date stringToDateTime(String date) {
        String sDate = date.substring(0, date.indexOf(" "));
        String sHour = date.substring(date.indexOf(" "));
        
        return stringToDateTime(sDate, sHour.replace(":", "."));
    }
    
    /**
     * Reads a file in the specified path and returns an File object
     * 
     * @param filePath
     * @return File object
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static ArrayList<String> readImportFile(String filePath) throws FileNotFoundException, IOException {
        BufferedReader bufferedReader = null;
        ArrayList<String> lines = new ArrayList<>();
        String fileLine = "";
        
        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf-8"));  
        
        while((fileLine = bufferedReader.readLine()) != null) {
            lines.add(fileLine);
        }
        
        bufferedReader.close();
        
        return lines;
    }
    
    /**
     * Return the key of the corresponding account
     * 
     * @param session
     * @param bankAccount bank account, field acc_num of bank_acc table
     * @return key of account with lenght 2.
     */
    public static int[] getAccCashKeyByAccount(final SGuiSession session, final String bankAccount) {
        String sql = "";
        ResultSet resultSet = null;
        int key[] = null;

        try {
            sql = "SELECT id_cob, id_acc_cash " +
            "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_ACC_CASH) + " AS ac " +
            "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BANK_ACC) + " AS ba ON ac.fid_bpb_n = ba.id_bpb AND ac.fid_bank_acc_n = ba.id_bank_acc " +
            "WHERE ba.acc_num = '" + bankAccount + "';";
            
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                key = new int[2];
                key[0] = resultSet.getInt("id_cob");
                key[1] = resultSet.getInt("id_acc_cash");
            }
        }
        catch (Exception e) {
            SLibUtils.printException(SFinUtils.class.getName(), e);
        }

        return key;
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
            lines = SImportPayments.readImportFile(filePath);
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
            row.setDateDeposit(SImportPayments.stringToDateTime(str[LAY_RIC_DATE].trim(), str[LAY_RIC_TIME].trim()));
            
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
            row.setBkcYear(SLibConstants.UNDEFINED);
            row.setBkcNum(SLibConstants.UNDEFINED);
            
            mdAmountFile += row.getAmountOrigCurrency();
            deposits.add(row);
            index++;
        }
        
        mnPaymentsFile = deposits.size();
        
        return deposits;
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
                                row.setBkcYear((int) xmlImportationPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_BKC_YEAR).getValue());
                                row.setBkcNum((int) xmlImportationPayment.getAttribute(SXmlAnalystImportationPayment.ATT_PAY_ANA_BKC_NUM).getValue());

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
