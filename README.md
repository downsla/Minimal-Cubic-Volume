# Minimal-Cubic-Volume
Attempts to determine the smallest cube a given rectangular prism can fit into, and output the dimensions of the cube and the rotation of the prism inside.

The program takes three integer values and performs calculations to output the smallest cube's volume in the input's units.
One of three orientations is also given:
  - flat - meaning that the rectangular prism is squarely in the corner of the minimal cube
  - tilt - meaning that the rectangular prism is rotated 45° while resting flat against one side of the minimal cube
  - diagonal - meaning that the rectangular prism is rotated 45° and then rotated some degrees in another dimension

For efficiency, I have used the bisection method to approximate the solution to the limitation of double's precision.

The calculations done are a result from my own research. While I cannot prove that if produces a true minimal cube containing a given rectangular prism, this was my attempt at finding the smallest and coding around the trends I found.
