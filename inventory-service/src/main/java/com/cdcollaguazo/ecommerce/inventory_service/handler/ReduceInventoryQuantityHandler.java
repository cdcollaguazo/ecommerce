package com.cdcollaguazo.ecommerce.inventory_service.handler;

import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryOperationRequest;
import com.cdcollaguazo.ecommerce.inventory_service.dto.ReduceInventoryQuantity;
import com.cdcollaguazo.ecommerce.inventory_service.entity.Inventory;
import com.cdcollaguazo.ecommerce.inventory_service.execption.InvalidInventoryOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReduceInventoryQuantityHandler implements InventoryOperation {

    private final Logger log = LoggerFactory.getLogger(ReduceInventoryQuantityHandler.class);

    @Override
    public boolean supports(InventoryOperationRequest request) {
        return request.reduceInventoryQuantity() != null;
    }

    @Override
    public void execute(Inventory inventory, InventoryOperationRequest request) {
        ReduceInventoryQuantity operation = request.reduceInventoryQuantity();

        if (operation.value() > inventory.getQuantity()) {
            throw new InvalidInventoryOperationException(inventory.getSku(), "unsufficient stock");
        }

        inventory.setQuantity(inventory.getQuantity() - operation.value());
    }

}
