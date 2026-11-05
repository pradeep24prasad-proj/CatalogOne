import { Request, Response, NextFunction } from 'express';
import { ProductService } from '../services/product.service';

export class ProductController {
  constructor(private productService: ProductService) {}

  getFeaturedProducts = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
      const products = await this.productService.getFeaturedProducts();
      res.json(products);
    } catch (error) {
      next(error);
    }
  };

  getCategories = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
      const categories = await this.productService.getCategories();
      res.json(categories);
    } catch (error) {
      next(error);
    }
  };

  getProductsByCategory = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
      const { category } = req.params;
      const products = await this.productService.getProductsByCategory(category);
      res.json(products);
    } catch (error) {
      next(error);
    }
  };

  getProductsOrSearch = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
      const searchQuery = req.query.search as string | undefined;
      if (searchQuery !== undefined) {
        const results = await this.productService.searchProducts(searchQuery);
        res.json(results);
      } else {
        // If no search query, return empty list or all products if requested
        const results = await this.productService.searchProducts('');
        res.json(results);
      }
    } catch (error) {
      next(error);
    }
  };

  getProductById = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
      const id = parseInt(req.params.id, 10);
      if (isNaN(id)) {
        res.status(400).json({ error: 'Invalid product ID' });
        return;
      }
      const product = await this.productService.getProductById(id);
      if (!product) {
        res.status(404).json({ error: 'Product not found' });
        return;
      }
      res.json(product);
    } catch (error) {
      next(error);
    }
  };
}
