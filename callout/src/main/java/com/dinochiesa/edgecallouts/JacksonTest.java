// JacksonTest.java
//
// This is the source code for a Java callout for Apigee Edge.
// This callout is very simple - it just deserializes a payload using FastXML Jackson.
//
// ------------------------------------------------------------------

package com.dinochiesa.edgecallouts;

import com.apigee.flow.execution.ExecutionContext;
import com.apigee.flow.execution.ExecutionResult;
import com.apigee.flow.execution.spi.Execution;
import com.apigee.flow.message.MessageContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class JacksonTest extends AbstractCallout implements Execution {
    private final static String varprefix= "jackson_";
    private final static ObjectMapper om = new ObjectMapper();

    public JacksonTest(Map properties) {
        this.properties = properties;
    }

    private static String varName(String s) { return varprefix + s;}

    public ExecutionResult execute (final MessageContext msgCtxt,
                                    final ExecutionContext execContext) {
        try {
            String payload = getSimpleRequiredProperty("payload", msgCtxt);
            StringReader reader = new StringReader(payload);
            Map<String,Object> map = om.readValue(reader, HashMap.class);
            msgCtxt.setVariable(varName("output"), map);
            return ExecutionResult.SUCCESS;
        }
        catch (java.lang.Exception exc1) {
            msgCtxt.setVariable(varName("error"), exc1.getMessage());
            msgCtxt.setVariable(varName("stacktrace"), ExceptionUtils.getStackTrace(exc1));
            return ExecutionResult.ABORT;
        }
    }
}
