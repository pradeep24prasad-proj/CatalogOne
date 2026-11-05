import request from 'supertest';
import { createApp } from '../src/app';
import { IProductRepository } from '../src/repositories/product.repository.interface';
import { ProductService } from '../src/services/product.service';
import { Product } from '../src/types/product.types';

const mockProducts: Product[] = [
  { id: 1, title: 'Shirt A', price: 10, description: 'Cool shirt', category: 'clothing', image: 'img1.png', rating: { rate: 4.5, count: 10 } },
  { id: 2, title: 'Laptop B', price: 1000, description: 'Fast computer', category: 'electronics', image: 'img2.png', rating: { rate: 4.9, count: 50 } },
  { id: 3, title: 'Phone C', price: 500, description: 'Smart phone', category: 'electronics', image: 'img3.png', rating: { rate: 4.8, count: 30 } },
  { id: 4, title: 'Shoes D', price: 80, description: 'Running shoes', category: 'footwear', image: 'img4.png', rating: { rate: 3.2, count: 5 } },
  { id: 5, title: 'Cap E', price: 15, description: 'Baseball cap', category: 'clothing', image: 'img5.png', rating: { rate: 4.0, count: 20 } },
  { id: 6, title: 'Watch F', price: 200, description: 'Analog watch', category: 'accessories', image: 'img6.png', rating: { rate: 4.7, count: 40 } },
];

class MockProductRepository implements IProductRepository {
  async getAllProducts(): Promise<Product[]> {
    return mockProducts;
  }
  async getProductById(id: number): Promise<Product | null> {
    return mockProducts.find((p) => p.id === id) || null;
  }
  async getCategories(): Promise<string[]> {
    return Array.from(new Set(mockProducts.map((p) => p.category)));
  }
  async getProductsByCategory(category: string): Promise<Product[]> {
    return mockProducts
      .filter((p) => p.category === category)
      .sort((a, b) => b.rating.rate - a.rating.rate);
  }
  async getFeaturedProducts(limit: number = 5): Promise<Product[]> {
    return [...mockProducts]
      .sort((a, b) => b.rating.rate - a.rating.rate)
      .slice(0, limit);
  }
  async searchProducts(query: string): Promise<Product[]> {
    if (!query) return [];
    const q = query.toLowerCase();
    return mockProducts.filter(
      (p) =>
        p.title.toLowerCase().includes(q) ||
        p.description.toLowerCase().includes(q) ||
        p.category.toLowerCase().includes(q)
    );
  }
}

describe('Product API Endpoints', () => {
  let app: ReturnType<typeof createApp>;

  beforeEach(() => {
    const mockRepo = new MockProductRepository();
    const productService = new ProductService(mockRepo);
    app = createApp(productService);
  });

  test('GET /api/products/featured returns exactly five products sorted by rating', async () => {
    const response = await request(app).get('/api/products/featured');

    expect(response.status).toBe(200);
    expect(Array.isArray(response.body)).toBe(true);
    expect(response.body.length).toBe(5);

    // Verify rating sort descending
    const rates = response.body.map((p: Product) => p.rating.rate);
    expect(rates).toEqual([4.9, 4.8, 4.7, 4.5, 4.0]);
  });

  test('GET /api/categories returns all unique categories', async () => {
    const response = await request(app).get('/api/categories');

    expect(response.status).toBe(200);
    expect(response.body).toEqual(['clothing', 'electronics', 'footwear', 'accessories']);
  });

  test('GET /api/categories/:category/products returns category products sorted by rating descending', async () => {
    const response = await request(app).get('/api/categories/electronics/products');

    expect(response.status).toBe(200);
    expect(response.body.length).toBe(2);
    expect(response.body[0].title).toBe('Laptop B'); // rate 4.9
    expect(response.body[1].title).toBe('Phone C');  // rate 4.8
  });

  test('GET /api/products?search=phone returns matching products by title/description/category', async () => {
    const response = await request(app).get('/api/products?search=phone');

    expect(response.status).toBe(200);
    expect(response.body.length).toBe(1);
    expect(response.body[0].title).toBe('Phone C');
  });

  test('GET /api/products/:id returns single product details', async () => {
    const response = await request(app).get('/api/products/1');

    expect(response.status).toBe(200);
    expect(response.body.title).toBe('Shirt A');
  });

  test('GET /api/products/:id handles non-existent product ID with 404', async () => {
    const response = await request(app).get('/api/products/999');

    expect(response.status).toBe(404);
    expect(response.body.error).toBe('Product not found');
  });

  test('GET /api/products/:id handles invalid non-numeric ID with 400', async () => {
    const response = await request(app).get('/api/products/invalid-id');

    expect(response.status).toBe(400);
    expect(response.body.error).toBe('Invalid product ID');
  });
});

describe('External API Failure Handling', () => {
  class FailingProductRepository implements IProductRepository {
    async getAllProducts(): Promise<Product[]> {
      throw new Error('FakeStore API is down');
    }
    async getProductById(): Promise<Product | null> {
      throw new Error('FakeStore API is down');
    }
    async getCategories(): Promise<string[]> {
      throw new Error('FakeStore API is down');
    }
    async getProductsByCategory(): Promise<Product[]> {
      throw new Error('FakeStore API is down');
    }
    async getFeaturedProducts(): Promise<Product[]> {
      throw new Error('FakeStore API is down');
    }
    async searchProducts(): Promise<Product[]> {
      throw new Error('FakeStore API is down');
    }
  }

  test('handles external API failure gracefully with 500 status', async () => {
    const failingService = new ProductService(new FailingProductRepository());
    const failingApp = createApp(failingService);

    const response = await request(failingApp).get('/api/products/featured');

    expect(response.status).toBe(500);
    expect(response.body.error).toBe('FakeStore API is down');
  });
});
