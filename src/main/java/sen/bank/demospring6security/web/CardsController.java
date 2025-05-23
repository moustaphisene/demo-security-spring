package sen.bank.demospring6security.web;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sen.bank.demospring6security.entity.Cards;
import sen.bank.demospring6security.repository.CardsRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CardsController {

    private final CardsRepository cardsRepository;

    @GetMapping("/yourCard")
    public List<Cards> getCardDetails(@RequestParam long customerId) {
        List<Cards> cards = cardsRepository.findByCustomerId(customerId);
        if (cards != null ) {
            return cards;
        }else {
            return null;
        }
    }



}
