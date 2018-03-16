package net.malpiszon.fundallocator.controllers;

import java.math.BigInteger;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.malpiszon.fundallocator.dtos.FundAllocationsDto;
import net.malpiszon.fundallocator.models.InvestmentType;
import net.malpiszon.fundallocator.services.impls.AllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/allocation")
@Api(description = "Allocation of investment amount for chosen investment funds and investment type")
public class AllocationController {


    @Autowired
    private AllocationService allocationService;

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
    public FundAllocationsDto getAllocation(@RequestParam(value = "amount") BigInteger amount,
                                          @RequestParam(value = "type") String type,
                                          @RequestParam(value = "fund") List<Long> funds)
            throws IllegalArgumentException {

        if (amount.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("Non-positive amount");
        }

        return allocationService.getAllocation(amount, InvestmentType.getInvestmentType(type), funds);
    }
}
