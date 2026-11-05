import { createApp } from './app';
import { env } from './config/env';

const app = createApp();

app.listen(env.PORT, () => {
  console.log(`🚀 CatalogHub Backend server listening on port ${env.PORT}`);
  console.log(`📦 FakeStore API target: ${env.FAKE_STORE_API_URL}`);
  console.log(`📧 SMTP Server target: ${env.SMTP_HOST}:${env.SMTP_PORT}`);
});
