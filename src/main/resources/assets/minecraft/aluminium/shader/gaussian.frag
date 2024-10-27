#version 120

uniform sampler2D textureIn;
uniform vec2 texelSize, direction;
uniform float radius, weights[256];

#define offset (texelSize * direction)

void main() {
    // 使用中心像素并乘以第一个权重值
    vec3 color = texture2D(textureIn, gl_TexCoord[0].st).rgb * weights[0];
    float totalWeight = weights[0];

    vec2 stepOffset = offset;

    for (int i = 1; i <= int(radius); i++) {
        float weight = weights[i];

        // 采样正向偏移
        color += texture2D(textureIn, gl_TexCoord[0].st + stepOffset).rgb * weight;
        // 采样反向偏移
        color += texture2D(textureIn, gl_TexCoord[0].st - stepOffset).rgb * weight;

        totalWeight += weight * 2.0;

        // 更新偏移量，避免重复计算
        stepOffset += offset;
    }

    gl_FragColor = vec4(color / totalWeight, 1.0);
}
