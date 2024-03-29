/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormCardIssuer.java
 *
 * Created on 15/10/2009, 12:35:32 PM
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mfin.data.SDataBankNbDay;
import erp.mfin.data.SFinUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.AbstractAction;
import sa.lib.SLibTimeUtils;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiUtils;

/**
 *
 * @author Isabel Servín
 */
public class SFormCopyBankNbDays extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mfin.data.SDataBankNbDay moDataBankNbDay;
    private erp.lib.form.SFormField moFieldYear;
    
    /** Creates new form SFormCardIssuer
     * @param client */
    public SFormCopyBankNbDays(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.FINU_BANK_NB_DAY;

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

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlYear = new javax.swing.JLabel();
        jtfYear = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Copiar días inhabiles bancarios");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.GridLayout(1, 2, 5, 1));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jlYear.setForeground(new java.awt.Color(0, 0, 255));
        jlYear.setText("Año a copiar:*");
        jlYear.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlYear);

        jtfYear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfYear.setText("0");
        jtfYear.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel4.add(jtfYear);

        jPanel3.add(jPanel4);

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setPreferredSize(new java.awt.Dimension(392, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jPanel1.add(jbCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(400, 300));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivate();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        mvFields = new Vector<SFormField>();

        moFieldYear = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfYear, jlYear);
        moFieldYear.setLengthMax(4);
        
        mvFields.add(moFieldYear);
        
        jbOk.addActionListener(this);
        jbCancel.addActionListener(this);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionCancel(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivate() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jtfYear.requestFocus();
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
            getRegistry();
            setVisible(false);
        }
    }

    private void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JLabel jlYear;
    private javax.swing.JTextField jtfYear;
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

        moDataBankNbDay = null;
        moFieldYear.setInteger(0);

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }
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
            if (moFieldYear.getInteger() < 2000 || moFieldYear.getInteger() > 2100) {
                validation.setMessage(SGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + SGuiUtils.getLabelName(jlYear) + "'" +
                        SGuiConsts.ERR_MSG_FIELD_VAL_GREAT_EQUAL + "2001 y" +
                        SGuiConsts.ERR_MSG_FIELD_VAL_LESS_EQUAL + "2100.");
                validation.setComponent(moFieldYear.getComponent());
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
        SDataBankNbDay registry = new SDataBankNbDay();
        try {
            if (SFinUtilities.finYearExists(miClient, miClient.getSession().getCurrentYear())) {
                if (!SDataBankNbDay.existBankNbDayInYear(miClient.getSession().getStatement(), miClient.getSession().getCurrentYear())) {
                    if (SDataBankNbDay.existBankNbDayInYear(miClient.getSession().getStatement(), moFieldYear.getInteger())) {
                        String sql = "SELECT * FROM erp.finu_bank_nb_day WHERE YEAR(nb_day) = " + moFieldYear.getInteger() + " AND NOT b_del;";
                        Statement statement = miClient.getSession().getStatement();
                        ResultSet resultSet = statement.executeQuery(sql);
                        
                        while (resultSet.next()) {
                            registry.reset();
                            registry.setName(resultSet.getString("name"));
                            registry.setNonBizDay(SLibTimeUtils.addDate(resultSet.getDate("nb_day"), miClient.getSession().getCurrentYear() - moFieldYear.getInteger(), 
                                    SLibConstants.UNDEFINED, SLibConstants.UNDEFINED));
                            registry.setIsCanEdit(resultSet.getBoolean("b_can_edit"));
                            registry.setIsCanDelete(resultSet.getBoolean("b_can_del"));
                            registry.setIsDeleted(false);
                            registry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                            registry.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
                            registry.setUserNewTs(miClient.getSession().getCurrentDate());
                            registry.setUserEditTs(miClient.getSession().getCurrentDate());
                            
                            registry.save(miClient.getSession().getDatabase().getConnection());
                        }
                        miClient.showMsgBoxInformation("Días inhábiles bancarios copiados al año " + miClient.getSession().getCurrentYear() + ".\nFavor de revisar nombre y fecha.");
                    }
                    else {
                        miClient.showMsgBoxInformation("El año indicado no tiene registros para copiar");
                    }
                }
                else {
                    miClient.showMsgBoxInformation("No se pueden copiar los días inhábiles bancarios.\nYa existen días registrados en el año " + miClient.getSession().getCurrentYear() + ".");
                    return registry;
                }
            }
            else {
                miClient.showMsgBoxInformation("No se pueden copiar los días inhábiles bancarios.\nSe debe primero crear el ejercicio contable correspondiente al año " + miClient.getSession().getCurrentYear() + ".");
                return registry;
            }
        }
        catch (Exception e) {
             miClient.showMsgBoxWarning(e.getMessage());
        }
        return registry;
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

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbCancel) {
                actionCancel();
            }
        }
    }
}
