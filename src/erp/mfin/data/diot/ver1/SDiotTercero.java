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
 * @author Sergio Flores
 */
@Deprecated
public class SDiotTercero implements Comparable<SDiotTercero> {

    /** Supplier's business partner ID for an undefined one + '-' + code of DIOT other operations. */
    public static final String GLOBAL_CLAVE = 0 + "-" + SDiotConsts.OPER_OTHER;
    
    public boolean IsGlobal;
    public boolean IsDomestic;
    public int BizPartnerId;
    
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
    
    public String OccasionalBizPartnerRfc; // fiscal ID (RFC) of occasional business partners that are not in catalog
    
    protected static final DecimalFormat FormatPipe;
    protected static final DecimalFormat FormatCsv;
    
    static {
        FormatPipe = new DecimalFormat("#0");
        FormatCsv = new DecimalFormat("#0.00");
    }
    
    /**
     * Create an empty tercero.
     */
    public SDiotTercero() {
        initTercero(false, true, 0, SDiotConsts.THIRD_UNDEF, SDiotConsts.OPER_UNDEF, "", "", "");
    }
    
    /**
     * Create an occasional tercero.
     * @param occasionalBizPartnerRfc 
     */
    public SDiotTercero(final String occasionalBizPartnerRfc) {
        initTercero(false, true, 0, SDiotConsts.THIRD_DOMESTIC, SDiotConsts.OPER_OTHER, occasionalBizPartnerRfc, "", occasionalBizPartnerRfc);
    }
    
    /**
     * Create a tercero from a business partener.
     * @param client
     * @param bizPartner 
     */
    public SDiotTercero(final SClientInterface client, final SDataBizPartner bizPartner) {
        if (bizPartner == null || isBizPartnerThisCompany(client, bizPartner.getPkBizPartnerId())) {
            initTercero(true, true, 0, SDiotConsts.THIRD_GLOBAL, SDiotConsts.OPER_OTHER, DCfdConsts.RFC_GEN_NAC, "", "");
        }
        else {
            boolean isDomestic = bizPartner.isDomestic(client);

            initTercero(false, isDomestic, bizPartner.getPkBizPartnerId(), bizPartner.getDiotTipoTercero(client), bizPartner.getDiotTipoOperación(), bizPartner.getFiscalId(), bizPartner.getFiscalFrgId(), "");
            
            if (!isDomestic) {
                String countryDiotCode = bizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial().getDbmsDataCountry().getDiotCode();
                
                ExtNombre = bizPartner.getBizPartner();
                ExtPaísResidencia = countryDiotCode;
                ExtNacionalidad = countryDiotCode;
            }
        }
    }
    
    private void initTercero(final boolean isGlobal, final boolean isDomestic, final int bizPartnerId, final String tipoTercero, final String tipoOperación, final String rfc, final String extIdFiscal, final String occasionalBizPartnerRfc) {
        IsGlobal = isGlobal;
        IsDomestic = isDomestic;
        BizPartnerId = bizPartnerId;
        
        TipoTercero = tipoTercero; // 1
        TipoOperación = tipoOperación; // 2
        Rfc = rfc; // 3
        ExtIdFiscal = extIdFiscal; // 4
        ExtNombre = ""; // 5
        ExtPaísResidencia = ""; // 6
        ExtNacionalidad = ""; // 7
        
        OccasionalBizPartnerRfc = occasionalBizPartnerRfc;
    }
    
    /**
     * Get Clave of Tercero.
     * @return When tercero is temporal: OccasionalBizPartnerRfc + '-' + TipoOperación; otherwise: BizPartnerId + '-' + TipoOperación.
     */
    public String getTerceroClave() {
        return IsGlobal ? GLOBAL_CLAVE : ((isOccasional() ? OccasionalBizPartnerRfc : BizPartnerId) + "-" + TipoOperación);
    }
    
    public String getComparableKey() {
        return TipoTercero + "-" + // 1
                TipoOperación + "-" + // 2
                Rfc + "-" + // 3
                ExtIdFiscal + "-" + // 4
                ExtNombre + "-" + // 5
                ExtPaísResidencia + "-" + // 6
                ExtNacionalidad;// 7
    }
    
    public boolean isOccasional() {
        return !OccasionalBizPartnerRfc.isEmpty();
    }
    
    public boolean isTotallyZero() {
        return ValorPagosNacIva1516 == 0 &&
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
     * @param format Format of DIOT layout row. Options defined in <code>SDiotLayout</code>.
     * @return DIOT layout row.
     * @throws java.lang.Exception
     */
    public String getLayoutRow(int format) throws Exception {
        String row = "";
        String separator = "";
        DecimalFormat decimalFormat = null;
        
        switch (format) {
            case SDiotConsts.FORMAT_PIPE:
                separator = "|";
                decimalFormat = FormatPipe;
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
                decimalFormat = FormatCsv;
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
                (ValorPagosNacIva1516 == 0 ? "" : decimalFormat.format(ValorPagosNacIva1516)) + separator +
                (ValorPagosNacIva15 == 0 ? "" : decimalFormat.format(ValorPagosNacIva15)) + separator +
                (IvaPagadoNoAcredNac1516 == 0 ? "" : decimalFormat.format(IvaPagadoNoAcredNac1516)) + separator +
                (ValorPagosNacIva1011 == 0 ? "" : decimalFormat.format(ValorPagosNacIva1011)) + separator +
                (ValorPagosNacIva10 == 0 ? "" : decimalFormat.format(ValorPagosNacIva10)) + separator +
                (ValorPagosNacIvaEstFront == 0 ? "" : decimalFormat.format(ValorPagosNacIvaEstFront)) + separator +
                (IvaPagadoNoAcredNac1011 == 0 ? "" : decimalFormat.format(IvaPagadoNoAcredNac1011)) + separator +
                (IvaPagadoNoAcredNacEstFront == 0 ? "" : decimalFormat.format(IvaPagadoNoAcredNacEstFront)) + separator +
                (ValorPagosImpIva1516 == 0 ? "" : decimalFormat.format(ValorPagosImpIva1516)) + separator +
                (IvaPagadoNoAcredImp1516 == 0 ? "" : decimalFormat.format(IvaPagadoNoAcredImp1516)) + separator +
                (ValorPagosImpIva1011 == 0 ? "" : decimalFormat.format(ValorPagosImpIva1011)) + separator +
                (IvaPagadoNoAcredImp1011 == 0 ? "" : decimalFormat.format(IvaPagadoNoAcredImp1011)) + separator +
                (ValorPagosImpIvaExento == 0 ? "" : decimalFormat.format(ValorPagosImpIvaExento)) + separator +
                (ValorPagosNacIva0 == 0 ? "" : decimalFormat.format(ValorPagosNacIva0)) + separator +
                (ValorPagosNacIvaExento == 0 ? "" : decimalFormat.format(ValorPagosNacIvaExento)) + separator +
                (IvaRetenido == 0 ? "" : decimalFormat.format(IvaRetenido)) + separator +
                (IvaNotasCréditoCompras == 0 ? "" : decimalFormat.format(IvaNotasCréditoCompras));
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
    
    public static String getLayoutRowHeadings() {
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
    
    public static boolean isBizPartnerThisCompany(final SClientInterface client, final int bizPartnerId) {
        return bizPartnerId == client.getSessionXXX().getCurrentCompany().getPkCompanyId();
    }
    
    public static String composeOccasionalClave(final String occasionalBizPartnerRfc) {
        return occasionalBizPartnerRfc + "-" + SDiotConsts.OPER_OTHER;
    }

    @Override
    public int compareTo(SDiotTercero other) {
        return this.getComparableKey().compareTo(other.getComparableKey());
    }
    
    @Override
    public SDiotTercero clone() {
        SDiotTercero cloned = new SDiotTercero();
        
        cloned.initTercero(this.IsGlobal, this.IsDomestic, this.BizPartnerId, this.TipoTercero, this.TipoOperación, this.Rfc, this.ExtIdFiscal, this.OccasionalBizPartnerRfc);

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
}
