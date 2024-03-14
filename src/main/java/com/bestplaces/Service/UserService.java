package com.bestplaces.Service;

import com.bestplaces.Dto.UserRegistrationDto;
import com.bestplaces.Entity.User;

public interface UserService {
    User save(UserRegistrationDto userRegistrationDto);
}
