package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.SearchException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CardService implements CRUDService<CardDto>{
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Override
    public void create(CardDto cardDto) {
        CardEntity cardEntity = mapToEntity(cardDto);
        Long userId = cardDto.getUserId();
        cardEntity.setUser(userRepository.findById(cardDto.getUserId()).orElseThrow());
        cardRepository.save(cardEntity);
    }

    @Override
    public CardDto getById(Long id) {
        CardEntity cardEntity = cardRepository.findById(id).orElseThrow(() ->
                new SearchException("Карты с id %d не существует".formatted(id)));
        return mapToDto(cardEntity);
    }

    @Override
    public Collection<CardDto> getAll() {
        return cardRepository.findAll().stream()
                .map(CardService::mapToDto)
                .toList();
    }

    @Override
    public void update(Long id, CardDto cardDto) {
        CardEntity cardEntity = cardRepository.findById(id).orElseThrow(() ->
                new SearchException("Карты с id %d не существет".formatted(id)));
        cardEntity.setCardNumber(cardDto.getCardNumber() == null ? cardEntity.getCardNumber() : cardDto.getCardNumber());
        cardEntity.setAmount(cardDto.getAmount() == null ? cardEntity.getAmount() : cardDto.getAmount());
        cardEntity.setStatus(cardDto.getStatus() == null ? cardEntity.getStatus() : cardDto.getStatus());
        cardEntity.setUser(userRepository.findById(cardDto.getUserId()).orElseThrow(() ->
                new SearchException("Пользователь с id %d не найден".formatted(cardDto.getUserId()))));
        cardRepository.save(cardEntity);
    }

    @Override
    public void delete(Long id) throws SearchException{
        cardRepository.findById(id).orElseThrow();
        cardRepository.deleteById(id);
    }

    public CardDto findByNumber(Long number){
        CardEntity cardEntity = cardRepository.findByCardNumber(number).orElseThrow(() ->
                new SearchException("Карта с номером %d не найдена".formatted(number)));
        return mapToDto(cardEntity);
    }

    public static CardDto mapToDto(CardEntity cardEntity) {
        CardDto cardDto = new CardDto();
        cardDto.setId(cardEntity.getId());
        Long userId = cardEntity.getUser().getId();
        cardDto.setUserId(userId);
        cardDto.setAmount(cardEntity.getAmount());
        cardDto.setCardNumber(cardEntity.getCardNumber());
        cardDto.setStatus(cardEntity.getStatus());
        return cardDto;
    }

    public static CardEntity mapToEntity(CardDto cardDto) {
        CardEntity cardEntity = new CardEntity();
        cardEntity.setAmount(cardDto.getAmount());
        cardEntity.setCardNumber(cardDto.getCardNumber());
        cardEntity.setStatus(cardDto.getStatus());
        return cardEntity;
    }
}
