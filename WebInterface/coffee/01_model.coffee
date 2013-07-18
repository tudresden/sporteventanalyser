class Model
  constructor: ->
    @last_stats_update = 0
    @stats = {}

  update_stats: (time, stats) ->
    @stats = stats
    @last_stats_update = time if time?

  toString: ->
    @.constructor.name

class MoveableModel extends Model
  constructor: ->
    super()
    @position =
      x: 0
      y: 0
      z: 0
    @last_move_update = 0

  update_position: (time, data) ->
    @position.x = data.x if data.x?
    @position.y = data.y if data.y?
    @position.z = data.z if data.z?
    @last_move_update = time if time?

class FieldModel extends MoveableModel
  constructor: (@reality) ->
    super()
    @ratio = @reality.width / @reality.height

class BallModel extends MoveableModel
  constructor: ->
    super()

class PlayerModel extends MoveableModel
  constructor: (@id, @name, @team) ->
    super()
    selected = 2

  update_stats: (@last_update, stats) ->
    res = @stats
    $.each stats, (k, v) ->
      k = ""+k
      if k not in ["id","tacklings","tacklesWon"]
        res[k] = v
    @stats = res

  toString: ->
    "PlayerModel(" + string.Join(", ", [@id, @name, @tricot_image]) + ")"
