package com.iavtar.cartservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iavtar.cartservice.request.dto.AddProductToCartRequest;
import com.iavtar.cartservice.request.dto.CheckCartPredicate;
import com.iavtar.cartservice.request.dto.CleanCartRequest;
import com.iavtar.cartservice.request.dto.CreateShoppingCartRequest;
import com.iavtar.cartservice.request.dto.DecreaseProductQtyRequest;
import com.iavtar.cartservice.request.dto.GetCartItemRequest;
import com.iavtar.cartservice.request.dto.RemoveProductFromCartRequest;
import com.iavtar.cartservice.service.RequestValidationService;
import com.iavtar.cartservice.service.ShoppingCartService;

@RestController
@RequestMapping("/api/shopping_cart")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private RequestValidationService requestValidator;

	@PostMapping("/check_cart_predicate")
	public ResponseEntity<?> checkCartPredicate(@RequestBody CheckCartPredicate request) {
		ResponseEntity<?> response = shoppingCartService.checkCartPredicate(request);
		return response;
	}

	@PostMapping("/create_shopping_cart")
	public ResponseEntity<?> createShoppingCart(@RequestBody CreateShoppingCartRequest request, BindingResult result) {

		ResponseEntity<?> errorResponseEntity = requestValidator.validateRequest(result);

		if (!(errorResponseEntity == null)) {
			return errorResponseEntity;
		} else {
			ResponseEntity<?> response = shoppingCartService.createShoppingCart(request);
			return response;
		}

	}

	@PostMapping("/add_product_to_cart")
	public ResponseEntity<?> addProductToShoppingCart(@RequestBody AddProductToCartRequest request) {
		ResponseEntity<?> response = shoppingCartService.addProductToCart(request);
		return response;
	}

	@PostMapping("/decrease_product_qty_from_cart")
	public ResponseEntity<?> decreaseProductQuantityFromCart(@RequestBody DecreaseProductQtyRequest request) {
		ResponseEntity<?> response = shoppingCartService.decreaseProductQuantityFromCart(request);
		return response;
	}

	@PostMapping("/remove_cart_item_f_cart")
	public ResponseEntity<?> removeCartItemFromCart(@RequestBody RemoveProductFromCartRequest request) {
		ResponseEntity<?> response = shoppingCartService.removeCartItemFromCart(request);
		return response;
	}
	
	@PostMapping("/cart_items")
	public ResponseEntity<?> getAllCartItems(@RequestBody GetCartItemRequest request){
		ResponseEntity<?> response = shoppingCartService.getAllCartItems(request);
		return response;
	}
	
	@PostMapping("/clean_cart")
	public ResponseEntity<?> cleanCart(@RequestBody CleanCartRequest request){
		ResponseEntity<?> response = shoppingCartService.cleanCart(request);
		return response;
	}

}
