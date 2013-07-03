class Engine
  constructor: (@ball) ->
    @resolution = [640, 480]
    @bgcolor = 0.fffff0;
    @obj_stack = []
    @scene = new THREE.Scene
    @camera = new THREE.PerspectiveCamera 75, @resolution[0] / @resolution[1], 1, 10000
    @camera_mode = "BIRD"

    @amb_light = new THREE.AmbientLight 0xffffff, 1.0
    @scene.add @amb_light

    @renderer = new THREE.WebGLRenderer
    # In case you want to view this in Chrome
    #@renderer = new THREE.CanvasRenderer
    @renderer.clearColor = @bgcolor
    @renderer.clear
    @renderer.setSize @resolution[0], @resolution[1]
    console.log @camera

    now = new Date
    @start_time = now.getTime()
    @.add @ball
    @mean_ball_cnt = 30
    @mean_ball_pos =
      x: 0
      y: 0
      z: 0
    @reality =
      width: 120
      height: 50
      offx: 0
      offy: 0
    @field = null
    @players = []

  set_field: (@field) ->
    @reality = @field.reality
    @.add @field

  reposition: (position) ->
    result = {}
    if @field
      if position.x
        result.x = (position.x - @reality.width/2) * @field.width / @reality.width
      if position.y
        result.y = (position.y) * @field.height / @reality.height
      if position.z
        result.z = position.z * (@field.width / @reality.width + @field.height / @reality.height) / 2
    return result

  get_canvas: (target_div) ->
    @renderer.domElement

  add: (obj) ->
    @obj_stack.push obj
    @scene.add drawable for drawable in obj.drawables

  render: ->
    time = (new Date).getTime() - @start_time

    @mean_ball_pos.x = @mean_ball_cnt * @mean_ball_pos.x + @ball.ball.position.x
    @mean_ball_pos.x /= @mean_ball_cnt + 1
    @mean_ball_pos.y = @mean_ball_cnt * @mean_ball_pos.y + @ball.ball.position.y
    @mean_ball_pos.y /= @mean_ball_cnt + 1
    @mean_ball_pos.z = @mean_ball_cnt * @mean_ball_pos.z + @ball.ball.position.z
    @mean_ball_pos.z /= @mean_ball_cnt + 1

    switch @camera_mode
      when "BIRD"
        @camera.position.set 0, 60, 0
        @camera.rotation.set -Math.PI / 2, 0, 0
      when "KEEPERA"
        @camera.position.set -60, 6, 0
        @camera.lookAt @mean_ball_pos
      when "KEEPERB"
        @camera.position.set 60, 6, 0
        @camera.lookAt @mean_ball_pos
      else
        @camera.position.x =       5 * Math.cos (time / 1200)
        @camera.position.y = 33 +  5 * Math.sin (time / 600)
        @camera.position.z = 50 + 25 * Math.sin (time / 2700)
        @camera.lookAt @mean_ball_pos
    obj.follow(@camera.position, @camera_mode == "BIRD") for obj in @obj_stack
    obj.animate(time) for obj in @obj_stack

    @renderer.render @scene, @camera

  select_players: (plr_ids) ->
    if plr_ids.length is 0
      @players.forEach (v, i) ->
        v.selected = 2
    else
      @players.forEach (v, i) ->
        v.selected = if v.id in plr_ids then 1 else 0
