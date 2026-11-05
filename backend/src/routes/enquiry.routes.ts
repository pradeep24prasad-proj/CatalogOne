import { Router } from 'express';
import { EnquiryController } from '../controllers/enquiry.controller';
import { EnquiryService } from '../services/enquiry.service';
import { FakeStoreProductRepository } from '../repositories/fakeStoreProduct.repository';
import { SmtpEmailProvider } from '../providers/smtpEmail.provider';

export function createEnquiryRouter(enquiryService?: EnquiryService): Router {
  const router = Router();
  const service = enquiryService || new EnquiryService(
    new FakeStoreProductRepository(),
    new SmtpEmailProvider()
  );
  const controller = new EnquiryController(service);

  router.post('/enquiries', controller.submitEnquiry);

  return router;
}
