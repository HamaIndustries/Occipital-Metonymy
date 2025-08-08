#version 150

#moj_import <minecraft:globals.glsl>

uniform sampler2D DepthSampler;
uniform sampler2D D2Sampler;
in vec4 gl_FragCoord;

out vec4 fragColor;

float easeOutQuint(float x) {
    return 1.0 - pow(1.0 - x, 10.0);
}

float linearizeDepth(float depth)
{
    //return depth;
    float zNear = 0.5;
    float zFar = 2000.0;
    return (2.0 * zNear) / (zFar + zNear - depth * (zFar - zNear));
}

void main() {
    //fragColor = vec4(gl_FragCoord.z, gl_FragCoord.z, gl_FragCoord.z, 1);
    float d = linearizeDepth(texture(D2Sampler, gl_FragCoord.xy / ScreenSize).x);
    d = clamp(d*5, 0.0, 1.0);
    fragColor = texture(DepthSampler, gl_FragCoord.xy / ScreenSize) * (1.0-clamp(d, 0.0, 1.0));
    //fragColor = vec4(d, d, d, 0.0);
}
