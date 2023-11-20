package br.com.acaboumony.account.service;

import br.com.acaboumony.account.dto.request.UserReqDTO;
import br.com.acaboumony.account.dto.request.UserUpdateDTO;
import br.com.acaboumony.account.dto.response.UserResDTO;
import br.com.acaboumony.account.model.UserAuth;
import br.com.acaboumony.account.model.Users;
import br.com.acaboumony.account.repository.UserAuthRepository;
import br.com.acaboumony.account.repository.UserRepository;
import br.com.acaboumony.util.GenericMapper;
import br.com.acaboumony.util.SecurityUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GenericMapper<UserReqDTO, Users> userReqMapper;
    private final GenericMapper<Users, UserResDTO> userResMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserAuthRepository userAuthRepository;


    public void createUser(UserReqDTO userReqDTO) {
        validateCpf(userReqDTO.getCpf());
        validateEmail(userReqDTO.getEmail());

        userReqDTO.setPassword(passwordEncoder.encode(userReqDTO.getPassword()));
        Users users = new Users();
        BeanUtils.copyProperties(userReqDTO, users);
        userRepository.save(userReqMapper.mapDtoToModel(userReqDTO, Users.class));
    }

    public String login(String email, String password) {
        Users user = userRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);

        if (passwordEncoder.matches(password,user.getPassword())){
            return generateOTP(user.getUserId());
        }else {
            throw new NotAuthorizedException("Usuario ou senha inválidos");
        }
    }

    public Page<UserResDTO> listUsers(Pageable pageable) {
        return userResMapper.listToList(userRepository.findAll(pageable), UserResDTO.class);
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

    private void validateEmail(String email) {
        List<String> emailsSaved = userRepository.findEmails();

        emailsSaved.forEach(existingEMail ->{
            if (existingEMail.equals(email) ) {
                throw new EntityExistsException("Email ja está em uso");
            }});
    }
    private void validateCpf(String cpf){
        List<String> cpfsSaved = userRepository.findCpfs();

        cpfsSaved.forEach(existingCpf ->{
            if (existingCpf.equals(cpf)) {
                throw new EntityExistsException("Cpf Já está em uso");
            }});

    }

    private String generateOTP(UUID userId){
        UserAuth userAuth = userAuthRepository.findUserAuthByUserIdAndIsUsedIsFalse(userId);
        String otpCode;
        if(userAuth != null){
            otpCode = userAuth.getOtpCode();
        }else {
            otpCode = SecurityUtil.generateOtpCode();
            userAuthRepository.save(new UserAuth(userId,otpCode,false));
        }

        return otpCode;
    }

    public Boolean validateOTP(String otpCode){

        return userAuthRepository.findUserAuthByOtpCodeAndIsUsedIsFalse(otpCode) != null;
    }

    public UserAuth getOtpEntity(String otpCode) {
        return userAuthRepository.findUserAuthByOtpCodeAndIsUsedIsFalse(otpCode);
    }
}
