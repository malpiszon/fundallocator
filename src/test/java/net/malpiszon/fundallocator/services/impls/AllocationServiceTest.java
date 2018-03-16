package net.malpiszon.fundallocator.services.impls;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.malpiszon.fundallocator.dtos.FundAllocationsDto;
import net.malpiszon.fundallocator.models.Fund;
import net.malpiszon.fundallocator.models.FundType;
import net.malpiszon.fundallocator.models.InvestmentType;
import net.malpiszon.fundallocator.repositories.FundRepository;
import net.malpiszon.fundallocator.services.IAllocationService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AllocationServiceTest {

    @TestConfiguration
    static class AllocationServiceContextConfiguration {

        @Bean
        public IAllocationService consumptionService() {
            return new AllocationService();
        }
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private IAllocationService allocationService;

    @MockBean
    private FundRepository fundRepository;

    private final Comparator<FundAllocationsDto.FundAllocation> fundAllocationComparator = (o1, o2) -> {
        int allocationCompareResult = o1.getAllocation().compareTo(o2.getAllocation());
        if (allocationCompareResult != 0) {
            return allocationCompareResult;
        }
        return o1.getFundName().compareTo(o2.getFundName());
    };

    private final BigInteger amount = BigInteger.valueOf(10000);
    private final List<Long> polishFundsList = Lists.newArrayList(1L, 2L, 3L);
    private final List<Fund> polishFunds = Lists.newArrayList(
            new Fund(1L, FundType.POLISH, "PL1"),
            new Fund(2L, FundType.POLISH, "PL2"),
            new Fund(3L, FundType.POLISH, "PL3")
    );
    private final List<Long> foreignFundsList = Lists.newArrayList(4L, 5L);
    private final List<Fund> foreignFunds = Lists.newArrayList(
            new Fund(4L, FundType.FOREIGN, "FO1"),
            new Fund(5L, FundType.FOREIGN, "FO2")
    );

    private final List<Long> financialFundsList = Lists.newArrayList(6L);
    private final List<Fund> financialFunds = Lists.newArrayList(
            new Fund(6L, FundType.FINANCIAL, "FI1")
    );
    private List<Long> allFundsList;

    @Before
    public void setUp() {
        allFundsList = Lists.newArrayList(polishFundsList);
        allFundsList.addAll(foreignFundsList);
        allFundsList.addAll(financialFundsList);

        Mockito.when(fundRepository.findByFundTypeAndIdIn(FundType.POLISH, polishFundsList)).thenReturn(polishFunds);
        Mockito.when(fundRepository.findByFundTypeAndIdIn(FundType.POLISH, allFundsList)).thenReturn(polishFunds);
        Mockito.when(fundRepository.findByFundTypeAndIdIn(FundType.FOREIGN, allFundsList)).thenReturn(foreignFunds);
        Mockito.when(fundRepository.findByFundTypeAndIdIn(FundType.FINANCIAL, allFundsList)).thenReturn(financialFunds);
    }

    @Test
    public void testGetAllocation_withSafeTypeAndOnlyPolishFunds_returnsValidData() {
        FundAllocationsDto result = allocationService.getAllocation(amount, InvestmentType.SAFE, polishFundsList);

        Mockito.verify(fundRepository, Mockito.times(3))
                .findByFundTypeAndIdIn(Mockito.any(FundType.class), Mockito.eq(polishFundsList));
        assertThat("Expected valid number of funds in result", result.getAllocations().size(), is(3));
        assertEquals("Expected proper allocation for 1st fund", BigInteger.valueOf(668),
                result.getAllocations().get(0).getAllocation());
        assertEquals("Expected proper allocation for 2nd fund", BigInteger.valueOf(666),
                result.getAllocations().get(1).getAllocation());
        assertEquals("Expected proper allocation for 3rd fund", BigInteger.valueOf(666),
                result.getAllocations().get(2).getAllocation());
        assertEquals("Expected proper percent for 1st fund", "6,68%",
                result.getAllocations().get(0).getPercent());
        assertEquals("Expected proper percent for 2nd fund", "6,66%",
                result.getAllocations().get(1).getPercent());
        assertEquals("Expected proper value of non-allocated amount", BigInteger.valueOf(8000),
                result.getNotAllocated());
    }

    @Test
    public void testGetAllocation_withSafeTypeAndOnlyPolishFundsAndSmallAmount_returnsValidData() {
        FundAllocationsDto result = allocationService.getAllocation(BigInteger.valueOf(5), InvestmentType.SAFE,
                polishFundsList);

        Mockito.verify(fundRepository, Mockito.times(3))
                .findByFundTypeAndIdIn(Mockito.any(FundType.class), Mockito.eq(polishFundsList));
        assertThat("Expected valid number of funds in result", result.getAllocations().size(), is(1));
        assertEquals("Expected proper allocation for 1st fund", BigInteger.valueOf(1),
                result.getAllocations().get(0).getAllocation());
        assertEquals("Expected proper percent for 1st fund", "20%",
                result.getAllocations().get(0).getPercent());
        assertEquals("Expected proper value of non-allocated amount", BigInteger.valueOf(4),
                result.getNotAllocated());
    }

    @Test
    public void testGetAllocation_withSafeTypeAndOnlyPolishFundsAndVerySmallAmount_returnsValidData() {
        FundAllocationsDto result = allocationService.getAllocation(BigInteger.valueOf(3), InvestmentType.SAFE,
                polishFundsList);

        Mockito.verify(fundRepository, Mockito.times(3))
                .findByFundTypeAndIdIn(Mockito.any(FundType.class), Mockito.eq(polishFundsList));
        assertThat("Expected valid number of funds in result", result.getAllocations().size(), is(0));
        assertEquals("Expected proper value of non-allocated amount", BigInteger.valueOf(3),
                result.getNotAllocated());
    }

    @Test
    public void testGetAllocation_withSafeTypeAndAllFunds_returnsValidData() {
        FundAllocationsDto result = allocationService.getAllocation(amount, InvestmentType.SAFE, allFundsList);
        result.getAllocations().sort(fundAllocationComparator);

        Mockito.verify(fundRepository, Mockito.times(3))
                .findByFundTypeAndIdIn(Mockito.any(FundType.class), Mockito.eq(allFundsList));
        assertThat("Expected valid number of funds in result", result.getAllocations().size(), is(6));

        assertEquals("Expected proper allocation for 1st fund", BigInteger.valueOf(500),
                result.getAllocations().get(0).getAllocation());
        assertEquals("Expected proper allocation for 2nd fund", BigInteger.valueOf(666),
                result.getAllocations().get(1).getAllocation());
        assertEquals("Expected proper allocation for 3rd fund", BigInteger.valueOf(666),
                result.getAllocations().get(2).getAllocation());
        assertEquals("Expected proper allocation for 4th fund", BigInteger.valueOf(668),
                result.getAllocations().get(3).getAllocation());
        assertEquals("Expected proper allocation for 5th fund", BigInteger.valueOf(3750),
                result.getAllocations().get(4).getAllocation());
        assertEquals("Expected proper allocation for 6th fund", BigInteger.valueOf(3750),
                result.getAllocations().get(5).getAllocation());
        assertEquals("Expected proper type for 1st fund", FundType.FINANCIAL.toString(),
                result.getAllocations().get(0).getFundType());
        assertEquals("Expected proper type for 2nd fund", FundType.POLISH.toString(),
                result.getAllocations().get(1).getFundType());
        assertEquals("Expected proper type for 3rd fund", FundType.POLISH.toString(),
                result.getAllocations().get(2).getFundType());
        assertEquals("Expected proper type for 4th fund", FundType.POLISH.toString(),
                result.getAllocations().get(3).getFundType());
        assertEquals("Expected proper type for 5th fund", FundType.FOREIGN.toString(),
                result.getAllocations().get(4).getFundType());
        assertEquals("Expected proper type for 6th fund", FundType.FOREIGN.toString(),
                result.getAllocations().get(5).getFundType());
        assertEquals("Expected proper value of non-allocated amount", result.getNotAllocated(),
                BigInteger.valueOf(0));
    }

    @Test
    public void testGetAllocation_withAggressiveTypeAndAllFunds_returnsValidData() {
        FundAllocationsDto result = allocationService.getAllocation(amount, InvestmentType.AGGRESSIVE, allFundsList);
        result.getAllocations().sort(fundAllocationComparator);

        Mockito.verify(fundRepository, Mockito.times(3))
                .findByFundTypeAndIdIn(Mockito.any(FundType.class), Mockito.eq(allFundsList));
        assertThat("Expected valid number of funds in result", result.getAllocations().size(), is(6));

        assertEquals("Expected proper allocation for 1st fund", BigInteger.valueOf(1000),
                result.getAllocations().get(0).getAllocation());
        assertEquals("Expected proper allocation for 2nd fund", BigInteger.valueOf(1000),
                result.getAllocations().get(1).getAllocation());
        assertEquals("Expected proper allocation for 3rd fund", BigInteger.valueOf(1333),
                result.getAllocations().get(2).getAllocation());
        assertEquals("Expected proper allocation for 4th fund", BigInteger.valueOf(1333),
                result.getAllocations().get(3).getAllocation());
        assertEquals("Expected proper allocation for 5th fund", BigInteger.valueOf(1334),
                result.getAllocations().get(4).getAllocation());
        assertEquals("Expected proper allocation for 6th fund", BigInteger.valueOf(4000),
                result.getAllocations().get(5).getAllocation());
        assertEquals("Expected proper type for 1st fund", FundType.FOREIGN.toString(),
                result.getAllocations().get(0).getFundType());
        assertEquals("Expected proper type for 2nd fund", FundType.FOREIGN.toString(),
                result.getAllocations().get(1).getFundType());
        assertEquals("Expected proper type for 3rd fund", FundType.POLISH.toString(),
                result.getAllocations().get(2).getFundType());
        assertEquals("Expected proper type for 4th fund", FundType.POLISH.toString(),
                result.getAllocations().get(3).getFundType());
        assertEquals("Expected proper type for 5th fund", FundType.POLISH.toString(),
                result.getAllocations().get(4).getFundType());
        assertEquals("Expected proper type for 6th fund", FundType.FINANCIAL.toString(),
                result.getAllocations().get(5).getFundType());
        assertEquals("Expected proper value of non-allocated amount", result.getNotAllocated(),
                BigInteger.valueOf(0));
    }

    @Test
    public void testGetAllocation_withBalancedTypeAndAllFunds_returnsValidData() {
        FundAllocationsDto result = allocationService.getAllocation(amount, InvestmentType.BALANCED, allFundsList);
        result.getAllocations().sort(fundAllocationComparator);

        Mockito.verify(fundRepository, Mockito.times(3))
                .findByFundTypeAndIdIn(Mockito.any(FundType.class), Mockito.eq(allFundsList));
        assertThat("Expected valid number of funds in result", result.getAllocations().size(), is(6));

        assertEquals("Expected proper allocation for 1st fund", BigInteger.valueOf(1000),
                result.getAllocations().get(0).getAllocation());
        assertEquals("Expected proper allocation for 2nd fund", BigInteger.valueOf(1000),
                result.getAllocations().get(1).getAllocation());
        assertEquals("Expected proper allocation for 3rd fund", BigInteger.valueOf(1000),
                result.getAllocations().get(2).getAllocation());
        assertEquals("Expected proper allocation for 4th fund", BigInteger.valueOf(1000),
                result.getAllocations().get(3).getAllocation());
        assertEquals("Expected proper allocation for 5th fund", BigInteger.valueOf(3000),
                result.getAllocations().get(4).getAllocation());
        assertEquals("Expected proper allocation for 6th fund", BigInteger.valueOf(3000),
                result.getAllocations().get(5).getAllocation());
        assertEquals("Expected proper type for 1st fund", FundType.FINANCIAL.toString(),
                result.getAllocations().get(0).getFundType());
        assertEquals("Expected proper type for 2nd fund", FundType.POLISH.toString(),
                result.getAllocations().get(1).getFundType());
        assertEquals("Expected proper type for 3rd fund", FundType.POLISH.toString(),
                result.getAllocations().get(2).getFundType());
        assertEquals("Expected proper type for 4th fund", FundType.POLISH.toString(),
                result.getAllocations().get(3).getFundType());
        assertEquals("Expected proper type for 5th fund", FundType.FOREIGN.toString(),
                result.getAllocations().get(4).getFundType());
        assertEquals("Expected proper type for 6th fund", FundType.FOREIGN.toString(),
                result.getAllocations().get(5).getFundType());
        assertEquals("Expected proper value of non-allocated amount", result.getNotAllocated(),
                BigInteger.valueOf(0));
    }

    @Test
    public void testGetAllocation_withSafeTypeAndAllFundsAndNonAllocatableAmount_returnsValidData() {
        FundAllocationsDto result = allocationService.getAllocation(BigInteger.valueOf(10001),
                InvestmentType.SAFE, allFundsList);
        result.getAllocations().sort(fundAllocationComparator);

        Mockito.verify(fundRepository, Mockito.times(3))
                .findByFundTypeAndIdIn(Mockito.any(FundType.class), Mockito.eq(allFundsList));
        assertThat("Expected valid number of funds in result", result.getAllocations().size(), is(6));

        assertEquals("Expected proper allocation for 1st fund", BigInteger.valueOf(500),
                result.getAllocations().get(0).getAllocation());
        assertEquals("Expected proper allocation for 2nd fund", BigInteger.valueOf(666),
                result.getAllocations().get(1).getAllocation());
        assertEquals("Expected proper allocation for 3rd fund", BigInteger.valueOf(666),
                result.getAllocations().get(2).getAllocation());
        assertEquals("Expected proper allocation for 4th fund", BigInteger.valueOf(668),
                result.getAllocations().get(3).getAllocation());
        assertEquals("Expected proper allocation for 5th fund", BigInteger.valueOf(3750),
                result.getAllocations().get(4).getAllocation());
        assertEquals("Expected proper allocation for 6th fund", BigInteger.valueOf(3750),
                result.getAllocations().get(5).getAllocation());
        assertEquals("Expected proper type for 1st fund", FundType.FINANCIAL.toString(),
                result.getAllocations().get(0).getFundType());
        assertEquals("Expected proper type for 2nd fund", FundType.POLISH.toString(),
                result.getAllocations().get(1).getFundType());
        assertEquals("Expected proper type for 3rd fund", FundType.POLISH.toString(),
                result.getAllocations().get(2).getFundType());
        assertEquals("Expected proper type for 4th fund", FundType.POLISH.toString(),
                result.getAllocations().get(3).getFundType());
        assertEquals("Expected proper type for 5th fund", FundType.FOREIGN.toString(),
                result.getAllocations().get(4).getFundType());
        assertEquals("Expected proper type for 6th fund", FundType.FOREIGN.toString(),
                result.getAllocations().get(5).getFundType());
        assertEquals("Expected proper value of non-allocated amount", result.getNotAllocated(),
                BigInteger.valueOf(1));
    }
}