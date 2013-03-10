uniform samplerBuffer tboSampler;

void main(void) {
   gl_FragColor = texelFetch(tboSampler, 0);
}
