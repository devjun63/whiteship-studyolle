package com.studyolle.account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc //컨트롤러 부터 밑 클래스까지 모두 테스트
class AccountControllerTest {

    @Autowired private MockMvc mockMvc;

    @DisplayName("회원 가입 화면 보이는지 테스트")
    @Test   //Junit5
    void signUpForm() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("account/sign-up"))
                .andExpect(model().attributeExists("signUpForm"));

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
}