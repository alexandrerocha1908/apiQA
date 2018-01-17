package projeto.flavia

class Person {

    String cpf
    String name

    static hasMany = [purchases: Purchase]

    static constraints = {
        cpf nullable: false
        name nullable: false

        purchases nullable: true
    }
}
