loadProperties('ServerEnv.properties')
connect(_connectUser,_connectPassword,_connectUrl)
edit()
servermb=getMBean("Servers/"+_serverName)
if servermb is None:
	print '@@@ No server MBean found'
else:
	print "@@@ Starting Script..."
	startEdit()
	print "@@@ Creating JMS Server..."
	jmsServer = create('SmartMeterJMSServer','JMSServer')
	jmsServer.addTarget(servermb)
	jmsMySystemResource = create("SmartMeterJMSModule","JMSSystemResource")
	jmsMySystemResource.addTarget(servermb)
	subDeployment = jmsMySystemResource.createSubDeployment('SmartMeterSubdeployment')
	subDeployment.addTarget(jmsServer)    
	theJMSResource = jmsMySystemResource.getJMSResource()

	print "@@@ Creating Connection Factory.."
	connfact1 = theJMSResource.createConnectionFactory('SmartMeterConnectionFactory')
	connfact1.setJNDIName('jms/SmartMeterConnectionFactory')
	connfact1.setSubDeploymentName('SmartMeterSubdeployment')

	print "@@@ Creating Queue..."
	jmsqueue1 = theJMSResource.createQueue('MeterStateEvents')
	jmsqueue1.setJNDIName('jms/MeterStateEvents')
	jmsqueue1.setSubDeploymentName('SmartMeterSubdeployment')
	
	print '@@@ Resources created.  Saving changes ...'
	save()
	activate(block="true")
	print '@@@ Changes activated.'
	
disconnect()
