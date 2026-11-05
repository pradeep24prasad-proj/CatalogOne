import { Request, Response, NextFunction } from 'express';
import { EnquiryService } from '../services/enquiry.service';
import { enquirySchema } from '../validators/enquiry.validator';

export class EnquiryController {
  constructor(private enquiryService: EnquiryService) {}

  submitEnquiry = async (req: Request, res: Response, next: NextFunction): Promise<void> => {
    try {
      // 1. Zod request validation
      const parseResult = enquirySchema.safeParse(req.body);
      if (!parseResult.success) {
        res.status(400).json({
          error: 'Validation Error',
          details: parseResult.error.errors.map((e) => ({
            field: e.path.join('.'),
            message: e.message
          }))
        });
        return;
      }

      const enquiryData = parseResult.data;

      // 2. Process enquiry
      const result = await this.enquiryService.processEnquiry(enquiryData);
      res.status(201).json(result);
    } catch (error: any) {
      if (error?.message === 'PRODUCT_NOT_FOUND') {
        res.status(404).json({ error: 'Product not found. Cannot submit enquiry for non-existent product.' });
        return;
      }
      if (error?.message === 'EMAIL_DISPATCH_FAILED') {
        res.status(500).json({ error: 'Failed to dispatch email notification. Please try again later.' });
        return;
      }
      next(error);
    }
  };
}
