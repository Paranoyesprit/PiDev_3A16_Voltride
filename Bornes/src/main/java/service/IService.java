package service;

import entities.borne;

import java.util.List;

    public interface IService <T>{
    void addpst(T t);
    void delete(int id);
    void update(int id,T t);
    List<T> readAll();

    List<borne> readAvailableBornes();

    T readById(int id);


}
