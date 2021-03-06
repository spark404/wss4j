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

package org.apache.wss4j.dom.common;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.util.KeyUtils;
import org.apache.xml.security.utils.Base64;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A Callback Handler implementation for the case of storing a secret key.
 */
public class SecretKeyCallbackHandler implements CallbackHandler {
    
    private Map<String, byte[]> secrets = new HashMap<>();
    private byte[] outboundSecret = null;
    
    public void handle(Callback[] callbacks)
        throws IOException, UnsupportedCallbackException {
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof WSPasswordCallback) {
                WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
                if ((pc.getUsage() == WSPasswordCallback.SECRET_KEY)
                    || (pc.getUsage() == WSPasswordCallback.SECURITY_CONTEXT_TOKEN)) {
                    byte[] secret = this.secrets.get(pc.getIdentifier());
                    if (secret == null) {
                        secret = outboundSecret;
                    }
                    pc.setKey(secret);
                    break;
                }
            } else {
                throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
            }
        }
    }
    
    public void addSecretKey(String identifier, byte[] secretKey) {
        secrets.put(identifier, secretKey);
    }
    
    public void setOutboundSecret(byte[] secret) throws WSSecurityException {
        outboundSecret = secret;
        byte[] encodedBytes = KeyUtils.generateDigest(outboundSecret);
        String identifier = Base64.encode(encodedBytes);
        addSecretKey(identifier, outboundSecret);
    }
}
