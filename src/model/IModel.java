package model;

import controller.AbstractController;

public interface IModel {
    void add(AbstractController observer);
    void remove(AbstractController observer);

}
