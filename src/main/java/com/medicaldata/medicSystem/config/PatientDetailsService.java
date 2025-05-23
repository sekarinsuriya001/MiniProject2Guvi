package com.medicaldata.medicSystem.config;

import com.medicaldata.medicSystem.model.Patient;
import com.medicaldata.medicSystem.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientDetailsService implements UserDetailsService {
    @Autowired
    private PatientRepository patientRepository;
    public PatientDetailsService(){
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Patient> optionalPatient = patientRepository.findByMobile(username);
//        if(optionalPatient.isPresent()){
//            return new PatientDetails(optionalPatient.get()); // will need a PatientDetails class for this method
//       }
        if(optionalPatient.isPresent()){
            return org.springframework.security.core.userdetails.User.builder()
                    .username(optionalPatient.get().getMobile())
                    .password(optionalPatient.get().getPassword())
                    .roles("PATIENT")
                    .build();
        }
        throw new UsernameNotFoundException("Invalid username.");
    }
}