package br.com.acaboumony.account.service;

import br.com.acaboumony.account.dto.request.UserLoginDTO;
import br.com.acaboumony.account.dto.request.UserReqDTO;
import br.com.acaboumony.account.dto.request.UserUpdateDTO;
import br.com.acaboumony.account.dto.response.UserResDTO;
import br.com.acaboumony.account.model.User;
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
    private final GenericMapper<UserReqDTO, User> userReqMapper;
    private final GenericMapper<User, UserResDTO> userResMapper;

    public UserService(UserRepository userRepository, GenericMapper<UserReqDTO, User> userReqMapper, GenericMapper<User, UserResDTO> userResMapper) {
        this.userRepository = userRepository;
        this.userReqMapper = userReqMapper;
        this.userResMapper = userResMapper;
    }

    public void createUser(UserReqDTO userReqDTO) {
        validateCpf(userReqDTO.getCpf());
        validateEmail(userReqDTO.getEmail());

        User user = new User();
        BeanUtils.copyProperties(userReqDTO, user);
        userRepository.save(userReqMapper.mapDtoToModel(userReqDTO, User.class));
    }

    public UUID login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        return user.getPassword().equals(password) ? user.getUserId() : null;
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
        User user = userRepository.findById(userUpdateDTO.userId()).orElseThrow(NoSuchElementException::new);

        if (userUpdateDTO.name() != null) {
            user.setName(userUpdateDTO.name());
        }
        if (userUpdateDTO.contact() != null) {
            user.setContact(userUpdateDTO.contact());
        }
        if (userUpdateDTO.email() != null) {
            user.setEmail(userUpdateDTO.email());
        }

    }

    public UserResDTO detailUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        return userResMapper.mapModelToDto(user, UserResDTO.class);
    }

    private void validateLogin(String password, User user){
        if(!user.getPassword().equals(password)){

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
