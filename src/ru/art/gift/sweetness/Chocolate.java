package ru.art.gift.sweetness;

public class Chocolate extends Sweetness {
    private String chocolateType;

    public Chocolate(String name, double weight, double cost, String chocolateType) {
        super(name, weight, cost);
        this.chocolateType = chocolateType;
    }

    public String getChocolateType() {
        return chocolateType;
    }

    public void setChocolateType(String chocolateType) {
        this.chocolateType = chocolateType;
    }

    @Override
    public String getUniqueParameter() {
        return chocolateType;
    }
}
