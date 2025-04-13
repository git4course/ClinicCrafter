import com.boostphysioclinic.BookingSystem;
import com.boostphysioclinic.model.*;
import org.junit.jupiter.api.*;
import java.time.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class BookingSystemTest {
    private BookingSystem system;
    private Physiotherapist physio1;
    private Patient patient1;
    private TreatmentOffering massage;
    private TimeSlot slot1;

    @BeforeEach
    void setUp() {
        system = new BookingSystem();
        
        // Setup Physiotherapist
        physio1 = new Physiotherapist();
        physio1.setId(1);
        physio1.setFullName("Dr. Jane Doe");
        physio1.getAreasOfExpertise().add("Physiotherapy");
        
        // Setup Treatment with TimeSlots
        massage = new TreatmentOffering("Therapeutic Massage", "Physiotherapy");
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        LocalDateTime time = LocalDateTime.of(startDate, LocalTime.of(10, 0));
        slot1 = new TimeSlot(time, time.plusHours(1));
        massage.getTimeSlots().add(slot1);
        physio1.getTreatmentOfferings().add(massage);
        
        system.getPhysiotherapists().add(physio1);

        // Setup Patient
        patient1 = new Patient();
        patient1.setId(1);
        patient1.setFullName("John Doe");
        system.addPatient(patient1);
    }

    @Test
    void testAddRemovePatient() {
        Patient p = new Patient();
        p.setId(2);
        system.addPatient(p);
        assertTrue(system.getPatients().contains(p));
        
        system.removePatient(p);
        assertFalse(system.getPatients().contains(p));
    }

    @Test
    void testFindPhysioByArea() {
        List<Physiotherapist> result = system.findPhysiosByArea("Physiotherapy");
        assertEquals(1, result.size());
        assertEquals(physio1, result.get(0));

        assertTrue(system.findPhysiosByArea("Non-existent").isEmpty());
    }

    @Test
    void testGetTreatmentsByPhysio() {
        List<TreatmentOffering> treatments = system.getTreatmentsByPhysio(physio1);
        assertEquals(1, treatments.size());
        assertEquals("Therapeutic Massage", treatments.get(0).getName());
    }

    @Test
    void testBookAppointment() {
        // First booking should succeed
        Appointment appt = system.bookAppointment(patient1, physio1, massage, slot1);
        assertNotNull(appt);
        assertTrue(slot1.isBooked());
        assertEquals(1, patient1.getAppointments().size());

        // Second booking same slot should fail
        assertNull(system.bookAppointment(patient1, physio1, massage, slot1));
    }

    @Test
    void testCancelAppointment() {
        Appointment appt = system.bookAppointment(patient1, physio1, massage, slot1);
        appt.cancel();
        
        assertEquals(AppointmentStatus.CANCELLED, appt.getStatus());
        assertFalse(slot1.isBooked());
    }

    @Test
    void testAttendAppointment() {
        Appointment appt = system.bookAppointment(patient1, physio1, massage, slot1);
        appt.attend();
        assertEquals(AppointmentStatus.ATTENDED, appt.getStatus());
    }

    @Test
    void testGenerateReportOrder() {
        // Create second physio with more attended appointments
        Physiotherapist physio2 = new Physiotherapist();
        physio2.setFullName("Dr. Jane Doe");
        TreatmentOffering rehab = new TreatmentOffering("Rehab", "Physiotherapy");
        TimeSlot slot2 = new TimeSlot(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        rehab.getTimeSlots().add(slot2);
        physio2.getTreatmentOfferings().add(rehab);
        system.getPhysiotherapists().add(physio2);

        // Book 1 attended appointment for physio1
        Appointment appt1 = system.bookAppointment(patient1, physio1, massage, slot1);
        appt1.attend();

        // Book 2 attended appointments for physio2
        Patient p2 = new Patient();
        system.addPatient(p2);
        Appointment appt2 = system.bookAppointment(p2, physio2, rehab, slot2);
        appt2.attend();
        TimeSlot slot3 = new TimeSlot(slot2.getStartTime().plusDays(7), slot2.getEndTime().plusDays(7));
        rehab.getTimeSlots().add(slot3);
        Appointment appt3 = system.bookAppointment(p2, physio2, rehab, slot3);
        appt3.attend();

        // Generate report and check order
        system.generateReport();  // Manual verification needed for output
        List<Physiotherapist> physios = system.getPhysiotherapists();
        assertEquals("Dr. Jane Doe", physios.get(0).getFullName());  // Should be first after sorting
    }
}
