package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setProductId("1");
        product1.setProductName("Product A");
        product1.setProductQuantity(10);

        product2 = new Product();
        product2.setProductId("2");
        product2.setProductName("Product B");
        product2.setProductQuantity(20);
    }

    @Test
    void testCreateProduct() {
        when(productRepository.create(any(Product.class))).thenReturn(product1);

        Product createdProduct = productService.create(product1);

        assertNotNull(createdProduct);
        assertEquals(product1.getProductId(), createdProduct.getProductId());
        verify(productRepository, times(1)).create(product1);
    }

    @Test
    void testFindAllProducts() {
        List<Product> productList = Arrays.asList(product1, product2);
        Iterator<Product> iterator = productList.iterator();
        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> retrievedProducts = productService.findAll();

        assertEquals(2, retrievedProducts.size());
        assertEquals("Product A", retrievedProducts.get(0).getProductName());
        assertEquals("Product B", retrievedProducts.get(1).getProductName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdProductExists() {
        when(productRepository.findById("1")).thenReturn(product1);

        Product foundProduct = productService.findById("1");

        assertNotNull(foundProduct);
        assertEquals("Product A", foundProduct.getProductName());
        verify(productRepository, times(1)).findById("1");
    }

    @Test
    void testFindByIdProductNotFound() {
        when(productRepository.findById("invalid-id")).thenReturn(null);

        Product foundProduct = productService.findById("invalid-id");

        assertNull(foundProduct);
        verify(productRepository, times(1)).findById("invalid-id");
    }

    @Test
    void testUpdateExistingProduct() {
        doNothing().when(productRepository).update(any(Product.class));

        productService.update("1", product1);

        verify(productRepository, times(1)).update(product1);
    }

    @Test
    void testDeleteExistingProduct() {
        doNothing().when(productRepository).delete(anyString());

        productService.delete("1");

        verify(productRepository, times(1)).delete("1");
    }
}
