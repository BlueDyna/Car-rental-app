import RentService from "@/service/RentService";
import { Rent, Rental } from "@/types";
import dayjs from "dayjs";
import { useTranslation } from "next-i18next";
import { useRouter } from "next/router";
import { FC, FormEvent, useState } from "react";

type Props = {
    rental: Rental;
  };
  
  const AddRentTable: FC<Props> = ({ rental }) => {
    const { t } = useTranslation();
    const router = useRouter();
    const [message, setMessage] = useState<string>("");
    const [error, setError] = useState<string>("");
    const [rent, setRent] = useState<Partial<Rent>>({
        renterPhoneNumber: "",       
    renterMail: "",             
    nationalRegisterNumber: "",  
    birthDate: dayjs(),              
    drivingLicenseNumber: "" 
                      
      
    });
  
    const field = {
      marginBottom: "20px",
    };
  
  
    const validate = () => {
        if (rent.renterPhoneNumber === "" || rent.renterMail === "" || rent.nationalRegisterNumber === "" || rent.birthDate === "" || rent.drivingLicenseNumber === "") {
          setError(t("general.fill"));
            return false;
        }
        if (rent.birthDate === undefined) {
          setError("Please select a birth date");
            return false;
        } if (!rent.drivingLicenseNumber || !/[0-9]{9}/.test(rent.drivingLicenseNumber)) {
          setError("Please enter a valid driving license number");
          return false;
      }
      
        if (!rent.nationalRegisterNumber || !/[0-9]{11}/.test(rent.nationalRegisterNumber)) {
          setError("Please enter a national register number");
            return false;
        }


        return true;


    };

    const handleDateChange = (
        field: "birthDate" ,
        date: dayjs.Dayjs | string | null
      ) => {
        setRent((prevRent) => ({ ...prevRent, [field]: dayjs(date) }));
      };
    
  
    const handleSubmit = async (e: FormEvent) => {
      e.preventDefault();
  
      const formattedRental = {
        ...rent,
        id: rent.id ?? 0,
        birthDate: dayjs(rent.birthDate).format("YYYY-MM-DD").toString(),
          
      };
      const check = validate();
      if (!check) {
        console.log("error");
      } else {
       const result = await RentService.createRent(formattedRental, rental.id ?? 0);
        if (result.status == 200) {
          console.log("SUCCESS ", result);
          setMessage(t("rent.succes"));
          setTimeout(() => {
          router.push("/rents");
          }, 2000);
        }
        else {
          console.log("ERROR ", result);
          setError(t("rent.error"));
        }
       
      } 
    };
  
    return (
      <form onSubmit={handleSubmit}>
      <div>
        <h2 className="text-xl font-semibold mb-2 text-black">{t("rentals.search.car")}</h2>
        <p className="text-gray-700">
          {rental.car?.brand} {rental.car?.model}
          {rental.car?.licensePlate}
        </p>
      </div>
      
      <div className="mt-4">
        <h2 className="text-xl font-semibold mb-2  text-black">{t("rent.add.period")}</h2>
        <p className="text-gray-700">
          {dayjs(rental.startDate).format("DD/MM/YYYY")} - {dayjs(rental.endDate).format("DD/MM/YYYY")}
        </p>
      </div>
     
      <div className="space-y-4 mt-4">
        <div className="flex flex-col">
          <label className="mb-1  text-black">{t("rent.form.phone")}:</label>
          <input
            type="text"
            value={rent.renterPhoneNumber}
            onChange={(e) => setRent({ ...rent, renterPhoneNumber: e.target.value })}
            className="border rounded px-3 py-2  text-black"
          />
        </div>
        
        <div className="flex flex-col">
          <label className="mb-1  text-black">{t("rent.form.email")}*:</label>
          <input
            type="text"
            value={rent.renterMail}
            onChange={(e) => setRent({ ...rent, renterMail: e.target.value })}
            className="border rounded px-3 py-2  text-black"
          />
        </div>
        
        <div className="flex flex-col">
          <label className="mb-1  text-black">{t("rent.form.ssn")}*:</label>
          <input
            type="text"
            value={rent.nationalRegisterNumber}
            onChange={(e) => setRent({ ...rent, nationalRegisterNumber: e.target.value })}
            className="border rounded px-3 py-2  text-black"
          />
        </div>
        
        <div className="flex flex-col">
          <label className="mb-1  text-black">{t("rent.form.birthdate")}*:</label>
          <input
            type="date"
            value={dayjs(rent.birthDate).format("YYYY-MM-DD")}
            onChange={(e) => handleDateChange("birthDate", e.target.value)}
            className="border rounded px-3 py-2  text-black"
          />
        </div>
        
        <div className="flex flex-col">
          <label className="mb-1  text-black">{t("rent.form.license")}*:</label>
          <input
            type="text"
            value={rent.drivingLicenseNumber}
            onChange={(e) => setRent({ ...rent, drivingLicenseNumber: e.target.value })}
            className="border rounded px-3 py-2  text-black"
          />
        </div>
      </div>
      
      <button 
        type="submit" 
        className="mt-4 w-full bg-orange-500 text-white py-2 rounded hover:bg-slate-400"
      >
        {t("general.submit")}
      </button>
      
      {error && <p className="text-red-500 text-center mt-4">{error}</p>}
      {message && <p className="text-green-500 text-center mt-4">{message}</p>}
     </form>
      
    );
  };
  
  export default AddRentTable;
  