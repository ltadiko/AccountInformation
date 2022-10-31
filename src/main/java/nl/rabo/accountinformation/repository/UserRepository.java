package nl.rabo.accountinformation.repository;

import nl.rabo.accountinformation.models.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity save(UserEntity userEntity);
}
