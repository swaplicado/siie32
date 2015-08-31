/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterDatePeriodRange;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import erp.table.SFilterConstants;
import erp.table.STabFilterCompanyBranchEntity;
import erp.table.STabFilterDocumentType;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Juan Barajas
 */
public class SViewStockMoves extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    public static final String TXT_DEC_INC = "Ver más decimales";
    public static final String TXT_DEC_DEC = "Ver menos decimales";

    private int mnColIn;
    private int mnColOut;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private erp.table.STabFilterCompanyBranchEntity moTabFilterCompanyBranchEntity;
    private erp.table.STabFilterDocumentType moTabFilterTypeDocument;
    private javax.swing.JToggleButton jtbDecimals;

    public SViewStockMoves(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.TRNX_STK_MOVES);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        STableColumn[] aoTableColumns = null;

        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);
        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterCompanyBranchEntity = new STabFilterCompanyBranchEntity(miClient, this, SDataConstantsSys.CFGS_CT_ENT_WH);
        moTabFilterTypeDocument = new STabFilterDocumentType(miClient, this, SDataConstants.TRNS_TP_IOG);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        addTaskBarUpperComponent(moTabFilterDatePeriodRange);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranchEntity);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterTypeDocument);
        addTaskBarUpperSeparator();

        jtbDecimals = new JToggleButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_dec_inc.gif")));
        jtbDecimals.setSelectedIcon(new ImageIcon(getClass().getResource("/erp/img/icon_std_dec_dec.gif")));
        jtbDecimals.setPreferredSize(new Dimension(23, 23));
        jtbDecimals.setToolTipText(TXT_DEC_INC);
        jtbDecimals.addActionListener(this);
        addTaskBarUpperComponent(jtbDecimals);

        aoTableColumns = new STableColumn[9];

        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 250);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 250);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        }

        mnColIn = i;
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_mov_i", "Entradas", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        mnColOut = i;
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_mov_o", "Salidas", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "wh.code", "Almacén", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp.tp_iog", "Tipo movimiento", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tpa.tp_iog_adj", "Tipo ajuste", 150);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.TRN_DIOG);

        populateTable();
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

    public void actionDecimals() {
        String toolTipText = !jtbDecimals.isSelected() ? TXT_DEC_INC : TXT_DEC_DEC;
        DefaultTableCellRenderer tcr = !jtbDecimals.isSelected() ?
            miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity() :
            miClient.getSessionXXX().getFormatters().getTableCellRendererValueUnitary();

        moTablePane.getTableColumn(mnColIn).setCellRenderer(tcr);
        moTablePane.getTableColumn(mnColOut).setCellRenderer(tcr);

        jtbDecimals.setToolTipText(toolTipText);

        actionRefresh(STableConstants.REFRESH_MODE_RELOAD);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        int[] key = null;
        java.util.Date[] range = null;
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "s.b_del = FALSE ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                range = (java.util.Date[])setting.getSetting();
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "s.dt BETWEEN '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[0])
                                    + "' AND '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(range[1]) + "' ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB_ENT) {
                key = (int[]) setting.getSetting();
                if (key != null) {
                    if (key[0] != SLibConstants.UNDEFINED) {
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "s.id_cob = " + key[0] + " ";
                    }
                    if (key[1] != SLibConstants.UNDEFINED) {
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "s.id_wh = " + key[1] + " ";
                    }
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_DOC_TP) {
                key = (int[]) setting.getSetting();
                if (key != null) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "tp.id_ct_iog = " + key[0] + " AND tp.id_cl_iog = " + key[1] + " AND tp.id_tp_iog = " + key[2] + " ";
                }
            }
        }

        msSql = "SELECT i.item_key, i.item, SUM(s.mov_in) AS f_mov_i, SUM(s.mov_out) AS f_mov_o, u.symbol, bpb.code, wh.code, tp.tp_iog, tpa.tp_iog_adj " +
                "FROM trn_stk AS s " +
                "INNER JOIN erp.itmu_item AS i ON s.id_item = i.id_item AND i.b_inv = TRUE " +
                "INNER JOIN erp.itmu_unit AS u ON s.id_unit = u.id_unit " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON s.id_cob = bpb.id_bpb " +
                "INNER JOIN erp.cfgu_cob_ent AS wh ON s.id_cob = wh.id_cob AND s.id_wh = wh.id_ent AND wh.fid_ct_ent = " + SDataConstantsSys.CFGS_CT_ENT_WH + " " +
                "INNER JOIN erp.trns_tp_iog AS tp ON s.fid_ct_iog = tp.id_ct_iog AND s.fid_cl_iog = tp.id_cl_iog AND s.fid_tp_iog = tp.id_tp_iog " +
                "INNER JOIN erp.trnu_tp_iog_adj AS tpa ON s.fid_tp_iog_adj = tpa.id_tp_iog_adj " +
                "WHERE " + sqlWhere +
                "GROUP BY i.item_key, i.item, u.symbol, bpb.code, wh.code, tp.tp_iog, tpa.id_tp_iog_adj " +
                "ORDER BY i.item_key, i.item, u.symbol, bpb.code, wh.code, tp.tp_iog, tpa.id_tp_iog_adj ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
        }
        else if (e.getSource() instanceof javax.swing.JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();

            if (toggleButton == jtbDecimals) {
                actionDecimals();
            }
        }
    }
}
