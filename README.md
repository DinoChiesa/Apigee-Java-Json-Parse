# Apigee - Java Json test

This directory contains the Java source code and pom.xml file required to compile a pair
of simple Java callouts for Apigee, that do Json Deserialization.

On the Apigee community, there was some discussion about whether the [FasterXML Jackson](https://github.com/FasterXML/jackson)
and [Google Gson](https://github.com/google/gson) libraries would work within Java callouts in Apigee
Edge. This project can be used to test those libraries.

In short - both of those libraries used to work, but now (October 2019) neither does.
You can use the javax.json libraries if you serialize and deserialize Maps. Follow the example in
[JavaxTest.java](./callout/src/main/java/com/google/apigee/callouts/JavaxTest.java).

It does not work to
deserialize custom POJOs.

Any use of reflection is disallowed in Apigee
Edge callouts. Both the Gson and Jackson libraries use reflection.

## Status

This is an example.

## Disclaimer

This example is not an official Google product, nor is it part of an official Google product.

## Usage Notes

There are three callout classes:
* com.google.apigee.callouts.GsonTest
* com.google.apigee.callouts.JacksonTest
* com.google.apigee.callouts.JavaxTest

Each uses a different de-serializer to produce a Map<String,Object> from a JSON.

In my tests, both the Gson and Jackson libraries use reflection, which is not
permitted in Apigee. This leads to a runtime error.  The Javax version does not
use reflection, and works in Apigee.


To test them, you can configure policies like this:

Gson:

```xml
<JavaCallout name='Java-DeserViaGson'>
  <Properties>
    <Property name="payload">{newRequest.content}</Property>
  </Properties>
  <ClassName>com.google.apigee.callouts.GsonTest</ClassName>
  <ResourceURL>java://edge-java-callout-json-test-20191021.jar</ResourceURL>
</JavaCallout>
```

Jackson:

```xml
<JavaCallout name='Java-DeserViaJackson'>
  <Properties>
    <Property name="payload">{newRequest.content}</Property>
  </Properties>
  <ClassName>com.google.apigee.callouts.JacksonTest</ClassName>
  <ResourceURL>java://edge-java-callout-json-test-20191021.jar</ResourceURL>
</JavaCallout>
```

Javax:

```xml
<JavaCallout name='Java-DeserViaGson'>
  <Properties>
    <Property name="payload">{newRequest.content}</Property>
  </Properties>
  <ClassName>com.google.apigee.callouts.JavaxTest</ClassName>
  <ResourceURL>java://edge-java-callout-json-test-20191021.jar</ResourceURL>
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

You can find a working [example API Proxy bundle](./bundle) in this repo.  To import it
and deploy it into an Apigee Edge organization, you can use something like
[importAndDeploy.js](https://github.com/DinoChiesa/apigee-edge-js/blob/master/examples/importAndDeploy.js),
or your own favorite import-and-deploy tool.

## Invoking it

After you deploy the bundle, you can invoke it like this:

```
ORG=myorg
ENV=myenv

curl -i https://${ORG}-${ENV}.apigee.net/json-test/jackson

curl -i https://${ORG}-${ENV}.apigee.net/json-test/gson

curl -i https://${ORG}-${ENV}.apigee.net/json-test/javax

```


## Building

You don't need to build the JAR in order to use it. But if you decide to change the code, then you will need to rebuild.

To do so, use maven:
```
mvn clean package
```

You can also just run the tests:
```
mvn clean test
```


## LICENSE

This material is Copyright 2017-2019 Google LLC.
and is licensed under the Apache 2.0 license. See the [LICENSE](LICENSE) file.


## Bugs

none?
