package projeto.flavia


import grails.rest.*
import grails.converters.*

class ProductController {
    static allowedMethods = [
            delete: "DELETE",
            index: "GET",
            save: "POST",
            show: "GET",
            update: "PUT"

    ]

    ProductService productService

    def delete() {
        render productService.delete(params, response)
    }

    def index() {
        render productService.index(response)
    }

    def save() {
        render productService.save(request, response)
    }

    def show() {
        render productService.show(params, response)
    }

    def update() {
        render productService.update(params, request, response)
    }
}
