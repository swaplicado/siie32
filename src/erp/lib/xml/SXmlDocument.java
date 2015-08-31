/*
 * Copyright 2010-2013 Software Aplicado SA de CV
 * All rights reserved.
 */

package erp.lib.xml;

import java.util.Vector;

/**
 *
 * @author Sergio Flores
 */
public abstract class SXmlDocument extends SXmlElement {

    protected Vector<SXmlElement> mvXmlElements;

    public SXmlDocument(String name) {
        super(name);
        mvXmlElements = new Vector<>();
    }

    public Vector<SXmlElement> getXmlElements() { return mvXmlElements; }

    public Vector<SXmlElement> getXmlElements(final String name) {
        Vector<SXmlElement> elements = new Vector<>();

        for (SXmlElement element : mvXmlElements) {
            if (name.compareTo(element.getName()) == 0) {
                elements.add(element);
            }
        }

        return elements;
    }

    public abstract void processXml(final String xml) throws Exception;

    public void updateNodes() {
    
    }

    @Override
    public void clear() {
        super.clear();
        mvXmlElements.clear();
    }

    @Override
    public String getXmlString() {
        String aux = "";
        String xml = "";

        xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n";
        xml += "<!-- Copyright (C) Software Aplicado SA de CV. All rights reserved. -->\n";
        xml += "<" + msName + " xmlns=\"http://www.swaplicado.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";

        for (SXmlAttribute attribute : mvXmlAttributes) {
            aux = attribute.getXmlString();
            xml += aux.length() == 0 ? "" : " " + aux;
        }

        xml += ">";

        for (SXmlElement element : mvXmlElements) {
            aux = element.getXmlString();
            xml += aux.length() == 0 ? "" : "\n" + aux;
        }

        xml += "\n</" + msName + ">";

        return xml;
    }
}
