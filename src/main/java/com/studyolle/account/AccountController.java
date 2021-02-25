package com.studyolle.account;

import com.studyolle.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;


@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    /*
     아키텍처 디자인에 따라 컨트롤러에서 repository 사용을 금지하기도 하지만
     이 강의에서는 Account와 동일한 도메인으로서 사용한다.
     But Controller나 Service를 Repository 혹은 Domain entity에서 참조하지는 않는다.
     */


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

        Account account = accountService.processNewAccount(signUpForm);
        accountService.login(account);
        return "redirect:/";
    }

    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";

        if (account == null){
            model.addAttribute("error","wrong email");
            return view;
        }
        if(!account.isValidToken(token)){
            model.addAttribute("error","wrong email");
            return view;
        }

        account.completeSignup();
        accountService.login(account);
        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());
        return view;
    }
}
