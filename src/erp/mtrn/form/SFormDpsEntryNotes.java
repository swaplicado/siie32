/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SFormDpsEntryNotes.java
 *
 * Created on 22/09/2009, 04:24:20 PM
 */

package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.lib.SLibConstants;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.mtrn.data.SDataDpsEntryNotes;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author Sergio Flores
 */
public class SFormDpsEntryNotes extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.mtrn.data.SDataDpsEntryNotes moDpsEntryNotes;
    private erp.lib.form.SFormField moFieldNotes;
    private erp.lib.form.SFormField moFieldIsDeleted;
    private erp.lib.form.SFormField moFieldIsAllDocs;
    private erp.lib.form.SFormField moFieldIsPrintable;
    private erp.lib.form.SFormField moFieldIsCfd;

    /** Creates new form SFormDpsEntryNotes
     * @param client
     */
    public SFormDpsEntryNotes(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient =  client;
        mnFormType = SDataConstants.TRN_DPS_ETY_NTS;

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

        jpRegistry = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlNotes = new javax.swing.JLabel();
        jckIsDeleted = new javax.swing.JCheckBox();
        jspNotes = new javax.swing.JScrollPane();
        jtaNotes = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jckIsAllDocs = new javax.swing.JCheckBox();
        jckIsPrintable = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        jckIsCfd = new javax.swing.JCheckBox();
        jpControls = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Notas del detalle del documento");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpRegistry.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpRegistry.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout(0, 3));

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jlNotes.setForeground(new java.awt.Color(0, 102, 102));
        jlNotes.setText("Notas: *");
        jlNotes.setPreferredSize(new java.awt.Dimension(32, 23));
        jPanel4.add(jlNotes, java.awt.BorderLayout.CENTER);

        jckIsDeleted.setForeground(java.awt.Color.red);
        jckIsDeleted.setText("Registro eliminado");
        jckIsDeleted.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jckIsDeleted.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel4.add(jckIsDeleted, java.awt.BorderLayout.EAST);

        jPanel2.add(jPanel4, java.awt.BorderLayout.NORTH);

        jspNotes.setPreferredSize(new java.awt.Dimension(100, 175));

        jtaNotes.setColumns(20);
        jtaNotes.setRows(5);
        jspNotes.setViewportView(jtaNotes);

        jPanel2.add(jspNotes, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.GridLayout(2, 1, 0, 3));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsAllDocs.setText("Heredable a todos los documentos dependientes");
        jckIsAllDocs.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel6.add(jckIsAllDocs);

        jckIsPrintable.setText("Visible al imprimir");
        jckIsPrintable.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel6.add(jckIsPrintable);

        jPanel5.add(jPanel6);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckIsCfd.setText("Se anexa a la descripción del concepto del CFDI");
        jckIsCfd.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel8.add(jckIsCfd);

        jPanel5.add(jPanel8);

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        jpRegistry.add(jPanel1, java.awt.BorderLayout.NORTH);

        getContentPane().add(jpRegistry, java.awt.BorderLayout.CENTER);

        jpControls.setPreferredSize(new java.awt.Dimension(392, 33));
        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jpControls.add(jbCancel);

        getContentPane().add(jpControls, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(656, 439));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        moFieldNotes = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtaNotes, jlNotes);
        moFieldNotes.setLengthMax(1023);
        moFieldNotes.setAutoCaseType(0);
        moFieldIsDeleted = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsDeleted);
        moFieldIsAllDocs = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsAllDocs);
        moFieldIsPrintable = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsPrintable);
        moFieldIsCfd = new SFormField(miClient, SLibConstants.DATA_TYPE_BOOLEAN, true, jckIsCfd);

        mvFields = new Vector<>();
        mvFields.add(moFieldNotes);
        mvFields.add(moFieldIsAllDocs);
        mvFields.add(moFieldIsPrintable);
        mvFields.add(moFieldIsCfd);
        mvFields.add(moFieldIsDeleted);

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

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jtaNotes.requestFocus();
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JCheckBox jckIsAllDocs;
    private javax.swing.JCheckBox jckIsCfd;
    private javax.swing.JCheckBox jckIsDeleted;
    private javax.swing.JCheckBox jckIsPrintable;
    private javax.swing.JLabel jlNotes;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JScrollPane jspNotes;
    private javax.swing.JTextArea jtaNotes;
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

        moDpsEntryNotes = null;

        for (int i = 0; i < mvFields.size(); i++) {
            ((erp.lib.form.SFormField) mvFields.get(i)).resetField();
        }

        moFieldIsAllDocs.setFieldValue(true);
        moFieldIsPrintable.setFieldValue(true);
        moFieldIsCfd.setFieldValue(false);

        jckIsDeleted.setEnabled(false);
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
        moDpsEntryNotes = (SDataDpsEntryNotes) registry;

        moFieldNotes.setFieldValue(moDpsEntryNotes.getNotes());
        moFieldIsDeleted.setFieldValue(moDpsEntryNotes.getIsDeleted());
        moFieldIsAllDocs.setFieldValue(moDpsEntryNotes.getIsAllDocs());
        moFieldIsPrintable.setFieldValue(moDpsEntryNotes.getIsPrintable());
        moFieldIsCfd.setFieldValue(moDpsEntryNotes.getIsCfd());

        jckIsDeleted.setEnabled(true);
    }

    @Override
    public erp.lib.data.SDataRegistry getRegistry() {
        if (moDpsEntryNotes == null) {
            moDpsEntryNotes = new SDataDpsEntryNotes();
            moDpsEntryNotes.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moDpsEntryNotes.setIsRegistryEdited(true);
            moDpsEntryNotes.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moDpsEntryNotes.setNotes(moFieldNotes.getString());
        moDpsEntryNotes.setIsAllDocs(moFieldIsAllDocs.getBoolean());
        moDpsEntryNotes.setIsPrintable(moFieldIsPrintable.getBoolean());
        moDpsEntryNotes.setIsCfd(moFieldIsCfd.getBoolean());
        moDpsEntryNotes.setIsDeleted(moFieldIsDeleted.getBoolean());

        return moDpsEntryNotes;
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
