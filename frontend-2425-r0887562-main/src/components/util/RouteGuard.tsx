import { useState, useEffect } from "react";
import { useRouter } from "next/router";

export { RouteGuard };

function RouteGuard({ children }: any) {
  const router = useRouter();
  const [authorized, setAuthorized] = useState(false);

  useEffect(() => {
    authCheck(router.asPath);
    const hideContent = () => setAuthorized(false);
    router.events.on("routeChangeStart", hideContent);
    router.events.on("routeChangeComplete", authCheck);
    return () => {
      router.events.off("routeChangeStart", hideContent);
      router.events.off("routeChangeComplete", authCheck);
    };
  }, []);

  function authCheck(url: any) {
    const publicPaths = ["/login", "/signup"];
    const restrictedPathsRents = ["/rents", /\/rents\/[0-9]+/];
    const restrictedPathsCars = ["/cars", "/cars/add"];
    const restrictedPathsRentals = ["/rentals", "/rentals/add"];
    const path = url.split("?")[0];
    const token = sessionStorage.getItem("token");
    const jwt = require("jsonwebtoken");
    let role = null;

    if (token) {
      role = jwt.decode(token)?.role.toString();
    }
    const isRestrictedPath = (restrictedPaths: string[]) => {
      return restrictedPaths.some((restrictedPath) =>
        path.startsWith(restrictedPath)
      );
    };

    if (!token && !publicPaths.includes(path)) {
      setUnauthorizedAndRedirect("/login");
    } else if (
      token &&
      isRestrictedPath(restrictedPathsRents.map(String)) &&
      !["Admin", "Renter"].includes(role)
    ) {
      setUnauthorizedAndRedirect("/unauthorized");
    } else if (
      token &&
      isRestrictedPath(restrictedPathsCars) &&
      !["Admin", "Owner"].includes(role)
    ) {
      setUnauthorizedAndRedirect("/unauthorized");
    } else if (
      token &&
      isRestrictedPath(restrictedPathsRentals) &&
      !["Admin", "Renter","Owner"].includes(role)
    ) {
      setUnauthorizedAndRedirect("/unauthorized");
    } else {
      setAuthorized(true);
    }
  }

  function setUnauthorizedAndRedirect(path: string) {
    setAuthorized(false);
    router.push(path, path, { locale: false });
  }

  return authorized && children;
}
