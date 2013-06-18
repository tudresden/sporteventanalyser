class Engine
  constructor: (@ball) ->
    console.log(@ball)
    @resolution = [640, 480]
    @bgcolor = 0.fffff0;
    @obj_stack = []
    @scene = new THREE.Scene
    @camera = new THREE.PerspectiveCamera 75, @resolution[0] / @resolution[1], 1, 10000
    @camera.position.y = 33
    @camera.position.z = 100
    @camera_mode = "BIRD"

    @amb_light = new THREE.AmbientLight 0xffffff, 1.0
    @scene.add @amb_light

    @renderer = new THREE.WebGLRenderer
    @renderer.clearColor = @bgcolor
    @renderer.clear
    @renderer.setSize @resolution[0], @resolution[1]

    now = new Date
    @start_time = now.getTime()
    @.add @ball
    @mean_ball_cnt = 1000
    @mean_ball_pos =
      x: 0
      y: 0
      z: 0

  get_canvas: (target_div) ->
    @renderer.domElement

  add: (obj) ->
    @obj_stack.push obj
    @scene.add drawable for drawable in obj.drawables

  render: ->
    time = (new Date).getTime() - @start_time

    obj.follow(@camera.position) for obj in @obj_stack
    obj.animate(time) for obj in @obj_stack

    @mean_ball_pos.x = @mean_ball_cnt * @mean_ball_pos.x + @ball.ball.position.x
    @mean_ball_pos.x /= @mean_ball_cnt + 1
    @mean_ball_pos.y = @mean_ball_cnt * @mean_ball_pos.y + @ball.ball.position.y
    @mean_ball_pos.y /= @mean_ball_cnt + 1
    @mean_ball_pos.z = @mean_ball_cnt * @mean_ball_pos.z + @ball.ball.position.z
    @mean_ball_pos.z /= @mean_ball_cnt + 1
    console.log(@mean_ball_pos)

    switch @camera_mode
      when "BIRD"
        @camera.position.set 0, 60, 0
        @camera.rotation.set -Math.PI / 2, 0, 0
      when "KEEPERA"
        @camera.position.set -60, 10, 0
        @camera.lookAt @mean_ball_pos
      when "KEEPERB"
        @camera.position.set 60, 10, 0
        @camera.lookAt @mean_ball_pos
      else
        @camera.position.x =       5 * Math.cos (time / 1200)
        @camera.position.y = 33 +  5 * Math.sin (time / 600)
        @camera.position.z = 50 + 25 * Math.sin (time / 2700)
        @camera.lookAt @mean_ball_pos

    @renderer.render @scene, @camera

  set_cam_mode: (mode) ->
    @camera_mode = mode
