class Drawable
  constructor: ->
    @registered = false
    @drawables = []
    @followers = []

  animate: (time) ->

  follow: (target) ->
    for f in @followers
      do (f) ->
        f.lookAt target

  toString: ->
    "Drawable"

class Field extends Drawable
  constructor: ->
    @width = 120
    @height = 90
    geometry = new THREE.PlaneGeometry(@width, @height)
    mat_cfg = 
      map:  new THREE.ImageUtils.loadTexture "img/Soccer_field_-_empty1.png"
      side: THREE.DoubleSide
    material = new THREE.MeshLambertMaterial(mat_cfg)
    @field = new THREE.Mesh geometry, material
    @field.rotation.x = Math.PI/2
    @drawables = [@field]
    @followers = []
  toString: ->
    "Field"

class Ball extends Drawable
  constructor: ->
    geometry = new THREE.PlaneGeometry(2, 2)
    mat_cfg =
      map:       new THREE.ImageUtils.loadTexture "img/ball.png"
      side:      THREE.DoubleSide
      alphaTest: 0.5
    material = new THREE.MeshBasicMaterial mat_cfg
    @ball = new THREE.Mesh(geometry, material)
    geometry = new THREE.PlaneGeometry 1, 1
    mat_cfg =
      color: 0x003300
      side:      THREE.DoubleSide
    material = new THREE.MeshBasicMaterial mat_cfg
    @shadow = new THREE.Mesh geometry, material
    @followers = [@ball]
    @drawables = [@ball, @shadow]
  animate: (time) ->
    @ball.position.y = @ball.geometry.height/2
    @ball.position.y += Math.abs @ball.geometry.height * Math.sin time/120
    
    @ball.position.x = @ball.geometry.height/2
    @ball.position.x += Math.abs @ball.geometry.height * Math.cos time/240

    @shadow.position.x = @ball.position.x
    @shadow.position.z = @ball.position.z

    @shadow.scale.x = @ball.position.y
    @shadow.scale.x -= @ball.geometry.height

    @shadow.scale.y = @ball.position.y
    @shadow.scale.y -= @ball.geometry.height
  toString: ->
    "Ball"
