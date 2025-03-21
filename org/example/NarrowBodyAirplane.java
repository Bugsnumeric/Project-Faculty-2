package org.example;

import java.util.LinkedList;

public class NarrowBodyAirplane extends Airplane {
    protected static LinkedList<NarrowBodyAirplane> narrow_airplanes = new LinkedList<>();

    protected NarrowBodyAirplane(BuildAirplane airplane) {
        super(airplane);
    }

    public String toString() {
        return "Narrow Body - " + super.toString();
    }

    protected String getStatus() {
        return super.getStatus();
    }

    protected void setStatus(Status status) {
        super.setStatus(status);
    }

    protected void setTime_maneuver(String time_maneuver) {
        super.setTime_maneuver(time_maneuver);
    }

    protected String getTime_maneuver() {
        return super.getTime_maneuver();
    }
}