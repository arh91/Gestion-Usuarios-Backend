package com.example.usuarios.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

import java.time.LocalDateTime;

import javax.persistence.Column;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "Usuarios")
public class Usuario {

	@Id
	@Column(name = "nbUser")
	@JsonProperty("nick")
	private String nick;

	@Column(name = "pwdUser")
	@JsonProperty("password")
	private String contraseña;

	@Column(name = "mailUser")
	@JsonProperty("mail")
	private String email;

	@Column(name = "phoneUser")
	@JsonProperty("telefono")
	private int numTelefono;

	@Column(name = "regUser")
	@JsonProperty("fechaRegistro")
	private LocalDateTime fecRegistro;

	@Column(name = "connected")
	@JsonProperty("conectado")
	private boolean conectado;

	public Usuario() {
		super();
	}

	public Usuario(String nick, String contraseña, String email, int numTelefono, LocalDateTime fecRegistro,
			boolean conectado) {
		this.nick = nick;
		this.contraseña = contraseña;
		this.email = email;
		this.numTelefono = numTelefono;
		this.fecRegistro = fecRegistro;
		this.conectado = conectado;
	}

	// Getters and Setters

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getNumTelefono() {
		return numTelefono;
	}

	public void setNumTelefono(int numTelefono) {
		this.numTelefono = numTelefono;
	}

	public boolean getConectado() {
		return conectado;
	}

	public boolean isConectado() {
		return conectado;
	}

	public void setConectado(boolean conectado) {
		this.conectado = conectado;
	}

	public void setFecRegistro(LocalDateTime fecRegistro) {
		this.fecRegistro = fecRegistro;
	}

	public LocalDateTime getFecRegistro() {
		return fecRegistro;
	}

}
