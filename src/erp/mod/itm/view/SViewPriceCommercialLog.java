/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.itm.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STableConstants;
import erp.mod.SModConsts;
import erp.mod.trn.form.SDialogItemPriceCardex;
import erp.mod.trn.view.SViewMaterialRequest;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDatePeriod;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridRowView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Isabel Servin, Sergio Flores, Edwin Carmona, Rodrigo Ayala
 */
public class SViewPriceCommercialLog extends SGridPaneView implements ActionListener {
    //public static final int PRICES_CURRENT = 1;
    //public static final int PRICES_ALL = 2;
    private JButton jbItemPriceCardex;
    private JButton mjbToSearch;
    private JButton mjbCleanSearch;
    private JButton mjbSearchItemKey;
    private JButton mjbViewDps;

    private String msSeekQueryText;
    private JTextField moTextToSearch;

    private SDialogItemPriceCardex moDialogItemPriceCardex;
    private SGridFilterDatePeriod moFilterDatePeriod;

    /**
     * Creates view of Price commercial log.
     * @param client GUI client.
     * @param title View's title.
     */
    public SViewPriceCommercialLog(SGuiClient client,int subtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.ITMU_PRICE_COMM_LOG, subtype, title);
        initComponetsCustom();
    }
    
    private void initComponetsCustom() {
        jbRowDisable.setEnabled(false);
        
        if (mnGridSubtype == SModConsts.ITMX_PRICE_COMM_LOG_CURR) {
            jbRowEdit.setEnabled(false);
            jbRowDelete.setEnabled(false);
            jtbFilterDeleted.setEnabled(false);
        }
        else if (mnGridSubtype == SModConsts.ITMX_PRICE_COMM_LOG_ALL){
            moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_YEAR, miClient.getSession().getCurrentDate().getTime()));        
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
        
        jbItemPriceCardex = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_kardex.gif")), "Ver historial de precios comerciales del ítem", this);
        mjbToSearch = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/switch_filter.gif")), "Buscar", this);
        mjbCleanSearch = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_delete.gif")), "Limpiar busqueda", this);
        mjbSearchItemKey = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_query_doc.gif")), "Buscar por clave de ítem", this);
        mjbViewDps = SGridUtils.createButton(miClient.getImageIcon(SLibConstants.ICON_LOOK), "Ver documento origen", this);        
        
        moTextToSearch = new JTextField("");
        moTextToSearch.setPreferredSize(new Dimension(150, 23));
        moTextToSearch.setToolTipText("Texto a buscar");
        
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbItemPriceCardex);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moTextToSearch);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbToSearch);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbCleanSearch);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbSearchItemKey);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(new JPopupMenu.Separator());
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(mjbViewDps);
        
        msSeekQueryText = "";
        
        moDialogItemPriceCardex = new SDialogItemPriceCardex(miClient, "Historial de precios comerciales");
    }
    
    private void actionCardex() {
        int[] key;
        
        if (jbItemPriceCardex.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                try {
                    SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                    if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                        miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                    }
                    else {
                        key = (int[]) gridRow.getRowPrimaryKey();
                        
                        moDialogItemPriceCardex.setFormParams(key);
                        moDialogItemPriceCardex.setVisible(true);
                    }
                }
                catch (Exception ex) {
                    Logger.getLogger(SViewMaterialRequest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private int[] getDpsPrimaryKey(int[] itemLogPk) {
        String sql = "SELECT fk_dps_year_n, fk_dps_doc_n "
                + "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_PRICE_COMM_LOG) + " "
                + "WHERE id_item = " + itemLogPk[0] + " AND id_unit = " + itemLogPk[1] + " AND id_log = " + itemLogPk[2] + " ";
        ResultSet resultSet = null;
        int[] dpsKey = null;
        try {
            resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                dpsKey = new int[] { resultSet.getInt(1), resultSet.getInt(2) };
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(SViewPriceCommercialLog.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try { resultSet.close(); } catch (Exception e) {}
        }
        return dpsKey != null ? dpsKey : new int[] { SLibConsts.UNDEFINED, SLibConsts.UNDEFINED };
    }

    private void actionSearch() {
        if (jtTable.getRowCount() > 1) {
            String text = moTextToSearch.getText().trim();
            if (text.length() > 0) {
                msSeekQueryText = "(DATE_FORMAT(v.dt, '%d/%m/%Y') LIKE '%" + text + "%' OR "
                        + "DATE_FORMAT(d.dt, '%d/%m/%Y') LIKE '%" + text + "%' OR "
                        + "i.item_key LIKE '%" + text + "%' OR "
                        + "i.item LIKE '%" + text + "%' OR "
                        + "u.symbol LIKE '%" + text + "%' OR "
                        + "CONCAT(d.num_ser, "
                        + " IF(LENGTH(d.num_ser) = 0, '', '-'), "
                        + " d.num) LIKE '%" + text + "%' OR "
                        + "b.bp LIKE '%" + text + "%' ";
                
                msSeekQueryText += ") ";
            }
            else {
                msSeekQueryText = "";
            }

            moTextToSearch.requestFocus();
            actionGridReload();
        }
    }
    
    private void actionCleanSearch() {
        moTextToSearch.setText("");
        msSeekQueryText = "";
        moTextToSearch.requestFocus();
        actionGridReload();
    }
    
    private void actionFilterItem() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else {
                String sItemKey = gridRow.getRowCode();
                moTextToSearch.setText(sItemKey);
                actionSearch();
            }
        }
    }
    
    private void actionViewDps() {
        if (jtTable.getSelectedRowCount() != 1) {
            miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
        }
        else {
            SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

            if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
            }
            else {
                int gui = SDataConstants.MOD_PUR;    // GUI module
                int dpsKey[] = getDpsPrimaryKey(gridRow.getRowPrimaryKey());
                if (dpsKey == null || dpsKey[0] == 0) {
                    miClient.showMsgBoxWarning("Este registro no proviene de un documento de compra");
                    return;
                }
                ((SClientInterface) miClient).getGuiModule(gui).setFormComplement(SDataConstantsSys.TRNU_TP_DPS_PUR_INV);
                ((SClientInterface) miClient).getGuiModule(gui).showForm(SDataConstants.TRNX_DPS_RO, dpsKey);
            }
        }
    }

    @Override
    public void prepareSqlQuery() {
        String where = "";
        Object filter;

        moPaneSettings = new SGridPaneSettings(3);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);
        moPaneSettings.setSystemApplying(true);
        
        if (mnGridSubtype == SModConsts.ITMX_PRICE_COMM_LOG_CURR) {
            moPaneSettings.setDeletedApplying(false);
            where += (where.isEmpty() ? "" : "AND ") + "NOT v.b_del ";
        }
        else if (mnGridSubtype == SModConsts.ITMX_PRICE_COMM_LOG_ALL) {
            moPaneSettings.setDeletedApplying(true);
            filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
            if ((Boolean) filter) {
                where += (where.isEmpty() ? "" : "AND ") + "NOT v.b_del ";
            }
            if (moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD) != null) {
                filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
                if (filter != null) {
                    where += (where.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter); //fecha de trabajo de la sesión de usuario
                }
            }
        }

        if (msSeekQueryText.length() > 0) {
            where += (where.isEmpty() ? "" : "AND ") + msSeekQueryText;
        }
        String from = "";
        
        if (mnGridSubtype == SModConsts.ITMX_PRICE_COMM_LOG_CURR) {
            from = "FROM (SELECT pcl.* " 
                + "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_PRICE_COMM_LOG) + " AS pcl " 
                + "INNER JOIN ( " 
                + " SELECT p.id_item, p.id_unit, MAX(p.id_log) AS _max_id_log " 
                + " FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_PRICE_COMM_LOG) + " AS p " 
                + " INNER JOIN ( " 
                + "  SELECT id_item, id_unit, MAX(dt) AS _max_dt " 
                + "  FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_PRICE_COMM_LOG) + " " 
                + "  WHERE NOT b_del " 
                + "  GROUP BY id_item, id_unit " 
                + "  ORDER BY id_item, id_unit) AS t1 ON t1.id_item = p.id_item AND t1.id_unit = p.id_unit AND t1._max_dt = p.dt " 
                + " WHERE NOT p.b_del " 
                + " GROUP BY p.id_item, p.id_unit " 
                + " ORDER BY p.id_item, p.id_unit) AS t2 ON t2.id_item = pcl.id_item AND t2.id_unit = pcl.id_unit AND t2._max_id_log = pcl.id_log " 
                + "WHERE NOT pcl.b_del " 
                + "ORDER BY pcl.id_item, pcl.id_unit) AS v ";
        }
        else if (mnGridSubtype == SModConsts.ITMX_PRICE_COMM_LOG_ALL){
            from = "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_PRICE_COMM_LOG) + " AS v ";
        }
        
        msSql = "SELECT "
                + "v.id_item AS " + SDbConsts.FIELD_ID + "1, " 
                + "v.id_unit AS " + SDbConsts.FIELD_ID + "2, " 
                + "v.id_log AS " + SDbConsts.FIELD_ID + "3, " 
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", " 
                + "i.item_key AS " + SDbConsts.FIELD_CODE + ", " 
                + "i.item AS " + SDbConsts.FIELD_NAME + ", "  
                + "u.symbol, "  
                + "v.price, "
                + "'" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS f_cur_key_local, " 
                + "b.bp, " 
                + "d.dt AS dt_dps, "
                + "IF(d.num_ser <> '', CONCAT(d.num_ser, '-', d.num), d.num) AS num_dps, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", " 
                + "v.b_sys AS " + SDbConsts.FIELD_IS_SYS + ", " 
                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "
                + from
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON " 
                + "v.id_item = i.id_item " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " AS u ON " 
                + "v.id_unit = u.id_unit " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON " 
                + "v.fk_usr_ins = ui.id_usr " 
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON " 
                + "v.fk_usr_upd = uu.id_usr "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON " 
                + "v.fk_dps_year_n = d.id_year AND v.fk_dps_doc_n = d.id_doc "
                + "LEFT JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON " 
                + "d.fid_bp_r = b.id_bp " 
                + (where.isEmpty() ? "" : "WHERE " + where) + " ";
        
        msSql += "ORDER BY "
                + (((erp.client.SClientInterface) miClient).getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? 
                    "i.item_key, i.item, " : 
                    "i.item, i.item_key, ");
        
        if (mnGridSubtype == SModConsts.ITMX_PRICE_COMM_LOG_CURR) {
            msSql += "u.symbol ";
        }
        else if (mnGridSubtype == SModConsts.ITMX_PRICE_COMM_LOG_ALL){
            msSql += "u.symbol, v.dt ASC, v.id_log ASC ";
        }
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();
        
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, SDbConsts.FIELD_CODE, "Clave ítem"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, SDbConsts.FIELD_NAME, "Ítem"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "symbol", "Unidad"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, mnGridSubtype == SModConsts.ITMX_PRICE_COMM_LOG_CURR ? "Vigente desde" : "Fecha")); // SGridConsts.COL_TITLE_DATE
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT_UNIT, "price", mnGridSubtype == SModConsts.ITMX_PRICE_COMM_LOG_CURR ?  "Precio comercial actual $" : "Precio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "f_cur_key_local", "Moneda local", STableConstants.WIDTH_CURRENCY_KEY));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt_dps", "Fecha documento origen"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "num_dps", "Folio documento origen", 75));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "bp", "Asociado de negocios"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_SYS, SGridConsts.COL_TITLE_IS_SYS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        
        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);
        moSuscriptionsSet.add(SModConsts.TRN_DPS);
        moSuscriptionsSet.add(SModConsts.BPSU_BP);
        moSuscriptionsSet.add(SModConsts.USRU_USR);     
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbItemPriceCardex) {
                actionCardex();
            }
            else if (button == mjbToSearch) {
                actionSearch();
            }
            else if (button == mjbCleanSearch) {
                actionCleanSearch();
            }
            else if (button == mjbSearchItemKey) {
                actionFilterItem();
            }
            else if (button == mjbViewDps) {
                actionViewDps();
            }
        }
    }
}
