/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.view;

import erp.data.SDataConstantsSys;
import erp.gui.account.SAccountUtils;
import erp.mcfg.data.SDataParamsCompany;
import erp.mod.SModConsts;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.grid.SGridColumnView;
import sa.lib.grid.SGridConsts;
import sa.lib.grid.SGridFilterDateRange;
import sa.lib.grid.SGridFilterValue;
import sa.lib.grid.SGridFilterYear;
import sa.lib.grid.SGridPaneSettings;
import sa.lib.grid.SGridPaneView;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiConsts;

/**
 *
 * @author Sergio Flores
 */
public class SViewCustomReportsExpenses extends SGridPaneView implements ActionListener, ItemListener {

    public static final int SUBTYPE_PERIOD = 1;
    public static final int SUBTYPE_MONTHS = 2;

    protected SCustomReportsParser moReportsParser;
    protected SCustomReportsParser.Report moReport;
    protected SCustomReportsParser.Access moReportAccess;
    protected SCustomReportsParser.User moReportUser;
    protected SCustomReportsParser.Config[] maoReportConfigs; // index 0: Config of Report; index 1: Config of Access to wich the User belongs

    protected SGridFilterDateRange moFilterDateRange;
    protected SGridFilterYear moFilterYear;
    
    protected boolean mbProcessingView;
    protected JButton moButtonShowConfig;
    protected SPaneCostCenterLevel moPaneCostCenterLevel;
    protected JCheckBox moCheckShowItem;
    protected JCheckBox moCheckShowItemAux;
    protected JCheckBox moCheckShowConcept;

    /**
     * 
     * @param client GUI Client.
     * @param subtype View subtype. Supported options: SModConsts.FINX_CUST_REPS_PER, SModConsts.FINX_CUST_REPS_MOS.
     * @param title View tab title.
     */
    public SViewCustomReportsExpenses(SGuiClient client, int subtype, String title) {
        super(client, SGridConsts.GRID_PANE_VIEW, SModConsts.FINX_CUST_REPS_EXPS, subtype, title, 0, null, true); // prevent from saving user GUI preferences!
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setRowButtonsEnabled(false);
        
        try {
            // get custom report settings:
            
            moReportsParser = new SCustomReportsParser();
            moReportsParser.readCustomReports(SCustomReportsParser.CUST_REPS_EXPENSES);
            
            ArrayList<SCustomReportsParser.Report> reports;
            
            if (miClient.getSession().getUser().isSupervisor()) {
                reports = moReportsParser.getCustomReports();
            }
            else {
                reports = moReportsParser.getUserCustomReports(miClient.getSession().getUser().getPkUserId());
            }
            
            moReport = null;
            moReportAccess = null;
            moReportUser = null;
            maoReportConfigs = null;
            
            if (reports.isEmpty()) {
                throw new Exception("No hay reportes para el usuario '" + miClient.getSession().getUser().getName() + "'.");
            }
            else if (reports.size() == 1) {
                moReport = reports.get(0);
            }
            else {
                SDialogCustomReportPicker picker = new SDialogCustomReportPicker(miClient, mnGridType, "Seleccionar reporte", reports);
                picker.resetForm();
                picker.setVisible(true);
                
                if (picker.getFormResult() == SGuiConsts.FORM_RESULT_OK) {
                    moReport = moReportsParser.getCustomReport((int) picker.getValue(SModConsts.FINX_CUST_REPS_EXPS));
                }
                else {
                    miClient.showMsgBoxWarning(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
            }
            
            boolean isReportAvailable = moReport != null;
            
            if (isReportAvailable) {
                moReportUser = moReport.getUser(miClient.getSession().getUser().getPkUserId());
                moReportAccess = moReport.getUserAccess(miClient.getSession().getUser().getPkUserId());
                maoReportConfigs = new SCustomReportsParser.Config[] { moReport.Config, (moReportAccess != null ? moReportAccess.Config : null) };
            }
            
            switch (mnGridSubtype) {
                case SUBTYPE_PERIOD:
                    moFilterDateRange = new SGridFilterDateRange(miClient, this);
                    moFilterDateRange.initFilter(new Date[] { SLibTimeUtils.getBeginOfMonth(miClient.getSession().getCurrentDate()), SLibTimeUtils.getEndOfMonth(miClient.getSession().getCurrentDate()) });
                    getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterDateRange);
                    break;

                case SUBTYPE_MONTHS:
                    moFilterYear = new SGridFilterYear(miClient, this);
                    moFilterYear.initFilter(new int[] { miClient.getSession().getCurrentYear() });
                    getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moFilterYear);
                    break;

                default:
                    // nothing
            }
            
            // set up filters and controls for query:
            
            String user = miClient.getSession().getUser().getName();
            JTextField textUser = new JTextField(user);
            textUser.setCaretPosition(0);
            textUser.setPreferredSize(new Dimension(100, 23));
            textUser.setEditable(false);
            textUser.setFocusable(false);
            textUser.setToolTipText("Usuario");
            
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(textUser);
            
            moButtonShowConfig = new JButton(new ImageIcon(getClass().getResource("/sa/lib/img/cmd_form_help.gif")));
            moButtonShowConfig.setEnabled(isReportAvailable);
            moButtonShowConfig.setToolTipText("Ver configuración");
            moButtonShowConfig.setPreferredSize(new Dimension(23, 23));
            moButtonShowConfig.addActionListener(this);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moButtonShowConfig);
            
            String report = moReport != null ? moReport.Report : SLibConsts.ERR_MSG_UNKNOWN;
            JTextField textReport = new JTextField(report);
            textReport.setCaretPosition(0);
            textReport.setPreferredSize(new Dimension(100, 23));
            textReport.setEditable(false);
            textReport.setFocusable(false);
            textReport.setToolTipText("Reporte");
            
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(textReport);
            
            mbProcessingView = false;
            
            moPaneCostCenterLevel = new SPaneCostCenterLevel(miClient, this);
            moPaneCostCenterLevel.setPaneEnabled(isReportAvailable);
            moPaneCostCenterLevel.initFilter(SAccountUtils.getLevels(((SDataParamsCompany) miClient.getSession().getConfigCompany()).getMaskCostCenter()));
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moPaneCostCenterLevel);
            
            moCheckShowItem = new JCheckBox("Ítems");
            moCheckShowItem.setEnabled(isReportAvailable);
            moCheckShowItem.setSelected(true);
            moCheckShowItem.setPreferredSize(new Dimension(85, 23));
            moCheckShowItem.addItemListener(this);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moCheckShowItem);
            
            moCheckShowItemAux = new JCheckBox("Ítems aux.");
            moCheckShowItemAux.setEnabled(isReportAvailable);
            moCheckShowItemAux.setSelected(true);
            moCheckShowItemAux.setPreferredSize(new Dimension(100, 23));
            moCheckShowItemAux.addItemListener(this);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moCheckShowItemAux);
            
            moCheckShowConcept = new JCheckBox("Conceptos");
            moCheckShowConcept.setEnabled(isReportAvailable);
            moCheckShowConcept.setSelected(false);
            moCheckShowConcept.setPreferredSize(new Dimension(100, 23));
            moCheckShowConcept.addItemListener(this);
            getPanelCommandsSys(SGuiConsts.PANEL_CENTER).add(moCheckShowConcept);
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedShowConfig() {
        String message = "REPORTE:\n" +
                SCustomReportsParser.humanizeConfigJson(moReport.Config.toString());
        
        if (moReportUser != null) {
            message += "\nUSUARIO:\n" +
                SCustomReportsParser.humanizeConfigJson(moReportUser.toString());
        }
        
        if (moReportAccess != null) {
            message += "\nACCESO:\n" +
                SCustomReportsParser.humanizeConfigJson(moReportAccess.Config.toString());
        }
        
        miClient.showMsgBoxInformation(message);
    }
    
    private void itemStateChangedOptions(final JCheckBox checkBox, final int stateChange) {
        try {
            mbProcessingView = true;

            if (checkBox == moCheckShowItem) {
                switch (stateChange) {
                    case ItemEvent.SELECTED:
                        moCheckShowItemAux.setEnabled(true);
                        moCheckShowItemAux.setSelected(true);

                        moCheckShowConcept.setEnabled(true);
                        moCheckShowConcept.setSelected(false);
                        break;

                    case ItemEvent.DESELECTED:
                        moCheckShowItemAux.setEnabled(false);
                        moCheckShowItemAux.setSelected(false);

                        moCheckShowConcept.setEnabled(false);
                        moCheckShowConcept.setSelected(false);
                        break;
                    default:
                        // nothing
                }
            }

            if (checkBox == moCheckShowItemAux) {
                switch (stateChange) {
                    case ItemEvent.SELECTED:
                        moCheckShowConcept.setEnabled(true);
                        moCheckShowConcept.setSelected(false);
                        break;

                    case ItemEvent.DESELECTED:
                        moCheckShowConcept.setEnabled(false);
                        moCheckShowConcept.setSelected(false);
                        break;
                    default:
                        // nothing
                }
            }

            initGrid(); // refresh again all view columns and rows!
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        finally {
            mbProcessingView = false;
        }
    }

    @Override
    public void prepareSqlQuery() {
        Object filter = null;
        Date[] filterDates = null;
        SPaneCostCenterLevel.Filter filterLevel = null;
        Integer year = null;
        Date beginOfYear = null;

        moPaneSettings = new SGridPaneSettings(0);

        switch (mnGridSubtype) {
            case SUBTYPE_PERIOD:
                filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_DATE_RANGE)).getValue();
                if (filter != null) {
                    filterDates = (Date[]) filter;

                    beginOfYear = SLibTimeUtils.getBeginOfYear(filterDates[0]);
                    year = SLibTimeUtils.digestYear(beginOfYear)[0];
                }
                break;
                
            case SUBTYPE_MONTHS:
                filter = ((SGridFilterValue) moFiltersMap.get(SGridConsts.FILTER_YEAR)).getValue();
                if (filter != null) {
                    year = ((int[]) filter)[0];
                    beginOfYear = SLibTimeUtils.getBeginOfYear(SLibTimeUtils.createDate(year));
                }
                break;
                
            default:
                // nothing
        }
        
        filter = ((SGridFilterValue) moFiltersMap.get(SPaneCostCenterLevel.FILTER_LEVEL)).getValue();
        if (filter != null) {
            filterLevel = (SPaneCostCenterLevel.Filter) filter;
        }
        
        // set up sections of query sentence:
        
        String sqlQueryFilters = "";
        String sqlWhereItemsMaskExcl = ""; // exclude items from showing its concepts at detail level
        String sqlWhereItemsMaskIncl = ""; // include items but masking its concepts at detail level
        
        if (filterDates != null) {
            sqlQueryFilters += "AND r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(filterDates[0]) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(filterDates[1]) + "' ";
        }
        
        for (SCustomReportsParser.Config config : maoReportConfigs) { // index 0: Config of Report; index 1: Config of Access to wich the User belongs
            if (config != null) {
                if (!config.Filter.isEmpty()) {
                    String jsonFilter = config.Filter;
                    jsonFilter = jsonFilter.replaceAll(SCustomReportsParser.CODE_ACC, "re.fid_acc");
                    jsonFilter = jsonFilter.replaceAll(SCustomReportsParser.CODE_CC, "re.fid_cc_n");
                    jsonFilter = jsonFilter.replaceAll(SCustomReportsParser.CODE_ITEM, "i.item_key");
                    
                    sqlQueryFilters += "AND (" + jsonFilter + ") ";
                }

                if (!config.Mask.isEmpty()) {
                    String jsonMask = config.Mask;
                    jsonMask = jsonMask.replaceAll(SCustomReportsParser.CODE_ACC, "re.fid_acc");
                    jsonMask = jsonMask.replaceAll(SCustomReportsParser.CODE_CC, "re.fid_cc_n");
                    jsonMask = jsonMask.replaceAll(SCustomReportsParser.CODE_ITEM, "i.item_key");
                    
                    sqlWhereItemsMaskExcl += "AND NOT (" + jsonMask + ") ";
                    sqlWhereItemsMaskIncl += "AND (" + jsonMask + ") ";
                }
            }
        }
        
        String sqlSelectCols = "";
        String sqlGroupByCols = "";
        String sqlOrderByCols = "";
        
        sqlSelectCols = "cc.id_cc AS " + SDbConsts.FIELD_CODE + ", cc.cc AS " + SDbConsts.FIELD_NAME;
        sqlGroupByCols = "cc.id_cc, cc.cc";
        sqlOrderByCols = SDbConsts.FIELD_CODE + ", " + SDbConsts.FIELD_NAME;
        
        if (moCheckShowItem.isSelected()) {
            sqlSelectCols += ", i.item_key, i.item, u.symbol";
            sqlGroupByCols += ", i.item_key, i.item, u.symbol";
            sqlOrderByCols += ", item_key, item, symbol";
        }
        
        if (moCheckShowItemAux.isSelected()) {
            sqlSelectCols += ", ia.item_key AS _item_key_aux, ia.item AS _item_aux";
            sqlGroupByCols += ", ia.item_key, ia.item";
            sqlOrderByCols += ", _item_key_aux, _item_aux";
        }
        
        if (moCheckShowConcept.isSelected()) {
            sqlSelectCols += ", r.dt, re.concept AS _concept"; // note that 're.concept' will be replaced by 'i.item' in masked section of query sentence
            sqlGroupByCols += ", r.dt, re.concept"; // note that 're.concept' will be replaced by 'i.item' in masked section of query sentence
            sqlOrderByCols += ", dt, _concept, _fin_rec";
        }
        
        // compose columns for concept:
        
        String sqlSelectColsConceptFull = "";
        String sqlSelectColsConceptMask = "";
        String sqlGroupByColsConceptFull = "";
        String sqlGroupByColsConceptMask = "";
        
        if (moCheckShowConcept.isSelected()) {
            sqlSelectColsConceptFull = ", CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + "), '-', re.sort_pos) AS _fin_rec";
            sqlSelectColsConceptMask = ", CONCAT(r.id_tp_rec, '-', erp.lib_fix_int(r.id_num, " + SDataConstantsSys.NUM_LEN_FIN_REC + ")) AS _fin_rec";
            
            sqlGroupByColsConceptFull = ", r.id_tp_rec, r.id_num, re.sort_pos";
            sqlGroupByColsConceptMask = ", r.id_tp_rec, r.id_num";
        }
        
        // compose aggregate functions for sum:
        
        String sqlSelectColsSum = "";
        
        if (mnGridSubtype == SUBTYPE_MONTHS) {
            for (int month = 1; month <= SLibTimeConsts.MONTHS; month++) {
                if (moCheckShowItem.isSelected()) {
                    sqlSelectColsSum += ", SUM(IF(r.id_per = " + month + ", re.units * IF(re.credit > 0, -1.0, 1.0), 0.0)) AS _unt_" + month;
                }
                
                sqlSelectColsSum += ", SUM(IF(r.id_per = " + month + ", re.debit - re.credit, 0.0)) AS _bal_" + month;
            }
        }
        
        if (moCheckShowItem.isSelected()) {
            sqlSelectColsSum += ", SUM(re.units * IF(re.credit > 0, -1.0, 1.0)) AS _unt";
        }
        
        sqlSelectColsSum += ", SUM(re.debit - re.credit) AS _bal";
        
        // compose query sentence:
        
        String sqlSelectFull = "SELECT " + sqlSelectCols + sqlSelectColsConceptFull + sqlSelectColsSum;
        String sqlSelectMask = "SELECT " + (sqlSelectCols + sqlSelectColsConceptMask + sqlSelectColsSum).replaceAll("re.concept", "i.item");
        String sqlFrom = "\nFROM fin_rec AS r "
                + "INNER JOIN fin_rec_ety AS re ON re.id_year = r.id_year AND re.id_per = r.id_per AND re.id_bkc = r.id_bkc AND re.id_tp_rec = r.id_tp_rec AND re.id_num = r.id_num "
                + "INNER JOIN fin_acc AS a ON a.id_acc = re.fid_acc "
                + "LEFT OUTER JOIN fin_cc AS cc ON cc.id_cc = " + (filterLevel == null || filterLevel.LeftChars == 0 ? "re.fid_cc_n" : "CONCAT(LEFT(re.fid_cc_n, " + filterLevel.LeftChars + "), '" + filterLevel.RightMask + "')") + " "
                + "LEFT OUTER JOIN erp.itmu_item AS i ON i.id_item = re.fid_item_n "
                + "LEFT OUTER JOIN erp.itmu_unit AS u ON u.id_unit = re.fid_unit_n "
                + "LEFT OUTER JOIN erp.itmu_item AS ia ON ia.id_item = re.fid_item_aux_n ";
        String sqlWhere = "\nWHERE NOT r.b_del AND NOT re.b_del AND NOT b_adj_year "
                + "AND r.id_year = " + year + " AND r.dt >= '" + SLibUtils.DbmsDateFormatDate.format(beginOfYear) + "' " + sqlQueryFilters;
        String sqlGropyByFull = "\nGROUP BY " + sqlGroupByCols + sqlGroupByColsConceptFull;
        String sqlGropyByMask = "\nGROUP BY " + (sqlGroupByCols + sqlGroupByColsConceptMask).replaceAll("re.concept", "i.item");
        String sqlOrderBy = "\nORDER BY " + sqlOrderByCols;
        
        if (!moCheckShowItem.isSelected()) {
            /*
             * Query sentence is composed in one simple section, without items in detail.
             */

            msSql = sqlSelectFull +
                    sqlFrom +
                    sqlWhere +
                    sqlGropyByFull +
                    ";";
        }
        else {
            /*
             * Query sentence is composed by two sections:
             * 1. Items that can be shown in detail.
             * 2. Items taht must be masked to hide concept.
             */

            msSql = sqlSelectFull +
                    sqlFrom +
                    sqlWhere +
                    sqlWhereItemsMaskExcl +
                    sqlGropyByFull +
                    "\nUNION\n" +
                    sqlSelectMask +
                    sqlFrom +
                    sqlWhere +
                    sqlWhereItemsMaskIncl +
                    sqlGropyByMask +
                    sqlOrderBy +
                    ";";
        }
    }

    @Override
    public ArrayList<SGridColumnView> createGridColumns() {
        SGridColumnView gridColumnView = null;
        ArrayList<SGridColumnView> gridColumnsViews = new ArrayList<>();

        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ACC, SDbConsts.FIELD_CODE, "No. centro costo"));
        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ACC, SDbConsts.FIELD_NAME, "Centro costo"));
        
        if (moCheckShowItem.isSelected()) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "item_key", "Clave ítem"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_S, "item", "Ítem"));
        }
        
        if (moCheckShowItemAux.isSelected()) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_ITM, "_item_key_aux", "Clave ítem aux."));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "_item_aux", "Ítem aux."));
        }
        
        if (moCheckShowConcept.isSelected()) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DATE, "dt", "Fecha"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "_concept", "Concepto"));
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "_fin_rec", "Póliza contable"));
        }
        
        if (moCheckShowItem.isSelected()) {
            gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_TEXT_CODE_UNT, "symbol", "Unidad"));
        }
        
        switch (mnGridSubtype) {
            case SUBTYPE_PERIOD:
                if (moCheckShowItem.isSelected()) {
                    gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "_unt", "Cantidad período"));
                }
                
                gridColumnView = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_bal", "Monto período $");
                gridColumnView.setSumApplying(true);
                gridColumnsViews.add(gridColumnView);
                break;
                
            case SUBTYPE_MONTHS:
                String[] months = SLibTimeUtils.createMonthsOfYearStd(Calendar.SHORT);

                for (int month = 1; month <= SLibTimeConsts.MONTHS; month++) {
                    if (moCheckShowItem.isSelected()) {
                        gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "_unt_" + month, "Cantidad " + months[month - 1]));
                    }

                    gridColumnView = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_bal_" + month, "Monto " + months[month - 1] + " $");
                    gridColumnView.setSumApplying(true);
                    gridColumnsViews.add(gridColumnView);
                }

                if (moCheckShowItem.isSelected()) {
                    gridColumnsViews.add(new SGridColumnView(SGridConsts.COL_TYPE_DEC_QTY, "_unt", "Cantidad anual"));
                }
                
                gridColumnView = new SGridColumnView(SGridConsts.COL_TYPE_DEC_AMT, "_bal", "Monto anual $");
                gridColumnView.setSumApplying(true);
                gridColumnsViews.add(gridColumnView);
                break;
                
            default:
                // nothing
        }

        return gridColumnsViews;
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(SModConsts.FIN_CC);
        moSuscriptionsSet.add(SModConsts.FIN_ACC);
        moSuscriptionsSet.add(SModConsts.FIN_REC);
        moSuscriptionsSet.add(SModConsts.ITMU_ITEM);
        moSuscriptionsSet.add(SModConsts.ITMU_UNIT);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == moButtonShowConfig) {
                actionPerformedShowConfig();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JCheckBox && !mbProcessingView) {
            itemStateChangedOptions((JCheckBox) e.getSource(), e.getStateChange());
        }
    }
}
