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
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDateCutOff;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.STrnDiogComplement;
import erp.table.SFilterConstants;
import erp.table.STabFilterBizPartner;
import erp.table.STabFilterCompanyBranch;
import erp.table.STabFilterDocumentType;
import java.awt.Dimension;
import java.util.Date;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibRpnArgument;
import sa.lib.SLibRpnArgumentType;
import sa.lib.SLibRpnOperator;

/**
 *
 * @author Sergio Flores
 */
public class SViewDpsStockReturn extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private javax.swing.JButton mjbReturn;
    private javax.swing.JButton mjbClose;
    private javax.swing.JButton mjbOpen;
    private javax.swing.JButton mjbViewDps;
    private javax.swing.JButton mjbViewNotes;
    private javax.swing.JButton mjbViewLinks;
    private erp.lib.table.STabFilterDateCutOff moTabFilterDateCutOff;
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterCompanyBranch moTabFilterCompanyBranch;
    private erp.table.STabFilterDocumentType moTabFilterDocumentType;
    private erp.table.STabFilterBizPartner moTabFilterBizPartner;

    /**
     * @param client Client interface.
     * @param tabTitle View tab title.
     * @param tabType View tab type. Constants defined in SDataConstats (TRNX_DPS_RETURN_PEND, TRNX_DPS_RETURN_PEND_ETY, TRNX_DPS_RETURNED, TRNX_DPS_RETURNED_ETY).
     * @param auxType01 DPS category. Constats defined in SDataConstatsSys (TRNS_CL_DPS_...[0]).
     * @param auxType02 DPS class. Constats defined in SDataConstatsSys (TRNS_CT_DPS_...[1]).
     */
    public SViewDpsStockReturn(erp.client.SClientInterface client, java.lang.String tabTitle, int tabType, int auxType01, int auxType02) {
        super(client, tabTitle, tabType, auxType01, auxType02);
        initComponents();
    }

    private boolean isViewForDocuments() {
        return mnTabType == SDataConstants.TRNX_DPS_RETURN_PEND || mnTabType == SDataConstants.TRNX_DPS_RETURNED;
    }

    private boolean isViewForDocumentEntries() {
        return mnTabType == SDataConstants.TRNX_DPS_RETURN_PEND_ETY || mnTabType == SDataConstants.TRNX_DPS_RETURNED_ETY;
    }

    private boolean isViewForPurchases() {
        return mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR;
    }

    private boolean isViewForReturn() {
        return mnTabType == SDataConstants.TRNX_DPS_RETURN_PEND || mnTabType == SDataConstants.TRNX_DPS_RETURN_PEND_ETY;
    }

    private void initComponents() {
        int i = 0;
        int cols = 0;
        int levelRightAllDocs = SDataConstantsSys.UNDEFINED;
        int levelRightDocTransaction = SDataConstantsSys.UNDEFINED;
        STableField[] aoKeyFields = null;
        STableColumn[] aoTableColumns = null;

        mjbReturn = new JButton(isViewForPurchases() ?
            new ImageIcon(getClass().getResource("/erp/img/icon_std_dps_stk_out.gif")) :
            new ImageIcon(getClass().getResource("/erp/img/icon_std_dps_stk_in.gif")));
        mjbClose = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_CLOSE));
        mjbOpen = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_OPEN));
        mjbViewDps = new JButton(miClient.getImageIcon(SLibConstants.ICON_LOOK));
        mjbViewNotes = new JButton(miClient.getImageIcon(SLibConstants.ICON_NOTES));
        mjbViewLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_LINK));

        mjbReturn.setPreferredSize(new Dimension(23, 23));
        mjbClose.setPreferredSize(new Dimension(23, 23));
        mjbOpen.setPreferredSize(new Dimension(23, 23));
        mjbViewDps.setPreferredSize(new Dimension(23, 23));
        mjbViewNotes.setPreferredSize(new Dimension(23, 23));
        mjbViewLinks.setPreferredSize(new Dimension(23, 23));

        mjbReturn.addActionListener(this);
        mjbClose.addActionListener(this);
        mjbOpen.addActionListener(this);
        mjbViewDps.addActionListener(this);
        mjbViewNotes.addActionListener(this);
        mjbViewLinks.addActionListener(this);

        mjbReturn.setToolTipText("Devolver");
        mjbClose.setToolTipText("Cerrar para devolución");
        mjbOpen.setToolTipText("Abrir para devolución");
        mjbViewDps.setToolTipText("Ver documento");
        mjbViewNotes.setToolTipText("Ver notas");
        mjbViewLinks.setToolTipText("Ver vínculos del documento");

        if (isViewForReturn()) {
            moTabFilterDateCutOff = new STabFilterDateCutOff(miClient, this, SLibTimeUtilities.getEndOfYear(miClient.getSessionXXX().getWorkingDate()));
            moTabFilterDatePeriod = null;
        }
        else {
            moTabFilterDateCutOff = null;
            moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        }

        moTabFilterCompanyBranch = new STabFilterCompanyBranch(miClient, this);
        moTabFilterDocumentType = new STabFilterDocumentType(miClient, this, SDataConstants.TRNU_TP_DPS, new int[] { mnTabTypeAux01, mnTabTypeAux02 });
        moTabFilterBizPartner = new STabFilterBizPartner(miClient, this, isViewForPurchases() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS);

        if (isViewForPurchases()) {
            levelRightAllDocs = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_TRN).Level;
            levelRightDocTransaction = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_OUT_PUR).Level;
        }
        else {
            levelRightAllDocs = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_TRN).Level;
            levelRightDocTransaction = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_INV_IN_SAL).Level;
        }

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);

        addTaskBarUpperComponent(mjbReturn);
        addTaskBarUpperComponent(mjbClose);
        addTaskBarUpperComponent(mjbOpen);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(isViewForReturn() ? moTabFilterDateCutOff : moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterCompanyBranch);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterDocumentType);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterBizPartner);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(mjbViewDps);
        addTaskBarUpperComponent(mjbViewNotes);
        addTaskBarUpperComponent(mjbViewLinks);

        mjbReturn.setEnabled(isViewForReturn() && levelRightDocTransaction >= SUtilConsts.LEV_AUTHOR);
        mjbClose.setEnabled(isViewForReturn() && levelRightDocTransaction >= SUtilConsts.LEV_AUTHOR);
        mjbOpen.setEnabled(!isViewForReturn() && levelRightDocTransaction >= SUtilConsts.LEV_AUTHOR);
        mjbViewDps.setEnabled(levelRightAllDocs == SUtilConsts.LEV_MANAGER);
        mjbViewNotes.setEnabled(true);
        mjbViewLinks.setEnabled(true);

        // View primary keys:

        aoKeyFields = new STableField[isViewForDocuments() ? 2 : 3];

        i = 0;
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_year");
        aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_doc");

        if (isViewForDocumentEntries()) {
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "id_ety");
        }

        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        // View columns:

        cols = isViewForDocuments() ? 14 : 17;
        if (levelRightAllDocs == SUtilConsts.LEV_MANAGER) {
            cols += 2;
        }

        aoTableColumns = new STableColumn[cols];

        i = 0;
        if (isViewForPurchases()) {
            // Purchases & suppliers:

            if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave proveedor", 50);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Proveedor", 200);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Proveedor", 200);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave proveedor", 50);
            }

            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb", "Sucursal proveedor", 75);

            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_dt_code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cb_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);
        }
        else {
            // Sales & customers:

            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_dt_code", "Tipo doc.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num", "Folio doc.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "num_ref", "Referencia doc.", STableConstants.WIDTH_DOC_NUM_REF);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dt", "Fecha doc.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_cb_code", "Sucursal empresa", STableConstants.WIDTH_CODE_COB);

            if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave cliente", 50);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Cliente", 200);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp", "Cliente", 200);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bp_key", "Clave cliente", 50);
            }

            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bpb", "Sucursal cliente", 75);
        }

        if (levelRightAllDocs == SUtilConsts.LEV_MANAGER) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "tot_cur_r", "Total mon $", STableConstants.WIDTH_VALUE_2X);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "cur_key", "Moneda doc.", STableConstants.WIDTH_CURRENCY_KEY);
        }

        if (isViewForDocumentEntries()) {
            if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", STableConstants.WIDTH_ITEM_2X);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item", "Ítem", STableConstants.WIDTH_ITEM_2X);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "item_key", "Clave ítem", STableConstants.WIDTH_ITEM_KEY);
            }
        }

        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_orig_qty", "Cant. original", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "f_ret_orig_qty", "Cant. devuelta", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i++].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i] = new STableColumn(SLibConstants.DATA_TYPE_DOUBLE, "", "Cant. pendiente", STableConstants.WIDTH_QUANTITY_2X);
        aoTableColumns[i].setCellRenderer(miClient.getSessionXXX().getFormatters().getTableCellRendererQuantity());
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_orig_qty", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i].getRpnArguments().add(new SLibRpnArgument("f_ret_orig_qty", SLibRpnArgumentType.OPERAND));
        aoTableColumns[i++].getRpnArguments().add(new SLibRpnArgument(SLibRpnOperator.SUBTRACTION, SLibRpnArgumentType.OPERATOR));
        if (isViewForDocumentEntries()) {
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_orig_unit", "Unidad", STableConstants.WIDTH_UNIT_SYMBOL);
        }

        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_BOOLEAN, "b_close", "Cerrado", STableConstants.WIDTH_BOOLEAN);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "usr", "Usr. cierre", STableConstants.WIDTH_USER);
        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE_TIME, "ts_close", "Cierre", STableConstants.WIDTH_DATE_TIME);

        for (i = 0; i < aoTableColumns.length; i++) {
            moTablePane.addTableColumn(aoTableColumns[i]);
        }

        mvSuscriptors.add(mnTabType);
        mvSuscriptors.add(SDataConstants.BPSU_BP);
        mvSuscriptors.add(SDataConstants.BPSU_BP_CT);
        mvSuscriptors.add(SDataConstants.BPSU_BPB);
        mvSuscriptors.add(SDataConstants.TRN_DPS);
        mvSuscriptors.add(SDataConstants.TRN_DIOG);
        mvSuscriptors.add(SDataConstants.USRU_USR);

        if (isViewForDocumentEntries()) {
            mvSuscriptors.add(SDataConstants.ITMU_ITEM);
            mvSuscriptors.add(SDataConstants.ITMU_UNIT);
        }

        moTablePane.setDoubleClickAction(this, "publicActionViewDps");

        populateTable();
    }

    private void actionReturnDoc() {
        if (mjbReturn.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int[] key = isViewForPurchases() ? SDataConstantsSys.TRNS_TP_IOG_OUT_PUR_PUR : SDataConstantsSys.TRNS_TP_IOG_IN_SAL_SAL;
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

                miClient.getGuiModule(SDataConstants.MOD_INV).setFormComplement(new STrnDiogComplement(key, dps));
                if (miClient.getGuiModule(SDataConstants.MOD_INV).showForm(SDataConstants.TRN_DIOG, null) == SLibConstants.DB_ACTION_SAVE_OK) {
                    miClient.getGuiModule(SDataConstants.MOD_INV).refreshCatalogues(SDataConstants.TRN_DIOG);
                }
            }
        }
    }

    private void actionCloseDoc() {
        if (mjbClose.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_DOC_CLOSE) == JOptionPane.YES_OPTION) {
                    Vector<Object> params = new Vector<Object>();

                    params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[0]);
                    params.add(((int[])moTablePane.getSelectedTableRow().getPrimaryKey())[1]);
                    params.add(miClient.getSession().getUser().getPkUserId());
                    params.add(true);   // true means "open"
                    params = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_OPEN_CLOSE_DPS, params, SLibConstants.EXEC_MODE_SILENT);
                    miClient.getGuiModule(isViewForPurchases() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_DPS);
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

                if (!oDps.getIsClosed()) {
                    miClient.showMsgBoxInformation("No se puede abrir un documento que no ha sido cerrado de forma manual.");
                }
                else {
                    if (miClient.showMsgBoxConfirm(SLibConstants.MSG_CNF_DOC_OPEN) == JOptionPane.YES_OPTION) {
                        Vector<Object> params = new Vector<Object>();

                        params.add(((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[0]);
                        params.add(((int[]) moTablePane.getSelectedTableRow().getPrimaryKey())[1]);
                        params.add(miClient.getSession().getUser().getPkUserId());
                        params.add(false);  // false means "close"
                        params = SDataUtilities.callProcedure(miClient, SProcConstants.TRN_OPEN_CLOSE_DPS, params, SLibConstants.EXEC_MODE_SILENT);
                        miClient.getGuiModule(isViewForPurchases() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL).refreshCatalogues(SDataConstants.TRN_DPS);
                    }
                }
            }
        }
    }

    private void actionViewDps() {
        if (mjbViewDps.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                int gui = isViewForPurchases() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;
                SDataDps dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, moTablePane.getSelectedTableRow().getPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);

                if (dps != null) {
                    miClient.getGuiModule(gui).setFormComplement(dps.getDpsTypeKey());
                    miClient.getGuiModule(gui).showForm(SDataConstants.TRNX_DPS_RO, dps.getPrimaryKey());
                }
            }
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

    public void publicActionViewDps() {
        actionViewDps();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void createSqlQuery() {
        int[] key = null;
        String sqlFilter = "";
        String sqlDiogPeriod = "";
        String sqlBizPartner = "";
        String sqlOrderByDoc = "";
        String sqlOrderByDocEty = "";

        for (STableSetting setting : mvTableSettings) {
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                if (isViewForReturn()) {
                    sqlDiogPeriod += setting.getSetting() == null ? "" : "g.dt <= '" + miClient.getSessionXXX().getFormatters().getDbmsDateFormat().format((Date) setting.getSetting()) + "' AND ";
                }
                else {
                    sqlFilter += "AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "d.dt");
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_DOC_TP) {
                key = (int[]) setting.getSetting();
                sqlFilter += key == null ? "" : "AND d.fid_ct_dps = " + key[0] + " AND d.fid_cl_dps = " + key[1] + " AND d.fid_tp_dps = " + key[2] + " ";
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_COB) {
                sqlFilter += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "AND d.fid_cob = " + (Integer) setting.getSetting() + " ");
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_BP) {
                sqlFilter += ((Integer) setting.getSetting() == SLibConstants.UNDEFINED ? "" : "AND d.fid_bp_r = " + (Integer) setting.getSetting() + " ");
            }
        }

        if (isViewForPurchases()) {
            // Purchases & suppliers:

            sqlBizPartner = "AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_SUP + " ";

            if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                sqlOrderByDoc += "bp_key, bp, id_bp, ";
                sqlOrderByDocEty += "bp_key, bp, id_bp, ";
            }
            else {
                sqlOrderByDoc += "bp, id_bp, bp_key, ";
                sqlOrderByDocEty += "bp, id_bp, bp_key, ";
            }

            sqlOrderByDoc += "f_dt_code, num_ser, CAST(num AS UNSIGNED INTEGER), num, dt ";
            sqlOrderByDocEty += "f_dt_code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt ";
        }
        else {
            // Sales & customers:

            sqlBizPartner = "AND bc.id_ct_bp = " + SDataConstantsSys.BPSS_CT_BP_CUS + " ";

            sqlOrderByDoc += "f_dt_code, num_ser, CAST(num AS UNSIGNED INTEGER), num, dt, ";
            sqlOrderByDocEty += "f_dt_code, d.num_ser, CAST(d.num AS UNSIGNED INTEGER), d.num, d.dt, ";

            if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                sqlOrderByDoc += "bp_key, bp, id_bp ";
                sqlOrderByDocEty += "bp_key, bp, id_bp ";
            }
            else {
                sqlOrderByDoc += "bp, id_bp, bp_key ";
                sqlOrderByDocEty += "bp, id_bp, bp_key ";
            }
        }

        if (isViewForDocumentEntries()) {
            if (miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                sqlOrderByDocEty += ", item_key, item, ";
            }
            else {
                sqlOrderByDocEty += ", item, item_key, ";
            }

            sqlOrderByDocEty += "fid_item, f_orig_unit, fid_orig_unit, f_orig_qty ";
        }

        if (isViewForDocumentEntries()) {
            msSql = "";
        }
        else {
            msSql = "SELECT id_year, id_doc, " +
                "dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_close, ts_close, usr, cur_key, f_num, " +
                "f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb, " +
                "SUM(f_qty) AS f_qty, " +
                "SUM(f_orig_qty) AS f_orig_qty, " +
                "COALESCE(SUM(f_ret_qty), 0) AS f_ret_qty, " +
                "COALESCE(SUM(f_ret_orig_qty), 0) AS f_ret_orig_qty " +
                "FROM ( ";
        }

        msSql += "SELECT de.id_year, de.id_doc, de.id_ety, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, " +
                "CONCAT(d.num_ser, IF(length(d.num_ser) = 0, '', '-'), d.num) AS f_num, " +
                "dt.code AS f_dt_code, cb.code AS f_cb_code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol AS f_unit, uo.symbol AS f_orig_unit, " +
                "de.qty AS f_qty, " +
                "de.orig_qty AS f_orig_qty, " +
                "COALESCE((SELECT SUM(ge.qty) FROM trn_diog_ety AS ge, trn_diog AS g WHERE " +
                "ge.fid_dps_adj_year_n = de.id_year AND ge.fid_dps_adj_doc_n = de.id_doc AND ge.fid_dps_adj_ety_n = de.id_ety AND " +
                "ge.id_year = g.id_year AND ge.id_doc = g.id_doc AND " + sqlDiogPeriod +
                "ge.b_del = 0 AND g.b_del = 0), 0) AS f_ret_qty, " +
                "COALESCE((SELECT SUM(ge.orig_qty) FROM trn_diog_ety AS ge, trn_diog AS g WHERE " +
                "ge.fid_dps_adj_year_n = de.id_year AND ge.fid_dps_adj_doc_n = de.id_doc AND ge.fid_dps_adj_ety_n = de.id_ety AND " +
                "ge.id_year = g.id_year AND ge.id_doc = g.id_doc AND " + sqlDiogPeriod +
                "ge.b_del = 0 AND g.b_del = 0), 0) AS f_ret_orig_qty " +
                "FROM trn_dps AS d " +
                "INNER JOIN erp.trnu_tp_dps AS dt ON d.fid_ct_dps = dt.id_ct_dps AND d.fid_cl_dps = dt.id_cl_dps AND d.fid_tp_dps = dt.id_tp_dps AND " +
                "d.b_del = 0 AND d.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " AND " +
                "d.fid_ct_dps = " + mnTabTypeAux01 + " AND d.fid_cl_dps = " + mnTabTypeAux02 + " " + sqlFilter +
                "INNER JOIN erp.cfgu_cur AS c ON d.fid_cur = c.id_cur " +
                "INNER JOIN erp.bpsu_bpb AS cb ON d.fid_cob = cb.id_bpb " +
                "INNER JOIN erp.bpsu_bp AS b ON d.fid_bp_r = b.id_bp " +
                "INNER JOIN erp.bpsu_bp_ct AS bc ON d.fid_bp_r = bc.id_bp " + sqlBizPartner +
                "INNER JOIN erp.bpsu_bpb AS bb ON d.fid_bpb = bb.id_bpb " +
                "INNER JOIN erp.usru_usr AS uc ON d.fid_usr_close = uc.id_usr " +
                "INNER JOIN trn_dps_ety AS de ON d.id_year = de.id_year AND d.id_doc = de.id_doc AND " +
                "de.b_del = 0 AND de.qty > 0 AND de.b_inv = 1 AND de.orig_qty > 0 AND de.fid_tp_dps_adj = " + SDataConstantsSys.TRNS_TP_DPS_ADJ_RET + " " +
                "INNER JOIN erp.itmu_item AS i ON de.fid_item = i.id_item " +
                "INNER JOIN erp.itmu_unit AS u ON de.fid_unit = u.id_unit " +
                "INNER JOIN erp.itmu_unit AS uo ON de.fid_orig_unit = uo.id_unit " +
                "GROUP BY de.id_year, de.id_doc, de.id_ety, " +
                "d.dt, d.num_ser, d.num, d.num_ref, d.tot_r, d.tot_cur_r, d.b_close, d.ts_close, uc.usr, c.cur_key, " +
                "dt.code, cb.code, b.id_bp, b.bp, bc.bp_key, bb.bpb, " +
                "de.fid_item, de.fid_unit, de.fid_orig_unit, i.item_key, i.item, u.symbol, uo.symbol, " +
                "de.qty, de.orig_qty ";

        if (mnTabType == SDataConstants.TRNX_DPS_RETURN_PEND || mnTabType == SDataConstants.TRNX_DPS_RETURN_PEND_ETY) {
            msSql += "HAVING (f_orig_qty - f_ret_orig_qty) <> 0 AND d.b_close = 0 ";
        }
        else {
            msSql += "HAVING (f_orig_qty - f_ret_orig_qty) = 0 OR d.b_close = 1 ";
        }

        if (isViewForDocumentEntries()) {
            msSql += "ORDER BY " + sqlOrderByDocEty + "; ";
        }
        else {
            msSql += ") AS DPS_ETY_TMP " +  // derived table
                    "GROUP BY id_year, id_doc, " +
                    "dt, num_ser, num, num_ref, tot_r, tot_cur_r, b_close, ts_close, usr, cur_key, f_num, " +
                    "f_dt_code, f_cb_code, id_bp, bp, bp_key, bpb " +
                    "ORDER BY " + sqlOrderByDoc + "; ";
        }
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

            if (button == mjbReturn) {
                actionReturnDoc();
            }
            else if (button == mjbClose) {
                actionCloseDoc();
            }
            else if (button == mjbOpen) {
                actionOpenDoc();
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
