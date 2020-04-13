package com.webapp.cmsshopping.repository;

import com.webapp.cmsshopping.models.Category;
import com.webapp.cmsshopping.models.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository  extends JpaRepository<Category,Integer> {
    Category findBySlug(String slug);
    Category findByName(String name);
}
