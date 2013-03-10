varying vec4 coord;

void main(void)
{
   gl_Position = ftransform();
   coord = gl_Vertex;
}