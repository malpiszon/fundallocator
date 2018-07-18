package net.malpiszon.fundallocator.controllers;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.malpiszon.fundallocator.dtos.FundAllocationRequestDto;
import net.malpiszon.fundallocator.dtos.FundAllocationsDto;
import net.malpiszon.fundallocator.services.impls.AllocationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/allocation")
@Api(description = "Allocation of investment amount for chosen investment funds and investment type")
public class AllocationController {

    private final AllocationService allocationService;

    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @ApiOperation(value = "List energy consumption in given period grouped by given period")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully allocated amount into funds",
                    response = FundAllocationsDto.class),
            @ApiResponse(code = 400, message = "Parameters in wrong format"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "Amount of money to invest",
                    dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "Type of investment strategy",
                    dataType = "string", paramType = "query", allowableValues = "SAFE, BALANCED, AGGRESSIVE"),
            @ApiImplicitParam(name = "fund", value = "IDs of fund to invest", allowMultiple = true,
                    dataType = "integer", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    public FundAllocationsDto getAllocation(@Valid FundAllocationRequestDto requestDto) {
        return allocationService.getAllocation(requestDto);
    }
}
