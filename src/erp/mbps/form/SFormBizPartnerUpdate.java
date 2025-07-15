/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.form;

import cfd.DCfdConsts;
import erp.cfd.SCfdXmlCatalogs;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataRegistry;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STablePane;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerCategory;
import erp.mbps.data.SDataBizPartnerUpdateLog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import sa.lib.SLibMethod;
import sa.lib.SLibUtils;

/**
 *
 * @author Claudio Peña
 */
public class SFormBizPartnerUpdate extends javax.swing.JDialog implements erp.lib.form.SFormExtendedInterface, java.awt.event.ActionListener {
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private java.util.Vector<erp.lib.form.SFormField> mvFieldsCategory;
    private erp.client.SClientInterface miClient;
    private int supplierBatrix = 1;
    private int supplierType = 2;
    

    private erp.mbps.data.SDataBizPartner moBizPartner;
    private erp.lib.form.SFormField moFieldBizPartner;
    private erp.lib.form.SFormField moFieldBizPartnerCommercial;
    private erp.lib.form.SFormField moFieldFiscalId;
    private erp.lib.form.SFormField moFieldEmail; 
    private erp.lib.form.SFormField moFieldLeadTime;
    private erp.lib.form.SFormField moFieldTaxRegime;

    private erp.mbps.data.SDataBizPartnerCategory moBizPartnerCategory;

    private erp.lib.table.STablePane moBizPartnerBranchPane;

    private erp.mbps.data.SDataBizPartnerCategory[] maoDbmsCategorySettings;
    private erp.lib.table.STablePane moCusBranchConfigPane;

    private int mnParamBizPartnerCategory;

    private int mnFormTypeExport;
    private boolean mbIsNeededPosSave;
    
    private String msTempPaymentAccount;
    private int mnTempFkPaymentSystemTypeId;
    
    private SCfdXmlCatalogs moXmlCatalogs;
    
    /**
     * @param client GUI client.
     */
    public SFormBizPartnerUpdate(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        initComponents();
        initComponentsExtra();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgGuaranteeType = new javax.swing.ButtonGroup();
        bgOrgNamesPolicy = new javax.swing.ButtonGroup();
        jpBizPartner = new javax.swing.JPanel();
        jpBizPartner11 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        jtfBizPartner = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        jlFiscalId = new javax.swing.JLabel();
        jtfFiscalId = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jlBizPartnerCommercial = new javax.swing.JLabel();
        jtfBizPartnerCommercial = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jlEmail = new javax.swing.JLabel();
        jtfEmail = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        jlTaxRegime = new javax.swing.JLabel();
        jcbTaxRegime = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel10 = new javax.swing.JPanel();
        jlLeadTime = new javax.swing.JLabel();
        jtfLeadTime = new javax.swing.JTextField();
        jlLeadTimeDays = new javax.swing.JLabel();
        jlLeadTimeHint = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Datos proveedores");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(300, 250));
        setPreferredSize(new java.awt.Dimension(320, 200));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new java.awt.BorderLayout(0, 1));

        jpBizPartner.setPreferredSize(new java.awt.Dimension(300, 200));
        jpBizPartner.setLayout(new java.awt.BorderLayout());

        jpBizPartner11.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del proveedor:"));
        jpBizPartner11.setLayout(new java.awt.GridLayout(6, 1, 0, 1));

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setText("Proveedor: ");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel32.add(jlBizPartner);

        jtfBizPartner.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel32.add(jtfBizPartner);

        jpBizPartner11.add(jPanel32);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFiscalId.setText("RFC: ");
        jlFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel33.add(jlFiscalId);

        jtfFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel33.add(jtfFiscalId);

        jpBizPartner11.add(jPanel33);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerCommercial.setText("Nombre comercial:");
        jlBizPartnerCommercial.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel34.add(jlBizPartnerCommercial);

        jtfBizPartnerCommercial.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel34.add(jtfBizPartnerCommercial);

        jpBizPartner11.add(jPanel34);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEmail.setText("Cuenta(s) correo(s):");
        jlEmail.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlEmail);

        jtfEmail.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel11.add(jtfEmail);

        jpBizPartner11.add(jPanel11);

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTaxRegime.setText("Régimen fiscal:");
        jlTaxRegime.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel23.add(jlTaxRegime);

        jcbTaxRegime.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel23.add(jcbTaxRegime);

        jpBizPartner11.add(jPanel23);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLeadTime.setText("Plazo de entrega:");
        jlLeadTime.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlLeadTime);

        jtfLeadTime.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfLeadTime.setText("0");
        jtfLeadTime.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel10.add(jtfLeadTime);

        jlLeadTimeDays.setForeground(java.awt.SystemColor.textInactiveText);
        jlLeadTimeDays.setText("días");
        jlLeadTimeDays.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel10.add(jlLeadTimeDays);

        jlLeadTimeHint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlLeadTimeHint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlLeadTimeHint.setToolTipText("Plazo de entrega en días naturales promedio del asociado de negocios");
        jlLeadTimeHint.setPreferredSize(new java.awt.Dimension(15, 23));
        jPanel10.add(jlLeadTimeHint);

        jpBizPartner11.add(jPanel10);

        jpBizPartner.add(jpBizPartner11, java.awt.BorderLayout.WEST);

        getContentPane().add(jpBizPartner, java.awt.BorderLayout.PAGE_START);

        jPanel16.setPreferredSize(new java.awt.Dimension(300, 33));
        jPanel16.setLayout(new java.awt.GridLayout(1, 1));

        jPanel42.setMinimumSize(new java.awt.Dimension(161, 20));
        jPanel42.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel42.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel42.add(jbCancel);

        jPanel16.add(jPanel42);

        getContentPane().add(jPanel16, java.awt.BorderLayout.SOUTH);

        getAccessibleContext().setAccessibleParent(this);

        setSize(new java.awt.Dimension(576, 389));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivate();
    }//GEN-LAST:event_formWindowActivated
    
    private void initComponentsExtra() {
        mvFields = new Vector<>();
        mvFieldsCategory = new Vector<>();

        moCusBranchConfigPane = new STablePane(miClient);

        moBizPartnerBranchPane = new STablePane(miClient);

        maoDbmsCategorySettings = new SDataBizPartnerCategory[SDataConstantsSys.BPSX_CT_BP_QTY];

        moFieldBizPartner = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfBizPartner, jlBizPartner);
        moFieldBizPartner.setLengthMax(202);
        moFieldBizPartnerCommercial = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfBizPartnerCommercial, jlBizPartnerCommercial);
        moFieldBizPartnerCommercial.setLengthMax(202);
        moFieldFiscalId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfFiscalId, jlFiscalId);
        moFieldFiscalId.setLengthMin(DCfdConsts.LEN_RFC_ORG);
        moFieldFiscalId.setLengthMax(DCfdConsts.LEN_RFC_PER);
        moFieldEmail = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfEmail, jlEmail);
        moFieldEmail.setLengthMax(255);
        moFieldEmail.setAutoCaseType(SLibConstants.UNDEFINED);
        moFieldLeadTime = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfLeadTime, jlLeadTime);
        moFieldTaxRegime = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbTaxRegime, jlTaxRegime);

        mvFields.add(moFieldBizPartnerCommercial);
        mvFieldsCategory.add(moFieldLeadTime);
        mvFieldsCategory.add(moFieldTaxRegime);
        mvFields.add(moFieldEmail);
        
        jtfFiscalId.setEnabled(false);
        jlLeadTime.setEnabled(true);
        jtfLeadTime.setEnabled(true);
        jlLeadTimeDays.setEnabled(true);
        jlLeadTimeHint.setEnabled(true);
        jlTaxRegime.setEnabled(true);
        jcbTaxRegime.setEnabled(true);
        moXmlCatalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        moXmlCatalogs.populateComboBox(jcbTaxRegime, SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, miClient.getSession().getSystemDate());
        
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);

        jbCancel.addActionListener(this);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { try {
                actionOk();
                } catch (Exception ex) {
                    Logger.getLogger(SFormBizPartnerUpdate.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), action, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivate() {
        if (mbFirstTime) {
            mbFirstTime = false;
        }
    }

    @SuppressWarnings("unchecked")
    private void renderBizPartnerCategory() {
        
    }
    
    private void actionOk() throws Exception {
        if (mnFormResult == SLibConstants.FORM_RESULT_OK) {
            return;
        }
        erp.lib.form.SFormValidation validation = formValidate();

            if (validation.getIsError()) {
                miClient.showMsgBoxWarning(validation.getMessage());
                validation.getComponent().requestFocus();
                return;
            }

            Object pk = ((SFormComponentItem) jcbTaxRegime.getSelectedItem()).getPrimaryKey();
            String taxRegime;

            if (pk instanceof int[]) {
                int[] key = (int[]) pk;
                taxRegime = key.length > 0 ? String.valueOf(key[0]) : "";
            }
            else if (pk instanceof String) {
                taxRegime = (String) pk;
            }
            else {
                taxRegime = "";
            }
            SDataBizPartnerUpdateLog data = new SDataBizPartnerUpdateLog();
            data.saveFromForm(
                miClient,
                moBizPartner.getPkBizPartnerId(),
                jtfBizPartnerCommercial.getText(),
                jtfEmail.getText(),
                taxRegime,
                jtfLeadTime.getText(),
                miClient.getSession().getUser().getPkUserId()
            );

        miClient.showMsgBoxInformation("Se actualizaron los datos del proveedor.");
        setVisible(false);
    }

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    public int getFormResult() {
        return mnFormResult;
    }
    
    private boolean isModifyBizParther() {
        boolean isModify = true;

        try {
            String sql = "SELECT * FROM erp.bpsu_bp_upd_log WHERE id_bp = ?";
            java.sql.PreparedStatement ps = miClient.getSession().getStatement().getConnection().prepareStatement(sql);
            ps.setInt(1, moBizPartner.getPkBizPartnerId());
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                isModify = false;
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            SLibUtilities.renderException(this, e);
            isModify = true;
        }

        return isModify;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgGuaranteeType;
    private javax.swing.ButtonGroup bgOrgNamesPolicy;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JComboBox<SFormComponentItem> jcbTaxRegime;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlBizPartnerCommercial;
    private javax.swing.JLabel jlEmail;
    private javax.swing.JLabel jlFiscalId;
    private javax.swing.JLabel jlLeadTime;
    private javax.swing.JLabel jlLeadTimeDays;
    private javax.swing.JLabel jlLeadTimeHint;
    private javax.swing.JLabel jlTaxRegime;
    private javax.swing.JPanel jpBizPartner;
    private javax.swing.JPanel jpBizPartner11;
    private javax.swing.JTextField jtfBizPartner;
    private javax.swing.JTextField jtfBizPartnerCommercial;
    private javax.swing.JTextField jtfEmail;
    private javax.swing.JTextField jtfFiscalId;
    private javax.swing.JTextField jtfLeadTime;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {
        moBizPartnerCategory = null;
    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        moBizPartner = null;
        moBizPartnerCategory = null;

        mnFormTypeExport = SLibConstants.UNDEFINED;
        mbIsNeededPosSave = false;
        
        msTempPaymentAccount = "";
        mnTempFkPaymentSystemTypeId = SLibConstants.UNDEFINED;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }
        for (int i = 0; i < mvFieldsCategory.size(); i++) {
            ((erp.lib.form.SFormField) mvFieldsCategory.get(i)).resetField();
        }

        jtfBizPartner.setText("");
        jtfEmail.setText("");
        bgOrgNamesPolicy.clearSelection();
        jtfFiscalId.setText("");
        moBizPartnerBranchPane.createTable(null);
        moBizPartnerBranchPane.clearTableRows();
        jtfBizPartner.setEnabled(false);

        maoDbmsCategorySettings = new SDataBizPartnerCategory[SDataConstantsSys.BPSX_CT_BP_QTY];

        renderBizPartnerCategory();

        moCusBranchConfigPane.createTable(null);
        moCusBranchConfigPane.clearTableRows();
        
        jlTaxRegime.setEnabled(true);
        jcbTaxRegime.setEnabled(true);
        moXmlCatalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        moXmlCatalogs.populateComboBox(jcbTaxRegime, SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, miClient.getSession().getSystemDate());

    }

    @Override
    @SuppressWarnings("unchecked")
    public void formRefreshCatalogues() {
        int bizCategory = 0;

        switch (mnParamBizPartnerCategory) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
                bizCategory = SDataConstants.BPSX_TP_BP_SUP;
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
                bizCategory = SDataConstants.BPSX_TP_BP_CUS;
                break;
            default:
                break;
        }
        
        moXmlCatalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        moXmlCatalogs.populateComboBox(jcbTaxRegime, SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, miClient.getSession().getSystemDate());

    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {        
        SFormValidation validation = new SFormValidation();
        String BizPartnerCommercial = jtfBizPartnerCommercial.getText();
        String email = jtfEmail.getText().trim();
        String emailPattern = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        if (isModifyBizParther()) {
            for (int i = 0; i < mvFields.size(); i++) {
                if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                    validation.setIsError(true);
                    validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                    break;
                }
            }

            if(BizPartnerCommercial.isEmpty() && email.isEmpty() && (!java.util.regex.Pattern.matches(emailPattern, email)) && jcbTaxRegime.getSelectedIndex() <= 0) {
                String leadTime = jtfLeadTime.getText().trim();
                int leadTimeValue = Integer.parseInt(leadTime);

                try {
                    if (leadTime.isEmpty() || (leadTimeValue == 0) ) {
                      validation.setIsError(true);
                      validation.setComponent(jtfLeadTime);
                      validation.setMessage("Se debe de agregar al menos un valor o el campo plazo de entrega debe ser mayor a 0.");
                      return validation;
                    }
                } catch (NumberFormatException e) {
                    validation.setIsError(true);
                    validation.setComponent(jtfLeadTime);
                    validation.setMessage("El plazo de entrega debe ser un número entero (sin letras ni decimales).");
                    return validation;
                }
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
    public void setRegistry(erp.lib.data.SDataRegistry registry) {
        moBizPartner = (SDataBizPartner) registry;
        moFieldBizPartner.setFieldValue(moBizPartner.getBizPartner());
        moFieldBizPartnerCommercial.setFieldValue(moBizPartner.getBizPartner().equals(moBizPartner.getBizPartnerCommercial()) ? "" : moBizPartner.getBizPartnerCommercial());
        moFieldFiscalId.setFieldValue(moBizPartner.getFiscalId());
        moBizPartnerBranchPane.renderTableRows();
        moBizPartnerBranchPane.setTableRowSelection(0);
        
        maoDbmsCategorySettings[1] = moBizPartner.getDbmsCategorySettingsSup();

        moBizPartnerCategory = maoDbmsCategorySettings[1];
       
        moFieldLeadTime.setFieldValue(moBizPartnerCategory.getLeadTime());
        moFieldTaxRegime.setFieldValue(moBizPartnerCategory.getTaxRegime());
        msTempPaymentAccount = moBizPartnerCategory.getPaymentAccount();
        mnTempFkPaymentSystemTypeId = moBizPartnerCategory.getFkPaymentSystemTypeId_n();
        
        if (moBizPartner.getPkBizPartnerId() > 0) {
            try {
                String sql = "SELECT con.email_01 " +
                             "FROM erp.bpsu_bp AS b " +
                             "INNER JOIN erp.bpsu_bpb AS bp ON bp.fid_bp = b.id_bp " +
                             "INNER JOIN erp.BPSU_BPB_CON AS con ON con.id_bpb = bp.id_bpb " +
                             "INNER JOIN erp.BPSU_BP_CT AS bct ON bct.id_bp = b.id_bp " +
                             "WHERE NOT b.b_del " +
                             "AND bp.fid_tp_bpb = " + supplierBatrix + " "+ 
                             "AND con.id_con = " + supplierBatrix + " "+
                             "AND bct.id_ct_bp = " + supplierType + " "+
                             "AND b.id_bp = ?";
                java.sql.PreparedStatement ps = miClient.getSession().getStatement().getConnection().prepareStatement(sql);
                ps.setInt(1, moBizPartner.getPkBizPartnerId());
                java.sql.ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    moFieldEmail.setFieldValue(rs.getString("email_01"));
                } else {
                    moFieldEmail.setFieldValue("");
                }
                rs.close();
                ps.close();
            } catch (Exception e) {
                SLibUtilities.renderException(this, e);
                moFieldEmail.setFieldValue("");
            }
        }
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        return null;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
    }

    @Override
    public java.lang.Object getValue(int type) {
        Object value = null;
        return value;
    }

    @Override
    public javax.swing.JLabel getTimeoutLabel() {
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbOk) {
                try {
                    actionOk();
                } catch (Exception ex) {
                    Logger.getLogger(SFormBizPartnerUpdate.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (button == jbCancel) {
                actionCancel();
            }
        }
    }

    @Override
    public SLibMethod getPostSaveMethod(SDataRegistry registry) {
        SLibMethod method = null;

        if (mbIsNeededPosSave) {
            try {
                method = new SLibMethod(registry, registry.getClass().getMethod("openCategory", new Class[] { SClientInterface.class, int.class, Object.class }), new Object[] { miClient, mnFormTypeExport, registry.getPrimaryKey() });
            }
            catch (NoSuchMethodException | SecurityException e) {
                SLibUtils.showException(this, e);
            }
        }
        return method;
    }
}
