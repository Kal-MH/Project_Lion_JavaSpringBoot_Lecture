package dev.kalmh.auth.infra;

import dev.kalmh.auth.entity.UserEntity;
import dev.kalmh.auth.entity.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public CustomUserDetailService(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;

        //회원가입 구현 전 미리 더미 사용자를 넣어줌.
        final UserEntity testUserEntity = new UserEntity();
        testUserEntity.setUsername("entity_user");
        //extracId() 메소드 안에서 비밀번호를 암호화를 진행해서 저장하게 되는데
        //평문으로 저장하면 오류가 발생한다.
        // - testUserEntity.setPassword("test1pass"); // error
        testUserEntity.setPassword(passwordEncoder.encode("test1pass"));
        this.userRepository.save(testUserEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity userEntity = userRepository.findByUsername(username);
        return new User(username, userEntity.getPassword(), new ArrayList<>());
    }
}
