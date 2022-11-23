/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mtrn.view;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.data.SDataSqlUtilities;
import erp.lib.table.STabFilterDatePeriod;
import erp.lib.table.STableColumn;
import erp.lib.table.STableConstants;
import erp.lib.table.STableField;
import erp.lib.table.STableSetting;
import erp.mtrn.data.SDataDps;
import erp.mtrn.data.SDataDpsEntry;
import erp.mtrn.data.SDataUserDnsDps;
import erp.mtrn.data.STrnUtilities;
import erp.table.SFilterConstants;
import erp.table.STabFilterDnsDps;
import erp.table.STabFilterFunctionalArea;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores, Uriel Castañeda, Sergio Flores, Isabel Servín
 */
public class SViewDpsLinksQuery extends erp.lib.table.STableTab implements java.awt.event.ActionListener {

    private boolean mbIsOrd;
    
    private erp.lib.table.STabFilterDatePeriod moTabFilterDatePeriod;
    private erp.table.STabFilterFunctionalArea moTabFilterFunctionalArea;
    private erp.table.STabFilterDnsDps moTabFilterDnsDps;

    private javax.swing.JButton jbDeleteLinks;

    /**
     * View to consult document links to other documents.
     * @param client GUI client interface.
     * @param tabTitle View tab title.
     * @param auxType01 Constants defined in SDataConstantsSys (TRNS_CT_DPS...).
     * @param auxType02 Constants defined in SDataConstantsSys (TRNX_TP_DPS...).
     */
    public SViewDpsLinksQuery(erp.client.SClientInterface client, java.lang.String tabTitle, int auxType01, int auxType02) {
        super(client, tabTitle, SDataConstants.TRNX_DPS_LINKS, auxType01, auxType02);
        
        mbIsOrd = SLibUtils.belongsTo(mnTabTypeAux02, new int[] {
            SDataConstantsSys.TRNX_LINK_ORD_SRC,
            SDataConstantsSys.TRNX_LINK_ORD_DES });
        
        initComponents();
    }

    private void initComponents() {
        int i;

        moTabFilterDatePeriod = new STabFilterDatePeriod(miClient, this, SLibConstants.GUI_DATE_AS_YEAR_MONTH);
        moTabFilterFunctionalArea = new STabFilterFunctionalArea(miClient, this);
        moTabFilterDnsDps = new STabFilterDnsDps(miClient, this);

        jbDeleteLinks = new JButton(miClient.getImageIcon(SLibConstants.ICON_DOC_LINK_NO));
        jbDeleteLinks.setPreferredSize(new Dimension(23, 23));
        jbDeleteLinks.addActionListener(this);
        jbDeleteLinks.setToolTipText("Eliminar vínculo");

        removeTaskBarUpperComponent(jbNew);
        removeTaskBarUpperComponent(jbEdit);
        removeTaskBarUpperComponent(jbDelete);
        
        addTaskBarUpperComponent(moTabFilterDatePeriod);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(jbDeleteLinks);
        addTaskBarUpperSeparator();
        addTaskBarUpperComponent(moTabFilterFunctionalArea);
        if (mbIsOrd) {
            addTaskBarUpperComponent(moTabFilterDnsDps);
        }

        STableField[] aoKeyFields = new STableField[4];
        STableColumn[] aoTableColumns = new STableColumn[12];

        i = 0;
        if (isViewFromSource()) {
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ds.id_year");
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ds.id_doc");
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "dd.id_year");
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "dd.id_doc");
        }
        else {
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "dd.id_year");
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "dd.id_doc");
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ds.id_year");
            aoKeyFields[i++] = new STableField(SLibConstants.DATA_TYPE_INTEGER, "ds.id_doc");
        }

        for (i = 0; i < aoKeyFields.length; i++) {
            moTablePane.getPrimaryKeyFields().add(aoKeyFields[i]);
        }

        i = 0;
        if (isViewFromSource()) {
            if (getDpsSortingType() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC) {
                if (isViewForCategoryPur()) {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave proveedor", 50);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 350);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 350);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave proveedor", 50);
                    }
                }
                else {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 50);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 350);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 350);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 50);
                    }
                }

                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dst.code", "Tipo doc. orig.", STableConstants.WIDTH_CODE_DOC);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num_ds", "Folio doc. orig.", STableConstants.WIDTH_DOC_NUM);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ds.num_ref", "Referencia doc. orig.", STableConstants.WIDTH_DOC_NUM_REF);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "ds.dt", "Fecha doc. orig.", STableConstants.WIDTH_DATE);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dscob.code", "Sucursal empresa orig.", STableConstants.WIDTH_CODE_COB);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dst.code", "Tipo doc. orig.", STableConstants.WIDTH_CODE_DOC);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num_ds", "Folio doc. orig.", STableConstants.WIDTH_DOC_NUM);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ds.num_ref", "Referencia doc. orig.", STableConstants.WIDTH_DOC_NUM_REF);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "ds.dt", "Fecha doc. orig.", STableConstants.WIDTH_DATE);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dscob.code", "Sucursal empresa orig.", STableConstants.WIDTH_CODE_COB);

                if (isViewForCategoryPur()) {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave proveedor", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 350);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 350);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave proveedor", 100);
                    }
                }
                else {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 350);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 350);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 100);
                    }
                }
            }

            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ddt.code", "Tipo doc. dest.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num_dd", "Folio doc. dest.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dd.num_ref", "Referencia dest.", 50);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dd.dt", "Fecha doc. dest.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ddcob.code", "Sucursal empresa dest.", STableConstants.WIDTH_CODE_COB);
        }
        else {
            if (getDpsSortingType() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC) {
                if (isViewForCategoryPur()) {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave proveedor", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 350);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 350);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave proveedor", 100);
                    }
                }
                else {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 350);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 350);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 100);
                    }
                }

                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ddt.code", "Tipo doc. dest.", STableConstants.WIDTH_CODE_DOC);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num_dd", "Folio doc. dest.", STableConstants.WIDTH_DOC_NUM);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dd.num_ref", "Referencia doc. dest.", STableConstants.WIDTH_DOC_NUM_REF);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dd.dt", "Fecha doc. dest.", STableConstants.WIDTH_DATE);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ddcob.code", "Sucursal empresa dest.", STableConstants.WIDTH_CODE_COB);
            }
            else {
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ddt.code", "Tipo doc. dest.", STableConstants.WIDTH_CODE_DOC);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num_dd", "Folio doc. dest.", STableConstants.WIDTH_DOC_NUM);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dd.num_ref", "Referencia doc. dest.", STableConstants.WIDTH_DOC_NUM_REF);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "dd.dt", "Fecha doc. dest.", STableConstants.WIDTH_DATE);
                aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ddcob.code", "Sucursal empresa dest.", STableConstants.WIDTH_CODE_COB);

                if (isViewForCategoryPur()) {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave proveedor", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 350);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Proveedor", 350);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave proveedor", 100);
                    }
                }
                else {
                    if (miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 100);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 350);
                    }
                    else {
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "b.bp", "Cliente", 350);
                        aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "bc.bp_key", "Clave cliente", 100);
                    }
                }
            }

            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_DATE, "ds.dt", "Fecha doc. orig.", STableConstants.WIDTH_DATE);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dst.code", "Tipo doc. orig.", STableConstants.WIDTH_CODE_DOC);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "f_num_ds", "Folio doc. orig.", STableConstants.WIDTH_DOC_NUM);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "ds.num_ref", "Referencia orig.", 50);
            aoTableColumns[i++] = new STableColumn(SLibConstants.DATA_TYPE_STRING, "dscob.code", "Sucursal empresa orig.", STableConstants.WIDTH_CODE_COB);
        }

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

    private void actionDeleteLinks() {
        int gui = isViewForCategoryPur() ? SDataConstants.MOD_PUR : SDataConstants.MOD_SAL;
        SDataDps oSrcDps = null;
        SDataDps oDesDps = null;
        int[] pk = null;
        if (jbDeleteLinks.isEnabled()) {
            if (moTablePane.getSelectedTableRow() == null) {
                miClient.showMsgBoxInformation(SLibConstants.MSG_ERR_GUI_ROW_UNDEF);
            }
            else {
                pk = (int[]) moTablePane.getSelectedTableRow().getPrimaryKey();

                if (isViewFromSource()) {
                    oSrcDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, new int[] { pk[0], pk[1] }, SLibConstants.EXEC_MODE_SILENT);
                    oDesDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, new int[] { pk[2], pk[3] }, SLibConstants.EXEC_MODE_SILENT);
                }
                else {
                    oSrcDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, new int[] { pk[2], pk[3] }, SLibConstants.EXEC_MODE_SILENT);
                    oDesDps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, new int[] { pk[0], pk[1] }, SLibConstants.EXEC_MODE_SILENT);
                }

                if (miClient.showMsgBoxConfirm("La acción eliminará el vínculo entre los docs. '" + oSrcDps.getDpsNumber() + "' y '" + oDesDps.getDpsNumber() + "', lo cual es irreversible,\n" + SLibConstants.MSG_CNF_MSG_CONT) == JOptionPane.YES_OPTION) {
                    try {
                         for (SDataDpsEntry entry : oDesDps.getDbmsDpsEntries()) {
                            entry.setContractPriceYear(0);
                            entry.setContractPriceMonth(0);
                            entry.setIsRegistryEdited(true);
                        }
                        SDataUtilities.saveRegistry(miClient, oDesDps);
                            
                        if (isViewFromSource()) {
                            STrnUtilities.deleteLink(miClient, pk[0], pk[1], pk[2], pk[3]);
                        }
                        else {
                            STrnUtilities.deleteLink(miClient, pk[2], pk[3], pk[0], pk[1]); 
                        }
                    }
                    catch (Exception e) {
                        SLibUtilities.renderException(this, e);
                    }
                    miClient.getGuiModule(gui).refreshCatalogues(mnTabTypeAux01);
                }
            }
        }
    }

    private boolean isViewForCategoryPur() {
        return mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR;
    }

    private boolean isViewForCategorySal() {
        return mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL;
    }

    private boolean isViewFromSource() {
        return SLibUtilities.belongsTo(mnTabTypeAux02, new int[] { SDataConstantsSys.TRNX_LINK_EST_EST_SRC, SDataConstantsSys.TRNX_LINK_EST_CON_SRC, SDataConstantsSys.TRNX_LINK_ORD_SRC });
    }

    private int getDpsSortingType() {
        int type = SLibConstants.UNDEFINED;

        if (isViewForCategoryPur() && mnTabTypeAux02 == SDataConstantsSys.TRNX_LINK_DOC_DES) {
            type = miClient.getSessionXXX().getParamsErp().getFkSortingDpsSupplierTypeId();
        }
        else {
            type = miClient.getSessionXXX().getParamsErp().getFkSortingDpsCustomerTypeId();
        }

        return type;
    }

    private int[] getDpsTypeKey() {
        int[] dpsTypeKey = null;

        if (isViewForCategoryPur()) {
            if (mnTabTypeAux02 == SDataConstantsSys.TRNX_LINK_EST_EST_SRC) {
                dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_PUR_EST;
            }
            else if (mnTabTypeAux02 == SDataConstantsSys.TRNX_LINK_EST_CON_SRC) {
                dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_PUR_CON;
            }
            else if (mnTabTypeAux02 == SDataConstantsSys.TRNX_LINK_ORD_SRC || mnTabTypeAux02 == SDataConstantsSys.TRNX_LINK_ORD_DES) {
                dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_PUR_ORD;
            }
            else if (mnTabTypeAux02 == SDataConstantsSys.TRNX_LINK_DOC_DES) {
                dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_PUR_INV;
            }
        }
        else {
            if (mnTabTypeAux02 == SDataConstantsSys.TRNX_LINK_EST_EST_SRC) {
                dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_SAL_EST;
            }
            else if (mnTabTypeAux02 == SDataConstantsSys.TRNX_LINK_EST_CON_SRC) {
                dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_SAL_CON;
            }
            else if (mnTabTypeAux02 == SDataConstantsSys.TRNX_LINK_ORD_SRC || mnTabTypeAux02 == SDataConstantsSys.TRNX_LINK_ORD_DES) {
                dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_SAL_ORD;
            }
            else if (mnTabTypeAux02 == SDataConstantsSys.TRNX_LINK_DOC_DES) {
                dpsTypeKey = SDataConstantsSys.TRNU_TP_DPS_SAL_INV;
            }
        }

        return dpsTypeKey;
    }

    @Override
    public void createSqlQuery() {
        int[] dpsTypeKey = getDpsTypeKey();
        String sqlDpsType = "";
        String sqlDatePeriod = "";
        String sqlFunctAreas = "";
        String sqlInnerJoinDps = "";
        String sqlOrderBy = "";
        STableSetting setting = null;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            setting = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (setting.getType() == STableConstants.SETTING_FILTER_PERIOD) {
                if (isViewFromSource()) {
                    sqlDatePeriod = "AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "ds.dt");
                }
                else {
                    sqlDatePeriod = "AND " + SDataSqlUtilities.composePeriodFilter((int[]) setting.getSetting(), "dd.dt");
                }
            }
            else if (setting.getType() == SFilterConstants.SETTING_FILTER_FUNC_AREA) {
                if (!((String) setting.getSetting()).isEmpty()) {
                    if (isViewFromSource()) {
                        sqlFunctAreas = "AND ds.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                    }
                    else {
                        sqlFunctAreas = "AND dd.fid_func IN (" + ((String) setting.getSetting()) + ") ";
                    }
                }
            }
        }
        
        String sqlSeries = "";
        boolean dnsRight = false; 
        if (mbIsOrd) {
            if (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_PUR) {
                dnsRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_DOC_ORD_ALL_DNS).HasRight;
            }
            else if (mnTabTypeAux01 == SDataConstantsSys.TRNS_CT_DPS_SAL) {
                dnsRight = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_DOC_ORD_ALL_DNS).HasRight;
            }
            if (!dnsRight) {
                ArrayList<SDataUserDnsDps> usrDnsDpss = miClient.getSessionXXX().getUser().getDbmsConfigurationTransaction().getUserDnsDps();
                if (!usrDnsDpss.isEmpty()) {
                    for (SDataUserDnsDps usrDnsDps : usrDnsDpss) {
                        sqlSeries += sqlSeries.isEmpty() ? "(" : "OR ";
                        sqlSeries += "dd.num_ser = '" + usrDnsDps.getDocumentNumberSeries().getDocNumberSeries() + "' ";
                    }
                    sqlSeries += ") ";
                }
            }
        }
        
        if (isViewFromSource()) {
            sqlDpsType = "AND ds.fid_ct_dps = " + dpsTypeKey[0] + " AND ds.fid_cl_dps = " + dpsTypeKey[1] + " AND ds.fid_tp_dps = " + dpsTypeKey[2] + " ";
        }
        else {
            sqlDpsType = "AND dd.fid_ct_dps = " + dpsTypeKey[0] + " AND dd.fid_cl_dps = " + dpsTypeKey[1] + " AND dd.fid_tp_dps = " + dpsTypeKey[2] + " ";
        }

        if (isViewFromSource()) {
            sqlInnerJoinDps = "FROM trn_dps AS ds " +
                "INNER JOIN trn_dps_dps_supply AS sup ON ds.id_year = sup.id_src_year AND ds.id_doc = sup.id_src_doc " +
                "INNER JOIN trn_dps AS dd ON sup.id_des_year = dd.id_year AND sup.id_des_doc = dd.id_doc ";

            if (getDpsSortingType() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC) {
                sqlOrderBy = "ORDER BY ";

                if ((isViewForCategoryPur() && miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) ||
                    (isViewForCategorySal() && miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME)) {
                    sqlOrderBy += "bc.bp_key, b.bp, ";
                }
                else {
                    sqlOrderBy += "b.bp, bc.bp_key, ";
                }

                sqlOrderBy += "b.id_bp, " +
                        "dst.code, ds.num_ser, CAST(ds.num AS UNSIGNED INTEGER), ds.num, ds.dt, ds.id_year, ds.id_doc, " +
                        "ddt.code, dd.num_ser, CAST(dd.num AS UNSIGNED INTEGER), dd.num, dd.dt, dd.id_year, dd.id_doc ";
            }
            else {
                sqlOrderBy = "ORDER BY " +
                        "dst.code, ds.num_ser, CAST(ds.num AS UNSIGNED INTEGER), ds.num, ds.dt, ds.id_year, ds.id_doc, " +
                        "ddt.code, dd.num_ser, CAST(dd.num AS UNSIGNED INTEGER), dd.num, dd.dt, dd.id_year, dd.id_doc, ";

                if ((isViewForCategoryPur() && miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) ||
                    (isViewForCategorySal() && miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME)) {
                    sqlOrderBy += "bc.bp_key, b.bp, ";
                }
                else {
                    sqlOrderBy += "b.bp, bc.bp_key, ";
                }

                sqlOrderBy += "b.id_bp ";
            }
        }
        else {
            sqlInnerJoinDps = "FROM trn_dps AS dd " +
                "INNER JOIN trn_dps_dps_supply AS sup ON dd.id_year = sup.id_des_year AND dd.id_doc = sup.id_des_doc " +
                "INNER JOIN trn_dps AS ds ON sup.id_src_year = ds.id_year AND sup.id_src_doc = ds.id_doc ";

            if (getDpsSortingType() == SDataConstantsSys.CFGS_TP_SORT_BIZ_P_DOC) {
                sqlOrderBy = "ORDER BY ";

                if ((isViewForCategoryPur() && miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) ||
                    (isViewForCategorySal() && miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME)) {
                    sqlOrderBy += "bc.bp_key, b.bp, ";
                }
                else {
                    sqlOrderBy += "b.bp, bc.bp_key, ";
                }

                sqlOrderBy += "b.id_bp, " +
                        "ddt.code, dd.num_ser, CAST(dd.num AS UNSIGNED INTEGER), dd.num, dd.dt, dd.id_year, dd.id_doc, " +
                        "dst.code, ds.num_ser, CAST(ds.num AS UNSIGNED INTEGER), ds.num, ds.dt, ds.id_year, ds.id_doc ";
            }
            else {
                sqlOrderBy = "ORDER BY " +
                        "ddt.code, dd.num_ser, CAST(dd.num AS UNSIGNED INTEGER), dd.num, dd.dt, dd.id_year, dd.id_doc, " +
                        "dst.code, ds.num_ser, CAST(ds.num AS UNSIGNED INTEGER), ds.num, ds.dt, ds.id_year, ds.id_doc, ";

                if ((isViewForCategoryPur() && miClient.getSessionXXX().getParamsErp().getFkSortingSupplierTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME) ||
                    (isViewForCategorySal() && miClient.getSessionXXX().getParamsErp().getFkSortingCustomerTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME)) {
                    sqlOrderBy += "bc.bp_key, b.bp, ";
                }
                else {
                    sqlOrderBy += "b.bp, bc.bp_key, ";
                }

                sqlOrderBy += "b.id_bp ";
            }
        }

        msSql = "SELECT DISTINCT ds.id_year, ds.id_doc, dd.id_year, dd.id_doc, " +
                "CONCAT(ds.num_ser, IF(LENGTH(ds.num_ser) = 0, '', '-'), ds.num) AS f_num_ds, " +
                "CONCAT(dd.num_ser, IF(LENGTH(dd.num_ser) = 0, '', '-'), dd.num) AS f_num_dd, " +
                "ds.dt, ds.num_ser, ds.num, ds.num_ref, dst.code, dscob.code, " +
                "dd.dt, dd.num_ser, dd.num, dd.num_ref, ddt.code, ddcob.code, " +
                "b.id_bp, b.bp, bc.bp_key " +
                sqlInnerJoinDps + sqlDpsType + sqlDatePeriod + sqlFunctAreas +
                "AND ds.b_del = 0 AND ds.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                "AND dd.b_del = 0 AND dd.fid_st_dps = " + SDataConstantsSys.TRNS_ST_DPS_EMITED + " " +
                "INNER JOIN erp.bpsu_bp AS b ON " +
                (isViewFromSource() ? "ds.fid_bp_r = b.id_bp " : "dd.fid_bp_r = b.id_bp ") +
                "INNER JOIN erp.bpsu_bp_ct AS bc ON b.id_bp = bc.id_bp AND bc.id_ct_bp = " +
                (isViewForCategoryPur() ? SDataConstantsSys.BPSS_CT_BP_SUP : SDataConstantsSys.BPSS_CT_BP_CUS) + " " +
                "INNER JOIN erp.trnu_tp_dps AS dst ON ds.fid_ct_dps = dst.id_ct_dps AND ds.fid_cl_dps = dst.id_cl_dps AND ds.fid_tp_dps = dst.id_tp_dps " +
                "INNER JOIN erp.trnu_tp_dps AS ddt ON dd.fid_ct_dps = ddt.id_ct_dps AND dd.fid_cl_dps = ddt.id_cl_dps AND dd.fid_tp_dps = ddt.id_tp_dps " +
                "INNER JOIN erp.bpsu_bpb AS dscob ON ds.fid_cob = dscob.id_bpb " +
                "INNER JOIN erp.bpsu_bpb AS ddcob ON dd.fid_cob = ddcob.id_bpb " +
                (sqlSeries.isEmpty() ? "" : "WHERE " + sqlSeries) + 
                sqlOrderBy;
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

            if (button == jbDeleteLinks) {
                actionDeleteLinks();
            }
        }
    }
}
