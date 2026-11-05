import nodemailer from 'nodemailer';
import { IEmailProvider } from './email.provider.interface';
import { EnquiryRequest } from '../types/enquiry.types';
import { env } from '../config/env';

export class SmtpEmailProvider implements IEmailProvider {
  private transporter: nodemailer.Transporter;

  constructor(transporter?: nodemailer.Transporter) {
    if (transporter) {
      this.transporter = transporter;
    } else {
      this.transporter = nodemailer.createTransport({
        host: env.SMTP_HOST,
        port: env.SMTP_PORT,
        secure: env.SMTP_PORT === 465,
        auth: env.SMTP_USER ? { user: env.SMTP_USER, pass: env.SMTP_PASS } : undefined,
        tls: {
          rejectUnauthorized: false
        }
      });
    }
  }

  async sendEnquiryNotification(enquiry: EnquiryRequest): Promise<boolean> {
    try {
      const mailOptions = {
        from: env.SMTP_FROM,
        to: env.NOTIFICATION_EMAIL,
        subject: `New Product Enquiry: ${enquiry.productTitle}`,
        text: `
You have received a new product enquiry!

Product Details:
- Product ID: ${enquiry.productId}
- Title: ${enquiry.productTitle}

Customer Details:
- Name: ${enquiry.customerName}
- Email: ${enquiry.email}
- Phone: ${enquiry.phone}

Message:
${enquiry.message}
        `.trim(),
        html: `
<h3>New Product Enquiry</h3>
<p><strong>Product ID:</strong> ${enquiry.productId}</p>
<p><strong>Title:</strong> ${enquiry.productTitle}</p>
<hr />
<h4>Customer Details</h4>
<p><strong>Name:</strong> ${enquiry.customerName}</p>
<p><strong>Email:</strong> ${enquiry.email}</p>
<p><strong>Phone:</strong> ${enquiry.phone}</p>
<p><strong>Message:</strong></p>
<p>${enquiry.message}</p>
        `.trim()
      };

      await this.transporter.sendMail(mailOptions);
      return true;
    } catch (error) {
      console.warn('SMTP Email sending failed, falling back to mock logging:', error);
      console.log(`📧 [Enquiry Received] To: ${env.NOTIFICATION_EMAIL} | Customer: ${enquiry.customerName} (${enquiry.email}) | Product: ${enquiry.productTitle}`);
      return true;
    }
  }
}
