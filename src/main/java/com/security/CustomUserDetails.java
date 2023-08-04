package com.security;

import com.entity.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;
    private int userId;
    private int code;
    private String userName;
    @JsonIgnore
    private String password;
    private String email;
    private String phone;
    private String timeCheckin;
    private String timeCheckout;
    private boolean userStatus;
    private Collection<? extends GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return this.authorities;
    }
    //Tu thong tin user chuyen sang thong tin CustomUserDetails
    public static CustomUserDetails mapUserToUserDetail(Employee employee) {
        // Lay cac quyen tu doi tuong user
        List<GrantedAuthority> listAuthorities= employee.getListRoles().stream()
                .map(roles -> new SimpleGrantedAuthority(roles.getRoleName().name()))
                .collect(Collectors.toList());
        // Tra ve doi tuong CustomUserDetails
        return new CustomUserDetails(
                employee.getEmployeeId(),
                employee.getCode(),
                employee.getUserName(),
                employee.getPassword(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getTimeCheckin(),
                employee.getTimeCheckout(),
                employee.isEmployeeStatus(),
                listAuthorities
        );
    }
    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return this.password;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
}
