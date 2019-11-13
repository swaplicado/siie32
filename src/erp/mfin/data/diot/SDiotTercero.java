package erp.mfin.data.diot;

import cfd.DCfdConsts;
import erp.client.SClientInterface;
import erp.mbps.data.SDataBizPartner;
import java.text.DecimalFormat;
import sa.lib.SLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class SDiotTercero {

    public static final String GLOBAL_CLAVE = 0 + "-" + SDiotConsts.THIRD_GLOBAL;
    
    public boolean IsCompany;
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
    
    protected static final DecimalFormat Format;
    
    static {
        Format = new DecimalFormat("0");
    }
    
    public SDiotTercero() {
        resetTercero(false, false, 0, SDiotConsts.THIRD_UNDEFINED, SDiotConsts.OPER_UNDEFINED, "", "");
    }
    
    public SDiotTercero(final SClientInterface client, final SDataBizPartner bizPartner) {
        if (bizPartner == null) {
            resetTercero(false, true, 0, SDiotConsts.THIRD_GLOBAL, SDiotConsts.OPER_OTHER, DCfdConsts.RFC_GEN_NAC, "");
        }
        else {
            String tipoTercero;
            String tipoOperación;
            boolean isCompany = bizPartner.getPkBizPartnerId() == client.getSessionXXX().getCurrentCompany().getPkCompanyId();

            if (isCompany) {
                tipoTercero = SDiotConsts.THIRD_UNDEFINED;
                tipoOperación = SDiotConsts.OPER_UNDEFINED;
            }
            else {
                tipoTercero = bizPartner.getDiotTipoTercero(client);
                tipoOperación = bizPartner.getDiotTipoOperación();
            }

            boolean isDomestic = bizPartner.isDomestic(client);

            resetTercero(isCompany, isDomestic, bizPartner.getPkBizPartnerId(), tipoTercero, tipoOperación, bizPartner.getFiscalId(), bizPartner.getFiscalFrgId());
            
            if (!isDomestic) {
                this.ExtNombre = bizPartner.getBizPartner();
                this.ExtPaísResidencia = bizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial().getDbmsDataCountry().getDiotCode();
                this.ExtNacionalidad = this.ExtPaísResidencia;
            }
        }
        
    }
    
    private void resetTercero(final boolean isCompany, final boolean isDomestic, final int bizPartnerId, final String tipoTercero, final String tipoOperación, final String rfc, final String extIdFiscal) {
        this.IsCompany = isCompany;
        this.IsDomestic = isDomestic;
        this.BizPartnerId = bizPartnerId;
        this.TipoTercero = tipoTercero;
        this.TipoOperación = tipoOperación;
        this.Rfc = rfc;
        this.ExtIdFiscal = extIdFiscal;
        this.ExtNombre = "";
        this.ExtPaísResidencia = "";
        this.ExtNacionalidad = "";
    }
    
    /**
     * Get Clave of Tercero.
     * @return BizPartnerId + "-" + TipoOperación.
     */
    public String getClave() {
        return BizPartnerId + "-" + TipoOperación;
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
        ValorPagosNacIva1516 = SLibUtils.roundAmount(ValorPagosNacIva1516 + SLibUtils.round(tercero.ValorPagosNacIva1516, 0));
        ValorPagosNacIva15 = SLibUtils.roundAmount(ValorPagosNacIva15 + SLibUtils.round(tercero.ValorPagosNacIva15, 0));
        IvaPagadoNoAcredNac1516 = SLibUtils.roundAmount(IvaPagadoNoAcredNac1516 + SLibUtils.round(tercero.IvaPagadoNoAcredNac1516, 0));
        ValorPagosNacIva1011 = SLibUtils.roundAmount(ValorPagosNacIva1011 + SLibUtils.round(tercero.ValorPagosNacIva1011, 0));
        ValorPagosNacIva10 = SLibUtils.roundAmount(ValorPagosNacIva10 + SLibUtils.round(tercero.ValorPagosNacIva10, 0));
        ValorPagosNacIvaEstFront = SLibUtils.roundAmount(ValorPagosNacIvaEstFront + SLibUtils.round(tercero.ValorPagosNacIvaEstFront, 0));
        IvaPagadoNoAcredNac1011 = SLibUtils.roundAmount(IvaPagadoNoAcredNac1011 + SLibUtils.round(tercero.IvaPagadoNoAcredNac1011, 0));
        IvaPagadoNoAcredNacEstFront = SLibUtils.roundAmount(IvaPagadoNoAcredNacEstFront + SLibUtils.round(tercero.IvaPagadoNoAcredNacEstFront, 0));
        ValorPagosImpIva1516 = SLibUtils.roundAmount(ValorPagosImpIva1516 + SLibUtils.round(tercero.ValorPagosImpIva1516, 0));
        IvaPagadoNoAcredImp1516 = SLibUtils.roundAmount(IvaPagadoNoAcredImp1516 + SLibUtils.round(tercero.IvaPagadoNoAcredImp1516, 0));
        ValorPagosImpIva1011 = SLibUtils.roundAmount(ValorPagosImpIva1011 + SLibUtils.round(tercero.ValorPagosImpIva1011, 0));
        IvaPagadoNoAcredImp1011 = SLibUtils.roundAmount(IvaPagadoNoAcredImp1011 + SLibUtils.round(tercero.IvaPagadoNoAcredImp1011, 0));
        ValorPagosImpIvaExento = SLibUtils.roundAmount(ValorPagosImpIvaExento + SLibUtils.round(tercero.ValorPagosImpIvaExento, 0));
        ValorPagosNacIva0 = SLibUtils.roundAmount(ValorPagosNacIva0 + SLibUtils.round(tercero.ValorPagosNacIva0, 0));
        ValorPagosNacIvaExento = SLibUtils.roundAmount(ValorPagosNacIvaExento + SLibUtils.round(tercero.ValorPagosNacIvaExento, 0));
        IvaRetenido = SLibUtils.roundAmount(IvaRetenido + SLibUtils.round(tercero.IvaRetenido, 0));
        IvaNotasCréditoCompras = SLibUtils.roundAmount(IvaNotasCréditoCompras + SLibUtils.round(tercero.IvaNotasCréditoCompras, 0));
    }
    
    /**
     * Get DIOT layout row.
     * @param format Options defined in <code>SDiotLayout</code>.
     * @return Layout row.
     */
    public String getLayoutRow(int format) {
        String row = "";
        String separator = "";
        
        switch (format) {
            case SDiotLayout.FORMAT_PIPE:
                separator = "|";
                row = TipoTercero + "|" +
                        TipoOperación + "|" +
                        Rfc + "|" +
                        ExtIdFiscal + "|" +
                        SLibUtils.textToAscii(ExtNombre) + "|" +
                        ExtPaísResidencia + "|" +
                        ExtNacionalidad + "|";
                break;
                
            case SDiotLayout.FORMAT_CSV:
                separator = ",";
                row = "\"" + TipoTercero + "\"," +
                        "\"" + TipoOperación + "\"," +
                        "\"" + Rfc + "\"," +
                        "\"" + ExtIdFiscal + "\"," +
                        "\"" + SLibUtils.textToAscii(ExtNombre) + "\"," +
                        "\"" + ExtPaísResidencia + "\"," +
                        "\"" + ExtNacionalidad + "\",";
                break;
                
            default:
        }
        
        return row +
                (ValorPagosNacIva1516 == 0 ? "" : Format.format(ValorPagosNacIva1516)) + separator +
                (ValorPagosNacIva15 == 0 ? "" : Format.format(ValorPagosNacIva15)) + separator +
                (IvaPagadoNoAcredNac1516 == 0 ? "" : Format.format(IvaPagadoNoAcredNac1516)) + separator +
                (ValorPagosNacIva1011 == 0 ? "" : Format.format(ValorPagosNacIva1011)) + separator +
                (ValorPagosNacIva10 == 0 ? "" : Format.format(ValorPagosNacIva10)) + separator +
                (ValorPagosNacIvaEstFront == 0 ? "" : Format.format(ValorPagosNacIvaEstFront)) + separator +
                (IvaPagadoNoAcredNac1011 == 0 ? "" : Format.format(IvaPagadoNoAcredNac1011)) + separator +
                (IvaPagadoNoAcredNacEstFront == 0 ? "" : Format.format(IvaPagadoNoAcredNacEstFront)) + separator +
                (ValorPagosImpIva1516 == 0 ? "" : Format.format(ValorPagosImpIva1516)) + separator +
                (IvaPagadoNoAcredImp1516 == 0 ? "" : Format.format(IvaPagadoNoAcredImp1516)) + separator +
                (ValorPagosImpIva1011 == 0 ? "" : Format.format(ValorPagosImpIva1011)) + separator +
                (IvaPagadoNoAcredImp1011 == 0 ? "" : Format.format(IvaPagadoNoAcredImp1011)) + separator +
                (ValorPagosImpIvaExento == 0 ? "" : Format.format(ValorPagosImpIvaExento)) + separator +
                (ValorPagosNacIva0 == 0 ? "" : Format.format(ValorPagosNacIva0)) + separator +
                (ValorPagosNacIvaExento == 0 ? "" : Format.format(ValorPagosNacIvaExento)) + separator +
                (IvaRetenido == 0 ? "" : Format.format(IvaRetenido)) + separator +
                (IvaNotasCréditoCompras == 0 ? "" : Format.format(IvaNotasCréditoCompras));
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
}
