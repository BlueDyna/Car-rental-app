import React, { useEffect, useState } from 'react';
import Link from 'next/link';
import { Car } from "@/types";
import { Car as CarIcon, Edit, Trash2, PlusCircle, AlertCircle } from 'lucide-react';
import { useTranslation } from 'next-i18next';

type Props = {
  car: Array<Car>;
}

const CarOverviewTable: React.FC<Props> = ({ car }) => {
  const {t} = useTranslation();
  const [stateCars, setStateCars] = useState<Car[] | null>([]);
  const role = localStorage.getItem("role");
  useEffect(() => {
    setStateCars(car);
  }, [car]);

  return (
    <div className="space-y-4">
      {/* Header Section */}
      <div className="flex justify-between items-center mb-6">
        <div className="flex items-center gap-2">
          <CarIcon className="text-orange-600" size={24} />
          <h2 className="text-2xl font-bold text-gray-800">{t("head.title.cars.overview")}</h2>
        </div>
        
      </div>

      {/* Empty State */}
      {stateCars?.length === 0 ? (
        <div className="flex flex-col items-center justify-center p-12 bg-white rounded-lg border-2 border-dashed border-gray-300">
          <AlertCircle size={48} className="text-gray-400 mb-4" />
          <h3 className="text-lg font-medium text-gray-900 mb-1">{t("cars.noCars")}</h3>
          <p className="text-gray-500 mb-4">{t("cars.start")}</p>
          <Link 
            href="/cars/add"
            className="inline-flex items-center gap-2 px-4 py-2 bg-orange-600 text-white rounded-lg hover:bg-orange-700 transition-colors"
          >
            <PlusCircle size={20} />
           {t("cars.new")}
          </Link>
        </div>
      ) : (

        <>
            <div>
              <Link
                href="/cars/add"
                className="inline-flex items-center gap-2 px-4 py-2 bg-orange-600 text-white rounded-lg hover:bg-orange-700 transition-colors"
              >
                <PlusCircle size={20} />
                {t("cars.new")}
              </Link>
            </div><div className="overflow-x-auto bg-white rounded-lg shadow">
              <table className="min-w-full divide-y divide-gray-200">
                <thead>
                  <tr className="bg-gray-50">
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    {t("cars.details")}
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    {t("cars.specs")}
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    {t("cars.features")}
                    </th>
                    <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    {t("cars.actions")}
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {stateCars?.map((carItem, index) => (
                    <tr
                      key={carItem.id ?? index}
                      className="hover:bg-gray-50 transition-colors"
                    >
                      {/* Car Details */}
                      <td className="px-6 py-4">
                        <div className="flex items-center">
                          <div className="flex-shrink-0 h-10 w-10">
                            <CarIcon className="h-10 w-10 text-gray-400" />
                          </div>
                          <div className="ml-4">
                            <div className="text-sm font-medium text-gray-900">
                              {carItem.brand} {carItem.model}
                            </div>
                            <div className="text-sm text-gray-500">
                              {carItem.licensePlate}
                            </div>
                          </div>
                        </div>
                      </td>

                      {/* Specifications */}
                      <td className="px-6 py-4">
                        <div className="text-sm text-gray-900">{t("cars.add.form.type")}: {carItem.type}</div>
                        <div className="text-sm text-gray-500">
                        {t("cars.add.form.seats.")}: {carItem.numberOfSeats} ({t("cars.add.form.seats.child")}: {carItem.numberOfChildSeats})
                        </div>
                      </td>

                      {/* Features */}
                      <td className="px-6 py-4">
                        <div className="space-y-1">
                          <div className="flex items-center gap-1">
                            <span className={`h-2 w-2 rounded-full ${carItem.haveFoldingRearSeats ? 'bg-green-400' : 'bg-gray-300'}`}></span>
                            <span className="text-sm text-gray-500">{t("cars.add.form.folding")}</span>
                          </div>
                          <div className="flex items-center gap-1">
                            <span className={`h-2 w-2 rounded-full ${carItem.hasTowBar ? 'bg-green-400' : 'bg-gray-300'}`}></span>
                            <span className="text-sm text-gray-500">{t("cars.add.form.towbar")}</span>
                          </div>
                        </div>
                      </td>

                      {/* Actions */}

                      
                      <td className="px-6 py-4 text-sm font-medium">
                        <div className="flex space-x-3">

                          {role !== "Owner" && ( 
                          <Link
                            href={`/rentals/add/${carItem.id}`}
                            className="text-orange-600 hover:text-orange-900 flex items-center gap-1"
                          >
                            <PlusCircle size={16} />
                            <span>{t("cars.rental")}</span>
                          </Link>
)}
                         


                          {role !== "Renter" && (
                               <Link
                               href={`/cars/delete/${carItem.id}`}
                               className="text-red-600 hover:text-red-900 flex items-center gap-1"
                             >
                               <Trash2 size={16} />
                               <span>{t("general.delete")}</span>
                             </Link>
                          )}
                         
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div></>
      )}
    </div>
  );
};

export default CarOverviewTable;