package com.studyolle.account;

import com.studyolle.domain.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc //컨트롤러 부터 밑 클래스까지 모두 테스트
class AccountControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @MockBean
    JavaMailSender javaMailSender;

    @DisplayName("인증 메일 확인 - 입력값 오류")
    @Test   //Junit5
    void checkEmailToken_with_wrong_input() throws Exception {
        mockMvc.perform(get("/check-email-token")
        .param("token","ajioasdfj")
        .param("email","email@email.com"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("error"))
        .andExpect(view().name("account/checked-email"))
        .andExpect(unauthenticated());
    }

    @DisplayName("인증 메일 확인 - 입력값 정상")
    @Test   //Junit5
    void checkEmailToken() throws Exception {
        Account account = Account.builder()
                .email("test@email.com")
                .password("12345678")
                .nickname("devjun")
                .build();
        Account testAccount = accountRepository.save(account);
        testAccount.generateEmailCheckToken();

        mockMvc.perform(get("/check-email-token")
                .param("token",testAccount.getEmailCheckToken())
                .param("email",testAccount.getEmail()))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeExists("nickname"))
                .andExpect(model().attributeExists("numberOfUser"))
                .andExpect(view().name("account/checked-email"))
                .andExpect(authenticated().withUsername("devjun"));
    }


    @DisplayName("회원 가입 화면 보이는지 테스트")
    @Test   //Junit5
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"))
                .andExpect(unauthenticated());

        // 200이 나오는 것으로 테스트 되는 것
        // security가 적용이 되면 sign-up form에 접근할 때
        // Access Denied Exception이 발생
        // 이 exception이 발생한 것을 security Filter가 잡아서
        // form generation 과 함께 login form을 보여주게 되어있음
        // 고로 200이 나오면 view가 제공 된다고 볼 수 있음
        // 보고 싶으면 andDo(print())로 볼 수 있음
        // thymeLeaf라서 viewTempalte rendering을 실제 servlet container가 하지 않고
        // view 생성을 해서 응답을 보여줌
        // MockMvcTest로 해도 View까지 테스트 가능
        // SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
        // SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
        // 로 하면 servlet이 실제로 뜬다
        // 위 어노테이션으로 Servlet을 띄울때는
        // @AutoConfigureWebTestClient or AutoConfigureWebClient을 사용하겠지만
        // Thymeleaf를 사용해서 MockMvcTest로도 충분

    }

    @DisplayName("회원 가입 처리 -  입력값 오류")
    @Test
    void singUpSubmit_wrong_input() throws Exception {
        mockMvc.perform(post("/sign-up")
                .param("nickname", "jungi")
                .param("email", "email..")
                .param("password", "12345")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
    }

    @DisplayName("회원 가입 처리 -  입력값 정상")
    @Test
    void singUpSubmit_correct_input() throws Exception {
        mockMvc.perform(post("/sign-up")
                .param("nickname", "jungi")
                .param("email", "jungi@email.com")
                .param("password", "12345678")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"))
                .andExpect(authenticated().withUsername("jungi"));

        Account account = accountRepository.findByEmail("jungi@email.com");
        assertNotNull(account);
        assertNotEquals(account.getPassword(), "12345678");
        assertNotNull(account.getEmailCheckToken());
        then(javaMailSender).should().send(any(SimpleMailMessage.class));
    }

    // 메일 메시지 까지 보다는
    // Mokito BDDAssertion.then에 있는 javaMailSender가
}