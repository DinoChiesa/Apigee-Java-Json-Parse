# Java Json test

This directory contains the Java source code and pom.xml file required to
compile a pair of simple Java callouts for Apigee Edge, that do Json Deserialization.

There was some discussion about whether the [FasterXML Jackson](https://github.com/FasterXML/jackson) and (Google Gson)[https://github.com/google/gson] libraries would work within Java callouts in Apigee Edge.
This project can be used to test same.


## Usage Notes

There are two callout classes, com.dinochiesa.edgecallouts.{JacksonTest, GsonTest} .

To use them, you can configure policies like this:

Gson:

```xml
<JavaCallout name='Java-DeserViaGson'>
  <Properties>
    <Property name="payload">{newRequest.content}</Property>
  </Properties>
  <ClassName>com.dinochiesa.edgecallouts.GsonTest</ClassName>
  <ResourceURL>java://edge-java-callout-json-test-1.0.1.jar</ResourceURL>
</JavaCallout>
```

Jackson:

```xml
<JavaCallout name='Java-DeserViaJackson'>
  <Properties>
    <Property name="payload">{newRequest.content}</Property>
  </Properties>
  <ClassName>com.dinochiesa.edgecallouts.JacksonTest</ClassName>
  <ResourceURL>java://edge-java-callout-json-test-1.0.1.jar</ResourceURL>
</JavaCallout>
```

Notice the ClassName is the only thing that changes in the above.

Each callout accepts a single Property named "payload".

* If the property is missing,
  the callout will throw an exception, which will put the Apigee flow into fault status.

* The payload ought to be valid json. If not, the callout will throw an exception, which will put the Apigee flow into fault status.

If the payload is valid JSON then the callout de-serializes it into a Map<String,Object> and stores that map into a context variable, named
gson_output or jackson_output, depending on the class used.


## Example Bundle

You can find a working [example API Proxy bundle](./bundle) in this repo.
To deploy it you can use something like [importAndDeploy.js](https://github.com/DinoChiesa/apigee-edge-js/blob/master/examples/importAndDeploy.js), or your own favorite import-and-deploy tool. 


## Building

You don't need to build the JARs in order to test or use them. But if you decide to change the code, then you will need to rebuild.

To do so, use maven:
```
mvn clean package
```

You can also run the tests:
```
mvn clean test
```


## LICENSE

This material is copyright 2017 Google Inc.
and is licensed under the Apache 2.0 license. See the [LICENSE](LICENSE) file.


## Bugs

none?
