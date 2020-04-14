package com.mfirsov.bank_account_generator.model;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractBankAccount {
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String patronymic;
    private long accountNumber;

    public AbstractBankAccount(UUID uuid, String firstName, String lastName, String patronymic, long accountNumber) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.accountNumber = accountNumber;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBankAccount that = (AbstractBankAccount) o;
        return accountNumber == that.accountNumber &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(patronymic, that.patronymic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, firstName, lastName, patronymic, accountNumber);
    }

    @Override
    public String toString() {
        return "AbstractBankAccount{" +
                "uuid=" + uuid +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", accountNumber=" + accountNumber +
                '}';
    }
}
