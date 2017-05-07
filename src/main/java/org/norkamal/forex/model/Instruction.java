package org.norkamal.forex.model;

import org.norkamal.forex.utils.CurrencyUtil;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Currency;

/**
 * The Instruction class represents a foreign exchange instruction.
 *
 * @author Norkamal Mimun
 * @version 1.0, May 2017
 */
public class Instruction {

    private String entity;
    private Operation operation;
    private BigDecimal agreedFx;
    private Currency currency;
    private LocalDate instructionDate;
    private LocalDate settlementDate;
    private int units;
    private BigDecimal pricePerUnit;

    public Instruction(InstructionBuilder builder) {

        this.entity = builder.entity;
        this.operation = builder.operation;
        this.agreedFx = builder.agreedFx;
        this.currency = builder.currency;
        this.instructionDate = builder.instructionDate;
        this.settlementDate = builder.settlementDate;
        this.units = builder.units;
        this.pricePerUnit = builder.pricePerUnit;
    }

    /**
     * @return the effective settlement date
     */
    public LocalDate getEffectiveSettlementDate() {
        LocalDate effectiveSettlementDate = getSettlementDate();

        while (isSettlementDateOnHoliday(effectiveSettlementDate)) {
            effectiveSettlementDate = effectiveSettlementDate.plusDays(1);
        }

        return effectiveSettlementDate;
    }

    /**
     * @param localDate date to check if falls on a holiday
     * @return true if it falls on a holiday, false otherwise
     */
    private boolean isSettlementDateOnHoliday(LocalDate localDate) {

        LocalDate settlementDate = localDate;
        if (CurrencyUtil.isTradingFromSundayToThursday(this.getCurrency())) {
            return (settlementDate.getDayOfWeek() == DayOfWeek.FRIDAY
                    || settlementDate.getDayOfWeek() == DayOfWeek.SATURDAY);

        } else {
            return (settlementDate.getDayOfWeek() == DayOfWeek.SATURDAY
                    || settlementDate.getDayOfWeek() == DayOfWeek.SUNDAY);
        }
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public BigDecimal getAgreedFx() {
        return agreedFx;
    }

    public void setAgreedFx(BigDecimal agreedFx) {
        this.agreedFx = agreedFx;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDate getInstrucionDate() {
        return instructionDate;
    }

    public void setInstrucionDate(LocalDate instrucionDate) {
        this.instructionDate = instrucionDate;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    /**
     * The InstructionBuilder inner class helps implement the builder pattern for Instruction class
     * @see Instruction
     *
     * @author Norkamal Mimun
     * @version 1.0, May 2017
     */
    public static class InstructionBuilder {

        private String entity;
        private Operation operation;
        private BigDecimal agreedFx;
        private Currency currency;
        private LocalDate instructionDate;
        private LocalDate settlementDate;
        private int units;
        private BigDecimal pricePerUnit;

        public InstructionBuilder(){}

        public InstructionBuilder withEntity(String entity){
            this.entity = entity;
            return this;
        }

        public InstructionBuilder withOperation(Operation operation){
            this.operation = operation;
            return this;
        }

        public InstructionBuilder withAgreeFx(BigDecimal agreedFx){
            this.agreedFx = agreedFx;
            return this;
        }

        public InstructionBuilder withCurrency(Currency currency){
            this.currency = currency;
            return this;
        }

        public InstructionBuilder withInstructionDate(LocalDate instrucionDate){
            this.instructionDate = instrucionDate;
            return this;
        }

        public InstructionBuilder withSettlementDate(LocalDate settlementDate){
            this.settlementDate = settlementDate;
            return this;
        }

        public InstructionBuilder withUnits(int units){
            this.units = units;
            return this;
        }

        public InstructionBuilder withPricePerUnits(BigDecimal pricePerUnit){
            this.pricePerUnit = pricePerUnit;
            return this;
        }

        public Instruction build(){
            return new Instruction(this);
        }

    }
}
