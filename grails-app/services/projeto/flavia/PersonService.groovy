package projeto.flavia

import grails.transaction.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import groovy.json.JsonSlurper
import org.grails.web.json.JSONObject
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Transactional
class PersonService {

    MessageSource messageSource

    JSONObject delete(GrailsParameterMap params, HttpServletResponse response) {
        JSONObject responseJSONObject = new JSONObject()
        try {
            Person person = Person.findById(params.id as Long)
            person.delete(flush: true, failOnError: true)
            response.status = HttpStatus.NO_CONTENT.value()
        } catch (IllegalArgumentServiceException e) {
            responseJSONObject.put('errors', e.getMessages(messageSource))
            response.setStatus(e.getHttpCode())
        }
        return responseJSONObject
    }

    JSONObject index(HttpServletResponse response) {
        JSONObject responseJSONObject = new JSONObject()
        try {
            List<Person> people = Person.findAll().sort({it.name})
            responseJSONObject = PersonUtil.instance.retrievePeopleJSONObject(people)
        } catch (IllegalArgumentServiceException e) {
            responseJSONObject.put('errors', e.getMessages(messageSource))
            response.setStatus(e.getHttpCode())
        }
        return responseJSONObject
    }

    JSONObject save(HttpServletRequest request, HttpServletResponse response) {
        JSONObject responseJSONObject = new JSONObject()
        JSONObject jsonObject = request.JSON as JSONObject

        try {
            Person person = new Person(
                    name: jsonObject.name,
                    cpf: jsonObject.cpf
            )
            if (!person.validate()) {
                throw new IllegalArgumentServiceException(person.errors)
            }

            person.save(flush: true, failOnError: true)
            responseJSONObject = PersonUtil.instance.retrievePersonJSONObject(person)
            response.status = HttpStatus.CREATED.value()

        } catch (IllegalArgumentServiceException e) {
            responseJSONObject.put('errors', e.getMessages(messageSource))
            response.setStatus(e.getHttpCode())
        }
        return responseJSONObject
    }

    JSONObject show(GrailsParameterMap params, HttpServletResponse response) {
        JSONObject responseJSONObject = new JSONObject()

        try {
            Person person = Person.read(params.id as Long)
            responseJSONObject = PersonUtil.instance.retrievePersonJSONObject(person)
        } catch (IllegalArgumentServiceException e) {
            responseJSONObject.put('errors', e.getMessages(messageSource))
            response.setStatus(e.getHttpCode())
        }
        return responseJSONObject
    }

    JSONObject update(GrailsParameterMap params, HttpServletRequest request, HttpServletResponse response) {
        JSONObject responseJSONObject = new JSONObject()
        JSONObject jsonObject = request.JSON as JSONObject

        try {
            Person person = Person.findById(params.id as Long)
            person.properties = new JsonSlurper().parseText(jsonObject.toString())
            if (!person.validate()) {
                throw new IllegalArgumentServiceException(person.errors)
            }

            person.save(flush: true, failOnError: true)
            responseJSONObject = PersonUtil.instance.retrievePersonJSONObject(person)
        } catch (IllegalArgumentServiceException e) {
            responseJSONObject.put('errors', e.getMessages(messageSource))
            response.setStatus(e.getHttpCode())
        }
        return responseJSONObject
    }
}
