varying float c;

void main(void)
{
   vec3 a = normalize(vec3(gl_ModelViewMatrix * gl_Vertex));
   vec3 b = gl_NormalMatrix * gl_Normal;
   c = length(cross(a,b));
   gl_Position = ftransform();
}