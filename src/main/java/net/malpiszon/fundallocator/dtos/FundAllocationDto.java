package net.malpiszon.fundallocator.dtos;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import net.malpiszon.fundallocator.models.Fund;

public class FundAllocationDto {
    private int lp;
    private final Fund fund;
    private final BigInteger allocation;
    private final double percent;
    private final DecimalFormat df;

    public FundAllocationDto(Fund fund, BigInteger allocation, double percent) {
        this.fund = fund;
        this.allocation = allocation;
        this.percent = percent;

        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
        formatSymbols.setDecimalSeparator(',');
        formatSymbols.setGroupingSeparator(' ');
        df = new DecimalFormat("##.##%", formatSymbols);
    }

    public void setLp(int lp) {
        this.lp = lp;
    }

    public int getLp() {
        return lp;
    }

    public String getFundType() {
        return fund.getFundType().toString();
    }

    public String getFundName() {
        return fund.getName();
    }

    public BigInteger getAllocation() {
        return allocation;
    }

    public String getPercent() {
        return df.format(this.percent);
    }
}
