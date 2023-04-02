package ru.inrtu.backend.service;

import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.inrtu.backend.customException.SchoolchildAlreadyExistException;
import ru.inrtu.backend.dto.SchoolchildrenStatusActivityDto;
import ru.inrtu.backend.dto.ActivityHistorySchoolchildrenInterface;
import ru.inrtu.backend.entity.Schoolchild;
import ru.inrtu.backend.repository.RoleRepository;
import ru.inrtu.backend.repository.SchoolchildRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс, в котором содержится логика для работы с сущностями "Школьник"
 */
@Service
public class SchoolchildService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(SchoolchildService.class);
    private SchoolchildRepository schoolchildRepository;
    private RoleRepository roleRepository;

    @Autowired
    public SchoolchildService(SchoolchildRepository schoolchildRepository, RoleRepository roleRepository){
        this.schoolchildRepository = schoolchildRepository;
        this.roleRepository = roleRepository;
    }

    public Schoolchild create(Schoolchild schoolchild) throws SchoolchildAlreadyExistException{
        Schoolchild existingSchoolchild = schoolchildRepository.findExistingInDB(schoolchild.getName(), schoolchild.getSurname(),
                schoolchild.getFatherName(), schoolchild.getEducationalClass());
        if (existingSchoolchild == null){
            return schoolchildRepository.saveAndFlush(schoolchild);
        }else{
            throw new SchoolchildAlreadyExistException();
        }
    }

    public List<SchoolchildrenStatusActivityDto>getRegisteredSchoolchildren(Long activityId){
        List<ActivityHistorySchoolchildrenInterface>schoolchildrenStatusActivityDtos = schoolchildRepository
                .findRegisteredSchoolchildren(activityId);
        List<SchoolchildrenStatusActivityDto> statusDtos = new ArrayList<>();
        schoolchildrenStatusActivityDtos.forEach(s -> statusDtos.add(interfaceToObject(s, false)));
        return statusDtos;
    }

    public List<SchoolchildrenStatusActivityDto>getAttendedSchoolchildren(Long activityId){
        List<ActivityHistorySchoolchildrenInterface>schoolchildrenStatusActivityDtos = schoolchildRepository
                .findAttendedSchoolchildren(activityId);
        List<SchoolchildrenStatusActivityDto> statusDtos = new ArrayList<>();
        schoolchildrenStatusActivityDtos.forEach(s -> statusDtos.add(interfaceToObject(s, true)));
        return statusDtos;
    }

    /**
     * Сравнение двух списков: школьники которые зарегистрированы на мероприятие и школьники которые посетили
     * мероприятие. Создание нового списка у объектов которого отмечается посетил ли школьник мероприятие или
     * только зарегистрировался.
     */
    private List<SchoolchildrenStatusActivityDto>markAttendedSchoolchildren(
            List<ActivityHistorySchoolchildrenInterface>registeredSchoolchildren,
            List<ActivityHistorySchoolchildrenInterface>attendedSchoolchildren){




        List<SchoolchildrenStatusActivityDto>resultSchoolchildren = new ArrayList<SchoolchildrenStatusActivityDto>();
        //Проходим по всем зарегистрированным школьникам и проверяем есть ли они в списке посетивших.
        for (ActivityHistorySchoolchildrenInterface schoolchild : registeredSchoolchildren){
            if (attendedSchoolchildren.contains(schoolchild)){
                resultSchoolchildren.add(interfaceToObject(schoolchild, true));
            }else{
                resultSchoolchildren.add(interfaceToObject(schoolchild, false));
            }
        }
        return resultSchoolchildren;
    }

    private SchoolchildrenStatusActivityDto interfaceToObject(ActivityHistorySchoolchildrenInterface activityHistorySchoolchildren,
                                                              boolean attendActivity){
        return new SchoolchildrenStatusActivityDto()
                .setId(activityHistorySchoolchildren.getId())
                .setName(activityHistorySchoolchildren.getName())
                .setSurname(activityHistorySchoolchildren.getSurname())
                .setFatherName(activityHistorySchoolchildren.getFatherName())
                .setEducationalClass(activityHistorySchoolchildren.getEducationalClass())
                .setEducationalOrganization(activityHistorySchoolchildren.getEducationalOrganization())
                .setEmail(activityHistorySchoolchildren.getEmail())
                .setPhoneNumber(activityHistorySchoolchildren.getPhoneNumber())
                .setAttendActivity(attendActivity);
    }

    public Schoolchild update(Schoolchild schoolchild){
        return schoolchildRepository.save(schoolchild);
    }

    public void delete(Long id){
        schoolchildRepository.deleteById(id);
    }

    public List<Schoolchild> getAll(){
        return schoolchildRepository.findAll();
    }

    public Schoolchild get(Long id){
        return schoolchildRepository.findById(id).orElseThrow();
    }

//    NEW

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("LOAD SCHOOLCHILD BY EMAIL: " + email);
        Schoolchild schoolchild = schoolchildRepository.findByEmail(email);
        if (schoolchild == null) {
            logger.info("USER NOT FOUND");
            throw new UsernameNotFoundException("User not found");
        }
        logger.info("USER FOUND SUCCESS: " + schoolchild);
        return schoolchild.getUserDetails();
    }

    public Schoolchild findUserByEmail(final String email) {

        final Schoolchild schoolchild = schoolchildRepository.findByEmail(email);
        if (schoolchild == null) {
            throw new UsernameNotFoundException("Email: " + email);
        }
        return schoolchild;
    }

    public Schoolchild findUserById(Long id) {
        Optional<Schoolchild> schoolchild = schoolchildRepository.findById(id);
        return schoolchild.orElse(new Schoolchild());
    }

    public List<Schoolchild> getAllUsers() {
        return schoolchildRepository.findAll();
    }

    public boolean saveUser(Schoolchild schoolchild, BCryptPasswordEncoder bCryptPasswordEncoder) {
        Schoolchild dbSchoolchild = schoolchildRepository.findByEmail(schoolchild.getEmail());
        if (dbSchoolchild != null) {
            logger.error(String.format("Пользователь: %s, уже существует", dbSchoolchild));
            return false;
        }
        schoolchild.getRoles()
                .add(roleRepository.findByName("USER").orElseThrow(() -> new ObjectNotFoundException("name", "USER")));
        schoolchild.setPassword(bCryptPasswordEncoder.encode(schoolchild.getPassword()));
        schoolchildRepository.save(schoolchild);
        logger.info(String.format("Пользователь: %s, сохранён успешно", schoolchild));
        return true;
    }

    public boolean deleteUser(Long id) {
        if (schoolchildRepository.findById(id).isPresent()) {
            schoolchildRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public void singUpUser(Schoolchild schoolchild, BCryptPasswordEncoder bCryptPasswordEncoder) {
        String encryptedPassword = bCryptPasswordEncoder.encode(schoolchild.getPassword());
        schoolchild.setPassword(encryptedPassword);
        Schoolchild createdUser = schoolchildRepository.save(schoolchild);
    }
}
