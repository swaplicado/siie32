/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd;

import cfd.DElement;

/**
 *
 * @author Juan Barajas
 */
public class SCfdDataAsociadoNegocios {

    protected int mnBizPartnerId;
    protected int mnBizPartnerBranchId;
    protected int mnBizPartnerBranchHqId;
    protected String msBizPartnerRfc;
    protected String msBizPartnerName;
    protected String msBizPartnerStreet;
    protected String msBizPartnerStreetNumberExt;
    protected String msBizPartnerStreetNumberInt;
    protected String msBizPartnerNeighborhood;
    protected String msBizPartnerReference;
    protected String msBizPartnerLocality;
    protected String msBizPartnerCounty;
    protected String msBizPartnerState;
    protected String msBizPartnerZipCode;
    protected String msBizPartnerPoBox;
    protected String msBizPartnerCountry;
    protected String msBizPartnerExpeditionStreet;
    protected String msBizPartnerExpeditionStreetNumberExt;
    protected String msBizPartnerExpeditionStreetNumberInt;
    protected String msBizPartnerExpeditionNeighborhood;
    protected String msBizPartnerExpeditionReference;
    protected String msBizPartnerExpeditionLocality;
    protected String msBizPartnerExpeditionCounty;
    protected String msBizPartnerExpeditionState;
    protected String msBizPartnerExpeditionZipCode;
    protected String msBizPartnerExpeditionPoBox;
    protected String msBizPartnerExpeditionCountry;
    protected String msBizPartnerFiscalRegime;
    protected boolean mbIsCfdi;
    protected float mfVersion;
    protected int mnCfdiType;

    public SCfdDataAsociadoNegocios() {
        mnBizPartnerId = 0;
        mnBizPartnerBranchId = 0;
        mnBizPartnerBranchHqId = 0;
        msBizPartnerRfc = "";
        msBizPartnerName = "";
        msBizPartnerStreet = "";
        msBizPartnerStreetNumberExt = "";
        msBizPartnerStreetNumberInt = "";
        msBizPartnerNeighborhood = "";
        msBizPartnerReference = "";
        msBizPartnerLocality = "";
        msBizPartnerCounty = "";
        msBizPartnerState = "";
        msBizPartnerZipCode = "";
        msBizPartnerPoBox = "";
        msBizPartnerCountry = "";
        msBizPartnerExpeditionStreet = "";
        msBizPartnerExpeditionStreetNumberExt = "";
        msBizPartnerExpeditionStreetNumberInt = "";
        msBizPartnerExpeditionNeighborhood = "";
        msBizPartnerExpeditionReference = "";
        msBizPartnerExpeditionLocality = "";
        msBizPartnerExpeditionCounty = "";
        msBizPartnerExpeditionState = "";
        msBizPartnerExpeditionZipCode = "";
        msBizPartnerExpeditionPoBox = "";
        msBizPartnerExpeditionCountry = "";
        msBizPartnerFiscalRegime = "";
        mbIsCfdi = false;
        mfVersion = 0f;
        mnCfdiType = 0;
    }

    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartnerBranchId(int n) { mnBizPartnerBranchId = n; }
    public void setBizPartnerBranchHqId(int n) { mnBizPartnerBranchHqId = n; }
    public void setBizPartnerRfc(String s) { msBizPartnerRfc = s; }
    public void setBizPartnerName(String s) { msBizPartnerName = s; }
    public void setBizPartnerStreet(String s) { msBizPartnerStreet = s; }
    public void setBizPartnerStreetNumberExt(String s) { msBizPartnerStreetNumberExt = s; }
    public void setBizPartnerStreetNumberInt(String s) { msBizPartnerStreetNumberInt = s; }
    public void setBizPartnerNeighborhood(String s) { msBizPartnerNeighborhood = s; }
    public void setBizPartnerReference(String s) { msBizPartnerReference = s; }
    public void setBizPartnerLocality(String s) { msBizPartnerLocality = s; }
    public void setBizPartnerCounty(String s) { msBizPartnerCounty = s; }
    public void setBizPartnerState(String s) { msBizPartnerState = s; }
    public void setBizPartnerZipCode(String s) { msBizPartnerZipCode = s; }
    public void setBizPartnerPoBox(String s) { msBizPartnerPoBox = s; }
    public void setBizPartnerCountry(String s) { msBizPartnerCountry = s; }
    public void setBizPartnerExpeditionStreet(String s) { msBizPartnerExpeditionStreet = s; }
    public void setBizPartnerExpeditionStreetNumberExt(String s) { msBizPartnerExpeditionStreetNumberExt = s; }
    public void setBizPartnerExpeditionStreetNumberInt(String s) { msBizPartnerExpeditionStreetNumberInt = s; }
    public void setBizPartnerExpeditionNeighborhood(String s) { msBizPartnerExpeditionNeighborhood = s; }
    public void setBizPartnerExpeditionReference(String s) { msBizPartnerExpeditionReference = s; }
    public void setBizPartnerExpeditionLocality(String s) { msBizPartnerExpeditionLocality = s; }
    public void setBizPartnerExpeditionCounty(String s) { msBizPartnerExpeditionCounty = s; }
    public void setBizPartnerExpeditionState(String s) { msBizPartnerExpeditionState = s; }
    public void setBizPartnerExpeditionZipCode(String s) { msBizPartnerExpeditionZipCode = s; }
    public void setBizPartnerExpeditionPoBox(String s) { msBizPartnerExpeditionPoBox = s; }
    public void setBizPartnerExpeditionCountry(String s) { msBizPartnerExpeditionCountry = s; }
    public void setBizPartnerFiscalRegime(String s) { msBizPartnerFiscalRegime= s; }
    public void setIsCfdi(boolean b) { mbIsCfdi = b; }
    public void setVersion(float f) { mfVersion = f; }
    public void setCfdiType(int n) { mnCfdiType = n; }

    public int getBizPartnerId() { return mnBizPartnerId; }
    public int getBizPartnerBranchId() { return mnBizPartnerBranchId; }
    public int getBizPartnerBranchHqId() { return mnBizPartnerBranchHqId; }
    public String getBizPartnerRfc() { return msBizPartnerRfc; }
    public String getBizPartnerName() { return msBizPartnerName; }
    public String getBizPartnerStreet() { return msBizPartnerStreet; }
    public String getBizPartnerStreetNumberExt() { return msBizPartnerStreetNumberExt; }
    public String getBizPartnerStreetNumberInt() { return msBizPartnerStreetNumberInt; }
    public String getBizPartnerNeighborhood() { return msBizPartnerNeighborhood; }
    public String getBizPartnerReference() { return msBizPartnerReference; }
    public String getBizPartnerLocality() { return msBizPartnerLocality; }
    public String getBizPartnerCounty() { return msBizPartnerCounty; }
    public String getBizPartnerState() { return msBizPartnerState; }
    public String getBizPartnerZipCode() { return msBizPartnerZipCode; }
    public String getBizPartnerPoBox() { return msBizPartnerPoBox; }
    public String getBizPartnerCountry() { return msBizPartnerCountry; }
    public String getBizPartnerExpeditionStreet() { return msBizPartnerExpeditionStreet; }
    public String getBizPartnerExpeditionStreetNumberExt() { return msBizPartnerExpeditionStreetNumberExt; }
    public String getBizPartnerExpeditionStreetNumberInt() { return msBizPartnerExpeditionStreetNumberInt; }
    public String getBizPartnerExpeditionNeighborhood() { return msBizPartnerExpeditionNeighborhood; }
    public String getBizPartnerExpeditionReference() { return msBizPartnerExpeditionReference; }
    public String getBizPartnerExpeditionLocality() { return msBizPartnerExpeditionLocality; }
    public String getBizPartnerExpeditionCounty() { return msBizPartnerExpeditionCounty; }
    public String getBizPartnerExpeditionState() { return msBizPartnerExpeditionState; }
    public String getBizPartnerExpeditionZipCode() { return msBizPartnerExpeditionZipCode; }
    public String getBizPartnerExpeditionPoBox() { return msBizPartnerExpeditionPoBox; }
    public String getBizPartnerExpeditionCountry() { return msBizPartnerExpeditionCountry; }
    public String getBizPartnerFiscalRegime() { return msBizPartnerFiscalRegime; }
    public boolean getIsCfdi() { return mbIsCfdi; }
    public float getVersion() { return mfVersion; }
    public int getCfdiType() { return mnCfdiType; }

    /**
     * Create element root for emisor with expedition spot for the business partner indicated
     * @param bizPartner business partner the expedition spot
     * @param bizPartnerBranch business partner branch the expedition spot
     * @param version version of XML
     * @return cfd.DElement node cfd.DElement for emisor
     * @throws java.lang.Exception
     * @throws java.lang.Exception
     */
    public cfd.DElement createRootElementEmisor() throws java.lang.Exception {
        DElement emisor = null;

        if (mbIsCfdi) {
            emisor = new cfd.ver3.DElementEmisor();

            ((cfd.ver3.DElementEmisor) emisor).getAttRfc().setString(msBizPartnerRfc);
            ((cfd.ver3.DElementEmisor) emisor).getAttNombre().setString(msBizPartnerName);
            
            if (mnCfdiType == SCfdConsts.CFD_TYPE_PAYROLL) {
                ((cfd.ver3.DElementEmisor) emisor).clearEltDomicilioFiscal();
            }
            else {
                ((cfd.ver3.DElementEmisor) emisor).getEltDomicilioFiscal().getAttCalle().setString(msBizPartnerStreet);
                ((cfd.ver3.DElementEmisor) emisor).getEltDomicilioFiscal().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
                ((cfd.ver3.DElementEmisor) emisor).getEltDomicilioFiscal().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
                ((cfd.ver3.DElementEmisor) emisor).getEltDomicilioFiscal().getAttColonia().setString(msBizPartnerNeighborhood);
                ((cfd.ver3.DElementEmisor) emisor).getEltDomicilioFiscal().getAttLocalidad().setString(msBizPartnerLocality);
                ((cfd.ver3.DElementEmisor) emisor).getEltDomicilioFiscal().getAttReferencia().setString(msBizPartnerReference);
                ((cfd.ver3.DElementEmisor) emisor).getEltDomicilioFiscal().getAttMunicipio().setString(msBizPartnerCounty);
                ((cfd.ver3.DElementEmisor) emisor).getEltDomicilioFiscal().getAttEstado().setString(msBizPartnerState);
                ((cfd.ver3.DElementEmisor) emisor).getEltDomicilioFiscal().getAttCodigoPostal().setString(msBizPartnerZipCode);
                ((cfd.ver3.DElementEmisor) emisor).getEltDomicilioFiscal().getAttPais().setString(msBizPartnerCountry);
            }

            if (mnBizPartnerBranchHqId != mnBizPartnerBranchId && mnCfdiType != SCfdConsts.CFD_TYPE_PAYROLL) {
                ((cfd.ver3.DElementEmisor) emisor).setEltOpcExpedidoEn(new cfd.ver3.DElementTipoUbicacion("cfdi:ExpedidoEn"));
                ((cfd.ver3.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttCalle().setString(msBizPartnerExpeditionStreet);
                ((cfd.ver3.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttNoExterior().setString(msBizPartnerExpeditionStreetNumberExt);
                ((cfd.ver3.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttNoInterior().setString(msBizPartnerExpeditionStreetNumberInt);
                ((cfd.ver3.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttColonia().setString(msBizPartnerExpeditionNeighborhood);
                ((cfd.ver3.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttLocalidad().setString(msBizPartnerExpeditionLocality);
                ((cfd.ver3.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttReferencia().setString(msBizPartnerExpeditionReference);
                ((cfd.ver3.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttMunicipio().setString(msBizPartnerExpeditionCounty);
                ((cfd.ver3.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttEstado().setString(msBizPartnerExpeditionState);
                ((cfd.ver3.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttCodigoPostal().setString(msBizPartnerExpeditionZipCode);
                ((cfd.ver3.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttPais().setString(msBizPartnerExpeditionCountry);
            }
        }
        else {
            emisor = new cfd.ver2.DElementEmisor(mfVersion);

            ((cfd.ver2.DElementEmisor) emisor).getAttRfc().setString(msBizPartnerRfc);
            ((cfd.ver2.DElementEmisor) emisor).getAttNombre().setString(msBizPartnerName);
            ((cfd.ver2.DElementEmisor) emisor).getEltDomicilioFiscal().getAttCalle().setString(msBizPartnerStreet);
            ((cfd.ver2.DElementEmisor) emisor).getEltDomicilioFiscal().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
            ((cfd.ver2.DElementEmisor) emisor).getEltDomicilioFiscal().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
            ((cfd.ver2.DElementEmisor) emisor).getEltDomicilioFiscal().getAttColonia().setString(msBizPartnerNeighborhood);
            ((cfd.ver2.DElementEmisor) emisor).getEltDomicilioFiscal().getAttLocalidad().setString(msBizPartnerLocality);
            ((cfd.ver2.DElementEmisor) emisor).getEltDomicilioFiscal().getAttReferencia().setString(msBizPartnerReference);
            ((cfd.ver2.DElementEmisor) emisor).getEltDomicilioFiscal().getAttMunicipio().setString(msBizPartnerCounty);
            ((cfd.ver2.DElementEmisor) emisor).getEltDomicilioFiscal().getAttEstado().setString(msBizPartnerState);
            ((cfd.ver2.DElementEmisor) emisor).getEltDomicilioFiscal().getAttCodigoPostal().setString(msBizPartnerZipCode);
            ((cfd.ver2.DElementEmisor) emisor).getEltDomicilioFiscal().getAttPais().setString(msBizPartnerCountry);

            if (mnBizPartnerBranchHqId != mnBizPartnerBranchId) {
                ((cfd.ver2.DElementEmisor) emisor).setEltOpcExpedidoEn(new cfd.ver2.DElementTipoUbicacion("ExpedidoEn"));
                ((cfd.ver2.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttCalle().setString(msBizPartnerExpeditionStreet);
                ((cfd.ver2.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttNoExterior().setString(msBizPartnerExpeditionStreetNumberExt);
                ((cfd.ver2.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttNoInterior().setString(msBizPartnerExpeditionStreetNumberInt);
                ((cfd.ver2.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttColonia().setString(msBizPartnerExpeditionNeighborhood);
                ((cfd.ver2.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttLocalidad().setString(msBizPartnerExpeditionLocality);
                ((cfd.ver2.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttReferencia().setString(msBizPartnerExpeditionReference);
                ((cfd.ver2.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttMunicipio().setString(msBizPartnerExpeditionCounty);
                ((cfd.ver2.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttEstado().setString(msBizPartnerExpeditionState);
                ((cfd.ver2.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttCodigoPostal().setString(msBizPartnerExpeditionZipCode);
                ((cfd.ver2.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttPais().setString(msBizPartnerExpeditionCountry);
            }
        }

        return emisor;
    }

    /**
     * Create element root for receptor with expedition spot for the business partner indicated
     * @param bizPartner business partner the expedition spot
     * @param bizPartnerBranch business partner branch the expedition spot
     * @param version version of XML
     * @return cfd.DElement node cfd.DElement for receptor
     * @throws java.lang.Exception
     * @throws java.lang.Exception
     */
    public cfd.DElement createRootElementReceptor() throws java.lang.Exception {
        DElement receptor = null;

        if (mbIsCfdi) {
            receptor = new cfd.ver3.DElementReceptor();

            ((cfd.ver3.DElementReceptor) receptor).getAttRfc().setString(msBizPartnerRfc);
            ((cfd.ver3.DElementReceptor) receptor).getAttNombre().setString(msBizPartnerName);
            
            if (mnCfdiType == SCfdConsts.CFD_TYPE_PAYROLL) {
                ((cfd.ver3.DElementReceptor) receptor).clearEltDomicilio();
            }
            else {
                ((cfd.ver3.DElementReceptor) receptor).getEltDomicilio().getAttCalle().setString(msBizPartnerStreet);
                ((cfd.ver3.DElementReceptor) receptor).getEltDomicilio().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
                ((cfd.ver3.DElementReceptor) receptor).getEltDomicilio().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
                ((cfd.ver3.DElementReceptor) receptor).getEltDomicilio().getAttColonia().setString(msBizPartnerNeighborhood);
                ((cfd.ver3.DElementReceptor) receptor).getEltDomicilio().getAttLocalidad().setString(msBizPartnerLocality);
                ((cfd.ver3.DElementReceptor) receptor).getEltDomicilio().getAttReferencia().setString(msBizPartnerReference);
                ((cfd.ver3.DElementReceptor) receptor).getEltDomicilio().getAttMunicipio().setString(msBizPartnerCounty);
                ((cfd.ver3.DElementReceptor) receptor).getEltDomicilio().getAttEstado().setString(msBizPartnerState);
                ((cfd.ver3.DElementReceptor) receptor).getEltDomicilio().getAttCodigoPostal().setString(msBizPartnerZipCode);
                ((cfd.ver3.DElementReceptor) receptor).getEltDomicilio().getAttPais().setString(msBizPartnerCountry);
            }
        }
        else {
            receptor = new cfd.ver2.DElementReceptor();

            ((cfd.ver2.DElementReceptor) receptor).getAttRfc().setString(msBizPartnerRfc);
            ((cfd.ver2.DElementReceptor) receptor).getAttNombre().setString(msBizPartnerName);
            ((cfd.ver2.DElementReceptor) receptor).getEltDomicilio().getAttCalle().setString(msBizPartnerStreet);
            ((cfd.ver2.DElementReceptor) receptor).getEltDomicilio().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
            ((cfd.ver2.DElementReceptor) receptor).getEltDomicilio().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
            ((cfd.ver2.DElementReceptor) receptor).getEltDomicilio().getAttColonia().setString(msBizPartnerNeighborhood);
            ((cfd.ver2.DElementReceptor) receptor).getEltDomicilio().getAttLocalidad().setString(msBizPartnerLocality);
            ((cfd.ver2.DElementReceptor) receptor).getEltDomicilio().getAttReferencia().setString(msBizPartnerReference);
            ((cfd.ver2.DElementReceptor) receptor).getEltDomicilio().getAttMunicipio().setString(msBizPartnerCounty);
            ((cfd.ver2.DElementReceptor) receptor).getEltDomicilio().getAttEstado().setString(msBizPartnerState);
            ((cfd.ver2.DElementReceptor) receptor).getEltDomicilio().getAttCodigoPostal().setString(msBizPartnerZipCode);
            ((cfd.ver2.DElementReceptor) receptor).getEltDomicilio().getAttPais().setString(msBizPartnerCountry);
        }

        return receptor;
    }
    
    public String getCfdLugarExpedicion() {
        if (mnBizPartnerBranchHqId != mnBizPartnerBranchId) {
            return mnCfdiType != SCfdConsts.CFD_TYPE_PAYROLL ? msBizPartnerExpeditionLocality + ", " + msBizPartnerExpeditionState : msBizPartnerZipCode;
        }
        else {
            return mnCfdiType != SCfdConsts.CFD_TYPE_PAYROLL ? msBizPartnerLocality + ", " + msBizPartnerState : msBizPartnerZipCode;
        }
    }
}
