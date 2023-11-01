package br.com.acaboumony.account.service;

import br.com.acaboumony.account.dto.request.UserReqDTO;
import br.com.acaboumony.account.dto.response.UserResDTO;
import br.com.acaboumony.account.model.User;
import br.com.acaboumony.account.repository.UserRepository;
import br.com.acaboumony.util.GenericMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    private GenericMapper <UserReqDTO, User> userReqMapper;

    private GenericMapper <UserResDTO, User> userResMapper;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public UserResDTO detailUser(UUID userId){
        return userResMapper.mapModelToDto(userRepository.findById(userId).orElseThrow(NoSuchElementException::new), UserResDTO.class);
    }

    public String getEmailByUserId(UUID userId){
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        return user.getEmail();
    }
}
