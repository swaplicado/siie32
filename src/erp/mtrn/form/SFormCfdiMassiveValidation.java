/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormCfdiMassiveValidation.java
 *
 * Created on 26/08/2020, 09:32:00 AM
 */

package erp.mtrn.form;

import cfd.DCfdUtils;
import cfd.ver33.DCfdi33Consts;
import erp.SClientUtils;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.mbps.data.SDataBizPartner;
import erp.mod.trn.db.STrnUtils;
import erp.mtrn.data.SCfdUtilsHandler;
import erp.mtrn.data.SRowCfdSatStatus;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;

/**
 * Validación de estatus SAT de documentos de manera masiva.
 * @author Isabel Servín, Sergio Flores
 */
public class SFormCfdiMassiveValidation extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener, java.awt.event.ItemListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private final int mnCant = 5000;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private final erp.client.SClientInterface miClient;

    private erp.lib.form.SFormField moFieldDateStart;
    private erp.lib.form.SFormField moFieldDateFinal;
    private erp.lib.form.SFormField moFieldCfdType;
    private erp.lib.form.SFormField moFieldBizPartnerId;
    
    private erp.lib.table.STablePane moResultPane;
    private final int mnModuleType;
    private final int mnModuleSubtype;
    
    private boolean mbErrShowed;

    /** Creates new form SFormCfdiMassiveValidation
     * @param client
     * @param moduleType modulo de donde se llama la forma
     * @param moduleSubType tipo de facturas que se van a mostrar
     */
    public SFormCfdiMassiveValidation(erp.client.SClientInterface client, int moduleType, int moduleSubType) {
        super(client.getFrame(), true);
        miClient = client;
        mnModuleType = moduleType;
        mnModuleSubtype = moduleSubType;
        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgCancelationType = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        jftDateStart = new javax.swing.JFormattedTextField();
        jbDateStart = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jlDateEnd = new javax.swing.JLabel();
        jftDateEnd = new javax.swing.JFormattedTextField();
        jbDateEnd = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jlCfdType = new javax.swing.JLabel();
        jcbCfdType = new javax.swing.JComboBox();
        jPanel8 = new javax.swing.JPanel();
        jlBizPartnerId = new javax.swing.JLabel();
        jcbBizPartnerId = new javax.swing.JComboBox();
        jbBizPartnerId = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jrbAllVouchers = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel14 = new javax.swing.JPanel();
        jrbVoucherCancellableWithoutAcceptance = new sa.lib.gui.bean.SBeanFieldRadio();
        jPanel15 = new javax.swing.JPanel();
        jrbVoucherCancellableWithAcceptance = new sa.lib.gui.bean.SBeanFieldRadio();
        jpResultPane = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jbCheckButton = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Validación masiva de estatus de CFDI");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Comprobantes a validar:"));
        jPanel3.setLayout(new java.awt.GridLayout(4, 1, 0, 2));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha inicial: *");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlDateStart);

        jftDateStart.setText("dd/mm/yyyy");
        jftDateStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel6.add(jftDateStart);

        jbDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_date_day.gif"))); // NOI18N
        jbDateStart.setToolTipText("Seleccionar fecha");
        jbDateStart.setFocusable(false);
        jbDateStart.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbDateStart);

        jPanel3.add(jPanel6);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateEnd.setText("Fecha final: *");
        jlDateEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlDateEnd);

        jftDateEnd.setText("dd/mm/yyyy");
        jftDateEnd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jftDateEnd);

        jbDateEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_date_day.gif"))); // NOI18N
        jbDateEnd.setToolTipText("Seleccionar fecha de pago");
        jbDateEnd.setFocusable(false);
        jbDateEnd.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jbDateEnd);

        jPanel3.add(jPanel10);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdType.setText("Tipo de CFD: *");
        jlCfdType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel9.add(jlCfdType);

        jcbCfdType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCfdType.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel9.add(jcbCfdType);

        jPanel3.add(jPanel9);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerId.setText("Receptor:");
        jlBizPartnerId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlBizPartnerId);

        jcbBizPartnerId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbBizPartnerId.setPreferredSize(new java.awt.Dimension(375, 23));
        jPanel8.add(jcbBizPartnerId);

        jbBizPartnerId.setText("jButton1");
        jbBizPartnerId.setToolTipText("Seleccionar asociado de negocios");
        jbBizPartnerId.setFocusable(false);
        jbBizPartnerId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel8.add(jbBizPartnerId);

        jLabel1.setPreferredSize(new java.awt.Dimension(15, 23));
        jPanel8.add(jLabel1);

        jPanel3.add(jPanel8);

        jPanel11.add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Estatus de cancelación:"));
        jPanel12.setLayout(new java.awt.GridLayout(3, 0));

        jPanel13.setPreferredSize(new java.awt.Dimension(310, 23));
        jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        bgCancelationType.add(jrbAllVouchers);
        jrbAllVouchers.setLabel("Todos los comprobantes");
        jrbAllVouchers.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel13.add(jrbAllVouchers);

        jPanel12.add(jPanel13);

        jPanel14.setPreferredSize(new java.awt.Dimension(310, 23));
        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        bgCancelationType.add(jrbVoucherCancellableWithoutAcceptance);
        jrbVoucherCancellableWithoutAcceptance.setText("Solo comprobantes cancelables sin aceptación");
        jrbVoucherCancellableWithoutAcceptance.setPreferredSize(new java.awt.Dimension(315, 23));
        jPanel14.add(jrbVoucherCancellableWithoutAcceptance);

        jPanel12.add(jPanel14);

        jPanel15.setPreferredSize(new java.awt.Dimension(310, 23));
        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        bgCancelationType.add(jrbVoucherCancellableWithAcceptance);
        jrbVoucherCancellableWithAcceptance.setText("Solo comprobantes cancelables con aceptación");
        jrbVoucherCancellableWithAcceptance.setPreferredSize(new java.awt.Dimension(315, 23));
        jPanel15.add(jrbVoucherCancellableWithAcceptance);

        jPanel12.add(jPanel15);

        jPanel11.add(jPanel12, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel11, java.awt.BorderLayout.NORTH);

        jpResultPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultados de la validación:"));
        jpResultPane.setLayout(new java.awt.BorderLayout());
        jPanel2.add(jpResultPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(692, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbCheckButton.setText("Validar");
        jbCheckButton.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel1.add(jbCheckButton);

        jbCancel.setText("Cerrar");
        jbCancel.setToolTipText("[Escape]");
        jbCancel.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel1.add(jbCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(803, 485));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        int i;
        erp.lib.table.STableColumnForm tableColumns[];

        moResultPane = new STablePane(miClient);
        jpResultPane.add(moResultPane, BorderLayout.CENTER);
        
        i = 0;
        tableColumns = new STableColumnForm[10];
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo de doc.", 40);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Folio", 65);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE, "Fecha doc.", STableConstants.WIDTH_DATE);
        if (mnModuleSubtype == SDataConstantsSys.TRNS_CT_DPS_PUR) {
            tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Emisor", 200);
            tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "RFC emisor", 100);
        }
        else {
            tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Receptor", 200);
            tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "RFC receptor", 100);
        }
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "UUID", 200);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Estatus", 150);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Código estatus", 200);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Es cancelable", 150);
        tableColumns[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Estatus cancelación", 150);
        
        for (i = 0; i < tableColumns.length; i++) {
            moResultPane.addTableColumn(tableColumns[i]);
        }

        moFieldDateStart = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateStart, jlDateStart);
        moFieldDateStart.setPickerButton(jbDateStart);
        moFieldDateFinal = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateEnd, jlDateEnd);
        moFieldDateFinal.setPickerButton(jbDateEnd);
        moFieldCfdType = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbCfdType, jlCfdType);
        moFieldBizPartnerId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbBizPartnerId, jlBizPartnerId);
        moFieldBizPartnerId.setPickerButton(jbBizPartnerId);
        
        mvFields = new Vector<SFormField>();
        mvFields.add(moFieldDateStart);
        mvFields.add(moFieldDateFinal);
        mvFields.add(moFieldCfdType);
        mvFields.add(moFieldBizPartnerId);
        
        jbCancel.addActionListener(this);
        jbDateStart.addActionListener(this);
        jbDateEnd.addActionListener(this);
        jbBizPartnerId.addActionListener(this);
        jbCheckButton.addActionListener(this);
        
        formReset();
        formRefreshCatalogues();
        
        jcbCfdType.addItemListener(this);
        
        if (mnModuleType == SDataConstants.MOD_PUR || mnModuleType == SDataConstants.MOD_SAL) {
            jcbCfdType.setEnabled(false);
            jcbCfdType.setSelectedIndex(1);
        }
        else if (mnModuleType == SDataConstants.MOD_HRS) {
            jcbCfdType.setEnabled(false); 
            jcbCfdType.setSelectedIndex(3);
        }

        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), action, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jftDateStart.requestFocus();
        }
    }

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void actionDateStart() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDateStart.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDateStart.setFieldValue(miClient.getGuiDatePickerXXX().getGuiDate());
            jftDateStart.requestFocus();
        }
    }

    private void actionDateFinal() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDateFinal.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDateFinal.setFieldValue(miClient.getGuiDatePickerXXX().getGuiDate());
            jftDateEnd.requestFocus();
        }
    }

    private void actionBizPartnerId() {
        miClient.pickOption(SDataConstants.BPSX_BP_SUP, moFieldBizPartnerId, null);
    }

    private void actionValidateButton() {
        if (miClient.showMsgBoxConfirm("Esta operación puede tardar varios minutos.\n¿Desea continuar?") == JOptionPane.OK_OPTION) {
            SFormValidation validation = formValidate();
            if (validation.getIsError()) {
                if (!mbErrShowed) {
                    miClient.showMsgBoxWarning(validation.getMessage());
                    validation.getComponent().requestFocus();
                }
            }
            else {
                String fechaInicial = SLibUtils.DbmsDateFormatDate.format(moFieldDateStart.getDate());
                String fechaFinal = SLibUtils.DbmsDateFormatDate.format(moFieldDateFinal.getDate());

                int cfdType = ((int[]) moFieldCfdType.getKey())[0];

                String colNumber = "";
                String colFiscalId = "";
                String sqlJoin = "";
                String sqlOrder = "";
                String sqlDpsType = "";
                
                switch (cfdType) {
                    case SDataConstantsSys.TRNS_TP_CFD_INV: 
                        colNumber = ", d.num_ser AS _cfd_series, d.num AS _cfd_number, d.fid_cl_dps AS _dps_type";
                        colFiscalId = "c.xml_rfc_emi";
                        sqlJoin = "INNER JOIN trn_dps AS d ON c.fid_dps_year_n = d.id_year AND c.fid_dps_doc_n = d.id_doc ";
                        sqlOrder = "ORDER BY bp_iss.bp, bp_rec.bp, c.id_cfd, c.ts ";
                        
                        int[] dpsKey = null;
                        int[] adjKey = null;
                        
                        switch (mnModuleSubtype) {
                            case SDataConstantsSys.TRNS_CT_DPS_PUR:
                                dpsKey = SDataConstantsSys.TRNU_TP_DPS_PUR_INV;
                                adjKey = SDataConstantsSys.TRNU_TP_DPS_PUR_CN;
                                break;
                                
                            case SDataConstantsSys.TRNS_CT_DPS_SAL:
                                dpsKey = SDataConstantsSys.TRNU_TP_DPS_SAL_INV;
                                adjKey = SDataConstantsSys.TRNU_TP_DPS_SAL_CN;
                                break;
                                
                            default:
                                // do nothing
                        }
                        
                        if (dpsKey != null && adjKey != null) {
                            sqlDpsType = "AND ("
                                    + "(d.fid_ct_dps = " + dpsKey[0] + " AND d.fid_cl_dps = " + dpsKey[1] + " AND d.fid_tp_dps = " + dpsKey[2] + ") OR"
                                    + "(d.fid_ct_dps = " + adjKey[0] + " AND d.fid_cl_dps = " + adjKey[1] + " AND d.fid_tp_dps = " + adjKey[2] + ")) ";
                        }
                        break;
                        
                    case SDataConstantsSys.TRNS_TP_CFD_PAY_REC:
                        colNumber = ", c.ser AS _cfd_series, c.num AS _cfd_number";
                        colFiscalId = "c.xml_rfc_rec";
                        sqlOrder  = "ORDER BY bp_rec.bp, bp_iss.bp, c.id_cfd, c.ts ";
                        break;
                        
                    case SDataConstantsSys.TRNS_TP_CFD_PAYROLL: 
                        colNumber = ", i.num_ser AS _cfd_series, i.num AS _cfd_number";
                        colFiscalId = "c.xml_rfc_rec";
                        sqlJoin = "INNER JOIN hrs_pay_rcp_iss AS i ON c.fid_pay_rcp_pay_n = i.id_pay AND c.fid_pay_rcp_emp_n = i.id_emp AND c.fid_pay_rcp_iss_n = i.id_iss ";
                        sqlOrder  = "ORDER BY bp_rec.bp, bp_iss.bp, c.id_cfd, c.ts ";
                        break;
                        
                    default:
                        // do nothing
                }

                String sqlWhere = "c.ts BETWEEN '" + fechaInicial + "' AND '" + fechaFinal + "'";

                if (jrbVoucherCancellableWithoutAcceptance.isSelected()) {
                    sqlWhere = "(" + sqlWhere +" AND IF (c.xml_tc = 0.0, c.xml_tot <= " + mnCant + " , (c.xml_tot * c.xml_tc) <= " + mnCant + ")) "
                            + "OR c.ts BETWEEN (NOW() - INTERVAL 3 DAY) AND NOW() ";    
                }
                else if (jrbVoucherCancellableWithAcceptance.isSelected()) {
                    sqlWhere += " AND IF (c.xml_tc = 0.0, c.xml_tot > " + mnCant + " , (c.xml_tot * c.xml_tc) > " + mnCant + ")";
                }

                String bizPartnerFiscalId = "";
                
                if (((int[]) moFieldBizPartnerId.getKey())[0] != 0){
                    SDataBizPartner moBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, 
                        SDataConstants.BPSU_BP, (int[]) moFieldBizPartnerId.getKey(), SLibConstants.EXEC_MODE_SILENT);
                    bizPartnerFiscalId = moBizPartner.getFiscalId();
                }
                try {
                    this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    
                    String sql = "SELECT c.id_cfd, c.ts, c.uuid, c.xml_tot, " +
                            "c.xml_rfc_emi, bp_iss.bp AS _bp_issuer, " +
                            "c.xml_rfc_rec, bp_rec.bp AS _bp_receiver, tc.doc_xml AS xml" + colNumber + " " +
                            "FROM trn_cfd AS c " +
                            "INNER JOIN " + SClientUtils.getComplementaryDdName(miClient) + ".trn_cfd AS tc ON c.id_cfd = tc.id_cfd " +
                            "INNER JOIN erp.bpsu_bp AS bp_iss ON c.xml_rfc_emi = bp_iss.fiscal_id " +
                            "INNER JOIN erp.bpsu_bp AS bp_rec ON c.xml_rfc_rec = bp_rec.fiscal_id " +
                            sqlJoin +
                            "WHERE fid_tp_cfd = " + cfdType + " " + sqlDpsType +
                            (bizPartnerFiscalId.isEmpty() ? "" : "AND " + colFiscalId + " = '" + bizPartnerFiscalId + "' ") +
                            "AND "+ sqlWhere + " " +
                            "GROUP BY c.id_cfd, c.ts " +
                            sqlOrder + ";";
                    
                    try (ResultSet resultSet = miClient.getSession().getStatement().getConnection().createStatement().executeQuery(sql)) {
                        int cfdiTotal = 0;
                        int cfdiValid = 0;
                        int cfdiCanceled = 0;
                        int cfdiNotFound = 0;
                        int cfdiWithOutXml = 0;
                        boolean results = false;
                        
                        moResultPane.createTable();
                        moResultPane.clearTableRows();
                        
                        while (resultSet.next()) {
                            cfdiTotal++;
                            results = true;
                            String docType = "";
                            
                            if (cfdType == SDataConstantsSys.TRNS_TP_CFD_INV) {
                                switch(resultSet.getInt("_dps_type")) {
                                    case SDataConstantsSys.TRNS_CL_DPS_DOC:
                                        docType = "F";
                                        break;
                                    case SDataConstantsSys.TRNS_CL_DPS_ADJ:
                                        docType = "N";
                                        break;
                                    default: 
                                }
                            }
                            
                            String docNumber = STrnUtils.formatDocNumber(resultSet.getString("_cfd_series"), resultSet.getString("_cfd_number"));
                            String uuid = resultSet.getString("c.uuid");
                            String xml = resultSet.getString("xml");
                            
                            if (xml.isEmpty()) {
                                cfdiWithOutXml++;
                            }
                            else {
                                cfd.ver33.DElementComprobante comprobante33 = null;
                                
                                try {
                                    comprobante33 = DCfdUtils.getCfdi33(xml);
                                }
                                catch (Exception e) {
                                    SLibUtils.printException(this, e);
                                }
                                
                                if (comprobante33 != null) {
                                    SCfdUtilsHandler.CfdiAckQuery cfdiAckQuery = new SCfdUtilsHandler(miClient).getCfdiSatStatus(comprobante33);

                                    switch (cfdiAckQuery.CfdiStatus){
                                        case DCfdi33Consts.CFDI_ESTATUS_VIG:
                                            cfdiValid++;
                                            break;
                                        case DCfdi33Consts.CFDI_ESTATUS_CAN:
                                            cfdiCanceled++;
                                            break;
                                        case DCfdi33Consts.CFDI_ESTATUS_NO_ENC:
                                            cfdiNotFound++;
                                            break;
                                        default:
                                            // do nothing
                                    }

                                    if (cfdType == SDataConstantsSys.TRNS_TP_CFD_INV) {
                                        addResultPaneRow(docType, docNumber, resultSet.getDate("c.ts"), resultSet.getString("_bp_issuer"), resultSet.getString("c.xml_rfc_emi"), uuid, cfdiAckQuery);
                                    }
                                    else {
                                        addResultPaneRow(docType, docNumber, resultSet.getDate("c.ts"), resultSet.getString("_bp_receiver"), resultSet.getString("c.xml_rfc_rec"), uuid, cfdiAckQuery);
                                    }
                                }
                            }
                        }
                        
                        moResultPane.renderTableRows();
                        moResultPane.setTableRowSelection(0);
                        
                        if (!results) {
                            miClient.showMsgBoxInformation("No se encontraron CFDI a validar para los parámetros proporcionados.");
                        }
                        else {
                            miClient.showMsgBoxInformation("Se validaron un total de " + cfdiTotal + " CFDI, de los cuales resultaron:\n" 
                                    + (cfdiValid == 0 ? "" : cfdiValid + " vigentes\n") + (cfdiCanceled == 0 ? "" : cfdiCanceled + " cancelados\n") + (cfdiNotFound == 0 ? "" : cfdiNotFound + " no encontrados\n")
                                    + (cfdiWithOutXml == 0 ? "" : cfdiWithOutXml + " sin XML."));
                        }
                    }
                    
                    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
                catch (Exception e) {
                    miClient.showMsgBoxWarning(e.getMessage());
                    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        }
    }
    
    private void addResultPaneRow(final String docType, final String docNumber, final Date docDate, final String bpName, final String bpFiscalId, final String uuid, final SCfdUtilsHandler.CfdiAckQuery cfdiAckQuery){
        SRowCfdSatStatus row = new SRowCfdSatStatus(docType, docNumber, docDate, bpName, bpFiscalId, uuid, cfdiAckQuery);
        moResultPane.addTableRow(row);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgCancelationType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbBizPartnerId;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbCheckButton;
    private javax.swing.JButton jbDateEnd;
    private javax.swing.JButton jbDateStart;
    private javax.swing.JComboBox jcbBizPartnerId;
    private javax.swing.JComboBox jcbCfdType;
    private javax.swing.JFormattedTextField jftDateEnd;
    private javax.swing.JFormattedTextField jftDateStart;
    private javax.swing.JLabel jlBizPartnerId;
    private javax.swing.JLabel jlCfdType;
    private javax.swing.JLabel jlDateEnd;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JPanel jpResultPane;
    private sa.lib.gui.bean.SBeanFieldRadio jrbAllVouchers;
    private sa.lib.gui.bean.SBeanFieldRadio jrbVoucherCancellableWithAcceptance;
    private sa.lib.gui.bean.SBeanFieldRadio jrbVoucherCancellableWithoutAcceptance;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        mvFields.stream().forEach((mvField) -> {
            ((erp.lib.form.SFormField) mvField).resetField();
        });

        moResultPane.createTable(null);
        moResultPane.clearTableRows();
        moFieldDateStart.setFieldValue(SLibTimeUtils.getBeginOfMonth(miClient.getSessionXXX().getWorkingDate()));
        moFieldDateFinal.setFieldValue(SLibTimeUtils.getEndOfMonth(miClient.getSessionXXX().getWorkingDate()));
        SFormUtilities.populateComboBox(miClient, jcbCfdType, SDataConstants.TRNS_TP_CFD);
        jrbAllVouchers.setSelected(true);
    }

    @Override
    public void formRefreshCatalogues() {
        if (((int[]) moFieldCfdType.getKey())[0] == SDataConstantsSys.TRNS_TP_CFD_PAYROLL) {
            SFormUtilities.populateComboBox(miClient, jcbBizPartnerId, SDataConstants.BPSX_BP_EMP);
            jlBizPartnerId.setText("Empleado:");
        }
        else if (mnModuleSubtype == SDataConstantsSys.TRNS_CT_DPS_PUR) {
            SFormUtilities.populateComboBox(miClient, jcbBizPartnerId, SDataConstants.BPSX_BP_SUP);
            jlBizPartnerId.setText("Emisor:");
        }
        else if (mnModuleSubtype == SDataConstantsSys.TRNS_CT_DPS_SAL) {
            SFormUtilities.populateComboBox(miClient, jcbBizPartnerId, SDataConstants.BPSX_BP_CUS);
            jlBizPartnerId.setText("Receptor:");
        }
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        SFormValidation validation = new SFormValidation();

        mbErrShowed = false;
        for (SFormField mvField : mvFields) {
            if (!((erp.lib.form.SFormField) mvField).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((erp.lib.form.SFormField) mvField).getComponent());
                mbErrShowed = true;
                break;
            }
        }

        if (!validation.getIsError()) {
            if (moFieldDateFinal.getDate().compareTo(moFieldDateStart.getDate()) < 0) {
                validation.setMessage("La fecha final debe ser mayor o igual a la fecha inicial.");
                validation.setComponent(jftDateEnd);
            }
        }
        
        return validation;
    }

    @Override
    public void setFormStatus(int status) {
        mnFormStatus = status;
    }

    @Override
    public void setFormVisible(boolean visible) {
        setVisible(visible);
    }

    @Override
    public int getFormStatus() {
        return mnFormStatus;
    }

    @Override
    public int getFormResult() {
        return mnFormResult;
    }

    @Override
    public void setRegistry(erp.lib.data.SDataRegistry registry) {
        
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public java.lang.Object getValue(int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JLabel getTimeoutLabel() {
        return null;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        int tipoCfd = ((int[]) moFieldCfdType.getKey())[0];
        switch (tipoCfd) {
            case SDataConstantsSys.TRNS_TP_CFD_PAY_REC: 
            case SDataConstantsSys.TRNS_TP_CFD_INV: 
                jrbAllVouchers.setEnabled(true);
                jrbVoucherCancellableWithoutAcceptance.setEnabled(true);
                jrbVoucherCancellableWithAcceptance.setEnabled(true);
                formRefreshCatalogues();
                break;    
            case SDataConstantsSys.TRNS_TP_CFD_PAYROLL: 
                jrbAllVouchers.setEnabled(false);
                jrbVoucherCancellableWithoutAcceptance.setEnabled(true);
                jrbVoucherCancellableWithoutAcceptance.setSelected(true); 
                jrbVoucherCancellableWithAcceptance.setEnabled(false);
                formRefreshCatalogues();
                break;
            default:
        }
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbCancel) {
                actionCancel();
            }
            else if (button == jbDateStart) {
                actionDateStart();
            }
            else if (button == jbDateEnd) {
                actionDateFinal();
            }
            else if (button == jbBizPartnerId) {
                actionBizPartnerId();
            }
            else if (button == jbCheckButton) {
                actionValidateButton();
            }
        }
    }
}
