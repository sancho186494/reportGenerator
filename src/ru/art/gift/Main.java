package ru.art.gift;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

public class Main {
    private static double maxTotalWeight = 0;       //максимальный вес для умной оптимизации
    private static double maxTotalCost = 0;         //максимальная стоимость для умной оптимизации

    public static void main(String[] args) {
        //создание и заполнение коробки, вывод информации
        GiftBox gift = new GiftBox();
        gift.addSweetness();
        gift.getInfo();
        optimize(gift);
    }

    //умная оптимизация
    public static void optimize(GiftBox gift) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("*****************************");
        System.out.println("Ограничить вес? (Ответ в граммах, либо \"нет\")");
        while (true) {
            try {
                String line = reader.readLine();
                if (line.equals("нет")) {
                    break;
                }
                maxTotalWeight = Double.parseDouble(line) / 1000;
                break;
            } catch (IOException e) {
                System.out.println("Некорректный ввод, просьба повторить");
            }
        }
        System.out.println("Ограничить стоимость? (Ответ в рублях, либо \"нет\")");
        while (true) {
            try {
                String line = reader.readLine();
                if (line.equals("нет")) {
                    break;
                }
                maxTotalCost = Double.parseDouble(line);
                break;
            } catch (IOException e) {
                System.out.println("Некорректный ввод, просьба повторить");
            }
        }

        if (maxTotalWeight < gift.getTotalWeight() && maxTotalWeight != 0) {
            gift.reduceWeight(maxTotalWeight);
        }

        if (maxTotalCost < gift.getTotalCost() && maxTotalCost != 0) {
            gift.reduceCost(maxTotalCost);
        }

        gift.getInfo();
    }
}
