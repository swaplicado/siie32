package erp.mfin.data.diot;

import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mfin.data.SDataAccount;
import erp.mfin.data.SDataTax;
import erp.mtrn.data.SDataDps;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SDiotLayout {
    
    public static final int FORMAT_PIPE = 1; // pipe separated values
    public static final int FORMAT_CSV = 2; // comma separated values
    public static final double AMOUNT_DIFF_ALLOWANCE = 0.1;
    
    protected SClientInterface miClient;
    protected Date mtStart;
    protected Date mtEnd;
    protected HashMap<String, SDataAccount> moAccounts; // key: code of account
    protected HashMap<String, SDataAccount> moMajorAccounts; // key: code of major account
    protected HashMap<String, SDataTax> moVats; // key: 'basic tax ID' + "-" + 'tax ID'
    protected HashMap<Integer, SDataBizPartner> moBizPartners; // key: ID of business partner
    protected HashMap<String, SDiotTercero> moTerceros; // key: 'business partner ID' + "-" + 'tipo de operación DIOT'
    protected String[] masDiotAccountCodes;
    
    public SDiotLayout(erp.client.SClientInterface client, Date start, Date end) throws Exception {
        miClient = client;
        mtStart = start;
        mtEnd = end;
        moAccounts = new HashMap<>();
        moMajorAccounts = new HashMap<>();
        moVats = new HashMap<>();
        moBizPartners = new HashMap<>();
        moTerceros = new HashMap<>();
        masDiotAccountCodes = SDiotUtils.getDiotAccounts(miClient.getSession().getStatement());
    }
    
    private SDataAccount getAccount(final String accountCodeUser) {
        return moAccounts.get(accountCodeUser);
    }
    
    private void addAccount(final SDataAccount account) throws Exception {
        if (account == null) {
            throw new Exception("Se debe proporcionar una cuenta contable.");
        }
        
        if (getAccount(account.getPkAccountIdXXX()) == null) {
            moAccounts.put(account.getPkAccountIdXXX(), account);
        }
    }
    
    private SDataAccount getMajorAccount(final String accountCodeUser) {
        return moMajorAccounts.get(accountCodeUser);
    }
    
    private void addMajorAccount(final SDataAccount account) throws Exception {
        if (account == null) {
            throw new Exception("Se debe proporcionar una cuenta contable de mayor.");
        }
        
        if (getMajorAccount(account.getPkAccountIdXXX()) == null) {
            moMajorAccounts.put(account.getPkAccountIdXXX(), account);
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
    
    private SDataBizPartner getBizPartner(final int bizPartnerId) {
        return moBizPartners.get(bizPartnerId);
    }
    
    private void addBizPartner(final SDataBizPartner bizPartner) throws Exception {
        if (bizPartner == null) {
            throw new Exception("Se debe proporcionar un asociado de negocios.");
        }
        
        if (getBizPartner(bizPartner.getPkBizPartnerId()) == null) {
            moBizPartners.put(bizPartner.getPkBizPartnerId(), bizPartner);
        }
    }
    
    private Object[] createFinRecordKey(final ResultSet resultSet, final String tableAlias) throws Exception {
        return new Object[] { 
            resultSet.getInt(tableAlias + ".id_year"),
            resultSet.getInt(tableAlias + ".id_per"),
            resultSet.getInt(tableAlias + ".id_bkc"),
            resultSet.getString(tableAlias + ".id_tp_rec"),
            resultSet.getInt(tableAlias + ".id_num")
        };
    }
    
    private String composeFinRecordEntry(final ResultSet resultSet, final String tableAlias, final SDataAccount account, final SDataBizPartner bizPartner) throws Exception {
        return "Renglón póliza: " + 
                resultSet.getInt(tableAlias + ".id_year") + "-" + 
                resultSet.getInt(tableAlias + ".id_per") + "-" + 
                resultSet.getInt(tableAlias + ".id_bkc") + "-" + 
                resultSet.getString(tableAlias + ".id_tp_rec") + "-" + 
                resultSet.getInt(tableAlias + ".id_num") + "-" +
                resultSet.getInt(tableAlias + ".id_ety") +
                (account == null ? "" : "; cuenta contable: " +  account.getPkAccountIdXXX()) +
                (bizPartner == null ? "" : "; tercero: " + bizPartner.getBizPartnerCommercial());
    }
    
    public String getLayout(final int format, final boolean excludeTotalZeros) throws Exception {
        int entries = 0;
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
        
        moAccounts.clear();
        moMajorAccounts.clear();
        moVats.clear();
        moBizPartners.clear();
        moTerceros.clear();
        
        // iterate through all DIOT accounts set up in company's configuration:
        
        ArrayList<SDiotAccount> diotAccounts = new ArrayList<>();
        
        for (String diotAccountCode : masDiotAccountCodes) {
            diotAccounts.add(new SDiotAccount(diotAccountCode, true));
        }
        
        diotAccounts.add(new SDiotAccount("", false)); // special element to trigger search in non configuration parameter accounts
        
        for (SDiotAccount diotAccount : diotAccounts) {
            String sql = null;
            
            if (diotAccount.IsConfigParamAccount) {
                // scan account set up in company's configuration:
                
                sql = "SELECT re.* "
                        + "FROM fin_rec AS r "
                        + "INNER JOIN fin_rec_ety AS re ON "
                        + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                        + "WHERE NOT r.b_del AND NOT re.b_del AND "
                        + "r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtEnd) + "' AND "
                        + "re.fid_acc LIKE '" + diotAccount.AccountCode + "%' "
                        + "ORDER BY r.dt, re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety;";
            }
            else {
                // scan other purchases and expenses accounts with explicitly tax input:
                
                sql = "SELECT re.* "
                        + "FROM fin_rec AS r "
                        + "INNER JOIN fin_rec_ety AS re ON "
                        + "r.id_year = re.id_year AND r.id_per = re.id_per AND r.id_bkc = re.id_bkc AND r.id_tp_rec = re.id_tp_rec AND r.id_num = re.id_num "
                        + "INNER JOIN fin_acc AS a ON "
                        + "re.fid_acc = a.code "
                        + "INNER JOIN fin_acc AS al ON "
                        + "f_acc_std_ldg(a.code) = al.code "
                        + "WHERE NOT r.b_del AND NOT re.b_del AND "
                        + "r.dt BETWEEN '" + SLibUtils.DbmsDateFormatDate.format(mtStart) + "' AND '" + SLibUtils.DbmsDateFormatDate.format(mtEnd) + "' AND "
                        + "al.fid_tp_acc_sys IN (" + SDataConstantsSys.FINS_TP_ACC_SYS_PUR + ", " + SDataConstantsSys.FINS_TP_ACC_SYS_PUR_ADJ + ") AND "
                        + "re.fid_tax_bas_n IS NOT NULL "
                        + "ORDER BY r.dt, re.id_year, re.id_per, re.id_bkc, re.id_tp_rec, re.id_num, re.id_ety;";
            }
            
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                entries++;
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
                    if (account.getLevel() == 1) {
                        majorAccount = account; // input account is a ledger account:
                    }
                    else {
                        majorAccount = (SDataAccount) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_ACC, new Object[] { account.getDbmsPkAccountMajorId() }, SLibConstants.EXEC_MODE_VERBOSE);
                    }
                    
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
                
                SDataDps dps = null;
                SDataBizPartner bizPartner = null;
                
                if (resultSet.getInt("re.fid_bp_nr") != 0) {
                    bizPartner = getBizPartner(resultSet.getInt("re.fid_bp_nr"));
                    
                    if (bizPartner == null) {
                        bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { resultSet.getInt("re.fid_bp_nr") }, SLibConstants.EXEC_MODE_VERBOSE);
                        addBizPartner(bizPartner);
                    }
                }
                
                // get DPS, if any, and, if needed, business partner indirectly through DPS:
                
                if (resultSet.getInt("re.fid_dps_year_n") != 0 && resultSet.getInt("re.fid_dps_doc_n") != 0) {
                    dps = (SDataDps) SDataUtilities.readRegistry(miClient, SDataConstants.TRN_DPS, new int[] { resultSet.getInt("re.fid_dps_year_n"), resultSet.getInt("re.fid_dps_doc_n") }, SLibConstants.EXEC_MODE_VERBOSE);
                }
                
                if (bizPartner == null && dps != null) {
                    bizPartner = getBizPartner(dps.getFkBizPartnerId_r());

                    if (bizPartner == null) {
                        bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { dps.getFkBizPartnerId_r() }, SLibConstants.EXEC_MODE_VERBOSE);
                        addBizPartner(bizPartner);
                    }
                }
                
                // get business partner (factoring bank) indirectly through CFD, if any:
                
                if (bizPartner == null && resultSet.getInt("re.fid_cfd_n") != 0) {
                    sql = "SELECT fid_fact_bank_n FROM trn_cfd WHERE id_cfd = " + resultSet.getInt("re.fid_cfd_n") + ";";
                    ResultSet resultSetAux = statementAux.executeQuery(sql);
                    if (resultSetAux.next()) {
                        bizPartner = getBizPartner(resultSetAux.getInt(1));
                        
                        if (bizPartner == null) {
                            bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { resultSetAux.getInt(1) }, SLibConstants.EXEC_MODE_VERBOSE);
                            addBizPartner(bizPartner);
                        }
                    }
                }
                
                // process out business partner:
                
                if (bizPartner == null) {
                    entriesWithoutBizPartner++;
                }
                else if (bizPartner.getPkBizPartnerId() == miClient.getSessionXXX().getCurrentCompany().getPkCompanyId()) {
                    entriesForCompany++;
                }
                
                // get DIOT Tercero:
                
                SDiotTercero tercero = moTerceros.get(bizPartner != null ? bizPartner.getDiotTerceroClave() : SDiotTercero.GLOBAL_CLAVE);
                
                if (tercero == null) {
                    tercero = new SDiotTercero(miClient, bizPartner);
                    moTerceros.put(tercero.getClave(), tercero);
                }
                
                String warning;
                double vatAmount = SLibUtils.roundAmount(Math.abs(debit + credit)); // VAT amout of payment
                boolean isDebit = SLibUtils.belongsTo(majorAccount.getAccountClassKey(), new int[][] { SDataConstantsSys.FINS_CL_ACC_ASSET, SDataConstantsSys.FINS_CL_ACC_RES_DBT });
                boolean isCredit = SLibUtils.belongsTo(majorAccount.getAccountClassKey(), new int[][] { SDataConstantsSys.FINS_CL_ACC_LIABTY, SDataConstantsSys.FINS_CL_ACC_RES_CDT });
                
                if (isDebit) {
                    // VAT creditable:
                    
                    // extract net total and VAT debits and credits:
                    
                    SDiotAccounting diotAccounting = new SDiotAccounting(
                            statementAux, 
                            resultSet.getInt("re.usr_id"), 
                            createFinRecordKey(resultSet, "re"), 
                            new int[] { resultSet.getInt("re.fid_bkk_year_n"), resultSet.getInt("re.fid_bkk_num_n") },
                            new int[] { resultSet.getInt("re.fid_dps_year_n"), resultSet.getInt("re.fid_dps_doc_n") }
                    );
                    
                    double paymentAmount = diotAccounting.getPaymentAmount();
                    double paymentRatio = dps == null || dps.getTotal_r() == 0 ? 1.0 : paymentAmount / dps.getTotal_r();
                    
                    if (debit > 0 || credit < 0 || (debit == 0 && credit == 0)) {
                        // VAT creditable of payments of purchases:
                        
                        if (debit == 0 && credit == 0) {
                            switch (vat.getVatType()) {
                                case SDiotConsts.VAT_TYPE_EXEMPT:
                                    if (tercero.IsDomestic) {
                                        tercero.ValorPagosImpIvaExento = SLibUtils.roundAmount(tercero.ValorPagosImpIvaExento + paymentAmount);
                                    }
                                    else {
                                        tercero.ValorPagosNacIvaExento = SLibUtils.roundAmount(tercero.ValorPagosNacIvaExento + paymentAmount);
                                    }
                                    break;

                                case SDiotConsts.VAT_TYPE_RATE_0:
                                case SDiotConsts.VAT_TYPE_GENERAL:          // VAT deliberately manipulated to be cero
                                case SDiotConsts.VAT_TYPE_BORDER:           // VAT deliberately manipulated to be cero
                                case SDiotConsts.VAT_TYPE_BORDER_NORTH_INC: // VAT deliberately manipulated to be cero
                                    tercero.ValorPagosNacIva0 = SLibUtils.roundAmount(tercero.ValorPagosNacIva0 + paymentAmount);
                                    break;

                                default:
                                    entriesNonZeroTaxButZero++;
                                    
                                    warning = "" + entries + ".- " + composeFinRecordEntry(resultSet, "re", account, bizPartner) + ":\n"
                                            + " No fue posible clasificar el impuesto '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                            + " importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + ";\n"
                                            + " total neto real: $" + SLibUtils.getDecimalFormatAmount().format(paymentAmount) + "."; 
                                    warnings += "\"" + warning + "\"\n";
                                    System.out.println(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION + "\n" + warning);
                            }
                        }
                        else {
                            // check if current VAT is not asigned explicitly to other third tax causings:
                            
                            ArrayList<SDiotTercero> tercerosToProcess = new ArrayList<>();
                            HashSet<Integer> causingIds = diotAccounting.getThirdTaxCausings((int[]) vat.getPrimaryKey());
                            
                            for (Integer causingId : causingIds) {
                                SDataBizPartner bizPartnerCausing = getBizPartner(causingId);

                                if (bizPartnerCausing == null) {
                                    bizPartnerCausing = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { causingId }, SLibConstants.EXEC_MODE_VERBOSE);
                                    addBizPartner(bizPartnerCausing);
                                }
                                
                                SDiotTercero terceroCausing = moTerceros.get(bizPartnerCausing.getDiotTerceroClave());
                                if (terceroCausing == null) {
                                    terceroCausing = new SDiotTercero(miClient, bizPartnerCausing);
                                    moTerceros.put(terceroCausing.getClave(), terceroCausing);
                                    tercerosToProcess.add(terceroCausing);
                                }
                            }
                            
                            // process DIOT terceros, including the directly current one:
                            
                            tercerosToProcess.add(tercero);
                            
                            double vatProcessed = 0;
                            double terceroExempt = 0;
                            
                            for (SDiotTercero terceroToProcess : tercerosToProcess) {
                                if (vatProcessed >= vatAmount) {
                                    if (terceroExempt != 0) {
                                        tercero.ValorPagosImpIvaExento = SLibUtils.roundAmount(tercero.ValorPagosImpIvaExento + terceroExempt);
                                    }
                                    break; // no more VAT to be processed
                                }
                                else {
                                    double vatToProcess;
                                    double netSubtotalCalculated;

                                    if (terceroToProcess == tercero) {
                                        // VAT corresponds to supplier:
                                        vatToProcess = SLibUtils.roundAmount(vatAmount - vatProcessed);
                                    }
                                    else {
                                        // VAT corresponds to a third tax causing:
                                        vatToProcess = SLibUtils.roundAmount(diotAccounting.getThirdTax(terceroToProcess.BizPartnerId, (int[]) vat.getPrimaryKey()) * paymentRatio);
                                        terceroExempt = SLibUtils.roundAmount(diotAccounting.getThirdTaxSubtotal(terceroToProcess.BizPartnerId, (int[]) vat.getPrimaryKey()) * paymentRatio);
                                    }

                                    vatProcessed = SLibUtils.roundAmount(vatProcessed + vatToProcess);
                                    netSubtotalCalculated = SLibUtils.roundAmount(vat.getPercentage() == 0 ? 0 : vatToProcess / vat.getPercentage());

                                    if (terceroToProcess == tercero) {
                                        double netTotalCalculated = SLibUtils.roundAmount(netSubtotalCalculated + diotAccounting.getVatSumDebits() - diotAccounting.getVatSumCredits());
                                        double netTotalRealVsCalculated = SLibUtils.roundAmount(paymentAmount - netTotalCalculated);

                                        if (netTotalRealVsCalculated < -AMOUNT_DIFF_ALLOWANCE) {
                                            // suspicious situation: calculated net-total is greater than the real one:

                                            warning = "" + entries + ".- " + composeFinRecordEntry(resultSet, "re", account, bizPartner) + ":\n"
                                                    + " Discrepancia NO corregible en impuesto '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                                    + " importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + "; "
                                                    + "IVA a procesar: $" + SLibUtils.getDecimalFormatAmount().format(vatToProcess) + "; "
                                                    + "IVA remanente: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount - vatProcessed) + ";\n"
                                                    + " total neto real: $" + SLibUtils.getDecimalFormatAmount().format(paymentAmount) + " < "
                                                    + "total neto calculado: $" + SLibUtils.getDecimalFormatAmount().format(netTotalCalculated) + " = "
                                                    + "diferencia: $" + SLibUtils.getDecimalFormatAmountUnitary().format(netTotalRealVsCalculated) + "."; 
                                            warnings += "\"" + warning + "\"\n";
                                            System.out.println(warning);
                                        }
                                        else if (netTotalRealVsCalculated > AMOUNT_DIFF_ALLOWANCE) {
                                            // manageable situation: calculated net-total is less than the real one:

                                            warning = "" + entries + ".- " + composeFinRecordEntry(resultSet, "re", account, bizPartner) + ":\n"
                                                    + " Discrepancia SÍ corregible en impuesto '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                                    + " importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + "; "
                                                    + "IVA a procesar: $" + SLibUtils.getDecimalFormatAmount().format(vatToProcess) + "; "
                                                    + "IVA remanente: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount - vatProcessed) + ":\n"
                                                    + " total neto real: $" + SLibUtils.getDecimalFormatAmount().format(paymentAmount) + " > "
                                                    + "total neto calculado: $" + SLibUtils.getDecimalFormatAmount().format(netTotalCalculated) + " = "
                                                    + "diferencia: $" + SLibUtils.getDecimalFormatAmountUnitary().format(netTotalRealVsCalculated) + "."; 
                                            warnings += "\"" + warning + "\"\n";
                                            System.out.println(warning);

                                            tercero.ValorPagosImpIvaExento = SLibUtils.roundAmount(tercero.ValorPagosImpIvaExento + netTotalRealVsCalculated);
                                        }
                                        else if (netTotalRealVsCalculated != 0) {
                                            // adjust calculated subtotal due to difference between real and calculated net-total:
                                            netSubtotalCalculated = SLibUtils.roundAmount(netSubtotalCalculated + netTotalRealVsCalculated);
                                        }
                                    }

                                    switch (vat.getVatType()) {
                                        case SDiotConsts.VAT_TYPE_GENERAL:
                                            if (terceroToProcess.IsDomestic) {
                                                terceroToProcess.ValorPagosNacIva1516 = SLibUtils.roundAmount(terceroToProcess.ValorPagosNacIva1516 + netSubtotalCalculated);
                                            }
                                            else {
                                                terceroToProcess.ValorPagosImpIva1516 = SLibUtils.roundAmount(terceroToProcess.ValorPagosImpIva1516 + netSubtotalCalculated);
                                            }
                                            break;

                                        case SDiotConsts.VAT_TYPE_BORDER:
                                            if (terceroToProcess.IsDomestic) {
                                                terceroToProcess.ValorPagosNacIva1011 = SLibUtils.roundAmount(terceroToProcess.ValorPagosNacIva1011 + netSubtotalCalculated);
                                            }
                                            else {
                                                terceroToProcess.ValorPagosImpIva1011 = SLibUtils.roundAmount(terceroToProcess.ValorPagosImpIva1011 + netSubtotalCalculated);
                                            }
                                            break;

                                        case SDiotConsts.VAT_TYPE_BORDER_NORTH_INC:
                                            terceroToProcess.ValorPagosNacIvaEstFront = SLibUtils.roundAmount(terceroToProcess.ValorPagosNacIvaEstFront + netSubtotalCalculated);
                                            break;

                                        default:
                                            entriesZeroTaxButNonZero++;

                                            warning = "" + entries + ".- " + composeFinRecordEntry(resultSet, "re", account, bizPartner) + ":\n"
                                                    + " No fue posible clasificar el impuesto '" + vat.getTax() + "', tipo IVA '" + vat.getVatType() + "':\n"
                                                    + " importe IVA: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount) + "; "
                                                    + "IVA a procesar: $" + SLibUtils.getDecimalFormatAmount().format(vatToProcess) + ";"
                                                    + "IVA remanente: $" + SLibUtils.getDecimalFormatAmount().format(vatAmount - vatProcessed) + ":\n"
                                                    + " total neto real: $" + SLibUtils.getDecimalFormatAmount().format(paymentAmount) + "."; 
                                            warnings += "\"" + warning + "\"\n";
                                            System.out.println(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION + "\n" + warning);
                                    }
                                }
                            }
                        }
                    }
                    else if (debit < 0 || credit > 0) {
                        // VAT creditable of purchases adjustments & VAT creditable settlements:
                        
                        if (tercero.IsCompany) {
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
                else if (isCredit) {
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
                        
                        if (tercero.IsCompany) {
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
                else {
                    throw new Exception("" + entries + ".- " + composeFinRecordEntry(resultSet, "re", account, bizPartner) + ":\n"
                            + " No pudo determinarse si la cuenta contable de mayor " + majorAccount.getCode() + ", '" + majorAccount.getAccount() + "', es de débito o de crédito.");
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
            boolean isTotallyZero = tercero.isTotallyZero();
            
            if (!tercero.IsCompany && (!excludeTotalZeros || !isTotallyZero)) {
                layout += tercero.getLayoutRow(format) + "\n";

                if (format == FORMAT_CSV) {
                    totales.addTercero(tercero);
                }
            }
            
            if (isTotallyZero) {
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
            layout += "\"renglones de pólizas contables procesados:\"," + entries + "\n";
            layout += "\"renglones de pólizas contables sin impuesto:\"," + entriesWithoutTax + ",\"(asignadas el impuesto predeterminado para DIOT: " + vatDefault.getTax() + ")\"\n";
            layout += "\"renglones de pólizas contables sin asociado de negocios:\"," + entriesWithoutBizPartner + ",\"(asignados al tercero global de este layout)\"\n";
            layout += "\"renglones de pólizas contables de la empresa '" + miClient.getSessionXXX().getCurrentCompanyName() + "':\"," + entriesForCompany + ",\"(excluidos de este layout)\"\n";
            layout += "\"renglones de pólizas contables de impuestos distintos a cero, pero iguales a cero:\"," + entriesNonZeroTaxButZero + ",\"(excluidos de este layout)\"\n";
            layout += "\"renglones de pólizas contables de impuestos similares a cero, pero distintos de cero:\"," + entriesZeroTaxButNonZero + ",\"(excluidos de este layout)\"\n";
            layout += "\"terceros totalmente en cero:\"," + totalZeros + ",\"" + (excludeTotalZeros ? "(excluídos de este layout)" : "(incluidos en este layout)") + "\"\n";

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
            layout += "\"Excepciones:\"\n";
            layout += warnings.isEmpty() ? "\"¡No hay excepciones!\"\n" : warnings; 
        }
        
        return SLibUtils.textToAscii(layout);
    }
}
