package com.bjcareer.userservice.service;

import com.bjcareer.userservice.domain.User;
import com.bjcareer.userservice.repository.DatabaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final DatabaseRepository databaseRepository;

    @Transactional(readOnly = true)
    public void login(User inputUser) throws AuthenticationException {
        Optional<User> userFromDatabase = databaseRepository.findByUserId(inputUser.getUserId());

        if(userFromDatabase.isEmpty()){
            throw new AuthenticationException("잘못된 ID나 PASSWORD를 입력했습니다.");
        }

        User storedUser = userFromDatabase.get();
        boolean isVerify = storedUser.verifyPassword(inputUser.getPassword());

        if(!isVerify){
            throw new AuthenticationException("잘못된 ID나 PASSWORD를 입력했습니다2.");
        }
    }
}
