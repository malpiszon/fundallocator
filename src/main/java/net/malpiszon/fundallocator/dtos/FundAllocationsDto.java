package net.malpiszon.fundallocator.dtos;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.List;

import com.google.common.collect.Lists;
import net.malpiszon.fundallocator.models.Fund;

public class FundAllocationsDto {
    private final List<FundAllocation> allocations;
    private BigInteger notAllocated;
    private int currentNo;
    private BigInteger currentAllocation;

    public FundAllocationsDto() {
        allocations = Lists.newArrayList();
        notAllocated = BigInteger.ZERO;
        currentNo = 0;
        currentAllocation = BigInteger.ZERO;
    }

    public void addAllocation(Fund fund, BigInteger allocation, double percent) {
        currentNo++;
        this.currentAllocation = currentAllocation.add(allocation);
        this.allocations.add(new FundAllocation(currentNo, fund, allocation, percent));
    }

    public void setNotAllocated(BigInteger notAllocated) {
        this.notAllocated = notAllocated;
    }

    public List<FundAllocation> getAllocations() {
        return allocations;
    }

    public BigInteger getNotAllocated() {
        return notAllocated;
    }

    public BigInteger getCurrentAllocation() {
        return currentAllocation;
    }

    public static class FundAllocation {
        private final int lp;
        private final Fund fund;
        private final BigInteger allocation;
        private final double percent;
        private final DecimalFormat df = new DecimalFormat("##.##%");

        public FundAllocation(int lp, Fund fund, BigInteger allocation, double percent) {
            this.lp = lp;
            this.fund = fund;
            this.allocation = allocation;
            this.percent = percent;
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
}
