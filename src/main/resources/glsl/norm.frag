varying float c;
uniform vec4 baseColor;

void main(void)
{
    float ad = .5 * pow(c,3);
    gl_FragColor = (baseColor + ad);

}