// utils/languageStorage.ts
export const LANGUAGE_KEY = 'preferred-language';

export const getStoredLanguage = () => {
  if (typeof window === 'undefined') return null;
  return localStorage.getItem(LANGUAGE_KEY);
};

export const setStoredLanguage = (lang: string) => {
  if (typeof window === 'undefined') return;
  localStorage.setItem(LANGUAGE_KEY, lang);
};