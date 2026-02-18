import CarService from "@/service/CarService";
import { Car } from "@/types";
import { useTranslation } from "next-i18next";
import router from "next/router";
import { useState } from "react";


const AddCarForm = () => {
  const [error, setError] = useState<string>("");
  const [statusMessage, setStatusMessage] = useState<string>("");
  const {t} = useTranslation();
  const [car, setCar] = useState<Car>({
    brand: "",
    model: "",
    type: "",
    licensePlate: "",
    numberOfSeats: 0,
    numberOfChildSeats: 0,
    haveFoldingRearSeats: false,
    hasTowBar: false,
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setCar((prevCar) => ({
      ...prevCar,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const validate = () => {
    if (car.brand === "") {
      setError(t("cars.add.error.brand.required"));
      return false;
    }
    if (car.model === "") {
      setError(t("cars.add.error.model.required"));
      return false;
    }
    if (car.type === "") {
      setError(t("cars.add.error.type"));
      return false;
    }
    if (car.licensePlate === "") {
      setError(t("cars.add.error.plate"));
      return false;
    }

        
    return true

  }

  const addCar = async () => {
    if (!validate()) {
      setStatusMessage(t("general.fill"));}
    // } else {
      try {
        const result = await CarService.createCar(car);
        if (result) {
          console.log("SUCCESS ", result);
          setStatusMessage(t("cars.success"));
          setTimeout(() => {
            router.push("/cars");
          }, 2000);
        } else {
         console.log("ERROR ", result);
        }
    
      } catch (error) {
        setStatusMessage(t("error.something"));
      }
    
    }
   

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Car added:", car);
    // Reset form
    setCar({
      brand: "",
      model: "",
      type: "",
      licensePlate: "",
      numberOfSeats: 0,
      numberOfChildSeats: 0,
      haveFoldingRearSeats: false,
      hasTowBar: false,
    });


  };

  return (
    <form onSubmit={handleSubmit} className="max-w-lg mx-auto p-6 bg-white shadow-lg rounded-lg">
  {/* Brand */}
  <div className="mb-4">
    <label className="block text-gray-700 text-sm font-bold mb-2">{t("cars.add.form.brand")}*:</label>
    <input
      type="text"
      name="brand"
      value={car.brand}
      onChange={handleChange}
      className="w-full px-3 py-2 border text-black border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
    />
  </div>

  {/* Model */}
  <div className="mb-4">
    <label className="block text-gray-700 text-sm font-bold mb-2">{t("cars.add.form.model")}*:</label>
    <input
      type="text"
      name="model"
      value={car.model}
      onChange={handleChange}
      className="w-full px-3 py-2 border  text-black border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
    />
  </div>

  {/* Type */}
  <div className="mb-4">
    <label className="block text-gray-700 text-sm font-bold mb-2">{t("cars.add.form.type")}*:</label>
    <input
      type="text"
      name="type"
      value={car.type}
      onChange={handleChange}
      className="w-full px-3 py-2 border  text-black border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
    />
  </div>

  {/* License Plate */}
  <div className="mb-4">
    <label className="block text-gray-700 text-sm font-bold mb-2">{t("cars.add.form.plate")}*:</label>
    <input
      type="text"
      name="licensePlate"
      value={car.licensePlate}
      onChange={handleChange}
      className="w-full px-3 py-2 border  text-black border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
    />
  </div>

  {/* Number of Seats */}
  <div className="mb-4">
    <label className="block text-gray-700 text-sm font-bold mb-2">{t("cars.add.form.seats.")}:</label>
    <input
      type="number"
      name="numberOfSeats"
      value={car.numberOfSeats}
      onChange={handleChange}
      className="w-full px-3 py-2 border  text-black border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
    />
  </div>

  {/* Number of Child Seats */}
  <div className="mb-4">
    <label className="block text-gray-700 text-sm font-bold mb-2">{t("cars.add.form.seats.child")}:</label>
    <input
      type="number"
      name="numberOfChildSeats"
      value={car.numberOfChildSeats}
      onChange={handleChange}
      className="w-full px-3 py-2 border  text-black border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
    />
  </div>

  {/* Folding Rear Seats */}
  <div className="mb-4">
    <label className="block text-gray-700 text-sm font-bold mb-2">{t("cars.add.form.folding")}:</label>
    <input
      type="checkbox"
      name="haveFoldingRearSeats"
      checked={car.haveFoldingRearSeats}
      onChange={handleChange}
      className="h-5 w-5 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
    />
  </div>

  {/* Has Tow Bar */}
  <div className="mb-4">
    <label className="block text-gray-700 text-sm font-bold mb-2">{t("cars.add.form.towbar")}:</label>
    <input
      type="checkbox"
      name="hasTowBar"
      checked={car.hasTowBar}
      onChange={handleChange}
      className="h-5 w-5 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
    />
  </div>

  {/* Submit Button */}
  <div>
    <button
    onClick={addCar}
      type="submit"
      className="w-full bg-orange-400 text-black font-bold py-2 px-4 rounded-lg hover:bg-white focus:outline-none focus:ring-2 focus:ring-orange-500"
    >
      {t("general.submit")}
    </button>
  </div>

  {/* Error Message */}
  {error && <p className="text-red-500 text-center mt-4">{error}</p>}

  {/* Error Message */}
  {statusMessage && <p className="text-green-500 text-center mt-4">{statusMessage}</p>}
   
</form>

  );
};

export default AddCarForm;
