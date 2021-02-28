package com.natwest.transfer.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "account")
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {

	private static final long serialVersionUID = 6715391253040516051L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "number", nullable = false, length = 50)
	private Integer number;

	@Column(name = "balance", nullable = false, precision = 12, scale = 2)
	private BigDecimal balance;

	@Version
	@ColumnDefault(value = "1")
	private Long version;

	@JsonBackReference
	@OneToMany(mappedBy = "fromAccount", fetch = FetchType.LAZY)
	public List<Transaction> transactionsFrom;

	@JsonBackReference
	@OneToMany(mappedBy = "toAccount", fetch = FetchType.LAZY)
	public List<Transaction> transactionsTo;

}
