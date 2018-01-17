package projeto.flavia

import org.grails.web.json.JSONArray
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.validation.Errors
import org.springframework.validation.ObjectError

class IllegalArgumentServiceException extends Exception {

    private Map<String, Object[]> errorsCodeList = new HashMap<>()
    private List<Errors> errorsList = new ArrayList<>()

    private int httpStatus

    IllegalArgumentServiceException() {
        httpStatus = HttpStatus.UNPROCESSABLE_ENTITY.value()
    }

    IllegalArgumentServiceException(String errorCode) {
        this(errorCode, null)
    }

    IllegalArgumentServiceException(String errorCode, Object[] params) {
        this(errorCode, params, HttpStatus.UNPROCESSABLE_ENTITY.value())
    }

    IllegalArgumentServiceException(String errorCode, int httpStatus) {
        this(errorCode, null, httpStatus)
    }

    IllegalArgumentServiceException(String errorCode, Object[] params, int httpStatus) {
        this.errorsCodeList.put(errorCode, params)
        this.httpStatus = httpStatus
    }

    IllegalArgumentServiceException(Errors errors) {
        this(errors, HttpStatus.UNPROCESSABLE_ENTITY.value())
    }

    IllegalArgumentServiceException(Errors errors, int httpStatus) {
        this.errorsList.add(errors)
        this.httpStatus = httpStatus
    }

    IllegalArgumentServiceException(List<Errors> errorsList, Map<String, Object[]> errorsCodes) {
        this(errorsList, errorsCodes, HttpStatus.UNPROCESSABLE_ENTITY.value())
    }

    IllegalArgumentServiceException(List<Errors> errorsList, Map<String, Object[]> errorsCodes, int httpStatus) {
        this.errorsList = errorsList
        this.errorsCodeList = errorsCodes
        this.httpStatus = httpStatus
    }

    void addMessage (String errorCode) {
        this.addMessage(errorCode, null)
    }

    void addMessage (String errorCode, Object[] params) {
        errorsCodeList.put(errorCode, params)
    }

    void addMessage (Errors errors) {
        errorsList.add(errors)
    }

    int getHttpCode () {
        return httpStatus
    }

    JSONArray getMessages (MessageSource messageSource) {
        JSONArray errorMessagesJSONArray = new JSONArray()

        String messageText = null

        errorsList.each { Errors errors ->
            errors.allErrors.each { ObjectError error ->
                messageText = messageSource.getMessage(error, null)

                if (!errorMessagesJSONArray.contains(messageText)) {
                    errorMessagesJSONArray.put(messageText)
                }
            }
        }

        errorsCodeList.each { String key, Object[] value ->
            messageText = messageSource.getMessage(key, value, null)

            if (!errorMessagesJSONArray.contains(messageText)) {
                errorMessagesJSONArray.put(messageText)
            }
        }

        return errorMessagesJSONArray
    }

    boolean haveErrors () {
        return !errorsCodeList.isEmpty() || !errorsList.isEmpty()
    }

    void throwIfHaveErrors () throws IllegalArgumentServiceException {
        if (haveErrors()) {
            throw this
        }
    }
}
