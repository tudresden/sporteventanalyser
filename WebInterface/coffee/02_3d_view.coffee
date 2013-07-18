class Drawable3d
  constructor: ->

  animate: (time) ->

  follow: (target, reset = false) ->
    for f in @followers
      do (f) ->
        if reset
          f.rotation.set -PI2, 0, 0
        else
          f.lookAt target.position
          #f.rotation.set 0, target.rotation.y, 0

  toString: ->
    @.constructor.name

class Moveable3d extends Drawable3d
  constructor: ->
    super()

  transition: (factor, current, target) ->
    return (current * factor + target) / (factor + 1)

class Field3d extends Drawable3d
  constructor: (texturefile, @model) ->
    @width = FIELD_WIDTH
    @height = FIELD_HEIGHT
    geometry = new THREE.PlaneGeometry(@width, @height)
    mat_cfg = 
      map:  new THREE.ImageUtils.loadTexture texturefile
      side: THREE.DoubleSide
    material = new THREE.MeshLambertMaterial(mat_cfg)
    @field = new THREE.Mesh geometry, material
    @field.rotation.set -PI2, 0, 0
    @drawables = [@field]
    @followers = []
    super()

class Ball3d extends Moveable3d
  constructor: (texturefile, @model) ->
    super()
    geometry = new THREE.PlaneGeometry(BALL_SIZE, BALL_SIZE)
    mat_cfg =
      map:       new THREE.ImageUtils.loadTexture texturefile
      alphaTest: 0.5
    material = new THREE.MeshBasicMaterial mat_cfg
    @ball = new THREE.Mesh(geometry, material)

    geometry = new THREE.PlaneGeometry 1, 1
    mat_cfg =
      map:         new THREE.ImageUtils.loadTexture "img/shadow.png"
      transparent: true
    material = new THREE.MeshBasicMaterial mat_cfg
    @shadow = new THREE.Mesh geometry, material
    @shadow.position.y = 0.001
    @shadow.rotation.set -PI2, 0, 0

    @followers = [@ball]
    @drawables = [@ball, @shadow]
    @ball.position.set 0, @ball.geometry.height/2, 0
    @last_anim_time = 0

  animate: (time) ->
    dt = Math.max 1, time - @last_anim_time
    @ball.position.x = @.transition ANIM_FACTOR/dt, @ball.position.x, @model.position.x
    @ball.position.y = Math.max @ball.geometry.height/2, @.transition ANIM_FACTOR/dt, @ball.position.y, 0.5 * @ball.geometry.height + @model.position.z
    @ball.position.z = @.transition ANIM_FACTOR/dt, @ball.position.z, @model.position.y

    @shadow.position.x = @ball.position.x
    @shadow.position.z = @ball.position.z

    s_scale = 1.0 + 1.0 * Math.max(1.0, @ball.position.y)
    @shadow.scale.set s_scale, s_scale, s_scale
    @shadow.material.opacity = Math.min(1.0, Math.max(0.0, 1.0 / @ball.position.y))
    @last_anim_time= time

class Player3d extends Moveable3d
  constructor: (tricot_image, @model) ->
    super()
    geometry = new THREE.PlaneGeometry(PLAYER_SIZE, PLAYER_SIZE)
    mat_cfg =
      map:       new THREE.ImageUtils.loadTexture tricot_image
      transparent: true
    material = new THREE.MeshBasicMaterial mat_cfg
    @shirt = new THREE.Mesh(geometry, material)
    @shirt.position.set 0, PLAYER_SIZE2, 0

    geometry = new THREE.PlaneGeometry PLAYER_SIZE, PLAYER_SIZE
    mat_cfg =
      map:         new THREE.ImageUtils.loadTexture "img/shadow.png"
      opacity:     0.8
      transparent: true
    material = new THREE.MeshBasicMaterial mat_cfg
    @shadow = new THREE.Mesh geometry, material
    @shadow.position.y = 0.005
    @shadow.rotation.set -PI2, 0, 0

    geometry = new THREE.PlaneGeometry SELECT_SIZE, SELECT_SIZE
    mat_cfg =
      map:         new THREE.ImageUtils.loadTexture "img/selection.png"
      transparent: true
    material = new THREE.MeshBasicMaterial mat_cfg
    @select = new THREE.Mesh geometry, material
    @select.position.y = 0.005
    @select.rotation.set -PI2, 0, 0

    @followers = [@shirt]
    @drawables = [@shirt, @shadow, @select]

    @last_anim_time = 0

  animate: (time) ->
    dt = Math.max 1, time - @last_anim_time
    @shirt.position.x = @.transition ANIM_FACTOR/dt, @shirt.position.x, @model.position.x
    @shirt.position.z = @.transition ANIM_FACTOR/dt, @shirt.position.z, @model.position.y

    @shadow.position.x = @shirt.position.x
    @shadow.position.z = @shirt.position.z

    s = Math.max(1, @shirt.position.y - PLAYER_SIZE2)
    @shadow.scale.set s, s, s

    switch @model.selected
      when 2
        @select.material.opacity = 0.01
        @shirt.material.opacity = 1
      when 1
        s = 1 + 0.2 * Math.sin(0.005 * time)
        @select.scale.set s, s, s
        @select.rotation.z = 0.01 * time
        @select.position.x = @shirt.position.x
        @select.position.z = @shirt.position.z
        @select.material.opacity = 1.0
        @shirt.material.opacity = 1.0
      else
        @select.material.opacity = 0
        @shirt.material.opacity = 0.5
    @last_anim_time = time

  toString: ->
    "Player3d(" + string.Join(", ", [@model]) + ")"
