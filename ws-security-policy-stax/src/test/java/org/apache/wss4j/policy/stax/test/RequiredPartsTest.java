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
package org.apache.wss4j.policy.stax.test;

import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.policy.stax.PolicyEnforcer;
import org.apache.wss4j.stax.ext.WSSConstants;
import org.apache.wss4j.stax.securityEvent.OperationSecurityEvent;
import org.apache.wss4j.stax.securityEvent.RequiredPartSecurityEvent;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.namespace.QName;

import java.util.ArrayList;
import java.util.List;

public class RequiredPartsTest extends AbstractPolicyTestBase {

    @Test
    public void testPolicy() throws Exception {
        String policyString =
                "<sp:RequiredParts xmlns:sp=\"http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200702\" xmlns:sp3=\"http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200802\">\n" +
                        "<sp:Header Name=\"a\" Namespace=\"http://example.org\"/>\n" +
                        "</sp:RequiredParts>";
        PolicyEnforcer policyEnforcer = buildAndStartPolicyEngine(policyString);

        RequiredPartSecurityEvent requiredPartSecurityEvent = new RequiredPartSecurityEvent();
        List<QName> headerPath = new ArrayList<>();
        headerPath.addAll(WSSConstants.SOAP_11_HEADER_PATH);
        headerPath.add(new QName("http://example.org", "a"));
        requiredPartSecurityEvent.setElementPath(headerPath);
        policyEnforcer.registerSecurityEvent(requiredPartSecurityEvent);

        //additional requiredParts are also allowed!
        requiredPartSecurityEvent = new RequiredPartSecurityEvent();
        headerPath = new ArrayList<>();
        headerPath.addAll(WSSConstants.SOAP_11_HEADER_PATH);
        headerPath.add(new QName("http://example.org", "b"));
        requiredPartSecurityEvent.setElementPath(headerPath);
        policyEnforcer.registerSecurityEvent(requiredPartSecurityEvent);

        OperationSecurityEvent operationSecurityEvent = new OperationSecurityEvent();
        operationSecurityEvent.setOperation(new QName("definitions"));
        policyEnforcer.registerSecurityEvent(operationSecurityEvent);

        policyEnforcer.doFinal();
    }

    @Test
    public void testPolicyMultipleAssertionEventsNegative() throws Exception {
        String policyString =
                "<sp:RequiredParts xmlns:sp=\"http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200702\" xmlns:sp3=\"http://docs.oasis-open.org/ws-sx/ws-securitypolicy/200802\">\n" +
                        "<sp:Header Name=\"a\" Namespace=\"http://example.org\"/>\n" +
                        "</sp:RequiredParts>";
        PolicyEnforcer policyEnforcer = buildAndStartPolicyEngine(policyString);

        RequiredPartSecurityEvent requiredPartSecurityEvent = new RequiredPartSecurityEvent();
        List<QName> headerPath = new ArrayList<>();
        headerPath.addAll(WSSConstants.SOAP_11_HEADER_PATH);
        headerPath.add(new QName("http://example.org", "b"));
        requiredPartSecurityEvent.setElementPath(headerPath);
        policyEnforcer.registerSecurityEvent(requiredPartSecurityEvent);

        OperationSecurityEvent operationSecurityEvent = new OperationSecurityEvent();
        operationSecurityEvent.setOperation(new QName("definitions"));

        try {
            policyEnforcer.registerSecurityEvent(operationSecurityEvent);
            Assert.fail("Exception expected");
        } catch (WSSecurityException e) {
            Assert.assertEquals(e.getMessage(), "Element {http://example.org}a must be present");
        }
    }
}
