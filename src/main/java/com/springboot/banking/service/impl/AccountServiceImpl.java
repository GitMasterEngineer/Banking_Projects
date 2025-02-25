package com.springboot.banking.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.springboot.banking.dto.AccountDto;
import com.springboot.banking.entity.Account;
import com.springboot.banking.mapper.AccountMapper;
import com.springboot.banking.repository.AccountRepositiory;
import com.springboot.banking.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	private AccountRepositiory accountRepositiory;

	public AccountServiceImpl(AccountRepositiory accountRepositiory) {
		this.accountRepositiory = accountRepositiory;
	}

	@Override
	public AccountDto createAccount(AccountDto accountDto) {

		Account account = AccountMapper.mapToAccount(accountDto);
		Account saveAccount = accountRepositiory.save(account);

		return AccountMapper.mapToAccountDto(saveAccount);
	}

	@Override
	public AccountDto getAccountById(Long id) {
		Account account = accountRepositiory.findById(id)
				.orElseThrow(() -> new RuntimeException("Account does not exist"));

		return AccountMapper.mapToAccountDto(account);
	}

	@Override
	public AccountDto deposit(Long id, double ammount) {
		Account account = accountRepositiory.findById(id)
				.orElseThrow(() -> new RuntimeException("Account does not exist"));

		double total = account.getBalance() + ammount;
		account.setBalance(total);
		Account saveAccount = accountRepositiory.save(account);
		return AccountMapper.mapToAccountDto(saveAccount);
	}

	@Override
	public AccountDto withdraw(Long id, double amount) {
		Account account = accountRepositiory.findById(id)
				.orElseThrow(() -> new RuntimeException("Account does not exist"));

		if (account.getBalance() < amount) {
			throw new RuntimeException("Insufficient amount");
		}

		double total = account.getBalance() - amount;
		account.setBalance(total);
		Account saveAccount = accountRepositiory.save(account);
		return AccountMapper.mapToAccountDto(saveAccount);
	}

	@Override
	public List<AccountDto> getAllAccounts() {
		List<Account> accounts = accountRepositiory.findAll();
		return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account)).collect(Collectors.toList());
	}

	@Override
	public void deleteAccounts(Long id) {
		accountRepositiory.findById(id).orElseThrow(()-> new RuntimeException("Account does not exist"));
		accountRepositiory.deleteById(id);
	}

}
