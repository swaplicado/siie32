package erp.mod.mkt.view;

import erp.data.SDataConstantsSys;
import erp.gui.grid.SGridFilterPanelSalesAgent;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.SLibConsts;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateRange;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.grid.SGridUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;
import sa.lib.gui.SGuiDate;

/**
 *
 * @author Edwin Carmona
 * 
 * View of commissions of sales agents by money flow
 */
public class SViewComissionsMoneyFlow extends SGridPaneView {

    private SGridFilterDateRange moFilterDateRange;
    private SGridFilterPanelSalesAgent moFilterSalesAgent;
    
    /**
     * @param client GUI client.
     * @param title View's GUI tab title.
     */
    public SViewComissionsMoneyFlow(SGuiClient client, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.MKTX_COMMS_PAY_REC, SLibConsts.UNDEFINED, title);
        setRowButtonsEnabled(false);
        jtbFilterDeleted.setEnabled(false);
        
        moFilterDateRange = new SGridFilterDateRange(miClient, this);
        moFilterDateRange.initFilter(new Date[] {new SGuiDate(SGuiConsts.GUI_DATE_DATE, miClient.getSession().getCurrentDate().getTime()), new SGuiDate(SGuiConsts.GUI_DATE_DATE, miClient.getSession().getCurrentDate().getTime())});
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);
        
        moFilterSalesAgent = new SGridFilterPanelSalesAgent(miClient, this, SModConsts.BPSX_BP_X_SAL_AGT);
        moFilterSalesAgent.initFilter(null);
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterSalesAgent);
    }
    
    @Override
    public void prepareSqlQuery() {
        String filterSql = "";
        Object filter = null;
        Date[] dateRange = null;

        filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DATE_RANGE)).getValue();
        if (filter != null) {
            dateRange = (Date[]) filter;
            filterSql += "AND " + SGridUtils.getSqlFilterDateRange("rec.dt", dateRange);
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SModConsts.BPSX_BP_X_SAL_AGT))== null ? null : ((SGridFilterValue) moFiltersMap.get(SModConsts.BPSX_BP_X_SAL_AGT)).getValue();
        if (filter != null && ((int[]) filter).length == 1) {
            filterSql += "AND IF(cb.fid_sal_agt_n IS NOT NULL, cb.fid_sal_agt_n, ccus.fid_sal_agt_n) = " + ((int[]) filter)[0] + " ";
        }
        
        moPaneSettings = new SGridPaneSettings(5);
        
        msSql = "SELECT " +
            "ety.id_year AS " + SDbConsts.FIELD_ID + "1, " +
            "ety.id_per AS " + SDbConsts.FIELD_ID + "2, " +
            "ety.id_bkc AS " + SDbConsts.FIELD_ID + "3, " +
            //"ety.id_tp_rec AS " + SDbConsts.FIELD_ID + "4, " +
            "ety.id_num AS " + SDbConsts.FIELD_ID + "4, " +
            "ety.id_ety AS " + SDbConsts.FIELD_ID + "5, " +
            "ety.id_tp_rec AS " + SDbConsts.FIELD_CODE + ", " +
            "ety.concept AS " + SDbConsts.FIELD_NAME + ", " +
            "rec.dt AS " + SDbConsts.FIELD_DATE + ", " +
            "ety.concept, bp.bp, bp.id_bp, " +
            "CONCAT(rec.id_year, '-', erp.lib_fix_int(rec.id_per, 2)) AS f_rper, CONCAT(rec.id_tp_rec, '-', erp.lib_fix_int(rec.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) AS f_rnum, " +
            "ety.debit, " +
            "bpa.id_bp AS _id_sal_agt, bpa.bp AS _sal_agt, " +
            "COALESCE(IF((SELECT agt_per FROM mkt_comms_sal_agt WHERE id_tp_link = " + SModSysConsts.ITMS_LINK_ALL + " AND " +
            "id_sal_agt = IF(cb.fid_sal_agt_n IS NOT NULL, cb.fid_sal_agt_n, ccus.fid_sal_agt_n)) IS NOT NULL, " +
            "(SELECT agt_per FROM mkt_comms_sal_agt WHERE id_tp_link = " + SModSysConsts.ITMS_LINK_ALL + " AND id_sal_agt = IF(cb.fid_sal_agt_n IS NOT NULL, cb.fid_sal_agt_n, ccus.fid_sal_agt_n)), " +
            "(SELECT agt_per FROM mkt_cfg_sal_agt AS cfa LEFT OUTER JOIN mkt_comms_sal_agt_tp AS atp ON cfa.fid_tp_sal_agt = atp.id_tp_sal_agt " +
            "WHERE id_tp_link = " + SModSysConsts.ITMS_LINK_ALL + " AND id_sal_agt = IF(cb.fid_sal_agt_n IS NOT NULL, cb.fid_sal_agt_n, ccus.fid_sal_agt_n))), 0) AS _com_perc " +
            "FROM fin_rec AS rec " +
            "INNER JOIN fin_rec_ety AS ety ON rec.id_year = ety.id_year AND rec.id_per = ety.id_per AND rec.id_tp_rec = ety.id_tp_rec AND rec.id_num = ety.id_num " +
            "INNER JOIN erp.bpsu_bp AS bp ON ety.fid_bp_nr = bp.id_bp " +
            "INNER JOIN mkt_cfg_cusb AS cb ON cb.id_cusb = bp.id_bp " +
            "INNER JOIN mkt_cfg_cus AS ccus ON ccus.id_cus = bp.id_bp " +
            "LEFT OUTER JOIN erp.bpsu_bp AS bpa ON IF(cb.fid_sal_agt_n IS NOT NULL, cb.fid_sal_agt_n, ccus.fid_sal_agt_n) = bpa.id_bp " +
            "WHERE NOT ety.b_del AND NOT bp.b_del AND NOT ccus.b_del AND fid_cl_sys_acc = " + SModSysConsts.FINS_CL_SYS_ACC_BPR_CUS + " " +
            "AND fid_bp_nr IS NOT NULL AND (ety.debit <> 0 OR ety.debit_cur <> 0) AND rec.id_tp_rec = '" + SModSysConsts.FINU_TP_REC_CASH_BANK + "' " +
            filterSql +
            "ORDER BY bp.bp, bp.id_bp, f_date, _sal_agt, _com_perc; ";
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> columns = new ArrayList<>();
        SGridColumnView col = null;
        
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_PER, "f_rper", "Periodo"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, "f_rnum", "Folio póliza"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, SGridConsts.COL_TITLE_DATE));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "ety.concept", "Concepto"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "bp.bp", "Cliente"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_BPR, "bp.id_bp", "Cve cliente"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT, "_sal_agt", "Agente ventas"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "ety.debit", "$ Monto"));
        columns.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "_com_perc", "Porcentaje"));
        
        col = new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "", "$ Comisión");
        col.getRpnArguments().add(new SLibRpnArgument("ety.debit", SLibRpnArgumentType.OPERAND));
        col.getRpnArguments().add(new SLibRpnArgument("_com_perc", SLibRpnArgumentType.OPERAND));
        col.getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.MULTIPLICATION, SLibRpnArgumentType.OPERATOR));
        columns.add(col);
        
        return columns;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
    }
}
