package projeto.flavia


import grails.rest.*
import grails.converters.*

class PersonController {
    static allowedMethods = [
            delete: "DELETE",
            index: "GET",
            save: "POST",
            show: "GET",
            update: "PUT"

    ]

    PersonService personService

    def delete() {
        render personService.delete(params, response)
    }

    def index() {
        render personService.index(response)
    }

    def save() {
        render personService.save(request, response)
    }

    def show() {
        render personService.show(params, response)
    }

    def update() {
        render personService.update(params, request, response)
    }
}
