/*
 * Copyright © Software Aplicado SA de CV. All rights reserverd.
 * SFinUtilities.java
 */
package erp.mfin.data;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SDbAccount;
import erp.mod.fin.db.SDbBankLayout;
import erp.mod.fin.db.SFinUtils;
import erp.mod.fin.db.SLayoutBankPaymentText;
import erp.mod.fin.db.SLayoutBankRow;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.STrnUtilities;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.JFileChooser;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiSession;
import sa.lib.prt.SPrtConsts;
import sa.lib.prt.SPrtUtils;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Daniel López, Claudio Peña, Sergio Flores, Rodrigo Ayala
 */
public abstract class SFinUtilities {
    
    public static final java.lang.String TXT_TYPE_LAY_BANBAJIO_LOCAL = "bco";
    public static final java.lang.String TXT_TYPE_LAY_BANBAJIO_TEF = "tef";
    public static final java.lang.String TXT_TYPE_LAY_BANBAJIO_SPEI = "spi";
    
    public static final java.lang.String TXT_TYPE_ACCOUNT_CHECK = "01";
    public static final java.lang.String TXT_TYPE_ACCOUNT_CLABE = "40";
    
    public static final java.lang.String TXT_TYPE_LAY_BBVA_TEF = "M";
    public static final java.lang.String TXT_TYPE_LAY_BBVA_SPEI = "H";
    
    public static final java.lang.String TXT_TYPE_CUR_MXP_BBVA = "MXP";
    public static final java.lang.String TXT_TYPE_CUR_USD_BBVA = "USD";
    public static final java.lang.String TXT_TYPE_CUR_EUR_BBVA = "EUR";
    
    public static final java.lang.String TXT_TYPE_BMX_TRAN_CHECK = "03";
    public static final java.lang.String TXT_TYPE_BMX_TRAN_CLABE = "09";
    
    public static final java.lang.String TXT_TYPE_CUR_MXP_BMX = "001";
    public static final java.lang.String TXT_TYPE_CUR_USD_BMX = "005";
    
    public static final java.lang.String TXT_TERM_BMX_SAME_DAY = "00";
    public static final java.lang.String TXT_TERM_BMX_NEXT_DAY = "24";
    
    private static double mdBalanceTot;
    private static double mdTaxChargedTot;
    private static boolean mbIsRepeated;

    /**
     * Calculate balance total by business partner.
     * @param vRows Vector rows of layout.
     * @param index Index of vector.
     * @param nBizPartnerId Id business partner
     * @return Index new.
     */
    private static int calculateBalanceTotal(Vector<SLayoutBankRow> vRows, int index, int nBizPartnerId, String accountCredit) {
        mbIsRepeated = false;
        SLayoutBankRow values = null;

        for (int j = index + 1; j < vRows.size(); j++) {
            values = vRows.get(j);
            if (values.getBizPartnerId() == nBizPartnerId && values.getBeneficiaryAccountNumber().compareTo(accountCredit) == 0) {
                mdBalanceTot += values.getBalanceTotByBizPartner();
                if (values.getTaxCharged() != 0) {
                    mdTaxChargedTot += (values.getTotalVat() * (values.getBalanceTotByBizPartner() / values.getTotal()));
                }
                else {
                    mdTaxChargedTot += 0;
                }
                index = j + 1;
                mbIsRepeated = true;
            }
            else {
                break;
            }
        }

        if (!mbIsRepeated) {
            index++;
        }

        return index;
    }
    
    /*
     * Bank layouts
     */
    
    public static void writeLayout(SClientInterface client, SDbBankLayout bankLayout, java.lang.String title) throws Exception {
        String fileExt = getFileExtLayout(client.getSession(), bankLayout.getFkBankLayoutTypeId()).toLowerCase();
        String fileName = getFileNameLayout(client.getSession(), bankLayout.getFkBankLayoutTypeId()) + "." + (fileExt.isEmpty() ? "txt" : fileExt);
        BufferedWriter bw;
        
        client.getFileChooser().setSelectedFile(new File(fileName));
        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            fileName = client.getFileChooser().getSelectedFile().getAbsolutePath();
            client.getFileChooser().setSelectedFile(new File(client.getSessionXXX().getFormatters().getFileNameDatetimeFormat().format(new java.util.Date())));
            
            File file = new File(fileName);

            try {
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ASCII"));
                
                bw.write(bankLayout.getLayoutText());
                bw.close();
            }
            catch (java.lang.Exception e) {
                SLibUtilities.renderException(SFinUtilities.class.getName(), e);
            }
        }
    }

    public static String getFileNameLayout(SGuiSession session, int layoutTpId) throws Exception {
        String fileName = "";
        String sql;
        ResultSet resulSet;

        sql = "SELECT file_name " +
               "FROM erp.finu_tp_lay_bank " +
               "WHERE NOT b_del AND id_tp_lay_bank = " + layoutTpId + ";";

        resulSet = session.getStatement().executeQuery(sql);
        if (resulSet.next()) {
            fileName = resulSet.getString("file_name");
        }
        
        return fileName;
    }
    
    public static String getFileExtLayout(SGuiSession session, int layoutTpId) throws Exception {
        String fileExt = "";
        String sql;
        ResultSet resulSet;

        sql = "SELECT file_ext " +
               "FROM erp.finu_tp_lay_bank " +
               "WHERE NOT b_del AND id_tp_lay_bank = " + layoutTpId + ";";

        resulSet = session.getStatement().executeQuery(sql);
        if (resulSet.next()) {
            fileExt = resulSet.getString("file_ext");
        }
        
        return fileExt;
    }
    
    public static String getNameTypePayLayout(SGuiSession session, int layoutId) throws Exception {
        String typePayBank = "";
        String sql = "";
        ResultSet resulSet = null;

        sql = "SELECT tpb.tp_pay_bank " +
                "FROM erp.fins_tp_pay_bank AS tpb " +
                "INNER JOIN erp.finu_tp_lay_bank AS tlb ON tpb.id_tp_pay_bank = tlb.fid_tp_pay_bank " +
                "INNER JOIN fin_lay_bank AS flb ON tlb.id_tp_lay_bank = flb.fk_tp_lay_bank " +
                "WHERE flb.id_lay_bank = " + layoutId + ";";

        resulSet = session.getStatement().executeQuery(sql);
        if (resulSet.next()) {
            typePayBank = resulSet.getString("tpb.tp_pay_bank");
        }
        
        return typePayBank;
    }
    
    public static ArrayList<SGuiItem> getAgreementReferences(final SGuiClient client, final int bizPartnerBranchId, final String agreement) throws Exception {
        ArrayList<SGuiItem> references = new ArrayList<>();
        
        String sql = "SELECT ref "
                + "FROM erp.BPSU_BANK_ACC "
                + "WHERE NOT b_del AND id_bpb = " + bizPartnerBranchId + " AND agree = '" + agreement + "';";
        
        try (Statement statement = client.getSession().getStatement().getConnection().createStatement()) {
            ResultSet result = statement.executeQuery(sql);
            
            while(result.next()) {
                references.add(new SGuiItem(result.getString(1)));
            }
        }
        
        return references;
    }
    
    /*
     * Bank layouts of HSBC
     */
    
    public static java.lang.String createLayoutHsbcThirdOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title) {
        int nMoveNum = 1;
        int n = 0;
        int nBizPartnerId = 0;
        java.lang.String sBizPartner = "";
        java.lang.String sReference = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sConcept = "";
        java.lang.String layout = "";
        SLayoutBankRow values = null;
        DecimalFormat formatDesc = new DecimalFormat("00000000000.00");
        mdBalanceTot = 0;
        mdTaxChargedTot = 0;
        mbIsRepeated = false;

        try {
            for (int i = 0; i < vRows.size();) {
                values = vRows.get(i);

                n = (int) (Math.floor(Math.log10(nMoveNum)) + 1);

                nBizPartnerId = values.getBizPartnerId();
                sBizPartner = SLibUtilities.textToAlphanumeric(values.getBizPartner());
                sReference = SLibUtilities.textToAlphanumeric(values.getReference());
                sAccountDebit = SLibUtilities.textTrim(values.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(values.getBeneficiaryAccountNumber());
                sConcept = SLibUtilities.textToAlphanumeric(values.getConcept());
                mdBalanceTot = values.getBalanceTotByBizPartner();
                /*
                 * XXX No required is optional (jbarajas)
                 * 
                if (values.getTaxCharged() != 0) {
                    mdTaxChargedTot = (values.getTotalVat() * (mdBalanceTot / values.getTotal()));
                }
                else {
                    mdTaxChargedTot = 0;
                }
                */ 

                i = calculateBalanceTotal(vRows, i, nBizPartnerId, sAccountCredit);

                layout += SLibUtilities.textRepeat("0", 6 - n).concat(nMoveNum++ + "");
                layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 10 ? 0 : 10 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 10));
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 10 ? 0 : 10 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 10));
                layout += formatDesc.format(mdBalanceTot).replace(".", "");
                layout += values.getCurrencyId();
                layout += SLibUtilities.textRepeat(" ", (sReference.length() >= 30 ? 0 : 430 - sReference.length())).concat(SLibUtilities.textLeft(sReference, 30));
                layout += (sBizPartner.length() > 40 ? SLibUtilities.textLeft(sBizPartner, 40) : sBizPartner).concat(SLibUtilities.textRepeat(" ", (40 - sBizPartner.length())));
                layout += values.getFiscalVoucher();
                layout += SLibUtilities.textRepeat(" ", 18);
                layout += formatDesc.format(0d).replace(".", "");
                layout += SLibUtilities.textRepeat(" ", 60);

                layout += "\r\n";
            }

            if (vRows.size() > 0) {
                // Summary
                nMoveNum = nMoveNum - 1;
                n = (int) (Math.floor(Math.log10(nMoveNum)) + 1);

                layout += SLibUtilities.textRepeat("0", 5).concat("1");
                layout += SLibUtilities.textRepeat("0", 6 - n).concat(nMoveNum + "");
                layout += sConcept.concat(SLibUtilities.textRepeat(" ", (40 - sConcept.length())));
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutHsbcThird(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title) {
        int nMoveNum = 1;
        int n = 0;
        java.lang.String sBizPartner = "";
        java.lang.String sReference = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sConcept = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("00000000000.00");
        mdBalanceTot = 0;
        mdTaxChargedTot = 0;
        mbIsRepeated = false;

        try {
            for (SLayoutBankPaymentText payment : payments) {
                n = (int) (Math.floor(Math.log10(nMoveNum)) + 1);

                sBizPartner = SLibUtilities.textToAlphanumeric(payment.getBizPartner());
                sReference = SLibUtilities.textToAlphanumeric(payment.getReference());
                sAccountDebit = SLibUtilities.textTrim(payment.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(payment.getAccountCredit());
                sConcept = SLibUtilities.textToAlphanumeric(payment.getConcept());
                mdBalanceTot = payment.getTotalAmount();

                layout += SLibUtilities.textRepeat("0", 6 - n).concat(nMoveNum++ + ""); // Transaction number
                layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 10 ? 0 : 10 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 10)); // Payer account
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 10 ? 0 : 10 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 10)); // Beneficiary account
                layout += formatDesc.format(mdBalanceTot).replace(".", ""); // Amount
                layout += payment.getCurrencyId(); // Currency
                layout += (sReference.length() > 30 ? SLibUtilities.textLeft(sReference, 30) : sReference).concat(SLibUtilities.textRepeat(" ", (30 - sReference.length()))); // Alphanumeric reference
                layout += (sBizPartner.length() > 40 ? SLibUtilities.textLeft(sBizPartner, 40) : sBizPartner).concat(SLibUtilities.textRepeat(" ", (40 - sBizPartner.length()))); // Beneficiary name
                layout += payment.getHsbcFiscalVoucher(); // Tax receipt
                layout += SLibUtilities.textRepeat(" ", 18); // RFC beneficiary
                layout += formatDesc.format(0d).replace(".", ""); // IVA
                layout += SLibUtilities.textRepeat(" ", 60); // Email

                layout += "\r\n";
            }

            if (payments.size() > 0) {
                // Summary:
                
                nMoveNum = nMoveNum - 1;
                n = (int) (Math.floor(Math.log10(nMoveNum)) + 1);

                layout += SLibUtilities.textRepeat("0", 5).concat("1"); // Number block
                layout += SLibUtilities.textRepeat("0", 6 - n).concat(nMoveNum + ""); // Total transactions
                layout += (sConcept.length() > 40 ? SLibUtilities.textLeft(sConcept, 40) : sConcept).concat(SLibUtilities.textRepeat(" ", (40 - sConcept.length()))); // Concept
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutHsbcThirdCsv(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title) {
        int nMoveNum = 1;
        String layout = "";
        String sConcept = "";
        String sReference;
        String sBizPartner;
        DecimalFormat formatDesc = new DecimalFormat("#0.00");
        
        try {
            for (SLayoutBankPaymentText payment : payments) {
                mdBalanceTot = payment.getTotalAmount();
                sConcept = SLibUtilities.textToAlphanumeric(payment.getConcept());
                sReference = SLibUtilities.textToAlphanumeric(payment.getReference());
                sBizPartner = SLibUtilities.textToAlphanumeric(payment.getBizPartner());
                
                layout += nMoveNum++ + ","; // Transaction number
                layout += SLibUtilities.textLeft(SLibUtilities.textTrim(payment.getAccountDebit()), 10) + ","; // Payer account
                layout += SLibUtilities.textLeft(SLibUtilities.textTrim(payment.getAccountCredit()), 10) + ","; // Beneficiary account
                layout += formatDesc.format(mdBalanceTot) + ","; // Amount
                layout += payment.getCurrencyId() + ","; // Currency
                layout += (sReference.length() > 30 ?  SLibUtilities.textLeft(sReference, 30) :  sReference) + ","; // Alphanumeric reference
                layout += (sBizPartner.length() > 40 ?  SLibUtilities.textLeft(sBizPartner, 40) :  sBizPartner) + ","; // Beneficiary name
                layout += payment.getHsbcFiscalVoucher(); // Tax receipt
                if (payment.getHsbcFiscalVoucher() == 1) {
                    layout += ",";
                    layout += payment.getHsbcFiscalIdCredit() + ","; // RFC beneficiary
                    layout += formatDesc.format(0d) + ""; // IVA
                    //layout += SLibUtilities.textRepeat(" ", 60); // Email
                }
                layout += "\n";
            }
            
            if (payments.size() > 0) {
                // Summary:
                
                nMoveNum = nMoveNum - 1;
                
                layout += "1,"; // Number block
                layout += nMoveNum + ""; // Total transactions
                layout += (sConcept.length() > 40 ? SLibUtilities.textLeft("," + sConcept, 40) : sConcept.isEmpty() ? "" : "," + sConcept) + ",,,,,"; // Concept
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        
        return layout;
    }

    public static java.lang.String createLayoutHsbcTefOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title) {
        int n = 0;
        int nBizPartnerId = 0;
        java.lang.String sBizPartner = "";
        java.lang.String sReference = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String layout = "";
        SLayoutBankRow values = null;
        DecimalFormat formatDesc = new DecimalFormat("000000000000.00");
        mdBalanceTot = 0;
        mdTaxChargedTot = 0;
        mbIsRepeated = false;

        try {
            for (int i = 0; i < vRows.size();) {
                values = vRows.get(i);

                n = (int) (Math.floor(Math.log10(values.getBankKey())) + 1);

                nBizPartnerId = values.getBizPartnerId();
                sBizPartner = SLibUtilities.textToAlphanumeric(values.getBizPartner());
                sReference = SLibUtilities.textToAlphanumeric(values.getReference());
                sAccountDebit = SLibUtilities.textTrim(values.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(values.getBeneficiaryAccountNumber());
                mdBalanceTot = values.getBalanceTotByBizPartner();
                if (values.getTaxCharged() != 0) {
                    mdTaxChargedTot = (values.getTotalVat() * (mdBalanceTot / values.getTotal()));
                }
                else {
                    mdTaxChargedTot = 0;
                }

                i = calculateBalanceTotal(vRows, i, nBizPartnerId, sAccountCredit);

                layout += values.getAccType();
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 20 ? 0 : 20 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 20));
                layout += SLibUtilities.textRepeat("0", 3 - n).concat(values.getBankKey() + "");
                layout += (sBizPartner.length() > 40 ? SLibUtilities.textLeft(sBizPartner, 40) : sBizPartner).concat(SLibUtilities.textRepeat(" ", (40 - sBizPartner.length())));
                layout += SLibUtilities.textRepeat("0", 3).concat(SLibUtilities.textRight(sAccountDebit, 4)); // Numerical Reference
                layout += SLibUtilities.textRepeat(" ", (sReference.length() >= 40 ? 0 : 40 - sReference.length())).concat(SLibUtilities.textLeft(sReference, 40));
                layout += formatDesc.format(mdBalanceTot).replace(".", "");
                //layout += values.getBeneficiaryFiscalId().concat(SLibUtilities.textRepeat(" ", (18 - values.getBeneficiaryFiscalId().length()))); XXX No required is optional (jbarajas)
                //layout += formatDescs.format(mdTaxChargedTot); XXX No required is optional (jbarajas)
                //layout += sEmail.concat(SLibUtilities.textRepeat(" ", (100 - sEmail.length()))); XXX No required is optional (jbarajas)

                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutHsbcTef(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title, Date date) {
        int n = 0;
        java.lang.String sBizPartner = "";
        java.lang.String sReference = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("000000000000.00");
        SimpleDateFormat formatDate = new SimpleDateFormat("ddMMyyyy");
        mdBalanceTot = 0;
        mdTaxChargedTot = 0;
        mbIsRepeated = false;

        try {
            sAccountDebit = SLibUtilities.textTrim(payments.get(0).getAccountDebit());
            
            layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 10 ? 0 : 10 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 10)); // Payer Account
            layout += formatDate.format(date); // Value Date
            layout += payments.get(0).getHsbcFiscalIdDebit().concat(SLibUtilities.textRepeat(" ", (18 - payments.get(0).getHsbcFiscalIdDebit().length()))); // RFC payer
            layout += SModSysConsts.FINS_FISCAL_CUR_MXN; // Currency
            layout += "1".concat(SLibUtilities.textRepeat(" ", 39)); // Number lot

            layout += "\r\n";
            
            for (SLayoutBankPaymentText payment : payments) {
                n = (int) (Math.floor(Math.log10(payment.getHsbcBankCode())) + 1);

                sBizPartner = SLibUtilities.textToAlphanumeric(payment.getBizPartner());
                sReference = SLibUtilities.textToAlphanumeric(payment.getReference());
                sAccountCredit = SLibUtilities.textTrim(payment.getAccountCredit());
                mdBalanceTot = payment.getTotalAmount();

                layout += payment.getHsbcAccountType(); // Account Type
                layout += SLibUtilities.textRepeat("0", 3 - n).concat(payment.getHsbcBankCode() + ""); // Code bank
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 20 ? 0 : 20 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 20)); // Beneficiary account
                layout += (sBizPartner.length() > 40 ? SLibUtilities.textLeft(sBizPartner, 40) : sBizPartner).concat(SLibUtilities.textRepeat(" ", (40 - sBizPartner.length()))); // Beneficiary name
                layout += formatDesc.format(mdBalanceTot).replace(".", ""); // Amount 
                layout += SLibUtilities.textRepeat("0", 3).concat(SLibUtilities.textRight(sAccountDebit, 4)); // Reference numeric
                layout += (sReference.length() > 40 ? SLibUtilities.textLeft(sReference, 40) : sReference).concat(SLibUtilities.textRepeat(" ", (40 - sReference.length()))); // Alphanumeric reference
                layout += SLibUtilities.textRepeat(" ", 18); // RFC Beneficiary
                layout += formatDesc.format(0d).replace(".", ""); // IVA
                layout += SLibUtilities.textRepeat(" ", 60); // Email

                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }

    public static java.lang.String createLayoutHsbcSpeiFdNOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title) {
        int nBizPartnerId = 0;
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sConcept = "";
        java.lang.String sDescription = "";
        java.lang.String layout = "";
        SLayoutBankRow values = null;
        DecimalFormat formatDesc = new DecimalFormat("0000000000000000.00");
        mdBalanceTot = 0;

        try {
            for (int i = 0; i < vRows.size();) {
                values = vRows.get(i);

                nBizPartnerId = values.getBizPartnerId();
                sAccountDebit = SLibUtilities.textTrim(values.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(values.getBeneficiaryAccountNumber());
                sConcept = SLibUtilities.textToAlphanumeric(values.getConcept());
                sDescription = SLibUtilities.textToAlphanumeric(values.getDescription());
                mdBalanceTot = values.getBalanceTotByBizPartner();

                i = calculateBalanceTotal(vRows, i, nBizPartnerId, sAccountCredit);

                layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 10 ? 0 : 10 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 10));
                layout += values.getBizPartnerDebitFiscalId().concat(SLibUtilities.textRepeat(" ", (18 - values.getBizPartnerDebitFiscalId().length())));
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 20 ? 0 : 20 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 20));
                layout += formatDesc.format(mdBalanceTot).replace(".", "");
                layout += SLibUtilities.textRepeat("0", 3).concat(SLibUtilities.textRight(sAccountDebit, 4)); // Numerical Reference
                layout += SLibUtilities.textRepeat(" ", (sConcept.length() >= 30 ? 0 : 30 - sConcept.length())).concat(SLibUtilities.textLeft(sConcept, 30));
                layout += SLibUtilities.textRepeat(" ", (sDescription.length() >= 40 ? 0 : 40 - sDescription.length())).concat(SLibUtilities.textLeft(sDescription, 40));

                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutHsbcSpeiFdN(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title) {
        int n = 0;
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sBizPartner = "";
        java.lang.String sReference = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000000000.00");
        mdBalanceTot = 0;

        try {
            for (SLayoutBankPaymentText payment : payments) {
                n = (int) (Math.floor(Math.log10(payment.getHsbcBankCode())) + 1);

                sAccountDebit = SLibUtilities.textTrim(payment.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(payment.getAccountCredit());
                sBizPartner = SLibUtilities.textToAlphanumeric(payment.getBizPartner());
                sReference = SLibUtilities.textToAlphanumeric(payment.getReference());
                mdBalanceTot = payment.getTotalAmount();

                layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 10 ? 0 : 10 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 10));// Payer account
                layout += payment.getHsbcFiscalIdDebit().concat(SLibUtilities.textRepeat(" ", (18 - payment.getHsbcFiscalIdDebit().length())));// RFC payer
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 20 ? 0 : 20 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 20));// Beneficiary account
                layout += SLibUtilities.textRepeat("0", 3 - n).concat(payment.getHsbcBankCode() + "");// Beneficiary bank
                layout += (sBizPartner.length() > 34 ? SLibUtilities.textLeft(sBizPartner, 34) : sBizPartner).concat(SLibUtilities.textRepeat(" ", (34 - sBizPartner.length())));// Beneficiary name
                layout += formatDesc.format(mdBalanceTot).replace(".", "");// Amount
                layout += SLibUtilities.textRepeat("0", 3).concat(SLibUtilities.textRight(sAccountDebit, 4)); // Reference numeric
                layout += (sReference.length() > 30 ? SLibUtilities.textLeft(sReference, 30) : sReference).concat(SLibUtilities.textRepeat(" ", (30 - sReference.length())));// Alphanumeric reference

                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutHsbcSpeiFdNCsv(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title) {
        String layout = "";
        String sAccountCredit;
        String sBizPartner;
        String sConcept;
        String sReference;
        DecimalFormat formatDesc = new DecimalFormat("#0.00");
        mdBalanceTot = 0;
        
        try {
            for (SLayoutBankPaymentText payment : payments) {
                mdBalanceTot = payment.getTotalAmount();
                sAccountCredit = SLibUtilities.textTrim(payment.getAccountCredit());
                int n = (int) (Math.floor(Math.log10(payment.getHsbcBankCode())) + 1);
                sBizPartner = SLibUtilities.textToAlphanumeric(payment.getBizPartner());
                sConcept = SLibUtilities.textToAlphanumeric(payment.getConcept());
                sReference = SLibUtilities.textToAlphanumeric(payment.getReference());
                
                layout += SLibUtilities.textLeft(SLibUtilities.textTrim(payment.getAccountDebit()), 10) + ",";// Payer account
                layout += payment.getHsbcFiscalIdDebit() + ","; // RFC payer
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 20 ? 0 : 20 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 20)) + ",";// Beneficiary account
                layout += SLibUtilities.textRepeat("0", 3 - n).concat(payment.getHsbcBankCode() + ",");// Beneficiary bank
                layout += (sBizPartner.length() > 34 ? SLibUtilities.textLeft(sBizPartner, 34) : sBizPartner) + ",";// Beneficiary name
                layout += formatDesc.format(mdBalanceTot) + ",";// Amount
                layout += SLibUtilities.textRight(payment.getAccountDebit(), 7) + ","; // Reference numeric
                layout += (sConcept.length() > 30 ? SLibUtilities.textLeft(sConcept, 30) : sConcept) + ","; // Payment concept
                layout += (sReference.length() > 30 ? SLibUtilities.textLeft(sReference, 30) : sReference) + "\n"; // Alphanumeric reference
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        
        return layout;
    }

    public static java.lang.String createLayoutHsbcSpeiFdYOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title) {
        int nBizPartnerId = 0;
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sConcept = "";
        java.lang.String layout = "";
        SLayoutBankRow values = null;
        DecimalFormat formatDesc = new DecimalFormat("0000000000000000.00");
        mdBalanceTot = 0;
        mdTaxChargedTot = 0;
        mbIsRepeated = false;

        try {
            for (int i = 0; i < vRows.size();) {
                values = vRows.get(i);

                nBizPartnerId = values.getBizPartnerId();
                sAccountDebit = SLibUtilities.textTrim(values.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(values.getBeneficiaryAccountNumber());
                sConcept = SLibUtilities.textToAlphanumeric(values.getConcept());
                mdBalanceTot = values.getBalanceTotByBizPartner();
                if (values.getTaxCharged() != 0) {
                    mdTaxChargedTot = (values.getTotalVat() * (mdBalanceTot / values.getTotal()));
                }
                else {
                    mdTaxChargedTot = 0;
                }

                i = calculateBalanceTotal(vRows, i, nBizPartnerId, sAccountCredit);

                layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 10 ? 0 : 10 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 10));
                layout += values.getBizPartnerDebitFiscalId().concat(SLibUtilities.textRepeat(" ", (18 - values.getBizPartnerDebitFiscalId().length())));
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 20 ? 0 : 20 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 20));
                layout += formatDesc.format(mdBalanceTot).replace(".", "");
                layout += "S"; // CF
                layout += values.getBeneficiaryFiscalId().concat(SLibUtilities.textRepeat(" ", (18 - values.getBeneficiaryFiscalId().length())));
                layout += values.getTaxCharged() != 0 ? formatDesc.format(mdTaxChargedTot).replace(".", "") : formatDesc.format(0d).replace(".", "");
                layout += SLibUtilities.textRepeat("0", 3).concat(SLibUtilities.textRight(sAccountDebit, 4)); // Numerical Reference
                layout += SLibUtilities.textRepeat(" ", (sConcept.length() >= 40 ? 0 : 40 - sConcept.length())).concat(SLibUtilities.textLeft(sConcept, 40));

                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutHsbcSpeiFdY(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title) {
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sConcept = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000000000.00");
        mdBalanceTot = 0;

        try {
            for (SLayoutBankPaymentText payment : payments) {
                sAccountDebit = SLibUtilities.textTrim(payment.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(payment.getAccountCredit());
                sConcept = SLibUtilities.textToAlphanumeric(payment.getConcept());
                mdBalanceTot = payment.getTotalAmount();

                layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 10 ? 0 : 10 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 10));
                layout += payment.getHsbcFiscalIdDebit().concat(SLibUtilities.textRepeat(" ", (18 - payment.getHsbcFiscalIdDebit().length())));
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 20 ? 0 : 20 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 20));
                layout += formatDesc.format(mdBalanceTot).replace(".", "");
                layout += "S"; // CF
                layout += payment.getHsbcFiscalIdCredit().concat(SLibUtilities.textRepeat(" ", (18 - payment.getHsbcFiscalIdCredit().length())));
                layout +=  formatDesc.format(0d).replace(".", "");
                layout += SLibUtilities.textRepeat("0", 3).concat(SLibUtilities.textRight(sAccountDebit, 4)); // Numerical Reference
                layout += SLibUtilities.textRepeat(" ", (sConcept.length() >= 40 ? 0 : 40 - sConcept.length())).concat(SLibUtilities.textLeft(sConcept, 40));

                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }

    /*
     * Bank layouts of Santander
     */
    
    public static java.lang.String createLayoutSantanderThirdOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title) {
        int nBizPartnerId = 0;
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sConcept = "";
        java.lang.String layout = "";
        SLayoutBankRow values = null;
        DecimalFormat formatDesc = new DecimalFormat("0000000000.00");
        SimpleDateFormat formatDate = new SimpleDateFormat("ddMMyyyy");
        mdBalanceTot = 0;
        mdTaxChargedTot = 0;
        mbIsRepeated = false;

        try {
            for (int i = 0; i < vRows.size();) {
                values = vRows.get(i);

                nBizPartnerId = values.getBizPartnerId();
                sAccountDebit = SLibUtilities.textTrim(values.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(values.getBeneficiaryAccountNumber());
                sConcept = SLibUtilities.textToAlphanumeric(values.getConcept());
                mdBalanceTot = values.getBalanceTotByBizPartner();
                if (values.getTaxCharged() != 0) {
                    mdTaxChargedTot = (values.getTotalVat() * (mdBalanceTot / values.getTotal()));
                }
                else {
                    mdTaxChargedTot = 0;
                }

                i = calculateBalanceTotal(vRows, i, nBizPartnerId, sAccountCredit);

                layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 11 ? 0 : 11 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 11)); // Debit acccount
                layout += SLibUtilities.textRepeat(" ", 5); // Blank
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 11 ? 0 : 11 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 11)); // Credit account
                layout += SLibUtilities.textRepeat(" ", 5); // Blank
                layout += formatDesc.format(mdBalanceTot);
                layout += SLibUtilities.textRepeat(" ", (sConcept.length() >= 40 ? 0 : 40 - sConcept.length())).concat(SLibUtilities.textLeft(sConcept, 40));
                layout += formatDate.format(values.getDpsDate());

                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutSantanderThird(ArrayList<SLayoutBankPaymentText> payments, Date date, java.lang.String title) {
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sConcept = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000.00");
        SimpleDateFormat formatDate = new SimpleDateFormat("ddMMyyyy");
        mdBalanceTot = 0;

        try {
            for (SLayoutBankPaymentText payment : payments) {
                sAccountDebit = SLibUtilities.textTrim(payment.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(payment.getAccountCredit());
                sConcept = SLibUtilities.textToAlphanumeric(payment.getConcept());
                mdBalanceTot = payment.getTotalAmount();

                layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 11 ? 0 : 11 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 11)); // Debit acccount
                layout += SLibUtilities.textRepeat(" ", 5); // Blank
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 11 ? 0 : 11 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 11)); // Credit account
                layout += SLibUtilities.textRepeat(" ", 5); // Blank
                layout += formatDesc.format(mdBalanceTot);
                layout += SLibUtilities.textRepeat(" ", (sConcept.length() >= 40 ? 0 : 40 - sConcept.length())).concat(SLibUtilities.textLeft(sConcept, 40));
                layout += formatDate.format(date);

                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutSantanderTefOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title) {
        int nBizPartnerId = 0;
        java.lang.String sBizPartner = "";
        java.lang.String sBizPartnerSantanderCode = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sConcept = "";
        java.lang.String sReference = "";
        java.lang.String layout = "";
        SLayoutBankRow values = null;
        DecimalFormat formatDesc = new DecimalFormat("0000000000000.00");
        mdBalanceTot = 0;
        mdTaxChargedTot = 0;
        mbIsRepeated = false;

        try {
            for (int i = 0; i < vRows.size();) {
                values = vRows.get(i);

                nBizPartnerId = values.getBizPartnerId();
                sBizPartner = SLibUtilities.textToAlphanumeric(values.getBizPartner());
                sBizPartnerSantanderCode = SLibUtilities.textToAlphanumeric(values.getSantanderBankCode());
                sAccountDebit = SLibUtilities.textTrim(values.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(values.getBeneficiaryAccountNumber());
                sConcept = SLibUtilities.textToAlphanumeric(values.getConcept());
                sReference = SLibUtilities.textToAlphanumeric(values.getReference());
                mdBalanceTot = values.getBalanceTotByBizPartner();
                if (values.getTaxCharged() != 0) {
                    mdTaxChargedTot = (values.getTotalVat() * (mdBalanceTot / values.getTotal()));
                }
                else {
                    mdTaxChargedTot = 0;
                }

                i = calculateBalanceTotal(vRows, i, nBizPartnerId, sAccountCredit);

                layout += (sAccountDebit.length() > 16 ? SLibUtilities.textLeft(sAccountDebit, 16) : sAccountDebit).concat(SLibUtilities.textRepeat(" ", (16 - sAccountDebit.length()))); // Debit acccount
                layout += (sAccountCredit.length() > 20 ? SLibUtilities.textLeft(sAccountCredit, 20) : sAccountCredit).concat(SLibUtilities.textRepeat(" ", (20 - sAccountCredit.length()))); // Credit account
                layout += (sBizPartnerSantanderCode.length() > 0 ? sBizPartnerSantanderCode.concat(SLibUtilities.textRepeat(" ", (5 - sBizPartnerSantanderCode.length()))) : SLibUtilities.textRepeat(" ", 5)); // Bank
                layout += (sBizPartner.length() > 40 ? SLibUtilities.textLeft(sBizPartner, 40) : sBizPartner).concat(SLibUtilities.textRepeat(" ", (40 - sBizPartner.length()))); // Beneficiary
                layout += "0000"; // Branch credit
                layout += formatDesc.format(mdBalanceTot).replace(".", "");
                layout += "00000"; // Plaza Banxico
                layout += SLibUtilities.textRepeat(" ", (sConcept.length() >= 40 ? 0 : 40 - sConcept.length())).concat(SLibUtilities.textLeft(sConcept, 40));
                layout += SLibUtilities.textRepeat(" ", 90); // Blank
                layout += SLibUtilities.textRepeat(" ", (sReference.length() >= 7 ? 0 : 7 - sReference.length())).concat(SLibUtilities.textLeft(sReference, 7));
                layout += 1;

                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutSantanderTef(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public static java.lang.String createLayoutSantanderSpeiFdNOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public static java.lang.String createLayoutSantanderSpeiFdN(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title) {
        java.lang.String sBizPartner = "";
        java.lang.String sBizPartnerSantanderCode = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sConcept = "";
        java.lang.String sReference = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000000.00");
        mdBalanceTot = 0;

        try {
            for (SLayoutBankPaymentText payment : payments) {
                sBizPartner = SLibUtilities.textToAlphanumeric(payment.getBizPartner());
                sBizPartnerSantanderCode = SLibUtilities.textToAlphanumeric(payment.getSantanderBankCode());
                sAccountDebit = SLibUtilities.textTrim(payment.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(payment.getAccountCredit());
                sConcept = SLibUtilities.textToAlphanumeric(payment.getConcept());
                sReference = SLibUtilities.textToAlphanumeric(payment.getReference());
                // Changes for layout, Andrea need: erase all leters and replace for " " (blank). 
                sReference = sReference.replaceAll("[A-Za-z\\u00C0-\\u017F]", " ");
                mdBalanceTot = payment.getTotalAmount();

                layout += (sAccountDebit.length() > 16 ? SLibUtilities.textLeft(sAccountDebit, 16) : sAccountDebit).concat(SLibUtilities.textRepeat(" ", (16 - sAccountDebit.length()))); // Debit acccount
                layout += (sAccountCredit.length() > 20 ? SLibUtilities.textLeft(sAccountCredit, 20) : sAccountCredit).concat(SLibUtilities.textRepeat(" ", (20 - sAccountCredit.length()))); // Credit account
                layout += (sBizPartnerSantanderCode.length() > 0 ? sBizPartnerSantanderCode.concat(SLibUtilities.textRepeat(" ", (5 - sBizPartnerSantanderCode.length()))) : SLibUtilities.textRepeat(" ", 5)); // Bank
                layout += (sBizPartner.length() > 40 ? SLibUtilities.textLeft(sBizPartner, 40) : sBizPartner).concat(SLibUtilities.textRepeat(" ", (40 - sBizPartner.length()))); // Beneficiary
                layout += "0000"; // Branch credit
                layout += formatDesc.format(mdBalanceTot).replace(".", "");
                layout += "00000"; // Plaza Banxico
                layout += SLibUtilities.textRepeat(" ", (sConcept.length() >= 40 ? 0 : 40 - sConcept.length())).concat(SLibUtilities.textLeft(sConcept, 40));
                layout += SLibUtilities.textRepeat(" ", 90); // Blank
                layout += SLibUtilities.textRepeat(" ", (sReference.length() >= 7 ? 0 : 7 - sReference.length())).concat(SLibUtilities.textLeft(sReference, 7));
                layout += 1;

                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutSantanderSpeiFdYOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title) {
        return createLayoutSantanderTefOld(client, vRows, title);
    }
    
    public static java.lang.String createLayoutSantanderSpeiFdY(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title) {
        return createLayoutSantanderSpeiFdN(payments, title);
    }
    
    /*
     * Bank layouts of BanBajío
     */
    
    private static java.lang.String createLayoutBanBajioOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title, Date date, Date dateApplication, int consecutiveDay, String typeLay, String typeAccountCredit) {
        int nMoveNum = 2;
        int nMoveNumTotal = 0;
        int n = 0;
        int m = 0;
        int nBizPartnerId = 0;
        java.lang.String sBizPartnerBanBajioCode = "";
        java.lang.String sBizPartnerAliasBanBajio = "";
        java.lang.String sReference = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String layout = "";
        SLayoutBankRow values = null;
        DecimalFormat formatDesc = new DecimalFormat("0000000000000.00");
        DecimalFormat formatDescTotal = new DecimalFormat("0000000000000000.00");
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
        double mdBalanceTotal = 0;
        mdBalanceTot = 0;
        mdTaxChargedTot = 0;
        mbIsRepeated = false;

        try {
            n = (int) (Math.floor(Math.log10(consecutiveDay)) + 1);

            layout += "01";
            layout += "0000001";
            layout += formatDate.format(date);
            layout += SLibUtilities.textRepeat("0", 3 - n).concat(consecutiveDay + ""); // Consecutive Day

            layout += "\r\n";

            for (int i = 0; i < vRows.size();) {
                values = vRows.get(i);

                n = (int) (Math.floor(Math.log10(nMoveNum)) + 1);

                nBizPartnerId = values.getBizPartnerId();
                sBizPartnerBanBajioCode = SLibUtilities.textToAlphanumeric(values.getBajioBankCode());
                sBizPartnerAliasBanBajio = SLibUtilities.textToAlphanumeric(values.getBajioBankAlias());
                sReference = SLibUtilities.textToAlphanumeric(values.getReference());
                sAccountDebit = SLibUtilities.textTrim(values.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(values.getBeneficiaryAccountNumber());
                mdBalanceTot = values.getBalanceTotByBizPartner();
                mdBalanceTotal += mdBalanceTot;
                if (values.getTaxCharged() != 0) {
                    mdTaxChargedTot = (values.getTotalVat() * (mdBalanceTot / values.getTotal()));
                }
                else {
                    mdTaxChargedTot = 0;
                }

                i = calculateBalanceTotal(vRows, i, nBizPartnerId, sAccountCredit);

                layout += "02";
                layout += SLibUtilities.textRepeat("0", 7 - n).concat(nMoveNum++ + "");
                layout += TXT_TYPE_ACCOUNT_CHECK;
                layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 20 ? 0 : 20 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 20)); // Debit acccount
                layout += "01"; // Currency
                layout += (sBizPartnerBanBajioCode.length() > 0 ? sBizPartnerBanBajioCode.concat(SLibUtilities.textRepeat(" ", (5 - sBizPartnerBanBajioCode.length()))) : SLibUtilities.textRepeat(" ", 5)); // Code Bank
                layout += formatDesc.format(mdBalanceTot).replace(".", "");
                layout += formatDate.format(dateApplication);
                layout += typeLay;
                layout += typeAccountCredit;
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 20 ? 0 : 20 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 20)); // Credit account
                layout += "000000000"; // FILLER
                layout += (sBizPartnerAliasBanBajio.length() > 15 ? SLibUtilities.textLeft(sBizPartnerAliasBanBajio, 15) : sBizPartnerAliasBanBajio).concat(SLibUtilities.textRepeat(" ", (15 - sBizPartnerAliasBanBajio.length()))); // ALIAS
                layout += formatDesc.format(0d).replace(".", "");
                layout += SLibUtilities.textRepeat(" ", (sReference.length() >= 40 ? 0 : 40 - sReference.length())).concat(SLibUtilities.textLeft(sReference, 40));

                layout += "\r\n";

                nMoveNumTotal++;
            }

            if (vRows.size() > 0) {
                // Summary
                n = (int) (Math.floor(Math.log10(nMoveNum)) + 1);
                m = (int) (Math.floor(Math.log10(nMoveNumTotal)) + 1);

                layout += "09";
                layout += SLibUtilities.textRepeat("0", 7 - n).concat(nMoveNum + "");
                layout += SLibUtilities.textRepeat("0", 7 - m).concat(nMoveNumTotal + "");
                layout += formatDescTotal.format(mdBalanceTotal).replace(".", "");
            }
            layout += "\r\n";
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    private static java.lang.String createLayoutBanBajio(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title, Date date, Date dateApplication, int consecutiveDay, String typeLay, String typeAccountCredit) {
        int nMoveNum = 2;
        int nMoveNumTotal = 0;
        int n = 0;
        int m = 0;
        java.lang.String sBizPartnerBanBajioCode = "";
        java.lang.String sBizPartnerAliasBanBajio = "";
        java.lang.String sReference = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000000.00");
        DecimalFormat formatDescTotal = new DecimalFormat("0000000000000000.00");
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
        double mdBalanceTotal = 0;
        mdBalanceTot = 0;

        try {
            n = (int) (Math.floor(Math.log10(consecutiveDay)) + 1);

            layout += "01";
            layout += "0000001";
            layout += formatDate.format(date);
            layout += SLibUtilities.textRepeat("0", 3 - n).concat(consecutiveDay + ""); // Consecutive Day

            layout += "\r\n";

            for (SLayoutBankPaymentText payment : payments) {
                n = (int) (Math.floor(Math.log10(nMoveNum)) + 1);
                
                sBizPartnerBanBajioCode = SLibUtilities.textToAlphanumeric(payment.getBajioBankCode());
                sBizPartnerAliasBanBajio = SLibUtilities.textToAlphanumeric(payment.getBajioBankNick());
                sReference = SLibUtilities.textToAlphanumeric(payment.getReference());
                sAccountDebit = SLibUtilities.textTrim(payment.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(payment.getAccountCredit());
                mdBalanceTot = payment.getTotalAmount();
                mdBalanceTotal += mdBalanceTot;

                layout += "02";
                layout += SLibUtilities.textRepeat("0", 7 - n).concat(nMoveNum++ + "");
                layout += TXT_TYPE_ACCOUNT_CHECK;
                layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 20 ? 0 : 20 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 20)); // Debit acccount
                layout += "01"; // Currency
                layout += (sBizPartnerBanBajioCode.length() > 0 ? sBizPartnerBanBajioCode.concat(SLibUtilities.textRepeat(" ", (5 - sBizPartnerBanBajioCode.length()))) : SLibUtilities.textRepeat(" ", 5)); // Code Bank
                layout += formatDesc.format(mdBalanceTot).replace(".", "");
                layout += formatDate.format(dateApplication);
                layout += typeLay;
                layout += typeAccountCredit;
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 20 ? 0 : 20 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 20)); // Credit account
                layout += "000000000"; // FILLER
                layout += (sBizPartnerAliasBanBajio.length() > 15 ? SLibUtilities.textLeft(sBizPartnerAliasBanBajio, 15) : sBizPartnerAliasBanBajio).concat(SLibUtilities.textRepeat(" ", (15 - sBizPartnerAliasBanBajio.length()))); // ALIAS
                layout += formatDesc.format(0d).replace(".", "");
                layout += SLibUtilities.textRepeat(" ", (sReference.length() >= 40 ? 0 : 40 - sReference.length())).concat(SLibUtilities.textLeft(sReference, 40));

                layout += "\r\n";

                nMoveNumTotal++;
            }

            if (payments.size() > 0) {
                // Summary
                n = (int) (Math.floor(Math.log10(nMoveNum)) + 1);
                m = (int) (Math.floor(Math.log10(nMoveNumTotal)) + 1);

                layout += "09";
                layout += SLibUtilities.textRepeat("0", 7 - n).concat(nMoveNum + "");
                layout += SLibUtilities.textRepeat("0", 7 - m).concat(nMoveNumTotal + "");
                layout += formatDescTotal.format(mdBalanceTotal).replace(".", "");
            }
            layout += "\r\n";
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutBanBajioThirdOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title, Date date, int consecutiveDay) {
        return createLayoutBanBajioOld(client, vRows, title, date, date, consecutiveDay, TXT_TYPE_LAY_BANBAJIO_LOCAL, TXT_TYPE_ACCOUNT_CHECK);
    }
    
    public static java.lang.String createLayoutBanBajioThird(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title, Date date, int consecutiveDay) {
        return createLayoutBanBajio(payments, title, date, date, consecutiveDay, TXT_TYPE_LAY_BANBAJIO_LOCAL, TXT_TYPE_ACCOUNT_CHECK);
    }
    
    public static java.lang.String createLayoutBanBajioTefOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title, Date date, int consecutiveDay) {
        return createLayoutBanBajioOld(client, vRows, title, date, date, consecutiveDay, TXT_TYPE_LAY_BANBAJIO_TEF, TXT_TYPE_ACCOUNT_CLABE);
    }
    
    public static java.lang.String createLayoutBanBajioTef(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title, Date date, int consecutiveDay) {
        return createLayoutBanBajio(payments, title, date, date, consecutiveDay, TXT_TYPE_LAY_BANBAJIO_TEF, TXT_TYPE_ACCOUNT_CLABE);
    }
    
    public static java.lang.String createLayoutBanBajioSpeiFdNOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title, Date date, int consecutiveDay) {
        return createLayoutBanBajioOld(client, vRows, title, date, date, consecutiveDay, TXT_TYPE_LAY_BANBAJIO_SPEI, TXT_TYPE_ACCOUNT_CLABE);
    }
    
    public static java.lang.String createLayoutBanBajioSpeiFdN(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title, Date date, int consecutiveDay) {
        return createLayoutBanBajio(payments, title, date, date, consecutiveDay, TXT_TYPE_LAY_BANBAJIO_SPEI, TXT_TYPE_ACCOUNT_CLABE);
    }
    
    /*
     * Bank layouts of BBVA
     */
    
    private static java.lang.String createLayoutBbvaInterbank(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title, String availability) {
        int n = 0;
        java.lang.String sBizPartner = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sReference = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000000.00");
        mdBalanceTot = 0;

        try {
            for (SLayoutBankPaymentText payment : payments) {
                n = (int) (Math.floor(Math.log10(payment.getBankKey())) + 1);
                
                sBizPartner = SLibUtilities.textToAlphanumeric(payment.getBizPartner());
                sAccountDebit = SLibUtilities.textTrim(payment.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(payment.getAccountCredit());
                sReference = SLibUtilities.textToAlphanumeric(payment.getReference());
                mdBalanceTot = payment.getTotalAmount();

                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 18 ? 0 : 18 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 18)); // Credit acccount
                layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 18 ? 0 : 18 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 18)); // Debit acccount
                layout += TXT_TYPE_CUR_MXP_BBVA; // badge
                layout += formatDesc.format(mdBalanceTot); // Total amount
                layout += (sBizPartner.length() > 30 ? SLibUtilities.textLeft(sBizPartner, 30) : sBizPartner).concat(SLibUtilities.textRepeat(" ", (30 - sBizPartner.length()))); // Beneficiary
                layout += TXT_TYPE_ACCOUNT_CLABE; // Type account
                layout += SLibUtilities.textRepeat("0", 3 - n).concat(payment.getBankKey() + ""); // Benefisary Bank Number
                layout += (sReference.length() > 30 ? SLibUtilities.textLeft(sReference, 30) : sReference).concat(SLibUtilities.textRepeat(" ", (30 - sReference.length()))); // Reason Payment
                layout += SLibUtilities.textRepeat(" ", 7); // Numeric Reference
                layout += availability; // Availability

                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutBbvaThird(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title) {
        java.lang.String sReference = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000000.00");
        mdBalanceTot = 0;
        mdTaxChargedTot = 0;
        mbIsRepeated = false;

        try {
            for (SLayoutBankPaymentText payment : payments) {
                sReference = SLibUtilities.textToAlphanumeric(payment.getReference());
                sAccountDebit = SLibUtilities.textTrim(payment.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(payment.getAccountCredit());
                mdBalanceTot = payment.getTotalAmount();

                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 18 ? 0 : 18 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 18)); // Credit acccount
                layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 18 ? 0 : 18 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 18)); // Debit acccount
                layout += (payment.getCurrencyId() == 1 ? TXT_TYPE_CUR_MXP_BBVA : (payment.getCurrencyId() == 2 ? TXT_TYPE_CUR_USD_BBVA : TXT_TYPE_CUR_EUR_BBVA)); // badge
                layout += formatDesc.format(mdBalanceTot); // Total amount
                layout += (sReference.length() > 30 ? SLibUtilities.textLeft(sReference, 30) : sReference).concat(SLibUtilities.textRepeat(" ", (30 - sReference.length()))); // Reason Payment
                
                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutBbvaTef(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title) {
        return createLayoutBbvaInterbank(payments, title, TXT_TYPE_LAY_BBVA_TEF);
    }
    
    public static java.lang.String createLayoutBbvaSpei(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title) {
        return createLayoutBbvaInterbank(payments, title, TXT_TYPE_LAY_BBVA_SPEI);
    }
    
    public static java.lang.String createLayoutBbvaCie(final ArrayList<SLayoutBankPaymentText> payments) {
        double dImporte = 0;
        java.lang.String sConceptoCie = "";
        java.lang.String sConvenioCie = "";
        java.lang.String sAsuntoOrdenante = "";                                                     // in fact, beneficiary's bank account
        java.lang.String sReferenciaCie = "";
        java.lang.String sLayout = "";
        
        DecimalFormat oFormatConvenioCie = new DecimalFormat(SLibUtils.textRepeat("0", 7));         // fixed length defined in layout specification
        DecimalFormat oFormatAsuntoOrdenante = new DecimalFormat(SLibUtils.textRepeat("0", 18));    // fixed length defined in layout specification
        DecimalFormat oFormatImporte = new DecimalFormat(SLibUtils.textRepeat("0", 13) + "." + SLibUtils.textRepeat("0", 2));   // fixed length defined in layout specification
        
        try {
            for (SLayoutBankPaymentText payment : payments) {
                sConceptoCie = SLibUtilities.textTrim(payment.getConceptCie());
                sConvenioCie = SLibUtilities.textToAlphanumeric(payment.getAgreement());
                sAsuntoOrdenante = SLibUtilities.textTrim(payment.getAccountDebit());
                dImporte = payment.getTotalAmount();
                sReferenciaCie = SLibUtilities.textToAlphanumeric(payment.getAgreementReference());
                
                sLayout += SPrtUtils.formatText(sConceptoCie, 30, SPrtConsts.ALIGN_LEFT, SPrtConsts.TRUNC_TRUNC);   // fixed length defined in layout specification
                sLayout += oFormatConvenioCie.format(SLibUtils.parseLong(sConvenioCie));
                sLayout += oFormatAsuntoOrdenante.format(SLibUtils.parseLong(sAsuntoOrdenante));
                sLayout += oFormatImporte.format(dImporte);
                sLayout += SPrtUtils.formatText(sConceptoCie, 30, SPrtConsts.ALIGN_LEFT, SPrtConsts.TRUNC_TRUNC);   // fixed length defined in layout specification, same text as Concepto CIE
                sLayout += SPrtUtils.formatText(sReferenciaCie, 20, SPrtConsts.ALIGN_LEFT, SPrtConsts.TRUNC_TRUNC); // fixed length defined in layout specification
                sLayout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        
        return sLayout;
    }
    
    /*
     * Bank layouts of Citibanamex
     */
    
    
    public static String getBizPartnerForCitibanamex(SGuiSession session, int bizPartnerId) throws Exception {
        String bizPartner = "";
        String firstname = "";
        String lastname = "";
        String motherLastname = "";
        String fatherLastname = "";
        String sql = "";
        ResultSet resulSet = null;

        sql = "SELECT bp, lastname, firstname, fid_tp_bp_idy " +
              "FROM erp.bpsu_bp " +
              "WHERE id_bp = " + bizPartnerId;

        resulSet = session.getDatabase().getConnection().createStatement().executeQuery(sql);
        if (resulSet.next()) {
            if (resulSet.getInt("fid_tp_bp_idy") == SModSysConsts.BPSS_TP_BP_IDY_ORG) {
                firstname = resulSet.getString("bp");
                bizPartner = "," + SLibUtilities.textToAlphanumeric(firstname) + "/";
            }
            else {
                firstname = resulSet.getString("firstname");
                lastname = resulSet.getString("lastname");
                fatherLastname = lastname.substring(0, !lastname.contains(" ") ? lastname.length() : lastname.indexOf(" "));
                motherLastname = lastname.length() > fatherLastname.length() + 1 ? lastname.substring(fatherLastname.length() + 1) : "";
                bizPartner = SLibUtilities.textToAlphanumeric(firstname) + "," + SLibUtilities.textToAlphanumeric(fatherLastname) + "/" + SLibUtilities.textToAlphanumeric(motherLastname);
            }
        }
        return bizPartner;
    }
    
    public static java.lang.String createLayoutCitibanamexThird(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title) {
        java.lang.String sReference = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountBranchDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sAccountBranchCredit = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("000000000000.00");
        mdBalanceTot = 0;
        mdTaxChargedTot = 0;
        mbIsRepeated = false;

        try {
            for (SLayoutBankPaymentText payment : payments) {
                sReference = SLibUtilities.textToAlphanumeric(payment.getReference());
                sAccountDebit = SLibUtilities.textTrim(payment.getAccountDebit());
                sAccountBranchDebit = SLibUtilities.textTrim(payment.getAccountBranchDebit());
                sAccountCredit = SLibUtilities.textTrim(payment.getAccountCredit());
                sAccountBranchCredit = SLibUtilities.textTrim(payment.getAccountBranchCredit());
                mdBalanceTot = payment.getTotalAmount();

                layout += TXT_TYPE_BMX_TRAN_CHECK; // Type transaction
                layout += TXT_TYPE_ACCOUNT_CHECK; // Type debit account
                layout += SLibUtilities.textRepeat("0", (sAccountBranchDebit.length() >= 4 ? 0 : 4 - sAccountBranchDebit.length())).concat(SLibUtilities.textLeft(sAccountBranchDebit, 4)); // Branch debit account
                layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 20 ? 0 : 20 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 20)); // Debit acccount
                layout += TXT_TYPE_ACCOUNT_CHECK; // Type credit account
                layout += SLibUtilities.textRepeat("0", (sAccountBranchCredit.length() >= 4 ? 0 : 4 - sAccountBranchCredit.length())).concat(SLibUtilities.textLeft(sAccountBranchCredit, 4)); // Branch credit account
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 20 ? 0 : 20 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 20)); // Credit acccount
                layout += formatDesc.format(mdBalanceTot).replace(".", ""); // Total amount
                layout += (payment.getCurrencyId() == 1 ? TXT_TYPE_CUR_MXP_BMX : TXT_TYPE_CUR_USD_BMX); // Currency transaction
                layout += (sReference.length() > 24 ? SLibUtilities.textLeft(sReference, 24) : sReference).concat(SLibUtilities.textRepeat(" ", (24 - sReference.length()))); // Description
                layout += (sReference.length() > 34 ? SLibUtilities.textLeft(sReference, 34) : sReference).concat(SLibUtilities.textRepeat(" ", (34 - sReference.length()))); // Concept
                layout += SLibUtilities.textRepeat("0", 6).concat(SLibUtilities.textRight(sAccountDebit, 4)); // Reference
                layout += "000"; // Currency
                layout += "000000"; // Date application
                layout += "0000"; // Hour application
                
                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    private static java.lang.String createLayoutCitibanamexInterbank(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title, String lapse, SGuiSession session) {
        int n = 0;
        java.lang.String sDescription = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountBranchDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sBizPartner = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("000000000000.00");
        DecimalFormat formatDescTax = new DecimalFormat("0000000000.00");
        mdBalanceTot = 0;
        mdTaxChargedTot = 0;
        mbIsRepeated = false;

        try {
            for (SLayoutBankPaymentText payment : payments) {
                n = (int) (Math.floor(Math.log10(payment.getBankKey())) + 1);

                sDescription = SLibUtilities.textToAlphanumeric(payment.getReference());
                sAccountDebit = SLibUtilities.textTrim(payment.getAccountDebit());
                sAccountBranchDebit = SLibUtilities.textTrim(payment.getAccountBranchDebit());
                sAccountCredit = SLibUtilities.textTrim(payment.getAccountCredit());
                sBizPartner = getBizPartnerForCitibanamex(session, payment.getBizPartnerId());
                mdBalanceTot = payment.getTotalAmount();

                layout += TXT_TYPE_BMX_TRAN_CLABE; // Type transaction
                layout += TXT_TYPE_ACCOUNT_CHECK; // Type debit account
                layout += SLibUtilities.textRepeat("0", (sAccountBranchDebit.length() >= 4 ? 0 : 4 - sAccountBranchDebit.length())).concat(SLibUtilities.textLeft(sAccountBranchDebit, 4)); // Branch debit account
                layout += SLibUtilities.textRepeat("0", (sAccountDebit.length() >= 20 ? 0 : 20 - sAccountDebit.length())).concat(SLibUtilities.textLeft(sAccountDebit, 20)); // Debit acccount
                layout += formatDesc.format(mdBalanceTot).replace(".", ""); // Total amount
                layout += TXT_TYPE_CUR_MXP_BMX; // Currency transaction
                layout += TXT_TYPE_ACCOUNT_CLABE; // Type credit account
                layout += SLibUtilities.textRepeat("0", (sAccountCredit.length() >= 20 ? 0 : 20 - sAccountCredit.length())).concat(SLibUtilities.textLeft(sAccountCredit, 20)); // Credit acccount
                layout += (sDescription.length() > 40 ? SLibUtilities.textLeft(sDescription, 40) : sDescription).concat(SLibUtilities.textRepeat(" ", (40 - sDescription.length()))); // Description
                layout += SLibUtilities.textRepeat("0", 3).concat(SLibUtilities.textRight(sAccountDebit, 4)); // Reference
                layout += (sBizPartner.length() > 55 ? SLibUtilities.textLeft(sBizPartner, 55) : sBizPartner).concat(SLibUtilities.textRepeat(" ", (55 - sBizPartner.length()))); // Beneficiary
                layout += lapse; // Lapse
                layout += payment.getHsbcFiscalIdCredit().concat(SLibUtilities.textRepeat(" ", (14 - payment.getHsbcFiscalIdCredit().length()))); // RFC
                layout += formatDescTax.format(0d).replace(".", ""); // IVA
                layout += SLibUtilities.textRepeat("0", 4 - n).concat(payment.getBankKey() + ""); // Benefisary Bank Number
                layout += "000000"; // Date application
                layout += "0000"; // Hour application
                
                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutCitibanamexTef(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title, SGuiSession session) {
        return createLayoutCitibanamexInterbank(payments, title, TXT_TERM_BMX_NEXT_DAY, session);
    }
    
    public static java.lang.String createLayoutCitibanamexSpei(ArrayList<SLayoutBankPaymentText> payments, java.lang.String title, SGuiSession session) {
        return createLayoutCitibanamexInterbank(payments, title, TXT_TERM_BMX_SAME_DAY, session);
    }
    
    /*
     * Miscellaneous
     */
    
    /**
     * Checks if accounting transactions have a business partner.
     * @param client
     * @param year
     * @param bizPartnerId
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static boolean hasBizPartnerMovesFinance(final SClientInterface client, final int year, final int bizPartnerId) throws Exception {
        boolean bHas = false;
        String sql = "";
        ResultSet resulSet = null;
        ArrayList<String> maDataBase = null;

        sql = "SELECT bd " +
              "FROM erp.cfgu_co " +
                "WHERE b_del = 0 ";

        resulSet = client.getSession().getStatement().executeQuery(sql);
        while (resulSet.next()) {
            maDataBase = new ArrayList<>();
            maDataBase.add(resulSet.getString("bd"));
        }
        
        for (String db : maDataBase) {
            sql = "SELECT * " +
                    "FROM " + db + ".fin_rec_ety " +
                    "WHERE b_del = 0 AND id_year = " + year + " AND fid_bp_nr = " + bizPartnerId + " ";

            resulSet = client.getSession().getStatement().executeQuery(sql);
            if (resulSet.next()) {
                bHas = true;
                break;
            }
        }
        return bHas;
    }
    
    public static boolean changeDeepAccount(final SClientInterface client, final int [] keyOldAccount, final int deep, boolean isValidate) throws Exception {
        SDbAccount account = null;
        
        account = new SDbAccount();
        account.read(client.getSession(), keyOldAccount);
        
        if (isValidate) {
            if (account.getIsDeleted()) {
                throw new Exception("La cuenta contable está eliminada.");
            }
            else if (!account.getIsActive()) {
                throw new Exception("La cuenta contable no está activa.");
            }
            else if (account.getDeep() == 0) {
                throw new Exception("La cuenta contable debe ser cuenta de mayor.");
            } 
            else if (account.getDeep() != 1) {
                throw new Exception("La profundidad de la cuenta contable debe ser 1.");
            } 
        }            
        account.saveField(client.getSession().getStatement(), account.getPrimaryKey(), SDbAccount.FIELD_DEEP, deep);
        return true;
    }
    
    public static boolean changeRecordEntriesAccount(final SClientInterface client, final int [] keyOldAccount, final int[] keyNewAccount) throws Exception {
        SDataRecordEntry recordEntry = null;
        SDbAccount oldAccount = null;
        SDbAccount newAccount = null;
        ResultSet resultSet = null;
        String sql = "";
        Object[] key = null;
        ArrayList<Object[]> keys = null;
        
        oldAccount = new SDbAccount();
        newAccount = new SDbAccount();
        
        oldAccount.read(client.getSession(), keyOldAccount);
        newAccount.read(client.getSession(), keyNewAccount);
        
        sql = "SELECT id_year, id_per, id_bkc, id_tp_rec, id_num, id_ety " +
                "FROM fin_rec_ety WHERE fid_acc = '" + oldAccount.getPkAccountIdXXX() + "' AND fk_acc = " + oldAccount.getPkAccountId();
        resultSet = client.getSession().getStatement().executeQuery(sql);
        keys = new ArrayList<>();
        
        while (resultSet.next()) {
            key = new Object[6];
            
            key[0] = resultSet.getInt("id_year");
            key[1] = resultSet.getInt("id_per"); 
            key[2] = resultSet.getInt("id_bkc"); 
            key[3] = resultSet.getString("id_tp_rec"); 
            key[4] = resultSet.getInt("id_num"); 
            key[5] = resultSet.getInt("id_ety");
            
            keys.add(key);
        }
        
        for (Object[] pk : keys) {
            recordEntry = new SDataRecordEntry();
            recordEntry.read(pk, client.getSession().getStatement());
            recordEntry.setFkAccountIdXXX(newAccount.getPkAccountIdXXX());
            recordEntry.setFkAccountId(newAccount.getPkAccountId());
            recordEntry.setFkUserEditId(client.getSession().getUser().getPkUserId());
            
            if (recordEntry.save(client.getSession().getStatement().getConnection()) != SLibConstants.DB_ACTION_SAVE_OK) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE);
            }
        }
        
        return true;
    }
    
    /**
     * Evalúa si una partida de documento comercial (DPS Entry) cumple con las reglas 
     * de negocio financieras para permitir la modificación de su cuenta contable o centro de costo.
     * 
     * @param client Iterfaz del cliente
     * @param dpsEntryPk Llave primaria de la partida (id_year, id_doc, id_ety)
     * @throws Exception Con el mensaje del motivo del rechazo.
     */
    public static void validateDpsEntryAccountingModification(final SClientInterface client, int[] dpsEntryPk) throws Exception {
        SDataDpsEntry entry = (SDataDpsEntry) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS_ETY, dpsEntryPk, SLibConstants.EXEC_MODE_VERBOSE);
        
        if (entry.getIsPrepayment()) {
            throw new Exception("No se puede modificar la contabilización.\n" + 
                                "La partida seleccionada es un Anticipo.");
        }
        
        // Filtrar por el tipo de operación, solo se permiten 11 (Operaciones) y 12 (Operaciones con aplicación de anticipo):
        int opsType = entry.getOperationsType();
        if (opsType != SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS && opsType != SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS_APP_PREPAY) {
            throw new Exception("No se puede modificar la contabilización.\n" +
                                "El tipo de operación de la partida es: " + SDataConstantsSys.OperationsTypesOpsMap.get(opsType) + "\n" +
                                "Solo se admite la modificación en operaciones de tipo: " + 
                                 SDataConstantsSys.OperationsTypesOpsMap.get(SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS) + " ó " +
                                 SDataConstantsSys.OperationsTypesOpsMap.get(SDataConstantsSys.TRNX_OPS_TYPE_OPS_OPS_APP_PREPAY));
        }
        
        // No permitir tipos de detalle Virtual (3) o Sistema (1), solo Ordinario  (2):
        int entryType = entry.getFkDpsEntryTypeId();
        if (entryType == SDataConstantsSys.TRNS_TP_DPS_ETY_SYS || entryType == SDataConstantsSys.TRNS_TP_DPS_ETY_VIRT) {
            String tipoDesc = (entryType == SDataConstantsSys.TRNS_TP_DPS_ETY_SYS) ? "Sistema" : "Virtual";
            throw new Exception("No se puede modificar la contabilización.\n" +
                                "La partida es de tipo '" + tipoDesc + "'.\n" +
                                "Solo se permite la modificación en partidas de tipo 'Ordinario'.");
        }
        
        // No permitir partidas sin valor monetario o cantidad:
        if (entry.getSubtotal_r() == 0 || entry.getQuantity() == 0) {
            throw new Exception("No se puede modificar la contabilización.\n" +
                                "La partida tiene cantidad 0 o subtotal $0.00.");
        }
    }
    
    public static boolean isCreditNote(final SClientInterface client, int[] dpsEntryPk)throws Exception {
        ResultSet resultSet = null;
        String sql = "";
        
        sql = "SELECT * FROM trn_dps AS dps " +
                "WHERE dps.id_year = " + dpsEntryPk[0] + " AND dps.id_doc = " + dpsEntryPk[1] + " AND " +
                "dps.fid_ct_dps in (" + SDataConstantsSys.TRNS_CT_DPS_PUR + "," + SDataConstantsSys.TRNS_CT_DPS_SAL + ") " +
                "AND dps.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_ADJ + " AND dps.fid_tp_dps = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_NA + ";" ;
        
        resultSet = client.getSession().getStatement().executeQuery(sql);
        
        return resultSet.next(); // retorna true si es NC, false si no
        
    }
    
    public static boolean hasLinkedCreditNotes(final SClientInterface client, int[] dpsEntryPk) throws Exception {
        ResultSet resultSet;
        
        String sql = "SELECT a.id_adj_year " +
                "FROM " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_DPS_ADJ) + " AS a " +
                "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS) + " AS d " +
                "ON a.id_adj_year = d.id_year AND a.id_adj_doc = d.id_doc " +
                "WHERE a.id_dps_year = " + dpsEntryPk[0] + " AND a.id_dps_doc = " + dpsEntryPk[1] + 
                " AND d.b_del = 0 AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_ADJ;
        
        // Si es a nivel de partida:
        if (dpsEntryPk.length >= 3 && dpsEntryPk[2] > 0) {
            sql += " AND a.id_dps_ety = " + dpsEntryPk[2];
        }

        resultSet = client.getSession().getStatement().executeQuery(sql);
        
        return resultSet.next();
    }
    
    public static String getAccountForDpsEntry(final SClientInterface client, int[] dpsEntryPk) throws Exception {
        ResultSet resultSet = null;
        String account = "";
        String sql = "";
        String sqlItemFilter = "";
        SDataDpsEntry entry = (SDataDpsEntry) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS_ETY, dpsEntryPk, SLibConstants.EXEC_MODE_VERBOSE);
        
        if (entry.getFkItemRefId_n() == 0) {
            sqlItemFilter = " AND re.fid_item_n = " + entry.getFkItemId() + " AND re.fid_item_aux_n IS NULL";
        }
        else {
            // Si el doc tiene ítem de ref. lo buscamos como el ítem principal en la contabilidad y el ítem principal del doc como el ítem auxiliar. (Se cruzan):
            sqlItemFilter = " AND re.fid_item_n = " + entry.getFkItemRefId_n() + " AND re.fid_item_aux_n = " + entry.getFkItemId();
        }
        
        sql = "SELECT fid_acc " +
                "FROM " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_REC) + " AS dr " +
                "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.FIN_REC) + " AS r ON dr.fid_rec_year = r.id_year AND dr.fid_rec_per = r.id_per AND dr.fid_rec_bkc = r.id_bkc AND dr.fid_rec_tp_rec = r.id_tp_rec AND dr.fid_rec_num = r.id_num " +
                "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.FIN_REC_ETY) + " AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num " +
                "WHERE dr.id_dps_year = " + dpsEntryPk[0] + " AND dr.id_dps_doc = " + dpsEntryPk[1] + 
                " AND re.b_del = 0 AND r.b_del = 0" + sqlItemFilter;
        resultSet = client.getSession().getStatement().executeQuery(sql);
        
        if(!resultSet.next()){
            throw new Exception("No se encontró el registro contable para esta partida.");
        }
        resultSet.beforeFirst();
        
        while (resultSet.next()) {
            account = resultSet.getString("fid_acc");
        }
        
        return account;
    }
    
    public static String getCostCenterForDpsEntry(final SClientInterface client, int[] dpsEntryPk) throws Exception {
        ResultSet resultSet = null;
        String costCenter = "";
        String sql = "";
        String sqlItemFilter = "";
        SDataDpsEntry entry = (SDataDpsEntry) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS_ETY, dpsEntryPk, SLibConstants.EXEC_MODE_VERBOSE);
        /*double unitBaseEquiv = 1; // se quitó porque hay polizas que no heredan la cantidad del documento origen (teoría: las que tienen itemRef)
        
        sql = "SELECT unit_base_equiv AS unitBaseEquiv FROM erp.itmu_unit WHERE id_unit = " + entry.getFkOriginalUnitId();
        resultSet = client.getSession().getStatement().executeQuery(sql);
        
        if (resultSet.next()) {
            unitBaseEquiv = Double.parseDouble(resultSet.getString("unitBaseEquiv"));
        }*/
        
        if (entry.getFkItemRefId_n() == 0) {
            sqlItemFilter = " AND re.fid_item_n = " + entry.getFkItemId() + " AND re.fid_item_aux_n IS NULL";
        }
        // Si el doc tiene ítem de ref. lo buscamos como el ítem principal en la contabilidad y el ítem principal del doc como el ítem auxiliar. (Se cruzan):
        else {
            sqlItemFilter = " AND re.fid_item_n = " + entry.getFkItemRefId_n() + " AND re.fid_item_aux_n = " + entry.getFkItemId();
        }
        
        sql = "SELECT IF(fid_cc_n IS NULL, '', fid_cc_n) AS _cc " +
                "FROM " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_REC) + " AS dr " +
                "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.FIN_REC) + " AS r ON dr.fid_rec_year = r.id_year AND dr.fid_rec_per = r.id_per AND dr.fid_rec_bkc = r.id_bkc AND dr.fid_rec_tp_rec = r.id_tp_rec AND dr.fid_rec_num = r.id_num " +
                "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.FIN_REC_ETY) + " AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num " +
                "WHERE dr.id_dps_year = " + dpsEntryPk[0] + " AND dr.id_dps_doc = " + dpsEntryPk[1] + 
                " AND re.b_del = 0 AND r.b_del = 0" + sqlItemFilter + " AND " +
                //"re.units = " + (entry.getQuantity() / unitBaseEquiv) + " AND " +// se quitó porque hay polizas que no heredan la cantidad del documento origen (teoría: las que tienen itemRef)
                "(re.credit = " + entry.getSubtotal_r() + " OR re.debit = " + entry.getSubtotal_r() + ") ";
        resultSet = client.getSession().getStatement().executeQuery(sql);
        
        if (!resultSet.next()) {
            throw new Exception("No se encontró el Centro de Costo contable.");
        }
        
        resultSet.beforeFirst();
        while (resultSet.next()) {
            costCenter = resultSet.getString("_cc");
        }
        
        return costCenter;
    }
    
    /**
     * Deduce y obtiene la llave primaria y datos extra del renglón contable (fin_rec_ety) 
     * que corresponde a una partida de documento (trn_dps_ety), usando un algoritmo posicional.
     * 
     * @return Object[] { id_year, id_per, id_bkc, id_tp_rec, id_num, id_ety, fid_acc, fid_cc_n }
     */
    public static Object[] getAccountingEntryKeyForDpsEntry(SClientInterface client, SDataDps dps, SDataDpsEntry dpsEty) throws Exception {
        int count = 0; 
        int position = 0;
        
        // Datos del documento con formato para mensaje:
        String docTypeName = SDataReadDescriptions.getCatalogueDescription(client, erp.data.SDataConstants.TRNU_TP_DPS, dps.getDpsTypeKey());
        if (docTypeName == null || docTypeName.trim().isEmpty()) {
            docTypeName = "Documento";
        }
        String folio = dps.getNumberSeries() != null && !dps.getNumberSeries().trim().isEmpty() 
            ? dps.getNumberSeries() + "-" + dps.getNumber() 
            : dps.getNumber();
            
        String docInfo = docTypeName + " " + folio;

        // Deducir posición de la partida
        for (SDataDpsEntry entry : dps.getDbmsDpsEntries()) {
            if (!entry.getIsDeleted() && 
                entry.getFkItemId() == dpsEty.getFkItemId() && 
                entry.getFkItemRefId_n() == dpsEty.getFkItemRefId_n() && 
                SLibUtils.compareAmount(entry.getSubtotal_r(), dpsEty.getSubtotal_r())) {

                count++;
                if (SLibUtils.compareKeys(entry.getPrimaryKey(), dpsEty.getPrimaryKey())) {
                    position = count;
                }
            }
        }

        if (count == 0 || position == 0) {
            throw new Exception("No se pudo determinar la posición de la partida seleccionada dentro del documento (" + docInfo + ").\n\n" +
                                "- Partidas similares encontradas en el documento: " + count + "\n" +
                                "- Posición de la partida actual: " + position + ".");
        }

        String sqlItemFilter = (dpsEty.getFkItemRefId_n() == 0) ?
                "AND re.fid_item_n = " + dpsEty.getFkItemId() + " AND re.fid_item_aux_n IS NULL" :
                "AND re.fid_item_n = " + dpsEty.getFkItemRefId_n() + " AND re.fid_item_aux_n = " + dpsEty.getFkItemId();

        String whereSql = "";
        int[] dpsKey = (int[]) dps.getPrimaryKey();

        if (dps.getFkDpsClassId() == SDataConstantsSys.TRNS_CL_DPS_ADJ) {
            whereSql = "WHERE re.fid_dps_adj_year_n = " + dpsKey[0] + " AND re.fid_dps_adj_doc_n = " + dpsKey[1] + " ";
        } else {
            whereSql = "WHERE re.fid_dps_year_n = " + dpsKey[0] + " AND re.fid_dps_doc_n = " + dpsKey[1] + " " +
                       "AND re.fid_dps_adj_year_n IS NULL AND re.fid_dps_adj_doc_n IS NULL ";
        }

        whereSql += sqlItemFilter + " AND (re.credit = " + dpsEty.getSubtotal_r() + " OR re.debit = " + dpsEty.getSubtotal_r() + ") AND NOT r.b_del AND NOT re.b_del;";

        String sql = "SELECT re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety, re.fid_acc, re.fid_cc_n " +
                     "FROM fin_rec AS r " +
                     "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num " +
                     whereSql;

        java.sql.ResultSet resultSet = client.getSession().getStatement().executeQuery(sql);
        java.util.List<Object[]> entriesRec = new java.util.ArrayList<>();

        while (resultSet.next()) {
            entriesRec.add(new Object[] { 
                resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getString(4), 
                resultSet.getInt(5), resultSet.getInt(6), resultSet.getString(7), resultSet.getString(8)
            });
        }
        resultSet.close();

        if (entriesRec.isEmpty()) {
            throw new Exception("No se puede actualizar la contabilidad de la partida.\n" +
                                "No se encontraron renglones contables en la contabilidad del documento.\n" +
                                "- Partidas similares encontradas en el documento (" + docInfo + "): " + count + ".");
        } else if (count != entriesRec.size()) {
            throw new Exception("No se puede actualizar la contabilidad de la partida.\n" +
                                "Existe una discrepancia de captura entre el documento y su contabilidad.\n" +
                                "- Partidas similares encontradas en el documento (" + docInfo + "): " + count + "\n" + 
                                "- Renglones compatibles encontrados en la póliza: " + entriesRec.size() + ".");
        }

        return entriesRec.get(position - 1);
    }
    
    public static boolean updateAccountCostCenterForDpsEntry(final SClientInterface client, final int[] dpsEntryKey, final String account, final String costCenter, final double originalQuantity, final double subTotal, int registryType, boolean updateNcAccount) throws Exception {
        
        /* 
         * Como no existe un vínculo directo (ID) entre el renglón de la póliza y la partida de la factura,
         * es imposible saber de forma directa qué apunte contable le corresponde a la partida seleccionada.
         * Por ello, el renglón correcto se deduce calculando su "posición" (orden de aparición) 
         * entre las partidas que tienen el mismo ítem y el mismo importe.
         */
        
        SDataDpsEntry dpsEntry = (SDataDpsEntry) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS_ETY, dpsEntryKey, SLibConstants.EXEC_MODE_VERBOSE);
        
        if (dpsEntry == null || dpsEntry.getIsDeleted()) {
            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND + "\nLa partida del documento"
                    + (dpsEntry.getIsDeleted() ? " está eliminada" : "") + " no fue encontrada.");
        }
        
        int count = 0; // número de partidas similares del documneto de la partida deseada.
        int position = 0; // posición de la partida deseada dentro del documento.
        SDataDps dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, dpsEntryKey, SLibConstants.EXEC_MODE_VERBOSE);
        
        for (SDataDpsEntry entry : dps.getDbmsDpsEntries()) {
            if (!entry.getIsDeleted()) {
                if (entry.getFkItemId() == dpsEntry.getFkItemId() && entry.getFkItemRefId_n() == dpsEntry.getFkItemRefId_n() && 
                        //(int) entry.getOriginalQuantity() * 100000000 == (int) dpsEntry.getOriginalQuantity() * 100000000 && // Quitamos cantidad para pólizas que no heredaron el item de la factura 
                        SLibUtils.compareAmount(entry.getSubtotal_r(), dpsEntry.getSubtotal_r())) {
                    count++;

                    // Entre las partidas similares dentro del documento buscamos nuestra partida deseada:
                    if (SLibUtils.compareKeys(entry.getPrimaryKey(), dpsEntry.getPrimaryKey())) {
                        position = count; // obtenemos su posición
                    }
                }
            }
        }
        
        if (count == 0 || position == 0) {
            throw new Exception("No se pudo determinar la posición de la partida seleccionada dentro del documento.\n\n" +
                                "- Partidas similares encontradas en el documento: " + count + "\n" +
                                "- Posición de la partida actual: " + position + ".");
        }
        
        // Hacemos cruce de ítems si la partida tiene ítem de referencia:
        
        String sqlItemFilter = (dpsEntry.getFkItemRefId_n() == 0) ?
                " AND re.fid_item_n = " + dpsEntry.getFkItemId() + " AND re.fid_item_aux_n IS NULL " :
                " AND re.fid_item_n = " + dpsEntry.getFkItemRefId_n() + " AND re.fid_item_aux_n = " + dpsEntry.getFkItemId() + " ";
        
        String whereSql = "";
        switch(registryType){
            case SDataConstantsSys.TRNX_TP_DPS_DOC:
                // Buscamos la póliza del Documento de compras original:
                whereSql = "WHERE re.fid_dps_year_n = " + dpsEntryKey[0] + " AND re.fid_dps_doc_n = " + dpsEntryKey[1] + " " +
                           "AND re.fid_dps_adj_year_n IS NULL AND re.fid_dps_adj_doc_n IS NULL" + 
                           sqlItemFilter +
                           " AND (re.credit = " + subTotal + " OR re.debit = " + subTotal + ") AND NOT r.b_del AND NOT re.b_del;";
                break;
            case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                // Buscamos la póliza de la Nota de Crédito:
                whereSql = "WHERE re.fid_dps_adj_year_n = " + dpsEntryKey[0] + " AND re.fid_dps_adj_doc_n = " + dpsEntryKey[1] + 
                           sqlItemFilter +
                           " AND (re.credit = " + subTotal + " OR re.debit = " + subTotal + ") AND NOT r.b_del AND NOT re.b_del;";
                break;
            default: 
                break;
        }
        
        // Contar renglones similares al renglón de documento deseado:
        
        String sql = "SELECT re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety, re.fid_acc " +
                "FROM fin_rec AS r " +
                "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num " +
                whereSql;
        
        ResultSet resultSet = client.getSession().getStatement().executeQuery(sql);
        
        ArrayList<Object[]> entries = new ArrayList<>();
        
        while (resultSet.next()) {
            entries.add(new Object[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3), resultSet.getString(4), 
                resultSet.getInt(5), resultSet.getInt(6), resultSet.getString(7)
            });
        }
        
        if (entries.isEmpty()) {
            throw new Exception("No se puede actualizar la contabilidad de la partida.\n" +
                                "No se encontraron renglones contables similares en la contabilidad.\n" +
                                "- Partidas similares encontradas en el documento: " + count + ".");
        }
        else if (count != entries.size()) {
            throw new Exception("No se puede actualizar la contabilidad de la partida.\n" +
                                "Existe una discrepancia de captura entre el documento y su contabilidad." +
                                "- Partidas similares encontradas en el documento: " + count + 
                                "- Renglones contables compatibles con la partida deseada del documento: " + entries.size() + ".");
        }
        
        Object[] entryKey = entries.get(position - 1); // el renglón contable que con mucha certeza es el correspondiente al de la partida deseada del docto.
        
        int accountPk = SFinUtils.getAccountId(client.getSession(), account);
        int costCenterPk = SFinUtils.getCostCenterId(client.getSession(), costCenter);
        
        // Metemos el documento a la lista que se mandará a la bitácora:
        
        String accOld = (String) entryKey[6];
        List<int[]> relatedDocs = new ArrayList<>();
        
        relatedDocs.add(new int[] {dpsEntryKey[0], dpsEntryKey[1], dpsEntryKey[2], dps.getFkDpsCategoryId(), dps.getFkDpsClassId(), dps.getFkDpsTypeId()});
        
        // Actualización en cascada para notas de crédito asociadas:
        
        if (registryType == SDataConstantsSys.TRNX_TP_DPS_DOC) {
            // Buscamos las notas de crédito asociadas:
            String sqlNC = "SELECT a.id_adj_year, a.id_adj_doc, a.id_adj_ety, e.qty, e.stot_r " +
                           "FROM " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_DPS_ADJ) + " AS a " +
                           "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS) + " AS d ON a.id_adj_year = d.id_year AND a.id_adj_doc = d.id_doc " +
                           "INNER JOIN " + SDataConstants.TablesMap.get(SDataConstants.TRN_DPS_ETY) + " AS e ON a.id_adj_year = e.id_year AND a.id_adj_doc = e.id_doc AND a.id_adj_ety = e.id_ety " +
                           "WHERE a.id_dps_year = " + dpsEntryKey[0] + " AND a.id_dps_doc = " + dpsEntryKey[1] + " AND a.id_dps_ety = " + dpsEntryKey[2] + " " +
                           "AND d.b_del = 0 AND e.b_del = 0 AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_ADJ + ";";
            
            sqlNC += "";
            
            ResultSet rsNC = client.getSession().getStatement().executeQuery(sqlNC);
            
            ArrayList<Object[]> ncList = new ArrayList<>();
            while (rsNC.next()){
                ncList.add(new Object[] {
                    rsNC.getInt("id_adj_year"), 
                    rsNC.getInt("id_adj_doc"), 
                    rsNC.getInt("id_adj_ety"), 
                    rsNC.getDouble("qty"), 
                    rsNC.getDouble("stot_r")
                });
            }
            
            rsNC.close();
            
            // Recorremos las notas de crédito asociadas guardadas:
            for (Object[] ncData : ncList) {
                int ncYear = (int) ncData[0];
                int ncDoc = (int) ncData[1];
                int ncEty = (int) ncData[2];
                double ncQty = (double) ncData[3];
                double ncSubTotal = (double) ncData[4];
                
                // Si el usuario aceptó cambiar la NC, mandamos la cuenta nueva; si es false, rescatamos la cuenta que ya tenía la NC:
                String targetAccount = updateNcAccount ? account : SFinUtilities.getAccountForDpsEntry(client, new int[] { ncYear, ncDoc, ncEty });
                
                // Le pasamos la NC asociada al mismo método recursivamente:
                updateAccountCostCenterForDpsEntry(client, new int[] { ncYear, ncDoc, ncEty }, targetAccount, costCenter, ncQty, ncSubTotal, SDataConstantsSys.TRNX_TP_DPS_ADJ, false);
            }
        }
        
        // Actualizar el documento:
        
         sql = "UPDATE fin_rec_ety " +
                "SET fid_acc = '" + account + "', fk_acc = " + accountPk + ", " +
                (costCenter == null || costCenter.isEmpty() ? "fid_cc_n = NULL, fk_cc_n = NULL" : "fid_cc_n = '" + costCenter + "', fk_cc_n = " + costCenterPk) + ", " +
                "fid_usr_edit = " + client.getSession().getUser().getPkUserId() + ", ts_edit = NOW() " +
                "WHERE id_year = " + entryKey[0] + " AND id_per = " + entryKey[1] + " AND id_bkc = " + entryKey[2] + " AND id_tp_rec = '" + entryKey[3] + "' AND id_num = " + entryKey[4] + " AND id_ety = " + entryKey[5] + ";";
        client.getSession().getStatement().execute(sql);
        
        sql = "UPDATE trn_dps_ety " +
                "SET " + (costCenter == null || costCenter.isEmpty() ? "fid_cc_n = NULL " : "fid_cc_n = '" + costCenter + "' ") + ", " +
                "fid_usr_edit = " + client.getSession().getUser().getPkUserId() + ", " + "ts_edit = NOW() " +
                "WHERE id_year = " + dpsEntryKey[0] + " AND id_doc = " + dpsEntryKey[1] + " AND id_ety = " + dpsEntryKey[2] + ";";
        client.getSession().getStatement().execute(sql);
        
        // Guardar los cambios en la bitácora:
                
        STrnUtilities.insertHistForAccounting(client.getSession().getStatement().getConnection(), client.getSession().getUser().getPkUserId(), 
            dpsEntry, accOld, account, costCenter, relatedDocs
        );
        
        return true;
    }
    
    public static boolean isSysMovementNatureDebtor(final int[] keySystemMovementType, final boolean isDocumentAvailable) {
        return SLibUtils.compareKeys(keySystemMovementType, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_DBR) ||
                (SLibUtils.compareKeys(keySystemMovementType, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS) && isDocumentAvailable) ||
                (SLibUtils.compareKeys(keySystemMovementType, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP) && !isDocumentAvailable);
    }
    
    public static boolean isSysMovementNatureCreditor(final int[] keySystemMovementType, final boolean isDocumentAvailable) {
        return SLibUtils.compareKeys(keySystemMovementType, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CDR) ||
                (SLibUtils.compareKeys(keySystemMovementType, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP) && isDocumentAvailable) ||
                (SLibUtils.compareKeys(keySystemMovementType, SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS) && !isDocumentAvailable);
    }
    
    public static boolean isSysMovementCashAccount(final int[] keySystemMovementType) {
        return SLibUtils.belongsTo(keySystemMovementType, new int[][] { 
            SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH, 
            SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK });
    }
    
    public static boolean isSysMovementBizPartner(final int[] keySystemMovementType) {
        return SLibUtils.belongsTo(keySystemMovementType, new int[][] { 
            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP, 
            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS, 
            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CDR, 
            SDataConstantsSys.FINS_TP_SYS_MOV_BPS_DBR });
    }
    
    /**
     * Gets business partner balance in local currency.
     * @param client
     * @param idBizPartner ID of business partner.
     * @param idBizPartnerCategory ID of business partner category.
     * @param keyDpsToExclude PK of document to exclude from query if necessary, can be null.
     * @return Business partner balance in local currency.
     */
    public static double getBizPartnerBalance(final SClientInterface client, final int idBizPartner, final int idBizPartnerCategory, final int[] keyDpsToExclude) {
        Vector<Object> in = null;
        Vector<Object> out = null;

        in = new Vector<>();
        in.add(idBizPartner);
        in.add(idBizPartnerCategory);
        in.add(keyDpsToExclude == null ? client.getSession().getCurrentYear() : keyDpsToExclude[0]);
        in.add(keyDpsToExclude == null ? 0 : keyDpsToExclude[1]);
        out = SDataUtilities.callProcedure(client, SProcConstants.FIN_GET_BP_BAL, in, SLibConstants.EXEC_MODE_VERBOSE);
        
        return ((Double) out.get(0));
    }
    
    public static Balance[] getBizPartnerBalances(final SClientInterface client, final int idBizPartner, final int idBizPartnerCategory, final Date cutoff) throws Exception {
        ArrayList<Balance> balances = new ArrayList<>();
        int[] sysMoveTypeKey = null;
        
        switch (idBizPartnerCategory) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
                sysMoveTypeKey = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP;
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
                sysMoveTypeKey = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS;
                break;
            default:
                // nothing
        }
        
        String sql = "SELECT "
                + "b.id_bp, b.bp, c.id_cur, c.cur_key, "
                + "SUM(IF (re.fid_dps_doc_n is null, re.debit - re.credit, 0.0)) as f_adv, "
                + "SUM(IF (re.fid_dps_doc_n is not null, re.debit - re.credit, 0.0)) as f_bal, "
                + "SUM(re.debit - re.credit) AS f_bal_net, "
                + "SUM(IF (re.fid_dps_doc_n is null, re.debit_cur - re.credit_cur, 0.0)) as f_cur_adv, "
                + "SUM(IF (re.fid_dps_doc_n is not null, re.debit_cur - re.credit_cur, 0.0)) as f_cur_bal, "
                + "SUM(re.debit_cur - re.credit_cur) AS f_cur_bal_net "
                + "FROM "
                + "fin_rec AS r INNER JOIN fin_rec_ety AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "INNER JOIN erp.cfgu_cur AS c ON c.id_cur = re.fid_cur "
                + "INNER JOIN erp.bpsu_bp AS b ON re.fid_bp_nr = b.id_bp "
                + "WHERE "
                + "r.id_year = " + SLibTimeUtils.digestYear(cutoff)[0] + " AND r.dt <= '" + SLibUtils.DbmsDateFormatDate.format(cutoff) + "' AND NOT r.b_del AND NOT re.b_del "
                + "AND re.fid_ct_sys_mov_xxx = " + sysMoveTypeKey[0] + " AND re.fid_tp_sys_mov_xxx = " + sysMoveTypeKey[1] + " "
                + "AND re.fid_bp_nr = " + idBizPartner + " "
                + "GROUP BY b.id_bp, b.bp, c.id_cur, c.cur_key "
                + "HAVING f_bal_net <> 0 "
                + "ORDER BY b.bp, b.id_bp, c.id_cur, c.cur_key;";
        
        try (Statement statement = client.getSession().getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            
            while (resultSet.next()) {
                balances.add(new Balance(client,
                        resultSet.getDouble("f_adv"), resultSet.getDouble("f_bal"), resultSet.getDouble("f_bal_net"),
                        resultSet.getDouble("f_cur_adv"), resultSet.getDouble("f_cur_bal"), resultSet.getDouble("f_cur_bal_net"),
                        resultSet.getInt("c.id_cur"), resultSet.getString("c.cur_key")));
            }
        }
        
        return balances.toArray(new Balance[0]);
    }
    
    public static boolean finYearExists(final SClientInterface client, int pkYearId) throws SQLException {
        String mySql = "SELECT * FROM fin_year WHERE id_year = " + pkYearId + " AND NOT b_closed AND NOT b_del;";
        Statement statement = client.getSession().getStatement().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(mySql);
        return resultSet.next();
    }
    
    public static class Balance implements Serializable {
        
        public double LocAdvance;
        public double LocBalance;
        public double LocNetBalance;
        public double CurAdvance;
        public double CurBalance;
        public double CurNetBalance;
        public int CurCurrencyId;
        public String CurCurrencyCode;
        public boolean IsLocCurrency;
        public String LocCurrencyCode;
        
        public Balance(final SClientInterface client,
                final double locAdvance, final double locBalance, final double locNetBalance,
                final double curAdvance, final double curBalance, final double curNetBalance,
                final int curCurrencyId, final String curCurrencyCode) {
            LocAdvance = locAdvance;
            LocBalance = locBalance;
            LocNetBalance = locNetBalance;
            CurAdvance = curAdvance;
            CurBalance = curBalance;
            CurNetBalance = curNetBalance;
            CurCurrencyId = curCurrencyId;
            CurCurrencyCode = curCurrencyCode;
            IsLocCurrency = ((SGuiClient) client).getSession().getSessionCustom().isLocalCurrency(new int[] { CurCurrencyId });
            LocCurrencyCode = ((SGuiClient) client).getSession().getSessionCustom().getLocalCurrencyCode();
        }
        
        public String getAdvanceAsString() {
            String string = "";
            
            if (CurAdvance != 0 || LocAdvance != 0) {
                string = "$" + SLibUtils.getDecimalFormatAmount().format(CurAdvance) + " " + CurCurrencyCode;
                if (!IsLocCurrency) {
                    string += " = $" + SLibUtils.getDecimalFormatAmount().format(LocAdvance) + " " + LocCurrencyCode;
                }
            }
            
            return string;
        }
    }
}
