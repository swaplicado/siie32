/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.table.STabFilterDate;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.trn.db.SDbMaintConfig;
import erp.mtrn.data.STrnDiogComplement;
import erp.mtrn.form.SDialogStockCardex;
import java.awt.Dimension;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sa.lib.SLibUtils;
import sa.lib.grid.SGridUtils;  

/**
 *
 * @author Sergio Flores, Gil De Jesús, Claudio Peña
 */
public class SViewMaintStock extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbStockCardex;
    private javax.swing.JButton jbMaintLentEmpOut;
    private javax.swing.JButton jbMaintLentContOut;
    private javax.swing.JButton jbMaintMaintOut;
    private javax.swing.JButton jbMaintLostOut;
    private javax.swing.JButton jbMaintLentEmpIn;
    private javax.swing.JButton jbMaintLentContIn;
    private javax.swing.JButton jbMaintMaintIn;
    private javax.swing.JButton jbMaintLostIn;
    private erp.lib.table.STabFilterDate moTabFilterDate;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.mtrn.form.SDialogStockCardex moDialogStockCardex;
    private erp.mod.trn.db.SDbMaintConfig moMaintConfig;

    /**
     * 
     * @param client GUI client.
     * @param tabTitle GUI-tab title.
     * @param auxType01 Use case of view: SModSysConsts.TRNX_MAINT_...
     */
    public SViewMaintStock(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_STK_ITEM, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        moTabFilterDate = new STabFilterDate(miClient, this, SLibTimeUtilities.getEndOfYear(miClient.getSessionXXX().getWorkingDate()));
        moTabFilterDeleted = new STabFilterDeleted(this);
        moDialogStockCardex = new SDialogStockCardex(miClient);
        moMaintConfig = (SDbMaintConfig) miClient.getSession().readRegistry(SModConsts.TRN_MAINT_CFG, new int[] { 1 });

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        jbStockCardex = new JButton(miClient.getImageIcon(SLibConstants.ICON_KARDEX));
        jbStockCardex.setPreferredSize(new Dimension(23, 23));
        jbStockCardex.setToolTipText("Ver tarjeta auxiliar de almacén");
        jbStockCardex.addActionListener(this);
        addTaskBarUpperComponent(jbStockCardex);
        //jbStockCardex.setEnabled(!isMaintUserNeeded());
 
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDate);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        
        switch (mnTabTypeAux01) {
            case SModSysConsts.TRNX_MAINT_PART:
                break;
                
            case SModSysConsts.TRNX_MAINT_TOOL:
                break;
                
            case SModSysConsts.TRNX_MAINT_TOOL_AVL:
                jbMaintLentEmpOut = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_stk_maint_lent_emp_out.gif")));
                jbMaintLentEmpOut.setPreferredSize(new Dimension(23, 23));
                jbMaintLentEmpOut.setToolTipText("Préstamo herramienta empleado");
                jbMaintLentEmpOut.addActionListener(this);
                addTaskBarUpperComponent(jbMaintLentEmpOut);

                jbMaintLentContOut = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_stk_maint_lent_cont_out.gif")));
                jbMaintLentContOut.setPreferredSize(new Dimension(23, 23));
                jbMaintLentContOut.setToolTipText("Préstamo herramienta contratista");
                jbMaintLentContOut.addActionListener(this);
                addTaskBarUpperComponent(jbMaintLentContOut);

                jbMaintMaintOut = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_stk_maint_maint_out.gif")));
                jbMaintMaintOut.setPreferredSize(new Dimension(23, 23));
                jbMaintMaintOut.setToolTipText("Mantenimiento herramienta");
                jbMaintMaintOut.addActionListener(this);
                addTaskBarUpperComponent(jbMaintMaintOut);

                jbMaintLostOut = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_stk_maint_lost_out.gif")));
                jbMaintLostOut.setPreferredSize(new Dimension(23, 23));
                jbMaintLostOut.setToolTipText("Extravío herramienta");
                jbMaintLostOut.addActionListener(this);
                addTaskBarUpperComponent(jbMaintLostOut);
                break;
                
            case SModSysConsts.TRNX_MAINT_TOOL_LENT:
                jbMaintLentEmpIn = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_stk_maint_lent_emp_in.gif")));
                
                jbMaintLentEmpIn.setPreferredSize(new Dimension(23, 23));
                jbMaintLentEmpIn.setToolTipText("Devolución herramienta empleado");
                jbMaintLentEmpIn.addActionListener(this);
                addTaskBarUpperComponent(jbMaintLentEmpIn);

                jbMaintLentContIn = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_stk_maint_lent_cont_in.gif")));
                jbMaintLentContIn.setPreferredSize(new Dimension(23, 23));
                jbMaintLentContIn.setToolTipText("Devolución herramienta contratista");
                jbMaintLentContIn.addActionListener(this);
                addTaskBarUpperComponent(jbMaintLentContIn);

                break;
                
            case SModSysConsts.TRNX_MAINT_TOOL_MAINT:
                jbMaintMaintIn = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_stk_maint_maint_in.gif")));
                jbMaintMaintIn.setPreferredSize(new Dimension(23, 23));
                jbMaintMaintIn.setToolTipText("Devolución herramienta mantenimiento");
                jbMaintMaintIn.addActionListener(this);
                addTaskBarUpperComponent(jbMaintMaintIn);
                break;
                
            case SModSysConsts.TRNX_MAINT_TOOL_LOST:
                jbMaintLostIn = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_stk_maint_lost_in.gif")));
                jbMaintLostIn.setPreferredSize(new Dimension(23, 23));
                jbMaintLostIn.setToolTipText("Devolución herramienta extraviada");
                jbMaintLostIn.addActionListener(this);
                addTaskBarUpperComponent(jbMaintLostIn);
                break;
                
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        switch (mnTabTypeAux01) {
            case SModSysConsts.TRNX_MAINT_PART:
                i = 0;
                aoKeyFields = new STableField[2];
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_item");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_unit");

                aoTableColumns = new STableColumn[11];
                break;

            case SModSysConsts.TRNX_MAINT_TOOL:
            case SModSysConsts.TRNX_MAINT_TOOL_AVL:
                i = 0;
                aoKeyFields = new STableField[2];
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_item");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_unit");

                aoTableColumns = new STableColumn[7];
                break;

            case SModSysConsts.TRNX_MAINT_TOOL_LENT:
                i = 0;
                aoKeyFields = new STableField[3];
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_item");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_unit");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "_id_bp");
                aoTableColumns = new STableColumn[11];
                break;

            case SModSysConsts.TRNX_MAINT_TOOL_MAINT:
                 i = 0;
                aoKeyFields = new STableField[3];
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_item");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_unit");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "_id_bp");
                aoTableColumns = new STableColumn[11];
                break;

            case SModSysConsts.TRNX_MAINT_TOOL_LOST:           
                i = 0;
                aoKeyFields = new STableField[3];
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_item");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "s.id_unit");
                aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "_id_bp");
                aoTableColumns = new STableColumn[11];
                break;

            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 250);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.part_num", "No. parte", 250);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 250);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.part_num", "No. parte", 250);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        }

        if (isMaintUserNeeded()) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_bp", "Responsable", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "supv.name", "Residente", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "_signed", "Firmado", STableConstants.WIDTH_BOOLEAN);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "_diog_dt", "Primer movimiento", 100);
        }
        
        if (isStockMaintPart()) {
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_ico", "Estatus existencias", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "sc.qty_min", "Mínimo", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[i++].setCellRenderer(SGridUtils.getCellRendererNumberQuantity());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "sc.rop", "Pto. reorden", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[i++].setCellRenderer(SGridUtils.getCellRendererNumberQuantity());
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "sc.qty_max", "Máximo", STableConstants.WIDTH_QUANTITY);
            aoTableColumns[i++].setCellRenderer(SGridUtils.getCellRendererNumberQuantity());
        }
        
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_mov_i", "Entradas", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++].setCellRenderer(SGridUtils.getCellRendererNumberQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_mov_o", "Salidas", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++].setCellRenderer(SGridUtils.getCellRendererNumberQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_stk", "Existencias", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++].setCellRenderer(SGridUtils.getCellRendererNumberQuantity());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.ITMU_ITEM);
        mvSuscriptors.add(SDataConstants.ITMU_UNIT);
        mvSuscriptors.add(SDataConstants.TRN_DIOG);
        mvSuscriptors.add(SDataConstants.TRNX_MAINT_DIOG);
        mvSuscriptors.add(SDataConstants.TRN_STK_CFG);

        populateTable();

        moTablePane.setDoubleClickAction(this, "actionCardex");
    }
    
    private boolean isMaintUserNeeded() {
        return SLibUtils.belongsTo(mnTabTypeAux01, new int [] { 
            SModSysConsts.TRNX_MAINT_TOOL_LENT, 
            SModSysConsts.TRNX_MAINT_TOOL_MAINT, 
            SModSysConsts.TRNX_MAINT_TOOL_LOST });
    }   
    
    private boolean isStockMaintPart() {
        return SLibUtils.belongsTo(mnTabTypeAux01, new int [] { 
            SModSysConsts.TRNX_MAINT_PART });
    }
    
    private int[] getWarehouseKey() {
        int[] key = null;
        
        switch (mnTabTypeAux01) {
            case SModSysConsts.TRNX_MAINT_PART:
                key = moMaintConfig.getKeyWarehouseParts();
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_AVL:
                key = moMaintConfig.getKeyWarehouseToolsAvailable();
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_LENT:
                key = moMaintConfig.getKeyWarehouseToolsLent();
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_MAINT:
                key = moMaintConfig.getKeyWarehouseToolsMaint();
                break;
            case SModSysConsts.TRNX_MAINT_TOOL_LOST:
                key = moMaintConfig.getKeyWarehouseToolsLost();
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }
        
        return key;
    }
    
    private void actionMove(final int maintMovementType, final int maintUserType) {
        STrnDiogComplement complement = new STrnDiogComplement();
        complement.setMaintMovementType(maintMovementType);
        complement.setMaintUserType(maintUserType);
        
        miClient.getGuiModule(SDataConstants.MOD_INV).setFormComplement(complement);
        if (miClient.getGuiModule(SDataConstants.MOD_INV).showForm(SDataConstants.TRNX_MAINT_DIOG, null) == SLibConstants.DB_ACTION_SAVE_OK) {
            miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(SDataConstants.TRNX_MAINT_DIOG);
        }
    }
    
    @Override
    public void actionNew() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionEdit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionDelete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void actionCardex() {
        if (jbStockCardex.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                int[] rowKey = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();
                int itemId = rowKey[0];
                int unitId = rowKey[1];
                int maintUserId = 0;
                if (isMaintUserNeeded()) {
                    maintUserId = rowKey[2];
                }

                moDialogStockCardex.formReset();
                moDialogStockCardex.setFormParams(moTabFilterDate.getDate(), itemId, unitId, SLibConstants.UNDEFINED, getWarehouseKey(), SLibConstants.MODE_QTY, maintUserId);
                moDialogStockCardex.setVisible(true);
            }
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        int year = 0;
        int[] key = null;
        Date date = null;
        String sqlWhere = "";
        String sqlHaving = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlHaving = "HAVING f_stk <> 0 ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                date = (Date) setting.getSetting();
                year = SLibTimeUtilities.digestYear(date)[0];
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "s.id_year = " + year + " AND " +
                        "s.dt <= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(date) + "' ";
            }
        }
        
        int[] warehouseKey;
        if (mnTabTypeAux01 == SModSysConsts.TRNX_MAINT_TOOL) {
            // special case, gather all tools warehouses
            sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "(";
            warehouseKey = moMaintConfig.getKeyWarehouseToolsAvailable();
            sqlWhere += "(s.id_cob = " + warehouseKey[0] + " AND s.id_wh = " + warehouseKey[1] + ") OR ";
            warehouseKey = moMaintConfig.getKeyWarehouseToolsLent();
            sqlWhere += "(s.id_cob = " + warehouseKey[0] + " AND s.id_wh = " + warehouseKey[1] + ") OR ";
            warehouseKey = moMaintConfig.getKeyWarehouseToolsMaint();
            sqlWhere += "(s.id_cob = " + warehouseKey[0] + " AND s.id_wh = " + warehouseKey[1] + ") OR ";
            warehouseKey = moMaintConfig.getKeyWarehouseToolsLost();
            sqlWhere += "(s.id_cob = " + warehouseKey[0] + " AND s.id_wh = " + warehouseKey[1] + ")";
            sqlWhere += ")";
        }
        else {
            warehouseKey = getWarehouseKey();
            if (warehouseKey != null) {
                sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + "s.id_cob = " + warehouseKey[0] + " AND s.id_wh = " + warehouseKey[1] + " ";
            } 
            else {
                sqlWhere += "";
            }
        }
        
        msSql = "SELECT s.id_item, s.id_unit, i.item_key, i.item, i.part_num, u.symbol, sc.qty_min, sc.rop, sc.qty_max, " +
                (!isMaintUserNeeded() ? "" : "COALESCE(b.id_bp, 0) AS _id_bp, COALESCE(b.bp, 'N/D') AS _bp, supv.name, MIN(s.dt) AS _diog_dt, ") +
                "IF(SUM(s.mov_in - s.mov_out) <= sc.qty_min, " + STableConstants.ICON_VIEW_LIG_RED + ", " +
                "IF(sc.qty_min < SUM(s.mov_in - s.mov_out) AND SUM(s.mov_in - s.mov_out) <= sc.rop, "  + STableConstants.ICON_VIEW_LIG_YEL + ", " +
                "IF(SUM(s.mov_in - s.mov_out) > sc.rop AND SUM(s.mov_in - s.mov_out) <= sc.qty_max, "  + STableConstants.ICON_VIEW_LIG_GRE + ", " +
                "IF(SUM(s.mov_in - s.mov_out) > sc.qty_max, " + STableConstants.ICON_WARN + ", " + STableConstants.ICON_VIEW_LIG_WHI + ")))) AS f_ico, " +
                "SUM(s.mov_in) AS f_mov_i, " +
                "SUM(s.mov_out) AS f_mov_o, " +
                "SUM(s.mov_in - s.mov_out) AS f_stk, " +
                "(SELECT COUNT(*) FROM trn_maint_diog_sig AS sig " +
                "INNER JOIN trn_diog AS dio " +
                "WHERE sig.fk_diog_year = dio.id_year AND sig.fk_diog_doc = dio.id_doc AND sig.ts_usr_ins >= dio.ts_edit) > 0 AS _signed "+
                "FROM trn_stk AS s " +
                "INNER JOIN erp.itmu_item AS i ON i.id_item = s.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON u.id_unit = s.id_unit " +
                "INNER JOIN trn_stk_cfg AS sc ON sc.id_item = s.id_item AND sc.id_unit = s.id_unit AND sc.id_cob = s.id_cob AND sc.id_wh = s.id_wh " +
                (!isMaintUserNeeded() ? "" : 
                "INNER JOIN trn_maint_user_supv AS supv ON s.fid_maint_user_supv = supv.id_maint_user_supv " +
                "LEFT OUTER JOIN erp.bpsu_bp AS b ON b.id_bp = s.fid_maint_user_n ") +
                "WHERE NOT s.b_del " + (sqlWhere.isEmpty() ? "" : "AND " + sqlWhere) +
                "GROUP BY s.id_item, s.id_unit, i.item_key, i.item, u.symbol, sc.qty_min, sc.rop, sc.qty_max" +
                (!isMaintUserNeeded() ? "" : ", b.id_bp, b.bp") + " " +
                sqlHaving +
                "ORDER BY " + (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "i.item_key, i.item, " : "i.item, i.item_key,  ") +
                "s.id_item, u.symbol, s.id_unit" +
                (!isMaintUserNeeded() ? "" : ", b.bp, b.id_bp") + " ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbStockCardex) {
                actionCardex();
            }
            else if (button == jbMaintLentEmpOut) {
                actionMove(SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LENT, SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE);
            }
            else if (button == jbMaintLentContOut) {
                actionMove(SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LENT, SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR);
            }
            else if (button == jbMaintMaintOut) {
                actionMove(SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_MAINT, SModSysConsts.TRNX_TP_MAINT_USER_TOOLS_MAINT_PROV);
            }
            else if (button == jbMaintLostOut) {
                actionMove(SModSysConsts.TRNS_TP_MAINT_MOV_OUT_STAT_TOOL_LOST, SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE);
            }
            else if (button == jbMaintLentEmpIn) {
                actionMove(SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LENT, SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE);
            }
            else if (button == jbMaintLentContIn) {
                actionMove(SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LENT, SModSysConsts.TRNX_TP_MAINT_USER_CONTRACTOR);
            }
            else if (button == jbMaintMaintIn) {
                actionMove(SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_MAINT, SModSysConsts.TRNX_TP_MAINT_USER_TOOLS_MAINT_PROV);
            }
            else if (button == jbMaintLostIn) {
                actionMove(SModSysConsts.TRNS_TP_MAINT_MOV_IN_STAT_TOOL_LOST, SModSysConsts.TRNX_TP_MAINT_USER_EMPLOYEE);
            }
        }
    }
}
