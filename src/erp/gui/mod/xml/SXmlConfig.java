package erp.gui.mod.xml;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sa.lib.SLibUtils;
import sa.lib.xml.SXmlUtils;

/**
 * Represents element <code>config</code> of XML GUI configuration file.
 *
 * @author Sergio Flores
 */
public class SXmlConfig {

    protected String msVersion;
    protected String msXml;
    protected ArrayList<SXmlScope> maChildScopes;

    public SXmlConfig(String version) {
        msVersion = version;
        msXml = "";
        maChildScopes = new ArrayList<>();
    }
    
    /*
     * Public setter/getter methods:
     */

    public ArrayList<SXmlScope> getChildScopes() {
        return maChildScopes;
    }

    public void setVersion(String s) {
        msVersion = s;
    }

    public void setXml(String s) {
        msXml = s;
    }

    public String getVersion() {
        return msVersion;
    }
    
    public String getXml() {
        return msXml;
    }
    
    /*
     * Public methods:
     */

    public SXmlScope getScope(int ambit, int reference) {
        SXmlScope xmlScope = null;
        
        for (SXmlScope scope : maChildScopes) {
            if (scope.getAmbit() == ambit && scope.getReference() == reference) {
                xmlScope = scope;
                break;
            }
        }
        
        return xmlScope;
    }
    
    public void parseConfig(final String xml) throws Exception {
        String version = "";
        
        maChildScopes.clear();
        
        Document doc = SXmlUtils.parseDocument(msXml = xml);
        Node configNode = SXmlUtils.extractElements(doc, "config").item(0);
        NamedNodeMap configAttribs = configNode.getAttributes();
        
        version = SXmlUtils.extractAttributeValue(configAttribs, "version", true);
        
        if (version.compareTo(msVersion) != 0) {
            throw new Exception(SXmlModConsts.MSG_ERR_VER);
        }
        
        if (SXmlUtils.hasChildElement(configNode, "scope")) {
            ArrayList<Node> scopeNodes = new ArrayList<Node>(SXmlUtils.extractChildElements(configNode, "scope"));
            for (Node scopeNode : scopeNodes) {
                NamedNodeMap scopeAttribs = scopeNode.getAttributes();
                SXmlScope scope = new SXmlScope(
                        SXmlModUtils.resolveAmbit(SXmlUtils.extractAttributeValue(scopeAttribs, "ambit", true)), 
                        SLibUtils.parseInt(SXmlUtils.extractAttributeValue(scopeAttribs, "reference", true)));

                if (SXmlUtils.hasChildElement(scopeNode, "module")) {
                    ArrayList<Node> moduleNodes = new ArrayList<Node>(SXmlUtils.extractChildElements(scopeNode, "module"));
                    for (Node moduleNode : moduleNodes) {
                        NamedNodeMap moduleAttribs = moduleNode.getAttributes();
                        SXmlModule module = new SXmlModule(
                                SXmlUtils.extractAttributeValue(moduleAttribs, "id", true));

                        if (SXmlUtils.hasChildElement(moduleNode, "menu")) {
                            ArrayList<Node> menuNodes = new ArrayList<Node>(SXmlUtils.extractChildElements(moduleNode, "menu"));
                            for (Node menuNode : menuNodes) {
                                NamedNodeMap menuAttribs = menuNode.getAttributes();
                                SXmlMenu menu = new SXmlMenu(
                                        SXmlUtils.extractAttributeValue(menuAttribs, "id", true));

                                if (SXmlUtils.hasChildElement(menuNode, "section")) {
                                    ArrayList<Node> sectionNodes = new ArrayList<Node>(SXmlUtils.extractChildElements(menuNode, "section"));
                                    for (Node sectionNode : sectionNodes) {
                                        NamedNodeMap sectionAttribs = sectionNode.getAttributes();
                                        SXmlMenuSection section = new SXmlMenuSection(
                                                SXmlUtils.extractAttributeValue(sectionAttribs, "id", true));

                                        if (SXmlUtils.hasChildElement(sectionNode, "item")) {
                                            ArrayList<Node> itemNodes = new ArrayList<Node>(SXmlUtils.extractChildElements(sectionNode, "item"));
                                            for (Node itemNode : itemNodes) {
                                                NamedNodeMap itemAttribs = itemNode.getAttributes();
                                                SXmlMenuSectionItem item = new SXmlMenuSectionItem(
                                                        SXmlUtils.extractAttributeValue(itemAttribs, "id", true));

                                                section.maChildItems.add(item);
                                            }
                                        }

                                        menu.maChildSections.add(section);
                                    }
                                }

                                module.maChildMenus.add(menu);
                            }
                        }

                        scope.maChildModules.add(module);
                    }
                }

                maChildScopes.add(scope);
            }
        }
    }
}
