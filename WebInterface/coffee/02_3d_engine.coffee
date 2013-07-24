class Engine
  constructor: (@ball, @resolution=[640, 480]) ->
    @bgcolor = 0.fffff0;
    @obj_stack = []
    @scene = new THREE.Scene
    @camera = new THREE.PerspectiveCamera 75, @resolution[0] / @resolution[1], 0.1, 10000
    @camera_mode = "BIRD"

    @amb_light = new THREE.AmbientLight 0xffffff, 1.0
    @scene.add @amb_light

    @renderer = new THREE.WebGLRenderer
    # In case you want to view this in Chrome
    #@renderer = new THREE.CanvasRenderer
    @renderer.clearColor = @bgcolor
    @renderer.clear
    @renderer.setSize @resolution[0], @resolution[1]

    now = new Date
    @start_time = now.getTime()
    @.add @ball
    @mean_ball_cnt = 30
    @mean_ball_pos =
      x: 0
      y: 0
      z: 0
    @reality =
      width: FIELD_WIDTH
      height: FIELD_HEIGHT
      offx: 0
      offy: 0
    @field = null
    @players = []

  set_field: (@field) ->
    @reality = @field.model.reality
    @.add @field

  reposition: (position) ->
    result = {}
    if @field  # TODO apply a transformation matrix here.
      if position.x
        result.y = (position.x - @reality.height/2) * @field.height / @reality.height
      if position.y
        result.x = (position.y) * @field.width / @reality.width
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
        @camera.position.set 0, FIELD_WIDTH/2, 0
        @camera.rotation.set -Math.PI / 2, 0, 0
      when "KEEPERA"
        @camera.position.set -FIELD_WIDTH/2, 6, 0
        @camera.lookAt @mean_ball_pos
      when "KEEPERB"
        @camera.position.set FIELD_WIDTH/2, 6, 0
        @camera.lookAt @mean_ball_pos
      else
        @camera.position.x =       5 * Math.cos (time / 1300)
        @camera.position.y = 33 +  5 * Math.sin (time / 700)
        @camera.position.z = FIELD_WIDTH/2 +  5 * Math.sin (time / 1900)
        @camera.lookAt @mean_ball_pos
    obj.follow(@camera, @camera_mode == "BIRD") for obj in @obj_stack
    obj.animate(time) for obj in @obj_stack

    @renderer.render @scene, @camera
