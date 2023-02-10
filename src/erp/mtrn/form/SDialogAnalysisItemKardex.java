/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mtrn.form;

import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormUtilities;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.lib.table.STableRowCustom;
import erp.lib.table.STableUtilities;
import erp.mitm.data.SDataItem;
import erp.mod.SModConsts;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 *
 * @author Edwin Carmona
 */
public class SDialogAnalysisItemKardex extends javax.swing.JDialog implements java.awt.event.ActionListener {

    private boolean mbFirstTime;
    private erp.client.SClientInterface miClient;
    private erp.lib.table.STablePane moPaneAnalysisKardex;

    private int mnIdItem;

    /** Creates new form SDialogStockCardexProdOrder
     * @param client */
    public SDialogAnalysisItemKardex(erp.client.SClientInterface client) {
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

        jpParams = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jlItem = new javax.swing.JLabel();
        jtfItemCode = new javax.swing.JTextField();
        jtfItem = new javax.swing.JTextField();
        jpStockMoves = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jtfSeek = new javax.swing.JTextField();
        jbSeek = new javax.swing.JButton();
        jbExportCsv = new javax.swing.JButton();
        jbRefresh = new javax.swing.JButton();
        jpControls = new javax.swing.JPanel();
        jbClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Análisis del ítem");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpParams.setLayout(new java.awt.BorderLayout(5, 0));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("ítem configurado"));
        jPanel4.setLayout(new java.awt.GridLayout(1, 1, 0, 5));

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlItem.setText("Ítem:");
        jlItem.setFocusable(false);
        jlItem.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel10.add(jlItem);

        jtfItemCode.setEditable(false);
        jtfItemCode.setText("TEXT");
        jtfItemCode.setFocusable(false);
        jtfItemCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jtfItemCode);

        jtfItem.setEditable(false);
        jtfItem.setText("TEXT");
        jtfItem.setFocusable(false);
        jtfItem.setPreferredSize(new java.awt.Dimension(450, 23));
        jPanel10.add(jtfItem);

        jPanel4.add(jPanel10);

        jpParams.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpParams, java.awt.BorderLayout.NORTH);

        jpStockMoves.setBorder(javax.swing.BorderFactory.createTitledBorder("Análisis del ítem"));
        jpStockMoves.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 3, 0));

        jtfSeek.setText("Seek");
        jtfSeek.setToolTipText("Texto a buscar [Ctrl+B]");
        jtfSeek.setEnabled(false);
        jtfSeek.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel3.add(jtfSeek);

        jbSeek.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_seek.gif"))); // NOI18N
        jbSeek.setToolTipText("Buscar");
        jbSeek.setEnabled(false);
        jbSeek.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel3.add(jbSeek);

        jbExportCsv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_file_csv.gif"))); // NOI18N
        jbExportCsv.setToolTipText("Exportar CSV [Ctrl+E]");
        jbExportCsv.setEnabled(false);
        jbExportCsv.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel3.add(jbExportCsv);

        jbRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_reload.gif"))); // NOI18N
        jbRefresh.setToolTipText("Refrescar [Ctrl+R]");
        jbRefresh.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel3.add(jbRefresh);

        jpStockMoves.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jpStockMoves, java.awt.BorderLayout.CENTER);
        jpStockMoves.getAccessibleContext().setAccessibleName("Análisis del ítem:");

        jpControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbClose.setText("Cerrar");
        jbClose.setPreferredSize(new java.awt.Dimension(75, 23));
        jpControls.add(jbClose);

        getContentPane().add(jpControls, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(736, 489));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsExtra() {
        int col = 0;
        STableColumnForm[] columns = null;

        columns = new STableColumnForm[17];
        
        columns[col++] = new  STableColumnForm(SLibConstants.DATA_TYPE_INTEGER, "#", STableConstants.WIDTH_NUM_SMALLINT);
        columns[col++] = new  STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Tipo análisis", STableConstants.WIDTH_ITEM_2X);
        columns[col++] = new  STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Análisis", STableConstants.WIDTH_ITEM);
        columns[col++] = new  STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Análisis", STableConstants.WIDTH_ITEM_2X);
        columns[col++] = new  STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        columns[col++] = new  STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Nombre unidad", STableConstants.WIDTH_ITEM);
        columns[col++] = new  STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Aplica mín", STableConstants.WIDTH_BOOLEAN);
        columns[col++] = new  STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Mínimo", STableConstants.WIDTH_VALUE);
        columns[col++] = new  STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Aplica máx", STableConstants.WIDTH_BOOLEAN);
        columns[col++] = new  STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Máximo", STableConstants.WIDTH_VALUE);
        columns[col++] = new  STableColumnForm(SLibConstants.DATA_TYPE_BOOLEAN, "Requerido", STableConstants.WIDTH_BOOLEAN);
        columns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. creación", STableConstants.WIDTH_USER);
        columns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Creación", STableConstants.WIDTH_DATE_TIME);
        columns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. modificación", STableConstants.WIDTH_USER);
        columns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Modificación", STableConstants.WIDTH_DATE_TIME);
        columns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Usr. eliminación", STableConstants.WIDTH_USER);
        columns[col++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE_TIME, "Eliminación", STableConstants.WIDTH_DATE_TIME);

        moPaneAnalysisKardex = new STablePane(miClient);
        jpStockMoves.add(moPaneAnalysisKardex, BorderLayout.CENTER);

        for (STableColumnForm column : columns) {
            moPaneAnalysisKardex.addTableColumn(column);
        }

        moPaneAnalysisKardex.createTable();
        moPaneAnalysisKardex.getTable().getTableHeader().setReorderingAllowed(false);

        jbClose.addActionListener(this);
        jbSeek.addActionListener(this);
        jbRefresh.addActionListener(this);
        jbExportCsv.addActionListener(this);
        jtfSeek.addActionListener(this);

        SFormUtilities.createActionMap(getRootPane(), this, "actionClose", "close", KeyEvent.VK_ESCAPE, SLibConstants.UNDEFINED);
        SFormUtilities.createActionMap(getRootPane(), this, "focusSeek", "seek", KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(getRootPane(), this, "actionExportCsv", "exportCsv", KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(getRootPane(), this, "actionRefresh", "refresh", KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
            jbClose.requestFocus();
        }
    }

    private void renderItem() {
        SDataItem item = null;
        item = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { mnIdItem }, SLibConstants.EXEC_MODE_VERBOSE);

        jtfItemCode.setText(item.getKey());
        jtfItem.setText(item.getItem());

        jtfItemCode.setCaretPosition(0);
        jtfItem.setCaretPosition(0);
    }

    @SuppressWarnings("unchecked")
    private void renderAnalysisKardex() {
        int row = 0;
        String sql = "";
        ResultSet resulSet = null;

        moPaneAnalysisKardex.clearTableRows();
        moPaneAnalysisKardex.createTable();
        moPaneAnalysisKardex.getTable().getTableHeader().setReorderingAllowed(false);

        try {
            sql = "SELECT  "
                + "    v.id_analysis, "
                + "    v.id_item, "
                + "    a.analysis_short_name, "
                + "    a.analysis_name, "
                + "    a.unit_symbol, "
                + "    a.unit_name, "
                + "    v.sort_pos, "
                + "    v.min_value, "
                + "    v.max_value, "
                + "    v.b_min, "
                + "    v.b_max, "
                + "    v.b_required, "
                + "    CONCAT(ta.type_code, '-', ta.type_name) AS analysis_type, "
                + "    ta.type_name, "
                + "    v.b_del, "
                + "    v.fid_usr_new, "
                + "    v.fid_usr_edit, "
                + "    v.fid_usr_del, "
                + "    v.ts_new, "
                + "    v.ts_edit, "
                + "    v.ts_del, "
                + "    un.usr AS usr_ins, "
                + "    ue.usr AS usr_upd, "
                + "    ud.usr AS usr_del "
                + " FROM "
                + "    " + SModConsts.TablesMap.get(SModConsts.QLT_ANALYSIS_ITEM) + " AS v "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON v.id_item = i.id_item "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.QLT_ANALYSIS) + " AS a ON v.id_analysis = a.id_analysis "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.QLT_TP_ANALYSIS) + " AS ta ON a.fk_tp_analysis_id = ta.id_analysis_type "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS un ON v.fid_usr_new = un.id_usr "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ue ON v.fid_usr_edit = ue.id_usr "
                + "        INNER JOIN "
                + "    " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ud ON v.fid_usr_del = ud.id_usr "
                + " WHERE "
                + " NOT v.b_del AND v.id_item = " + mnIdItem + " "
                + " ORDER BY v.sort_pos ASC";

            resulSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resulSet.next()) {
                STableRowCustom rowCustom = new STableRowCustom();

                rowCustom.getValues().add(++row);
                rowCustom.getValues().add(resulSet.getString("analysis_type"));
                rowCustom.getValues().add(resulSet.getString("analysis_short_name"));
                rowCustom.getValues().add(resulSet.getString("analysis_name"));
                rowCustom.getValues().add(resulSet.getString("unit_symbol"));
                rowCustom.getValues().add(resulSet.getString("unit_name"));
                rowCustom.getValues().add(resulSet.getBoolean("b_min"));
                rowCustom.getValues().add(resulSet.getString("min_value"));
                rowCustom.getValues().add(resulSet.getBoolean("b_max"));
                rowCustom.getValues().add(resulSet.getString("max_value"));
                rowCustom.getValues().add(resulSet.getBoolean("b_required"));
                rowCustom.getValues().add(resulSet.getString("usr_ins"));
                rowCustom.getValues().add(resulSet.getDate("ts_new"));
                rowCustom.getValues().add(resulSet.getString("usr_upd"));
                rowCustom.getValues().add(resulSet.getDate("ts_edit"));
                rowCustom.getValues().add(resulSet.getString("usr_del"));
                rowCustom.getValues().add(resulSet.getDate("ts_del"));

                moPaneAnalysisKardex.addTableRow(rowCustom);
            }

            moPaneAnalysisKardex.renderTableRows();
            moPaneAnalysisKardex.setTableRowSelection(0);

            if (moPaneAnalysisKardex.getTableGuiRowCount() == 0) {
                jtfSeek.setEnabled(false);
                jbSeek.setEnabled(false);
                jbExportCsv.setEnabled(false);
            }
            else {
                jtfSeek.setEnabled(true);
                jbSeek.setEnabled(true);
                jbExportCsv.setEnabled(true);
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }

    public void actionClose() {
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbExportCsv;
    private javax.swing.JButton jbRefresh;
    private javax.swing.JButton jbSeek;
    private javax.swing.JLabel jlItem;
    private javax.swing.JPanel jpControls;
    private javax.swing.JPanel jpParams;
    private javax.swing.JPanel jpStockMoves;
    private javax.swing.JTextField jtfItem;
    private javax.swing.JTextField jtfItemCode;
    private javax.swing.JTextField jtfSeek;
    // End of variables declaration//GEN-END:variables

    public void focusSeek() {
        if (jtfSeek.isEnabled()) {
            jtfSeek.requestFocus();
        }
    }

    public void actionSeek() {
        if (jbSeek.isEnabled()) {
            STableUtilities.actionSeek(miClient, moPaneAnalysisKardex, jtfSeek.getText().trim());
        }
    }

    public void actionExportCsv() {
        if (jbExportCsv.isEnabled()) {
            STableUtilities.actionExportCsv(miClient, moPaneAnalysisKardex, getTitle());
        }
    }

    public void actionRefresh() {
        renderAnalysisKardex();
    }

    public void setFormParams(final int idItem) {
        mnIdItem = idItem;

        renderItem();
        renderAnalysisKardex();
    }

    public void formReset() {
        mbFirstTime = true;

        jtfSeek.setText("");
        jtfSeek.setEnabled(false);
        jbSeek.setEnabled(false);
        jbExportCsv.setEnabled(false);
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbClose) {
                actionClose();
            }
            else if (button == jbSeek) {
                actionSeek();
            }
            else if (button == jbExportCsv) {
                actionExportCsv();
            }
            else if (button == jbRefresh) {
                actionRefresh();
            }
        }
        else if (e.getSource() instanceof javax.swing.JTextField) {
            JTextField textField = (JTextField) e.getSource();

            if (textField == jtfSeek) {
                actionSeek();
            }
        }
    }
}
