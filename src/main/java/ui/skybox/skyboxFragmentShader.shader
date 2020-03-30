#version 400 core

in vec3 textureCoords;
out vec4 out_Color;

uniform samplerCube cubeMap;
uniform vec3 fogColor;

const float lowerLimit = 1.0;
const float upperLimit = 1.0001;

void main(void){

    out_Color = texture(cubeMap, textureCoords);

}