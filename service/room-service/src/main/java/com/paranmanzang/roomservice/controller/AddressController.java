package com.paranmanzang.roomservice.controller;

import com.paranmanzang.roomservice.model.domain.AddressModel;
import com.paranmanzang.roomservice.model.domain.AddressUpdateModel;
import com.paranmanzang.roomservice.service.impl.AddressServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Objects;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {
    private final String Client_Id="";
    private final String Client_Secret_Key= "";
    private final AddressServiceImpl addressService;

    @GetMapping("/search")
    public ResponseEntity<?> searchAddress(@RequestParam("query") String query){

        return ResponseEntity.ok(Objects.requireNonNull(WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-Naver-Client-Id", Client_Id)
                .defaultHeader("X-Naver-Client-Secret", Client_Secret_Key)
                .build().get()
                .uri(UriComponentsBuilder
                        .fromUriString("https://openapi.naver.com")
                        .path("/v1/search/local.json")
                        .queryParam("query",query)
                        .queryParam("display",5)
                        .queryParam("start",1)
                        .queryParam("sort","random")
                        .encode(StandardCharsets.UTF_8)
                        .build()
                        .toUri())
                .retrieve()
                .toEntity(String.class)
                .block()).getBody());

    }

    @PostMapping("/add")
    public ResponseEntity<?> save(@Valid @RequestBody AddressModel addressModel, BindingResult result) throws BindException {
        if (result.hasErrors()) throw new BindException(result);
        return ResponseEntity.ok(addressService.save(addressModel));
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody AddressUpdateModel addressModel, BindingResult result) throws BindException{
        if(result.hasErrors()) throw new BindException(result);
        return ResponseEntity.ok(addressService.update(addressModel));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        return ResponseEntity.ok(addressService.delete(id));
    }
    @GetMapping("/one/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(addressService.findById(id));
    }
    @GetMapping("/list")
    public ResponseEntity<?> getList(){
        return ResponseEntity.ok(addressService.findAll());
    }
}
