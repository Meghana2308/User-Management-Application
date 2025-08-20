package com.springboot.usermanagementsystemapplication.repository;

import com.springboot.usermanagementsystemapplication.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
