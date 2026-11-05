import { Router } from 'express';
import { ProductController } from '../controllers/product.controller';
import { ProductService } from '../services/product.service';
import { FakeStoreProductRepository } from '../repositories/fakeStoreProduct.repository';

export function createProductRouter(productService?: ProductService): Router {
  const router = Router();
  const service = productService || new ProductService(new FakeStoreProductRepository());
  const controller = new ProductController(service);

  router.get('/products/featured', controller.getFeaturedProducts);
  router.get('/categories', controller.getCategories);
  router.get('/categories/:category/products', controller.getProductsByCategory);
  router.get('/products', controller.getProductsOrSearch);
  router.get('/products/:id', controller.getProductById);

  return router;
}
