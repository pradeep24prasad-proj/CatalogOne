import { z } from 'zod';

export const enquirySchema = z.object({
  productId: z.number({ required_error: 'productId is required' }).int().positive(),
  productTitle: z.string({ required_error: 'productTitle is required' }).min(1),
  customerName: z.string({ required_error: 'customerName is required' }).min(1, 'Customer name cannot be empty'),
  email: z.string({ required_error: 'email is required' }).email('Invalid email address format'),
  phone: z.string({ required_error: 'phone is required' }).min(5, 'Phone number must be at least 5 characters'),
  message: z.string({ required_error: 'message is required' }).min(5, 'Enquiry message must be at least 5 characters'),
});

export type EnquiryInput = z.infer<typeof enquirySchema>;
