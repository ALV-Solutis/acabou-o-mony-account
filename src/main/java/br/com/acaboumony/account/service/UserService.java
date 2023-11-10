package br.com.acaboumony.account.service;

import br.com.acaboumony.account.dto.request.UserReqDTO;
import br.com.acaboumony.account.dto.request.UserUpdateDTO;
import br.com.acaboumony.account.dto.response.UserResDTO;
import br.com.acaboumony.account.model.Users;
import br.com.acaboumony.account.repository.UserRepository;
import br.com.acaboumony.util.GenericMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GenericMapper<UserReqDTO, Users> userReqMapper;
    private final GenericMapper<Users, UserResDTO> userResMapper;

    public UserService(UserRepository userRepository, GenericMapper<UserReqDTO, Users> userReqMapper, GenericMapper<Users, UserResDTO> userResMapper) {
        this.userRepository = userRepository;
        this.userReqMapper = userReqMapper;
        this.userResMapper = userResMapper;
    }

    public void createUser(UserReqDTO userReqDTO) {
        validateCpf(userReqDTO.getCpf());
        validateEmail(userReqDTO.getEmail());

        Users users = new Users();
        BeanUtils.copyProperties(userReqDTO, users);
        userRepository.save(userReqMapper.mapDtoToModel(userReqDTO, Users.class));
    }

    public UUID login(String email, String password) {
        Users users = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        return users.getPassword().equals(password) ? users.getUserId() : null;
    }

    public List<UserResDTO> listUsers() {
        return userResMapper.listToList(userRepository.findAll(), UserResDTO.class);
    }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        validateEmail(userUpdateDTO.email());
        Users users = userRepository.findById(userUpdateDTO.userId()).orElseThrow(NoSuchElementException::new);

        if (userUpdateDTO.name() != null) {
            users.setName(userUpdateDTO.name());
        }
        if (userUpdateDTO.contact() != null) {
            users.setContact(userUpdateDTO.contact());
        }
        if (userUpdateDTO.email() != null) {
            users.setEmail(userUpdateDTO.email());
        }

    }

    public UserResDTO detailUser(UUID userId) {
        Users users = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        return userResMapper.mapModelToDto(users, UserResDTO.class);
    }

    private void validateLogin(String password, Users users){
        if(!users.getPassword().equals(password)){

        }
    }

    private void validateEmail(String email) {
        List<String> emailsSaved = userRepository.findEmails();

        emailsSaved.stream().forEach(existingEMail ->{
            if (existingEMail.equals(email) ) {
                throw new EntityExistsException("Email ja está em uso");
            }});
    }
    private void validateCpf(String cpf){
        List<String> cpfsSaved = userRepository.findCpfs();

        cpfsSaved.stream().forEach(existingCpf ->{
            if (existingCpf.equals(cpf)) {
                throw new EntityExistsException("Cpf Já está em uso");
            }});

    }
}
