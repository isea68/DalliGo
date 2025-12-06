package com.human.dalligo.vo;

import java.time.LocalDate;

public class LshEventVO {
private int id;
private String title;
private String location;
private java.time.LocalDateTime startDate;
private java.time.LocalDateTime endDate;
private String distance;
private java.math.BigDecimal price;
private String org;
private String regUrl;
private String description;
private java.time.LocalDateTime inDate;
private int total;


public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public String getLocation() {
	return location;
}

public void setLocation(String location) {
	this.location = location;
}

public java.time.LocalDateTime getStartDate() {
	return startDate;
}

public void setStartDate(java.time.LocalDateTime startDate) {
	this.startDate = startDate;
}

public java.time.LocalDateTime getEndDate() {
	return endDate;
}

public void setEndDate(java.time.LocalDateTime endDate) {
	this.endDate = endDate;
}

public String getDistance() {
	return distance;
}

public void setDistance(String distance) {
	this.distance = distance;
}

public java.math.BigDecimal getPrice() {
	return price;
}

public void setPrice(java.math.BigDecimal price) {
	this.price = price;
}

public String getOrg() {
	return org;
}

public void setOrg(String org) {
	this.org = org;
}

public String getRegUrl() {
	return regUrl;
}

public void setRegUrl(String regUrl) {
	this.regUrl = regUrl;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public java.time.LocalDateTime getInDate() {
	return inDate;
}

public void setInDate(java.time.LocalDateTime inDate) {
	this.inDate = inDate;
}

public int getTotal() {
	return total;
}

public void setTotal(int total) {
	this.total = total;
}

public LocalDate getStartDateOnly() {
    return (startDate != null) ? startDate.toLocalDate() : null;
}

}