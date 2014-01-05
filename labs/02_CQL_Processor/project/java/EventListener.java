package com.oracle.oep.training.listeners;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bea.wlevs.ede.api.EventRejectedException;
import com.bea.wlevs.ede.api.StreamSink;

import com.oracle.oep.demo.jaxb.event.MeterStateEvent;
import com.oracle.oep.event.FlickerEvent; 

public class EventListener implements StreamSink {

	Log log_ = LogFactory.getLog(EventListener.class);
	
	public void onInsertEvent(Object event) throws EventRejectedException {
		log_.debug("onInsertEvent():" + event);
		
		if(event instanceof MeterStateEvent) {
			MeterStateEvent theEvent = (MeterStateEvent) event;
			String id = theEvent.getMeterId();
			String state = theEvent.getState();
			String transformer = theEvent.getTransformerId();
			String cust = theEvent.getCustomerType();
			//log_.info("EventListener: Received 'MeterStateEvent' for meter id: " + id + " state:" + state + "\n");
			System.out.println("EventListener: Received 'MeterStateEvent' for meter id: " + id + " state: " + state + " transformerId: " + transformer + " custType: " + cust + "\n");
		}
		
		if(event instanceof FlickerEvent) {
			FlickerEvent theEvent = (FlickerEvent) event;
			System.out.println(theEvent.toString());
		}
		
		
		
	}

}