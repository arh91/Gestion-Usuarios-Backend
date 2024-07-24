package com.example.usuarios.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario crearUsuario(Usuario usuario) {
        usuario.setFecRegistro(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    public Usuario obtenerUsuarioPorId(String nick) {
        Optional<Usuario> usuario = usuarioRepository.findById(nick);
        // Verificamos si el usuario existe
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con nick: " + nick);
        } else {
            return usuario.get();
        }
    }

    public List<Usuario> obtenerListaUsuarios() {
        return usuarioRepository.findAll();
    }

    public void eliminarUsuario(String nick) {
        if (!usuarioRepository.existsById(nick)) {
            throw new RuntimeException("Usuario no encontrado con nick: " + nick);
            // return;
        }
        usuarioRepository.deleteById(nick);
    }

    public Usuario modificarUsuario(String nick, Usuario nuevoUsuario) {
        // Buscamos el usuario existente por su nick
        Usuario antiguoUsuario = usuarioRepository.findById(nick)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con nick: " + nick));

        // Actualizamos los datos del usuario existente con los nuevos datos
        antiguoUsuario.setNick(nuevoUsuario.getNick());
        antiguoUsuario.setContraseña(nuevoUsuario.getContraseña());
        antiguoUsuario.setEmail(nuevoUsuario.getEmail());
        antiguoUsuario.setNumTelefono(nuevoUsuario.getNumTelefono());

        // Guardamos el usuario modificado en la base de datos
        return usuarioRepository.save(antiguoUsuario);
    }

    public boolean existsByNick(String nick) {
        return usuarioRepository.existsByNick(nick);
    }

    public Usuario autenticarUsuario(String nick, String contraseña) {
        Usuario usuario = usuarioRepository.findByNickAndPassword(nick, contraseña);
        if (usuario != null) {
            usuarioRepository.actualizarEstadoConectado(nick, true);
        }
        return usuario;
    }

    public void actualizarEstadoConectado(String nick, boolean conectado) {
        usuarioRepository.actualizarEstadoConectado(nick, conectado);
    }

    public String login(Usuario usuario) {
        // Lógica de autenticación (solo como ejemplo)
        if (usuario.getNick().equals("alvaro91") && usuario.getContraseña().equals("1991")) {
            return "token_de_acceso_generado";
        } else {
            return null;
        }
    }
}