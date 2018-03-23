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
import erp.table.STabFilterUsers;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableCellRenderer;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SViewStockMovesEntry extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    public static final String TXT_DEC_INC = "Ver más decimales";
    public static final String TXT_DEC_DEC = "Ver menos decimales";

    private int mnColIn;
    private int mnColOut;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriodRange moTabFilterDatePeriodRange;
    private erp.table.STabFilterCompanyBranchEntity moTabFilterCompanyBranchEntity;
    private erp.table.STabFilterDocumentType moTabFilterTypeDocument;
    private erp.table.STabFilterUsers moTabFilterUser;
    private javax.swing.JToggleButton jtbDecimals;

    public SViewStockMovesEntry(erp.client.SClientInterface client, java.lang.String tabTitle) {
        super(client, tabTitle, SDataConstants.TRNX_STK_MOVES_ETY);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        STableColumn[] aoTableColumns = null;

        moTabFilterDatePeriodRange = new STabFilterDatePeriodRange(miClient, this);
        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterCompanyBranchEntity = new STabFilterCompanyBranchEntity(miClient, this, SDataConstantsSys.CFGS_CT_ENT_WH);
        moTabFilterTypeDocument = new STabFilterDocumentType(miClient, this, SDataConstants.TRNS_TP_IOG);
        moTabFilterUser = new STabFilterUsers(miClient, this);

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
        addTaskBarUpperComponent(moTabFilterUser);
        addTaskBarUpperSeparator();

        jtbDecimals = new JToggleButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_dec_inc.gif")));
        jtbDecimals.setSelectedIcon(new ImageIcon(getClass().getResource("/erp/img/icon_std_dec_dec.gif")));
        jtbDecimals.setPreferredSize(new Dimension(23, 23));
        jtbDecimals.setToolTipText(TXT_DEC_INC);
        jtbDecimals.addActionListener(this);
        addTaskBarUpperComponent(jtbDecimals);

        aoTableColumns = new STableColumn[39];

        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 250);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 250);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave", STableConstants.WIDTH_ITEM_KEY);
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "l.lot", "Lote", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "l.dt_exp_n", "Caducidad", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "l.b_block", "Bloqueado", STableConstants.WIDTH_BOOLEAN);
        mnColIn = i;
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "s.mov_in", "Entradas", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        mnColOut = i;
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "s.mov_out", "Salidas", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cob.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ent.code", "Almacén", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "s.dt", "Fecha", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "st.code", "Código tipo movimiento ES", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "st.tp_iog", "Tipo movimiento", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tpa.tp_iog_adj", "Tipo ajuste", 150);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "s.debit", "Cargos $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "s.credit", "Abonos $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "", "Saldo $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("s.debit", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("s.credit", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "iog.dt", "Fecha doc. ES", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "iogt.code", "Tipo doc. ES", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_iog_num", "Folio doc. ES", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "iog_cob.code", "Sucursal empresa doc. ES", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "iog_ent.code", "Almacén doc. ES", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "s.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dpst.code", "Tipo doc. CV", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_dps_num", "Folio doc. CV", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dps.dt", "Fecha doc. CV", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dps_cob.code", "Sucursal empresa doc. CV", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "adjt.code", "Tipo adj. CV", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_adj_num", "Folio adj. CV", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "adj.dt", "Fecha adj. CV", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "adj_cob.code", "Sucursal empresa adj. CV", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_mfg_num", "Folio ord. prod.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "mfgt.tp", "Tipo ord. prod.", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "mfg_cob.code", "Sucursal empresa ord. prod.", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "mfg_ent.code", "Planta ord. prod.", STableConstants.WIDTH_CODE_COB_ENT);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.TRN_DIOG);
        mvSuscriptors.add(SDataConstants.TRNX_DIOG_MAINT);

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
        String sqlDiogUserId = "";
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
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "iog.fid_ct_iog = " + key[0] + " AND iog.fid_cl_iog = " + key[1] + " AND iog.fid_tp_iog = " + key[2] + " ";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                if ((Integer) setting.getSetting() != SLibConstants.UNDEFINED) {
                    sqlDiogUserId += " AND iog.fid_usr_new = " + (Integer) setting.getSetting() + " ";
                }
            }
        }

        msSql = "SELECT s.id_year, s.id_item, s.id_unit, s.id_lot, s.id_cob, s.id_wh, s.id_mov, s.b_del, " +   // 06
                    "s.dt, st.code, st.tp_iog, tpa.tp_iog_adj, l.lot, l.dt_exp_n, l.b_block, cob.code, ent.code, " +    // 15
                    "i.item_key, i.item, s.mov_in, s.mov_out, u.symbol, s.debit, s.credit, " +                              // 20
                    "iog.dt, iogt.code, CONCAT(iog.num_ser, IF(length(iog.num_ser) = 0, '', '-'), erp.lib_fix_int(iog.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) AS f_iog_num, iog_cob.code, iog_ent.code, " +    // 25
                    "un.usr, iog.ts_new, ue.usr, iog.ts_edit, " +                                       // 29
                    "dpst.code, CONCAT(dps.num_ser, IF(length(dps.num_ser) = 0, '', '-'), dps.num) AS f_dps_num, dps.dt, dps_cob.code, " +  // 33
                    "adjt.code, CONCAT(adj.num_ser, IF(length(adj.num_ser) = 0, '', '-'), adj.num) AS f_adj_num, adj.dt, adj_cob.code, " +  // 37
                    "CAST(CONCAT(mfg.id_year, '-', mfg.num) AS CHAR) AS f_mfg_num, mfgt.tp, mfg_cob.code, mfg_ent.code " + // 41
                    "FROM trn_stk AS s " +
                    "INNER JOIN erp.itmu_item AS i ON s.id_item = i.id_item AND i.b_inv = TRUE " +
                    "INNER JOIN erp.itmu_unit AS u ON s.id_unit = u.id_unit " +
                    "INNER JOIN trn_lot AS l ON s.id_item = l.id_item AND s.id_unit = l.id_unit AND s.id_lot = l.id_lot " +
                    "INNER JOIN erp.bpsu_bpb AS cob ON s.id_cob = cob.id_bpb " +
                    "INNER JOIN erp.cfgu_cob_ent AS ent ON s.id_cob = ent.id_cob AND s.id_wh = ent.id_ent " +
                    "INNER JOIN erp.trns_tp_iog AS st ON s.fid_ct_iog = st.id_ct_iog AND s.fid_cl_iog = st.id_cl_iog AND s.fid_tp_iog = st.id_tp_iog " +
                    "INNER JOIN erp.trnu_tp_iog_adj AS tpa ON s.fid_tp_iog_adj = tpa.id_tp_iog_adj " +
                    "INNER JOIN trn_diog AS iog ON s.fid_diog_year = iog.id_year AND s.fid_diog_doc = iog.id_doc " + sqlDiogUserId +
                    "INNER JOIN erp.trns_tp_iog AS iogt ON iog.fid_ct_iog = iogt.id_ct_iog AND iog.fid_cl_iog = iogt.id_cl_iog AND iog.fid_tp_iog = iogt.id_tp_iog " +
                    "INNER JOIN erp.bpsu_bpb AS iog_cob ON iog.fid_cob = iog_cob.id_bpb " +
                    "INNER JOIN erp.cfgu_cob_ent AS iog_ent ON iog.fid_cob = iog_ent.id_cob AND iog.fid_wh = iog_ent.id_ent " +
                    "INNER JOIN erp.usru_usr AS un ON iog.fid_usr_new = un.id_usr " +
                    "INNER JOIN erp.usru_usr AS ue ON iog.fid_usr_edit = ue.id_usr " +
                    "LEFT OUTER JOIN trn_dps AS dps ON s.fid_dps_year_n = dps.id_year AND s.fid_dps_doc_n = dps.id_doc " +
                    "LEFT OUTER JOIN erp.trnu_tp_dps AS dpst ON dps.fid_ct_dps = dpst.id_ct_dps AND dps.fid_cl_dps = dpst.id_cl_dps AND dps.fid_tp_dps = dpst.id_tp_dps " +
                    "LEFT OUTER JOIN erp.bpsu_bpb AS dps_cob ON dps.fid_cob = dps_cob.id_bpb " +
                    "LEFT OUTER JOIN trn_dps AS adj ON s.fid_dps_adj_year_n = adj.id_year AND s.fid_dps_adj_doc_n = adj.id_doc " +
                    "LEFT OUTER JOIN erp.trnu_tp_dps AS adjt ON adj.fid_ct_dps = adjt.id_ct_dps AND adj.fid_cl_dps = adjt.id_cl_dps AND adj.fid_tp_dps = adjt.id_tp_dps " +
                    "LEFT OUTER JOIN erp.bpsu_bpb AS adj_cob ON adj.fid_cob = adj_cob.id_bpb " +
                    "LEFT OUTER JOIN mfg_ord AS mfg ON iog.fid_mfg_year_n = mfg.id_year AND iog.fid_mfg_ord_n = mfg.id_ord " +
                    "LEFT OUTER JOIN erp.mfgu_tp_ord AS mfgt ON mfg.fid_tp_ord = mfgt.id_tp " +
                    "LEFT OUTER JOIN erp.bpsu_bpb AS mfg_cob ON mfg.fid_cob = mfg_cob.id_bpb " +
                    "LEFT OUTER JOIN erp.cfgu_cob_ent AS mfg_ent ON mfg.fid_cob = mfg_ent.id_cob AND mfg.fid_ent = mfg_ent.id_ent " +
                    "WHERE " + sqlWhere +
                    "ORDER BY i.item_key, i.item, l.lot, s.mov_in, s.mov_out, s.id_unit, cob.code, ent.code, s.dt, st.code, st.tp_iog, iog.num_ser, erp.lib_fix_int(iog.num, " + SDataConstantsSys.NUM_LEN_IOG + ") ";
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
