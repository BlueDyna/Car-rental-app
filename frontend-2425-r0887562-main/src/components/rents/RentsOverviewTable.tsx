import { Rent } from "@/types";
import dayjs from "dayjs";
import { useTranslation } from "next-i18next";
import Link from "next/link";

type Props = {
    rent: Array<Rent>;
}

const RentsOverviewTable: React.FC<Props> = ({ rent }) => {
    const { t } = useTranslation();
    
    return (
        <div className="w-full overflow-x-auto shadow-sm rounded-lg">
            <table className="w-full text-sm text-left">
                <thead className="text-xs text-gray-700 uppercase bg-gray-50">
                    <tr>
                        <th className="px-6 py-3">{t("rentals.search.car")}</th>
                        <th className="px-6 py-3">{t("rent.form.search.start")}</th>
                        <th className="px-6 py-3">{t("rent.form.search.end")}</th>
                        <th className="px-6 py-3">{t("rent.overview.owner")}</th>
                        <th className="px-6 py-3">{t("rent.overview.renter")}</th>
                        <th className="px-6 py-3">{t("rent.status.")}</th>
                        <th className="px-6 py-3">{t("cars.actions")}</th>
                    </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                    {rent.map((rent, index) => (
                        <tr 
                            key={index}
                            className="hover:bg-gray-50 transition-colors"
                        >
                            <td className="px-6 py-4 whitespace-nowrap  text-black">
                                {rent.rental?.car?.brand} {rent.rental?.car?.model} {rent.rental?.car?.licensePlate}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap  text-black">
                                {dayjs(rent.rental?.startDate).format("DD/MM/YYYY")}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap  text-black">
                                {dayjs(rent.rental?.endDate).format("DD/MM/YYYY")}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap  text-black">
                                {rent.rental?.email}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap  text-black">
                                {rent.renterMail}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                                <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full
                                    ${rent.status === "PENDING" ? "bg-yellow-100 text-yellow-800" : 
                                      rent.status === "CONFIRMED" ? "bg-green-100 text-green-800" : 
                                      rent.status === "CANCELLED" ? "bg-red-100 text-red-800" : 
                                      "bg-gray-100 text-gray-800"}`}>
                                    {rent.status}
                                </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm">
                                {rent.status !== "PENDING" && (
                                    <Link 
                                        href={`/rents/cancel/${rent.id}`}
                                        className="text-red-600 hover:text-red-900 font-medium"
                                    >
                                        {t("general.cancel")}
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

export default RentsOverviewTable;