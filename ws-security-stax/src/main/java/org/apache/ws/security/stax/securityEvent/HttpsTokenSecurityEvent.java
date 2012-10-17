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
package org.apache.ws.security.stax.securityEvent;

import org.apache.ws.security.stax.ext.WSSConstants;
import org.apache.xml.security.stax.ext.SecurityToken;
import org.apache.xml.security.stax.ext.XMLSecurityConstants;
import org.apache.xml.security.stax.impl.securityToken.AbstractInboundSecurityToken;
import org.apache.xml.security.stax.securityEvent.TokenSecurityEvent;

/**
 * @author $Author$
 * @version $Revision$ $Date$
 */
public class HttpsTokenSecurityEvent extends TokenSecurityEvent {

    public enum AuthenticationType {
        HttpBasicAuthentication,
        HttpDigestAuthentication,
        HttpsClientCertificateAuthentication
    }

    private AuthenticationType authenticationType;
    //todo issuer only when a client cert is provided
    //todo remove issuerName here and reference it directly from the security token?
    private String issuerName;

    public HttpsTokenSecurityEvent() {
        super(WSSecurityEventConstants.HttpsToken);
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    @Override
    public SecurityToken getSecurityToken() {
        SecurityToken securityToken = super.getSecurityToken();
        if (securityToken == null) {
            securityToken = new AbstractInboundSecurityToken(null, null, null) {
                @Override
                public XMLSecurityConstants.TokenType getTokenType() {
                    return WSSConstants.HttpsToken;
                }
            };
        }
        setSecurityToken(securityToken);
        return securityToken;
    }
}
