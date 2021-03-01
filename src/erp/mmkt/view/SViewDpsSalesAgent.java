/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mmkt.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mod.SModConsts;
import erp.mtrn.data.SDataDps;
import erp.mtrn.form.SDialogUpdateDpsSalesAgentComms;
import erp.table.SFilterConstants;
import erp.table.STabFilterBizPartner;
import erp.table.STabFilterCompanyBranch;
import erp.table.STabFilterFunctionalArea;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;

/**
 *
 * @author Juan Barajas, Sergio Flores, Edwin Carmona
 */
public class SViewDpsSalesAgent extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton mjbClose;
    private javax.swing.JButton mjbOpen;
    private javax.swing.JButton mjbViewDps;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton mjbViewLinks;
    private javax.swing.JButton mjbChangeAgentSupervisor;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterCompanyBranch moTabFilterCompanyBranch;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    private erp.mtrn.form.SDialogUpdateDpsSalesAgentComms moDialogUpdateDpsSalesAgentComms;

    public SViewDpsSalesAgent(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.MKTX_COMMS_DPS_SAL_AGT, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        mjbClose = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_CLOSE));
        mjbOpen = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_OPEN));
        mjbChangeAgentSupervisor = new JButton(new ImageIcon(getClass().getResource("/erp/img/icon_std_bp_pay_cash.gif")));
        mjbViewDps = new JButton(miClient.getImageIcon(SLibConstants.ICON_LOOK));
        mjbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        mjbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));

        mjbClose.setPreferredSize(new Dimension(23, 23));
        mjbOpen.setPreferredSize(new Dimension(23, 23));
        mjbChangeAgentSupervisor.setPreferredSize(new Dimension(23, 23));
        mjbViewDps.setPreferredSize(new Dimension(23, 23));
        mjbViewNotes.setPreferredSize(new Dimension(23, 23));
        mjbViewLinks.setPreferredSize(new Dimension(23, 23));

        mjbClose.addActionListener(this);
        mjbOpen.addActionListener(this);
        mjbChangeAgentSupervisor.addActionListener(this);
        mjbViewDps.addActionListener(this);
        mjbViewNotes.addActionListener(this);
        mjbViewLinks.addActionListener(this);

        mjbClose.setToolTipText("Cerrar para pago comisiones");
        mjbOpen.setToolTipText("Abrir para pago comisiones");
        mjbChangeAgentSupervisor.setToolTipText("Cambiar agente/supervisor");
        mjbViewDps.setToolTipText("Ver documento");
        mjbViewNotes.setToolTipText("Ver notas");
        mjbViewLinks.setToolTipText("Ver vínculos del documento");

        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        
        moTabFilterCompanyBranch = new STabFilterCompanyBranch(miClient, this);
        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, SDataConstantsSys.BPSS_CT_BP_CUS);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this, SModConsts.CFGU_FUNC, new int[] { miClient.getSession().getUser().getPkUserId() });
        moDialogUpdateDpsSalesAgentComms = new SDialogUpdateDpsSalesAgentComms(miClient);

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        addTaskBarUpperComponent(mjbClose);
        addTaskBarUpperComponent(mjbOpen);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranch);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterFunctionalArea);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbChangeAgentSupervisor);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbViewDps);
        addTaskBarUpperComponent(mjbViewNotes);
        addTaskBarUpperComponent(mjbViewLinks);

        mjbClose.setEnabled(true);
        mjbOpen.setEnabled(true);
        mjbViewDps.setEnabled(true);
        mjbViewNotes.setEnabled(true);
        mjbViewLinks.setEnabled(true);
        mjbChangeAgentSupervisor.setEnabled(true);

        aoKeyFields = new STableField[2];
        aoTableColumns = new STableColumn[23];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "code_bpb", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt_doc_delivery_n", "Entrega programada", STableConstants.WIDTH_DATE);

        if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave cliente", 50);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Cliente", 200);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Cliente", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave cliente", 50);
        }
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb", "Sucursal cliente", 75);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_sal_agt", "Agente", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "comm_agt", "Comisión agente $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "pay_agt", "Pago agente $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "", "Dif. agente $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("comm_agt", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("pay_agt", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_comms_date_pay_agt", "Agente últ pago", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_sal_sup", "Supervisor", 200);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "comm_sup", "Comisión supervisor $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "pay_sup", "Pago supervisor $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "", "Dif. supervisor $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("comm_sup", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("pay_sup", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "f_comms_date_pay_sup", "Supervisor últ pago", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "pay_other", "Pago otros $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "f_close", "Cerrado", STableConstants.WIDTH_BOOLEAN);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        getTablePane().setDoubleClickAction(this, "publicActionViewDps");

        populateTable();
    }

    private void actionCloseDoc() {
        if (mjbClose.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_DOC_CLOSE) == JOptionPane.YES_OPTION) {
                    try {
                        SDataDps oDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

                        oDps.saveField(miClient.getSession().getDatabase().getConnection(), (int[]) oDps.getPrimaryKey(), SDataDps.FIELD_CLO_COMMS, 1);
                        oDps.saveField(miClient.getSession().getDatabase().getConnection(), (int[]) oDps.getPrimaryKey(), SDataDps.FIELD_CLO_COMMS_USR, miClient.getSession().getUser().getPkUserId());
                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                    }
                    catch (Exception e) {
                        SLibUtilities.printOutException(this, e);
                    }
                }
            }
        }
    }

    private void actionOpenDoc() {
        if (mjbOpen.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                SDataDps oDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

                if (!oDps.getIsClosedCommissions()) {
                    miClient.showMsgBoxInformation("No se puede abrir un documento que no ha sido cerrado de forma manual para pago de comisiones.");
                }
                else {
                    try {
                        oDps.saveField(miClient.getSession().getDatabase().getConnection(), (int[]) oDps.getPrimaryKey(), SDataDps.FIELD_CLO_COMMS, 0);
                        oDps.saveField(miClient.getSession().getDatabase().getConnection(), (int[]) oDps.getPrimaryKey(), SDataDps.FIELD_CLO_COMMS_USR, miClient.getSession().getUser().getPkUserId());
                        miClient.getGuiModule(SDataConstants.MOD_SAL).refreshCatalogues(mnTabType);
                    }
                    catch (Exception e) {
                        SLibUtilities.printOutException(this, e);
                    }
                }
            }
        }
    }

    private void actionViewDps() {
        int gui = SDataConstants.MOD_SAL;

        if (moTablePane.getSelectedTableRow() != null) {
            miClient.getGuiModule(gui).setFormComplement(SDataConstantsSys.TRNU_TP_DPS_SAL_INV);
            miClient.getGuiModule(gui).showForm(SDataConstants.TRNX_DPS_RO, moTablePane.getSelectedTableRow().getPrimaryKey());
        }
    }

    private void actionViewNotes() {
        if (mjbViewNotes.isEnabled()) {
            SModuleUtilities.showDocumentNotes(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow());
        }
    }

    private void actionViewLinks() {
        if (mjbViewLinks.isEnabled()) {
            SModuleUtilities.showDocumentLinks(miClient, moTablePane.getSelectedTableRow());
        }
    }

    private void actionChangeAgentSupervisor() {
        if (mjbChangeAgentSupervisor.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int gui = SDataConstants.MOD_SAL;    // GUI module

                moDialogUpdateDpsSalesAgentComms.formReset();
                moDialogUpdateDpsSalesAgentComms.setValue(SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey());
                moDialogUpdateDpsSalesAgentComms.setFormVisible(true);

                if (moDialogUpdateDpsSalesAgentComms.getFormResult() == SLibConstants.FORM_RESULT_OK) {
                    miClient.getGuiModule(gui).refreshCatalogues(mnTabType);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        String sqlDatePeriod = "";
        String sqlCompanyBranch = "";
        String sqlBizPartner = "";
        String sqlFunctAreas = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);

            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlDatePeriod = "AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB) {
                sqlCompanyBranch = ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "AND d.fid_cob = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                sqlBizPartner = ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "AND d.fid_bp_r = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (! ((String) setting.getSetting()).isEmpty()) {
                    sqlFunctAreas += "AND d.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
        }

        msSql = "SELECT DISTINCT(id_year), id_doc, dt, dt_doc_delivery_n, b_close, b_del, ts_close, " +
                "num_ser, num, num_ref, CONCAT(num_ser, IF(length(num_ser) = 0, '', '-'), num) AS f_num, " +
                "ts_audit, tot_cur_r, code, cur_key, id_bp, bp, bp_key, id_bpb, bpb, code_bpb, usr, " +
                "f_sal_agt, f_sal_sup, comm_agt, comm_sup, pay_agt, pay_sup, pay_other, f_comms_date_pay_agt, f_comms_date_pay_sup, f_close " +
                "FROM(";

        msSql += "(SELECT d.id_year, d.id_doc, d.dt, d.dt_doc_delivery_n, d.b_close, d.b_del, d.ts_close, " +
                    "d.num_ser, d.num, d.num_ref, CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                    "d.ts_audit, d.tot_cur_r, dt.code, c.cur_key, b.id_bp, b.bp, bc.bp_key, bb.id_bpb, bb.bpb, cb.code AS code_bpb, ua.usr, " +
                    "a.bp AS f_sal_agt, s.bp AS f_sal_sup, " +
                    "(SELECT COALESCE(SUM(comms), 0) AS f_comms " +
                    "FROM mkt_comms AS c " +
                    "WHERE c.id_year = d.id_year AND c.id_doc = d.id_doc AND c.id_sal_agt = d.fid_sal_agt_n) AS comm_agt, " +
                    "(SELECT COALESCE(SUM(comms), 0) AS f_comms " +
                    "FROM mkt_comms AS c " +
                    "WHERE c.id_year = d.id_year AND c.id_doc = d.id_doc AND c.id_sal_agt = d.fid_sal_sup_n) AS comm_sup, " +
                    "(SELECT COALESCE(SUM(pay), 0) AS f_pay " +
                    "FROM mkt_comms_pay AS c " +
                    "INNER JOIN mkt_comms_pay_ety AS ce ON c.id_pay = ce.id_pay " +
                    "WHERE c.b_del = 0 AND ce.fk_year = d.id_year AND ce.fk_doc = d.id_doc AND ce.fk_sal_agt = d.fid_sal_agt_n) AS pay_agt, " +
                    "(SELECT COALESCE(SUM(pay), 0) AS f_pay " +
                    "FROM mkt_comms_pay AS c " +
                    "INNER JOIN mkt_comms_pay_ety AS ce ON c.id_pay = ce.id_pay " +
                    "WHERE c.b_del = 0 AND ce.fk_year = d.id_year AND ce.fk_doc = d.id_doc AND ce.fk_sal_agt = d.fid_sal_sup_n) AS pay_sup, " +
                    "(SELECT COALESCE(SUM(pay), 0) AS f_pay " +
                    "FROM mkt_comms_pay AS c " +
                    "INNER JOIN mkt_comms_pay_ety AS ce ON c.id_pay = ce.id_pay " +
                    "WHERE c.b_del = 0 AND ce.fk_year = d.id_year AND ce.fk_doc = d.id_doc AND (ce.fk_sal_agt <> COALESCE(d.fid_sal_agt_n, 0) AND ce.fk_sal_agt <> COALESCE(d.fid_sal_sup_n, 0))) AS pay_other, " +
                    "COALESCE((SELECT MAX(p.dt) " +
                    "FROM mkt_comms_pay AS p " +
                    "INNER JOIN mkt_comms_pay_ety AS pe ON " +
                    "p.id_pay = pe.id_pay " +
                    "WHERE p.b_del = 0 AND pe.fk_year = d.id_year AND pe.fk_doc = d.id_doc AND pe.fk_sal_agt = d.fid_sal_agt_n " +
                    "GROUP BY  pe.fk_year, pe.fk_doc), null) AS f_comms_date_pay_agt, " +
                    "COALESCE((SELECT MAX(p.dt) " +
                    "FROM mkt_comms_pay AS p " +
                    "INNER JOIN mkt_comms_pay_ety AS pe ON " +
                    "p.id_pay = pe.id_pay " +
                    "WHERE p.b_del = 0 AND pe.fk_year = d.id_year AND pe.fk_doc = d.id_doc AND pe.fk_sal_agt = d.fid_sal_sup_n " +
                    "GROUP BY  pe.fk_year, pe.fk_doc), null) AS f_comms_date_pay_sup, " +
                    "d.b_close_comms AS f_close " +
                    "FROM trn_dps AS d " +
                    "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps ";

                 switch (mnTabTypeAux01) {
                    case SDataConstantsSys.TRNX_TP_DPS_DOC:
                        msSql += " AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_INV[2] + " ";
                        break;

                    case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                        msSql += " AND d.fid_ct_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CN[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CN[1] + " AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CN[2] + " ";
                        break;
                    default:
                }

                msSql +=
                    sqlDatePeriod + sqlCompanyBranch + sqlBizPartner + sqlFunctAreas +
                    "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                    "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                    "INNER JOIN erp.bpsu_bp_ct AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_CUS + " " +
                    "INNER JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                    "INNER JOIN erp.bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb " +
                    "INNER JOIN erp.usru_usr AS ua ON d.fid_usr_audit = ua.id_usr " +
                    "LEFT OUTER JOIN erp.bpsu_bp AS a ON d.fid_sal_agt_n = a.id_bp " +
                    "LEFT OUTER JOIN erp.bpsu_bp AS s ON d.fid_sal_sup_n = s.id_bp " +
                    "WHERE d.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                    "ORDER BY dt.code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt, b.bp, bc.bp_key, b.id_bp, bb.bpb, bb.id_bpb) " +
                    ") AS t GROUP BY id_year, id_doc ";
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

    public void publicActionViewDps() {
        actionViewDps();
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        super.actionPerformed(e);

        if (e.getSource() instanceof javax.swing.JButton) {
            JButton button = (javax.swing.JButton) e.getSource();

            if (button == mjbClose) {
                actionCloseDoc();
            }
            else if (button == mjbOpen) {
                actionOpenDoc();
            }
            else if (button == mjbChangeAgentSupervisor) {
                actionChangeAgentSupervisor();
            }
            else if (button == mjbViewDps) {
                actionViewDps();
            }
            else if (button == mjbViewNotes) {
                actionViewNotes();
            }
            else if (button == mjbViewLinks) {
                actionViewLinks();
            }
        }
    }
}
