package ru.inrtu.backend.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class Schoolchild {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private String fatherName;
    private String educationalOrganization;
    private String educationalClass;
    private String email;
    private String phoneNumber;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "schoolchild_roles", joinColumns = @JoinColumn(name = "schoolchild_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public UserDetails getUserDetails() {
        return org.springframework.security.core.userdetails.User.builder()
                .password(this.password)
                .username(this.email)
                .authorities(this.roles)
                .build();
    }

    public boolean isAdmin() {
        if (this.getRoles() != null) {
            return this.roles.stream().anyMatch(role -> role.getName().equals("ADMIN"));
        }
        return false;
    }

    @Override
    public String toString() {
        return "Schoolchild{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", educationalOrganization='" + educationalOrganization + '\'' +
                ", educationalClass='" + educationalClass + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
