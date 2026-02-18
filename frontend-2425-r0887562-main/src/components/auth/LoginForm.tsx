import UserService from "@/service/UserService";
import { loginInput, StatusMessage } from "@/types";
import { useRouter } from "next/router";
import { ChangeEvent, useState } from "react";
import { useTranslation } from "next-i18next";

export default function LoginForm() {
  const [passwordError, setPasswordError] = useState("");
  const [emailError, setEmailError] = useState("");
  const [statusMessage, setStatusMessage] = useState<StatusMessage[]>([]);
  const router = useRouter();
  const { t } = useTranslation("common");
  const [user, setUser] = useState<loginInput>({
    email: "",
    password: ""
  });

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setUser((prevUser) => ({
      ...prevUser,
      [name]: value,
    }));
  };

  const clearErrors = () => {
    setEmailError("");
    setPasswordError("");
    setStatusMessage([]);
  };

  const validate = (): boolean => {
    let result = true;

    if (!user.email) {
      setEmailError(t("login.error.email")); // Updated for i18n
      result = false;
    } else if (!user.password) {
      setPasswordError(t("login.error.password")); // Updated for i18n
      result = false;
    }
    return result;
  };

  const handleSubmit = async (event: any) => {
    event.preventDefault();
    clearErrors();
    if (validate()) {
      const login = await UserService.login(user);
      if (login.status == 200) {
        const authentication = await login.json();
        setStatusMessage([
          { message: t("login.messages.success"), type: "success" }, // Updated for i18n
        ]);
        sessionStorage.setItem("username", authentication.user.username);
        sessionStorage.setItem("token", authentication.token);
        sessionStorage.setItem("userId", authentication.user.id);
        sessionStorage.setItem("email", authentication.user.email);
        sessionStorage.setItem("role", authentication.user.role);
        setTimeout(() => {
          router.push("/");
        }, 2000);
      } else if (login.status == 401) {
        setStatusMessage([{ message: t("login.messages.error"), type: "error" }]); // Updated for i18n
      } else {
        setStatusMessage([
          {
            message: t("login.messages.systemError"), // Updated for i18n
            type: "error",
          },
        ]);
      }
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <h1 className="text-center text-3xl font-bold text-gray-900 mb-8">
          {t("login.")} {/* Uses i18n key */}
        </h1>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white py-8 px-4 shadow-md sm:rounded-lg sm:px-10">
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label 
                htmlFor="email" 
                className="block text-sm font-medium text-gray-700"
              >
                {t("login.email")} {/* Uses i18n key */}
              </label>
              <div className="mt-1">
                <input
                  type="email"
                  name="email"
                  id="email"
                  value={user.email}
                  onChange={handleChange}
                  className="appearance-none text-black block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 
                           focus:outline-none focus:ring-orange-500 focus:border-orange-500"
                />
              </div>
              {emailError && (
                <p className="mt-2 text-sm text-red-600">
                  {emailError}
                </p>
              )}
            </div>

            <div>
              <label 
                htmlFor="password" 
                className="block text-sm font-medium text-gray-700"
              >
                {t("login.password")} {/* Uses i18n key */}
              </label>
              <div className="mt-1">
                <input
                  type="password"
                  name="password"
                  id="password"
                  value={user.password}
                  onChange={handleChange}
                  className="appearance-none text-black  block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 
                           focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                />
              </div>
              {passwordError && (
                <p className="mt-2 text-sm text-red-600">
                  {passwordError}
                </p>
              )}
            </div>

            <div>
              <button
                type="submit"
                className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium 
                         text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 
                         focus:ring-blue-500 transition-colors duration-200"
              >
                {t("general.submit")} {/* Uses i18n key */}
              </button>
            </div>

            {statusMessage.map((message, index) => (
              <p 
                key={index} 
                className={`text-sm ${
                  message.type === 'error' ? 'text-red-600' : 
                  message.type === 'success' ? 'text-green-600' : 
                  'text-blue-600'
                }`}
              >
                {message.message}
              </p>
            ))}
          </form>
        </div>

        <div className="mt-6 text-center">
          <p className="text-sm text-gray-600">
            {t("login.noAccount")} {/* Uses i18n key */}
            {' '}
            <a 
              href="/signup" 
              className="font-medium text-orange-500 hover:text-black transition-colors duration-200"
            >
              {t("login.signup")} {/* Uses i18n key */}
            </a>
          </p>
        </div>
      </div>
    </div>
  );
}
