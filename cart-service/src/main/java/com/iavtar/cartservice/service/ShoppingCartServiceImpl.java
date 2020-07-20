package com.iavtar.cartservice.service;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iavtar.cartservice.entity.CartItem;
import com.iavtar.cartservice.entity.ShoppingCart;
import com.iavtar.cartservice.repository.CartItemRepository;
import com.iavtar.cartservice.repository.ShoppingCartRepository;
import com.iavtar.cartservice.request.dto.AddProductToCartRequest;
import com.iavtar.cartservice.request.dto.CheckCartPredicate;
import com.iavtar.cartservice.request.dto.CleanCartRequest;
import com.iavtar.cartservice.request.dto.CreateShoppingCartRequest;
import com.iavtar.cartservice.request.dto.DecreaseProductQtyRequest;
import com.iavtar.cartservice.request.dto.GetCartItemRequest;
import com.iavtar.cartservice.request.dto.RemoveProductFromCartRequest;
import com.iavtar.cartservice.response.dto.CreateShoppingCartResponse;
import com.iavtar.cartservice.response.dto.ServiceResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private ShoppingCartRepository shoppingCartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Override
	public ResponseEntity<?> createShoppingCart(CreateShoppingCartRequest request) {
		ResponseEntity<?> response;
		CreateShoppingCartResponse createShoppingCartResponse = new CreateShoppingCartResponse();
		ServiceResponse res = new ServiceResponse();
		try {
			ShoppingCart cart = new ShoppingCart();
			cart.setUserId(request.getUserId());
			ShoppingCart savedShoppingCart = shoppingCartRepository.save(cart);
			createShoppingCartResponse.setCode("200");
			createShoppingCartResponse.setMessage("Shopping cart created!");
			createShoppingCartResponse.setCartId(savedShoppingCart.getId());
			response = new ResponseEntity<CreateShoppingCartResponse>(createShoppingCartResponse, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Error Creating Shoppin Cart");
			res.setCode("501");
			res.setMessage("Internal Server Error");
			response = new ResponseEntity<ServiceResponse>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}

	@Override
	public ResponseEntity<?> addProductToCart(AddProductToCartRequest request) {
		ResponseEntity<?> response = null;
		ServiceResponse res = new ServiceResponse();
		BigDecimal totalPrice = BigDecimal.ZERO;
		try {
			CartItem presentCartItem = cartItemRepository.findByCartIdAndProductId(request.getCartId(),
					request.getProductItem().getProductId());
			if (!(presentCartItem == null)) {
				presentCartItem.setProductQuantity(presentCartItem.getProductQuantity() + 1);
				totalPrice = presentCartItem.getTotalPrice().add(presentCartItem.getPricePerUnit());
				// totalPrice =
				// presentCartItem.getPricePerUnit().add(presentCartItem.getTotalPrice());
				presentCartItem.setTotalPrice(totalPrice);
				cartItemRepository.save(presentCartItem);
				res.setCode("200");
				res.setMessage("products added successfully to the cart");
				response = new ResponseEntity<ServiceResponse>(res, HttpStatus.OK);
			} else {
				ShoppingCart cart = shoppingCartRepository.findById(request.getCartId()).orElse(null);
				CartItem cartItem = new CartItem();
				cartItem.setProductId(request.getProductItem().getProductId());
				cartItem.setProductName(request.getProductItem().getProductName());
				cartItem.setProductQuantity(request.getProductItem().getProductQuantity());
				totalPrice = request.getProductItem().getPricePerUnit()
						.multiply(new BigDecimal(request.getProductItem().getProductQuantity()));
				cartItem.setTotalPrice(totalPrice);
				cartItem.setPricePerUnit(request.getProductItem().getPricePerUnit());
				cartItem.setProductImageUrl(request.getProductItem().getImageUrl());
				cart.addItemToCart(cartItem);
				cartItem.setCart(cart);
				shoppingCartRepository.save(cart);
				cartItemRepository.save(cartItem);
				res.setCode("200");
				res.setMessage("products added successfully to the cart");
				response = new ResponseEntity<ServiceResponse>(res, HttpStatus.OK);
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			res.setCode("501");
			res.setMessage("Internal Server Error!");
			response = new ResponseEntity<ServiceResponse>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return response;
	}

	@Transactional
	@Override
	public ResponseEntity<?> removeCartItemFromCart(RemoveProductFromCartRequest request) {

		ResponseEntity<?> response = null;
		ServiceResponse res = new ServiceResponse();

		try {
			cartItemRepository.deleteByCartIdAndProductId(request.getCartId(), request.getProductId());
			res.setCode("200");
			res.setMessage("Item removed from cart");
			response = new ResponseEntity<ServiceResponse>(res, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			res.setCode("500");
			res.setMessage("Internal Server Error");
			response = new ResponseEntity<ServiceResponse>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}

	@Override
	public ResponseEntity<?> checkCartPredicate(CheckCartPredicate request) {

		ResponseEntity<?> response = null;
		ServiceResponse res = new ServiceResponse();

		try {
			ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(request.getUserId());
			if (!(shoppingCart == null)) {
				res.setCode("200");
				res.setMessage("User Cart Exists");
				response = new ResponseEntity<ServiceResponse>(res, HttpStatus.OK);
			} else {
				res.setCode("404");
				res.setMessage("User Cart Not Exists");
				response = new ResponseEntity<ServiceResponse>(res, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error(e.toString());
			res.setCode("501");
			res.setMessage("Internal Server Error");
			response = new ResponseEntity<ServiceResponse>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return response;
	}

	@Transactional
	@Override
	public ResponseEntity<?> decreaseProductQuantityFromCart(DecreaseProductQtyRequest request) {
		ResponseEntity<?> response = null;
		ServiceResponse res = new ServiceResponse();
		BigDecimal totalPrice = BigDecimal.ZERO;

		try {
			CartItem presentCartItem = cartItemRepository.findByCartIdAndProductId(request.getCartId(),
					request.getProductId());
			System.out.println(presentCartItem.equals(null));
			if (presentCartItem.equals(null)) {
				res.setCode("200");
				res.setMessage("product removed from the cart");
				response = new ResponseEntity<ServiceResponse>(res, HttpStatus.OK);
			} else {
				if (presentCartItem.getTotalPrice().equals(presentCartItem.getPricePerUnit())) {
					cartItemRepository.deleteByCartIdAndProductId(request.getCartId(), request.getProductId());
					res.setCode("200");
					res.setMessage("product removed from the cart");
					response = new ResponseEntity<ServiceResponse>(res, HttpStatus.OK);
				} else {
					presentCartItem.setProductQuantity(presentCartItem.getProductQuantity() - 1);
					totalPrice = presentCartItem.getTotalPrice().subtract(presentCartItem.getPricePerUnit());
					presentCartItem.setTotalPrice(totalPrice);
					cartItemRepository.save(presentCartItem);
					res.setCode("200");
					res.setMessage("products qty updated successfully in the cart");
					response = new ResponseEntity<ServiceResponse>(res, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			log.error(e.toString());
			res.setCode("404");
			res.setMessage("product removed from the cart");
			response = new ResponseEntity<ServiceResponse>(res, HttpStatus.OK);
		}
		return response;
	}

	@Override
	public ResponseEntity<?> getAllCartItems(GetCartItemRequest request) {
		
		ResponseEntity<?> response = null;
		ServiceResponse res = new ServiceResponse();
		
		ShoppingCart cartItems = shoppingCartRepository.findById(request.getCartId()).orElse(null);
		
		if(!(cartItems == null)) {
			response = new ResponseEntity<ShoppingCart>(cartItems, HttpStatus.OK);
		}else {
			res.setCode("200");
			res.setMessage("No Cart Found");
			response = new ResponseEntity<ServiceResponse>(res, HttpStatus.OK);
		}
		
		return response;
	}

	@Transactional
	@Override
	public ResponseEntity<?> cleanCart(CleanCartRequest request) {
		
		ResponseEntity<?> responseEntity;
		ServiceResponse response = new ServiceResponse();
		try {
			for(int i = 0; i < request.getProductIds().size(); i++) {
				cartItemRepository.deleteByCartIdAndProductId(request.getCartId(), request.getProductIds().get(i));
			}
			response.setCode("200");
			response.setMessage("Cart Successfully cleaned");
			responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.toString());
			response.setCode("500");
			response.setMessage("Internal server Error");
			responseEntity = new ResponseEntity<>(request, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return responseEntity;
	}

}