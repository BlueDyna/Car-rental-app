import { Notification } from "@/types";
import dayjs from "dayjs";
import { useTranslation } from "next-i18next";
import Link from "next/link";

type Props = {
  notification: Array<Notification>;
};

const NotificationOverviewTable: React.FC<Props> = ({ notification }) => {
  const { t } = useTranslation();

  return (
    <table className="min-w-full bg-white border border-gray-200">
      <thead>
        <tr className="bg-gray-100 text-gray-700">
          <th className="py-2 px-4 text-left">{t("notifications.type")}</th>
          <th className="py-2 px-4 text-left">{t("notifications.message")}</th>
        </tr>
      </thead>
      <tbody>
        {notification.map((notif) => (
          <Link href={`/notification/info/${notif.id}`} key={notif.id} passHref>
            <tr
              className="border-t text-black hover:bg-gray-400 cursor-pointer"
              role="row"
              aria-label={`${notif.type} notification`}
            >
              <td className="py-2 px-4">
                {notif.type} {dayjs(notif.timestamp).format("DD/MM/YYYY hh:mm")}
              </td>
              <td className="py-2 px-4 text-black">{notif.message}</td>
            </tr>
          </Link>
        ))}
      </tbody>
    </table>
  );
};

export default NotificationOverviewTable;
