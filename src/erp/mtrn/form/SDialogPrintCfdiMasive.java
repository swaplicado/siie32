/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogPrintCfdiMasive.java
 *
 * Created on 10/04/2017 
 */

package erp.mtrn.form;

import erp.SClient;
import erp.cfd.SCfdConsts;
import erp.cfd.SDialogCfdProcessing;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author Alfredo Pérez, Sergio Flores
 */
public class SDialogPrintCfdiMasive extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;
    private int[] manKey;

    private erp.lib.form.SFormField moFieldDateStart;
    private erp.lib.form.SFormField moFieldNumberInitial;
    private erp.lib.form.SFormField moFieldNumberEnd;

    /** Creates new form SDialogPrintCfdiMasive
     * @param client
     * @param key */
    public SDialogPrintCfdiMasive(erp.client.SClientInterface client, int[] key) {
        super(client.getFrame(), true);
        miClient =  client;
        manKey = key;
        
        initComponents();
        initComponentsExtra();
        
        formRefreshCatalogues();
        formReset();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupRangeOptions = new javax.swing.ButtonGroup();
        buttonGroupCurrencyOptions = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jbPrint = new javax.swing.JButton();
        jbExit = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlDateStart = new javax.swing.JLabel();
        jftDateStart = new javax.swing.JFormattedTextField();
        jbDateStart = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlNumberInitial = new javax.swing.JLabel();
        jtfNumberInitial = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jlNumberEnd = new javax.swing.JLabel();
        jtfNumberEnd = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Impresión masiva de CFDI");
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(300, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbPrint.setText("Imprimir");
        jbPrint.setToolTipText("[Ctrl + Enter]");
        jbPrint.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbPrint);

        jbExit.setText("Cerrar");
        jbExit.setToolTipText("[Escape]");
        jbExit.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbExit);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración de impresión:"));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(5, 1));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateStart.setText("Fecha: *");
        jlDateStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlDateStart);

        jftDateStart.setText("dd/mm/yyyy");
        jftDateStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jftDateStart);

        jbDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_date_day.gif"))); // NOI18N
        jbDateStart.setToolTipText("Seleccionar fecha inicial");
        jbDateStart.setFocusable(false);
        jbDateStart.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel3.add(jbDateStart);

        jPanel2.add(jPanel3);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel2.add(jPanel7);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNumberInitial.setText("Folio inicial: *");
        jlNumberInitial.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlNumberInitial);

        jtfNumberInitial.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNumberInitial.setPreferredSize(new java.awt.Dimension(75, 23));
        jtfNumberInitial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNumberInitialActionPerformed(evt);
            }
        });
        jPanel4.add(jtfNumberInitial);

        jPanel2.add(jPanel4);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlNumberEnd.setText("Folio final: *");
        jlNumberEnd.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlNumberEnd);

        jtfNumberEnd.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfNumberEnd.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel6.add(jtfNumberEnd);

        jPanel2.add(jPanel6);

        jPanel8.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel8, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(336, 239));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jtfNumberInitialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfNumberInitialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtfNumberInitialActionPerformed

    public void initComponentsExtra() {
        mvFields = new Vector<SFormField>();

        moFieldDateStart = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateStart, jlDateStart);
        moFieldDateStart.setPickerButton(jbDateStart);
        moFieldNumberInitial = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfNumberInitial, jlNumberInitial);
        moFieldNumberEnd = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfNumberEnd, jlNumberEnd); 
        
        mvFields.add(moFieldDateStart);
        mvFields.add(moFieldNumberInitial);
        mvFields.add(moFieldNumberEnd);

        jbPrint.addActionListener(this);
        jbExit.addActionListener(this);
        jbDateStart.addActionListener(this);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionPrint(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "print", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionClose(); }
        };

        SFormUtilities.putActionMap(getRootPane(), action, "exit", KeyEvent.VK_ESCAPE, 0);

        setModalityType(ModalityType.MODELESS);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jftDateStart.requestFocus();
        }
    }
    
    private void actionPrint() {
        String[] asNumberDpsInitial = null;
        String [] asNumberDpsEnd = null;
        String serieIni = "";
        String serieEnd = "";
        String folioIni = "";
        String folioEnd = "";
        ArrayList<int[]> keysDps = null;
        SFormValidation validation = formValidate();
        ArrayList<SDataCfd> cfds ;
        
        keysDps = new ArrayList<>();
        cfds = null;
        
        if (validation.getIsError()) {
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
            if (validation.getMessage().length() > 0) {
                miClient.showMsgBoxWarning(validation.getMessage());
            }
        }
        else {
            asNumberDpsInitial = SLibUtilities.textExplode(moFieldNumberInitial.getString(), "-");
            asNumberDpsEnd = SLibUtilities.textExplode(moFieldNumberEnd.getString(), "-");

            if (asNumberDpsInitial.length > 1) {
                serieIni = asNumberDpsInitial[0];
                folioIni = asNumberDpsInitial[1];
            }
            else {
                serieIni = "";
                folioIni = asNumberDpsInitial[0];
            }

            if (asNumberDpsEnd.length > 1) {
                serieEnd = asNumberDpsEnd[0];
                folioEnd = asNumberDpsEnd[1];
            }
            else {
                serieEnd = "";
                folioEnd = asNumberDpsEnd[0];
            }
            
            try {
                keysDps = SDataUtilities.obtainDpsIds(miClient, serieIni, serieEnd, folioIni, folioEnd, moFieldDateStart.getDate(), manKey);
                cfds = SCfdUtils.getCfds(miClient, SDataConstantsSys.TRNS_TP_CFD_INV, SLibConstants.UNDEFINED, keysDps);
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }

            actionClose();
            
            SDialogCfdProcessing dialog = new SDialogCfdProcessing((SClient) miClient, "Procesamiento de impresión", SCfdConsts.REQ_PRINT_DOC);
            dialog.setFormParams((SClientInterface) miClient, cfds, null, 0, null, true, SLibConstants.UNDEFINED, SLibConstants.UNDEFINED);
            dialog.setVisible(true);
        }
    }

    private void actionClose() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void actionDateInitial() {
        miClient.getGuiDatePickerXXX().formReset();
        miClient.getGuiDatePickerXXX().setDate(moFieldDateStart.getDate());
        miClient.getGuiDatePickerXXX().setVisible(true);

        if (miClient.getGuiDatePickerXXX().getFormResult() == SLibConstants.FORM_RESULT_OK) {
            moFieldDateStart.setDate(miClient.getGuiDatePickerXXX().getGuiDate());
            jftDateStart.requestFocus();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroupCurrencyOptions;
    private javax.swing.ButtonGroup buttonGroupRangeOptions;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbDateStart;
    private javax.swing.JButton jbExit;
    private javax.swing.JButton jbPrint;
    private javax.swing.JFormattedTextField jftDateStart;
    private javax.swing.JLabel jlDateStart;
    private javax.swing.JLabel jlNumberEnd;
    private javax.swing.JLabel jlNumberInitial;
    private javax.swing.JTextField jtfNumberEnd;
    private javax.swing.JTextField jtfNumberInitial;
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
        
        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }
        moFieldDateStart.setFieldValue(miClient.getSessionXXX().getWorkingDate());
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
        throw new UnsupportedOperationException("Not supported yet.");
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
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();
            if (button == jbPrint) {
                actionPrint();
            }
            else if (button == jbExit) {
                actionClose();
            }
            else if (button == jbDateStart) {
                actionDateInitial();
            }
        }
    }
}