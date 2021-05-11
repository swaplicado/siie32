/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.gui.SModuleUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STabFilterDeleted;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mod.SModConsts;
import erp.table.SFilterConstants;
import erp.table.STabFilterFunctionalArea;
import java.awt.Dimension;
import javax.swing.JButton;
import sa.gui.util.SUtilConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewDpsEntryReference extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton jbViewNotes;
    private javax.swing.JButton jbViewLinks;
    private erp.lib.table.STabFilterDeleted moTabFilterDeleted;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;

    private boolean mbIsCategoryPur = false;
    private boolean mbIsCategorySal = false;
    private boolean mbIsEstEst = false;
    private boolean mbIsEstCon = false;
    private boolean mbIsOrd = false;
    private boolean mbIsDoc = false;
    private boolean mbIsDocAdj = false;

    /**
     * View to audit documents.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     * @param auxType01 Constants defined in SDataConstantsSys (TRNS_CT_DPS...).
     * @param auxType02 Constants defined in SDataConstantsSys (TRNX_TP_DPS...).
     */
    public SViewDpsEntryReference(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_ETY_REF, auxType01, auxType02);
        initComponents();
    }

    private void initComponents() {
        int i = 0;
        int levelDoc = SDataConstantsSys.UNDEFINED;

        mbIsCategoryPur = mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR;
        mbIsCategorySal = mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL;
        mbIsEstEst = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_EST;
        mbIsEstCon = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_CON;
        mbIsOrd = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ORD;
        mbIsDoc = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_DOC;
        mbIsDocAdj = mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_ADJ;

        if (mbIsCategoryPur) {
            if (mbIsEstEst || mbIsEstCon) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_EST).Level;
            }
            else if (mbIsOrd) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD).Level;
            }
            else if (mbIsDoc) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN).Level;
            }
            else if (mbIsDocAdj) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN_ADJ).Level;
            }
        }
        else {
            if (mbIsEstEst || mbIsEstCon) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_EST).Level;
            }
            else if (mbIsOrd) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD).Level;
            }
            else if (mbIsDoc) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN).Level;
            }
            else if (mbIsDocAdj) {
                levelDoc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN_ADJ).Level;
            }
        }

        jbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        jbViewNotes.setPreferredSize(new Dimension(23, 23));
        jbViewNotes.addActionListener(this);
        jbViewNotes.setToolTipText("Ver notas del documento");

        jbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));
        jbViewLinks.setPreferredSize(new Dimension(23, 23));
        jbViewLinks.addActionListener(this);
        jbViewLinks.setToolTipText("Ver vínculos del documento");

        moTabFilterDeleted = new STabFilterDeleted(this);
        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, mbIsEstCon ? SLibConstants.GUI_DATE_AS_YEAR : SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this, SModConsts.CFGU_FUNC, new int[] { miClient.getSession().getUser().getPkUserId() });

        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDeleted);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterFunctionalArea);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbViewNotes);
        addTaskBarUpperComponent(jbViewLinks);

        jbNew.setEnabled(false);
        jbEdit.setEnabled(true);
        jbDelete.setEnabled(false);
        jbViewNotes.setEnabled(true);
        jbViewLinks.setEnabled(true);

        STableField[] aoKeyFields = new STableField[2];
        STableColumn[] aoTableColumns = new STableColumn[27];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "d.id_doc");
        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (getDpsSortingType() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC) {
            if (mbIsCategoryPur) {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                }
            }
            else {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                }
            }

            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_ico", "Estatus", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cob.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dt.code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "d.num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "d.dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cob.code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
            aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_INTEGER, "_ico", "Estatus", STableConstants.WIDTH_ICON);
            aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererIcon());

            if (mbIsCategoryPur) {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Proveedor", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave proveedor", 50);
                }
            }
            else {
                if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                }
                else {
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp.bp", "Cliente", 200);
                    aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpc.bp_key", "Clave cliente", 50);
                }
            }
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "d.tot_cur_r", "Total mon. $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);

        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 200);
        }
        else {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item", "Ítem", 200);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "i.item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
        }
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.orig_qty", "Cantidad", STableConstants.WIDTH_QUANTITY);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "uo.symbol", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "de.ref", "Referencia part.", 100);

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.stot_cur_r", "Subtotal part. mon. $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.tax_charged_cur_r", "Imp tras part. mon. $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.tax_retained_cur_r", "Imp ret part. mon. $", STableConstants.WIDTH_VALUE);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "de.tot_cur_r", "Total part. mon. $", STableConstants.WIDTH_VALUE_2X);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "c.cur_key", "Moneda", STableConstants.WIDTH_CURRENCY_KEY);
        
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "d.b_del", "Eliminado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "un.usr", "Usr. creación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_new", "Creación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ue.usr", "Usr. modificación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_edit", "Modificación", STableConstants.WIDTH_DATE_TIME);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ud.usr", "Usr. eliminación", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "d.ts_del", "Eliminación", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        populateTable();
    }

    private int getDpsSortingType() {
        int type = SLibConstants.UNDEFINED;

        if (mbIsCategoryPur && (mbIsDoc || mbIsDocAdj)) {
            type = miClient.getSessionXXX().getParamsErp().getFkSortingDpsSupplierTypeId();
        }
        else {
            type = miClient.getSessionXXX().getParamsErp().getFkSortingDpsCustomerTypeId();
        }

        return type;
    }

    private int[] getDpsTypeKey() {
        int[] key = null;

        switch (mnTabTypeAux02) {
            case SDataConstantsSys.TRNX_TP_DPS_EST_EST:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_EST : SDataConstantsSys.TRNU_TP_DPS_SAL_EST;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_EST_CON:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_CON : SDataConstantsSys.TRNU_TP_DPS_SAL_CON;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_ORD:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_ORD : SDataConstantsSys.TRNU_TP_DPS_SAL_ORD;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_DOC:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_INV : SDataConstantsSys.TRNU_TP_DPS_SAL_INV;
                break;
            case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                key = mbIsCategoryPur ? SDataConstantsSys.TRNU_TP_DPS_PUR_CN : SDataConstantsSys.TRNU_TP_DPS_SAL_CN;
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }

        return key;
    }

    @Override
    public void actionNew() {
        
    }
    
    @Override
    public void actionEdit() {
       if (jbEdit.isEnabled()) {
           if (moTablePane.getSelectedTableRow() != null) {
                int gui = mbIsCategoryPur ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL; // GUI module

                miClient.getGuiModule(gui).setFormComplement(getDpsTypeKey()); // document type key
                miClient.getGuiModule(gui).setCurrentUserPrivilegeLevel(SUtilConsts.LEV_READ);
                miClient.getGuiModule(gui).showForm(SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey());
            }
        }
    }

    @Override
    public void actionDelete() {
        
    }

    private void actionViewNotes() {
        if (jbViewNotes.isEnabled()) {
            SModuleUtilities.showDocumentNotes(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow());
        }
    }

    private void actionViewLinks() {
        if (jbViewLinks.isEnabled()) {
            SModuleUtilities.showDocumentLinks(miClient, moTablePane.getSelectedTableRow());
        }
    }

    @Override
    public void createSqlQuery() {
        String sqlWhere = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_DELETED && setting.getStatus() == STableConstants.STATUS_ON) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + "d.b_del = 0 AND de.b_del = 0 ";
            }
            else if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                sqlWhere += (sqlWhere.length() == 0 ? "" : "AND ") + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (! ((String) setting.getSetting()).isEmpty()) {
                    sqlWhere += (sqlWhere.isEmpty() ? "" : "AND ") + " d.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                }
            }
        }

        msSql = "SELECT d.id_year, d.id_doc, d.dt, d.num_ref, d.tot_cur_r, d.b_del, d.ts_new, d.ts_edit, d.ts_del, " +
                "dt.code, cob.code, c.cur_key, '" + miClient.getSession().getSessionCustom().getLocalCurrencyCode() + "' AS f_cur_key_loc, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS _num, " +
                "IF(d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_ANNULED + ", " + STableConstants.ICON_ST_ANNUL + ", " + STableConstants.ICON_NULL + ") AS _ico, " +
                "bp.id_bp, bp.bp, bpc.bp_key, un.usr, ue.usr, ud.usr, " +
                "de.ref, de.stot_cur_r, de.tax_charged_cur_r, de.tax_retained_cur_r, de.tot_cur_r, de.orig_qty, i.item_key, i.item, uo.symbol " +
                "FROM trn_dps AS d " +
                "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND de.ref <> '' " +
                "INNER JOIN erp.bpsu_bpb AS cob ON d.fid_cob = cob.id_bpb " +
                "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN erp.bpsu_bp AS bp ON d.fid_bp_r = bp.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bpc ON bp.id_bp = bpc.id_bp AND bpc.id_ct_bp = " + (mbIsCategoryPur ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS uo ON de.fid_orig_unit = uo.id_unit " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND ";

        switch (mnTabTypeAux02) {
            case SDataConstantsSys.TRNX_TP_DPS_EST_EST:
            case SDataConstantsSys.TRNX_TP_DPS_EST_CON:
                msSql += (mbIsCategoryPur ?
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_EST[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_EST[1] + " " :
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_EST[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_EST[1] + " ");

                if (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_EST) {
                    msSql += (mbIsCategoryPur ?
                            "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_EST[2] + " " :
                            "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_EST[2] + " ");
                }
                else if (mnTabTypeAux02 == SDataConstantsSys.TRNX_TP_DPS_EST_CON) {
                    msSql += (mbIsCategoryPur ?
                            "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_PUR_CON[2] + " " :
                            "AND d.fid_tp_dps = " + SDataConstantsSys.TRNU_TP_DPS_SAL_CON[2] + " ");
                }
                break;

            case SDataConstantsSys.TRNX_TP_DPS_ORD:
                msSql += (mbIsCategoryPur ?
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ORD[1] + " " :
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ORD[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ORD[1] + " ");
                break;

            case SDataConstantsSys.TRNX_TP_DPS_DOC:
                msSql += (mbIsCategoryPur ?
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] + " " :
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] + " ");
                break;

            case SDataConstantsSys.TRNX_TP_DPS_ADJ:
                msSql += (mbIsCategoryPur ?
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1] + " " :
                        "d.fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[0] + " AND d.fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1] + " ");
                break;
            default:
        }

        msSql +=
                "INNER JOIN erp.usru_usr AS un ON d.fid_usr_new = un.id_usr " +
                "INNER JOIN erp.usru_usr AS ue ON d.fid_usr_edit = ue.id_usr " +
                "INNER JOIN erp.usru_usr AS ud ON d.fid_usr_del = ud.id_usr ";

        msSql += (sqlWhere.length() == 0 ? "" : "WHERE " + sqlWhere);

        if (getDpsSortingType() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC) {
            msSql += "ORDER BY ";

            if ((mbIsCategoryPur && miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) ||
                (mbIsCategorySal && miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME)) {
                msSql += "bpc.bp_key, bp.bp, ";
            }
            else {
                msSql += "bp.bp, bpc.bp_key, ";
            }

            msSql += "bp.id_bp, dt.code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt, ";
        }
        else {
            msSql += "ORDER BY dt.code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt, ";

            if ((mbIsCategoryPur && miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) ||
                (mbIsCategorySal && miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME)) {
                msSql += "bpc.bp_key, bp.bp, ";
            }
            else {
                msSql += "bp.bp, bpc.bp_key, ";
            }

            msSql += "bp.id_bp, ";
        }
        
        if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
            msSql += "i.item_key, i.item, ";
        }
        else {
            msSql += "i.item, i.item_key, ";
        }

        msSql += "de.fid_item, uo.symbol, de.fid_orig_unit ";
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        super.actionPerformed(evt);

        try {
            if (evt.getSource() instanceof javax.swing.JButton) {
                JButton button = (JButton) evt.getSource();

                if (button == jbViewNotes) {
                    actionViewNotes();
                }
                else if (button == jbViewLinks) {
                    actionViewLinks();
                }
            }
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
    }
}
