package com.kyuwon.spring.domain.user.domain;

import com.kyuwon.spring.domain.user.model.Address;
import com.kyuwon.spring.domain.user.model.Authority;
import com.kyuwon.spring.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Table(name = "user_account", indexes = {
        @Index(columnList = "email")
})
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserAccount extends BaseEntity implements UserDetails
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter @Column(nullable = false, length = 50, name = "email") private String email;
    @Setter @Column(nullable = false, length = 200, name = "password") private String password;
    @Setter @Column(nullable = false, length = 30, name = "name") private String name;
    @Embedded @Column(length = 300, name = "address") private Address address;
    @JoinTable(name = "user_role") @ElementCollection @Column(nullable = false, length = 50, name = "authority") @Enumerated(EnumType.STRING) private Set<Authority> authority;
    @Setter @Column(length=300, name = "refresh_token") private String refreshToken;
    @Builder
    public UserAccount(String email, String password, String name, Address address, Set<Authority> authority) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.authority = authority;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authority.stream().map(r -> new SimpleGrantedAuthority("ROLE_"+ r.name())).collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return id.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}