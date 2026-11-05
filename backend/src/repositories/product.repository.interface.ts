import { Product } from '../types/product.types';

export interface IProductRepository {
  getAllProducts(): Promise<Product[]>;
  getProductById(id: number): Promise<Product | null>;
  getCategories(): Promise<string[]>;
  getProductsByCategory(category: string): Promise<Product[]>;
  searchProducts(query: string): Promise<Product[]>;
  getFeaturedProducts(limit?: number): Promise<Product[]>;
}
