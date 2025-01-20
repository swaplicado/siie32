/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.mtrn.data.SDataDiog;
import erp.mtrn.data.SDataStockClosing;
import erp.mtrn.data.STrnStockMove;
import erp.mtrn.data.STrnUtilities;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author Edwin Carmona
 */
public class SDialogUtilStockClosingCost extends javax.swing.JDialog {

    private int mnFormResult;
    private boolean mbFirstTime;
    private boolean mbWithCost;

    protected int mnPkYearId;
    protected int mnPkDocId;
    protected int mnFkCompanyBranchId;
    protected int mnFkWarehouseId;
    protected int mnFkUserId;

    private erp.client.SClientInterface miClient;
    private erp.lib.form.SFormField moFieldYear;

    private Vector<SDataDiog> mvDbmsDiogs;

    /** Creates new form SDialogUtilStockClosing */
    public SDialogUtilStockClosingCost(erp.client.SClientInterface client) {
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

        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jlYear = new javax.swing.JLabel();
        jtfYear = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Generación de inventarios iniciales con costo");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros de generación de inventarios iniciales $:"));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridLayout(7, 1, 0, 1));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlYear.setText("Ejercicio para el que se generan inventarios iniciales $: *");
        jlYear.setPreferredSize(new java.awt.Dimension(275, 23));
        jPanel1.add(jlYear);

        jtfYear.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfYear.setText("0000");
        jtfYear.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel1.add(jtfYear);

        jPanel4.add(jPanel1);

        jLabel1.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jPanel4.add(jLabel1);

        jLabel2.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel2.setText("Nota:");
        jPanel4.add(jLabel2);

        jLabel3.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel3.setText("Si el ejercicio que se cierra es el 2000, el valor que se debe especificar para");
        jPanel4.add(jLabel3);

        jLabel4.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel4.setText("el campo 'Ejercicio para el que se generan inventarios iniciales' debe ser 2001.");
        jPanel4.add(jLabel4);

        jLabel5.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel5.setText("El proceso puede demorar varios segundos.");
        jPanel4.add(jLabel5);

        jLabel6.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(204, 0, 0));
        jLabel6.setText("Esta funcionalidad agrupa los inventarios iniciales por costo para las valuaciones.");
        jPanel4.add(jLabel6);

        jPanel3.add(jPanel4, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);
        jPanel3.getAccessibleContext().setAccessibleName("Parámetros de generación de inventarios iniciales $:");

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jbOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOkActionPerformed(evt);
            }
        });
        jPanel2.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setToolTipText("[Escape]");
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });
        jPanel2.add(jbCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        setSize(new java.awt.Dimension(422, 300));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jbOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOkActionPerformed
        actionOk();
    }//GEN-LAST:event_jbOkActionPerformed

    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        actionCancel();
    }//GEN-LAST:event_jbCancelActionPerformed

    private void initComponentsExtra() {
        moFieldYear = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfYear, jlYear);
        moFieldYear.setIntegerMin(2000);
        moFieldYear.setIntegerMax(2100);
        moFieldYear.setMinInclusive(true);
        moFieldYear.setMaxInclusive(true);
        moFieldYear.setDecimalFormat(miClient.getSessionXXX().getFormatters().getYearFormat());

        SFormUtilities.createActionMap(rootPane, this, "actionOk", "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionCancel", "cancel", KeyEvent.VK_ESCAPE, 0);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;

            if (miClient.getSessionXXX().getCurrentCompanyBranchId() == 0) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_SESSION_BRANCH);
                actionCancel();
            }
            else {
                jtfYear.requestFocus();
            }
        }
    }

    public Vector<SDataDiog> createDiogs() {
        String sql = "";
        Statement statement = null;
        ResultSet resulSet = null;
        ArrayList<STrnStockMove> stockMoves = null;
        SDataDiog iog = null;

        try {
            statement = miClient.getSession().getStatement().getConnection().createStatement();

            sql = "SELECT DISTINCT id_cob, id_wh " +
                    "FROM trn_stk " +
                    "WHERE b_del = 0 AND id_year = " + (mnPkYearId - 1) + " " +
                    "ORDER BY id_cob, id_wh ";

            resulSet = statement.executeQuery(sql);
            boolean withCost = true;
            while (resulSet.next()) {
                mnFkCompanyBranchId = resulSet.getInt("id_cob");
                mnFkWarehouseId = resulSet.getInt("id_wh");
                    stockMoves = STrnUtilities.obtainStockWarehouseCost(miClient, (mnPkYearId - 1), SLibTimeUtilities.createDate(mnPkYearId - 1, 12, 31), new int[] { mnFkCompanyBranchId, mnFkWarehouseId });
                    iog = STrnUtilities.createDataDiogSystem(miClient, mnPkYearId, SLibTimeUtilities.createDate(mnPkYearId, 1, 1), mnFkCompanyBranchId, mnFkWarehouseId, SDataConstantsSys.TRNS_TP_IOG_IN_ADJ_INV, "EA", new Vector<>(stockMoves), withCost);
                    mvDbmsDiogs.add(iog);                                
            }
        }
        catch (java.sql.SQLException e) {
            SLibUtilities.renderException(this, e);
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }

        return mvDbmsDiogs;
    }

    public void actionOk() {
        Cursor cursor = null;
        SDataStockClosing syc = null;

        if (moFieldYear.validateField()) {

            if (!SDataUtilities.isPeriodOpen(miClient, new int[] { moFieldYear.getInteger(), 1 })) {
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_GUI_PER_CLOSE + "\nPeríodo cerrado: " + moFieldYear.getInteger() + "-01.");
            }
            else {
                try {
                    cursor = getCursor();
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    mvDbmsDiogs = new Vector<>();
                    mnPkYearId = moFieldYear.getInteger();

                    syc = new SDataStockClosing();
                    syc.setPkYearId(moFieldYear.getInteger());
                    syc.getDbmsDiogs().addAll(createDiogs());

                    SDataUtilities.saveRegistry(miClient, syc);
                    miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(SDataConstants.TRN_DIOG);
                    miClient.showMsgBoxInformation(SLibConstants.MSG_INF_PROC_FINISH);
                    actionCancel();
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                }
                finally {
                    setCursor(cursor);
                }
            }
        }
    }

    public void actionCancel() {
        mnFormResult = SLibConstants.FORM_RESULT_CANCEL;
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JLabel jlYear;
    private javax.swing.JTextField jtfYear;
    // End of variables declaration//GEN-END:variables

    public void resetForm() {
        int[] date = SLibTimeUtilities.digestDate(miClient.getSessionXXX().getWorkingDate());

        mnFormResult = SLibConstants.UNDEFINED;
        mbFirstTime = true;
        mbWithCost = false;

        mnPkYearId = 0;
        mnPkDocId = 0;
        mnFkCompanyBranchId = 0;
        mnFkWarehouseId = 0;
        mvDbmsDiogs = null;
        mnFkUserId = miClient.getSession().getUser().getPkUserId();

        moFieldYear.setFieldValue(date[0]);
    }

    public int getFormResult() {
        return mnFormResult;
    }

    public boolean isWithCost() {
        return mbWithCost;
    }

    public void setWithCost(boolean mbWithCost) {
        this.mbWithCost = mbWithCost;
    }
}
