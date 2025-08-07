#version 150

uniform sampler2D BlitSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    fragColor = texture(BlitSampler, texCoord);
}
