package br.com.acaboumony.account.service;

import br.com.acaboumony.account.dto.request.UserReqDTO;
import br.com.acaboumony.account.dto.request.UserUpdateDTO;
import br.com.acaboumony.account.dto.response.UserResDTO;
import br.com.acaboumony.account.model.User;
import br.com.acaboumony.account.repository.UserRepository;
import br.com.acaboumony.util.GenericMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final GenericMapper <UserReqDTO, User> userReqMapper;

    private final GenericMapper <UserResDTO, User> userResMapper;

    public UserService(UserRepository userRepository, GenericMapper<UserReqDTO, User> userReqMapper, GenericMapper<UserResDTO, User> userResMapper) {
        this.userRepository = userRepository;
        this.userReqMapper = userReqMapper;
        this.userResMapper = userResMapper;
    }

    public void createUser(UserReqDTO userReqDTO){
        userRepository.save(userReqMapper.mapDtoToModel(userReqDTO, User.class));
    }

    public List<UserResDTO> listUsers(){
        return userReqMapper.listToList(userRepository.findAll(), UserResDTO.class);
    }

    public void deleteUser(UUID userId){
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(UserUpdateDTO userUpdateDTO) {
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

    public UserResDTO detailUser(UUID userId){
        return userResMapper.mapModelToDto(userRepository.findById(userId).orElseThrow(NoSuchElementException::new), UserResDTO.class);
    }

    public String getEmailByUserId(UUID userId){
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        return user.getEmail();
    }
}
