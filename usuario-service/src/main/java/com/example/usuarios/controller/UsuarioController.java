package com.example.usuarios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.usuarios.service.UsuarioService;
import com.example.usuarios.model.Usuario;

@RestController
@RequestMapping("/usuarios")
// @CrossOrigin(origins = "http://localhost:4200") // URL del frontend Angular
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public String login(@RequestBody Usuario usuario) {
        return usuarioService.login(usuario);
    }

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/registro")
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

    @GetMapping("/validarNick/{nick}")
    public ResponseEntity<Boolean> validarNick(@PathVariable String nick) {
        boolean exists = usuarioService.existsByNick(nick);
        return ResponseEntity.ok(exists);
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
