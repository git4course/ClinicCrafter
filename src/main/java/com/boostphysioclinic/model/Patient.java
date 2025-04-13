package com.boostphysioclinic.model;

import java.util.ArrayList;
import java.util.List;

public class Patient extends Member {
    private List<Appointment> appointments = new ArrayList<>();

    public List<Appointment> getAppointments() { return appointments; }
}
