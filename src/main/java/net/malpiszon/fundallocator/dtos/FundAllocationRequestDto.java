package net.malpiszon.fundallocator.dtos;

import java.math.BigInteger;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.malpiszon.fundallocator.models.InvestmentType;

public class FundAllocationRequestDto {

    @Min(1)
    private BigInteger amount;
    @NotNull
    private InvestmentType type;
    @NotNull
    @Size(min = 1)
    private List<Long> fund;

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public InvestmentType getType() {
        return type;
    }

    public void setType(InvestmentType type) {
        this.type = type;
    }

    public List<Long> getFund() {
        return fund;
    }

    public void setFund(List<Long> fund) {
        this.fund = fund;
    }
}
