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
 * There are four types of tercero:
 * 1. "empty tercero". Only for subtotals and totals in CSV DIOT. Not linked to any business partner. Type of third party: undefined. Type of operations: undefined.
 * 2. "global tercero". Not linked to any business partner. Type of third party: domestic. Type of operations: other.
 * 3. "standard tercero" for current company: Linked to current company. Type of third party: undefined. Type of operations: undefined.
 * 4. "standard tercero" for third party: Linked to some business partner. Type of third party: according to the business partner. Type of operations: according to the business partner.
 * @author Sergio Flores
 */
public class SDiotTercero implements Comparable<SDiotTercero> {

    /**
     * Supplier's business partner ID for an undefined one + '-' + code of DIOT other operations.
     */
    public static final String GLOBAL_CLAVE = 0 + "-" + SDiotConsts.OPER_OTHER;
    
    public boolean IsCurrentCompany;
    public String Clave;
    
    // Third party:
    
    public Boolean IsGlobal;
    public Boolean IsDomestic;
    public int BizPartnerId;
    public String OccasionalRfc; // RFC of occasional business partners that are not in catalog
    
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

                    TerExtNombre = bizPartner.getBizPartner();
                    TerExtPaísResidencia = countryDiotCode;
                    TerExtJurisdicción = "";
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

            TerTipoTercero = SDiotConsts.THIRD_UNDEF; // 1
            TerTipoOperación = SDiotConsts.OPER_UNDEF; // 2
            TerRfc = rfc; // 3
            TerExtIdFiscal = ""; // 4
            TerExtNombre = ""; // 5
            TerExtPaísResidencia = ""; // 6
            TerExtJurisdicción = ""; // 7
        }
        else {
            IsGlobal = isGlobal;
            IsDomestic = isDomestic;
            BizPartnerId = bizPartnerId;
            OccasionalRfc = occasionalRfc;

            TerTipoTercero = tipoTercero; // 1
            TerTipoOperación = tipoOperación; // 2
            TerRfc = rfc; // 3
            TerExtIdFiscal = extIdFiscal; // 4
            TerExtNombre = ""; // 5
            TerExtPaísResidencia = ""; // 6
            TerExtJurisdicción = ""; // 7
        }
        
        if (IsGlobal != null) {
            Clave = IsGlobal ? GLOBAL_CLAVE : ((isOccasional() ? OccasionalRfc : BizPartnerId) + "-" + TerTipoOperación);
        }
        else {
            Clave = ""; // no need for any "clave"
        }
        
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
    
    public String getComparableKey() {
        return
                TerTipoTercero + "-" + // 1
                TerTipoOperación + "-" + // 2
                TerRfc + "-" + // 3
                TerExtIdFiscal + "-" + // 4
                TerExtNombre + "-" + // 5
                TerExtPaísResidencia + "-" + // 6
                TerExtJurisdicción; // 7
    }
    
    public boolean isOccasional() {
        return !OccasionalRfc.isEmpty();
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
        String efectosFiscales = "";
        DecimalFormat format = null;
        
        // calculate VAT creditable:

        AcrFrontNteGravs = SLibUtils.roundAmount((ValFrontNtePagos - ValFrontNteReembs) * SDiotConsts.VatRates.get(SDiotConsts.VAT_TYPE_BORDER_N));
        AcrFrontSurGravs = SLibUtils.roundAmount((ValFrontSurPagos - ValFrontSurReembs) * SDiotConsts.VatRates.get(SDiotConsts.VAT_TYPE_BORDER_S));
        AcrTasaGralNacGravs = SLibUtils.roundAmount((ValTasaGralNacPagos - ValTasaGralNacReembs) * SDiotConsts.VatRates.get(SDiotConsts.VAT_TYPE_GENERAL));
        AcrTasaGralIntProdsGravs = SLibUtils.roundAmount((ValTasaGralIntProdsPagos - ValTasaGralIntProdsReembs) * SDiotConsts.VatRates.get(SDiotConsts.VAT_TYPE_GENERAL));
        AcrTasaGralIntServsGravs = SLibUtils.roundAmount((ValTasaGralIntServsPagos - ValTasaGralIntServsReembs) * SDiotConsts.VatRates.get(SDiotConsts.VAT_TYPE_GENERAL));
                
        // compose layout row:
                
        switch (formatType) {
            case SDiotConsts.FORMAT_PIPE:
                separator = "|";
                format = FormatPipe;
                row =   TerTipoTercero + separator + // 1
                        TerTipoOperación + separator + // 2
                        TerRfc + separator + // 3
                        TerExtIdFiscal + separator + // 4
                        TerExtNombre + separator + // 5
                        TerExtPaísResidencia + separator + // 6
                        TerExtJurisdicción + separator; // 7
                efectosFiscales = DatEfectosFiscales;  // 54
                break;
                
            case SDiotConsts.FORMAT_CSV:
                separator = ",";
                format = FormatCsv;
                row =   "\"" + TerTipoTercero + "\"" + separator + // 1
                        "\"" + TerTipoOperación + "\"" + separator + // 2
                        "\"" + TerRfc + "\"" + separator + // 3
                        "\"" + TerExtIdFiscal + "\"" + separator + // 4
                        "\"" + TerExtNombre + "\"" + separator + // 5
                        "\"" + TerExtPaísResidencia + "\"" + separator + // 6
                        "\"" + TerExtJurisdicción + "\"" + separator; // 7
                efectosFiscales = "\"" + DatEfectosFiscales + "\""; // 54
                break;
                
            default:
                throw new Exception(SLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return row +
                (ValFrontNtePagos == 0 ? "" : format.format(ValFrontNtePagos)) + separator + // 8
                (ValFrontNteReembs == 0 && ValFrontNteReembs == 0 ? "" : format.format(ValFrontNteReembs)) + separator + // 9
                (ValFrontSurPagos == 0 ? "" : format.format(ValFrontSurPagos)) + separator + // 10
                (ValFrontSurReembs == 0 && ValFrontSurReembs == 0 ? "" : format.format(ValFrontSurReembs)) + separator + // 11
                (ValTasaGralNacPagos == 0 ? "" : format.format(ValTasaGralNacPagos)) + separator + // 12
                (ValTasaGralNacReembs == 0 && ValTasaGralNacReembs == 0 ? "" : format.format(ValTasaGralNacReembs)) + separator + // 13
                (ValTasaGralIntProdsPagos == 0 ? "" : format.format(ValTasaGralIntProdsPagos)) + separator + // 14
                (ValTasaGralIntProdsReembs == 0 && ValTasaGralIntProdsReembs == 0 ? "" : format.format(ValTasaGralIntProdsReembs)) + separator + // 15
                (ValTasaGralIntServsPagos == 0 ? "" : format.format(ValTasaGralIntServsPagos)) + separator + // 16
                (ValTasaGralIntServsReembs == 0 && ValTasaGralIntServsReembs == 0 ? "" : format.format(ValTasaGralIntServsReembs)) + separator + // 17

                (AcrFrontNteGravs == 0 && AcrFrontNteGravs == 0 ? "" : format.format(AcrFrontNteGravs)) + separator + // 18
                (AcrFrontNteProp == 0 && AcrFrontNteProp == 0 ? "" : format.format(AcrFrontNteProp)) + separator + // 19
                (AcrFrontSurGravs == 0 && AcrFrontSurGravs == 0 ? "" : format.format(AcrFrontSurGravs)) + separator + // 20
                (AcrFrontSurProp == 0 && AcrFrontSurProp == 0 ? "" : format.format(AcrFrontSurProp)) + separator + // 21
                (AcrTasaGralNacGravs == 0 && AcrTasaGralNacGravs == 0 ? "" : format.format(AcrTasaGralNacGravs)) + separator + // 22
                (AcrTasaGralNacProp == 0 && AcrTasaGralNacProp == 0 ? "" : format.format(AcrTasaGralNacProp)) + separator + // 23
                (AcrTasaGralIntProdsGravs == 0 && AcrTasaGralIntProdsGravs == 0 ? "" : format.format(AcrTasaGralIntProdsGravs)) + separator + // 24
                (AcrTasaGralIntProdsProp == 0 && AcrTasaGralIntProdsProp == 0 ? "" : format.format(AcrTasaGralIntProdsProp)) + separator + // 25
                (AcrTasaGralIntServsGravs == 0 && AcrTasaGralIntServsGravs == 0 ? "" : format.format(AcrTasaGralIntServsGravs)) + separator + // 26
                (AcrTasaGralIntServsProp == 0 && AcrTasaGralIntServsProp == 0 ? "" : format.format(AcrTasaGralIntServsProp)) + separator + // 27

                (NoAcrFrontNteProp == 0 ? "" : format.format(NoAcrFrontNteProp)) + separator + // 28
                (NoAcrFrontNteNoReq == 0 ? "" : format.format(NoAcrFrontNteNoReq)) + separator + // 29
                (NoAcrFrontNteExent == 0 ? "" : format.format(NoAcrFrontNteExent)) + separator + // 30
                (NoAcrFrontNteNoObj == 0 ? "" : format.format(NoAcrFrontNteNoObj)) + separator + // 31
                (NoAcrFrontSurProp == 0 ? "" : format.format(NoAcrFrontSurProp)) + separator + // 32
                (NoAcrFrontSurNoReq == 0 ? "" : format.format(NoAcrFrontSurNoReq)) + separator + // 33
                (NoAcrFrontSurExent == 0 ? "" : format.format(NoAcrFrontSurExent)) + separator + // 34
                (NoAcrFrontSurNoObj == 0 ? "" : format.format(NoAcrFrontSurNoObj)) + separator + // 35
                (NoAcrTasaGralNacProp == 0 ? "" : format.format(NoAcrTasaGralNacProp)) + separator + // 36
                (NoAcrTasaGralNacNoReq == 0 ? "" : format.format(NoAcrTasaGralNacNoReq)) + separator + // 37
                (NoAcrTasaGralNacExent == 0 ? "" : format.format(NoAcrTasaGralNacExent)) + separator + // 38
                (NoAcrTasaGralNacNoObj == 0 ? "" : format.format(NoAcrTasaGralNacNoObj)) + separator + // 39
                (NoAcrTasaGralIntProdsProp == 0 ? "" : format.format(NoAcrTasaGralIntProdsProp)) + separator + // 40
                (NoAcrTasaGralIntProdsNoReq == 0 ? "" : format.format(NoAcrTasaGralIntProdsNoReq)) + separator + // 41
                (NoAcrTasaGralIntProdsExent == 0 ? "" : format.format(NoAcrTasaGralIntProdsExent)) + separator + // 42
                (NoAcrTasaGralIntProdsNoObj == 0 ? "" : format.format(NoAcrTasaGralIntProdsNoObj)) + separator + // 43
                (NoAcrTasaGralIntServsProp == 0 ? "" : format.format(NoAcrTasaGralIntServsProp)) + separator + // 44
                (NoAcrTasaGralIntServsNoReq == 0 ? "" : format.format(NoAcrTasaGralIntServsNoReq)) + separator + // 45
                (NoAcrTasaGralIntServsExent == 0 ? "" : format.format(NoAcrTasaGralIntServsExent)) + separator + // 46
                (NoAcrTasaGralIntServsNoObj == 0 ? "" : format.format(NoAcrTasaGralIntServsNoObj)) + separator + // 47

                (DatIvaRetenido == 0 ? "" : format.format(DatIvaRetenido)) + separator + // 48
                (DatPagosExentInt == 0 ? "" : format.format(DatPagosExentInt)) + separator + // 49
                (DatPagosExentNac == 0 ? "" : format.format(DatPagosExentNac)) + separator + // 50
                (DatPagosTasa0Nac == 0 ? "" : format.format(DatPagosTasa0Nac)) + separator + // 51
                (DatPagosNoObjNac == 0 ? "" : format.format(DatPagosNoObjNac)) + separator + // 52
                (DatPagosNoObjInt == 0 ? "" : format.format(DatPagosNoObjInt)) + separator + // 53
                efectosFiscales; // 54
    }
    
    @Override
    public int compareTo(SDiotTercero other) {
        return this.getComparableKey().compareTo(other.getComparableKey());
    }
    
    @Override
    public String toString() {
        String string = "";
        
        string += "ValFrontNtePagos: " + SLibUtils.getDecimalFormatAmount().format(ValFrontNtePagos) + "; "; // 8
        string += "ValFrontNteReembs: " + SLibUtils.getDecimalFormatAmount().format(ValFrontNteReembs) + "; "; // 9
        string += "ValFrontSurPagos: " + SLibUtils.getDecimalFormatAmount().format(ValFrontSurPagos) + "; "; // 10
        string += "ValFrontSurReembs: " + SLibUtils.getDecimalFormatAmount().format(ValFrontSurReembs) + "; "; // 11
        string += "ValTasaGralNacPagos: " + SLibUtils.getDecimalFormatAmount().format(ValTasaGralNacPagos) + "; "; // 12
        string += "ValTasaGralNacReembs: " + SLibUtils.getDecimalFormatAmount().format(ValTasaGralNacReembs) + "; "; // 13
        string += "ValTasaGralIntProdsPagos: " + SLibUtils.getDecimalFormatAmount().format(ValTasaGralIntProdsPagos) + "; "; // 14
        string += "ValTasaGralIntProdsReembs: " + SLibUtils.getDecimalFormatAmount().format(ValTasaGralIntProdsReembs) + "; "; // 15
        string += "ValTasaGralIntServsPagos: " + SLibUtils.getDecimalFormatAmount().format(ValTasaGralIntServsPagos) + "; "; // 16
        string += "ValTasaGralIntServsReembs: " + SLibUtils.getDecimalFormatAmount().format(ValTasaGralIntServsReembs) + "; "; // 17

        string += "AcrFrontNteGravs: " + SLibUtils.getDecimalFormatAmount().format(AcrFrontNteGravs) + "; "; // 18
        string += "AcrFrontNteProp: " + SLibUtils.getDecimalFormatAmount().format(AcrFrontNteProp) + "; "; // 19
        string += "AcrFrontSurGravs: " + SLibUtils.getDecimalFormatAmount().format(AcrFrontSurGravs) + "; "; // 20
        string += "AcrFrontSurProp: " + SLibUtils.getDecimalFormatAmount().format(AcrFrontSurProp) + "; "; // 21
        string += "AcrTasaGralNacGravs: " + SLibUtils.getDecimalFormatAmount().format(AcrTasaGralNacGravs) + "; "; // 22
        string += "AcrTasaGralNacProp: " + SLibUtils.getDecimalFormatAmount().format(AcrTasaGralNacProp) + "; "; // 23
        string += "AcrTasaGralIntProdsGravs: " + SLibUtils.getDecimalFormatAmount().format(AcrTasaGralIntProdsGravs) + "; "; // 24
        string += "AcrTasaGralIntProdsProp: " + SLibUtils.getDecimalFormatAmount().format(AcrTasaGralIntProdsProp) + "; "; // 25
        string += "AcrTasaGralIntServsGravs: " + SLibUtils.getDecimalFormatAmount().format(AcrTasaGralIntServsGravs) + "; "; // 26
        string += "AcrTasaGralIntServsProp: " + SLibUtils.getDecimalFormatAmount().format(AcrTasaGralIntServsProp) + "; "; // 27

        string += "NoAcrFrontNteProp: " + SLibUtils.getDecimalFormatAmount().format(NoAcrFrontNteProp) + "; "; // 28
        string += "NoAcrFrontNteNoReq: " + SLibUtils.getDecimalFormatAmount().format(NoAcrFrontNteNoReq) + "; "; // 29
        string += "NoAcrFrontNteExent: " + SLibUtils.getDecimalFormatAmount().format(NoAcrFrontNteExent) + "; "; // 30
        string += "NoAcrFrontNteNoObj: " + SLibUtils.getDecimalFormatAmount().format(NoAcrFrontNteNoObj) + "; "; // 31
        string += "NoAcrFrontSurProp: " + SLibUtils.getDecimalFormatAmount().format(NoAcrFrontSurProp) + "; "; // 32
        string += "NoAcrFrontSurNoReq: " + SLibUtils.getDecimalFormatAmount().format(NoAcrFrontSurNoReq) + "; "; // 33
        string += "NoAcrFrontSurExent: " + SLibUtils.getDecimalFormatAmount().format(NoAcrFrontSurExent) + "; "; // 34
        string += "NoAcrFrontSurNoObj: " + SLibUtils.getDecimalFormatAmount().format(NoAcrFrontSurNoObj) + "; "; // 35
        string += "NoAcrTasaGralNacProp: " + SLibUtils.getDecimalFormatAmount().format(NoAcrTasaGralNacProp) + "; "; // 36
        string += "NoAcrTasaGralNacNoReq: " + SLibUtils.getDecimalFormatAmount().format(NoAcrTasaGralNacNoReq) + "; "; // 37
        string += "NoAcrTasaGralNacExent: " + SLibUtils.getDecimalFormatAmount().format(NoAcrTasaGralNacExent) + "; "; // 38
        string += "NoAcrTasaGralNacNoObj: " + SLibUtils.getDecimalFormatAmount().format(NoAcrTasaGralNacNoObj) + "; "; // 39
        string += "NoAcrTasaGralIntProdsProp: " + SLibUtils.getDecimalFormatAmount().format(NoAcrTasaGralIntProdsProp) + "; "; // 40
        string += "NoAcrTasaGralIntProdsNoReq: " + SLibUtils.getDecimalFormatAmount().format(NoAcrTasaGralIntProdsNoReq) + "; "; // 41
        string += "NoAcrTasaGralIntProdsExent: " + SLibUtils.getDecimalFormatAmount().format(NoAcrTasaGralIntProdsExent) + "; "; // 42
        string += "NoAcrTasaGralIntProdsNoObj: " + SLibUtils.getDecimalFormatAmount().format(NoAcrTasaGralIntProdsNoObj) + "; "; // 43
        string += "NoAcrTasaGralIntServsProp: " + SLibUtils.getDecimalFormatAmount().format(NoAcrTasaGralIntServsProp) + "; "; // 44
        string += "NoAcrTasaGralIntServsNoReq: " + SLibUtils.getDecimalFormatAmount().format(NoAcrTasaGralIntServsNoReq) + "; "; // 45
        string += "NoAcrTasaGralIntServsExent: " + SLibUtils.getDecimalFormatAmount().format(NoAcrTasaGralIntServsExent) + "; "; // 46
        string += "NoAcrTasaGralIntServsNoObj: " + SLibUtils.getDecimalFormatAmount().format(NoAcrTasaGralIntServsNoObj) + "; "; // 47

        string += "DatIvaRetenido: " + SLibUtils.getDecimalFormatAmount().format(DatIvaRetenido) + "; "; // 48
        string += "DatPagosExentInt: " + SLibUtils.getDecimalFormatAmount().format(DatPagosExentInt) + "; "; // 49
        string += "DatPagosExentNac: " + SLibUtils.getDecimalFormatAmount().format(DatPagosExentNac) + "; "; // 50
        string += "DatPagosTasa0Nac: " + SLibUtils.getDecimalFormatAmount().format(DatPagosTasa0Nac) + "; "; // 51
        string += "DatPagosNoObjNac: " + SLibUtils.getDecimalFormatAmount().format(DatPagosNoObjNac) + "; "; // 52
        string += "DatPagosNoObjInt: " + SLibUtils.getDecimalFormatAmount().format(DatPagosNoObjInt) + "; "; // 53
        string += "DatEfectosFiscales: " + DatEfectosFiscales; // 54
        
        return string;
    }

    @Override
    public SDiotTercero clone() {
        SDiotTercero cloned = new SDiotTercero();
        
        cloned.initTercero(this.IsCurrentCompany, this.IsGlobal, this.IsDomestic, this.BizPartnerId, this.TerTipoTercero, this.TerTipoOperación, this.TerRfc, this.TerExtIdFiscal, this.OccasionalRfc);
        
        cloned.TerExtNombre = this.TerExtNombre; // 5
        cloned.TerExtPaísResidencia = this.TerExtPaísResidencia; // 6
        cloned.TerExtJurisdicción = this.TerExtJurisdicción; // 7

        cloned.ValFrontNtePagos = this.ValFrontNtePagos; // 8
        cloned.ValFrontNteReembs = this.ValFrontNteReembs; // 9
        cloned.ValFrontSurPagos = this.ValFrontSurPagos; // 10
        cloned.ValFrontSurReembs = this.ValFrontSurReembs; // 11
        cloned.ValTasaGralNacPagos = this.ValTasaGralNacPagos; // 12
        cloned.ValTasaGralNacReembs = this.ValTasaGralNacReembs; // 13
        cloned.ValTasaGralIntProdsPagos = this.ValTasaGralIntProdsPagos; // 14
        cloned.ValTasaGralIntProdsReembs = this.ValTasaGralIntProdsReembs; // 15
        cloned.ValTasaGralIntServsPagos = this.ValTasaGralIntServsPagos; // 16
        cloned.ValTasaGralIntServsReembs = this.ValTasaGralIntServsReembs; // 17

        cloned.AcrFrontNteGravs = this.AcrFrontNteGravs; // 18
        cloned.AcrFrontNteProp = this.AcrFrontNteProp; // 19
        cloned.AcrFrontSurGravs = this.AcrFrontSurGravs; // 20
        cloned.AcrFrontSurProp = this.AcrFrontSurProp; // 21
        cloned.AcrTasaGralNacGravs = this.AcrTasaGralNacGravs; // 22
        cloned.AcrTasaGralNacProp = this.AcrTasaGralNacProp; // 23
        cloned.AcrTasaGralIntProdsGravs = this.AcrTasaGralIntProdsGravs; // 24
        cloned.AcrTasaGralIntProdsProp = this.AcrTasaGralIntProdsProp; // 25
        cloned.AcrTasaGralIntServsGravs = this.AcrTasaGralIntServsGravs; // 26
        cloned.AcrTasaGralIntServsProp = this.AcrTasaGralIntServsProp; // 27

        cloned.NoAcrFrontNteProp = this.NoAcrFrontNteProp; // 28
        cloned.NoAcrFrontNteNoReq = this.NoAcrFrontNteNoReq; // 29
        cloned.NoAcrFrontNteExent = this.NoAcrFrontNteExent; // 30
        cloned.NoAcrFrontNteNoObj = this.NoAcrFrontNteNoObj; // 31
        cloned.NoAcrFrontSurProp = this.NoAcrFrontSurProp; // 32
        cloned.NoAcrFrontSurNoReq = this.NoAcrFrontSurNoReq; // 33
        cloned.NoAcrFrontSurExent = this.NoAcrFrontSurExent; // 34
        cloned.NoAcrFrontSurNoObj = this.NoAcrFrontSurNoObj; // 35
        cloned.NoAcrTasaGralNacProp = this.NoAcrTasaGralNacProp; // 36
        cloned.NoAcrTasaGralNacNoReq = this.NoAcrTasaGralNacNoReq; // 37
        cloned.NoAcrTasaGralNacExent = this.NoAcrTasaGralNacExent; // 38
        cloned.NoAcrTasaGralNacNoObj = this.NoAcrTasaGralNacNoObj; // 39
        cloned.NoAcrTasaGralIntProdsProp = this.NoAcrTasaGralIntProdsProp; // 40
        cloned.NoAcrTasaGralIntProdsNoReq = this.NoAcrTasaGralIntProdsNoReq; // 41
        cloned.NoAcrTasaGralIntProdsExent = this.NoAcrTasaGralIntProdsExent; // 42
        cloned.NoAcrTasaGralIntProdsNoObj = this.NoAcrTasaGralIntProdsNoObj; // 43
        cloned.NoAcrTasaGralIntServsProp = this.NoAcrTasaGralIntServsProp; // 44
        cloned.NoAcrTasaGralIntServsNoReq = this.NoAcrTasaGralIntServsNoReq; // 45
        cloned.NoAcrTasaGralIntServsExent = this.NoAcrTasaGralIntServsExent; // 46
        cloned.NoAcrTasaGralIntServsNoObj = this.NoAcrTasaGralIntServsNoObj; // 47

        cloned.DatIvaRetenido = this.DatIvaRetenido; // 48
        cloned.DatPagosExentInt = this.DatPagosExentInt; // 49
        cloned.DatPagosExentNac = this.DatPagosExentNac; // 50
        cloned.DatPagosTasa0Nac = this.DatPagosTasa0Nac; // 51
        cloned.DatPagosNoObjNac = this.DatPagosNoObjNac; // 52
        cloned.DatPagosNoObjInt = this.DatPagosNoObjInt; // 53
        cloned.DatEfectosFiscales = this.DatEfectosFiscales; // 54
        
        return cloned;
    }
    
    public static String composeOccasionalClave(final String occasionalBizPartnerRfc) {
        return occasionalBizPartnerRfc + "-" + SDiotConsts.OPER_OTHER;
    }
    
    public static String getLayoutCsvHeadings() {
        return
                "\"1. Tipo de tercero\"," +
                "\"2. Tipo de operación\"," +
                "\"3. Registro Federal de Contribuyentes\"," +
                "\"4. Número de identificación fiscal\"," +
                "\"5. Nombre del extranjero\"," +
                "\"6. País o jurisdicción de residencia fiscal\"," +
                "\"7. Especificar lugar de jurisdicción fiscal\"," +
                
                "\"8. Valor total de actos o actividades pagadas/ Región fronteriza norte\"," +
                "\"9. Devoluciones, descuentos y bonificaciones/ Región fronteriza norte\"," +
                "\"10. Valor total de actos o actividades pagadas/ Región fronteriza sur\"," +
                "\"11. Devoluciones, descuentos y bonificaciones/ Región fronteriza sur\"," +
                "\"12. Valor total de actos o actividades pagadas/ Tasa del 16% de IVA\"," +
                "\"13. Devoluciones, descuentos y bonificaciones/ Tasa del 16% de IVA\"," +
                "\"14. Valor total de actos o actividades pagadas/ Importación por aduana de bienes tangibles a la tasa del 16% de IVA\"," +
                "\"15. Devoluciones, descuentos y bonificaciones/ Importación por aduana de bienes tangibles a la tasa del 16% de IVA\"," +
                "\"16. Valor total de actos o actividades pagadas/ Importación por aduana de bienes intangibles y servicios a la tasa del 16% de IVA\"," +
                "\"17. Devoluciones, descuentos y bonificaciones/ Importación por aduana de bienes intangibles y servicios a la tasa del 16% de IVA\"," +
                
                "\"18. (IVA acr.) Exclusivamente de actividades gravadas/ Región fronteriza norte\"," +
                "\"19. (IVA acr.) Asociado a actividades por las cuales se aplicó una proporción/ Región fronteriza norte\"," +
                "\"20. (IVA acr.) Exclusivamente de actividades gravadas/ Región fronteriza sur\"," +
                "\"21. (IVA acr.) Asociado a actividades por las cuales se aplicó una proporción/ Región fronteriza sur\"," +
                "\"22. (IVA acr.) Exclusivamente de actividades gravadas/ Tasa del 16% de IVA\"," +
                "\"23. (IVA acr.) Asociado a actividades por las cuales se aplicó una proporción/ Tasa del 16% de IVA\"," +
                "\"24. (IVA acr.) Exclusivamente de actividades gravadas/ Importación por aduana de bienes tangibles a la tasa del 16% de IVA\"," +
                "\"25. (IVA acr.) Asociado a actividades por las cuales se aplicó una proporción/ Importación por aduana de bienes tangibles a la tasa del 16% de IVA\"," +
                "\"26. (IVA acr.) Exclusivamente de actividades gravadas/ Importación por aduana de bienes intangibles y servicios a la tasa del 16% de IVA\"," +
                "\"27. (IVA acr.) Asociado a actividades por las cuales se aplicó una proporción/ Importación por aduana de bienes intangibles y servicios a la tasa del 16% de IVA\"," +
                
                "\"28. (IVA no acr.) Asociado a actividades por las cuales se aplicó una proporción/ Región fronteriza norte\"," +
                "\"29. (IVA no acr.) Asociado a actividades que no cumple con requisitos/ Región fronteriza norte\"," +
                "\"30. (IVA no acr.) Asociado a actividades exentas/ Región fronteriza norte\"," +
                "\"31. (IVA no acr.) Asociado a actividades no objeto/ Región fronteriza norte\"," +
                "\"32. (IVA no acr.) Asociado a actividades por las cuales se aplicó una proporción/ Región fronteriza sur\"," +
                "\"33. (IVA no acr.) Asociado a actividades que no cumple con requisitos/ Región fronteriza sur\"," +
                "\"34. (IVA no acr.) Asociado a actividades exentas/ Región fronteriza sur\"," +
                "\"35. (IVA no acr.) Asociado a actividades no objeto/ Región fronteriza sur\"," +
                "\"36. (IVA no acr.) Asociado a actividades por las cuales se aplicó una proporción\"," +
                "\"37. (IVA no acr.) Asociado a actividades que no cumple con requisitos/ Tasa del 16% de IVA\"," +
                "\"38. (IVA no acr.) Asociado a actividades exentas/ Tasa del 16% de IVA\"," +
                "\"39. (IVA no acr.) Asociado a actividades no objeto/ Tasa del 16% de IVA\"," +
                "\"40. (IVA no acr.) Asociado a actividades por las cuales se aplicó una proporción/ Tasa del 16% de IVA\"," +
                "\"41. (IVA no acr.) Asociado a actividades que no cumple con requisitos/ Importación por aduana de bienes tangibles a la tasa del 16% de IVA\"," +
                "\"42. (IVA no acr.) Asociado a actividades exentas/ Importación por aduana de bienes tangibles a la tasa del 16% de IVA\"," +
                "\"43. (IVA no acr.) Asociado a actividades no objeto/ Importación por aduana de bienes tangibles a la tasa del 16% de IVA\"," +
                "\"44. (IVA no acr.) Asociado a actividades por las cuales se aplicó una proporción/ Importación por aduana de bienes tangibles a la tasa del 16% de IVA\"," +
                "\"45. (IVA no acr.) Asociado a actividades que no cumple con requisitos/ Importación por aduana de bienes intangibles y servicios a la tasa del 16% de IVA\"," +
                "\"46. (IVA no acr.) Asociado a actividades exentas/ Importación por aduana de bienes intangibles y servicios a la tasa del 16% de IVA\"," +
                "\"47. (IVA no acr.) Asociado a actividades no objeto/ Importación por aduana de bienes intangibles y servicios a la tasa del 16% de IVA\"," +
                
                "\"48. IVA retenido por el contribuyente\"," +
                "\"49. Actos o actividades pagados en la importación de bienes y servicios por los que no se pagará el IVA (Exentos)\"," +
                "\"50. Actos o actividades pagados por los que no se pagará el IVA (Exentos)\"," +
                "\"51. Demás actos o actividades pagados a la tasa del 0% de IVA\"," +
                "\"52. Actos o actividades no objeto del IVA realizados en territorio nacional\"," +
                "\"53. Actos o actividades no objeto del IVA por no contar con establecimiento en territorio nacional\"," +
                "\"54. Manifiesto que se dio efectos fiscales a los comprobantes que amparan las operaciones realizadas con el proveedor\"";
    }
}
