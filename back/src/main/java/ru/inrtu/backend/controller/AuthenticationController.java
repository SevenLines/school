package ru.inrtu.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.inrtu.backend.dto.SchoolchildDto;
import ru.inrtu.backend.mapper.SchoolchildMapper;
import ru.inrtu.backend.service.SchoolchildService;
import ru.inrtu.backend.utils.JwtUtil;

/**
 * Класс, в котором обрабатываются запросы на API методы для регистрации/входа в платформу
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class AuthenticationController {

    private final SchoolchildService schoolchildService;

    private final AuthenticationManager authenticationManager;

    private final SchoolchildMapper schoolchildMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registration(@RequestBody final SchoolchildDto schoolchildForRegister) {

        System.out.println("Registration request received");
        if (schoolchildService.saveUser(schoolchildMapper.toEntity(schoolchildForRegister), bCryptPasswordEncoder)) {
            return ResponseEntity.ok("User successfully created");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during registration");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<SchoolchildDto> login(@RequestBody final SchoolchildDto schoolchildDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(schoolchildDto.getEmail(), schoolchildDto.getPassword()));
        } catch (final BadCredentialsException e) {
            return ResponseEntity.badRequest().build();
        }
        final UserDetails userDetails = schoolchildService.loadUserByUsername(schoolchildDto.getEmail());
        SchoolchildDto existedSchoolchildDto =
                schoolchildMapper.toDto(schoolchildService.findUserByEmail(schoolchildDto.getEmail()));
        final String jwt = JwtUtil.generateToken(userDetails);
        existedSchoolchildDto.setToken(jwt);
        System.out.println("Schoolchild in response: " + existedSchoolchildDto);
        return ResponseEntity.ok(existedSchoolchildDto);
    }

}
