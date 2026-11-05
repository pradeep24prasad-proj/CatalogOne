export interface EnquiryRequest {
  productId: number;
  productTitle: string;
  customerName: string;
  email: string;
  phone: string;
  message: string;
}

export interface EnquiryResponse {
  success: boolean;
  message: string;
  enquiryId?: string;
}
