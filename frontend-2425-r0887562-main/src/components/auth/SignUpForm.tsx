import UserService from "@/service/UserService";
import { Role, User } from "@/types";
import { useTranslation } from "next-i18next";
import { useRouter } from "next/router";
import React, { useState } from "react";

// Define the UserRequest type based on the Java DTO structure

const SignUpForm: React.FC = () => {
  const { t } = useTranslation();
  const router = useRouter();
  const [user, setUser] = useState<User>({
    id: 0,
    username: "",
    password: "",
    email: "",
    role: Role.Owner
  });
  const [error, setError] = useState<string>("");
  const [statusMessage, setStatusMessage] = useState<string>("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setUser((prevUser) => ({
      ...prevUser,
      [name]: value,
    }));
  };

  const validate = () => {
    if (user.username === "") {
      setError(t("signup.error.username"));
      return false;
    }
    if (user.password === "") {
      setError(t("signup.error.password"));
      return false;
    }
    if (user.email === "") {
      setError(t("signup.error.email"));
      return false;
    }
   
    return true;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    if (!validate()) {
      return;
    }

    try {
     
        const response = await UserService.register(user);
        const data = await response.json();
        console.log(data);

      setStatusMessage(t("signup.messages.success"));
      setUser({
        id: 0,
        username: "",
        password: "",
        email: "",
        role: Role.Owner
      });

      setTimeout(() => {
        router.push("/login");
      }, 2000);
    } catch (error) {
      setError(t("signup.messages.error"));
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md">
        <h1 className="text-center text-3xl font-bold text-gray-900 mb-8">
          {t("signup.")}
        </h1>
      </div>

      <form onSubmit={handleSubmit} className="max-w-xl mx-auto p-8 bg-white shadow-xl rounded-xl space-y-6">        

        {/* Username field */}
        <div className="space-y-2">
          <label className="block text-gray-700 text-sm font-semibold">
            {t("signup.username")}
          </label>
          <input
            type="text"
            name="username"
            value={user.username}
            onChange={handleChange}
            className="w-full px-4 py-3 border text-black border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition duration-200"
            placeholder="Enter your username"
          />
        </div>

        {/* Email field */}
        <div className="space-y-2">
          <label className="block text-gray-700 text-sm font-semibold">
            {t("signup.email")}
          </label>
          <input
            type="email"
            name="email"
            value={user.email}
            onChange={handleChange}
            className="w-full px-4 py-3 border text-black border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition duration-200"
            placeholder="Enter your email"
          />
        </div>

        {/* Password field */}
        <div className="space-y-2">
          <label className="block text-gray-700 text-sm font-semibold">
            {t("signup.password")}
          </label>
          <input
            type="password"
            name="password"
            value={user.password}
            onChange={handleChange}
            className="w-full px-4 py-3 border text-black border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition duration-200"
            placeholder="Create a password"
          />
        </div>

        {/* Role selection */}
        <div className="space-y-2">
          <label className="block text-gray-700 text-sm font-semibold">
            {t("signup.role.")}
          </label>
          <select
            name="role"
            value={user.role}
            onChange={handleChange}
            className="w-full px-4 py-3 border text-black border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition duration-200 bg-white"
          >
            <option className="text-black" value="Owner">{t("signup.role.owner")}</option>
            <option className="text-black" value="Renter">{t("signup.role.renter")}</option>
          </select>
        </div>

        {/* Submit button */}
        <div className="pt-4">
          <button
            type="submit"
            className="w-full bg-blue-600 text-white font-semibold py-3 px-4 rounded-lg hover:bg-blue-700 
                     focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 
                     transform transition duration-200 hover:scale-[1.02]"
          >
            {t("general.submit")}
          </button>
        </div>

        {error && (
          <div className="bg-red-50 border-l-4 border-red-500 p-4 mb-6">
            <p className="text-red-700">{error}</p>
          </div>
        )}
        
        {statusMessage && (
          <div className="bg-green-50 border-l-4 border-green-500 p-4 mb-6">
            <p className="text-green-700">{statusMessage}</p>
          </div>
        )}

        {/* Sign in link */}
        <div className="text-center mt-6">
          <p className="text-sm text-gray-600">
            {t("signup.haveAccount")}{' '}
            <a 
              href="/login" 
              className="font-medium text-orange-500 hover:text-black transition-colors duration-200"
            >
              {t("signup.signin")}
            </a>
          </p>
        </div>
      </form>
    </div>
  );
};

export default SignUpForm;
