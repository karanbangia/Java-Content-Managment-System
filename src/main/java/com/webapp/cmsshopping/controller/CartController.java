package com.webapp.cmsshopping.controller;

import com.webapp.cmsshopping.models.Cart;
import com.webapp.cmsshopping.models.Product;
import com.webapp.cmsshopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@RequestMapping("/cart")
@SuppressWarnings("unchecked")
public class CartController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/add/{id}")
    public String add(@PathVariable int id, HttpSession httpSession, Model model, @RequestParam(value = "cartPage", required = false) String cartPage) {

        Product product = productRepository.getOne(id);
        if (httpSession.getAttribute("cart") == null) {
            HashMap<Integer, Cart> cartHashMap = new HashMap<>();
            cartHashMap.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
            httpSession.setAttribute("cart", cartHashMap);
        } else {
            HashMap<Integer, Cart> cartHashMap = (HashMap<Integer, Cart>) httpSession.getAttribute("cart");
            if (cartHashMap.containsKey(id)) {
                int qty = cartHashMap.get(id).getQuantity();
                cartHashMap.put(id, new Cart(id, product.getName(), product.getPrice(), qty + 1, product.getImage()));
            } else {
                cartHashMap.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
                httpSession.setAttribute("cart", cartHashMap);
            }
        }
        HashMap<Integer, Cart> cartHashMap = (HashMap<Integer, Cart>) httpSession.getAttribute("cart");
        int cartSize = 0;
        double total = 0;
        for (Cart value : cartHashMap.values()) {
            cartSize += value.getQuantity();
            total += value.getQuantity() * Double.parseDouble(value.getPrice());
        }
        model.addAttribute("size", cartSize);
        model.addAttribute("total", total);

        if (cartPage != null) {
            return "redirect:/cart/view";
        }
        return "cart_view";
    }

    @GetMapping("/view")
    public String view(HttpSession httpSession, Model model) {
        if (httpSession.getAttribute("cart") == null) {
            return "redirect:/";
        }
        HashMap<Integer, Cart> cartHashMap = (HashMap<Integer, Cart>) httpSession.getAttribute("cart");
        model.addAttribute("cart", cartHashMap);
        model.addAttribute("notCartViewPage", true);
        return "cart";
    }

    @GetMapping("/subtract/{id}")
    public String subtract(@PathVariable int id, HttpSession session, Model model, HttpServletRequest httpServletRequest) {

        Product product = productRepository.getOne(id);

        HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");

        int qty = cart.get(id).getQuantity();
        if (qty == 1) {
            cart.remove(id);
            if (cart.size() == 0) {
                session.removeAttribute("cart");
            }
        } else {
            cart.put(id, new Cart(id, product.getName(), product.getPrice(), --qty, product.getImage()));
        }

        String refererLink = httpServletRequest.getHeader("referer");

        return "redirect:" + refererLink;
    }
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable int id, HttpSession session, Model model, HttpServletRequest httpServletRequest) {

        HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");

        cart.remove(id);
        if (cart.size() == 0) {
            session.removeAttribute("cart");
        }

        String refererLink = httpServletRequest.getHeader("referer");

        return "redirect:" + refererLink;
    }
    @GetMapping("/clear")
    public String clear(HttpSession session, HttpServletRequest httpServletRequest) {

        session.removeAttribute("cart");

        String refererLink = httpServletRequest.getHeader("referer");

        return "redirect:" + refererLink;
    }


}
