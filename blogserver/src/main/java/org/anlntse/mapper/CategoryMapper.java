package org.anlntse.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.anlntse.bean.Category;

import java.util.List;


/**
 * @author: Jun Xie
 * @date: 7/18/21
 **/

@Mapper
public interface CategoryMapper {

    List<Category> getAllCategories();

    int deleteCategoryByIds(@Param("ids") String[] ids);

    int updateCategoryById(Category category);

    int addCategory(Category category);
}
