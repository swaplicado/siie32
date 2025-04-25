/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod;

import erp.mod.qlt.db.SDbDatasheetTemplate;
import erp.mod.qlt.db.SDbDatasheetTemplateLink;
import erp.mod.qlt.db.SDbLotApproved;
import erp.mod.qlt.db.SDbQltyAnalysis;
import erp.mod.qlt.db.SDbQltyAnalysisType;
import erp.mod.qlt.form.SFormAnalysis;
import erp.mod.qlt.form.SFormDatasheetTemplate;
import erp.mod.qlt.form.SFormDatasheetTemplateLink;
import erp.mod.qlt.form.SFormQltLotApproved;
import erp.mod.qlt.view.SViewAnalysis;
import erp.mod.qlt.view.SViewDatasheetTemplate;
import erp.mod.qlt.view.SViewDatasheetTemplateLink;
import erp.mod.qlt.view.SViewQltLotApproved;
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
 * @author Uriel Castañeda, Edwin Carmona
 */
public class SModuleQlt extends SGuiModule {
    
    private SFormQltLotApproved moFormQualityLot;
    private SFormAnalysis moFormAnalysis;
    private SFormDatasheetTemplate moFormDataSheetTemplate;
    private SFormDatasheetTemplateLink moFormDataSheetTemplateLink;

    public SModuleQlt(SGuiClient client) {
        super(client, SModConsts.MOD_QLT_N, SLibConsts.UNDEFINED);
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
            case SModConsts.QLT_LOT_APR:
                registry = new SDbLotApproved();
                break;
            case SModConsts.QLT_TP_ANALYSIS:
                registry = new SDbQltyAnalysisType();
                break;
            case SModConsts.QLT_ANALYSIS:
                registry = new SDbQltyAnalysis();
                break;
            case SModConsts.QLT_DATASHEET_TEMPLATE:
                registry = new SDbDatasheetTemplate();
                break;
            case SModConsts.QLT_DATASHEET_TEMPLATE_LINK:
                registry = new SDbDatasheetTemplateLink();
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public SGuiCatalogueSettings getCatalogueSettings(final int type, final int subtype, final SGuiParams params) {
        String sql = "";
        SGuiCatalogueSettings settings = null;

        switch (type) {
            case SModConsts.QLT_TP_ANALYSIS:
                settings = new SGuiCatalogueSettings("Tipo de análisis", 1);
                sql = "SELECT id_tp_analysis AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name)  AS " + SDbConsts.FIELD_ITEM + " "
                        + " FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = FALSE "
                        + "ORDER BY name ASC ";
                break;
            case SModConsts.QLT_ANALYSIS:
                settings = new SGuiCatalogueSettings("Análisis", 1);
                sql = "SELECT id_analysis AS " + SDbConsts.FIELD_ID + "1, CONCAT(analysis_name, ' - ', unit_symbol)  AS " + SDbConsts.FIELD_ITEM + " "
                        + " FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = FALSE "
                        + "ORDER BY analysis_name ASC ";
                break;
            case SModConsts.QLT_DATASHEET_TEMPLATE:
                settings = new SGuiCatalogueSettings("Fichas técnicas", 1);
                sql = "SELECT id_datasheet_template AS " + SDbConsts.FIELD_ID + "1, CONCAT(template_name,' - ',template_standard,' - ',template_version)  AS " + SDbConsts.FIELD_ITEM + " "
                        + " FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = FALSE "
                        + "ORDER BY template_name ASC ";
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
       
        switch (type) {
            case SModConsts.QLT_LOT_APR:
                view = new SViewQltLotApproved(miClient, "Lotes aprobados");
                break;
            case SModConsts.QLT_ANALYSIS:
                view = new SViewAnalysis(miClient, "Análisis de laboratorio");
                break;
            case SModConsts.QLT_DATASHEET_TEMPLATE:
                view = new SViewDatasheetTemplate(miClient, "Fichas ténicas");
                break;
            case SModConsts.QLT_DATASHEET_TEMPLATE_LINK:
                view = new SViewDatasheetTemplateLink(miClient, "Fichas ténicas e ítems");
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return view;
    }

    @Override
    public SGuiOptionPicker getOptionPicker(final int type, final int subtype, final SGuiParams params) {
        String sql = "";
        ArrayList<SGridColumnForm> gridColumns = new ArrayList<>();
        SGuiOptionPickerSettings settings = null;
        SGuiOptionPicker picker = null;

        switch (type) {
            case SModConsts.QLT_TP_ANALYSIS:
                sql = "SELECT ta.id_tp_analysis AS " + SDbConsts.FIELD_ID + "1, "
                        + "ta.code AS " + SDbConsts.FIELD_PICK + "1, ta.name AS " + SDbConsts.FIELD_PICK + "2 "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.QLT_TP_ANALYSIS) + " AS ta "
                        + "WHERE ta.b_del = 0 "
                        + "ORDER BY ta.name, ta.code ";
                
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Código"));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "Nombre"));
                settings = new SGuiOptionPickerSettings("Tipo de análisis", sql, gridColumns, 1);

                SBeanOptionPicker moPickerAnalysisType = new SBeanOptionPicker();
                moPickerAnalysisType.setPickerSettings(miClient, type, subtype, settings);
                picker = moPickerAnalysisType;
                break;
                
            case SModConsts.QLT_ANALYSIS:
                sql = "SELECT a.id_analysis AS " + SDbConsts.FIELD_ID + "1, "
                        + "a.analysis_name AS " + SDbConsts.FIELD_PICK + "1, a.unit_symbol AS " + SDbConsts.FIELD_PICK + "2 "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.QLT_ANALYSIS) + " AS a "
                        + "WHERE a.b_del = 0 "
                        + "ORDER BY a.analysis_name, a.id_analysis ";
                
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Análisis"));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_BPR_L, "Unidad"));
                settings = new SGuiOptionPickerSettings("Análisis", sql, gridColumns, 1);

                SBeanOptionPicker moPickerAnalysis = new SBeanOptionPicker();
                moPickerAnalysis.setPickerSettings(miClient, type, subtype, settings);
                picker = moPickerAnalysis;
                break;
                
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return picker;
    }

    @Override
    public SGuiForm getForm(final int type, final int subtype, final SGuiParams params) {
        SGuiForm form = null;

        switch (type) {
            
            case SModConsts.QLT_LOT_APR:
                if (moFormQualityLot == null) moFormQualityLot = new SFormQltLotApproved(miClient, "Lotes aprobados");
                form = moFormQualityLot;
                break;
            case SModConsts.QLT_ANALYSIS:
                if (moFormAnalysis == null) moFormAnalysis = new SFormAnalysis(miClient, "Análisis de laboratorio");
                form = moFormAnalysis;
                break;
            case SModConsts.QLT_DATASHEET_TEMPLATE:
                if (moFormDataSheetTemplate == null) moFormDataSheetTemplate = new SFormDatasheetTemplate(miClient, "Ficha técnica");
                form = moFormDataSheetTemplate;
                break;
            case SModConsts.QLT_DATASHEET_TEMPLATE_LINK:
                if (moFormDataSheetTemplateLink == null) moFormDataSheetTemplateLink = new SFormDatasheetTemplateLink(miClient, "Ficha técnica e ítem");
                form = moFormDataSheetTemplateLink;
                break;
            
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
    }

    @Override
    public SGuiReport getReport(final int type, final int subtype, final SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
