package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasIDandEmail;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

@Component
public class EmailCustomValidator implements Validator {

    UserRepository userRepository;

    public EmailCustomValidator(@Qualifier("dataJpaUserRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return HasIDandEmail.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        HasIDandEmail user = (HasIDandEmail) o;
        User userDB = userRepository.getByEmail(user.getEmail().toLowerCase());

        if (userDB != null && !userDB.getId().equals(user.getId())) {
            errors.rejectValue("email", "Already exists");
        }


    }
}
