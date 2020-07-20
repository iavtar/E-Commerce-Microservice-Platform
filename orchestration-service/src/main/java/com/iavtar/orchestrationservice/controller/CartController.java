package com.iavtar.orchestrationservice.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iavtar.orchestrationservice.dto.request.AddProductToCartRequest;
import com.iavtar.orchestrationservice.dto.request.DecreaseProductQtyRequest;
import com.iavtar.orchestrationservice.dto.request.GetCartItemRequest;
import com.iavtar.orchestrationservice.dto.request.RemoveProductFromCartRequest;
import com.iavtar.orchestrationservice.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;

	@PostMapping("/add-product-to-cart")
	public ResponseEntity<?> addProductToCart(Principal principal,@RequestBody AddProductToCartRequest request) {
		Long UserId = Long.parseLong(principal.getName());
		ResponseEntity<?> response = cartService.addProductToCart(UserId, request);
		return response;
	}
	
	@PostMapping("/decrease_product_qty_f_cart")
	public ResponseEntity<?> decreaseProductQuantityFromCart(Principal principal, @RequestBody DecreaseProductQtyRequest request){
		Long userId = Long.parseLong(principal.getName());
		ResponseEntity<?> response = cartService.decreaseProductQuantityFromCart(userId, request);
		return response;
	}
	
	@PostMapping("/remove_product_f_cart")
	public ResponseEntity<?> removeProductFromCart(Principal principal, @RequestBody RemoveProductFromCartRequest request){
		Long userId = Long.parseLong(principal.getName());
		ResponseEntity<?> response = cartService.removeProductFromCart(userId, request);
		return response;
	}
	
	@PostMapping("/cart_items")
	public ResponseEntity<?> getAllCartItems(Principal principal, @RequestBody GetCartItemRequest request){
		Long userId = Long.parseLong(principal.getName());
		ResponseEntity<?> response = cartService.getAllCartItems(userId, request);
		return response;
	}

	@PostMapping("/checkout-cart")
	public ResponseEntity<?> checkOutCart() {
		return null;
	}

}
