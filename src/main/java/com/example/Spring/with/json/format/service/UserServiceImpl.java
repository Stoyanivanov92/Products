package com.example.Spring.with.json.format.service;

import com.example.Spring.with.json.format.model.dto.SoldProductsByUsersDTO;
import com.example.Spring.with.json.format.model.dto.SoldProductsDto;
import com.example.Spring.with.json.format.model.dto.UserDto;
import com.example.Spring.with.json.format.model.entity.Product;
import com.example.Spring.with.json.format.model.entity.User;
import com.example.Spring.with.json.format.repository.UserRepository;
import com.example.Spring.with.json.format.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService {

    private static final String FILE_PATH_USERS ="src/main/resources/files/users.json";

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public void seedUsersDB() throws IOException {
        if (userRepository.count() > 0) {
            return;
        }
        String jsonUsers = Files.readString(Path.of(FILE_PATH_USERS));
        UserDto[] userDtos = gson.fromJson(jsonUsers, UserDto[].class);

        for (UserDto userDto : userDtos) {
            if (validationUtil.violations(userDto).isEmpty()) {
                User user = modelMapper.map(userDto, User.class);
                userRepository.save(user);
            }
        }
    }

    @Override
    public User findUser() {
        long id = ThreadLocalRandom.current().nextLong(1, userRepository.count() + 1);
        return userRepository.findById(id).orElse(null);

    }

    @Override
    public String getAllUsersWithAtLeastOneItemSoldWithBuyer() {
        List<SoldProductsByUsersDTO> soldProductsByUsersDTOS = new ArrayList<>();
        List<User> filteredUsers = new ArrayList<>();
        List<User> usersAll = userRepository.findAll();
        for (User user : usersAll) {
            Set<Product> products = user.getProducts();
            if (!products.isEmpty()) {
                Set<Product> filteredProducts = products.stream().filter(p -> p.getBuyer() != null).collect(Collectors.toSet());
                user.setProducts(filteredProducts);
                if (!filteredProducts.isEmpty()) {
                    filteredUsers.add(user);
                }
            }


        }

        List<User> orderedUsers = filteredUsers
                .stream()
                .sorted((u1, u2) -> {
                    int result = u1.getLastName().compareTo(u2.getLastName());
                    if (result == 0) {
                        if (u1.getFirstName() != null && u2.getFirstName() != null) {
                            result = u1.getFirstName().compareTo(u2.getFirstName());
                        }

                    }
                    return result;
                }).collect(Collectors.toList());

        for (User user : orderedUsers) {
            SoldProductsByUsersDTO soldProductsByUsersDTO = modelMapper.map(user, SoldProductsByUsersDTO.class);
            soldProductsByUsersDTO.setSoldProducts(modelMapper.map(user.getProducts(), SoldProductsDto[].class));
            soldProductsByUsersDTOS.add(soldProductsByUsersDTO);
        }
        return gson.toJson(soldProductsByUsersDTOS);

    }
}
