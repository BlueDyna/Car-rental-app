import pkg from "./next-i18next.config.js";

const { i18n } = pkg;

/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  i18n
};

export default {
 
   ...nextConfig
} ;