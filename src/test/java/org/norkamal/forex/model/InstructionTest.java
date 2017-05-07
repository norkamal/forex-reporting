package org.norkamal.forex.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.Month;
import java.util.Currency;

/**
 * The InstructionTest class implements the unit tests for Instruction API
 *
 * @author Norkamal Mimun
 * @version 1.0, May 2017
 */
public class InstructionTest {

    Instruction firstInstruction, secondInstruction, thirdInstruction;

    LocalDate expectedFirstInstEffectiveSettlementDate, expectedSecondInstEffectiveSettlementDate, expectedThirdInstEffectiveSettlementDate;

    @Before
    public void setUp() {
    }

    @Test
    public void Should_Return_EffectiveSettlementDate_When_DealingWithSundayToThursdayTradingCurrency() {

        expectedFirstInstEffectiveSettlementDate = LocalDate.of(2016, Month.JUNE, 19);
        expectedSecondInstEffectiveSettlementDate = LocalDate.of(2016, Month.AUGUST, 21);
        expectedThirdInstEffectiveSettlementDate = LocalDate.of(2016, Month.SEPTEMBER, 15);

        firstInstruction = getInstruction(Currency.getInstance("AED"), LocalDate.of(2016, Month.JUNE, 17));
        secondInstruction = getInstruction(Currency.getInstance("SAR"), LocalDate.of(2016, Month.AUGUST, 20));
        thirdInstruction = getInstruction(Currency.getInstance("AED"), LocalDate.of(2016, Month.SEPTEMBER, 15));

        Assert.assertEquals(expectedFirstInstEffectiveSettlementDate, firstInstruction.getEffectiveSettlementDate());
        Assert.assertEquals(expectedSecondInstEffectiveSettlementDate, secondInstruction.getEffectiveSettlementDate());
        Assert.assertEquals(expectedThirdInstEffectiveSettlementDate, thirdInstruction.getEffectiveSettlementDate());
    }


    @Test
    public void Should_Return_EffectiveSettlementDate_When_DealingWithMondayToFridayTradingCurrency() {

        expectedFirstInstEffectiveSettlementDate = LocalDate.of(2016, Month.JUNE, 17);
        expectedSecondInstEffectiveSettlementDate = LocalDate.of(2016, Month.OCTOBER, 14);
        expectedThirdInstEffectiveSettlementDate = LocalDate.of(2016, Month.NOVEMBER, 28);

        firstInstruction = getInstruction(Currency.getInstance("USD"), LocalDate.of(2016, Month.JUNE, 17));
        secondInstruction = getInstruction(Currency.getInstance("GBP"), LocalDate.of(2016, Month.OCTOBER, 14));
        thirdInstruction = getInstruction(Currency.getInstance("EUR"), LocalDate.of(2016, Month.NOVEMBER, 26));

        Assert.assertEquals(expectedFirstInstEffectiveSettlementDate, firstInstruction.getEffectiveSettlementDate());
        Assert.assertEquals(expectedSecondInstEffectiveSettlementDate, secondInstruction.getEffectiveSettlementDate());
        Assert.assertEquals(expectedThirdInstEffectiveSettlementDate, thirdInstruction.getEffectiveSettlementDate());
    }


    @After
    public void tearDown() {
    }

    private Instruction getInstruction(Currency currency, LocalDate settlementDate) {

        MathContext MATH_CTX = new MathContext(10);

        Instruction instruction =
                new Instruction.InstructionBuilder()
                        .withEntity("foo")
                        .withOperation(Operation.BUY)
                        .withAgreeFx(new BigDecimal(0.50, MATH_CTX).setScale(2, BigDecimal.ROUND_HALF_EVEN))
                        .withCurrency(currency)
                        .withInstructionDate(LocalDate.of(2016, Month.JANUARY, 1))
                        .withSettlementDate(settlementDate)
                        .withUnits(200)
                        .withPricePerUnits(
                                new BigDecimal(100.25, MATH_CTX).setScale(2, BigDecimal.ROUND_HALF_EVEN))
                        .build();

        return instruction;
    }
}