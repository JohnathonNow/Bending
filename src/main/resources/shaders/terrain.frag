#version 330 core
out vec4 FragColor;

in vec2 TexCoord;

uniform sampler2D terrainData;
uniform sampler2D textureAtlas;
uniform ivec2 viewPos;

// Each element of this array corresponds to a terrain type.
// The x and y components are the offset in the texture atlas (e.g., 0,0 for grass, 1,0 for sand)
// The z and w components are the size of the texture in the atlas (e.g., 0.25, 0.5 for a 4x2 atlas)
uniform vec4[128] atlasMapping;

void main()
{
    vec2 terrainTexCoord = TexCoord + (vec2(viewPos.x, viewPos.y) / textureSize(terrainData, 0));
    float terrainId = texture(terrainData, terrainTexCoord).r * 255.0;
    int terrainIdInt = int(terrainId);

    if (terrainIdInt == 0) // AIR
    {
        // This is the sky, which is drawn separately.
        // We can discard this fragment and let the background show through.
        discard;
    }

    vec4 mapping = atlasMapping[terrainIdInt];
    vec2 atlasOffset = mapping.xy;
    vec2 atlasScale = mapping.zw;

    vec2 atlasCoord = TexCoord * atlasScale + atlasOffset;

    FragColor = texture(textureAtlas, atlasCoord);
}
