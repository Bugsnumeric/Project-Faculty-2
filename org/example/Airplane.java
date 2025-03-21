package org.example;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class Airplane implements Comparable<Airplane>{
    private LocalTime time_alloc;
    private String type;
    private String model;
    private String ID_plane;
    private String location_src;
    private String location_dest;
    private LocalTime desired_time;
    private String ID_runway;
    private Status status;
    private String urgent;
    private String time_maneuver;

    protected static LinkedList<Airplane> airplanes = new LinkedList<>();

    public enum Status {
        WAITING_FOR_TAKEOFF,
        DEPARTED,
        WAITING_FOR_LANDING,
        LANDED
    }

    public String toString() {
        return model + " - " + ID_plane + " - " + location_src + " - " + location_dest +
                " - " + status + " - " + desired_time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public Airplane() {}

    protected Airplane(BuildAirplane airplane) {
        this.time_alloc = airplane.time_alloc;
        this.type = airplane.type;
        this.model = airplane.model;
        this.ID_plane = airplane.ID_plane;
        this.location_src = airplane.location_src;
        this.location_dest = airplane.location_dest;
        this.desired_time = airplane.desired_time;
        this.ID_runway = airplane.ID_runway;
        this.status = airplane.status;
        this.urgent = airplane.urgent;
    }

    public int compareTo(Airplane o) {
        if (!this.urgent.equals("urgent") && o.getUrgent().equals("urgent")) {
            return 1;
        } else if (this.urgent.equals("urgent") && !o.getUrgent().equals("urgent")) {
            return -1;
        }
        return this.desired_time.compareTo(o.getDesired_time());
    }

    public String getID_runway() {
        return ID_runway;
    }

    public String getID_plane() {
        return ID_plane;
    }

    protected String getStatus() {
        return status.toString();
    }

    public String getUrgent() {
        return urgent;
    }

    public LocalTime getDesired_time() {
        return desired_time;
    }

    protected void setStatus(Status status) {
        this.status = status;
    }

    protected void setTime_maneuver(String time_maneuver) {
        this.time_maneuver = time_maneuver;
    }

    protected String getTime_maneuver() {
        return time_maneuver;
    }

    public static boolean check_ID_airplane(LinkedList<? extends Airplane> list, String ID_plane) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getID_plane().equals(ID_plane)) {
                Main.aux_airplane = i;
                return true;
            }
        }
        return false;
    }

    public static void status_setter(LinkedList<? extends Airplane> list, boolean is_takeoff, String ID_maneuver, String time) {
        if (is_takeoff) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getStatus().equals("WAITING_FOR_TAKEOFF") && list.get(i).getID_runway().equals(ID_maneuver)) {
                    list.get(i).setStatus(Status.DEPARTED);
                    list.get(i).setTime_maneuver(time);
                    break;
                }
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getStatus().equals("WAITING_FOR_LANDING") && list.get(i).getID_runway().equals(ID_maneuver)) {
                    list.get(i).setStatus(Status.LANDED);
                    list.get(i).setTime_maneuver(time);
                    break;
                }
            }
        }
    }

    public static class BuildAirplane {
        private LocalTime time_alloc;
        private String type;
        private String model;
        private String ID_plane;
        private String location_src;
        private String location_dest;
        private LocalTime desired_time;
        private String ID_runway;
        private Status status;

        private String urgent = "not_urgent";

        protected BuildAirplane(LocalTime time_alloc, String type, String model, String ID_plane, String location_src, String location_dest,
                                LocalTime desired_time, String ID_runway) {
            this.time_alloc = time_alloc;
            this.type = type;
            this.model = model;
            this.ID_plane = ID_plane;
            this.location_src = location_src;
            this.location_dest = location_dest;
            this.desired_time = desired_time;
            this.ID_runway = ID_runway;
            if (location_src.equals("Bucharest")) {
                this.status = Status.WAITING_FOR_TAKEOFF;
            } else {
                this.status = Status.WAITING_FOR_LANDING;
            }
        }

        public BuildAirplane setUrgent(String urgent) {
            this.urgent = urgent;
            return this;
        }

        protected Airplane build() {
            return new Airplane(this);
        }
    }
}