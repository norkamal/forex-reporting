package org.norkamal.forex.reporting;

import org.norkamal.forex.model.Instruction;
import org.norkamal.forex.model.Operation;
import org.norkamal.forex.utils.CurrencyUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * The ReportingManager class represents the interface to produce the different reports
 *
 * @author Norkamal Mimun
 * @version 1.0, May 2017
 */
public class ReportingManager {

    public static final int OPERATION_SCALE = 3;
    public static final int RESULT_SCALE = 2;


    /**
     * Prints in the standard output the result of the different reports:
     * Incoming amount settled per day report
     * Outgoing amount settled per day report
     * Entities incoming ranking report
     * Entities outgoing ranking report
     *
     * @param instructions list of instructions to report against
     */
    public static void printGlobalForexReport(final List<Instruction> instructions) {

           printAmountSettledPerDayReport(instructions, Operation.SELL);

           printAmountSettledPerDayReport(instructions, Operation.BUY);

           printEntitiesRankingReport(instructions, Operation.SELL);

           printEntitiesRankingReport(instructions, Operation.BUY);
    }

    /**
     * Prints the incoming or outgoing amounts settled per day report
     *
     * @param instructions list of instructions to report against
     * @param operation    incoming or outgoing
     */
    public static void printAmountSettledPerDayReport(
            final List<Instruction> instructions, Operation operation) {

        System.out.println("\nAmount in USD settled per day for " + operation.toString() + " instructions:");

        if(validInputs(instructions, operation)) {

            Map<LocalDate, BigDecimal> inAmountSettledPerDay = generateAmountSettledPerDayData(instructions, operation);

            inAmountSettledPerDay.forEach(
                    (k, v) ->
                            System.out.println("Day: " + k.toString() + ", Amount: " + v.toString() + " " + CurrencyUtil.USD));
        }

    }

    /**
     * Prints the incoming or outgoing entities ranking report
     *
     * @param instructions list of instructions to report against
     * @param operation    incoming or outgoing
     */
    public static void printEntitiesRankingReport(
            final List<Instruction> instructions, Operation operation) {

        System.out.println("\nRanking of entities in descending order by amount instructed to " + operation.toString() + " instructions:");

        if(validInputs(instructions, operation)) {

            Map<String, BigDecimal> orderedEntitiesRanking = generateEntitiesRankingData(instructions, operation);

            orderedEntitiesRanking.forEach((k, v) -> System.out.printf("%-15s, %-10s.\n", "Entity: " + k, "Amount: " + v + " USD"));
        }
    }


    private static Map<LocalDate, BigDecimal> generateAmountSettledPerDayData(final List<Instruction> instructions, Operation operation) {

        Map<LocalDate, BigDecimal> inAmountSettledPerDay = new TreeMap<>();

        for (Instruction instruction : instructions) {

            if (instruction.getOperation() == operation) {
                updateSumCurrentAmount(inAmountSettledPerDay, instruction);
            }
        }

        return inAmountSettledPerDay;
    }

    private static Map<String, BigDecimal> generateEntitiesRankingData(final List<Instruction> instructions, Operation operation) {

        Map<String, BigDecimal> entitiesRanking = new HashMap<>();
        Map<String, BigDecimal> orderedEntitiesRanking = new LinkedHashMap<>();

        for (Instruction instruction : instructions) {

            if (instruction.getOperation() == operation)
                updateEntityCurrentAmount(entitiesRanking, instruction);
        }

        entitiesRanking.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .forEachOrdered(x -> orderedEntitiesRanking.put(x.getKey(), x.getValue()));

        return orderedEntitiesRanking;
    }


    private static void updateSumCurrentAmount(
            Map<LocalDate, BigDecimal> inAmountSettledByDate, Instruction instruction) {

        BigDecimal currentValue = inAmountSettledByDate.get(instruction.getEffectiveSettlementDate());
        BigDecimal total =
                instruction
                        .getAgreedFx()
                        .multiply(instruction.getPricePerUnit())
                        .setScale(OPERATION_SCALE, BigDecimal.ROUND_HALF_EVEN);
        total =
                total
                        .multiply(new BigDecimal(instruction.getUnits()))
                        .setScale(OPERATION_SCALE, BigDecimal.ROUND_HALF_EVEN);

        if (currentValue == null) {
            inAmountSettledByDate.put(instruction.getEffectiveSettlementDate(), total.setScale(RESULT_SCALE, BigDecimal.ROUND_HALF_EVEN));
        } else {
            inAmountSettledByDate.put(
                    instruction.getSettlementDate(),
                    total.add(currentValue).setScale(RESULT_SCALE, BigDecimal.ROUND_HALF_EVEN));
        }
    }

    private static void updateEntityCurrentAmount(
            Map<String, BigDecimal> entitiesRanking, Instruction instruction) {

        BigDecimal currentAmount = entitiesRanking.get(instruction.getEntity());
        BigDecimal total =
                instruction
                        .getAgreedFx()
                        .multiply(instruction.getPricePerUnit())
                        .setScale(OPERATION_SCALE, BigDecimal.ROUND_HALF_EVEN);
        total =
                total
                        .multiply(new BigDecimal(instruction.getUnits()))
                        .setScale(OPERATION_SCALE, BigDecimal.ROUND_HALF_EVEN);

        if (currentAmount == null) {
            entitiesRanking.put(instruction.getEntity(), total.setScale(RESULT_SCALE, BigDecimal.ROUND_HALF_EVEN));
        } else {
            entitiesRanking.put(
                    instruction.getEntity(),
                    total.add(currentAmount).setScale(RESULT_SCALE, BigDecimal.ROUND_HALF_EVEN));
        }
    }

    private static boolean validInputs(final List<Instruction> instructions, Operation operation){

        return (instructions!=null && !instructions.isEmpty() && operation!=null);
    }
}
