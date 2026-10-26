/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mbps.data;

import sa.lib.grid.SGridRow;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Clase que representa un socio de negocios importado desde una fuente externa.
 *
 * Implementa la interfaz para poder ser utilizada dentro de
 * componentes tipo grid/tablas del sistema ERP.
 * 
 * La clase encapsula información general del socio de negocios, incluyendo:
 * 
 *   Datos fiscales y de contacto.
 *   Área funcional asociada.
 *   Indicadores de cliente/proveedor.
 *   Información de dirección.
 *   Estado de importación y edición.
 * @author Claudio Peña
 */

public class SImportedBizPartner implements SGridRow {

    private String name;
    private String email;
    private String fiscalId;
    private String functionalArea;
    private int functionalAreaId;
    private Date authorizedAt;
    private boolean isImported;
    private boolean edited;

    private String firstName;
    private String lastName;
    private String tradeName;
    private String phone;
    private boolean isVendor;
    private boolean isCustomer;

    private String street;
    private String streetNumberExt;
    private String county;
    private String state;
    private String locality;
    private String zipCode;
    private int countryId;
    private String countryCode;
    
    private String partnerFiscalId;
    private int entityType;
    private String fiscalRegime;
    
    /**
     *
     * Inicializa un socio de negocios importado a partir de los datos básicos
     * y de un mapa con información adicional obtenida de una fuente externa.
     *
     * @param name Nombre del socio de negocios.
     * @param email Correo electrónico.
     * @param fiscalId Identificador fiscal (RFC).
     * @param functionalArea Nombre del área funcional.
     * @param authorizedAt Fecha de autorización.
     * @param partner Mapa con información adicional del socio.
     */
    public SImportedBizPartner(String name, String email, String fiscalId, String functionalArea, Date authorizedAt, Map<String, Object> partner) {
        this.name = name != null ? name.trim().toUpperCase() : "";
        this.email = email != null ? email.trim().toUpperCase() : "";
        this.fiscalId = fiscalId != null ? fiscalId.trim().toUpperCase() : "";
        this.functionalArea = functionalArea;
        this.authorizedAt = authorizedAt;
        this.isImported = false;

        try {
            this.functionalAreaId = partner.get("functional_area") != null ? Integer.parseInt(partner.get("functional_area").toString()) : 0;
        }
        catch (Exception e) {
            this.functionalAreaId = 0;
        }

        this.firstName = getString(partner, "first_name");
        this.lastName = getString(partner, "last_name");
        this.tradeName = getString(partner, "trade_name");
        this.phone = getString(partner, "phone");
        this.isVendor = getBoolean(partner, "is_vendor");
        this.isCustomer = getBoolean(partner, "is_customer");
        this.partnerFiscalId = getString(partner, "partner_fiscal_id");
        this.entityType = getInt(partner, "entity_type");
        this.countryId = getInt(partner, "country");
        this.countryCode = getString(partner, "country_code");
        this.fiscalRegime = getString(partner, "fiscal_regime_code");
        
        try {
    Object addrObj = partner.get("partner_address_partner_applying");

    if (addrObj instanceof List) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> addresses = (List<Map<String, Object>>) addrObj;

        if (!addresses.isEmpty()) {
            Map<String, Object> addr = addresses.get(0);

            this.street = getString(addr, "street");
            this.streetNumberExt = getString(addr, "number");
            this.county = getString(addr, "county");
            this.state = getString(addr, "state");
            this.locality = getString(addr, "city");
            this.zipCode = getString(addr, "postal_code");

            this.countryId = addr.get("country") != null
                    ? Integer.parseInt(addr.get("country").toString())
                    : 0;
        }
    }
}
catch (Exception e) {
}
    }

    private String getString(Map<String, Object> map, String key) {
        return map.get(key) != null ? map.get(key).toString().trim().toUpperCase() : "";
    }
    
    private int getInt(Map<String, Object> map, String key) {
        try {
            return map.get(key) != null ? Integer.parseInt(map.get(key).toString()) : 0;
        }
        catch (Exception e) {
            return 0;
        }
    }
    
    private boolean getBoolean(Map<String, Object> map, String key) {
        return map.get(key) != null && Boolean.parseBoolean(map.get(key).toString());
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getFiscalId() { return fiscalId; }
    public String getFunctionalArea() { return functionalArea; }
    public int getFunctionalAreaId() { return functionalAreaId; }
    public Date getAuthorizedAt() { return authorizedAt; }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getTradeName() { return tradeName; }
    public String getPhone() { return phone; }
    public boolean isVendor() { return isVendor; }
    public boolean isCustomer() { return isCustomer; }

    public String getStreet() { return street; }
    public String getStreetNumberExt() { return streetNumberExt; }
    public String getCounty() { return county; }
    public String getState() { return state; }
    public String getLocality() { return locality; }
    public String getZipCode() { return zipCode; }
    public String getCountryCode() { return countryCode; }
    public int getCountryId() { return countryId; }
    public String getPartnerFiscalId() { return partnerFiscalId; }
    public int getEntityType() { return entityType; }
    public String getFiscalRegime() { return fiscalRegime; }

    public boolean isImported() { return isImported; }
    public void setIsImported(boolean isImported) { this.isImported = isImported; }

    @Override
    public int[] getRowPrimaryKey() { return null; }

    @Override
    public String getRowCode() { return name; }

    @Override
    public String getRowName() { return name; }

     /**
     * Obtiene los datos de la fila para representación tabular.
     *
     * @return Lista con los datos visibles del grid.
     */
    public ArrayList<Object> getRowData() {
        ArrayList<Object> data = new ArrayList<>();

        data.add(name);
        data.add(email);
        data.add(fiscalId);
        data.add(functionalArea);
        data.add(authorizedAt);
        data.add(isImported);
        return data;
    }

    @Override
    public boolean isRowSystem() { return false; }

    @Override
    public boolean isRowDeletable() { return false; }

    @Override
    public boolean isRowEdited() { return edited; }

    @Override
    public void setRowEdited(boolean edited) { this.edited = edited; }

    @Override
    public Object getRowValueAt(int i) {
        switch (i) {
            case 0: return name;
            case 1: return email;
            case 2: return fiscalId;
            case 3: return functionalArea;
            case 4: return authorizedAt;
            case 5: return isImported;
            default: return null;
        }
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        if (col == 5) {
            isImported = (Boolean) value;
        }
    }
}