package net.malpiszon.fundallocator.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigInteger;
import java.util.List;

import net.malpiszon.fundallocator.dtos.FundAllocationDto;
import net.malpiszon.fundallocator.dtos.FundAllocationRequestDto;
import net.malpiszon.fundallocator.dtos.FundAllocationsDto;
import net.malpiszon.fundallocator.models.Fund;
import net.malpiszon.fundallocator.models.FundType;
import net.malpiszon.fundallocator.models.InvestmentType;
import net.malpiszon.fundallocator.services.impls.AllocationService;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AllocationController.class)
public class AllocationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AllocationService allocationService;

    @Test
    public void testGetAllocation_withoutParameters_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/allocation")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllocation_withNegativeAmount_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/allocation?amount=-100&type=BALANCED&fund=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllocation_withInvalidInvestType_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/allocation?amount=100&type=BLAH&fund=1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllocation_withInvalidFundId_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/allocation?amount=100&type=BALANCED&fund=blah")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllocation_withValidParameters_returnsOk() throws Exception {
        long fundId1 = 1L;
        long fundId2 = 2L;
        FundType fundType = FundType.POLISH;

        FundAllocationsDto result = new FundAllocationsDto();
        result.addAllocations(Lists.newArrayList(new FundAllocationDto(
                new Fund(fundId1, fundType, "name1"), BigInteger.valueOf(50), 0.5)));
        result.addAllocations(Lists.newArrayList(new FundAllocationDto(
                new Fund(fundId2, fundType, "name2"), BigInteger.valueOf(50), 0.5)));

        when(allocationService.getAllocation(any(FundAllocationRequestDto.class))).thenReturn(result);

        mockMvc.perform(get("/api/allocation?amount=100&type=BALANCED&fund=1&fund=2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allocations", hasSize(2)))
                .andExpect(jsonPath("$.allocations[0].fundType", is(fundType.toString())))
                .andExpect(jsonPath("$.allocations[0].lp", is(1)))
                .andExpect(jsonPath("$.allocations[1].lp", is(2)))
                .andExpect(jsonPath("$.notAllocated", is(0)));

        ArgumentCaptor<FundAllocationRequestDto> fardCaptor = ArgumentCaptor.forClass(FundAllocationRequestDto.class);
        verify(allocationService, times(1)).getAllocation(fardCaptor.capture());
        assertEquals(BigInteger.valueOf(100), fardCaptor.getValue().getAmount());
        assertEquals(InvestmentType.BALANCED, fardCaptor.getValue().getType());
        assertEquals(Lists.newArrayList(fundId1, fundId2), fardCaptor.getValue().getFund());
        verifyNoMoreInteractions(allocationService);
    }
}