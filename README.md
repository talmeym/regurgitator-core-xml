regurgitator
============

regurgitator is a modular, light-weight, extendable java-based processing framework designed to 'regurgitate' canned or clever responses to incoming requests.

See more at [regurgitator-core](http://github.com/talmeym/regurgitator-core)

regurgitator-core-xml
=====================

regurgitator-core functionality + xml configuration
---------------------------------------------------

regurgitator xml configuration is schema validated on document load. two main schemas are provided for xml configuration, ``regurgitatorCommon.xsd`` and ``regurgitatorCore.xsd``. the former provides basic definitions of types used in all regurgitator schemas (incuding yours if you extend the system) and the latter provides definitions for all the core regurgitator steps. 

