package com.example.demo.domain.user.entity;

import com.example.demo.domain.BaseEntity;
import com.example.demo.domain.enums.UserRole;
import com.example.demo.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users",
       indexes = {
           @Index(name = "idx_email", columnList = "email"),
           @Index(name = "idx_nickname", columnList = "nickname")
       },
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_email", columnNames = "email"),
           @UniqueConstraint(name = "uk_nickname", columnNames = "nickname")
       })
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(length = 1000)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @Version
    private Long version;  // 낙관적 락

    @Builder
    public User(String email, String password, String nickname, String profileImage) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    // 프로필 정보 수정
    public void updateProfile(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    // 비밀번호 변경
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // 계정 상태 변경
    public void changeStatus(UserStatus status) {
        this.status = status;
    }

    // 권한 변경
    public void changeRole(UserRole role) {
        this.role = role;
    }

    // 계정 활성화 여부 확인
    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    // 관리자 여부 확인
    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    // 계정 삭제
    public void delete() {
        this.status = UserStatus.DELETED;
    }

    // 계정 차단
    public void block() {
        this.status = UserStatus.BLOCKED;
    }
}
