/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cfd;

import erp.lib.form.SFormComponentItem;
import erp.mod.SModConsts;
import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import sa.lib.xml.SXmlUtils;

/**
 * Class from management of catalogs from SAT.
 * @author Juan Barajas, Sergio Flores
 */
public class SCfdXmlCatalogs {
    
    private SGuiSession moSession;
    private HashMap<Integer, ArrayList<SCfdXmlCatalogEntry>> mhmCatalogs = new HashMap<>();

    public SCfdXmlCatalogs(SGuiSession session) throws Exception {
        moSession = session;
        computeCatalogs();
    }
    
    /**
     * Reads all CFD XML catalogs.
     * @throws Exception 
     */
    private void computeCatalogs() throws Exception {
        mhmCatalogs.clear();
        
        String sql = "SELECT id_cfd_cat, xml " +
               "FROM " + SModConsts.TablesMap.get(SModConsts.TRNS_CFD_CAT) + " " +
               "WHERE NOT b_del " + 
               "ORDER BY id_cfd_cat ";

        ResultSet resultSet = moSession.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            String xml = resultSet.getString("xml");
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docBuilder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            
            ArrayList<SCfdXmlCatalogEntry> entries = new ArrayList<>();
            Node catalogNode = SXmlUtils.extractElements(doc, "Catalog").item(0);
            Vector<Node> entryNodes = SXmlUtils.extractChildElements(catalogNode, "Entry");
            for (Node entryNode : entryNodes) {
                NamedNodeMap entryNodeMap = entryNode.getAttributes();
                entries.add(new SCfdXmlCatalogEntry(
                SXmlUtils.extractAttributeValue(entryNodeMap, "code", true),
                SXmlUtils.extractAttributeValue(entryNodeMap, "name", true),
                SLibUtils.DbmsDateFormatDate.parse(SXmlUtils.extractAttributeValue(entryNodeMap, "validity_start", true)),
                SXmlUtils.extractAttributeValue(entryNodeMap, "validity_end", true).isEmpty() ? null : SLibUtils.DbmsDateFormatDate.parse(SXmlUtils.extractAttributeValue(entryNodeMap, "validity_end", true)),
                SLibUtils.parseInt(SXmlUtils.extractAttributeValue(entryNodeMap, "taxpayer_per", false)) == 1,
                SLibUtils.parseInt(SXmlUtils.extractAttributeValue(entryNodeMap, "taxpayer_org", false)) == 1));
            }

            int catalog = resultSet.getInt("id_cfd_cat");
            mhmCatalogs.put(catalog, entries);
        }
    }
    
    /**
     * Gets all the entries for given catalog as an array of <code>SFormComponentItem</code> objects to be added to a <code>JComboBox</code>.
     * @param catalog Desired catalog (SDataConstantsSys.TRNS_CFD_CAT_...).
     * @param date Date to check validity of catalog entries.
     * @return Vector of component items.
     */
    public Vector<SFormComponentItem> getComponentItems(final int catalog, final Date date) {
        Vector<SFormComponentItem> items = new Vector<SFormComponentItem>();
        items.add(new SFormComponentItem(new int[] {}, "(Seleccionar opción)"));
        
        ArrayList<SCfdXmlCatalogEntry> entries = mhmCatalogs.get(catalog);
        if (entries != null) {
            for (SCfdXmlCatalogEntry entry : entries) {
                if (entry.getValidityStart().compareTo(date) <= 0 && (entry.getValidityEnd() == null || entry.getValidityEnd().compareTo(date) >= 0)) {
                    items.add(new SFormComponentItem(entry.getCode(), entry.getCode() + " - " + entry.getName()));
                }
            }
        }
        
        return items;
    }
    
    /**
     * Populates given combo box with entries for given catalog.
     * @param comboBox Combo box to populate.
     * @param catalog Desired catalog (SDataConstantsSys.TRNS_CFD_CAT_...).
     * @param date Date to check validity of catalog entries.
     */
    public void populateComboBox(final JComboBox comboBox, final int catalog, final Date date) {
        Vector<SFormComponentItem> items = getComponentItems(catalog, date);
        comboBox.removeAllItems();
        for (SFormComponentItem item : items) {
            comboBox.addItem(item);
        }
    }
    
    /**
     * Gets code of catalog entry for corresponding name from given catalog.
     * @param catalog Desired catalog (SDataConstantsSys.TRNS_CFD_CAT_...).
     * @param name Name of desired catalog-entry's code.
     * @return Code of desired catalog entry. An empty <code>String</code> for invalid catalog or name.
     */
    public String getEntryCode(final int catalog, final String name) {
        String code = "";
        
        ArrayList<SCfdXmlCatalogEntry> entries = mhmCatalogs.get(catalog);
        for (SCfdXmlCatalogEntry entry : entries) {
            if (entry.getName().compareTo(name) == 0) {
                code = entry.getCode();
            }
        }
        
        return code;
    }
    
    /**
     * Gets name of catalog entry for corresponding code from given catalog.
     * @param catalog Desired catalog (SDataConstantsSys.TRNS_CFD_CAT_...).
     * @param code Code of desired catalog-entry's name.
     * @return Name of desired catalog entry. An empty <code>String</code> for invalid catalog or code.
     */
    public String getEntryName(final int catalog, final String code) {
        String name = "";
        
        ArrayList<SCfdXmlCatalogEntry> entries = mhmCatalogs.get(catalog);
        for (SCfdXmlCatalogEntry entry : entries) {
            if (entry.getCode().compareTo(code) == 0) {
                name = entry.getName();
            }
        }
        
        return name;
    }
    
    /**
     * Composes code and name of catalog entry for corresponding code of given catalog entry.
     * @param catalog Desired catalog (SDataConstantsSys.TRNS_CFD_CAT_...).
     * @param code Code of desired catalog entry.
     * @return Composed code and name of desired catalog entry. An empty <code>String</code> for invalid catalog or code.
     */
    public String composeEntryDescription(final int catalog, final String code) {
        String name = getEntryName(catalog, code);
        return name.isEmpty() ? "" : code + " - " + name;
    }
}
