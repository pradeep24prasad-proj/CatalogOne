import dotenv from 'dotenv';
import path from 'path';

dotenv.config({ path: path.resolve(__dirname, '../../.env') });

export const env = {
  PORT: process.env.PORT ? parseInt(process.env.PORT, 10) : 3000,
  FAKE_STORE_API_URL: process.env.FAKE_STORE_API_URL || 'https://fakestoreapi.com',
  EMAIL_PROVIDER: process.env.EMAIL_PROVIDER || 'smtp',
  SMTP_HOST: process.env.SMTP_HOST || 'localhost',
  SMTP_PORT: process.env.SMTP_PORT ? parseInt(process.env.SMTP_PORT, 10) : 1025,
  SMTP_USER: process.env.SMTP_USER || '',
  SMTP_PASS: process.env.SMTP_PASS || '',
  SMTP_FROM: process.env.SMTP_FROM || 'noreply@cataloghub.com',
  NOTIFICATION_EMAIL: process.env.NOTIFICATION_EMAIL || 'admin@cataloghub.com',
};
