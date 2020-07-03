package ru.art.gift.sweetness;

public class Gum extends Sweetness {
    private int gumsCount;

    public Gum(String name, double weight, double cost, int gumsCount) {
        super(name, weight, cost);
        this.gumsCount = gumsCount;
    }

    public int getGumsCount() {
        return gumsCount;
    }

    public void setGumsCount(int gumsCount) {
        this.gumsCount = gumsCount;
    }

    @Override
    public String getUniqueParameter() {
        return "(в пачке " + gumsCount + " шт)";
    }
}
