package projeto.flavia

import org.grails.web.json.JSONArray
import org.grails.web.json.JSONObject

@Singleton
class PersonUtil {
    JSONObject retrievePeopleJSONObject(List<Person> people) {
        JSONArray peopleJSONArray = new JSONArray()
        people.each { Person person ->
            peopleJSONArray.add(retrievePersonJSONObject(person))
        }
        return new JSONObject('people': peopleJSONArray)
    }

    JSONObject retrievePersonJSONObject(Person person) {
        JSONObject personJSONObject = new JSONObject(
                'id': person.id,
                'name': person.name,
                'cpf': person.cpf
        )

        return personJSONObject
    }
}
