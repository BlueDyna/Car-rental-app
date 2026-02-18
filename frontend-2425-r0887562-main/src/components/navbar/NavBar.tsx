import { useState } from "react";
import { ChevronDown, Car, Key, Home, Calendar, ClipboardList, MailIcon, Menu } from "lucide-react";
import { useRouter } from "next/router";
import { useTranslation } from "next-i18next";
import LanguageSwitcher from "../util/Language";

const NavBar = () => {
  const [activeDropdown, setActiveDropdown] = useState<string | null>(null);
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const role = sessionStorage.getItem("role");
  const token = sessionStorage.getItem("token");
  const router = useRouter();
  const { t } = useTranslation();

  const toggleDropdown = (dropdown: string) => {
    setActiveDropdown(activeDropdown === dropdown ? null : dropdown);
  };

  const Logout = () => {
   sessionStorage.clear();
    setTimeout(() => {
      router.push("/login");
    }, 2000);
  };

  const NavItem = ({ text, href, icon: Icon }: { text: string; href: string; icon: React.ComponentType<any> }) => (
    <a href={href} className="flex items-center gap-2 px-4 py-2 text-gray-700 hover:bg-orange-50 transition-colors duration-200">
      <Icon size={18} />
      <span>{text}</span>
    </a>
  );

  const DropdownButton = ({ text, isActive, onClick, icon: Icon }: { text: string; isActive: boolean; onClick: () => void; icon: React.ComponentType<any> }) => (
    <button onClick={onClick} className="flex items-center gap-2 px-4 py-2 text-white hover:text-orange-100 focus:outline-none transition-colors duration-200">
      <Icon size={18} />
      <span>{text}</span>
      <ChevronDown size={16} className={`transform transition-transform duration-200 ${isActive ? "rotate-180" : ""}`} />
    </button>
  );

  return (
    <nav className="bg-orange-700 shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          
          
          {/* Mobile menu button */}
          <div className="sm:hidden">
            <button onClick={() => setMobileMenuOpen(!mobileMenuOpen)} className="text-white hover:text-orange-100 focus:outline-none">
              <Menu size={24} />
            </button>
          </div>

          

          {/* Desktop Menu */}
          <div className="hidden sm:flex sm:items-center sm:space-x-8">

          <div className="flex items-center">
            <a href="/" className="flex items-center gap-2 text-white hover:text-orange-100 transition-colors duration-200">
              <Home size={18} />
              <span className="font-medium">{t("header.navbar.home")}</span>
            </a>
          </div>

            {role !== "Renter" && (
              <div className="relative">
                <DropdownButton
                  text={t("header.navbar.cars")}
                  isActive={activeDropdown === "cars"}
                  onClick={() => toggleDropdown("cars")}
                  icon={Car}
                />
                {activeDropdown === "cars" && (
                  <div className="absolute left-0 mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none z-10">
                    <div className="py-1">
                      <NavItem text={t("head.title.cars.overview")} href="/cars" icon={Car} />
                      <NavItem text={t("head.title.cars.add")} href="/cars/add" icon={Car} />
                    </div>
                  </div>
                )}
              </div>
            )}

            <div className="relative">
              <DropdownButton
                text={t("header.navbar.rentals")}
                isActive={activeDropdown === "rentals"}
                onClick={() => toggleDropdown("rentals")}
                icon={Calendar}
              />
              {activeDropdown === "rentals" && (
                <div className="absolute left-0 mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none z-10">
                  <div className="py-1">
                    <NavItem text={t("head.title.rentals.overview")} href="/rentals" icon={Calendar} />
                    <NavItem text={t("head.title.rentals.create")} href="/cars" icon={Calendar} />
                  </div>
                </div>
              )}
            </div>


            {role !== "Owner" && (

                  <div className="relative">
                  <DropdownButton
                    text={t("header.navbar.rents")}
                    isActive={activeDropdown === "rents"}
                    onClick={() => toggleDropdown("rents")}
                    icon={ClipboardList}
                  />
                  {activeDropdown === "rents" && (
                    <div className="absolute left-0 mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none z-10">
                      <div className="py-1">
                        <NavItem text={t("head.title.rents.overview")} href="/rents" icon={ClipboardList} />
                        <NavItem text={t("head.title.rents.create")} href="/rentals" icon={ClipboardList} />
                      </div>
                    </div>
                  )}
                </div>

            )}
            

            

            <div className="relative">
              <DropdownButton
                text={t("header.navbar.notifications")}
                isActive={activeDropdown === "notifications"}
                onClick={() => toggleDropdown("notifications")}
                icon={MailIcon}
              />
              {activeDropdown === "notifications" && (
                <div className="absolute left-0 mt-2 w-56 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 focus:outline-none z-10">
                  <div className="py-1">
                    <NavItem text={t("head.title.notifications.overview")} href="/notification" icon={MailIcon} />
                  </div>
                </div>
              )}
            </div>
          </div>

          {/* languege component buttons */}
          < LanguageSwitcher />

          <div>
            {token === null ? (
              <a href="/login" className="flex items-center gap-2 text-white hover:text-orange-100 transition-colors duration-200">
                <Key size={18} />
                <span className="font-medium">{t("header.navbar.login")}</span>
              </a>
            ) : (
              <button onClick={Logout} className="flex items-center gap-2 text-white hover:text-orange-100 transition-colors duration-200">
                <Key size={18} />
                <span className="font-medium">{t("header.navbar.logout")}</span>
              </button>
            )}
          </div>
        </div>
      </div>

      {/* Mobile Menu */}
      {mobileMenuOpen && (
        <div className="sm:hidden bg-orange-600 px-2 pt-2 pb-3 space-y-1">
          <NavItem text={t("header.navbar.home")} href="/" icon={Home} />
          {role !== "Renter" && <NavItem text={t("header.navbar.cars")} href="/cars" icon={Car} />}
          <NavItem text={t("header.navbar.rentals")} href="/rentals" icon={Calendar} />
          <NavItem text={t("header.navbar.rents")} href="/rents" icon={ClipboardList} />
          <NavItem text={t("header.navbar.notifications")} href="/notification" icon={MailIcon} />
        </div>
      )}
    </nav>
  );
};

export default NavBar;
