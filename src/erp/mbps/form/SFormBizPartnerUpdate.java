/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mbps.form;

import erp.cfd.SCfdXmlCatalogs;
import erp.data.SDataConstantsSys;
import erp.gui.session.SSessionCustom;
import erp.lib.SLibConstants;
import erp.lib.form.SFormComponentItem;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerUpdateLogEntry;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;

/**
 *
 * @author Claudio Peña, Sergio Flores
 */
public class SFormBizPartnerUpdate extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {
    
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mbps.data.SDataBizPartner moBizPartner;
    
    private erp.lib.form.SFormField moFieldBizPartnerCommercial;
    private erp.lib.form.SFormField moFieldEmail; 
    private erp.lib.form.SFormField moFieldTaxRegime;
    private erp.lib.form.SFormField moFieldLeadTime;

    private int mnBizPartnerCategory;
    private String msOldBizPartnerCommercial;
    private String msOldEmail01;
    private String msOldTaxRegime;
    private int mnOldLeadTime;
    
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

        jpBizPartner = new javax.swing.JPanel();
        jpBizPartner11 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        jtfBizPartner = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        jlFiscalId = new javax.swing.JLabel();
        jtfFiscalId = new javax.swing.JTextField();
        jPanel35 = new javax.swing.JPanel();
        jlFiscalFrgId = new javax.swing.JLabel();
        jtfFiscalFrgId = new javax.swing.JTextField();
        jPanel34 = new javax.swing.JPanel();
        jlBizPartnerCommercial = new javax.swing.JLabel();
        jtfBizPartnerCommercial = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jlOldBizPartnerCommercial = new javax.swing.JLabel();
        jtfOldBizPartnerCommercial = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jlEmail = new javax.swing.JLabel();
        jtfEmail = new javax.swing.JTextField();
        jlAddingMultipleMailHelp = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jlOldEmail = new javax.swing.JLabel();
        jtfOldEmail = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        jlTaxRegime = new javax.swing.JLabel();
        jcbTaxRegime = new javax.swing.JComboBox<SFormComponentItem>();
        jPanel3 = new javax.swing.JPanel();
        jlOldTaxRegime = new javax.swing.JLabel();
        jtfOldTaxRegime = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jlLeadTime = new javax.swing.JLabel();
        jtfLeadTime = new javax.swing.JTextField();
        jlLeadTimeDays = new javax.swing.JLabel();
        jlLeadTimeHint = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jlOldLeadTime = new javax.swing.JLabel();
        jtfOldLeadTime = new javax.swing.JTextField();
        jlOldLeadTimeDays = new javax.swing.JLabel();
        jpCommands = new javax.swing.JPanel();
        jpCommands1 = new javax.swing.JPanel();
        jtfPkBizPartnerId_Ro = new javax.swing.JTextField();
        jpCommands2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Actualización de datos");
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

        jpBizPartner11.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del asociado de negocios:"));
        jpBizPartner11.setLayout(new java.awt.GridLayout(11, 1, 0, 5));

        jPanel32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setText("Nombre o razón social:");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel32.add(jlBizPartner);

        jtfBizPartner.setEditable(false);
        jtfBizPartner.setText("TEXT");
        jtfBizPartner.setFocusable(false);
        jtfBizPartner.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel32.add(jtfBizPartner);

        jpBizPartner11.add(jPanel32);

        jPanel33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFiscalId.setText("RFC: ");
        jlFiscalId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel33.add(jlFiscalId);

        jtfFiscalId.setEditable(false);
        jtfFiscalId.setText("TEXT");
        jtfFiscalId.setFocusable(false);
        jtfFiscalId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel33.add(jtfFiscalId);

        jpBizPartner11.add(jPanel33);

        jPanel35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFiscalFrgId.setText("ID fiscal:");
        jlFiscalFrgId.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel35.add(jlFiscalFrgId);

        jtfFiscalFrgId.setEditable(false);
        jtfFiscalFrgId.setText("TEXT");
        jtfFiscalFrgId.setFocusable(false);
        jtfFiscalFrgId.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel35.add(jtfFiscalFrgId);

        jpBizPartner11.add(jPanel35);

        jPanel34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerCommercial.setText("Nombre comercial:");
        jlBizPartnerCommercial.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel34.add(jlBizPartnerCommercial);

        jtfBizPartnerCommercial.setText("TEXT");
        jtfBizPartnerCommercial.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel34.add(jtfBizPartnerCommercial);

        jpBizPartner11.add(jPanel34);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOldBizPartnerCommercial.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel1.add(jlOldBizPartnerCommercial);

        jtfOldBizPartnerCommercial.setEditable(false);
        jtfOldBizPartnerCommercial.setForeground(java.awt.SystemColor.textInactiveText);
        jtfOldBizPartnerCommercial.setText("TEXT");
        jtfOldBizPartnerCommercial.setToolTipText("Valor original");
        jtfOldBizPartnerCommercial.setFocusable(false);
        jtfOldBizPartnerCommercial.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel1.add(jtfOldBizPartnerCommercial);

        jpBizPartner11.add(jPanel1);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEmail.setForeground(new java.awt.Color(0, 102, 102));
        jlEmail.setText("Cuenta(s) correo-e:");
        jlEmail.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel11.add(jlEmail);

        jtfEmail.setText("TEXT");
        jtfEmail.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel11.add(jtfEmail);

        jlAddingMultipleMailHelp.setForeground(new java.awt.Color(109, 109, 109));
        jlAddingMultipleMailHelp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlAddingMultipleMailHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_view_help.png"))); // NOI18N
        jlAddingMultipleMailHelp.setToolTipText("Separar varias cuentas con \";\", sin espacios, p. ej., \"cuenta1@mail.com;cuenta2@mail.com\"");
        jlAddingMultipleMailHelp.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jlAddingMultipleMailHelp);

        jpBizPartner11.add(jPanel11);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOldEmail.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel2.add(jlOldEmail);

        jtfOldEmail.setEditable(false);
        jtfOldEmail.setForeground(java.awt.SystemColor.textInactiveText);
        jtfOldEmail.setText("TEXT");
        jtfOldEmail.setToolTipText("Valor original");
        jtfOldEmail.setFocusable(false);
        jtfOldEmail.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel2.add(jtfOldEmail);

        jpBizPartner11.add(jPanel2);

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTaxRegime.setText("Régimen fiscal para CFDI:");
        jlTaxRegime.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel23.add(jlTaxRegime);

        jcbTaxRegime.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel23.add(jcbTaxRegime);

        jpBizPartner11.add(jPanel23);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOldTaxRegime.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel3.add(jlOldTaxRegime);

        jtfOldTaxRegime.setEditable(false);
        jtfOldTaxRegime.setForeground(java.awt.SystemColor.textInactiveText);
        jtfOldTaxRegime.setText("TEXT");
        jtfOldTaxRegime.setToolTipText("Valor original");
        jtfOldTaxRegime.setFocusable(false);
        jtfOldTaxRegime.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel3.add(jtfOldTaxRegime);

        jpBizPartner11.add(jPanel3);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLeadTime.setText("Plazo de entrega:");
        jlLeadTime.setPreferredSize(new java.awt.Dimension(150, 23));
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

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOldLeadTime.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel4.add(jlOldLeadTime);

        jtfOldLeadTime.setEditable(false);
        jtfOldLeadTime.setForeground(java.awt.SystemColor.textInactiveText);
        jtfOldLeadTime.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfOldLeadTime.setText("0");
        jtfOldLeadTime.setToolTipText("Valor original");
        jtfOldLeadTime.setFocusable(false);
        jtfOldLeadTime.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel4.add(jtfOldLeadTime);

        jlOldLeadTimeDays.setForeground(java.awt.SystemColor.textInactiveText);
        jlOldLeadTimeDays.setText("días");
        jlOldLeadTimeDays.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel4.add(jlOldLeadTimeDays);

        jpBizPartner11.add(jPanel4);

        jpBizPartner.add(jpBizPartner11, java.awt.BorderLayout.NORTH);

        getContentPane().add(jpBizPartner, java.awt.BorderLayout.CENTER);

        jpCommands.setPreferredSize(new java.awt.Dimension(300, 33));
        jpCommands.setLayout(new java.awt.GridLayout(1, 2));

        jpCommands1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jtfPkBizPartnerId_Ro.setEditable(false);
        jtfPkBizPartnerId_Ro.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfPkBizPartnerId_Ro.setToolTipText("ID del registro");
        jtfPkBizPartnerId_Ro.setFocusable(false);
        jtfPkBizPartnerId_Ro.setPreferredSize(new java.awt.Dimension(65, 23));
        jpCommands1.add(jtfPkBizPartnerId_Ro);

        jpCommands.add(jpCommands1);

        jpCommands2.setMinimumSize(new java.awt.Dimension(161, 20));
        jpCommands2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCommands2.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jpCommands2.add(jbCancel);

        jpCommands.add(jpCommands2);

        getContentPane().add(jpCommands, java.awt.BorderLayout.SOUTH);

        getAccessibleContext().setAccessibleParent(this);

        setSize(new java.awt.Dimension(656, 439));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivate();
    }//GEN-LAST:event_formWindowActivated
    
    private void initComponentsExtra() {
        mvFields = new Vector<>();

        moFieldBizPartnerCommercial = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfBizPartnerCommercial, jlBizPartnerCommercial);
        moFieldBizPartnerCommercial.setLengthMax(202);
        moFieldEmail = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfEmail, jlEmail);
        moFieldEmail.setLengthMax(255);
        moFieldEmail.setAutoCaseType(SLibConstants.UNDEFINED);
        moFieldLeadTime = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, false, jtfLeadTime, jlLeadTime);
        moFieldTaxRegime = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, false, jcbTaxRegime, jlTaxRegime);

        mvFields.add(moFieldBizPartnerCommercial);
        mvFields.add(moFieldEmail);
        mvFields.add(moFieldTaxRegime);
        mvFields.add(moFieldLeadTime);
        
        moXmlCatalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        moXmlCatalogs.populateComboBox(jcbTaxRegime, SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, miClient.getSession().getSystemDate());
        
        jbOk.addActionListener(this);
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

    private void actionOk() {
        SFormValidation validation = formValidate();

        if (validation.getIsError()) {
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
            if (validation.getMessage().length() > 0) {
                miClient.showMsgBoxWarning(validation.getMessage());
            }
        }
        else {
            mnFormResult = SLibConstants.FORM_RESULT_OK;
            setVisible(false);
        }
    }

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    public int getFormResult() {
        return mnFormResult;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JComboBox<SFormComponentItem> jcbTaxRegime;
    private javax.swing.JLabel jlAddingMultipleMailHelp;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlBizPartnerCommercial;
    private javax.swing.JLabel jlEmail;
    private javax.swing.JLabel jlFiscalFrgId;
    private javax.swing.JLabel jlFiscalId;
    private javax.swing.JLabel jlLeadTime;
    private javax.swing.JLabel jlLeadTimeDays;
    private javax.swing.JLabel jlLeadTimeHint;
    private javax.swing.JLabel jlOldBizPartnerCommercial;
    private javax.swing.JLabel jlOldEmail;
    private javax.swing.JLabel jlOldLeadTime;
    private javax.swing.JLabel jlOldLeadTimeDays;
    private javax.swing.JLabel jlOldTaxRegime;
    private javax.swing.JLabel jlTaxRegime;
    private javax.swing.JPanel jpBizPartner;
    private javax.swing.JPanel jpBizPartner11;
    private javax.swing.JPanel jpCommands;
    private javax.swing.JPanel jpCommands1;
    private javax.swing.JPanel jpCommands2;
    private javax.swing.JTextField jtfBizPartner;
    private javax.swing.JTextField jtfBizPartnerCommercial;
    private javax.swing.JTextField jtfEmail;
    private javax.swing.JTextField jtfFiscalFrgId;
    private javax.swing.JTextField jtfFiscalId;
    private javax.swing.JTextField jtfLeadTime;
    private javax.swing.JTextField jtfOldBizPartnerCommercial;
    private javax.swing.JTextField jtfOldEmail;
    private javax.swing.JTextField jtfOldLeadTime;
    private javax.swing.JTextField jtfOldTaxRegime;
    private javax.swing.JTextField jtfPkBizPartnerId_Ro;
    // End of variables declaration//GEN-END:variables

    @Override
    public void formClearRegistry() {

    }

    @Override
    public void formReset() {
        mnFormResult = SLibConstants.UNDEFINED;
        mnFormStatus = SLibConstants.UNDEFINED;
        mbFirstTime = true;

        moBizPartner = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        jtfBizPartner.setText("");
        jtfFiscalId.setText("");
        jtfFiscalFrgId.setText("");
        jtfPkBizPartnerId_Ro.setText("");

        msOldBizPartnerCommercial = "";
        msOldEmail01 = "";
        msOldTaxRegime = "";
        mnOldLeadTime = 0;
    
        moXmlCatalogs = ((SSessionCustom) miClient.getSession().getSessionCustom()).getCfdXmlCatalogs();
        moXmlCatalogs.populateComboBox(jcbTaxRegime, SDataConstantsSys.TRNS_CFD_CAT_TAX_REG, miClient.getSession().getSystemDate());
    }

    @Override
    public void formRefreshCatalogues() {

    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {        
        SFormValidation validation = new SFormValidation();
        
        for (int i = 0; i < mvFields.size(); i++) {
            if (!((erp.lib.form.SFormField) mvFields.get(i)).validateField()) {
                validation.setIsError(true);
                validation.setComponent(((erp.lib.form.SFormField) mvFields.get(i)).getComponent());
                break;
            }
        }
        
        if (!validation.getIsError()) {
            boolean noChanges = moFieldBizPartnerCommercial.getString().equals(msOldBizPartnerCommercial) &&
                    moFieldEmail.getString().equals(msOldEmail01) &&
                    (msOldTaxRegime.isEmpty() && jcbTaxRegime.getSelectedIndex() <= 0 || moFieldTaxRegime.getString().equals(msOldTaxRegime)) &&
                    moFieldLeadTime.getInteger() == mnOldLeadTime;
            
            if (noChanges) {
                validation.setMessage("Todos los nuevos valores son iguales a los anteriores.\n"
                        + "Si no es necesario actualizarlos, dé clik en '" + jbCancel.getText() + "'.");
                validation.setComponent(jtfBizPartnerCommercial);
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
        
        jtfBizPartner.setText(moBizPartner.getBizPartner());
        jtfFiscalId.setText(moBizPartner.getFiscalId());
        jtfFiscalFrgId.setText(moBizPartner.getFiscalFrgId());
        jtfPkBizPartnerId_Ro.setText("" + moBizPartner.getPkBizPartnerId());
        
        jtfBizPartner.setCaretPosition(0);
        jtfFiscalId.setCaretPosition(0);
        jtfFiscalFrgId.setCaretPosition(0);
        jtfPkBizPartnerId_Ro.setCaretPosition(0);
        
        // preseve old values:
        
        msOldBizPartnerCommercial = moBizPartner.getBizPartner().equals(moBizPartner.getBizPartnerCommercial()) ? "" : moBizPartner.getBizPartnerCommercial();
        msOldEmail01 = moBizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchContactOfficial().getEmail01();
        msOldTaxRegime = moBizPartner.getDbmsCategorySettingsSup().getTaxRegime();
        mnOldLeadTime = moBizPartner.getDbmsCategorySettingsSup().getLeadTime();
        
        // show editable values:
        
        moFieldBizPartnerCommercial.setFieldValue(msOldBizPartnerCommercial);
        moFieldEmail.setFieldValue(msOldEmail01);
        moFieldTaxRegime.setFieldValue(msOldTaxRegime);
        moFieldLeadTime.setFieldValue(mnOldLeadTime);
        
        // show old values:
        
        jtfOldBizPartnerCommercial.setText(msOldBizPartnerCommercial);
        jtfOldEmail.setText(msOldEmail01);
        jtfOldTaxRegime.setText(msOldTaxRegime);
        jtfOldLeadTime.setText("" + mnOldLeadTime);
        
        jtfOldBizPartnerCommercial.setCaretPosition(0);
        jtfOldEmail.setCaretPosition(0);
        jtfOldTaxRegime.setCaretPosition(0);
        jtfOldLeadTime.setCaretPosition(0);
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        SDataBizPartnerUpdateLogEntry entry = new SDataBizPartnerUpdateLogEntry();
        
        entry.setPkBizPartnerId(moBizPartner.getPkBizPartnerId());
        //entry.setPkLogId(...);
        entry.setBizPartnerCommercial(moFieldBizPartnerCommercial.getString().isEmpty() ? moBizPartner.getBizPartner() : moFieldBizPartnerCommercial.getString());
        entry.setEmail01(moFieldEmail.getString());
        entry.setTaxRegime(jcbTaxRegime.getSelectedIndex() <= 0 ? "" : moFieldTaxRegime.getKey().toString());
        entry.setLeadTime(moFieldLeadTime.getInteger());
        entry.setFkUserUpdate(miClient.getSession().getUser().getPkUserId());
        //entry.setUserUpdateTs(...);

        entry.setOldBizPartnerCommercial(msOldBizPartnerCommercial);
        entry.setOldEmail01(msOldEmail01);
        entry.setOldTaxRegime(msOldTaxRegime);
        entry.setOldLeadTime(mnOldLeadTime);

        entry.setAuxBizPartnerCategoryId(mnBizPartnerCategory);
        
        return entry;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case SDataConstantsSys.VALUE_BIZ_PARTNER_CATEGORY:
                mnBizPartnerCategory = SDataConstantsSys.BPSS_CT_BP_SUP;
                break;
            default:
                // nothing
        }
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
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
        }
    }
}
