package jpabook.jpashop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        Book book = new Book();
        // 더 나은 설계는 아래처럼 세터 쓰는 게 아니라 엔티티 안에서 create() 로직을 추가하는 것임!
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId); // 예제 단순하게 하려고 캐스팅

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {

        /**
         * 아래의 Book 객체는 준영속 엔티티.
         * 준영속 엔티티: 영속성 컨텍스트가 더이상 관리하지 않는 엔티티를 말함.
         * 아래의 Book 객체는 새로 만든 것 같지만 빈 Book 객체에 이미 DB에 저장되어 있는 애를 입힌 것임.
         * 그러니까 식별자(id)가 존재. 이렇게 임의로 만들어낸 엔티티도 기존 식별자를 갖고 있다면 준영속 엔티티로
         * 봐야 한다.
         *
         * 준영속 엔티티를 수정하는 2가지 방법:
         * 1) 변경 감지 기능 사용
         * 2) 병합(Merge) 사용 -> 세터 쓰고 마지막에 save() 던져서 merge() 던지면 병합. 근데 이 방식은 좋지 않다.
         */

        Book book = new Book();
        /**
         * 병합: 준영속 상태 엔티티를 영속 상태로 변경할 때 사용하는 기능
         */
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
//        itemService.saveItem(book);

        /**
         * 컨트롤러에서 어설프게 엔티티로 받지 말고 DTO를 받아라!
         */
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }
}
