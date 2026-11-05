import { IProductRepository } from '../repositories/product.repository.interface';
import { Product } from '../types/product.types';

export class ProductService {
  constructor(private productRepository: IProductRepository) {}

  async getFeaturedProducts(): Promise<Product[]> {
    return this.productRepository.getFeaturedProducts(5);
  }

  async getCategories(): Promise<string[]> {
    return this.productRepository.getCategories();
  }

  async getProductsByCategory(category: string): Promise<Product[]> {
    return this.productRepository.getProductsByCategory(category);
  }

  async searchProducts(query: string): Promise<Product[]> {
    return this.productRepository.searchProducts(query);
  }

  async getProductById(id: number): Promise<Product | null> {
    return this.productRepository.getProductById(id);
  }
}
