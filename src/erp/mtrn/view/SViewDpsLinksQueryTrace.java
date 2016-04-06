/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import javax.swing.JButton;

/**
 *
 * @author Sergio Flores
 */
public class SViewDpsLinksQueryTrace extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;

    public SViewDpsLinksQueryTrace(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_LINKS_TRACE, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        addTaskBarUpperComponent(moTabFilterDatePeriod);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[19];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "de.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "de.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;

        if (isViewForCategoryPur()) {
            if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave proveedor", 50);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 200);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 200);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave proveedor", 50);
            }
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bb.bpb", "Sucursal proveedor", 75);

            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_d_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dcb.code", "Sucursal empresa doc.", STableConstants.WIDTH_CODE_COB);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_d_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dcb.code", "Sucursal empresa doc.", STableConstants.WIDTH_CODE_COB);

            if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 50);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 200);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 200);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 50);
            }
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bb.bpb", "Sucursal cliente", 75);
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", STableConstants.WIDTH_ITEM_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.orig_qty", "Cantidad", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "u.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ot.code", "Tipo pedido", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_o_num", "Folio pedido", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "o.dt", "Fecha pedido", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ocb.code", "Sucursal empresa pedido", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.code", "Tipo cot./cont.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_c_num", "Folio cot./cont.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "c.dt", "Fecha cot./cont.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ccb.code", "Sucursal empresa cot./cont.", STableConstants.WIDTH_CODE_COB);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabTypeAux01);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.TRN_DVY);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_LINK_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_LINKED);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    private boolean isViewForCategoryPur() {
        return mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR;
    }

    @Override
    public void createSqlQuery() {
        String sqlDatePeriod = "";
        String sqlDocType = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlDatePeriod += "AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
            }
        }

        if (isViewForCategoryPur()) {
            sqlDocType = "AND d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0] + " AND " +
                    "d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] + " ";
        }
        else {
            sqlDocType = "AND d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0] + " AND " +
                    "d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] + " ";
        }

        msSql = "SELECT " +
                "b.bp, bc.bp_key, bb.bpb, i.id_item, i.item_key, i.item, de.orig_qty, u.symbol, " +
                "de.id_year, de.id_doc, de.id_ety, d.dt, dt.code, IF(LENGTH(d.num_ser) = 0, d.num, CONCAT(d.num_ser, '-', d.num)) AS f_d_num, dcb.code, " +
                "oe.id_year, oe.id_doc, oe.id_ety, o.dt, ot.code, IF(LENGTH(o.num_ser) = 0, o.num, CONCAT(o.num_ser, '-', o.num)) AS f_o_num, ocb.code, " +
                "ce.id_year, ce.id_doc, ce.id_ety, c.dt, ct.code, IF(LENGTH(c.num_ser) = 0, c.num, CONCAT(c.num_ser, '-', c.num)) AS f_c_num, ccb.code " +
                "FROM trn_dps AS d INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND d.b_del = 0 AND de.b_del = 0 AND " +
                "d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND d.fid_st_dps_val = " + SDataConstantsSys.TRNS_ST_DPS_VAL_EFF + " " + sqlDocType + sqlDatePeriod +
                "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = " +
                (isViewForCategoryPur() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "INNER JOIN erp.bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps " +
                "INNER JOIN erp.bpsu_bpb AS dcb ON d.fid_cob = dcb.id_bpb " +
                "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON de.fid_orig_unit = u.id_unit " +
                "LEFT OUTER JOIN trn_dps_dps_supply AS dos ON de.id_year = dos.id_des_year AND de.id_doc = dos.id_des_doc AND de.id_ety = dos.id_des_ety " +
                "LEFT OUTER JOIN trn_dps AS o ON dos.id_src_year = o.id_year AND dos.id_src_doc = o.id_doc " +
                "LEFT OUTER JOIN trn_dps_ety AS oe ON dos.id_src_year = oe.id_year AND dos.id_src_doc = oe.id_doc AND dos.id_src_ety = oe.id_ety " +
                "LEFT OUTER JOIN erp.trnu_tp_dps AS ot ON o.fid_ct_dps = ot.id_ct_dps AND o.fid_cl_dps = ot.id_cl_dps AND o.fid_tp_dps = ot.id_tp_dps " +
                "LEFT OUTER JOIN erp.bpsu_bpb AS ocb ON o.fid_cob = ocb.id_bpb " +
                "LEFT OUTER JOIN trn_dps_dps_supply AS ocs ON oe.id_year = ocs.id_des_year AND oe.id_doc = ocs.id_des_doc AND oe.id_ety = ocs.id_des_ety " +
                "LEFT OUTER JOIN trn_dps AS c ON ocs.id_src_year = c.id_year AND ocs.id_src_doc = c.id_doc " +
                "LEFT OUTER JOIN trn_dps_ety AS ce ON ocs.id_src_year = ce.id_year AND ocs.id_src_doc = ce.id_doc AND ocs.id_src_ety = ce.id_ety " +
                "LEFT OUTER JOIN erp.trnu_tp_dps AS ct ON c.fid_ct_dps = ct.id_ct_dps AND c.fid_cl_dps = ct.id_cl_dps AND c.fid_tp_dps = ct.id_tp_dps " +
                "LEFT OUTER JOIN erp.bpsu_bpb AS ccb ON c.fid_cob = ccb.id_bpb " +
                "ORDER BY " +
                "d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt, d.id_year, d.id_doc, de.sort_pos, " +
                "o.num_ser, CAST(o.num AS UNSIGNED INTEGER), o.num, o.dt, o.id_year, o.id_doc, oe.sort_pos, " +
                "c.num_ser, CAST(c.num AS UNSIGNED INTEGER), c.num, c.dt, c.id_year, c.id_doc, ce.sort_pos; ";
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
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

        }
    }
}
