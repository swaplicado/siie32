/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDateType;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.table.SFilterConstants;
import erp.table.STabFilterBizPartner;
import erp.table.STabFilterCompanyBranch;
import erp.table.STabFilterFunctionalArea;
import java.util.Date;
import javax.swing.JButton;

/**
 *
 * @author Néstor Ávalos, Sergio Flores
 */
public class SViewDpsBalanceAging extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.lib.table.STabFilterDateType moTabFilterDateType;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;
    private erp.table.STabFilterCompanyBranch moTabFilterCompanyBranch;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;

    public SViewDpsBalanceAging(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_BAL_AGING, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_DATE);
        moTabFilterDateType = new STabFilterDateType(this);
        moTabFilterCompanyBranch = new STabFilterCompanyBranch(miClient, this);
        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this, new int[] { miClient.getSession().getUser().getPkUserId() });

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDateType);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranch);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterFunctionalArea);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL ? 28 : 27];
        
        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "btp.tp_bp", "Tipo Asociado", 100);
        switch (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ?
            miClient.getSessionXXX().getParamsErp().getFkSortingDpsSupplierTypeId() :
            miClient.getSessionXXX().getParamsErp().getFkSortingDpsCustomerTypeId()) {

            case SDataConstantsSys.CFGS_TP_SORT_DOC_BIZ_P:
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave AN", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave AN", 50);
                }
                break;
            case SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC:

                if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave AN", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave AN", 50);
                }
                break;
            default:
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Asociado negocios", 200);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ct.bp_key", "Clave AN", 50);
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_days_cred", "D. crédito", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_cred_lim", "Lím. crédito", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "f_days_grace", "D. gracia", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt_start_cred", "Base créd.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "date_exp", "Vencimiento", STableConstants.WIDTH_DATE);
        if (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "agent", "Agente de Vtas.", 150);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_audit", "Auditado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "date_due", "Mora", 50);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "range1", "1-15", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "range2", "16-30", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "range3", "31-45", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "range4", "46-60", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "range5", "61-90", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "range6", "91-180", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "range7", "181-360", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "range8", "Más de 360", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "def", "Vencido", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "byDef", "Por vencer", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++].setSumApplying(true);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "street", "Calle", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_street_num", "Número", 50);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "neighborhood", "Colonia", 70);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "zip_code", "CP", 40);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_loc", "Localidad", 100);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tel_01", "Teléfono", 100);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }
        setIsSummaryApplying(true);

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_AUDIT_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DPS_AUDITED);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_TP_BP);

        populateTable();
    }

    @Override
    public void actionNew() {

    }

    @Override
    public void actionEdit() {

    }

    @Override
    public void actionDelete() {

    }

    @Override
    public void createSqlQuery() {
        int[] anData = null;
        Date tSqlDateCut = miClient.getSessionXXX().getWorkingDate();
        String sqlBizPartner = "";
        String sqlCompanyBranch = "";
        String sqlDateType = "";
        String sqlFunctAreas = "";

        String sSqlOrderBy = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                anData = (int[]) setting.getSetting();

                if (anData.length < 3) {
                    miClient.showMsgBoxWarning("Favor de especificar una fecha de corte.");
                } else {
                    tSqlDateCut = SLibTimeUtilities.createDate(anData[0], anData[1], anData[2]);
                }
            }
            else if (setting.getType() == STableConstants.SETTING_OPTION_DATE_TYPE) {

                if (setting.getStatus() > 0) {
                    sqlDateType = "d.dt_start_cred";
                }
                else {
                    sqlDateType = "d.dt_doc";
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB) {
                sqlCompanyBranch += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "AND d.fid_cob = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                sqlBizPartner += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "AND re.fid_bp_nr = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (! ((String) setting.getSetting()).isEmpty()) {
                    sqlFunctAreas += (sqlFunctAreas.isEmpty() ? "" : "AND ") + "d.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
        }

        msSql = "SELECT btp.tp_bp, bp.bp, bpb.code, ct.bp_key, IF(ct.b_cred_usr, ct.days_cred, btp.days_cred) AS f_days_cred, " +
            "IF(ct.b_cred_usr, ct.cred_lim, btp.cred_lim) AS f_cred_lim, IF(ct.b_cred_usr, ct.days_grace, btp.days_grace) AS f_days_grace, " +
            "a.bpb_add, a.street, RTRIM(CONCAT(a.street_num_ext, ' ', a.street_num_int)) AS f_street_num, a.neighborhood, a.zip_code, " +
            "a.po_box, a.fid_tp_add, ta.tp_add, CONCAT(a.locality, ', ', a.state) AS f_loc, " +
            "CONCAT(bpb_con.tel_area_code_01, CASE WHEN LENGTH(bpb_con.tel_area_code_01) = 0 OR LENGTH(bpb_con.tel_num_01) = 0 THEN '' ELSE '-' END, " +
            "bpb_con.tel_num_01, CASE WHEN LENGTH(bpb_con.tel_num_01) = 0 OR LENGTH(bpb_con.tel_ext_01) = 0 THEN '' ELSE '-' END, bpb_con.tel_ext_01) AS tel_01, d.id_year, " +
            "d.id_doc, d.dt_doc, d.dt_start_cred, d.b_audit, d.days_cred  AS days_cred_dps, " +
            "(IF(ac.fid_tp_acc_r = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL ? SDataConstantsSys.FINS_CL_ACC_LIABTY[0] : SDataConstantsSys.FINS_CL_ACC_ASSET[0]) + " AND " +
            "ac.fid_cl_acc_r = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL ? SDataConstantsSys.FINS_CL_ACC_LIABTY[1] : SDataConstantsSys.FINS_CL_ACC_ASSET[1]) + ", " +
            "'(ANT)', IF(d.num_ser IS NULL AND d.num IS NULL, '(S/D)',CONCAT(d.num_ser,IF(LENGTH(d.num_ser) = 0, '', '-'), d.num)))) AS f_num, " +
            "(SELECT bp FROM erp.bpsu_bp WHERE id_bp = COALESCE(d.fid_sal_agt_bp_n, 0)) AS agent, " +
            "DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY) AS date_exp, " +
            "DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) AS date_due, " +
            "c.cur_key, cb.code, c.id_cur, COALESCE(bp1.id_bp, 0) AS agt, COALESCE(rt.id_sal_route, 0) AS rt, d.tot_r, d.tot_r - COALESCE( " +
            "(SELECT SUM(dda.val) FROM trn_dps_dps_adj AS dda WHERE dda.id_dps_year = d.id_year AND dda.id_dps_doc = d.id_doc), 0) AS f_tot_net," +
            "SUM(IF(re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND " +
            "re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + " " + ", -1, 1) * (re.debit - re.credit)) AS f_balance, " +
            "SUM(IF(re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + ", -1, 1) * (re.debit_cur - re.credit_cur)) AS f_balance_cur, " +
            "IF (DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) > 0 AND " +
            "DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) < 16, " +
            "SUM(IF(re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + ", -1, 1) * (re.debit - re.credit)), 0) AS range1, " +
            "IF (DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) > 15 AND " +
            "DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) < 31, " +
            "SUM(IF(re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + ", -1, 1) * (re.debit - re.credit)), 0) AS range2, " +
            "IF (DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) > 30 AND " +
            "DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) < 46, " +
            "SUM(IF(re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + ", -1, 1) * (re.debit - re.credit)), 0) AS range3, " +
            "IF (DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) > 45 AND " +
            "DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) < 61, " +
            "SUM(IF(re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + ", -1, 1) * (re.debit - re.credit)), 0) AS range4, " +
            "IF (DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) > 60 AND " +
            "DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) < 91, " +
            "SUM(IF(re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + ", -1, 1) * (re.debit - re.credit)), 0) AS range5, " +
            "IF (DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) > 90 AND " +
            "DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) < 181, " +
            "SUM(IF(re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + ", -1, 1) * (re.debit - re.credit)), 0) AS range6, " +
            "IF (DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) > 180 AND " +
            "DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) < 361, " +
            "SUM(IF(re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + ", -1, 1) * (re.debit - re.credit)), 0) AS range7, " +
            "IF (DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) > 360, " +
            "SUM(IF(re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + ", -1, 1) * (re.debit - re.credit)), 0) AS range8, " +
            "IF (DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) > 0, " +
            "SUM(IF(re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + ", -1, 1) * (re.debit - re.credit)), 0) AS def, " +
            "IF (DATEDIFF('" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "', DATE_ADD(" + sqlDateType + ", INTERVAL d.days_cred DAY)) < 1 OR (" + sqlDateType + " IS NULL), " +
            "SUM(IF(re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + ", -1, 1) * (re.debit - re.credit)), 0) AS byDef " +
            "FROM fin_rec AS r " +
            "INNER JOIN fin_rec_ety AS re ON r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num AND " +
            "r.b_del = FALSE AND re.b_del = FALSE AND r.id_year = " + (anData.length > 0 ? anData[0] : miClient.getSessionXXX().getWorkingYear())  + " AND r.dt <= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format(tSqlDateCut) + "' AND " +
            "re.fid_ct_sys_mov_xxx = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] : SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[0]) + " AND " +
            "re.fid_tp_sys_mov_xxx = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR ? SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] : SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS[1]) + " " + sqlBizPartner +
            "INNER JOIN fin_acc AS ac ON LEFT(re.fid_acc, INSTR(re.fid_acc, '-')) = LEFT(ac.id_acc, INSTR(ac.id_acc, '-')) AND ac.lev = 1 " +
            "LEFT OUTER JOIN erp.bpsu_bp AS bp ON re.fid_bp_nr = bp.id_bp " +
            "LEFT OUTER JOIN erp.bpsu_bp_ct AS ct ON bp.id_bp = ct.id_bp AND ct.id_ct_bp = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL ? SDataConstantsSys.BPSS_CT_BP_CUS : SDataConstantsSys.BPSS_CT_BP_SUP) + " " +
            "LEFT OUTER JOIN erp.bpsu_tp_bp AS btp ON ct.fid_tp_bp = btp.id_tp_bp AND btp.id_ct_bp = " + (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL ? SDataConstantsSys.BPSS_CT_BP_CUS : SDataConstantsSys.BPSS_CT_BP_SUP) + " " +
            "LEFT OUTER JOIN trn_dps AS d ON re.fid_dps_year_n = d.id_year AND re.fid_dps_doc_n = d.id_doc " + sqlCompanyBranch +
            "LEFT OUTER JOIN erp.bpsu_bpb AS bpb ON d.fid_bpb = bpb.id_bpb " +
            "LEFT OUTER JOIN erp.cfgu_cur AS c ON re.fid_cur = c.id_cur " +
            "LEFT OUTER JOIN erp.bpsu_bpb AS cb ON re.fid_bpb_n = cb.id_bpb " +
            "LEFT OUTER JOIN erp.bpsu_bpb_add AS a ON re.fid_bpb_n = a.id_bpb AND  IF(d.fid_add IS NULL, 1, d.fid_add) = a.id_add AND a.b_del = FALSE " +
            "LEFT OUTER JOIN erp.bpss_tp_add AS ta ON a.fid_tp_add = ta.id_tp_add " +
            "LEFT OUTER JOIN erp.bpsu_bpb_con AS bpb_con ON re.fid_bpb_n = bpb_con.id_bpb  AND bpb_con.id_con = 1 " +
            "LEFT OUTER JOIN mkt_cfg_cus AS cus ON re.fid_bp_nr = cus.id_cus " +
            "LEFT OUTER JOIN erp.bpsu_bp AS bp1 ON re.fid_bpb_n = cus.fid_sal_agt_n " +
            "LEFT OUTER JOIN mkt_cfg_cusb AS cusb ON re.fid_bpb_n = cusb.id_cusb " +
            "LEFT OUTER JOIN mktu_sal_route AS rt ON cusb.fid_sal_route = rt.id_sal_route " +
            "GROUP BY btp.tp_bp, bp.bp, d.id_year, d.id_doc, d.fid_bpb " +
            "HAVING f_balance <> 0 ";

        // Query order by:

        if (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL) {
            if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                sSqlOrderBy = "btp.tp_bp, ct.bp_key, bp.bp_comm, bp.bp, bp.id_bp, ac.fid_tp_acc_r, ac.fid_cl_acc_r DESC, ";
            }
            else if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_NAME_KEY) {
                sSqlOrderBy = "btp.tp_bp, bp.bp, ct.bp_key, bp.bp_comm, bp.id_bp, ac.fid_tp_acc_r, ac.fid_cl_acc_r DESC, ";
            }
            else {
                sSqlOrderBy = "btp.tp_bp, bp.bp_comm, ct.bp_key, bp.bp, bp.id_bp, ac.fid_tp_acc_r, ac.fid_cl_acc_r DESC, ";
            }
        }
        else {
            if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                sSqlOrderBy = "btp.tp_bp, ct.bp_key, bp.bp_comm, bp.bp, bp.id_bp, ac.fid_tp_acc_r, ac.fid_cl_acc_r ASC, ";
            }
            else if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_NAME_KEY) {
                sSqlOrderBy = "btp.tp_bp, bp.bp, ct.bp_key, bp.bp_comm, bp.id_bp, ac.fid_tp_acc_r, ac.fid_cl_acc_r ASC, ";
            }
            else {
                sSqlOrderBy = "btp.tp_bp, bp.bp_comm, ct.bp_key, bp.bp, bp.id_bp, ac.fid_tp_acc_r, ac.fid_cl_acc_r ASC, ";
            }
        }

        msSql += "ORDER BY " + sSqlOrderBy + " date_exp, CONCAT(d.num_ser, '-', d.num), d.dt, re.fid_dps_year_n, re.fid_dps_doc_n, ct.bp_key, f_tot_net, c.cur_key ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (JButton) e.getSource();
        }
    }
}
