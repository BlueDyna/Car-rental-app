import { useTranslation } from 'next-i18next';
import React from 'react';

const Unauthorized: React.FC = () => {
  const { t } = useTranslation();
  return (

    <main  style={{
      backgroundImage: `url('/images/car-rental-copyright-free.jpeg')`,
    }} >
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
    <div className="text-center p-8 bg-white rounded-lg shadow-lg">
      <h2 className="text-3xl font-bold text-red-600 mb-4">{t("unauthorized.")}</h2>
      <p className="text-black mb-6">{t("unauthorized.message")}</p>
      <a 
        href="/" 
        className="inline-block px-6 py-2 bg-orange-600 text-black rounded-md hover:bg-black hover:text-orange-600 transition-colors duration-200"
      >
         {t("unauthorized.back")}
      </a>
    </div>
  </div>
    </main>
    

  );
};

export default Unauthorized;