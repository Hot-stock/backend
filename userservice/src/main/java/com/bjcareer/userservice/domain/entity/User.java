package com.bjcareer.userservice.domain.entity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public static final String SHA_256 = "SHA-256";

    @Id
    @Column(name = "user_id")
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;  // Store the hashed password

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RoleAssignments> assignments = new ArrayList<>();

    public User(String email, String password) {
        this.email = email;
        this.password = hashPassword(password);  // Hash the password directly
        this.userType = UserType.NORMAL;
    }

    // Verify the password by re-hashing it and comparing it with the stored hash
    public boolean verifyPassword(String password)  {
        String hashedPassword = hashPassword(password);
        return this.password.equals(hashedPassword);
    }

    // Hash the password using MessageDigest with SHA-256
    private static String hashPassword(String password)  {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256);
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new NoHashFuctionException("No such hash function: " + SHA_256);
        }
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
            ", version=" + version +
            '}';
    }
}
