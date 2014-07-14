package com.oracle.cep.event;

public class TemperatureEvent {

	private String substationId ;
	private double temperature ;
	
	public String getSubstationId() {
		return substationId;
	}
	public void setSubstationId(String substationId) {
		this.substationId = substationId;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	
	
	
	
}
