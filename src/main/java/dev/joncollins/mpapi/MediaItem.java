package dev.joncollins.mpapi;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class MediaItem {
    private String mediaId;
    private String title;
    private String type;
    private String posterPath;
    private int primaryGenreId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        MediaItem mediaObj = (MediaItem) obj;
        if (mediaObj.getMediaId()
                    .equals(this.mediaId)) {
            return true;
        }
        return false;
    }
}
