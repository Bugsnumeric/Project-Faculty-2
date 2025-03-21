package org.example;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;

public class Main {
    private static final String wide = "wide body";
    private static final String narrow = "narrow body";
    private static final String takeoff = "takeoff";
    private static final String add_runway = "add_runway_in_use";
    private static final String allocate = "allocate_plane";
    private static final String flight_info = "flight_info";
    private static final String runway_info = "runway_info";
    private static final String perms_maneuver = "permission_for_maneuver";
    private static final String exit = "exit";
    private static final String antetResources = "src/main/resources/";
    private static final String input_file = "/input.in";
    private static final String output_file_flight = "/flight_info.out";
    private static final String exceptions_file = "/board_exceptions.out";
    private static LocalTime time_maneuver_takeoff = LocalTime.of(0,0,0);
    private static LocalTime time_maneuver_land = LocalTime.of(0,0,0);
    protected static int aux_airplane = 0;

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            System.out.println(args[0]);
            String name_test = args[0];

            FileReader fr = new FileReader(antetResources + name_test + input_file);
            //flight_info
            FileWriter f_i = new FileWriter(antetResources + name_test + output_file_flight);
            PrintWriter p_fi = new PrintWriter(f_i);
            //board_exceptions
            FileWriter b_e = new FileWriter(antetResources + name_test + exceptions_file);
            PrintWriter p_be = new PrintWriter(b_e);

            try(BufferedReader br = new BufferedReader(fr)) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(" - ");
                    String command = parts[1];

                    switch (command) {
                        //////////////////////////add_runway_in_use
                        case add_runway:
                            LocalTime time_alloc = LocalTime.parse(parts[0]);
                            String ID_runway = parts[2];
                            String type_runway = parts[3];
                            String type_airplane_runway = parts[4];

                            if (type_runway.equals(takeoff)) {
                                Runway.runways_takeoff.add(new Runway(time_alloc, ID_runway, type_runway, type_airplane_runway));
                            } else {
                                Runway.runways_land.add(new Runway(time_alloc, ID_runway, type_runway, type_airplane_runway));
                            }

                            break;

                        ////////////////////////allocate plane
                        case allocate:
                            LocalTime time_allocate = LocalTime.parse(parts[0]);
                            String type_airplane = parts[2];
                            String model = parts[3];
                            String ID_plane = parts[4];
                            String location_src = parts[5];
                            String location_dest = parts[6];
                            LocalTime desired_time = LocalTime.parse(parts[7]);
                            String ID_runway_plane = parts[8];

                            try {
                                if (line.contains("urgent") && location_dest.equals("Bucharest")) {
                                    //////Builder design pattern for Airplanes
                                    Airplane.airplanes.add(new Airplane.BuildAirplane(time_allocate, type_airplane, model, ID_plane,
                                            location_src, location_dest, desired_time, ID_runway_plane)
                                            .setUrgent("urgent")
                                            .build());
                                } else {
                                    Airplane.airplanes.add(new Airplane.BuildAirplane(time_allocate, type_airplane, model, ID_plane,
                                            location_src, location_dest, desired_time, ID_runway_plane)
                                            .build());
                                }

                                if (location_src.equals("Bucharest")) {
                                    IncorrectRunwayException.verification_runway(Airplane.airplanes, Runway.runways_land);
                                } else {
                                    IncorrectRunwayException.verification_runway(Airplane.airplanes, Runway.runways_takeoff);
                                }
                            } catch (IncorrectRunwayException e) {
                                p_be.println(time_allocate.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " | " + e.getMessage());
                                break;
                            }

                            if (type_airplane.equals(wide) && line.contains("urgent") && location_dest.equals("Bucharest")) {
                                WideBodyAirplane.wide_airplanes.add(new WideBodyAirplane(new Airplane.BuildAirplane(
                                        time_allocate, type_airplane, model, ID_plane, location_src, location_dest, desired_time, ID_runway_plane
                                ).setUrgent("urgent")));
                                break;
                            } else if (type_airplane.equals(narrow) && line.contains("urgent") && location_dest.equals("Bucharest")) {
                                NarrowBodyAirplane.narrow_airplanes.add(new NarrowBodyAirplane(new Airplane.BuildAirplane(
                                        time_allocate, type_airplane, model, ID_plane, location_src, location_dest, desired_time, ID_runway_plane
                                ).setUrgent("urgent")));
                                break;
                            }

                            if (type_airplane.equals(wide)) {
                                WideBodyAirplane.wide_airplanes.add(new WideBodyAirplane(new Airplane.BuildAirplane(
                                        time_allocate, type_airplane, model, ID_plane, location_src, location_dest, desired_time, ID_runway_plane
                                )));
                            } else {
                                NarrowBodyAirplane.narrow_airplanes.add(new NarrowBodyAirplane(new Airplane.BuildAirplane(
                                        time_allocate, type_airplane, model, ID_plane, location_src, location_dest, desired_time, ID_runway_plane
                                )));
                            }

                            break;

                        ///////////////////////////////flight info
                        case flight_info:
                            LocalTime time_info = LocalTime.parse(parts[0]);
                            String ID_flightInfo = parts[2];
                            boolean checkID_wide = Airplane.check_ID_airplane(WideBodyAirplane.wide_airplanes, ID_flightInfo);
                            boolean checkID_narrow = Airplane.check_ID_airplane(NarrowBodyAirplane.narrow_airplanes, ID_flightInfo);

                            if (checkID_wide) {
                                int i = aux_airplane;

                                if (WideBodyAirplane.wide_airplanes.get(i).getStatus().equals("DEPARTED") ||
                                        WideBodyAirplane.wide_airplanes.get(i).getStatus().equals("LANDED")) {
                                    p_fi.println(time_info.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " | " +
                                            WideBodyAirplane.wide_airplanes.get(i).toString() + " - " +
                                            WideBodyAirplane.wide_airplanes.get(i).getTime_maneuver());
                                    break;
                                }

                                p_fi.println(time_info.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " | " +
                                        WideBodyAirplane.wide_airplanes.get(i).toString());
                            }

                            if (checkID_narrow) {
                                int i = aux_airplane;

                                if (NarrowBodyAirplane.narrow_airplanes.get(i).getStatus().equals("DEPARTED") ||
                                        NarrowBodyAirplane.narrow_airplanes.get(i).getStatus().equals("LANDED")) {
                                    p_fi.println(time_info.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " | " +
                                            NarrowBodyAirplane.narrow_airplanes.get(i).toString() + " - " +
                                            NarrowBodyAirplane.narrow_airplanes.get(i).getTime_maneuver());
                                    break;
                                }

                                p_fi.println(time_info.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " | " +
                                        NarrowBodyAirplane.narrow_airplanes.get(i).toString());
                            }

                            break;

                        ///////////////////////////////runway info
                        case runway_info:
                            String time_runway_info = parts[0].replace(":", "-");
                            String ID_runwayInfo = parts[2];

                            Collections.sort(Airplane.airplanes);
                            Collections.sort(WideBodyAirplane.wide_airplanes);
                            Collections.sort(NarrowBodyAirplane.narrow_airplanes);

                            FileWriter r_i = new FileWriter(antetResources + name_test + "/" + runway_info + "_" +
                                    ID_runwayInfo + "_" + time_runway_info + ".out");
                            PrintWriter p_ri = new PrintWriter(r_i);

                            if (Runway.check_ID_runway(Runway.runways_takeoff, ID_runwayInfo)) {
                                Duration duration = Duration.between(LocalTime.parse(parts[0]), time_maneuver_takeoff).abs();
                                if (duration.toMinutes() <= 5) {
                                    p_ri.println(ID_runwayInfo + " - OCCUPIED");
                                } else {
                                    p_ri.println(ID_runwayInfo + " - FREE");
                                }
                            } else {
                                Duration duration = Duration.between(LocalTime.parse(parts[0]), time_maneuver_land).abs();
                                if (duration.toMinutes() <= 10) {
                                    p_ri.println(ID_runwayInfo + " - OCCUPIED");
                                } else {
                                    p_ri.println(ID_runwayInfo + " - FREE");
                                }
                            }

                            for (int i = 0; i < WideBodyAirplane.wide_airplanes.size(); i++) {
                                if (WideBodyAirplane.wide_airplanes.get(i).getID_runway().equals(ID_runwayInfo) &&
                                        (WideBodyAirplane.wide_airplanes.get(i).getStatus().equals("WAITING_FOR_TAKEOFF") ||
                                                WideBodyAirplane.wide_airplanes.get(i).getStatus().equals("WAITING_FOR_LANDING"))) {
                                    p_ri.println(WideBodyAirplane.wide_airplanes.get(i).toString());
                                }
                            }

                            for (int i = 0; i < NarrowBodyAirplane.narrow_airplanes.size(); i++) {
                                if (NarrowBodyAirplane.narrow_airplanes.get(i).getID_runway().equals(ID_runwayInfo) &&
                                        (NarrowBodyAirplane.narrow_airplanes.get(i).getStatus().equals("WAITING_FOR_TAKEOFF") ||
                                                NarrowBodyAirplane.narrow_airplanes.get(i).getStatus().equals("WAITING_FOR_LANDING"))) {
                                    p_ri.println(NarrowBodyAirplane.narrow_airplanes.get(i).toString());
                                }
                            }

                            p_ri.close();

                            break;
                        ////////////////////////permission maneuver
                        case perms_maneuver:
                            LocalTime time_perms_maneuver = LocalTime.parse(parts[0]);
                            String ID_maneuver = parts[2];
                            boolean ID_finder_maneuver = Runway.check_ID_runway(Runway.runways_takeoff, ID_maneuver);

                            Collections.sort(Airplane.airplanes);
                            Collections.sort(WideBodyAirplane.wide_airplanes);
                            Collections.sort(NarrowBodyAirplane.narrow_airplanes);

                            try {
                                if (ID_finder_maneuver) {
                                    UnavailableRunwayException.verification_maneuver(time_maneuver_takeoff, time_perms_maneuver, ID_finder_maneuver);
                                    time_maneuver_takeoff = time_perms_maneuver;
                                } else {
                                    UnavailableRunwayException.verification_maneuver(time_maneuver_land, time_perms_maneuver, ID_finder_maneuver);
                                    time_maneuver_land = time_perms_maneuver;
                                }

                                Airplane.status_setter(WideBodyAirplane.wide_airplanes, ID_finder_maneuver, ID_maneuver, parts[0]);
                                Airplane.status_setter(NarrowBodyAirplane.narrow_airplanes, ID_finder_maneuver, ID_maneuver, parts[0]);

                            } catch (UnavailableRunwayException e) {
                                p_be.println(time_perms_maneuver.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " | " + e.getMessage());
                            }

                            break;
                        ///////////////////exit code
                        case exit:
                            Runway.runways_land.clear();
                            Runway.runways_takeoff.clear();
                            Airplane.airplanes.clear();
                            WideBodyAirplane.wide_airplanes.clear();
                            NarrowBodyAirplane.narrow_airplanes.clear();
                            p_be.close();
                            p_fi.close();
                            break;
                    }
                }
            } catch (IOException e) {
                System.out.println("File doesn't exist");
            }
        } else {
            System.out.println("Tema2");
        }
    }
}