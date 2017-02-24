/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd;

import cfd.DCfdConsts;
import cfd.DElement;
import erp.mod.fin.db.SFinConsts;

/**
 *
 * @author Juan Barajas
 */
public class SCfdDataAsociadoNegocios {

    protected int mnBizPartnerId;
    protected int mnBizPartnerBranchId;
    protected int mnBizPartnerBranchHqId;
    protected String msBizPartnerRfc;
    protected String msBizPartnerCurp;
    protected String msBizPartnerFiscalForeing;
    protected String msBizPartnerName;
    protected String msBizPartnerStreet;
    protected String msBizPartnerStreetNumberExt;
    protected String msBizPartnerStreetNumberInt;
    protected String msBizPartnerNeighborhood;
    protected String msBizPartnerReference;
    protected String msBizPartnerLocality;
    protected String msBizPartnerCounty;
    protected String msBizPartnerStateCode;
    protected String msBizPartnerStateName;
    protected String msBizPartnerZipCode;
    protected String msBizPartnerPoBox;
    protected String msBizPartnerCountryCode;
    protected String msBizPartnerCountryName;
    protected int msBizPartnerCountryId;
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
    protected String msBizPartnerExpeditionCountryName;
    protected String msBizPartnerFiscalRegime;
    protected boolean mbIsCfdi;
    protected boolean mbIsCfdiWithCce;
    protected boolean mbIsStateCodeAssociate;
    protected float mfVersion;
    protected int mnCfdiType;

    public SCfdDataAsociadoNegocios() {
        mnBizPartnerId = 0;
        mnBizPartnerBranchId = 0;
        mnBizPartnerBranchHqId = 0;
        msBizPartnerRfc = "";
        msBizPartnerCurp = "";
        msBizPartnerFiscalForeing = "";
        msBizPartnerName = "";
        msBizPartnerStreet = "";
        msBizPartnerStreetNumberExt = "";
        msBizPartnerStreetNumberInt = "";
        msBizPartnerNeighborhood = "";
        msBizPartnerReference = "";
        msBizPartnerLocality = "";
        msBizPartnerCounty = "";
        msBizPartnerStateCode = "";
        msBizPartnerStateName = "";
        msBizPartnerZipCode = "";
        msBizPartnerPoBox = "";
        msBizPartnerCountryCode = "";
        msBizPartnerCountryName = "";
        msBizPartnerCountryId = 0;
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
        msBizPartnerExpeditionCountryName = "";
        msBizPartnerFiscalRegime = "";
        mbIsCfdi = false;
        mbIsCfdiWithCce = false;
        mbIsStateCodeAssociate = false;
        mfVersion = 0f;
        mnCfdiType = 0;
    }

    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartnerBranchId(int n) { mnBizPartnerBranchId = n; }
    public void setBizPartnerBranchHqId(int n) { mnBizPartnerBranchHqId = n; }
    public void setBizPartnerRfc(String s) { msBizPartnerRfc = s; }
    public void setBizPartnerCurp(String s) { msBizPartnerCurp = s; }
    public void setBizPartnerFiscalForeing(String s) { msBizPartnerFiscalForeing = s; }
    public void setBizPartnerName(String s) { msBizPartnerName = s; }
    public void setBizPartnerStreet(String s) { msBizPartnerStreet = s; }
    public void setBizPartnerStreetNumberExt(String s) { msBizPartnerStreetNumberExt = s; }
    public void setBizPartnerStreetNumberInt(String s) { msBizPartnerStreetNumberInt = s; }
    public void setBizPartnerNeighborhood(String s) { msBizPartnerNeighborhood = s; }
    public void setBizPartnerReference(String s) { msBizPartnerReference = s; }
    public void setBizPartnerLocality(String s) { msBizPartnerLocality = s; }
    public void setBizPartnerCounty(String s) { msBizPartnerCounty = s; }
    public void setBizPartnerStateCode(String s) { msBizPartnerStateCode = s; }
    public void setBizPartnerStateName(String s) { msBizPartnerStateName = s; }
    public void setBizPartnerZipCode(String s) { msBizPartnerZipCode = s; }
    public void setBizPartnerPoBox(String s) { msBizPartnerPoBox = s; }
    public void setBizPartnerCountryCode(String s) { msBizPartnerCountryCode = s; }
    public void setBizPartnerCountryName(String s) { msBizPartnerCountryName = s; }
    public void setBizPartnerCountryId(int n) { msBizPartnerCountryId = n; }
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
    public void setBizPartnerExpeditionCountryName(String s) { msBizPartnerExpeditionCountryName = s; }
    public void setBizPartnerFiscalRegime(String s) { msBizPartnerFiscalRegime= s; }
    public void setIsCfdi(boolean b) { mbIsCfdi = b; }
    public void setIsCfdiWithCce(boolean b) { mbIsCfdiWithCce = b; }
    public void setIsStateCodeAssociate(boolean b) { mbIsStateCodeAssociate = b; }
    public void setVersion(float f) { mfVersion = f; }
    public void setCfdiType(int n) { mnCfdiType = n; }

    public int getBizPartnerId() { return mnBizPartnerId; }
    public int getBizPartnerBranchId() { return mnBizPartnerBranchId; }
    public int getBizPartnerBranchHqId() { return mnBizPartnerBranchHqId; }
    public String getBizPartnerRfc() { return msBizPartnerRfc; }
    public String getBizPartnerCurp() { return msBizPartnerCurp; }
    public String getBizPartnerFiscalForeing() { return msBizPartnerFiscalForeing; }
    public String getBizPartnerName() { return msBizPartnerName; }
    public String getBizPartnerStreet() { return msBizPartnerStreet; }
    public String getBizPartnerStreetNumberExt() { return msBizPartnerStreetNumberExt; }
    public String getBizPartnerStreetNumberInt() { return msBizPartnerStreetNumberInt; }
    public String getBizPartnerNeighborhood() { return msBizPartnerNeighborhood; }
    public String getBizPartnerReference() { return msBizPartnerReference; }
    public String getBizPartnerLocality() { return msBizPartnerLocality; }
    public String getBizPartnerCounty() { return msBizPartnerCounty; }
    public String getBizPartnerStateCode() { return msBizPartnerStateCode; }
    public String getBizPartnerStateName() { return msBizPartnerStateName; }
    public String getBizPartnerZipCode() { return msBizPartnerZipCode; }
    public String getBizPartnerPoBox() { return msBizPartnerPoBox; }
    public String getBizPartnerCountryCode() { return msBizPartnerCountryCode; }
    public String getBizPartnerCountryName() { return msBizPartnerCountryName; }
    public int getBizPartnerCountryId() { return msBizPartnerCountryId; }
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
    public String getBizPartnerExpeditionCountryName() { return msBizPartnerExpeditionCountryName; }
    public String getBizPartnerFiscalRegime() { return msBizPartnerFiscalRegime; }
    public boolean getIsCfdi() { return mbIsCfdi; }
    public boolean getIsCfdiWithCce() { return mbIsCfdiWithCce; }
    public boolean getIsStateCodeAssociate() { return mbIsStateCodeAssociate; }
    public float getVersion() { return mfVersion; }
    public int getCfdiType() { return mnCfdiType; }

    /**
     * Create element root for emisor with expedition spot for the business partner indicated
     * @return cfd.DElement node cfd.DElement for emisor
     * @throws java.lang.Exception
     */
    public cfd.DElement createRootElementEmisor() throws java.lang.Exception {
        DElement emisor = null;

        if (mbIsCfdi) {
            if (mfVersion == DCfdConsts.CFDI_VER_33) {
                emisor = new cfd.ver33.DElementEmisor();

                ((cfd.ver33.DElementEmisor) emisor).getAttRfc().setString(msBizPartnerRfc);
                ((cfd.ver33.DElementEmisor) emisor).getAttNombre().setString(msBizPartnerName);
                //((cfd.ver33.DElementEmisor) emisor).getAttRegimenFiscal().setString(msBizPartnerName);
            }
            else if (mfVersion == DCfdConsts.CFDI_VER_32) {
                emisor = new cfd.ver32.DElementEmisor();
                
                ((cfd.ver32.DElementEmisor) emisor).getAttRfc().setString(msBizPartnerRfc);
                ((cfd.ver32.DElementEmisor) emisor).getAttNombre().setString(msBizPartnerName);

                if (mnCfdiType == SCfdConsts.CFD_TYPE_PAYROLL) {
                    ((cfd.ver32.DElementEmisor) emisor).clearEltDomicilioFiscal();
                }
                else {
                    ((cfd.ver32.DElementEmisor) emisor).getEltDomicilioFiscal().getAttCalle().setString(msBizPartnerStreet);
                    ((cfd.ver32.DElementEmisor) emisor).getEltDomicilioFiscal().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
                    ((cfd.ver32.DElementEmisor) emisor).getEltDomicilioFiscal().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
                    ((cfd.ver32.DElementEmisor) emisor).getEltDomicilioFiscal().getAttColonia().setString(msBizPartnerNeighborhood);
                    ((cfd.ver32.DElementEmisor) emisor).getEltDomicilioFiscal().getAttLocalidad().setString(msBizPartnerLocality);
                    ((cfd.ver32.DElementEmisor) emisor).getEltDomicilioFiscal().getAttReferencia().setString(msBizPartnerReference);
                    ((cfd.ver32.DElementEmisor) emisor).getEltDomicilioFiscal().getAttMunicipio().setString(msBizPartnerCounty);
                    ((cfd.ver32.DElementEmisor) emisor).getEltDomicilioFiscal().getAttEstado().setString(mbIsCfdiWithCce ? msBizPartnerStateCode : msBizPartnerStateName);
                    ((cfd.ver32.DElementEmisor) emisor).getEltDomicilioFiscal().getAttCodigoPostal().setString(msBizPartnerZipCode);
                    ((cfd.ver32.DElementEmisor) emisor).getEltDomicilioFiscal().getAttPais().setString(mbIsCfdiWithCce ? msBizPartnerCountryCode : msBizPartnerCountryName);
                }

                if (mnBizPartnerBranchHqId != mnBizPartnerBranchId && mnCfdiType != SCfdConsts.CFD_TYPE_PAYROLL) {
                    ((cfd.ver32.DElementEmisor) emisor).setEltOpcExpedidoEn(new cfd.ver32.DElementTipoUbicacion("cfdi:ExpedidoEn"));
                    ((cfd.ver32.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttCalle().setString(msBizPartnerExpeditionStreet);
                    ((cfd.ver32.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttNoExterior().setString(msBizPartnerExpeditionStreetNumberExt);
                    ((cfd.ver32.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttNoInterior().setString(msBizPartnerExpeditionStreetNumberInt);
                    ((cfd.ver32.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttColonia().setString(msBizPartnerExpeditionNeighborhood);
                    ((cfd.ver32.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttLocalidad().setString(msBizPartnerExpeditionLocality);
                    ((cfd.ver32.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttReferencia().setString(msBizPartnerExpeditionReference);
                    ((cfd.ver32.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttMunicipio().setString(msBizPartnerExpeditionCounty);
                    ((cfd.ver32.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttEstado().setString(msBizPartnerExpeditionState);
                    ((cfd.ver32.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttCodigoPostal().setString(msBizPartnerExpeditionZipCode);
                    ((cfd.ver32.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttPais().setString(msBizPartnerExpeditionCountryName);
                }
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
            ((cfd.ver2.DElementEmisor) emisor).getEltDomicilioFiscal().getAttEstado().setString(msBizPartnerStateName);
            ((cfd.ver2.DElementEmisor) emisor).getEltDomicilioFiscal().getAttCodigoPostal().setString(msBizPartnerZipCode);
            ((cfd.ver2.DElementEmisor) emisor).getEltDomicilioFiscal().getAttPais().setString(msBizPartnerCountryName);

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
                ((cfd.ver2.DElementEmisor) emisor).getEltOpcExpedidoEn().getAttPais().setString(msBizPartnerExpeditionCountryName);
            }
        }

        return emisor;
    }
    
    public cfd.DElement createRootElementEmisorCce() throws java.lang.Exception {
        DElement emisor = null;
        
        if (mfVersion == DCfdConsts.CFDI_VER_33) {
            emisor = new cfd.ver3.cce11.DElementEmisor();

            if (msBizPartnerRfc.length() == 13) {
                ((cfd.ver3.cce11.DElementEmisor) emisor).getAttCurp().setString(msBizPartnerCurp);
            }
            
            if (mnCfdiType == SCfdConsts.CFD_TYPE_PAYROLL) {
                ((cfd.ver32.DElementEmisor) emisor).clearEltDomicilioFiscal();
            }
            else {
                ((cfd.ver3.cce11.DElementEmisor) emisor).getEltDomicilio().getAttCalle().setString(msBizPartnerStreet);
                ((cfd.ver3.cce11.DElementEmisor) emisor).getEltDomicilio().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
                ((cfd.ver3.cce11.DElementEmisor) emisor).getEltDomicilio().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
                ((cfd.ver3.cce11.DElementEmisor) emisor).getEltDomicilio().getAttColonia().setString(msBizPartnerNeighborhood);
                ((cfd.ver3.cce11.DElementEmisor) emisor).getEltDomicilio().getAttLocalidad().setString(msBizPartnerLocality);
                ((cfd.ver3.cce11.DElementEmisor) emisor).getEltDomicilio().getAttReferencia().setString(msBizPartnerReference);
                ((cfd.ver3.cce11.DElementEmisor) emisor).getEltDomicilio().getAttMunicipio().setString(msBizPartnerCounty);
                ((cfd.ver3.cce11.DElementEmisor) emisor).getEltDomicilio().getAttEstado().setString(msBizPartnerStateCode);
                ((cfd.ver3.cce11.DElementEmisor) emisor).getEltDomicilio().getAttCodigoPostal().setString(msBizPartnerZipCode);
                ((cfd.ver3.cce11.DElementEmisor) emisor).getEltDomicilio().getAttPais().setString(msBizPartnerCountryCode);
            }
        }
        else if (mfVersion == DCfdConsts.CFDI_VER_32) {
            emisor = new cfd.ver3.cce11.DElementEmisor();

            if (msBizPartnerRfc.length() == 13) {
                ((cfd.ver3.cce11.DElementEmisor) emisor).getAttCurp().setString(msBizPartnerCurp);
            }
        }
        
        return emisor;
    }

    /**
     * Create element root for receptor with expedition spot for the business partner indicated
     * @return cfd.DElement node cfd.DElement for receptor
     * @throws java.lang.Exception
     */
    public cfd.DElement createRootElementReceptor() throws java.lang.Exception {
        DElement receptor = null;

        if (mbIsCfdi) {
            if (mfVersion == DCfdConsts.CFDI_VER_33) {
                receptor = new cfd.ver33.DElementReceptor();

                ((cfd.ver33.DElementReceptor) receptor).getAttRfc().setString(msBizPartnerRfc);
                ((cfd.ver33.DElementReceptor) receptor).getAttNombre().setString(msBizPartnerName);
                
                if (mbIsCfdiWithCce && msBizPartnerRfc.compareTo(SFinConsts.RFC_INT) == 0) {
                    
                    if (msBizPartnerCountryCode.isEmpty()) {
                        throw new Exception("El código del país no existe y es requerido.");
                    }
                    
                    if (msBizPartnerFiscalForeing.isEmpty()) {
                        throw new Exception("El ID fiscal no existe y es requerido.");
                    }
                    
                    ((cfd.ver33.DElementReceptor) receptor).getAttResidenciaFiscal().setString(msBizPartnerCountryCode);
                    ((cfd.ver33.DElementReceptor) receptor).getAttNumRegIdTrib().setString(msBizPartnerFiscalForeing);
                }
                //((cfd.ver33.DElementReceptor) receptor).getAttUsoCFDI().setString(msBizPartnerName); is complete in where it will be used
            }
            else if (mfVersion == DCfdConsts.CFDI_VER_32) {
                receptor = new cfd.ver32.DElementReceptor();

                if (mbIsCfdiWithCce && msBizPartnerRfc.compareTo(SFinConsts.RFC_INT) != 0) {
                    throw new Exception("El RFC del receptor no cumple con la estructura '" + SFinConsts.RFC_INT + "' requerida.");
                }
                
                ((cfd.ver32.DElementReceptor) receptor).getAttRfc().setString(msBizPartnerRfc);
                ((cfd.ver32.DElementReceptor) receptor).getAttNombre().setString(msBizPartnerName);

                if (mnCfdiType == SCfdConsts.CFD_TYPE_PAYROLL) {
                    ((cfd.ver32.DElementReceptor) receptor).clearEltDomicilio();
                }
                else {
                    ((cfd.ver32.DElementReceptor) receptor).getEltDomicilio().getAttCalle().setString(msBizPartnerStreet);
                    ((cfd.ver32.DElementReceptor) receptor).getEltDomicilio().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
                    ((cfd.ver32.DElementReceptor) receptor).getEltDomicilio().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
                    ((cfd.ver32.DElementReceptor) receptor).getEltDomicilio().getAttColonia().setString(msBizPartnerNeighborhood);
                    ((cfd.ver32.DElementReceptor) receptor).getEltDomicilio().getAttLocalidad().setString(msBizPartnerLocality);
                    ((cfd.ver32.DElementReceptor) receptor).getEltDomicilio().getAttReferencia().setString(msBizPartnerReference);
                    ((cfd.ver32.DElementReceptor) receptor).getEltDomicilio().getAttMunicipio().setString(msBizPartnerCounty);
                    ((cfd.ver32.DElementReceptor) receptor).getEltDomicilio().getAttEstado().setString(mbIsCfdiWithCce && mbIsStateCodeAssociate ? msBizPartnerStateCode : msBizPartnerStateName);
                    ((cfd.ver32.DElementReceptor) receptor).getEltDomicilio().getAttCodigoPostal().setString(msBizPartnerZipCode);
                    ((cfd.ver32.DElementReceptor) receptor).getEltDomicilio().getAttPais().setString(mbIsCfdiWithCce ? msBizPartnerCountryCode : msBizPartnerCountryName);
                }
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
            ((cfd.ver2.DElementReceptor) receptor).getEltDomicilio().getAttEstado().setString(msBizPartnerStateName);
            ((cfd.ver2.DElementReceptor) receptor).getEltDomicilio().getAttCodigoPostal().setString(msBizPartnerZipCode);
            ((cfd.ver2.DElementReceptor) receptor).getEltDomicilio().getAttPais().setString(msBizPartnerCountryName);
        }

        return receptor;
    }
    
    public cfd.DElement createRootElementReceptorCce() throws java.lang.Exception {
        DElement receptor = null;

        if (mfVersion == DCfdConsts.CFDI_VER_33) {
            receptor = new cfd.ver3.cce11.DElementReceptor();
            
            if (mnCfdiType == SCfdConsts.CFD_TYPE_PAYROLL) {
                ((cfd.ver32.DElementReceptor) receptor).clearEltDomicilio();
            }
            else {
                ((cfd.ver3.cce11.DElementReceptor) receptor).getEltDomicilio().getAttCalle().setString(msBizPartnerStreet);
                ((cfd.ver3.cce11.DElementReceptor) receptor).getEltDomicilio().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
                ((cfd.ver3.cce11.DElementReceptor) receptor).getEltDomicilio().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
                ((cfd.ver3.cce11.DElementReceptor) receptor).getEltDomicilio().getAttColonia().setString(msBizPartnerNeighborhood);
                ((cfd.ver3.cce11.DElementReceptor) receptor).getEltDomicilio().getAttLocalidad().setString(msBizPartnerLocality);
                ((cfd.ver3.cce11.DElementReceptor) receptor).getEltDomicilio().getAttReferencia().setString(msBizPartnerReference);
                ((cfd.ver3.cce11.DElementReceptor) receptor).getEltDomicilio().getAttMunicipio().setString(msBizPartnerCounty);
                ((cfd.ver3.cce11.DElementReceptor) receptor).getEltDomicilio().getAttEstado().setString(msBizPartnerStateName);
                ((cfd.ver3.cce11.DElementReceptor) receptor).getEltDomicilio().getAttCodigoPostal().setString(msBizPartnerZipCode);
                ((cfd.ver3.cce11.DElementReceptor) receptor).getEltDomicilio().getAttPais().setString(msBizPartnerCountryName);
            }
            
        }
        else if (mfVersion == DCfdConsts.CFDI_VER_32) {
            receptor = new cfd.ver3.cce11.DElementReceptor();

            ((cfd.ver3.cce11.DElementReceptor) receptor).getAttNumRegIdTrib().setString(msBizPartnerFiscalForeing);
            
            ((cfd.ver3.cce11.DElementReceptor) receptor).clearEltDomicilio();
        }
        
        return receptor;
    }
    
    public String getCfdLugarExpedicion() {
        if (mnBizPartnerBranchHqId != mnBizPartnerBranchId) {
            return mnCfdiType != SCfdConsts.CFD_TYPE_PAYROLL && mfVersion == DCfdConsts.CFDI_VER_32 && !mbIsCfdiWithCce ? msBizPartnerExpeditionLocality + ", " + msBizPartnerExpeditionState : msBizPartnerZipCode;
        }
        else {
            return mnCfdiType != SCfdConsts.CFD_TYPE_PAYROLL && mfVersion == DCfdConsts.CFDI_VER_32 && !mbIsCfdiWithCce ? msBizPartnerLocality + ", " + msBizPartnerStateName : msBizPartnerZipCode;
        }
    }
}
