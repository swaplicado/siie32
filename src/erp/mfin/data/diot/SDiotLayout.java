package erp.mfin.data.diot;

import cfd.DCfdConsts;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataTax;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SDiotLayout {
    
    public static final int FORMAT_PIPE = 1; // pipe separated values
    public static final int FORMAT_CSV = 2; // comma separated values
    
    protected SClientInterface miClient;
    protected Date mtStart;
    protected Date mtEnd;
    protected HashMap<String, SDataAccount> moAccounts; // key: code of account
    protected HashMap<String, SDataAccount> moMajorAccounts; // key: code of major account
    protected HashMap<String, SDataTax> moVats; // key: 'basic tax ID' + "-" + 'tax ID'
    protected HashMap<String, SDiotTercero> moTerceros; // key: 'business partner ID' + "-" + 'tipo de operación DIOT'
    protected String[] maDiotAccountCodes;
    
    public SDiotLayout(erp.client.SClientInterface client, Date start, Date end) throws Exception {
        miClient = client;
        mtStart = start;
        mtEnd = end;
        moAccounts = new HashMap<>();
        moMajorAccounts = new HashMap<>();
        moVats = new HashMap<>();
        moTerceros = new HashMap<>();
        maDiotAccountCodes = SDiotUtils.getDiotAccounts(miClient.getSession().getStatement());
    }
    
    private SDataAccount getAccount(final String accountCode) {
        return moAccounts.get(accountCode);
    }
    
    private void addAccount(final SDataAccount account) throws Exception {
        if (account == null) {
            throw new Exception("Se debe proporcionar una cuenta contable.");
        }
        
        if (getAccount(account.getCode()) == null) {
            moAccounts.put(account.getCode(), account);
        }
    }
    
    private SDataAccount getMajorAccount(final String accountCode) {
        return moMajorAccounts.get(accountCode);
    }
    
    private void addMajorAccount(final SDataAccount account) throws Exception {
        if (account == null) {
            throw new Exception("Se debe proporcionar una cuenta contable de mayor.");
        }
        
        if (getMajorAccount(account.getCode()) == null) {
            moMajorAccounts.put(account.getCode(), account);
        }
    }
    
    private SDataTax getVat(final int[] vatPk) {
        return moVats.get(SLibUtils.textKey(vatPk));
    }
    
    private void addVat(final SDataTax tax) throws Exception {
        if (tax == null) {
            throw new Exception("Se debe proporcionar un impuesto.");
        }
        
        if (getVat((int[]) tax.getPrimaryKey()) == null) {
            if (tax.getVatType().isEmpty()) {
                throw new Exception(SDataTax.ERR_MSG_VAT_TYPE + "'" + tax.getTax() + "'.");
            }
            moVats.put(SLibUtils.textKey((int[]) tax.getPrimaryKey()), tax);
        }
    }
    
    private double getTaxedAmount(final Statement statement, final int[] bkkNumberKey) throws Exception {
        double amount = 0;
        
        String sql = "SELECT SUM(re.debit) "
                + "FROM fin_rec AS r "
                + "INNER JOIN fin_rec_ety AS re ON "
                + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                + "WHERE NOT r.b_del AND NOT re.b_del AND "
                + "re.fid_bkk_year_n = " + bkkNumberKey[0] + " AND re.fid_bkk_num_n = " + bkkNumberKey[1] + " AND "
                + "re.fid_ct_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[0] + " AND re.fid_tp_sys_mov_xxx = " + SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP[1] + ";";

        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            amount = resultSet.getDouble(1);
        }
        
        return amount;
    }
    
    private String composeFinRecEty(final ResultSet resultSet, final String alias) throws Exception {
        return "Renglón póliza: " + 
                resultSet.getInt(alias + ".id_year") + "-" + 
                resultSet.getInt(alias + ".id_per") + "-" + 
                resultSet.getInt(alias + ".id_bkc") + "-" + 
                resultSet.getString(alias + ".id_tp_rec") + "-" + 
                resultSet.getInt(alias + ".id_num") + "-" +
                resultSet.getInt(alias + ".id_ety");
    }
    
    public String getLayout(final int format, final boolean excludeTotalZeros) throws Exception {
        int entriesTotal = 0;
        int entriesWithoutTax = 0;
        int entriesWithoutBizPartner = 0;
        int entriesForCompany = 0;
        int entriesNonZeroTaxButZero = 0;
        int entriesZeroTaxButNonZero = 0;
        int totalZeros = 0;
        int[] vatDefaultPk = SDiotUtils.getDiotVatDefaultPk(miClient.getSession().getStatement());
        double debit = 0;
        double credit = 0;
        double debitTotal = 0;
        double creditTotal = 0;
        String warnings = "";
        Statement statement = miClient.getSession().getStatement().getConnection().createStatement();
        Statement statementAux = miClient.getSession().getStatement().getConnection().createStatement();
        HashMap<SDataTax, Double> vatSettlements = new HashMap<>();
        HashMap<SDataTax, Double> vatWithhelds = new HashMap<>();
        HashMap<SDataTax, Double> vatWithheldSettlements = new HashMap<>();
        
        moTerceros.clear();
        
        // iterate through all DIOT accounts:
        
        for (String diotAccountCode : maDiotAccountCodes) {
            String sql = "SELECT re.* "
                    + "FROM fin_rec AS r "
                    + "INNER JOIN fin_rec_ety AS re ON "
                    + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                    + "WHERE NOT r.b_del AND NOT re.b_del AND "
                    + "r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtEnd) + "' AND "
                    + "re.fid_acc LIKE '" + diotAccountCode + "%';";
            
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                entriesTotal++;
                debit = resultSet.getDouble("re.debit");
                credit = resultSet.getDouble("re.credit");
                debitTotal = SLibUtils.roundAmount(debitTotal + debit);
                creditTotal = SLibUtils.roundAmount(creditTotal + credit);
        
                // get account:
                
                SDataAccount account = getAccount(resultSet.getString("re.fid_acc"));
                
                if (account == null) {
                    account = (SDataAccount) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC, new Object[] { resultSet.getString("re.fid_acc") }, SLibConstants.EXEC_MODE_VERBOSE);
                    addAccount(account);
                }
                
                SDataAccount majorAccount = getMajorAccount(account.getDbmsPkAccountMajorId());
                
                if (majorAccount == null) {
                    majorAccount = (SDataAccount) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC, new Object[] { account.getDbmsPkAccountMajorId() }, SLibConstants.EXEC_MODE_VERBOSE);
                    addMajorAccount(majorAccount);
                }
                
                // get VAT:
                
                int[] vatPk = null;
                
                if (resultSet.getInt("re.fid_tax_bas_n") != 0 && resultSet.getInt("re.fid_tax_n") != 0) {
                    vatPk = new int[] { resultSet.getInt("re.fid_tax_bas_n"), resultSet.getInt("re.fid_tax_n") };
                }
                else {
                    entriesWithoutTax++;
                    vatPk = vatDefaultPk;
                }
                
                SDataTax vat = getVat(vatPk);
                    
                if (vat == null) {
                    vat = (SDataTax) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_TAX, vatPk, SLibConstants.EXEC_MODE_VERBOSE);
                    addVat(vat);
                }
                
                // get business partner directly through ID, if any:
                
                SDataBizPartner bizPartner = null;
                
                if (resultSet.getInt("re.fid_bp_nr") != 0) {
                    bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { resultSet.getInt("re.fid_bp_nr") }, SLibConstants.EXEC_MODE_VERBOSE);
                }
                
                // get business partner indirectly through DPS, if any:
                
                if (bizPartner == null && resultSet.getInt("re.fid_dps_year_n") != 0 && resultSet.getInt("re.fid_dps_doc_n") != 0) {
                    sql = "SELECT fid_bp_r FROM trn_dps WHERE id_year = " + resultSet.getInt("re.fid_dps_year_n") + " AND id_doc = " + resultSet.getInt("re.fid_dps_doc_n") + ";";
                    ResultSet resultSetAux = statementAux.executeQuery(sql);
                    if (resultSetAux.next()) {
                        bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { resultSetAux.getInt(1) }, SLibConstants.EXEC_MODE_VERBOSE);
                    }
                }
                
                // get business partner (factoring bank) indirectly through CFD, if any:
                
                if (bizPartner == null && resultSet.getInt("re.fid_cfd_n") != 0) {
                    sql = "SELECT fid_fact_bank_n FROM trn_cfd WHERE id_cfd = " + resultSet.getInt("re.fid_cfd_n") + ";";
                    ResultSet resultSetAux = statementAux.executeQuery(sql);
                    if (resultSetAux.next()) {
                        bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { resultSetAux.getInt(1) }, SLibConstants.EXEC_MODE_VERBOSE);
                    }
                }
                
                // process DIOT Tercero:
                
                boolean isEntryForCompany = false;
                
                if (bizPartner == null) {
                    entriesWithoutBizPartner++;
                }
                else if (bizPartner.getPkBizPartnerId() == miClient.getSessionXXX().getCurrentCompany().getPkCompanyId()) {
                    isEntryForCompany = true;
                    entriesForCompany++;
                }
                
                SDiotTercero tercero = null;
                
                if (bizPartner == null) {
                    tercero = moTerceros.get(SDiotTercero.GLOBAL_CLAVE);
                }
                else {
                    tercero = moTerceros.get(bizPartner.getDiotTerceroClave());
                }
                
                if (tercero == null) {
                    if (bizPartner == null) {
                        tercero = new SDiotTercero(false, 0, SDiotConsts.THIRD_GLOBAL, SDiotConsts.OPER_OTHER, DCfdConsts.RFC_GEN_NAC, "");
                        moTerceros.put(SDiotTercero.GLOBAL_CLAVE, tercero);
                    }
                    else {
                        String tipoTercero;
                        String tipoOperación;
                        
                        if (isEntryForCompany) {
                            tipoTercero = SDiotConsts.THIRD_UNDEFINED;
                            tipoOperación = SDiotConsts.OPER_UNDEFINED;
                        }
                        else {
                            tipoTercero = bizPartner.getDiotTipoTercero(miClient);
                            tipoOperación = bizPartner.getDiotTipoOperación();
                        }
                        
                        tercero = new SDiotTercero(isEntryForCompany, bizPartner.getPkBizPartnerId(), tipoTercero, tipoOperación, bizPartner.getFiscalId(), bizPartner.getFiscalFrgId());
                        moTerceros.put(bizPartner.getDiotTerceroClave(), tercero);
                    }
                }
                
                boolean isAsset = SLibUtils.compareKeys(majorAccount.getAccountClassKey(), SDataConstantsSys.FINS_CL_ACC_ASSET);
                boolean isLiability = SLibUtils.compareKeys(majorAccount.getAccountClassKey(), SDataConstantsSys.FINS_CL_ACC_LIABTY);
                
                if (!isAsset && !isLiability) {
                    throw new Exception(composeFinRecEty(resultSet, "re") + ": "
                            + "La cuenta contable de mayor " + majorAccount.getCode() + ", '" + majorAccount.getAccount() + "', no es de balance.");
                }
                
                boolean isDomesticBizPartner = bizPartner == null ? true : bizPartner.isDomestic(miClient);
                
                if (!isDomesticBizPartner) {
                    tercero.ExtNombre = bizPartner.getBizPartner();
                    tercero.ExtPaísResidencia = bizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial().getDbmsDataCountry().getDiotCode();
                    tercero.ExtNacionalidad = tercero.ExtPaísResidencia;
                }
                
                String warning;
                double vatAmount = SLibUtils.roundAmount(Math.abs(debit + credit));
                
                if (isAsset) {
                    // VAT creditable:
                    
                    if (debit > 0 || credit < 0 || (debit == 0 && credit == 0)) {
                        // VAT creditable of payments of purchases:
                        
                        if (debit == 0 && credit == 0) {
                            double taxedAmount = getTaxedAmount(statementAux, new int[] { resultSet.getInt("re.fid_bkk_year_n"), resultSet.getInt("re.fid_bkk_num_n") });
                            
                            switch (vat.getVatType()) {
                                case SDiotConsts.VAT_TYPE_EXEMPT:
                                    if (isDomesticBizPartner) {
                                        tercero.ValorPagosImpIvaExento = SLibUtils.roundAmount(tercero.ValorPagosImpIvaExento + taxedAmount);
                                    }
                                    else {
                                        tercero.ValorPagosNacIvaExento = SLibUtils.roundAmount(tercero.ValorPagosNacIvaExento + taxedAmount);
                                    }
                                    break;

                                case SDiotConsts.VAT_TYPE_RATE_0:
                                    tercero.ValorPagosNacIva0 = SLibUtils.roundAmount(tercero.ValorPagosNacIva0 + taxedAmount);
                                    break;

                                default:
                                    entriesNonZeroTaxButZero++;
                                    
                                    warning = composeFinRecEty(resultSet, "re") + ": "
                                            + "Impuesto '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "', con asiento de $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + "."; 
                                    warnings += "\"" + warning + "\"\n";
                                    System.out.println(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION + "\n" + warning);
                            }
                        }
                        else {
                            switch (vat.getVatType()) {
                                case SDiotConsts.VAT_TYPE_GENERAL:
                                    if (isDomesticBizPartner) {
                                        tercero.ValorPagosNacIva1516 = SLibUtils.roundAmount(tercero.ValorPagosNacIva1516 + (vat.getPercentage() == 0 ? 0 : vatAmount / vat.getPercentage()));
                                    }
                                    else {
                                        tercero.ValorPagosImpIva1516 = SLibUtils.roundAmount(tercero.ValorPagosImpIva1516 + (vat.getPercentage() == 0 ? 0 : vatAmount / vat.getPercentage()));
                                    }
                                    break;

                                case SDiotConsts.VAT_TYPE_BORDER:
                                    if (isDomesticBizPartner) {
                                        tercero.ValorPagosNacIva1011 = SLibUtils.roundAmount(tercero.ValorPagosNacIva1011 + (vat.getPercentage() == 0 ? 0 : vatAmount / vat.getPercentage()));
                                    }
                                    else {
                                        tercero.ValorPagosImpIva1011 = SLibUtils.roundAmount(tercero.ValorPagosImpIva1011 + (vat.getPercentage() == 0 ? 0 : vatAmount / vat.getPercentage()));
                                    }
                                    break;

                                case SDiotConsts.VAT_TYPE_BORDER_INC:
                                    tercero.ValorPagosNacIvaEstFront = SLibUtils.roundAmount(tercero.ValorPagosNacIvaEstFront + (vat.getPercentage() == 0 ? 0 : vatAmount / vat.getPercentage()));
                                    break;

                                default:
                                    entriesZeroTaxButNonZero++;
                                    
                                    warning = composeFinRecEty(resultSet, "re") + ": "
                                            + "Impuesto '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "', con asiento de $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + "."; 
                                    warnings += "\"" + warning + "\"\n";
                                    System.out.println(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION + "\n" + warning);
                            }
                        }
                    }
                    else if (debit < 0 || credit > 0) {
                        // VAT creditable of purchases adjustments & VAT creditable settlements:
                        
                        if (isEntryForCompany) {
                            // VAT creditable settlement:
                            
                            Double settlement = vatSettlements.get(vat);
                            
                            if (settlement == null) {
                                vatSettlements.put(vat, vatAmount);
                            }
                            else {
                                vatSettlements.put(vat, SLibUtils.roundAmount(settlement + vatAmount));
                            }
                        }
                        else {
                            // VAT creditable of purchases adjustment:
                            
                            tercero.IvaNotasCréditoCompras = SLibUtils.roundAmount(tercero.IvaNotasCréditoCompras + vatAmount);
                        }
                    }
                }
                else {
                    // VAT creditable withheld:
                    
                    if (debit < 0 || credit > 0 || (debit == 0 && credit == 0)) {
                        // VAT creditable withheld of payments of purchases:
                        
                        tercero.IvaRetenido = SLibUtils.roundAmount(tercero.IvaRetenido + vatAmount);
                        
                        // preserve withheld:
                        
                        Double withheld = vatWithhelds.get(vat);
                        
                        if (withheld == null) {
                            vatWithhelds.put(vat, vatAmount);
                        }
                        else {
                            vatWithhelds.put(vat, SLibUtils.roundAmount(withheld + vatAmount));
                        }
                    }
                    else if (debit > 0 || credit < 0) {
                        // VAT creditable withheld of purchases adjustments & VAT creditable withheld settlements:
                        
                        if (isEntryForCompany) {
                            // VAT creditable withheld settlement:
                            
                            Double withheldSettlement = vatWithheldSettlements.get(vat);
                            
                            if (withheldSettlement == null) {
                                vatWithheldSettlements.put(vat, vatAmount);
                            }
                            else {
                                vatWithheldSettlements.put(vat, SLibUtils.roundAmount(withheldSettlement + vatAmount));
                            }
                        }
                        else {
                            // VAT creditable withheld of purchases adjustment:
                            
                            tercero.IvaRetenido = SLibUtils.roundAmount(tercero.IvaRetenido - vatAmount);
                            
                            // preserve withheld adjustment:
                        
                            Double withheld = vatWithhelds.get(vat);

                            if (withheld == null) {
                                vatWithhelds.put(vat, vatAmount);
                            }
                            else {
                                vatWithhelds.put(vat, SLibUtils.roundAmount(withheld - vatAmount));
                            }
                        }
                    }
                }
                
                // identify business partner by CFDI:
                // identify business partner by ID of business partner:
                // process non identifiable business partner:
            }
        }

        String layout = "";
        SDiotTercero totales = null;
        
        if (format == FORMAT_CSV) {
            totales = new SDiotTercero();
            
            layout += "\"" + miClient.getSessionXXX().getCurrentCompanyName() + "\"\n";
            layout += "\"Layout DIOT\"\n";
            layout += "\"Del " + SLibUtils.DateFormatDate.format(mtStart) + " al " + SLibUtils.DateFormatDate.format(mtEnd) + "\"\n";
            layout += SDiotTercero.getLayoutRowHeadings() + "\n";
            layout += "\n";
        }
        
        for (SDiotTercero tercero : moTerceros.values()) {
            boolean isTotalZero = tercero.isTotalZero();
            
            if (!tercero.IsCompany && (!excludeTotalZeros || !isTotalZero)) {
                layout += tercero.getLayoutRow(format) + "\n";

                if (format == FORMAT_CSV) {
                    totales.addTercero(tercero);
                }
            }
            
            if (isTotalZero) {
                totalZeros++;
            }
        }
        
        if (format == FORMAT_CSV) {
            DecimalFormat amountFormat = new DecimalFormat("0.00");
            
            layout += "\n";
            layout += "\"Totales:\"\n";
            layout += totales.getLayoutRow(format) + "\n";
            
            SDataTax vatDefault = getVat(vatDefaultPk);
            
            layout += "\n";
            layout += "\"Resumen:\"\n";
            layout += "\"renglones de pólizas contables procesados:\"," + entriesTotal + "\n";
            layout += "\"renglones de pólizas contables sin impuesto:\"," + entriesWithoutTax + ",\"(asignadas el impuesto predeterminado para DIOT: " + vatDefault.getTax() + ")\"\n";
            layout += "\"renglones de pólizas contables sin asociado de negocios:\"," + entriesWithoutBizPartner + ",\"(asignados al tercero global de este layout)\"\n";
            layout += "\"renglones de pólizas contables de la empresa '" + miClient.getSessionXXX().getCurrentCompanyName() + "':\"," + entriesForCompany + ",\"(excluidos de este layout)\"\n";
            layout += "\"renglones de pólizas contables de impuestos distintos a cero, pero iguales a cero:\"," + entriesNonZeroTaxButZero + ",\"(excluidos de este layout)\"\n";
            layout += "\"renglones de pólizas contables de impuestos similares a cero, pero distintos de cero:\"," + entriesZeroTaxButNonZero + ",\"(excluidos de este layout)\"\n";
            layout += "\"terceros totalmente en cero:\"," + entriesZeroTaxButNonZero + ",\"" + (excludeTotalZeros ? "(excluídos de este layout)" : "(incluidos en este layout)") + "\"\n";

            layout += "\n";
            layout += "\"Liquidación de impuestos:\"\n";
            for (SDataTax vat : vatSettlements.keySet()) {
                Double amount = vatSettlements.get(vat);
                layout += "\"impuesto:\",\"" + vat.getTax() + "\",\"tasa:\"," + SLibUtils.getDecimalFormatPercentageTax().format(vat.getPercentage()) + ",\"monto:\"," + amountFormat.format(amount) + "\n";
            }

            layout += "\n";
            layout += "\"Retención de impuestos:\"\n";
            for (SDataTax vat : vatWithhelds.keySet()) {
                Double amount = vatWithhelds.get(vat);
                layout += "\"impuesto:\",\"" + vat.getTax() + "\",\"tasa:\"," + SLibUtils.getDecimalFormatPercentageTax().format(vat.getPercentage()) + ",\"monto:\"," + amountFormat.format(amount) + "\n";
            }

            layout += "\n";
            layout += "\"Liquidación de retención de impuestos:\"\n";
            for (SDataTax vat : vatWithheldSettlements.keySet()) {
                Double amount = vatWithheldSettlements.get(vat);
                layout += "\"impuesto:\",\"" + vat.getTax() + "\",\"tasa:\"," + SLibUtils.getDecimalFormatPercentageTax().format(vat.getPercentage()) + ",\"monto:\"," + amountFormat.format(amount) + "\n";
            }

            layout += "\n";
            layout += "\"Advertencias:\"\n";
            layout += warnings.isEmpty() ? "\"Ninguna.\"\n" : warnings; 
        }
        
        return SLibUtils.textToAscii(layout);
    }
}
