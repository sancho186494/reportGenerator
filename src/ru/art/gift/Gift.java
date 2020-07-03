package ru.art.gift;

public interface Gift {
    void addSweetness();                //добавить сладости
    void deleteSweetness(int index);    //удалить сладость по индексу
    void deleteLastSweetness();         //удалить последнюю сладость
    double getTotalWeight();            //получить общий вес
    double getTotalCost();              //получить общую стоимость
    void showAll();                     //показать содержимое коробки
}
