package com.boostphysioclinic.model;

public class Appointment {
    private Patient patient;
    private Physiotherapist physiotherapist;
    private TreatmentOffering treatment;
    private TimeSlot timeSlot;
    private AppointmentStatus status;

    public Appointment(Patient patient, Physiotherapist physiotherapist, TreatmentOffering treatment, TimeSlot timeSlot) {
        this.patient = patient;
        this.physiotherapist = physiotherapist;
        this.treatment = treatment;
        this.timeSlot = timeSlot;
        this.status = AppointmentStatus.BOOKED;
        timeSlot.setBooked(true);
    }

    public void cancel() {
        status = AppointmentStatus.CANCELLED;
        timeSlot.setBooked(false);
    }

    public void attend() {
        status = AppointmentStatus.ATTENDED;
    }

    public Patient getPatient() { return patient; }
    public Physiotherapist getPhysiotherapist() { return physiotherapist; }
    public TreatmentOffering getTreatment() { return treatment; }
    public TimeSlot getTimeSlot() { return timeSlot; }
    public AppointmentStatus getStatus() { return status; }
}
