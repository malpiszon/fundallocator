package net.malpiszon.fundallocator.services.impls;

import java.math.BigInteger;
import java.util.List;

import net.malpiszon.fundallocator.dtos.FundAllocationsDto;
import net.malpiszon.fundallocator.models.Fund;
import net.malpiszon.fundallocator.models.InvestmentType;
import net.malpiszon.fundallocator.repositories.FundRepository;
import net.malpiszon.fundallocator.services.IAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Returns allocation of given amount of money and strategy type into given funds.
 */
@Service
public class AllocationService implements IAllocationService {

    @Autowired
    FundRepository fundRepository;

    @Override
    public FundAllocationsDto getAllocation(BigInteger amount, InvestmentType investmentType, List<Long> fundIds) {
        FundAllocationsDto fundAllocations = new FundAllocationsDto();

        investmentType.getFundsTypePercentages().forEach(
                (k, v) -> addAllocationForFundType(fundAllocations, amount, v,
                        fundRepository.findByFundTypeAndIdIn(k, fundIds))
        );

        if (amount.subtract(fundAllocations.getCurrentAllocation()).compareTo(BigInteger.ZERO) > 0) {
            fundAllocations.setNotAllocated(amount.subtract(fundAllocations.getCurrentAllocation()));
        }

        return fundAllocations;
    }

    private void addAllocationForFundType(FundAllocationsDto fundAllocations, BigInteger amount, Integer percent,
                                          List<Fund> funds) {
        if (funds.isEmpty()) {
            return;
        }
        BigInteger amountForFundType = amount.multiply(BigInteger.valueOf(percent))
                .divide(BigInteger.valueOf(100));
        if (amountForFundType.compareTo(BigInteger.ZERO) <= 0) {
            return;
        }

        BigInteger amountForSingleFund = amountForFundType.divide(BigInteger.valueOf(funds.size()));
        BigInteger amountToShare = amountForFundType
                .subtract(amountForSingleFund.multiply(BigInteger.valueOf(funds.size())));

        for (Fund fund : funds) {
            amountToShare = addAllocationForFund(fundAllocations, amount, amountForSingleFund, amountToShare, fund);
        }
    }

    private BigInteger addAllocationForFund(FundAllocationsDto fundAllocations, BigInteger amount,
                                            BigInteger amountForSingleFund, BigInteger amountToShare, Fund fund) {
        BigInteger amountForFund = amountForSingleFund;
        if (amountToShare.compareTo(BigInteger.ZERO) > 0) {
            amountForFund = amountForFund.add(amountToShare);
        }
        if (amountForFund.compareTo(BigInteger.ZERO) <= 0) {
            return BigInteger.ZERO;
        }
        double percentForFund = amountForFund.doubleValue() / amount.doubleValue();
        fundAllocations.addAllocation(fund, amountForFund, percentForFund);

        return BigInteger.ZERO;
    }
}
