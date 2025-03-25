package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Clasa reprezentująca węzeł grafu CPM
 */
public class Node {
    private String name;
    private Double activityDuration;
    List<Node> children;

    private Double reserve;
    private Double earlyStartTime;
    private Double earlyEndTime;
    private Double lateStartTime;
    private Double lateEndTime;

    public Node(String name, Double activityDuration) {
        this.name = name;
        this.activityDuration = activityDuration;
        this.children = new ArrayList<Node>();

        this.reserve = (double) 0;
        this.earlyStartTime = (double) 0;
        this.earlyEndTime = (double) 0;
        this.lateStartTime = (double) 0;
        this.lateEndTime = (double) 0;

    }
    public void addChild(Node node) {
        children.add(node);
    }
    public void removeChild(Node node) {
        children.remove(node);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getActivityDuration() {
        return activityDuration;
    }
    public void setActivityDuration(Double activityDuration) {
        this.activityDuration = activityDuration;
    }
    public Double getReserve() {
        return reserve;
    }
    public void setReserve(Double reserve) {
        this.reserve = reserve;
    }
    public Double getEarlyStartTime() {
        return earlyStartTime;
    }
    public void setEarlyStartTime(Double earlyStartTime) {
        this.earlyStartTime = earlyStartTime;
    }
    public Double getEarlyEndTime() {
        return earlyEndTime;
    }
    public void setEarlyEndTime(Double earlyEndTime) {
        this.earlyEndTime = earlyEndTime;
    }
    public Double getLateStartTime() {
        return lateStartTime;
    }
    public void setLateStartTime(Double lateStartTime) {
        this.lateStartTime = lateStartTime;
    }
    public Double getLateEndTime() {
        return lateEndTime;
    }
    public void setLateEndTime(Double lateEndTime) {
        this.lateEndTime = lateEndTime;
    }
}
