package com.oracle.oep.training.converter;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Marshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import com.bea.wlevs.adapters.jms.api.InboundMessageConverter;
import com.bea.wlevs.adapters.jms.api.MessageConverterException;

import com.oracle.oep.demo.jaxb.event.*;


public class InboundJmsJaxbMessageConverter implements InboundMessageConverter, InitializingBean {

	Log log_ = LogFactory.getLog(InboundJmsJaxbMessageConverter.class);
	private JAXBContext jaxbContext_;
	private Unmarshaller unmarshaller_;
	private Marshaller marshaller_;
	
	
	InboundJmsJaxbMessageConverter(){
		initJaxb();
	}
	
	@SuppressWarnings("rawtypes")
	public List convert(Message message) throws MessageConverterException {
		
		//System.out.println("ADAPTER: RECEIVED MESSAGE!" );
		
		List result = null;
		if (message instanceof TextMessage) {
			result = handleMessage((TextMessage) message);
		} else {
			log_.warn("onMessage():skipping unexpected message type:" + message.toString());
			result = Collections.EMPTY_LIST;
		}
		return result;
	}

	/**
	 * For now we assume that this is XML and we need to unmarshall into a JAXB
	 * class
	 * @param message
	 */
	@SuppressWarnings("rawtypes")
	private List handleMessage(TextMessage message) throws MessageConverterException {
		try {
						
			Object eventPoJo = unmarshaller_.unmarshal(new StringReader(message.getText()));
			marshaller_.marshal( eventPoJo, System.out );
			System.out.println();
						
			List<Object> result = new ArrayList<Object>();
			
			if (eventPoJo instanceof JAXBElement) {
			
				Object element = ((JAXBElement) eventPoJo).getValue();
				if(element instanceof MeterStateEvent) {
					MeterStateEvent theEvent = (MeterStateEvent) element;
					//theEvent.getMeter().getMeterId();
					//log_.info("ADAPTER: INCOMING METER STATE EVENT: " + theEvent.getMeter().getMeterId() + "\n");
					result.add(theEvent);
				}
        	}			
			
			return result;
		} catch(JMSException jmse) {
			throw new MessageConverterException("Error getting JMS message content",jmse);
		}
		catch (JAXBException jaxbException) {
			throw new MessageConverterException("JAXB unmarshalling failure:", jaxbException);
		}
	}

	private void initJaxb() {
		try {
			jaxbContext_ = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
			unmarshaller_ = jaxbContext_.createUnmarshaller();
			marshaller_ = jaxbContext_.createMarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException("Could not init JAXB :" + e.getMessage(), e);
		}
		if(log_.isInfoEnabled()) {
			log_.info("Initialized JAXB");
		}
	}
	
	/**
	 * Spring callback
	 */
	public void afterPropertiesSet() throws Exception {

	}

	
}
