import express, { Express } from 'express';
import cors from 'cors';
import { createProductRouter } from './routes/product.routes';
import { createEnquiryRouter } from './routes/enquiry.routes';
import { errorHandler } from './middleware/errorHandler.middleware';
import { ProductService } from './services/product.service';
import { EnquiryService } from './services/enquiry.service';

export function createApp(
  productService?: ProductService,
  enquiryService?: EnquiryService
): Express {
  const app = express();

  app.use(cors());
  app.use(express.json());

  // Health check route
  app.get('/health', (req, res) => {
    res.json({ status: 'ok', timestamp: new Date().toISOString() });
  });

  // Mount API routers
  app.use('/api', createProductRouter(productService));
  app.use('/api', createEnquiryRouter(enquiryService));

  // Global Error Handler
  app.use(errorHandler);

  return app;
}
