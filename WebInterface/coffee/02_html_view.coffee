class HTMLView
  constructor: (@model) ->

  tbody: ->
    res = "<tbody>"
    $.each @model.stats, (k, v) ->
      res += "<tr><td>" + t(""+k) + "</td><td>" + v + "</td></tr>\n"
    return res + "</tbody>"

  toString: ->
    @.constructor.name

class FieldHTML extends HTMLView
  constructor: (@model) ->
    super @model

class BallHTML extends HTMLView
  constructor: (@model) ->
    super @model

class PlayerHTML extends HTMLView
  constructor: (@model) ->
    super @model

  tbody: ->
    res = "<tbody>"
    $.each @model.stats, (k, v) ->
      k = ""+k
      if k not in ["possessionTime",]
        res += "<tr><td>" + t(k) + "</td><td>" + v + "</td></tr>\n"
      else
        seconds = v / 1000 / 1000 / 1000 / 1000
        minutes = Math.floor seconds / 60
        seconds = Math.floor seconds % 60
        if seconds < 10
          seconds = "0" + seconds
        res += "<tr><td>" + t(k) + "</td><td>" + minutes + ":" + seconds + "</td></tr>\n"
    return res + "</tbody>"

  toString: ->
    "Player(" + string.Join(", ", [@id, @name, @tricot_image]) + ")"
