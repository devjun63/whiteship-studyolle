package com.studyolle.account;

import com.studyolle.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;

    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);

        // signUpForm데이터를 받을 때 타입의 camelCase이름을 따라감
        // @Valid SignUpForm signUpForm(mapping)

        /*
        signUpSubmit에
        validate 검증을 한번 더 하는 것을 InitBinder로
        signUpSubmit에들어 왔을때 validate 검사를 하고
        signUpFormValidator.validate(signUpForm, errors);
        if(errors.hasErrors());
            return "account/sign-up";
        }
        */
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute(new SignUpForm()); // 객체 attribute의 이름이 된다.
        // camel Case 값 -> "signUpForm"문자열이 들어감
        return "account/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        // 복합 객체 ( 여러 값) -> ModelAttribute로 받아오는데 파라미터로 쓰일때 생략이 가능하다.
        // Data를 컨버젼하거나 binding할때 발생할 수 있는 에러를 받아주는 타입 -> Erros
        if(errors.hasErrors()){
            //SignUpForm JSR 303 Annotation에 정의한 값에 걸리면
            // Binding Errors errors에 담기고 에러가 있다고 조건문에 걸려서 form으로 돌아감
            return "account/sign-up";
        }

        accountService.processNewAccount(signUpForm);
        return "redirect:/";
    }


}
