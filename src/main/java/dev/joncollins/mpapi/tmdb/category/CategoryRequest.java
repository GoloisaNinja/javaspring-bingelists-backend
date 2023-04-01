package dev.joncollins.mpapi.tmdb.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    private String page;
    private String media_type;
    private String genre;
    private String sort_by;
}
