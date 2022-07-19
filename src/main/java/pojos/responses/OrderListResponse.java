package pojos.responses;

import pojos.OrderInfo;
import pojos.PageInfo;
import pojos.Station;

import java.util.ArrayList;

public class OrderListResponse {
    private ArrayList<OrderInfo> orders;
    private PageInfo pageInfo;
    private ArrayList<Station> availableStations;

    public ArrayList<OrderInfo> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<OrderInfo> orders) {
        this.orders = orders;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public ArrayList<Station> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(ArrayList<Station> availableStations) {
        this.availableStations = availableStations;
    }
}
