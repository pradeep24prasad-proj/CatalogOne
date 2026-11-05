import { IProductRepository } from './product.repository.interface';
import { Product } from '../types/product.types';
import { env } from '../config/env';

export class FakeStoreProductRepository implements IProductRepository {
  private baseUrl: string;

  constructor(baseUrl: string = env.FAKE_STORE_API_URL) {
    this.baseUrl = baseUrl;
  }

  async getAllProducts(): Promise<Product[]> {
    try {
      const response = await fetch(`${this.baseUrl}/products`);
      if (!response.ok) {
        throw new Error(`FakeStore API error: ${response.statusText}`);
      }
      const data = await response.json();
      return data as Product[];
    } catch (error) {
      console.error('Error fetching products from FakeStore API:', error);
      throw error;
    }
  }

  async getProductById(id: number): Promise<Product | null> {
    try {
      const response = await fetch(`${this.baseUrl}/products/${id}`);
      if (!response.ok) {
        if (response.status === 404) return null;
        throw new Error(`FakeStore API error: ${response.statusText}`);
      }
      const text = await response.text();
      if (!text || text === 'null') return null;
      return JSON.parse(text) as Product;
    } catch (error) {
      console.error(`Error fetching product ${id} from FakeStore API:`, error);
      throw error;
    }
  }

  async getCategories(): Promise<string[]> {
    try {
      const response = await fetch(`${this.baseUrl}/products/categories`);
      if (!response.ok) {
        throw new Error(`FakeStore API error: ${response.statusText}`);
      }
      const data = await response.json();
      return data as string[];
    } catch (error) {
      console.error('Error fetching categories from FakeStore API:', error);
      throw error;
    }
  }

  async getProductsByCategory(category: string): Promise<Product[]> {
    try {
      const response = await fetch(`${this.baseUrl}/products/category/${encodeURIComponent(category)}`);
      if (!response.ok) {
        throw new Error(`FakeStore API error: ${response.statusText}`);
      }
      const data = (await response.json()) as Product[];
      // Sort by rating descending (rate then count)
      return data.sort((a, b) => (b.rating?.rate || 0) - (a.rating?.rate || 0));
    } catch (error) {
      console.error(`Error fetching products for category ${category}:`, error);
      throw error;
    }
  }

  async getFeaturedProducts(limit: number = 5): Promise<Product[]> {
    const products = await this.getAllProducts();
    const sorted = [...products].sort((a, b) => (b.rating?.rate || 0) - (a.rating?.rate || 0));
    return sorted.slice(0, limit);
  }

  async searchProducts(query: string): Promise<Product[]> {
    if (!query || query.trim() === '') {
      return [];
    }
    const products = await this.getAllProducts();
    const q = query.toLowerCase().trim();
    return products.filter((product) => {
      const titleMatch = product.title.toLowerCase().includes(q);
      const descMatch = product.description.toLowerCase().includes(q);
      const categoryMatch = product.category.toLowerCase().includes(q);
      return titleMatch || descMatch || categoryMatch;
    });
  }
}
