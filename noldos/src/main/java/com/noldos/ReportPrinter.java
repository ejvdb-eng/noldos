package com.noldos;

import java.util.List;
import java.lang.StringBuilder;

public class ReportPrinter {

    public String format(TotalContract report, List<Finding> findings) {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("contract report");
        stringbuilder.append(System.lineSeparator());
        stringbuilder.append(report.provider());
        stringbuilder.append(System.lineSeparator());
        stringbuilder.append(report.criticalOrImportant());
        stringbuilder.append(System.lineSeparator());

        for (Finding finding : findings) {
            stringbuilder.append(finding.status())
                         .append(" - ")
                         .append(finding.requirement())
                         .append(" (Clause ")
                         .append(finding.clauseNumbers())
                         .append("): ")
                         .append(finding.message())
                         .append(System.lineSeparator());
        }

        int totalAmountOfFoundWarnings = findings.size();
        stringbuilder.append("Total findings: ").append(totalAmountOfFoundWarnings);

        return stringbuilder.toString();
    }
}


