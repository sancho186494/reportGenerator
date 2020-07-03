package ru.art.gift.sweetness;

public class Cookie extends Sweetness {
    private String cookieBrand;

    public Cookie(String name, double weight, double cost, String cookieBrand) {
        super(name, weight, cost);
        this.cookieBrand = cookieBrand;
    }

    public String getCookieBrand() {
        return cookieBrand;
    }

    public void setCookieBrand(String cookieBrand) {
        this.cookieBrand = cookieBrand;
    }

    @Override
    public String getUniqueParameter() {
        return cookieBrand;
    }
}
