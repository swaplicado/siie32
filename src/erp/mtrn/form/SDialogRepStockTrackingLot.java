/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogRepStockTrackingLot.java
 *
 * Created on 02/08/2012, 11:09:39 AM
 */

package erp.mtrn.form;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.AbstractAction;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;
import net.sf.jasperreports.view.save.JRPdfSaveContributor.*;
import net.sf.jasperreports.view.JRViewer.*;
import net.sf.jasperreports.view.save.JRMultipleSheetsXlsSaveContributor.*;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;

/**
 *
 * @author Juan Barajas
 */
public class SDialogRepStockTrackingLot extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.lib.form.SFormField moFieldPkItemId;
    private erp.lib.form.SFormField moFieldPkUnitId;
    private erp.lib.form.SFormField moFieldPkLotId;

    /** Creates new form SDialogRepSalesPurchasesByConcept */
    public SDialogRepStockTrackingLot(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;

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
        jbPrint = new javax.swing.JButton();
        jbExit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlPkItemId = new javax.swing.JLabel();
        jcbPkItemId = new javax.swing.JComboBox();
        jbPkItemId = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jlPkUnitId = new javax.swing.JLabel();
        jcbPkUnidId = new javax.swing.JComboBox();
        jbPkUnitId = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jlLot = new javax.swing.JLabel();
        jtfLot = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Rastreo de lote");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(392, 33));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbPrint.setText("Imprimir");
        jbPrint.setToolTipText("[Ctrl + Enter]");
        jbPrint.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbPrint);

        jbExit.setText("Cerrar");
        jbExit.setToolTipText("[Escape]");
        jbExit.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbExit);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración del reporte:"));
        jPanel2.setLayout(new java.awt.GridLayout(5, 1, 0, 1));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPkItemId.setText("Ítem: *");
        jlPkItemId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlPkItemId);

        jcbPkItemId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbPkItemId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel6.add(jcbPkItemId);

        jbPkItemId.setText("jButton1");
        jbPkItemId.setToolTipText("Seleccionar ítem");
        jbPkItemId.setFocusable(false);
        jbPkItemId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel6.add(jbPkItemId);

        jPanel2.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPkUnitId.setText("Unidad: *");
        jlPkUnitId.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlPkUnitId);

        jcbPkUnidId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbPkUnidId.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel7.add(jcbPkUnidId);

        jbPkUnitId.setText("jButton2");
        jbPkUnitId.setToolTipText("Seleccionar unidad");
        jbPkUnitId.setFocusable(false);
        jbPkUnitId.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel7.add(jbPkUnitId);

        jPanel2.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLot.setText("Lote: *");
        jlLot.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlLot);

        jtfLot.setText("LOT");
        jtfLot.setToolTipText("Lote");
        jtfLot.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel8.add(jtfLot);

        jPanel2.add(jPanel8);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-436)/2, (screenSize.height-226)/2, 436, 226);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        mvFields = new Vector<SFormField>();

        moFieldPkItemId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkItemId, jlPkItemId);
        moFieldPkItemId.setPickerButton(jbPkItemId);
        moFieldPkUnitId = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbPkUnidId, jlPkUnitId);
        moFieldPkUnitId.setPickerButton(jbPkUnitId);
        moFieldPkLotId = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfLot, jlLot);

        mvFields.add(moFieldPkItemId);
        mvFields.add(moFieldPkUnitId);
        mvFields.add(moFieldPkLotId);

        jbPrint.addActionListener(this);
        jbExit.addActionListener(this);
        jbPkItemId.addActionListener(this);
        jbPkUnitId.addActionListener(this);

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
            jcbPkItemId.requestFocus();
        }
    }

    private void actionPrint() {
        Cursor cursor = getCursor();
        SFormValidation validation = formValidate();
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;

        if (validation.getIsError()) {
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
            if (validation.getMessage().length() > 0) {
                miClient.showMsgBoxWarning(validation.getMessage());
            }
        }
        else {
            try {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));

                map = miClient.createReportParams();
                map.put("sCompanyBranch", "(TODAS)");
                map.put("sWarehouse", "(TODOS)");
                map.put("sItemGeneric", "(TODOS)");
                map.put("sSqlWhereCompanyBranch","");
                map.put("sSqlWhereWarehouse", "");
                map.put("sSqlWhereItemGeneric","");
                map.put("nFidCtEnt", SDataConstantsSys.CFGS_CT_ENT_WH);
                map.put("bSortItemByKey", miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? true : false);
                map.put("bIsByPeriod", false);
                map.put("sSqlWhere", " WHERE stk.b_del = FALSE AND stk.id_item = " +moFieldPkItemId.getKeyAsIntArray()[0] + " AND stk.id_unit = " + moFieldPkUnitId.getKeyAsIntArray()[0] +
                        " AND lt.lot = '" + moFieldPkLotId.getString() + "' ");

                jasperPrint = SDataUtilities.fillReport(miClient, SDataConstantsSys.REP_TRN_STK_MOV, map);
                jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setTitle("Rastreo de lote");
                jasperViewer.setVisible(true);
            }
            catch(Exception e) {
                SLibUtilities.renderException(this, e);
            }
            finally {
                setCursor(cursor);
            }
        }
    }

    private void actionClose() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    private void actionPkItemId() {
        miClient.pickOption(SDataConstants.ITMU_ITEM, moFieldPkItemId, null);
    }

    private void actionPkUnitId() {
        miClient.pickOption(SDataConstants.ITMU_UNIT, moFieldPkUnitId, null);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jbExit;
    private javax.swing.JButton jbPkItemId;
    private javax.swing.JButton jbPkUnitId;
    private javax.swing.JButton jbPrint;
    private javax.swing.JComboBox jcbPkItemId;
    private javax.swing.JComboBox jcbPkUnidId;
    private javax.swing.JLabel jlLot;
    private javax.swing.JLabel jlPkItemId;
    private javax.swing.JLabel jlPkUnitId;
    private javax.swing.JTextField jtfLot;
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

        mbResetingForm = false;
    }

    @Override
    public void formRefreshCatalogues() {
        mbResetingForm = true;
        SFormUtilities.populateComboBox(miClient, jcbPkItemId, SDataConstants.ITMU_ITEM);
        SFormUtilities.populateComboBox(miClient, jcbPkUnidId, SDataConstants.ITMU_UNIT);
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
        throw new UnsupportedOperationException("Not supported yet.");
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
            else if (button == jbPkItemId) {
                actionPkItemId();
            }
            else if (button == jbPkUnitId) {
                actionPkUnitId();
            }
        }
    }
}
