/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDateCutOff;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mcfg.data.SCfgUtils;
import erp.table.SFilterConstants;
import erp.table.STabFilterCompanyBranchEntity;
import erp.table.STabFilterUsers;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Isabel Servín
 */
public class SViewDiogAuth extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton mjbAuthYes;
    private javax.swing.JButton mjbAuthNo;
    private javax.swing.JButton mjbViewDps;
    private javax.swing.JButton mjbViewNotes;
    private erp.lib.table.STabFilterDateCutOff moTabFilterDateCutOff;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterCompanyBranchEntity moTabFilterCompanyBranchEntity;
    private erp.table.STabFilterUsers moTabFilterUser;

    private boolean mbHasRightAuthor = false;

    /**
     * View to audit documents.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     * @param auxType01 Constants defined in SDataConstants (TRNX_DIOG_AUTH_PEND or TRNX_DIOG_AUTHORIZED).
     */
    public SViewDiogAuth(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01) {
        super(client, tabTitle, SDataConstants.TRN_DIOG, auxType01);
        initComponents();
    }

    private void initComponents() {
        int i;
        int levelDoc = SDataConstantsSys.UNDEFINED;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        mbHasRightAuthor = false;

        mjbAuthYes = new JButton(miClient.getImageIcon(SLibConstants.ICON_APPROVE));
        mjbAuthNo = new JButton(miClient.getImageIcon(SLibConstants.ICON_APPROVE_NO));
        mjbViewDps = new JButton(miClient.getImageIcon(SLibConstants.ICON_LOOK));
        mjbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));

        mjbAuthYes.setPreferredSize(new Dimension(23, 23));
        mjbAuthNo.setPreferredSize(new Dimension(23, 23));
        mjbViewDps.setPreferredSize(new Dimension(23, 23));
        mjbViewNotes.setPreferredSize(new Dimension(23, 23));

        mjbAuthYes.addActionListener(this);
        mjbAuthNo.addActionListener(this);
        mjbViewDps.addActionListener(this);
        mjbViewNotes.addActionListener(this);

        mjbAuthYes.setToolTipText("Marcar como auditado");
        mjbAuthNo.setToolTipText("Desmarcar como auditado");
        mjbViewDps.setToolTipText("Ver documento");
        mjbViewNotes.setToolTipText("Ver notas");

        mbHasRightAuthor = levelDoc == SUtilConsts.LEV_AUTHOR;

        if (isDiogAuthPending()) {
            moTabFilterDateCutOff = new STabFilterDateCutOff(miClient, this, SLibTimeUtilities.getEndOfYear(miClient.getSessionXXX().getWorkingDate()));
            moTabFilterDatePeriod = null;
        }
        else {
            moTabFilterDateCutOff = null;
            moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        }

        moTabFilterCompanyBranchEntity = new STabFilterCompanyBranchEntity(miClient, this, SDataConstantsSys.CFGS_CT_ENT_WH);

        moTabFilterUser = new STabFilterUsers(miClient, this);
        moTabFilterUser.removeButtonUser();
        moTabFilterUser.setUserId(mbHasRightAuthor ? miClient.getSession().getUser().getPkUserId() : SDataConstantsSys.UNDEFINED);


        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        addTaskBarUpperComponent(mjbAuthYes);
        addTaskBarUpperComponent(mjbAuthNo);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(isDiogAuthPending() ? moTabFilterDateCutOff : moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranchEntity);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbViewDps);
        addTaskBarUpperComponent(mjbViewNotes);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterUser);

        mjbAuthYes.setEnabled(isDiogAuthPending());
        mjbAuthNo.setEnabled(!isDiogAuthPending());
        mjbViewDps.setEnabled(true);
        mjbViewNotes.setEnabled(true);

        aoKeyFields = new STableField[2];
        aoTableColumns = new STableColumn[31];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "iog.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "iog.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "iog.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_iog.code", "Código tipo doc.", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_iog.tp_iog", "Tipo doc.", 125);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ent.code", "Almacén", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "iog.val_r", "Valor $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num_cp", "Folio doc. complemento", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_iog_cp.code", "Código tipo doc. complemento", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "tp_iog_cp.tp_iog", "Tipo doc. complemento", 125);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb_cp.code", "Sucursal empresa complemento", STableConstants.WIDTH_CODE_COB);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ent_cp.code", "Almacén complemento", STableConstants.WIDTH_CODE_COB_ENT);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dpst.code", "Tipo doc. CV", STableConstants.WIDTH_CODE_DOC);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_dps_num", "Folio doc. CV", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dps.dt", "Fecha doc. CV", STableConstants.WIDTH_DATE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_mfg", "Ord. prod.", STableConstants.WIDTH_DOC_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_rec", "Folio póliza", STableConstants.WIDTH_RECORD_NUM);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "iog.b_audit", "Auditado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ua.usr", "Usr. auditoría", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_audit", "Auditoría", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "iog.b_authorn", "Autorizado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ut.usr", "Usr. autorización", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_authorn", "Autorización", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "iog.b_sys", "Sistema", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "iog.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "iog.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.TRN_DIOG);
        mvSuscriptors.add(SDataConstants.TRNX_DIOG_AUTH_PEND);
        mvSuscriptors.add(SDataConstants.TRNX_DIOG_AUTHORIZED);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        getTablePane().setDoubleClickAction(this, "publicActionViewDps");

        populateTable();
    }

    private boolean isDiogAuthPending() {
        return mnTabTypeAux01 == SDataConstants.TRNX_DIOG_AUTH_PEND;
    }

    private void actionAuthYes() {
        if (mjbAuthYes.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_DOC_AUDIT_YES) == JOptionPane.YES_OPTION) {
                    Vector<Object> params = new Vector<>();

                    params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[0]);
                    params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1]);
                    params.add(SDataConstantsSys.UPD_DIOG_FL_AUDIT);
                    params.add(1);
                    params.add(miClient.getSession().getUser().getPkUserId());
                    params = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_DIOG_UPD, params, SLibConstants.EXEC_MODE_SILENT);

                    if (params.isEmpty()) {
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_PROCESS);
                    }
                    else {
                        if (SLibUtilities.parseInt((String) params.get(0)) != 0) {
                            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_PROCESS + "\n" + params.get(1) + " (Error: " + params.get(0) + ")");
                        }
                        else {
                            miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(mnTabType);
                        }
                    }
                }
            }
        }
    }

    private void actionAuthNo() {
        if (mjbAuthNo.isEnabled()) {
            if (moTablePane.getSelectedTableRow() != null) {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_DOC_AUDIT_NO) == JOptionPane.YES_OPTION) {
                    Vector<Object> params = new Vector<>();

                    params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[0]);
                    params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1]);
                    params.add(SDataConstantsSys.UPD_DIOG_FL_AUDIT);
                    params.add(0);
                    params.add(miClient.getSession().getUser().getPkUserId());
                    params = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_DIOG_UPD, params, SLibConstants.EXEC_MODE_SILENT);

                    if (params.isEmpty()) {
                        miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_PROCESS);
                    }
                    else {
                        if (SLibUtilities.parseInt((String) params.get(0)) != 0) {
                            miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_DB_REG_PROCESS + "\n" + params.get(1) + " (Error: " + params.get(0) + ")");
                        }
                        else {
                            miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(mnTabType);
                        }
                    }
                }
            }
        }
    }

    private void actionViewDps() {
        int gui = SDataConstants.MOD_INV;

        if (moTablePane.getSelectedTableRow() != null) {
            miClient.getGuiModule(gui).showForm(SDataConstants.TRN_DIOG, moTablePane.getSelectedTableRow().getPrimaryKey());
        }
    }

    private void actionViewNotes() {
        if (mjbViewNotes.isEnabled()) {
            SModuleUtilities.showDocumentNotes(miClient, SDataConstants.TRN_DIOG, moTablePane.getSelectedTableRow());
        }
    }
    
    private String getClParam() {
        String where = "AND iog.fid_ct_iog = 0 AND iog.fid_cl_iog = 0 ";
        try {
            String param = SCfgUtils.getParamValue(miClient.getSession().getStatement(), SDataConstantsSys.CFG_PARAM_TRN_DIOG_CL_AUTHORN);
            if (param.isEmpty()) {
                return where;
            }
            else {
                String aux[] = param.split(";");
                for (String a : aux) {
                    
                }
            }
        } catch (Exception e) { }
        return where;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        int[] key = null;
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                if (isDiogAuthPending()) {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + (setting.getSetting() == null ? "" : "iog.dt <= '" + (new java.sql.Date(((java.util.Date) setting.getSetting()).getTime())) + "' ");
                }
                else {
                    sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "iog.dt");
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB_ENT) {
                key = (int[]) setting.getSetting();
                if (key != null) {
                    if (key[0] != SLibConstants.UNDEFINED) {
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "iog.fid_cob = " + key[0] + " ";
                    }
                    if (key[1] != SLibConstants.UNDEFINED) {
                        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "iog.fid_wh = " + key[1] + " ";
                    }
                }
            }
        }

        sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + " iog.b_authorn = " + !isDiogAuthPending() + " ";
        
        if (isDiogAuthPending()) {
            sqlWhere += getClParam();
        }

        msSql = "SELECT iog.id_year, iog.id_doc, iog.dt, iog.val_r, iog.b_audit, iog.b_authorn, iog.b_sys, iog.b_del, " +
                "CONCAT(iog.num_ser, IF(LENGTH(iog.num_ser) = 0, '', '-'), erp.lib_fix_int(iog.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) AS f_num, " +
                "tp_iog.tp_iog, tp_iog.code, bpb.code, ent.code, " +
                "CONCAT(iog_cp.num_ser, IF(LENGTH(iog_cp.num_ser) = 0, '', '-'), erp.lib_fix_int(iog_cp.num, " + SDataConstantsSys.NUM_LEN_IOG + ")) AS f_num_cp, " +
                "tp_iog_cp.tp_iog, tp_iog_cp.code, bpb_cp.code, ent_cp.code, " +
                "dpst.code, dps.dt, CONCAT(dps.num_ser, IF(length(dps.num_ser) = 0, '', '-'), dps.num) AS f_dps_num, " +
                "CASE WHEN iog.fid_mfg_year_n IS NULL THEN '' ELSE CONCAT(mo.id_year, '-', mo.num) END as f_mfg, " +
                "CONCAT(r.fid_rec_tp_rec, '-', erp.lib_fix_int(r.fid_rec_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) as f_rec, " +
                "iog.ts_audit, iog.ts_authorn, iog.ts_new, iog.ts_edit, iog.ts_del, ut.usr, ua.usr, un.usr, ue.usr, ud.usr " +
                "FROM trn_diog AS iog " +
                "INNER JOIN erp.trns_ct_iog AS ct_iog ON iog.fid_ct_iog = ct_iog.id_ct_iog AND iog.b_del = 0 AND iog.b_sys = 0 " +
                "INNER JOIN erp.trns_cl_iog AS cl_iog ON iog.fid_ct_iog = cl_iog.id_ct_iog AND iog.fid_cl_iog = cl_iog.id_cl_iog " +
                "INNER JOIN erp.trns_tp_iog AS tp_iog ON iog.fid_ct_iog = tp_iog.id_ct_iog AND iog.fid_cl_iog = tp_iog.id_cl_iog AND iog.fid_tp_iog = tp_iog.id_tp_iog " +
                "INNER JOIN erp.bpsu_bpb AS bpb ON iog.fid_cob = bpb.id_bpb " +
                "INNER JOIN erp.cfgu_cob_ent AS ent ON iog.fid_cob = ent.id_cob AND iog.fid_wh = ent.id_ent " +
                "INNER JOIN erp.usru_usr AS ua ON iog.fid_usr_audit = ua.id_usr " +
                "INNER JOIN erp.usru_usr AS ut ON iog.fid_usr_authorn = ut.id_usr " +
                "INNER JOIN erp.usru_usr AS un ON iog.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON iog.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON iog.fid_usr_del = ud.id_usr " +
                "LEFT OUTER JOIN trn_diog AS iog_cp ON iog.fid_diog_year_n = iog_cp.id_year AND iog.fid_diog_doc_n = iog_cp.id_doc " +
                "LEFT OUTER JOIN erp.trns_tp_iog AS tp_iog_cp ON iog_cp.fid_tp_iog = tp_iog_cp.id_tp_iog AND iog_cp.fid_ct_iog = tp_iog_cp.id_ct_iog AND iog_cp.fid_cl_iog = tp_iog_cp.id_cl_iog " +
                "LEFT OUTER JOIN erp.bpsu_bpb AS bpb_cp ON iog_cp.fid_cob = bpb_cp.id_bpb " +
                "LEFT OUTER JOIN erp.cfgu_cob_ent AS ent_cp ON iog_cp.fid_cob = ent_cp.id_cob AND iog_cp.fid_wh = ent_cp.id_ent " +
                "LEFT OUTER JOIN trn_dps AS dps ON iog.fid_dps_year_n = dps.id_year AND iog.fid_dps_doc_n = dps.id_doc " +
                "LEFT OUTER JOIN erp.trnu_tp_dps AS dpst ON dps.fid_ct_dps = dpst.id_ct_dps AND dps.fid_cl_dps = dpst.id_cl_dps AND dps.fid_tp_dps = dpst.id_tp_dps " +
                "LEFT OUTER JOIN mfg_ord AS mo ON iog.fid_mfg_year_n = mo.id_year AND iog.fid_mfg_ord_n = mo.id_ord " +
                "LEFT OUTER JOIN trn_diog_rec AS r ON iog.id_year = r.id_iog_year AND iog.id_doc = r.id_iog_doc " +
                (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere) +
                "ORDER BY iog.dt, f_num, iog.id_year, iog.id_doc ";
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

            if (button == mjbAuthYes) {
                actionAuthYes();
            }
            else if (button == mjbAuthNo) {
                actionAuthNo();
            }
            else if (button == mjbViewDps) {
                actionViewDps();
            }
            else if (button == mjbViewNotes) {
                actionViewNotes();
            }
        }
    }
}
