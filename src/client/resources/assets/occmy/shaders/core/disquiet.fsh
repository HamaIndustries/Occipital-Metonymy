#version 150

#moj_import <minecraft:globals.glsl>

uniform sampler2D DepthSampler;
uniform sampler2D ColorSampler;
in vec4 gl_FragCoord;

out vec4 fragColor;

float easeOutQuint(float x) {
    return 1.0 - pow(1.0 - x, 2.0);
}

float easeOut2(float x) {
    return 1.0 - pow(1.0 - x, 7.0);
}

float rand(vec2 co){
    return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453);
}

float linearizeDepth(float depth)
{
    //return depth;
    float zNear = 0.5;
    float zFar = 2000.0;
    return (2.0 * zNear) / (zFar + zNear - depth * (zFar - zNear));
}

void main() {
    float depth = texture(DepthSampler, gl_FragCoord.xy / ScreenSize).x;
    float d = linearizeDepth(depth);
    vec4 color = texture(ColorSampler, gl_FragCoord.xy / ScreenSize);
    //color2 = vec4(fract(color.r + color.g + color.b), 0.0, 0.0, 1.0);
    float amt = step(0.65, d);
    fragColor = color * (1.0-amt) + amt * vec4(1-color.r, 0.0, 0.0, 1.0);
}
