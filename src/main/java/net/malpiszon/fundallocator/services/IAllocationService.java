package net.malpiszon.fundallocator.services;

import java.math.BigInteger;
import java.util.List;

import net.malpiszon.fundallocator.dtos.FundAllocationsDto;
import net.malpiszon.fundallocator.models.InvestmentType;

public interface IAllocationService {
    FundAllocationsDto getAllocation(BigInteger amount, InvestmentType investmentType, List<Long> fundIds);
}
