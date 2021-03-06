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

package org.apache.wss4j.dom.str;

import org.w3c.dom.Element;

import org.apache.wss4j.dom.WSDocInfo;
import org.apache.wss4j.dom.handler.RequestData;

/**
 * This class holds the parameters for parsing a SecurityTokenReference Element by a STRParser implementation.
 */
public class STRParserParameters {
    
    private int derivationKeyLength;
    private Element strElement;
    private RequestData data;
    private WSDocInfo wsDocInfo;
    
    public int getDerivationKeyLength() {
        return derivationKeyLength;
    }
    
    public void setDerivationKeyLength(int derivationKeyLength) {
        this.derivationKeyLength = derivationKeyLength;
    }
    
    public Element getStrElement() {
        return strElement;
    }
    
    public void setStrElement(Element strElement) {
        this.strElement = strElement;
    }
    
    public RequestData getData() {
        return data;
    }
    
    public void setData(RequestData data) {
        this.data = data;
    }
    
    public WSDocInfo getWsDocInfo() {
        return wsDocInfo;
    }
    
    public void setWsDocInfo(WSDocInfo wsDocInfo) {
        this.wsDocInfo = wsDocInfo;
    }

    
}
