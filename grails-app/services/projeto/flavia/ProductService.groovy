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
class ProductService {

    MessageSource messageSource

    JSONObject delete(GrailsParameterMap params, HttpServletResponse response) {
        JSONObject responseJSONObject = new JSONObject()
        try {
            Product product = Person.findById(params.id as Long)
            product.delete(flush: true, failOnError: true)
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
            List<Product> products = Product.findAll().sort({it.name})
            responseJSONObject = ProductUtil.instance.retrieveProductsJSONObject(products)
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
            Product product = new Product(
                    name: jsonObject.name,
                    code: jsonObject.code
            )
            if (!product.validate()) {
                throw new IllegalArgumentServiceException(product.errors)
            }

            product.save(flush: true, failOnError: true)
            responseJSONObject = ProductUtil.instance.retrieveProductJSONObject(product)
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
            Product product = Product.read(params.id as Long)
            responseJSONObject = ProductUtil.instance.retrieveProductJSONObject(product)
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
            Product product = Product.findById(params.id as Long)
            product.properties = new JsonSlurper().parseText(jsonObject.toString())
            if (!product.validate()) {
                throw new IllegalArgumentServiceException(product.errors)
            }

            product.save(flush: true, failOnError: true)
            responseJSONObject = ProductUtil.instance.retrieveProductJSONObject(product)
        } catch (IllegalArgumentServiceException e) {
            responseJSONObject.put('errors', e.getMessages(messageSource))
            response.setStatus(e.getHttpCode())
        }
        return responseJSONObject
    }
}
