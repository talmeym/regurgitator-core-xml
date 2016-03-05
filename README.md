# regurgitator-core-xml

regurgitator is a modular, light-weight, extendable java-based processing framework designed to 'regurgitate' canned or clever responses to incoming requests; useful for mocking or prototyping services.

start your reading here: [regurgitator-all](http://github.com/talmeym/regurgitator-all#regurgitator)

## xml configuration of regurgitator

below is an example of an xml configuration file for regurgitator:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<rg:regurgitator-configuration
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:rg="http://core.regurgitator.emarte.com"
		xmlns:rge="http://extensions.regurgitator.emarte.com"
		xmlns:rgw="http://web.extensions.regurgitator.emarte.com"
		xsi:schemaLocation="http://core.regurgitator.emarte.com regurgitatorCore.xsd
                            http://extensions.regurgitator.emarte.com regurgitatorExtensions.xsd
                            http://web.extensions.regurgitator.emarte.com regurgitatorExtensionsWeb.xsd"
		id="my-configuration">

	<rg:create-parameter name="response" value="quick">
		<rge:freemarker-processor>That was ${value}</rge:freemarker-processor>
	</rg:create-parameter>

	<rgw:create-http-response content-type="text/plain" status-code="200" source="response"/>
	
</rg:regurgitator-configuration>
```

every regurgitator xml file is schema validated upon load. all elements must be namespaced and each module of regurgitator has it's own namespace uri and schema file, eg:
- regurgitator-core ``http://core.regurgitator.emarte.com`` ``regurgitatorCore.xsd``
- regurgitator-extensions ``http://extensions.regurgitator.emarte.com``  ``regurgitatorExtensions.xsd``
- regurgitator-extensions-web ``http://web.extensions.regurgitator.emarte.com``  ``regurgitatorExtensionsWeb.xsd``

all steps in a regurgitator configuration, from ``regurgitator-configuration`` downwards, can be given an id attribute. ids can be used for identifying which step to run next (see decision, below) and therefore must be unique. if no id attribute is given for a step, a system-generated one will be assigned ot it at load time, combining the type of the step with a 4 digit randon number, eg: ``create-parameter-6557``

## core steps in xml

### sequence

a sequence executes a series of child steps, one after another in order

```xml
<rg:sequence id="my-sequence">
	<rg:create-parameter id="my-step-1" name="response" value="this is the response"/>
	<rg:create-response id="my-step-2" source="response"/>
</rg:sequence>
```

by default, when each child step executes, it is passed the same message object that the sequence received. it is possible to "isolate" a sequence's child steps from the data contained in the message object, by giving the sequence an isolation level. this prevents a child step from receiving data it shouldn't see or that it won't need.

```xml
<rg:sequence id="my-sequence" isolate="with-parameter">
	<rg:create-parameter id="my-step-1" name="response" value="this is the response"/>
	<rg:create-response id="my-step-2" source="response"/>
</rg:sequence>
```

isolation has 4 settings:

| value | child step receives |
| :--- | :--- |
| ``true`` | new blank message object |
| ``with-parameters`` | new message object containing the parameters context of the original message |
| ``with-session`` | new message object containing the session context of the original message |
| ``with-parameters-and-session`` | new message object containing parameters and session of the original message |

### decision

a decision executes one or more child steps, using ``rules`` and ``conditions`` to determine which steps to run

```xml
<rg:decision>
	<rg:steps>
		<rg:create-response id="default-step" value="this is the default response">
		<rg:create-response id="special-step" value="this is the special response"/>
	</rg:steps>
	<rg:rules default-step="default-step">
		<rg:rule step="special-step">
			<rg:condition source="parameters:special" equals="true"/>
		</rg:rule>
	</rg:rules>
</rg:decision>
```

upon execution a decision evaluates all of its rules to see which pass. it then uses its ``rules behaviour`` to determines which of the passed rules should have their corresponding step executed. the default rules behaviour is ``FIRST_MATCH`` whereby the first rule that passes provides the step to be executed.

there are 3 core rules behaviours:

| value | behaviour |
| :--- | :--- |
| ``FIRST_MATCH`` | execute the step of the first rule passed |
| ``FIRST_MATCH_ONWARDS`` | execute the step of the first rule passed, and the steps of all subsequent passed rules |
| ``ALL_MATCHES`` | execute the steps of all passed rules |

each rule has one or more conditions that must be satisfied to make the rule pass. each condition evaluates the value of a parameter within the message object, specified by the ``source`` attribute, against an operand. each condition has a ``condition behaviour`` that dictates the manner in which the value is evaluated against the operand. the example above uses the ``equals`` condition behaviour, specified as an attribute.

the behaviour of a condition can also be specified as a child element of the parent condition, as shown below:

```xml
	...
	<rg:rules default-step="no-id-found">
		<rg:rule step="found-id">
			<rg:condition source="parameters:xml">
				<rge:contains-xpath namespaces="rg=http://url.com">/rg:config/@id</rge:contains-xpath>
			</rg:condition>
		</rg:rule>
	</rg:rules>

	...
```

this allows certain condition behaviours to have attributes beyond the operand (in the example above, "/rg:config/@id") which is always the text of the child element.

there are 5 core condition behaviours:

| value | behaviour |
| :--- | :--- |
| ``equals`` | checks the parameter value equals the operand |
| ``equals-param`` | checks the parameter value equals the value of another parameter |
| ``exists`` | checks the parameter value exists (read as 'parameter exists') |
| ``contains`` | checks the parameter value contains the operand |
| ``contains-param`` | checks the parameter value contains the value of another parameter |

### create-parameter

a create-parameter creates a parameter in the message, with a type and a value

```xml
<rg:create-parameter name="index" type="NUMBER" value="5" merge="CONCAT"/>
```

a create-parameter can have one of the following value sources:

| attribute| value source | example |
|:---|:---|:---|
| ``source`` | value drawn from a source parameter | ``request-metadata:query-param`` |
| ``value`` | value provided explicitly | ``arg1=this,arg2=that`` |
| ``file`` | value loaded from a file | ``classpath:/query-param.txt`` |

a create-parameter can have a ``merge`` attribute that specifies a ``conflict policy`` defining what to do if the parameter being created already exists in the message. both the ``merge`` and ``type`` attributes are optional, with their defaults being ``REPLACE`` and ``STRING``, respectively.

there are 4 core conflict policies available:

| merge type | behaviour | ``STRING`` example | result |
|:---|:---|:---|:---|
| ``LEAVE`` | leave the existing value in place | existing: ``some`` new: ``thing`` | ``some`` |
| ``REPLACE`` | replace the existing value with the new | existing: ``some`` new: ``thing`` | ``thing`` |
| ``CONCAT`` | concatenate the existing and new values | existing:``some`` new: ``thing`` | ``something`` |
| ``REMOVE`` | remove the new value from the existing | existing:``some`` new: ``me`` | ``so`` |

when using ``NUMBER`` and ``DECIMAL`` parameter types, ``CONCAT`` and ``REMOVE`` conflict policies behave as addition and subtraction operators. when collection-based parameter types are used, ``CONCAT`` and ``REMOVE`` behaves like java collection ``add-all`` and ``remove-all`` operations, respectively.

find more details on parameter types in [regurgitator-core](https://github.com/talmeym/regurgitator-core#parameter-types).

to create a parameter in a particular parameter context, simple prepend your parameters name with the desired context, separated by a colon, eg: 

```xml 
<rg:create-parameter name="context:param-name" value="some value"/> 
``` 

to draw a source parameter value from a particular context, use the same notation in the ``source`` attribute, eg: 

```xml 
<rg:create-parameter name="some-param" source="context:other-param"/>
```

#### value-processors

all steps that create parameters (as well as ``create-response``) have the ability to include a ``value-processor``. this is extra processing that is applied to the steps value after it is built / generated / retrieved, as seen below:

```xml 
<rg:create-parameter name="positive-spin" value="you are unhappy">
	<rg:substitute-processor token="un" replacement="very "/>
</rg:create-parameter>
```

the manner in which the value is processed depends on the processor included.

### build-parameter

a build-parameter creates a parameter in the message, with it's value built by a ``value-builder``

```xml
<rg:build-parameter name="response" type="STRING" merge="CONCAT">
	<rge:freemarker-builder file="classpath:/response_file.ftl"/>
</rg:build-parameter>
```

as with create-parameter above, a build-parameter can have optional ``merge`` and ``type`` attributes, their defaults being ``REPLACE`` and ``STRING``, respectively.

### generate-parameter

a generate-parameter creates a parameter in the message, with it's value generated by a ``value-generator``

```xml
<rg:generate-parameter name="random-number" type="NUMBER" merge="REPLACE">
	<rg:number-generator max="10"/>
</rg:generate-parameter>
```

as with create-parameter above, a generate-parameter can have optional ``merge`` and ``type`` attributes, their defaults being ``REPLACE`` and ``STRING``, respectively.

generators that require no configuration can be specified through the ``generator`` attribute, eg:

```xml
<rg:generate-parameter name="new-id" type="STRING" generator="uuid-generator"/>
```

### create-response

a create-response returns a response back from regurgitator via a message's ``response-callback`` mechanism. 

```xml
<rg:create-response source="parameters:response-test"/>

<rg:create-response value="<xml>this is the response</xml>"/>

<rg:create-response file="classpath:/canned_response.xml"/>
```
a create-response can have the same value sources as create-parameter, ``source``, ``value``, or ``file``. 

regurgitator can be configured to return as many responses as is desired, but that may be incompatible with some single request / response usages, such as over [http](https://github.com/talmeym/regurgitator-extensions-web#regurgitator-over-http).

### identify-session

an identify-session step allows you to create or retrieve a cached session parameter context for your message object.

```xml
<rg:identify-session source="request-metadata:http-session-id"/>
```

having identified your session, you can store and retrieve parameters from the session context as you would any other context, eg:

```xml 
<rg:create-parameter name="session:stored-value" value="store this"/> 

<rg:create-parameter name="local-parameter" source="session:stored-value"/>
```

## core constructs in xml

### number-generator

a number-generator generates a random number parameter value.

```xml
<rg:generate-parameter name="param-name" type="NUMBER">
	<rg:number-generator max="1000"/>
</rg:generate-parameter>
```

the ``max`` attribute is optional:

```xml
<rg:generate-parameter name="param-name" type="NUMBER" generator="number-generator"/>
```

more info on ``value-generator`` [here](https://github.com/talmeym/regurgitator-core-xml#generate-parameter).

### uuid-generator

a uuid-generator generates a uuid parameter value.

```xml
<rg:generate-parameter name="new-id" type="STRING">
	<rg:uuid-generator/>
</rg:generate-parameter>

<rg:generate-parameter name="new-id" type="STRING" generator="uuid-generator"/>
```

more info on ``value-generator`` [here](https://github.com/talmeym/regurgitator-core-xml#generate-parameter).

### extract-processor

an extract-processor extracts a value from another value, using the [java.text.MessageFormat](https://docs.oracle.com/javase/7/docs/api/java/text/MessageFormat.html) syntax.

```xml
<rg:create-parameter name="customer-id" source="request-metadata:query-string">
	<rg:extract-processor format="order={0}&amp;customer={1}" index="1"/>
</rg:create-parameter>
```

more info on ``value-processor`` [here](https://github.com/talmeym/regurgitator-core-xml#value-processors).

### substitute-processor

a substitute-processor manipulates ``STRING`` values, replacing occurrences of one value with another.

```xml
<rg:create-parameter name="positive-spin" value="you are unhappy">
	<rg:substitute-processor token="un" replacement="very "/>
</rg:create-parameter>
```

more info on ``value-processor`` [here](https://github.com/talmeym/regurgitator-core-xml#value-processors).

### index-processor

an index-processor manipulates collection parameter values, such as ``LIST_OF_STRING``, returning the data value at a given index, starting from zero.

```xml
<rg:create-parameter name="data" type="LIST_OF_STRING" value="not this one,or this one,but this one"/>

<rg:create-parameter name="the-one" type="STRING" source="data">
	<rg:index-processor value="2"/>
</rg:create-parameter>
```

the index to be looked up can be specified using either a ``value`` attribute, or ``source`` to retrieve the index from a parameter.

more info on ``value-processor`` [here](https://github.com/talmeym/regurgitator-core-xml#value-processors).

### index-of-processor

an index-of-processor manipulates collection parameter values, such as ``LIST_OF_STRING``, returning the index of a given data value, starting from zero.

```xml
<rg:create-parameter name="data" type="LIST_OF_STRING" value="not this one,or this one,but this one"/>

<rg:create-parameter name="index-of-the-one" type="NUMBER" source="data">
	<rg:index-of-processor value="but this one"/>
</rg:create-parameter>
```

the data value to be looked up can be specified using either a ``value`` attribute, or ``source`` to retrieve the data value from a parameter.

more info on ``value-processor`` [here](https://github.com/talmeym/regurgitator-core-xml#value-processors).

### size-processor

a size-processor manipulates collection parameter values, such as ``LIST_OF_STRING``, returning the size of the collection

```xml
<rg:create-parameter name="data" type="LIST_OF_STRING" source="one,two,three,four"/>

<rg:create-parameter name="data-size" type="NUMBER" source="data" processor="size-processor"/>
```

the optional ``as-index`` attribute returns the size zero-indexed, eg. 3 items returning a value of 2. without this attribute, the processor can be in-lined.

```xml
<rg:create-parameter name="data" type="LIST_OF_STRING" value="one,two,three,four"/>

<rg:create-parameter name="four" type="STRING" source="data">
	<rg:size-processor as-index="true"/>
</rg:create-parameter>
```

more info on ``value-processor`` [here](https://github.com/talmeym/regurgitator-core-xml#value-processors).
