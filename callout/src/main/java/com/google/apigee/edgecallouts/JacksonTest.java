// JacksonTest.java
//
// This is the source code for a Java callout for Apigee Edge.
// This callout is very simple - it just deserializes a payload using FastXML Jackson.
//
// ------------------------------------------------------------------

package com.google.apigee.edgecallouts;

import com.apigee.flow.execution.ExecutionContext;
import com.apigee.flow.execution.ExecutionResult;
import com.apigee.flow.execution.spi.Execution;
import com.apigee.flow.message.MessageContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class JacksonTest extends AbstractCallout implements Execution {
  static {
    varprefix = "jackson_";
  }

  private static final ObjectMapper om = new ObjectMapper();

  public JacksonTest(Map properties) {
    this.properties = properties;
  }

  public ExecutionResult execute(final MessageContext msgCtxt, final ExecutionContext execContext) {
    try {
      String payload = getSimpleRequiredProperty("payload", msgCtxt);
      StringReader reader = new StringReader(payload);
      Map<String, Object> map = om.readValue(reader, HashMap.class);
      msgCtxt.setVariable(varName("output"), map);
      return ExecutionResult.SUCCESS;
    } catch (java.lang.Exception exc1) {
      setExceptionVariables(exc1, msgCtxt);
      msgCtxt.setVariable(varName("stacktrace"), getStackTraceAsString(exc1));
      return ExecutionResult.ABORT;
    }
  }
}
