package org.norkamal.forex.demo;

import org.norkamal.forex.data.SampleDataProducer;
import org.norkamal.forex.reporting.ReportingManager;

/**
 * The ReportingDemo is a class that allows to illustrate the reporting functionality in action.
 * The result will be a report printed in the system standard output
 *
 * @author Norkamal Mimun
 * @version 1.0, May 2017
 */
public class ReportingDemo {

    public static void main(String[] args) {

        ReportingManager.printGlobalForexReport(SampleDataProducer.getListWithInstructions());
    }
}
