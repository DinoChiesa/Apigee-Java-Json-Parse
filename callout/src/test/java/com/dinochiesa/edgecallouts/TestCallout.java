package com.dinochiesa.edgecallouts;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import mockit.Mock;
import mockit.MockUp;

import com.apigee.flow.execution.ExecutionContext;
import com.apigee.flow.execution.ExecutionResult;
import com.apigee.flow.message.MessageContext;
import com.apigee.flow.message.Message;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TestCallout {
    private final static String testDataDir = "src/test/resources/test-data";

    MessageContext msgCtxt;
    InputStream messageContentStream;
    Message message;
    ExecutionContext exeCtxt;

    @BeforeMethod()
    public void beforeMethod() {

        msgCtxt = new MockUp<MessageContext>() {
            private Map variables;
            public void $init() {
                variables = new HashMap();
            }

            @Mock()
            public <T> T getVariable(final String name){
                if (variables == null) {
                    variables = new HashMap();
                }
                return (T) variables.get(name);
            }

            @Mock()
            public boolean setVariable(final String name, final Object value) {
                if (variables == null) {
                    variables = new HashMap();
                }
                variables.put(name, value);
                return true;
            }

            @Mock()
            public boolean removeVariable(final String name) {
                if (variables == null) {
                    variables = new HashMap();
                }
                if (variables.containsKey(name)) {
                    variables.remove(name);
                }
                return true;
            }

            @Mock()
            public Message getMessage() {
                return message;
            }
        }.getMockInstance();

        exeCtxt = new MockUp<ExecutionContext>(){ }.getMockInstance();

        message = new MockUp<Message>(){
            @Mock()
            public InputStream getContentAsStream() {
                // new ByteArrayInputStream(messageContent.getBytes(StandardCharsets.UTF_8));
                return messageContentStream;
            }
        }.getMockInstance();
    }


    @Test
    public void test_GsonDeserializeSimple() throws Exception {
        msgCtxt.setVariable("json-source", SourceData.jsonPayload1);

        Map<String,String> props = new HashMap<String,String>();
        props.put("payload","{json-source}");

        GsonTest callout = new GsonTest(props);

        // execute and retrieve output
        ExecutionResult actualResult = callout.execute(msgCtxt, exeCtxt);
        Assert.assertEquals(actualResult, ExecutionResult.SUCCESS, "result not as expected");

        Map<String,Object> output = (Map<String,Object>) msgCtxt.getVariable("gson_output");
        //System.out.println(output);

        String appName = (String) output.get("applicationName");
        Assert.assertEquals(appName, "sandbox", "sandbox property not as expected");

        System.out.println("=========================================================");
    }

    @Test
    public void test_JacksonDeserializeSimple() throws Exception {
        msgCtxt.setVariable("json-source", SourceData.jsonPayload1);

        Map<String,String> props = new HashMap<String,String>();
        props.put("payload","{json-source}");

        JacksonTest callout = new JacksonTest(props);

        // execute and retrieve output
        ExecutionResult actualResult = callout.execute(msgCtxt, exeCtxt);
        Assert.assertEquals(actualResult, ExecutionResult.SUCCESS, "result not as expected");

        Map<String,Object> output = (Map<String,Object>) msgCtxt.getVariable("jackson_output");
        System.out.println(output);

        String appName = (String) output.get("applicationName");
        Assert.assertEquals(appName, "sandbox", "sandbox property not as expected");

        System.out.println("=========================================================");
    }
    
    @Test
    public void test_JacksonDeserialize_NonJsonPayload() throws Exception {
        msgCtxt.setVariable("json-source", "this is not {valid: json}");

        Map<String,String> props = new HashMap<String,String>();
        props.put("payload","{json-source}");
        JacksonTest callout = new JacksonTest(props);
        ExecutionResult actualResult = callout.execute(msgCtxt, exeCtxt);
        Assert.assertEquals(actualResult, ExecutionResult.ABORT, "result not as expected");
        String errorOutput = (String) msgCtxt.getVariable("jackson_error");
        Assert.assertNotNull(errorOutput, "errorOutput");
        Assert.assertTrue(errorOutput.startsWith("Unrecognized token 'this':"), "errorOutput");
    }
    
    @Test
    public void test_JacksonDeserialize_NoPayload() throws Exception {
        Map<String,String> props = new HashMap<String,String>();
        JacksonTest callout = new JacksonTest(props);
        ExecutionResult actualResult = callout.execute(msgCtxt, exeCtxt);
        Assert.assertEquals(actualResult, ExecutionResult.ABORT, "result not as expected");
        String errorOutput = (String) msgCtxt.getVariable("jackson_error");
        Assert.assertNotNull(errorOutput, "errorOutput");
        Assert.assertEquals(errorOutput,"payload resolves to an empty string.", "errorOutput");
    }

}
