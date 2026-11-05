import { EnquiryRequest } from '../types/enquiry.types';

export interface IEmailProvider {
  sendEnquiryNotification(enquiry: EnquiryRequest): Promise<boolean>;
}
