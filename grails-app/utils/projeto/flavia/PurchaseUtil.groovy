package projeto.flavia

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

@Singleton
class PurchaseUtil {
    JSONObject retrievePurchaseJSONObject(Purchase purchase) {
        return new JSONObject(
                'cupon': purchase.cupon,
                'personName': purchase.person.name,
                'productName': purchase.product.name,
                'quantity': purchase.quantity,
                'singlePrice': purchase.price,
                'totalPrice': purchase.totalPrice,
                'purchaseDate': DateUtil.instance.formatDate(purchase.purchaseDate, "dd/MM/YYYY")
        )
    }

    JSONObject retrievePurchasesJSONObject(List<Purchase> purchaseList) {
        JSONArray purchasesJSONArray = new JSONArray()
        purchaseList.each { Purchase purchase ->
            purchasesJSONArray.add(retrievePurchaseJSONObject(purchase))
        }

        return new JSONObject('purchases': purchasesJSONArray)
    }
}
