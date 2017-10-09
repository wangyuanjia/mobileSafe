package com.yuanjia.mobilesafe.domain;

public class ScanInfo {

	public String name;
	public String packname;
	public boolean virus;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public boolean isVirus() {
		return virus;
	}
	public void setVirus(boolean virus) {
		this.virus = virus;
	}
	
}
