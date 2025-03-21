/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mfin.form;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.form.SDialogShowImportErrors;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mcfg.data.SDataCurrency;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataAccountCash;
import erp.mfin.data.SDataCostCenter;
import erp.mfin.data.SDataRecord;
import erp.mfin.data.SDataRecordEntry;
import erp.mfin.data.SDataTax;
import erp.mfin.data.diot.SDiotUtils;
import erp.mitm.data.SDataItem;
import erp.mod.SModSysConsts;
import erp.mod.bps.db.SBpsUtils;
import erp.mtrn.data.SDataDps;
import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sa.lib.SLibConsts;

/**
 * Clase de utilería para válidar y crear renglones de pólizas contables desde la forma de captura o desde un archivo externo.
 * @author Isabel Servín
 */
public abstract class SFinRecordUtils {
    
    public static int SOURCE_FORM = 1;
    public static int SOURCE_FILE = 2;
    
    public static int COMP_ACC = 1;
    public static int COMP_CONCEPT = 2;
    public static int COMP_BIZPARTNER = 3;
    public static int COMP_OCCASIONAL = 4;
    public static int COMP_REFERENCE = 5;
    public static int COMP_TAX_APP = 6;
    public static int COMP_TAX = 7;
    public static int COMP_TAX_CASH = 8;
    public static int COMP_ENTITY = 9;
    public static int COMP_ITEM = 10;
    public static int COMP_QTY = 11;
    public static int COMP_UNIT = 12;
    public static int COMP_ITEM_AUX = 13;
    public static int COMP_DPS = 14;
    public static int COMP_DPS_ADJ = 15;
    public static int COMP_YEAR = 16;
    public static int COMP_CHECK = 17;
    public static int COMP_CUR = 18;
    public static int COMP_DEBIT_CY = 19;
    public static int COMP_CREDIT_CY = 20;
    public static int COMP_DEBIT = 21;
    public static int COMP_CREDIT = 22;
    public static int COMP_EXC_DIFF = 23;
    public static int COMP_EXC_RATE = 24;
    public static int COMP_CC = 25;
    public static int COMP_FOREGN = 26;
    
    /**
     * Procesar un archivo para el ingreso de manera masiva de partidas de pólizas contables.
     * @param client
     * @param file
     * @param record
     * @return ArrayList<SDataRecordEntry>
     */
    public static ArrayList<SDataRecordEntry> processRecordEntriesFile(SClientInterface client, File file, SDataRecord record) {
        ArrayList<SDataRecordEntry> maRecordEntries = new ArrayList<>();
        ArrayList<SFinRecordEntry> maFinRecordEntries = new ArrayList<>();
        
        // Nombres de las celdas en el formato predefinido
        
        HashMap<Integer, String> fileColumns = new HashMap<>();
        fileColumns.put(COMP_ACC, "No. cuenta contable");
        fileColumns.put(COMP_CONCEPT, "Concepto");
        fileColumns.put(COMP_DEBIT, "Debe $");
        fileColumns.put(COMP_CREDIT, "Haber $");
        fileColumns.put(COMP_EXC_RATE, "Tipo cambio");
        fileColumns.put(COMP_DEBIT_CY, "Debe moneda $");
        fileColumns.put(COMP_CREDIT_CY, "Haber moneda $");
        fileColumns.put(COMP_CUR, "Código moneda");
        fileColumns.put(COMP_EXC_DIFF, "Es dif. cambiaria");
        fileColumns.put(COMP_CC, "No. centro costo");
        fileColumns.put(COMP_BIZPARTNER, "RFC asociado negocio");
        fileColumns.put(COMP_OCCASIONAL, "RFC ocasional");
        fileColumns.put(COMP_FOREGN, "ID fiscal extranjero");
        fileColumns.put(COMP_REFERENCE, "Repositorio contable");
        fileColumns.put(COMP_TAX_APP, "Aplican impuestos");
        fileColumns.put(COMP_TAX, "Impuesto base, Tipo impuesto, Tasa impuesto, Tipo IVA");
        fileColumns.put(COMP_TAX_CASH, "Tipo aplicación impuesto");
        fileColumns.put(COMP_ENTITY, "Código entidad");
        fileColumns.put(COMP_ITEM, "Código ítem");
        fileColumns.put(COMP_ITEM_AUX, "Código ítem auxiliar");
        fileColumns.put(COMP_QTY, "Cantidad unidad");
        fileColumns.put(COMP_UNIT, "Código unidad");
        fileColumns.put(COMP_DPS, "Folio factura");
        fileColumns.put(COMP_DPS_ADJ, "Folio NC");
        fileColumns.put(COMP_YEAR, "Ejercicio contable");
        
        String sql;
        ResultSet resultSet;
        Statement statement;
        int mnAccountSystemTypeId;
        Object[] accountsAndDescription;
        int total = 0;
        int errors = 0;
        String error = "Errores:\n";
        int warnings = 0;
        String warning = "Advertencias:\n";
        int taxBaseId;
        int i = 1;
        boolean mbIsCurrentAccountDiogAccount = false;
        String msEmptyAccountId = SDataUtilities.createNewFormattedAccountId(client, client.getSessionXXX().getParamsErp().getDeepAccounts());
        Vector<Integer> mvAccountLevels = SDataUtilities.getAccountLevels(msEmptyAccountId);
        
        // Leer el archivo y crear las partidas
        
        try {
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // lee la primera hoja
            for (i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                SFinRecordEntry entry = new SFinRecordEntry();
                
                Row row = sheet.getRow(i);
                if (row != null) {
                    
                    String accountId = row.getCell(0) == null ? "" : row.getCell(0).getStringCellValue();
                    String concept = row.getCell(1) == null ? "" : row.getCell(1).getStringCellValue();
                    double debit = row.getCell(2) == null || row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING ? 0 : row.getCell(2).getNumericCellValue();
                    double credit = row.getCell(3) == null || row.getCell(3).getCellType() == Cell.CELL_TYPE_STRING ? 0 : row.getCell(3).getNumericCellValue();
                    double exchangeRate = row.getCell(4) == null || row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING ? 0 : row.getCell(4).getNumericCellValue();
                    double debitCy = row.getCell(5) == null || row.getCell(5).getCellType() == Cell.CELL_TYPE_STRING ? 0 : row.getCell(5).getNumericCellValue();
                    double creditCy = row.getCell(6) == null || row.getCell(6).getCellType() == Cell.CELL_TYPE_STRING  ? 0 : row.getCell(6).getNumericCellValue();
                    String curCode = row.getCell(7) == null ? "" : row.getCell(7).getStringCellValue();
                    String exchangeDiference = row.getCell(8) == null ? "" : row.getCell(8).getStringCellValue();
                    String costCenter = row.getCell(9) == null ? "" : row.getCell(9).getStringCellValue();
                    String fiscalId = row.getCell(10) == null ? "" : row.getCell(10).getStringCellValue();
                    String ocassionalFiscalId = row.getCell(11) == null ? "" : row.getCell(11).getStringCellValue();
                    String country = row.getCell(12) == null ? "" : row.getCell(12).getStringCellValue();
                    String fiscalForegnId = row.getCell(13) == null ? "" : row.getCell(13).getStringCellValue();
                    String repository = row.getCell(14) == null ? "" : row.getCell(14).getStringCellValue();
                    String taxAppliying = row.getCell(15) == null ? "" : row.getCell(15).getStringCellValue();
                    String taxBase = row.getCell(16) == null ? "" : row.getCell(16).getStringCellValue();
                    String taxType = row.getCell(17) == null ? "" : row.getCell(17).getStringCellValue();
                    double taxRate = row.getCell(18) == null || row.getCell(18).getCellType() == Cell.CELL_TYPE_STRING ? 0 : row.getCell(18).getNumericCellValue();
                    String vatType = row.getCell(19) == null ? "" : row.getCell(19).getStringCellValue();
                    String taxAppType = row.getCell(20) == null ? "" : row.getCell(20).getStringCellValue();
                    String entity = row.getCell(21) == null ? "" : row.getCell(21).getStringCellValue();
                    String itemKey = row.getCell(22) == null ? "" : row.getCell(22).getStringCellValue();
                    String itemAuxKey = row.getCell(23) == null ? "" : row.getCell(23).getStringCellValue();
                    double quantity = row.getCell(24) == null || row.getCell(24).getCellType() == Cell.CELL_TYPE_STRING ? 0 : row.getCell(24).getNumericCellValue();
                    String unit = row.getCell(25) == null ? "" : row.getCell(25).getStringCellValue();
                    String dpsInvoice = row.getCell(26) == null ? "" : row.getCell(26).getStringCellValue();
                    String dpsInvoiceNC = row.getCell(27) == null ? "" : row.getCell(27).getStringCellValue();
                    int year = row.getCell(28) == null || row.getCell(28).getCellType() == Cell.CELL_TYPE_STRING ? 0 : (int) row.getCell(28).getNumericCellValue();

                    accountsAndDescription = SDataUtilities.getInputAccountsAndDescription(client, accountId, mvAccountLevels);
                    SDataAccount accMajor = (SDataAccount) accountsAndDescription[0];
                    SDataAccount acc = accountsAndDescription[1] == null ? (SDataAccount) accountsAndDescription[0] : (SDataAccount) accountsAndDescription[1];

                    if (acc != null) {
                        if (concept.isEmpty() && concept.length() > 100) {
                            error += "El tamaño del campo Concepto debe ser de 1 a 100 caracteres para el renglón " + (i + 1) + ".\n";
                            errors++;
                        }
                        
                        entry.AccountId = acc.getPkAccountIdXXX();
                        entry.Account = acc;
                        entry.AccountMajor = accMajor;
                        entry.Concept = concept;
                        mnAccountSystemTypeId = accMajor.getFkAccountSystemTypeId();
                        int[] anAccountSubclass = new int[] { accMajor.getFkAccountTypeId_r(), accMajor.getFkAccountClassId_r(), accMajor.getFkAccountSubclassId_r() };

                        boolean isAccSysBizPartnerAll = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_SUP, SDataConstantsSys.FINS_TP_ACC_SYS_CUS, SDataConstantsSys.FINS_TP_ACC_SYS_CDR, SDataConstantsSys.FINS_TP_ACC_SYS_DBR });
                        boolean isAccSysBizPartnerSupCus = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_SUP, SDataConstantsSys.FINS_TP_ACC_SYS_CUS });
                        boolean isAccSysPurchases = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_PUR, SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ });
                        boolean isAccClsPurchases = SLibUtilities.belongsTo(anAccountSubclass, new int[][] {SDataConstantsSys.FINS_CLS_ACC_PUR, SDataConstantsSys.FINS_CLS_ACC_PUR_ADJ });
                        boolean isAccSysSales = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_SAL, SDataConstantsSys.FINS_TP_ACC_SYS_SAL_ADJ });
                        boolean isAccClsSales = SLibUtilities.belongsTo(anAccountSubclass, new int[][] {SDataConstantsSys.FINS_CLS_ACC_SAL, SDataConstantsSys.FINS_CLS_ACC_SAL_ADJ });
                        boolean isAccSysTax = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT, SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT });
                        boolean isTrnSupplierTax = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_SUP, SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT });
                        boolean isTrnCustomerTax = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_CUS, SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT });

                        try {
                            mbIsCurrentAccountDiogAccount = SDiotUtils.isDiotAccount(client.getSession().getStatement(), accMajor) || 
                                    SDiotUtils.isDiotAccount(client.getSession().getStatement(), acc);
                            entry.IsCurrentAccountDiogAccount = mbIsCurrentAccountDiogAccount;
                        }
                        catch (Exception e) {
                            client.showMsgBoxWarning(e.getMessage());
                        }

                        // Guardar los montos

                        entry.Debit = debit;
                        entry.Credit = credit;
                        entry.ExchangeRate = exchangeRate;
                        entry.IsExchangeDifference = exchangeDiference.equalsIgnoreCase("Sí");
                        if (entry.ExchangeRate == 0 && !entry.IsExchangeDifference) {
                            error += "No se especificó un valor en Tipo cambio para el renglón " + (i + 1) + ".\n";
                            errors++;
                        }
                        entry.DebitCy = debitCy;
                        entry.CreditCy = creditCy;
                        entry.CurId = SDataUtilities.obtainCurrencyId(client, curCode);
                        if (entry.CurId == 0) {
                            error += "No se encontró el Código moneda '" + curCode + "' para el renglón " + (i + 1) + ".\n";
                            errors++;
                        }
                        
                        if (entry.Debit == 0d && entry.Credit == 0d && entry.DebitCy == 0d && entry.CreditCy == 0d) {
                            error += "No se ingresó ningún monto en los campos Debe, Haber, Debe moneda, Haber moneda para el renglón " + (i + 1) + ".\n";
                            errors++;
                        }
                        
                        entry.CostCenter = costCenter;

                        int bizPartnerId;
                        statement = client.getSession().getStatement();

                        // Validar si es necesario el asociado de negocios
                        
                        if (accMajor.getIsRequiredBizPartner() || isAccSysBizPartnerAll || isAccSysPurchases || isAccClsPurchases || isAccSysSales || isAccClsSales || isAccSysTax || mbIsCurrentAccountDiogAccount) {
                            if (isAccSysBizPartnerAll || (isAccSysPurchases && isAccClsPurchases) || (isAccSysSales && isAccClsSales)) {
                                entry.IsBizPartnerRequired = true;
                            }
                            
                            // Validar el asociado de negocios
                            
                            if (!fiscalId.isEmpty()) {
                                int[] bpCategory;
                                
                                if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_SUP || isAccSysPurchases || mbIsCurrentAccountDiogAccount) {
                                    bpCategory = new int[] { SDataConstantsSys.BPSS_CT_BP_SUP };
                                }
                                else if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_CUS || isAccSysSales) {
                                    bpCategory = new int[] { SDataConstantsSys.BPSS_CT_BP_CUS };
                                }
                                else if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_CDR) {
                                    bpCategory = new int[] { SDataConstantsSys.BPSS_CT_BP_CDR };
                                }
                                else if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_DBR) {
                                    bpCategory = new int[] { SDataConstantsSys.BPSS_CT_BP_DBR };
                                }
                                else if (isAccSysTax) {
                                    bpCategory = new int[] { SDataConstantsSys.BPSS_CT_BP_SUP, SDataConstantsSys.BPSS_CT_BP_CUS };
                                }
                                else {
                                    switch (accMajor.getFkAccountLedgerTypeId()) {
                                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_CUS:
                                            bpCategory = new int[] { SDataConstantsSys.BPSS_CT_BP_CUS };
                                            break;
                                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_DBR:
                                            bpCategory = new int[] { SDataConstantsSys.BPSS_CT_BP_CUS, SDataConstantsSys.BPSS_CT_BP_DBR };
                                            break;
                                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_SUP:
                                            bpCategory = new int[] { SDataConstantsSys.BPSS_CT_BP_SUP };
                                            break;
                                        case SDataConstantsSys.FINU_TP_ACC_LEDGER_CDR:
                                            bpCategory = new int[] { SDataConstantsSys.BPSS_CT_BP_SUP, SDataConstantsSys.BPSS_CT_BP_CDR };
                                            break;
                                        default:
                                            bpCategory = null;
                                    }
                                }
                                
                                bizPartnerId = SBpsUtils.getBizParterIdByFiscalId(statement, fiscalId, !"".equals(country) ? fiscalForegnId : "", bpCategory);
                                if (bizPartnerId != SLibConsts.UNDEFINED) {
                                    entry.BizPartnerId = bizPartnerId;
                                }
                                else {
                                    error += "No se encontró el RFC asociado negocios '" + fiscalId + "' para el renglón " + (i + 1) + ".\n";
                                    errors++;
                                }
                            }
                            
                            if (!ocassionalFiscalId.isEmpty()) {
                                int bizPartnerOcassional = SBpsUtils.getBizParterIdByFiscalId(statement, ocassionalFiscalId, "", SLibConsts.UNDEFINED);
                                if (bizPartnerOcassional != 0) {
                                    error += "Hay asociados de negocios con el mismo RFC ocasional '" + ocassionalFiscalId + "' para el renglón " + (i + 1) + ".\n";
                                }
                                else {
                                    entry.OccasionalFiscalId = ocassionalFiscalId;
                                }
                            }
                            entry.MissingFieldsBizPartnerAndOccasionalFiscalId = entry.BizPartnerId == 0 && entry.OccasionalFiscalId.isEmpty();
                            entry.IsForegn = !country.isEmpty();
                        }

                        // Validar si se debe agregar el repositorio
                        
                        if (isAccSysBizPartnerAll) {
                            entry.Repository = repository;
                            entry.IsReferenceTax = taxAppliying.equalsIgnoreCase("Sí");
                        }

                        // Validar si se deben agregar los impuestos
                        
                        if (isAccSysTax || isAccSysPurchases) {
                            entry.IsTaxRequired = isAccSysTax;
                            if (!taxBase.isEmpty()) {
                                sql = "SELECT id_tax_bas FROM erp.finu_tax_bas WHERE tax_bas = '" + taxBase + "' AND NOT b_del;";
                                resultSet = statement.executeQuery(sql);
                                if (resultSet.next()) {
                                    taxBaseId = resultSet.getInt("id_tax_bas");
                                    sql = "SELECT id_tax_bas, id_tax FROM erp.finu_tax WHERE tax LIKE '" + taxBase + "%' " +
                                            (taxBaseId == SDataConstantsSys.FINU_TAX_BAS_VAT ? "AND vat_type = '" + vatType + "' " : "") +
                                            "AND per = " + taxRate/100 + " " + 
                                            "AND fid_tp_tax = " + (taxType.equalsIgnoreCase("retenido") ? SDataConstantsSys.FINS_TP_TAX_RET : SDataConstantsSys.FINS_TP_TAX_TRA) + " " +
                                            "AND NOT b_del;";
                                    ResultSet resultSetTax = client.getSession().getDatabase().getConnection().createStatement().executeQuery(sql);
                                    if (resultSetTax.next()) {
                                        entry.TaxKey = new int[] { resultSetTax.getInt("id_tax_bas"), resultSetTax.getInt("id_tax")};
                                    }
                                    else {
                                        error += "No se encontró el impuesto '" + taxBase + " " + taxType + " " + taxRate + "' para el renglón " + (i + 1) + ".\n";
                                        errors++;
                                    }
                                }
                                else {
                                    sql = "SELECT id_tax_bas, id_tax FROM erp.finu_tax WHERE tax = '" + taxBase + "' AND NOT b_del;";
                                    ResultSet resultSetTax = client.getSession().getDatabase().getConnection().createStatement().executeQuery(sql);
                                    if (resultSetTax.next()) {
                                        entry.TaxKey = new int[] { resultSetTax.getInt("id_tax_bas"), resultSetTax.getInt("id_tax")};
                                    }
                                    else {
                                        error += "No se encontró el Impuesto base '" + taxBase + "' para el renglón " + (i + 1) + ".\n";
                                        errors++;
                                    }
                                }
                            }
                        }
                        
                        entry.IsTaxCash = taxAppType.equalsIgnoreCase("devengado");

                        // Validar si se deben agregar entidades

                        boolean isAccSysCash = SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH, SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK });
                        boolean isAccSysInventory = mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_INV;

                        if (accMajor.getIsRequiredEntity() || isAccSysCash || isAccSysInventory) {
                            entry.IsRequiredEntity = true;
                            
                            int idCob = client.getSessionXXX().getCurrentCompanyBranchId();
                            if (idCob != 0) {
                                if (!entity.isEmpty()) {
                                    sql = "SELECT id_ent FROM erp.cfgu_cob_ent WHERE id_cob = " + idCob + " AND NOT b_del AND b_act AND code = '" + entity + "' ";
                                    switch (mnAccountSystemTypeId) {
                                        case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH:
                                            sql += "AND fid_ct_ent = 1 AND fid_tp_ent = 1";
                                            break;
                                        case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK:
                                            sql += "AND fid_ct_ent = 1 AND fid_tp_ent = 2";
                                            break;
                                        case SDataConstantsSys.FINS_TP_ACC_SYS_INV:
                                            sql += "AND fid_ct_ent = 2";
                                            break;
                                    }
                                    resultSet = statement.executeQuery(sql);
                                    if (resultSet.next()) {
                                        entry.EntityKey = new int[] { idCob, resultSet.getInt("id_ent") };
                                    }
                                    else {
                                        error += "No se encontró el Código entidad '" + entity + "' para el renglón " + (i + 1) + ".\n";
                                        errors++;
                                    }
                                }
                            }
                            else {
                                throw new Exception("No hay una sucursal seleccionada.");
                            }
                        }

                        // Validar si se debe agregar ítems

                        entry.MissingFieldItem = accMajor.getIsRequiredItem() || SLibUtilities.belongsTo(accMajor.getFkAccountSystemTypeId(), new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_PUR, SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ, SDataConstantsSys.FINS_TP_ACC_SYS_SAL, SDataConstantsSys.FINS_TP_ACC_SYS_SAL_ADJ });
                        if (accMajor.getIsRequiredItem() || isAccSysBizPartnerSupCus || isAccSysPurchases || isAccSysSales || isAccSysInventory || isAccSysTax) {
                            if (!itemKey.isEmpty()) {
                                sql = "SELECT id_item FROM erp.itmu_item WHERE NOT b_del AND item_key = '" + itemKey + "' ";
                                resultSet = statement.executeQuery(sql);
                                if (resultSet.next()) {
                                    entry.ItemKey = resultSet.getInt("id_item");
                                }
                                else {
                                    error += "No se encontró el Código ítem '" + itemKey + "' en del renglón " + (i + 1) + ".\n";
                                    errors++;
                                }

                                sql = "SELECT id_unit FROM erp.itmu_unit WHERE NOT b_del AND symbol = '" + unit + "' ";
                                resultSet = statement.executeQuery(sql);
                                if (resultSet.next()) {
                                    entry.UnitId = resultSet.getInt("id_unit");
                                }
                                else {
                                    error += "No se encontró el Código unidad '" + unit + "' para el renglón " + (i + 1) + ".\n";
                                    errors++;
                                }
                                
                                entry.FilledFieldUnits = quantity != 0;
                                entry.Quantity = quantity;
                            }
                            if (!itemAuxKey.isEmpty()) {
                                entry.FilledFieldItemAux = true;
                                sql = "SELECT id_item FROM erp.itmu_item WHERE NOT b_del AND item_key = '" + itemAuxKey + "' ";
                                resultSet = statement.executeQuery(sql);
                                if (resultSet.next()) {
                                    entry.ItemAuxKey = resultSet.getInt("id_item");
                                }
                                else {
                                    error += "No se encontró el Código ítem auxiliar '" + itemKey + "' para el renglón " + (i + 1) + ".\n";
                                    errors++;
                                }
                            }
                        }

                        // Validar si es necesario agregar documentos DPS

                        if (isAccSysBizPartnerSupCus || isAccSysPurchases || isAccSysSales || isAccSysTax || mbIsCurrentAccountDiogAccount) {
                            if (!dpsInvoice.isEmpty()) {
                                if (entry.BizPartnerId != 0) {
                                    String[] dpsInvoiceParts = dpsInvoice.split("-");
                                    if (isAccSysPurchases || isTrnSupplierTax) {
                                        sql = "SELECT id_year, id_doc FROM trn_dps WHERE NOT b_del AND fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[0] + " AND fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_DOC[1] + " "
                                                + "AND " + (dpsInvoiceParts.length == 2 ? "num_ser = '" + dpsInvoiceParts[0] + "' AND num = '" + dpsInvoiceParts[1] + "'" : " num = '" + dpsInvoiceParts[0] + "'") + " "
                                                + "AND fid_bp_r = " + entry.BizPartnerId + " " 
                                                + "ORDER BY dt DESC";
                                        resultSet = statement.executeQuery(sql);
                                        if (resultSet.last()) { // Mueve el cursor al último registro
                                            int rowCount = resultSet.getRow(); // Obtiene el número de filas
                                            if (rowCount > 1) {
                                                client.showMsgBoxInformation("Se encontró mas de un documento con el folio '" + dpsInvoice + "' para el asociado de negocion con el RFC " + (entry.IsForegn ? fiscalForegnId : fiscalId) + "\n"
                                                        + "del renglón " + (i + 1) + ", por lo que se tomo en cuenta sólo al más reciente.");
                                            } 
                                            resultSet.beforeFirst(); // Vuelve el cursor al inicio si necesitas recorrerlo después
                                        }
                                        if (resultSet.next()) {
                                            entry.DpsKey = new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc") };
                                        }
                                        else {
                                            error += "No se encontró el Folio factura (compras) '" + dpsInvoice + "' para el renglón " + (i + 1) + ".\n";
                                            errors++;
                                        }    
                                    }
                                    else {
                                        sql = "SELECT id_year, id_doc FROM trn_dps WHERE NOT b_del AND fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[0] + " AND fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_DOC[1] + " "
                                                + "AND " + (dpsInvoiceParts.length == 2 ? "num_ser = '" + dpsInvoiceParts[0] + "' AND num = '" + dpsInvoiceParts[1] + "'" : " num = '" + dpsInvoiceParts[0] + "'") + " "
                                                + "AND fid_bp_r = " + entry.BizPartnerId + " " 
                                                + "ORDER BY dt DESC";
                                        resultSet = statement.executeQuery(sql);
                                        if (resultSet.last()) { // Mueve el cursor al último registro
                                            int rowCount = resultSet.getRow(); // Obtiene el número de filas
                                            if (rowCount > 1) {
                                                client.showMsgBoxInformation("Se encontró mas de un documento con el folio " + dpsInvoice + " para el asociado de negocion con el RFC " + (entry.IsForegn ? fiscalForegnId : fiscalId) + "\n"
                                                        + "del renglón " + (i + 1) + ", por lo que se tomo en cuenta sólo al más reciente.");
                                            } 
                                            resultSet.beforeFirst(); // Vuelve el cursor al inicio si necesitas recorrerlo después
                                        }
                                        if (resultSet.next()) {
                                            entry.DpsKey = new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc") };
                                        }
                                        else {
                                            error += "No se encontró el Folio factura (ventas) '" + dpsInvoice + "' para el renglón " + (i + 1) + ".\n";
                                            errors++;
                                        }
                                    }
                                }
                                else {
                                    error += "Se indicó Folio factura pero no se indicó RFC asociado negocios para el renglón " + (i + 1) + ".\n";
                                    errors++;
                                }
                            }
                        }

                        // Validar si se debe agregar documentos DPS de ajustes

                        if (isTrnSupplierTax || isTrnCustomerTax || SLibUtilities.belongsTo(mnAccountSystemTypeId, new int[] { SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ, SDataConstantsSys.FINS_TP_ACC_SYS_SAL_ADJ })) {
                            if (!dpsInvoiceNC.isEmpty()) {
                                if (entry.BizPartnerId != 0) {
                                    String[] dpsInvoiceNCParts = dpsInvoiceNC.split("-");
                                    if (isTrnSupplierTax || mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ) {
                                        sql = "SELECT id_year, id_doc FROM trn_dps WHERE NOT b_del AND fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[0] + " AND fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_PUR_ADJ[1] + " "
                                                + "AND " + (dpsInvoiceNCParts.length == 2 ? "num_ser = '" + dpsInvoiceNCParts[0] + "' AND num = '" + dpsInvoiceNCParts[1] + "'" : " num = '" + dpsInvoiceNCParts[0] + "'") + " " 
                                                + "AND fid_bp_r = " + entry.BizPartnerId + " " 
                                                + "ORDER BY dt DESC";
                                        resultSet = statement.executeQuery(sql);
                                        if (resultSet.last()) { // Mueve el cursor al último registro
                                            int rowCount = resultSet.getRow(); // Obtiene el número de filas
                                            if (rowCount > 1) {
                                                client.showMsgBoxInformation("Se encontró mas de un documento con el folio " + dpsInvoice + " para el asociado de negocion con el RFC " + (entry.IsForegn ? fiscalForegnId : fiscalId) + "\n"
                                                        + "del renglón " + (i + 1) + ", por lo que se tomo en cuenta sólo al más reciente.");
                                            } 
                                            resultSet.beforeFirst(); // Vuelve el cursor al inicio si necesitas recorrerlo después
                                        }
                                        if (resultSet.next()) {
                                            entry.DpsKeyAdj = new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc") };
                                        }
                                        else {
                                            error += "No se encontró el Folio NC (compras) '" + dpsInvoiceNC + "' para el renglón " + (i + 1) + ".\n";
                                            errors++;                                    
                                        }
                                    }
                                    else {
                                        sql = "SELECT id_year, id_doc FROM trn_dps WHERE NOT b_del AND fid_ct_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[0] + " AND fid_cl_dps = " + SDataConstantsSys.TRNS_CL_DPS_SAL_ADJ[1] + " "
                                                    + "AND " + (dpsInvoiceNCParts.length == 2 ? "num_ser = '" + dpsInvoiceNCParts[0] + "' AND num = '" + dpsInvoiceNCParts[1] + "'" : " num = '" + dpsInvoiceNCParts[0] + "'") + " "
                                                    + "AND fid_bp_r = " + entry.BizPartnerId + " " 
                                                    + "ORDER BY dt DESC";
                                        resultSet = statement.executeQuery(sql);
                                        if (resultSet.last()) { // Mueve el cursor al último registro
                                            int rowCount = resultSet.getRow(); // Obtiene el número de filas
                                            if (rowCount > 1) {
                                                client.showMsgBoxInformation("Se encontró mas de un documento con el folio " + dpsInvoice + " para el asociado de negocion con el RFC " + (entry.IsForegn ? fiscalForegnId : fiscalId) + "\n"
                                                        + "del renglón " + (i + 1) + ", por lo que se tomo en cuenta sólo al más reciente.");
                                            } 
                                            resultSet.beforeFirst(); // Vuelve el cursor al inicio si necesitas recorrerlo después
                                        }
                                        if (resultSet.next()) {
                                            entry.DpsKeyAdj = new int[] { resultSet.getInt("id_year"), resultSet.getInt("id_doc") };
                                        }
                                        else {
                                            error += "No se encontró el Folio NC (ventas) '" + dpsInvoiceNC + "' del renglón " + (i + 1) + ".\n";
                                            errors++;
                                        }
                                    }
                                }
                                else {
                                    error += "Se indicó el folio de un documento de ajuste pero no se indicó un asociado de negocios para el renglón " + (i + 1) + ".\n";
                                    errors++;
                                }
                            }
                        }

                        // validar si se debe agregar el año

                        if (mnAccountSystemTypeId == SDataConstantsSys.FINS_TP_ACC_SYS_PROF_LOSS) {
                            entry.IsRequiredYear = true;
                            if (year < 2000 || year > 2100) {
                                error += "El Ejercicio contable esta fuera de rango para el renglón " + (i + 1) + ".\n";
                                errors++;
                            }
                            else {
                                entry.Year = year;
                            }
                        }

                        // Validar toda la información obtenida
                        
                        SFinRecordEntryValidation validation = validateRecordEntry(client, record, entry, fileColumns, SOURCE_FILE);
                        if (!validation.Message.isEmpty()) {
                            validation.Message += " Renglón " + (i + 1);
                            if (validation.ErrorId != 0) {
                                error += validation.Message + ".\n";
                                errors++;
                            }
                            else {
                                warning += (errors >= 1 ? "\n" : "") + validation.Message + ".\n";
                                warnings++;
                            }
                        }
                        maFinRecordEntries.add(entry);
                    }
                    else {
                        error += "No se encontró Cuenta contable '" + accountId + "' para el renglón " + (i + 1) + ".\n";
                        errors++;
                    }
                }
                total++;
            }
            
            // Muestra los errores y advertencias en el dialogo
            
            SDialogShowImportErrors dialog = new SDialogShowImportErrors(error, warning, errors, warnings);
            if (errors > 0) {
                dialog.setVisible(true);
            }
            else {
                int ans = 0;
                if (warnings > 0) {
                    dialog.setVisible(true);
                    ans = client.showMsgBoxConfirm("¿Desea continuar con la inserción a pesar de las advertencias?");
                }
                if (ans == JOptionPane.OK_OPTION) {
                    try {
                        for (SFinRecordEntry finEty : maFinRecordEntries) {
                            SDataRecordEntry ety = composeRecordEntry(client, null, finEty);
                            maRecordEntries.add(ety);
                        }
                        client.showMsgBoxInformation("Se importaron " + total + " partidas desde el archivo externo.");
                    }
                    catch (Exception e) {
                        client.showMsgBoxWarning("Error al ingresar las partidas, contacte a soporte.");
                    }
                }
            }
        } 
        catch (Exception e) {
            client.showMsgBoxWarning("Error al leer el archivo: " + e.getMessage() + " renglón " + (i + 1));
        }
        
        return maRecordEntries;
    }
    
    /**
     * Validar la información contenida en un objeto SFinRecordEntry
     * @param client
     * @param record
     * @param entry
     * @param names
     * @param source
     * @return SFinRecordEntryValidation
     */
    public static SFinRecordEntryValidation validateRecordEntry(SClientInterface client, SDataRecord record, SFinRecordEntry entry, HashMap<Integer, String> names, int source) {
        SFinRecordEntryValidation validation = new SFinRecordEntryValidation();
        
        String message = SDataUtilities.validateAccount(client, entry.Account, record.getDate());

        if (!message.isEmpty()) {
            validation.ErrorId = COMP_ACC;
            validation.Message = message;
        }
        else {
            // retrieve tax and check if filled account applies for DIOT:

            SDataTax tax = null;

            if (entry.TaxKey != null) {
                tax = (SDataTax) SDataUtilities.readRegistry(client, SDataConstants.FINU_TAX, entry.TaxKey, SLibConstants.EXEC_MODE_VERBOSE);
            }

            // check if filled occasional fiscal ID does not match with existing business partners:

            boolean filledFieldOccasionalFiscalId = !entry.OccasionalFiscalId.isEmpty(); // convenience variable

            if (validation.ErrorId == 0) {
                // further inpút validations:

                boolean isCheckAppliying = entry.IsCheckAppliying;
                boolean isBizPartnerRequired = entry.IsBizPartnerRequired;
                boolean isCurrentAccountDiogAccount = entry.IsCurrentAccountDiogAccount;
                boolean isTaxRequired = entry.IsTaxRequired;
                boolean isRequiredEntity = entry.IsRequiredEntity;
                boolean isRequiredYear = entry.IsRequiredYear;
                boolean missingFieldsBizPartnerAndOccasionalFiscalId = entry.MissingFieldsBizPartnerAndOccasionalFiscalId; // convenience variable
                boolean missingFieldItem = entry.MissingFieldItem; // convenience variable
                boolean filledFieldItemAux = entry.FilledFieldItemAux; // convenience variable
                boolean filledFieldUnits = entry.FilledFieldUnits; // convenience variable

                if (isBizPartnerRequired && entry.BizPartnerId == 0) {
                    message = (SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_BIZPARTNER) + "'.");
                    validation.ErrorId = COMP_BIZPARTNER;
                    validation.Message = message;
                }
                else if (entry.BizPartnerId > 0 && filledFieldOccasionalFiscalId) {
                    validation.ErrorId = COMP_BIZPARTNER;
                    validation.Message = (SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_BIZPARTNER) + "' o '" + names.get(COMP_OCCASIONAL) + "', pero no para ambos al mismo tiempo.");
                }
                else if (missingFieldsBizPartnerAndOccasionalFiscalId && tax != null) { 
                    if (source == SOURCE_FORM) {
                        if (client.showMsgBoxConfirm("¿Está seguro que desea dejar sin valor a los campos '" + names.get(COMP_BIZPARTNER) + "' y '" + names.get(COMP_OCCASIONAL) + "'?\n"
                                + "(Se seleccionó el valor '" + tax.getTax() + "' para el campo '" + names.get(COMP_TAX) + "'.)") != JOptionPane.YES_OPTION) {
                            validation.ErrorId = COMP_BIZPARTNER;
                            validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_BIZPARTNER) + "' o '" + names.get(COMP_OCCASIONAL) + "'.";
                        }
                    }
                    else {
                        validation.Message = "Se seleccionó el valor '" + tax.getTax() + "' para el campo '" + names.get(COMP_TAX) + "', mientras que los campos '" + names.get(COMP_BIZPARTNER) + "' o '" + names.get(COMP_OCCASIONAL) + "' no tienen valor por lo que se guardarán de ese modo.";
                    }
                }
                else if (missingFieldsBizPartnerAndOccasionalFiscalId && isCurrentAccountDiogAccount) {
                    if (source == SOURCE_FORM) {
                        if (client.showMsgBoxConfirm("¿Está seguro que desea dejar sin valor a los campos '" + names.get(COMP_BIZPARTNER) + "' y '" + names.get(COMP_OCCASIONAL) + "'?\n"
                            + "(La cuenta contable '" + entry.AccountId + "' aplica para la DIOT, pero si deja sin valor a estos campos, esta partida será clasificada como público en general en la DIOT.)") != JOptionPane.YES_OPTION) {
                            validation.ErrorId = COMP_BIZPARTNER;
                            validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_BIZPARTNER) + "' o '" + names.get(COMP_OCCASIONAL) + "'.";
                        }
                    }
                    else {
                        validation.Message = "La cuenta contable '" + entry.AccountId + "' aplica para la DIOT, al dejar sin valor los campos " + names.get(COMP_BIZPARTNER) + "' o '" + names.get(COMP_OCCASIONAL) + "' la partida sera clasificada como público en general en la DIOT.";
                    }
                }
                else if (isTaxRequired && entry.TaxKey == null) {
                    validation.ErrorId = (COMP_TAX);
                    validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_TAX) + "'.";
                }
                else if (isCurrentAccountDiogAccount && entry.TaxKey == null) {
                    if (source == SOURCE_FORM) {
                        if (client.showMsgBoxConfirm("¿Está seguro que desea dejar sin valor al campo '" + names.get(COMP_TAX) + "'?\n"
                                + "(La cuenta contable '" + entry.AccountId + "' aplica para la DIOT, pero si deja sin valor al campo '" + names.get(COMP_TAX) + "', esta partida será clasificada como IVA predeterminado en la DIOT.)") != JOptionPane.OK_OPTION) {
                            validation.ErrorId = COMP_TAX;
                            validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_TAX) + "'.";
                        }
                    }
                    else {
                        validation.Message = "La cuenta contable '" + entry.AccountId + "' aplica para la DIOT, pero si deja sin valor al campo '" + names.get(COMP_TAX) + "', esta partida será clasificada como IVA predeterminado en la DIOT.)";
                    }
                }
                else if (!isCurrentAccountDiogAccount && filledFieldOccasionalFiscalId && entry.TaxKey == null) {
                    if (source == SOURCE_FORM) {
                        if (client.showMsgBoxConfirm("¿Está seguro que desea dejar sin valor al campo '" + names.get(COMP_TAX) + "'?\n"
                                + "(Se capturó un valor para el campo '" + names.get(COMP_OCCASIONAL) + "', pero si deja sin valor al campo '" + names.get(COMP_TAX) + "', esta partida no será tomada en cuenta en la DIOT.)") != JOptionPane.YES_OPTION) {
                            validation.ErrorId = COMP_TAX;
                            validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_TAX) + "'.";
                        }
                    }
                    else {
                        validation.Message = "Se capturó un valor para el campo '" + names.get(COMP_OCCASIONAL) + "', pero si deja sin valor al campo '" + names.get(COMP_TAX) + "', esta partida no será tomada en cuenta en la DIOT.";
                    }
                }
                else if (isCurrentAccountDiogAccount && tax != null && tax.getVatType().isEmpty()) {
                    validation.ErrorId = COMP_TAX;
                    validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + names.get(COMP_TAX) + "'.\n"
                        + "(La cuenta contable '" + entry.AccountId + "' aplica para la DIOT, pero el impuesto tiene un problema:\n" + SDataTax.ERR_MSG_VAT_TYPE + "'" + tax.getTax() + "').";
                }
                else if (!isCurrentAccountDiogAccount && filledFieldOccasionalFiscalId && tax != null && tax.getVatType().isEmpty()) {
                    if (source == SOURCE_FORM) {
                        if (client.showMsgBoxConfirm(SDataTax.ERR_MSG_VAT_TYPE + "'" + tax.getTax() + "'. ¿Está seguro que desea dejar este valor en al campo '" + names.get(COMP_TAX) + "'?\n"
                                + "(Se capturó un valor para el campo '" + names.get(COMP_OCCASIONAL) + "', pero si no cambia el valor del campo '" + names.get(COMP_TAX) + "', esta partida no será tomada en cuenta en la DIOT.)") != JOptionPane.YES_OPTION) {
                            validation.ErrorId = COMP_TAX;
                            validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_TAX) + "'.";
                        }
                    }
                    else {
                        validation.Message = "Se capturó un valor para el campo '" + names.get(COMP_OCCASIONAL) + "', pero si no cambia el valor del campo '" + names.get(COMP_TAX) + "', esta partida no será tomada en cuenta en la DIOT.";
                    }
                }
                else if (isRequiredEntity && entry.EntityKey == null) {
                    validation.ErrorId = COMP_ENTITY;
                    validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_ENTITY) + "'.";
                }
                else if ((entry.AccountMajor.getIsRequiredItem() && entry.ItemKey == 0) && (missingFieldItem || filledFieldItemAux || filledFieldUnits)) {
                    validation.ErrorId = COMP_ITEM;
                    validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_ITEM) + "'.";
                }
                else if (filledFieldItemAux && filledFieldUnits) {
                    validation.ErrorId = COMP_UNIT;
                    validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_VALUE_NOT_REQ + "'" + names.get(COMP_UNIT) + "'.";
                }
                else if (isRequiredYear && entry.Year == 0) {
                    validation.ErrorId = COMP_YEAR;
                    validation.Message = "Año no valído.";
                }
                else if (isCheckAppliying && entry.CheckIndex <= 0) {
                    validation.ErrorId = COMP_CHECK;
                    validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_CHECK) + "'.";
                }
                else {
                    SDataDps entryDps = null; 
                    SDataDps entryDpsAdj = null; 
                    boolean isCompany = entry.BizPartnerId != 0 && entry.BizPartnerId == client.getSessionXXX().getCurrentCompany().getPkCompanyId();
                    
                    if (entry.DpsKey != null) {
                        entryDps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, entry.DpsKey, SLibConstants.EXEC_MODE_VERBOSE);
                    }
                    if (entry.DpsKeyAdj != null) {
                        entryDpsAdj = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, entry.DpsKey, SLibConstants.EXEC_MODE_VERBOSE);
                    }
                    
                    if (entryDps == null && entryDpsAdj != null) {
                        validation.ErrorId = COMP_DPS;
                        validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_DPS) + "'.";
                    }
                    else if (entryDps == null && isCurrentAccountDiogAccount && !isCompany) {
                        if (source == SOURCE_FORM) {
                            if (client.showMsgBoxConfirm("¿Está seguro que desea dejar sin valor al campo '" + names.get(COMP_DPS) + "'?") != JOptionPane.YES_OPTION) {
                                validation.ErrorId = COMP_DPS;
                                validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_DPS) + "'.";
                            }
                        }
                        else {
                            validation.Message = "Se dejará sin valor el campo '" + names.get(COMP_DPS) + "'.";
                        }
                    }
                    else if (entryDps != null && entry.CurId != entryDps.getFkCurrencyId()) {
                        validation.ErrorId = COMP_CUR;
                        validation.Message = "El valor para el campo '" + names.get(COMP_CUR) + "' debe ser: '" + entryDps.getDbmsCurrency() + "'.";
                    }
                    else if (entryDps != null && entryDpsAdj != null && entryDps.getFkCurrencyId() != entryDpsAdj.getFkCurrencyId()) {
                        validation.ErrorId = COMP_DPS;
                        validation.Message = "La moneda de los campos '" + names.get(COMP_DPS) + "' y '" + names.get(COMP_DPS_ADJ) + "' debe ser la misma.";
                    }
                    else {
                        if (entry.DebitCy == 0d && entry.CreditCy == 0d && !entry.IsExchangeDifference) {
                            if (source == SOURCE_FORM) {
                                if (client.showMsgBoxConfirm("No se ha especificado un valor para los campos '" + names.get(COMP_DEBIT_CY) + "' o '" + names.get(COMP_CREDIT_CY) + "'.\n" + SLibConstants.MSG_CNF_MSG_CONT) != JOptionPane.YES_OPTION) {
                                    validation.ErrorId = COMP_DEBIT_CY;
                                    validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_DEBIT_CY) + "' o '" + names.get(COMP_CREDIT_CY) + "'.";
                                }
                            }
                            else {
                                validation.Message = "Se dejará el valor en 0 para los campos '" + names.get(COMP_DEBIT_CY) + "' y '" + names.get(COMP_CREDIT_CY) + "'.";
                            }
                        }
                        if (validation.ErrorId == 0) {
                            if (entry.IsExchangeDifference && entry.CurId == client.getSessionXXX().getParamsErp().getFkCurrencyId()) {
                                validation.ErrorId = COMP_CUR;
                                validation.Message = "El valor para el campo '" + names.get(COMP_CUR) + "' debe ser diferente de '" + client.getSessionXXX().getParamsErp().getDbmsDataCurrency().getCurrency() + "',\n" +
                                        " debido a que está seleccionado el campo '" + names.get(COMP_EXC_DIFF) + "'.";
                            }
                            
                            if (entry.Debit == 0d && entry.Credit == 0d) {
                                if (source == SOURCE_FORM) {
                                   if (client.showMsgBoxConfirm("No se ha especificado un valor para los campos '" + names.get(COMP_DEBIT) + "' o '" + names.get(COMP_CREDIT) + "'.\n" + SLibConstants.MSG_CNF_MSG_CONT) != JOptionPane.YES_OPTION) {
                                        validation.ErrorId = COMP_DEBIT;
                                        validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_DEBIT) + "' o '" + names.get(COMP_CREDIT) + "'.";
                                    }
                                }
                                else {
                                    validation.Message = "No se ha especificado un valor para los campos '" + names.get(COMP_DEBIT) + "' o '" + names.get(COMP_CREDIT) + "', se continuará sin valor.";
                                }
                            }

                            if (validation.ErrorId == 0) {
                                if (entry.ExchangeRate == 0d && !entry.IsExchangeDifference) {
                                    validation.ErrorId = COMP_EXC_RATE;
                                    validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_EXC_RATE) + "'.";
                                }
                                else if (entry.CostCenter.isEmpty() && (
                                        entry.AccountMajor.getFkAccountTypeId_r() == SDataConstantsSys.FINS_TP_ACC_RES ||
                                        entry.AccountMajor.getDbmsIsRequiredCostCenter() ||
                                        entry.Account.getDbmsIsRequiredCostCenter())) {
                                    validation.ErrorId = COMP_CC;
                                    validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_EMPTY + "'" + names.get(COMP_CC) + "'.";
                                }
                                else {
                                    if (!entry.CostCenter.isEmpty()) {
                                        // Cost center has been specified and must be validated:
                                        SDataCostCenter costCenter = new SDataCostCenter();
                                        costCenter.read(new Object[] { entry.CostCenter}, client.getSession().getStatement());
                                        message = SDataUtilities.validateCostCenter(client, costCenter, record.getDate());

                                        if (message.length() > 0) {
                                            validation.ErrorId = COMP_CC;
                                            validation.Message = message;
                                        }
                                    }

                                    if (record.getDbmsDataAccountCash() != null) {
                                        // Record has a cash account, validate record entry currency:

                                        if (record.getDbmsDataAccountCash().getFkCurrencyId() != entry.CurId && !entry.IsExchangeDifference) {
                                            String currency = client.getSession().getSessionCustom().getCurrency(new int[] { record.getDbmsDataAccountCash().getFkCurrencyId() });

                                            if (source == SOURCE_FORM) {
                                                if (client.showMsgBoxConfirm("La moneda de la partida no coincide con " +
                                                        "la moneda de la cuenta de efectivo de la póliza contable (" + currency + ").\n" +
                                                        "¿Desea continuar?") != JOptionPane.YES_OPTION) {
                                                    validation.ErrorId = COMP_CUR;
                                                    validation.Message = "El valor para el campo '" + names.get(COMP_CUR) + "' debe ser: '" + currency + "'.";
                                                }
                                            }
                                            else {
                                                validation.Message = "La moneda de la partida no coincide con " +
                                                        "la moneda de la cuenta de efectivo de la póliza contable (" + currency + ").";
                                            }
                                        }
                                    }

                                    if (validation.ErrorId == 0) {
                                        SDataAccountCash entryAccountCash = null;
                                        if (entry.EntityKey != null) {
                                            entryAccountCash = (SDataAccountCash) SDataUtilities.readRegistry(client, SDataConstants.FIN_ACC_CASH, entry.EntityKey, SLibConstants.EXEC_MODE_SILENT);
                                        }
                                        if (entryAccountCash != null) {
                                            // Record entry has a cash account:

                                            // Validate bookeeping account:

                                            if (entryAccountCash.getFkAccountId().compareTo(entry.Account.getPkAccountIdXXX()) != 0) {
                                                if (source == SOURCE_FORM) {
                                                    if (client.showMsgBoxConfirm("La cuenta contable de esta partida no coincide con " +
                                                            "la cuenta contable de la cuenta de efectivo de la partida (" + entryAccountCash.getFkAccountId() + ").\n" +
                                                            "¿Desea continuar?") != JOptionPane.YES_OPTION) {
                                                        validation.ErrorId = COMP_ACC;
                                                        validation.Message = "El valor para el campo '" + names.get(COMP_ACC) + "' debe ser: '" + entryAccountCash.getFkAccountId() + "'.";
                                                    }
                                                }
                                                else {
                                                    validation.Message = "La cuenta contable de esta partida no coincide con " +
                                                            "la cuenta contable de la cuenta de efectivo de la partida (" + entryAccountCash.getFkAccountId() + ")";
                                                }
                                            }
                                            
                                            if (validation.ErrorId == 0) {
                                                // Validate record entry currency:

                                                if ( entryAccountCash.getFkCurrencyId() != entry.CurId && !entry.IsExchangeDifference) {
                                                    String currency = client.getSession().getSessionCustom().getCurrency(new int[] { entryAccountCash.getFkCurrencyId() });

                                                    if (source == SOURCE_FORM) {
                                                        if (client.showMsgBoxConfirm("La moneda de esta partida no coincide con " +
                                                                "la moneda de la cuenta de efectivo de la partida (" + currency + ").\n" +
                                                                "¿Desea continuar?") != JOptionPane.YES_OPTION) {
                                                            validation.ErrorId = COMP_CUR;
                                                            validation.Message = "El valor para el campo '" + names.get(COMP_CUR) + "' debe ser: '" + currency + "'.";
                                                        }
                                                    }
                                                    else {
                                                        validation.Message = "La moneda de esta partida no coincide con la moneda de la cuenta de efectivo de la partida (" + currency + ").";
                                                    }
                                                }
                                            }      
                                        }
                                    }
                                }

                                if (validation.ErrorId == 0) {
                                    // Validate than amounts in domestic and original currencies are correct:

                                    if (entry.DebitCy != 0d) {
                                        // Validate debit amount:
                                        message = SDataUtilities.validateExchangeRate(client, entry.DebitCy, entry.ExchangeRate, entry.Debit, names.get(COMP_EXC_RATE));
                                        if (message.length() > 0) {
                                            if (source == SOURCE_FORM) {
                                                if (client.showMsgBoxConfirm(message + "\n" + SLibConstants.MSG_CNF_MSG_CONT) != JOptionPane.YES_OPTION) {
                                                    validation.ErrorId = COMP_DEBIT_CY;
                                                    validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + names.get(COMP_DEBIT_CY) + "'.";
                                                }
                                            }
                                            else {
                                                validation.Message = message;
                                            }
                                        }
                                    }
                                    else {
                                        // Validate credit amount:
                                        message = SDataUtilities.validateExchangeRate(client, entry.CreditCy, entry.ExchangeRate, entry.Credit, names.get(COMP_EXC_RATE));
                                        if (message.length() > 0) {
                                            if (source == SOURCE_FORM) {
                                                if (client.showMsgBoxConfirm(message + "\n" + SLibConstants.MSG_CNF_MSG_CONT) != JOptionPane.YES_OPTION) {
                                                    validation.ErrorId = COMP_DEBIT_CY;
                                                    validation.Message = SLibConstants.MSG_ERR_GUI_FIELD_VALUE_DIF + "'" + names.get(COMP_CREDIT_CY) + "'.";
                                                }
                                            }
                                            else {
                                                validation.Message = message;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (validation.ErrorId == 0) {
                        if (entryDps != null && (isBizPartnerRequired && entry.BizPartnerId != 0)) { 
                            if (entryDps.getFkBizPartnerId_r() != entry.BizPartnerId) {
                                validation.ErrorId = COMP_BIZPARTNER;
                                validation.Message = "El asociado de negocios de la partida debe ser igual que el del documento seleccionado.";
                            }
                        }

                        if (validation.ErrorId == 0 && entryDpsAdj != null && (isBizPartnerRequired && entry.BizPartnerId != 0)) { 
                            if (entryDpsAdj.getFkBizPartnerId_r() != entry.BizPartnerId) {
                                validation.ErrorId = COMP_BIZPARTNER;
                                validation.Message = "El asociado de negocios de la partida debe ser igual que el del documento seleccionado.";
                            }
                        }
                    }
                }
            }
        }
        return validation;
    }
    
    /**
     * Obtener el tipo de cuenta de sistema.
     * @param accountMajor
     * @return 
     */
    public static int[] getSystemAccountTypeKey(SDataAccount accountMajor) {
        int[] type = null;

        if (accountMajor != null) {
            switch (accountMajor.getFkAccountSystemTypeId()) {
                case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH:
                    type = SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_CSH;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK:
                    type = SModSysConsts.FINS_TP_SYS_ACC_ENT_CSH_BNK;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_INV:
                    type = SModSysConsts.FINS_TP_SYS_ACC_ENT_WAH_WAH;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_SUP:
                    type = SModSysConsts.FINS_TP_SYS_ACC_BPR_SUP_BAL;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_CUS:
                    type = SModSysConsts.FINS_TP_SYS_ACC_BPR_CUS_BAL;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_CDR:
                    type = SModSysConsts.FINS_TP_SYS_ACC_BPR_CDR_BAL;
                    break;
                case SDataConstantsSys.FINS_TP_ACC_SYS_DBR:
                    type = SModSysConsts.FINS_TP_SYS_ACC_BPR_DBR_BAL;
                    break;
                default:
                    type = SModSysConsts.FINS_TP_SYS_ACC_NA_NA;
            }
        }

        return type;
    }
    
    /**
     * Obtener el tipo de movimiento de sistema.
     * @param accountMajor
     * @param debit
     * @param credit
     * @return 
     */
    public static int[] getSystemMoveTypeKey(SDataAccount accountMajor, double debit, double credit) {
        int[] key = null;
        boolean isDebitLike = debit > 0 || credit < 0 || debit == 0;

        if (accountMajor != null) {
            switch (accountMajor.getFkAccountSystemTypeId()) {
                case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH:
                case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK:
                    key = isDebitLike ? SModSysConsts.FINS_TP_SYS_MOV_MI_ADJ : SModSysConsts.FINS_TP_SYS_MOV_MO_ADJ;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_SUP:
                    key = isDebitLike ? SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_DEC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_SUP_BAL_INC_ADJ;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_CUS:
                    key = isDebitLike ? SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_INC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_CUS_BAL_DEC_ADJ;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_CDR:
                    key = isDebitLike ? SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_DEC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_CDR_BAL_INC_ADJ;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_DBR:
                    key = isDebitLike ? SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_INC_ADJ : SModSysConsts.FINS_TP_SYS_MOV_DBR_BAL_DEC_ADJ;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT:
                    key = SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT:
                    key = SModSysConsts.FINS_TP_SYS_MOV_JOU_CDT;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_PUR:
                    key = SModSysConsts.FINS_TP_SYS_MOV_PUR;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ:
                    key = SModSysConsts.FINS_TP_SYS_MOV_PUR_DEC;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_SAL:
                    key = SModSysConsts.FINS_TP_SYS_MOV_SAL;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_SAL_ADJ:
                    key = SModSysConsts.FINS_TP_SYS_MOV_SAL_DEC;
                    break;

                default:
                    key = isDebitLike ? SModSysConsts.FINS_TP_SYS_MOV_JOU_DBT : SModSysConsts.FINS_TP_SYS_MOV_JOU_CDT;
            }
        }

        return key;
    }
    
    /**
     * Obtener el tipo de movimiento de sistema XXX.
     * @param client
     * @param accountMajor
     * @param isTaxCash
     * @param itemKey
     * @param debit
     * @param credit
     * @return 
     */
    public static int[] getSystemMoveTypeKeyXXX(SClientInterface client, SDataAccount accountMajor, boolean isTaxCash, int[] itemKey, double debit, double credit) {
        int[] key = SDataConstantsSys.FINS_TP_SYS_MOV_NA;
        SDataItem item;

        if (accountMajor != null) {
            switch (accountMajor.getFkAccountSystemTypeId()) {
                case SDataConstantsSys.FINS_TP_ACC_SYS_ASSET_FIX:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_ASSET_ASSET;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_INV:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_ASSET_STOCK;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_CASH:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_CASH;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_CASH_BANK:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_CASH_BANK;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_SUP:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_CUS:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_CDR:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CDR;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_DBR:
                    key = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_DBR;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_TAX_DBT:
                    key = isTaxCash ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_DBT_PEND;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_TAX_CDT:
                    key = isTaxCash ? SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT : SDataConstantsSys.FINS_TP_SYS_MOV_TAX_CDT_PEND;
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_PUR:
                case SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ:
                    item = (SDataItem) SDataUtilities.readRegistry(client, SDataConstants.ITMU_ITEM, itemKey, SLibConstants.EXEC_MODE_SILENT);
                    if (item == null) {
                        key = SDataConstantsSys.FINS_TP_SYS_MOV_PUR_SERV;   // no item was provided!, by the way, it is optional.
                    }
                    else {
                        if (item.getDbmsDataItemGeneric().getFkItemCategoryId() == SDataConstantsSys.ITMS_CT_ITEM_ASS) {
                            key = SDataConstantsSys.FINS_TP_SYS_MOV_PUR_ASSET;
                        }
                        else if (SLibUtilities.belongsTo(new int[] { item.getDbmsDataItemGeneric().getFkItemCategoryId(), item.getDbmsDataItemGeneric().getFkItemClassId() },
                                new int[][] { SDataConstantsSys.ITMS_CL_ITEM_SAL_PRO, SDataConstantsSys.ITMS_CL_ITEM_PUR_CON })) {
                            key = SDataConstantsSys.FINS_TP_SYS_MOV_PUR_GOOD;
                        }
                        else {
                            key = SDataConstantsSys.FINS_TP_SYS_MOV_PUR_SERV;
                        }
                    }
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_SAL:
                case SDataConstantsSys.FINS_TP_ACC_SYS_SAL_ADJ:
                    item = (SDataItem) SDataUtilities.readRegistry(client, SDataConstants.ITMU_ITEM, itemKey, SLibConstants.EXEC_MODE_SILENT);
                    if (item == null) {
                        key = SDataConstantsSys.FINS_TP_SYS_MOV_SAL_SERV;   // no item was provided!, by the way, it is optional.
                    }
                    else {
                        if (item.getDbmsDataItemGeneric().getFkItemCategoryId() == SDataConstantsSys.ITMS_CT_ITEM_ASS) {
                            key = SDataConstantsSys.FINS_TP_SYS_MOV_SAL_ASSET;
                        }
                        else if (SLibUtilities.belongsTo(new int[] { item.getDbmsDataItemGeneric().getFkItemCategoryId(), item.getDbmsDataItemGeneric().getFkItemClassId() },
                                new int[][] { SDataConstantsSys.ITMS_CL_ITEM_SAL_PRO, SDataConstantsSys.ITMS_CL_ITEM_PUR_CON })) {
                            key = SDataConstantsSys.FINS_TP_SYS_MOV_SAL_GOOD;
                        }
                        else {
                            key = SDataConstantsSys.FINS_TP_SYS_MOV_SAL_SERV;
                        }
                    }
                    break;

                case SDataConstantsSys.FINS_TP_ACC_SYS_PROF_LOSS:
                    if (debit > 0d || credit < 0d) {
                        // Debit:

                        key = SDataConstantsSys.FINS_TP_SYS_MOV_PROF_LOSS;
                    }
                    else {
                        // Credit:

                        key = SDataConstantsSys.FINS_TP_SYS_MOV_PROF_PROF;
                    }
                    break;

                default:
            }
        }

        return key;
    }

    /**
     * Construye el objeto de la partida de una póliza contable.
     * @param client
     * @param recordEntry
     * @param entry
     * @return SDataRecordEntry
     */
    public static SDataRecordEntry composeRecordEntry(SClientInterface client, SDataRecordEntry recordEntry, SFinRecordEntry entry) {
        int[] keySystemAccountType = getSystemAccountTypeKey(entry.AccountMajor);
        int[] keySystemMoveType = getSystemMoveTypeKey(entry.AccountMajor, entry.Debit, entry.Credit);
        int[] keySystemMoveTypeXXX = getSystemMoveTypeKeyXXX(client, entry.AccountMajor, entry.IsTaxCash, new int[] { entry.ItemKey }, entry.Debit, entry.Credit);

        SDataTax tax = null;
        SDataAccountCash entryAccountCash = null;
        SDataDps dps = null;
        SDataDps dpsAdj = null;
        
        if (entry.TaxKey != null) {
            tax = (SDataTax) SDataUtilities.readRegistry(client, SDataConstants.FINU_TAX, entry.TaxKey, SLibConstants.EXEC_MODE_VERBOSE);
        }
        if (entry.EntityKey != null) {
            entryAccountCash = (SDataAccountCash) SDataUtilities.readRegistry(client, SDataConstants.FIN_ACC_CASH, entry.EntityKey, SLibConstants.EXEC_MODE_SILENT);
        }
        if (entry.DpsKey != null) {
            dps = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, entry.DpsKey, SLibConstants.EXEC_MODE_SILENT);
        }
        if (entry.DpsKeyAdj != null) {
            dpsAdj = (SDataDps) SDataUtilities.readRegistry(client, SDataConstants.TRN_DPS, entry.DpsKeyAdj, SLibConstants.EXEC_MODE_SILENT);
        }
        
        SDataCurrency currency = (SDataCurrency) SDataUtilities.readRegistry(client, SDataConstants.CFGU_CUR, new int[] { entry.CurId }, SLibConstants.EXEC_MODE_VERBOSE);
        SDataItem item = (SDataItem) SDataUtilities.readRegistry(client, SDataConstants.ITMU_ITEM, new int[] { entry.ItemKey }, SLibConstants.EXEC_MODE_SILENT);
        SDataItem itemAux = (SDataItem) SDataUtilities.readRegistry(client, SDataConstants.ITMU_ITEM, new int[] { entry.ItemAuxKey }, SLibConstants.EXEC_MODE_SILENT);
        
        if (recordEntry == null) {
            recordEntry = new SDataRecordEntry();
            recordEntry.setFkAccountingMoveTypeId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[0]);
            recordEntry.setFkAccountingMoveClassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[1]);
            recordEntry.setFkAccountingMoveSubclassId(SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL[2]);
            recordEntry.setFkUserNewId(client.getSession().getUser().getPkUserId());

            recordEntry.setDbmsAccountingMoveSubclass(SDataReadDescriptions.getCatalogueDescription(client, SDataConstants.FINS_CLS_ACC_MOV, SDataConstantsSys.FINS_CLS_ACC_MOV_JOURNAL));
        }
        else {
            recordEntry.setIsRegistryEdited(true);
            recordEntry.setFkUserEditId(client.getSession().getUser().getPkUserId());
        }

        recordEntry.setConcept(entry.Concept);
        recordEntry.setDebit(entry.Debit);
        recordEntry.setCredit(entry.Credit);
        recordEntry.setExchangeRate(entry.ExchangeRate);
        recordEntry.setExchangeRateSystem(entry.ExchangeRateSystem);
        recordEntry.setDebitCy(entry.DebitCy);
        recordEntry.setCreditCy(entry.CreditCy);
        recordEntry.setFkCurrencyId(entry.CurId);
        recordEntry.setFkAccountIdXXX(entry.AccountId);
        recordEntry.setFkCostCenterIdXXX_n(entry.CostCenter);
        recordEntry.setIsExchangeDifference(entry.IsExchangeDifference);
        recordEntry.setIsSystem(entry.IsSystem);
        recordEntry.setIsDeleted(entry.IsDeleted);

        recordEntry.setFkSystemMoveClassId(keySystemMoveType[0]);
        recordEntry.setFkSystemMoveTypeId(keySystemMoveType[1]);
        recordEntry.setFkSystemAccountClassId(keySystemAccountType[0]);
        recordEntry.setFkSystemAccountTypeId(keySystemAccountType[1]);
        recordEntry.setFkSystemMoveCategoryIdXXX(keySystemMoveTypeXXX[0]);
        recordEntry.setFkSystemMoveTypeIdXXX(keySystemMoveTypeXXX[1]);

        recordEntry.setDbmsAccount(entry.Account == null ? "" : entry.Account.getPkAccountIdXXX());
        recordEntry.setDbmsAccountComplement("");
        recordEntry.setDbmsCostCenter_n(entry.CostCenter);
        recordEntry.setDbmsCurrencyKey(currency.getKey());

        if (entry.BizPartnerId > 0) {
            SDataBizPartner bp = (SDataBizPartner) SDataUtilities.readRegistry(client, SDataConstants.BPSU_BP, new int[] { entry.BizPartnerId }, SLibConstants.EXEC_MODE_VERBOSE);
            recordEntry.setFkBizPartnerId_nr(entry.BizPartnerId);
            recordEntry.setFkBizPartnerBranchId_n(bp.getDbmsBizPartnerBranchHq().getPkBizPartnerBranchId());
            recordEntry.setDbmsBizPartnerCode(bp.getPkBizPartnerId() + "");
            recordEntry.setDbmsBizPartner(bp.getBizPartner());
        }
        else {
            recordEntry.setFkBizPartnerId_nr(0);
            recordEntry.setFkBizPartnerBranchId_n(0);
            recordEntry.setDbmsBizPartnerCode("");
            recordEntry.setDbmsBizPartner("");
        }
        
        if (!entry.OccasionalFiscalId.isEmpty()) {
            recordEntry.setOccasionalFiscalId(entry.OccasionalFiscalId);
        }
        else {
            recordEntry.setOccasionalFiscalId("");
        }

        if (!entry.Repository.isEmpty()) {
            recordEntry.setReference(entry.Repository);
            recordEntry.setIsReferenceTax(entry.IsReferenceTax);
        }
        else {
            recordEntry.setReference("");
            recordEntry.setIsReferenceTax(false);
        }

        if (entry.TaxKey != null) {
            recordEntry.setFkTaxBasicId_n(entry.TaxKey[0]);
            recordEntry.setFkTaxId_n(entry.TaxKey[1]);
            recordEntry.setDbmsTax(tax != null ? tax.getTax() : "");
        }
        else {
            recordEntry.setFkTaxBasicId_n(0);
            recordEntry.setFkTaxId_n(0);
            recordEntry.setDbmsTax("");
        }

        if (entry.EntityKey != null) {
            recordEntry.setFkCompanyBranchId_n(entry.EntityKey[0]);
            recordEntry.setFkEntityId_n(entry.EntityKey[1]);
            recordEntry.setDbmsEntityCode(entryAccountCash != null ? entryAccountCash.getAuxCode(): "");
            recordEntry.setDbmsEntity(entryAccountCash != null ? entryAccountCash.getAuxEntity(): "");
        }
        else {
            recordEntry.setFkCompanyBranchId_n(0);
            recordEntry.setFkEntityId_n(0);
            recordEntry.setDbmsEntityCode("");
            recordEntry.setDbmsEntity("");
        }

        if (entry.ItemKey > 0) {
            recordEntry.setFkItemId_n(item.getPkItemId());
            recordEntry.setDbmsItemCode(item.getCode());
            recordEntry.setDbmsItem(item.getItem());
            
            if (entry.ItemAuxKey > 0) {
                recordEntry.setUnits(0);
                recordEntry.setFkUnitId_n(SModSysConsts.ITMU_UNIT_NA);
                recordEntry.setFkItemAuxId_n(entry.ItemAuxKey);
                recordEntry.setDbmsItemAuxCode(itemAux.getCode());
                recordEntry.setDbmsItemAux(itemAux.getItem());
            }
            else {
                recordEntry.setUnits(entry.Quantity);
                recordEntry.setFkUnitId_n(item.getFkUnitId());
                recordEntry.setFkItemAuxId_n(SLibConstants.UNDEFINED);
                recordEntry.setDbmsItemAuxCode("");
                recordEntry.setDbmsItemAux("");
            }

            recordEntry.setDbmsAccountComplement(item.getItem());
        }
        else {
            recordEntry.setUnits(0);
            recordEntry.setFkItemId_n(0);
            recordEntry.setFkUnitId_n(0);
            recordEntry.setFkItemAuxId_n(0);
            recordEntry.setDbmsItemCode("");
            recordEntry.setDbmsItem("");
            recordEntry.setDbmsItemAuxCode("");
            recordEntry.setDbmsItemAux("");
            
            recordEntry.setDbmsAccountComplement("");
        }

        recordEntry.setFkYearId_n(entry.Year);

        if (!entry.IsCheckAppliying) {
            recordEntry.setFkCheckWalletId_n(0);
            recordEntry.setFkCheckId_n(0);
            recordEntry.setAuxCheckNumber(0);
        }
        /* Se deja comentado para mantener la consistencia en las validaciones, se en realidad se valida en la forma de captura convencional
        else {
            recordEntry.setFkCheckWalletId_n(moFieldFkCheckId_n.getKeyAsIntArray()[0]);
            recordEntry.setFkCheckId_n(moFieldFkCheckId_n.getKeyAsIntArray()[1]);
            recordEntry.setAuxCheckNumber((Integer) ((SFormComponentItem) jcbFkCheckId_n.getSelectedItem()).getComplement());
        }
        */
        
        if (entry.DpsKey != null) {
            recordEntry.setFkDpsYearId_n(dps.getPkYearId());
            recordEntry.setFkDpsDocId_n(dps.getPkDocId());
            recordEntry.setDbmsDps(dps.getDpsNumber());
        }
        else {
            recordEntry.setFkDpsYearId_n(0);
            recordEntry.setFkDpsDocId_n(0);
            recordEntry.setDbmsDps("");
        }

        if (entry.DpsKeyAdj != null) {
            recordEntry.setFkDpsAdjustmentYearId_n(dpsAdj.getPkYearId());
            recordEntry.setFkDpsAdjustmentDocId_n(dpsAdj.getPkDocId());
        }
        else {
            recordEntry.setFkDpsAdjustmentYearId_n(0);
            recordEntry.setFkDpsAdjustmentDocId_n(0);
        }
        
        return recordEntry;
    }
}

class SFinRecordEntry {

    public String AccountId;
    public String Concept;
    public double Debit;
    public double Credit;
    public double ExchangeRate;
    public double ExchangeRateSystem;
    public double DebitCy;
    public double CreditCy;
    public int CurId;
    public boolean IsExchangeDifference;
    public String CostCenter;
    public int BizPartnerId;
    public String OccasionalFiscalId;
    public boolean IsForegn;
    public String Repository;
    public boolean IsReferenceTax;
    public int[] TaxKey;
    public int[] EntityKey;
    public int ItemKey;
    public int ItemAuxKey;
    public double Quantity;
    public int UnitId;
    public int[] DpsKey;
    public int[] DpsKeyAdj;
    public int Year;
    public int CheckIndex;
    public boolean IsTaxCash;

    public boolean IsSystem;
    public boolean IsDeleted;

    public SDataAccount Account;
    public SDataAccount AccountMajor;

    public boolean IsCheckAppliying;
    public boolean IsBizPartnerRequired;
    public boolean IsCurrentAccountDiogAccount;
    public boolean IsTaxRequired;
    public boolean IsRequiredEntity;
    public boolean IsRequiredYear;
    public boolean MissingFieldsBizPartnerAndOccasionalFiscalId;
    public boolean MissingFieldItem;
    public boolean FilledFieldItemAux;
    public boolean FilledFieldUnits;

    public SFinRecordEntry() {
        AccountId = "";
        Concept = "";
        Debit = 0.0;
        Credit = 0.0;
        ExchangeRate = 0.0;
        ExchangeRateSystem = 0.0;
        DebitCy = 0.0;
        CreditCy = 0.0;
        CurId = 0;
        IsExchangeDifference = false;
        CostCenter = "";
        BizPartnerId = 0;
        OccasionalFiscalId = "";
        IsForegn = false;
        Repository = "";
        IsReferenceTax = false;
        TaxKey = null;
        EntityKey = null;
        ItemKey = 0;
        ItemAuxKey = 0;
        Quantity = 0.0;
        UnitId = 0;
        DpsKey = null;
        DpsKeyAdj = null;
        Year = 0;
        CheckIndex = 0;
        IsTaxCash = false;

        IsSystem = false;
        IsDeleted = false;

        Account = null;
        AccountMajor = null;

        IsCheckAppliying = false;
        IsBizPartnerRequired = false;
        IsCurrentAccountDiogAccount = false;
        IsTaxRequired = false;
        IsRequiredEntity = false;
        IsRequiredYear = false;
        MissingFieldsBizPartnerAndOccasionalFiscalId = false;
        MissingFieldItem = false;
        FilledFieldItemAux = false;
        FilledFieldUnits = false;
    }
}

class SFinRecordEntryValidation {
    
    int ErrorId;
    String Message;

    public SFinRecordEntryValidation() {
        ErrorId = 0;
        Message = "";
    }
}

