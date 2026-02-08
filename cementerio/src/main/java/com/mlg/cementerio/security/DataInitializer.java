package com.mlg.cementerio.security;

import com.mlg.cementerio.entity.Rol;
import com.mlg.cementerio.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;

    public DataInitializer(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public void run(String... args) {
        if (rolRepository.findByNombre("USUARIO").isEmpty()) {
            rolRepository.save(new Rol("USUARIO"));
        }
        if (rolRepository.findByNombre("ADMIN").isEmpty()) {
            rolRepository.save(new Rol("ADMIN"));
        }
    }
}
