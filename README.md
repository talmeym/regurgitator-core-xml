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
| ``FIRST_MATCH_ONWARDS`` | execute the step of the first rule passed, and all seqsequent steps |
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
