#version 150

#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0;
in vec4 gl_FragCoord;

out vec4 fragColor;

void main() {
    fragColor = texture(Sampler0, gl_FragCoord.xy / ScreenSize);
}
