/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.mod;

import erp.data.SDataConstantsSys;
import erp.mod.fin.db.SDbAbpBizPartner;
import erp.mod.fin.db.SDbAbpBizPartnerLink;
import erp.mod.fin.db.SDbAbpEntity;
import erp.mod.fin.db.SDbAbpEntityLink;
import erp.mod.fin.db.SDbAbpItem;
import erp.mod.fin.db.SDbAbpItemLink;
import erp.mod.fin.db.SDbAbpTax;
import erp.mod.fin.db.SDbAccount;
import erp.mod.fin.db.SDbAccountItemLink;
import erp.mod.fin.db.SDbBankLayout;
import erp.mod.fin.db.SDbBankLayoutDeposits;
import erp.mod.fin.db.SDbBankLayoutDepositsAnalyst;
import erp.mod.fin.db.SDbCheckWallet;
import erp.mod.fin.db.SDbCostCenter;
import erp.mod.fin.db.SDbTaxItemLink;
import erp.mod.fin.form.SFormAbpBizPartner;
import erp.mod.fin.form.SFormAbpBizPartnerLink;
import erp.mod.fin.form.SFormAbpEntity;
import erp.mod.fin.form.SFormAbpEntityLink;
import erp.mod.fin.form.SFormAbpItem;
import erp.mod.fin.form.SFormAbpItemLink;
import erp.mod.fin.form.SFormAbpTax;
import erp.mod.fin.form.SFormAccountItemLink;
import erp.mod.fin.form.SFormImportPayments;
import erp.mod.fin.form.SFormLayoutBank;
import erp.mod.fin.form.SFormTaxItemLink;
import erp.mod.fin.view.SViewAbpBizPartner;
import erp.mod.fin.view.SViewAbpBizPartnerLink;
import erp.mod.fin.view.SViewAbpEntity;
import erp.mod.fin.view.SViewAbpEntityLink;
import erp.mod.fin.view.SViewAbpItem;
import erp.mod.fin.view.SViewAbpItemLink;
import erp.mod.fin.view.SViewAbpTax;
import erp.mod.fin.view.SViewAccountItemLink;
import erp.mod.fin.view.SViewBankLayout;
import erp.mod.fin.view.SViewBankLayoutPayments;
import erp.mod.fin.view.SViewImportFile;
import erp.mod.fin.view.SViewTaxItemLink;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JMenu;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbRegistry;
import sa.lib.db.SDbRegistrySysFly;
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
 * @author Juan Barajas, Edwin Carmona
 */
public class SModuleFin extends SGuiModule {

    private SFormAccountItemLink moFormAccountItemLink;
    private SFormTaxItemLink moFormTaxItemLink;
    private SFormAbpEntity moFormAbpEntityCash;
    private SFormAbpEntityLink moFormAbpEntityLinkCash;
    private SFormAbpEntity moFormAbpEntityWarehouse;
    private SFormAbpEntityLink moFormAbpEntityLinkWarehouse;
    private SFormAbpEntity moFormAbpEntityPlant;
    private SFormAbpEntityLink moFormAbpEntityLinkPlant;
    private SFormAbpBizPartner moFormAbpBizPartner;
    private SFormAbpBizPartnerLink moFormAbpBizPartnerLink;
    private SFormAbpItem moFormAbpItem;
    private SFormAbpItemLink moFormAbpItemLink;
    private SFormAbpTax moFormAbpTax;
    private SFormLayoutBank moFormBankLayoutAccounting;
    private SFormLayoutBank moFormBankLayoutDps;
    private SFormLayoutBank moFormBankLayoutAdvance;
    private SFormImportPayments moFormImportPayments;

    private SBeanOptionPicker moPickerExchangeRateMxn;
    private SBeanOptionPicker moPickerExchangeRateUsd;
    private SBeanOptionPicker moPickerExchangeRateEur;
    private SBeanOptionPicker moPickerTaxes;

    
    
    public SModuleFin(SGuiClient client) {
        super(client, SModConsts.MOD_FIN_N, SLibConsts.UNDEFINED);
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
            case SModConsts.FINS_CLS_ACC_MOV:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tp_acc_mov = " + pk[0] + " AND id_cl_acc_mov = " + pk[1] + " AND id_cls_acc_mov = " + pk[2] + " "; }
                    @Override
                    public Object readField(final Statement statement, final int[] pk, final int field) throws SQLException, Exception {
                        Object value = null;
                        ResultSet resultSet = null;

                        initQueryMembers();
                        mnQueryResultId = SDbConsts.READ_ERROR;

                        msSql = "SELECT ";

                        switch (field) {
                            case SDbRegistry.FIELD_NAME:
                                msSql += "cls_acc_mov ";
                                break;
                            default:
                                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }

                        msSql += getSqlFromWhere(pk);

                        resultSet = statement.executeQuery(msSql);
                        if (!resultSet.next()) {
                            throw new Exception(SDbConsts.ERR_MSG_REG_NOT_FOUND);
                        }
                        else {
                            switch (field) {
                                case SDbRegistry.FIELD_CODE:
                                    value = resultSet.getString(1);
                                    break;
                                default:
                            }
                        }

                        mnQueryResultId = SDbConsts.READ_OK;
                        return value;
                    }
                };
                break;
            case SModConsts.FINS_CL_SYS_MOV:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_cl_sys_mov = " + pk[0] + " "; }
                };
                break;
            case SModConsts.FINS_TP_SYS_MOV:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_cl_sys_mov = " + pk[0] + " AND id_tp_sys_mov = " + pk[1] + " "; }
                };
                break;
            case SModConsts.FINS_CL_SYS_ACC:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_cl_sys_acc = " + pk[0] + " "; }
                };
                break;
            case SModConsts.FINS_TP_SYS_ACC:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_cl_sys_acc = " + pk[0] + " AND id_tp_sys_acc = " + pk[1] + " "; }
                };
                break;
            case SModConsts.FINS_FISCAL_ACC:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_fiscal_acc = " + pk[0] + " "; }
                };
                break;
            case SModConsts.FINS_FISCAL_CUR:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_fiscal_cur = " + pk[0] + " "; }
                };
                break;
            case SModConsts.FINS_FISCAL_BANK:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_fiscal_bank = " + pk[0] + " "; }
                };
                break;
            case SModConsts.FINS_FISCAL_PAY_MET:
                registry = new SDbRegistrySysFly(type) {
                    public void initRegistry() { }
                    public String getSqlTable() { return SModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_pay_met = " + pk[0] + " "; }
                };
                break;
            case SModConsts.FIN_ACC:
                registry = new SDbAccount();
                break;
            case SModConsts.FIN_CC:
                registry = new SDbCostCenter();
                break;
            case SModConsts.FIN_ACC_ITEM_LINK:
                registry = new SDbAccountItemLink();
                break;
            case SModConsts.FIN_TAX_ITEM_LINK:
                registry = new SDbTaxItemLink();
                break;
            case SModConsts.FIN_ABP_ENT:
                registry = new SDbAbpEntity();
                break;
            case SModConsts.FIN_ABP_ENT_LINK:
                registry = new SDbAbpEntityLink();
                break;
            case SModConsts.FIN_ABP_BP:
                registry = new SDbAbpBizPartner();
                break;
            case SModConsts.FIN_ABP_BP_LINK:
                registry = new SDbAbpBizPartnerLink();
                break;
            case SModConsts.FIN_ABP_ITEM:
                registry = new SDbAbpItem();
                break;
            case SModConsts.FIN_ABP_ITEM_LINK:
                registry = new SDbAbpItemLink();
                break;
            case SModConsts.FIN_ABP_TAX:
                registry = new SDbAbpTax();
                break;
            case SModConsts.FIN_LAY_BANK:
                registry = new SDbBankLayout();
                break;
            case SModConsts.FIN_LAY_BANK_DEP:
                registry = new SDbBankLayoutDeposits();
                break;
            case SModConsts.FIN_LAY_BANK_DEP_ANA:
                registry = new SDbBankLayoutDepositsAnalyst();
                break;
            case SModConsts.FIN_CHECK_WAL:
                registry = new SDbCheckWallet();
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
            case SModConsts.FINS_FISCAL_ACC:
                settings = new SGuiCatalogueSettings("Código agrupador SAT", 1, 0, SLibConsts.DATA_TYPE_BOOL);
                sql = "SELECT id_fiscal_acc AS " + SDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name, IF(b_eli, '', ' (!)')) AS " + SDbConsts.FIELD_ITEM + ", b_eli AS " + SDbConsts.FIELD_COMP  + "  "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY id_fiscal_acc ";
                break;
            case SModConsts.FINS_FISCAL_CUR:
                settings = new SGuiCatalogueSettings("Moneda SAT", 1);
                sql = "SELECT id_fiscal_cur AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY id_fiscal_cur ";
                break;
            case SModConsts.FINS_FISCAL_BANK:
                settings = new SGuiCatalogueSettings("Banco SAT", 1);
                sql = "SELECT id_fiscal_bank AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY id_fiscal_bank ";
                break;
            case SModConsts.FINS_FISCAL_PAY_MET:
                settings = new SGuiCatalogueSettings("Método pago SAT", 1);
                sql = "SELECT id_fiscal_pay_met AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY id_fiscal_pay_met ";
                break;
            case SModConsts.FINS_CFD_TAX:
                settings = new SGuiCatalogueSettings("Impuesto SAT", 1);
                sql = "SELECT id_cfd_tax AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_cfd_tax ";
                break;
            case SModConsts.FIN_YEAR:
                settings = new SGuiCatalogueSettings("Año contable", 1);
                sql = "SELECT id_year AS " + SDbConsts.FIELD_ID + "1, id_year AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY id_year ";
                break;
            case SModConsts.FINU_TAX_REG:
                settings = new SGuiCatalogueSettings("Región impuesto", 1);
                sql = "SELECT id_tax_reg AS " + SDbConsts.FIELD_ID + "1, tax_reg AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY tax_reg, id_tax_reg ";
                break;
            case SModConsts.FINU_TAX_BAS:
                settings = new SGuiCatalogueSettings("Impuesto básico", 1);
                sql = "SELECT id_tax_bas AS " + SDbConsts.FIELD_ID + "1, tax_bas AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY tax_bas, id_tax_bas ";
                break;
            case SModConsts.FINU_TAX:
                settings = new SGuiCatalogueSettings("Impuesto", 2, 1);
                sql = "SELECT id_tax_bas AS " + SDbConsts.FIELD_ID + "1, id_tax AS " + SDbConsts.FIELD_ID + "2, tax AS " + SDbConsts.FIELD_ITEM + ", id_tax_bas AS " + SDbConsts.FIELD_FK + "1 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY tax, id_tax_bas, id_tax ";
                break;
            case SModConsts.FINU_TP_LAY_BANK:
                settings = new SGuiCatalogueSettings("Tipo layout", 1, 2);
                sql = "SELECT id_tp_lay_bank AS " + SDbConsts.FIELD_ID + "1, tp_lay_bank AS " + SDbConsts.FIELD_ITEM + ", "
                        + "fid_tp_pay_bank AS " + SDbConsts.FIELD_FK + "1 fid_bank AS " + SDbConsts.FIELD_FK + "2 "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 " + (subtype == SLibConsts.UNDEFINED ? "" : " AND lay_bank = " + subtype) + " "
                        + "ORDER BY tp_lay_bank, id_tp_lay_bank ";
                break;
            case SModConsts.FIN_ABP_ENT:
                /* Use of other parameters:
                 * subtype = Filter: Entity category ID. Can be 'SLibConsts.UNDEFINED' meaning that all categories are requested.
                 */
                settings = new SGuiCatalogueSettings("Paq. contab. aut. entidad", 1);
                sql = "SELECT id_abp_ent AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0" + (subtype == SLibConsts.UNDEFINED ? "" : " AND fk_ct_ent = " + subtype) + " "
                        + "ORDER BY name, id_abp_ent ";
                break;
            case SModConsts.FIN_ABP_BP:
                /* Use of other parameters:
                 * subtype = Filter: Business partner category ID. Can be 'SLibConsts.UNDEFINED' meaning that all categories are requested.
                 */
                settings = new SGuiCatalogueSettings("Paq. contab. aut. asociado negocios", 1);
                sql = "SELECT id_abp_bp AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 " + (subtype == SLibConsts.UNDEFINED ? "" : " AND fk_ct_bp = " + subtype) + " "
                        + "ORDER BY name, id_abp_bp ";
                break;
            case SModConsts.FIN_ABP_ITEM:
                settings = new SGuiCatalogueSettings("Paq. contab. aut. ítem", 1);
                sql = "SELECT id_abp_item AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 "
                        + "ORDER BY name, id_abp_item ";
                break;
            case SModConsts.FIN_ABP_TAX:
                settings = new SGuiCatalogueSettings("Paq. contab. aut. impuesto", 1);
                sql = "SELECT id_abp_tax AS " + SDbConsts.FIELD_ID + "1, name AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " "
                        + "WHERE b_del = 0 "
                        + "ORDER BY name, id_abp_tax ";
                break;
            case SModConsts.FIN_TAX_GRP:
                settings = new SGuiCatalogueSettings("Grupo impuesto", 1);
                sql = "SELECT id_tax_grp AS " + SDbConsts.FIELD_ID + "1, tax_grp AS " + SDbConsts.FIELD_ITEM + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY tax_grp, id_tax_grp ";
                break;
            case SModConsts.FIN_ACC_CASH:
                settings = new SGuiCatalogueSettings("Cuenta de dinero", 2);
                settings.setCodeApplying(true);
                sql = "SELECT ac.id_cob " + SDbConsts.FIELD_ID + "1, ac.id_acc_cash AS " + SDbConsts.FIELD_ID + "2, ce.ent AS " + SDbConsts.FIELD_ITEM + ", ce.code AS " + SDbConsts.FIELD_CODE + " "
                        + "FROM " + SModConsts.TablesMap.get(type) + " AS ac "
                        + "INNER JOIN " + SModConsts.TablesMap.get(SModConsts.CFGU_COB_ENT) + " AS ce ON "
                        + "ac.id_cob = ce.id_cob AND ac.id_acc_cash = ce.id_ent "
                        + (params == null ? "" : (params.getKey().length == 1 ? "" : " INNER JOIN erp.bpsu_bank_acc AS bbc ON ac.fid_bpb_n = bbc.id_bpb AND ac.fid_bank_acc_n = bbc.id_bank_acc "))
                        + "WHERE ac.b_del = 0 AND ce.b_del = 0 " + (params != null && params.getKey() != null ? " AND ac.fid_cur = " + params.getKey()[0] : "") + " ";
                switch (subtype) {
                    case SModConsts.FINX_ACC_CASH_BANK:
                        sql += "AND ce.fid_ct_ent = " + SDataConstantsSys.CFGS_TP_ENT_CASH_BANK[0] + " AND " +
                                "ce.fid_tp_ent = " + SDataConstantsSys.CFGS_TP_ENT_CASH_BANK[1] + " " +
                                (params == null ? "" : (params.getKey().length == 1 ? "" : " AND bbc.fid_bank = " + params.getKey()[1] + " " ));
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                sql += "ORDER BY ce.ent, ce.code, ac.id_cob, ac.id_acc_cash ";
                
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
            case SModConsts.FIN_ACC_ITEM_LINK:
                view = new SViewAccountItemLink(miClient, "Filtros ítems ctas. contables");
                break;
            case SModConsts.FIN_TAX_ITEM_LINK:
                view = new SViewTaxItemLink(miClient, "Ítems impuestos");
                break;
            case SModConsts.FIN_ABP_ENT:
                switch (subtype) {
                    case SModSysConsts.CFGS_CT_ENT_CASH:
                        view = new SViewAbpEntity(miClient, subtype, "Paq. contab. aut. cuentas dinero");
                        break;
                    case SModSysConsts.CFGS_CT_ENT_WH:
                        view = new SViewAbpEntity(miClient, subtype, "Paq. contab. aut. almacenes bienes");
                        break;
                    case SModSysConsts.CFGS_CT_ENT_PLANT:
                        view = new SViewAbpEntity(miClient, subtype, "Paq. contab. aut. plantas producción");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.FIN_ABP_ENT_LINK:
                switch (subtype) {
                    case SModSysConsts.CFGS_CT_ENT_CASH:
                        view = new SViewAbpEntityLink(miClient, subtype, "Config. contab. cuentas dinero");
                        break;
                    case SModSysConsts.CFGS_CT_ENT_WH:
                        view = new SViewAbpEntityLink(miClient, subtype, "Config. contab. almacenes bienes");
                        break;
                    case SModSysConsts.CFGS_CT_ENT_PLANT:
                        view = new SViewAbpEntityLink(miClient, subtype, "Config. contab. plantas producción");
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.FIN_ABP_BP:
                view = new SViewAbpBizPartner(miClient, "Paq. contab. aut. asoc. negocios");
                break;
            case SModConsts.FIN_ABP_BP_LINK:
                view = new SViewAbpBizPartnerLink(miClient, "Config. contab. asoc. negocios");
                break;
            case SModConsts.FIN_ABP_ITEM:
                view = new SViewAbpItem(miClient, "Paq. contab. aut. ítems");
                break;
            case SModConsts.FIN_ABP_ITEM_LINK:
                view = new SViewAbpItemLink(miClient, "Config. contab. ítems");
                break;
            case SModConsts.FIN_ABP_TAX:
                view = new SViewAbpTax(miClient, "Paq. contab. aut. impuestos");
                break;
            case SModConsts.FIN_LAY_BANK:
                if (subtype == SModSysConsts.FIN_LAY_BANK_DPS) {
                    if (params == null) {
                        view = new SViewBankLayout(miClient, subtype, "Layouts transferencias");
                    }
                    else {
                        switch (params.getType()) {
                            case SModConsts.VIEW_ST_PEND:
                                view = new SViewBankLayoutPayments(miClient, subtype, "Layouts transferencias x pagar", params);
                                break;
                            case SModConsts.VIEW_ST_DONE:
                                view = new SViewBankLayoutPayments(miClient, subtype, "Layouts transferencias pagados", params);
                                break;
                            default:
                                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                    }
                }
                else if (subtype == SModSysConsts.FIN_LAY_BANK_ADV) {
                    if (params == null) {
                        view = new SViewBankLayout(miClient, subtype, "Layouts anticipos");
                    }
                    else {
                        switch (params.getType()) {
                            case SModConsts.VIEW_ST_PEND:
                                view = new SViewBankLayoutPayments(miClient, subtype, "Layouts anticipos x pagar", params);
                                break;
                            case SModConsts.VIEW_ST_DONE:
                                view = new SViewBankLayoutPayments(miClient, subtype, "Layouts anticipos pagados", params);
                                break;
                            default:
                                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                    }
                }
                break;
            case SModConsts.FIN_LAY_BANK_DEP:
                view = new SViewImportFile(miClient, subtype, "Importación de archivos");
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return view;
    }

    @Override
    public SGuiOptionPicker getOptionPicker(final int type, final int subtype, final SGuiParams params) {
        String sql = "";
        ArrayList<SGridColumnForm> gridColumns = new ArrayList<SGridColumnForm>();
        SGuiOptionPickerSettings settings = null;
        SGuiOptionPicker picker = null;

        switch (type) {
            case SModConsts.FIN_EXC_RATE:
                sql = "SELECT id_dt AS " + SDbConsts.FIELD_PICK + "1, exc_rate AS " + SDbConsts.FIELD_PICK + "2, exc_rate AS " + SDbConsts.FIELD_VALUE + " "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.FIN_EXC_RATE) + " "
                        + "WHERE b_del = 0 AND id_cur = " + subtype + " AND "
                        + "DATEDIFF('" + SLibUtils.DbmsDateFormatDate.format(miClient.getSession().getCurrentDate()) + "', id_dt) <= 30 "
                        + "ORDER BY id_dt, exc_rate ";
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DATE, SGridConsts.COL_TITLE_DATE));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_EXC_RATE, "Tipo de cambio"));
                settings = new SGuiOptionPickerSettings(SUtilConsts.TXT_SELECT + " tipo de cambio " + miClient.getSession().getSessionCustom().getCurrencyCode(new int[] { subtype }) + " / " + miClient.getSession().getSessionCustom().getLocalCurrencyCode(), sql, gridColumns, 0, SLibConsts.DATA_TYPE_DEC);

                switch (subtype) {
                    case SModSysConsts.CFGU_CUR_MXN:
                        if (moPickerExchangeRateMxn == null) {
                            moPickerExchangeRateMxn = new SBeanOptionPicker();
                            moPickerExchangeRateMxn.setPickerSettings(miClient, type, subtype, settings);
                        }
                        picker = moPickerExchangeRateMxn;
                        break;
                    case SModSysConsts.CFGU_CUR_USD:
                        if (moPickerExchangeRateUsd == null) {
                            moPickerExchangeRateUsd = new SBeanOptionPicker();
                            moPickerExchangeRateUsd.setPickerSettings(miClient, type, subtype, settings);
                        }
                        picker = moPickerExchangeRateUsd;
                        break;
                    case SModSysConsts.CFGU_CUR_EUR:
                        if (moPickerExchangeRateEur == null) {
                            moPickerExchangeRateEur = new SBeanOptionPicker();
                            moPickerExchangeRateEur.setPickerSettings(miClient, type, subtype, settings);
                        }
                        picker = moPickerExchangeRateEur;
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.FINU_TAX:
                sql = "SELECT id_tax_bas AS " + SDbConsts.FIELD_ID + "1, "
                        + "id_tax AS " + SDbConsts.FIELD_ID + "2, "
                        + "tax AS " + SDbConsts.FIELD_PICK + "1, per AS " + SDbConsts.FIELD_PICK + "2, "
                        + "val_u AS " + SDbConsts.FIELD_PICK + "3, val AS " + SDbConsts.FIELD_PICK + "4 "
                        + "FROM " + SModConsts.TablesMap.get(SModConsts.FINU_TAX) + " "
                        + "WHERE b_del = 0 "
                        + "ORDER BY tax, id_tax_bas, id_tax ";
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Impuesto"));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_PER_2D, "Porcentaje"));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Valor unitario"));
                gridColumns.add(new SGridColumnForm(SGridConsts.COL_TYPE_DEC_2D, "Valor"));
                settings = new SGuiOptionPickerSettings("Impuesto", sql, gridColumns, 2);

                if (moPickerTaxes == null) {
                    moPickerTaxes = new SBeanOptionPicker();
                    moPickerTaxes.setPickerSettings(miClient, type, subtype, settings);
                }
                picker = moPickerTaxes;
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
            case SModConsts.FIN_ACC_ITEM_LINK:
                if (moFormAccountItemLink == null) moFormAccountItemLink = new SFormAccountItemLink(miClient, "Filtro de ítem para cuenta contable");
                form = moFormAccountItemLink;
                break;
            case SModConsts.FIN_TAX_ITEM_LINK:
                if (moFormTaxItemLink == null) moFormTaxItemLink = new SFormTaxItemLink(miClient, "Ítem e impuesto");
                form = moFormTaxItemLink;
                break;
            case SModConsts.FIN_ABP_ENT:
                switch (subtype) {
                    case SModSysConsts.CFGS_CT_ENT_CASH:
                        if (moFormAbpEntityCash == null) moFormAbpEntityCash = new SFormAbpEntity(miClient, subtype, "Paquete contab. automática de cuenta de dinero");
                        form = moFormAbpEntityCash;
                        break;
                    case SModSysConsts.CFGS_CT_ENT_WH:
                        if (moFormAbpEntityWarehouse == null) moFormAbpEntityWarehouse = new SFormAbpEntity(miClient, subtype, "Paquete contab. automática de almacén de bienes");
                        form = moFormAbpEntityWarehouse;
                        break;
                    case SModSysConsts.CFGS_CT_ENT_PLANT:
                        if (moFormAbpEntityPlant == null) moFormAbpEntityPlant = new SFormAbpEntity(miClient, subtype, "Paquete contab. automática de planta de producción");
                        form = moFormAbpEntityPlant;
                        break;
                }
                break;
            case SModConsts.FIN_ABP_ENT_LINK:
                switch (subtype) {
                    case SModSysConsts.CFGS_CT_ENT_CASH:
                        if (moFormAbpEntityLinkCash == null) moFormAbpEntityLinkCash = new SFormAbpEntityLink(miClient, subtype, "Configuración contab. de cuenta de dinero");
                        form = moFormAbpEntityLinkCash;
                        break;
                    case SModSysConsts.CFGS_CT_ENT_WH:
                        if (moFormAbpEntityLinkWarehouse == null) moFormAbpEntityLinkWarehouse = new SFormAbpEntityLink(miClient, subtype, "Configuración contab. de almacén de bienes");
                        form = moFormAbpEntityLinkWarehouse;
                        break;
                    case SModSysConsts.CFGS_CT_ENT_PLANT:
                        if (moFormAbpEntityLinkPlant == null) moFormAbpEntityLinkPlant = new SFormAbpEntityLink(miClient, subtype, "Configuración contab. de planta de producción");
                        form = moFormAbpEntityLinkPlant;
                        break;
                }
                break;
            case SModConsts.FIN_ABP_BP:
                if (moFormAbpBizPartner == null) moFormAbpBizPartner = new SFormAbpBizPartner(miClient, type, "Paquete contab. automática de asociado de negocios");
                form = moFormAbpBizPartner;
                break;
            case SModConsts.FIN_ABP_BP_LINK:
                if (moFormAbpBizPartnerLink == null) moFormAbpBizPartnerLink = new SFormAbpBizPartnerLink(miClient, "Configuración contab. de asociado de negocios");
                form = moFormAbpBizPartnerLink;
                break;
            case SModConsts.FIN_ABP_ITEM:
                if (moFormAbpItem == null) moFormAbpItem = new SFormAbpItem(miClient, "Paquete contab. automática de ítem");
                form = moFormAbpItem;
                break;
            case SModConsts.FIN_ABP_ITEM_LINK:
                if (moFormAbpItemLink == null) moFormAbpItemLink = new SFormAbpItemLink(miClient, "Configuración contab. de ítem");
                form = moFormAbpItemLink;
                break;
            case SModConsts.FIN_ABP_TAX:
                if (moFormAbpTax == null) moFormAbpTax = new SFormAbpTax(miClient, "Paquete contab. automática de impuesto");
                form = moFormAbpTax;
                break;
            case SModConsts.FIN_LAY_BANK:
                switch (subtype) {
                    case SModConsts.FIN_REC:
                        if (moFormBankLayoutAccounting == null) moFormBankLayoutAccounting = new SFormLayoutBank(miClient, subtype, "Aplicación de pagos de layout bancario");
                        form = moFormBankLayoutAccounting;
                        break;
                    case SModSysConsts.FIN_LAY_BANK_DPS:
                        if (moFormBankLayoutDps == null) moFormBankLayoutDps = new SFormLayoutBank(miClient, subtype, "Layout de transferencias");
                        form = moFormBankLayoutDps;
                        break;
                    case SModSysConsts.FIN_LAY_BANK_ADV:
                        if (moFormBankLayoutAdvance == null) moFormBankLayoutAdvance = new SFormLayoutBank(miClient, subtype, "Layout de anticipos");
                        form = moFormBankLayoutAdvance;
                        break;
                    default:
                        miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case SModConsts.FIN_LAY_BANK_DEP:
                moFormImportPayments = new SFormImportPayments(miClient, subtype, "Importación de pagos por analista");
                form = moFormImportPayments;
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
    }

    @Override
    public SGuiReport getReport(final int type, final int subtype, final SGuiParams params) {
        String title = "";
        SGuiReport guiReport = null;

        switch (type) {
            case SModConsts.FINR_CSH_FLW_EXP:
                guiReport = new SGuiReport("reps/fin_inc_exp_due.jasper", "Reporte de ingresos y egresos esperados por periodo");
                break;
            case SModConsts.FINR_DPS_TAX_PEND:
                title = "Impuestos pendientes de ";
                switch (subtype) {
                    case SModSysConsts.TRNS_CT_DPS_PUR:
                        title += "egresos";
                        break;
                    case SModSysConsts.TRNS_CT_DPS_SAL:
                        title += "ingresos";
                        break;
                }
                guiReport = new SGuiReport("reps/fin_dps_tax_pend.jasper", title);
                break;
            default:
                miClient.showMsgBoxError(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return guiReport;
    }
}
