package projeto.flavia

class Product {

    String name
    String code

    static hasMany = [purchases: Purchase]

    static constraints = {
        name nullable: false
        code nullable: false

        purchases nullable: true
    }
}
