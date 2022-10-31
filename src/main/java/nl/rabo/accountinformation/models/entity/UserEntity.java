package nl.rabo.accountinformation.models.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.rabo.accountinformation.models.enums.UserStatus;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "USERS")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private long userId;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "CREATED_DATE")
    private Timestamp createdDate;
    @Column(name = "LAST_MODIFIED_DATE")
    private Timestamp lastModifiedDate;
}
