import { ConfigFactory } from '@nestjs/config';

const configuration: ConfigFactory = () => ({
  port: parseInt(process.env.PORT, 10) || 3000,
  database: {
    uri: process.env.DATABASE_URI,
  },
});

export default configuration;
