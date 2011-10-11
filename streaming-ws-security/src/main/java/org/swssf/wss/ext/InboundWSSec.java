/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.swssf.wss.ext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.swssf.wss.impl.WSSDocumentContextImpl;
import org.swssf.wss.impl.WSSecurityContextImpl;
import org.swssf.wss.impl.processor.input.SecurityHeaderInputProcessor;
import org.swssf.wss.impl.processor.input.SignatureConfirmationInputProcessor;
import org.swssf.wss.securityEvent.SecurityEvent;
import org.swssf.wss.securityEvent.SecurityEventListener;
import org.swssf.xmlsec.ext.InputProcessor;
import org.swssf.xmlsec.impl.InputProcessorChainImpl;
import org.swssf.xmlsec.impl.XMLSecurityStreamReader;
import org.swssf.xmlsec.impl.processor.input.LogInputProcessor;
import org.swssf.xmlsec.impl.processor.input.XMLEventReaderInputProcessor;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Inbound Streaming-WebService-Security
 * An instance of this class can be retrieved over the WSSec class
 *
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class InboundWSSec {

    protected static final transient Log log = LogFactory.getLog(InboundWSSec.class);

    private WSSSecurityProperties securityProperties;

    public InboundWSSec(WSSSecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    /**
     * Warning:
     * configure your xmlStreamReader correctly. Otherwise you can create a security hole.
     * At minimum configure the following properties:
     * xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
     * xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
     * xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, false);
     * xmlInputFactory.setProperty(WstxInputProperties.P_MIN_TEXT_SEGMENT, new Integer(8192));
     * <p/>
     * This method is the entry point for the incoming security-engine.
     * Hand over the original XMLStreamReader and use the returned one for further processing
     *
     * @param xmlStreamReader The original XMLStreamReader
     * @return A new XMLStreamReader which does transparently the security processing.
     * @throws XMLStreamException  thrown when a streaming error occurs
     * @throws WSSecurityException thrown when a Security failure occurs
     */
    public XMLStreamReader processInMessage(XMLStreamReader xmlStreamReader) throws XMLStreamException, WSSecurityException {
        return this.processInMessage(xmlStreamReader, new ArrayList<SecurityEvent>(), null);
    }

    /**
     * Warning:
     * configure your xmlStreamReader correctly. Otherwise you can create a security hole.
     * At minimum configure the following properties:
     * xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
     * xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
     * xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, false);
     * xmlInputFactory.setProperty(WstxInputProperties.P_MIN_TEXT_SEGMENT, new Integer(8192));
     * <p/>
     * This method is the entry point for the incoming security-engine.
     * Hand over the original XMLStreamReader and use the returned one for further processing
     *
     * @param xmlStreamReader       The original XMLStreamReader
     * @param securityEventListener A SecurityEventListener to receive security-relevant events.
     * @return A new XMLStreamReader which does transparently the security processing.
     * @throws XMLStreamException  thrown when a streaming error occurs
     * @throws WSSecurityException thrown when a Security failure occurs
     */
    public XMLStreamReader processInMessage(XMLStreamReader xmlStreamReader, List<SecurityEvent> requestSecurityEvents, SecurityEventListener securityEventListener) throws XMLStreamException, WSSecurityException {

        final WSSecurityContextImpl securityContextImpl = new WSSecurityContextImpl();
        securityContextImpl.putList(SecurityEvent.class, requestSecurityEvents);
        securityContextImpl.setSecurityEventListener(securityEventListener);

        final XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        securityContextImpl.put(WSSConstants.XMLINPUTFACTORY, xmlInputFactory);
        final XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(xmlStreamReader);

        WSSDocumentContextImpl documentContext = new WSSDocumentContextImpl();
        documentContext.setEncoding(xmlStreamReader.getEncoding() != null ? xmlStreamReader.getEncoding() : "UTF-8");
        InputProcessorChainImpl inputProcessorChain = new InputProcessorChainImpl(securityContextImpl, documentContext);
        inputProcessorChain.addProcessor(new XMLEventReaderInputProcessor(securityProperties, xmlEventReader));
        inputProcessorChain.addProcessor(new SecurityHeaderInputProcessor(securityProperties));

        if (securityProperties.isEnableSignatureConfirmationVerification()) {
            inputProcessorChain.addProcessor(new SignatureConfirmationInputProcessor(securityProperties));
        }

        if (log.isTraceEnabled()) {
            LogInputProcessor logInputProcessor = new LogInputProcessor(securityProperties);
            logInputProcessor.getAfterProcessors().add(SecurityHeaderInputProcessor.class.getName());
            inputProcessorChain.addProcessor(logInputProcessor);
        }

        List<InputProcessor> additionalInputProcessors = securityProperties.getInputProcessorList();
        Iterator<InputProcessor> inputProcessorIterator = additionalInputProcessors.iterator();
        while (inputProcessorIterator.hasNext()) {
            InputProcessor inputProcessor = inputProcessorIterator.next();
            inputProcessorChain.addProcessor(inputProcessor);
        }

        return new XMLSecurityStreamReader(inputProcessorChain, securityProperties);
    }
}