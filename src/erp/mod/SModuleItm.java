/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod;

import erp.data.SDataConstantsSys;
import erp.mcfg.data.SDataParamsErp;
import erp.mod.itm.db.SDbItem;
import erp.mod.itm.db.SDbUnit;
import erp.mod.itm.db.SDbUnitType;
import java.util.ArrayList;
import javax.swing.JMenu;
import sa.lib.SLibConsts;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.grid.SGridColumnForm;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiCatalogueSettings;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiForm;
import sa.lib.gui.SGuiModule;
import sa.lib.gui.SGuiOptionPicker;
import sa.lib.gui.SGuiOptionPickerSettings;
import sa.lib.gui.SGuiParams;
import sa.lib.gui.SGuiReport;
import sa.lib.gui.bean.SBeanOptionPicker;

/**
 *
 * @author Juan Barajas, Uriel Castañeda
 */
public class SModuleItm extends SGuiModule {

    private SBeanOptionPicker moPickerItem;
    private SBeanOptionPicker moPickerUnit;
    
    public SModuleItm(SGuiClient client) {
        super(client, SModConsts.MOD_ITM_N, SLibConsts.UNDEFINED);
        moModuleIcon = miClient.getImageIcon(mnModuleType);
    }

    @Override
    public JMenu[] getMenus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SDbRegistry getRegistry(final int type, final SGuiParams params) {
        SDbRegistry registry = null;

        switch (type) {
            case SModConsts.ITMU_TP_UNIT:
                registry = new SDbUnitType();
                break;
            case SModConsts.ITMU_UNIT:
                registry = new SDbUnit();
                break;
            case SModConsts.ITMU_ITEM:
                registry = new SDbItem();
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public SGuiCatalogueSettings getCatalogueSettings(final int type, final int subtype, final SGuiParams params) {
        String sql = "";
        String aux = "";
        Object value = null;
        SGuiCatalogueSettings settings = null;

        switch (type) {
            case SModConsts.ITMS_LINK:
                settings = new SGuiCatalogueSettings("Tipo de referencia", 1);
                switch (subtype) {
                    case SModConsts.TRN_MAT_CC_GRP_ITEM:
                        sql = "SELECT id_link AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " " +
                                "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 " +
                                "AND (id_link = " + SModSysConsts.ITMS_LINK_IGEN + " OR id_link = " + SModSysConsts.ITMS_LINK_ITEM + ") " +
                                "ORDER BY id_link ";
                        break;
                    default:
                        sql = "SELECT id_link AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " " +
                                " FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 " +
                                "ORDER BY id_link ";
                        break;
                }
                break;
                case SModConsts.ITMS_CT_ITEM:
                settings = new SGuiCatalogueSettings("Categoría de ítem", 1);
                sql = "SELECT ct_idx AS " + SDbConsts.FIELD_ID + "1, ct_item AS " + SDbConsts.FIELD_ITEM + " " +
                        "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY ct_idx ";
                break;
            case SModConsts.ITMS_CL_ITEM:
                settings = new SGuiCatalogueSettings("Clase de ítem", 1, 1);
                sql = "SELECT cl_idx AS " + SDbConsts.FIELD_ID + "1, cl_item AS " + SDbConsts.FIELD_ITEM + ", " +
                        "id_ct_item AS " + SDbConsts.FIELD_FK + "1 " +
                        "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY cl_idx ";
                break;
            case SModConsts.ITMS_TP_ITEM:
                settings = new SGuiCatalogueSettings("Tipo de ítem", 1, 2);
                sql = "SELECT tp_idx AS " + SDbConsts.FIELD_ID + "1, tp_item AS " + SDbConsts.FIELD_ITEM + ", " +
                        "id_ct_item AS " + SDbConsts.FIELD_FK + "1, id_cl_item AS " + SDbConsts.FIELD_FK + "2 " +
                        "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY tp_idx ";
                break;
            case SModConsts.ITMS_CFD_PROD_SERV:
                settings = new SGuiCatalogueSettings("Producto-servicio SAT", 1);
                sql = "SELECT id_cfd_prod_serv AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " " +
                        "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.ITMS_CFD_UNIT:
                settings = new SGuiCatalogueSettings("Unidad SAT", 1);
                sql = "SELECT id_cfd_unit AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + SDbConsts.FIELD_ITEM + " " +
                        "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case SModConsts.ITMU_IFAM:
                settings = new SGuiCatalogueSettings("Familia de ítem", 1);
                sql = "SELECT id_ifam AS " + SDbConsts.FIELD_ID + "1, ifam AS " + SDbConsts.FIELD_ITEM + " " +
                        " FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY ifam, id_ifam ";
                break;
            case SModConsts.ITMU_IGRP:
                settings = new SGuiCatalogueSettings("Grupo de ítems", 1, 1);
                sql = "SELECT id_igrp AS " + SDbConsts.FIELD_ID + "1, igrp AS " + SDbConsts.FIELD_ITEM + ", fid_ifam AS " + SDbConsts.FIELD_FK + "1 " +
                        "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 " +
                        "ORDER BY igrp, id_igrp ";
                break;
            case SModConsts.ITMU_IGEN:
                settings = new SGuiCatalogueSettings("Ítem genérico", 1, 1);
                switch (subtype) {
                    case SModConsts.ITMX_IGEN_INV:
                        sql = "SELECT id_igen AS " + SDbConsts.FIELD_ID + "1, CONCAT(igen, ' (', code, ')') AS " + SDbConsts.FIELD_ITEM + ", fid_igrp AS " + SDbConsts.FIELD_FK + "1 " +
                                "FROM " + SModConsts.TablesMap.get(type) + " " +
                                "WHERE NOT b_del AND b_inv " +
                                "ORDER BY igen, id_igen ";
                        break;
                    case SLibConsts.UNDEFINED:
                        sql = "SELECT id_igen AS " + SDbConsts.FIELD_ID + "1, CONCAT(igen, ' (', code, ')') AS " + SDbConsts.FIELD_ITEM + ", fid_igrp AS " + SDbConsts.FIELD_FK + "1 " +
                                "FROM " + SModConsts.TablesMap.get(type) + " " +
                                "WHERE b_del = 0 " +
                                "ORDER BY igen, id_igen ";
                        break;
                }
                break;
            case SModConsts.ITMU_LINE:
                settings = new SGuiCatalogueSettings("Línea de ítems", 1, 1);

                if (params != null) {
                    value = params.getKey();

                    if (value != null) {
                        aux += "AND fid_igen = " + ((int[]) value)[0] + " ";
                    }
                }

                sql = "SELECT id_line AS " + SDbConsts.FIELD_ID + "1, CONCAT(line, ' (', code, ')') AS " + SDbConsts.FIELD_ITEM + ", fid_igen AS " + SDbConsts.FIELD_FK + "1 " +
                        "FROM " + SModConsts.TablesMap.get(type) + " " +
                        "WHERE b_del = 0 " + aux +
                        "ORDER BY line, id_line ";
                break;
            case SModConsts.ITMU_BRD:
                settings = new SGuiCatalogueSettings("Tipo de marca", 1);
                sql = "SELECT id_brd AS " + SDbConsts.FIELD_ID + "1, brd AS " + SDbConsts.FIELD_ITEM + " " +
                        "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY brd, id_brd ";
                break;
            case SModConsts.ITMU_MFR:
                settings = new SGuiCatalogueSettings("Tipo de fabricante", 1);
                sql = "SELECT id_mfr AS f_id_1, mfr AS " + SDbConsts.FIELD_ITEM + " " +
                        "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY mfr, id_mfr ";
                break;
            case SModConsts.ITMU_ITEM:
                settings = new SGuiCatalogueSettings("Ítems", 1, 1);
                sql = "SELECT i.id_item AS " + SDbConsts.FIELD_ID + "1, " +
                        (!((SDataParamsErp) miClient.getSession().getConfigSystem()).getIsItemKeyApplying() ? "i.item " :
                        (((SDataParamsErp) miClient.getSession().getConfigSystem()).getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ?
                        "CONCAT(i.item_key, ' - ', i.item) " : "CONCAT(i.item, ' - ', i.item_key) ")) + " AS " + SDbConsts.FIELD_ITEM + ", " +
                        "i.fid_igen AS " + SDbConsts.FIELD_FK + "1, u.symbol AS f_comp " +
                        "FROM " + SModConsts.TablesMap.get(type) + " AS i " +
                        "INNER JOIN erp.itmu_unit AS u ON i.fid_unit = u.id_unit " +
                        "INNER JOIN erp.itmu_igen AS ig ON i.fid_igen = ig.id_igen " +
                        "WHERE i.b_del = 0 " +
                        "ORDER BY " + (!((SDataParamsErp) miClient.getSession().getConfigSystem()).getIsItemKeyApplying() ? "i.item, " :
                        ((SDataParamsErp) miClient.getSession().getConfigSystem()).getFkSortingItemTypeId() == SDataConstantsSys.CFGS_TP_SORT_KEY_NAME ? "i.item_key, i.item, " : "i.item, i.item_key, ") + "id_item ";
                break;
            case SModConsts.ITMU_UNIT:
                settings = new SGuiCatalogueSettings("Unidad", 1);
                sql = "SELECT id_unit AS " + SDbConsts.FIELD_ID + "1, " + " CONCAT(unit,' (',symbol,')') AS " + SDbConsts.FIELD_ITEM + " " + 
                      "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " WHERE b_del = 0 " +
                      "ORDER BY unit, id_unit" ;
                break;        
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        if (settings != null) {
            settings.setSql(sql);
        }

        return settings;
    }

    @Override
    public SGridPaneView getView(final int type, final int subtype, final SGuiParams params) {
        SGridPaneView view = null;

        return view;
    }

    @Override
    public SGuiOptionPicker getOptionPicker(final int type, final int subtype, final SGuiParams params) {
        String sql = "";
        ArrayList<SGridColumnForm> gridColumns = new ArrayList<SGridColumnForm>();
        SGuiOptionPickerSettings settings = null;
        SGuiOptionPicker picker = null;
        
        switch (type) {
            case SModConsts.ITMU_ITEM:
                switch (subtype) {
                    case SModConsts.TRN_MAT_REQ:
                        sql = "SELECT a.id_item AS " + SDbConsts.FIELD_ID + "1, "
                                + "a.item_key AS " + SDbConsts.FIELD_PICK + "1, a.item AS " + SDbConsts.FIELD_PICK + "2 "
                                + "FROM ("
                                + "SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CC_GRP_ITEM) + " AS igen ON "
                                + "igen.id_link = " + SModSysConsts.ITMS_LINK_IGEN + " AND igen.id_ref = i.fid_igen " 
                                + "UNION "
                                + "SELECT * FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " AS i "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CC_GRP_ITEM) + " AS ii ON "
                                + "ii.id_link = " + SModSysConsts.ITMS_LINK_ITEM + " AND ii.id_ref = i.id_item "
                                + ") AS a "
                                + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.TRN_MAT_CC_GRP_USR) + " AS ccgu ON "
                                + "a.id_mat_cc_grp = ccgu.id_mat_cc_grp "
                                + "WHERE NOT a.b_del AND a.id_mat_cc_grp = " + params.getParamsMap().get(SModConsts.TRN_MAT_CC_GRP) + " "
                                + "AND ccgu.id_link = " + SModSysConsts.USRS_LINK_USR + " "
                                + "AND ccgu.id_ref = " + params.getParamsMap().get(SModConsts.USRU_USR) + " "
                                + "ORDER BY a.item_key, a.item, a.id_item ";
                        break;
                    default:
                        sql = "SELECT id_item AS " + SDbConsts.FIELD_ID + "1, "
                                + "item_key AS " + SDbConsts.FIELD_PICK + "1, item AS " + SDbConsts.FIELD_PICK + "2 "
                                + "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_ITEM) + " "
                                + "WHERE b_del = 0 "
                                + "ORDER BY item_key, item, id_item ";
                        break;
                }
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "Clave"));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Ítem"));
                settings = new SGuiOptionPickerSettings("Ítem", sql, gridColumns, 1);

                if (moPickerItem == null) {
                    moPickerItem = new SBeanOptionPicker();
                    moPickerItem.setPickerSettings(miClient, type, subtype, settings);
                }
                picker = moPickerItem;
                break;
                
            case SModConsts.ITMU_UNIT:
                sql = "SELECT id_unit AS " + SDbConsts.FIELD_ID + "1, symbol AS " + SDbConsts.FIELD_PICK + "1, unit AS " +  SDbConsts.FIELD_PICK + "2 "
                      + "FROM " + SModConsts.TablesMap.get(SModConsts.ITMU_UNIT) + " "
                      + "WHERE NOT b_del " + (subtype == SLibConsts.UNDEFINED ? "" : "AND fid_tp_unit = " + subtype + " ")
                      + "ORDER BY unit, symbol, id_unit ";
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Unidad"));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "Clave"));
                settings = new SGuiOptionPickerSettings("Unidad", sql, gridColumns, 1);

                if (moPickerUnit == null) {
                    moPickerUnit = new SBeanOptionPicker();
                    moPickerUnit.setPickerSettings(miClient, type, subtype, settings);
                }
                picker = moPickerUnit;
                break;    

            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return picker;
    }

    @Override
    public SGuiForm getForm(final int type, final int subtype, final SGuiParams params) {
        SGuiForm form = null;

        return form;
    }

    @Override
    public SGuiReport getReport(final int type, final int subtype, final SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
