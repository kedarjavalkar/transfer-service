package com.natwest.transfer.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.natwest.transfer.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

	@Lock(LockModeType.PESSIMISTIC_READ)
	@Query("SELECT a FROM Account a WHERE a.number=:number")
	Account getByNumber(Integer number);

}
