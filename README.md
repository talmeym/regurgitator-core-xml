regurgitator-core-xml
=====================

regurgitator is a modular, light-weight, extendable java-based processing framework designed to 'regurgitate' canned or clever responses to incoming requests.

See more at [regurgitator-core](http://github.com/talmeym/regurgitator-core)

xml configuration of regurgitator core functionality
----------------------------------------------------

an xml configuration file provides an easy way to configure regurgitator

```xml
<?xml version="1.0" encoding="UTF-8"?>
<rg:regurgitator-configuration 
            	   xmlns:rg="http://core.regurgitator.emarte.com"
							   xmlns:rgcct="http://stuff.test.regurgitator.emarte.com"
							   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
							   xsi:schemaLocation="http://core.regurgitator.emarte.com regurgitatorCore.xsd                                                           http://stuff.test.regurgitator.emarte.com test.xsd">
	<rg:decision id="decision-1">
		<rg:steps>
			<rgcct:test-step id="test-step-1"/>
		</rg:steps>
		<rg:rules>
			<rg:rule id="rule-1" step="test-step-1">
				<rg:condition id="condition-1" source="context:location" contains="value"/>
			</rg:rule>
		</rg:rules>
	</rg:decision>
</rg:regurgitator-configuration>
```

all regurgitator xml files are schema validated on document load. two main schemas are provided for xml configuration, ``regurgitatorCommon.xsd`` and ``regurgitatorCore.xsd``. the former provides basic definitions of types used in all regurgitator schemas (incuding yours if you extend the system) and the latter provides definitions for all the core regurgitator steps.

