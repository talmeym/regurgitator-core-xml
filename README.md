## regurgitator-core-xml

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

### sequence

a sequence is a step that executes a list of other steps, one after another in order

```xml
<rg:sequence id="my-sequence">
	<rg:create-parameter id="my-step-1" name="response" value="text/plain"/>
	<rg:create-response id="my-step-2" source="response"/>
</rg:sequence>
```

when the sequence executes each of it's steps, the message passed to the sequence is passed to the child steps. a sequence can isolate it's child steps from some of the data in the message, to avoid the steps interpretting data they shouldn't. 

```xml
<rg:sequence id="my-sequence" isolate="with=parameter">
	<rg:create-parameter id="my-step-1" name="response" value="text/plain"/>
	<rg:create-response id="my-step-2" source="response"/>
</rg:sequence>
```

isolation has 4 settings:

| value | meaning |
| :--- | :--- |
| false | isolation off |
| with-parameters | pass only the parameters context through in the execution message of the held steps |
| with-session | pass only the session context through in the execution message of the held steps |
| with-parameters-and-session | pass both the parameters and session contexts through in the execution message of the held steps |
