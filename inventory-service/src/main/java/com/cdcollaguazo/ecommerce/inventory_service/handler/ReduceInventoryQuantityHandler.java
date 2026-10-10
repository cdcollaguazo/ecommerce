package com.cdcollaguazo.ecommerce.inventory_service.handler;

import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryOperationRequest;
import com.cdcollaguazo.ecommerce.inventory_service.dto.ReduceInventoryQuantity;
import com.cdcollaguazo.ecommerce.inventory_service.entity.Inventory;
import com.cdcollaguazo.ecommerce.inventory_service.execption.InvalidInventoryOperationException;
import com.cdcollaguazo.ecommerce.inventory_service.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReduceInventoryQuantityHandler implements InventoryOperation {

    private final Logger log = LoggerFactory.getLogger(ReduceInventoryQuantityHandler.class);
    private final InventoryRepository inventoryRepository;

    public ReduceInventoryQuantityHandler(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public boolean supports(Class<?> request) {
        return ReduceInventoryQuantity.class.isAssignableFrom(request);
    }

    @Override
    public Inventory apply(Inventory inventory, InventoryOperationRequest request) {
        ReduceInventoryQuantity operation = (ReduceInventoryQuantity)request;

        if (operation.value() > inventory.getQuantity()) {
            throw new InvalidInventoryOperationException(inventory.getSku(), "unsufficient stock");
        }

        inventory.setQuantity(inventory.getQuantity() - operation.value());

        Inventory savedInventory = inventoryRepository.save(inventory);
        log.info("Inventory {} quantity decreased by {}. Current quantity: {}", inventory.getSku(), operation.value(),
                savedInventory.getQuantity());

        return inventory;
    }

}
