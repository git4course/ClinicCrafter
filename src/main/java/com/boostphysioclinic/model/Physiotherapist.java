package com.boostphysioclinic.model;

import java.util.ArrayList;
import java.util.List;

public class Physiotherapist extends Member {
    private List<String> areasOfExpertise = new ArrayList<>();
    private List<TreatmentOffering> treatmentOfferings = new ArrayList<>();

    public List<String> getAreasOfExpertise() { return areasOfExpertise; }
    public List<TreatmentOffering> getTreatmentOfferings() { return treatmentOfferings; }
}
