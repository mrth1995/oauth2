package com.daksa.oauth.domain;

import io.olivia.webutil.IDGen;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Entity
@Table(name = "account")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
		@NamedQuery(name = "Account.findByUsername", query = "SELECT a FROM Account a WHERE a.username = :username")
})
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column
	private String id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String username;
	@XmlTransient
	@Column(name = "password_hash", length = 64)
	private String passwordHash;
	@XmlTransient
	@Column(name = "password_salt", length = 64)
	private String passwordSalt;
	@Column(nullable = false)
	private BigDecimal amount;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AccountStatus status;

	public Account() {
		this.id = IDGen.generate();
		this.amount = BigDecimal.ZERO;
		this.status = AccountStatus.ACTIVE;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean verifyPassword(String password) {
		if (passwordHash != null && password != null) {
			return hashPassword(password).equals(passwordHash);
		} else {
			return false;
		}
	}

	private String hashPassword(String password) {
		try {
			byte[] saltBytes = Hex.decodeHex(passwordSalt.toCharArray());
			return hashPassword(password, saltBytes);
		} catch (DecoderException e) {
			throw new RuntimeException(e);
		}
	}

	private String hashPassword(String password, byte[] saltBytes) {
		try {
			byte[] passwordBytes = password.getBytes();
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(saltBytes);
			byte[] tokenHash = digest.digest(passwordBytes);
			int iterations = 2 - 1;
			for (int i = 0; i < iterations; i++) {
				digest.reset();
				tokenHash = digest.digest(tokenHash);
			}
			String tokenHashHex = Hex.encodeHexString(tokenHash);
			return tokenHashHex;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public final void setPassword(String password) {
		if (password != null) {
			Random random = new Random();
			byte[] saltBytes = new byte[16];
			random.nextBytes(saltBytes);

			this.passwordHash = hashPassword(password, saltBytes);
			this.passwordSalt = Hex.encodeHexString(saltBytes);
		} else {
			this.passwordHash = null;
			this.passwordSalt = null;
		}
	}
}
