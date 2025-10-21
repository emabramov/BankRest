package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.ErrorDto;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.exception.SearchException;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/cards")
public class CardController {
        private final CardService cardService;

        @GetMapping
        public Collection<CardDto> getAll(){
            return cardService.getAll();
        }

        @GetMapping("/{id}")
        public CardDto getCardDto(@PathVariable Long id) throws SearchException {
            return cardService.getById(id);
        }

        @PostMapping("/create")
        public CardDto createCard(@RequestBody CardDto cardDto){
            cardService.create(cardDto);
            return cardService.findByNumber(cardDto.getCardNumber());
        }

        @PutMapping("/update/{id}")
        public void update(@PathVariable Long id, @RequestBody CardDto cardDto) throws SearchException{
            cardService.update(id, cardDto);
        }

        @DeleteMapping("/delete/{id}")
        public String deleteCard(@PathVariable Long id) throws SearchException{
            cardService.delete(id);
            return "redirect:/";
        }

        @ExceptionHandler(SearchException.class)
        public ResponseEntity<ErrorDto> handleCardException(SearchException ex){
            ErrorDto error = new ErrorDto();
            error.setError(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
}
