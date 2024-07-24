package com.example.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.example.usuarios.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    @Query("SELECT u FROM Usuario u WHERE u.nick = :nick AND u.contraseña = :password")
    Usuario findByNickAndPassword(@Param("nick") String nick, @Param("password") String password);

    @Transactional
    @Modifying
    @Query("UPDATE Usuario u SET u.conectado = :conectado WHERE u.nick = :nick")
    void actualizarEstadoConectado(@Param("nick") String nick, @Param("conectado") boolean conectado);

    boolean existsByNick(String nick);
}