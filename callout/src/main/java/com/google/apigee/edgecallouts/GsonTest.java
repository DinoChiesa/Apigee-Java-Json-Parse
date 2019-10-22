// GsonTest.java
//
// This is the source code for a Java callout for Apigee Edge.
// This callout is very simple - it just deserializes a payload using Google's Gson library.
//
// ------------------------------------------------------------------

package com.google.apigee.edgecallouts;

import com.apigee.flow.execution.ExecutionContext;
import com.apigee.flow.execution.ExecutionResult;
import com.apigee.flow.execution.spi.Execution;
import com.apigee.flow.message.MessageContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class GsonTest extends AbstractCallout implements Execution {
  static {
    varprefix = "gson_";
  }

  private static final Gson gson = new Gson();

  public GsonTest(Map properties) {
    this.properties = properties;
  }

  public ExecutionResult execute(final MessageContext msgCtxt, final ExecutionContext execContext) {
    try {
      String payload = getSimpleRequiredProperty("payload", msgCtxt);
      StringReader reader = new StringReader(payload);
      //java.lang.reflect.Type t = new TypeToken<Map<String, Object>>(){}.getType();      
      //Map<String, Object> map = gson.fromJson(reader, t);
      Map<String, Object> map = gson.fromJson(reader, Map.class);
      msgCtxt.setVariable(varName("output"), map);
      return ExecutionResult.SUCCESS;
    } catch (java.lang.Exception exc1) {
      setExceptionVariables(exc1, msgCtxt);
      msgCtxt.setVariable(varName("stacktrace"), getStackTraceAsString(exc1));
      return ExecutionResult.ABORT;
    }
  }
}
