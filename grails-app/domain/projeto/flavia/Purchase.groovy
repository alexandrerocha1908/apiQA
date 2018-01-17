package projeto.flavia

class Purchase {

    String cupon
    Date purchaseDate
    Double price
    Integer quantity
    Double totalPrice

    Person person
    Product product

    static belongsTo = Person

    static constraints = {
        cupon unique: true
        cupon nullable: false
        purchaseDate nullable: false
        price nullable: false
        quantity nullable: false
        totalPrice nullable: false

        person nullable: false
        product nullable: false
    }

    static mapping = {
        id: ['cupon']

        person column: "person_id"
        product column: "product_id"
    }
}
