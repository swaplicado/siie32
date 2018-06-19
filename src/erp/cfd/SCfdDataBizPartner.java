/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd;

import cfd.DCfdConsts;
import cfd.DElement;
import cfd.ver3.DCfdVer3Utils;
import cfd.ver3.cce11.DElementTipoDomicilioInt;
import cfd.ver3.cce11.DElementTipoDomicilioNac;
import erp.data.SDataConstantsSys;
import erp.mod.fin.db.SFinConsts;

/**
 *
 * @author Juan Barajas, Sergio Flores
 */
public class SCfdDataBizPartner {

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
    protected boolean mbIsCfdiWithIntCommerce;
    protected boolean mbIsStateCodeAssociate;
    protected float mfVersion;
    protected int mnCfdiType;

    public SCfdDataBizPartner() {
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
        mbIsCfdiWithIntCommerce = false;
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
    public void setIsCfdiWithIntCommerce(boolean b) { mbIsCfdiWithIntCommerce = b; }
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
    public boolean isCfdiWithIntCommerce() { return mbIsCfdiWithIntCommerce; }
    public boolean isStateCodeAssociate() { return mbIsStateCodeAssociate; }
    public float getVersion() { return mfVersion; }
    public int getCfdiType() { return mnCfdiType; }

    /**
     * Creates element root for emisor with issue place for business partner indicated.
     * @return cfd.DElement node for emisor.
     * @throws java.lang.Exception
     */
    public cfd.DElement createRootElementEmisor() throws java.lang.Exception {
        DElement element = null;

        if (mfVersion == DCfdConsts.CFDI_VER_33) {
            cfd.ver33.DElementEmisor emisor = new cfd.ver33.DElementEmisor();

            emisor.getAttRfc().setString(msBizPartnerRfc);
            emisor.getAttNombre().setString(DCfdVer3Utils.formatAttributeValueAsText(msBizPartnerName));
            //emisor.getAttRegimenFiscal().setString(...
            
            element = emisor;
        }
        else if (mfVersion == DCfdConsts.CFDI_VER_32) {
            cfd.ver32.DElementEmisor emisor = new cfd.ver32.DElementEmisor();

            emisor.getAttRfc().setString(msBizPartnerRfc);
            emisor.getAttNombre().setString(msBizPartnerName);

            if (mnCfdiType == SDataConstantsSys.TRNS_TP_CFD_PAYROLL) {
                emisor.clearEltDomicilioFiscal();
            }
            else {
                emisor.getEltDomicilioFiscal().getAttCalle().setString(msBizPartnerStreet);
                emisor.getEltDomicilioFiscal().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
                emisor.getEltDomicilioFiscal().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
                emisor.getEltDomicilioFiscal().getAttColonia().setString(msBizPartnerNeighborhood);
                emisor.getEltDomicilioFiscal().getAttLocalidad().setString(msBizPartnerLocality);
                emisor.getEltDomicilioFiscal().getAttReferencia().setString(msBizPartnerReference);
                emisor.getEltDomicilioFiscal().getAttMunicipio().setString(msBizPartnerCounty);
                emisor.getEltDomicilioFiscal().getAttEstado().setString(mbIsCfdiWithIntCommerce ? msBizPartnerStateCode : msBizPartnerStateName);
                emisor.getEltDomicilioFiscal().getAttCodigoPostal().setString(msBizPartnerZipCode);
                emisor.getEltDomicilioFiscal().getAttPais().setString(mbIsCfdiWithIntCommerce ? msBizPartnerCountryCode : msBizPartnerCountryName);
            }

            if (mnBizPartnerBranchHqId != mnBizPartnerBranchId && mnCfdiType != SDataConstantsSys.TRNS_TP_CFD_PAYROLL) {
                emisor.setEltOpcExpedidoEn(new cfd.ver32.DElementTipoUbicacion("cfdi:ExpedidoEn"));
                emisor.getEltOpcExpedidoEn().getAttCalle().setString(msBizPartnerExpeditionStreet);
                emisor.getEltOpcExpedidoEn().getAttNoExterior().setString(msBizPartnerExpeditionStreetNumberExt);
                emisor.getEltOpcExpedidoEn().getAttNoInterior().setString(msBizPartnerExpeditionStreetNumberInt);
                emisor.getEltOpcExpedidoEn().getAttColonia().setString(msBizPartnerExpeditionNeighborhood);
                emisor.getEltOpcExpedidoEn().getAttLocalidad().setString(msBizPartnerExpeditionLocality);
                emisor.getEltOpcExpedidoEn().getAttReferencia().setString(msBizPartnerExpeditionReference);
                emisor.getEltOpcExpedidoEn().getAttMunicipio().setString(msBizPartnerExpeditionCounty);
                emisor.getEltOpcExpedidoEn().getAttEstado().setString(msBizPartnerExpeditionState);
                emisor.getEltOpcExpedidoEn().getAttCodigoPostal().setString(msBizPartnerExpeditionZipCode);
                emisor.getEltOpcExpedidoEn().getAttPais().setString(msBizPartnerExpeditionCountryName);
            }
            
            element = emisor;
        }
        else {
            cfd.ver2.DElementEmisor emisor = new cfd.ver2.DElementEmisor(mfVersion);

            emisor.getAttRfc().setString(msBizPartnerRfc);
            emisor.getAttNombre().setString(msBizPartnerName);
            emisor.getEltDomicilioFiscal().getAttCalle().setString(msBizPartnerStreet);
            emisor.getEltDomicilioFiscal().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
            emisor.getEltDomicilioFiscal().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
            emisor.getEltDomicilioFiscal().getAttColonia().setString(msBizPartnerNeighborhood);
            emisor.getEltDomicilioFiscal().getAttLocalidad().setString(msBizPartnerLocality);
            emisor.getEltDomicilioFiscal().getAttReferencia().setString(msBizPartnerReference);
            emisor.getEltDomicilioFiscal().getAttMunicipio().setString(msBizPartnerCounty);
            emisor.getEltDomicilioFiscal().getAttEstado().setString(msBizPartnerStateName);
            emisor.getEltDomicilioFiscal().getAttCodigoPostal().setString(msBizPartnerZipCode);
            emisor.getEltDomicilioFiscal().getAttPais().setString(msBizPartnerCountryName);

            if (mnBizPartnerBranchHqId != mnBizPartnerBranchId) {
                emisor.setEltOpcExpedidoEn(new cfd.ver2.DElementTipoUbicacion("ExpedidoEn"));
                emisor.getEltOpcExpedidoEn().getAttCalle().setString(msBizPartnerExpeditionStreet);
                emisor.getEltOpcExpedidoEn().getAttNoExterior().setString(msBizPartnerExpeditionStreetNumberExt);
                emisor.getEltOpcExpedidoEn().getAttNoInterior().setString(msBizPartnerExpeditionStreetNumberInt);
                emisor.getEltOpcExpedidoEn().getAttColonia().setString(msBizPartnerExpeditionNeighborhood);
                emisor.getEltOpcExpedidoEn().getAttLocalidad().setString(msBizPartnerExpeditionLocality);
                emisor.getEltOpcExpedidoEn().getAttReferencia().setString(msBizPartnerExpeditionReference);
                emisor.getEltOpcExpedidoEn().getAttMunicipio().setString(msBizPartnerExpeditionCounty);
                emisor.getEltOpcExpedidoEn().getAttEstado().setString(msBizPartnerExpeditionState);
                emisor.getEltOpcExpedidoEn().getAttCodigoPostal().setString(msBizPartnerExpeditionZipCode);
                emisor.getEltOpcExpedidoEn().getAttPais().setString(msBizPartnerExpeditionCountryName);
            }
            
            element = emisor;
        }

        return element;
    }
    
    /**
     * Creates element root for International Commerce emisor for business partner indicated.
     * @return cfd.ver3.cce11.DElementEmisor node for International Commerce emisor.
     * @throws java.lang.Exception 
     */
    public cfd.ver3.cce11.DElementEmisor createRootElementEmisorIntCommerce() throws java.lang.Exception {
        cfd.ver3.cce11.DElementEmisor emisor = null;
        
        if (mfVersion == DCfdConsts.CFDI_VER_33) {
            if (msBizPartnerRfc.length() == SFinConsts.RFC_PER_LEN) {
                if (msBizPartnerCurp.isEmpty()) {
                    throw new Exception("El CURP del emisor no existe, y es requerido.");
                }
            }
            
            emisor = new cfd.ver3.cce11.DElementEmisor();
            
            emisor.getAttCurp().setString(msBizPartnerCurp);

            emisor.setEltDomicilio(new DElementTipoDomicilioNac());
            emisor.getEltDomicilio().getAttCalle().setString(msBizPartnerStreet);
            emisor.getEltDomicilio().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
            emisor.getEltDomicilio().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
            emisor.getEltDomicilio().getAttColonia().setString(msBizPartnerNeighborhood);
            emisor.getEltDomicilio().getAttLocalidad().setString(msBizPartnerLocality);
            emisor.getEltDomicilio().getAttReferencia().setString(msBizPartnerReference);
            emisor.getEltDomicilio().getAttMunicipio().setString(msBizPartnerCounty);
            emisor.getEltDomicilio().getAttEstado().setString(msBizPartnerStateCode);
            emisor.getEltDomicilio().getAttCodigoPostal().setString(msBizPartnerZipCode);
            emisor.getEltDomicilio().getAttPais().setString(msBizPartnerCountryCode);
        }
        else if (mfVersion == DCfdConsts.CFDI_VER_32) {
            emisor = new cfd.ver3.cce11.DElementEmisor();

            if (msBizPartnerRfc.length() == SFinConsts.RFC_PER_LEN) {
                emisor.getAttCurp().setString(msBizPartnerCurp);
            }
        }
        
        return emisor;
    }

    /**
     * Creates element root for receptor for business partner indicated.
     * @return cfd.DElement node for receptor.
     * @throws java.lang.Exception
     */
    public cfd.DElement createRootElementReceptor() throws java.lang.Exception {
        DElement element = null;

        if (mfVersion == DCfdConsts.CFDI_VER_33) {
            if (mbIsCfdiWithIntCommerce) {
                if (msBizPartnerRfc.compareTo(DCfdConsts.RFC_GEN_INT) != 0) {
                    throw new Exception("El RFC del receptor debe ser '" + DCfdConsts.RFC_GEN_INT + "'.");
                }
                if (msBizPartnerCountryCode.isEmpty()) {
                    throw new Exception("El código de país del receptor no existe, y es requerido.");
                }
                if (msBizPartnerFiscalForeing.isEmpty()) {
                    throw new Exception("El ID fiscal del receptor no existe, y es requerido.");
                }
            }
            
            cfd.ver33.DElementReceptor receptor = new cfd.ver33.DElementReceptor();

            receptor.getAttRfc().setString(msBizPartnerRfc);
            receptor.getAttNombre().setString(DCfdVer3Utils.formatAttributeValueAsText(msBizPartnerName));

            if (mbIsCfdiWithIntCommerce) {
                receptor.getAttResidenciaFiscal().setString(msBizPartnerCountryCode);
                receptor.getAttNumRegIdTrib().setString(msBizPartnerFiscalForeing);
            }
            
            //receptor.getAttUsoCFDI().setString(...
            
            element = receptor;
        }
        else if (mfVersion == DCfdConsts.CFDI_VER_32) {
            if (mbIsCfdiWithIntCommerce) {
                if (msBizPartnerRfc.compareTo(DCfdConsts.RFC_GEN_INT) != 0) {
                    throw new Exception("El RFC del receptor debe ser '" + DCfdConsts.RFC_GEN_INT + "'.");
                }
            }

            cfd.ver32.DElementReceptor receptor = new cfd.ver32.DElementReceptor();
            
            receptor.getAttRfc().setString(msBizPartnerRfc);
            receptor.getAttNombre().setString(msBizPartnerName);

            if (mnCfdiType == SDataConstantsSys.TRNS_TP_CFD_PAYROLL) {
                receptor.clearEltDomicilio();
            }
            else {
                receptor.getEltDomicilio().getAttCalle().setString(msBizPartnerStreet);
                receptor.getEltDomicilio().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
                receptor.getEltDomicilio().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
                receptor.getEltDomicilio().getAttColonia().setString(msBizPartnerNeighborhood);
                receptor.getEltDomicilio().getAttLocalidad().setString(msBizPartnerLocality);
                receptor.getEltDomicilio().getAttReferencia().setString(msBizPartnerReference);
                receptor.getEltDomicilio().getAttMunicipio().setString(msBizPartnerCounty);
                receptor.getEltDomicilio().getAttEstado().setString(mbIsCfdiWithIntCommerce && mbIsStateCodeAssociate ? msBizPartnerStateCode : msBizPartnerStateName);
                receptor.getEltDomicilio().getAttCodigoPostal().setString(msBizPartnerZipCode);
                receptor.getEltDomicilio().getAttPais().setString(mbIsCfdiWithIntCommerce ? msBizPartnerCountryCode : msBizPartnerCountryName);
            }
            
            element = receptor;
        }
        else {
            cfd.ver2.DElementReceptor receptor = new cfd.ver2.DElementReceptor();

            receptor.getAttRfc().setString(msBizPartnerRfc);
            receptor.getAttNombre().setString(msBizPartnerName);
            receptor.getEltDomicilio().getAttCalle().setString(msBizPartnerStreet);
            receptor.getEltDomicilio().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
            receptor.getEltDomicilio().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
            receptor.getEltDomicilio().getAttColonia().setString(msBizPartnerNeighborhood);
            receptor.getEltDomicilio().getAttLocalidad().setString(msBizPartnerLocality);
            receptor.getEltDomicilio().getAttReferencia().setString(msBizPartnerReference);
            receptor.getEltDomicilio().getAttMunicipio().setString(msBizPartnerCounty);
            receptor.getEltDomicilio().getAttEstado().setString(msBizPartnerStateName);
            receptor.getEltDomicilio().getAttCodigoPostal().setString(msBizPartnerZipCode);
            receptor.getEltDomicilio().getAttPais().setString(msBizPartnerCountryName);
            
            element = receptor;
        }

        return element;
    }
    
     /**
     * Creates element root for International Commerce receptor for business partner indicated.
     * @return cfd.ver3.cce11.DElementReceptor node for International Commerce receptor.
     * @throws java.lang.Exception 
     */
    public cfd.ver3.cce11.DElementReceptor createRootElementReceptorIntCommerce() throws java.lang.Exception {
        cfd.ver3.cce11.DElementReceptor receptor = null;

        if (mfVersion == DCfdConsts.CFDI_VER_33) {
            receptor = new cfd.ver3.cce11.DElementReceptor();
            
            receptor.setEltDomicilio(new DElementTipoDomicilioInt());
            receptor.getEltDomicilio().getAttCalle().setString(msBizPartnerStreet);
            receptor.getEltDomicilio().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
            receptor.getEltDomicilio().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
            receptor.getEltDomicilio().getAttColonia().setString(msBizPartnerNeighborhood);
            receptor.getEltDomicilio().getAttLocalidad().setString(msBizPartnerLocality);
            receptor.getEltDomicilio().getAttReferencia().setString(msBizPartnerReference);
            receptor.getEltDomicilio().getAttMunicipio().setString(msBizPartnerCounty);
            receptor.getEltDomicilio().getAttEstado().setString(!msBizPartnerStateCode.isEmpty() ? msBizPartnerStateCode : msBizPartnerStateName);
            receptor.getEltDomicilio().getAttCodigoPostal().setString(msBizPartnerZipCode);
            receptor.getEltDomicilio().getAttPais().setString(msBizPartnerCountryCode);
        }
        else if (mfVersion == DCfdConsts.CFDI_VER_32) {
            receptor = new cfd.ver3.cce11.DElementReceptor();

            receptor.getAttNumRegIdTrib().setString(msBizPartnerFiscalForeing);
        }
        
        return receptor;
    }
    
     /**
     * Creates element root for International Commerce addressee for business partner indicated.
     * @return cfd.ver3.cce11.DElementDestinatario node for International Commerce receptor.
     * @throws java.lang.Exception 
     */
    public cfd.ver3.cce11.DElementDestinatario createRootElementDestinatarioIntCommerce() throws java.lang.Exception {
        cfd.ver3.cce11.DElementDestinatario destinatario = null;

        if (mfVersion == DCfdConsts.CFDI_VER_33) {
            destinatario = new cfd.ver3.cce11.DElementDestinatario();
            
            destinatario.getAttNumRegIdTrib().setString(msBizPartnerFiscalForeing);
            destinatario.getAttNombre().setString(msBizPartnerName);
            
            destinatario.getEltDomicilio().getAttCalle().setString(msBizPartnerStreet);
            destinatario.getEltDomicilio().getAttNoExterior().setString(msBizPartnerStreetNumberExt);
            destinatario.getEltDomicilio().getAttNoInterior().setString(msBizPartnerStreetNumberInt);
            destinatario.getEltDomicilio().getAttColonia().setString(msBizPartnerNeighborhood);
            destinatario.getEltDomicilio().getAttLocalidad().setString(msBizPartnerLocality);
            destinatario.getEltDomicilio().getAttReferencia().setString(msBizPartnerReference);
            destinatario.getEltDomicilio().getAttMunicipio().setString(msBizPartnerCounty);
            destinatario.getEltDomicilio().getAttEstado().setString(!msBizPartnerStateCode.isEmpty() ? msBizPartnerStateCode : msBizPartnerStateName);
            destinatario.getEltDomicilio().getAttCodigoPostal().setString(msBizPartnerZipCode);
            destinatario.getEltDomicilio().getAttPais().setString(msBizPartnerCountryCode);
        }
        else if (mfVersion == DCfdConsts.CFDI_VER_32) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        return destinatario;
    }
    
    /**
     * Gets issue place for CFD 2.0, 2.2 and CFDI 3.2.
     * @return 
     */
    public String getCfdLugarExpedicion() throws Exception {
        if (mfVersion == DCfdConsts.CFDI_VER_33) {
            throw new Exception("Versión inválida de CFDI: " + DCfdConsts.CFDI_VER_33 + ".");
        }
        else {
            if (mnBizPartnerBranchHqId != mnBizPartnerBranchId) {
                return mnCfdiType != SDataConstantsSys.TRNS_TP_CFD_PAYROLL && mfVersion == DCfdConsts.CFDI_VER_32 && !mbIsCfdiWithIntCommerce ? 
                        msBizPartnerExpeditionLocality + ", " + msBizPartnerExpeditionState : 
                        msBizPartnerExpeditionZipCode;
            }
            else {
                return mnCfdiType != SDataConstantsSys.TRNS_TP_CFD_PAYROLL && mfVersion == DCfdConsts.CFDI_VER_32 && !mbIsCfdiWithIntCommerce ? 
                        msBizPartnerLocality + ", " + msBizPartnerStateName : 
                        msBizPartnerZipCode;
            }
        }
    }
}
