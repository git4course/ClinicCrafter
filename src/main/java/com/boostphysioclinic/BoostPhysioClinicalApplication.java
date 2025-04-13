package com.boostphysioclinic;

import com.boostphysioclinic.model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;

public class BoostPhysioClinicalApplication {
    public static void main(String[] args) {
        BookingSystem bookingSystem = new BookingSystem();

        // Setup Physiotherapists
        Physiotherapist physio1 = new Physiotherapist();
        physio1.setId(1);
        physio1.setFullName("Dr. Sarah Smith");
        physio1.setAddress("123 Wellness Rd");
        physio1.setTelephone("555-0101");
        physio1.getAreasOfExpertise().addAll(Arrays.asList("Physiotherapy", "Rehabilitation"));

        TreatmentOffering massage = new TreatmentOffering("Therapeutic Massage", "Physiotherapy");
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        for (int week = 0; week < 4; week++) {
            LocalDate monday = startDate.plusWeeks(week).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            massage.getTimeSlots().add(new TimeSlot(
                    LocalDateTime.of(monday, LocalTime.of(10, 0)),
                    LocalDateTime.of(monday, LocalTime.of(11, 0))));

            LocalDate wednesday = startDate.plusWeeks(week).with(TemporalAdjusters.previousOrSame(DayOfWeek.WEDNESDAY));
            massage.getTimeSlots().add(new TimeSlot(
                    LocalDateTime.of(wednesday, LocalTime.of(14, 0)),
                    LocalDateTime.of(wednesday, LocalTime.of(15, 0))));
        }
        physio1.getTreatmentOfferings().add(massage);

        TreatmentOffering rehab = new TreatmentOffering("Pool Rehabilitation", "Rehabilitation");
        for (int week = 0; week < 4; week++) {
            LocalDate tuesday = startDate.plusWeeks(week).with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
            rehab.getTimeSlots().add(new TimeSlot(
                    LocalDateTime.of(tuesday, LocalTime.of(9, 0)),
                    LocalDateTime.of(tuesday, LocalTime.of(10, 0))));
        }
        physio1.getTreatmentOfferings().add(rehab);

        Physiotherapist physio2 = new Physiotherapist();
        physio2.setId(2);
        physio2.setFullName("Dr. John Doe");
        physio2.setAddress("456 Health Ave");
        physio2.setTelephone("555-0202");
        physio2.getAreasOfExpertise().add("Osteopathy");

        TreatmentOffering osteopathy = new TreatmentOffering("Spinal Adjustment", "Osteopathy");
        for (int week = 0; week < 4; week++) {
            LocalDate thursday = startDate.plusWeeks(week).with(TemporalAdjusters.nextOrSame(DayOfWeek.THURSDAY));
            osteopathy.getTimeSlots().add(new TimeSlot(
                    LocalDateTime.of(thursday, LocalTime.of(11, 0)),
                    LocalDateTime.of(thursday, LocalTime.of(12, 0))));
        }
        physio2.getTreatmentOfferings().add(osteopathy);

        bookingSystem.getPhysiotherapists().addAll(Arrays.asList(physio1, physio2));

        // Setup Patients
        for (int i = 1; i <= 12; i++) {
            Patient p = new Patient();
            p.setId(i);
            p.setFullName("Patient " + i);
            p.setAddress("Address " + i);
            p.setTelephone("555-1" + String.format("%03d", i));
            bookingSystem.addPatient(p);
        }

        // Simulate bookings
        Patient patient1 = bookingSystem.getPatients().get(0);
        List<Physiotherapist> physios = bookingSystem.findPhysiosByArea("Physiotherapy");
        if (!physios.isEmpty()) {
            Physiotherapist physio = physios.get(0);
            TreatmentOffering treatment = physio.getTreatmentOfferings().get(0);
            List<TimeSlot> slots = bookingSystem.getAvailableTimeSlots(treatment);
            if (!slots.isEmpty()) {
                Appointment appt = bookingSystem.bookAppointment(patient1, physio, treatment, slots.get(0));
                if (appt != null) appt.attend();
            }
        }

        Patient patient2 = bookingSystem.getPatients().get(1);
        physios = bookingSystem.findPhysiosByArea("Osteopathy");
        if (!physios.isEmpty()) {
            Physiotherapist physio = physios.get(0);
            TreatmentOffering treatment = physio.getTreatmentOfferings().get(0);
            List<TimeSlot> slots = bookingSystem.getAvailableTimeSlots(treatment);
            if (!slots.isEmpty()) {
                Appointment appt = bookingSystem.bookAppointment(patient2, physio, treatment, slots.get(0));
            }
        }

        // Generate report
        bookingSystem.generateReport();
    }
}
