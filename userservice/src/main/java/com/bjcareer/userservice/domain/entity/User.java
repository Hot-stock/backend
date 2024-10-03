package com.bjcareer.userservice.domain.entity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.bjcareer.userservice.domain.entity.exceptions.NoHashFuctionException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "client")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    public static final String HASH_ALGORITHM = "SHA-256";
    public static final int SALT_SIZE = 16;

    @Id
    @Column(name = "user_id")
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    private String salt;  // Store the hashed password
    private String password;  // Store the hashed password

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RoleAssignments> assignments = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserActive> userActives = new ArrayList<>();

    @Version
    private Long version;

    public User(String email, String password) {
        this.email = email;
        this.salt = generateSalt();  // Generate a random salt
        this.password = hashPassword(password, salt);  // Hash the password directly
        this.userType = UserType.NORMAL;
    }

    // Verify the password by re-hashing it and comparing it with the stored hash
    public boolean verifyPassword(String password)  {
        String hashedPassword = hashPassword(password, this.salt);
        return this.password.equals(hashedPassword);
    }

    // 비밀번호와 salt를 사용해 해시값 생성
    private String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            String passwordWithSalt = password + salt;
            byte[] hashBytes = digest.digest(passwordWithSalt.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new NoHashFuctionException("No such hash function: " + HASH_ALGORITHM);
        }
    }

    // 16바이트 salt 생성
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Retrieve the user's roles from their assignments
    public List<RoleType> getRoles() {
        List<RoleType> roles = new ArrayList<>();
        assignments.stream().forEach(assignment -> roles.add(assignment.getRole().getType()));
        return roles;
    }

    @Override
    public String toString() {
        return "User{" +
            "id='" + id + '\'' +
            ", alias='" + email + '\'' +
            ", userType=" + userType +
            '}';
    }
}
