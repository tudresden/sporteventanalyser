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
  "commentPred:Any":        "Well.."
  "commentPred:shotOnGoal": "There's going to be a goal."
  "commentPred:outOfPlay":  "Out!"
  "commentPred:turnOver":   "They'll lose the ball."

t = (word) ->
  res = translation_dict["" + word]
  if not res?
    return "##" + word
  return res
