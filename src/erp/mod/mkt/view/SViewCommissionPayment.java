/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.mkt.view;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.mod.SModConsts;
import erp.mod.bps.db.SDbBizPartner;
import erp.mod.mkt.db.SDbCommissionPayment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sa.lib.SLibUtils;
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
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Néstor Ávalos
 */
public class SViewCommissionPayment extends SGridPaneView implements ActionListener {

    private JButton jbPrint;
    private SGridFilterDatePeriod moFilterDatePeriod;

    public SViewCommissionPayment(SGuiClient client, int gridSubtype, String title, SGuiParams params) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.MKT_COMMS_PAY, gridSubtype, title, params);
        initComponentCustom();
    }

    public void initComponentCustom() {
        setRowButtonsEnabled(false);

        moFilterDatePeriod = new SGridFilterDatePeriod(miClient, this, SGuiConsts.DATE_PICKER_DATE_PERIOD);
        moFilterDatePeriod.initFilter(new SGuiDate(SGuiConsts.GUI_DATE_MONTH, miClient.getSession().getCurrentDate().getTime()));
        getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);

        if (mnGridSubtype == SModConsts.VIEW_SC_SUM) {
            jbRowDelete.setEnabled(true);
            jbPrint = SGridUtils.createButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_print.gif")), "Imprimir reporte", this);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(jbPrint);
        }
    }

    /*
     * Private methods
     */

    private void actionPrint() {
        Map<String, Object> oMap = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;
        SDbCommissionPayment payment = null;
        
        if (jbPrint.isEnabled()) {
            if (jtTable.getSelectedRowCount() != 1) {
                miClient.showMsgBoxInformation(SGridConsts.MSG_SELECT_ROW);
            }
            else {
                SGridRowView gridRow = (SGridRowView) getSelectedGridRow();

                if (gridRow.getRowType() != SGridConsts.ROW_TYPE_DATA) {
                    miClient.showMsgBoxWarning(SGridConsts.ERR_MSG_ROW_TYPE_DATA);
                }
                else {
                    try {
                        //SMktUtils.printReportCommissionsPayment(miClient, gridRow.getRowPrimaryKey()); 16/12/2014 jbarajas new report of commissions
                        payment = (SDbCommissionPayment) miClient.getSession().readRegistry(SModConsts.MKT_COMMS_PAY, gridRow.getRowPrimaryKey());
                        
                        oMap = miClient.createReportParams();

                        oMap.put("sSqlWherePeriodCom", "");
                        oMap.put("nFidCtDps", SDataConstantsSys.TRNS_CT_DPS_SAL);
                        oMap.put("nFidClDps", SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1]);
                        oMap.put("nFidTpDps", SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2]);
                        oMap.put("nFidStDps", SDataConstantsSys.TRNS_ST_DPS_EMITED);
                        oMap.put("nFidValDpsId", SDataConstantsSys.TRNS_ST_DPS_VAL_EFF);
                        oMap.put("nFidCtBpId", SDataConstantsSys.BPSS_CT_BP_CUS);
                        oMap.put("nSalesAgentId", payment.getFkSalesAgentId());
                        oMap.put("sSalesAgent", miClient.getSession().readField(SModConsts.BPSU_BP, new int[] { payment.getFkSalesAgentId() }, SDbBizPartner.FIELD_NAME));
                        oMap.put("sCompanyBranch", ((SClientInterface) miClient).getSessionXXX().getCurrentCompanyBranch() == null ? "(TODAS)" : ((SClientInterface) miClient).getSessionXXX().getCurrentCompanyBranch().getBizPartnerBranch());
                        oMap.put("sSqlWhereCompanyBranch", ((SClientInterface) miClient).getSessionXXX().getCurrentCompanyBranch() == null ? "" : " AND d.fid_cob = " + ((SClientInterface) miClient).getSessionXXX().getCurrentCompanyBranch().getPkBizPartnerBranchId() + " ");
                        oMap.put("sSqlHaving", "");
                        oMap.put("sSqlInnerLeftPayment", " INNER JOIN mkt_comms_pay AS p ON p.fk_sal_agt = c.id_sal_agt " +
                                "INNER JOIN mkt_comms_pay_ety AS pe ON p.id_pay = pe.id_pay AND pe.fk_year = d.id_year " +
                                "AND pe.fk_doc = d.id_doc AND pe.fk_ety = de.id_ety AND pe.fk_sal_agt = c.id_sal_agt ");
                        oMap.put("sSqlWherePayment", " AND pe.id_pay = " + payment.getPkPaymentId() + " ");

                        jasperPrint = SDataUtilities.fillReport(((SClientInterface) miClient), SDataConstantsSys.REP_TRN_COMMS_DPS, oMap);
                        jasperViewer = new JasperViewer(jasperPrint, false);
                        jasperViewer.setTitle("Reporte de comisiones pagadas");
                        jasperViewer.setVisible(true);
                        
                    } catch (Exception e) {
                        SLibUtils.showException(this, e);
                    }
                }
            }
        }
    }

    /*
     * Public methods
     */

    @Override
    public void prepareSqlQuery() {
        try {
            String sql = "";
            Object filter = null;

            moPaneSettings = new SGridPaneSettings(1);

            moPaneSettings.setUserUpdateApplying(true);

            filter = (Boolean) moFiltersMap.get(SGridConsts.FILTER_DELETED).getValue();
            if ((Boolean) filter) {
                sql += (sql.isEmpty() ? "" : "AND ") + "v.b_del = 0 ";
            }

            filter = (SGuiDate) moFiltersMap.get(SGridConsts.FILTER_DATE_PERIOD).getValue();
            sql += (sql.isEmpty() ? "" : "AND ") + SGridUtils.getSqlFilterDate("v.dt", (SGuiDate) filter);

            msSql = "SELECT "
                + "v.id_pay AS " + SDbConsts.FIELD_ID + "1, "
                + "v.num AS " + SDbConsts.FIELD_CODE + ", "
                + "'' AS " + SDbConsts.FIELD_NAME + ", "
                + "v.dt AS " + SDbConsts.FIELD_DATE + ", "
                + "a.bp, "

                + (mnGridSubtype == SModConsts.VIEW_SC_SUM ?
                  "v.pay_r, "
                + "v.rfd_r, "
                + "v.tot_r, "
                + "v.b_ann, "
                + "v.b_del AS " + SDbConsts.FIELD_IS_DEL + ", "
                + "v.fk_sal_agt, "
                + "v.fk_usr_ann, "
                + "v.ts_usr_ann, "
                + "v.fk_usr_ann AS f_fk_usr_annul, "
                + "v.ts_usr_ann AS f_ts_usr_annul, "
                + "ua.usr AS f_usr_annul, " : "")

                + (mnGridSubtype == SModConsts.VIEW_SC_DET ?
                  "pe.id_pay, "
                + "pe.id_ety, "
                + "pe.pay, "
                + "pe.rfd, "
                + "(pe.pay - pe.rfd) AS f_tot_r, "
                + "pe.fk_year, "
                + "pe.fk_doc, "
                + "pe.fk_ety, "
                + "pe.fk_sal_agt, "
                + "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, "
                + "d.stot_r AS f_stot_r, "
                + "b.bp, "
                + "i.item, "
                + "i.item_key, " : "")

                + "v.fk_usr_ins AS " + SDbConsts.FIELD_USER_INS_ID + ", "
                + "v.ts_usr_ins AS " + SDbConsts.FIELD_USER_INS_TS + ", "
                + "ui.usr AS " + SDbConsts.FIELD_USER_INS_NAME + ", "
                + "v.fk_usr_upd AS " + SDbConsts.FIELD_USER_UPD_ID + ", "
                + "v.ts_usr_upd AS " + SDbConsts.FIELD_USER_UPD_TS + ", "
                + "uu.usr AS " + SDbConsts.FIELD_USER_UPD_NAME + " "

                + "FROM " + SModConsts.TablesMap.get(mnGridType) + " AS v "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS_PAY_ETY) + " AS pe ON "
                + "v.id_pay = pe.id_pay "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS a ON "
                + "v.fk_sal_agt = a.id_bp "

                + (mnGridSubtype == SModConsts.VIEW_SC_DET ?
                  "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.MKT_COMMS) + " AS c ON "
                + "pe.fk_year = c.id_year AND pe.fk_doc = c.id_doc AND pe.fk_ety = c.id_ety AND pe.fk_sal_agt = c.id_sal_agt "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS) + " AS d ON "
                + "c.id_year = d.id_year AND c.id_doc = d.id_doc "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_DPS_ETY) + " AS de ON "
                + "d.id_year = de.id_year AND d.id_doc = de.id_doc AND c.id_ety = de.id_ety "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.BPSU_BP) + " AS b ON "
                + "d.fid_bp_r = b.id_bp "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i ON "
                + "de.fid_item = i.id_item "
                : "" )

                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ua ON "
                + "v.fk_usr_ann = ua.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS ui ON "
                + "v.fk_usr_ins = ui.id_usr "
                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.USRU_USR) + " AS uu ON "
                + "v.fk_usr_upd = uu.id_usr "
                + (sql.isEmpty() ? "" : "WHERE " + sql) + " "
                + "GROUP BY " + (mnGridSubtype == SModConsts.VIEW_SC_SUM ?
                  "v.id_pay " :
                  "pe.id_pay, pe.id_ety ");
        } 
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<SGridColumnView>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_REG_NUM, SDbConsts.FIELD_CODE, "Folio"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, SDbConsts.FIELD_DATE, "Fecha"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "a.bp", "Agente ventas"));

        if (mnGridSubtype == SModConsts.VIEW_SC_SUM) {

            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.pay_r", "Comisión $"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.rfd_r", "Reembolso $"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "v.tot_r", "Total $"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, "v.b_ann", "Anulado"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_BOOL_S, SDbConsts.FIELD_IS_DEL, SGridConsts.COL_TITLE_IS_DEL));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, "f_usr_annul", "Usr anu"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, "f_ts_usr_annul", "Usr TS anu"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_INS_NAME, SGridConsts.COL_TITLE_USER_INS_NAME));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_INS_TS, SGridConsts.COL_TITLE_USER_INS_TS));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_USR, SDbConsts.FIELD_USER_UPD_NAME, SGridConsts.COL_TITLE_USER_UPD_NAME));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE_DATETIME, SDbConsts.FIELD_USER_UPD_TS, SGridConsts.COL_TITLE_USER_UPD_TS));
        }
        else {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "pe.pay", "Comisión $"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "pe.rfd", "Reembolso $"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_tot_r", "Total $"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "b.bp", "Asociado negocios"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_CAT, "f_num", "Folio docto"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_2D, "f_stot_r", "Subtotal $"));
        }

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SDataConstants.MKT_COMMS);
        moSuscriptionsSet.add(SDataConstants.USRU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPrint) {
                actionPrint();
            }
        }
    }
}
