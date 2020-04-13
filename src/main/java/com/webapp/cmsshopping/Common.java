package com.webapp.cmsshopping;

import com.webapp.cmsshopping.models.Cart;
import com.webapp.cmsshopping.models.Category;
import com.webapp.cmsshopping.models.Page;
import com.webapp.cmsshopping.repository.CategoryRepository;
import com.webapp.cmsshopping.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class Common {
    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @ModelAttribute
    public void sharedData(Model model, HttpSession httpSession, Principal principal) {
        if(principal!=null)
        {
            model.addAttribute("principal",principal.getName());
        }
        List<Page> pages = pageRepository.findAllByOrderBySortingAsc();
        List<Category> categories = categoryRepository.findAll();
        boolean cartActive = false;
        if(httpSession.getAttribute("cart")!=null) {
            HashMap<Integer, Cart> cartHashMap = (HashMap<Integer, Cart>) httpSession.getAttribute("cart");
            int cartSize = 0;
            double total = 0;
            for (Cart value : cartHashMap.values()) {
                cartSize += value.getQuantity();
                total += value.getQuantity() * Double.parseDouble(value.getPrice());
            }
            cartActive = true;
            model.addAttribute("csize", cartSize);
            model.addAttribute("ctotal", total);
        }
        model.addAttribute("cpages", pages);
        model.addAttribute("ccategories", categories);
        model.addAttribute("cartActive",cartActive);
    }
}

