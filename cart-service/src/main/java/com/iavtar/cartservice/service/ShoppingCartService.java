package com.iavtar.cartservice.service;

import org.springframework.http.ResponseEntity;

import com.iavtar.cartservice.request.dto.AddProductToCartRequest;
import com.iavtar.cartservice.request.dto.CheckCartPredicate;
import com.iavtar.cartservice.request.dto.CleanCartRequest;
import com.iavtar.cartservice.request.dto.CreateShoppingCartRequest;
import com.iavtar.cartservice.request.dto.DecreaseProductQtyRequest;
import com.iavtar.cartservice.request.dto.GetCartItemRequest;
import com.iavtar.cartservice.request.dto.RemoveProductFromCartRequest;

public interface ShoppingCartService {

	ResponseEntity<?> createShoppingCart(CreateShoppingCartRequest request);

	ResponseEntity<?> addProductToCart(AddProductToCartRequest request);

	ResponseEntity<?> removeCartItemFromCart(RemoveProductFromCartRequest request);

	ResponseEntity<?> checkCartPredicate(CheckCartPredicate request);

	ResponseEntity<?> decreaseProductQuantityFromCart(DecreaseProductQtyRequest request);

	ResponseEntity<?> getAllCartItems(GetCartItemRequest request);

	ResponseEntity<?> cleanCart(CleanCartRequest request);

	
}
