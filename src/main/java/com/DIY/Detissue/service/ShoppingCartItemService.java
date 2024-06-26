package com.DIY.Detissue.service;

import com.DIY.Detissue.entity.ProductSkus;
import com.DIY.Detissue.entity.ShoppingCartItem;
import com.DIY.Detissue.exception.CustomException;
import com.DIY.Detissue.payload.request.AddProductToCartRequest;
import com.DIY.Detissue.payload.response.ShoppingCartItemResponse;
import com.DIY.Detissue.repository.*;
import com.DIY.Detissue.service.Imp.ShoppingCartItemServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartItemService implements ShoppingCartItemServiceImp {
    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductSkusRepository productSkusRepository;
    @Autowired
    private ImagesRepository imagesRepository;


    @Override
    public List<ShoppingCartItemResponse> findByUserId(int id) {
        List<ShoppingCartItemResponse> responses = new ArrayList<>();
        try {
            List<ShoppingCartItem> list = shoppingCartItemRepository.findByUserId(id);
            for (ShoppingCartItem item : list) {
                ShoppingCartItemResponse response = new ShoppingCartItemResponse();
                response.setId(item.getId());
                response.setQuantity(item.getQuantity());
                ProductSkus productSkus = item.getProductSkus();
                response.setName(productSkus.getProduct().getName());
                response.setPrice(productSkus.getPrice());
                response.setSize(productSkus.getSize().getName());

                List<String> imageList = new ArrayList<>();
                imagesRepository.findByProductId(productSkus.getProduct().getId()).forEach(image -> {
                    imageList.add(image.getSource());
                });

                response.setImage(imageList);

                responses.add(response);
            }
        } catch (Exception e) {
            throw new CustomException("Error findByUserId in ShoppingCartItemService " + e.getMessage());
        }
        return responses;
    }

    @Override
    public boolean addShoppingCartItem(AddProductToCartRequest request) {
        try {
            // if sizeId is 0, get the first sizeId
            if (request.getSizeId() == 0) {
                request.setSizeId(sizeRepository.findAll().get(0).getId());
            }

            // check if item exist
            ShoppingCartItem item = shoppingCartItemRepository.findByUserIdAndProductIdAndSizeId(
                    request.getUserId(),
                    request.getProductId(),
                    request.getSizeId()
            );
            // check if productSkus exist
            ProductSkus productSkus = productSkusRepository.findByProductIdAndSizeId(
                    request.getProductId(),
                    request.getSizeId()
            );
            if (productSkus == null) {
                throw new CustomException("ProductSkus not found");
            }

            // if item exist, increase quantity
            if (item != null) {
                item.setQuantity(item.getQuantity() + request.getQuantity());
                if (productSkus.getStockQuantity() < item.getQuantity()) {
                    throw new CustomException("Stock not enough, Please reduce quantity !");
                }
                shoppingCartItemRepository.save(item);
            } else {
                // if item not exist, create new item
                if (productSkus.getStockQuantity() < request.getQuantity()) {
                    throw new CustomException("Stock not enough, Please reduce quantity !");
                }
                ShoppingCartItem newItem = new ShoppingCartItem();

                cartRepository.findByUserId(request.getUserId()).ifPresentOrElse(cart -> {
                    newItem.setCart(cart);
                }, () -> {
                    throw new CustomException("Cart not found");
                });

                newItem.setProductSkus(productSkus);
                newItem.setQuantity(request.getQuantity());
                shoppingCartItemRepository.save(newItem);
            }
        } catch (Exception e) {
            throw new CustomException("Error addShoppingCartItem in ShoppingCartItemService " + e.getMessage());
        }
        return true;
    }
    @Override
    public boolean deleteShopingCartItemById(int id) {
        try {
            ShoppingCartItem item = shoppingCartItemRepository.findById(id).
                                    orElseThrow(() -> new CustomException("ShoppingCartItem not found"));
            shoppingCartItemRepository.delete(item);
        } catch (Exception e) {
            throw new CustomException("Error deleteShopingCartItemById in ShoppingCartItemService " + e.getMessage());
        }
        return true;
    }
}
