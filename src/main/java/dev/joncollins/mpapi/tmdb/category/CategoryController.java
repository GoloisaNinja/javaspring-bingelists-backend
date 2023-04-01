package dev.joncollins.mpapi.tmdb.category;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/category")
@AllArgsConstructor
public class CategoryController {
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResponseEntity<String> fetchCategoryList(@RequestParam String media_type) {
        return ResponseEntity.ok(categoryService.fetchCategoryList(media_type));
    }

    @GetMapping
    public ResponseEntity<String> fetchCategoryTypeAndPage(@RequestParam Map<String, String> params) {
        String sortBy = params.get("sort_by") == null ? "not provided" : params.get("sort_by");
        var req = CategoryRequest.builder()
                .page(params.get("page"))
                .media_type(params.get("media_type"))
                .genre(params.get("genre"))
                .sort_by(sortBy)
                .build();
        return ResponseEntity.ok(categoryService.fetchCategoryByTypeAndPage(req));
    }
}
