package com.hobbyjoin.ships.model.user;
import com.hobbyjoin.ships.Translator;
import com.hobbyjoin.ships.exception.InvalidTokenException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private TokenRepo tokenRepo;
    private MailService mailService;
    private AppUserRepo appUserRepo;
    private PasswordEncoder passwordEncoder;

    public UserService(AppUserRepo appUserRepo, PasswordEncoder passwordEncoder, TokenRepo tokenRepo, MailService mailService) {
        this.appUserRepo = appUserRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepo = tokenRepo;
        this.mailService = mailService;
    }

    public HashMap<String,String> getLoggedUserData() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = null;
        String role = null;
        String id = null;
        HashMap<String,String> userData = new HashMap<String,String>();
        if (principal instanceof AppUser) {
            username = ((AppUser) principal).getUsername();
            role = ((AppUser) principal).getRole();
            id = ((AppUser) principal).getId().toString();
            userData.put("id", id);
            userData.put("username", username);
            userData.put("role", role);
        }
        return userData;
    }


    public void addUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUser.setRole("ROLE_USER");
        appUserRepo.save(appUser);
        sendToken(appUser);
    }

    public String getValidationPasswordsError(AppUser appUser,String confirmPassword){
        String errorValue="";
        String translatedErrorValue="";
        if(appUser.getPassword().equals("") || confirmPassword.equals("")){
            errorValue="register.password.empty";
        }
        else{
            if(!(appUser.getPassword().equals(confirmPassword))){
            errorValue="register.password.different";
            }
        }
        if(errorValue.length()>0){
            Translator translator = Translator.getInstance();
            translator.setTranslatorHandler(LocaleContextHolder.getLocale().toString());
            translatedErrorValue=translator.translate(errorValue);
        }
        return translatedErrorValue;
    }

    private void sendToken(AppUser appUser) {
        String tokenValue = UUID.randomUUID().toString();
        Token token = new Token();
        token.setValue(tokenValue);
        token.setAppUser(appUser);
        tokenRepo.save(token);
        String url = "http://localhost:8080/token?value=" + tokenValue;
        //TODO do properties
        Translator translator = Translator.getInstance();
        translator.setTranslatorHandler(LocaleContextHolder.getLocale().toString());
        try {
            mailService.sendMail(appUser.getUsername(), translator.translate("ShipsApp_confirm_mail"), url, false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public AppUser getUserFromVerifyToken(String token) {
        final Optional<Token> byValue = tokenRepo.findByValue(token);
        if(byValue.isEmpty()){
            throw new InvalidTokenException();
        }
        final AppUser appUser = byValue.get().getAppUser();
        return appUser;
    }

    public void setUserEnabled(AppUser appUser) {
        appUser.setEnabled(true);
        appUserRepo.save(appUser);
    }
}