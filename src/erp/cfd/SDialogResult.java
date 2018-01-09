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
import erp.mod.fin.util.STreasuryBankLayoutRequest;
import erp.mod.hrs.db.SDbPayrollReceipt;
import erp.mod.hrs.db.SDbPayrollReceiptIssue;
import erp.mod.hrs.db.SHrsCfdUtils;
import erp.mod.hrs.db.SHrsUtils;
import static erp.mod.hrs.db.SHrsUtils.getMapPayrollReceipt;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.mtrn.data.SDataDps;
import erp.print.SDataConstantsPrint;
import erp.print.SPrintCfdiThread;
import java.awt.Cursor;
import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbRegistry;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiUtils;
import sa.lib.gui.SGuiValidation;

/**
 *
 * @author Juan Barajas, Alfredo Pérez, Sergio Flores
 */
public class SDialogResult extends sa.lib.gui.bean.SBeanFormDialog {

    protected SClientInterface miClient;
    
    protected ArrayList<SDataCfd> maCfds;
    protected ArrayList<int[]> maPayrollReceiptsIds;
    protected ArrayList<SDbPayrollReceipt> maPayrollReceipts;
    protected int mnTotalStamps;
    protected Date mtCancellationDate;
    protected boolean mbValidateStamp;
    
    protected boolean mbFirstTime;
    protected int mnSubtypeCfd;
    protected int mnNumCopies;
    protected int mnTpDpsAnn;
    
    /**
     * Creates new form SDialogResult
     */
    public SDialogResult(SGuiClient client, String title, int subType) {
        setFormSettings(client, SGuiConsts.BEAN_FORM_EDIT, SModConsts.TRN_CFD, subType, title);
        initComponents();
        initComponentsCustom();
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
        jlTotalToProcess = new javax.swing.JLabel();
        moIntTotalToProcess = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel21 = new javax.swing.JPanel();
        jlTotalCorrect = new javax.swing.JLabel();
        moIntTotalCorrect = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel22 = new javax.swing.JPanel();
        jlTotalIncorrect = new javax.swing.JLabel();
        moIntTotalIncorrect = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel4 = new javax.swing.JPanel();
        jlTotalProcess = new javax.swing.JLabel();
        moIntTotalProcess = new sa.lib.gui.bean.SBeanFieldInteger();
        jpStampInfo = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jlTotalStamps = new javax.swing.JLabel();
        moIntTotalStamp = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel24 = new javax.swing.JPanel();
        jlTotalConsumed = new javax.swing.JLabel();
        moIntTotalConsumed = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel25 = new javax.swing.JPanel();
        jlTotalAvailables = new javax.swing.JLabel();
        moIntTotalAvailables = new sa.lib.gui.bean.SBeanFieldInteger();
        jPanel8 = new javax.swing.JPanel();
        jlWarningMesage = new javax.swing.JLabel();
        jpDetail = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaMessage = new javax.swing.JTextArea();

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

        jlTotalToProcess.setText("CFDI a procesar:");
        jlTotalToProcess.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel6.add(jlTotalToProcess);

        moIntTotalToProcess.setEditable(false);
        jPanel6.add(moIntTotalToProcess);

        jPanel5.add(jPanel6);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTotalCorrect.setText("Procesados correctamente:");
        jlTotalCorrect.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel21.add(jlTotalCorrect);

        moIntTotalCorrect.setEditable(false);
        jPanel21.add(moIntTotalCorrect);

        jPanel5.add(jPanel21);

        jPanel22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTotalIncorrect.setText("Procesados incorrectamente:");
        jlTotalIncorrect.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel22.add(jlTotalIncorrect);

        moIntTotalIncorrect.setEditable(false);
        jPanel22.add(moIntTotalIncorrect);

        jPanel5.add(jPanel22);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTotalProcess.setText("CFDI procesados:");
        jlTotalProcess.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel4.add(jlTotalProcess);

        moIntTotalProcess.setEditable(false);
        jPanel4.add(moIntTotalProcess);

        jPanel5.add(jPanel4);

        jpInformation.add(jPanel5);

        jpStampInfo.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTotalStamps.setText("Timbres disponibles:");
        jlTotalStamps.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel7.add(jlTotalStamps);

        moIntTotalStamp.setEditable(false);
        jPanel7.add(moIntTotalStamp);

        jpStampInfo.add(jPanel7);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTotalConsumed.setText("Timbres consumidos:");
        jlTotalConsumed.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel24.add(jlTotalConsumed);

        moIntTotalConsumed.setEditable(false);
        jPanel24.add(moIntTotalConsumed);

        jpStampInfo.add(jPanel24);

        jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTotalAvailables.setText("Timbres restantes:");
        jlTotalAvailables.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel25.add(jlTotalAvailables);

        moIntTotalAvailables.setEditable(false);
        jPanel25.add(moIntTotalAvailables);

        jpStampInfo.add(jPanel25);

        jPanel8.setLayout(new java.awt.GridLayout(1, 0));

        jlWarningMesage.setToolTipText("");
        jlWarningMesage.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jlWarningMesage.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jPanel8.add(jlWarningMesage);

        jpStampInfo.add(jPanel8);

        jpInformation.add(jpStampInfo);

        jPanel1.add(jpInformation, java.awt.BorderLayout.NORTH);

        jpDetail.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle:"));
        jpDetail.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setAutoscrolls(true);

        jtaMessage.setEditable(false);
        jtaMessage.setColumns(20);
        jtaMessage.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        jtaMessage.setRows(5);
        jtaMessage.setFocusable(false);
        jtaMessage.setOpaque(false);
        jScrollPane1.setViewportView(jtaMessage);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jlTotalAvailables;
    private javax.swing.JLabel jlTotalConsumed;
    private javax.swing.JLabel jlTotalCorrect;
    private javax.swing.JLabel jlTotalIncorrect;
    private javax.swing.JLabel jlTotalProcess;
    private javax.swing.JLabel jlTotalStamps;
    private javax.swing.JLabel jlTotalToProcess;
    private javax.swing.JLabel jlWarningMesage;
    private javax.swing.JPanel jpDetail;
    private javax.swing.JPanel jpInformation;
    private javax.swing.JPanel jpStampInfo;
    private javax.swing.JTextArea jtaMessage;
    private sa.lib.gui.bean.SBeanFieldInteger moIntTotalAvailables;
    private sa.lib.gui.bean.SBeanFieldInteger moIntTotalConsumed;
    private sa.lib.gui.bean.SBeanFieldInteger moIntTotalCorrect;
    private sa.lib.gui.bean.SBeanFieldInteger moIntTotalIncorrect;
    private sa.lib.gui.bean.SBeanFieldInteger moIntTotalProcess;
    private sa.lib.gui.bean.SBeanFieldInteger moIntTotalStamp;
    private sa.lib.gui.bean.SBeanFieldInteger moIntTotalToProcess;
    // End of variables declaration//GEN-END:variables

    private void windowsActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            
            try {
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
        
        jlWarningMesage.setText("");
        
        moIntTotalToProcess.setIntegerSettings(SGuiUtils.getLabelName(jlTotalToProcess), SGuiConsts.GUI_TYPE_INT, true);
        moIntTotalProcess.setIntegerSettings(SGuiUtils.getLabelName(jlTotalProcess), SGuiConsts.GUI_TYPE_INT, true);
        moIntTotalCorrect.setIntegerSettings(SGuiUtils.getLabelName(jlTotalCorrect), SGuiConsts.GUI_TYPE_INT, true);
        moIntTotalIncorrect.setIntegerSettings(SGuiUtils.getLabelName(jlTotalIncorrect), SGuiConsts.GUI_TYPE_INT, true);
        moIntTotalStamp.setIntegerSettings(SGuiUtils.getLabelName(jlTotalStamps), SGuiConsts.GUI_TYPE_INT, true);
        moIntTotalConsumed.setIntegerSettings(SGuiUtils.getLabelName(jlTotalConsumed), SGuiConsts.GUI_TYPE_INT, true);
        moIntTotalAvailables.setIntegerSettings(SGuiUtils.getLabelName(jlTotalAvailables), SGuiConsts.GUI_TYPE_INT, true);

        moFields.addField(moIntTotalToProcess);
        moFields.addField(moIntTotalProcess);
        moFields.addField(moIntTotalCorrect);
        moFields.addField(moIntTotalIncorrect);
        moFields.addField(moIntTotalStamp);
        moFields.addField(moIntTotalConsumed);
        moFields.addField(moIntTotalAvailables);

        moFields.setFormButton(jbSave);
    }
    
    private void process() throws Exception {
        miClient.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        if (mnFormSubtype == SCfdConsts.PROC_REQ_SND_RCP) {
            processReceipts();
            miClient.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        else { 
            if (maPayrollReceiptsIds != null) {
                processPayroll();
            }
            else if (maCfds != null) {
                processCfd();            
            }
        }
    }
    
    public void processPayroll() {
        int cfdsProcessed = 0;
        int cfdsCorrect = 0;
        int cfdsIncorrect = 0;
        String detailMessage = "";
        String warningMessage = "";
        
        if (maPayrollReceiptsIds != null) {
            for (int[] key : maPayrollReceiptsIds) {
                int number = 0;
                cfdsProcessed++;
                try {
                    switch (mnFormSubtype) {
                        case SCfdConsts.PROC_REQ_STAMP:
                            SDbPayrollReceiptIssue receiptIssue = (SDbPayrollReceiptIssue) miClient.getSession().readRegistry(SModConsts.HRS_PAY_RCP_ISS, key);

                            if (receiptIssue.getPkIssueId() != SLibConsts.UNDEFINED) {
                                if (receiptIssue.getNumber() != 0) {
                                    number = receiptIssue.getNumber();
                                }
                                else {
                                    number = SHrsUtils.getPayrollReceiptNextNumber(miClient.getSession(), receiptIssue.getNumberSeries());
                                    receiptIssue.saveField(miClient.getSession().getStatement(), key, SDbPayrollReceiptIssue.FIELD_NUMBER, number);
                                }
                            }

                            SHrsCfdUtils.computeSignCfdi(miClient.getSession(), key);
                            detailMessage += (receiptIssue.getNumberSeries().length() > 0 ? receiptIssue.getNumberSeries() + "-" : "") + number + "   Timbrado" + (miClient.getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticHrs() ? " y enviado.\n" : ".\n");
                            cfdsCorrect++;
                            break;
                        default:
                    }
                }
                catch(Exception e) {
                    detailMessage += "NOM-" + number + "   " + e.getMessage() + "\n";
                    cfdsIncorrect++;
                }

                if (mnTotalStamps > 0) {
                    updateForm(cfdsProcessed, cfdsCorrect, cfdsIncorrect, detailMessage, mnTotalStamps);
                }
                else {
                    updateForm(cfdsProcessed, cfdsCorrect, cfdsIncorrect, detailMessage);
                }
                update(getGraphics());
                jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getMaximum());
            }
            
            warningMessage = SCfdUtils.verifyCertificateExpiration(miClient);
            jlWarningMesage.setText(warningMessage);
            
            miClient.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private void processReceipts() throws Exception {
        boolean isSent = false;
        int cfdsProcessed = 0;
        int cfdsCorrect = 0;
        int cfdsIncorrect = 0;
        String detailMessage = "";
        String mail = "";
        
        STreasuryBankLayoutRequest layoutRequest = null;
        SDataBizPartner bizPartner  = null;
        HashMap<String, Object> map = new HashMap<>();
        File pdf = null;
        
        moIntTotalToProcess.setValue(maPayrollReceipts.size());
        
        for (SDbPayrollReceipt receipt : maPayrollReceipts) {
            
            bizPartner  = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { receipt.getPkEmployeeId() }, SLibConstants.EXEC_MODE_SILENT);
            mail = bizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchContacts().get(0).getEmail01();
            
            map = getMapPayrollReceipt((SGuiClient) miClient, SDataConstantsPrint.PRINT_MODE_PDF, receipt.getPrimaryKey());
            pdf = SHrsUtils.createPayrollReceipt(map, (SGuiClient) miClient);
            cfdsProcessed++;
        
            if (pdf != null) {
                isSent = false;
                layoutRequest = new STreasuryBankLayoutRequest((SGuiClient) miClient, null);
                isSent = layoutRequest.sendMail(null, "", pdf, STreasuryBankLayoutRequest.SND_TP_PAY_RCP, mail);
                    
                if (isSent) {
                    cfdsCorrect++;
                    detailMessage += "Recibo enviado\n";
                }
                else {
                    cfdsIncorrect++;
                    detailMessage += "No se ha enviado\n";
                }
            }
            else {
                cfdsIncorrect++;
                detailMessage += "No se creo el PDF\n";
            }
            
            updateForm(cfdsProcessed, cfdsCorrect, cfdsIncorrect, detailMessage);
            
            update(getGraphics());
            jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getMaximum());
        }
    }
    
    public void processCfd() throws Exception {
        int cfdsProcessed = 0;
        int cfdsCorrect = 0;
        int cfdsIncorrect = 0;
        SDataFormerPayrollEmp payrollEmp = null;
        SDbPayrollReceiptIssue payrollReceiptIssue = null;
        SDataDps dps = null;
        String detailMessage = "";
        String numberSeries = "";
        String number = "";
        String sSql = "";
        String warningMessage = "";
        ResultSet resultSet = null;
        SPrintCfdiThread thread = null; 
        
        moIntTotalToProcess.setValue(maCfds.size());
        
        if (maCfds != null) {
            for(SDataCfd cfd : maCfds) {
                cfdsProcessed++;

                switch (cfd.getFkCfdTypeId()) {
                    case SDataConstantsSys.TRNS_TP_CFD_INV:
                        dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, new int[] { cfd.getFkDpsYearId_n(), cfd.getFkDpsDocId_n() }, SLibConstants.EXEC_MODE_SILENT);

                        numberSeries = dps.getNumberSeries();
                        number = dps.getNumber();
                        break;
                    case SDataConstantsSys.TRNS_TP_CFD_PAYROLL:
                        switch (mnSubtypeCfd) {
                            case SCfdConsts.CFDI_PAYROLL_VER_OLD:
                                payrollEmp = (SDataFormerPayrollEmp) SDataUtilities.readRegistry(miClient, SDataConstants.HRS_SIE_PAY_EMP, new int[] { cfd.getFkPayrollPayrollId_n(), cfd.getFkPayrollEmployeeId_n() }, SLibConstants.EXEC_MODE_SILENT);

                                numberSeries = payrollEmp.getNumberSeries();
                                number = "" + payrollEmp.getNumber();
                                break;
                            case SCfdConsts.CFDI_PAYROLL_VER_CUR:
                                /*
                                payrollReceipt = new SDbPayrollReceipt();
                                payrollReceipt.read(miClient.getSession(), new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n() });
                                
                                if (payrollReceipt.getPayrollReceiptIssues() != null) {
                                    numberSeries = payrollReceipt.getPayrollReceiptIssues().getNumberSeries();
                                    number = "" + payrollReceipt.getPayrollReceiptIssues().getNumber();
                                }
                                */
                                // Read Issue last:

                                sSql = "SELECT id_iss "
                                        + "FROM " + SModConsts.TablesMap.get(SModConsts.HRS_PAY_RCP_ISS) + " "
                                        + "WHERE id_pay = " + cfd.getFkPayrollReceiptPayrollId_n() + " AND id_emp = " + cfd.getFkPayrollReceiptEmployeeId_n() + " "
                                        + "ORDER BY id_iss DESC LIMIT 1";

                                resultSet = miClient.getSession().getDatabase().getConnection().createStatement().executeQuery(sSql);
                                if (resultSet.next()) {
                                    payrollReceiptIssue = (SDbPayrollReceiptIssue) miClient.getSession().readRegistry(SModConsts.HRS_PAY_RCP_ISS, new int[] { cfd.getFkPayrollReceiptPayrollId_n(), cfd.getFkPayrollReceiptEmployeeId_n(), resultSet.getInt("id_iss") });
                                    numberSeries = payrollReceiptIssue.getNumberSeries();
                                    number = "" + payrollReceiptIssue.getNumber();
                                }
                                break;
                            default:
                                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }

                        break;
                    default:
                        throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }

                try {
                    switch (mnFormSubtype) {
                        case SCfdConsts.PROC_REQ_STAMP:
                            SCfdUtils.signCfdi(miClient, cfd, mnSubtypeCfd, false, false);
                            detailMessage += (numberSeries.length() > 0 ? numberSeries + "-" : "") + number + "   Timbrado.\n";
                            break;
                        case SCfdConsts.PROC_REQ_ANNUL:
                            SCfdUtils.cancelCfdi(miClient, cfd, mnSubtypeCfd, mtCancellationDate, mbValidateStamp, false, mnTpDpsAnn);
                            detailMessage += (numberSeries.length() > 0 ? numberSeries + "-" : "") + number + "   Anulado.\n";
                            break;
                        case SCfdConsts.PROC_PRT_DOC:
                            thread = new SPrintCfdiThread(miClient, cfd.getPkCfdId(), SDataConstantsPrint.PRINT_MODE_STREAM, mnNumCopies, mnSubtypeCfd, this);
                            
                            thread.startThread();
                            thread.join();
                            //SCfdUtils.printCfd(miClient, cfd.getFkCfdTypeId(), cfd, SDataConstantsPrint.PRINT_MODE_STREAM, mnNumCopies, mnSubtypeCfd, false);
                            detailMessage += (numberSeries.length() > 0 ? numberSeries + "-" : "") + number + "   Impreso.\n";
                            break;
                        case SCfdConsts.PROC_PRT_DOCS:
                            SCfdUtils.printCfd(miClient, cfd.getFkCfdTypeId(), cfd, SDataConstantsPrint.PRINT_MODE_STREAM, mnNumCopies, mnSubtypeCfd, false);
                            detailMessage += (numberSeries.length() > 0 ? numberSeries + "-" : "") + number + "   Impreso.\n";
                            break;
                        case SCfdConsts.PROC_PRT_ACK_ANNUL:
                            SCfdUtils.printAcknowledgmentCancellationCfd(miClient, cfd, SDataConstantsPrint.PRINT_MODE_STREAM, mnSubtypeCfd);
                            detailMessage += (numberSeries.length() > 0 ? numberSeries + "-" : "") + number + "   Impreso.\n";
                            break;
                        case SCfdConsts.PROC_SND_DOC:
                            SCfdUtils.sendCfd(miClient, cfd.getFkCfdTypeId(), cfd, mnSubtypeCfd, false, false, true);
                            detailMessage += (numberSeries.length() > 0 ? numberSeries + "-" : "") + number + "   Enviado.\n";
                            break;
                        case SCfdConsts.PROC_REQ_STAMP_AND_SND:
                            if (miClient.getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticHrs()) {
                                SCfdUtils.signAndSendCfdi(miClient, cfd, mnSubtypeCfd, false, false);
                            }
                            else {
                                SCfdUtils.signCfdi(miClient, cfd, mnSubtypeCfd, false, false);
                            }
                            detailMessage += (numberSeries.length() > 0 ? numberSeries + "-" : "") + number + "Timbrado" + (miClient.getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticHrs() ? " y enviado.\n" : ".\n");
                            break;
                        case SCfdConsts.PROC_REQ_ANNUL_AND_SND:
                            if (miClient.getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticHrs()) {
                                SCfdUtils.cancelAndSendCfdi(miClient, cfd, mnSubtypeCfd, mtCancellationDate, mbValidateStamp, false, mnTpDpsAnn);
                            }
                            else {
                                SCfdUtils.cancelCfdi(miClient, cfd, mnSubtypeCfd, mtCancellationDate, mbValidateStamp, false, mnTpDpsAnn);
                            }
                            detailMessage += (numberSeries.length() > 0 ? numberSeries + "-" : "") + number + "   Anulado" + (miClient.getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticHrs() ? " y enviado.\n" : ".\n");
                            break;
                        case SCfdConsts.PROC_REQ_VERIFY:
                            SCfdUtils.verifyCfdi(miClient, cfd, mnSubtypeCfd, false);
                            detailMessage += (numberSeries.length() > 0 ? numberSeries + "-" : "") + number + "   Enviado.\n";
                            break;
                        default:
                    }
                    cfdsCorrect++;
                }
                catch (Exception e) {
                    detailMessage += (numberSeries.length() > 0 ? numberSeries + "-" : "") + number + "   " + e.getMessage() + "\n";
                    cfdsIncorrect++;
                }

                if (mnTotalStamps > 0) {
                    updateForm(cfdsProcessed, cfdsCorrect, cfdsIncorrect, detailMessage, mnTotalStamps);
                }
                else {
                    updateForm(cfdsProcessed, cfdsCorrect, cfdsIncorrect, detailMessage);
                }
                
                update(getGraphics());
                jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getMaximum());
            }
            
            if (maCfds.isEmpty() && mnFormSubtype == SCfdConsts.PROC_PRT_DOCS ) {
                detailMessage += "No se encontró ningún documento a imprimir para la fecha y folios indicados.\n";
                updateForm(cfdsProcessed, cfdsCorrect, cfdsIncorrect, detailMessage);
            }
            
            warningMessage = SCfdUtils.verifyCertificateExpiration(miClient);
            jlWarningMesage.setText(warningMessage);
           
            miClient.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public synchronized SGuiSession getSession() {
        return miClient.getSession();
    }
    
    private void updateForm(final int totalProcess, final int totalCorrect, final int totalIncorrect, final String message, final int totalStamp) {
        moIntTotalProcess.setValue(totalProcess);
        moIntTotalCorrect.setValue(totalCorrect);
        moIntTotalIncorrect.setValue(totalIncorrect);
        moIntTotalStamp.setValue(totalStamp);
        moIntTotalConsumed.setValue(totalCorrect);
        moIntTotalAvailables.setValue(totalStamp - totalCorrect);
        
        jtaMessage.setText(message);
    }
    
    public void updateForm(final int totalProcess, final int totalCorrect, final int totalIncorrect, final String message) {
        moIntTotalProcess.setValue(totalProcess);
        moIntTotalCorrect.setValue(totalCorrect);
        moIntTotalIncorrect.setValue(totalIncorrect);
        moIntTotalStamp.setValue(0);
        moIntTotalConsumed.setValue(0);
        moIntTotalAvailables.setValue(0);
        
        jtaMessage.setText(message);
    }
    
    public void setFormParams(final SClientInterface client, final ArrayList<SDataCfd> cfds, final ArrayList<int[]> payrollReceipts, final int totalStamp, Date cancellationDate, final boolean validateStamp, final int subtypeCfd, final int tpDpsAnn) {
        mbFirstTime = true;
        miClient = client;
        maCfds = cfds;
        maPayrollReceiptsIds = payrollReceipts;
        mnTotalStamps = totalStamp;
        mtCancellationDate = cancellationDate;
        mbValidateStamp = validateStamp;
        mnSubtypeCfd = subtypeCfd;
        mnTpDpsAnn = tpDpsAnn;
    }
    
    public void setReceipts(ArrayList<SDbPayrollReceipt> actives) {
       maPayrollReceipts = actives;
    }
    
    public void setNumberCopies(final int numCopies) {
        mnNumCopies = numCopies;
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