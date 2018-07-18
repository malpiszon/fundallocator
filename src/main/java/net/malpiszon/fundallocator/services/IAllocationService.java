package net.malpiszon.fundallocator.services;

import net.malpiszon.fundallocator.dtos.FundAllocationRequestDto;
import net.malpiszon.fundallocator.dtos.FundAllocationsDto;

public interface IAllocationService {
    FundAllocationsDto getAllocation(FundAllocationRequestDto requestDto);
}
