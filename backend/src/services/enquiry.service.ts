import { IProductRepository } from '../repositories/product.repository.interface';
import { IEmailProvider } from '../providers/email.provider.interface';
import { EnquiryRequest, EnquiryResponse } from '../types/enquiry.types';

export class EnquiryService {
  constructor(
    private productRepository: IProductRepository,
    private emailProvider: IEmailProvider
  ) {}

  async processEnquiry(enquiry: EnquiryRequest): Promise<EnquiryResponse> {
    // 1. Verify product exists
    const product = await this.productRepository.getProductById(enquiry.productId);
    if (!product) {
      throw new Error(`PRODUCT_NOT_FOUND`);
    }

    // 2. Send email notification
    const emailSent = await this.emailProvider.sendEnquiryNotification(enquiry);
    if (!emailSent) {
      throw new Error(`EMAIL_DISPATCH_FAILED`);
    }

    return {
      success: true,
      message: 'Enquiry submitted successfully and notification email dispatched.',
      enquiryId: `enq_${Date.now()}`
    };
  }
}
