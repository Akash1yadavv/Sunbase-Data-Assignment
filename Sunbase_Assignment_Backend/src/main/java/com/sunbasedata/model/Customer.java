package com.sunbasedata.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Entity
public class Customer implements UserDetails{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer custId;
    private String uuid;
	private String firstName;
	private String lastName;
	private String street;
	private String city;
	private String state;
	private String phone;
	private String email;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private String address;
	
	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
	private Role role;

	@Override
	public String getUsername() {
		return email;
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

	public Customer setRole(Role role) {
	    this.role = role;
	    return this;
	
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getName().toString());
	    return List.of(authority);
	}
	
	
}