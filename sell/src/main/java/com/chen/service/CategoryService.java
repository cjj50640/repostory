package com.chen.service;

import com.chen.dataobject.ProductCategory;

import java.util.List;

public interface CategoryService {

    ProductCategory findone(Integer categoryId);

    List<ProductCategory> findall();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> catagoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
