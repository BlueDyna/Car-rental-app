import { Rental } from "@/types";
import dayjs from "dayjs";
import { useTranslation } from "next-i18next";
import Link from "next/link";

type Props = {
    rental: Array<Rental>;
}

const RentalOverviewTable: React.FC<Props> = ({ rental }) => {
    const role = localStorage.getItem("role");
    const { t } = useTranslation();
    return (
        <div className="w-full overflow-x-auto rounded-lg shadow-md">
            <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            {t("rent.search.car")} {/* Car */}
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            {t("rent.form.search.start")} {/* Start Date */}
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            {t("rent.form.search.end")} {/* End Date */}
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            {t("rentals.search.city")} {/* City */}
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            {t("rentals.search.email")} {/* City */}
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            {t("rentals.add.street")} {/* Street */}
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        {t("cars.actions")}
                        {/* Actions */}
                        </th>
                    </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                    {rental.map((rental, index) => (
                        <tr 
                            key={index} 
                            className="hover:bg-gray-50 transition-colors duration-200"
                        >
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                {rental.car?.brand} {rental.car?.model} 
                                <span className="text-gray-500 ml-1">
                                    {rental.car?.licensePlate}
                                </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                {dayjs(rental.startDate).format("DD/MM/YYYY")}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                {dayjs(rental.endDate).format("DD/MM/YYYY")}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                {rental.city} 
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                {rental.email}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                {rental.street}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium space-x-3">
                               
                               {role !== "Renter" &&  (  
                                <Link 
                                    href={`/rents/add/${rental.id}`}
                                    className="text-blue-600 hover:text-blue-900 bg-blue-50 px-3 py-1 rounded-md transition-colors duration-200"
                                >
                                    {t("general.rent")} {/* Rent */}
                                </Link>)}
                              
                                {role !== "Owner" &&  (
                                <Link 
                                    href={`/rentals/cancel/${rental.id}`}
                                    className="text-red-600 hover:text-red-900 bg-red-50 px-3 py-1 rounded-md transition-colors duration-200"
                                >
                                    {t("general.cancel")} {/* Cancel */}
                                </Link>
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default RentalOverviewTable;