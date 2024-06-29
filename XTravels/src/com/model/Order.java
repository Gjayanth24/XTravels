package com.model;

public class Order {
	private int orderId;
	private double orderAmount;
	private String orderStatus;
	private Journey requestedJourneyPlan;
	private Route route;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Journey getRequestedJourneyPlan() {
		return requestedJourneyPlan;
	}

	public void setRequestedJourneyPlan(Journey requestedJourneyPlan) {
		this.requestedJourneyPlan = requestedJourneyPlan;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", orderAmount=" + orderAmount + ", orderStatus=" + orderStatus
				+ ", requestedJourneyPlan=" + requestedJourneyPlan + ", route=" + route + "]";
	}
}
