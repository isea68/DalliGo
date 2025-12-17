package com.human.dalligo.vo.events;

import java.sql.Timestamp;

public class LshTripVO {
private int tripId;
private int eventId;
private String userId;
private String startCity;
private String endCity;
private java.math.BigDecimal distance;
private int cost;
private int currentPeople;
private Timestamp tripDate;
private java.time.LocalTime tripTime;
private String status;
private Timestamp createdAt;


public int getTripId() { return tripId; }
public void setTripId(int tripId) { this.tripId = tripId; }


public int getEventId() { return eventId; }
public void setEventId(int eventId) { this.eventId = eventId; }


public String getUserId() { return userId; }
public void setUserId(String userId) { this.userId = userId; }


public String getStartCity() { return startCity; }
public void setStartCity(String startCity) { this.startCity = startCity; }


public String getEndCity() { return endCity; }
public void setEndCity(String endCity) { this.endCity = endCity; }


public java.math.BigDecimal getDistance() { return distance; }
public void setDistance(java.math.BigDecimal distance) { this.distance = distance; }


public int getCost() { return cost; }
public void setCost(int cost) { this.cost = cost; }


public int getCurrentPeople() { return currentPeople; }
public void setCurrentPeople(int currentPeople) { this.currentPeople = currentPeople; }


public Timestamp getTripDate() { return tripDate; }
public void setTripDate(Timestamp tripDate) { this.tripDate = tripDate; }


public java.time.LocalTime getTripTime() { return tripTime; }
public void setTripTime(java.time.LocalTime tripTime) { this.tripTime = tripTime; }


public String getStatus() { return status; }
public void setStatus(String status) { this.status = status; }


public Timestamp getCreatedAt() { return createdAt; }
public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}