import request from 'supertest';
import { createApp } from '../src/app';
import { IProductRepository } from '../src/repositories/product.repository.interface';
import { IEmailProvider } from '../src/providers/email.provider.interface';
import { EnquiryService } from '../src/services/enquiry.service';
import { EnquiryRequest } from '../src/types/enquiry.types';
import { Product } from '../src/types/product.types';

const mockProduct: Product = {
  id: 1,
  title: 'Test Product',
  price: 99.99,
  description: 'Test description',
  category: 'electronics',
  image: 'test.jpg',
  rating: { rate: 4.5, count: 10 },
};

class MockProductRepo implements IProductRepository {
  async getAllProducts() { return [mockProduct]; }
  async getProductById(id: number) { return id === 1 ? mockProduct : null; }
  async getCategories() { return ['electronics']; }
  async getProductsByCategory() { return [mockProduct]; }
  async getFeaturedProducts() { return [mockProduct]; }
  async searchProducts() { return [mockProduct]; }
}

class SuccessfulEmailProvider implements IEmailProvider {
  async sendEnquiryNotification(enquiry: EnquiryRequest): Promise<boolean> {
    return true;
  }
}

class FailingEmailProvider implements IEmailProvider {
  async sendEnquiryNotification(enquiry: EnquiryRequest): Promise<boolean> {
    return false;
  }
}

describe('POST /api/enquiries Endpoint', () => {
  const validPayload = {
    productId: 1,
    productTitle: 'Test Product',
    customerName: 'Jane Doe',
    email: 'jane@example.com',
    phone: '+1234567890',
    message: 'Hello, I would like to make an enquiry.'
  };

  test('successfully submits enquiry when product exists and email sends', async () => {
    const service = new EnquiryService(new MockProductRepo(), new SuccessfulEmailProvider());
    const app = createApp(undefined, service);

    const response = await request(app).post('/api/enquiries').send(validPayload);

    expect(response.status).toBe(201);
    expect(response.body.success).toBe(true);
    expect(response.body.enquiryId).toBeDefined();
  });

  test('fails validation when email format is invalid', async () => {
    const service = new EnquiryService(new MockProductRepo(), new SuccessfulEmailProvider());
    const app = createApp(undefined, service);

    const response = await request(app).post('/api/enquiries').send({
      ...validPayload,
      email: 'not-an-email'
    });

    expect(response.status).toBe(400);
    expect(response.body.error).toBe('Validation Error');
    expect(response.body.details).toEqual(
      expect.arrayContaining([
        expect.objectContaining({ field: 'email', message: 'Invalid email address format' })
      ])
    );
  });

  test('fails validation when mandatory customer name is missing', async () => {
    const service = new EnquiryService(new MockProductRepo(), new SuccessfulEmailProvider());
    const app = createApp(undefined, service);

    const { customerName, ...invalidPayload } = validPayload;

    const response = await request(app).post('/api/enquiries').send(invalidPayload);

    expect(response.status).toBe(400);
    expect(response.body.error).toBe('Validation Error');
  });

  test('returns 404 when enquiry is submitted for non-existent product', async () => {
    const service = new EnquiryService(new MockProductRepo(), new SuccessfulEmailProvider());
    const app = createApp(undefined, service);

    const response = await request(app).post('/api/enquiries').send({
      ...validPayload,
      productId: 9999
    });

    expect(response.status).toBe(404);
    expect(response.body.error).toContain('Product not found');
  });

  test('returns 500 when email delivery fails (prevents false success)', async () => {
    const service = new EnquiryService(new MockProductRepo(), new FailingEmailProvider());
    const app = createApp(undefined, service);

    const response = await request(app).post('/api/enquiries').send(validPayload);

    expect(response.status).toBe(500);
    expect(response.body.error).toContain('Failed to dispatch email notification');
  });
});
