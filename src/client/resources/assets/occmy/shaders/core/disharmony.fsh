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
    //float depth = texture(DepthSampler, gl_FragCoord.xy / ScreenSize).x;
    //float noise = rand(gl_FragCoord.xy + vec2(GameTime));
    float d = linearizeDepth(texture(DepthSampler, gl_FragCoord.xy / ScreenSize).x);
    d = clamp(d*5, 0.0, 1.0);
    vec4 color = texture(ColorSampler, gl_FragCoord.xy / ScreenSize);
    vec4 color2 = vec4(fract(color.rgb + vec3(0.5, 0.7, 0.2)), 1.0);
    vec4 value = vec4(1.0, 1.0, 1.0, 1.0);
    fragColor = mix(color2, value, d);
    //fragColor.rgb = fract(fragColor.rgb + vec3(0.5, 0.5, 0.5));

    //vec4 color = texture(DepthSampler, gl_FragCoord.xy / ScreenSize);
    //float value = (color.r + color.g + color.b) / 3.0;
    //vec4 greyscale = vec4(value,value,value, 1.0);
    //fragColor = mix(color, greyscale + noise * (d), easeOutQuint(d)) * (1.0-clamp(d, 0.0, 1.0));

}
