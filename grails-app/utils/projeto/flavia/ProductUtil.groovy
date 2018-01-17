package projeto.flavia

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

/**
 * Created by alexandre on 09/01/18.
 */
@Singleton
class ProductUtil {
    JSONObject retrieveProductsJSONObject(List<Product> products) {
        JSONArray productsJSONArray = new JSONArray()
        products.each { Product product ->
            productsJSONArray.add(retrieveProductJSONObject(product))
        }
        return new JSONObject('products': productsJSONArray)
    }

    JSONObject retrieveProductJSONObject(Product product) {
        JSONObject productJSONObject = new JSONObject(
                'id': product.id,
                'name': product.name,
                'code': product.code
        )

        return productJSONObject
    }
}
