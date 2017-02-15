/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mmfg.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.lib.form.SFormValidation;
import erp.lib.table.STableColumnForm;
import erp.lib.table.STableConstants;
import erp.lib.table.STablePane;
import erp.lib.table.STableUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mcfg.data.SDataCompanyBranchEntity;
import erp.mitm.data.SDataItem;
import erp.mitm.data.SDataItemForeignLanguage;
import erp.mmfg.data.SDataBom;
import erp.mmfg.data.SDataBomSubgoods;
import erp.mmfg.data.SDataBomSubstitute;
import erp.mmfg.data.SDataExplotionMaterials;
import erp.mmfg.data.SDataExplotionMaterialsEntry;
import erp.mmfg.data.SDataExplotionMaterialsEntryItem;
import erp.mmfg.data.SDataExplotionMaterialsRequisition;
import erp.mmfg.data.SDataExplotionMaterialsResultRow;
import erp.mmfg.data.SDataProductionOrder;
import erp.mmfg.data.SDataProductionOrderBomSubgoods;
import erp.mmfg.data.SDataProductionOrderCharge;
import erp.mmfg.data.SDataProductionOrderChargeEntry;
import erp.mmfg.data.SDataRequisition;
import erp.mmfg.data.SDataRequisitionEntry;
import erp.mmfg.data.SDataRequisitionPurchaseOrder;
import erp.mmkt.data.SParamsItemPriceList;
import erp.mod.SModSysConsts;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.STrnStock;
import erp.mtrn.data.STrnStockMove;
import erp.mtrn.data.STrnStockSegregationUtils;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author  Néstor Ávalos, Uriel Castañeda, Edwin Carmona
 */
public class SDialogExplotionMaterialsRawMaterial extends javax.swing.JDialog implements erp.lib.form.SFormInterface, java.awt.event.ActionListener {

    private int mnFormType;
    private int mnFormResult;
    private int mnFormStatus;
    private boolean mbFirstTime;
    private boolean mbResetingForm;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;
    private erp.client.SClientInterface miClient;

    private erp.lib.form.SFormField moFieldDate;
    private erp.lib.form.SFormField moFieldProductionOrderInitial;
    private erp.lib.form.SFormField moFieldProductionOrderEnd;
    private erp.lib.form.SFormField moFieldReference;
    private erp.lib.form.SFormField moFieldComments;
    private erp.lib.form.SFormField moFieldDbmsProductionOrderQuantity;
    private erp.lib.form.SFormField moFieldDbmsProductionOrderUnit;

    private erp.mbps.data.SDataBizPartner moBizPartner;
    private erp.mbps.data.SDataBizPartnerCategory moBizPartnerCategory;
    private erp.mitm.data.SDataItem moItem;
    private erp.mmfg.data.SDataBom moBom;
    private erp.mmfg.data.SDataExplotionMaterials moExplotionMaterials;
    private erp.mmfg.data.SDataExplotionMaterialsRequisition moExpMatReqMat;
    private erp.mmfg.data.SDataProductionOrder moProductionOrder;
    private erp.mmfg.data.SDataRequisition moRequisition;
    private erp.mmfg.data.SDataRequisitionEntry moRequisitionEntry;
    private erp.mtrn.data.SDataDps moDps;
    private erp.mtrn.data.SDataDpsEntry moDpsEntry;
    private erp.mbps.data.SDataBizPartnerBranch moCompanyBranchSource;
    private erp.mcfg.data.SDataCompanyBranchEntity moWarehouseSource;

    private erp.server.SServerRequest moRequest;
    private erp.server.SServerResponse moResponse;

    private erp.lib.table.STablePane moExplotionMaterialsEntriesPane;

    private java.util.Vector<erp.mmfg.data.SDataExplotionMaterialsEntry> mvExplotionMaterialsEntries;
    private java.util.Vector<erp.mmfg.data.SDataExplotionMaterialsEntryItem> mvExplotionMaterialsEntriesItem;
    private java.util.Vector<erp.mmfg.data.SDataRequisition> mvRequisition;
    private java.util.Vector<erp.mmfg.data.SDataRequisitionEntry> mvRequisitionEntries;
    private java.util.Vector<erp.mmfg.data.SDataProductionOrder> mvProductionsOrders;
    private java.util.Vector<erp.mmfg.data.SDataProductionOrderCharge> mvProductionOrderCharges;
    private java.util.Vector<erp.mmfg.data.SDataProductionOrderBomSubgoods> mvProductionOrderSubgoods;

    private int mnPkExpYearId;
    private int mnPkExpId;
    private int mnOrdYearIdStart;
    private int mnOrdIdStart;
    private int mnOrdYearIdEnd;
    private int mnOrdIdEnd;

    private boolean mbIsCompleteExplotion;
    private boolean mbIsProgrammed;
    private boolean mbIsForecast;
    private int mnFkCompanyBranchId;
    private ArrayList<int[]> moFkEntityId;

    /** Creates new form DFormCompany */
    public SDialogExplotionMaterialsRawMaterial(erp.client.SClientInterface client) {
        super(client.getFrame(), true);
        miClient = client;
        mnFormType = SDataConstants.MFG_EXP;

        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpHead = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlReference = new javax.swing.JLabel();
        jtfReference = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jlWarehouse = new javax.swing.JLabel();
        jtfCompanyBranch = new javax.swing.JTextField();
        jtfCompanyBranchCode = new javax.swing.JTextField();
        jtfWarehouse = new javax.swing.JTextField();
        jtfWarehouseCode = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        jlDate = new javax.swing.JLabel();
        jtfDate = new javax.swing.JFormattedTextField();
        jbDate = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jlProductionOrderInitial = new javax.swing.JLabel();
        jtfProductionOrderInitial = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jlProductionOrderEnd = new javax.swing.JLabel();
        jtfProductionOrderEnd = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jlDbmsProductionOrderQuantity = new javax.swing.JLabel();
        jtfDbmsProductionOrderQuantity = new javax.swing.JTextField();
        jtfDbmsProductionOrderUnit = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jlComments = new javax.swing.JLabel();
        jtfComments = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbDelete = new javax.swing.JButton();
        jbClose = new javax.swing.JButton();
        jpItems = new javax.swing.JPanel();
        jpNotesAction = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jtfSeek = new javax.swing.JTextField();
        jbSeek = new javax.swing.JButton();
        jbExportCsv = new javax.swing.JButton();
        jbRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Explosión"); // NOI18N
        setModal(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new java.awt.BorderLayout(5, 5));

        jpHead.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpHead.setLayout(new java.awt.GridLayout(7, 1, 0, 2));

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlReference.setText("Referencia: ");
        jlReference.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel4.add(jlReference);

        jtfReference.setEditable(false);
        jtfReference.setText("REFERENCE");
        jtfReference.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel4.add(jtfReference);

        jpHead.add(jPanel4);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlWarehouse.setText("Almacén:");
        jlWarehouse.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel11.add(jlWarehouse);

        jtfCompanyBranch.setBackground(java.awt.Color.lightGray);
        jtfCompanyBranch.setEditable(false);
        jtfCompanyBranch.setText("TEXT");
        jtfCompanyBranch.setFocusable(false);
        jtfCompanyBranch.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jtfCompanyBranch);

        jtfCompanyBranchCode.setBackground(java.awt.Color.lightGray);
        jtfCompanyBranchCode.setEditable(false);
        jtfCompanyBranchCode.setText("CODE");
        jtfCompanyBranchCode.setFocusable(false);
        jtfCompanyBranchCode.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel11.add(jtfCompanyBranchCode);

        jtfWarehouse.setBackground(java.awt.Color.lightGray);
        jtfWarehouse.setEditable(false);
        jtfWarehouse.setText("TEXT");
        jtfWarehouse.setFocusable(false);
        jtfWarehouse.setPreferredSize(new java.awt.Dimension(155, 23));
        jPanel11.add(jtfWarehouse);

        jtfWarehouseCode.setBackground(java.awt.Color.lightGray);
        jtfWarehouseCode.setEditable(false);
        jtfWarehouseCode.setText("CODE");
        jtfWarehouseCode.setFocusable(false);
        jtfWarehouseCode.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel11.add(jtfWarehouseCode);

        jpHead.add(jPanel11);

        jPanel30.setPreferredSize(new java.awt.Dimension(230, 23));
        jPanel30.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDate.setText("Fecha de evaluación:");
        jlDate.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel30.add(jlDate);

        jtfDate.setEditable(false);
        jtfDate.setText("DATE");
        jtfDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel30.add(jtfDate);

        jbDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbDate.setToolTipText("Seleccionar fecha");
        jbDate.setEnabled(false);
        jbDate.setFocusable(false);
        jbDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel30.add(jbDate);

        jpHead.add(jPanel30);

        jPanel1.setMinimumSize(new java.awt.Dimension(153, 25));
        jPanel1.setPreferredSize(new java.awt.Dimension(306, 23));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProductionOrderInitial.setText("Orden de producción inicial:");
        jlProductionOrderInitial.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel1.add(jlProductionOrderInitial);

        jtfProductionOrderInitial.setEditable(false);
        jtfProductionOrderInitial.setText("INITIAL PRODUCTION ORDER");
        jtfProductionOrderInitial.setFocusable(false);
        jtfProductionOrderInitial.setPreferredSize(new java.awt.Dimension(600, 23));
        jPanel1.add(jtfProductionOrderInitial);

        jpHead.add(jPanel1);

        jPanel3.setMinimumSize(new java.awt.Dimension(153, 25));
        jPanel3.setPreferredSize(new java.awt.Dimension(306, 23));
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlProductionOrderEnd.setText("Orden de producción final:");
        jlProductionOrderEnd.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel3.add(jlProductionOrderEnd);

        jtfProductionOrderEnd.setEditable(false);
        jtfProductionOrderEnd.setText("ENd PRODUCTION ORDER");
        jtfProductionOrderEnd.setFocusable(false);
        jtfProductionOrderEnd.setPreferredSize(new java.awt.Dimension(600, 23));
        jPanel3.add(jtfProductionOrderEnd);

        jpHead.add(jPanel3);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDbmsProductionOrderQuantity.setText("Cantidad:");
        jlDbmsProductionOrderQuantity.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel8.add(jlDbmsProductionOrderQuantity);

        jtfDbmsProductionOrderQuantity.setEditable(false);
        jtfDbmsProductionOrderQuantity.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfDbmsProductionOrderQuantity.setText("QUANTITY");
        jtfDbmsProductionOrderQuantity.setFocusable(false);
        jtfDbmsProductionOrderQuantity.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jtfDbmsProductionOrderQuantity);

        jtfDbmsProductionOrderUnit.setEditable(false);
        jtfDbmsProductionOrderUnit.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jtfDbmsProductionOrderUnit.setText("UNIT");
        jtfDbmsProductionOrderUnit.setFocusable(false);
        jtfDbmsProductionOrderUnit.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel8.add(jtfDbmsProductionOrderUnit);

        jpHead.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlComments.setText("Comentarios:");
        jlComments.setPreferredSize(new java.awt.Dimension(140, 23));
        jPanel9.add(jlComments);

        jtfComments.setEditable(false);
        jtfComments.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jtfComments.setText("COMMENTS");
        jtfComments.setFocusable(false);
        jtfComments.setPreferredSize(new java.awt.Dimension(600, 23));
        jPanel9.add(jtfComments);

        jpHead.add(jPanel9);

        getContentPane().add(jpHead, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar"); // NOI18N
        jbOk.setToolTipText("[Ctrl + Enter]");
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel2.add(jbOk);

        jbDelete.setText("Eliminar y cerrar"); // NOI18N
        jbDelete.setToolTipText("[Escape]");
        jbDelete.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel2.add(jbDelete);

        jbClose.setText("Cerrar"); // NOI18N
        jbClose.setToolTipText("[Escape]");
        jbClose.setPreferredSize(new java.awt.Dimension(115, 23));
        jPanel2.add(jbClose);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jpItems.setBorder(javax.swing.BorderFactory.createTitledBorder("Ítems:"));
        jpItems.setLayout(new java.awt.BorderLayout());

        jpNotesAction.setPreferredSize(new java.awt.Dimension(771, 23));
        jpNotesAction.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));

        jPanel24.setPreferredSize(new java.awt.Dimension(735, 23));
        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));

        jtfSeek.setText("Seek");
        jtfSeek.setToolTipText("Texto a buscar [Ctrl+B]");
        jtfSeek.setEnabled(false);
        jtfSeek.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel24.add(jtfSeek);

        jbSeek.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_seek.gif"))); // NOI18N
        jbSeek.setToolTipText("Buscar");
        jbSeek.setEnabled(false);
        jbSeek.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel24.add(jbSeek);

        jbExportCsv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_file_csv.gif"))); // NOI18N
        jbExportCsv.setToolTipText("Exportar CSV [Ctrl+E]");
        jbExportCsv.setEnabled(false);
        jbExportCsv.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel24.add(jbExportCsv);

        jbRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/icon_reload.gif"))); // NOI18N
        jbRefresh.setToolTipText("Refrescar [Ctrl+R]");
        jbRefresh.setPreferredSize(new java.awt.Dimension(23, 23));
        jbRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRefreshActionPerformed(evt);
            }
        });
        jPanel24.add(jbRefresh);

        jpNotesAction.add(jPanel24);

        jpItems.add(jpNotesAction, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jpItems, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-998)/2, (screenSize.height-634)/2, 998, 634);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jbRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRefreshActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jbRefreshActionPerformed

    private void initComponentsExtra() {
        int i;
        erp.lib.table.STableColumnForm oColsExplotionMaterialsEntries[];

        mvFields = new Vector<SFormField>();
        moExplotionMaterialsEntriesPane = new STablePane(miClient);
        jpItems.add(moExplotionMaterialsEntriesPane, BorderLayout.CENTER);

        moFieldDate = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_DATE, false, jtfDate, jlDate);
        moFieldProductionOrderInitial = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfProductionOrderInitial, jlProductionOrderInitial);
        moFieldProductionOrderEnd = new erp.lib.form.SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfProductionOrderEnd, jlProductionOrderEnd);
        moFieldReference = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, true, jtfReference, jlReference);
        moFieldReference.setLengthMax(15);
        moFieldComments = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfComments, jlComments);
        moFieldComments.setLengthMax(255);
        moFieldDbmsProductionOrderQuantity = new SFormField(miClient, SLibConstants.DATA_TYPE_DOUBLE, false, jtfDbmsProductionOrderQuantity, jlDbmsProductionOrderQuantity);
        moFieldDbmsProductionOrderUnit = new SFormField(miClient, SLibConstants.DATA_TYPE_STRING, false, jtfDbmsProductionOrderUnit, jlDbmsProductionOrderQuantity);

        mvFields.add(moFieldDate);
        mvFields.add(moFieldProductionOrderInitial);
        mvFields.add(moFieldProductionOrderEnd);
        mvFields.add(moFieldReference);
        mvFields.add(moFieldComments);
        mvFields.add(moFieldDbmsProductionOrderQuantity);
        mvFields.add(moFieldDbmsProductionOrderUnit);

        jbSeek.addActionListener(this);
        jbRefresh.addActionListener(this);
        jbExportCsv.addActionListener(this);
        jtfSeek.addActionListener(this);

        //jbRequisition.addActionListener(this);
        //jbPurchaseOrder.addActionListener(this);
        jbDelete.addActionListener(this);
        jbClose.addActionListener(this);
        jbOk.addActionListener(this);

        i = 0;
        oColsExplotionMaterialsEntries = new STableColumnForm[11];
        oColsExplotionMaterialsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Clave", STableConstants.WIDTH_ITEM_KEY);
        oColsExplotionMaterialsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Insumo", 200);
        oColsExplotionMaterialsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        oColsExplotionMaterialsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_STRING, "Proveedor", 140);
        oColsExplotionMaterialsEntries[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Necs. brutas", STableConstants.WIDTH_QUANTITY_2X);
        oColsExplotionMaterialsEntries[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        oColsExplotionMaterialsEntries[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Stock seguridad", STableConstants.WIDTH_QUANTITY_2X);
        oColsExplotionMaterialsEntries[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        oColsExplotionMaterialsEntries[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Disponible", STableConstants.WIDTH_QUANTITY_2X);
        oColsExplotionMaterialsEntries[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        oColsExplotionMaterialsEntries[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Backorder", STableConstants.WIDTH_QUANTITY_2X);
        oColsExplotionMaterialsEntries[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        oColsExplotionMaterialsEntries[i] = new STableColumnForm(SLibConstants.DATA_TYPE_DOUBLE, "Necs. netas", STableConstants.WIDTH_QUANTITY_2X);
        oColsExplotionMaterialsEntries[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        oColsExplotionMaterialsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE, "F. leadtime", STableConstants.WIDTH_DATE);
        oColsExplotionMaterialsEntries[i++] = new STableColumnForm(SLibConstants.DATA_TYPE_DATE, "F. entrega", STableConstants.WIDTH_DATE);
        for (i = 0; i < oColsExplotionMaterialsEntries.length; i++) {
            moExplotionMaterialsEntriesPane.addTableColumn(oColsExplotionMaterialsEntries[i]);
        }
        moExplotionMaterialsEntriesPane.createTable(null);

        AbstractAction actionOk = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionOk(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionOk, "ok", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);

        AbstractAction actionCancel = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { actionClose(); }
        };

        SFormUtilities.putActionMap(getRootPane(), actionCancel, "cancel", KeyEvent.VK_ESCAPE, 0);

        SFormUtilities.createActionMap(rootPane, this, "publicActionEdit", "modify", KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;
        }
    }

    private void actionExplotionMaterials() {
        boolean b = false;
        double dAvailable = 0;
        double dPurchaseOrder = 0;
        double dNetRequeriment = 0;
        double dMinimunAvailable = 0;
        double dSafetyStock = 0;
        double dSegregated = 0;
        double dProductionOrderQty = 0;
        double dRequerimentQuantityByItem = 0;
        int i = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        int[] anLtime = null;
        String key = "";
        String sProductionOrderUnit = "";
        String unitSymbol = "";
        String sAvailableItem = "";

        Calendar c = null;
        Date tLeadtime = null;
        Date tTsDly = null;
        Object[] oAux = null;
        Object[] oList = null;
        Object[] oProductionOrder = new Object[] { 0, 0, "", 0, "", 0, 0, "", null, 0 };
        Vector<Object> vItems = new Vector<Object>();
        Vector<Object> vParams1 = new Vector<Object>();
        Vector<Object> vParams2 = new Vector<Object>();

        SDataBomSubgoods oBomSubgoods = null;
        SDataExplotionMaterialsEntry oExplotionMaterialsEntry = null;
        SDataExplotionMaterialsEntryItem oExplotionMaterialsEntryItem = null;
        SDataProductionOrderBomSubgoods oProductionOrderBomSubgoods = null;
        SDataProductionOrderCharge oProductionOrderCharges = null;
        SDataProductionOrderChargeEntry oProductionOrderChargesEntry = null;
        SDataItem oItem = null;
        SDataBizPartner oBizPartner = null;
        
        // Validate if explotion of materials record can be created:

        if (validateExplotionMaterialsRecord()) {

            // Get explotion materiarls if exist and get lots by entry:

            if (mnPkExpYearId > 0 && mnPkExpId > 0) {
                moExplotionMaterials = (SDataExplotionMaterials) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_EXP, new int[] { mnPkExpYearId, mnPkExpId }, SLibConstants.EXEC_MODE_SILENT);
                moExplotionMaterials.setIsDeleted(false);
            }
            else {
                moExplotionMaterials = new SDataExplotionMaterials();
            }

            // Get range of productions orders:

            vItems.removeAllElements();
            mvExplotionMaterialsEntries.removeAllElements();
            mvExplotionMaterialsEntriesItem.removeAllElements();
            mvProductionsOrders.removeAllElements();
            vParams1.removeAllElements();
            vParams1.add(1); // 1. Explotion of materials for production orders, 2. Explotion of materials for bill of materials
            vParams1.add(mnOrdYearIdStart);
            vParams1.add(mnOrdIdStart);
            vParams1.add(mnOrdYearIdEnd);
            vParams1.add(mnOrdIdEnd);
            vParams1.add(mbIsForecast);
            vParams1 = SDataUtilities.callProcedure(miClient, SProcConstants.MFG_ORD_ITEM_QRY,  vParams1, SLibConstants.EXEC_MODE_SILENT);
            
            for (i=0; i < vParams1.size(); i++) {
                oList = (Object[]) vParams1.get(i);
                oProductionOrder[0] = (Integer) oList[0]; // Id year
                oProductionOrder[1] = (Integer) oList[1]; // Id ord
                oProductionOrder[2] = oList[2].toString(); // Ref
                oProductionOrder[3] = (Integer) oList[3]; // Id bom
                oProductionOrder[4] = oList[4].toString(); // Bom
                oProductionOrder[5] = (Integer) oList[5]; // Root
                oProductionOrder[6] = (Double) oList[6]; // Qty product
                oProductionOrder[7] = oList[7].toString(); // Unit
                oProductionOrder[8] = oList[8]; // Ts dly
                oProductionOrder[9] = (Integer) oList[9]; // Charges

                // Explotion of materials (BOM):

                //mvItems.removeAllElements();
                vParams2.removeAllElements();
                vParams2.add((Integer) oProductionOrder[5]); // Root
                vParams2.add(oProductionOrder[4].toString()); // Reference
                vParams2.add((Double) oProductionOrder[6]); // Qty
                vParams2.add(0); // Production Order, id_year
                vParams2.add(0); // Production Order, id_ord
                vParams2.add("Ttmp");
                vParams2.add(miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId());
                vParams2 = SDataUtilities.callProcedure(miClient, SProcConstants.MFG_BOM_EXP_GREQ,  vParams2, SLibConstants.EXEC_MODE_SILENT);

                // Add production order:

                if (oProductionOrder[2].toString().length() > 0) {

                    // Add production order if formula isn't sub-formula:

                    b = false;
                    for (k = 0; k < mvProductionsOrders.size(); k++) {
                        moProductionOrder = mvProductionsOrders.get(k);
                        
                        if (SLibUtilities.compareKeys(moProductionOrder.getPrimaryKey(), new int[] { (Integer) oProductionOrder[0], (Integer) oProductionOrder[1] })) {
                            b = true;
                            break;
                        }
                    }

                    if (!b) {
                        moProductionOrder = (SDataProductionOrder) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_ORD, new int[] { (Integer) oProductionOrder[0], (Integer) oProductionOrder[1] }, SLibConstants.EXEC_MODE_VERBOSE);
                        mvProductionsOrders.add(moProductionOrder);
                    }
                    
                    // Sum the quantity of production order while is the same unit in all productions orders:

                    if (sProductionOrderUnit.length() > 0) {
                        if (sProductionOrderUnit.compareTo(oProductionOrder[7].toString()) == 0) {
                            dProductionOrderQty += (Double) oProductionOrder[6];
                        }
                        else {
                            sProductionOrderUnit = "(n/a)";
                            dProductionOrderQty = 0;
                        }
                    }
                    else {
                        sProductionOrderUnit = oProductionOrder[7].toString();
                        dProductionOrderQty += (Double) oProductionOrder[6];
                    }
                }

                // Get items, list doesn´t have repeated items:

                mvExplotionMaterialsEntriesItem.removeAllElements();
                for (j = 0; j < vParams2.size(); j++) {
                    oList = (java.lang.Object[]) vParams2.get(j);

                    try {
                        b = true;
                        oAux = SDataUtilities.isBillOfMaterials(miClient, (Integer) oList[1]);

                        // Check if item can be explode and is bill of materials (BOM)

                        if (((Boolean) oList[6] == false && (Integer) oAux[0] != 0) ||
                                (mbIsCompleteExplotion && (Integer) oAux[0] != 0)) {

                            vParams1.add(new Object[] {
                                (Integer) oProductionOrder[0], (Integer) oProductionOrder[1], oAux[1], oAux[0], oAux[1], oAux[2], (Double) oList[2], oProductionOrder[7].toString(), oProductionOrder[8], oProductionOrder[9] });
                            oProductionOrder[9] = 0;
                            b = false;
                        }
                        else {

                            // Add entry item (gross requirement by explotion materials) to vector if item formula can and can´t be explode:

                            /* if ((Boolean) oList[6] == false) { XXX */

                            oExplotionMaterialsEntryItem = new SDataExplotionMaterialsEntryItem();

                            oExplotionMaterialsEntryItem.setPkItemId((Integer) oList[1]);
                            oExplotionMaterialsEntryItem.setPkUnitId((Integer) oList[7]);
                            oExplotionMaterialsEntryItem.setGrossRequirement((Double) oList[2]);
                            oExplotionMaterialsEntryItem.setPkOrdYearId((Integer) oProductionOrder[0]);
                            oExplotionMaterialsEntryItem.setPkOrdId((Integer) oProductionOrder[1]);
                            //oExplotionMaterialsEntryItem.setDbmsIsRequest((Integer) oAux[0] != 0 ? false : true);
                            oExplotionMaterialsEntryItem.setDbmsIsRequest((Boolean) oList[10]);

                            mvExplotionMaterialsEntriesItem.add(oExplotionMaterialsEntryItem);
                            /*}
                            else {
                                b = false;
                            } XXX  */
                        }
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                        vItems.removeAllElements();
                        break;
                    }

                    // Sum and add items that not are Bill Of Materials (BOM):

                    if (b) {
                        for (k=0; k < vItems.size(); k++) {
                            oAux = (java.lang.Object[]) vItems.get(k);

                            if (SLibUtilities.parseInt(oAux[1].toString()) == SLibUtilities.parseInt(oList[1].toString())) {
                                oAux[2] = ((Double) oAux[2]) + ((Double) oList[2]);
                                vItems.set(k, oAux);
                                b = false;
                            }
                        }

                        if (b) {
                            vItems.add(oList);
                        }
                    }
                }

                // Create charges:

                moBom = (SDataBom) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_BOM, new int[] { moProductionOrder.getFkBomId() }, SLibConstants.EXEC_MODE_VERBOSE);
                for (k = 0; k < (Integer) oProductionOrder[9]; k++) {

                    oProductionOrderCharges = new SDataProductionOrderCharge();
                    oProductionOrderCharges.setPrimaryKey(new int[] { (Integer) oProductionOrder[0], (Integer) oProductionOrder[1], 0});
                    oProductionOrderCharges.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

                    // Create charges entries:

                    for (l = 0; l < mvExplotionMaterialsEntriesItem.size(); l++) {

                        oExplotionMaterialsEntryItem = mvExplotionMaterialsEntriesItem.get(l);

                        oProductionOrderChargesEntry = new SDataProductionOrderChargeEntry();
                        oProductionOrderChargesEntry.setPrimaryKey(new int[] { (Integer) oProductionOrder[0], (Integer) oProductionOrder[1], 0, 0 });
                        oProductionOrderChargesEntry.setGrossRequirement_r(oExplotionMaterialsEntryItem.getGrossRequirement() / (Integer)oProductionOrder[9]);
                        oProductionOrderChargesEntry.setIsRequest(oExplotionMaterialsEntryItem.getDbmsIsRequest());
                        oProductionOrderChargesEntry.setFkItemId_r(oExplotionMaterialsEntryItem.getPkItemId());
                        oProductionOrderChargesEntry.setFkUnitId_r(oExplotionMaterialsEntryItem.getPkUnitId());
                        oProductionOrderChargesEntry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

                        // Validate if is the last charge:

                        if (k+1 == (Integer)oProductionOrder[9]) {
                            oProductionOrderChargesEntry.setGrossRequirement_r(oExplotionMaterialsEntryItem.getGrossRequirement() - (k * (oExplotionMaterialsEntryItem.getGrossRequirement() / (Integer)oProductionOrder[9])));
                        }

                        oProductionOrderCharges.getDbmsProductionOrderChargeEntries().add(oProductionOrderChargesEntry);
                    }
                    mvProductionOrderCharges.add(oProductionOrderCharges);

                    // Create subgoods entries:

                    for (l = 0; l < moBom.getDbmsBomSubgoods().size(); l++) {

                        oBomSubgoods = moBom.getDbmsBomSubgoods().get(l);
                        oProductionOrderBomSubgoods = new SDataProductionOrderBomSubgoods();

                        oProductionOrderBomSubgoods.setPkYearId(moProductionOrder.getPkYearId());
                        oProductionOrderBomSubgoods.setPkOrderId(moProductionOrder.getPkOrdId());
                        oProductionOrderBomSubgoods.setPkSubgoodsId(oBomSubgoods.getPkSgdsId());
                        oProductionOrderBomSubgoods.setQuantity((moProductionOrder.getQuantity() * oBomSubgoods.getQuantity()) / (Integer)oProductionOrder[9]);
                        oProductionOrderBomSubgoods.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                        oProductionOrderBomSubgoods.setFkUserEditId(miClient.getSession().getUser().getPkUserId());

                        // Validate if is the last charge:

                        if (k+1 == (Integer)oProductionOrder[9]) {
                            oProductionOrderBomSubgoods.setQuantity((moProductionOrder.getQuantity() * oBomSubgoods.getQuantity()) - (k * ((moProductionOrder.getQuantity() * oBomSubgoods.getQuantity()) / (Integer)oProductionOrder[9])));
                        }

                        mvProductionOrderSubgoods.add(oProductionOrderBomSubgoods);
                    }
                }

                // Create raw material substitute entries:

                for (SDataBom oBomLevel : moBom.getDbmsLevel()) {
                    for (SDataBomSubstitute oBomSubstitute : oBomLevel.getDbmsBomSubstitute()) {
                        if (oBomSubstitute != null && !oBomSubstitute.getIsDeleted()) {

                            vItems.add(new Object[] {
                                oBomSubstitute.getDbmsItemSubstitute(),
                                oBomSubstitute.getFkItemSubstituteId(),
                                (Double) 0.0,
                                oBomSubstitute.getPkBomId(),
                                oBomLevel.getDateStart(),
                                oBomLevel.getLevel(),
                                false,
                                oBomSubstitute.getFkUnitSubstituteId(),
                                oBomSubstitute.getDbmsItemSubstitute(),
                                oBomSubstitute.getDbmsItemSubstituteKey(),
                                oBomSubstitute.getDbmsUnitSubstitute(),
                                ""
                            });
                        }
                    }
                }
            }
            
            // Calculate inventory safety stock, inventory available, purchases order and net requeriment:
            STrnStockMove stockMoveParams = null;
            STrnStock objStock = null;

            for (j = 0; j < vItems.size(); j++) {
                anLtime = new int[] { 0, 0 };
                dAvailable = 0;
                dSafetyStock = 0;
                dSegregated = 0;
                dPurchaseOrder = 0;
                dNetRequeriment = 0;
                
                try {
                    stockMoveParams = new STrnStockMove();
                
                    stockMoveParams.setPkYearId(SLibTimeUtilities.digestYear(moFieldDate.getDate())[0]);
                    stockMoveParams.setPkItemId((Integer) oList[1]);
                    stockMoveParams.setPkUnitId(oItem.getFkUnitId());
                    stockMoveParams.setPkCompanyBranchId(mnFkCompanyBranchId);
                    stockMoveParams.setPkWarehouseId(moFkEntityId != null ? moFkEntityId.size() == 1 ? moFkEntityId.get(0)[1] : 0 : 0);
                    
                    objStock = STrnStockSegregationUtils.getStkSegregated(miClient, stockMoveParams);
                    dSegregated = objStock != null ? objStock.getSegregatedStock() : 0;
                }
                catch (Exception ex) {
                    SLibUtilities.printOutException(this, ex);
                }

                oList = (java.lang.Object[]) vItems.get(j);

                oBizPartner = null;

                // Get lead time for ítem:

                anLtime = getLeadTime((Integer) oList[1]);

                c = java.util.Calendar.getInstance();
                c.setTime(moFieldDate.getDate());
                c.add(Calendar.DAY_OF_YEAR, anLtime[1]);
                tLeadtime = c.getTime();
                tTsDly = (Date) oProductionOrder[8];

                // Get biz partner:

                if (anLtime[0] > 0) {
                    oBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { anLtime[0] }, SLibConstants.EXEC_MODE_VERBOSE);
                }

                // Get unit item:

                oItem = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { (Integer) oList[1] }, SLibConstants.EXEC_MODE_SILENT);
                if (oItem != null) {
                    unitSymbol = oItem.getDbmsDataUnit().getSymbol();
                    key = oItem.getKey();
                }
                else {
                    unitSymbol = "?";
                    key = "?";
                }

                // Get available item:
                if (moFkEntityId == null || moFkEntityId.isEmpty()) {
                    vParams2.removeAllElements();
                    vParams2.add(SLibTimeUtilities.digestYear(moFieldDate.getDate())[0]); // Year
                    vParams2.add(oItem != null ? oItem.getPkItemId() : 0); // Item
                    vParams2.add(oItem != null ? oItem.getFkUnitId() : 0); // Unid
                    vParams2.add(null); // Lot
                    vParams2.add(mnFkCompanyBranchId); // Company branch
                    vParams2.add(null); // Warehouse
                    vParams2.add("'" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(moFieldDate.getDate()) + "'"); // Date
                    vParams2 = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_STK_GET, vParams2, SLibConstants.EXEC_MODE_SILENT);

                    if (vParams2.size() > 0) {
                        dAvailable += (Double) vParams2.get(0);
                    }

                    // Get safety stock item:
                    vParams2.removeAllElements();
                    vParams2.add(oItem != null ? oItem.getPkItemId() : 0);
                    vParams2.add(oItem != null ? oItem.getFkUnitId() : 0);
                    vParams2.add(mnFkCompanyBranchId);
                    vParams2.add(0);
                    vParams2 = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_STK_SFTY_GET, vParams2, SLibConstants.EXEC_MODE_SILENT);

                    if (vParams2.size() > 0) {
                        dSafetyStock += (Double) vParams2.get(0);
                    }
                }
                else {
                    for (int[] fkEntityId : moFkEntityId ) {
                        vParams2.removeAllElements();
                        vParams2.add(SLibTimeUtilities.digestYear(moFieldDate.getDate())[0]); // Year
                        vParams2.add(oItem != null ? oItem.getPkItemId() : 0); // Item
                        vParams2.add(oItem != null ? oItem.getFkUnitId() : 0); // Unid
                        vParams2.add(null); // Lot
                        vParams2.add(fkEntityId[0]); // Company branch
                        vParams2.add(fkEntityId[1]); // Warehouse
                        vParams2.add("'" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(moFieldDate.getDate()) + "'"); // Date
                        vParams2 = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_STK_GET, vParams2, SLibConstants.EXEC_MODE_SILENT);

                        if (vParams2.size() > 0) {
                            dAvailable += (Double) vParams2.get(0);
                        }

                        // Get safety stock item:
                        vParams2.removeAllElements();
                        vParams2.add(oItem != null ? oItem.getPkItemId() : 0);
                        vParams2.add(oItem != null ? oItem.getFkUnitId() : 0);
                        vParams2.add(fkEntityId[0]);
                        vParams2.add(fkEntityId[1]);
                        vParams2 = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_STK_SFTY_GET, vParams2, SLibConstants.EXEC_MODE_SILENT);

                        if (vParams2.size() > 0) {
                            dSafetyStock += (Double) vParams2.get(0);
                        }  
                    }
                }
                
                dAvailable -= dSegregated;

                // Get purchase order:
                try {
                    dPurchaseOrder = SDataUtilities.obtainBackorderItem(miClient, SDataConstantsSys.TRNS_CT_DPS_PUR, 0, (Integer) oList[1], moFieldDate.getDate());
                }
                catch (Exception e) {
                    SLibUtilities.renderException(this, e);
                    mvExplotionMaterialsEntries.removeAllElements();
                    break;
                }

                // Get net requeriments:

                if ((((Double) oList[2]) + dSafetyStock) >= (dAvailable + dPurchaseOrder)) {
                    dNetRequeriment = (((Double) oList[2]) + dSafetyStock) - (dAvailable + dPurchaseOrder);
                }
                else {
                    dNetRequeriment = 0;
                }

                // Check minimun available:

                if (mvProductionsOrders.size() == 1) {
                    if (dAvailable < (Double) oList[2]) {
                        if (((dAvailable / (Double) oList[2]) <= dMinimunAvailable) || dMinimunAvailable == 0) {
                            dMinimunAvailable = (dAvailable / (Double) oList[2]);
                            dRequerimentQuantityByItem = (dAvailable / ((Double) oList[2] / dProductionOrderQty));
                            sAvailableItem = key + " - " + SLibUtilities.textTrim(oList[0].toString()) + " " + unitSymbol;
                        }
                    }
                }

                oExplotionMaterialsEntry = new SDataExplotionMaterialsEntry();
                oExplotionMaterialsEntry.setPkExpId(0);
                oExplotionMaterialsEntry.setPkItemId((Integer) oList[1]);
                oExplotionMaterialsEntry.setPkUnitId(oItem.getFkUnitId());
                oExplotionMaterialsEntry.setDbmsItemKey(key);
                oExplotionMaterialsEntry.setDbmsItem(SLibUtilities.textTrim(oList[0].toString()));
                oExplotionMaterialsEntry.setDbmsBizPartner(oBizPartner != null ? oBizPartner.getBizPartner() : "?");
                oExplotionMaterialsEntry.setDbmsItemUnitSymbol(unitSymbol);
                oExplotionMaterialsEntry.setGrossReq((Double) oList[2]);
                oExplotionMaterialsEntry.setSafetyStock(dSafetyStock);
                oExplotionMaterialsEntry.setAvailable(dAvailable);
                oExplotionMaterialsEntry.setBackorder(dPurchaseOrder);
                oExplotionMaterialsEntry.setNet(dNetRequeriment);
                oExplotionMaterialsEntry.setDeliveryTs(new java.sql.Date (tTsDly.getTime()));
                oExplotionMaterialsEntry.setLtimeTs_n(anLtime[1] > 0 ? tLeadtime : null);
                oExplotionMaterialsEntry.setFkCobId(mnFkCompanyBranchId);
                oExplotionMaterialsEntry.setFkCobId_n(moFkEntityId != null ? moFkEntityId.size() == 1 ? moFkEntityId.get(0)[0] : 0 : 0);
                oExplotionMaterialsEntry.setFkWarehouseId_n(moFkEntityId != null ? moFkEntityId.size() == 1 ? moFkEntityId.get(0)[1] : 0 : 0);
                oExplotionMaterialsEntry.setFkBizPartnerId_n(anLtime[0]);

                /* XXX: Change form lot by new form lot general
                 *
                // Get lots if exist in explotion materials:

                if (moExplotionMaterials != null) {
                    for (k=0; k < moExplotionMaterials.getDbmsExplotionMaterialsEntry().size(); k++) {
                        if (moExplotionMaterials.getDbmsExplotionMaterialsEntry().get(k).getPkItemId() == (Integer) oList[1]) {
                            for (int m=0; m<moExplotionMaterials.getDbmsExplotionMaterialsEntry().get(k).getDbmsLots().size(); m++) {

                                oExplotionMaterialsEntryLot = moExplotionMaterials.getDbmsExplotionMaterialsEntry().get(k).getDbmsLots().get(m);
                                oExplotionMaterialsEntry.getDbmsLots().add(oExplotionMaterialsEntryLot);
                            }
                            break;

                        }
                    }
                }
                 */

                mvExplotionMaterialsEntries.add(oExplotionMaterialsEntry);
            }

            if (!mvExplotionMaterialsEntries.isEmpty()) {

                // Assign comment:

                moExplotionMaterials.setComments(sAvailableItem.length() > 0 ?
                    "Por el insumo: '" + sAvailableItem + "' sólo puede producir " + SLibUtilities.round(dRequerimentQuantityByItem, 2) + " " + sProductionOrderUnit :
                    "Ninguno");

                // Save explotion of materials:

                saveExplotionMaterials();

                // Assign quantity and unit of production order to fields:

                moFieldDbmsProductionOrderQuantity.setDouble(dProductionOrderQty);
                moFieldDbmsProductionOrderUnit.setString(sProductionOrderUnit);
                moFieldComments.setFieldValue(moExplotionMaterials.getComments());
            }
        }
    }

    private int[] getLeadTime(int nItemId) {

        int[] anLtime = new int[] { 0, 0 };
        Vector<Object> vParams = new Vector<Object>();

        // Get cob leadtime:

        vParams.removeAllElements();
        vParams.add(miClient.getSessionXXX().getCurrentCompanyBranchId()); // Cob
        vParams.add(nItemId); // Item id
        vParams = SDataUtilities.callProcedure(miClient, SProcConstants.MFG_LTIME_COB_GET,  vParams, SLibConstants.EXEC_MODE_SILENT);

        if (vParams.size() == 1) {

            // Only there is one supplier for buy ítem:

            anLtime = (int[]) vParams.get(0);
        }
        else if (vParams.size() > 1) {

            // Find the best supplier for buy ítem:

            anLtime = getLeadTimeSupplier(nItemId, vParams);
        }

        // Check if there is supplier and lead time:

        if (anLtime[0] == 0 && anLtime[1] == 0) {

            // Get co leadtime:

            vParams.removeAllElements();
            vParams.add(nItemId); // Item id
            vParams = SDataUtilities.callProcedure(miClient, SProcConstants.MFG_LTIME_CO_GET,  vParams, SLibConstants.EXEC_MODE_SILENT);

            if (vParams.size() == 1) {

                // Only there is one supplier for buy ítem:

                anLtime = (int[]) vParams.get(0);
            }
            else if (vParams.size() > 1) {

                // Find the best supplier for buy ítem:

                anLtime = getLeadTimeSupplier(nItemId, vParams);
            }
        }

        return anLtime;
    }

    private int[] getLeadTimeSupplier(int nItemId, Vector<Object> vParams) {

        int nBpId = 0;
        int nLtime = 0;
        int[] anLtime = new int[] { 0, 0 };
        Object[] aux = null;
        String sSuppliersIds = "";

        for (int m=0; m < vParams.size(); m++) {

            anLtime = (int[]) vParams.get(m);

            if (m == 0) {
                nBpId = anLtime[0];
                nLtime = anLtime[1];
            }

            sSuppliersIds += anLtime[0] + (m+1 == vParams.size() ? "" : ", ");
        }

        // Find supplier with less price:

        try {
            aux = SDataUtilities.obtainLastPriceForSupplierItem(miClient, nItemId, sSuppliersIds);
        } catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }

        // Check if there is a record:

        if ((Integer) aux[0] > 0) {
            anLtime = new int[] { (Integer) aux[0], nLtime };
        }
        else {
            anLtime = new int[] { nBpId, nLtime };
        }

        return anLtime;
    }

    private void renderExplotionMaterialsList() {
        SDataExplotionMaterialsResultRow oBomExpResRow = null;

        moExplotionMaterials = new SDataExplotionMaterials();
        mvExplotionMaterialsEntries = new Vector<SDataExplotionMaterialsEntry>();
        mvProductionsOrders = new Vector<SDataProductionOrder>();

        actionExplotionMaterials();
        for (int i = 0; i < mvExplotionMaterialsEntries.size(); i++) {
            oBomExpResRow = new SDataExplotionMaterialsResultRow(mvExplotionMaterialsEntries.get(i));
            moExplotionMaterialsEntriesPane.addTableRow(oBomExpResRow);
        }
    }

    private boolean validateExplotionMaterialsRecord() {
        Object oRes[] = null;

        try {
            // Check if explotion materials can be created checking production orders and requisition materials.

            oRes = SDataUtilities.checkExplotionMaterials(miClient, mnOrdYearIdStart, mnOrdIdStart, mnOrdYearIdEnd, mnOrdIdEnd);
            mnPkExpYearId = (Integer) oRes[1];
            mnPkExpId = (Integer) oRes[2];
        }
        catch(Exception e) {
            System.out.println(e);
        }

        if (oRes[0].toString().length() > 0) {
            miClient.showMsgBoxInformation(oRes[0].toString());
        }

        return oRes[0].toString().length() > 0 ? false : true;
    }

    private void saveExplotionMaterials() {

        // Save explotion of materials:

        try {
            moRequest = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
            moRequest.setPacket(getRegistryExplotionMaterials());
            moResponse = miClient.getSessionXXX().request(moRequest);

            if (moResponse.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                throw new Exception(moResponse.getMessage());
            }
            else {
                if (moResponse.getResultType() != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + (moResponse.getMessage().length() == 0 ? "" : "\n" + moResponse.getMessage()));
                }
                else {
                    if (mvProductionsOrders != null && mbIsProgrammed) {
                        for (SDataProductionOrder po : mvProductionsOrders) {
                            if (!po.getIsForecast()) {
                                po.program(miClient, true);
                            }
                        }
                    }
                    
                    moExplotionMaterials = (SDataExplotionMaterials) moResponse.getPacket();
                    mnPkExpYearId = moExplotionMaterials.getPkYearId();
                    mnPkExpId = moExplotionMaterials.getPkExpId();

                    moExplotionMaterials = (SDataExplotionMaterials) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_EXP, new int[] { mnPkExpYearId, mnPkExpId }, SLibConstants.EXEC_MODE_SILENT);
                    if (moExplotionMaterials != null) {
                        moFieldReference.setString(moExplotionMaterials.getReference());
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println(e);
            miClient.showMsgBoxWarning("Error al guardar la orden de producción.");
        }
    }

    private erp.mmfg.data.SDataExplotionMaterials getRegistryExplotionMaterials() {
        moExplotionMaterials.setPkYearId(mnPkExpYearId > 0 ? mnPkExpYearId : SLibTimeUtilities.digestYear(moFieldDate.getDate())[0]);
        moExplotionMaterials.setPkExpId(mnPkExpId);
        moExplotionMaterials.setDateDocument(moFieldDate.getDate());
        moExplotionMaterials.setReference(moExplotionMaterials.getReference()); //sTrnStockValidator
        moExplotionMaterials.setDbmsExplotionMaterialsEntry(mvExplotionMaterialsEntries);
        moExplotionMaterials.setDbmsProductionsOrders(mvProductionsOrders);
        moExplotionMaterials.setDbmsProductionOrderSubgoods(mvProductionOrderSubgoods);
        moExplotionMaterials.setDbmsExplotionMaterialsEntryItem(mvExplotionMaterialsEntriesItem);
        moExplotionMaterials.setDbmsProductionOrderCharges(mvProductionOrderCharges);
        moExplotionMaterials.setDbmsIsExplotion(true);
        moExplotionMaterials.setIsNewRegistry(mnPkExpYearId > 0 ? false : true);

        if (mnPkExpYearId > 0) {
            moExplotionMaterials.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moExplotionMaterials.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }

        return moExplotionMaterials;
    }

    private boolean actionGenerateRequisition(boolean bPurchaseOrder) {
        boolean b = true;

        // Validate that the requisition materials has not purchase orders:

        if (validateRequisitionMaterials()) {

            // Delete records from table 'mfg_exp_req' (bom vs requisition):

            if (deleteRecordsExplotionMaterialsRequisition()) {

                // Validate that bom has supplier:

                for (int i=0; i < mvExplotionMaterialsEntries.size(); i++) {

                    if (mvExplotionMaterialsEntries.get(i).getFkBizPartnerId_n() <= 0) {
                        miClient.showMsgBoxWarning("El ítem '" + mvExplotionMaterialsEntries.get(i).getDbmsItem() + "' no tiene un proveedor asignado.");
                        b = false;
                        break;
                    }
                }

                if (b) {
                    getRegistryRequisition();

                    // Save requisition:

                    for (int i=0; i<mvRequisition.size(); i++) {

                        try {
                            moRequest = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
                            moRequest.setPacket(mvRequisition.get(i));
                            moResponse = miClient.getSessionXXX().request(moRequest);

                            if (moResponse.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                                b = false;
                                throw new Exception(moResponse.getMessage());
                            }
                            else {
                                if (moResponse.getResultType() != SLibConstants.DB_ACTION_SAVE_OK) {
                                    b = false;
                                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + (moResponse.getMessage().length() == 0 ? "" : "\n" + moResponse.getMessage()));
                                }
                                else {
                                    mvRequisition.set(i, (SDataRequisition) moResponse.getPacket());
                                }
                            }
                        }
                        catch (Exception e) {
                            miClient.showMsgBoxWarning("Error al guardar la orden de producción.");
                            b = false;
                            break;
                        }

                        // Save explotion of materials vs. requisition materials:

                        try {
                            moRequest = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
                            moRequest.setPacket(getRegistryExplotionMaterialsRequisition(mnPkExpYearId, mnPkExpId, ((SDataRequisition) mvRequisition.get(i)).getPkYearId(), ((SDataRequisition) mvRequisition.get(i)).getPkReqId()));
                            moResponse = miClient.getSessionXXX().request(moRequest);

                            if (moResponse.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                                throw new Exception(moResponse.getMessage());
                            }
                            else {
                                if (moResponse.getResultType() != SLibConstants.DB_ACTION_SAVE_OK) {
                                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + (moResponse.getMessage().length() == 0 ? "" : "\n" + moResponse.getMessage()));
                                }
                            }
                        }
                        catch (Exception e) {
                            miClient.showMsgBoxWarning("Error al guardar la orden de producción.");
                            b = false;
                            break;
                        }
                    }

                    if (b && !bPurchaseOrder) {
                        miClient.showMsgBoxInformation("Las requisiciones de materiales han sido creadas.");
                    }
                }
            }
        }

        return b;
    }

    private erp.mmfg.data.SDataExplotionMaterialsRequisition getRegistryExplotionMaterialsRequisition(int nPkExpYearId, int nPkExpId, int nPkReqYearId, int nPkReqId) {
        moExpMatReqMat = new SDataExplotionMaterialsRequisition();
        moExpMatReqMat.setPkExpYearId(nPkExpYearId);
        moExpMatReqMat.setPkExpId(nPkExpId);
        moExpMatReqMat.setPkReqYearId(nPkReqYearId);
        moExpMatReqMat.setPkReqId(nPkReqId);
        moExpMatReqMat.setDbmsIsDelRequisition(true);

        return moExpMatReqMat;
    }

    private boolean validateRequisitionMaterials() {
        boolean b = true;

        try {
            b = SDataUtilities.checkRequisitionMatExplotionMat(miClient, mnPkExpYearId, mnPkExpId, false);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        if (!b) {
            miClient.showMsgBoxInformation("La requisiciones de materiales ya tienen órdenes de compra generadas.");
        }

        return b;
    }

    private boolean deleteRecordsExplotionMaterialsRequisition() {
        boolean b = true;

        try {
            moRequest = new SServerRequest(SServerConstants.REQ_DB_ACTION_DELETE);
            moRequest.setPacket(getRegistryExplotionMaterialsRequisition(mnPkExpYearId, mnPkExpId, 0, 0));
            moResponse = miClient.getSessionXXX().request(moRequest);

            if (moResponse.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                throw new Exception(moResponse.getMessage());
            }
            else {
                if (moResponse.getResultType() != SLibConstants.DB_ACTION_DELETE_OK) {
                    b = false;
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_DELETE + (moResponse.getMessage().length() == 0 ? "" : "\n" + moResponse.getMessage()));
                }
            }
        }
        catch (Exception e) {
            b = false;
            System.out.println(e);
        }

        if (!b) {
            miClient.showMsgBoxInformation("La requisiciones de materiales tiene órdenes de compra generadas.");
        }

        return b;
    }

    private boolean getRegistryRequisition() {

        boolean bRequisition = true;

        SDataExplotionMaterialsEntry oExpMatEntry = null;
        SDataRequisition oRequisition = null;
        SDataRequisitionEntry oRequisitionEntry = null;

        mvRequisition = new Vector<SDataRequisition>();

        // Get items that are buy with the same supplier:

        for (int i=0; i < mvExplotionMaterialsEntries.size(); i++) {

            oExpMatEntry = mvExplotionMaterialsEntries.get(i);

            // Check if net requirement is diferent of 0:

            if ((Double) oExpMatEntry.getNet() != 0) {

                // Look up supplier into requisitions:

                bRequisition = true;
                for (int j=0; j<mvRequisition.size(); j++) {

                    oRequisition = mvRequisition.get(j);
                    if (oExpMatEntry.getFkBizPartnerId_n() == oRequisition.getFkBizPartnerId()) {

                        // Add requisition entry to requisition:

                        oRequisitionEntry = new SDataRequisitionEntry();

                        oRequisitionEntry.setQuantity(oExpMatEntry.getNet());
                        oRequisitionEntry.setFkItemId(oExpMatEntry.getPkItemId());

                        oRequisition.getDbmsRequisitionEntry().add(oRequisitionEntry);
                        mvRequisition.set(j, oRequisition);

                        bRequisition = false;
                        break;
                    }
                }
            }
            else{
                bRequisition = false;
            }

            // Create requisition and requisition entry:

            if (bRequisition) {

                oRequisition = new SDataRequisition();
                oRequisition.setPkYearId(SLibTimeUtilities.digestYear(moFieldDate.getDate())[0]);
                oRequisition.setDate(moFieldDate.getDate());
                oRequisition.setFkBizPartnerId(oExpMatEntry.getFkBizPartnerId_n());
                oRequisition.setFkStatusId(SDataConstantsSys.MFGS_ST_ORD_NEW);
                oRequisition.setFkTypeId(SDataConstantsSys.MFGS_TP_REQ_REQ);
                oRequisition.setFkUserNewId(miClient.getSession().getUser().getPkUserId());

                oRequisitionEntry = new SDataRequisitionEntry();
                oRequisitionEntry.setQuantity(oExpMatEntry.getNet());
                oRequisitionEntry.setFkItemId(oExpMatEntry.getPkItemId());

                oRequisition.getDbmsRequisitionEntry().add(oRequisitionEntry);
                mvRequisition.add(oRequisition);
            }
        }

        return bRequisition;

        // End requisition:
    }

    private void actionGeneratePurchaseOrder() {
        // Begin purchase order:

        boolean b = true;

        if (actionGenerateRequisition(true)) {

            for (int iReq=0; iReq<mvRequisition.size() && b; iReq++) {

                // Get requisition entries:

                moRequisition = mvRequisition.get(iReq);
                mvRequisitionEntries = moRequisition.getDbmsRequisitionEntry();

                // Save purchase order:

                if (getRegistryPurchaseOrder()) {

                    try {
                        moRequest = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
                        moRequest.setPacket(moDps);
                        moResponse = miClient.getSessionXXX().request(moRequest);

                        if (moResponse.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                            throw new Exception(moResponse.getMessage());
                        }
                        else {
                            if (moResponse.getResultType() != SLibConstants.DB_ACTION_SAVE_OK) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + (moResponse.getMessage().length() == 0 ? "" : "\n" + moResponse.getMessage()));
                            }
                            else {
                                moDps = (SDataDps) moResponse.getPacket();
                            }
                        }
                    }
                    catch (Exception e) {
                        miClient.showMsgBoxWarning("Error al guardar la orden de compra.");
                        b = false;
                    }

                    // Save requisition materials vs. purchase order:

                    for (int i=0; i<mvRequisitionEntries.size() && b; i++) {
                        for (int j=0; j<moDps.getDbmsDpsEntries().size(); j++) {

                            moRequisitionEntry = mvRequisitionEntries.get(i);
                            moDpsEntry = moDps.getDbmsDpsEntries().get(j);

                            if (moRequisitionEntry.getFkItemId() == moDpsEntry.getFkItemId()) {

                                try {
                                    moRequest = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
                                    moRequest.setPacket(getRegistryRequisitionMaterialsPurchaseOrder(
                                            moRequisitionEntry.getPkReqYearId(), moRequisitionEntry.getPkReqId(), moRequisitionEntry.getPkEntryId(),
                                            moDpsEntry.getPkYearId(), moDpsEntry.getPkDocId(), moDpsEntry.getPkEntryId(), moRequisitionEntry.getQuantity()));
                                    moResponse = miClient.getSessionXXX().request(moRequest);

                                    if (moResponse.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                                        throw new Exception(moResponse.getMessage());
                                    }
                                    else {
                                        if (moResponse.getResultType() != SLibConstants.DB_ACTION_SAVE_OK) {
                                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + (moResponse.getMessage().length() == 0 ? "" : "\n" + moResponse.getMessage()));
                                        }
                                    }
                                }
                                catch (Exception e) {
                                    miClient.showMsgBoxWarning("Error al guardar la orden de compra.");
                                    b = false;
                                }
                                break;
                            }
                        }
                    }
                }
            }

            if (b) {
                miClient.showMsgBoxInformation("Las órdenes de compra han sido creadas.");
            }
        }
    }

    private erp.mmfg.data.SDataRequisitionPurchaseOrder getRegistryRequisitionMaterialsPurchaseOrder(int nPkReqYearId, int nPkReqId, int nPkReqEntryId, int nPkDpsYearId, int nPkDpsId,int nPkDpsEntryId, double dQty) {
        SDataRequisitionPurchaseOrder oRequisitionPurchaseOrder = new SDataRequisitionPurchaseOrder();

        oRequisitionPurchaseOrder.setPkReqYearId(nPkReqYearId);
        oRequisitionPurchaseOrder.setPkReqId(nPkReqId);
        oRequisitionPurchaseOrder.setPkReqEntryId(nPkReqEntryId);
        oRequisitionPurchaseOrder.setPkDpsYearId(nPkDpsYearId);
        oRequisitionPurchaseOrder.setPkDpsDocId(nPkDpsId);
        oRequisitionPurchaseOrder.setPkDpsEntryId(nPkDpsEntryId);
        oRequisitionPurchaseOrder.setQuantity(dQty);

        return oRequisitionPurchaseOrder;
    }

    private boolean getRegistryPurchaseOrder() {
        boolean b = true;
        String item = "";

        moBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { moRequisition.getFkBizPartnerId() }, SLibConstants.EXEC_MODE_VERBOSE);

        if (moBizPartner == null) {
            miClient.showMsgBoxWarning("No se encontro el proveedor.");
            b = false;
        }
        else {
            // Create purchase order:

            createPurchaseOrder();

            moDps.getDbmsDpsEntries().removeAllElements();
            for (int i=0; i < mvRequisitionEntries.size(); i++) {

                // Read requisition entry:

                moRequisitionEntry = mvRequisitionEntries.get(i);
                if (moRequisitionEntry == null) {
                    miClient.showMsgBoxWarning("No se pudo leer la entrada de la requisición de materiales.");
                    b = false;
                    break;
                }

                // Read item:

                moItem = (SDataItem) SDataUtilities.readRegistry(miClient, SDataConstants.ITMU_ITEM, new int[] { moRequisitionEntry.getFkItemId() }, SLibConstants.EXEC_MODE_VERBOSE);
                if (moItem == null) {
                    miClient.showMsgBoxWarning("No se pudo leer el ítem.");
                    b = false;
                    break;
                }

                // Create requisition and requisition entry:

                moDpsEntry = new SDataDpsEntry();
                moDpsEntry.setPkYearId(SLibTimeUtilities.digestYear(moFieldDate.getDate())[0]);
                moDpsEntry.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
                moDpsEntry.setFkDpsAdjustmentTypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[0]);
                moDpsEntry.setFkDpsAdjustmentSubtypeId(SDataConstantsSys.TRNS_STP_DPS_ADJ_NA_NA[1]);
                moDpsEntry.setFkDpsEntryTypeId(SDataConstantsSys.TRNS_TP_DPS_ETY_ORDY);
                moDpsEntry.setFkItemId(moRequisitionEntry.getFkItemId());

                // Item concept:

                if (moDps.getFkLanguajeId() != miClient.getSessionXXX().getParamsErp().getFkLanguageId()) {
                    for (SDataItemForeignLanguage description : moItem.getDbmsItemForeignLanguageDescriptions()) {
                        if (moDps.getFkLanguajeId() == description.getPkLanguageId()) {
                            item = description.getItemShort();
                            break;
                        }
                    }
                }

                if (item.length() == 0) {
                    item = moItem.getItemShort();
                }

                moDpsEntry.setConceptKey(moItem.getKey());
                moDpsEntry.setConcept(item);
                moDpsEntry.setLength(moItem.getLength());
                moDpsEntry.setSurface(moItem.getSurface());
                moDpsEntry.setVolume(moItem.getVolume());
                moDpsEntry.setMass(moItem.getMass());
                moDpsEntry.setWeightGross(moItem.getWeightGross());
                moDpsEntry.setWeightDelivery(moItem.getWeightDelivery());
                moDpsEntry.setSurplusPercentage(0);
                moDpsEntry.setFkItemRefId_n(moRequisitionEntry.getFkItemId());
                moDpsEntry.setFkCostCenterId_n("");
                moDpsEntry.setIsInventoriable(moItem.getIsInventoriable());
                moDpsEntry.setIsDeleted(false);
                moDpsEntry.setQuantity(moRequisitionEntry.getQuantity());
                moDpsEntry.setFkItemId(moRequisitionEntry.getFkItemId());

                calculateTotal();

                moDps.getDbmsDpsEntries().add(moDpsEntry);
            }
        }

        return b;
    }

    private void createPurchaseOrder() {
        double rate = 0;

        moBizPartnerCategory = moBizPartner.getDbmsCategorySettingsSup();

        moDps = new SDataDps();

        moDps.setPkYearId(SLibTimeUtilities.digestYear(moFieldDate.getDate())[0]);
        moDps.setPkDocId(0);
        moDps.setDate(moFieldDate.getDate());
        moDps.setDateDoc(moFieldDate.getDate());
        moDps.setDateStartCredit(moFieldDate.getDate());
        moDps.setNumberSeries("");
        moDps.setNumber("");
        moDps.setIsRegistryNew(true);
        moDps.setIsLinked(false);
        moDps.setIsClosed(false);
        moDps.setIsAudited(false);
        moDps.setIsAuthorized(false);
        moDps.setIsSystem(false);
        moDps.setIsDeleted(false);
        moDps.setFkDpsCategoryId(SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[0]);
        moDps.setFkDpsClassId(SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[1]);
        moDps.setFkDpsTypeId(SDataConstantsSys.TRNU_TP_DPS_PUR_ORD[2]);
        moDps.setFkDpsStatusId(SDataConstantsSys.TRNS_ST_DPS_EMITED);
        moDps.setFkDpsValidityStatusId(SDataConstantsSys.TRNS_ST_DPS_VAL_EFF);
        moDps.setFkDpsAuthorizationStatusId(SDataConstantsSys.TRNS_ST_DPS_AUTHORN_NA);
        moDps.setFkDpsAnnulationTypeId(SModSysConsts.TRNU_TP_DPS_ANN_NA);
        moDps.setFkUserLinkedId(SDataConstantsSys.USRX_USER_NA);
        moDps.setFkUserClosedId(SDataConstantsSys.USRX_USER_NA);
        moDps.setFkUserAuditedId(SDataConstantsSys.USRX_USER_NA);
        moDps.setFkUserAuthorizedId(SDataConstantsSys.USRX_USER_NA);
        moDps.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        moDps.setFkUserEditId(SDataConstantsSys.USRX_USER_NA);
        moDps.setFkUserDeleteId(SDataConstantsSys.USRX_USER_NA);
        moDps.setFkCompanyBranchId(miClient.getSessionXXX().getCurrentCompanyBranchId());
        moDps.setFkBizPartnerId_r(moBizPartner.getPkBizPartnerId());
        moDps.setFkBizPartnerBranchId(moBizPartner.getDbmsBizPartnerBranches().size() > 0 ? moBizPartner.getDbmsBizPartnerBranches().get(0).getPkBizPartnerBranchId() : 0);
        moDps.setFkBizPartnerBranchAddressId(moBizPartner.getDbmsBizPartnerBranches().size() > 0 ? moBizPartner.getDbmsBizPartnerBranches().get(0).getDbmsBizPartnerBranchAddressOfficial().getPkAddressId() : 0);
        moDps.setFkBizPartnerAltId_r(moBizPartner.getPkBizPartnerId());
        moDps.setFkBizPartnerBranchAltId(moBizPartner.getDbmsBizPartnerBranches().size() > 0 ? moBizPartner.getDbmsBizPartnerBranches().get(0).getPkBizPartnerBranchId() : 0);
        moDps.setFkBizPartnerBranchAddressAltId(moBizPartner.getDbmsBizPartnerBranches().size() > 0 ? moBizPartner.getDbmsBizPartnerBranches().get(0).getDbmsBizPartnerBranchAddressOfficial().getPkAddressId() : 0);
        moDps.setFkPaymentSystemTypeId(1);
        moDps.setFkTaxIdentityEmisorTypeId(moBizPartner.getFkTaxIdentityId());
        moDps.setFkTaxIdentityReceptorTypeId(miClient.getSessionXXX().getCompany().getDbmsDataCompany().getFkTaxIdentityId());
        moDps.resetRecord();

        moDps.setIsRecordAutomatic(true);

        // Document's payment type:

        if (moBizPartnerCategory.getEffectiveCreditTypeId() == SDataConstantsSys.BPSS_TP_CRED_CRED_NO) {
            moDps.setFkPaymentTypeId(SDataConstantsSys.TRNS_TP_PAY_CASH);
        }
        else {
            moDps.setFkPaymentTypeId(SDataConstantsSys.TRNS_TP_PAY_CREDIT);
        }

        // Document's language:

        if (moBizPartnerCategory.getFkLanguageId_n() == SLibConstants.UNDEFINED) {
            moDps.setFkLanguajeId(miClient.getSessionXXX().getParamsErp().getFkLanguageId());
        }
        else {
            moDps.setFkLanguajeId(moBizPartnerCategory.getFkLanguageId_n());
        }

        // Document's currency:

        if (moBizPartnerCategory.getFkCurrencyId_n() == SLibConstants.UNDEFINED) {
            moDps.setFkCurrencyId(miClient.getSessionXXX().getParamsErp().getFkCurrencyId());
        }
        else {
            moDps.setFkCurrencyId(moBizPartnerCategory.getFkCurrencyId_n());

            try {
                rate = SDataUtilities.obtainExchangeRate(miClient, moBizPartnerCategory.getFkCurrencyId_n(), moFieldDate.getDate());
            }
            catch (Exception e) {
                SLibUtilities.renderException(this, e);
            }
            moDps.setExchangeRate(rate);
        }
    }

    private void calculateTotal() {
        SParamsItemPriceList paramsItemPriceList = null;

        SDataBizPartnerBranch oParamBizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { moDps.getFkBizPartnerBranchId() }, SLibConstants.EXEC_MODE_VERBOSE);

        // Unitary discount settings:

        if (moItem.getDbmsIsFreeDiscountUnitary()) {
            moDpsEntry.setIsDiscountUnitaryPercentage(false);
            moDpsEntry.setOriginalDiscountUnitaryCy(0d);
        }

        // Entry discount settings:

        if (moItem.getDbmsIsFreeDiscountEntry()) {
            moDpsEntry.setIsDiscountEntryPercentage(false);
            moDpsEntry.setDiscountEntryCy(0d);
        }

        // Document discount settings:

        if (!moDps.getIsDiscountDocApplying() || moItem.getDbmsIsFreeDiscountDoc()) {
            moDpsEntry.setIsDiscountDocApplying(false);
            moDpsEntry.setDiscountDocCy(0d);
        }

        // Obtain item price with discount included or with discount separate:

        try {
            paramsItemPriceList = SDataUtilities.obtainItemPrice(miClient,
                    moBizPartner.getPkBizPartnerId(), moDps.getFkBizPartnerBranchId(),
                    (moDps.getFkDpsCategoryId() == SDataConstantsSys.TRNS_CT_DPS_SAL ?
                    moBizPartner.getDbmsCategorySettingsCus().getFkBizPartnerCategoryId() :
                    moBizPartner.getDbmsCategorySettingsSup().getFkBizPartnerCategoryId()),
                    (moDps.getFkDpsCategoryId() == SDataConstantsSys.TRNS_CT_DPS_SAL ?
                    moBizPartner.getDbmsCategorySettingsCus().getFkBizPartnerTypeId() :
                    moBizPartner.getDbmsCategorySettingsSup().getFkBizPartnerTypeId()),
                    moDps.getFkDpsCategoryId(), moDps.getDateDoc(), moItem.getPkItemId(), moDps.getFkCurrencyId());

            // Check document currency:

            if (SLibUtilities.compareKeys(new int[] { moDps.getFkCurrencyId() }, new int[] { miClient.getSessionXXX().getParamsErp().getFkCurrencyId() })) {
                moDpsEntry.setOriginalPriceUnitaryCy(paramsItemPriceList.getItemPrice());
                moDpsEntry.setDiscountUnitaryCy(paramsItemPriceList.getItemDiscount());
            }
            else {
                moDpsEntry.setOriginalPriceUnitaryCy(moDps.getExchangeRate() != 0 ? paramsItemPriceList.getItemPrice()/moDps.getExchangeRate() : 0);
                moDpsEntry.setOriginalDiscountUnitaryCy(moDps.getExchangeRate() != 0 ? paramsItemPriceList.getItemDiscount()/moDps.getExchangeRate() : 0);
                moDpsEntry.setOriginalPriceUnitarySystemCy(moDps.getExchangeRate() != 0 ? paramsItemPriceList.getItemDiscount()/moDps.getExchangeRate() : 0); // XXX
            }
        }
        catch (Exception e) {
            SLibUtilities.printOutException(this, e);
        }

        moDpsEntry.setOriginalQuantity(moRequisitionEntry.getQuantity());
        moDpsEntry.setIsDiscountUnitaryPercentageSystem(false);   // XXX
        moDpsEntry.setDiscountUnitaryPercentage(0);
        moDpsEntry.setDiscountUnitaryPercentageSystem(0);         // XXX
        moDpsEntry.setDiscountEntryPercentage(0);
        moDpsEntry.setOriginalDiscountUnitarySystemCy(0);        // XXX
        moDpsEntry.setIsTaxesAutomaticApplying(true);

        // Calculate DPS entry's value:

        moDpsEntry.setFkItemId(moItem.getPkItemId());
        moDpsEntry.setFkUnitId(moItem.getFkUnitId());
        moDpsEntry.setFkOriginalUnitId(moItem.getFkUnitId());
        moDpsEntry.setFkTaxRegionId(oParamBizPartnerBranch.getFkTaxRegionId_n() != 0 ? oParamBizPartnerBranch.getFkTaxRegionId_n() : miClient.getSessionXXX().getParamsCompany().getFkDefaultTaxRegionId_n());
        moDpsEntry.setDbmsFkItemGenericId(moItem.getFkItemGenericId());

        moDpsEntry.calculateTotal(miClient, moDps.getDate(),
                moDps.getFkTaxIdentityEmisorTypeId(), moDps.getFkTaxIdentityReceptorTypeId(),
                moDps.getIsDiscountDocPercentage(), moDps.getDiscountDocPercentage(), moDps.getExchangeRate());

        if (moItem.getDbmsDataItemGeneric().getIsLengthApplying() && !moItem.getIsLengthVariable() && !moItem.getDbmsDataItemGeneric().getIsLengthVariable()) {
            moDpsEntry.setLength(moDpsEntry.getQuantity() * moItem.getLength());
        }
        if (moItem.getDbmsDataItemGeneric().getIsMassApplying() && !moItem.getIsMassVariable() && !moItem.getDbmsDataItemGeneric().getIsMassVariable()) {
            moDpsEntry.setMass(moDpsEntry.getQuantity() * moItem.getMass());
        }
        if (moItem.getDbmsDataItemGeneric().getIsWeightGrossApplying()) {
            moDpsEntry.setWeightGross(moDpsEntry.getQuantity() * moItem.getWeightGross());
        }
        if (moItem.getDbmsDataItemGeneric().getIsWeightDeliveryApplying()) {
            moDpsEntry.setWeightDelivery(moDpsEntry.getQuantity() * moItem.getWeightDelivery());
        }

        // End purchase order:
    }

    /*
    private void actionExplotionMaterialsEntryEdit() {
        int index = moExplotionMaterialsEntriesPane.getTable().getSelectedRow();
        SFormExplotionMaterialsEntry oExplotionMaterialsEntryForm = new SFormExplotionMaterialsEntry(miClient);
        SDataExplotionMaterialsEntry oExplotionMaterialsEntryData = null;
        SDataExplotionMaterialsEntryRow oExplotionMaterialsEntryRow = null;

        oExplotionMaterialsEntryForm.formReset();
        oExplotionMaterialsEntryForm.formRefreshCatalogues();
        if (index != -1) {
            oExplotionMaterialsEntryData = (SDataExplotionMaterialsEntry) moExplotionMaterialsEntriesPane.getTableRow(index).getData();
            oExplotionMaterialsEntryForm.setRegistry(oExplotionMaterialsEntryData);
            oExplotionMaterialsEntryForm.setVisible(true);
            if (oExplotionMaterialsEntryForm.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
                oExplotionMaterialsEntryData = (SDataExplotionMaterialsEntry) oExplotionMaterialsEntryForm.getRegistry();
                moExplotionMaterialsEntriesPane.setTableRow(oExplotionMaterialsEntryRow = new SDataExplotionMaterialsEntryRow(oExplotionMaterialsEntryData), index);
                moExplotionMaterialsEntriesPane.renderTableRows();
            }
        }
    }
    */

    public void saveExplotionMaterialsList() {
        if (moExplotionMaterials == null) {
            moExplotionMaterials = new SDataExplotionMaterials();
            moExplotionMaterials.setFkUserNewId(miClient.getSession().getUser().getPkUserId());
        }
        else {
            moExplotionMaterials.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
        }

        moExplotionMaterials.getDbmsExplotionMaterialsEntry().removeAllElements();
        for (int i = 0; i < moExplotionMaterialsEntriesPane.getTableGuiRowCount(); i++) {
            moExplotionMaterials.getDbmsExplotionMaterialsEntry().add((SDataExplotionMaterialsEntry) moExplotionMaterialsEntriesPane.getTableRow(i).getData());
        }

        actionSave();
    }

    private void actionSave() {
        try {
            moRequest = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
            moRequest.setPacket(moExplotionMaterials);
            moResponse = miClient.getSessionXXX().request(moRequest);

            if (moResponse.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                throw new Exception(moResponse.getMessage());
            }
            else {
                if (moResponse.getResultType() != SLibConstants.DB_ACTION_SAVE_OK) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + (moResponse.getMessage().length() == 0 ? "" : "\n" + moResponse.getMessage()));
                }
                else {
                    moExplotionMaterials = (SDataExplotionMaterials) moResponse.getPacket();
                }
            }
        }
        catch (Exception e) {
            miClient.showMsgBoxWarning("Error al guardar la orden de producción.");
        }
    }

    private void renderCompanyBranch() {
        moCompanyBranchSource = null;

        moCompanyBranchSource = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { mnFkCompanyBranchId }, SLibConstants.EXEC_MODE_VERBOSE);

        jtfCompanyBranch.setText(moCompanyBranchSource.getBizPartnerBranch());
        jtfCompanyBranchCode.setText(moCompanyBranchSource.getCode());
        jtfWarehouse.setText("(n/a)");
        jtfWarehouseCode.setText("(n/a)");
    }

    private void renderEntity() {
        moCompanyBranchSource = null;
        moWarehouseSource = null;

        moCompanyBranchSource = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { moFkEntityId.get(0)[0] }, SLibConstants.EXEC_MODE_VERBOSE);
        moWarehouseSource = (SDataCompanyBranchEntity) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_COB_ENT, moFkEntityId.get(0), SLibConstants.EXEC_MODE_VERBOSE);

        jtfCompanyBranch.setText(moCompanyBranchSource.getBizPartnerBranch());
        jtfCompanyBranchCode.setText(moCompanyBranchSource.getCode());
        jtfWarehouse.setText(moWarehouseSource.getEntity());
        jtfWarehouseCode.setText(moWarehouseSource.getCode());
    }

    private void actionDelete() {
        if (miClient.showMsgBoxConfirm("¿Está seguro(a) de eliminar la explosión de materiales?") == JOptionPane.YES_OPTION) {
            moExplotionMaterials.setIsDeleted(true);
            actionSave();
            actionOk();
        }
    }

    private void actionEdit(boolean edit) {

    }

    private void actionOk() {
        erp.lib.form.SFormValidation validation = formValidate();

        if (validation.getIsError()) {
            if (validation.getComponent() != null) {
                validation.getComponent().requestFocus();
            }
            if (validation.getMessage().length() > 0) {
                miClient.showMsgBoxWarning(validation.getMessage());
            }
        }
        else {

            /*if (moExplotionMaterialsEntriesPane.getTableGuiRowCount() > 0) {
                saveExplotionMaterialsList();
            }*/

            mnFormResult = SLibConstants.FORM_RESULT_OK;
            setVisible(false);
        }
    }

    private void actionClose() {
        mnFormResult = SLibConstants.FORM_RESULT_OK;
        setVisible(false);
    }

    /*public void publicActionEdit() {
        actionExplotionMaterialsEntryEdit();
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbDate;
    private javax.swing.JButton jbDelete;
    private javax.swing.JButton jbExportCsv;
    private javax.swing.JButton jbOk;
    private javax.swing.JButton jbRefresh;
    private javax.swing.JButton jbSeek;
    private javax.swing.JLabel jlComments;
    private javax.swing.JLabel jlDate;
    private javax.swing.JLabel jlDbmsProductionOrderQuantity;
    private javax.swing.JLabel jlProductionOrderEnd;
    private javax.swing.JLabel jlProductionOrderInitial;
    private javax.swing.JLabel jlReference;
    private javax.swing.JLabel jlWarehouse;
    private javax.swing.JPanel jpHead;
    private javax.swing.JPanel jpItems;
    private javax.swing.JPanel jpNotesAction;
    private javax.swing.JTextField jtfComments;
    private javax.swing.JTextField jtfCompanyBranch;
    private javax.swing.JTextField jtfCompanyBranchCode;
    private javax.swing.JFormattedTextField jtfDate;
    private javax.swing.JTextField jtfDbmsProductionOrderQuantity;
    private javax.swing.JTextField jtfDbmsProductionOrderUnit;
    private javax.swing.JTextField jtfProductionOrderEnd;
    private javax.swing.JTextField jtfProductionOrderInitial;
    private javax.swing.JTextField jtfReference;
    private javax.swing.JTextField jtfSeek;
    private javax.swing.JTextField jtfWarehouse;
    private javax.swing.JTextField jtfWarehouseCode;
    // End of variables declaration//GEN-END:variables

    public void focusSeek() {
        if (jtfSeek.isEnabled()) {
            jtfSeek.requestFocus();
        }
    }

    public void actionSeek() {
        if (jbSeek.isEnabled()) {
            STableUtilities.actionSeek(miClient, moExplotionMaterialsEntriesPane, jtfSeek.getText().trim());
        }
    }

    public void actionExportCsv() {
        if (jbExportCsv.isEnabled()) {
            STableUtilities.actionExportCsv(miClient, moExplotionMaterialsEntriesPane, getTitle());
        }
    }

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

        moBizPartner = null;
        moBizPartnerCategory = null;
        moItem = null;
        moBom = null;
        moExplotionMaterials = null;
        moExpMatReqMat = null;
        moProductionOrder = null;
        moRequisition = null;
        moRequisitionEntry = null;
        moDps = null;
        moDpsEntry = null;

        moRequest = null;
        moResponse = null;

        mvExplotionMaterialsEntries = new Vector<SDataExplotionMaterialsEntry>();
        mvExplotionMaterialsEntriesItem = new Vector<SDataExplotionMaterialsEntryItem>();
        mvRequisition = new Vector<SDataRequisition>();
        mvRequisitionEntries = new Vector<SDataRequisitionEntry>();
        mvProductionsOrders = new Vector<SDataProductionOrder>();
        mvProductionOrderCharges = new Vector<SDataProductionOrderCharge>();
        mvProductionOrderSubgoods = new Vector<SDataProductionOrderBomSubgoods>();

        mnPkExpYearId = 0;
        mnPkExpId = 0;
        mnOrdYearIdStart = 0;
        mnOrdIdStart = 0;
        mnOrdYearIdEnd = 0;
        mnOrdIdEnd = 0;
        mnFkCompanyBranchId = 0;
        mbIsCompleteExplotion = false;
        moFkEntityId = null;

        moExplotionMaterialsEntriesPane.createTable(null);
        moExplotionMaterialsEntriesPane.clearTableRows();

        jbSeek.setEnabled(true);
        jbRefresh.setEnabled(true);
        jbExportCsv.setEnabled(true);
        jtfSeek.setEnabled(true);
        jtfSeek.setEditable(true);
        jtfSeek.setText("");

        jbOk.setVisible(false);
    }

    @Override
    public void formRefreshCatalogues() {

    }

    @Override
    public erp.lib.form.SFormValidation formValidate() {
        erp.lib.form.SFormValidation validation = new SFormValidation();

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
        return moExplotionMaterials;
    }

    @Override
    public void setValue(int type, java.lang.Object value) {
        switch (type) {
            case 1:
                mnFkCompanyBranchId = (Integer) ((int[]) value)[0];
                renderCompanyBranch();
                break;
            case 2:
                moFkEntityId = (ArrayList<int[]>) value;

                if (moFkEntityId != null) {
                    renderEntity();
                }
                break;
            case 3:
                moFieldDate.setDate((java.util.Date) value);
                break;
            case 4:
                mbIsForecast = (Boolean) value;
                break;
            case 5:
                mbIsCompleteExplotion = (Boolean) value;
                break;
            case 6:
                mbIsProgrammed = (Boolean) value;
                break;
            case 7:
                moFieldProductionOrderInitial.setString(value.toString());
                break;
            case 8:
                moFieldProductionOrderEnd.setString(value.toString());
                break;
            case 9:
                mnOrdYearIdStart = (Integer) ((int[])value)[0];
                mnOrdIdStart = (Integer) ((int[])value)[1];
                mnOrdYearIdEnd = (Integer) ((int[])value)[2];
                mnOrdIdEnd = (Integer) ((int[]) value)[3];
                renderExplotionMaterialsList();
                break;
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
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JButton) {
            javax.swing.JButton button = (javax.swing.JButton) e.getSource();

            if (button == jbOk) {
                actionOk();
            }
            else if (button == jbClose) {
                actionClose();
            }
            else if (button == jbDelete) {
                actionDelete();
            }
            else if (button == jbSeek) {
                actionSeek();
            }
            else if (button == jbExportCsv) {
                actionExportCsv();
            }
            //else if (button == jbRequisition) {
            //    actionGenerateRequisition(false);
            //}
            //else if (button == jbPurchaseOrder) {
            //    actionGeneratePurchaseOrder();
            //}
        }
        else if (e.getSource() instanceof javax.swing.JTextField) {
            JTextField textField = (JTextField) e.getSource();

            if (textField == jtfSeek) {
                actionSeek();
            }
        }
    }
}
