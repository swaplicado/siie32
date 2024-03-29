/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogShowDocumentNotes.java
 *
 * Created on 16/03/2010, 01:48:52 PM
 */

package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataDiogNotesRow;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsNotesRow;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.AbstractAction;

/**
 *
 * @author Alfonso Flores, Sergio Flores
 */
public class SDialogShowDocumentNotes extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private int mnParamTypeDocument;
    private java.lang.Object moParamDocumentKey;
    private erp.lib.table.STablePane moNotesPane;
    private erp.mtrn.data.SDataDiog moDiog;
    private erp.mtrn.data.SDataDps moDps;

    /** Creates new form SDialogShowDocumentNotes
     * @param client GUI client.
     * @param typeDocument SDataConstants.TRN_DPS or SDataConstants.TRN_DIOG.
     */
    public SDialogShowDocumentNotes(erp.client.SClientInterface client, int typeDocument) {
        super(client.getFrame(), true);
        miClient = client;
        mnParamTypeDocument = typeDocument;

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

        jPanel1 = new javax.swing.JPanel();
        jbClose = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jpNotesPane = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Notas del documento");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(492, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbClose.setText("Cerrar");
        jbClose.setToolTipText("[Escape]");
        jbClose.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbClose);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Notas:"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jpNotesPane.setLayout(new java.awt.BorderLayout());
        jPanel2.add(jpNotesPane, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(650, 400));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        int i = 0;

        mvFields = new Vector<>();

        moNotesPane = new STablePane(miClient);
        jpNotesPane.add(moNotesPane, BorderLayout.CENTER);

        jbClose.addActionListener(this);

        erp.lib.table.STableColumnForm tableColumnsNote[];

        i = 0;
        tableColumnsNote = new STableColumnForm[mnParamTypeDocument == SDataConstants.TRN_DPS ? 11 : 9];
        
        tableColumnsNote[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Notas", 500);
        
        switch (mnParamTypeDocument) {
            case SDataConstants.TRN_DPS:
                tableColumnsNote[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Heredable a todos los documentos dependientes", STableConstants.WIDTH_BOOLEAN_2X);
                break;
            case SDataConstants.TRN_DIOG:
                break;
            default:
        }
        
        tableColumnsNote[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Visible al imprimir", STableConstants.WIDTH_BOOLEAN_2X);
        
        switch (mnParamTypeDocument) {
            case SDataConstants.TRN_DPS:
                tableColumnsNote[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Complemento CFDI Leyendas Fiscales", STableConstants.WIDTH_BOOLEAN_2X);
                break;
            case SDataConstants.TRN_DIOG:
                break;
            default:
        }
        
        tableColumnsNote[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Eliminado", STableConstants.WIDTH_BOOLEAN);
        tableColumnsNote[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. creación", STableConstants.WIDTH_USER);
        tableColumnsNote[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Creación", STableConstants.WIDTH_DATE_TIME);
        tableColumnsNote[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. modificación", STableConstants.WIDTH_USER);
        tableColumnsNote[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Modificación", STableConstants.WIDTH_DATE_TIME);
        tableColumnsNote[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. eliminación", STableConstants.WIDTH_USER);
        tableColumnsNote[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Eliminación", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < tableColumnsNote.length; i++) {
            moNotesPane.addTableColumn(tableColumnsNote[i]);
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
            jbClose.requestFocus();
        }
    }

    private void actionCancel() {
        setVisible(false);
    }

    private void populateDataView() {
        switch (mnParamTypeDocument) {
            case SDataConstants.TRN_DPS:
                moDps = (SDataDps) SDataUtilities.readRegistry(miClient, mnParamTypeDocument, moParamDocumentKey, SLibConstants.EXEC_MODE_SILENT);
                for (int i = 0; i < moDps.getDbmsDpsNotes().size(); i++) {
                    moNotesPane.addTableRow(new SDataDpsNotesRow(moDps.getDbmsDpsNotes().get(i)));
                }
                break;
            case SDataConstants.TRN_DIOG:
                moDiog = (SDataDiog) SDataUtilities.readRegistry(miClient, mnParamTypeDocument, moParamDocumentKey, SLibConstants.EXEC_MODE_SILENT);
                for (int i = 0; i < moDiog.getDbmsNotes().size(); i++) {
                    moNotesPane.addTableRow(new SDataDiogNotesRow(moDiog.getDbmsNotes().get(i)));
                }
                break;
            default:
        }

        moNotesPane.renderTableRows();
        moNotesPane.setTableRowSelection(0);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbClose;
    private javax.swing.JPanel jpNotesPane;
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

        moNotesPane.createTable();
        moNotesPane.clearTableRows();
    }

    @Override
    public void formRefreshCatalogues() {
    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        throw new UnsupportedOperationException("Not supported yet.");
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

            if (button == jbClose) {
                actionCancel();
            }
        }
    }

    public void setParamDocumentKey(java.lang.Object key) {
        moParamDocumentKey = key;
        populateDataView();
    }
}
