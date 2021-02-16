package com.studyolle.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
// bean과 bean들만 의존성 주입 받을 수 있기 때문에 component어노테이션
@RequiredArgsConstructor
// lombok -> private fianl에 해당하는 member variable의 생성사를 만들어줌
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;    // bean

    /*
    @RequiredArgsConstructor ->
    롬복이 Annotation으로 만들어 줌 어떤 bean이 생성자가 하나만 있고
    그 생성자가 받는 parameter들이 bean으로 등록이 되어있다면
    spring 4.2이후 부터는 자동으로 bean을 주입해주기 때문에
    autowired나 inject같은 어노테이션을 쓰지않아도 의존성 주입이 된다.
    public SignUpFormValidator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }*/

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        // TODO email, nickname 중복 여부 검사
        SignUpForm signUpForm = (SignUpForm)errors;
        if(accountRepository.existsByEmail(signUpForm.getEmail())){
            errors.rejectValue("email","invalid.email", new Object[]{signUpForm.getEmail()}, "이미 사용중인 이메일입니다.");
            // message 소스는 이번에 다루지 않고 국제화 서비스를 가정하고 메시지 다국화를 다루면서
            // 그 때 다룰 예정
            if(accountRepository.existsByNickName(signUpForm.getNickname())){
                errors.rejectValue("nickname","invalid.nickname", new Object[]{signUpForm.getNickname()}, "이미 사용중인 닉네임입니다.");
            }
        }
    }
}
