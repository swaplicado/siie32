package erp.mfin.data.diot.ver1;

import cfd.DCfdConsts;
import erp.client.SClientInterface;
import erp.mbps.data.SDataBizPartner;
import erp.mfin.data.diot.SDiotConsts;
import java.text.DecimalFormat;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;

/**
 * For DIOT layout valid until 2024-12-31.
 * There are four types of tercero:
 * 1. "empty tercero". Only for subtotals and totals in CSV DIOT. Not linked to any business partner. Type of third party: undefined. Type of operations: undefined.
 * 2. "global tercero". Not linked to any business partner. Type of third party: domestic. Type of operations: other.
 * 3. "standard tercero" for current company: Linked to current company. Type of third party: undefined. Type of operations: undefined.
 * 4. "standard tercero" for third party: Linked to some business partner. Type of third party: according to the business partner. Type of operations: according to the business partner.
 * @author Sergio Flores
 */
@Deprecated
public class SDiotTercero implements Comparable<SDiotTercero> {

    /**
     * Supplier's business partner ID for an undefined one + '-' + code of DIOT other operations.
     */
    public static final String GLOBAL_CLAVE = 0 + "-" + SDiotConsts.OPER_OTHER;
    
    public boolean IsCurrentCompany;
    public String Clave;
    
    public Boolean IsGlobal;
    public Boolean IsDomestic;
    public int BizPartnerId;
    public String OccasionalRfc; // RFC of occasional business partners that are not in catalog
    
    public String TipoTercero; // 1
    public String TipoOperación; // 2
    public String Rfc; // 3
    public String ExtIdFiscal; // 4
    public String ExtNombre; // 5
    public String ExtPaísResidencia; // 6
    public String ExtNacionalidad; // 7
    public double ValorPagosNacIva1516; // 8
    public double ValorPagosNacIva15; // 9
    public double IvaPagadoNoAcredNac1516; // 10
    public double ValorPagosNacIva1011; // 11
    public double ValorPagosNacIva10; // 12
    public double ValorPagosNacIvaEstFront; // 13
    public double IvaPagadoNoAcredNac1011; // 14
    public double IvaPagadoNoAcredNacEstFront; // 15
    public double ValorPagosImpIva1516; // 16
    public double IvaPagadoNoAcredImp1516; // 17
    public double ValorPagosImpIva1011; // 18
    public double IvaPagadoNoAcredImp1011; // 19
    public double ValorPagosImpIvaExento; // 20
    public double ValorPagosNacIva0; // 21
    public double ValorPagosNacIvaExento; // 22
    public double IvaRetenido; // 23
    public double IvaNotasCréditoCompras; // 24
    
    protected static final DecimalFormat FormatPipe = new DecimalFormat("#0");
    protected static final DecimalFormat FormatCsv = new DecimalFormat("#0.00");
    
    /**
     * Creates an "empty tercero".
     */
    public SDiotTercero() {
        initTerceroAsEmpty();
    }
    
    /**
     * Creates a "standard tercero" in normal conditions, or a "global tercero".
     * @param client GUI client.
     * @param bizPartner Business partner. When <code>null</code>, a "global tercero" is instantiated. When is current company, a "standard tercero" for current company is instantiated.
     */
    public SDiotTercero(final SClientInterface client, final SDataBizPartner bizPartner) {
        if (bizPartner != null) {
            if (!bizPartner.isCurrentCompany(client)) {
                // "standard tercero"...
                
                boolean isDomestic = bizPartner.isDomestic(client);
                
                initTercero(false, Boolean.FALSE, isDomestic, bizPartner.getPkBizPartnerId(), bizPartner.getDiotTipoTercero(client), bizPartner.getDiotTipoOperación(), bizPartner.getFiscalId(), bizPartner.getFiscalFrgId(), "");
                
                if (!isDomestic) {
                    String countryDiotCode = bizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial().getDbmsDataCountry().getDiotCode();

                    ExtNombre = bizPartner.getBizPartner();
                    ExtPaísResidencia = countryDiotCode;
                    ExtNacionalidad = countryDiotCode;
                }
            }
            else {
                // "standard tercero" for current company...
                initTerceroAsCurrentCompany(bizPartner.getPkBizPartnerId(), bizPartner.getFiscalId());
            }
        }
        else {
            // "global tercero"...
            initTerceroAsGlobal();
        }
    }
    
    /**
     * Creates an "occasional tercero" in normal conditions, or a "standard tercero" or a "global tercero".
     * @param client GUI client.
     * @param occasionalRfc Occasional RFC. When <code>null</code> or empty, a "global tercero" is instantiated. When corresponds to current company, a "standard tercero" for current company is instantiated.
     */
    public SDiotTercero(final SClientInterface client, final String occasionalRfc) {
        if (occasionalRfc != null && !occasionalRfc.isEmpty() && (occasionalRfc.length() == DCfdConsts.LEN_RFC_ORG || occasionalRfc.length() == DCfdConsts.LEN_RFC_PER)) {
            SDataBizPartner bizPartner = client.getSessionXXX().getCurrentCompany().getDbmsDataCompany();
            
            if (!bizPartner.getFiscalId().equals(occasionalRfc)) {
                // "occasional tercero"...
                
                boolean isDomestic = !occasionalRfc.equals(DCfdConsts.RFC_GEN_INT);
                String tipoTercero = isDomestic ? SDiotConsts.THIRD_DOMESTIC : SDiotConsts.THIRD_INTERNAT;
                
                initTercero(false, Boolean.FALSE, isDomestic, 0, tipoTercero, SDiotConsts.OPER_OTHER, occasionalRfc, "", occasionalRfc);
            }
            else {
                // "standard tercero" for current company...
                initTerceroAsCurrentCompany(bizPartner.getPkBizPartnerId(), bizPartner.getFiscalId());
            }
        }
        else {
            // "global tercero"...
            initTerceroAsGlobal();
        }
    }
    
    private void initTerceroAsEmpty() {
        initTercero(false, null, null, 0, SDiotConsts.THIRD_UNDEF, SDiotConsts.OPER_UNDEF, "", "", "");
    }
    
    private void initTerceroAsGlobal() {
        initTercero(false, Boolean.TRUE, Boolean.TRUE, 0, SDiotConsts.THIRD_GLOBAL, SDiotConsts.OPER_OTHER, DCfdConsts.RFC_GEN_NAC, "", "");
    }
    
    private void initTerceroAsCurrentCompany(final int bizPartnerId, final String rfc) {
        initTercero(true, Boolean.FALSE, Boolean.TRUE, bizPartnerId, SDiotConsts.THIRD_UNDEF, SDiotConsts.OPER_UNDEF, rfc, "", "");
    }
    
    private void initTercero(final boolean isCurrentCompany, final Boolean isGlobal, final Boolean isDomestic, final int bizPartnerId, final String tipoTercero, final String tipoOperación, final String rfc, final String extIdFiscal, final String occasionalRfc) {
        IsCurrentCompany = isCurrentCompany;
        
        if (IsCurrentCompany) {
            IsGlobal = false;
            IsDomestic = true;
            BizPartnerId = bizPartnerId;
            OccasionalRfc = "";

            TipoTercero = SDiotConsts.THIRD_UNDEF; // 1
            TipoOperación = SDiotConsts.OPER_UNDEF; // 2
            Rfc = rfc; // 3
            ExtIdFiscal = ""; // 4
            ExtNombre = ""; // 5
            ExtPaísResidencia = ""; // 6
            ExtNacionalidad = ""; // 7
        }
        else {
            IsGlobal = isGlobal;
            IsDomestic = isDomestic;
            BizPartnerId = bizPartnerId;
            OccasionalRfc = occasionalRfc;

            TipoTercero = tipoTercero; // 1
            TipoOperación = tipoOperación; // 2
            Rfc = rfc; // 3
            ExtIdFiscal = extIdFiscal; // 4
            ExtNombre = ""; // 5
            ExtPaísResidencia = ""; // 6
            ExtNacionalidad = ""; // 7
        }
        
        if (IsGlobal != null) {
            Clave = IsGlobal ? GLOBAL_CLAVE : ((isOccasional() ? OccasionalRfc : BizPartnerId) + "-" + TipoOperación);
        }
        else {
            Clave = ""; // no need for any "clave"
        }
    }
    
    public String getComparableKey() {
        return
                TipoTercero + "-" + // 1
                TipoOperación + "-" + // 2
                Rfc + "-" + // 3
                ExtIdFiscal + "-" + // 4
                ExtNombre + "-" + // 5
                ExtPaísResidencia + "-" + // 6
                ExtNacionalidad;// 7
    }
    
    public boolean isOccasional() {
        return !OccasionalRfc.isEmpty();
    }
    
    public boolean isTotallyZero() {
        return
                ValorPagosNacIva1516 == 0 &&
                ValorPagosNacIva15 == 0 &&
                IvaPagadoNoAcredNac1516 == 0 &&
                ValorPagosNacIva1011 == 0 &&
                ValorPagosNacIva10 == 0 &&
                ValorPagosNacIvaEstFront == 0 &&
                IvaPagadoNoAcredNac1011 == 0 &&
                IvaPagadoNoAcredNacEstFront == 0 &&
                ValorPagosImpIva1516 == 0 &&
                IvaPagadoNoAcredImp1516 == 0 &&
                ValorPagosImpIva1011 == 0 &&
                IvaPagadoNoAcredImp1011 == 0 &&
                ValorPagosImpIvaExento == 0 &&
                ValorPagosNacIva0 == 0 &&
                ValorPagosNacIvaExento == 0 &&
                IvaRetenido == 0 &&
                IvaNotasCréditoCompras == 0;
    }
    
    public void addTercero(final SDiotTercero tercero) {
        ValorPagosNacIva1516 = SLibUtils.roundAmount(ValorPagosNacIva1516 + tercero.ValorPagosNacIva1516); // 8
        ValorPagosNacIva15 = SLibUtils.roundAmount(ValorPagosNacIva15 + tercero.ValorPagosNacIva15); // 9
        IvaPagadoNoAcredNac1516 = SLibUtils.roundAmount(IvaPagadoNoAcredNac1516 + tercero.IvaPagadoNoAcredNac1516); // 10
        ValorPagosNacIva1011 = SLibUtils.roundAmount(ValorPagosNacIva1011 + tercero.ValorPagosNacIva1011); // 11
        ValorPagosNacIva10 = SLibUtils.roundAmount(ValorPagosNacIva10 + tercero.ValorPagosNacIva10); // 12
        ValorPagosNacIvaEstFront = SLibUtils.roundAmount(ValorPagosNacIvaEstFront + tercero.ValorPagosNacIvaEstFront); // 13
        IvaPagadoNoAcredNac1011 = SLibUtils.roundAmount(IvaPagadoNoAcredNac1011 + tercero.IvaPagadoNoAcredNac1011); // 14
        IvaPagadoNoAcredNacEstFront = SLibUtils.roundAmount(IvaPagadoNoAcredNacEstFront + tercero.IvaPagadoNoAcredNacEstFront); // 15
        ValorPagosImpIva1516 = SLibUtils.roundAmount(ValorPagosImpIva1516 + tercero.ValorPagosImpIva1516); // 16
        IvaPagadoNoAcredImp1516 = SLibUtils.roundAmount(IvaPagadoNoAcredImp1516 + tercero.IvaPagadoNoAcredImp1516); // 17
        ValorPagosImpIva1011 = SLibUtils.roundAmount(ValorPagosImpIva1011 + tercero.ValorPagosImpIva1011); // 18
        IvaPagadoNoAcredImp1011 = SLibUtils.roundAmount(IvaPagadoNoAcredImp1011 + tercero.IvaPagadoNoAcredImp1011); // 19
        ValorPagosImpIvaExento = SLibUtils.roundAmount(ValorPagosImpIvaExento + tercero.ValorPagosImpIvaExento); // 20
        ValorPagosNacIva0 = SLibUtils.roundAmount(ValorPagosNacIva0 + tercero.ValorPagosNacIva0); // 21
        ValorPagosNacIvaExento = SLibUtils.roundAmount(ValorPagosNacIvaExento + tercero.ValorPagosNacIvaExento); // 22
        IvaRetenido = SLibUtils.roundAmount(IvaRetenido + tercero.IvaRetenido); // 23
        IvaNotasCréditoCompras = SLibUtils.roundAmount(IvaNotasCréditoCompras + tercero.IvaNotasCréditoCompras); // 24
    }
    
    /**
     * Get DIOT layout row.
     * @param formatType Format of DIOT layout row. Options defined in <code>SDiotLayout</code>.
     * @return DIOT layout row.
     * @throws java.lang.Exception
     */
    public String getLayoutRow(int formatType) throws Exception {
        String row = "";
        String separator = "";
        DecimalFormat format = null;
        
        // compose layout row:
                
        switch (formatType) {
            case SDiotConsts.FORMAT_PIPE:
                separator = "|";
                format = FormatPipe;
                row = TipoTercero + separator +
                        TipoOperación + separator +
                        Rfc + separator +
                        ExtIdFiscal + separator +
                        SLibUtils.textToAscii(ExtNombre) + separator +
                        ExtPaísResidencia + separator +
                        ExtNacionalidad + separator;
                break;
                
            case SDiotConsts.FORMAT_CSV:
                separator = ",";
                format = FormatCsv;
                row = "\"" + TipoTercero + "\"" + separator +
                        "\"" + TipoOperación + "\"" + separator +
                        "\"" + Rfc + "\"" + separator +
                        "\"" + ExtIdFiscal + "\"" + separator +
                        "\"" + SLibUtils.textToAscii(ExtNombre) + "\"" + separator +
                        "\"" + ExtPaísResidencia + "\"" + separator +
                        "\"" + ExtNacionalidad + "\"" + separator;
                break;
                
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return row +
                (ValorPagosNacIva1516 == 0 ? "" : format.format(ValorPagosNacIva1516)) + separator +
                (ValorPagosNacIva15 == 0 ? "" : format.format(ValorPagosNacIva15)) + separator +
                (IvaPagadoNoAcredNac1516 == 0 ? "" : format.format(IvaPagadoNoAcredNac1516)) + separator +
                (ValorPagosNacIva1011 == 0 ? "" : format.format(ValorPagosNacIva1011)) + separator +
                (ValorPagosNacIva10 == 0 ? "" : format.format(ValorPagosNacIva10)) + separator +
                (ValorPagosNacIvaEstFront == 0 ? "" : format.format(ValorPagosNacIvaEstFront)) + separator +
                (IvaPagadoNoAcredNac1011 == 0 ? "" : format.format(IvaPagadoNoAcredNac1011)) + separator +
                (IvaPagadoNoAcredNacEstFront == 0 ? "" : format.format(IvaPagadoNoAcredNacEstFront)) + separator +
                (ValorPagosImpIva1516 == 0 ? "" : format.format(ValorPagosImpIva1516)) + separator +
                (IvaPagadoNoAcredImp1516 == 0 ? "" : format.format(IvaPagadoNoAcredImp1516)) + separator +
                (ValorPagosImpIva1011 == 0 ? "" : format.format(ValorPagosImpIva1011)) + separator +
                (IvaPagadoNoAcredImp1011 == 0 ? "" : format.format(IvaPagadoNoAcredImp1011)) + separator +
                (ValorPagosImpIvaExento == 0 ? "" : format.format(ValorPagosImpIvaExento)) + separator +
                (ValorPagosNacIva0 == 0 ? "" : format.format(ValorPagosNacIva0)) + separator +
                (ValorPagosNacIvaExento == 0 ? "" : format.format(ValorPagosNacIvaExento)) + separator +
                (IvaRetenido == 0 ? "" : format.format(IvaRetenido)) + separator +
                (IvaNotasCréditoCompras == 0 ? "" : format.format(IvaNotasCréditoCompras));
    }
    
    @Override
    public int compareTo(SDiotTercero other) {
        return this.getComparableKey().compareTo(other.getComparableKey());
    }
    
    @Override
    public String toString() {
        String string = "";
        
        string += "ValorPagosNacIva1516: " + SLibUtils.getDecimalFormatAmount().format(ValorPagosNacIva1516) + "; "; // 8
        string += "ValorPagosNacIva15: " + SLibUtils.getDecimalFormatAmount().format(ValorPagosNacIva15) + "; "; // 9
        string += "IvaPagadoNoAcredNac1516: " + SLibUtils.getDecimalFormatAmount().format(IvaPagadoNoAcredNac1516) + "; "; // 10
        string += "ValorPagosNacIva1011: " + SLibUtils.getDecimalFormatAmount().format(ValorPagosNacIva1011) + "; "; // 11
        string += "ValorPagosNacIva10: " + SLibUtils.getDecimalFormatAmount().format(ValorPagosNacIva10) + "; "; // 12
        string += "ValorPagosNacIvaEstFront: " + SLibUtils.getDecimalFormatAmount().format(ValorPagosNacIvaEstFront) + "; "; // 13
        string += "IvaPagadoNoAcredNac1011: " + SLibUtils.getDecimalFormatAmount().format(IvaPagadoNoAcredNac1011) + "; "; // 14
        string += "IvaPagadoNoAcredNacEstFront: " + SLibUtils.getDecimalFormatAmount().format(IvaPagadoNoAcredNacEstFront) + "; "; // 15
        string += "ValorPagosImpIva1516: " + SLibUtils.getDecimalFormatAmount().format(ValorPagosImpIva1516) + "; "; // 16
        string += "IvaPagadoNoAcredImp1516: " + SLibUtils.getDecimalFormatAmount().format(IvaPagadoNoAcredImp1516) + "; "; // 17
        string += "ValorPagosImpIva1011: " + SLibUtils.getDecimalFormatAmount().format(ValorPagosImpIva1011) + "; "; // 18
        string += "IvaPagadoNoAcredImp1011: " + SLibUtils.getDecimalFormatAmount().format(IvaPagadoNoAcredImp1011) + "; "; // 19
        string += "ValorPagosImpIvaExento: " + SLibUtils.getDecimalFormatAmount().format(ValorPagosImpIvaExento) + "; "; // 20
        string += "ValorPagosNacIva0: " + SLibUtils.getDecimalFormatAmount().format(ValorPagosNacIva0) + "; "; // 21
        string += "ValorPagosNacIvaExento: " + SLibUtils.getDecimalFormatAmount().format(ValorPagosNacIvaExento) + "; "; // 22
        string += "IvaRetenido: " + SLibUtils.getDecimalFormatAmount().format(IvaRetenido) + "; "; // 23
        string += "IvaNotasCréditoCompras: " + SLibUtils.getDecimalFormatAmount().format(IvaNotasCréditoCompras); // 24
        
        return string;
    }

    @Override
    public SDiotTercero clone() {
        SDiotTercero cloned = new SDiotTercero();
        
        cloned.initTercero(this.IsCurrentCompany, this.IsGlobal, this.IsDomestic, this.BizPartnerId, this.TipoTercero, this.TipoOperación, this.Rfc, this.ExtIdFiscal, this.OccasionalRfc);

        cloned.ExtNombre = this.ExtNombre; // 5
        cloned.ExtPaísResidencia = this.ExtPaísResidencia; // 6
        cloned.ExtNacionalidad = this.ExtNacionalidad; // 7
        
        cloned.ValorPagosNacIva1516 = this.ValorPagosNacIva1516; // 8
        cloned.ValorPagosNacIva15 = this.ValorPagosNacIva15; // 9
        cloned.IvaPagadoNoAcredNac1516 = this.IvaPagadoNoAcredNac1516; // 10
        cloned.ValorPagosNacIva1011 = this.ValorPagosNacIva1011; // 11
        cloned.ValorPagosNacIva10 = this.ValorPagosNacIva10; // 12
        cloned.ValorPagosNacIvaEstFront = this.ValorPagosNacIvaEstFront; // 13
        cloned.IvaPagadoNoAcredNac1011 = this.IvaPagadoNoAcredNac1011; // 14
        cloned.IvaPagadoNoAcredNacEstFront = this.IvaPagadoNoAcredNacEstFront; // 15
        cloned.ValorPagosImpIva1516 = this.ValorPagosImpIva1516; // 16
        cloned.IvaPagadoNoAcredImp1516 = this.IvaPagadoNoAcredImp1516; // 17
        cloned.ValorPagosImpIva1011 = this.ValorPagosImpIva1011; // 18
        cloned.IvaPagadoNoAcredImp1011 = this.IvaPagadoNoAcredImp1011; // 19
        cloned.ValorPagosImpIvaExento = this.ValorPagosImpIvaExento; // 20
        cloned.ValorPagosNacIva0 = this.ValorPagosNacIva0; // 21
        cloned.ValorPagosNacIvaExento = this.ValorPagosNacIvaExento; // 22
        cloned.IvaRetenido = this.IvaRetenido; // 23
        cloned.IvaNotasCréditoCompras = this.IvaNotasCréditoCompras; // 24
        
        return cloned;
    }
    
    public static String composeOccasionalClave(final String occasionalBizPartnerRfc) {
        return occasionalBizPartnerRfc + "-" + SDiotConsts.OPER_OTHER;
    }
    
    public static String getLayoutCsvHeadings() {
        return "\"1. Tipo de tercero\"," +
                "\"2. Tipo de operación\"," +
                "\"3. Registro Federal de Contribuyentes\"," +
                "\"4. Número de ID fiscal\"," +
                "\"5. Nombre del extranjero\"," +
                "\"6. País de residencia\"," +
                "\"7. Nacionalidad\"," +
                "\"8. Valor de los actos o actividades pagados a la tasa del 15% ó 16% de IVA\"," +
                "\"9. Valor de los actos o actividades pagados a la tasa del 15% de IVA\"," +
                "\"10. Monto del IVA pagado no acreditable a la tasa del 15% ó 16% (correspondiente en la proporción de las deducciones autorizadas)\"," +
                "\"11. Valor de los actos o actividades pagados a la tasa del 10% u 11% de IVA\"," +
                "\"12. Valor de los actos o actividades pagados a la tasa del 10% de IVA\"," +
                "\"13. Valor de los actos o actividades pagados sujeto al estimulo de la region fronteriza norte\"," +
                "\"14. Monto del IVA pagado no acreditable a la tasa del 10% u 11% (correspondiente en la proporción de las deducciones autorizadas)\"," +
                "\"15. Monto del IVA pagado no acreditable sujeto al estimulo de la region fronteriza norte (correspondiente en la proporcion de las deducciones autorizadas)\"," +
                "\"16. Valor de los actos o actividades pagados en la importación de bienes y servicios a la tasa del 15% ó 16% de  IVA\"," +
                "\"17. Monto del IVA pagado no acreditable por la importación  a la tasa del 15% ó 16% (correspondiente en la proporción de las deducciones autorizadas)\"," +
                "\"18. Valor de los actos o actividades pagados en la importación de bienes y servicios a la tasa del 10% u 11% de IVA\"," +
                "\"19. Monto del IVA pagado no acreditable por la importación a la tasa del 10% u 11% (correspondiente en la proporción de las deducciones autorizadas)\"," +
                "\"20. Valor de los actos o actividades pagados en la importación de bienes y servicios por los que no se paragá el IVA (Exentos)\"," +
                "\"21. Valor de los demás actos o actividades pagados a la tasa del 0% de IVA\"," +
                "\"22. Valor de los actos o actividades pagados por los que no se pagará el IVA (Exentos)\"," +
                "\"23. IVA Retenido por el contribuyente\"," +
                "\"24. IVA correspondiente a las devoluciones, descuentos y bonificaciones sobre compras\"";
    }
}
