translation_dict =
  "id":             "Software ID"
  "ballContacts":   "Ball contacts"
  "goalsScored":    "Goals scored"
  "passesMade":     "Passes made"
  "passesMissed":   "Passes missed"
  "passesReceived": "Passes received"
  "tacklings":      "Tacklings"
  "tacklesWon":     "Tacklings won"
  "possessionTime": "Possession time"
  "totalDistance":  "Total distance"

t = (word) ->
  res = translation_dict["" + word]
  if not res?
    return "##" + word
  return res
