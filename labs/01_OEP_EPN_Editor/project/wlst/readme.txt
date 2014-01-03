
There is 1 queues and 1 connection factory are created:

JMS SERVER
==========
SmartMeterJMSServer

JMS MODULE
===========
SmartMeterJMSServer

CONNECTION FACTORY
==================
jms/SmartMeterConnectionFactory

QUEUES
======
jms/MeterStateEvents


The configJms.cmd script can be run to create JMS queues.  
The WLS server must be running. 
The admin username/password/url and server name can be configured via the ServerEnv.properties file.