package ru.art.gift.sweetness;

public class Candy extends Sweetness {
    private boolean isChewing;

    public Candy(String name, double weight, double cost, boolean isChewing) {
        super(name, weight, cost);
        this.isChewing = isChewing;
    }

    public boolean isChewing() {
        return isChewing;
    }

    public void setChewing(boolean chewing) {
        isChewing = chewing;
    }

    @Override
    public String getUniqueParameter() {
        if (isChewing)
            return "жевательные";
        else
            return "обычные";
    }
}
