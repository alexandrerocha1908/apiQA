package projeto.flavia

import groovy.time.TimeCategory

import javax.servlet.http.HttpServletRequest
import java.text.SimpleDateFormat

@Singleton
class DateUtil {

    Date getDateByHeaderNow (HttpServletRequest request) {
        return getDate(DataUtil.instance.getLong(request.getHeader("now")))
    }

    Date addOffset(Date date, Long offset) {
        return getDate(date.getTime() + offset.longValue())
    }

    Date clearTimeAndAddThreeHours(Date date) {
        Date copyDate = new Date(date.getTime())

        use (TimeCategory) {
            return copyDate.clearTime() + 3.hours
        }
    }

    Date clearTimeAndAddThreeHours(long timestamp) {
        Date date = getDate(timestamp)
        return clearTimeAndAddThreeHours(date)
    }

    String formatDate (Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format)
        return simpleDateFormat.format(date)
    }

    Long formatDate (Date date) {
        return date.getTime()
    }

    String formatDate (Calendar calendar, String format) {
        return formatDate(calendar.getTime(), format)
    }

    String formatDatetoISO8601 (Calendar calendar) {
        return formatDate(calendar, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }

    String formatDateToTime (Date date) {
        return formatDate(date, "HH:mm")
    }

    String formatDateToTimeWithSeconds (Date date) {
        return formatDate(date, "HH:mm:ss")
    }

    Date getDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy")
        format.setLenient(false)

        return format.parse(dateString)
    }

    Integer retrieveSemester (monthId) {
        if (monthId <= 5) {
            return 1
        } else {
            return 2
        }
    }

    Date getDate (Long dateLong) throws IllegalArgumentServiceException {
        return new Date(dateLong)
    }

    Date getFirstDayOfYear (int year) {
        Calendar initialDate = Calendar.getInstance()
        initialDate.set(year, 1, 1)

        return initialDate.getTime()
    }

    String getMonthInitials (monthId) {
        String result

        switch (monthId) {
            case "0":
                result = "JAN"
                break

            case "1":
                result = "FEV"
                break

            case "2":
                result = "MAR"
                break

            case "3":
                result = "ABR"
                break

            case "4":
                result = "MAI"
                break

            case "5":
                result = "JUN"
                break

            case "6":
                result = "JUL"
                break

            case "7":
                result = "AGO"
                break

            case "8":
                result = "SET"
                break

            case "9":
                result = "OUT"
                break

            case "10":
                result = "NOV"
                break

            case "11":
                result = "DEZ"
                break
        }

        return result
    }
}
