package com.dinochiesa.edgecallouts;

import com.apigee.flow.message.MessageContext;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractCallout {
    private static final String variableReferencePatternString = "(.*?)\\{([^\\{\\} ]+?)\\}(.*?)";
    private static final Pattern variableReferencePattern = Pattern.compile(variableReferencePatternString);
    Map properties; // read-only

    protected String getSimpleRequiredProperty(String propName, MessageContext msgCtxt) throws Exception {
        String value = (String) this.properties.get(propName);
        if (value == null) {
            throw new IllegalStateException(propName + " resolves to an empty string.");
        }
        value = value.trim();
        if (value.equals("")) {
            throw new IllegalStateException(propName + " resolves to an empty string.");
        }
        value = resolvePropertyValue(value, msgCtxt);
        if (value == null || value.equals("")) {
            throw new IllegalStateException(propName + " resolves to an empty string.");
        }
        return value;
    }

    // If the value of a property contains a pair of curlies,
    // eg, {apiproxy.name}, then "resolve" the value by de-referencing
    // the context variable whose name appears between the curlies.
    private String resolvePropertyValue(String spec, MessageContext msgCtxt) {
        Matcher matcher = variableReferencePattern.matcher(spec);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "");
            sb.append(matcher.group(1));
            sb.append((String) msgCtxt.getVariable(matcher.group(2)));
            sb.append(matcher.group(3));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
