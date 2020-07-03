package ru.art.gift;

import ru.art.gift.sweetness.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GiftBox implements Gift {
    private List<Sweetness> box = new ArrayList<>();

    @Override
    public void addSweetness() {
        box.add(new Chocolate("Шоколад", 0.25, 150.5, "молочный"));
        box.add(new Chocolate("Шоколад", 0.15, 300, "темный"));
        box.add(new Gum("Жевательная резинка", 0.05, 40, 10));
        box.add(new Gum("Жевательная резинка", 0.05, 50, 12));
        box.add(new Cookie("Печенье", 0.5, 120, "ChocoPie"));
        box.add(new Cookie("Печенье", 0.6, 180, "WagonWheels"));
        box.add(new Candy("Кофеты", 0.1, 50, false));
        box.add(new Candy("Кофеты", 0.2, 90, true));
    }

    @Override
    public void deleteSweetness(int index) {
        box.remove(index);
    }

    @Override
    public void deleteLastSweetness() {
        box.remove(box.size() - 1);
    }

    @Override
    public double getTotalWeight() {
        double summ = 0;
        for (Sweetness sweetness : box) {
            summ += sweetness.getWeight();
        }
        return summ;
    }

    @Override
    public double getTotalCost() {
        double summ = 0;
        for (Sweetness sweetness : box) {
            summ += sweetness.getCost();
        }
        return summ;
    }

    @Override
    public void showAll() {
        for (Sweetness sweetness : box) {
            System.out.println(String.format("%s %s, масса %sкг, стоимость %s руб", sweetness.getName(),
                    sweetness.getUniqueParameter(), sweetness.getWeight(), sweetness.getCost()));
        }
    }

    public void getInfo() {
        System.out.println("Общий вес " + BigDecimal.valueOf(this.getTotalWeight()).setScale(2, BigDecimal.ROUND_DOWN) + " кг");
        System.out.println("Общая стоимость " + BigDecimal.valueOf(this.getTotalCost()).setScale(2, BigDecimal.ROUND_DOWN) + " руб");
    }

    //команды умной оптимизации
    public void reduceWeight(double maxTotalWeight) {
        while (maxTotalWeight < this.getTotalWeight()) {
            int index = 0;
            double minWeight = box.get(0).getWeight();
            for (int i = 1; i < box.size(); i++) {
                if (box.get(i).getWeight() < minWeight) {
                    index = i;
                    minWeight = box.get(i).getWeight();
                }
            }
            box.remove(index);
        }
    }
    public void reduceCost(double maxTotalCost) {
        while (maxTotalCost < this.getTotalCost()) {
            int index = 0;
            double minCost = box.get(0).getCost();
            for (int i = 1; i < box.size(); i++) {
                if (box.get(i).getCost() < minCost) {
                    index = i;
                    minCost = box.get(i).getCost();
                }
            }
            box.remove(index);
        }
    }
}
