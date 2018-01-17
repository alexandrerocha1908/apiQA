package projeto.flavia


import grails.rest.*
import grails.converters.*

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class PurchaseController {
    static allowedMethods = [
            index: "GET",
            save: "POST",
            retrievePuchaseByCupon: "POST",
            retrievePurchaseByPerson: "POST",
            retrievePurchaseByProduct: "POST",
            retrievePurchaseByDate: "POST"
    ]

    PurchaseService purchaseService

    def save() {
        render purchaseService.save(request, response)
    }

    def index() {
        render purchaseService.index(response)
    }

    def retrievePuchaseByCupon() {
        render purchaseService.retrievePuchaseByCupon(request, response)
    }

    def retrievePurchaseByPerson() {
        render purchaseService.retrievePurchaseByPerson(request, response)
    }

    def retrievePurchaseByProduct() {
        render purchaseService.retrievePurchaseByProduct(request, response)
    }

    def retrievePurchaseByDate() {
        render purchaseService.retrieveByDate(request, response)
    }
}
