package erp.mfin.data.diot.ver2;

import cfd.DCfdConsts;
import erp.client.SClientInterface;
import erp.mbps.data.SDataBizPartner;
import erp.mfin.data.diot.SDiotConsts;
import java.text.DecimalFormat;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;

/**
 * For DIOT layout valid since 2025-01-01.
 * @author Sergio Flores
 */
public class SDiotTercero implements Comparable<SDiotTercero> {

    /**
     * Supplier's business partner ID for an undefined one + '-' + code of DIOT other operations.
     */
    public static final String GLOBAL_CLAVE = 0 + "-" + SDiotConsts.OPER_OTHER;
    
    public static final DecimalFormat DecimalFormatPipe;
    public static final DecimalFormat DecimalFormatCsv;
    
    static {
        DecimalFormatPipe = new DecimalFormat("#0");
        DecimalFormatCsv = new DecimalFormat("#0.00");
    }
    
    // Third party:
    
    public boolean IsGlobal;
    public boolean IsDomestic;
    public int BizPartnerId;
    public String BizPartnerOccasionalRfc; // fiscal ID (RFC) of occasional business partners that are not in catalog
    
    // 1. Datos del tercero declarado:
    
    public String TerTipoTercero; // 1
    public String TerTipoOperación; // 2
    public String TerRfc; // 3
    public String TerExtIdFiscal; // 4
    public String TerExtNombre; // 5
    public String TerExtPaísResidencia; // 6
    public String TerExtJurisdicción; // 7
    
    // 2. Valor de los actos o actividades:
    
    public double ValFrontNtePagos; // 8
    public double ValFrontNteReembs; // 9
    public double ValFrontSurPagos; // 10
    public double ValFrontSurReembs; // 11
    public double ValTasaGralNacPagos; // 12
    public double ValTasaGralNacReembs; // 13
    public double ValTasaGralIntProdsPagos; // 14
    public double ValTasaGralIntProdsReembs; // 15
    public double ValTasaGralIntServsPagos; // 16
    public double ValTasaGralIntServsReembs; // 17
    
    // 3. IVA acreditable:
    
    public double AcrFrontNteGravs; // 18
    public double AcrFrontNteProp; // 19
    public double AcrFrontSurGravs; // 20
    public double AcrFrontSurProp; // 21
    public double AcrTasaGralNacGravs; // 22
    public double AcrTasaGralNacProp; // 23
    public double AcrTasaGralIntProdsGravs; // 24
    public double AcrTasaGralIntProdsProp; // 25
    public double AcrTasaGralIntServsGravs; // 26
    public double AcrTasaGralIntServsProp; // 27
    
    // 4. IVA no acreditable:
    
    public double NoAcrFrontNteProp; // 28
    public double NoAcrFrontNteNoReq; // 29
    public double NoAcrFrontNteExent; // 30
    public double NoAcrFrontNteNoObj; // 31
    public double NoAcrFrontSurProp; // 32
    public double NoAcrFrontSurNoReq; // 33
    public double NoAcrFrontSurExent; // 34
    public double NoAcrFrontSurNoObj; // 35
    public double NoAcrTasaGralNacProp; // 36
    public double NoAcrTasaGralNacNoReq; // 37
    public double NoAcrTasaGralNacExent; // 38
    public double NoAcrTasaGralNacNoObj; // 39
    public double NoAcrTasaGralIntProdsProp; // 40
    public double NoAcrTasaGralIntProdsNoReq; // 41
    public double NoAcrTasaGralIntProdsExent; // 42
    public double NoAcrTasaGralIntProdsNoObj; // 43
    public double NoAcrTasaGralIntServsProp; // 44
    public double NoAcrTasaGralIntServsNoReq; // 45
    public double NoAcrTasaGralIntServsExent; // 46
    public double NoAcrTasaGralIntServsNoObj; // 47
    
    // 5. Datos adicionales:
    
    public double DatIvaRetenido; // 48
    public double DatPagosExentInt; // 49
    public double DatPagosExentNac; // 50
    public double DatPagosTasa0Nac; // 51
    public double DatPagosNoObjNac; // 52
    public double DatPagosNoObjInt; // 53
    public String DatEfectosFiscales; // 54
    
    public double XValorPagosNacIva1516; // 8
    public double XValorPagosNacIva15; // 9
    public double XIvaPagadoNoAcredNac1516; // 10
    public double XValorPagosNacIva1011; // 11
    public double XValorPagosNacIva10; // 12
    public double XValorPagosNacIvaEstFront; // 13
    public double XIvaPagadoNoAcredNac1011; // 14
    public double XIvaPagadoNoAcredNacEstFront; // 15
    public double XValorPagosImpIva1516; // 16
    public double XIvaPagadoNoAcredImp1516; // 17
    public double XValorPagosImpIva1011; // 18
    public double XIvaPagadoNoAcredImp1011; // 19
    public double XValorPagosImpIvaExento; // 20
    public double XValorPagosNacIva0; // 21
    public double XValorPagosNacIvaExento; // 22
    public double XIvaRetenido; // 23
    public double XIvaNotasCréditoCompras; // 24
    
    /**
     * Create an empty tercero.
     */
    public SDiotTercero() {
        initTercero(false, true, 0, SDiotConsts.THIRD_UNDEF, SDiotConsts.OPER_UNDEF, "", "", "");
    }
    
    /**
     * Create an occasional tercero.
     * @param bizPartnerOccasionalRfc 
     */
    public SDiotTercero(final String bizPartnerOccasionalRfc) {
        initTercero(false, true, 0, SDiotConsts.THIRD_DOMESTIC, SDiotConsts.OPER_OTHER, bizPartnerOccasionalRfc, "", bizPartnerOccasionalRfc);
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
                
                TerExtNombre = bizPartner.getBizPartner();
                TerExtPaísResidencia = countryDiotCode;
                TerExtJurisdicción = "";
            }
        }
    }
    
    private void initTercero(final boolean isGlobal, final boolean isDomestic, final int bizPartnerId, final String tipoTercero, final String tipoOperación, final String rfc, final String idFiscal, final String bizPartnerOcassionalRfc) {
        IsGlobal = isGlobal;
        IsDomestic = isDomestic;
        BizPartnerId = bizPartnerId;
        BizPartnerOccasionalRfc = bizPartnerOcassionalRfc;
        
        TerTipoTercero = tipoTercero; // 1
        TerTipoOperación = tipoOperación; // 2
        TerRfc = rfc; // 3
        TerExtIdFiscal = idFiscal; // 4
        TerExtNombre = ""; // 5
        TerExtPaísResidencia = ""; // 6
        TerExtJurisdicción = ""; // 7

        ValFrontNtePagos = 0; // 8
        ValFrontNteReembs = 0; // 9
        ValFrontSurPagos = 0; // 10
        ValFrontSurReembs = 0; // 11
        ValTasaGralNacPagos = 0; // 12
        ValTasaGralNacReembs = 0; // 13
        ValTasaGralIntProdsPagos = 0; // 14
        ValTasaGralIntProdsReembs = 0; // 15
        ValTasaGralIntServsPagos = 0; // 16
        ValTasaGralIntServsReembs = 0; // 17

        AcrFrontNteGravs = 0; // 18
        AcrFrontNteProp = 0; // 19
        AcrFrontSurGravs = 0; // 20
        AcrFrontSurProp = 0; // 21
        AcrTasaGralNacGravs = 0; // 22
        AcrTasaGralNacProp = 0; // 23
        AcrTasaGralIntProdsGravs = 0; // 24
        AcrTasaGralIntProdsProp = 0; // 25
        AcrTasaGralIntServsGravs = 0; // 26
        AcrTasaGralIntServsProp = 0; // 27

        NoAcrFrontNteProp = 0; // 28
        NoAcrFrontNteNoReq = 0; // 29
        NoAcrFrontNteExent = 0; // 30
        NoAcrFrontNteNoObj = 0; // 31
        NoAcrFrontSurProp = 0; // 32
        NoAcrFrontSurNoReq = 0; // 33
        NoAcrFrontSurExent = 0; // 34
        NoAcrFrontSurNoObj = 0; // 35
        NoAcrTasaGralNacProp = 0; // 36
        NoAcrTasaGralNacNoReq = 0; // 37
        NoAcrTasaGralNacExent = 0; // 38
        NoAcrTasaGralNacNoObj = 0; // 39
        NoAcrTasaGralIntProdsProp = 0; // 40
        NoAcrTasaGralIntProdsNoReq = 0; // 41
        NoAcrTasaGralIntProdsExent = 0; // 42
        NoAcrTasaGralIntProdsNoObj = 0; // 43
        NoAcrTasaGralIntServsProp = 0; // 44
        NoAcrTasaGralIntServsNoReq = 0; // 45
        NoAcrTasaGralIntServsExent = 0; // 46
        NoAcrTasaGralIntServsNoObj = 0; // 47

        DatIvaRetenido = 0; // 48
        DatPagosExentInt = 0; // 49
        DatPagosExentNac = 0; // 50
        DatPagosTasa0Nac = 0; // 51
        DatPagosNoObjNac = 0; // 52
        DatPagosNoObjInt = 0; // 53
        DatEfectosFiscales = SDiotConsts.TAX_EFFECT_YES; // 54
    }
    
    /**
     * Get clave of Tercero.
     * @return When tercero is ocassional: BizPartnerOcassionalRfc + '-' + TipoOperación; otherwise: BizPartnerId + '-' + TipoOperación.
     */
    public String getTerceroClave() {
        return IsGlobal ? GLOBAL_CLAVE : ((isOccasional() ? BizPartnerOccasionalRfc : BizPartnerId) + "-" + TerTipoOperación);
    }
    
    public String getComparableKey() {
        return
                TerTipoTercero + "-" + // 1
                TerTipoOperación + "-" + // 2
                TerRfc + "-" + // 3
                TerExtIdFiscal + "-" + // 4
                TerExtNombre + "-" + // 5
                TerExtPaísResidencia + "-" + // 6
                TerExtJurisdicción;// 7
    }
    
    public boolean isOccasional() {
        return !BizPartnerOccasionalRfc.isEmpty();
    }
    
    public boolean isTotallyZero() {
        return
                ValFrontNtePagos == 0 && // 8
                ValFrontNteReembs == 0 && // 9
                ValFrontSurPagos == 0 && // 10
                ValFrontSurReembs == 0 && // 11
                ValTasaGralNacPagos == 0 && // 12
                ValTasaGralNacReembs == 0 && // 13
                ValTasaGralIntProdsPagos == 0 && // 14
                ValTasaGralIntProdsReembs == 0 && // 15
                ValTasaGralIntServsPagos == 0 && // 16
                ValTasaGralIntServsReembs == 0 && // 17

                AcrFrontNteGravs == 0 && // 18
                AcrFrontNteProp == 0 && // 19
                AcrFrontSurGravs == 0 && // 20
                AcrFrontSurProp == 0 && // 21
                AcrTasaGralNacGravs == 0 && // 22
                AcrTasaGralNacProp == 0 && // 23
                AcrTasaGralIntProdsGravs == 0 && // 24
                AcrTasaGralIntProdsProp == 0 && // 25
                AcrTasaGralIntServsGravs == 0 && // 26
                AcrTasaGralIntServsProp == 0 && // 27

                NoAcrFrontNteProp == 0 && // 28
                NoAcrFrontNteNoReq == 0 && // 29
                NoAcrFrontNteExent == 0 && // 30
                NoAcrFrontNteNoObj == 0 && // 31
                NoAcrFrontSurProp == 0 && // 32
                NoAcrFrontSurNoReq == 0 && // 33
                NoAcrFrontSurExent == 0 && // 34
                NoAcrFrontSurNoObj == 0 && // 35
                NoAcrTasaGralNacProp == 0 && // 36
                NoAcrTasaGralNacNoReq == 0 && // 37
                NoAcrTasaGralNacExent == 0 && // 38
                NoAcrTasaGralNacNoObj == 0 && // 39
                NoAcrTasaGralIntProdsProp == 0 && // 40
                NoAcrTasaGralIntProdsNoReq == 0 && // 41
                NoAcrTasaGralIntProdsExent == 0 && // 42
                NoAcrTasaGralIntProdsNoObj == 0 && // 43
                NoAcrTasaGralIntServsProp == 0 && // 44
                NoAcrTasaGralIntServsNoReq == 0 && // 45
                NoAcrTasaGralIntServsExent == 0 && // 46
                NoAcrTasaGralIntServsNoObj == 0 && // 47

                DatIvaRetenido == 0 && // 48
                DatPagosExentInt == 0 && // 49
                DatPagosExentNac == 0 && // 50
                DatPagosTasa0Nac == 0 && // 51
                DatPagosNoObjNac == 0 && // 52
                DatPagosNoObjInt == 0; // 53
    }
    
    public void addTercero(final SDiotTercero tercero) {
        ValFrontNtePagos = SLibUtils.roundAmount(ValFrontNtePagos + tercero.ValFrontNtePagos); // 8
        ValFrontNteReembs = SLibUtils.roundAmount(ValFrontNteReembs + tercero.ValFrontNteReembs); // 9
        ValFrontSurPagos = SLibUtils.roundAmount(ValFrontSurPagos + tercero.ValFrontSurPagos); // 10
        ValFrontSurReembs = SLibUtils.roundAmount(ValFrontSurReembs + tercero.ValFrontSurReembs); // 11
        ValTasaGralNacPagos = SLibUtils.roundAmount(ValTasaGralNacPagos + tercero.ValTasaGralNacPagos); // 12
        ValTasaGralNacReembs = SLibUtils.roundAmount(ValTasaGralNacReembs + tercero.ValTasaGralNacReembs); // 13
        ValTasaGralIntProdsPagos = SLibUtils.roundAmount(ValTasaGralIntProdsPagos + tercero.ValTasaGralIntProdsPagos); // 14
        ValTasaGralIntProdsReembs = SLibUtils.roundAmount(ValTasaGralIntProdsReembs + tercero.ValTasaGralIntProdsReembs); // 15
        ValTasaGralIntServsPagos = SLibUtils.roundAmount(ValTasaGralIntServsPagos + tercero.ValTasaGralIntServsPagos); // 16
        ValTasaGralIntServsReembs = SLibUtils.roundAmount(ValTasaGralIntServsReembs + tercero.ValTasaGralIntServsReembs); // 17

        AcrFrontNteGravs = SLibUtils.roundAmount(AcrFrontNteGravs + tercero.AcrFrontNteGravs); // 18
        AcrFrontNteProp = SLibUtils.roundAmount(AcrFrontNteProp + tercero.AcrFrontNteProp); // 19
        AcrFrontSurGravs = SLibUtils.roundAmount(AcrFrontSurGravs + tercero.AcrFrontSurGravs); // 20
        AcrFrontSurProp = SLibUtils.roundAmount(AcrFrontSurProp + tercero.AcrFrontSurProp); // 21
        AcrTasaGralNacGravs = SLibUtils.roundAmount(AcrTasaGralNacGravs + tercero.AcrTasaGralNacGravs); // 22
        AcrTasaGralNacProp = SLibUtils.roundAmount(AcrTasaGralNacProp + tercero.AcrTasaGralNacProp); // 23
        AcrTasaGralIntProdsGravs = SLibUtils.roundAmount(AcrTasaGralIntProdsGravs + tercero.AcrTasaGralIntProdsGravs); // 24
        AcrTasaGralIntProdsProp = SLibUtils.roundAmount(AcrTasaGralIntProdsProp + tercero.AcrTasaGralIntProdsProp); // 25
        AcrTasaGralIntServsGravs = SLibUtils.roundAmount(AcrTasaGralIntServsGravs + tercero.AcrTasaGralIntServsGravs); // 26
        AcrTasaGralIntServsProp = SLibUtils.roundAmount(AcrTasaGralIntServsProp + tercero.AcrTasaGralIntServsProp); // 27

        NoAcrFrontNteProp = SLibUtils.roundAmount(NoAcrFrontNteProp + tercero.NoAcrFrontNteProp); // 28
        NoAcrFrontNteNoReq = SLibUtils.roundAmount(NoAcrFrontNteNoReq + tercero.NoAcrFrontNteNoReq); // 29
        NoAcrFrontNteExent = SLibUtils.roundAmount(NoAcrFrontNteExent + tercero.NoAcrFrontNteExent); // 30
        NoAcrFrontNteNoObj = SLibUtils.roundAmount(NoAcrFrontNteNoObj + tercero.NoAcrFrontNteNoObj); // 31
        NoAcrFrontSurProp = SLibUtils.roundAmount(NoAcrFrontSurProp + tercero.NoAcrFrontSurProp); // 32
        NoAcrFrontSurNoReq = SLibUtils.roundAmount(NoAcrFrontSurNoReq + tercero.NoAcrFrontSurNoReq); // 33
        NoAcrFrontSurExent = SLibUtils.roundAmount(NoAcrFrontSurExent + tercero.NoAcrFrontSurExent); // 34
        NoAcrFrontSurNoObj = SLibUtils.roundAmount(NoAcrFrontSurNoObj + tercero.NoAcrFrontSurNoObj); // 35
        NoAcrTasaGralNacProp = SLibUtils.roundAmount(NoAcrTasaGralNacProp + tercero.NoAcrTasaGralNacProp); // 36
        NoAcrTasaGralNacNoReq = SLibUtils.roundAmount(NoAcrTasaGralNacNoReq + tercero.NoAcrTasaGralNacNoReq); // 37
        NoAcrTasaGralNacExent = SLibUtils.roundAmount(NoAcrTasaGralNacExent + tercero.NoAcrTasaGralNacExent); // 38
        NoAcrTasaGralNacNoObj = SLibUtils.roundAmount(NoAcrTasaGralNacNoObj + tercero.NoAcrTasaGralNacNoObj); // 39
        NoAcrTasaGralIntProdsProp = SLibUtils.roundAmount(NoAcrTasaGralIntProdsProp + tercero.NoAcrTasaGralIntProdsProp); // 40
        NoAcrTasaGralIntProdsNoReq = SLibUtils.roundAmount(NoAcrTasaGralIntProdsNoReq + tercero.NoAcrTasaGralIntProdsNoReq); // 41
        NoAcrTasaGralIntProdsExent = SLibUtils.roundAmount(NoAcrTasaGralIntProdsExent + tercero.NoAcrTasaGralIntProdsExent); // 42
        NoAcrTasaGralIntProdsNoObj = SLibUtils.roundAmount(NoAcrTasaGralIntProdsNoObj + tercero.NoAcrTasaGralIntProdsNoObj); // 43
        NoAcrTasaGralIntServsProp = SLibUtils.roundAmount(NoAcrTasaGralIntServsProp + tercero.NoAcrTasaGralIntServsProp); // 44
        NoAcrTasaGralIntServsNoReq = SLibUtils.roundAmount(NoAcrTasaGralIntServsNoReq + tercero.NoAcrTasaGralIntServsNoReq); // 45
        NoAcrTasaGralIntServsExent = SLibUtils.roundAmount(NoAcrTasaGralIntServsExent + tercero.NoAcrTasaGralIntServsExent); // 46
        NoAcrTasaGralIntServsNoObj = SLibUtils.roundAmount(NoAcrTasaGralIntServsNoObj + tercero.NoAcrTasaGralIntServsNoObj); // 47

        DatIvaRetenido = SLibUtils.roundAmount(DatIvaRetenido + tercero.DatIvaRetenido); // 48
        DatPagosExentInt = SLibUtils.roundAmount(DatPagosExentInt + tercero.DatPagosExentInt); // 49
        DatPagosExentNac = SLibUtils.roundAmount(DatPagosExentNac + tercero.DatPagosExentNac); // 50
        DatPagosTasa0Nac = SLibUtils.roundAmount(DatPagosTasa0Nac + tercero.DatPagosTasa0Nac); // 51
        DatPagosNoObjNac = SLibUtils.roundAmount(DatPagosNoObjNac + tercero.DatPagosNoObjNac); // 52
        DatPagosNoObjInt = SLibUtils.roundAmount(DatPagosNoObjInt + tercero.DatPagosNoObjInt); // 53
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
        
        switch (formatType) {
            case SDiotConsts.FORMAT_PIPE:
                separator = "|";
                format = DecimalFormatPipe;
                row = TerTipoTercero + separator +
                        TerTipoOperación + separator +
                        TerRfc + separator +
                        TerExtIdFiscal + separator +
                        TerExtNombre + separator +
                        TerExtPaísResidencia + separator +
                        TerExtJurisdicción + separator;
                break;
                
            case SDiotConsts.FORMAT_CSV:
                separator = ",";
                format = DecimalFormatCsv;
                row = "\"" + TerTipoTercero + "\"" + separator +
                        "\"" + TerTipoOperación + "\"" + separator +
                        "\"" + TerRfc + "\"" + separator +
                        "\"" + TerExtIdFiscal + "\"" + separator +
                        "\"" + TerExtNombre + "\"" + separator +
                        "\"" + TerExtPaísResidencia + "\"" + separator +
                        "\"" + TerExtJurisdicción + "\"" + separator;
                break;
                
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return row +
                (ValFrontNtePagos == 0 ? "" : format.format(ValFrontNtePagos)) + separator + // 8
                (ValFrontNteReembs == 0) + separator + // 9
                (ValFrontSurPagos == 0) + separator + // 10
                (ValFrontSurReembs == 0) + separator + // 11
                (ValTasaGralNacPagos == 0) + separator + // 12
                (ValTasaGralNacReembs == 0) + separator + // 13
                (ValTasaGralIntProdsPagos == 0) + separator + // 14
                (ValTasaGralIntProdsReembs == 0) + separator + // 15
                (ValTasaGralIntServsPagos == 0) + separator + // 16
                (ValTasaGralIntServsReembs == 0) + separator + // 17

                (AcrFrontNteGravs == 0) + separator + // 18
                (AcrFrontNteProp == 0) + separator + // 19
                (AcrFrontSurGravs == 0) + separator + // 20
                (AcrFrontSurProp == 0) + separator + // 21
                (AcrTasaGralNacGravs == 0) + separator + // 22
                (AcrTasaGralNacProp == 0) + separator + // 23
                (AcrTasaGralIntProdsGravs == 0) + separator + // 24
                (AcrTasaGralIntProdsProp == 0) + separator + // 25
                (AcrTasaGralIntServsGravs == 0) + separator + // 26
                (AcrTasaGralIntServsProp == 0) + separator + // 27

                (NoAcrFrontNteProp == 0) + separator + // 28
                (NoAcrFrontNteNoReq == 0) + separator + // 29
                (NoAcrFrontNteExent == 0) + separator + // 30
                (NoAcrFrontNteNoObj == 0) + separator + // 31
                (NoAcrFrontSurProp == 0) + separator + // 32
                (NoAcrFrontSurNoReq == 0) + separator + // 33
                (NoAcrFrontSurExent == 0) + separator + // 34
                (NoAcrFrontSurNoObj == 0) + separator + // 35
                (NoAcrTasaGralNacProp == 0) + separator + // 36
                (NoAcrTasaGralNacNoReq == 0) + separator + // 37
                (NoAcrTasaGralNacExent == 0) + separator + // 38
                (NoAcrTasaGralNacNoObj == 0) + separator + // 39
                (NoAcrTasaGralIntProdsProp == 0) + separator + // 40
                (NoAcrTasaGralIntProdsNoReq == 0) + separator + // 41
                (NoAcrTasaGralIntProdsExent == 0) + separator + // 42
                (NoAcrTasaGralIntProdsNoObj == 0) + separator + // 43
                (NoAcrTasaGralIntServsProp == 0) + separator + // 44
                (NoAcrTasaGralIntServsNoReq == 0) + separator + // 45
                (NoAcrTasaGralIntServsExent == 0) + separator + // 46
                (NoAcrTasaGralIntServsNoObj == 0) + separator + // 47

                (DatIvaRetenido == 0) + separator + // 48
                (DatPagosExentInt == 0) + separator + // 49
                (DatPagosExentNac == 0) + separator + // 50
                (DatPagosTasa0Nac == 0) + separator + // 51
                (DatPagosNoObjNac == 0) + separator + // 52
                (DatPagosNoObjInt == 0) + separator + // 53
        
                (XValorPagosNacIva1516 == 0 ? "" : format.format(XValorPagosNacIva1516)) + separator +
                (XValorPagosNacIva15 == 0 ? "" : format.format(XValorPagosNacIva15)) + separator +
                (XIvaPagadoNoAcredNac1516 == 0 ? "" : format.format(XIvaPagadoNoAcredNac1516)) + separator +
                (XValorPagosNacIva1011 == 0 ? "" : format.format(XValorPagosNacIva1011)) + separator +
                (XValorPagosNacIva10 == 0 ? "" : format.format(XValorPagosNacIva10)) + separator +
                (XValorPagosNacIvaEstFront == 0 ? "" : format.format(XValorPagosNacIvaEstFront)) + separator +
                (XIvaPagadoNoAcredNac1011 == 0 ? "" : format.format(XIvaPagadoNoAcredNac1011)) + separator +
                (XIvaPagadoNoAcredNacEstFront == 0 ? "" : format.format(XIvaPagadoNoAcredNacEstFront)) + separator +
                (XValorPagosImpIva1516 == 0 ? "" : format.format(XValorPagosImpIva1516)) + separator +
                (XIvaPagadoNoAcredImp1516 == 0 ? "" : format.format(XIvaPagadoNoAcredImp1516)) + separator +
                (XValorPagosImpIva1011 == 0 ? "" : format.format(XValorPagosImpIva1011)) + separator +
                (XIvaPagadoNoAcredImp1011 == 0 ? "" : format.format(XIvaPagadoNoAcredImp1011)) + separator +
                (XValorPagosImpIvaExento == 0 ? "" : format.format(XValorPagosImpIvaExento)) + separator +
                (XValorPagosNacIva0 == 0 ? "" : format.format(XValorPagosNacIva0)) + separator +
                (XValorPagosNacIvaExento == 0 ? "" : format.format(XValorPagosNacIvaExento)) + separator +
                (XIvaRetenido == 0 ? "" : format.format(XIvaRetenido)) + separator +
                (XIvaNotasCréditoCompras == 0 ? "" : format.format(XIvaNotasCréditoCompras));
    }
    
    @Override
    public String toString() {
        String string = "";
        
        string += "ValorPagosNacIva1516: " + SLibUtils.getDecimalFormatAmount().format(XValorPagosNacIva1516) + "; "; // 8
        string += "ValorPagosNacIva15: " + SLibUtils.getDecimalFormatAmount().format(XValorPagosNacIva15) + "; "; // 9
        string += "IvaPagadoNoAcredNac1516: " + SLibUtils.getDecimalFormatAmount().format(XIvaPagadoNoAcredNac1516) + "; "; // 10
        string += "ValorPagosNacIva1011: " + SLibUtils.getDecimalFormatAmount().format(XValorPagosNacIva1011) + "; "; // 11
        string += "ValorPagosNacIva10: " + SLibUtils.getDecimalFormatAmount().format(XValorPagosNacIva10) + "; "; // 12
        string += "ValorPagosNacIvaEstFront: " + SLibUtils.getDecimalFormatAmount().format(XValorPagosNacIvaEstFront) + "; "; // 13
        string += "IvaPagadoNoAcredNac1011: " + SLibUtils.getDecimalFormatAmount().format(XIvaPagadoNoAcredNac1011) + "; "; // 14
        string += "IvaPagadoNoAcredNacEstFront: " + SLibUtils.getDecimalFormatAmount().format(XIvaPagadoNoAcredNacEstFront) + "; "; // 15
        string += "ValorPagosImpIva1516: " + SLibUtils.getDecimalFormatAmount().format(XValorPagosImpIva1516) + "; "; // 16
        string += "IvaPagadoNoAcredImp1516: " + SLibUtils.getDecimalFormatAmount().format(XIvaPagadoNoAcredImp1516) + "; "; // 17
        string += "ValorPagosImpIva1011: " + SLibUtils.getDecimalFormatAmount().format(XValorPagosImpIva1011) + "; "; // 18
        string += "IvaPagadoNoAcredImp1011: " + SLibUtils.getDecimalFormatAmount().format(XIvaPagadoNoAcredImp1011) + "; "; // 19
        string += "ValorPagosImpIvaExento: " + SLibUtils.getDecimalFormatAmount().format(XValorPagosImpIvaExento) + "; "; // 20
        string += "ValorPagosNacIva0: " + SLibUtils.getDecimalFormatAmount().format(XValorPagosNacIva0) + "; "; // 21
        string += "ValorPagosNacIvaExento: " + SLibUtils.getDecimalFormatAmount().format(XValorPagosNacIvaExento) + "; "; // 22
        string += "IvaRetenido: " + SLibUtils.getDecimalFormatAmount().format(XIvaRetenido) + "; "; // 23
        string += "IvaNotasCréditoCompras: " + SLibUtils.getDecimalFormatAmount().format(XIvaNotasCréditoCompras); // 24
        
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
        
        cloned.initTercero(this.IsGlobal, this.IsDomestic, this.BizPartnerId, this.TerTipoTercero, this.TerTipoOperación, this.TerRfc, this.TerExtIdFiscal, this.BizPartnerOccasionalRfc);

        cloned.XValorPagosNacIva1516 = this.XValorPagosNacIva1516; // 8
        cloned.XValorPagosNacIva15 = this.XValorPagosNacIva15; // 9
        cloned.XIvaPagadoNoAcredNac1516 = this.XIvaPagadoNoAcredNac1516; // 10
        cloned.XValorPagosNacIva1011 = this.XValorPagosNacIva1011; // 11
        cloned.XValorPagosNacIva10 = this.XValorPagosNacIva10; // 12
        cloned.XValorPagosNacIvaEstFront = this.XValorPagosNacIvaEstFront; // 13
        cloned.XIvaPagadoNoAcredNac1011 = this.XIvaPagadoNoAcredNac1011; // 14
        cloned.XIvaPagadoNoAcredNacEstFront = this.XIvaPagadoNoAcredNacEstFront; // 15
        cloned.XValorPagosImpIva1516 = this.XValorPagosImpIva1516; // 16
        cloned.XIvaPagadoNoAcredImp1516 = this.XIvaPagadoNoAcredImp1516; // 17
        cloned.XValorPagosImpIva1011 = this.XValorPagosImpIva1011; // 18
        cloned.XIvaPagadoNoAcredImp1011 = this.XIvaPagadoNoAcredImp1011; // 19
        cloned.XValorPagosImpIvaExento = this.XValorPagosImpIvaExento; // 20
        cloned.XValorPagosNacIva0 = this.XValorPagosNacIva0; // 21
        cloned.XValorPagosNacIvaExento = this.XValorPagosNacIvaExento; // 22
        cloned.XIvaRetenido = this.XIvaRetenido; // 23
        cloned.XIvaNotasCréditoCompras = this.XIvaNotasCréditoCompras; // 24
        
        return cloned;
    }
}
