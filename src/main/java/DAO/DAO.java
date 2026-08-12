package DAO;

public interface DAO<T>{
    void insert(T par);

    void update(T par);

    void delete(T par);

    void select(T par);
}
