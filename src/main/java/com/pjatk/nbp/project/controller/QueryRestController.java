package com.pjatk.nbp.project.controller;

import com.pjatk.nbp.project.model.Entry;
import com.pjatk.nbp.project.service.DateService;
import com.pjatk.nbp.project.service.LoggerService;
import com.pjatk.nbp.project.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "NBP_APP_API", description = "API for connecting to the NBP endpoint")
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/average")
public class QueryRestController {

    @Autowired
    RestService restService;

    @Autowired
    LoggerService loggerService;

    @Autowired
    DateService date;

    @Operation(summary = "Get average value of a given currency in PLN from a given number of last days", tags = { "currency", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = Entry.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }) })
    @GetMapping(value = "{currency}")
    @ResponseBody
    public ResponseEntity<String> getRequestForCurrency(@RequestParam(defaultValue = "1") int days, @PathVariable String currency) throws HttpClientErrorException.BadRequest, HttpClientErrorException.NotFound, HttpClientErrorException.TooManyRequests {
        Entry requestResult;
        try {
            requestResult = restService.getNbpData(currency, days);
        } catch (NullPointerException e) {
            System.out.println("[RestController][ERROR] " + date.getTime() + " Rest Controller was unable to reach the requested endpoint!");
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
        if (requestResult != null) loggerService.logQuery(requestResult);
        return new ResponseEntity<>(requestResult.toString(), HttpStatus.OK);
    }

    }
