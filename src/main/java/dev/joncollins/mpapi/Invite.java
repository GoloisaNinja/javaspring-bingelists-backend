package dev.joncollins.mpapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Builder
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Invite {
    @Id
    private String id;
    private String bingeListId;
    private String bingeListName;
    private String invitedByName;
    private String invitedById;
    private String invitedUserName;
    private String invitedUserId;
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Invite inviteObj = (Invite) o;
        if (inviteObj.getId().equals(this.getId())) {
            return true;
        }
        return false;
    }
}
