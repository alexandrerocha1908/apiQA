package projeto.flavia

import grails.transaction.Transactional
import groovy.json.JsonSlurper
import org.grails.web.json.JSONObject
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Transactional
class PurchaseService {

    MessageSource messageSource

    JSONObject save(HttpServletRequest request, HttpServletResponse response) {
        JSONObject responseJSONObject = new JSONObject()
        JSONObject jsonObject = request.JSON as JSONObject

        try {
            Person person = Person.findById(jsonObject.personId as Long)
            Product product = Product.findById(jsonObject.productId as Long)

            Purchase purchase = new Purchase(
                    purchaseDate: DateUtil.instance.getDate(jsonObject.purchaseDate as String),
                    person: person,
                    price: jsonObject.price,
                    product: product,
                    quantity: jsonObject.quantity,
            )

            purchase.totalPrice = purchase.quantity * purchase.price

            purchase.cupon = Math.abs(new Random().nextInt() % 6000) + 1

            if (!purchase.validate()) {
                throw new IllegalArgumentServiceException(purchase.errors)
            }

            purchase.save(flush: true, failOnError: true)
            responseJSONObject = PurchaseUtil.instance.retrievePurchaseJSONObject(purchase)
            response.status = HttpStatus.CREATED.value()

        } catch (IllegalArgumentServiceException e) {
            responseJSONObject.put('errors', e.getMessages(messageSource))
            response.setStatus(e.getHttpCode())
        }

        return responseJSONObject
    }

    JSONObject index(HttpServletResponse response) {
        JSONObject responseJSONObject = new JSONObject()

        try {
            List<Purchase> purchaseList = Purchase.findAll().sort({it.purchaseDate})
            responseJSONObject = PurchaseUtil.instance.retrievePurchasesJSONObject(purchaseList)
        } catch (IllegalArgumentServiceException e) {
            responseJSONObject.put('errors', e.getMessages(messageSource))
            response.setStatus(e.getHttpCode())
        }

        return responseJSONObject
    }

    JSONObject retrievePuchaseByCupon(HttpServletRequest request, HttpServletResponse response) {
        JSONObject responseJSONObject = new JSONObject()
        JSONObject jsonObject = request.JSON as JSONObject

        try {
            Purchase purchase = Purchase.findByCupon(jsonObject.cupon as String)
            if (!purchase) {
                throw new IllegalArgumentServiceException("projeto.flavia.Purchase.not.found", HttpStatus.NOT_FOUND)
            }

            responseJSONObject = PurchaseUtil.instance.retrievePurchaseJSONObject(purchase)
        } catch (IllegalArgumentServiceException e) {
            responseJSONObject.put('errors', e.getMessages(messageSource))
            response.setStatus(e.getHttpCode())
        }

        return responseJSONObject
    }

    JSONObject retrievePurchaseByPerson(HttpServletRequest request, HttpServletResponse response) {
        JSONObject responseJSONObject = new JSONObject()
        JSONObject jsonObject = request.JSON as JSONObject

        try {
            Person person = Person.findByCpf(jsonObject.cpf as String)
            List<Purchase> purchaseList = Purchase.findAllByPerson(person)
            if (!purchaseList) {
                throw new IllegalArgumentServiceException("projeto.flavia.Purchase.not.found", HttpStatus.NOT_FOUND)
            }

            responseJSONObject = PurchaseUtil.instance.retrievePurchasesJSONObject(purchaseList)
        } catch (IllegalArgumentServiceException e) {
            responseJSONObject.put('errors', e.getMessages(messageSource))
            response.setStatus(e.getHttpCode())
        }

        return responseJSONObject
    }

    JSONObject retrievePurchaseByProduct(HttpServletRequest request, HttpServletResponse response) {
        JSONObject responseJSONObject = new JSONObject()
        JSONObject jsonObject = request.JSON as JSONObject

        try {
            Product product = Product.findByCode(jsonObject.code as String)
            List<Purchase> purchaseList = Purchase.findAllByProduct(product)
            if (!purchaseList) {
                throw new IllegalArgumentServiceException("projeto.flavia.Purchase.not.found", HttpStatus.NOT_FOUND)
            }

            responseJSONObject = PurchaseUtil.instance.retrievePurchasesJSONObject(purchaseList)
        } catch (IllegalArgumentServiceException e) {
            responseJSONObject.put('errors', e.getMessages(messageSource))
            response.setStatus(e.getHttpCode())
        }

        return responseJSONObject
    }

    JSONObject retrieveByDate(HttpServletRequest request, HttpServletResponse response) {
        JSONObject responseJSONObject = new JSONObject()
        JSONObject jsonObject = request.JSON as JSONObject

        try {
            List<Purchase> purchaseList = Purchase.findAllByPurchaseDate(DateUtil.instance.getDate(jsonObject.purchaseDate as String))
            if (!purchaseList) {
                throw new IllegalArgumentServiceException("projeto.flavia.Purchase.not.found", HttpStatus.NOT_FOUND)
            }

            responseJSONObject = PurchaseUtil.instance.retrievePurchasesJSONObject(purchaseList)
        } catch (IllegalArgumentServiceException e) {
            responseJSONObject.put('errors', e.getMessages(messageSource))
            response.setStatus(e.getHttpCode())
        }
    }
}
