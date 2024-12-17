package com.server.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.server.dto.UserDTO;
import com.server.entities.User;
import com.server.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getAll() throws Exception {
        List<User> users = userRepository.findAll();
        if (users.isEmpty())
            throw new Exception("Users not found");

        return users;
    }

    public Map<String, Object> getAll(int page, int size) throws Exception {
        Map<String, Object> responseBody = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);
        if (users.isEmpty())
            throw new Exception("Users not found");

        responseBody.put("data", users.getContent());
        responseBody.put("total", users.getTotalElements());
        responseBody.put("page", users.getPageable().getPageNumber());

        return responseBody;
    }

    public Optional<User> findById(long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty())
            throw new Exception("User not found");

        return user;
    }

    public Optional<User> findByEmail(String email) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty())
            throw new Exception("User not found");

        return user;
    }

    public User create(UserDTO user) throws Exception {
        if (!userRepository.findByEmail(user.getEmail()).isEmpty()) {
            throw new Exception("A user with this email already exists");
        }
        User newUser = User.builder().firstName(user.getFirstName()).lastName(user.getLastName())
                .email(user.getEmail()).password(user.getPassword()).isConfirmed(false).build();
        userRepository.save(newUser);

        return newUser;
    }

    public User update(long id, UserDTO user) throws Exception {
        long userId = this.findById(id).get().getId();
        User updatedUser = User.builder().id(userId).firstName(user.getFirstName()).lastName(user.getLastName())
                .email(user.getEmail()).password(user.getPassword()).isConfirmed(false).build();
        userRepository.save(updatedUser);

        return updatedUser;
    }

    public void deleteById(long id) throws Exception {
        this.findById(id);
        userRepository.deleteById(id);
    }

    public void confirm(long id) throws Exception {
        User user = this.findById(id).get();

        if (user.isConfirmed()) {
            throw new Exception("User already verified");
        }

        user.setConfirmed(true);
        userRepository.save(user);
    }
}
