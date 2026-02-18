import RentalService from "@/service/RentalService";
import { Car, Rental } from "@/types";
import dayjs from "dayjs";
import { useTranslation } from "next-i18next";
import { useRouter } from "next/router";
import { FC, FormEvent, useState } from "react";

type Props = {
  car: Car;
};

const AddRentalTable: FC<Props> = ({ car }) => {
  const { t } = useTranslation();
  const router = useRouter();
  const [message, setMessage] = useState<string>("");
  const [rental, setRental] = useState<Partial<Rental>>({
    startDate: dayjs(),
    endDate: dayjs(),
    street: "",
    city: "",
    postalCode: 1000,
    phoneNumber: "",
    email: "",

  });

  const field = {
    marginBottom: "20px",
  };

  const handleDateChange = (
    field: "startDate" | "endDate",
    date: dayjs.Dayjs | string | null
  ) => {
    setRental((prevRental) => ({ ...prevRental, [field]: dayjs(date) }));
  };


  const validate = () => {
    if (rental.startDate === undefined || rental.endDate === undefined) {
      setMessage(t("rentals.dates"));
      return false;
    }
    if (rental.startDate > rental.endDate) {
      setMessage(t("rentals.form.error.endVstart"));
      return false;
    }
    if (rental.startDate < dayjs()) {
      setMessage(t("rentals.form.error.future"));
      return false;
    }
    if (rental.endDate < dayjs()) {
      setMessage(t("rentals.form.error.end"));
      return false;
    }
    if (rental.street === "" || rental.city === "" || rental.postalCode === 0) {
      setMessage(t("general.fill"));
      return false;
    }
    return true;
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();

    const formattedRental = {
      ...rental,
      id: rental.id ?? 0,
      startDate:
        dayjs(rental.startDate).format("YYYY-MM-DD").toString() +
        "T01:00:00.000+00:00",
      endDate: dayjs(rental.endDate).format("YYYY-MM-DD").toString(),
    };
    const check = validate();
    if (!check) {
      console.log("error");
    } else {
      const result = await RentalService.createRental(formattedRental, car.id ?? 0);
      if (result) {
        console.log("SUCCESS ", result);
        setMessage(t("rentals.success"));
        setTimeout(() => {
          router.push("/rentals");
        }, 2000);
      } else {
        setMessage(t("rentals.error"));
      }
      
    }
  };

  return (

    <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md">
      <h2>{t("rentals.search.car")}</h2>
  <div className="mb-6">
    <p className="text-black font-medium text-lg">{car.brand} {car.model}</p>
    <p className="text-black">{car.licensePlate}</p>
  </div>
  <div className="mb-6">
    <label className="block text-black font-medium mb-2">{t("rentals.search.start")}*:</label>
    <input
      type="date"
      value={dayjs(rental.startDate).format("YYYY-MM-DD")}
      onChange={(e) => handleDateChange("startDate", e.target.value)}
      className="bg-gray-100 border-gray-300 text-black rounded-md py-2 px-3 block w-full focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
    />
    <p className="text-red-500 mt-2">{message}</p>
  </div>
  <div className="mb-6">
    <label className="block text-black font-medium mb-2">{t("rentals.search.end")}*:</label>
    <input
      type="date"
      value={dayjs(rental.endDate).format("YYYY-MM-DD")}
      onChange={(e) => handleDateChange("endDate", e.target.value)}
      className="bg-gray-100 border-gray-300 text-black rounded-md py-2 px-3 block w-full focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
    />
  </div>
  <h2 className="text-black font-medium text-xl mb-4">{t("rentals.add.pickup")}</h2>
  <div className="mb-6">
    <label className="block text-black font-medium mb-2">{t("rentals.add.street")}:</label>
    <input
      type="text"
      value={rental.street}
      onChange={(e) => setRental({ ...rental, street: e.target.value })}
      className="bg-gray-100 border-gray-300 text-black rounded-md py-2 px-3 block w-full focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
    />
  </div>
  <div className="mb-6">
    <label className="block text-black font-medium mb-2">{t("rentals.add.postal")}:</label>
    <input
      type="number"
      value={rental.postalCode}
      onChange={(e) => setRental({ ...rental, postalCode: +e.target.value })}
      className="bg-gray-100 border-gray-300 text-black rounded-md py-2 px-3 block w-full focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
    />
  </div>
  <div className="mb-6">
    <label className="block text-black font-medium mb-2">{t("rentals.search.city")}*:</label>
    <input
      type="text"
      value={rental.city}
      onChange={(e) => setRental({ ...rental, city: e.target.value })}
      className="bg-gray-100 border-gray-300 text-black rounded-md py-2 px-3 block w-full focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
    />
  </div>
  <h2 className="text-black font-medium text-xl mb-4">{t("rentals.add.contact")}</h2>
  <div className="mb-6">
    <label className="block text-black font-medium mb-2">{t("rentals.add.phone")}*:</label>
    <input
      type="text"
      value={rental.phoneNumber}
      onChange={(e) => setRental({ ...rental, phoneNumber: e.target.value })}
      className="bg-gray-100 border-gray-300 text-black rounded-md py-2 px-3 block w-full focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
    />
  </div>
  <div className="mb-6">
    <label className="block text-black font-medium mb-2">{t("rentals.search.email")}*</label>
    <input
      type="text"
      value={rental.email}
      onChange={(e) => setRental({ ...rental, email: e.target.value })}
      className="bg-gray-100 border-gray-300 text-black rounded-md py-2 px-3 block w-full focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
    />
  </div>
  <button type="submit" className="bg-blue-500 hover:bg-blue-600 text-white font-medium py-2 px-4 rounded-md transition-colors duration-200">{t("general.submit")}</button>
  <p className="text-red-500 mt-4">{message}</p>
</form>
  
    
  );
};

export default AddRentalTable;
