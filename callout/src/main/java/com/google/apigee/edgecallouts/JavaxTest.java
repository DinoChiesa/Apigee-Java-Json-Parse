// JavaxTest.java
//
// This is the source code for a Java callout for Apigee Edge.
// This callout is very simple - it just deserializes a payload using the javax.json library.
//
// ------------------------------------------------------------------

package com.google.apigee.edgecallouts;

import com.google.apigee.json.JavaxJson;
import com.apigee.flow.execution.ExecutionContext;
import com.apigee.flow.execution.ExecutionResult;
import com.apigee.flow.execution.spi.Execution;
import com.apigee.flow.message.MessageContext;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class JavaxTest extends AbstractCallout implements Execution {
  static {
    varprefix = "javaxjson_";
  }

  public JavaxTest(Map properties) {
    this.properties = properties;
  }

  public ExecutionResult execute(final MessageContext msgCtxt, final ExecutionContext execContext) {
    try {
      String payload = getSimpleRequiredProperty("payload", msgCtxt);
      Map<String, Object> map = JavaxJson.fromJson(payload, Map.class);
      msgCtxt.setVariable(varName("output"), map);
      return ExecutionResult.SUCCESS;
    } catch (java.lang.Exception exc1) {
      setExceptionVariables(exc1, msgCtxt);
      msgCtxt.setVariable(varName("stacktrace"), getStackTraceAsString(exc1));
      System.out.printf("\n** %s\n", getStackTraceAsString(exc1));

      return ExecutionResult.ABORT;
    }
  }
}
