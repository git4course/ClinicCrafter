package com.boostphysioclinic.model;

import java.util.ArrayList;
import java.util.List;

public class TreatmentOffering {
    private String name;
    private String area;
    private List<TimeSlot> timeSlots = new ArrayList<>();

    public TreatmentOffering(String name, String area) {
        this.name = name;
        this.area = area;
    }

    public String getName() { return name; }
    public String getArea() { return area; }
    public List<TimeSlot> getTimeSlots() { return timeSlots; }
}
