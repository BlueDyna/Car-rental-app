import { Car, Mail, Phone, MapPin, Facebook, Twitter, Instagram } from "lucide-react";

const Footer = () => {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-gray-800 text-gray-300">
      <div className="max-w-7xl mx-auto px-4 py-8">
        {/* Main Footer Content */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-8">
          {/* Company Info */}
          <div className="space-y-4">
            <div className="flex items-center gap-2">
              <Car className="text-orange-500" size={24} />
              <h3 className="text-xl font-bold text-white">Car App</h3>
            </div>
            <p className="text-sm">
              Established in 2024, Car App has been providing exceptional car rental
              services with a commitment to quality and customer satisfaction.
            </p>
          </div>

          {/* Contact Information */}
          <div className="space-y-4">
            <h4 className="text-lg font-semibold text-white">Contact Us</h4>
            <div className="space-y-2">
              <div className="flex items-center gap-2">
                <Mail size={16} className="text-orange-500" />
                <a href="mailto:info@carapp.com" className="hover:text-orange-500 transition-colors">
                  info@carapp.com
                </a>
              </div>
              <div className="flex items-center gap-2">
                <Phone size={16} className="text-orange-500" />
                <a href="tel:+1234567890" className="hover:text-orange-500 transition-colors">
                  (123) 456-7890
                </a>
              </div>
              <div className="flex items-center gap-2">
                <MapPin size={16} className="text-orange-500" />
                <span>123 Car Street, Auto City, AC 12345</span>
              </div>
            </div>
          </div>

          {/* Social Links */}
          <div className="space-y-4">
            <h4 className="text-lg font-semibold text-white">Follow Us</h4>
            <div className="flex space-x-4">
              <a
                href="#"
                className="hover:text-orange-500 transition-colors"
                aria-label="Facebook"
              >
                <Facebook size={24} />
              </a>
              <a
                href="#"
                className="hover:text-orange-500 transition-colors"
                aria-label="Twitter"
              >
                <Twitter size={24} />
              </a>
              <a
                href="#"
                className="hover:text-orange-500 transition-colors"
                aria-label="Instagram"
              >
                <Instagram size={24} />
              </a>
            </div>
          </div>
        </div>

        {/* Bottom Bar */}
        <div className="border-t border-gray-700 pt-8">
          <div className="flex flex-col md:flex-row justify-between items-center space-y-4 md:space-y-0">
            <div className="text-sm">
              &copy; {currentYear} Car App. All rights reserved.
            </div>
            <div className="flex space-x-6 text-sm">
              <a href="/privacy" className="hover:text-orange-500 transition-colors">
                Privacy Policy
              </a>
              <a href="/terms" className="hover:text-orange-500 transition-colors">
                Terms of Service
              </a>
              <a href="/sitemap" className="hover:text-orange-500 transition-colors">
                Sitemap
              </a>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;