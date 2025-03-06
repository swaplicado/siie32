/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.table.STabFilterDate;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableCellRendererIcon;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mtrn.form.SDialogStockCardex;
import erp.mtrn.form.SDialogStockSegregations;
import erp.table.SFilterConstants;
import erp.table.STabFilterCompanyBranchEntity;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores, Edwin Carmona, Alfredo Perez, Claudio Peña, Isabel Servín, Sergio Flores
 */
public class SViewStock extends erp.lib.table.STableTab implements java.awt.event.ActionListener, java.awt.event.ItemListener {

    public static final String TXT_DEC_INC = "Ver más decimales";
    public static final String TXT_DEC_DEC = "Ver menos decimales";
    
    private int mnColIn;
    private int mnColOut;
    private int mnColStk;
    private int mnColSeg;
    private int mnColAvl;
    protected boolean mbProcessingView;
    private javax.swing.JButton jbCardex;
    private javax.swing.JButton jbSegregations;
    private javax.swing.JToggleButton jtbDecimals;
    private javax.swing.JCheckBox moCheckAllStockConfigs;
    private javax.swing.JLabel moLabelAllStockConfigsHint;
    private erp.lib.table.STabFilterDate moTabFilterDate;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.table.STabFilterCompanyBranchEntity moTabFilterCompanyBranchEntity;
    private erp.mtrn.form.SDialogStockCardex moDialogStockCardex;
    private erp.mtrn.form.SDialogStockSegregations moDialogStockSegregations;

    /*
     * Stock. Supported options:
     * - SDataConstants.TRNX_STK_STK: Stock.
     * - SDataConstants.TRNX_STK_STK_WH: Stock per warehouse.
     * - SDataConstants.TRNX_STK_STK_LOT: Stock per lot.
     * - SDataConstants.TRNX_STK_STK_LOT_WH: Stock per lot, per warehouse.
     * - SDataConstants.TRNX_STK_STK_VALUE_COMM: Stock with commercial value.
     * - SDataConstants.TRNX_STK_STK_VALUE_ACC: Stock with accounting value.
     * 
     * @param auxType01 Constants defined in SDataConstats (TRNX_STK_STK_...).
     *
     */
    public SViewStock(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRN_STK, auxType01);
        initComponents();
    }
      
    private void initComponents() {
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        moTabFilterDate = new STabFilterDate(miClient, this, SLibTimeUtilities.getEndOfYear(miClient.getSessionXXX().getWorkingDate()));
        moTabFilterDeleted = new STabFilterDeleted(this, "Filtrar ítems sin existencias");
        moTabFilterCompanyBranchEntity = new STabFilterCompanyBranchEntity(miClient, this, SDataConstantsSys.CFGS_CT_ENT_WH);
        moDialogStockCardex = new SDialogStockCardex(miClient);
        moDialogStockSegregations = new SDialogStockSegregations(miClient);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        jbCardex = new JButton(miClient.getImageIcon(SLibConstants.ICON_KARDEX));
        jbCardex.setPreferredSize(new Dimension(23, 23));
        jbCardex.setToolTipText("Ver tarjeta auxiliar de almacén");
        jbCardex.addActionListener(this);
        addTaskBarUpperComponent(jbCardex);

        if (showingSegregations()) {
            jbSegregations = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_lock.gif")));
            jbSegregations.setPreferredSize(new Dimension(23, 23));
            jbSegregations.setToolTipText("Ver unidades segregadas");
            jbSegregations.addItemListener(this);
            addTaskBarUpperComponent(jbSegregations);
        }

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDate);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranchEntity);
        addTaskBarUpperSeparator();
                
        jtbDecimals = new JToggleButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_dec_inc.gif")));
        jtbDecimals.setSelectedIcon(new ImageIcon(getClass().getResource("/erp/img/icon_std_dec_dec.gif")));
        jtbDecimals.setPreferredSize(new Dimension(23, 23));
        jtbDecimals.setToolTipText(TXT_DEC_INC);
        jtbDecimals.addActionListener(this);
        addTaskBarUpperComponent(jtbDecimals);
        
        if (showingStockConfigs()) {
            addTaskBarUpperSeparator();
            
            moLabelAllStockConfigsHint = new JLabel(STableCellRendererIcon.moIconInfo, SwingConstants.LEFT);
            moLabelAllStockConfigsHint.setEnabled(false);
            moLabelAllStockConfigsHint.setPreferredSize(new Dimension(15, 23));
            moLabelAllStockConfigsHint.setToolTipText("Máximos y mínimos de ítems sin movimientos en el año mostrado");
            addTaskBarUpperComponent(moLabelAllStockConfigsHint);
            
            moCheckAllStockConfigs = new JCheckBox("Todos los máximos y mínimos");
            moCheckAllStockConfigs.setEnabled(false);
            moCheckAllStockConfigs.setSelected(false);
            moCheckAllStockConfigs.setPreferredSize(new Dimension(200, 23));
            moCheckAllStockConfigs.addItemListener(this);
            addTaskBarUpperComponent(moCheckAllStockConfigs);
        }
        
        int key = 0;
        
        switch (mnTabTypeAux01) {
            case SDataConstants.TRNX_STK_STK:
                aoKeyFields = new STableField[2];
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_item");
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_unit");
                
                aoTableColumns = new STableColumn[9];
                break;

            case SDataConstants.TRNX_STK_STK_WH:
                aoKeyFields = new STableField[4];
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_item");
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_unit");
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_cob");
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_wh");

                aoTableColumns = new STableColumn[15];
                break;
                                
            case SDataConstants.TRNX_STK_STK_LOT:
                aoKeyFields = new STableField[3];
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_item");
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_unit");
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_lot");

                aoTableColumns = new STableColumn[10];
                break;

            case SDataConstants.TRNX_STK_STK_LOT_WH:
                aoKeyFields = new STableField[5];
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_item");
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_unit");
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_lot");
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_cob");
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_wh");

                aoTableColumns = new STableColumn[12];
                break;
            
            case SDataConstants.TRNX_STK_STK_VALUE_COMM:
                aoKeyFields = new STableField[2];
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_item");
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_unit");
                
                aoTableColumns = new STableColumn[9];
                break;
                
            case SDataConstants.TRNX_STK_STK_VALUE_ACC:
                aoKeyFields = new STableField[2];
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_item");
                aoKeyFields[key++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_unit");
                
                aoTableColumns = new STableColumn[10];
                break;
                
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        for (int i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        int col = 0;
        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", 100);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 250);
        }
        else {
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", 250);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", 100);
        }
        
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "part_num", "Número parte", 75);

        if (showingWarehouses()) {
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_cob_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_ent_code", "Almacén", STableConstants.WIDTH_CODE_COB_ENT);
        }

        if (showingStockLots()) {
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "lot", "Lote", 100);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt_exp_n", "Caducidad", STableConstants.WIDTH_DATE);
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_block", "Bloqueado", STableConstants.WIDTH_BOOLEAN);
        }

        if (showingStockConfigs()) {
            aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_ico", "Nivel existencias", STableConstants.WIDTH_ICON);
            aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "qty_min", "Mínimo", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "rop", "Pto. reorden", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "qty_max", "Máximo", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        }

        mnColIn = col;
        aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_mov_i", "Entradas", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        mnColOut = col;
        aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_mov_o", "Salidas", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        mnColStk = col;
        aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_stk", "Existencias", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        
        if (showingSegregations()) {
            mnColSeg = col;
            aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_stk_seg", "Segregadas", STableConstants.WIDTH_QUANTITY_2X);
            aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
            mnColAvl = col;
            aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_stk_ava", "Disponibles", STableConstants.WIDTH_QUANTITY_2X);
            aoTableColumns[col++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        }
        else {
            mnColSeg = -1;
            mnColAvl = -1;
        }
        
        aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        
        if (showingValueCommercial()) {
            aoTableColumns[col++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "price", "Precio comercial $", STableConstants.WIDTH_VALUE_UNITARY);
            aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_comm_val", "Valor comercial $", STableConstants.WIDTH_VALUE_2X);
            aoTableColumns[col++].setSumApplying(true);
            
            setIsSummaryApplying(true);
        }
        else if (showingValueAccounting()) {
            aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_debit", "Debe $", STableConstants.WIDTH_VALUE_2X);
            aoTableColumns[col++].setSumApplying(true);
            aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_credit", "Haber $", STableConstants.WIDTH_VALUE_2X);
            aoTableColumns[col++].setSumApplying(true);
            aoTableColumns[col] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "_acc_val", "Balance $", STableConstants.WIDTH_VALUE_2X);
            aoTableColumns[col++].setSumApplying(true);
            
            setIsSummaryApplying(true);
        }
 
        for (int i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        
        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.ITMU_UNIT);
        mvSuscriptors.add(SDataConstants.TRN_LOT);
        mvSuscriptors.add(SDataConstants.TRN_DIOG);
        mvSuscriptors.add(SDataConstants.TRNX_MAINT_DIOG);
        mvSuscriptors.add(SDataConstants.TRN_STK_CFG);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_SUPPLY_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_SUPPLY_PEND_ETY);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_RETURN_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_RETURN_PEND_ETY);

        populateTable();

        moTablePane.setDoubleClickAction(this, "actionCardex");
    }

    /**
     * Combines with showingStockConfigs(), showingSegregations() and showingStockLots().
     * @return 
     */
    private boolean showingWarehouses() {
        return mnTabTypeAux01 == SDataConstants.TRNX_STK_STK_WH || mnTabTypeAux01 == SDataConstants.TRNX_STK_STK_LOT_WH;
    }

    /**
     * Combines with showingWarehouses().
     * Doesn't combine with showingStockConfigs() nor showingSegregations().
     * @return 
     */
    private boolean showingStockLots() {
        return mnTabTypeAux01 == SDataConstants.TRNX_STK_STK_LOT || mnTabTypeAux01 == SDataConstants.TRNX_STK_STK_LOT_WH;
    }
    
    /**
     * Combines with showingWarehouses() and showingSegregations().
     * Doesn't combine with showingStockLots().
     * @return 
     */
    private boolean showingStockConfigs() {
        return mnTabTypeAux01 == SDataConstants.TRNX_STK_STK_WH;
    }

    /**
     * Combines with showingWarehouses() and showingStockConfigs().
     * Doesn't combine with showingStockLots().
     * @return 
     */
    private boolean showingSegregations() {
        return mnTabTypeAux01 == SDataConstants.TRNX_STK_STK || mnTabTypeAux01 == SDataConstants.TRNX_STK_STK_WH;
    }

    /**
     * Doesn't combine with none of the showing methods.
     * @return 
     */
    private boolean showingValueCommercial() {
        return mnTabTypeAux01 == SDataConstants.TRNX_STK_STK_VALUE_COMM;
    }

    /**
     * Doesn't combine with none of the showing methods.
     * @return 
     */
    private boolean showingValueAccounting() {
        return mnTabTypeAux01 == SDataConstants.TRNX_STK_STK_VALUE_ACC;
    }
    
    private void enableAllStockConfigs(final boolean enable) {
        moLabelAllStockConfigsHint.setEnabled(enable);
        
        moCheckAllStockConfigs.setEnabled(enable);
        moCheckAllStockConfigs.setSelected(false); // allways deselect check box!
    }

    /**
     * Must be public to be accesible from actions map.
     */
    public void actionCardex() {
        int mode = jtbDecimals.isSelected() ? SLibConstants.MODE_QTY_EXT : SLibConstants.MODE_QTY;
        if (jbCardex.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                int[] key = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                int[] whKey = showingWarehouses() ? new int[] { key[key.length - 2], key[key.length - 1] } : moTabFilterCompanyBranchEntity.getCompanyBranchEntityKey();
                int itemId = key[0];
                int unitId = key[1];
                int lotId = showingStockLots() ? key[2] : SLibConstants.UNDEFINED;

                moDialogStockCardex.formReset();
                moDialogStockCardex.setFormParams(moTabFilterDate.getDate(), itemId, unitId, lotId, whKey, mode);
                moDialogStockCardex.setVisible(true);
           }
        }
    }

    private void actionSegregations() {
        int mode = jtbDecimals.isSelected() ? SLibConstants.MODE_QTY_EXT : SLibConstants.MODE_QTY;
        if (jbSegregations.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                int[] key = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                int[] whKey = showingWarehouses() ? new int[] { key[key.length - 2], key[key.length - 1] } : moTabFilterCompanyBranchEntity.getCompanyBranchEntityKey();
                int itemId = key[0];
                int unitId = key[1];

                moDialogStockSegregations.formReset();
                moDialogStockSegregations.setFormParams(moTabFilterDate.getDate(), itemId, unitId, whKey, mode);
                moDialogStockSegregations.setVisible(true);
            }
        }
    }

    private void actionDecimals() {
        String toolTipText = !jtbDecimals.isSelected() ? TXT_DEC_INC : TXT_DEC_DEC;
        DefaultTableCellRenderer tcr = !jtbDecimals.isSelected() ?
            miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity() :
            miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary();

        moTablePane.getTableColumn(mnColIn).setCellRenderer(tcr);
        moTablePane.getTableColumn(mnColOut).setCellRenderer(tcr);
        moTablePane.getTableColumn(mnColStk).setCellRenderer(tcr);
        
        if (mnColSeg != -1) {
            moTablePane.getTableColumn(mnColSeg).setCellRenderer(tcr);
        }
        
        if (mnColAvl != -1) {
            moTablePane.getTableColumn(mnColAvl).setCellRenderer(tcr);
        }

        jtbDecimals.setToolTipText(toolTipText);

        actionRefresh(STableConstants.REFRESH_MODE_RELOAD);
    }
    
    private void itemStateChangedAllStockConfigs() {
        try {
            mbProcessingView = true;
            
            actionReload();
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            mbProcessingView = false;
        }
    }

    @Override
    public void actionNew() {
        if (jbNew.isEnabled()) {

        }
    }

    @Override
    public void actionEdit() {
        if (jbEdit.isEnabled()) {

        }
    }

    @Override
    public void actionDelete() {
        if (jbDelete.isEnabled()) {

        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        int year = 0;
        String mainSqlWhere = "";
        String mainSqlHaving = "";
        String segregSqlWhere = "";
        String segregSqlJoinOn = "";
        String segregSqlColumns = "";
        
        for (int i = 0; i < mvTableSettings.size(); i++) {
            STableSetting setting = (STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED) {
                if (setting.getStatus() == STableConstants.STATUS_ON) {
                    mainSqlHaving += (mainSqlHaving.isEmpty() ? "HAVING " : "OR ") + "_stk <> 0.0 ";
                }
                
                if (showingStockConfigs()) {
                    boolean enable = setting.getStatus() == STableConstants.STATUS_OFF;
                    
                    if (enable != moCheckAllStockConfigs.isEnabled()) {
                        enableAllStockConfigs(enable);
                    }
                }
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                Date date = (Date) setting.getSetting();
                year = SLibTimeUtilities.digestYear(date)[0];
                mainSqlWhere += (mainSqlWhere.isEmpty() ? "" : "AND ") + "s.id_year = " + year + " AND s.dt <= '" + SLibUtils.DbmsDateFormatDate.format(date) + "' ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB_ENT) {
                int[] key = (int[]) setting.getSetting();
                if (key != null) {
                    if (key[0] != SLibConstants.UNDEFINED) {
                        mainSqlWhere += (mainSqlWhere.isEmpty() ? "" : "AND ") + "s.id_cob = " + key[0] + " ";
                        
                        if (showingSegregations()) {
                            segregSqlWhere += (segregSqlWhere.isEmpty() ? "" : "AND ") + "sgwe.id_cob = " + key[0] + " ";
                            segregSqlJoinOn += (segregSqlJoinOn.isEmpty() ? "" : "AND ") + "s.id_cob = tsg.id_cob ";
                            segregSqlColumns += "sgwe.id_cob, ";
                        }
                    }
                    if (key[1] != SLibConstants.UNDEFINED) {
                        mainSqlWhere += (mainSqlWhere.isEmpty() ? "" : "AND ") + "s.id_wh = " + key[1] + " ";
                        
                        if (showingSegregations()) {
                            segregSqlWhere += (segregSqlWhere.isEmpty() ? "" : "AND ") + "sgwe.id_whs = " + key[1] + " ";
                            segregSqlJoinOn += (segregSqlJoinOn.isEmpty() ? "" : "AND ") + "s.id_wh = tsg.id_whs ";
                            segregSqlColumns += "sgwe.id_whs, ";
                        }
                    }
                }
            }
        }
        
        if (mnTabTypeAux01 == SDataConstants.TRNX_STK_STK_VALUE_ACC) {
            mainSqlHaving += (mainSqlHaving.isEmpty() ? "HAVING " : "OR ") + "_acc_val <> 0.0 ";
        }
        
        String tableSegr = "";
        String tablePrice = "";
        String exprStockStd = "SUM(s.mov_in - s.mov_out)";
        String exprStockAvl = exprStockStd;
        
        if (showingSegregations()) {
            tableSegr = "(" +
                    "SELECT " + segregSqlColumns + "sgwe.fid_item, sgwe.fid_unit, SUM(sgwe.qty_inc - sgwe.qty_dec) AS _stk_seg " +
                    "FROM trn_stk_seg AS sg " +
                    "INNER JOIN trn_stk_seg_whs_ety AS sgwe ON sg.id_stk_seg = sgwe.id_stk_seg " +
                    "WHERE NOT sg.b_del AND sgwe.fid_year = " + year + " " + (segregSqlWhere.isEmpty() ? "" : " AND " + segregSqlWhere) + 
                    "GROUP BY " + segregSqlColumns + "sgwe.fid_item, sgwe.fid_unit " +
                    "ORDER BY " + segregSqlColumns + "sgwe.fid_item, sgwe.fid_unit)";
            
            exprStockAvl = "(" + exprStockStd + " - COALESCE(tsg._stk_seg, 0.0))";
        }
        
        if (showingValueCommercial()) {
            tablePrice = "(" +
                    "SELECT pc3.id_item, pc3.id_unit, pc3.price " +
                    "FROM itmu_price_comm_log AS pc3 " +
                    "INNER JOIN (" +
                        "SELECT pc2.id_item, pc2.id_unit, MAX(pc2.id_log) AS _max_id_log " +
                        "FROM itmu_price_comm_log AS pc2 " +
                        "INNER JOIN (" +
                            "SELECT pc1.id_item, pc1.id_unit, MAX(pc1.dt) AS _max_dt " +
                            "FROM itmu_price_comm_log AS pc1 " +
                            "WHERE NOT pc1.b_del " +
                            "GROUP BY pc1.id_item, pc1.id_unit " +
                            "ORDER BY pc1.id_item, pc1.id_unit) AS t1 ON t1.id_item = pc2.id_item AND t1.id_unit = pc2.id_unit AND t1._max_dt = pc2.dt " +
                        "WHERE NOT pc2.b_del " +
                        "GROUP BY pc2.id_item, pc2.id_unit " +
                        "ORDER BY pc2.id_item, pc2.id_unit) AS t2 ON t2.id_item = pc3.id_item AND t2.id_unit = pc3.id_unit AND t2._max_id_log = pc3.id_log " +
                    "WHERE NOT pc3.b_del " +
                    "ORDER BY pc3.id_item, pc3.id_unit)";
        }
        
        String sqlUnionStockConfigs = "";
        String sqlOrderBy = "";
        
        if (showingStockConfigs() && moCheckAllStockConfigs.isSelected()) {
            // warehouses and segregations are being shown as well...
            
            sqlUnionStockConfigs = "\nUNION\n" +
                    "SELECT sc.id_item, sc.id_unit, " +
                    "sc.id_cob, sc.id_wh, cob.code AS _cob_code, ent.code AS _ent_code, " +
                    "sc.qty_min, sc.rop, sc.qty_max, " +
                    "0.0 AS _stk_seg, " +
                    "i.item_key, i.item, i.part_num, u.symbol, " +
                    STableConstants.ICON_INFO + " AS _ico, " +
                    "0.0 AS _mov_i, 0.0 AS _mov_o, 0.0 AS _stk, 0.0 AS _stk_ava" +
                    "\n" +
                    "FROM trn_stk_cfg AS sc " +
                    "INNER JOIN erp.itmu_item AS i ON sc.id_item = i.id_item " +
                    "INNER JOIN erp.itmu_unit AS u ON sc.id_unit = u.id_unit " +
                    "INNER JOIN erp.bpsu_bpb AS cob ON sc.id_cob = cob.id_bpb " +
                    "INNER JOIN erp.cfgu_cob_ent AS ent ON sc.id_cob = ent.id_cob AND sc.id_wh = ent.id_ent " +
                    "\n" +
                    "WHERE NOT sc.b_del AND NOT i.b_del AND NOT u.b_del AND NOT cob.b_del AND NOT ent.b_del " +
                    "AND NOT EXISTS (" +
                        "SELECT DISTINCT s.id_item, s.id_unit, s.id_cob, s.id_wh " +
                        "FROM trn_stk AS s " +
                        "WHERE s.id_item = sc.id_item AND s.id_unit = sc.id_unit AND s.id_cob = sc.id_cob AND s.id_wh = sc.id_wh AND " +
                        "NOT s.b_del " + (mainSqlWhere.isEmpty() ? "" : "AND " + mainSqlWhere) + ") ";
            
            // ORDER BY for UNION query:
            
            sqlOrderBy = "ORDER BY " +
                    (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "item_key, item, " : "item, item_key, ") +
                    "part_num, id_item, symbol, id_unit, " +
                    "_cob_code, _ent_code, id_cob, id_wh ";
        }
        else {
            // ORDER BY for standard query:
            
            sqlOrderBy = "ORDER BY " +
                    (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "i.item_key, i.item, " : "i.item, i.item_key, ") +
                    "i.part_num, s.id_item, u.symbol, s.id_unit " +
                    (!showingWarehouses() ? "" : ", _cob_code, _ent_code, s.id_cob, s.id_wh ") +
                    (!showingStockLots() ? "" : ", l.lot, l.dt_exp_n, l.b_block, s.id_lot ");
        }
        
        msSql = "SELECT s.id_item, s.id_unit, " +
                (!showingStockLots() ? "" : "s.id_lot, l.lot, l.dt_exp_n, l.b_block, ") +
                (!showingWarehouses() ? "" : "s.id_cob, s.id_wh, cob.code AS _cob_code, ent.code AS _ent_code, ") +
                (!showingStockConfigs()? "" : "sc.qty_min, sc.rop, sc.qty_max, ") +
                (!showingSegregations()? "" : "tsg._stk_seg, ") +
                (!showingValueCommercial() ? "" : "tpc.price, ") +
                "i.item_key, i.item, i.part_num, u.symbol, " +
                (!showingStockConfigs()? "" : "CASE " +
                "WHEN " + exprStockAvl + " <= sc.qty_min THEN " + STableConstants.ICON_VIEW_LIG_RED + " " +
                "WHEN " + exprStockAvl + " > sc.qty_min AND " + exprStockAvl + " <= sc.rop THEN "  + STableConstants.ICON_VIEW_LIG_YEL + " " +
                "WHEN " + exprStockAvl + " > sc.rop AND " + exprStockAvl + " <= sc.qty_max THEN "  + STableConstants.ICON_VIEW_LIG_GRE + " " +
                "WHEN " + exprStockAvl + " > sc.qty_max THEN " + STableConstants.ICON_WARN + " " +
                "ELSE " + STableConstants.ICON_VIEW_LIG_WHI + " " +
                "END AS _ico, ") +
                "SUM(s.mov_in) AS _mov_i, SUM(s.mov_out) AS _mov_o, " + exprStockStd + " AS _stk" +
                (!showingSegregations() ? "" : ", " + exprStockAvl + " AS _stk_ava") +
                (!showingValueCommercial() ? "" : ", " + exprStockAvl + " * COALESCE(tpc.price, 0.0) AS _comm_val") +
                (!showingValueAccounting() ? "" : ", SUM(s.debit) AS _debit, SUM(s.credit) AS _credit, SUM(s.debit - s.credit) AS _acc_val") +
                "\n" +
                "FROM trn_stk AS s " +
                "INNER JOIN erp.itmu_item AS i ON s.id_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON s.id_unit = u.id_unit " +
                (!showingStockLots() ? "" : "INNER JOIN trn_lot AS l ON s.id_item = l.id_item AND s.id_unit = l.id_unit AND s.id_lot = l.id_lot ") +
                (!showingWarehouses() ? "" : "INNER JOIN erp.bpsu_bpb AS cob ON s.id_cob = cob.id_bpb INNER JOIN erp.cfgu_cob_ent AS ent ON s.id_cob = ent.id_cob AND s.id_wh = ent.id_ent ") +
                (!showingStockConfigs()? "" : "INNER JOIN trn_stk_cfg AS sc ON sc.id_item = i.id_item AND sc.id_unit = u.id_unit AND sc.id_cob = cob.id_bpb AND sc.id_wh = ent.id_ent ") +
                (!showingSegregations()? "" : "LEFT JOIN " + tableSegr + " AS tsg ON i.id_item = tsg.fid_item AND u.id_unit = tsg.fid_unit" + (segregSqlJoinOn.isEmpty() ? " " : "AND " + segregSqlJoinOn)) +
                (!showingValueCommercial() ? "" : "LEFT JOIN " + tablePrice + " AS tpc ON i.id_item = tpc.id_item AND u.id_unit = tpc.id_unit ") +
                "\n" +
                "WHERE NOT s.b_del " + (mainSqlWhere.isEmpty() ? "" : "AND " + mainSqlWhere) +
                "\n" +
                "GROUP BY s.id_item, s.id_unit, " +
                (!showingStockLots() ? "" : "s.id_lot, l.lot, l.dt_exp_n, l.b_block, ") +
                (!showingWarehouses() ? "" : "s.id_cob, s.id_wh, cob.code, ent.code, ") +
                (!showingStockConfigs()? "" : "sc.qty_min, sc.rop, sc.qty_max, ") +
                (!showingSegregations()? "" : "tsg._stk_seg, ") +
                (!showingValueCommercial() ? "" : "tpc.price, ") +
                "i.item_key, i.item, i.part_num, u.symbol " +
                (mainSqlHaving.isEmpty() ? "" : "\n" + mainSqlHaving) +
                sqlUnionStockConfigs + // when needed a complementary query will be added
                "\n" +
                sqlOrderBy;
    }
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbCardex) {
                actionCardex();
            }
            else if (button == jbSegregations) {
                actionSegregations();
            }
        }        
       else if (e.getSource() instanceof javax.swing.JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();

            if (toggleButton == jtbDecimals) {
                actionDecimals();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JCheckBox && !mbProcessingView) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            
            if (checkBox == moCheckAllStockConfigs && moCheckAllStockConfigs.isEnabled()) { // prevent refreshing when check box is disabled!
                itemStateChangedAllStockConfigs();
            }
        }
    }
}
