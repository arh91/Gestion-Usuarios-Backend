package com.example.usuarios.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.example.usuarios.model.Usuario;
import com.example.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public String cadCodigo = "";

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

    // Método que genera un código aleatorio de 6 digitos
    public String generarCodigoVerificacion() {
        SecureRandom secureRandom = new SecureRandom();
        int codigo = 100000 + secureRandom.nextInt(900000);
        cadCodigo = String.valueOf(codigo);
        return cadCodigo;
    }

    // Método para enviar correo con un código para generar nueva contraseña
    public void enviarCodigoVerificacion(String email, String codigo) throws MessagingException {

        String asunto = "Verifica tu correo electrónico";
        String mensaje = "Hola, hemos recibido una solicitud para acceder a tu cuenta de ARH Store a través de tu dirección de correo electrónico: "
                + "Tu código de verificación de ARH Store es: " + codigo;

        // Configuración del servidor de correo (puede variar según tu proveedor)
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true"); // Activar STARTTLS
        propiedades.put("mail.smtp.host", "smtp.gmail.com"); // Servidor SMTP de Gmail
        propiedades.put("mail.smtp.port", "587"); // Puerto SMTP para STARTTLS
        propiedades.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Confiar en el servidor SSL

        // Autenticación del correo (usuario y contraseña)
        Session session = Session.getInstance(propiedades, new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                // Se obtiene desde variables de entorno o properties
                return new javax.mail.PasswordAuthentication(System.getenv("EMAIL_ADDRESS"),
                        System.getenv("EMAIL_PASSWORD"));
            }
        });

        // Crear el mensaje
        Message mensajeCorreo = new MimeMessage(session);
        mensajeCorreo.setFrom(new InternetAddress(System.getenv("EMAIL_ADDRESS"))); // Emisor
        mensajeCorreo.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); // Receptor
        mensajeCorreo.setSubject(asunto);
        mensajeCorreo.setText(mensaje);

        // Enviar el correo
        javax.mail.Transport.send(mensajeCorreo);
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