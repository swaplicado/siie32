/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mhrs.data.SDataFormerPayrollEmp;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.hrs.db.SDbConfig;
import erp.mod.hrs.db.SDbPayroll;
import erp.mod.hrs.db.SDbPayrollReceipt;
import erp.mod.hrs.db.SDbPayrollReceiptIssue;
import erp.mod.hrs.db.SHrsCfdUtils;
import erp.mod.hrs.db.SHrsUtils;
import erp.mod.trn.db.STrnUtils;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.STrnUtilities;
import erp.print.SDataConstantsPrint;
import java.awt.Cursor;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;
import sa.lib.gui.bean.SBeanFormDialog;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores
 */
public class SDialogCfdProcessing extends SBeanFormDialog {
    
    protected ArrayList<SDataCfd> maCfds;
    protected ArrayList<int[]> maPayrollReceiptKeys;
    protected ArrayList<SDbPayrollReceipt> maPayrollReceipts;
    protected int mnStampsAvailable;
    protected Date mtAnnulmentDate;
    protected boolean mbValidateStamp;
    
    protected boolean mbFirstTime;
    protected final boolean mbIsCfdiSendingAutomaticHrs;
    protected int mnPayrollCfdVersion;
    protected int mnNumberCopies;
    protected int mnDpsAnnulType;
    protected String msAnnulReason;
    protected String msAnnulRelatedUuid;
    protected boolean mbRetryCancel;
    
    /**
     * Creates new form SDialogResult
     * @param client
     * @param title
     * @param subtype
     */
    public SDialogCfdProcessing(SGuiClient client, String title, int subtype) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.TRN_CFD, subtype, title);
        initComponents();
        initComponentsCustom();
        mbIsCfdiSendingAutomaticHrs = ((SClientInterface) miClient).getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticHrs();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jpInformation = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlCfdToProcess = new javax.swing.JLabel();
        moIntCfdToProcess = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel21 = new javax.swing.JPanel();
        jlCfdProcessedOk = new javax.swing.JLabel();
        moIntCfdProcessedOk = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel22 = new javax.swing.JPanel();
        jlCfdProcessedWrong = new javax.swing.JLabel();
        moIntCfdProcessedWrong = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel4 = new javax.swing.JPanel();
        jlCfdProcessed = new javax.swing.JLabel();
        moIntCfdProcessed = new sa.lib.gui.bean.SBeanFieldInteger();
        jpStampInfo = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jlStampsAvailable = new javax.swing.JLabel();
        moIntStampsAvailable = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel24 = new javax.swing.JPanel();
        jlStampsConsumed = new javax.swing.JLabel();
        moIntStampsConsumed = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel25 = new javax.swing.JPanel();
        jlStampsRemaining = new javax.swing.JLabel();
        moIntStampsRemaining = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel8 = new javax.swing.JPanel();
        jtfWarningMessage = new javax.swing.JTextField();
        jpDetail = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jspMessages = new javax.swing.JScrollPane();
        jtaMessages = new javax.swing.JTextArea();

        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout(0, 5));

        jpInformation.setBorder(javax.swing.BorderFactory.createTitledBorder("Resumen:"));
        jpInformation.setLayout(new java.awt.GridLayout(1, 2, 0, 5));

        jPanel5.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdToProcess.setText("CFDI a procesar:");
        jlCfdToProcess.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel6.add(jlCfdToProcess);

        moIntCfdToProcess.setEditable(false);
        jPanel6.add(moIntCfdToProcess);

        jPanel5.add(jPanel6);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdProcessedOk.setText("Procesados correctamente:");
        jlCfdProcessedOk.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel21.add(jlCfdProcessedOk);

        moIntCfdProcessedOk.setEditable(false);
        jPanel21.add(moIntCfdProcessedOk);

        jPanel5.add(jPanel21);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdProcessedWrong.setText("Procesados incorrectamente:");
        jlCfdProcessedWrong.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel22.add(jlCfdProcessedWrong);

        moIntCfdProcessedWrong.setEditable(false);
        jPanel22.add(moIntCfdProcessedWrong);

        jPanel5.add(jPanel22);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdProcessed.setText("CFDI procesados:");
        jlCfdProcessed.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel4.add(jlCfdProcessed);

        moIntCfdProcessed.setEditable(false);
        jPanel4.add(moIntCfdProcessed);

        jPanel5.add(jPanel4);

        jpInformation.add(jPanel5);

        jpStampInfo.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlStampsAvailable.setText("Timbres disponibles:");
        jlStampsAvailable.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel7.add(jlStampsAvailable);

        moIntStampsAvailable.setEditable(false);
        jPanel7.add(moIntStampsAvailable);

        jpStampInfo.add(jPanel7);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlStampsConsumed.setText("Timbres consumidos:");
        jlStampsConsumed.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel24.add(jlStampsConsumed);

        moIntStampsConsumed.setEditable(false);
        jPanel24.add(moIntStampsConsumed);

        jpStampInfo.add(jPanel24);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlStampsRemaining.setText("Timbres restantes:");
        jlStampsRemaining.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel25.add(jlStampsRemaining);

        moIntStampsRemaining.setEditable(false);
        jPanel25.add(moIntStampsRemaining);

        jpStampInfo.add(jPanel25);

        jPanel8.setLayout(new java.awt.GridLayout(1, 0));

        jtfWarningMessage.setEditable(false);
        jtfWarningMessage.setFocusable(false);
        jPanel8.add(jtfWarningMessage);

        jpStampInfo.add(jPanel8);

        jpInformation.add(jpStampInfo);

        jPanel1.add(jpInformation, java.awt.BorderLayout.NORTH);

        jpDetail.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle:"));
        jpDetail.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        jspMessages.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jspMessages.setAutoscrolls(true);

        jtaMessages.setEditable(false);
        jtaMessages.setColumns(20);
        jtaMessages.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        jtaMessages.setRows(5);
        jtaMessages.setFocusable(false);
        jtaMessages.setOpaque(false);
        jspMessages.setViewportView(jtaMessages);

        jPanel2.add(jspMessages, java.awt.BorderLayout.CENTER);

        jpDetail.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.add(jpDetail, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowsActivated();
    }//GEN-LAST:event_formWindowActivated

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel jlCfdProcessed;
    private javax.swing.JLabel jlCfdProcessedOk;
    private javax.swing.JLabel jlCfdProcessedWrong;
    private javax.swing.JLabel jlCfdToProcess;
    private javax.swing.JLabel jlStampsAvailable;
    private javax.swing.JLabel jlStampsConsumed;
    private javax.swing.JLabel jlStampsRemaining;
    private javax.swing.JPanel jpDetail;
    private javax.swing.JPanel jpInformation;
    private javax.swing.JPanel jpStampInfo;
    private javax.swing.JScrollPane jspMessages;
    private javax.swing.JTextArea jtaMessages;
    private javax.swing.JTextField jtfWarningMessage;
    private sa.lib.gui.bean.SBeanFieldInteger moIntCfdProcessed;
    private sa.lib.gui.bean.SBeanFieldInteger moIntCfdProcessedOk;
    private sa.lib.gui.bean.SBeanFieldInteger moIntCfdProcessedWrong;
    private sa.lib.gui.bean.SBeanFieldInteger moIntCfdToProcess;
    private sa.lib.gui.bean.SBeanFieldInteger moIntStampsAvailable;
    private sa.lib.gui.bean.SBeanFieldInteger moIntStampsConsumed;
    private sa.lib.gui.bean.SBeanFieldInteger moIntStampsRemaining;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods:
     */
    
    private void windowsActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            
            try {
                miClient.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
                process();
            }
            catch (Exception e) {
                SLibUtils.printException(this, e);
            }
            finally {
                miClient.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }
    
    private void initComponentsCustom() {
        SGuiUtils.setWindowBounds(this, 720, 450);

        jbSave.setEnabled(false);
        jbCancel.setText("Aceptar");
        
        moIntCfdToProcess.setIntegerSettings(SGuiUtils.getLabelName(jlCfdToProcess), SGuiConsts.GUI_TYPE_INT, true);
        moIntCfdProcessedOk.setIntegerSettings(SGuiUtils.getLabelName(jlCfdProcessedOk), SGuiConsts.GUI_TYPE_INT, true);
        moIntCfdProcessedWrong.setIntegerSettings(SGuiUtils.getLabelName(jlCfdProcessedWrong), SGuiConsts.GUI_TYPE_INT, true);
        moIntCfdProcessed.setIntegerSettings(SGuiUtils.getLabelName(jlCfdProcessed), SGuiConsts.GUI_TYPE_INT, true);
        moIntStampsAvailable.setIntegerSettings(SGuiUtils.getLabelName(jlStampsAvailable), SGuiConsts.GUI_TYPE_INT, true);
        moIntStampsConsumed.setIntegerSettings(SGuiUtils.getLabelName(jlStampsConsumed), SGuiConsts.GUI_TYPE_INT, true);
        moIntStampsRemaining.setIntegerSettings(SGuiUtils.getLabelName(jlStampsRemaining), SGuiConsts.GUI_TYPE_INT, true);

        moFields.addField(moIntCfdToProcess);
        moFields.addField(moIntCfdProcessedOk);
        moFields.addField(moIntCfdProcessedWrong);
        moFields.addField(moIntCfdProcessed);
        moFields.addField(moIntStampsAvailable);
        moFields.addField(moIntStampsConsumed);
        moFields.addField(moIntStampsRemaining);

        moFields.setFormButton(jbSave);
    }
    
    private void process() throws Exception {
        jtfWarningMessage.setText("");
        
        if (mnFormSubtype == SCfdConsts.REQ_SEND_PAYROLL) {
            sendPayrollReceipts();
        }
        else { 
            if (maPayrollReceiptKeys != null) {
                processPayrollReceipts();
            }
            else if (maCfds != null) {
                processCfds();            
            }
        }
    }
    
    /**
     * Send simple payroll receipts, that is, not as CFD.
     * @throws Exception 
     */
    private void sendPayrollReceipts() throws Exception {
        if (maPayrollReceipts != null) {
            int cfdProcessed = 0;
            int cfdProcessedOk = 0;
            int cfdProcessedWrong = 0;
            String detailMessage = "";

            moIntCfdToProcess.setValue(maPayrollReceipts.size());
            jtfWarningMessage.setText("");
            
            try {
                SCfdUtils.initDataSetForPayroll(mnFormSubtype);
                
                if (!maPayrollReceipts.isEmpty()) {
                    SDbPayroll payroll = (SDbPayroll) miClient.getSession().readRegistry(SModConsts.HRS_PAY, new int[] { maPayrollReceipts.get(0).getPkPayrollId() });
                    SCfdUtils.DataSet.put(SModConsts.HRS_PAY, payroll); // payroll registry will be used in method SHrsUtils.createPayrollReceiptMap()
                }

                for (SDbPayrollReceipt payrollReceipt : maPayrollReceipts) {
                    SDataBizPartner bizPartner = (SDataBizPartner) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.BPSU_BP, new int[] { payrollReceipt.getPkEmployeeId() }, SLibConstants.EXEC_MODE_SILENT);
                    String recipient = bizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContacts().get(0).getEmail01();
                    HashMap<String, Object> map = SHrsUtils.createPayrollReceiptMap((SGuiClient) miClient, payrollReceipt.getPrimaryKey(), SDataConstantsPrint.PRINT_MODE_PDF_FILE);
                    File pdf = SHrsUtils.createPayrollReceipt((SGuiClient) miClient, map);

                    cfdProcessed++;

                    if (pdf != null) {
                        String subject = "Envío de recibo de nómina";
                        String body = "Se adjunta recibo de nómina en formato PDF.";
                        boolean sent = STrnUtilities.sendMailPdf((SClientInterface) miClient, SModSysConsts.CFGS_TP_MMS_CFD, pdf, subject, body, recipient);

                        if (sent) {
                            cfdProcessedOk++;
                            detailMessage += "Recibo enviado.\n";
                        }
                        else {
                            cfdProcessedWrong++;
                            detailMessage += "No se ha enviado.\n";
                        }
                    }
                    else {
                        cfdProcessedWrong++;
                        detailMessage += "No se creó el archivo PDF.\n";
                    }

                    updateForm(cfdProcessed, cfdProcessedOk, cfdProcessedWrong, detailMessage);
                }
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
            finally {
                SCfdUtils.resetDataSetForPayroll();
            }
        }
    }
    
    private void processPayrollReceipts() {
        if (maPayrollReceiptKeys != null) {
            int cfdProcessed = 0;
            int cfdProcessedOk = 0;
            int cfdProcessedWrong = 0;
            String detailMessage = "";
            String series = ((SDbConfig) miClient.getSession().readRegistry(SModConsts.HRS_CFG, new int[] { 1 })).getNumberSeries();

            moIntCfdToProcess.setValue(maPayrollReceiptKeys.size());
            jtfWarningMessage.setText(SCfdUtils.verifyCertificateExpiration((SClientInterface) miClient));
            jtfWarningMessage.setCaretPosition(0);
            
            try {
                SCfdUtils.initDataSetForPayroll(mnFormSubtype);

                for (int[] key : maPayrollReceiptKeys) {
                    int number = 0;
                    cfdProcessed++;

                    try {
                        switch (mnFormSubtype) {
                            case SCfdConsts.REQ_STAMP:
                                SDbPayrollReceiptIssue receiptIssue = (SDbPayrollReceiptIssue) miClient.getSession().readRegistry(SModConsts.HRS_PAY_RCP_ISS, key);

                                if (receiptIssue.getNumber() != 0) {
                                    // preserve already defined number:
                                    number = receiptIssue.getNumber();
                                }
                                else {
                                    // generate a new number:
                                    number = SHrsUtils.getPayrollReceiptNextNumber(miClient.getSession(), receiptIssue.getNumberSeries());
                                    receiptIssue.setNumber(number); // update memory
                                    receiptIssue.saveField(miClient.getSession().getStatement(), receiptIssue.getPrimaryKey(), SDbPayrollReceiptIssue.FIELD_NUMBER, number); // update persistent storage as well
                                }

                                SHrsCfdUtils.computeSignCfdi(miClient.getSession(), key);
                                detailMessage += receiptIssue.getPayrollReceiptIssueNumber() + ": Timbrado" + (mbIsCfdiSendingAutomaticHrs ? " y enviado.\n" : ".\n");
                                cfdProcessedOk++;
                                break;

                            default:
                        }
                    }
                    catch (Exception e) {
                        detailMessage += STrnUtils.formatDocNumber(series, "" + number) + ": " + e.getMessage() + "\n";
                        cfdProcessedWrong++;
                    }

                    updateForm(cfdProcessed, cfdProcessedOk, cfdProcessedWrong, detailMessage);
                }

                if (cfdProcessedOk > 0) {
                    miClient.getSession().notifySuscriptors(SModConsts.HRS_PAY);
                }
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
            finally {
                SCfdUtils.resetDataSetForPayroll();
            }
        }
    }
    
    private void processCfds() throws Exception {
        if (maCfds != null) {
            int cfdProcessed = 0;
            int cfdProcessedOk = 0;
            int cfdProcessedWrong = 0;
            int registryType = 0;
            SDataDps dps = null;
            SDataFormerPayrollEmp formerPayrollEmp = null;
            SDbPayrollReceiptIssue payrollReceiptIssue = null;
            String series = "";
            String number = "";
            String detailMessage = "";
            boolean processingPayrollReceipts = !maCfds.isEmpty() && maCfds.get(0).getFkCfdTypeId() == SDataConstantsSys.TRNS_TP_CFD_PAYROLL;

            moIntCfdToProcess.setValue(maCfds.size());
            jtfWarningMessage.setText(SCfdUtils.verifyCertificateExpiration((SClientInterface) miClient));
            jtfWarningMessage.setCaretPosition(0);

            try {
                if (processingPayrollReceipts) {
                    SCfdUtils.initDataSetForPayroll(mnFormSubtype);
                }

                // process CFD:

                for (SDataCfd cfd : maCfds) {
                    cfdProcessed++;

                    switch (cfd.getFkCfdTypeId()) {
                        case SDataConstantsSys.TRNS_TP_CFD_INV:
                            dps = (SDataDps) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.TRN_DPS, new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() }, SLibConstants.EXEC_MODE_SILENT);
                            series = dps.getNumberSeries();
                            number = dps.getNumber();

                            registryType = SModConsts.TRN_DPS;
                            break;

                        case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                            throw new Exception("Not supported yet!");

                        case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                            switch (mnPayrollCfdVersion) {
                                case SCfdConsts.CFDI_PAYROLL_VER_OLD:
                                    formerPayrollEmp = (SDataFormerPayrollEmp) SDataUtilities.readRegistry((SClientInterface) miClient, SDataConstants.HRS_SIE_PAY_EMP, new int[] { cfd.getFkPayrollPayrollId_n(), cfd.getFkPayrollEmployeeId_n() }, SLibConstants.EXEC_MODE_SILENT);
                                    series = formerPayrollEmp.getNumberSeries();
                                    number = "" + formerPayrollEmp.getNumber();
                                    break;

                                case SCfdConsts.CFDI_PAYROLL_VER_CUR:
                                    payrollReceiptIssue = (SDbPayrollReceiptIssue) miClient.getSession().readRegistry(SModConsts.HRS_PAY_RCP_ISS, new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), cfd.getFkPayrollReceiptIssueId_n() });
                                    series = payrollReceiptIssue.getNumberSeries();
                                    number = "" + payrollReceiptIssue.getNumber();
                                    break;

                                default:
                                    throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                            }

                            registryType = SModConsts.HRS_PAY;
                            break;

                        default:
                            throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }

                    try {
                        SGuiUtils.setCursorWait(miClient);
                        
                        switch (mnFormSubtype) {
                            case SCfdConsts.REQ_STAMP:
                                SCfdUtils.signCfdi((SClientInterface) miClient, cfd, mnPayrollCfdVersion, false, false);
                                detailMessage += (series.isEmpty() ? "" : series + "-") + number + ": Timbrado.\n";
                                break;

                            case SCfdConsts.REQ_ANNUL:
                                SCfdUtils.cancelCfdi((SClientInterface) miClient, cfd, mnPayrollCfdVersion, mtAnnulmentDate, mbValidateStamp, false, mnDpsAnnulType, msAnnulReason, msAnnulRelatedUuid, mbRetryCancel);
                                detailMessage += (series.isEmpty() ? "" : series + "-") + number + ": Anulado.\n";
                                break;

                            case SCfdConsts.REQ_PRINT_DOC:
                                SCfdUtils.printCfd((SClientInterface) miClient, cfd, mnPayrollCfdVersion, SDataConstantsPrint.PRINT_MODE_PRINT, mnNumberCopies, false);
                                detailMessage += (series.isEmpty() ? "" : series + "-") + number + ": Impreso.\n";
                                break;

                            case SCfdConsts.REQ_PRINT_ANNUL_ACK:
                                SCfdUtils.printCfdCancelAck((SClientInterface) miClient, cfd, mnPayrollCfdVersion, SDataConstantsPrint.PRINT_MODE_PRINT);
                                detailMessage += (series.isEmpty() ? "" : series + "-") + number + ": Impreso.\n";
                                break;

                            case SCfdConsts.REQ_SEND_DOC:
                                SCfdUtils.sendCfd((SClientInterface) miClient, cfd.getFkCfdTypeId(), cfd, mnPayrollCfdVersion, false, false, true);
                                detailMessage += (series.isEmpty() ? "" : series + "-") + number + ": Enviado.\n";
                                break;

                            case SCfdConsts.REQ_SEND_PAYROLL:
                                // case awkwardly implemented in method sendPayrollReceipts()
                                break;

                            case SCfdConsts.REQ_STAMP_SEND:
                                if (mbIsCfdiSendingAutomaticHrs) {
                                    SCfdUtils.signAndSendCfdi((SClientInterface) miClient, cfd, mnPayrollCfdVersion, false, false);
                                }
                                else {
                                    SCfdUtils.signCfdi((SClientInterface) miClient, cfd, mnPayrollCfdVersion, false, false);
                                }
                                detailMessage += (series.isEmpty() ? "" : series + "-") + number + ": Timbrado" + (mbIsCfdiSendingAutomaticHrs ? " y enviado.\n" : ".\n");
                                break;

                            case SCfdConsts.REQ_ANNUL_SEND:
                                if (mbIsCfdiSendingAutomaticHrs) {
                                    SCfdUtils.cancelAndSendCfdi((SClientInterface) miClient, cfd, mnPayrollCfdVersion, mtAnnulmentDate, mbValidateStamp, false, mnDpsAnnulType, msAnnulReason, msAnnulRelatedUuid, mbRetryCancel);
                                }
                                else {
                                    SCfdUtils.cancelCfdi((SClientInterface) miClient, cfd, mnPayrollCfdVersion, mtAnnulmentDate, mbValidateStamp, false, mnDpsAnnulType, msAnnulReason, msAnnulRelatedUuid, mbRetryCancel);
                                }
                                detailMessage += (series.isEmpty() ? "" : series + "-") + number + ": Anulado" + (mbIsCfdiSendingAutomaticHrs ? " y enviado.\n" : ".\n");
                                break;

                            case SCfdConsts.REQ_VERIFY:
                                SCfdUtils.validateCfdi((SClientInterface) miClient, cfd, mnPayrollCfdVersion, false);
                                detailMessage += (series.isEmpty() ? "" : series + "-") + number + ": Verificado.\n";
                                break;

                            default:
                        }

                        cfdProcessedOk++;
                    }
                    catch (Exception e) {
                        detailMessage += (series.isEmpty() ? "" : series + "-") + number + ": " + e.getMessage() + "\n";
                        cfdProcessedWrong++;
                    }
                    finally {
                        SGuiUtils.setCursorDefault(miClient);
                    }

                    updateForm(cfdProcessed, cfdProcessedOk, cfdProcessedWrong, detailMessage);
                }

                if (maCfds.isEmpty()) {
                    detailMessage += "No se encontraron CFD para ser procesados.\n";
                    updateForm(cfdProcessed, cfdProcessedOk, cfdProcessedWrong, detailMessage);
                }

                if (cfdProcessedOk > 0 && registryType != SLibConsts.UNDEFINED) {
                    miClient.getSession().notifySuscriptors(registryType);
                }
            }
            catch (Exception e) {
                SLibUtils.showException(this, e);
            }
            finally {
                if (processingPayrollReceipts) {
                    SCfdUtils.resetDataSetForPayroll();
                }
            }
        }
    }

    private void updateForm(final int cfdProcessed, final int cfdProcessedOk, final int cfdProcessedWrong, final String messages) {
        moIntCfdProcessed.setValue(cfdProcessed);
        moIntCfdProcessedOk.setValue(cfdProcessedOk);
        moIntCfdProcessedWrong.setValue(cfdProcessedWrong);
        
        if (mnStampsAvailable == 0) {
            moIntStampsAvailable.setValue(0);
            moIntStampsConsumed.setValue(0);
            moIntStampsRemaining.setValue(0);
        }
        else {
            moIntStampsAvailable.setValue(mnStampsAvailable);
            moIntStampsConsumed.setValue(cfdProcessedOk);
            moIntStampsRemaining.setValue(mnStampsAvailable - cfdProcessedOk);
        }
        
        jtaMessages.setText(messages);
        
        update(getGraphics());
        jspMessages.getVerticalScrollBar().setValue(jspMessages.getVerticalScrollBar().getMaximum());
    }
    
    /*
     * Public methods:
     */
    
    /**
     * Set form parameters.
     * @param cfds
     * @param payrollReceiptKeys
     * @param stampsAvailable
     * @param annulmentDate
     * @param validateStamp
     * @param payrollCfdVersion Constants defined in SCfdConsts.CFDI_PAYROLL_VER_...
     * @param dpsAnnulType 
     * @param annulReason 
     * @param annulRelatedUuid 
     */
    public void setFormParams(final ArrayList<SDataCfd> cfds, final ArrayList<int[]> payrollReceiptKeys, final int stampsAvailable, Date annulmentDate, final boolean validateStamp, final int payrollCfdVersion, final int dpsAnnulType, final String annulReason, final String annulRelatedUuid, final boolean retryCancel) {
        mbFirstTime = true;
        
        maCfds = cfds;
        maPayrollReceiptKeys = payrollReceiptKeys;
        mnStampsAvailable = stampsAvailable;
        mtAnnulmentDate = annulmentDate;
        mbValidateStamp = validateStamp;
        mnPayrollCfdVersion = payrollCfdVersion;
        mnDpsAnnulType = dpsAnnulType;
        msAnnulReason = annulReason;
        msAnnulRelatedUuid = annulRelatedUuid;
        mbRetryCancel = retryCancel;
    }
    
    public void setPayrollReceipts(final ArrayList<SDbPayrollReceipt> payrollReceipts) {
       maPayrollReceipts = payrollReceipts;
    }
    
    public void setNumberCopies(final int numberCopies) {
        mnNumberCopies = numberCopies;
    }

    @Override
    public void addAllListeners() {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void reloadCatalogues() {
        
    }

    @Override
    public void setRegistry(SDbRegistry registry) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SGuiValidation validateForm() {
        SGuiValidation validation = moFields.validateFields();

        return validation;
    }
}
