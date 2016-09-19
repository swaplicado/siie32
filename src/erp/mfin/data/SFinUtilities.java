/*
 * Copyright © Software Aplicado SA de CV. All rights reserverd.
 */
package erp.mfin.data;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mod.SModSysConsts;
import erp.mod.fin.db.SDbAccount;
import erp.mod.fin.db.SFinUtils;
import erp.mod.fin.db.SLayoutBankPaymentTxt;
import erp.mod.fin.db.SLayoutBankRow;
import erp.mtrn.data.SDataDpsEntry;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.JFileChooser;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Juan Barajas
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
            if (values.getBizPartnerId() == nBizPartnerId && values.getAccountCredit().compareTo(accountCredit) == 0) {
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
    
    public static void writeLayout(SClientInterface client, String layout, java.lang.String title) {
        String fileName = "";
        BufferedWriter bw = null;
        
        client.getFileChooser().setSelectedFile(new File("Archivo Layout.txt"));
        if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            fileName = client.getFileChooser().getSelectedFile().getAbsolutePath();
            client.getFileChooser().setSelectedFile(new File(client.getSessionXXX().getFormatters().getFileNameDatetimeFormat().format(new java.util.Date()) + " " + title + ".txt"));
            
            File file = new File(fileName.endsWith(".txt") ? fileName : fileName + ".txt");

            try {
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ASCII"));
                
                bw.write(layout);
                bw.close();
            }
            catch (java.lang.Exception e) {
                SLibUtilities.renderException(SFinUtilities.class.getName(), e);
            }
        }
    }

    public static String getFileNameLayout(SGuiSession session, int layoutId) throws Exception {
        String fileName = "";
        String sql = "";
        ResultSet resulSet = null;

        sql = "SELECT file_name " +
               "FROM erp.finu_tp_lay_bank " +
               "WHERE b_del = 0 AND id_tp_lay_bank = " + layoutId;

        resulSet = session.getStatement().executeQuery(sql);
        if (resulSet.next()) {
            fileName = resulSet.getString("file_name");
        }
        
        return fileName;
    }
    
    public static String getBizPartnerForBanamex(SGuiSession session, int bizPartnerId) throws Exception {
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

        resulSet = session.getStatement().executeQuery(sql);
        if (resulSet.next()) {
            if (resulSet.getInt("fid_tp_bp_idy") == SModSysConsts.BPSS_TP_BP_IDY_ORG) {
                firstname = resulSet.getString("bp");
                bizPartner = "," + SLibUtilities.textToAlphanumeric(firstname) + "/";
            }
            else {
                firstname = resulSet.getString("firstname");
                lastname = resulSet.getString("lastname");
                fatherLastname = lastname.substring(0, lastname.indexOf(" "));
                motherLastname = lastname.substring(fatherLastname.length() + 1);
                bizPartner = SLibUtilities.textToAlphanumeric(firstname) + "," + SLibUtilities.textToAlphanumeric(fatherLastname) + "/" + SLibUtilities.textToAlphanumeric(motherLastname);
            }
        }
        return bizPartner;
    }
    
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
                sAccountCredit = SLibUtilities.textTrim(values.getAccountCredit());
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
                layout += values.getCf();
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
    
    public static java.lang.String createLayoutHsbcThird(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title) {
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
            for (SLayoutBankPaymentTxt payment : payments) {

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
                sAccountCredit = SLibUtilities.textTrim(values.getAccountCredit());
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
                //layout += values.getBizPartnerCreditFiscalId().concat(SLibUtilities.textRepeat(" ", (18 - values.getBizPartnerCreditFiscalId().length()))); XXX No required is optional (jbarajas)
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
    
    public static java.lang.String createLayoutHsbcTef(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title, Date date) {
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
            layout += "MXN"; // Currency
            layout += "1".concat(SLibUtilities.textRepeat(" ", 39)); // Number lot

            layout += "\r\n";
            
            for (SLayoutBankPaymentTxt payment : payments) {

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
                sAccountCredit = SLibUtilities.textTrim(values.getAccountCredit());
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
    
    public static java.lang.String createLayoutHsbcSpeiFdN(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title) {
        int n = 0;
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sBizPartner = "";
        java.lang.String sReference = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000000000.00");
        mdBalanceTot = 0;

        try {
            for (SLayoutBankPaymentTxt payment : payments) {

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
                sAccountCredit = SLibUtilities.textTrim(values.getAccountCredit());
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
                layout += values.getBizPartnerCreditFiscalId().concat(SLibUtilities.textRepeat(" ", (18 - values.getBizPartnerCreditFiscalId().length())));
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
    
    public static java.lang.String createLayoutHsbcSpeiFdY(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title) {
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sConcept = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000000000.00");
        mdBalanceTot = 0;

        try {
            for (SLayoutBankPaymentTxt payment : payments) {

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
                sAccountCredit = SLibUtilities.textTrim(values.getAccountCredit());
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
                layout += formatDate.format(values.getDate());

                layout += "\r\n";
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutSantanderThird(ArrayList<SLayoutBankPaymentTxt> payments, Date date, java.lang.String title) {
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sConcept = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000.00");
        SimpleDateFormat formatDate = new SimpleDateFormat("ddMMyyyy");
        mdBalanceTot = 0;

        try {
            for (SLayoutBankPaymentTxt payment : payments) {

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
                sAccountCredit = SLibUtilities.textTrim(values.getAccountCredit());
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
    
    public static java.lang.String createLayoutSantanderTef(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title) {
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
            for (SLayoutBankPaymentTxt payment : payments) {

                sBizPartner = SLibUtilities.textToAlphanumeric(payment.getBizPartner());
                sBizPartnerSantanderCode = SLibUtilities.textToAlphanumeric(payment.getSantanderBankCode());
                sAccountDebit = SLibUtilities.textTrim(payment.getAccountDebit());
                sAccountCredit = SLibUtilities.textTrim(payment.getAccountCredit());
                sConcept = SLibUtilities.textToAlphanumeric(payment.getConcept());
                sReference = SLibUtilities.textToAlphanumeric(payment.getReference());
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
    
    public static java.lang.String createLayoutSantanderSpeiFdNOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public static java.lang.String createLayoutSantanderSpeiFdN(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public static java.lang.String createLayoutSantanderSpeiFdYOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title) {
        return createLayoutSantanderTefOld(client, vRows, title);
    }
    
    public static java.lang.String createLayoutSantanderSpeiFdY(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title) {
        return createLayoutSantanderTef(payments, title);
    }
    
    public static java.lang.String createLayoutBanBajioOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title, Date date, Date dateApplication, int consecutiveDay, String typeLay, String typeAccountCredit) {
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
                sAccountCredit = SLibUtilities.textTrim(values.getAccountCredit());
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
                layout += formatDescTotal.format(mdBalanceTotal).replace(".", "");;
            }
            layout += "\r\n";
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(SFinUtilities.class.getName(), e);
        }
        return layout;
    }
    
    public static java.lang.String createLayoutBanBajio(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title, Date date, Date dateApplication, int consecutiveDay, String typeLay, String typeAccountCredit) {
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

            for (SLayoutBankPaymentTxt payment : payments) {

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
                layout += formatDescTotal.format(mdBalanceTotal).replace(".", "");;
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
    
    public static java.lang.String createLayoutBanBajioThird(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title, Date date, int consecutiveDay) {
        return createLayoutBanBajio(payments, title, date, date, consecutiveDay, TXT_TYPE_LAY_BANBAJIO_LOCAL, TXT_TYPE_ACCOUNT_CHECK);
    }
    
    public static java.lang.String createLayoutBanBajioTefOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title, Date date, int consecutiveDay) {
        return createLayoutBanBajioOld(client, vRows, title, date, date, consecutiveDay, TXT_TYPE_LAY_BANBAJIO_TEF, TXT_TYPE_ACCOUNT_CLABE);
    }
    
    public static java.lang.String createLayoutBanBajioTef(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title, Date date, int consecutiveDay) {
        return createLayoutBanBajio(payments, title, date, date, consecutiveDay, TXT_TYPE_LAY_BANBAJIO_TEF, TXT_TYPE_ACCOUNT_CLABE);
    }
    
    public static java.lang.String createLayoutBanBajioSpeiFdNOld(erp.client.SClientInterface client, Vector<SLayoutBankRow> vRows, java.lang.String title, Date date, int consecutiveDay) {
        return createLayoutBanBajioOld(client, vRows, title, date, date, consecutiveDay, TXT_TYPE_LAY_BANBAJIO_SPEI, TXT_TYPE_ACCOUNT_CLABE);
    }
    
    public static java.lang.String createLayoutBanBajioSpeiFdN(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title, Date date, int consecutiveDay) {
        return createLayoutBanBajio(payments, title, date, date, consecutiveDay, TXT_TYPE_LAY_BANBAJIO_SPEI, TXT_TYPE_ACCOUNT_CLABE);
    }
    
    public static java.lang.String createLayoutBbvaThird(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title) {
        java.lang.String sReference = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000000.00");
        mdBalanceTot = 0;
        mdTaxChargedTot = 0;
        mbIsRepeated = false;

        try {
            for (SLayoutBankPaymentTxt payment : payments) {

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
    
    private static java.lang.String createLayoutBbvaInterbank(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title, String availability) {
        int n = 0;
        java.lang.String sBizPartner = "";
        java.lang.String sAccountDebit = "";
        java.lang.String sAccountCredit = "";
        java.lang.String sReference = "";
        java.lang.String layout = "";
        DecimalFormat formatDesc = new DecimalFormat("0000000000000.00");
        mdBalanceTot = 0;

        try {
            for (SLayoutBankPaymentTxt payment : payments) {

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
    
    public static java.lang.String createLayoutBbvaTef(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title) {
        return createLayoutBbvaInterbank(payments, title, TXT_TYPE_LAY_BBVA_TEF);
    }
    
    public static java.lang.String createLayoutBbvaSpei(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title) {
        return createLayoutBbvaInterbank(payments, title, TXT_TYPE_LAY_BBVA_SPEI);
    }
    
    public static java.lang.String createLayoutBanamexThird(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title) {
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
            for (SLayoutBankPaymentTxt payment : payments) {

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
    
    private static java.lang.String createLayoutBanamexInterbank(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title, String lapse, SGuiSession session) {
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
            for (SLayoutBankPaymentTxt payment : payments) {
                n = (int) (Math.floor(Math.log10(payment.getBankKey())) + 1);

                sDescription = SLibUtilities.textToAlphanumeric(payment.getReference());
                sAccountDebit = SLibUtilities.textTrim(payment.getAccountDebit());
                sAccountBranchDebit = SLibUtilities.textTrim(payment.getAccountBranchDebit());
                sAccountCredit = SLibUtilities.textTrim(payment.getAccountCredit());
                sBizPartner = getBizPartnerForBanamex(session, payment.getBizPartnerId());
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
    
    public static java.lang.String createLayoutBanamexTef(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title, SGuiSession session) {
        return createLayoutBanamexInterbank(payments, title, TXT_TERM_BMX_NEXT_DAY, session);
    }
    
    public static java.lang.String createLayoutBanamexSpei(ArrayList<SLayoutBankPaymentTxt> payments, java.lang.String title, SGuiSession session) {
        return createLayoutBanamexInterbank(payments, title, TXT_TERM_BMX_SAME_DAY, session);
    }
    
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
            maDataBase = new ArrayList<String>();
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
    
    public static String getAccountForDpsEntry(final SClientInterface client, int[] dpsEntryPk) throws Exception {
        ResultSet resultSet = null;
        String account = "";
        String sql = "";
        SDataDpsEntry entry = (SDataDpsEntry) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS_ETY, dpsEntryPk, SLibConstants.EXEC_MODE_VERBOSE);
        
        sql = "SELECT fid_acc " +
                "FROM trn_dps_rec AS dr " +
                "INNER JOIN fin_rec AS r ON dr.fid_rec_year = r.id_year AND dr.fid_rec_per = r.id_per AND dr.fid_rec_bkc = r.id_bkc AND dr.fid_rec_tp_rec = r.id_tp_rec AND dr.fid_rec_num = r.id_num " +
                "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num " +
                "WHERE dr.id_dps_year = " + dpsEntryPk[0] + " AND dr.id_dps_doc = " + dpsEntryPk[1] + " AND re.b_del = 0 AND re.fid_item_n = " + entry.getFkItemId();
        resultSet = client.getSession().getStatement().executeQuery(sql);
        
        while (resultSet.next()) {
            account = resultSet.getString("fid_acc");
        }
        
        return account;
    }
    
    public static String getCostCenterForDpsEntry(final SClientInterface client, int[] dpsEntryPk) throws Exception {
        ResultSet resultSet = null;
        String costCenter = "";
        String sql = "";
        SDataDpsEntry entry = (SDataDpsEntry) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS_ETY, dpsEntryPk, SLibConstants.EXEC_MODE_VERBOSE);
        
        sql = "SELECT IF(fid_cc_n IS NULL, '', fid_cc_n) AS _cc " +
                "FROM trn_dps_rec AS dr " +
                "INNER JOIN fin_rec AS r ON dr.fid_rec_year = r.id_year AND dr.fid_rec_per = r.id_per AND dr.fid_rec_bkc = r.id_bkc AND dr.fid_rec_tp_rec = r.id_tp_rec AND dr.fid_rec_num = r.id_num " +
                "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num " +
                "WHERE dr.id_dps_year = " + dpsEntryPk[0] + " AND dr.id_dps_doc = " + dpsEntryPk[1] + " AND re.b_del = 0 AND re.fid_item_n = " + entry.getFkItemId();
        resultSet = client.getSession().getStatement().executeQuery(sql);
        
        while (resultSet.next()) {
            costCenter = resultSet.getString("_cc");
        }
        
        return costCenter;
    }
    
    public static boolean updateAccountCostCenterForDpsEntry(final SClientInterface client, int[] dpsEntryPk, String account, String costCenter) throws Exception {
        int accountPk = 0;
        int costCenterPk = 0;
        ResultSet resultSet = null;
        String sql = "";
        Object[] key = null;
        SDataDpsEntry entry = null;
        
        entry = (SDataDpsEntry) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS_ETY, dpsEntryPk, SLibConstants.EXEC_MODE_VERBOSE);
        accountPk = SFinUtils.getAccountId(client.getSession(), account);
        costCenterPk = SFinUtils.getCostCenterId(client.getSession(), costCenter);
        
        sql = "SELECT re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety " +
                "FROM trn_dps_rec AS dr " +
                "INNER JOIN fin_rec AS r ON dr.fid_rec_year = r.id_year AND dr.fid_rec_per = r.id_per AND dr.fid_rec_bkc = r.id_bkc AND dr.fid_rec_tp_rec = r.id_tp_rec AND dr.fid_rec_num = r.id_num " +
                "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num " +
                "WHERE dr.id_dps_year = " + dpsEntryPk[0] + " AND dr.id_dps_doc = " + dpsEntryPk[1] + " AND re.b_del = 0 AND re.fid_item_n = " + entry.getFkItemId();
        resultSet = client.getSession().getStatement().executeQuery(sql);
        
        while (resultSet.next()) {
            key = new Object[6];
            
            key[0] = resultSet.getInt("re.id_year");
            key[1] = resultSet.getInt("re.id_per"); 
            key[2] = resultSet.getInt("re.id_bkc"); 
            key[3] = resultSet.getString("re.id_tp_rec"); 
            key[4] = resultSet.getInt("re.id_num"); 
            key[5] = resultSet.getInt("re.id_ety");
        }
        
        sql = "UPDATE fin_rec_ety SET fid_acc = '" + account + "', fk_acc = " + accountPk + ", " +
                (costCenter.isEmpty() ? "fid_cc_n = NULL, fk_cc_n = 0" : "fid_cc_n = '" + costCenter + "', fk_cc_n = " + costCenterPk) + ", " +
                "fid_usr_edit = " + client.getSession().getUser().getPkUserId() + ", ts_edit = NOW() " +
                "WHERE id_year = " + key[0] + " AND id_per = " + key[1] + " AND id_bkc = " + key[2] + " AND id_tp_rec = '" + key[3] + "' AND id_num = " + key[4] + " AND " +
                "id_ety = " + key[5] + ";";
        client.getSession().getStatement().execute(sql);
        
        sql = "UPDATE trn_dps_ety " +
                "SET " + (costCenter.isEmpty() ? "fid_cc_n = NULL " : "fid_cc_n = '" + costCenter + "' ") +
                "WHERE id_year = " + dpsEntryPk[0] + " AND id_doc = " + dpsEntryPk[1] + " AND id_ety = " + dpsEntryPk[2] + ";";
        client.getSession().getStatement().execute(sql);
        
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
}
