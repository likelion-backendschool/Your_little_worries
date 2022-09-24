package likelion.ylw.category;

import likelion.ylw.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    public List<Category> getList() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList;
    }

    public Category findById(Integer category_id) {
        Optional<Category> category = categoryRepository.findById(category_id);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new DataNotFoundException("category not found");
        }
    }
}
