
package com.oracle.cep.training.adapter;

import com.bea.wlevs.ede.api.StreamSender;
import com.bea.wlevs.ede.api.StreamSource;
import com.bea.wlevs.ede.api.RunnableBean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ConnectException;
import java.lang.ArrayIndexOutOfBoundsException;

//******* IMPORT YOUR EVENT TYPE ***************//
import com.oracle.cep.event.TemperatureEvent;

public class TemperatureSocketAdapter implements RunnableBean, StreamSource {

	private StreamSender eventSender;

	private int port;

	private String server;

	private Socket socket;
	
	private boolean suspended = false;

	public void setEventSender(StreamSender sender) {
		this.eventSender = sender;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public void run() {
		suspended = false;
		
		while (!isSuspended()){
		try {
			this.socket = new Socket(server, port);
			BufferedReader networkIn = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			System.out.println("Connected to server:  " + socket);

			while (!isSuspended()) { // Generate messages forever...
				String line = networkIn.readLine();
				if (line != null && line.length() > 0) {
					String[] fields = line.split(",");
					
					
					//***** CREATE YOUR EVENT ***********//
					TemperatureEvent event = new TemperatureEvent();
					try {
						event.setSubstationId(fields[0]);
						event.setTemperature(Double.parseDouble(fields[1]));
						//System.out.println("TEMPERATURE EVENT CREATED: " + event.getSubstationId() + " temp: " + event.getTemperature());
					}
					catch (ArrayIndexOutOfBoundsException ae){					
						//ae.printStackTrace();
						System.out.println("Input file was not read as expected!");						
					}
					
					//****** SEND THE EVENT TO LISTENERS **************//
					eventSender.sendInsertEvent(event);								
				}
			}
						
		} catch (ConnectException ce) {
			
			try {Thread.sleep(1000);} catch (InterruptedException e1) {}
					
		} catch (Exception e) {
						
			if (!isSuspended()){
				e.printStackTrace();
				try {Thread.sleep(5000);} catch (InterruptedException e1) {}
			}
		}
		}
	}

	public synchronized void suspend() throws Exception {
		suspended = true;
		if (socket != null) {
			socket.close();
		}
	}
	

    private synchronized boolean isSuspended() {
        return suspended;
    }
	

}
