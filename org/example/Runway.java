package org.example;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;

public class Runway {
    private LocalTime time_alloc;
    private String ID_runway;
    private String type_runway;
    private String type_airplane_runway;

    protected static ArrayList<Runway> runways_land = new ArrayList<>();
    protected static ArrayList<Runway> runways_takeoff = new ArrayList<>();

    public Runway(LocalTime time_alloc, String ID_runway, String type_runway, String type_airplane_runway) {
        this.time_alloc = time_alloc;
        this.ID_runway = ID_runway;
        this.type_runway = type_runway;
        this.type_airplane_runway = type_airplane_runway;
    }

    protected String getID_runway() {
        return ID_runway;
    }

    protected static boolean check_ID_runway(ArrayList<? extends Runway> list, String ID_runway_find) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getID_runway().equals(ID_runway_find)) {
                return true;
            }
        }
        return false;
    }
}

//////////////////// IncorrectRunwayException using generics
class IncorrectRunwayException extends Exception {
    public IncorrectRunwayException(String message) {
        super(message);
    }

    protected static void verification_runway(LinkedList<? extends Airplane> list_airplane,
                                              ArrayList<? extends Runway> list_runway) throws IncorrectRunwayException {
        String ID_runway_Airplane = list_airplane.getLast().getID_runway();

        for (int i = 0; i < list_runway.size(); i++) {
            if (list_runway.get(i).getID_runway().equals(ID_runway_Airplane)) {
                list_airplane.removeLast();
                throw new IncorrectRunwayException("The chosen runway for allocating the plane is incorrect");
            }
        }
    }
}

///////////////////// UnavailableRunwayException using generics
class UnavailableRunwayException extends Exception {
    public UnavailableRunwayException(String message) {
        super(message);
    }

    public static void verification_maneuver(LocalTime t1, LocalTime t2, boolean ID_finder) throws UnavailableRunwayException {
        Duration duration = Duration.between(t1, t2).abs();
        ////////takeoff rule
        if (ID_finder) {
            if (duration.toMinutes() <= 5) {
                throw new UnavailableRunwayException("The chosen runway for maneuver is currently occupied");
            }
        } else {////////landed rule
            if (duration.toMinutes() <= 10) {
                throw new UnavailableRunwayException("The chosen runway for maneuver is currently occupied");
            }
        }
    }
}