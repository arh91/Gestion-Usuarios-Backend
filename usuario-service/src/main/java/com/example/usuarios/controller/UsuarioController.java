package com.example.usuarios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import com.example.usuarios.service.UsuarioService;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
// @CrossOrigin(origins = "http://localhost:4200") // URL del frontend Angular
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    Usuario u = new Usuario();

    @PostMapping("/login")
    public String login(@RequestBody Usuario usuario) {
        return usuarioService.login(usuario);
    }

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/registro")
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

    @PostMapping("/enviar-codigo")
    public ResponseEntity<String> enviarCodigoRecuperacionContrasenha(@RequestParam String nick) {
        System.out.println("ENVIANDO CÓDIGO RECUPERACIÓN...");
        String codVerificacion = usuarioService.generarCodigoVerificacion();
        u.setCodigoVerificacion(codVerificacion);
        Optional<String> optionalMail = usuarioRepository.getMailByNick(nick);
        String mail = optionalMail.get();

        // Llamamos al método para enviar el correo de verificación
        try {
            usuarioService.enviarCodigoVerificacion(mail, codVerificacion);
        } catch (Exception e) {
            // Se registra el error y retorna un error HTTP 500
            System.out.println("Error al enviar el código: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se pudo enviar el código de verificación. Intente de nuevo.");
        }

        // Retornar una respuesta de éxito
        return ResponseEntity.status(HttpStatus.CREATED).body("Se ha enviado un mail a su cuenta de correo.");
    }

    @PostMapping("/autenticar")
    public ResponseEntity<Usuario> autenticarUsuario(@RequestParam String nick, @RequestParam String contraseña) {
        Usuario usuario = usuarioService.autenticarUsuario(nick, contraseña);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/cerrar-sesion")
    public ResponseEntity<Void> cerrarSesion(@RequestParam String nick) {
        usuarioService.actualizarEstadoConectado(nick, false);
        return ResponseEntity.ok().build();
    }

    // Endpoint para obtener la lista de todos los usuarios
    @GetMapping("/lista")
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioService.obtenerListaUsuarios();
    }

    // Endpoint para obtener un usuario por su ID
    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(@PathVariable String id) {
        return usuarioService.obtenerUsuarioPorId(id);
    }

    // Endpoint que devuelve el código de recuperación de contraseña enviado al
    // usuario por correo
    @GetMapping("/obtener-codigo")
    public String obtenerCodigoRecuperacion() {
        String codigo = u.getCodigoVerificacion();
        return codigo;
    }

    @GetMapping("/validarNick/{nick}")
    public ResponseEntity<Boolean> validarNick(@PathVariable String nick) {
        boolean exists = usuarioService.existsByNick(nick);
        return ResponseEntity.ok(exists);
    }

    // Endpoint para eliminar un usuario por su ID
    @DeleteMapping("/{id}")
    public void eliminarUsuarioPorId(@PathVariable String id) {
        usuarioService.eliminarUsuario(id);
    }

    // Endpoint para modificar un usuario por su ID
    @PutMapping("/{id}")
    public Usuario modificarUsuario(@PathVariable String id, @RequestBody Usuario usuarioModificado) {
        return usuarioService.modificarUsuario(id, usuarioModificado);
    }
}