
const path = require('path'); // Import the path module

module.exports = {
  debug: process.env.NODE_ENV === "development",
  i18n: {
    defaultLocale: 'en',
    locales: ['en', 'nl', 'es', 'de'],
  },
  localePath: path.resolve('./public/locales/'),
};
