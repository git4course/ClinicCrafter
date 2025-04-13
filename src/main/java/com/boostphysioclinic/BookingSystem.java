package com.boostphysioclinic;

import com.boostphysioclinic.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookingSystem {
    private List<Patient> patients = new ArrayList<>();
    private List<Physiotherapist> physiotherapists = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();

    public void addPatient(Patient patient) { patients.add(patient); }

    public void removePatient(Patient patient) { patients.remove(patient); }

    public List<Patient> getPatients() { return patients; }

    public List<Physiotherapist> getPhysiotherapists() { return physiotherapists; }

    public List<Physiotherapist> findPhysiosByArea(String area) {
        return physiotherapists.stream()
                .filter(p -> p.getAreasOfExpertise().contains(area))
                .collect(Collectors.toList());
    }

    public List<TreatmentOffering> getTreatmentsByPhysioAndArea(Physiotherapist physio, String area) {
        return physio.getTreatmentOfferings().stream()
                .filter(t -> t.getArea().equals(area))
                .collect(Collectors.toList());
    }

    public List<TreatmentOffering> getTreatmentsByPhysio(Physiotherapist physio) {
        return physio.getTreatmentOfferings();
    }

    public List<TimeSlot> getAvailableTimeSlots(TreatmentOffering treatment) {
        return treatment.getTimeSlots().stream()
                .filter(ts -> !ts.isBooked())
                .collect(Collectors.toList());
    }

    public Appointment bookAppointment(Patient patient, Physiotherapist physio, TreatmentOffering treatment, TimeSlot timeSlot) {
        if (!timeSlot.isBooked()) {
            Appointment appointment = new Appointment(patient, physio, treatment, timeSlot);
            appointments.add(appointment);
            patient.getAppointments().add(appointment);
            return appointment;
        }
        return null;
    }

    public void generateReport() {
        Map<Physiotherapist, List<Appointment>> appointmentsByPhysio = appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getPhysiotherapist));

        List<Physiotherapist> sortedPhysios = physiotherapists.stream()
                .sorted((p1, p2) -> {
                    long count1 = appointmentsByPhysio.getOrDefault(p1, Collections.emptyList()).stream()
                            .filter(a -> a.getStatus() == AppointmentStatus.ATTENDED)
                            .count();
                    long count2 = appointmentsByPhysio.getOrDefault(p2, Collections.emptyList()).stream()
                            .filter(a -> a.getStatus() == AppointmentStatus.ATTENDED)
                            .count();
                    return Long.compare(count2, count1);
                })
                .collect(Collectors.toList());

        System.out.println("=== BPC Treatment Report ===");
        for (Physiotherapist physio : sortedPhysios) {
            System.out.println("\nPhysiotherapist: " + physio.getFullName());
            List<Appointment> physioAppointments = appointmentsByPhysio.getOrDefault(physio, Collections.emptyList());
            if (physioAppointments.isEmpty()) {
                System.out.println("  No appointments.");
            } else {
                for (Appointment appt : physioAppointments) {
                    System.out.printf("  Treatment: %-25s | Patient: %-20s | Time: %s - %s | Status: %s%n",
                            appt.getTreatment().getName(),
                            appt.getPatient().getFullName(),
                            appt.getTimeSlot().getStartTime().format(java.time.format.DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm")),
                            appt.getTimeSlot().getEndTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")),
                            appt.getStatus());
                }
            }
        }
    }
}
