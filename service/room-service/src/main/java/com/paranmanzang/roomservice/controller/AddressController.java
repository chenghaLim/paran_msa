package com.paranmanzang.roomservice.controller;

import com.paranmanzang.roomservice.model.domain.AddressModel;
import com.paranmanzang.roomservice.model.domain.AddressUpdateModel;
import com.paranmanzang.roomservice.service.impl.AddressServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "05. Address")
@RequestMapping("/api/rooms/addresses")
public class AddressController {
    private final String Client_Id="";
    private final String Client_Secret_Key= "";
    private final AddressServiceImpl addressService;

    @GetMapping("/search")
    @Operation(summary = "검색 조회", description = "검색어에 해당하는 주소 정보를 최대 5개까지 조회합니다.", tags = {"05. Address",})
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
    @GetMapping("")

    @PostMapping("/add")
    @Operation(summary = "주소 등록", description = "주소를 db에 저장합니다.")
    public ResponseEntity<?> save(@Valid @RequestBody AddressModel addressModel, BindingResult result) throws BindException {
        if (result.hasErrors()) throw new BindException(result);
        return ResponseEntity.ok(addressService.save(addressModel));
    }

    @PutMapping("/update")
    @Operation(summary = "주소 수정", description = "주소를 수정합니다.")
    public ResponseEntity<?> update(@Valid @RequestBody AddressUpdateModel addressModel, BindingResult result) throws BindException{
        if(result.hasErrors()) throw new BindException(result);
        return ResponseEntity.ok(addressService.update(addressModel));
    }
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "주소 삭제", description = "id 값을 기준으로 주소정보를 삭제합니다.")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        return ResponseEntity.ok(addressService.delete(id));
    }
    @GetMapping("/one/{id}")
    @Operation(summary = "단일 주소 조회", description = "id 값에 해당하는 1건의 주소정보를 조회합니다.")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(addressService.findById(id));
    }
    @GetMapping("/list")
    @Operation(summary = "전체 주소 조회", description = "존재하는 모든 주소정보를 조회합니다.")
    public ResponseEntity<?> getList(){
        return ResponseEntity.ok(addressService.findAll());
    }

    @GetMapping("/find/{query}")
    @Operation(summary = "db내 검색", description = "주소 기반으로 정보를 조회합니다.")
    public ResponseEntity<?> getFind(@PathVariable("query") String query){
        return ResponseEntity.ok(addressService.find(query));
    }
}
