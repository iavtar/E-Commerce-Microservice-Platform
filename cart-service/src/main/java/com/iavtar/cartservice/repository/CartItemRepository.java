package com.iavtar.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iavtar.cartservice.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	CartItem findByCartIdAndProductId(Long cartId, Long productId);

	void deleteByCartIdAndProductId(Long cartId, Long productId);

}
