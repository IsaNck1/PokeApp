package com.isabellnoack.myapp.api;

public class BerryFlavor {
    public NameWithURL flavor;
    public int potency;

    @Override
    public String toString() {
        return "FlavorWithPotency{" +
                "flavor=" + flavor +
                ", potency=" + potency +
                '}';
    }
}
