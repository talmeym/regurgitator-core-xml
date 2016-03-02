# regurgitator-core-xml

regurgitator is a modular, light-weight, extendable java-based processing framework designed to 'regurgitate' canned or clever responses to incoming requests.

See more at [regurgitator-all](http://github.com/talmeym/regurgitator-all)

## xml configuration of regurgitator core functionality

below is an example of an xml configuration file for regurgitator:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<rg:regurgitator-configuration xmlns:rg="http://core.regurgitator.emarte.com" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://core.regurgitator.emarte.com regurgitatorCore.xsd">
	<rg:decision id="check-greeting">
		<rg:steps>
			<rg:create-response id="before-lunch" value="it is before lunch"/>
			<rg:create-response id="after-lunch" value="it is after lunch"/>
		</rg:steps>
		<rg:rules defaultStep="after-lunch">
			<rg:rule step="before-lunch">
				<rg:condition source="greeting" behaviour="equals" value="good morning"/>
			</rg:rule>
		</rg:rules>
	</rg:decision>
</rg:regurgitator-configuration>
```

all regurgitator xml files are schema validated on document load. two main schemas are provided for xml configuration, [``regurgitatorCommon.xsd``](https://github.com/talmeym/regurgitator-core-xml/blob/master/src/main/resources/regurgitatorCommon.xsd) and [``regurgitatorCore.xsd``](https://github.com/talmeym/regurgitator-core-xml/blob/master/src/main/resources/regurgitatorCore.xsd). the former provides basic definitions of types used in all regurgitator schemas (incuding yours if you extend the system) and the latter provides definitions for all the core regurgitator steps.

## steps

### sequence

a sequence is a step that executes a series of child steps, one after another in order

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

| value | meaning |
| :--- | :--- |
| true | child steps given new blank execution message |
| with-parameters | child steps given new execution message with only the parameters context of the original |
| with-session | child steps given new execution message with only the session context of the original |
| with-parameters-and-session | child steps given new execution message with the parameters and session context of the original |

### decision

a decision is a collection of steps where ``rules`` and ``conditions`` dictate which steps are run

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

a decision step, when executed, first evaluates all of its rules to see which pass, then determines which of the passed rules should have their corresponding steps executed, using its ``rules behaviour``. the default decision rules behaviour is ``FIRST_MATCH`` whereby the first rule that passes dictates the step to be executed.

a rule has one or more conditions to be satisfied to make the rule pass. each condition evaluates the value of a parameter within the execution message, specified by the ``source`` attribute. each condition has a ``condition behaviour`` that dictates the manner in which the parameter's value is evaluated against the operand. the example above uses the ``equals`` condition behaviour.

the behaviour of a condition can also be specified as a child element of the parent condition, as below:

```xml
	...
	<rg:rules default-step="default-step">
		<rg:rule step="special-step">
			<rg:condition source="parameters:special">
				<rg:equals>true</rg:equals>
			</rg:condition>
		</rg:rule>
	</rg:rules>
	...
```

this allows custom condition behaviours to have attributes besides the behaviour value (in the example above, "true") which is always the text of the child element.

there are 3 core condition behaviours:

| behaviour | meaning |
| :--- | :--- |
| equals | checks the parameter value equals the operand |
| exists | checks the parameter value exists (read as 'parameter exists') |
| contains | checks the parameter value contains the operand |
