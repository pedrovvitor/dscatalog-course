package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats WHERE "
            + "(COALESCE(:categories) IS NULL OR cats IN :categories) AND "
            + "(LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%'))) ")
    Page<Product> find(List<Category> categories, String name, Pageable pageable);

    //Consulta que põe as categorias em memoria e evitar o problema da N+1 consultas(1 ida ao banco para cada obj encontrado
    @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj IN :products")
    List<Product> findProductsWithCategories(List<Product> products);
}
