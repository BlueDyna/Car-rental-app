import { useTranslation } from "next-i18next";
import { useRouter } from "next/router";
import { useEffect } from "react";
import { getStoredLanguage, setStoredLanguage } from "./LaguageStorage";

const LanguageSwitcher: React.FC = () => {
  const { i18n } = useTranslation();
  const router = useRouter();

  useEffect(() => {
    const storedLang = getStoredLanguage();
    if (storedLang && storedLang !== i18n.language) {
      changeLanguage(storedLang);
    }
  }, []);

  const changeLanguage = (lng: string) => {
    const { pathname, asPath, query } = router;
    setStoredLanguage(lng);
    
    i18n.changeLanguage(lng).then(() => {
      router.push({ pathname, query }, asPath, {
        locale: lng,
        scroll: false
      });
    });
  };

  const getLanguageLabel = (lng: string) => {
    const labels: Record<string, string> = {
      en: "English",
      es: "Español",
      nl: "Nederlands",
      de: "Deutsch"
    };
    return labels[lng] || lng.toUpperCase();
  };

  return (
    <div className="flex items-center gap-1.5 p-1.5 bg-gray-50 rounded-lg shadow-sm">
      {["en", "es", "nl", "de"].map((lng) => {
        const isActive = i18n.language === lng;
        return (
          <button
            key={lng}
            onClick={() => changeLanguage(lng)}
            className={`
              relative px-3 py-1.5 
              text-sm font-medium
              rounded-md
              transition-all duration-200
              ${isActive ? 
                "bg-white text-indigo-600 shadow-sm ring-1 ring-indigo-100" : 
                "text-gray-600 hover:bg-white hover:text-indigo-600 hover:shadow-sm"
              }
              focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-1
              disabled:opacity-50 disabled:cursor-not-allowed
              group
            `}
            disabled={isActive}
            aria-pressed={isActive}
            title={`Switch to ${getLanguageLabel(lng)}`}
          >
            <span className="relative z-10 flex items-center gap-1.5">
              {lng.toUpperCase()}
              {isActive && (
                <span className="sr-only">(Current Language)</span>
              )}
            </span>
            {isActive && (
              <span className="absolute inset-0 bg-indigo-50/50 rounded-md" />
            )}
          </button>
        );
      })}
    </div>
  );
};

export default LanguageSwitcher;