package com.bomberman.repository;

import java.util.List;

public interface IRepository<T> {
    // Tüm kayıtları getir
    List<T> getAll();

    // ID'ye göre getir
    T getById(int id);

    // Yeni kayıt ekle
    void add(T entity);

    // Kayıt güncelle
    void update(T entity);

    // Kayıt sil
    void delete(int id);
}