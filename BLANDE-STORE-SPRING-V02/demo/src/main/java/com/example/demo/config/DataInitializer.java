package com.example.demo.config;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminUsername = "admin1";
        String adminPassword = "admin1234";

        Usuario adminUser = usuarioRepository.findByUsername(adminUsername);

        if (adminUser != null) {
            // Actualizar la contraseña del admin existente a una versión hasheada
            String hashedPassword = passwordEncoder.encode(adminPassword);
            
            // Solo actualizar si la contraseña actual no está ya hasheada
            if (!passwordEncoder.matches(adminPassword, adminUser.getPassword())) {
                adminUser.setPassword(hashedPassword);
                usuarioRepository.save(adminUser);
                System.out.println("***********************************************************");
                System.out.println("Contraseña del usuario '" + adminUsername + "' actualizada exitosamente.");
                System.out.println("Ya puedes iniciar sesión con la nueva contraseña.");
                System.out.println("***********************************************************");
            }
        } else {
            // Opcional: Crear el usuario admin si no existe
            System.out.println("***********************************************************");
            System.out.println("Usuario admin '" + adminUsername + "' no encontrado. Creando uno nuevo.");
            Usuario newAdmin = new Usuario();
            newAdmin.setUsername(adminUsername);
            newAdmin.setPassword(passwordEncoder.encode(adminPassword));
            newAdmin.setRol("admin");
            usuarioRepository.save(newAdmin);
            System.out.println("Usuario '" + adminUsername + "' creado con la contraseña especificada.");
            System.out.println("***********************************************************");
        }
    }
}
