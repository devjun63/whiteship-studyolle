package com.studyolle.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {

    // ---- 로그인 ----

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)  // 로그인 시 유니크 해야 하는 컬럼
    private String email;

    @Column(unique = true)  // 로그인 시 유니크 해야 하는 컬럼
    private String nickname;

    private String password;

    private boolean emailVerified;  // 이메일 인증 절차 -> 이메일 인증 여부 검토하는 컬럼

    private String emailCheckToken; // 이메일 검증시 필요한 토큰값

    private LocalDateTime joinedAt; // 검증 후 가입날짜 설정을 위한 컬럼


    // ---- 프로필 ------
    private String bio; // 자기소개

    private String url; // 웹사이트 url

    private String occupation;  // 직업

    private String location;  // 지역 varchar(255)

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String profileImage;    // 프로필 이미지

    // varchar(255)보다 클 수 있음 -> Lob을 이용해 text타입으로 매칭
    // fetch타입이 그때 그때 바뀔 수 있기 때문에 명시적으로 선언

    // ---- 알림 설정 ----

    private boolean studyCreatedByEmail;

    private boolean studyCreateByWeb;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdateByWeb;
}


