package net.malpiszon.fundallocator.dtos;

import java.math.BigInteger;
import java.util.List;

import com.google.common.collect.Lists;

public class FundAllocationsDto {
    private final List<FundAllocationDto> allocations;
    private BigInteger notAllocated;
    private int currentNo;
    private BigInteger currentAllocation;

    public FundAllocationsDto() {
        allocations = Lists.newArrayList();
        notAllocated = BigInteger.ZERO;
        currentNo = 0;
        currentAllocation = BigInteger.ZERO;
    }

    public void addAllocations(List<FundAllocationDto> fundAllocations) {
        for (FundAllocationDto fundAllocation : fundAllocations) {
            fundAllocation.setLp(++currentNo);
            this.currentAllocation = this.currentAllocation.add(fundAllocation.getAllocation());
            this.allocations.add(fundAllocation);
        }
    }

    public void setNotAllocated(BigInteger notAllocated) {
        this.notAllocated = notAllocated;
    }

    public List<FundAllocationDto> getAllocations() {
        return allocations;
    }

    public BigInteger getNotAllocated() {
        return notAllocated;
    }

    public BigInteger getCurrentAllocation() {
        return currentAllocation;
    }
}
