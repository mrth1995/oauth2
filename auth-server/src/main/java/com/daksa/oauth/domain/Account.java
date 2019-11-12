package com.daksa.oauth.domain;

import io.olivia.webutil.IDGen;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column
	private String id;
	@Column(nullable = false)
	private String name;
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
}
