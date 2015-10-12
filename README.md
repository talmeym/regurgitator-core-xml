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
		   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		   xsi:schemaLocation="http://core.regurgitator.emarte.com regurgitatorCore.xsd">
	<rg:decision id="decision-1">
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

```java
import com.emarte.regurgitator.core.*;

public class MyClass {
	private Regurgitator regurgitator;

	public MyClass() {
		Step rootStep = ConfigurationFile.loadFile("classpath:/my_configuration.xml");
		regurgitator = new Regurgitator("my-regurgitator", rootStep);
	}
}
```

all regurgitator xml files are schema validated on document load. two main schemas are provided for xml configuration, ``regurgitatorCommon.xsd`` and ``regurgitatorCore.xsd``. the former provides basic definitions of types used in all regurgitator schemas (incuding yours if you extend the system) and the latter provides definitions for all the core regurgitator steps.

