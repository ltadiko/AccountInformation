package nl.rabo.accountinformation.services;

import nl.rabo.accountinformation.exceptions.UserNotFoundException;
import nl.rabo.accountinformation.models.UserRequest;
import nl.rabo.accountinformation.models.entity.UserEntity;
import nl.rabo.accountinformation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * @author : Lakshmaiah Tatikonda
 * User service implements managing users business logic by communicating with backend database
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
    }

    public void addUser(UserRequest userRequest) {
        userRepository.save(UserEntity.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .status(userRequest.getStatus())
                .createdDate(Timestamp.from(Instant.now()))
                .lastModifiedDate(Timestamp.from(Instant.now()))
                .email(userRequest.getEmail())
                .build());
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


}
